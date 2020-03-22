/**
 * Project Looking Glass
 *
 * $RCSfile: MouseWheelEvent3D.java,v $
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
 * $Revision: 1.6 $
 * $Date: 2007-07-02 22:01:23 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.event;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.displayserver.fws.MouseEventNodeInfo;

/**
 * An event which indicates that a mouse wheel action occurred. 
 */
public class MouseWheelEvent3D extends MouseEvent3D {
    /** Constant represting scrolling by a &quot;block&quot; (like scrolling with page-up, page-down keys) */
//    public static final int WHEEL_BLOCK_SCROLL = MouseWheelEvent.WHEEL_BLOCK_SCROLL;
    /** Constant representing scrolling by &quot;units&quot; (like scrolling with the arrow keys) */
//    public static final int WHEEL_UNIT_SCROLL = MouseWheelEvent.WHEEL_UNIT_SCROLL;
    
    /**
     * Create a new MouseWheelEvent3D from the AWT mouse event
     * @param evt the AWT event
     * @param nodeInfo destination node info for the event
     * @param cursorPos the position of the 3D cursor
     */
    public MouseWheelEvent3D(MouseEvent evt, MouseEventNodeInfo nodeInfo,
			     Vector3f cursorPos) {
        this( evt, evt.getID(), nodeInfo, cursorPos );
    }
    
    /**
     * Convert MouseEvent into a MouseEvent3D and change the event id to newID
     * @param evt the AWT event
     * @param newID the new id of the event
     * @param nodeInfo destination node info for the event
     * @param cursorPos the position of the 3D cursor
     */
    public MouseWheelEvent3D(MouseEvent evt, int newID, MouseEventNodeInfo nodeInfo,
			     Vector3f cursorPos) {
        super( evt, newID, nodeInfo, cursorPos );
    }
    
    /**
     * Returns the number of "clicks" the mouse wheel was rotated.
     *
     * @return negative values if the mouse wheel was rotated up/away from the 
     * user, and positive values if the mouse wheel was rotated down/ 
     * towards the user
     */
    public int getWheelRotation() {
        return ((MouseWheelEvent)awtEvent).getWheelRotation();
    }
    
    /**
     * return a stringified refernce of the event
     * @return the stringifield version
     */
    public String toString() {
        return super.toString() + ",intersection=" + getIntersection(new Point3f());
    }
}
