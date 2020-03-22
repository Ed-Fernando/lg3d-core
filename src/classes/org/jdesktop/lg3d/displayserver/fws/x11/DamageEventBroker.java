/**
 * Project Looking Glass
 *
 * $RCSfile: DamageEventBroker.java,v $
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
 * $Revision: 1.18 $
 * $Date: 2006-09-26 23:13:40 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.fws.x11;

import java.awt.image.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.media.j3d.*;
import java.util.logging.Logger;
import org.jdesktop.lg3d.displayserver.fws.*;
import org.jdesktop.lg3d.displayserver.fws.WindowContents;
import org.jdesktop.lg3d.displayserver.nativewindow.TiledNativeWindowImage;

import gnu.x11.Display;
import gnu.x11.extension.NotFoundException;

/**
 * DamageEventBroker is an event broker which receives damage events 
 * from the CookedEventPoller and which invokes the appropriate image 
 * updates with the registered ImageUpdaters.
 */

class DamageEventBroker extends EventBroker
{
    private static final Logger logger = Logger.getLogger("lg.x11");

    // Copies the entire image from a 32-bit source window contents object 
    // into a 32-bit destination Java BufferedImage data (intAry). dstW is the 
    // width of of the destination (in pixels).

    native static void copyImageEntire32 (int[] intAry, int dstWidth, int srcImageWidth,
					  int srcImageHeight, long srcImageData);


    // Copies a specified sub-rectangle of 32-bit source window contents object
    // into a 32-bit destination Java BufferedImage data (intAry). dstW is the 
    // width of of the destination (in pixels).

    native static void copyImageRect32 (int[] intAry, int dstX, int dstY, int dstW, int dstH,
					int srcX, int srcY, int dstImageWidth, int srcImageWidth,
					int srcImageHeight, long srcImageData);
    
    // Copies the entire image from a 16-bit source window contents object into a 
    // 16-bit destination Java BufferedImage data (shrtAry). dstW is the 
    // width of of the destination (in pixels).

    native static void copyImageEntire16 (short[] shortAry, int dstWidth, int srcImageWidth,
					  int srcImageHeight, long srcImageData);

    // Copies a specified sub-rectangle of 16-bit source window contents object
    // into a 16-bit destination Java BufferedImage data (shrtAry). dstW is the 
    // width of of the destination (in pixels).

    native static void copyImageRect16 (short[] shortAry, int dstX, int dstY, int dstW, int dstH,
					int srcX, int srcY, int dstImageWidth, int srcImageWidth,
					int srcImageHeight, long srcImageData);

    private LightPipe lp;
    private Display dpy;
    private int damageNotifyEventCode;

    // A table of WindowResizeListeners
    private HashMap<Long, WindowResizeListener> wrlTable = new HashMap();

    private HashMap<Long, TiledNativeWindowImage> windowToImage = new HashMap();

    // For performance measurement
    //private StatBufTimer timer;

    static {
        String soFile = System.getProperty("lg.libraries.DamageEventBroker", "");
        if (!"".equals(soFile)) {
            System.load(soFile);
        } else {
            System.loadLibrary("DamageEventBroker");
        }
    }

    public DamageEventBroker (Display damDpy, ExtensionSet exts) {
        this.lp = new LightPipe(damDpy, this, exts);
	dpy = damDpy;

	// For performance measurement
	//timer = new StatBufTimer(100);
    }

    private synchronized TiledNativeWindowImage getWindowImage (long wid) {
	TiledNativeWindowImage image = (TiledNativeWindowImage) windowToImage.get(wid);
	if (image == null) {
	    // Sometimes the entry can be null. This (I suspect) occurs when 
	    // a damage event arrives after the window has been mapped and tracking
	    // has been removed.
	    return null;
	}
	return image;
    }

    private synchronized void setWindowImage (long wid, TiledNativeWindowImage image) {
	windowToImage.put(wid, image);
    }

    private synchronized void removeWindowImage (long wid) {
	windowToImage.remove(wid);
    }

    /**
     * Associates a window with an image. Damage to the window will be tracked
     * and damaged areas will be updated in the image. If image is null, the
     * association is removed and damage tracking for this window will cease.
     *
     * You should only call this method for InputOutput windows. An exception
     * will be thrown if you call it for an InputOnly window.
     *
     * @param    wid   track damage for this window 
     * @param    image update damaged window areas within this image
     */
    public void trackDamageForWindow (long wid, TiledNativeWindowImage image) 
    {
	TiledNativeWindowImage boundImage = getWindowImage(wid);

	if (image == null) {
	    if (boundImage == null) {
		// Note: this used to throw an exception. But we discovered that
		// the X server can send multiple UnmapNotify events for the same window.
		// So if this routine is called when damage tracking is already disabled,
		// that's okay
		return;
	    }
	    removeWindowImage(wid);
	    lp.ignoreWindow(wid);
	    return;
	}

	if (boundImage != null) {
	    // We're already tracking damage for this window
	    logger.warning("Redundant track: wid: " + wid);
            return;		
	}

	//System.err.println("Track damage for wid " + wid);

	// Associate image with this window
	setWindowImage(wid, image);

	// Start listening for damage events on window 
	lp.attendWindow(wid);

	//prn("DEB.trackDamageForWindow returned\n");
    }

    public void destroyWindow (long wid) {
	removeWindowImage(wid);
	lp.destroyWindow(wid);
    }
    
    public void addWindowResizeListener (long wid, WindowResizeListener l) {
        if (l == null) return;
	Long widObj = new Long(wid);
	wrlTable.put(widObj, l);
    }
	
