/**
 * Project Looking Glass
 *
 * $RCSfile: Transform3D.java,v $
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
 * $Date: 2005-04-14 23:04:17 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg;

import javax.vecmath.*;
import java.lang.*;
import java.lang.reflect.Constructor;
import org.jdesktop.lg3d.sg.internal.wrapper.Transform3DWrapper;

/**
 * A generalized transform object represented internally as a 4x4 
 * double-precision floating point matrix.  The mathematical 
 * representation is
 * row major, as in traditional matrix mathematics.  
 * A Transform3D is used to perform translations, rotations, and
 * scaling and shear effects.<P>
 * 
 * A transform has an associated type, and
 * all type classification is left to the Transform3D object.
 * A transform will typically have multiple types, unless it is a
 * general, unclassifiable matrix, in which case it won't be assigned
 * a type.  <P>
 *
 * The Transform3D type is internally computed when the transform
 * object is constructed and updated any time it is modified. A
 * matrix will typically have multiple types. For example, the type
 * associated with an identity matrix is the result of ORing all of
 * the types, except for ZERO and NEGATIVE_DETERMINANT, together.
 * There are public methods available to get the ORed type of the
 * transformation, the sign of the determinant, and the least
 * general matrix type. The matrix type flags are defined as
 * follows:<P>
 * <UL>
 * <LI>ZERO - zero matrix. All of the elements in the matrix
 * have the value 0.</LI><P>
 * <LI>IDENTITY - identity matrix. A matrix with ones on its
 * main diagonal and zeros every where else.</LI><P>
 * <LI>SCALE - the matrix is a uniform scale matrix - there are
 * no rotational or translation components.</LI><P>
 * <LI>ORTHOGONAL - the four row vectors that make up an orthogonal
 * matrix form a basis, meaning that they are mutually orthogonal.
 * The scale is unity and there are no translation components.</LI><P>
 * <LI>RIGID - the upper 3 X 3 of the matrix is orthogonal, and 
 * there is a translation component-the scale is unity.</LI><P>
 * <LI>CONGRUENT - this is an angle- and length-preserving matrix, 
 * meaning that it can translate, rotate, and reflect about an axis, 
 * and scale by an amount that is uniform in all directions. These 
 * operations preserve the distance between any two points, and the 
 * angle between any two intersecting lines.</LI><P>
 * <LI>AFFINE - an affine matrix can translate, rotate, reflect, 
 * scale anisotropically, and shear. Lines remain straight, and parallel 
 * lines remain parallel, but the angle between intersecting lines can 
 * change.</LI><P>
 * </UL>
 * A matrix is also classified by the sign of its determinant:<P>
 * <UL>
 * NEGATIVE_DETERMINANT - this matrix has a negative determinant. 
 * An orthogonal matrix with a positive determinant is a rotation 
 * matrix. An orthogonal matrix with a negative determinant is a 
 * reflection and rotation matrix.<P></UL>
 * The Java 3D model for 4 X 4 transformations is:<P>
 * <UL><pre>
 * [ m00 m01 m02 m03 ]   [ x ]   [ x' ]
 * [ m10 m11 m12 m13 ] . [ y ] = [ y' ]
 * [ m20 m21 m22 m23 ]   [ z ]   [ z' ]
 * [ m30 m31 m32 m33 ]   [ w ]   [ w' ]
 * 
 * x' = m00 . x+m01 . y+m02 . z+m03 . w
 * y' = m10 . x+m11 . y+m12 . z+m13 . w
 * z' = m20 . x+m21 . y+m22 . z+m23 . w
 * w' = m30 . x+m31 . y+m32 . z+m33 . w
 * </pre></ul><P>
 * Note: When transforming a Point3f or a Point3d, the input w is set to
 * 1. When transforming a Vector3f or Vector3d, the input w is set to 0.
 */

public class Transform3D {

    Transform3DWrapper wrapped;

    /**
     * A zero matrix.
     */
    public static final int ZERO = 0x01;
   
   /**
    * An identity matrix.
    */
    public static final int IDENTITY = 0x02;
    

   /**
    * A Uniform scale matrix with no translation or other 
    * off-diagonal components.
    */
    public static final int SCALE = 0x04;

   /**
    * A translation-only matrix with ones on the diagonal.
    *
    */
    public static final int TRANSLATION = 0x08;

   /**
    * The four row vectors that make up an orthogonal matrix form a basis,
    * meaning that they are mutually orthogonal; an orthogonal matrix with
    * positive determinant is a pure rotation matrix; a negative 
    * determinant indicates a rotation and a reflection.
    */
    public static final int ORTHOGONAL = 0x10;

   /**
    * This matrix is a rotation and a translation with unity scale;
    * The upper 3x3 of the matrix is orthogonal, and there is a 
    * translation component.
    */
    public static final int RIGID = 0x20;

