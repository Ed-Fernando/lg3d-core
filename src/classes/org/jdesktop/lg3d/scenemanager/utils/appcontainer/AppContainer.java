/**
 * Project Looking Glass
 *
 * $RCSfile: AppContainer.java,v $
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
 * $Date: 2006-03-16 06:11:49 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.appcontainer;


import java.util.logging.Logger;
import org.jdesktop.lg3d.wg.Container3D;
import org.jdesktop.lg3d.wg.Frame3D;


public abstract class AppContainer extends Container3D {
    protected static final Logger logger = Logger.getLogger("lg.scenemanager");
    private boolean enabled = false;
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
//        setPickable(enabled);
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public abstract void initialize();
    
    public abstract void addFrame3D(Frame3D frame3d);
    
    public abstract void removeFrame3D(Frame3D frame3d);
}

