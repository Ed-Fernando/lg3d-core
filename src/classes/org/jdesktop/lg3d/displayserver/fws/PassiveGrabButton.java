/**
 * Project Looking Glass
 *
 * $RCSfile: PassiveGrabButton.java,v $
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
import org.jdesktop.lg3d.wg.Cursor3D;

public class PassiveGrabButton extends PassiveGrab {

    protected int buttonNum;
    protected Cursor3D cursor;
    
    public PassiveGrabButton (int buttonNum, long modifiers, Node grabNode, Cursor3D cursor) {
	super(POINTER, BUTTON, modifiers, grabNode);
	this.buttonNum = buttonNum;
	this.cursor = cursor;
	modifiersDetail = new GrabDetail(modifiers);
	detail = new GrabDetail(buttonNum);
    }

    public int getButtonNum () { return buttonNum; }
    public Cursor3D getCursor () { return cursor; }

    public String toString () {
	String str;

        str = "[";
        str += super.toString();
	str += ",buttonNum=";
	str += buttonNum;
	str += ",cursor=";
	str += cursor;
        str += "]";

	return str;
    }
}


