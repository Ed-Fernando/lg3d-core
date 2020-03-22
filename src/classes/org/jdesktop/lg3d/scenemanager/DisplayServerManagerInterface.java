/**
 * Project Looking Glass
 *
 * $RCSfile: DisplayServerManagerInterface.java,v $
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
 * $Date: 2006-04-26 09:34:25 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager;

import org.jdesktop.lg3d.sg.BranchGroup;
import org.jdesktop.lg3d.sg.Transform3D;
import org.jdesktop.lg3d.wg.event.LgEventSource;


/**
 * The Master Control interface used by the Scene Manager
 *
 * @author  Paul
 */
public interface DisplayServerManagerInterface {
    
    /**
     * Set transform of the ViewPlatformTransform 
     */
    public void setViewPlatformTransform(Transform3D viewTransform);
    
    /**
     * Returns the Root node that the scene manager will attach
     * everything to.
     */
    public BranchGroup getSceneRoot();
        
    /**
     * Set the cursor module
     */
    public void setCursorModule(CursorModule cursorModule);

    /**
     * Set the gesture module
     */
    public void setGestureModule(GestureModule gestureModule);
    
    /**
     * Print a description of the current contents of the scene graph
     * to stdout.
     */
    public void printSceneGraph();
}
