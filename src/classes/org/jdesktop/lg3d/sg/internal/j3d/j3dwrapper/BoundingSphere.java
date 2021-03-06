/**
 * Project Looking Glass
 *
 * $RCSfile: BoundingSphere.java,v $
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
 * $Date: 2004-06-23 18:50:34 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.j3d.j3dwrapper;

import javax.vecmath.Point3f;
import javax.vecmath.Point3d;

import org.jdesktop.lg3d.sg.internal.wrapper.BoundingSphereWrapper;
import org.jdesktop.lg3d.sg.internal.wrapper.BoundsWrapper;
import org.jdesktop.lg3d.sg.internal.wrapper.Transform3DWrapper;

/**
 * This class defines a spherical bounding region which is defined by a
 * center point and a radius.
 */

public class BoundingSphere extends Bounds implements BoundingSphereWrapper {
	
    /**
     * Constructs and initializes a BoundingSphere from a center and radius.
     * @param center the center of the bounding sphere
     * @param radius the radius of the bounding sphere
     */
    public BoundingSphere(Point3f center, float radius) {
        setCenter( center );
        setRadius( radius );
    }
    
    public BoundingSphere() {
    }
    
    protected void createWrapped() {
        wrapped = new javax.media.j3d.BoundingSphere();
        reverseLookup.put( wrapped, this );
    }
 
    /**
     * Returns the radius of this bounding sphere as a double.
     * @return the radius of the bounding sphere
     */
    public float getRadius() {
	return (float)((javax.media.j3d.BoundingSphere)wrapped).getRadius();
    }

    /**
     * Returns the position of this bounding sphere as a point.
     * @param center a Point to receive the center of the bounding sphere
     */
    public void getCenter(Point3f center) {
        Point3d tmp = new Point3d();
	((javax.media.j3d.BoundingSphere)wrapped).getCenter( tmp );
        center.x = (float)tmp.x;
        center.y = (float)tmp.y;
        center.z = (float)tmp.z;
    }
    
    /**
     * Sets the value of this BoundingSphere.
     * @param boundsObject another bounds object
     */
    public void set(BoundsWrapper  boundsObject){
	((javax.media.j3d.BoundingSphere)wrapped).set( ((Bounds)boundsObject).wrapped );
    }
    
    /**
     * Creates a copy of the bounding sphere.
     * @return a BoundingSphere 
     */
    public Object clone() {
        throw new RuntimeException("Not Implemented");
	//return new BoundingSphere(this.center, this.radius);
    }


    /**
     * Indicates whether the specified <code>bounds</code> object is
     * equal to this BoundingSphere object.  They are equal if the
     * specified <code>bounds</code> object is an instance of
     * BoundingSphere and all of the data
     * members of <code>bounds</code> are equal to the corresponding
     * data members in this BoundingSphere.
     * @param bounds the object with which the comparison is made.
     * @return true if this BoundingSphere is equal to <code>bounds</code>;
     * otherwise false
     *
     * @since Java 3D 1.2
     */
    public boolean equals(Object bounds) {
	return ((javax.media.j3d.BoundingSphere)wrapped).equals( bounds );
    }


    /**
     * Returns a hash code value for this BoundingSphere object
     * based on the data values in this object.  Two different
     * BoundingSphere objects with identical data values (i.e.,
     * BoundingSphere.equals returns true) will return the same hash
     * code value.  Two BoundingSphere objects with different data
     * members may return the same hash code value, although this is
     * not likely.
     * @return a hash code value for this BoundingSphere object.
     *
     * @since Java 3D 1.2
     */
    public int hashCode() {
	return ((javax.media.j3d.BoundingSphere)wrapped).hashCode();
    }


    /** 
     * Combines this bounding sphere with a bounding object so that the
     * resulting bounding sphere encloses the original bounding sphere and the
     * given bounds object.
     * @param boundsObject another bounds object
     */
    public void combine(BoundsWrapper boundsObject) {
        ((javax.media.j3d.BoundingSphere)wrapped).combine( ((Bounds)boundsObject).wrapped );
    }
    
