/**
 * Project Looking Glass
 *
 * $RCSfile: SceneControl.java,v $
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
 * $Date: 2006-03-24 10:36:41 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.scenemanager.utils;

import org.jdesktop.lg3d.scenemanager.utils.appcontainer.AppContainer;
import org.jdesktop.lg3d.scenemanager.utils.background.Background;
import org.jdesktop.lg3d.scenemanager.utils.plugin.SceneManagerPlugin;


public interface SceneControl {
    public AppContainer getAppContainer(int index);
    public int getCurrentAppContainer();
    public void setCurrentAppContainer(int index);
    public void swapAppContainer(int indexA, int indexB);
    
    public void setBackground(Background background);
}
