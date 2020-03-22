/**
 * Project Looking Glass
 *
 * $RCSfile: SceneGraphObjectRemote.java,v $
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
 * $Date: 2004-06-23 18:51:21 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.rmi.rmiserver;

import java.util.Hashtable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * SceneGraphObject is a common superclass for
 * all scene graph component objects.  This includes Node,
 * Geometry, Appearance, etc.
 */
public interface SceneGraphObjectRemote extends Remote {
   // Any global flags? (e.g., execution cullable, collideable)
   
    /**
     * Retrieves the specified capability bit.  Note that only one capability
     * bit may be retrieved per method invocation--capability bits cannot
     * be ORed together.
     * @param bit the bit whose value is returned
     * @return true if the bit is set, false if the bit is clear
     */
    public boolean getCapability(int bit) throws java.rmi.RemoteException;

    /**
     * Sets the specified capability bit.  Note that only one capability bit
     * may be set per method invocation--capability bits cannot be ORed
     * together.
     * @param bit the bit to set
     * @exception RestrictedAccessException if this object is part of live
     * or compiled scene graph
     */
    public void setCapability(int bit) throws java.rmi.RemoteException;

    /**
     * Clear the specified capability bit.  Note that only one capability bit
     * may be cleared per method invocation--capability bits cannot be ORed
     * together.
     * @param bit the bit to clear
     * @exception RestrictedAccessException if this object is part of live
     * or compiled scene graph
     */
    public void clearCapability(int bit) throws java.rmi.RemoteException;

    /**
     * Returns a flag indicating whether the node is part of a live
     * scene graph.
     * @return true if node is part of a live scene graph, else false
     */
    public boolean isLive() throws java.rmi.RemoteException;

    /**
     * Sets the userData field associated with this scene graph object.
     * The userData field is a reference to an arbitrary object
     * and may be used to store any user-specific data associated
     * with this scene graph object--it is not used by the Java 3D API.
     * If this object is cloned, the userData field is copied
     * to the newly cloned object.
     * @param userData a reference to the new userData field
     */
    public void setUserData(Object userData) throws java.rmi.RemoteException;

    public void setName(String name) throws java.rmi.RemoteException;
    
    public String getName() throws java.rmi.RemoteException;
}

