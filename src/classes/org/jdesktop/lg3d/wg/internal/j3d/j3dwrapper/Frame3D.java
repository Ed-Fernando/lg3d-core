/**
 * Project Looking Glass
 *
 * $RCSfile: Frame3D.java,v $
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
 * $Date: 2005-01-20 22:06:15 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.internal.j3d.j3dwrapper;


import org.jdesktop.lg3d.wg.internal.wrapper.Frame3DWrapper;
import org.jdesktop.lg3d.wg.internal.wrapper.Component3DWrapper;
import org.jdesktop.lg3d.wg.internal.j3d.j3dnodes.J3dFrame3D;
import org.jdesktop.lg3d.wg.internal.j3d.j3dnodes.J3dComponent3D;

/**
 * Component3D object is a top level object for widget development.
 *
 * It supports visual rendering by adding scene graph node children 
 * and event handling.
 */
public class Frame3D extends Container3D implements Frame3DWrapper {

    /**
     * Creates a new instance of Component3D.
     * 
     * By default, it is visible (i.e. getVisible() returns 'ture')
     * and it doesn't have any asscociated cursor.
     */
    public Frame3D() {
    }
    
    protected void createWrapped() {
        wrapped = new J3dFrame3D();
        wrapped.setUserData(this);
    }

    public boolean getActive() {
        throw new RuntimeException("Should never be called");
    }
    
    public void setActive(boolean active) {
        throw new RuntimeException("Should never be called");
    }
    
    public void setThumbnail(Component3DWrapper thumbnail) {
        J3dComponent3D thumbnailWrapped 
            = (thumbnail == null)
                ?(null)
                :((J3dComponent3D)((Component3D)thumbnail).getWrapped());
	((J3dFrame3D)wrapped).setThumbnail(thumbnailWrapped);
    }
    
    public Component3DWrapper getThumbnail() {
        J3dComponent3D thumbnail = ((J3dFrame3D)wrapped).getThumbnail();
	return (thumbnail == null)?(null):((Component3DWrapper)thumbnail.getUserData());
    }
    
    public float getScreenWidth() {
        return ((J3dFrame3D)wrapped).getScreenWidth();
    }
    
    public float getScreenHeight() {
        return ((J3dFrame3D)wrapped).getScreenHeight();
    }
}
