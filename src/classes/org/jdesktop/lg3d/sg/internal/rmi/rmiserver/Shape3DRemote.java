/**
 * Project Looking Glass
 *
 * $RCSfile: Shape3DRemote.java,v $
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
 * $Date: 2004-06-23 18:51:22 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.rmi.rmiserver;

import java.util.Enumeration;
import java.util.Vector;


/**
 * The Shape3D leaf node specifies all geometric objects.  It contains
 * a list of one or more Geometry component objects and a single
 * Appearance component object.  The geometry objects define the shape
 * node's geometric data.  The appearance object specifies that
 * object's appearance attributes, including color, material, texture,
 * and so on.
 * <p>
 * The list of geometry objects must all be of the same equivalence
 * class, that is, the same basic type of primitive.  For subclasses
 * of GeometryArray, all point objects are equivalent, all line
 * objects are equivalent, and all polygon objects are equivalent.
 * For other subclasses of Geometry, only objects of the same
 * subclass are equivalent.  The equivalence classes are as follows:
 * <ul>
 * <li>GeometryArray (point): [Indexed]PointArray</li>
 * <li>GeometryArray (line): [Indexed]{LineArray, LineStripArray}</li>
 * <li>GeometryArray (polygon): [Indexed]{TriangleArray, TriangleStripArray,
 * TriangleFanArray, QuadArray}</li>
 * <li>CompressedGeometry</li>
 * <li>Raster</li>
 * <li>Text3D</li>
 * </ul>
 * <p>
 * When Shape3D is used with multiple geometry components, Java 3D may
 * choose to use individual geometry bounds instead of the shape's
 * bounds for region of influence operations, such as lighting.
 * For example, the individual characters of a Text3D shape object
 * may be rendered with a different light set.
 */

public interface Shape3DRemote extends LeafRemote {

    /**
     * Replaces the geometry component at index 0 in this Shape3D node's
     * list of geometry components with the specified geometry component.
     * If there are existing geometry components in the list (besides
     * the one being replaced), the new geometry component must be of
     * the same equivalence class (point, line, polygon, CompressedGeometry,
     * Raster, Text3D) as the others.
     * @param geometry the geometry component to be stored at index 0.
     * @exception IllegalArgumentException if the new geometry
     * component is not of of the same equivalence class as the
     * existing geometry components.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setGeometry(GeometryRemote geometry) throws java.rmi.RemoteException;

    /**
     * Retrieves the geometry component at index 0 from this Shape3D node's
     * list of geometry components.
     * @return the geometry component at index 0.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public GeometryRemote getGeometry() throws java.rmi.RemoteException;


    /**
     * Replaces the geometry component at the specified index in this
     * Shape3D node's list of geometry components with the specified
     * geometry component.
     * If there are existing geometry components in the list (besides
     * the one being replaced), the new geometry component must be of
     * the same equivalence class (point, line, polygon, CompressedGeometry,
     * Raster, Text3D) as the others.
     * @param geometry the geometry component to be stored at the
     * specified index.
     * @param index the index of the geometry component to be replaced.
     * @exception IllegalArgumentException if the new geometry
     * component is not of of the same equivalence class as the
     * existing geometry components.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.2
     */
    public void setGeometry(GeometryRemote geometry, int index) throws java.rmi.RemoteException;


    /**
     * Retrieves the geometry component at the specified index from
     * this Shape3D node's list of geometry components.
     * @param index the index of the geometry component to be returned.
     * @return the geometry component at the specified index.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.2
     */
    public GeometryRemote getGeometry(int index) throws java.rmi.RemoteException;


    /**
     * Inserts the specified geometry component into this Shape3D
     * node's list of geometry components at the specified index.
     * If there are existing geometry components in the list, the new
     * geometry component must be of the same equivalence class
     * (point, line, polygon, CompressedGeometry, Raster, Text3D) as
     * the others.
     * @param geometry the geometry component to be inserted at the
     * specified index.
     * @param index the index at which the geometry component is inserted.
     * @exception IllegalArgumentException if the new geometry
     * component is not of of the same equivalence class as the
     * existing geometry components.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.2
     */
    public void insertGeometry(GeometryRemote geometry, int index) throws java.rmi.RemoteException;


    /**
     * Removes the geometry component at the specified index from
     * this Shape3D node's list of geometry components.
     * @param index the index of the geometry component to be removed.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.2
     */
    public void removeGeometry(int index) throws java.rmi.RemoteException;
  

    /**
     * Returns an enumeration of this Shape3D node's list of geometry
     * components.
     * @return an Enumeration object containing all geometry components in
     * this Shape3D node's list of geometry components.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.2
     */
    public Enumeration getAllGeometries() throws java.rmi.RemoteException;


