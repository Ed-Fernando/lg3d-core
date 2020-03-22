/**
 * Project Looking Glass
 *
 * $RCSfile: SceneManagerConfig.java,v $
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
 * $Date: 2006-05-01 22:03:02 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.scenemanager.config;

import org.jdesktop.lg3d.scenemanager.SceneManager;

/**
 *
 * @author  paulby
 */
public abstract class SceneManagerConfig extends ConfigData {

    
    /** Creates a new instance of ConfigurationCompleteEvent */
    public SceneManagerConfig() {
    }

    public void doConfig() {
        // Special case, nothing to do
    }
    
    /**
     * Create and return the scene manager
     */
    public abstract SceneManager createSceneManager();

}
