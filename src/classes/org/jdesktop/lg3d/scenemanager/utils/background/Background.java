/**
 * Project Looking Glass
 *
 * $RCSfile: Background.java,v $
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
 * $Revision: 1.12 $
 * $Date: 2006-03-24 10:36:41 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.background;


import java.util.logging.Logger;

import org.jdesktop.lg3d.scenemanager.utils.SceneControl;
import org.jdesktop.lg3d.wg.Container3D;


public abstract class Background extends Container3D {
    protected static final Logger logger = Logger.getLogger("lg.scenemanager");
    
    /**
     * Called once before the background is first used, and everytime
     * when AppContainerControl is changed.  It is called before the
     * background is attached to the scene graph and before setEnabled(true) 
     * is called.
     */
    public abstract void initialize(SceneControl scenemanager);
    
    /**
     * Called each time the user selects this background. It is called with
     * a true argument before the background is attached to the scene graph.
     * And called with a false argument after the background has been detached
     * from the scene graph.
     */
    public abstract void setEnabled(boolean enabled);
    
    // TODO #86
//    /**
//     * Called to destroy the background. Only called on a deactivated
//     * background.
//     */
//    public abstract void destroy(AppContainerControl scenemanager);
}
