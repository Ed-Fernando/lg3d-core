/**
 * Project Looking Glass
 *
 * $RCSfile: CursorImageEvent.java,v $
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
 * $Date: 2005-01-20 22:05:21 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.fws.x11;

import gnu.x11.Display;
import gnu.x11.event.Event;
import javax.media.j3d.*;

/*
** X Xfixes CursorImageNotify extension event
*/

public class CursorImageEvent extends Event {

    public static final int CODE_OFFSET = 1;

    public CursorImageEvent (Display dpy, byte[] data) {
	super(dpy, data, 4); 
    }

    public int cursor_serial () { 
	return read4(8); 
    }

    public String toString () {
	String str;

	str = "XCursorImageNotify: code=" + code();
	
        str += " serial=" + cursor_serial();
	str += "\n";

        return str;
    }

}
