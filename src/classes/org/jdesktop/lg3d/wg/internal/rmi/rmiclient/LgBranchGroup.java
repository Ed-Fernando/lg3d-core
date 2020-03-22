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
 * $Date: 2005-04-14 23:05:23 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.internal.rmi.rmiclient;

import org.jdesktop.lg3d.sg.internal.rmi.rmiclient.BranchGroup;
import org.jdesktop.lg3d.sg.internal.rmi.rmiclient.SceneGraphSetup;
import org.jdesktop.lg3d.wg.internal.wrapper.LgBranchGroupWrapper;

import org.jdesktop.lg3d.wg.internal.rmi.rmiserver.LgBranchGroupRemote;

/**
 * The BranchGroup serves as a pointer to the root of a
 * scene graph branch; BranchGroup objects are the only objects that
 * can be inserted into a Locale's set of objects. A subgraph, rooted
 * by a BranchGroup node can be thought of as a compile unit. The
 * following things may be done with BranchGroup:
 * <P><UL>
 * <LI>A BranchGroup may be compiled by calling its compile method. This causes the
 * entire subgraph to be compiled. If any BranchGroup nodes are contained within the
 * subgraph, they are compiled as well (along with their descendants).</LI>
 * <p>
 * <LI>A BranchGroup may be inserted into a virtual universe by attaching it to a
 * Locale. The entire subgraph is then said to be live.</LI>
 * <p>
 * <LI>A BranchGroup that is contained within another subgraph may be reparented or
 * detached at run time if the appropriate capabilities are set.</LI>
 * </UL>
 * Note that that if a BranchGroup is included in another subgraph, as a child of
 * some other group node, it may not be attached to a Locale.
 *
 * @version 	1.54, 04/01/28 13:11:07188

 */

public class LgBranchGroup extends BranchGroup implements LgBranchGroupWrapper {

    /**
     * Constructs and initializes a new BranchGroup node object.
     */
    public LgBranchGroup() {
    }
    
    protected void createRemote() {
        try {            
            remote = SceneGraphSetup.getSGObjectFactory().newInstance( org.jdesktop.lg3d.wg.internal.rmi.rmiserver.LgBranchGroup.class );
            setRemote( remote );
        } catch( java.rmi.RemoteException re ) {
            throw new RuntimeException( re );
        }
    }

    public boolean isMouseEventSource(Class eventClass) {
        try {
            return ((LgBranchGroupRemote)remote).isMouseEventSource(eventClass);
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }
    
    public void setMouseEventSource(Class eventClass, boolean enabled) {
        try {
            ((LgBranchGroupRemote)remote).setMouseEventSource(eventClass, enabled);
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }
    
    public boolean isMouseEventEnabled() {
        try {
            return ((LgBranchGroupRemote)remote).isMouseEventEnabled();
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }
    
    public void setMouseEventEnabled(boolean enabled) {
        try {
            ((LgBranchGroupRemote)remote).setMouseEventEnabled(enabled);
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }
    
    public boolean isMouseEventPropagatable() {
        try {
            return ((LgBranchGroupRemote)remote).isMouseEventPropagatable();
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }
    
    public void setMouseEventPropagatable(boolean enabled) {
        try {
            ((LgBranchGroupRemote)remote).setMouseEventPropagatable(enabled);
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }
    
    public boolean isKeyEventSource() {
        try {
            return ((LgBranchGroupRemote)remote).isKeyEventSource();
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }
    
    public void setKeyEventSource(boolean enabled) {
        try {
            ((LgBranchGroupRemote)remote).setKeyEventSource(enabled);
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }
    
    public void setNodeID(org.jdesktop.lg3d.displayserver.NodeID nodeID) {
        try {
            ((LgBranchGroupRemote)remote).setNodeID(nodeID);
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }
    
    public void setCapabilities() {
        try {
            ((LgBranchGroupRemote)remote).setCapabilities();
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }
    
}
