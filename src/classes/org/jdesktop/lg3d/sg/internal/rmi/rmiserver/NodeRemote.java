/**
 * Project Looking Glass
 *
 * $RCSfile: NodeRemote.java,v $
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
 * $Date: 2004-06-23 18:51:18 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.rmi.rmiserver;

import org.jdesktop.lg3d.sg.*;
import java.awt.AWTEvent;
import java.awt.event.*;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.lang.reflect.Constructor;

/**
 * The Node class provides an abstract class for all Group and Leaf Nodes.
 * It provides a common framework for constructing a Java 3D scene graph,
 * specifically bounding volumes.
 * <p>
 * NOTE: Applications should <i>not</i> extend this class directly.
 * 
 * The Node class supports the ability to add various types of 
 * AWT listeners to a Node instance. Specifically, the following types
 * of listeners may be added:
 *
 * <UL>
 * <LI>Focus
 * <LI>Key
 * <LI>Mouse
 * <LI>MouseMotion
 * <LI>MouseWheel
 * 
 * Whenever a mouse event occurs when the cursor is over an instance of
 * Node (or its subclasses), if that node has had any listeners for that
 * mouse event type added to it, the appropriate methods of each of these 
 * listeners are called. 
 * 
 * The client can specify a particular node to be the keyboard focus
 * node.  In order to specify this, the client calls the following
 * new Canvas3D method:
 * 
 *     public void setKeyboardFocusNode (Node n)
 * 
 * An argument of null specifies that no node is the keyboard focus node.
 * For more information on this method, refer to this method in the Canvas3D
 * class.
 *
 * In order to configure a node so that its node listeners will be called,
 * the following settings must to be performed:
 * 
 *     1. A Shape3D node must have the Shape3D.ALLOW_GEOMETRY_READ capability 
 *        set. A Morph node must have the Morph.ALLOW_GEOMETRY_READ capability 
 *        set.
 * 
 *     2. All geometries owned by the node must have the following capabilities
 *        set:
 * 
 *         Geometry.ALLOW_INTERSECT 
 * 	   Geometry.ALLOW_FORMAT_READ
 * 	   Geometry.ALLOW_COUNT_READ
 * 	   Geometry.ALLOW_COORDINATE_READ 
 * 
 * If an AWT event is received by a node and there are no applicable listeners
 * defined for that event type, the event is propogated to the node's parent.
 * The event is propogated upward through the scene graph until a node is found
 * with one or more applicable listeners for that event type. The appropriate 
 * methods of each of these listeners is then invoked If the event reaches 
 * without finding an appropriate listener, the event is discarded.
 *
 * When a listener method is called, and the argument event is e, e.getSource()
 * returns the node to which the event occurred.
 *
 * Performance Note: picking is used internally within this class in order
 * to implement Node Listeners. By default, the "pickable" attribute is 
 * enabled for all nodes. If there is a scene graph node that you don't
 * intend to register listeners with, or if you don't intend to perform
 * any explicit picking operations through the API, it is more efficient 
 * to disable picking for this node.
 */

public interface NodeRemote extends SceneGraphObjectRemote {

    /**
     * Specifies that this Node will be reported in the pick 
     * SceneGraphPath if a pick occurs.  This capability is only 
     * specifiable for Group nodes; it is ignored for leaf nodes.
     * The default for Group nodes is false.  All interior nodes not 
     * needed for uniqueness in a SceneGraphPath that don't have 
     * ENABLE_PICK_REPORTING set to true will not be reported in the 
     * SceneGraphPath.
     * @see SceneGraphPath
     */
    public static final int
    ENABLE_PICK_REPORTING = org.jdesktop.lg3d.sg.Node.ENABLE_PICK_REPORTING;

    /**
     * Specifies that this Node will be reported in the collision 
     * SceneGraphPath if a collision occurs.  This capability is only
     * specifiable for Group nodes; it is ignored for leaf nodes.
     * The default for Group nodes is false.  All interior nodes not 
     * needed for uniqueness in a SceneGraphPath that don't have
     * ENABLE_COLLISION_REPORTING set to true will not be reported
     * in the SceneGraphPath.
     * @see SceneGraphPath
     */  
    public static final int
    ENABLE_COLLISION_REPORTING = org.jdesktop.lg3d.sg.Node.ENABLE_COLLISION_REPORTING;

    /**
     * Specifies that this Node allows read access to its bounds
     * information.
     */
    public static final int
    ALLOW_BOUNDS_READ = org.jdesktop.lg3d.sg.Node.ALLOW_BOUNDS_READ;

    /**
     * Specifies that this Node allows write access to its bounds
     * information.
     */
    public static final int
    ALLOW_BOUNDS_WRITE = org.jdesktop.lg3d.sg.Node.ALLOW_BOUNDS_WRITE;

