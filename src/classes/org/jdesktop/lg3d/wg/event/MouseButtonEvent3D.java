/**
 * Project Looking Glass
 *
 * $RCSfile: MouseButtonEvent3D.java,v $
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
 * $Date: 2006-03-07 07:16:50 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.event;

import java.awt.event.MouseEvent;
import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.displayserver.fws.MouseEventNodeInfo;

/**
 * An event which indicates that a mouse button action occurred. 
 */
public class MouseButtonEvent3D extends MouseEvent3D {
    private int changedModifiers;

    /**
     * Create a new MouseWheelEvent3D from the AWT mouse event
     * @param evt the AWT event
     * @param nodeInfo Destination node info for the event
     * @param cursorPos the coordinates of the cursor (in virtual world coordinates)
     */
    public MouseButtonEvent3D(MouseEvent evt, MouseEventNodeInfo nodeInfo, 
			      Vector3f cursorPos) {
        this( evt, evt.getID(), nodeInfo, cursorPos );
    }
    
    /**
     * Convert MouseEvent into a MouseEvent3D and change the event id to newID
     * @param evt the AWT event
     * @param newID the new id of the event
     * @param nodeInfo Destination node info for the event
     * @param cursorPos the coordinates of the cursor (in virtual world coordinates)
     */
    public MouseButtonEvent3D(MouseEvent evt, int newID, MouseEventNodeInfo nodeInfo, 
			      Vector3f cursorPos) {
        super( evt, newID, nodeInfo, cursorPos );
    }
    
    /**
     * Convert MouseEvent into a MouseEvent3D and change the event id to newID
     * @param evt the AWT event
     * @param newID the new id of the event
     * @param nodeInfo Destination node info for the event
     * @param cursorPos the coordinates of the cursor (in virtual world coordinates)
     */
    public MouseButtonEvent3D(MouseEvent evt, int newID, MouseEventNodeInfo nodeInfo, 
        int changedModifiers, Vector3f cursorPos) 
    {
        super( evt, newID, nodeInfo, cursorPos );
        this.changedModifiers = changedModifiers;
    }
    
    /**
     * Returns true if this event indicates mouse click.
     */
    public boolean isClicked() {
        return (getID() == MouseEvent.MOUSE_CLICKED);
    }
    
    /**
     * Returns true if this event indicates mouse press.
     */
    public boolean isPressed() {
        return (getID() == MouseEvent.MOUSE_PRESSED);
    }
    
    /**
     * Returns true if this event indicates mouse release.
     */
    public boolean isReleased() {
        return (getID() == MouseEvent.MOUSE_RELEASED);
    }
    
    /**
     * Returns the number of mouse clicks associated with this event.
     * @return integer value for the number of clicks
     */
    public int getClickCount() {
        return awtEvent.getClickCount();
    }
    
    /**
     * Returns a string representation of the object.
     */
    public String toString () {
	String str = "[" + super.toString(); 
	str += ",changedModifiers=" + changedModifiers;
	str += "]";
	return str;
    }
}