    /** 
     * Combines this bounding sphere with an array of bounding objects so that the
     * resulting bounding sphere encloses the original bounding sphere and the
     * given array of bounds object.
     * @param boundsObjects an array of bounds objects
     */
    public void combine(BoundsWrapper[] boundsObjects) {
        throw new RuntimeException("Not Implemented");
    }

    /**
     * Modifies the bounding sphere so that it bounds the volume
     * generated by transforming the given bounding object.
     * @param boundsObject the bounding object to be transformed 
     * @param matrix a transformation matrix
     */
    public void transform( BoundsWrapper boundsObject, Transform3DWrapper matrix) {
	((javax.media.j3d.BoundingSphere)wrapped).transform( ((Bounds)boundsObject).wrapped, ((Transform3D)matrix).wrapped );
    }

    /** 
     * Transforms this bounding sphere by the given matrix.
     */
    public void transform( Transform3DWrapper trans) {
	((javax.media.j3d.BoundingSphere)wrapped).transform( ((Transform3D)trans).wrapped );
    }


    /** 
     * Test for intersection with a ray.
     * @param origin the starting point of the ray
     * @param direction the direction of the ray
     * @return true or false indicating if an intersection occured
     */ 
//    public boolean intersect(Point3d origin, Vector3d direction ) {
//
//	return ((javax.media.j3d.BoundingSphere)wrapped).intersect( origin, direction );
//    }

    
    /**
     * Test for intersection with a point.
     * @param point a point defining a position in 3-space
     * @return true or false indicating if an intersection occured
     */ 
//    public boolean intersect(Point3d point ) {
//	return ((javax.media.j3d.BoundingSphere)wrapped).intersect( point );
//    }

    /**
     * Test for intersection with a point.
     * @param point a point defining a position in 3-space
     * @return true or false indicating if an intersection occured
     */ 
    public boolean intersect(Point3f point ) {
	return ((javax.media.j3d.BoundingSphere)wrapped).intersect( new Point3d(point) );
      
    }

    /**
     * Tests whether the bounding sphere is empty.  A bounding sphere is
     * empty if it is null (either by construction or as the result of
     * a null intersection) or if its volume is negative.  A bounding sphere
     * with a volume of zero is <i>not</i> empty.
     * @return true if the bounding sphere is empty;
     * otherwise, it returns false
     */
    public boolean isEmpty() {
	return ((javax.media.j3d.BoundingSphere)wrapped).isEmpty();
    }

    /**
     * Test for intersection with another bounds object.
     * @param boundsObject another bounds object
     * @return true or false indicating if an intersection occured
     */ 
    public boolean intersect(BoundsWrapper boundsObject) {
	return ((javax.media.j3d.BoundingSphere)wrapped).intersect( ((Bounds)boundsObject).wrapped );
    }

    /**
     * Test for intersection with another bounds object.
     * @param boundsObjects an array of bounding objects
     * @return true or false indicating if an intersection occured
     */ 
    public boolean intersect(BoundsWrapper[] boundsObjects) {
	throw new RuntimeException("Not Implemented");
    }    

    /** 
     * Finds closest bounding object that intersects this bounding sphere.
     * @param boundsObjects an array of  bounds objects 
     * @return closest bounding object 
     */
    public BoundsWrapper closestIntersection( BoundsWrapper[] boundsObjects) {

	throw new RuntimeException("Not Implemented");

    }



    /**
     * Returns a string representation of this class.
     */
    public String toString() {
	return ((javax.media.j3d.BoundingSphere)wrapped).toString();
    
    }
    
    public void setCenter(Point3f center) {
	((javax.media.j3d.BoundingSphere)wrapped).setCenter(new Point3d(center));
    }
    
    public void setRadius(float radius) {
	((javax.media.j3d.BoundingSphere)wrapped).setRadius(radius);
    }
    
}




