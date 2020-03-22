/**
 * Project Looking Glass
 *
 * $RCSfile: Shape3D.java,v $
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
 * $Revision: 1.3 $
 * $Date: 2005-04-14 23:04:16 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg;

import java.util.Enumeration;
import java.util.Vector;

import org.jdesktop.lg3d.sg.internal.wrapper.Shape3DWrapper;
import org.jdesktop.lg3d.sg.internal.wrapper.GeometryWrapper;
import org.jdesktop.lg3d.sg.internal.wrapper.AppearanceWrapper;


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

public class Shape3D extends Leaf {

    /**
     * Id used in the compile optimization to determine
     * how to get to the geometry in the case of read
     * or picking ..
     */
    int id;

    /**
     * Specifies that the node allows read access to its geometry information.
     */
    public static final int
    ALLOW_GEOMETRY_READ = CapabilityBits.SHAPE3D_ALLOW_GEOMETRY_READ;

    /**
     * Specifies that the node allows write access to its geometry information.
     */
    public static final int
    ALLOW_GEOMETRY_WRITE = CapabilityBits.SHAPE3D_ALLOW_GEOMETRY_WRITE;

    /**
     * Specifies that the node allows read access to its appearance
     * information.
     */
    public static final int
    ALLOW_APPEARANCE_READ = CapabilityBits.SHAPE3D_ALLOW_APPEARANCE_READ;

    /**
     * Specifies that the node allows write access to its appearance
     * information.
     */
    public static final int
    ALLOW_APPEARANCE_WRITE = CapabilityBits.SHAPE3D_ALLOW_APPEARANCE_WRITE;

    /**
     * Specifies that the node allows reading its collision Bounds
     */
    public static final int
    ALLOW_COLLISION_BOUNDS_READ = CapabilityBits.SHAPE3D_ALLOW_COLLISION_BOUNDS_READ;

    /**
     * Specifies the node allows writing its collision Bounds
     */
    public static final int
    ALLOW_COLLISION_BOUNDS_WRITE = CapabilityBits.SHAPE3D_ALLOW_COLLISION_BOUNDS_WRITE;

    /**
     * Specifies that this node allows reading its appearance override
     * enable flag.
     *
     * @since Java 3D 1.2
     */
    public static final int ALLOW_APPEARANCE_OVERRIDE_READ =
	CapabilityBits.SHAPE3D_ALLOW_APPEARANCE_OVERRIDE_READ;

    /**
     * Specifies that this node allows writing its appearance override
     * enable flag.
     *
     * @since Java 3D 1.2
     */
    public static final int ALLOW_APPEARANCE_OVERRIDE_WRITE =
	CapabilityBits.SHAPE3D_ALLOW_APPEARANCE_OVERRIDE_WRITE;

    /**
     * Constructs a Shape3D node with default parameters.  The default
     * values are as follows:
     * <ul>
     * appearance : null<br>
     * geometry : { null }<br>
     * collision bounds : null<br>
     * appearance override enable : false<br>
     * </ul>
     * The list of geometry components is initialized with a null
     * geometry component as the single element with an index of 0.
     * A null geometry component specifies
     * that no geometry is drawn. A null appearance component specifies
     * that default values are used for all appearance attributes.
     */
    public Shape3D() {
    }
    
    protected void createWrapped() {
        wrapped = instantiate( SceneGraphSetup.getWrapperPackage()+"Shape3D");
    }

    /**
     * Constructs and initializes a Shape3D node with the specified
     * geometry component and a null appearance component.
     * The list of geometry components is initialized with the
     * specified geometry component as the single element with an
     * index of 0.
     * A null appearance component specifies that default values are
     * used for all appearance attributes.
     * @param geometry the geometry component with which to initialize
     * this shape node.
     */
    public Shape3D(Geometry geometry) {
        setGeometry(geometry);
    }

    /**
     * Constructs and initializes a Shape3D node with the specified
     * geometry and appearance components.
     * The list of geometry components is initialized with the
     * specified geometry component as the single element with an
     * index of 0.
     * @param geometry the geometry component with which to initialize
     * this shape node
     * @param appearance the appearance component of the shape node
     */
    public Shape3D(Geometry geometry, Appearance appearance) {
        setGeometry(geometry);
        setAppearance(appearance);
    }


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
    public void setGeometry(Geometry geometry) {
	((Shape3DWrapper)wrapped).setGeometry((GeometryWrapper)geometry.wrapped);
    }

    /**
     * Retrieves the geometry component at index 0 from this Shape3D node's
     * list of geometry components.
     * @return the geometry component at index 0.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public Geometry getGeometry() {
	return (Geometry)((Shape3DWrapper)wrapped).getGeometry().getUserData();
    }


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
    public void setGeometry(Geometry geometry, int index) {
	((Shape3DWrapper)wrapped).setGeometry((GeometryWrapper)geometry.wrapped, index);
    }


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
    public Geometry getGeometry(int index) {
	return (Geometry)((Shape3DWrapper)wrapped).getGeometry(index).getUserData();
    }


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
    public void insertGeometry(Geometry geometry, int index) {
	((Shape3DWrapper)wrapped).insertGeometry( (GeometryWrapper)geometry.wrapped, index);
     }


    /**
     * Removes the geometry component at the specified index from
     * this Shape3D node's list of geometry components.
     * @param index the index of the geometry component to be removed.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.2
     */
    public void removeGeometry(int index) {
	((Shape3DWrapper)wrapped).removeGeometry(index);
    }
  

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
    public Enumeration getAllGeometries() {
        Enumeration e = ((Shape3DWrapper)wrapped).getAllGeometries();
        Vector list = new Vector();
        
        while( e.hasMoreElements() )
            list.add( ((GeometryWrapper)e.nextElement()).getUserData() );
        
	return list.elements();
    }


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
    public void addGeometry(Geometry geometry) {
	((Shape3DWrapper)wrapped).addGeometry((GeometryWrapper)geometry.wrapped);
    }

  
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
    public int numGeometries() {
	return ((Shape3DWrapper)wrapped).numGeometries();
    }


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
    public int indexOfGeometry(Geometry geometry) {
	return ((Shape3DWrapper)wrapped).indexOfGeometry(((GeometryWrapper)geometry.wrapped));
    }


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
    public void removeGeometry(Geometry geometry) {
	((Shape3DWrapper)wrapped).removeGeometry(((GeometryWrapper)geometry.wrapped));
    }


    /**
     * Removes all geometry components from this Shape3D node.
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.3
     */
    public void removeAllGeometries() {
	((Shape3DWrapper)wrapped).removeAllGeometries();
    }


    /**
     * Sets the appearance component of this Shape3D node.  Setting it to null
     * specifies that default values are used for all appearance attributes.
     * @param appearance the new appearance component for this shape node
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setAppearance(Appearance appearance) {
	((Shape3DWrapper)wrapped).setAppearance((AppearanceWrapper)appearance.wrapped);
    }

    /**
     * Retrieves the appearance component of this shape node.
     * @return the appearance component of this shape node
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
     public Appearance getAppearance() {
         AppearanceWrapper app = ((Shape3DWrapper)wrapped).getAppearance();
         if (app==null)
             return null;
	 return (Appearance)app.getUserData();
    }


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
    public boolean intersect(SceneGraphPath path, PickShape pickShape) {
	return intersect(path, pickShape, (float[])null);
    }

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
    public boolean intersect(SceneGraphPath path,
			     PickRay pickRay,
			     float[] dist) {

//        double[] tmp = new double[dist.length];
//        for(int i=0; i<dist.length; i++)
//            tmp[i] = (float)dist[i];
//                                 
//	boolean ret = ((Shape3DWrapper)wrapped).intersect(
//                        (SceneGraphPathWrapper)path.wrapped, 
//                        (PickRayWrapper)pickRay.wrapped, 
//                        tmp);
//        
//        for(int i=0; i<dist.length; i++)
//            dist[i] = new Float(tmp[i]).floatValue();
//
//        return ret;
        throw new RuntimeException("Not Implemented");
    }

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
    public boolean intersect(SceneGraphPath path,
			     PickShape pickShape,
			     float[] dist) {

//        double[] tmp = new double[dist.length];
//        for(int i=0; i<dist.length; i++)
//            tmp[i] = (float)dist[i];
//
//        boolean ret = ((Shape3DWrapper)wrapped).intersect(
//                        (javax.media.j3d.SceneGraphPath)path.wrapped, 
//                        (javax.media.j3d.PickShape)pickShape.wrapped, 
//                        tmp);
//        
//        for(int i=0; i<dist.length; i++)
//            dist[i] = new Float(tmp[i]).floatValue();
//
//        return ret;
                                 throw new RuntimeException("Not Implemented");
    }

}
