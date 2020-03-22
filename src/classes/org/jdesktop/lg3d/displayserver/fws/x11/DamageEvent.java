/**
 * Project Looking Glass
 *
 * $RCSfile: DamageEvent.java,v $
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
 * $Date: 2005-01-20 22:05:22 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.fws.x11;

import gnu.x11.Display;
import gnu.x11.event.Event;
import javax.media.j3d.*;

/*
** X Damage DamageNotify extension event
*/

public class DamageEvent extends Event {

    public static final int CODE_OFFSET = 0;

    public DamageEvent (Display dpy, int code) {
	super(dpy, code, 4); 
    }

    public DamageEvent (Display dpy, byte[] data) {
	super(dpy, data, 4); 
    }

    public int level () { 
	return read1(1); 
    }

    public void set_level (int level) { 
	write1(1, level); 
    }

    public long drawable_id () { 
	return read4(4); 
    }

    public void set_drawable_id (int drawable) { 
	write4(4, drawable); 
    }

    public int time () { 
	return read4(12); 
    }

    public void set_time (int level) { 
	write4(12, level); 
    }

    public int area_x () { 
	int x = read2(16); 
	// Be sure to sign extend
	return (x<<16)>>16;
    }

    public void set_area_x (int x) { 
	write2(16, x); 
    }

    public int area_y () { 
	int y = read2(18); 
	// Be sure to sign extend
	return (y<<16)>>16;
    }

    public void set_area_y (int y) { 
	write2(18, y); 
    }

    public int area_width () { 
	return read2(20); 
    }

    public void set_area_width (int width) { 
	write2(20, width); 
    }

    public int area_height () { 
	return read2(22); 
    }

    public void set_area_height (int height) { 
	write2(22, height); 
    }

    public String toString () {
	String str;

	str = "XDamageNotify: code=" + code();
	
        str += " draw=" + drawable_id();
        str += " area=[";
	str += area_x();
	str += ",";
	str += area_y();
	str += ",";
	str += area_width();
	str += ",";
	str += area_height();
	str += "]\n";

        return str;
    }

}
