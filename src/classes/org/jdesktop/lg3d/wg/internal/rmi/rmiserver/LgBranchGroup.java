/**
 * Project Looking Glass
 *
 * $RCSfile: LgBranchGroup.java,v $
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
 * $Date: 2005-04-14 23:05:27 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.internal.rmi.rmiserver;

import org.jdesktop.lg3d.displayserver.LgNodeManager;
import org.jdesktop.lg3d.sg.internal.rmi.rmiserver.BranchGroup;


/**
 * The root class for all higher level looking glass components
 *
 * @author  Paul
 */
public class LgBranchGroup extends BranchGroup implements LgBranchGroupRemote {
    
    /** Creates a new instance of LgBranchGroup */
    public LgBranchGroup() throws java.rmi.RemoteException {
    }
    
    protected void createWrapped() {
        logger.finest("Creating LgBranchGroup");
        wrapped = new org.jdesktop.lg3d.wg.Component3D();
        wrapped.setUserData( this );
    }
    
    public void setMouseEventSource(Class eventClass, boolean enabled) throws java.rmi.RemoteException {
        ((org.jdesktop.lg3d.wg.Component3D)wrapped).setMouseEventSource(eventClass, enabled);
    }
    
    public boolean isMouseEventSource(Class eventClass) throws java.rmi.RemoteException {
        return ((org.jdesktop.lg3d.wg.Component3D)wrapped).isMouseEventSource(eventClass);
    }
    
    public void setMouseEventEnabled(boolean enabled) throws java.rmi.RemoteException {
        ((org.jdesktop.lg3d.wg.Component3D)wrapped).setMouseEventEnabled(enabled);
    }
    
    public boolean isMouseEventEnabled() throws java.rmi.RemoteException {
        return ((org.jdesktop.lg3d.wg.Component3D)wrapped).isMouseEventEnabled();
    }
    
    public void setMouseEventPropagatable(boolean enabled) throws java.rmi.RemoteException {
        ((org.jdesktop.lg3d.wg.Component3D)wrapped).setMouseEventPropagatable(enabled);
    }
    
    public boolean isMouseEventPropagatable() throws java.rmi.RemoteException {
        return ((org.jdesktop.lg3d.wg.Component3D)wrapped).isMouseEventPropagatable();
    }
    
    public void setKeyEventSource(boolean enabled) throws java.rmi.RemoteException {
        ((org.jdesktop.lg3d.wg.Component3D)wrapped).setKeyEventSource(enabled);
    }
    
    public boolean isKeyEventSource() throws java.rmi.RemoteException {
        return ((org.jdesktop.lg3d.wg.Component3D)wrapped).isKeyEventSource();
    }
    
    public void setNodeID(org.jdesktop.lg3d.displayserver.NodeID nodeID) throws java.rmi.RemoteException {
        // ((org.jdesktop.lg3d.wg.Component3D)wrapped).setNodeID( nodeID );
        LgNodeManager.c3dAccessHelper.setNodeID((org.jdesktop.lg3d.wg.Component3D)wrapped, nodeID);
    }
    
    public void setCapabilities() throws java.rmi.RemoteException {
        throw new RuntimeException("to be removed");
    }
}

