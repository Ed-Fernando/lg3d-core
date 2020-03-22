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
 * $Revision: 1.4 $
 * $Date: 2006-01-31 04:30:35 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.internal.rmi.rmiclient;

import org.jdesktop.lg3d.wg.internal.wrapper.Container3DWrapper;
import org.jdesktop.lg3d.wg.internal.wrapper.Component3DWrapper;
import org.jdesktop.lg3d.sg.internal.rmi.rmiclient.SceneGraphSetup;
import org.jdesktop.lg3d.wg.internal.rmi.rmiserver.Container3DRemote;
import org.jdesktop.lg3d.wg.internal.rmi.rmiserver.Component3DRemote;


public class Container3D extends Component3D implements Container3DWrapper {

    /**
     * Constructs and initializes a new BranchGroup node object.
     */
    public Container3D() {
    }
    
    protected void createRemote() {
        try {            
            remote = SceneGraphSetup.getSGObjectFactory().newContainer3D();
            setRemote( remote );
        } catch( java.rmi.RemoteException re ) {
            throw new RuntimeException( re );
        }
    }

    public void setSizeHint(float width, float height, float depth) {
        try {
            ((Container3DRemote)remote).setSizeHint(width, height, depth);
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
    }  
    
    public void setDecoration(Component3DWrapper component3D) {
        try {
            ((Container3DRemote)remote).setDecoration((Component3DRemote)((Component3D)component3D).remote);
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
    }
    
    public Component3DWrapper getDecoration() {
        try {
            return (Component3D)getLocal(((Container3DRemote)remote).getDecoration());
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
    }
}