   /**
    * This is an angle and length preserving matrix, meaning that it
    * can translate, rotate, and reflect
    * about an axis, and scale by an amount that is uniform in all directions.
    * These operations preserve the distance between any two points and the
    * angle between any two intersecting lines.
    */
    public static final int CONGRUENT = 0x40;

   /**
    * An affine matrix can translate, rotate, reflect, scale anisotropically,
    * and shear.  Lines remain straight, and parallel lines remain parallel,
    * but the angle between intersecting lines can change. In order for a 
    * transform to be classified as affine, the 4th row must be: [0, 0, 0, 1].
    */
    public static final int AFFINE = 0x80;

   /**
    * This matrix has a negative determinant; an orthogonal matrix with
    * a positive determinant is a rotation matrix; an orthogonal matrix
    * with a negative determinant is a reflection and rotation matrix.
    */
    public static final int NEGATIVE_DETERMINANT = 0x100;

    /* The upper 3x3 column vectors that make up an orthogonal 
     * matrix form a basis meaning that they are mutually orthogonal. 
     * It can have non-uniform or zero x/y/z scale as long as 
     * the dot product of any two column is zero.
     * This one is used by Java3D internal only and should not
     * expose to the user.
     */ 
    private static final int ORTHO = 0x40000000;

    Transform3DWrapper instantiate( String classname, Class[] parameterTypes, Object[] parameterArgs ) {
        try {
            Class cl = Class.forName( classname );
            Constructor constructor = cl.getConstructor( parameterTypes );
            
            
            Transform3DWrapper sgo = (Transform3DWrapper)constructor.newInstance(parameterArgs);
            //sgo.setUserData( this );
            
            return sgo;
        } catch( Exception ie ) {
            throw new RuntimeException(ie);
        }
    }
    
    /**
     * Constructs and initializes a transform from the 4 x 4 matrix.  The 
     * type of the constructed transform will be classified automatically. 
     * @param m1 the 4 x 4 transformation matrix
     */
    public Transform3D(Matrix4f m1) {  
	//wrapped = new javax.media.j3d.Transform3D(m1);
        wrapped = instantiate( SceneGraphSetup.getWrapperPackage()+"Transform3D", new Class[] {Matrix4f.class}, new Object[] { m1 } );
    }

    /**
     * Constructs and initializes a transform from the 4 x 4 matrix.  The
     * type of the constructed transform will be classified automatically.
     * @param m1 the 4 x 4 transformation matrix
     */
//    private Transform3D(Matrix4d m1) {
//	set(m1);
//    }

    /**
     * Constructs and initializes a transform from the Transform3D object.
     * @param t1  the transformation object to be copied
     */
    public Transform3D(Transform3D t1) {
	//wrapped = new javax.media.j3d.Transform3D(t1.wrapped);
        wrapped = instantiate( SceneGraphSetup.getWrapperPackage()+"Transform3D", new Class[] {Transform3D.class}, new Object[] { t1 } );
    }

    /**
     * Constructs and initializes a transform to the identity matrix.
     */
    public Transform3D() {
        //wrapped = new javax.media.j3d.Transform3D();
        wrapped = instantiate( SceneGraphSetup.getWrapperPackage()+"Transform3D", null, null );
    }

   /**
     * Constructs and initializes a transform from the float array of 
     * length 16; the top row of the matrix is initialized to the first
     * four elements of the array, and so on.  The type of the transform 
     * object is classified internally.
     * @param matrix  a float array of 16
     */
    public Transform3D(float[] matrix) {
        this();
	wrapped.set(matrix);
    }


   /**
     * Constructs and initializes a transform from the quaternion,
     * translation, and scale values.   The scale is applied only to the
     * rotational components of the matrix (upper 3 x 3) and not to the
     * translational components of the matrix.
     * @param q1  the quaternion value representing the rotational component
     * @param t1  the translational component of the matrix
     * @param s   the scale value applied to the rotational components
     */  
    public Transform3D(Quat4f q1, Vector3f t1, float s) {
        this();
	wrapped.set(q1, t1, s);
    }
 
   /**    
     * Constructs and initializes a transform from the rotation matrix,  
     * translation, and scale values.   The scale is applied only to the  
     * rotational components of the matrix (upper 3x3) and not to the  
     * translational components of the matrix.     
     * @param m1  the rotation matrix representing the rotational component  
     * @param t1  the translational component of the matrix  
     * @param s   the scale value applied to the rotational components   
     */   
    public Transform3D(Matrix3f m1, Vector3f t1, float s) {   
        this();
	wrapped.set(m1, t1, s);
    }    
 
   /**
     * Returns the type of this matrix as an or'ed bitmask of 
     * of all of the type classifications to which it belongs.
     * @return  or'ed bitmask of all of the type classifications 
     * of this transform
     */
    public final int getType() {    
	return wrapped.getType();
    }   


