/**
 * Project Looking Glass
 *
 * $RCSfile: LightPipe.java,v $
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
 * $Revision: 1.5 $
 * $Date: 2006-04-11 18:10:43 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.fws.x11;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import gnu.x11.Display;
import gnu.x11.Error;
import gnu.x11.event.Event;
import gnu.x11.event.ConfigureNotify;

public class LightPipe {

    private ReentrantLock lock = new ReentrantLock();

    private static final Logger logger = Logger.getLogger("lg.x11");
    private static HashMap windowTable = new HashMap();

    private Display damDpy;
    private DamageEventBroker ebDamage;
    private ExtensionSet exts;

    public LightPipe (Display damDpy, DamageEventBroker ebDamage, 
		      ExtensionSet exts) {
	//System.err.println("Enter LightPipe(), ds = " + ds);

	this.damDpy = damDpy;
	this.ebDamage = ebDamage;
	this.exts = exts;

	damDpy.ignore_error(Error.BAD_PIXMAP);
	damDpy.ignore_error(Error.BAD_GC);
    }

    public LightPipeWindow lookupWindow (long wid) {
        //System.err.println("Enter lp.lookupWindow");
	LightPipeWindow lpw;
	lock.lock();
	try {
	    lpw = (LightPipeWindow) windowTable.get(wid);
	} finally {
	    lock.unlock();
	}
        //System.err.println("Exit lp.lookupWindow");
	return lpw;
    }

    private void addWindow (long wid, LightPipeWindow lpw) {
        //System.err.println("Enter lp.addWindow");
	lock.lock();
	try {
	    windowTable.put(wid, lpw);
	} finally {
	    lock.unlock();
	}
        //System.err.println("Exit lp.addWindow");
    }

    private void removeWindow (long wid) {
        //System.err.println("Enter lp.removeWindow");
	lock.lock();
	try {
	    windowTable.remove(wid);
	} finally {
	    lock.unlock();
	}
        //System.err.println("Exit lp.removeWindow");
    }

    // Start tracking damage for the given window.
    public void attendWindow (long wid) {
	//System.err.println("Enter attendWindow, wid = " + wid);
	LightPipeWindow lpw = lookupWindow(wid);
	if (lpw != null) {
	    logger.log(Level.SEVERE, "Already attending window " + wid);
	    throw new RuntimeException();
	}
	lpw = new LightPipeWindow(damDpy, ebDamage, exts, wid);
	addWindow(wid, lpw);
	//System.err.println("LightPipe: Attending window " + wid);
    }

    // Stop tracking damage for the given window
    public void ignoreWindow (long wid) {
	LightPipeWindow lpw = lookupWindow(wid);
	if (lpw == null) {
	    logger.log(Level.SEVERE, "Not attending window " + wid);
	    throw new RuntimeException();
	}
	removeWindow(wid);
        lpw.ignore();
	//System.err.println("LightPipe: Ignoring window " + wid);
    }

    // The client longer cares about this window.
    // Clean up its client-side resources.
    public void destroyWindow (long wid) {
	LightPipeWindow lpw = lookupWindow(wid);
	if (lpw == null) {
	    return;
	}
	removeWindow(wid);
	lpw.destroy();
	//System.err.println("LightPipe: Destroyed window " + wid);
    }

    // Blocks until there is damage on any window
    // When this routine returns, the lock on this
    // object is held. Calling release window releases
    // the lock.
    public LightPipeWindow checkAny () {
	LightPipeWindow lpw;

	while (true) {

	    // Process any events which may be available. 
	    // Do not block if there are none.
	    processEvents();

	    lock.lock();

	    // See if there are any damaged windows.
	    // Return the first one we find
	    lpw = LightPipeWindow.firstDamagedWindow();
	    //System.err.println("first damaged window = " + lpw);
	    if (lpw != null) {
		lpw.update();
		// Return with lock held!
		return lpw;
	    }

	    lock.unlock();

	    // Wait for event but don't remove from queue
	    //System.err.println("CEP: waiting for DEB event");
	    ebDamage.waitForEvent();
	} 
    }

    // Call this when you're done processing the window returned
    // by checkAny.
    //
    // Note: the user is required to call this within 
    // a synchronized block which synchronizes on this object
    public void releaseWindow (LightPipeWindow lpw) {
        lpw.undamage();
	lock.unlock();
    }

    // While there are any events available, process them.
    // But return if there aren't any events.
    private void processEvents () {

	while (ebDamage.size() > 0) {

	    // Get the next event
	    Event event = ebDamage.dequeue();
	    //System.err.println("LightPipe.processEvents: received event = " + event);

	    // Process the event
	    lock.lock();
	    try {
		processEvent(event);
	    } finally {
		lock.unlock();
	    }
	}
    }

    private void processEvent (Event event) {
	if (event instanceof DamageEvent) {
	    processDamageNotify((DamageEvent)event);
	} else if (event instanceof ConfigureNotify) {
	    processConfigureNotify((ConfigureNotify)event);
	} else {
	    logger.log(Level.SEVERE, "LightPipe process_event: Unrecognized event, code = %d\n" + 
		       event.code());
	}
    }

    private void processConfigureNotify (ConfigureNotify event) {
	long wid = event.window_id();
	LightPipeWindow lpw = (LightPipeWindow) windowTable.get(wid);

	if (lpw == null) {
	    // Note: This is not an error. This can happen if the ConfigureNotify 
	    // event arrives before the window manager starts damage tracking for 
	    // this window. So just ignore it.
	    return;
	}

	int newWidth = event.width();
	int newHeight = event.height();
        // TODO: event.border_width is not yet implemented in Escher! 
	//int newBw = event.border_width(); 
	int newBw = event.read2(24); 

	//System.err.println("LightPipe: ConfigureNotify event received");
	//System.err.println("newWidth = " + newWidth);
	//System.err.println("newHeight = " + newHeight);
	//System.err.println("newBw = " + newBw);
	
	boolean damageAll = false;

        if (newWidth  != lpw.getWidth() ||
	    newHeight != lpw.getHeight()) {
	    lpw.resize(newWidth, newHeight);
	    damageAll = true;
	}

	if (newBw != lpw.getBorderWidth()) {
	    lpw.setBorderWidth(newBw);
	    // Damage everything if the border width changes.
	    // This is a very rare occurrence.
	    damageAll = true;
        }

	if (damageAll) {
	    lpw.damageAll();
// Probably not necessary because a damage event always follows
lpw.update();
	}
    }

    private void processDamageNotify (DamageEvent event) {
	long wid = event.drawable_id();
	LightPipeWindow lpw = (LightPipeWindow) windowTable.get(wid);

	//System.err.println("Received DamageNotifyEvent for wid = " + wid);
	//System.err.println("x = " + event.area_x());
	//System.err.println("y = " + event.area_y());
	//System.err.println("w = " + event.area_width());
	//System.err.println("h = " + event.area_height());

	if (lpw == null) {
	    // Note: This is not an error. This can happen if the DamageNotify 
	    // event arrives before the window manager starts damage tracking for 
	    // this window. So just ignore it.
	    //System.err.println("Exit processDamageNotify 1");
	    return;
	}

	lpw.damage(event.area_x(), event.area_y(),
		   event.area_width(), event.area_height());
	//System.err.println("Exit processDamageNotify 2");
    }
}

