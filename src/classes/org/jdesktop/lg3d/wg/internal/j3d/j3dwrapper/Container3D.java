/**
 * Project Looking Glass
 *
 * $RCSfile: Container3D.java,v $
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
 * $Date: 2006-01-31 04:30:34 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.internal.j3d.j3dwrapper;


import org.jdesktop.lg3d.wg.internal.wrapper.Container3DWrapper;
import org.jdesktop.lg3d.wg.internal.wrapper.Component3DWrapper;
import org.jdesktop.lg3d.wg.internal.j3d.j3dnodes.J3dContainer3D;
import org.jdesktop.lg3d.wg.internal.j3d.j3dnodes.J3dComponent3D;


/**
 * Component3D object is a top level object for widget development.
 *
 * It supports visual rendering by adding scene graph node children 
 * and event handling.
 */
public class Container3D extends Component3D implements Container3DWrapper {

    /**
     * Creates a new instance of Component3D.
     * 
     * By default, it is visible (i.e. getVisible() returns 'ture')
     * and it doesn't have any asscociated cursor.
     */
    public Container3D() {
    }
    
    protected void createWrapped() {
        wrapped = new J3dContainer3D();
        wrapped.setUserData(this);
    }
    
    public void setDecoration(Component3DWrapper deco) {
        ((J3dContainer3D)wrapped).setDecoration((J3dComponent3D)((Component3D)deco).getWrapped());
    }   
    
    public Component3DWrapper getDecoration() {
        J3dComponent3D deco = ((J3dContainer3D)wrapped).getDecoration();
        if (deco == null) {
            return null;
        }
	return (Component3DWrapper)deco.getUserData();
    }  
}
