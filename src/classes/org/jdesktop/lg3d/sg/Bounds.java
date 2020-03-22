/**
 * Project Looking Glass
 *
 * $RCSfile: Bounds.java,v $
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
 * $Date: 2006-06-30 17:55:07 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg;

import javax.vecmath.*;
import java.lang.Math;
import java.util.HashMap;
import java.lang.reflect.Constructor;

import org.jdesktop.lg3d.sg.internal.wrapper.BoundsWrapper;

/**
 * The abstract base class for bounds objects.  Bounds objects define
 * a convex, closed volume that is used for various intersection and
 * culling operations.
 */

public abstract class Bounds extends Object implements Cloneable {
    static final double EPSILON = .000001;
    static final boolean debug = false;

    static final int BOUNDING_BOX = 0x1;
    static final int BOUNDING_SPHERE = 0x2;
    static final int BOUNDING_POLYTOPE = 0x4;

    BoundsWrapper wrapped;
    
    /**
     * Bounds does not have user data so this structure lets us
     * determine the LG Bounds object for a given Java3D Bounds
     */
//    protected static HashMap reverseLookup = new HashMap();
    
     BoundsWrapper instantiate( String classname, Class[] parameterTypes, Object[] parameterArgs ) {
        try {
            Class cl = Class.forName( classname );
            Constructor constructor = cl.getConstructor( parameterTypes );
                        
            BoundsWrapper sgo = (BoundsWrapper)constructor.newInstance(parameterArgs);
//            reverseLookup.put( sgo, this );
            sgo.setUserData(this);
            
            return sgo;
        } catch( Exception ie ) {
            throw new RuntimeException(ie);
        }
    }
     
     BoundsWrapper instantiate( String classname) {
        try {
            Class cl = Class.forName( classname );
                        
            BoundsWrapper sgo = (BoundsWrapper)cl.newInstance();
//            reverseLookup.put( sgo, this );
            sgo.setUserData(this);
            
            return sgo;
        } catch( Exception ie ) {
            throw new RuntimeException(ie);
        }
    }
     
     /**
     * Constructs a new Bounds object.
     */
    public Bounds() {
        createWrapped();
    }
    
    protected abstract void createWrapped();


    /**
     * Makes a copy of a bounds object. 
     */
    //public abstract Object clone();


    /**
     * Indicates whether the specified <code>bounds</code> object is
     * equal to this Bounds object.  They are equal if both the
     * specified <code>bounds</code> object and this Bounds are
     * instances of the same Bounds subclass and all of the data
     * members of <code>bounds</code> are equal to the corresponding
     * data members in this Bounds.
     * @param bounds the object with which the comparison is made.
     * @return true if this Bounds object is equal to <code>bounds</code>;
     * otherwise false
     *
     * @since Java 3D 1.2
     */
    public abstract boolean equals(Object bounds);


    /**
     * Returns a hash code for this Bounds object based on the
     * data values in this object.  Two different Bounds objects of
     * the same type with identical data values (i.e., Bounds.equals
     * returns true) will return the same hash code.  Two Bounds
     * objects with different data members may return the same hash code
     * value, although this is not likely.
     * @return a hash code for this Bounds object.
     *
     * @since Java 3D 1.2
     */
    public abstract int hashCode();


  /**
   * Test for intersection with a ray.
   * @param origin the starting point of the ray
   * @param direction the direction of the ray
   * @return true or false indicating if an intersection occured
   */
  //public abstract boolean intersect( Point3d origin, Vector3d direction );

   /**
    * Test for intersection with a point.
    * @param point a point defining a position in 3-space
    * @return true or false indicating if an intersection occured
    */
  //public abstract boolean intersect( Point3d point );


    /**
     * Test for intersection with another bounds object.
     * @param boundsObject another bounds object
     * @return true or false indicating if an intersection occurred
     */
  public abstract boolean intersect( Bounds boundsObject ); 

    /**
     * Test for intersection with another bounds object.
     * @param boundsObjects an array of bounding objects
     * @return true or false indicating if an intersection occured
     */
  public abstract boolean intersect( Bounds[] boundsObjects );

   
     /**
     * Finds closest bounding object that intersects this bounding object.
     * @param boundsObjects an array of  bounds objects
     * @return closest bounding object
     */  
  public abstract Bounds closestIntersection( Bounds[] boundsObjects); 


     /**
      * Combines this bounding object with a bounding object so that the
      * resulting bounding object encloses the original bounding object and the
      * given bounds object.
      * @param boundsObject another bounds object
      */ 
  public abstract void combine( Bounds   boundsObject );


     /** 
      * Combines this bounding object with an array of bounding objects so that the
      * resulting bounding object encloses the original bounding object and the
      * given array of bounds object.
      * @param boundsObjects an array of bounds objects
      */ 
  public abstract void combine( Bounds[] boundsObjects);


  /**
   * Transforms this bounding object by the given matrix.
   * @param trans the transformation matrix
   */
  public abstract void transform(Transform3D trans);

     /**
      * Modifies the bounding object so that it bounds the volume
      * generated by transforming the given bounding object.
      * @param bounds the bounding object to be transformed
      * @param trans the transformation matrix
      */ 
  public abstract void transform( Bounds bounds, Transform3D trans);

    /**
     * Tests whether the bounds is empty.  A bounds is
     * empty if it is null (either by construction or as the result of
     * a null intersection) or if its volume is negative.  A bounds
     * with a volume of zero is <i>not</i> empty.
     * @return true if the bounds is empty; otherwise, it returns false
     */
    public abstract boolean isEmpty();

  /**
   * Sets the value of this Bounds object.
   * @param boundsObject another bounds object. 
   */
  public abstract void set( Bounds boundsObject);
  
  /**
   * Returns the looking glass bounds given a java3d bounds
   */
//  static Bounds getLGBounds( javax.media.j3d.Bounds j3dBounds ) {
//      return (Bounds)reverseLookup.get( j3dBounds );
//  }

}