   /**
     * Returns the least general type of this matrix; the order of
     * generality from least to most is: ZERO, IDENTITY, 
     * SCALE/TRANSLATION, ORTHOGONAL, RIGID, CONGRUENT, AFFINE.
     * If the matrix is ORTHOGONAL, calling the method 
     * getDeterminantSign() will yield more information.
     * @return the least general matrix type
     */
    public final int getBestType() {
	return wrapped.getBestType();
    }

    /*
    private void print_type() {
        if ((type & ZERO)                 > 0 ) System.out.print(" ZERO");
	if ((type & IDENTITY)             > 0 ) System.out.print(" IDENTITY");
	if ((type & SCALE)                > 0 ) System.out.print(" SCALE");
	if ((type & TRANSLATION)          > 0 ) System.out.print(" TRANSLATION");
	if ((type & ORTHOGONAL)           > 0 ) System.out.print(" ORTHOGONAL");
	if ((type & RIGID)                > 0 ) System.out.print(" RIGID");
	if ((type & CONGRUENT)            > 0 ) System.out.print(" CONGRUENT");
	if ((type & AFFINE)               > 0 ) System.out.print(" AFFINE");
	if ((type & NEGATIVE_DETERMINANT) > 0 ) System.out.print(" NEGATIVE_DETERMINANT");
	}
    */

    /**
     * Returns the sign of the determinant of this matrix; a return value
     * of true indicates a positive determinant; a return value of false
     * indicates a negative determinant.  In general, an orthogonal matrix
     * with a positive determinant is a pure rotation matrix; an orthogonal
     * matrix with a negative determinant is a both a rotation and a 
     * reflection matrix.
     * @return  determinant sign : true means positive, false means negative
     */
    public final boolean getDeterminantSign() {
        return wrapped.getDeterminantSign();
    }

    /**
     * Sets a flag that enables or disables automatic SVD
     * normalization.  If this flag is enabled, an automatic SVD
     * normalization of the rotational components (upper 3x3) of this
     * matrix is done after every subsequent matrix operation that
     * modifies this matrix.  This is functionally equivalent to
     * calling normalize() after every subsequent call, but may be
     * less computationally expensive.
     * The default value for this parameter is false.
     * @param autoNormalize  the boolean state of auto normalization
     */
    public final void setAutoNormalize(boolean autoNormalize) {
	wrapped.setAutoNormalize( autoNormalize );
    }
    
    /**
     * Returns the state of auto-normalization.
     * @return  boolean state of auto-normalization
     */
    public final boolean getAutoNormalize() {
	return wrapped.getAutoNormalize();
    }


   /**
     * Returns the matrix elements of this transform as a string.
     * @return  the matrix elements of this transform
     */
    public String toString() {
	// also, print classification?
	return wrapped.toString();
    }
    
    /**
     * Sets this transform to the identity matrix.
     */
    public final void setIdentity() {
	wrapped.setIdentity();
    }

   /** 
     * Sets this transform to all zeros. 
     */
    public final void setZero() {
	wrapped.setZero();
    }


   /**
     * Adds this transform to transform t1 and places the result into
     * this: this = this + t1.
     * @param t1  the transform to be added to this transform
     */
    public final void add(Transform3D t1) {
	wrapped.add( t1.wrapped );
    }

    /** 
     * Adds transforms t1 and t2 and places the result into this transform.
     * @param t1  the transform to be added
     * @param t2  the transform to be added
     */
    public final void add(Transform3D t1, Transform3D t2) {
	wrapped.add( t1.wrapped, t2.wrapped );
    }

    /**
     * Subtracts transform t1 from this transform and places the result 
     * into this: this = this - t1.
     * @param t1  the transform to be subtracted from this transform
     */
    public final void sub(Transform3D t1) {
        wrapped.sub( t1.wrapped );
    }


    /** 
     * Subtracts transform t2 from transform t1 and places the result into
     * this: this = t1 - t2.
     * @param t1   the left transform
     * @param t2   the right transform
     */
    public final void sub(Transform3D t1, Transform3D t2) {
	wrapped.sub( t1.wrapped, t2.wrapped );
    } 


   /**
     * Transposes this matrix in place.
     */
    public final void transpose() {
        wrapped.transpose();
    }

    /**
     * Transposes transform t1 and places the value into this transform.  
     * The transform t1 is not modified.
     * @param t1  the transform whose transpose is placed into this transform
     */ 
    public final void transpose(Transform3D t1) {
        wrapped.transpose( t1.wrapped );
    }

   /**
     * Sets the value of this transform to the matrix conversion of the
     * single precision quaternion argument; the non-rotational 
     * components are set as if this were an identity matrix. 
     * @param q1  the quaternion to be converted
     */ 
    public final void set(Quat4f q1) {
        wrapped.set( q1 );
    }


