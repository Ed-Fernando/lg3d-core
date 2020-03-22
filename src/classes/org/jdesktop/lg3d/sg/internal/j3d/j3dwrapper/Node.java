/**
 * Project Looking Glass
 *
 * $RCSfile: Node.java,v $
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
 * $Revision: 1.6 $
 * $Date: 2006-06-30 17:55:07 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.j3d.j3dwrapper;

import java.lang.reflect.Field;
import org.jdesktop.lg3d.sg.internal.wrapper.*;

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

public abstract class Node extends SceneGraphObject implements NodeWrapper {

    /**
     * Retrieves the parent of this Node.
     * @return the parent of this node, or null if this node has no parent
     * @exception CapabilityNotSetException - if appropriate capability is not set and this object is part of live or compiled scene graph
     */
    public NodeWrapper getParent() {
        javax.media.j3d.Node parent = ((javax.media.j3d.Node)wrapped).getParent();
        if (parent==null)
            return null;
        
        // Some java3d implementations of the lg3d nodes contain internal
        // java3d nodes with no corresponding lg3d node (eg children in J3dComponent3d)
        // This loop ensures we get to a valid lg3d parent.
        NodeWrapper parentWrapper = (NodeWrapper)parent.getUserData();
        while(parentWrapper==null) {
            parent = parent.getParent();
            if (parent==null)
                return null;
            parentWrapper = (NodeWrapper)parent.getUserData();
        }
        
        return parentWrapper;
    }

    /**
     * Sets the geometric bounds of a node.
     * @param bounds the bounding object for a node
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setBounds(BoundsWrapper bounds) {
	((javax.media.j3d.Node)wrapped).setBounds( ((Bounds)bounds).wrapped );
    }

    /**
     * Returns the bounding object of a node.
     * @return the node's bounding object
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     * @exception SceneGraphCycleException if there is a cycle in the
     * scene graph
     */
    public BoundsWrapper getBounds() {
        Bounds ret;
        
        javax.media.j3d.Bounds j3dB = ((javax.media.j3d.Node)wrapped).getBounds();
        if (j3dB==null)
            return null;
        
        ret = Bounds.getLGBounds(j3dB);
        
        // Check if we have a record of this bounds object, if not create an
        // lg3d object to wrap it.
        if (ret!=null)
            return ret;
        else if (j3dB instanceof javax.media.j3d.BoundingSphere) {
            org.jdesktop.lg3d.sg.BoundingSphere bSphere = new org.jdesktop.lg3d.sg.BoundingSphere();
            ret = (Bounds) getWrapped(bSphere);
            ret.changeWrapped(j3dB);
        } else if (j3dB instanceof javax.media.j3d.BoundingBox) {
            org.jdesktop.lg3d.sg.BoundingBox bBox = new org.jdesktop.lg3d.sg.BoundingBox();
            ret = (Bounds) getWrapped(bBox);
            ret.changeWrapped(j3dB);
        } else
            throw new RuntimeException("Unsupported bounds type "+j3dB.getClass().getName());
        
        return ret;
    }

    /** Returns the private wrapped object from bounds
     */
    private BoundsWrapper getWrapped(org.jdesktop.lg3d.sg.Bounds bounds) {
        try {
            Field w = bounds.getClass().getSuperclass().getDeclaredField("wrapped");
            w.setAccessible(true);
            return (BoundsWrapper)w.get(bounds);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Turns the automatic calcuation of geometric bounds of a node on/off.
     * @param autoCompute indicates if the node's bounding object is
     * automatically computed.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setBoundsAutoCompute(boolean autoCompute) {
	((javax.media.j3d.Node)wrapped).setBoundsAutoCompute(autoCompute);
    }

    /**
     * Gets the value indicating if the automatic calcuation of geometric bounds of a node is on/off.
     * @return the node's auto compute flag for the geometric bounding object
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public boolean getBoundsAutoCompute() {
	return ((javax.media.j3d.Node)wrapped).getBoundsAutoCompute();
    }

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
    public void getLocalToVworld(Transform3DWrapper t) {
	((javax.media.j3d.Node)wrapped).getLocalToVworld(((Transform3D)t).wrapped);
    }

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
    public void getLocalToVworld(SceneGraphPathWrapper path, Transform3DWrapper t) {
	((javax.media.j3d.Node)wrapped).getLocalToVworld(((SceneGraphPath)path).wrapped,((Transform3D)t).wrapped);
    }

    /**
     * When set to <code>true</code> this <code>Node</code> can be Picked.
     * Setting to false indicates that this node and it's children
     * are ALL unpickable.
     *
     * @param pickable Indicates if this node should be pickable or not
     */
    public void setPickable( boolean pickable ) {
	((javax.media.j3d.Node)wrapped).setPickable( pickable );
    }

    /**
     * Returns true if this <code>Node</code> is pickable,
     * false if it is not pickable.
     */
    public boolean getPickable() {
	return ((javax.media.j3d.Node)wrapped).getPickable();
    }

}

