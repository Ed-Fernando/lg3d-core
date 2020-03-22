/**
 * Project Looking Glass
 *
 * $RCSfile: SceneLayoutManager.java,v $
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
 * $Revision: 1.1 $
 * $Date: 2005-08-23 12:39:40 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.advanced.layout;

import org.jdesktop.lg3d.wg.Container3D;


/**
 * Defines the interface for classes that know how to lay out Desktops.
 * All the method invocations are synchronized by the associated Container3D.
 */
public interface SceneLayoutManager {
        
    /**
     * Lays out the associated container.
     */
    public void layoutScene();
    
    /**
     * Set the default Desktop. default desktop is normaly placed at 0,0,0
     *
     * @param desktop the default Desktop
     */
    public void setDefaultScene(Container3D[] desktop);
    
    /**
     * Adds the specified desktop to the layout, using the specified 
     * constraint object.
     *
     * @param desktop the desktop to be added
     * @param constraints where/how the component is added to the layout.
     */
    public void addLayoutScene(Container3D desktop, Object constraints);
    
    /**
     * Removes the specified component from the layout.
     *
     * @param desktop the desktop to be removed
     */
    public void removeLayoutScene(Container3D desktop);
    
    /**
     * Rearrange the specified component in the layout to a new location,
     * using the specified newConstraints object.
     *
     * @param desktop the desktop to be rearranged
     * @param newConstraints where/how the component is added to the layout.
     * @return true if rearrrangement is taken place and layoutContainer()
     * needs to be invoked by the associated container.
     */
    public boolean rearrangeLayoutScene(Container3D desktop, Object newConstraints);
}