    public void removeWindowResizeListener(long wid, WindowResizeListener l) {
        if (l == null) return;
	Long widObj = new Long(wid);
	wrlTable.remove(widObj);
    }

    public WindowResizeListener getWindowResizeListener (long wid) {
	Long widObj = new Long(wid);
	return (WindowResizeListener) wrlTable.get(widObj);
    }

    public void updateDamagedRegion (LightPipeWindow lpw, TiledNativeWindowImage image, 
				     ImageLoader loader, WindowContents winContents) {

	if (winContents == null) return;
	if (winContents.width <= 0 ||
	    winContents.height <= 0) {
	    return;
	}

	// Damaged rect is in borderSize coordinates. Translate into window
	// border relative coordinates.
	int x = lpw.getDamageX(); 
	int y = lpw.getDamageY();
	int w = lpw.getDamageWidth(); 
	int h = lpw.getDamageHeight();

	//System.err.println("fillTiles subregion: damaged region");
	//System.err.println("x = " + x);
	//System.err.println("y = " + y);
	//System.err.println("w = " + w);
	//System.err.println("h = " + h);

    	    // Clamp damaged region to image dimensions
	int winBorderWidth = lpw.getWidthWithBorder();
	int winBorderHeight = lpw.getHeightWithBorder();
	int xmax = x + w - 1;
	int ymax = y + h - 1;
	if (x < 0) {
	    x = 0;
	}
	if (y < 0) {
	    y = 0;
	}
	if (xmax >= winBorderWidth) {
	    xmax = winBorderWidth - 1;
	}
	if (ymax >= winBorderHeight) {
	    ymax = winBorderHeight - 1;
	}
	w = xmax - x + 1;
	h = ymax - y + 1;

	//System.err.println("fillTiles subregion: damaged region - after clamp");
	//System.err.println("x = " + x);
	//System.err.println("y = " + y);
	//System.err.println("w = " + w);
	//System.err.println("h = " + h);

	// For performance measurement
	//timer.startInterval();

	image.fillTiles(loader, x, y, w, h,
			winContents);

	// For performance measurement
	//timer.addInterval();
    }

    private int getPowerOfTwoUpperBound (int value) {

	if (value < 1)
	    return value;
	
	int powerValue = 1;

	for (;;) {
	    powerValue *= 2;
	    if (value < powerValue) {
		return powerValue;
	    }
	}
    }

    public void setDamageNotifyEventCode (int damageNotifyEventCode) {
	this.damageNotifyEventCode = damageNotifyEventCode;
    }

    // Note: this routine accomplishes its task by injecting a "pseudo-damage 
    // event" into the damage event pipline. Therefore the actual damage
    // processing will occur sometime in the future after this routine returns.

    public void damageWindowRectangle (long wid, int x, int y, int w, int h) {
	DamageEvent de = new DamageEvent(dpy, damageNotifyEventCode);
	de.set_drawable_id((int) wid);
	de.set_area_x(x);
	de.set_area_y(y);
	de.set_area_width(w);
	de.set_area_height(h);
	enqueue(de);
    }

    public void run () {
        ImageUpdater imageUpdater = new ImageUpdater();
        ImageLoader  imageLoader = new ImageLoader(imageUpdater);
	LightPipeWindow lpw = null;
	long wid;

	for (;;) {
	   
	    try {

		// Block until a window has been damaged
		lpw = lp.checkAny();
		// The lock is held at this point!

		wid = lpw.getWid();
		TiledNativeWindowImage image = getWindowImage(wid);
		if (image == null) {
		    continue;
		}

		int oldWidth  = image.getWinWidth();
		int oldHeight = image.getWinHeight();
		int newWidth  = lpw.getWidthWithBorder();
		int newHeight = lpw.getHeightWithBorder();;

		if (oldWidth != newWidth || oldHeight != newHeight) {
		    // User is expected to allocate a new image Object of the 
		    // new dimensions 
		    //prn("Calling WindowResize listener!");
		    //prn("newWidth = " + newWidth);
		    //prn("newHeight = " + newHeight);
		    try {
			TiledNativeWindowImage newImage = invokeWindowResizeListener(wid, 
								 newWidth, newHeight);
			if (newImage != null && newImage != image) {
			    setWindowImage(wid, newImage);
			}
		    } catch (IllegalArgumentException iae) {
			//System.err.println("Illegal argument to window resize listener. Cannot resize window.");
			continue;
		    } catch (Exception e) {
			logger.warning("DamageEventBroker: window resize listener threw exception: " + e);
			e.printStackTrace();
		    } catch (Error err) {
			logger.warning("DamageEventBroker: window resize listener threw error: " + err);
			err.printStackTrace();
		    }
		}
	    
		WindowContents winContents = lpw.getContents();

		// Arrange for damaged rectangle from lpw image to be downloaded into 
		// the texture image 
		updateDamagedRegion(lpw, image, imageLoader, winContents);

	    } catch (Exception e) {
		e.printStackTrace();
		logger.severe("DamageEventBroker thread encountered an exception, e = " + e);
	    } finally {
		// Make sure we always release the lock
		if (lpw != null) lp.releaseWindow(lpw);
	    }
	}
    }

    // Call the window resize listener which has been registered for this window
    private TiledNativeWindowImage invokeWindowResizeListener (long wid, 
					       int newWidth, int newHeight) 
	throws IllegalArgumentException
    {
        WindowResizeListener wrl = getWindowResizeListener(wid);

	if (wrl == null) return null;

	return wrl.sizeChanged(wid, newWidth, newHeight);
    }

    private void prn (String str) { System.err.println(str); }
}

