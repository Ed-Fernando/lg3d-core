/**
 * Project Looking Glass
 *
 * $RCSfile: SyntheticDeviceEvent.java,v $
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
 * $Revision: 1.2 $
 * $Date: 2005-06-24 19:48:08 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.displayserver.fws.x11;

import gnu.x11.Display;
import gnu.x11.event.MotionNotify;

class SyntheticDeviceEvent extends DeviceEvent {

    private static final int LEFT  = 0;
    private static final int RIGHT = 1;

    private static final int SCREEN_WIDTH  = 1280;
    private static final int SCREEN_HEIGHT = 1024;

    private static final int INITIAL_X     = SCREEN_WIDTH / 2;
    private static final int INITIAL_Y     = SCREEN_HEIGHT / 2;

    private static int xNext = INITIAL_X;
    private static int yNext = INITIAL_Y;
    
    private static int direction = LEFT;

    private int rootX, rootY;

    public static DeviceEvent nextDeviceEvent (Display dpy) {
	SyntheticDeviceEvent event = new SyntheticDeviceEvent(dpy, xNext, yNext);
	
	// Move pointer left and right across the screen
	if (direction == LEFT) {
	    xNext -= 1;
	    if (xNext < 0) {
		xNext = 1;
		direction = RIGHT;
	    }
	} else {
	    xNext += 1;
	    if (xNext >= SCREEN_WIDTH) {
		xNext = SCREEN_WIDTH - 2;
		direction = LEFT;
	    }
	}

	return event;
    }

    public SyntheticDeviceEvent (Display dpy, int rootX, int rootY) {
	super(dpy, new byte[32]);
	this.rootX = rootX;
	this.rootY = rootY;
    }

    public int code () { return MotionNotify.CODE; }

    public int root_x () { return rootX; }
    public int root_y () { return rootY; }
}

