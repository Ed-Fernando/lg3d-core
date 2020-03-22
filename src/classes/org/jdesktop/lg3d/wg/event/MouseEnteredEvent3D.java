/**
 * Project Looking Glass
 *
 * $RCSfile: MouseEnteredEvent3D.java,v $
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
 * $Date: 2005-06-24 19:48:44 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.event;

import java.awt.event.MouseEvent;
import org.jdesktop.lg3d.displayserver.fws.MouseEventNodeInfo;
import javax.vecmath.Vector3f;

/**
 * An event which indicates that a mouse enter and exit action occurred. 
 */
public class MouseEnteredEvent3D extends MouseEvent3D {

    /**
     * Create a new MouseWheelEvent3D from the AWT mouse event
     * @param evt the AWT event
     * @param nodeInfo destination node info for the event
     * @param cursorPos the coordinates of the cursor (in virtual world coordinates)
     */
    public MouseEnteredEvent3D(MouseEvent evt, MouseEventNodeInfo nodeInfo,
			       Vector3f cursorPos) {
        this( evt, evt.getID(), nodeInfo, cursorPos );
    }
    
    /**
     * Convert MouseEvent into a MouseEvent3D and change the event id to newID
     * @param evt the AWT event
     * @param newID the new id of the event
     * @param nodeInfo destination node info for the event
     * @param cursorPos the coordinates of the cursor (in virtual world coordinates)
     */
    public MouseEnteredEvent3D(MouseEvent evt, int newID, 
			       MouseEventNodeInfo nodeInfo, Vector3f cursorPos) {
        super( evt, newID, nodeInfo, cursorPos );
    }
    
    /**
     * Returns true if this event indicates mouse enter.
     * False return value indicates mouse exit event.
     */
    public boolean isEntered() {
        assert(getID() == MouseEvent.MOUSE_ENTERED || getID() == MouseEvent.MOUSE_EXITED);
        return (getID() == MouseEvent.MOUSE_ENTERED);
    }
}
