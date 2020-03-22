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
 * $Revision: 1.3 $
 * $Date: 2005-01-20 22:06:19 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.internal.rmi.rmiserver;



import java.awt.event.*;

import java.util.HashSet;
import org.jdesktop.lg3d.sg.internal.rmi.rmiserver.Node;
import org.jdesktop.lg3d.sg.internal.rmi.rmiserver.NodeRemote;
import org.jdesktop.lg3d.sg.internal.rmi.rmiserver.BranchGroupRemote;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.internal.j3d.j3dnodes.J3dComponent3D;

/**
 * The root class for all higher level looking glass components
 *
 * @author  Paul
 */

public class Cursor3D extends Component3D implements Cursor3DRemote {

    /** Creates a new instance of LgBranchGroup */

    public Cursor3D(String name) throws java.rmi.RemoteException {
        wrapped = new org.jdesktop.lg3d.wg.Cursor3D(name);
        wrapped.setUserData( this );
    }
    
    protected void createWrapped() {
    }
      
}

