/**
 * Project Looking Glass
 *
 * $RCSfile: LgBranchGroupRemote.java,v $
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

import org.jdesktop.lg3d.sg.internal.rmi.rmiserver.BranchGroupRemote;

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

public interface LgBranchGroupRemote extends BranchGroupRemote {

    public void setMouseEventSource(Class eventClass, boolean enabled) throws java.rmi.RemoteException;

    public boolean isMouseEventSource(Class eventClass) throws java.rmi.RemoteException;
    
    public void setMouseEventEnabled(boolean enabled) throws java.rmi.RemoteException;
    
    public boolean isMouseEventEnabled() throws java.rmi.RemoteException;
    
    public void setMouseEventPropagatable(boolean enabled) throws java.rmi.RemoteException;
    
    public boolean isMouseEventPropagatable() throws java.rmi.RemoteException;
    
    public void setKeyEventSource(boolean enabled) throws java.rmi.RemoteException;
    
    public boolean isKeyEventSource() throws java.rmi.RemoteException;
    
    public void setNodeID(org.jdesktop.lg3d.displayserver.NodeID nodeID) throws java.rmi.RemoteException;
    
    public void setCapabilities() throws java.rmi.RemoteException;
}
