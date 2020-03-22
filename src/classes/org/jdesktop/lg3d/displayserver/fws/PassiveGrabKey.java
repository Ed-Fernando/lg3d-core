/**
 * Project Looking Glass
 *
 * $RCSfile: PassiveGrabKey.java,v $
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
 * $Date: 2005-06-24 19:47:55 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.displayserver.fws;

import javax.media.j3d.Node;

public class PassiveGrabKey extends PassiveGrab {

    private int keyCode;

    public PassiveGrabKey (int keyCode, long modifiers, Node grabNode) {
	super(KEYBOARD, KEY, modifiers, grabNode);
	this.keyCode = keyCode;
	modifiersDetail = new GrabDetail(modifiers);
	detail = new GrabDetail(keyCode);
    }

    public int getKeyCode () { return keyCode; }

    public String toString () {
	String str;

        str = "[";
        str += super.toString();
	str += ",keyCode=";
	str += keyCode;
        str += "]";

	return str;
    }

}