   /**
     * Sets the rotational component (upper 3x3) of this transform to the
     * matrix values in the single precision Matrix3f argument; the other
     * elements of this transform are unchanged; any pre-existing scale 
     * will be preserved; the argument matrix m1 will be checked for proper
     * normalization when this transform is internally classified.
     * @param m1   the single precision 3x3 matrix
     */
     public final void setRotation(Matrix3f m1) {
        wrapped.setRotation( m1 );
     }


    /**
     * Sets the rotational component (upper 3x3) of this transform to the
     * matrix equivalent values of the quaternion argument; the other
     * elements of this transform are unchanged; any pre-existing scale
     * in the transform is preserved.
     * @param q1    the quaternion that specifies the rotation
    */
    public final void setRotation(Quat4f q1) {
        wrapped.setRotation( q1 );
    }  

    /**
     * Sets the value of this transform to the matrix conversion
     * of the single precision axis-angle argument; all of the matrix
     * values are modified.
     * @param a1 the axis-angle to be converted (x, y, z, angle)
     */  
    public final void set(AxisAngle4f a1) {
        wrapped.set( a1 );
    }


   /**   
     * Sets the rotational component (upper 3x3) of this transform to the
     * matrix equivalent values of the axis-angle argument; the other
     * elements of this transform are unchanged; any pre-existing scale
     * in the transform is preserved.
     * @param a1 the axis-angle to be converted (x, y, z, angle)
     */ 
    public final void setRotation(AxisAngle4f a1)  {    
        wrapped.setRotation( a1 );
    } 
    

    /**
     * Sets the value of this transform to a counter clockwise rotation 
     * about the x axis. All of the non-rotational components are set as 
     * if this were an identity matrix.
     * @param angle the angle to rotate about the X axis in radians
     */  
    public void rotX(double angle) {
        wrapped.rotX( angle );
    }

    /**
     * Sets the value of this transform to a counter clockwise rotation about 
     * the y axis. All of the non-rotational components are set as if this 
     * were an identity matrix.
     * @param angle the angle to rotate about the Y axis in radians
     */
    public void rotY(double angle) {  
        wrapped.rotY( angle );
    }

 
    /**
     * Sets the value of this transform to a counter clockwise rotation 
     * about the z axis.  All of the non-rotational components are set 
     * as if this were an identity matrix.
     * @param angle the angle to rotate about the Z axis in radians
     */
    public void rotZ(double angle)  {  
        wrapped.rotZ( angle );
    }
 

   /**
     * Sets the translational value of this matrix to the Vector3f parameter 
     * values, and sets the other components of the matrix as if this
     * transform were an identity matrix.
     * @param trans  the translational component
     */
    public final void set(Vector3f trans) {
        wrapped.set( trans );
    }


    /**
     * Sets the scale component of the current transform; any existing
     * scale is first factored out of the existing transform before
     * the new scale is applied.
     * @param scale  the new scale amount
     * @since WSG
     */
    public final void setScale(float scale) {
        wrapped.setScale(scale);
    }


    /**
     * Sets the possibly non-uniform scale component of the current
     * transform; any existing scale is first factored out of the
     * existing transform before the new scale is applied.
     * @param scale  the new x,y,z scale values
     * @since WSG
     */
     public final void setScale(Vector3f scale) {
       wrapped.setScale( scale );
     }

    /** 
     * Replaces the translational components of this transform to the values
     * in the Vector3f argument; the other values of this transform are not 
     * modified.
     * @param trans  the translational component
     */
    public final void setTranslation(Vector3f trans) {
       wrapped.setTranslation( trans );
    }

    /** 
     * Sets the value of this matrix from the rotation expressed 
     * by the quaternion q1, the translation t1, and the scale s.
     * @param q1 the rotation expressed as a quaternion 
     * @param t1 the translation 
     * @param s the scale value 
     */  
    public final void set(Quat4f q1, Vector3f t1, float s) {
        wrapped.set( q1, t1, s );
    }

    /**
     * Sets the value of this matrix from the rotation expressed
     * by the rotation matrix m1, the translation t1, and the scale s.
     * The scale is only applied to the
     * rotational component of the matrix (upper 3x3) and not to the
     * translational component of the matrix.
     * @param m1 the rotation matrix
     * @param t1 the translation
     * @param s the scale value
     */
    public final void set(Matrix3f m1, Vector3f t1, float s) {  
	wrapped.set( m1, t1, s );
    }

    /** 
     * Sets the matrix, type, and state of this transform to the matrix,
     * type, and state of transform t1. 
     * @param t1  the transform to be copied
     */  
    public final void set(Transform3D t1){
        wrapped.set( t1.wrapped );
    }

