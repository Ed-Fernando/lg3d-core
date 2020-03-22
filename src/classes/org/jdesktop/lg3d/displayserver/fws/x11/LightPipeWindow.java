/**
 * Project Looking Glass
 *
 * $RCSfile: LightPipeWindow.java,v $
 *
 * Copyright (c) 2004, Sun Microsystems, Inc., All Rights Reserved
 *
 * Redistributions in source code form must reproduce the above
 * copyright and this condition.
 *
 * The contents of this file are subject to the GNU General Public
 * License, Version 2 (the "License"); you may not use this file
 * except in compliance with the License. A copy of the License is
 * available at http://www.opensource.org/licenses/gpl-license.php.
 *
 * $Revision: 1.8 $
 * $Date: 2006-09-26 19:38:11 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.fws.x11;

import java.util.logging.Logger;
import java.util.List;
import java.util.LinkedList;
import gnu.x11.Display;
import gnu.x11.Window;
import gnu.x11.Pixmap;
import gnu.x11.GC;
import gnu.x11.event.Event;
import org.jdesktop.lg3d.displayserver.fws.WindowContents;

public class LightPipeWindow extends Window {

    // List of damaged windows    
    static private LinkedList<LightPipeWindow> damagedWindows = 
        new LinkedList<LightPipeWindow>();

    private DamageEventBroker ebDamage;
    private ExtensionSet exts;

    private long wid;
    private long damageId;

    private int width, height;
    private int depth;
    private int bw;

    // These are in image coordinates (includes the border)
    private int damageX;
    private int damageY;
    private int damageWidth;
    private int damageHeight;

    private Window.AttributesReply attributes;
    private Window.GeometryReply   geometry;

    // The repair region
    private long regionId;

    // The window contents image
    private WindowContents winContents = null;

    private ShmSegInfo shmInfo;
    private Pixmap shmPixmap;
    private GC     shmGC;

    private native int shmget (int imageSize);
    private native long shmat (int shmid);
    private native long shmdt (long shmAddr);
    private native long shmctl (int shmid);

    static {
        String soFile = System.getProperty("lg.libraries.LightPipeWindow", "");
        if (!"".equals(soFile)) {
            System.load(soFile);
        } else {
            System.loadLibrary("LightPipeWindow");
        }
    }

    public LightPipeWindow (Display damDpy, DamageEventBroker ebDamage, 
			    ExtensionSet exts, long wid) {

	super(damDpy, (int)wid);
	
	//System.err.println("Enter LightPipeWindow()");

	this.ebDamage = ebDamage;
	this.exts = exts;
	this.wid = wid;

	// Allocate a damage id for this window
	damageId = exts.damageExtForDam.create(wid, X11DamageExt.REPORT_BOUNDING_BOX);
	//System.err.println("damageId = " + damageId);

	// Start listening to ConfigureNotify events
	select_input(Event.STRUCTURE_NOTIFY_MASK);

	// Cache the current geometry
	attributes = attributes();
	geometry = geometry();
	width = geometry.width();
	height = geometry.height();
	depth = geometry.depth();
	bw = geometry.border_width();

	//System.err.println("Current window geometry");
	//System.err.println("width = " + width);
	//System.err.println("height = " + height);
	//System.err.println("depth = " + depth);
	//System.err.println("bw = " + bw);

	// Create a client-accesible region
	regionId = exts.xfixesExtForDam.createRegionEmpty();
	//System.err.println("regionId = " + regionId);

	// Post initial damage, if window is viewable
	// Be sure to damage the border as well
        if (attributes.map_state() == Window.AttributesReply.VIEWABLE) {
	    damageInitial();
	}
    }

    // Stop tracking damage for this window
    public void ignore () {
        undamage();
	winContentsDestroy(true);

	try {
	    exts.damageExtForDam.destroy(damageId);
	    exts.damageExtForDam.display.check_error(); // XSync
	} catch (Throwable t) {
	    // If this fails it is because the window may have already been destroyed
	    // and so the damage id has been destroyed also. So just ignore it.
	}

	exts.xfixesExtForDam.destroyRegion(regionId);
    }

    // Deallocate client-side resources for this window
    public void destroy () {
	ignore();
        unintern();
    }

    // Update the window contents with server contents
    // by telling the X server to copy from the window
    // into the shared pixmap associated with the window
    // contents.

    public void update () {
	if (damageWidth <= 0 || damageHeight <= 0) return;

	// Convert the area from the damaged region to window coordinates
	// and remove from the damaged region. 

	exts.xfixesExtForDam.setRegionToRect(regionId, damageX - bw, damageY - bw,
			    damageWidth, damageHeight);

	try {

	    if (shmPixmap == null || shmGC == null) return;

	    // Now copy the pixels. The source coordinates need to be in 
	    // window coordinates. The destination coordinates need to be 
	    // in image coordinates.
	    shmPixmap.copy_area(this, shmGC, damageX - bw, damageY - bw, 
				damageWidth, damageHeight,
				damageX, damageY);

	    // Inform the X server that we've processed the damage
	    //
	    // (Note: I tried implementing the suggested fix in 
	    // bug 246 (putting the subtract before the copy but
	    // it actually made things worse!)

	    exts.damageExtForDam.subtract(damageId, regionId, 0L);

	    shmPixmap.display.check_error(); // XSync

	} catch (Throwable t) {
	    // Ignore Damage extension errors; the window might be already gone
	}

	//System.err.println("Copied from redirected window to pixmap");
	//System.err.println("damageX = " + damageX);
	//System.err.println("damageY = " + damageY);
	//System.err.println("damageWidth = " + damageWidth);
	//System.err.println("damageHeight = " + damageHeight);
    }

    // Resize window in response to a ConfigureNotify event
    public void resize (int newWidth, int newHeight) {
	//System.err.println("Enter LightPipeWindow.resize");
	//System.err.println("old width = " + width);
	//System.err.println("old height = " + height);

	width = newWidth;
	height = newHeight;

	//System.err.println("new width = " + width);
	//System.err.println("new height = " + height);
    }

    public long getWid () { return wid; }

    public int getWidth () { return width; }
    public int getHeight () { return height; }
    public int getWidthWithBorder () { return width + bw * 2; }
    public int getHeightWithBorder () { return height + bw * 2; }
    public int getDepth () { return depth; }           
    public int getBorderWidth () { return bw; }
    public void setBorderWidth (int bw) { this.bw = bw; }

    public int getDamageX () { return damageX; }
    public int getDamageY () { return damageY; }
    public int getDamageWidth () { return damageWidth; }
    public int getDamageHeight () { return damageHeight; }

    // Return the first window on the damaged list
    public static LightPipeWindow firstDamagedWindow () {
	if (damagedWindows.size() == 0) return null;
	return damagedWindows.getFirst();
    }

    // Damage the entire window, including the border.
    // Then create a damage event and inject it into the 
    // event queue. This must be done to kick us out of the
    // event wait in lp.checkAny in case the attendWindow call
    // was outraced by the initial damage event from the X server.

    public void damageInitial () {

	damageAll();

	// Note: the 
	DamageEvent event = new DamageEvent(display, exts.damageExtForDam.first_event);
	event.set_drawable_id((int)wid);
	event.set_area_x(damageX - bw);
	event.set_area_y(damageY - bw);
	event.set_area_width(damageWidth);
	event.set_area_height(damageHeight);
	ebDamage.enqueue(event);
    }


    // Damage the entire window, including the border    
    public void damageAll () {
	//System.err.println("Enter LPW.damageAll");

	winContentsDestroy(true);
	winContentsCreate();

	damagedWindowsUpdate();

	// Include border
	damageX = 0;
	damageY = 0;
	damageWidth = width + bw * 2;
	damageHeight = height + bw * 2;
    }

    // Add a damaged rectangle to window. Coordinates are in 
    // window coordinates, NOT image coordinates.

    public void damage (int x, int y, int width, int height) {
	//System.err.println("Enter LightPipeWindow.damage");
	//System.err.println("x = " + x);
	//System.err.println("y = " + y);
	//System.err.println("width = " + width);
	//System.err.println("height = " + height);

	if (winContents == null) {
	    winContentsCreate();
	}

	damagedWindowsUpdate();

	// Translate from window coords into image coords
	x += bw;
	y += bw;

	if (damageWidth == 0) {
	    damageX = x;
	    damageY = y;
	    damageWidth = width;
	    damageHeight = height;
	} else {
	    int oldX2 = damageX + damageWidth;
	    int oldY2 = damageY + damageHeight;
	    int newX2 = x + width;
	    int newY2 = y + height;
	    
	    if (x < damageX) {
		damageX = x;
	    }
	    if (newX2 > oldX2) {
		oldX2 = newX2;
	    }
	    damageWidth = oldX2 - damageX;
	    if (y < damageY) {
		damageY = y;
	    }
	    if (newY2 > oldY2) {
		oldY2 = newY2;
	    }
	    damageHeight = oldY2 - damageY;
	}

	//System.err.println("LPW.damage: Damaged region");
	//System.err.println("damageX = " + damageX);
	//System.err.println("damageY = " + damageY);
	//System.err.println("damageWidth = " + damageWidth);
	//System.err.println("damageHeight = " + damageHeight);
    }

    // Add this window to the list of damaged windows if it is
    // not already in the list
    private void damagedWindowsUpdate () {
	if (damagedWindows.indexOf(this) == -1) {
	    damagedWindows.add(this);
	}
    }

    // Clear damage from this window
    public void undamage () {
	damageX = 0;
	damageY = 0;
	damageWidth = 0;
	damageHeight = 0;
	damagedWindows.remove(this);
    }

    private final int roundup (int nbytes, int pad) {
	return (((nbytes + (pad-1)) / pad) * (pad>>3));
    }


    private void resetShmInfo () {
	shmInfo = null;
	shmPixmap = null;
	shmGC = null;

	if (winContents != null) {
	    winContents.width = 0;
	    winContents.height = 0;
	    winContents.bytesPerLine = 0;
	    winContents.nativeMemorySize = 0;
	    winContents.nativeMemoryPointer = 0;
	}
    }

    // Create a WindowContents object which is the size of 
    // the window (including its border width) and which refers
    // to the same memory as the shared pixmap.

    private void winContentsCreate () {
	WindowContents wc;

        // Desired dimensions (including border)
	int bwidth = width + 2 * bw;
        int bheight = height + 2 * bw;

	wc = new WindowContents();
        wc.width = bwidth;
	wc.height = bheight;

        int bitmapPad = (depth == 16) ? 16 : 32;
	int bitsPerPixel = bitmapPad;

	wc.bytesPerLine = roundup((bitsPerPixel * bwidth), bitmapPad);

	wc.nativeMemorySize = wc.bytesPerLine * bheight;
	if (wc.nativeMemorySize <= 0) {
	    resetShmInfo();
	    return;
	}

        shmInfo = new ShmSegInfo();
	shmInfo.shmid = shmget(wc.nativeMemorySize);
	if (shmInfo.shmid < 0) {
	    resetShmInfo();
	    return;
	}

	shmInfo.shmAddr = shmat(shmInfo.shmid);
	if (shmInfo.shmAddr == -1) {
	    shmctl(shmInfo.shmid);
	    resetShmInfo();
	}

	shmInfo.readOnly = false;
	exts.shmExtForDam.attach(shmInfo);
	wc.nativeMemoryPointer = shmInfo.shmAddr;

	winContents = wc;

	shmPixmap = exts.shmExtForDam.createPixmap((Window)this, shmInfo.shmAddr, shmInfo,
				bwidth, bheight, depth);

	GC.Values gcv = new GC.Values();
	gcv.set_graphics_exposures(false);
	gcv.set_subwindow_mode(GC.Values.INCLUDE_INTERIORS);
	shmGC = new GC(this, gcv);
    }
    
    // Deallocate the window's contents image
    private void winContentsDestroy (boolean sync) {

	if (winContents == null) return;

	exts.shmExtForDam.detach(shmInfo);
	if (sync) {
	    try {
		exts.shmExtForDam.display.check_error(); // XSync
	    } catch (Throwable t) {
		// Ignore Damage extension errors
	    }
	}

	if (shmInfo != null) {
	    shmdt(shmInfo.shmAddr);
	    shmctl(shmInfo.shmid);
	}

	winContents = null;

	if (shmPixmap != null) {
	    shmPixmap.free();
	}

	if (shmGC != null) {
	    shmGC.free();
	}
    }

    // Return the pointer to the underlying shared memory 
    public WindowContents getContents () {
	return winContents;
    }
}



