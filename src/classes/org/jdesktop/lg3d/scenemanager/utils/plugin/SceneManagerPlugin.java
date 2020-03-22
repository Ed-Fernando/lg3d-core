/**
 * Project Looking Glass
 *
 * $RCSfile: SceneManagerPlugin.java,v $
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
 * $Date: 2006-03-24 10:36:43 $A
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.plugin;

import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.scenemanager.utils.SceneControl;

public interface SceneManagerPlugin {
    public void initialize(SceneControl sceneControl);
    public void destroy();
    public boolean isRemovable();
    public Component3D getPluginRoot();
}
