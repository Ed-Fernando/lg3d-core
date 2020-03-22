/**
 * Project Looking Glass
 *
 * $RCSfile: MouseMovedEvent3D.java,v $
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
 * $Date: 2005-06-24 19:48:45 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.event;

import java.awt.event.MouseEvent;
import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.displayserver.fws.MouseEventNodeInfo;

/**
 * An event which indicates that a mouse move action occurred. 
 */
public class MouseMovedEvent3D extends MouseMotionEvent3D {

    /**
     * Create a new MouseWheelEvent3D from the AWT mouse event
     * @param evt the AWT event
     * @param nodeInfo destination node info for the event
     * @param cursorPos the position of the 3D cursor
     */
    public MouseMovedEvent3D(MouseEvent evt, MouseEventNodeInfo nodeInfo, 
                              Vector3f cursorPos) 
    {
        super(evt, nodeInfo, cursorPos);
    }
}
