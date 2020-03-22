/**
 * Project Looking Glass
 *
 * $RCSfile: LgXKeyEvent.java,v $
 *
 * Copyright (c) 2006, Sun Microsystems, Inc., All Rights Reserved
 *
 * Redistributions in source code form must reproduce the above
 * copyright and this condition.
 *
 * The contents of this file are subject to the GNU General Public
 * License, Version 2 (the "License"); you may not use this file
 * except in compliance with the License. A copy of the License is
 * available at http://www.opensource.org/licenses/gpl-license.php.
 *
 * $Revision: 1.3 $
 * $Date: 2007-01-23 20:48:59 $
 * $State: Exp $
 *
 */
package sun.awt.X11;

public class LgXKeyEvent extends XKeyEvent {

    private int type;
    private int keycode;
    private int state;
    private long time;

    public LgXKeyEvent (int type, int keycode, int state, long time) {
	this.type = type;
	this.keycode = keycode;
	this.state = state;
	this.time = time;
    }

    public int get_type() { 
	return type;
    }

    public long get_display() { 
	// TODO: is the AWT display (the user display) the the right display?
	// Or should it be the LG display?
	// TODO: For session mode this has to be the LG display.
	return XToolkit.getDisplay();
    }

    public long get_time() { 
	return type;
    }

    public int get_keycode() { 
	return keycode;
    }

    public int get_state() { 
	return state;
    }
}

