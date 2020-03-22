/**
 * Project Looking Glass
 *
 * $RCSfile: SceneManager.java,v $
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
 * $Revision: 1.3 $
 * $Date: 2006-03-24 10:36:39 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager;

import org.jdesktop.lg3d.wg.Frame3D;


/**
 * The interface for all scene manager implementations
 *
 * @author  Paul
 */
public interface SceneManager {
    /** 
     * Inititalize a new Scene Manager.  This is invoked right after
     * its creation.
     * @param displayServer the display server manager to connect frames to
     */
    public void initialize(DisplayServerManagerInterface displayServer);
    
    /*
     * Add an App3D to the Scene
     * @param app3d the frame to add
     * @see #removeFrame3D
     */
    public void addFrame3D(Frame3D app3d);
    
    /*
     * Remove an App3D to the Scene
     * @param app3d the frame to remove
     * @see #addFrame3D
     */
    public void removeFrame3D(Frame3D app3d);
    
}
