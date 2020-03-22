/**
 * Project Looking Glass
 *
 * $RCSfile: CursorModule.java,v $
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
 * $Revision: 1.9 $
 * $Date: 2007-07-21 01:34:29 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager;

import javax.media.j3d.Canvas3D;
import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.wg.Cursor3D;
import org.jdesktop.lg3d.sg.BranchGroup;
import org.jdesktop.lg3d.displayserver.fws.MouseEventNodeInfo;


/**
 * Interface for the DisplayServer cursor module. The cursor module handles the
 * cursor position. Implementation allows for multiple cursor types, and
 * handling by mutliple devices.
 * This inteface also provides an abstraction of 3D drag implementation.
 */
public interface CursorModule {

    /**
     * Add the cursor to the display
     * @param cursor the cursor to add
     * @see #removeCursor
     */
    public void addCursor( Cursor3D cursor );
    
    /**
     * Remove the cursor
     * @param cursor the cursor to remove
     * @see #addCursor
     */
    public void removeCursor( Cursor3D cursor );
    
    /**
     * Set the currently visible Cursor.
     *
     * If the cursor has not already been added to the set of available cursors
     * using addCursor(..) then this call will add it.
     *
     * The transition from one cursor to another may involve some animation
     * so the change is not immediate. This call starts the transition, it
     * does not wait for it to complete
     * @param name the cursor to show
     */
    public void setCursor( Cursor3D cursor );
    
    /**
     * Returns the currently visible cursor
     * @see #setCursor
     */
    public Cursor3D getCursor ();

    /**
     * Given an (x,y) position in a canvas return where the cursor
     * would be in virtual world coordinates. This routine returns the
     * same value as setCursorPosition, but unlike this function it does
     * not actually move the cursor.
     *
     * awtX and awtY are the cursor position within the canvas
     * distance is the desired distance from the eye to place the cursor
     * @param cursorPos the Vector3f object to put in the cursor position.
     * @param awtX the x cursor position within the canvas
     * @param awtY the y cursor position within the canvas
     * @param canvas the canvas the cursor is on
     * @param distance Desired distance from the eye to place the cursor.
     *                 Set to NaN for the default distance.
     *
     * @return the cursor position in virtual world coordinates.
     */
    public Vector3f cursorPositionInVworld(Vector3f cursorPos, 
                        int awtX, int awtY, Canvas3D canvas3d, float distance);
    
    /**
     * Set the position of the cursor
     *
     * awtX and awtY are the cursor position within the canvas
     * distance is the desired distance from the eye to place the cursor
     * @param cursorPos the Vector3f object to put in the cursor position.
     * @param awtX the x cursor position within the canvas
     * @param awtY the y cursor position within the canvas
     * @param canvas the canvas the cursor is on
     * @param distance Desired distance from the eye to place the cursor.
     *                 Set to NaN for the default distance.
     *
     * @return the cursor position
     */
    public Vector3f setCursorPosition(Vector3f cursorPos, 
                        int awtX, int awtY, Canvas3D canvas, float distance);
    
    /**
     * Calculates the drag point based on the currently dragged node
     * and the info on the nodes picked at the current mouse position
     * following the 3D drag policy the implementor want to achieve. 
     *
     * @param dragPoint the Vector3f object to put in the drag point.
     * @param awtX the x cursor position within the canvas
     * @param awtY the y cursor position within the canvas
     * @param canvas the canvas the cursor is on
     * @param draggedNodeInfo nodeInfo for the dragged node
     * @param pickedNodeInfo nodeInfo for the picked nodes
     *
     * @return the cursor position in virtual world coordinates.
     */
    public Vector3f dragPointInVworld(Vector3f dragPoint, 
                        int awtX, int awtY, Canvas3D canvas3d, 
                        MouseEventNodeInfo draggedNodeInfo, 
                        MouseEventNodeInfo pickedNodeInfo);
    
    /**
     * Set the group to which cursor module should
     * attach the cursor managament graph
     * @param root the branch group to attach the cursor module to
     */
    public void setModuleRoot( BranchGroup root );
}
