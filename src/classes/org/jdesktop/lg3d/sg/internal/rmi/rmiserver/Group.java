/**
 * Project Looking Glass
 *
 * $RCSfile: Group.java,v $
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
 * $Date: 2004-06-23 18:51:11 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.rmi.rmiserver;

import java.util.BitSet;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

/**
 * The Group node object is a general-purpose grouping node. Group
 * nodes have exactly one parent and an arbitrary number of children
 * that are rendered in an unspecified order (or in parallel).  Null
 * children are allowed; no operation is performed on a null child
 * node.  Operations on Group node objects include adding, removing,
 * and enumerating the children of the Group node. The subclasses of
 * Group node add additional semantics.
 */

public class Group extends Node implements GroupRemote {
    /**
     * Specifies that this Group node allows reading its children.
     */
    public static final int
    ALLOW_CHILDREN_READ = CapabilityBits.GROUP_ALLOW_CHILDREN_READ;

    /**
     * Specifies that this Group node allows writing its children.
     */
    public static final int
    ALLOW_CHILDREN_WRITE = CapabilityBits.GROUP_ALLOW_CHILDREN_WRITE;

    /**
     * Specifies that this Group node allows adding new children.
     */
    public static final int
    ALLOW_CHILDREN_EXTEND = CapabilityBits.GROUP_ALLOW_CHILDREN_EXTEND;

    /**
     * Specifies that this Group node allows reading its collision Bounds
     */
    public static final int
    ALLOW_COLLISION_BOUNDS_READ =
        CapabilityBits.GROUP_ALLOW_COLLISION_BOUNDS_READ;

    /**
     * Specifies that this Group node allows writing its collision Bounds
     */
    public static final int
    ALLOW_COLLISION_BOUNDS_WRITE =
        CapabilityBits.GROUP_ALLOW_COLLISION_BOUNDS_WRITE;

    /**
     * Replaces the child node at the specified index in this
     * group node's list of children with the specified child.
     * @param child the new child
     * @param index which child to replace.  The <code>index</code> must
     * be a value
     * greater than or equal to 0 and less than <code>numChildren()</code>.
     * @exception CapabilityNotSetException if the appropriate capability is
     * not set and this group node is part of live or compiled scene graph
     * @exception RestrictedAccessException if this group node is part of
     * live or compiled scene graph and the child node being set is not
     * a BranchGroup node
     * @exception MultipleParentException if <code>child</code> has already
     * been added as a child of another group node
     * @exception IndexOutOfBoundsException if <code>index</code> is invalid
     */
    public void setChild(NodeRemote child, int index) throws java.rmi.RemoteException {
	((org.jdesktop.lg3d.sg.Group)wrapped).setChild( (org.jdesktop.lg3d.sg.Node)((Node)child).wrapped, index );
    }
  
    /**
     * Inserts the specified child node in this group node's list of
     * children at the specified index.
     * @param child the new child
     * @param index at which location to insert. The <code>index</code>
     * must be a value
     * greater than or equal to 0 and less than or equal to
     * <code>numChildren()</code>.
     * @exception CapabilityNotSetException if the appropriate capability is
     * not set and this group node is part of live or compiled scene graph
     * @exception RestrictedAccessException if this group node is part of
     * live
     * or compiled scene graph and the child node being inserted is not
     * a BranchGroup node
     * @exception MultipleParentException if <code>child</code> has already
     * been added as a child of another group node.
     * @exception IndexOutOfBoundsException if <code>index</code> is invalid.
     */
    public void insertChild(NodeRemote child, int index) throws java.rmi.RemoteException {
	((org.jdesktop.lg3d.sg.Group)wrapped).insertChild( (org.jdesktop.lg3d.sg.Node)((Node)child).wrapped, index );
    }
  
    /**
     * Removes the child node at the specified index from this group node's
     * list of children.
     * @param index which child to remove.  The <code>index</code>
     * must be a value
     * greater than or equal to 0 and less than <code>numChildren()</code>.
     * @exception CapabilityNotSetException if the appropriate capability is
     * not set and this group node is part of live or compiled scene graph
     * @exception RestrictedAccessException if this group node is part of
     * live or compiled scene graph and the child node being removed is not
     * a BranchGroup node
     * @exception IndexOutOfBoundsException if <code>index</code> is invalid.
     */
    public void removeChild(int index) throws java.rmi.RemoteException {
	((org.jdesktop.lg3d.sg.Group)wrapped).removeChild( index );
    }
  
    /**
     * Retrieves the child node at the specified index in
     * this group node's list of children.
     * @param index which child to return.
     * @return the children at location index.  The <code>index</code>
     * must be a value
     * greater than or equal to 0 and less than <code>numChildren()</code>.
     * @exception CapabilityNotSetException if the appropriate capability is
     * not set and this group node is part of live or compiled scene graph
     * @exception IndexOutOfBoundsException if <code>index</code> is invalid.
     */
    public NodeRemote getChild(int index) throws java.rmi.RemoteException {
	Node ret = (Node)((org.jdesktop.lg3d.sg.Group)wrapped).getChild( index ).getUserData();
        return ret;
    }
  
