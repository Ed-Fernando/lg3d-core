/**
 * Project Looking Glass
 *
 * $RCSfile: NativeButtonPressEvent.java,v $
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
 * $Date: 2006-04-25 21:33:54 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.displayserver.fws.x11;

import java.awt.event.MouseEvent;
import javax.media.j3d.Canvas3D;

/**
 * An event which is generated when the user clicks the interior of
 * a native window to move it to the top of the stack.
 */

public class NativeButtonPressEvent extends SequencedMouseEvent {

    private int  rootX;
    private int  rootY;

    public NativeButtonPressEvent (Canvas3D canvas, long rootWid, long wid, long when, 
				   int modifiers, int x, int y, int clickCount, 
				   boolean popupTrigger, int button, int rootX, int rootY) {
	super(canvas, rootWid, wid, MouseEvent.MOUSE_PRESSED, when, modifiers, x, y, 
	      clickCount, popupTrigger, button);
	this.rootX = rootX;
	this.rootY = rootY;
    }

    public int getRootX () { return rootX; }
    public int getRootY () { return rootY; }

    public String toString () {
	String s = super.toString();
	s += ", rootX = " + rootX;
	s += ", rootY = " + rootY;
	return s;
    }

}

