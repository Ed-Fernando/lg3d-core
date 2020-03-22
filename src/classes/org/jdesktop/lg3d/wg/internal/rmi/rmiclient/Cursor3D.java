/**
 * Project Looking Glass
 *
 * $RCSfile: Cursor3D.java,v $
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
 * $Revision: 1.2 $
 * $Date: 2004-06-23 18:52:30 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.internal.rmi.rmiclient;

import org.jdesktop.lg3d.wg.internal.wrapper.Cursor3DWrapper;
import org.jdesktop.lg3d.wg.internal.wrapper.Component3DWrapper;
import org.jdesktop.lg3d.sg.internal.rmi.rmiclient.SceneGraphSetup;
import org.jdesktop.lg3d.wg.internal.rmi.rmiserver.Container3DRemote;
import org.jdesktop.lg3d.wg.internal.rmi.rmiserver.Component3DRemote;


public class Cursor3D extends Component3D implements Cursor3DWrapper {

    /**
     * Constructs and initializes a new BranchGroup node object.
     */
    public Cursor3D(String cursorName) {
        try {            
            remote = SceneGraphSetup.getSGObjectFactory().newInstance(
                            org.jdesktop.lg3d.wg.Cursor3D.class,
                            new Class[] { String.class },
                            new Object[] { cursorName } );
            setRemote( remote );
        } catch( java.rmi.RemoteException re ) {
            throw new RuntimeException( re );
        }
    }
    
    protected void createRemote() {
    }


}
