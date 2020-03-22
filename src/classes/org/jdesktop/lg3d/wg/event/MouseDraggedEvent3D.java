/**
 * Project Looking Glass
 *
 * $RCSfile: MouseDraggedEvent3D.java,v $
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
 * $Revision: 1.11 $
 * $Date: 2006-03-16 01:23:43 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.event;

import java.awt.event.MouseEvent;
import javax.media.j3d.Canvas3D;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.jdesktop.lg3d.displayserver.fws.MouseEventNodeInfo;
import org.jdesktop.lg3d.displayserver.fws.FoundationWinSys;
import org.jdesktop.lg3d.scenemanager.CursorModule;


/**
 * An event which indicates that a mouse drag action occurred.  
 */
public class MouseDraggedEvent3D extends MouseMotionEvent3D {
    private MouseEventNodeInfo grabbedNodeInfo;
    private Vector3f tmpV3f = new Vector3f();
    
    /**
     * Create a new MouseWheelEvent3D from the AWT mouse event
     * @param evt the AWT event
     * @param nodeInfo node info for the pick caused by this event
     * @param grabbedNodeInfo destination node info for the event
     * @param cursorPos the position of the 3D cursor
     */
    public MouseDraggedEvent3D(MouseEvent evt, MouseEventNodeInfo nodeInfo, 
                              MouseEventNodeInfo grabbedNodeInfo, Vector3f cursorPos) 
    {
        super(evt, nodeInfo, cursorPos);
        this.grabbedNodeInfo = grabbedNodeInfo;
    }

    /**
     * While dragging, the dragPoint points to the coordinates of a point in 
     * the same plane as the picked object, but under the current cusor position.
     *
     * REMINDER -- this method hard-codes the 3D drag policy described above. 
     * The implementation might better be made pluggable.
     *  
     * @return the point where the object was dragged to
     */
    public Point3f getDragPoint(Point3f ret) {
        if (ret == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
        FoundationWinSys fws = FoundationWinSys.getFoundationWinSys();
        CursorModule cursorModule = fws.getCursorModule();
        synchronized (tmpV3f) {
            cursorModule.dragPointInVworld(tmpV3f, 
                    awtEvent.getX(), awtEvent.getY(), (Canvas3D)awtEvent.getSource(), 
                    grabbedNodeInfo, nodeInfo);
            ret.set(tmpV3f);
        }
        return ret;
    }
    
    /**
     * While dragging, the dragPoint points to the coordinates of a point in 
     * the same plane as the picked object, but under the current cusor position.
     *
     * @return the point in the local coordinate where the object was dragged to
     */
    public Point3f getLocalDragPoint(Point3f ret) {
        return translateToLocalCoord(getDragPoint(ret));
    }
}