    /**
     * Specifies that this Node allows reading its pickability state.
     */
    public static final int
    ALLOW_PICKABLE_READ = org.jdesktop.lg3d.sg.Node.ALLOW_PICKABLE_READ;

    /**
     * Specifies that this Node allows write access its pickability state.
     */
    public static final int
    ALLOW_PICKABLE_WRITE = org.jdesktop.lg3d.sg.Node.ALLOW_PICKABLE_WRITE;

    /**
     * Specifies that this Node allows reading its collidability state.
     */
    public static final int
    ALLOW_COLLIDABLE_READ = org.jdesktop.lg3d.sg.Node.ALLOW_COLLIDABLE_READ;

    /**
     * Specifies that this Node allows write access its collidability state.
     */
    public static final int
    ALLOW_COLLIDABLE_WRITE = org.jdesktop.lg3d.sg.Node.ALLOW_COLLIDABLE_WRITE;

    /**
     * Specifies that this Node allows read access to its bounds
     * auto compute information.
     */
    public static final int
    ALLOW_AUTO_COMPUTE_BOUNDS_READ = org.jdesktop.lg3d.sg.Node.ALLOW_AUTO_COMPUTE_BOUNDS_READ;

    /**
     * Specifies that this Node allows write access to its bounds
     * auto compute information.
     */
    public static final int
    ALLOW_AUTO_COMPUTE_BOUNDS_WRITE = org.jdesktop.lg3d.sg.Node.ALLOW_AUTO_COMPUTE_BOUNDS_WRITE;

    /**
     * Specifies that this Node allows read access to its local
     * coordinates to virtual world (Vworld) coordinates transform.
     */
    public static final int
    ALLOW_LOCAL_TO_VWORLD_READ = org.jdesktop.lg3d.sg.Node.ALLOW_LOCAL_TO_VWORLD_READ;
    
    /**
     * Retrieves the parent of this Node.  This method is only valid
     * during the construction of the scene graph.
     * @return the parent of this node, or null if this node has no parent
     * @exception RestrictedAccessException if this object is part of live
     * or compiled scene graph
     */
    public NodeRemote getParent() throws java.rmi.RemoteException;

    /**
     * Sets the geometric bounds of a node.
     * @param bounds the bounding object for a node
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setBounds(BoundsRemote bounds) throws java.rmi.RemoteException;

    /**
     * Returns the bounding object of a node.
     * @return the node's bounding object
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     * @exception SceneGraphCycleException if there is a cycle in the
     * scene graph
     */
    public BoundsRemote getBounds() throws java.rmi.RemoteException;


    /**
     * Turns the automatic calcuation of geometric bounds of a node on/off.
     * @param autoCompute indicates if the node's bounding object is
     * automatically computed.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setBoundsAutoCompute(boolean autoCompute) throws java.rmi.RemoteException;

    /**
     * Gets the value indicating if the automatic calcuation of geometric bounds of a node is on/off.
     * @return the node's auto compute flag for the geometric bounding object
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public boolean getBoundsAutoCompute() throws java.rmi.RemoteException;

    /**
     * Retrieves the local coordinates to virtual world coordinates
     * transform for this node in the scene graph.  This is the composite
     * of all transforms in the scene graph from the root down to
     * <code>this</code> node.  It is only valid
     * for nodes that are part of a live scene graph.
     * @param t the object that will receive the local coordinates to
     * Vworld coordinates transform.
     * @exception RestrictedAccessException if the node is <em>not</em>
     * part of a live scene graph
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this node is part of live scene graph
     * @exception IllegalSharingException if the node is a descendant
     * of a SharedGroup node.
     */
    public void getLocalToVworld(Transform3DRemote t) throws java.rmi.RemoteException;

    /**
     * Retrieves the local coordinates to virtual world coordinates
     * transform for the particular path in the scene graph ending with
     * this node.  This is the composite
     * of all transforms in the scene graph from the root down to
     * <code>this</code> node via the specified Link nodes.  It is
     * only valid for nodes that are part of a live scene graph.
     * @param path the specific path from the node to the Locale
     * @param t the object that will receive the local coordinates to
     * Vworld coordinates transform.
     * @exception RestrictedAccessException if the node is <em>not</em>
     * part of a live scene graph
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this node is part of live scene graph
     * @exception IllegalArgumentException if the specified path does
     * not contain a valid Locale, or if the last node in the path is
     * different from this node
     */
    public void getLocalToVworld(SceneGraphPathRemote path, Transform3DRemote t) throws java.rmi.RemoteException;

    /**
     * When set to <code>true</code> this <code>Node</code> can be Picked.
     * Setting to false indicates that this node and it's children
     * are ALL unpickable.
     *
     * @param pickable Indicates if this node should be pickable or not
     */
    public void setPickable( boolean pickable ) throws java.rmi.RemoteException;

    /**
     * Returns true if this <code>Node</code> is pickable,
     * false if it is not pickable.
     */
    public boolean getPickable() throws java.rmi.RemoteException;



}

