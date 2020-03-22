/**
 * Project Looking Glass
 *
 * $RCSfile: DeviceEvent.java,v $
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
 * $Date: 2006-03-07 00:16:57 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.fws.x11;

import java.util.HashMap;
import javax.media.j3d.Canvas3D;
import gnu.x11.Display;
import gnu.x11.Data;
import gnu.x11.Window;
import gnu.x11.event.MotionNotify;
import gnu.x11.event.ButtonPress;
import gnu.x11.event.ButtonRelease;
import gnu.x11.event.KeyPress;
import gnu.x11.event.KeyRelease;
import gnu.x11.event.Input;

public class DeviceEvent extends Input {

    private Canvas3D canvas;
    private Window prwin;

    DeviceEvent (Display dpy, Input event) {
	super(dpy, event.data);
    }

    DeviceEvent (Display dpy, byte[] data) {
	super(dpy, data);
    }

    public boolean isMotion () { return code() == MotionNotify.CODE; }    

    public boolean isButton () { 
	return code() == ButtonPress.CODE || 
	       code() == ButtonRelease.CODE; 
    }    

    Canvas3D getCanvas () { return canvas; }
    Window getPrwin () { return prwin; }

    void setCanvas (Canvas3D canvas) { this.canvas = canvas; }
    void setPrwin (Window prwin) { this.prwin = prwin; }

    public String toString () {
	return toString(false);
    }

    public String toString (boolean forSend) {
	String str;

	switch (code()) {
	case KeyPress.CODE: 
	    str = "KeyPress:     "; 
	    break;
	case KeyRelease.CODE: 
	    str = "KeyRelease:   "; 
	    break;
	case ButtonPress.CODE: 
	    str = "ButtonPress:  "; 
	    break;
	case ButtonRelease.CODE: 
	    str = "ButtonRelease:"; 
	    break;
	case MotionNotify.CODE: 
	    str = "Motion:       "; 
	    break;
	default:
	    return "*** UNKNOWN EVENT TYPE ***, type = " + code();
        }

	if (forSend) {
	    str += " eventXY = " + event_x() + "," + event_y();
	    str += ", rootXY = " + root_x() + "," + root_y();
	    str += ", win = " + window_id();
	    str += ", pickSeq = " + seq_no();
	} else {
	    str += " rootXY = " + root_x() + "," + root_y();
	    str += ", pickSeq = " + seq_no();
	}
        
        return str;
    }

    public void set_seq_no (int n) { write2(2, n); }

    public int root_x () {
        int x = super.root_x();
	// Be sure to sign extend
	return (x<<16)>>16;
    }

    public int root_y () {
        int y = super.root_y();
	// Be sure to sign extend
	return (y<<16)>>16;
    }
}
