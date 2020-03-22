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
 * $Revision: 1.5 $
 * $Date: 2006-01-31 04:30:36 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.internal.rmi.rmiserver;


/**

 * The root class for all higher level looking glass components

 *

 * @author  Paul

 */

public class Container3D extends Component3D implements Container3DRemote {

    /** Creates a new instance of LgBranchGroup */

    public Container3D() throws java.rmi.RemoteException {
    }
    
    protected void createWrapped() {
        wrapped = new org.jdesktop.lg3d.wg.Container3D();
        wrapped.setUserData( this );
    }
    
    public void setDecoration(Component3DRemote component3D) throws java.rmi.RemoteException {
        ((org.jdesktop.lg3d.wg.Container3D)wrapped).setDecoration((org.jdesktop.lg3d.wg.Component3D)((Component3D)component3D).getWrapped());
    }
    
    public Component3DRemote getDecoration() throws java.rmi.RemoteException {
        org.jdesktop.lg3d.wg.Component3D deco 
                = ((org.jdesktop.lg3d.wg.Container3D)wrapped).getDecoration();
        if (deco == null) {
            return null;
        }
	return (Component3D)deco.getUserData();
    }
}

