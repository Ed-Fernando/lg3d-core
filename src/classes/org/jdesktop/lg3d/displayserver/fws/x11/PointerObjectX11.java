/**
 * Project Looking Glass
 *
 * $RCSfile: PointerObjectX11.java,v $
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
 * $Revision: 1.3 $
 * $Date: 2006-09-14 22:27:59 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.displayserver.fws.x11;

import org.jdesktop.lg3d.displayserver.fws.PointerObject;

/**
 * Contains information about the native window and scene graph node 
 * that the pointer is currently inside of. Also contains information 
 * derived from the node and the scene graph.
 */

public class PointerObjectX11 extends PointerObject {

    private static final int INVALID_WINDOW = 0;

    // The native window the pointer is inside of
    long wid = INVALID_WINDOW;

    // The 3D event info (from the Picker)
    EventInfo3D evinfo;

    public void reset () {
	super.reset();
	wid = INVALID_WINDOW;
	evinfo = null;
    }

    public void copyFrom (PointerObjectX11 po) {
	super.copyFrom(po);
	wid = po.wid;
	evinfo = po.evinfo;
    }

    public boolean equals (PointerObject po) {
	if (wid != ((PointerObjectX11)po).wid) return false;
	return super.equals(po);
    }

    public String toString () {
	String str = super.toString();
	
	str += ",wid=" + wid;
	str += ",evinfo=" + evinfo;

	return str;
    }
}