    /**
     * Returns an Enumeration object of this group node's list of children.
     * @return an Enumeration object of all the children
     * @exception CapabilityNotSetException if the appropriate capability is
     * not set and this group node is part of live or compiled scene graph
     *
     * Enumeration is not serializable so we return the vector. The public
     * wrapper will return Enumeration.
     */
    public Vector getAllChildren() throws java.rmi.RemoteException {
	Enumeration e = ((org.jdesktop.lg3d.sg.Group)wrapped).getAllChildren();
        Vector list = new Vector( ((org.jdesktop.lg3d.sg.Group)wrapped).numChildren() );
        
        while(e.hasMoreElements()) {
            list.add( (SceneGraphObject)((org.jdesktop.lg3d.sg.Node)e.nextElement()).getUserData() );
        }
        
        return list;
    }

    /**
     * Appends the specified child node to this group node's list of children.
     * @param child the child to add to this node's list of children
     * @exception CapabilityNotSetException if the appropriate capability is
     * not set and this group node is part of live or compiled scene graph
     * @exception RestrictedAccessException if this group node is part
     * of live
     * or compiled scene graph and the child node being added is not
     * a BranchGroup node
     * @exception MultipleParentException if <code>child</code> has already
     * been added as a child of another group node.
     */
    public void addChild(NodeRemote child) throws java.rmi.RemoteException {
        if (child==null)
            ((org.jdesktop.lg3d.sg.Group)wrapped).addChild(null);
        else
            ((org.jdesktop.lg3d.sg.Group)wrapped).addChild( (org.jdesktop.lg3d.sg.Node)((Node)SGObjectFactoryImpl.getFactoryImpl().getLocal(child)).wrapped );
    }
  
    /**
     * Moves the specified branch group node from its existing location to
     * the end of this group node's list of children.
     * @param branchGroup the branch group node to move to this node's list
     * of children
     * @exception CapabilityNotSetException if the appropriate capability is
     * not set and this group node is part of live or compiled scene graph
     */
    public void moveTo(BranchGroupRemote branchGroup) throws java.rmi.RemoteException {
	((org.jdesktop.lg3d.sg.Group)wrapped).moveTo( (org.jdesktop.lg3d.sg.BranchGroup)((BranchGroup)branchGroup).wrapped );
    }
  
    /**
     * Returns a count of this group node's children.
     * @return the number of children descendant from this node.
     * @exception CapabilityNotSetException if the appropriate capability is
     * not set and this group node is part of live or compiled scene graph
     */
    public int numChildren() throws java.rmi.RemoteException {
	return ((org.jdesktop.lg3d.sg.Group)wrapped).numChildren();
    }


    /**
     * Retrieves the index of the specified child node in
     * this group node's list of children.
     *
     * @param child the child node to be looked up.
     * @return the index of the specified child node;
     * returns -1 if the object is not in the list.
     * @exception CapabilityNotSetException if the appropriate capability is
     * not set and this group node is part of live or compiled scene graph
     *
     * @since Java 3D 1.3
     */
    public int indexOfChild(NodeRemote child) throws java.rmi.RemoteException {
        return ((org.jdesktop.lg3d.sg.Group)wrapped).indexOfChild( (org.jdesktop.lg3d.sg.Node)((Node)child).wrapped );
    }


    /**
     * Removes the specified child node from this group node's
     * list of children.
     * If the specified object is not in the list, the list is not modified.
     *
     * @param child the child node to be removed.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     * @exception RestrictedAccessException if this group node is part of
     * live or compiled scene graph and the child node being removed is not
     * a BranchGroup node
     *
     * @since Java 3D 1.3
     */
    public void removeChild(NodeRemote child) throws java.rmi.RemoteException {
        ((org.jdesktop.lg3d.sg.Group)wrapped).removeChild( (org.jdesktop.lg3d.sg.Node)((Node)child).wrapped );
    }


    /**
     * Removes all children from this Group node.
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     * @exception RestrictedAccessException if this group node is part of
     * live or compiled scene graph and any of the children being removed are
     * not BranchGroup nodes
     *
     * @since Java 3D 1.3
     */
    public void removeAllChildren() throws java.rmi.RemoteException {
        ((org.jdesktop.lg3d.sg.Group)wrapped).removeAllChildren();
    }

    /**
     * Constructs a Group node with default parameters.  The default
     * values are as follows:
     * <ul>
     * collision bounds : null<br>
     * alternate collision target : false<br>
     * </ul>
     */
    public Group() throws java.rmi.RemoteException {
    }
    
    protected void createWrapped() {
        wrapped = new org.jdesktop.lg3d.sg.Group();
        wrapped.setUserData( this );
    }
}
