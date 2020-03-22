/**
 * Project Looking Glass
 *
 * $RCSfile: TestSceneManagerConfig.java,v $
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
 * $Date: 2006-09-26 23:13:43 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.test;

import org.jdesktop.lg3d.scenemanager.SceneManager;

/**
 *
 * @author root
 */
public class TestSceneManagerConfig extends org.jdesktop.lg3d.scenemanager.config.SceneManagerConfig {
    
    
    /** Creates a new instance of TestSceneManagerConfig */
    public TestSceneManagerConfig() {
    }
    
    public SceneManager createSceneManager() {
        return new TestSceneManager();
    }
    
 
}