   /**
     * Sets the matrix values of this transform to the matrix values in the
     * single precision array parameter.  The matrix type is classified  
     * internally by the Transform3D class. 
     * @param matrix  the single precision array of length 16 in row major format 
     */
    public final void set(float[] matrix) {
        wrapped.set( matrix );
    }


    /**  
     * Sets the matrix values of this transform to the matrix values in the
     * single precision Matrix4f argument.  The transform type is classified
     * internally by the Transform3D class.
     * @param m1   the single precision 4x4 matrix
     */  
    public final void set(Matrix4f m1) {
        wrapped.set( m1 );
    }


    /**  
     * Sets the rotational component (upper 3x3) of this transform to the 
     * matrix values in the single precision Matrix3f argument; the other 
     * elements of this transform are initialized as if this were an identity
     * matrix (i.e., affine matrix with no translational component).  
     * @param m1   the single precision 3x3 matrix 
     */   
    public final void set(Matrix3f m1) {
        wrapped.set( m1 );
    }

    /**                                  
     * Places the values of this transform into the single precision array
     * of length 16.  The first four elements of the array will contain   
     * the top row of the transform matrix, etc. 
     * @param matrix  the single precision array of length 16
     */  
    public final void get(float[] matrix) {
        wrapped.get( matrix );
    }


    /**
     * Places the normalized rotational component of this transform
     * into the 3x3 matrix argument.
     * @param m1  the matrix into which the rotational component is placed
     */  
    public final void get(Matrix3f m1) {
        wrapped.get( m1 );
    }

    /**
     * Places the quaternion equivalent of the normalized rotational 
     * component of this transform into the quaternion parameter.
     * @param q1  the quaternion into which the rotation component is placed
     */
    public final void get(Quat4f q1) {
        wrapped.get( q1 );
    } 


    /**
     * Places the values of this transform into the single precision matrix
     * argument.
     * @param matrix   the single precision matrix
     */  
    public final void get(Matrix4f matrix) {
	wrapped.get( matrix );
    }

   /**   
     * Places the quaternion equivalent of the normalized rotational
     * component of this transform into the quaternion parameter; 
     * places the translational component into the Vector parameter.
     * @param q1  the quaternion representing the rotation
     * @param t1  the translation component
     * @return  the scale component of this transform
     */ 
    public final float get(Quat4f q1, Vector3f t1) {  
        return wrapped.get( q1, t1 );
    }

    /**    
     * Places the normalized rotational component of this transform
     * into the matrix parameter; place the translational component
     * into the vector parameter.
     * @param m1  the normalized matrix representing the rotation 
     * @param t1  the translation component 
     * @return  the scale component of this transform
     */  
    public final float get(Matrix3f m1, Vector3f t1) {    
        return wrapped.get( m1, t1 );
    } 

    /**
     * Returns the uniform scale factor of this matrix.
     * If the matrix has non-uniform scale factors, the largest of the 
     * x, y, and z scale factors will be returned.
     * @return  the scale factor of this matrix
     */
    public final double getScale() {
	return wrapped.getScale();
   }


    /** 
     * Retrieves the translational components of this transform. 
     * @param trans  the vector that will receive the translational component
     */
    public final void get(Vector3f trans) {
	wrapped.get( trans );
    }

    /**
     * Sets the value of this transform to the inverse of the passed
     * Transform3D parameter.  This method uses the transform type
     * to determine the optimal algorithm for inverting transform t1.
     * @param t1  the transform to be inverted
     * @exception SingularMatrixException thrown if transform t1 is
     * not invertible
     */
    public final void invert(Transform3D t1) {
	wrapped.invert( t1.wrapped );
    }
    
    /**
     * Inverts this transform in place.  This method uses the transform
     * type to determine the optimal algorithm for inverting this transform.
     * @exception SingularMatrixException thrown if this transform is
     * not invertible
     */
    public final void invert() {
	wrapped.invert();
    }

    /** 
     * Calculates and returns the determinant of this transform.
     * @return  the double precision determinant
     */
     public final double determinant() {
	return wrapped.determinant();
     }
     
    /**
     * Sets the value of this transform to a scale and translation
     * matrix; the scale is not applied to the translation and all
     * of the matrix values are modified.
     * @param scale the scale factor for the transform
     * @param v1 the translation amount 
     */  
    public final void set(float scale, Vector3f v1)  {
	wrapped.set( scale, v1 );
    }

    /** 
     * Sets the value of this transform to a scale and translation matrix;
     * the translation is scaled by the scale factor and all of the 
     * matrix values are modified.
     * @param v1 the translation amount
     * @param scale the scale factor for the transform AND the translation 
     */ 
    public final void set(Vector3f v1, float scale) {
	wrapped.set( v1, scale ); 
    }