    /**
     * Appends the specified geometry component to this Shape3D
     * node's list of geometry components.
     * If there are existing geometry components in the list, the new
     * geometry component must be of the same equivalence class
     * (point, line, polygon, CompressedGeometry, Raster, Text3D) as
     * the others.
     * @param geometry the geometry component to be appended.
     * @exception IllegalArgumentException if the new geometry
     * component is not of of the same equivalence class as the
     * existing geometry components.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.2
     */
    public void addGeometry(GeometryRemote geometry) throws java.rmi.RemoteException;
  
    /**
     * Returns the number of geometry components in this Shape3D node's
     * list of geometry components.
     * @return the number of geometry components in this Shape3D node's
     * list of geometry components.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.2
     */
    public int numGeometries() throws java.rmi.RemoteException;


    /**
     * Retrieves the index of the specified geometry component in
     * this Shape3D node's list of geometry components.
     *
     * @param geometry the geometry component to be looked up.
     * @return the index of the specified geometry component;
     * returns -1 if the object is not in the list.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.3
     */
    public int indexOfGeometry(GeometryRemote geometry) throws java.rmi.RemoteException;


    /**
     * Removes the specified geometry component from this
     * Shape3D node's list of geometry components.
     * If the specified object is not in the list, the list is not modified.
     *
     * @param geometry the geometry component to be removed.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.3
     */
    public void removeGeometry(GeometryRemote geometry) throws java.rmi.RemoteException;


    /**
     * Removes all geometry components from this Shape3D node.
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.3
     */
    public void removeAllGeometries() throws java.rmi.RemoteException;


    /**
     * Sets the appearance component of this Shape3D node.  Setting it to null
     * specifies that default values are used for all appearance attributes.
     * @param appearance the new appearance component for this shape node
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setAppearance(AppearanceRemote appearance) throws java.rmi.RemoteException;

    /**
     * Retrieves the appearance component of this shape node.
     * @return the appearance component of this shape node
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
     public AppearanceRemote getAppearance() throws java.rmi.RemoteException;


    /**
     * Checks whether the geometry in this shape node intersects with
     * the specified pickShape.
     *
     * @param path the SceneGraphPath to this shape node
     * @param pickShape the PickShape to be intersected
     *
     * @return true if the pick shape intersects this node; false
     * otherwise.
     *
     * @exception IllegalArgumentException if pickShape is a PickPoint.
     * Java 3D doesn't have spatial information of the surface.
     * Use PickBounds with BoundingSphere and a small radius, instead.
     *
     * @exception CapabilityNotSetException if the Geometry.ALLOW_INTERSECT
     * capability bit is not set in all of the Geometry objects
     * referred to by this shape node.
     */
//    public boolean intersect(SceneGraphPath path, PickShape pickShape) {
//	return intersect(path, pickShape, (float[])null);
//    }

    /**
     * Checks whether the geometry in this shape node intersects with
     * the specified pickRay.
     *
     * @param path the SceneGraphPath to this shape node
     * @param pickRay the PickRay to be intersected
     * @param dist the closest distance of the intersection
     *
     * @return true if the pick shape intersects this node; false
     * otherwise.  If true, dist contains the closest distance of
     * intersection.
     *
     * @exception CapabilityNotSetException if the Geometry.ALLOW_INTERSECT
     * capability bit is not set in all of the Geometry objects
     * referred to by this shape node.
     */
//    public boolean intersect(SceneGraphPath path,
//			     PickRay pickRay,
//			     float[] dist);

    /**
     * Checks whether the geometry in this shape node intersects with
     * the specified pickShape.
     *
     * @param path the SceneGraphPath to this shape node
     * @param pickShape the PickShape to be intersected
     * @param dist the closest distance of the intersection
     *
     * @return true if the pick shape intersects this node; false
     * otherwise.  If true, dist contains the closest distance of
     * intersection.
     *
     * @exception IllegalArgumentException if pickShape is a PickPoint.
     * Java 3D doesn't have spatial information of the surface.
     * Use PickBounds with BoundingSphere and a small radius, instead.
     *
     * @exception CapabilityNotSetException if the Geometry.ALLOW_INTERSECT
     * capability bit is not set in all of the Geometry objects
     * referred to by this shape node.
     *
     * @since Java 3D 1.3
     */
//    public boolean intersect(SceneGraphPath path,
//			     PickShape pickShape,
//			     float[] dist);

    /**
     * See parent class for the documentation on getBounds().
     */
    //public Bounds getBounds();
    

}
