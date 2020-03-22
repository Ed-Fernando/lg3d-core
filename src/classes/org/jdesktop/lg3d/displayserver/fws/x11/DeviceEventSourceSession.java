/**
 * Project Looking Glass
 *
 * $RCSfile: DeviceEventSourceSession.java,v $
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
 * $Revision: 1.4 $
 * $Date: 2006-08-14 23:13:28 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.fws.x11;

import java.util.HashMap;
import java.util.logging.Logger;
import javax.media.j3d.Canvas3D;
import gnu.x11.Display;
import gnu.x11.Window;
import gnu.x11.event.*;

/**
 * DeviceEventSourceSession is an EventBroker which provides raw device events
 * to the Picker. These events are acquired from the X Server. This event source
 * is used when LG is running in "session" (full screen) mode.
 */

class DeviceEventSourceSession extends DeviceEventSource
{
    // For debug: set to true to enable hands-off motion events
    private static final boolean syntheticInput = false;

    class CanvasPrwin {
        Canvas3D canvas;
        Window   prwin;
    }

    private HashMap<Long, CanvasPrwin> canvasList = new HashMap<Long, CanvasPrwin>();

    public DeviceEventSourceSession (Display devDpy, Window[] prwins, Canvas3D[] canvases) {
	super(devDpy, prwins, canvases);

	for (int i=0; i < prwins.length; i++) {		
	    CanvasPrwin cvprwin = new CanvasPrwin();
	    cvprwin.canvas = canvases[i];
	    cvprwin.prwin = prwins[i];
	    canvasList.put(((long)prwins[i].id), cvprwin);
	}	

	this.start();
    }

    public void run () {

	for (;;) {
	    Input inputEvent;

	    // Attempt to get the next event. Block until one is available.
	    if (!syntheticInput) {
		Event xevent = devDpy.next_event();

		// Ignore all events other than keyboard and mouse events.
		// There are some events which the X server sends to us which
		// there is no way to stop (e.g. MappingNotify events).
		if (xevent.code() < KeyPress.CODE ||
		    xevent.code() > MotionNotify.CODE) {
		    continue;
		}

		inputEvent = (Input) xevent;
	    }

	    DeviceEvent devEvent;
	    if (syntheticInput) {
		devEvent = SyntheticDeviceEvent.nextDeviceEvent(devDpy);
	    } else {
		devEvent = new DeviceEvent(devDpy, inputEvent);
	    }

	    devEvent.window_offset = 12;

	    // Note: the root id of raw device events on LG screens is the prw
	    long prw = devEvent.root_id();
	    CanvasPrwin cvprwin = canvasList.get(prw);
	    if (cvprwin == null) {
		System.err.println("INTERNAL ERROR: Cannot find canvas for prw = " + prw);
		System.err.println("This event will be ignored");
	    } else {
                devEvent.setCanvas(cvprwin.canvas);
                devEvent.setPrwin(cvprwin.prwin);
            }
	    trackLastPosition(devEvent);
	
	    //System.err.println("Received devEvent = " + devEvent);
	    enqueue(devEvent);
	}
    }
}