   /**
     * Multiplies each element of this transform by a scalar.
     * @param scalar  the scalar multiplier
     */
    public final void mul(double scalar) {
	wrapped.mul( scalar );
    }

   /**
     * Multiplies each element of transform t1 by a scalar and places
     * the result into this.  Transform t1 is not modified.
     * @param scalar  the scalar multiplier
     * @param t1  the original transform
     */
    public final void mul(double scalar, Transform3D t1)  {
	wrapped.mul( scalar, t1.wrapped );
    }


    /**
     * Sets the value of this transform to the result of multiplying itself
     * with transform t1 (this = this * t1).
     * @param t1 the other transform
     */ 
    public final void mul(Transform3D t1) {
	wrapped.mul( t1.wrapped );
    } 

    /**  
     * Sets the value of this transform to the result of multiplying transform
     * t1 by transform t2 (this = t1*t2).
     * @param t1  the left transform
     * @param t2  the right transform
     */  
    public final void mul(Transform3D t1, Transform3D t2)
    {
	wrapped.mul( t1.wrapped, t2.wrapped );
    } 

    /**
     * Multiplies this transform by the inverse of transform t1. The final
     * value is placed into this matrix (this = this*t1^-1).
     * @param t1  the matrix whose inverse is computed.
     */
    public final void mulInverse(Transform3D t1) {
	wrapped.mulInverse( t1.wrapped );
    } 


    /** 
     * Multiplies transform t1 by the inverse of transform t2. The final
     * value is placed into this matrix (this = t1*t2^-1). 
     * @param t1  the left transform in the multiplication 
     * @param t2  the transform whose inverse is computed. 
     */ 
    public final void mulInverse(Transform3D t1, Transform3D t2) {
        wrapped.mulInverse( t1.wrapped, t2.wrapped );
    } 

    /** 
     * Multiplies transform t1 by the transpose of transform t2 and places
     * the result into this transform (this = t1 * transpose(t2)).
     * @param t1  the transform on the left hand side of the multiplication    
     * @param t2  the transform whose transpose is computed  
     */  
    public final void mulTransposeRight(Transform3D t1, Transform3D t2) {
	wrapped.mulTransposeRight( t1.wrapped, t2.wrapped );
    }

 
    /** 
     * Multiplies the transpose of transform t1 by transform t2 and places
     * the result into this matrix (this = transpose(t1) * t2).
     * @param t1  the transform whose transpose is computed
     * @param t2  the transform on the right hand side of the multiplication  
     */  
    public final void mulTransposeLeft(Transform3D t1, Transform3D t2){
        wrapped.mulTransposeLeft( t1.wrapped, t2.wrapped );
    }


    /**   
     * Multiplies the transpose of transform t1 by the transpose of
     * transform t2 and places the result into this transform
     * (this = transpose(t1) * transpose(t2)). 
     * @param t1  the transform on the left hand side of the multiplication  
     * @param t2  the transform on the right hand side of the multiplication  
     */  
    public final void mulTransposeBoth(Transform3D t1, Transform3D t2) {  
	wrapped.mulTransposeBoth( t1.wrapped, t2.wrapped );
    }

 
    /**
     * Normalizes the rotational components (upper 3x3) of this matrix
     * in place using a Singular Value Decomposition (SVD).
     * This operation ensures that the column vectors of this matrix
     * are orthogonal to each other.  The primary use of this method
     * is to correct for floating point errors that accumulate over
     * time when concatenating a large number of rotation matrices.
     * Note that the scale of the matrix is not altered by this method.
     */
    public final void normalize() {  
        wrapped.normalize();
    }

    /**    
     * Normalizes the rotational components (upper 3x3) of transform t1
     * using a Singular Value Decomposition (SVD), and places the result
     * into this transform.
     * This operation ensures that the column vectors of this matrix
     * are orthogonal to each other.  The primary use of this method
     * is to correct for floating point errors that accumulate over
     * time when concatenating a large number of rotation matrices.
     * Note that the scale of the matrix is not altered by this method.
     *
     * @param t1  the source transform, which is not modified
     */ 
    public final void normalize(Transform3D t1){
	wrapped.normalize( (Transform3DWrapper)t1.wrapped );
    }

    /**     
     * Normalizes the rotational components (upper 3x3) of this transform 
     * in place using a Cross Product (CP) normalization. 
     * This operation ensures that the column vectors of this matrix
     * are orthogonal to each other.  The primary use of this method
     * is to correct for floating point errors that accumulate over
     * time when concatenating a large number of rotation matrices.
     * Note that the scale of the matrix is not altered by this method.
     */ 
    public final void normalizeCP()  {
	wrapped.normalizeCP();
    }


