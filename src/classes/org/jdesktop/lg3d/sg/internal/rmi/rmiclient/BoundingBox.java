/**
 * Project Looking Glass
 *
 * $RCSfile: BoundingBox.java,v $
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
 * $Date: 2004-06-23 18:50:48 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.rmi.rmiclient;

import javax.vecmath.*;
import org.jdesktop.lg3d.sg.internal.wrapper.BoundingBoxWrapper;
import org.jdesktop.lg3d.sg.internal.wrapper.BoundsWrapper;
import org.jdesktop.lg3d.sg.internal.wrapper.Transform3DWrapper;
import org.jdesktop.lg3d.sg.internal.rmi.rmiserver.BoundingBoxRemote;

/**
 *  This class defines an axis aligned bounding box which is used for
 *  bounding regions.
 *
 */

public class BoundingBox extends Bounds implements BoundingBoxWrapper {

    /**
     * Constructs and initializes a BoundingBox given min,max in x,y,z.
     * @param lower the "small" corner
     * @param upper the "large" corner
     */
    public BoundingBox(Point3f lower, Point3f upper) {
        setUpper( upper.x, upper.y, upper.z );
        setLower( lower.x, lower.y, lower.z );
    }
    
    /**
     * Constructs and initializes a 2X bounding box about the
     * origin. The lower corner is initialized to (-1.0d, -1.0d, -1.0d)
     * and the opper corner is initialized to (1.0d, 1.0d, 1.0d).
     */
    public BoundingBox() {
    }
    
