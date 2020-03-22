/**
 * Project Looking Glass
 *
 * $RCSfile: SceneGraphObject.java,v $
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
import java.rmi.server.UnicastRemoteObject;

/**
 * SceneGraphObject is a common superclass for
 * all scene graph component objects.  This includes Node,
 * Geometry, Appearance, etc.
 */
public abstract class SceneGraphObject extends UnicastRemoteObject implements SceneGraphObjectRemote {
   // Any global flags? (e.g., execution cullable, collideable)

    protected org.jdesktop.lg3d.sg.SceneGraphObject wrapped;
    
    private Object userData;
    
    protected java.util.logging.Logger logger = java.util.logging.Logger.getLogger("lg.sg");

    /**
     * Constructs a SceneGraphObject with default parameters.  The default
     * values are as follows:
     * <ul>
     * capability bits : clear (all bits)<br>
     * isLive : false<br>
     * isCompiled : false<br>
     * user data : null<br>
     * </ul>
     */
    public SceneGraphObject() throws java.rmi.RemoteException {	
        createWrapped();
    }
    
    /**
     * TODO make this abstract
     */
    protected abstract void createWrapped();
//    {
//        System.out.println("Warning - createWrapped not implemented in "+this.getClass() );
//    }
  
    /**
     * Retrieves the specified capability bit.  Note that only one capability
     * bit may be retrieved per method invocation--capability bits cannot
     * be ORed together.
     * @param bit the bit whose value is returned
     * @return true if the bit is set, false if the bit is clear
     */
    public final boolean getCapability(int bit) throws java.rmi.RemoteException {
	return wrapped.getCapability( bit );
    }

    /**
     * Sets the specified capability bit.  Note that only one capability bit
     * may be set per method invocation--capability bits cannot be ORed
     * together.
     * @param bit the bit to set
     * @exception RestrictedAccessException if this object is part of live
     * or compiled scene graph
     */
    public final void setCapability(int bit) throws java.rmi.RemoteException {
	wrapped.setCapability( bit );
    }

    /**
     * Clear the specified capability bit.  Note that only one capability bit
     * may be cleared per method invocation--capability bits cannot be ORed
     * together.
     * @param bit the bit to clear
     * @exception RestrictedAccessException if this object is part of live
     * or compiled scene graph
     */
    public final void clearCapability(int bit) throws java.rmi.RemoteException {
        wrapped.clearCapability( bit );
    }

    /**
     * Returns a flag indicating whether the node is part of a live
     * scene graph.
     * @return true if node is part of a live scene graph, else false
     */
    public final boolean isLive() throws java.rmi.RemoteException {
	return wrapped.isLive();
    }

    /**
     * Sets the userData field associated with this scene graph object.
     * The userData field is a reference to an arbitrary object
     * and may be used to store any user-specific data associated
     * with this scene graph object--it is not used by the Java 3D API.
     * If this object is cloned, the userData field is copied
     * to the newly cloned object.
     * @param userData a reference to the new userData field
     */
    public void setUserData(Object userData) throws java.rmi.RemoteException {
	this.userData = userData;
    }
    
    public org.jdesktop.lg3d.sg.SceneGraphObject getWrapped() {
        return wrapped;
    }
    
    public void setName( String name ) throws java.rmi.RemoteException {
        wrapped.setName(name);
    }
    
    public String getName() throws java.rmi.RemoteException {
        return wrapped.getName();
    }

    /**
     * Retrieves the userData field from this scene graph object.
     * @return the current userData field
     */
//    public Object getUserData() {
//	return userData;
//    }
    

    SceneGraphObject getLocal( SceneGraphObjectRemote remote ) {
        return SGObjectFactoryImpl.getFactoryImpl().getLocal( remote );
    }
    
    Transform3D getLocal( Transform3DRemote remote ) {
        return SGObjectFactoryImpl.getFactoryImpl().getLocal( remote );
    }
    
}