    /**
     * Normalizes the rotational components (upper 3x3) of transform t1
     * using a Cross Product (CP) normalization, and
     * places the result into this transform.
     * This operation ensures that the column vectors of this matrix
     * are orthogonal to each other.  The primary use of this method
     * is to correct for floating point errors that accumulate over
     * time when concatenating a large number of rotation matrices.
     * Note that the scale of the matrix is not altered by this method.
     *
     * @param t1 the transform to be normalized
     */ 
    public final void normalizeCP(Transform3D t1) {  
	wrapped.normalizeCP( (Transform3DWrapper)t1.wrapped );
    }


    /**
     * Returns true if all of the data members of transform t1 are
     * equal to the corresponding data members in this Transform3D.
     * @param t1  the transform with which the comparison is made
     * @return  true or false
     */  
    public boolean equals(Transform3D t1) {
	return ((Transform3DWrapper)wrapped).equals( (Transform3DWrapper)t1.wrapped );
    } 


   /**
     * Returns true if the Object o1 is of type Transform3D and all of the
     * data members of o1 are equal to the corresponding data members in
     * this Transform3D.
     * @param o1  the object with which the comparison is made.
     * @return  true or false
     */  
    public boolean equals(Object o1) {
        if (o1 instanceof Transform3D)
            return wrapped.equals((Transform3DWrapper)((Transform3D)o1).wrapped);
	return wrapped.equals( o1 );
    }
    

    /**
     * Returns true if the L-infinite distance between this matrix
     * and matrix m1 is less than or equal to the epsilon parameter,
     * otherwise returns false.  The L-infinite
     * distance is equal to
     * MAX[i=0,1,2,3 ; j=0,1,2,3 ; abs[(this.m(i,j) - m1.m(i,j)]
     * @param t1  the transform to be compared to this transform
     * @param epsilon  the threshold value
     */  
    public boolean epsilonEquals(Transform3D t1, double epsilon) {
        return wrapped.epsilonEquals( t1.wrapped, epsilon );
    }


    /**
     * Returns a hash code value based on the data values in this
     * object.  Two different Transform3D objects with identical data
     * values (i.e., Transform3D.equals returns true) will return the
     * same hash number.  Two Transform3D objects with different data
     * members may return the same hash value, although this is not
     * likely.
     * @return the integer hash code value
     */
    public int hashCode() {
	return wrapped.hashCode();
    } 


    /** 
     * Transform the vector vec using this Transform and place the 
     * result into vecOut.
     * @param vec  the single precision vector to be transformed
     * @param vecOut  the vector into which the transformed values are placed
     */
    public final void transform(Vector4f vec, Vector4f vecOut)  {
	wrapped.transform( vec, vecOut );
    }


    /**  
     * Transform the vector vec using this Transform and place the  
     * result back into vec. 
     * @param vec  the single precision vector to be transformed 
     */ 
    public final void transform(Vector4f vec) {
	wrapped.transform( vec );
    }

    /**
     * Transforms the point parameter with this transform and
     * places the result into pointOut.  The fourth element of the
     * point input paramter is assumed to be one.
     * @param point  the input point to be transformed
     * @param pointOut  the transformed point
     */
    public final void transform(Point3f point, Point3f pointOut)  { 
	wrapped.transform( point, pointOut );
    } 


    /**
     * Transforms the point parameter with this transform and
     * places the result back into point.  The fourth element of the
     * point input paramter is assumed to be one.
     * @param point  the input point to be transformed
     */
    public final void transform(Point3f point) { 
        wrapped.transform( point );
    } 


    /**
     * Transforms the normal parameter by this transform and places the value
     * into normalOut.  The fourth element of the normal is assumed to be zero.
     * Note: For correct lighting results, if a transform has uneven scaling 
     * surface normals should transformed by the inverse transpose of 
     * the transform. This the responsibility of the application and is not
     * done automatically by this method.
     * @param normal   the input normal to be transformed
     * @param normalOut  the transformed normal
     */
    public final void transform(Vector3f normal, Vector3f normalOut) {
	wrapped.transform( normal, normalOut );
    }

    /**
     * Transforms the normal parameter by this transform and places the value
     * back into normal.  The fourth element of the normal is assumed to be zero.
     * Note: For correct lighting results, if a transform has uneven scaling 
     * surface normals should transformed by the inverse transpose of 
     * the transform. This the responsibility of the application and is not
     * done automatically by this method.
     * @param normal   the input normal to be transformed
     */
    public final void transform(Vector3f normal) { 
        wrapped.transform( normal );
    }


    /**
     * Replaces the upper 3x3 matrix values of this transform with the 
     * values in the matrix m1.
     * @param m1  the matrix that will be the new upper 3x3
     */
    public final void setRotationScale(Matrix3f m1) {
	wrapped.setRotationScale( m1 );
    }

   
    /**
     *  Scales transform t1 by a Uniform scale matrix with scale 
     *  factor s and then adds transform t2 (this = S*t1 + t2). 
     *  @param s  the scale factor
     *  @param t1 the transform to be scaled
     *  @param t2 the transform to be added
     */
    public final void scaleAdd(double s, Transform3D t1, Transform3D t2) {
	wrapped.scaleAdd( s, t1.wrapped, t2.wrapped );
    }