    protected void createRemote() {
        try {
            remote = SceneGraphSetup.getSGObjectFactory().newBoundingBox();
            reverseLookup.put( remote, this );
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
    }
    
    /**
     * Constructs a BoundingBox from a  bounding object. 
     * @param boundsObject  a bounds object 
     */
    public BoundingBox(Bounds boundsObject) {
	set(boundsObject);
    }

    /**
     * Constructs a BoundingBox from an array of bounding objects. 
     * @param bounds an array of bounding objects
     */
//    public BoundingBox(Bounds[] bounds) {
//        throw new RuntimeException("Not implemented");
//      	//wrapped = new javax.media.j3d.BoundingBox( bounds );
//    }

    /**
     * Gets the lower corner of this bounding box.
     * @param p1 a Point to receive the lower corner of the bounding box
     */
    public void getLower(Point3f p1) {
        try {
            ((BoundingBoxRemote)remote).getLower( p1 );
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
    }
    
     /**
     * Gets the upper corner of this bounding box.
     * @param p1 a Point to receive the upper corner of the bounding box
     */
    public void getUpper(Point3f p1) {
        try {
            ((BoundingBoxRemote)remote).getUpper( p1 );
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
    }

    /**
     * Sets the lower corner of this bounding box.
     * @param xmin minimum x value of boundining box
     * @param ymin minimum y value of boundining box
     * @param zmin minimum z value of boundining box
     */
    public void setLower(float xmin, float ymin, float zmin ) {
        try {
            ((BoundingBoxRemote)remote).setLower( xmin, ymin, zmin );
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
    }
    

    /**
     * Sets the upper corner of this bounding box.
     * @param xmax max x value of boundining box
     * @param ymax max y value of boundining box
     * @param zmax max z value of boundining box
     */
    public void setUpper(float xmax, float ymax, float zmax ) {
        try {
            ((BoundingBoxRemote)remote).setLower( xmax, ymax, zmax );
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
    }


    /**
     * Sets the the value of this BoundingBox
     * @param boundsObject another bounds object
     */
    public void set(BoundsWrapper  boundsObject) {
        try {
            ((BoundingBoxRemote)remote).set( ((Bounds)boundsObject).remote );
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
    }

    /** 
     * Combines this bounding box with a bounding object   so that the
     * resulting bounding box encloses the original bounding box and the
     * specified bounds object.
     * @param boundsObject another bounds object
     */
    public void combine(BoundsWrapper boundsObject) {
        try {
            ((BoundingBoxRemote)remote).combine( ((Bounds)boundsObject).remote );
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
    }

    /** 
     * Combines this bounding box with an array of bounding objects  
     * so that the resulting bounding box encloses the original bounding 
     * box and the array of bounding objects.
     * @param boundsObjects an array of bounds objects
     */
    public void combine(BoundsWrapper[] bounds) {
        throw new RuntimeException( "Not implented" );
       //((javax.media.j3d.BoundingBox)wrapped).combine( 
    }

    /**
     * Modifies the bounding box so that it bounds the volume
     * generated by transforming the given bounding object.
     * @param boundsObject the bounding object to be transformed 
     * @param matrix a transformation matrix
     */
    public void transform( BoundsWrapper boundsObject, Transform3DWrapper matrix) {
        try {
            ((BoundingBoxRemote)remote).transform( ((Bounds)boundsObject).remote, ((Transform3D)matrix).remote );
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
    }

    /** 
     * Transforms this bounding box by the given matrix.
     * @param matrix a transformation matrix
     */
    public void transform(Transform3DWrapper matrix) {
        try {
            ((BoundingBoxRemote)remote).transform( ((Transform3D)matrix).remote );
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
    }
    
    /** 
     * Test for intersection with a ray.
     * @param origin the starting point of the ray   
     * @param direction the direction of the ray
     * @return true or false indicating if an intersection occured 
     */
    public boolean intersect(Point3d origin, Vector3d direction ) {
        try {
            return ((BoundingBoxRemote)remote).intersect( origin, direction );
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
    }
    
    /** 
     * Test for intersection with a point.
     * @param point a point defining a position in 3-space 
     * @return true or false indicating if an intersection occured 
     */
    public boolean intersect(Point3d point ) {
        try {
            return ((BoundingBoxRemote)remote).intersect( point);
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
    }

    /** 
     * Test for intersection with a point.
     * @param point a point defining a position in 3-space 
     * @return true or false indicating if an intersection occured 
     */
    public boolean intersect(Point3f point ) {
        try {
            return ((BoundingBoxRemote)remote).intersect( point);
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
    }

    /**
     * Tests whether the bounding box is empty.  A bounding box is
     * empty if it is null (either by construction or as the result of
     * a null intersection) or if its volume is negative.  A bounding box
     * with a volume of zero is <i>not</i> empty.
     * @return true if the bounding box is empty; otherwise, it returns false
     */
    public boolean isEmpty() {
        try {
            return ((BoundingBoxRemote)remote).isEmpty();
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
    }

    /** 
     * Test for intersection with another bounds object. 
     * @param boundsObject another bounds object 
     * @return true or false indicating if an intersection occured 
     */
    public boolean intersect(BoundsWrapper boundsObject) {
        try {
            return ((BoundingBoxRemote)remote).intersect( ((Bounds)boundsObject).remote );
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
    }

    /** 
     * Test for intersection with an array of bounds objects.
     * @param boundsObjects an array of bounding objects 
     * @return true or false indicating if an intersection occured 
     */
    public boolean intersect(BoundsWrapper[] boundsObjects) {

	throw new RuntimeException("Not implemented");
    }
    
    /** 
     * Test for intersection with another bounding box.
     * @param boundsObject another bounding object 
     * @param newBoundBox the new bounding box which is the intersection of
     *        the boundsObject and this BoundingBox
     * @return true or false indicating if an intersection occured 
     */
    public boolean intersect(BoundsWrapper boundsObject, BoundingBoxWrapper newBoundBox) {
        throw new RuntimeException("Not Implemented");
//        try {
//            return ((BoundingBoxRemote)remote).intersect( ((Bounds)boundsObject).remote,
//                                                    ((BoundingBox)newBoundBox).remote );
//        } catch( java.rmi.RemoteException rex ) {
//            throw new RuntimeException(rex);
//        }
    }

    /** 
     * Test for intersection with an array of  bounds objects.
     * @param boundsObjects an array of  bounds objects 
     * @param newBoundBox the new bounding box which is the intersection of
     *	      the boundsObject and this BoundingBox
     * @return true or false indicating if an intersection occured 
     */
    public boolean intersect(BoundsWrapper[] boundsObjects, BoundingBoxWrapper newBoundBox) {

       throw new RuntimeException("Not Implemented");
    }


    /** 
     * Finds closest bounding object that intersects this bounding box.
     * @param boundsObjects an array of bounds objects 
     * @return closest bounding object 
     */
    public BoundsWrapper closestIntersection( BoundsWrapper[] boundsObjects) {

	throw new RuntimeException("Not Implemented");
    }
    

    /** 
     * Returns a string representation of this class.
     */ 
//    public String toString() { 
//        try {
//            return ((BoundingBoxRemote)remote).toString();
//        } catch( java.rmi.RemoteException rex ) {
//            throw new RuntimeException(rex);
//        }
//    } 


    /**
     * Returns the position of this bounding sphere as a point.
     * @param center a Point to receive the center of the bounding box
     */
//     public Point3f getCenter() {
//	 Point3d p3d = ((javax.media.j3d.BoundingBox)wrapped).getCenter();
//         
//         return new Point3f( p3d );
//     }

}      

