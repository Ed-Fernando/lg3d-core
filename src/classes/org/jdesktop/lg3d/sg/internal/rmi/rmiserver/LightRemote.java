/**
 * Project Looking Glass
 *
 * $RCSfile: LightRemote.java,v $
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
 * $Date: 2004-06-23 18:51:17 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.rmi.rmiserver;

import javax.vecmath.Color3f;
import java.util.Enumeration;

public interface LightRemote extends LeafRemote {

    /**
     * Turns the light on or off.
     * @param state true or false to set light on or off
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setEnable(boolean state) throws java.rmi.RemoteException;

    /**
     * Retrieves this Light's current state (on/off).
     * @return this node's current state (on/off)
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public boolean getEnable() throws java.rmi.RemoteException;

    /**
     * Sets the Light's current color.
     * @param color the value of this node's new color
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setColor(Color3f color) throws java.rmi.RemoteException;

    /**
     * Gets this Light's current color and places it in the parameter specified.
     * @param color the vector that will receive this node's color
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void getColor(Color3f color) throws java.rmi.RemoteException;

    /**
     * Replaces the node at the specified index in this Light node's
     * list of scopes with the specified Group node.
     * By default, Light nodes are scoped only by their influencing
     * bounds.  This allows them to be further scoped by a list of
     * nodes in the hierarchy.
     * @param scope the Group node to be stored at the specified index.
     * @param index the index of the Group node to be replaced.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     * @exception RestrictedAccessException if the specified group node
     * is part of a compiled scene graph
     */
    public void setScope(GroupRemote scope, int index) throws java.rmi.RemoteException;


    /**
     * Retrieves the Group node at the specified index from this Light node's
     * list of scopes.
     * @param index the index of the Group node to be returned.
     * @return the Group node at the specified index.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public Group getScope(int index) throws java.rmi.RemoteException;


    /**
     * Inserts the specified Group node into this Light node's
     * list of scopes at the specified index.
     * By default, Light nodes are scoped only by their influencing
     * bounds.  This allows them to be further scoped by a list of
     * nodes in the hierarchy.
     * @param scope the Group node to be inserted at the specified index.
     * @param index the index at which the Group node is inserted.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     * @exception RestrictedAccessException if the specified group node
     * is part of a compiled scene graph
     */
    public void insertScope(GroupRemote scope, int index) throws java.rmi.RemoteException;


    /**
     * Removes the node at the specified index from this Light node's
     * list of scopes.  If this operation causes the list of scopes to
     * become empty, then this Light will have universe scope: all nodes
     * within the region of influence will be affected by this Light node.
     * @param index the index of the Group node to be removed.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     * @exception RestrictedAccessException if the group node at the
     * specified index is part of a compiled scene graph
     */
    public void removeScope(int index) throws java.rmi.RemoteException;


    /**
     * Returns an enumeration of this Light node's list of scopes.
     * @return an Enumeration object containing all nodes in this Light node's
     * list of scopes.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public Enumeration getAllScopes() throws java.rmi.RemoteException;


    /**
     * Appends the specified Group node to this Light node's list of scopes.
     * By default, Light nodes are scoped only by their influencing
     * bounds.  This allows them to be further scoped by a list of
     * nodes in the hierarchy.
     * @param scope the Group node to be appended.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     * @exception RestrictedAccessException if the specified group node
     * is part of a compiled scene graph
     */
    public void addScope(GroupRemote scope) throws java.rmi.RemoteException;


    /**
     * Returns the number of nodes in this Light node's list of scopes.
     * If this number is 0, then the list of scopes is empty and this
     * Light node has universe scope: all nodes within the region of
     * influence are affected by this Light node.
     * @return the number of nodes in this Light node's list of scopes.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public int numScopes() throws java.rmi.RemoteException;


    /**
     * Retrieves the index of the specified Group node in this
     * Light node's list of scopes.
     *
     * @param scope the Group node to be looked up.
     * @return the index of the specified Group node;
     * returns -1 if the object is not in the list.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.3
     */
    public int indexOfScope(GroupRemote scope) throws java.rmi.RemoteException;

    /**
     * Removes the specified Group node from this Light
     * node's list of scopes.  If the specified object is not in the
     * list, the list is not modified.  If this operation causes the
     * list of scopes to become empty, then this Light
     * will have universe scope: all nodes within the region of
     * influence will be affected by this Light node.
     *
     * @param scope the Group node to be removed.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     * @exception RestrictedAccessException if the specified group node
     * is part of a compiled scene graph
     *
     * @since Java 3D 1.3
     */
    public void removeScope(GroupRemote scope) throws java.rmi.RemoteException;


    /**
     * Removes all Group nodes from this Light node's
     * list of scopes.  The Light node will then have
     * universe scope: all nodes within the region of influence will
     * be affected by this Light node.
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     * @exception RestrictedAccessException if any group node in this
     * node's list of scopes is part of a compiled scene graph
     *
     * @since Java 3D 1.3
     */
    public void removeAllScopes() throws java.rmi.RemoteException;


    /**
     * Sets the Light's influencing region to the specified bounds.
     * This is used when the influencing bounding leaf is set to null.
     * @param region the bounds that contains the Light's new influencing
     * region.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setInfluencingBounds(BoundsRemote region) throws java.rmi.RemoteException;

    /**
     * Retrieves the Light node's influencing bounds.
     * @return this Light's influencing bounds information
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public BoundsRemote getInfluencingBounds() throws java.rmi.RemoteException;

    /**
     * Sets the Light's influencing region to the specified bounding leaf.
     * When set to a value other than null, this overrides the influencing
     * bounds object.
     * @param region the bounding leaf node used to specify the Light
     * node's new influencing region.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
//    public void setInfluencingBoundingLeaf(BoundingLeaf region) {
//    	if (isLiveOrCompiled())
//	    if(!this.getCapability(ALLOW_INFLUENCING_BOUNDS_WRITE))
//	    	throw new CapabilityNotSetException(J3dI18N.getString("Light11"));
//
//	if (isLive())
//	    ((LightRetained)this.retained).setInfluencingBoundingLeaf(region);
//	else
//	    ((LightRetained)this.retained).initInfluencingBoundingLeaf(region);
//    }

    /**
     * Retrieves the Light node's influencing bounding leaf.
     * @return this Light's influencing bounding leaf information
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
//    public BoundingLeaf getInfluencingBoundingLeaf() {
//    	if (isLiveOrCompiled())
//	    if(!this.getCapability(ALLOW_INFLUENCING_BOUNDS_READ))
//	    	throw new CapabilityNotSetException(J3dI18N.getString("Light12"));
//
//	return ((LightRetained)this.retained).getInfluencingBoundingLeaf();
//    }


}