    /**
     *  Scales this transform by a Uniform scale matrix with scale factor 
     *  s and then adds transform t1 (this = S*this + t1).  
     *  @param s  the scale factor
     *  @param t1 the transform to be added
     */
    public final void scaleAdd(double s, Transform3D t1) {
	wrapped.scaleAdd( s, t1.wrapped );
   }


    /**
     * Gets the upper 3x3 values of this matrix and places them into
     * the matrix m1.
     * @param m1  the matrix that will hold the values
     */
    public final void getRotationScale(Matrix3f m1) {
	wrapped.getRotationScale( m1 );
    }


    /**
     * Creates a perspective projection transform that mimics a standard,
     * camera-based,
     * view-model.  This transform maps coordinates from Eye Coordinates (EC)
     * to Clipping Coordinates (CC).  Note that unlike the similar function
     * in OpenGL, the clipping coordinates generated by the resulting
     * transform are in a right-handed coordinate system
     * (as are all other coordinate systems in Java 3D).
     * <p>
     * The frustum function-call establishes a view model with the eye
     * at the apex of a symmetric view frustum. The arguments
     * define the frustum and its associated perspective projection:
     * (left, bottom, -near) and (right, top, -near) specify the
     * point on the near clipping plane that maps onto the 
     * lower-left and upper-right corners of the window respectively,
     * assuming the eye is located at (0, 0, 0).
     * @param left the vertical line on the left edge of the near
     * clipping plane mapped to the left edge of the graphics window
     * @param right the vertical line on the right edge of the near
     * clipping plane mapped to the right edge of the graphics window
     * @param bottom the horizontal line on the bottom edge of the near
     * clipping plane mapped to the bottom edge of the graphics window
     * @param top the horizontal line on the top edge of the near
     * @param near the distance to the frustum's near clipping plane.
     * This value must be positive, (the value -near is the location of the
     * near clip plane).
     * @param far the distance to the frustum's far clipping plane.
     * This value must be positive, and must be greater than near.
     */
    public void frustum(double left, double right,
			double bottom, double top,
			double near, double far) {
	wrapped.frustum( left, right, bottom, top, near, far );
    }


    /**
     * Creates a perspective projection transform that mimics a standard,
     * camera-based,
     * view-model.  This transform maps coordinates from Eye Coordinates (EC)
     * to Clipping Coordinates (CC).  Note that unlike the similar function
     * in OpenGL, the clipping coordinates generated by the resulting
     * transform are in a right-handed coordinate system
     * (as are all other coordinate systems in Java 3D). Also note that the
     * field of view is specified in radians.
     * @param fovx specifies the field of view in the x direction, in radians
     * @param aspect specifies the aspect ratio and thus the field of
     * view in the x direction. The aspect ratio is the ratio of x to y,
     * or width to height.
     * @param zNear the distance to the frustum's near clipping plane.
     * This value must be positive, (the value -zNear is the location of the
     * near clip plane).
     * @param zFar the distance to the frustum's far clipping plane
     */
    public void perspective(double fovx, double aspect,
			    double zNear, double zFar) {
	wrapped.perspective( fovx, aspect, zNear, zFar );
    }


    /**
     * Creates an orthographic projection transform that mimics a standard,
     * camera-based,
     * view-model.  This transform maps coordinates from Eye Coordinates (EC)
     * to Clipping Coordinates (CC).  Note that unlike the similar function
     * in OpenGL, the clipping coordinates generated by the resulting
     * transform are in a right-handed coordinate system
     * (as are all other coordinate systems in Java 3D).
     * @param left the vertical line on the left edge of the near
     * clipping plane mapped to the left edge of the graphics window
     * @param right the vertical line on the right edge of the near
     * clipping plane mapped to the right edge of the graphics window
     * @param bottom the horizontal line on the bottom edge of the near
     * clipping plane mapped to the bottom edge of the graphics window
     * @param top the horizontal line on the top edge of the near
     * clipping plane mapped to the top edge of the graphics window
     * @param near the distance to the frustum's near clipping plane
     * (the value -near is the location of the near clip plane)
     * @param far the distance to the frustum's far clipping plane
     */
    public void ortho(double left, double right, double bottom,
                        double top, double near, double far) {
	wrapped.ortho( left, right, bottom, top, near, far );
    }


    /**
     * Not intended for public use. Users should NEVER call this.
     */
    public Transform3DWrapper getWrapped() {
        return wrapped;
    }
}
