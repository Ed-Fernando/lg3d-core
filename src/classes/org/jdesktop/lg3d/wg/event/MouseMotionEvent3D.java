/**
 * Project Looking Glass
 *
 * $RCSfile: MouseMotionEvent3D.java,v $
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
 * $Revision: 1.5 $
 * $Date: 2006-03-07 07:16:51 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.event;

import java.awt.event.MouseEvent;
import java.awt.event.InputEvent;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.displayserver.fws.MouseEventNodeInfo;

/**
 * An event which indicates that a mouse motion action occurred. 
 */
public abstract class MouseMotionEvent3D extends MouseEvent3D {    

    /**
     * Create a new MouseMotionEvent3D from the awt MouseEvent
     * @param evt the MouseEvent to encapsulate
     * @param nodeInfo destination node info for the event
     * @param cursorPos the position of the 3D cursor
     */
    public MouseMotionEvent3D(MouseEvent evt, MouseEventNodeInfo nodeInfo, 
                              Vector3f cursorPos) {
        this( evt, evt.getID(), nodeInfo, cursorPos );
    }
    
    /**
     * Convert MouseEvent into a MouseEvent3D and change the event id to newID
     * @param evt the MouseEvent to encapsulate
     * @param newID the new id of the event
     * @param nodeInfo destination node info for the event
     */
    public MouseMotionEvent3D(MouseEvent evt, int newID, MouseEventNodeInfo nodeInfo,
			      Vector3f cursorPos) {
        super( evt, newID, nodeInfo, cursorPos );
    }

    /**
     * Returns which, if any, of the mouse buttons is being pressed.
     * Returns ButtonId.NOBUTTON for no button is pressed.
     * In case where multiple buttuns are being pressed, BUTTON1 takes 
     * precedence over BUTTON2, and BUTTON2 takes precedence over BUTTON3.
     *
     * @return one of the following enums: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3.
     */
    public ButtonId getButton() {
        int modifierMask = awtEvent.getModifiersEx();
        ButtonId ret = ButtonId.NOBUTTON;
        
        if ((modifierMask & InputEvent.BUTTON1_DOWN_MASK) > 0) {
            ret = ButtonId.BUTTON1;
        }
        else if ((modifierMask & InputEvent.BUTTON2_DOWN_MASK) > 0) {
            ret = ButtonId.BUTTON2;
        }
        else if ((modifierMask & InputEvent.BUTTON3_DOWN_MASK) > 0) {
            ret = ButtonId.BUTTON3;
        }
	return ret;
    }
    
    /**
     * Returns a string representation of the object.
     */
    public String toString () {
	String str = "[" + super.toString(); 
	str += ",intersection=" + getIntersection(new Point3f());
	str += "]";
	return str;
    }
}
