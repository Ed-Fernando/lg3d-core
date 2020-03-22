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
 * $Date: 2004-06-23 18:50:51 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.rmi.rmiclient;

import java.util.Vector;
import java.util.Enumeration;

import org.jdesktop.lg3d.sg.internal.rmi.rmiserver.GroupRemote;
import org.jdesktop.lg3d.sg.internal.rmi.rmiserver.BranchGroupRemote;
import org.jdesktop.lg3d.sg.internal.rmi.rmiserver.NodeRemote;
import org.jdesktop.lg3d.sg.internal.rmi.rmiserver.SceneGraphObjectRemote;

import org.jdesktop.lg3d.sg.internal.wrapper.*;

/**
 * The Group node object is a general-purpose grouping node. Group
 * nodes have exactly one parent and an arbitrary number of children
 * that are rendered in an unspecified order (or in parallel).  Null
 * children are allowed; no operation is performed on a null child
 * node.  Operations on Group node objects include adding, removing,
 * and enumerating the children of the Group node. The subclasses of
 * Group node add additional semantics.
 */

public class Group extends Node implements GroupWrapper {
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
    public void setChild(NodeWrapper child, int index) {
        assert( child!=null );
        try {
            ((GroupRemote)remote).setChild( (NodeRemote)((Node)child).remote, index );
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
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
    public void insertChild(NodeWrapper child, int index) {
        assert( child!=null );
        try {
            ((GroupRemote)remote).insertChild( (NodeRemote)((Node)child).remote, index );
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
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
    public void removeChild(int index) {
       try {
            ((GroupRemote)remote).removeChild( index );
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
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
    public NodeWrapper getChild(int index) {
        try {
            return (Node)getLocal(((GroupRemote)remote).getChild( index ));
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }
  
    /**
     * Returns an Enumeration object of this group node's list of children.
     * @return an Enumeration object of all the children
     * @exception CapabilityNotSetException if the appropriate capability is
     * not set and this group node is part of live or compiled scene graph
     */
    public Enumeration getAllChildren() {
        try {
            Enumeration e = ((GroupRemote)remote).getAllChildren().elements();
            Vector list = new Vector();

            while(e.hasMoreElements()) {
                list.add( ((Node)getLocal( (SceneGraphObjectRemote)e.nextElement()) ));
            }
            return list.elements();
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }        
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
    public void addChild(NodeWrapper child) {
        assert( child!=null );
        try {
            ((GroupRemote)remote).addChild( (NodeRemote)((Node)child).remote );
        } catch( java.rmi.RemoteException rex ) {
            rex.printStackTrace();
        }
    }
  
    /**
     * Moves the specified branch group node from its existing location to
     * the end of this group node's list of children.
     * @param branchGroup the branch group node to move to this node's list
     * of children
     * @exception CapabilityNotSetException if the appropriate capability is
     * not set and this group node is part of live or compiled scene graph
     */
    public void moveTo(BranchGroupWrapper branchGroup) {
        assert( branchGroup!=null );
       try {
            ((GroupRemote)remote).moveTo( (BranchGroupRemote)((BranchGroup)branchGroup).remote );
        } catch( java.rmi.RemoteException rex ) {
            rex.printStackTrace();
        }
    }
  
    /**
     * Returns a count of this group node's children.
     * @return the number of children descendant from this node.
     * @exception CapabilityNotSetException if the appropriate capability is
     * not set and this group node is part of live or compiled scene graph
     */
    public int numChildren() {
        try {
            return ((GroupRemote)remote).numChildren();
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
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
    public int indexOfChild(NodeWrapper child) {
        try {
            return ((GroupRemote)remote).indexOfChild( (NodeRemote)((Node)child).remote );
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
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
    public void removeChild(NodeWrapper child) {
        assert( child!=null );
        try {
            ((GroupRemote)remote).removeChild( (NodeRemote)((Node)child).remote );
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
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
    public void removeAllChildren() {
        try {
            ((GroupRemote)remote).removeAllChildren();
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }

    /**
     * Constructs a Group node with default parameters.  The default
     * values are as follows:
     * <ul>
     * collision bounds : null<br>
     * alternate collision target : false<br>
     * </ul>
     */
    public Group() {
    }
    
    protected void createRemote() {
        try {
            remote = SceneGraphSetup.getSGObjectFactory().newGroup();
            setRemote( remote );
        } catch( java.rmi.RemoteException re ) {
            throw new RuntimeException(re);
        }
    }
    
}
