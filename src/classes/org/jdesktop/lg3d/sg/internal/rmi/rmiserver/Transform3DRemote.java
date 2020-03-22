/**
 * Project Looking Glass
 *
 * $RCSfile: Transform3DRemote.java,v $
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
 * $Date: 2004-06-23 18:51:24 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.rmi.rmiserver;

import javax.vecmath.*;
import java.lang.*;
import java.rmi.Remote;

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

public interface Transform3DRemote extends Remote {
 
   /**
     * Returns the type of this matrix as an or'ed bitmask of 
     * of all of the type classifications to which it belongs.
     * @return  or'ed bitmask of all of the type classifications 
     * of this transform
     */
    public  int getType() throws java.rmi.RemoteException;

   /**
     * Returns the least general type of this matrix; the order of
     * generality from least to most is: ZERO, IDENTITY, 
     * SCALE/TRANSLATION, ORTHOGONAL, RIGID, CONGRUENT, AFFINE.
     * If the matrix is ORTHOGONAL, calling the method 
     * getDeterminantSign() will yield more information.
     * @return the least general matrix type
     */
    public  int getBestType() throws java.rmi.RemoteException;

    /**
     * Returns the sign of the determinant of this matrix; a return value
     * of true indicates a positive determinant; a return value of false
     * indicates a negative determinant.  In general, an orthogonal matrix
     * with a positive determinant is a pure rotation matrix; an orthogonal
     * matrix with a negative determinant is a both a rotation and a 
     * reflection matrix.
     * @return  determinant sign : true means positive, false means negative
     */
    public  boolean getDeterminantSign() throws java.rmi.RemoteException;

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
    public  void setAutoNormalize(boolean autoNormalize) throws java.rmi.RemoteException;
    
    /**
     * Returns the state of auto-normalization.
     * @return  boolean state of auto-normalization
     */
    public  boolean getAutoNormalize() throws java.rmi.RemoteException;

    /**
     * Sets this transform to the identity matrix.
     */
    public  void setIdentity() throws java.rmi.RemoteException;
   /** 
     * Sets this transform to all zeros. 
     */
    public  void setZero() throws java.rmi.RemoteException;

   /**
     * Adds this transform to transform t1 and places the result into
     * this: this = this + t1.
     * @param t1  the transform to be added to this transform
     */
    public  void add(Transform3DRemote t1) throws java.rmi.RemoteException;

    /** 
     * Adds transforms t1 and t2 and places the result into this transform.
     * @param t1  the transform to be added
     * @param t2  the transform to be added
     */
    public  void add(Transform3DRemote t1, Transform3DRemote t2) throws java.rmi.RemoteException;

    /**
     * Subtracts transform t1 from this transform and places the result 
     * into this: this = this - t1.
     * @param t1  the transform to be subtracted from this transform
     */
    public  void sub(Transform3DRemote t1) throws java.rmi.RemoteException;


    /** 
     * Subtracts transform t2 from transform t1 and places the result into
     * this: this = t1 - t2.
     * @param t1   the left transform
     * @param t2   the right transform
     */
    public  void sub(Transform3DRemote t1, Transform3DRemote t2) throws java.rmi.RemoteException;


   /**
     * Transposes this matrix in place.
     */
    public  void transpose() throws java.rmi.RemoteException;

    /**
     * Transposes transform t1 and places the value into this transform.  
     * The transform t1 is not modified.
     * @param t1  the transform whose transpose is placed into this transform
     */ 
    public  void transpose(Transform3DRemote t1) throws java.rmi.RemoteException;

   /**
     * Sets the value of this transform to the matrix conversion of the
     * single precision quaternion argument; the non-rotational 
     * components are set as if this were an identity matrix. 
     * @param q1  the quaternion to be converted
     */ 
    public  void set(Quat4f q1) throws java.rmi.RemoteException;


   /**
     * Sets the rotational component (upper 3x3) of this transform to the
     * matrix values in the single precision Matrix3f argument; the other
     * elements of this transform are unchanged; any pre-existing scale 
     * will be preserved; the argument matrix m1 will be checked for proper
     * normalization when this transform is internally classified.
     * @param m1   the single precision 3x3 matrix
     */
     public  void setRotation(Matrix3f m1) throws java.rmi.RemoteException;


    /**
     * Sets the rotational component (upper 3x3) of this transform to the
     * matrix equivalent values of the quaternion argument; the other
     * elements of this transform are unchanged; any pre-existing scale
     * in the transform is preserved.
     * @param q1    the quaternion that specifies the rotation
    */
    public  void setRotation(Quat4f q1) throws java.rmi.RemoteException;

    /**
     * Sets the value of this transform to the matrix conversion
     * of the single precision axis-angle argument; all of the matrix
     * values are modified.
     * @param a1 the axis-angle to be converted (x, y, z, angle)
     */  
    public  void set(AxisAngle4f a1) throws java.rmi.RemoteException;


   /**   
     * Sets the rotational component (upper 3x3) of this transform to the
     * matrix equivalent values of the axis-angle argument; the other
     * elements of this transform are unchanged; any pre-existing scale
     * in the transform is preserved.
     * @param a1 the axis-angle to be converted (x, y, z, angle)
     */ 
    public  void setRotation(AxisAngle4f a1) throws java.rmi.RemoteException;
    

    /**
     * Sets the value of this transform to a counter clockwise rotation 
     * about the x axis. All of the non-rotational components are set as 
     * if this were an identity matrix.
     * @param angle the angle to rotate about the X axis in radians
     */  
    public void rotX(double angle) throws java.rmi.RemoteException;

    /**
     * Sets the value of this transform to a counter clockwise rotation about 
     * the y axis. All of the non-rotational components are set as if this 
     * were an identity matrix.
     * @param angle the angle to rotate about the Y axis in radians
     */
    public void rotY(double angle) throws java.rmi.RemoteException;

 
    /**
     * Sets the value of this transform to a counter clockwise rotation 
     * about the z axis.  All of the non-rotational components are set 
     * as if this were an identity matrix.
     * @param angle the angle to rotate about the Z axis in radians
     */
    public void rotZ(double angle) throws java.rmi.RemoteException;

   /**
     * Sets the translational value of this matrix to the Vector3f parameter 
     * values, and sets the other components of the matrix as if this
     * transform were an identity matrix.
     * @param trans  the translational component
     */
    public  void set(Vector3f trans) throws java.rmi.RemoteException;

    /**
     * Sets the scale component of the current transform; any existing
     * scale is first factored out of the existing transform before
     * the new scale is applied.
     * @param scale  the new scale amount
     * @since WSG
     */
    public  void setScale(float scale) throws java.rmi.RemoteException;

    /**
     * Sets the possibly non-uniform scale component of the current
     * transform; any existing scale is first factored out of the
     * existing transform before the new scale is applied.
     * @param scale  the new x,y,z scale values
     * @since WSG
     */
     public  void setScale(Vector3f scale) throws java.rmi.RemoteException;


    /** 
     * Replaces the translational components of this transform to the values
     * in the Vector3f argument; the other values of this transform are not 
     * modified.
     * @param trans  the translational component
     */
    public  void setTranslation(Vector3f trans) throws java.rmi.RemoteException;

    /** 
     * Sets the value of this matrix from the rotation expressed 
     * by the quaternion q1, the translation t1, and the scale s.
     * @param q1 the rotation expressed as a quaternion 
     * @param t1 the translation 
     * @param s the scale value 
     */  
    public  void set(Quat4f q1, Vector3f t1, float s) throws java.rmi.RemoteException;

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
    public  void set(Matrix3f m1, Vector3f t1, float s) throws java.rmi.RemoteException;

    /** 
     * Sets the matrix, type, and state of this transform to the matrix,
     * type, and state of transform t1. 
     * @param t1  the transform to be copied
     */  
    public  void set(Transform3DRemote t1) throws java.rmi.RemoteException;

   /**
     * Sets the matrix values of this transform to the matrix values in the
     * single precision array parameter.  The matrix type is classified  
     * internally by the Transform3D class. 
     * @param matrix  the single precision array of length 16 in row major format 
     */
    public  void set(float[] matrix) throws java.rmi.RemoteException;


    /**  
     * Sets the matrix values of this transform to the matrix values in the
     * single precision Matrix4f argument.  The transform type is classified
     * internally by the Transform3D class.
     * @param m1   the single precision 4x4 matrix
     */  
    public  void set(Matrix4f m1) throws java.rmi.RemoteException;


    /**  
     * Sets the rotational component (upper 3x3) of this transform to the 
     * matrix values in the single precision Matrix3f argument; the other 
     * elements of this transform are initialized as if this were an identity
     * matrix (i.e., affine matrix with no translational component).  
     * @param m1   the single precision 3x3 matrix 
     */   
    public  void set(Matrix3f m1) throws java.rmi.RemoteException;

    /**                                  
     * Places the values of this transform into the single precision array
     * of length 16.  The first four elements of the array will contain   
     * the top row of the transform matrix, etc. 
     * @param matrix  the single precision array of length 16
     */  
    public  void get(float[] matrix) throws java.rmi.RemoteException;


    /**
     * Places the normalized rotational component of this transform
     * into the 3x3 matrix argument.
     * @param m1  the matrix into which the rotational component is placed
     */  
    public  void get(Matrix3f m1) throws java.rmi.RemoteException;

    /**
     * Places the quaternion equivalent of the normalized rotational 
     * component of this transform into the quaternion parameter.
     * @param q1  the quaternion into which the rotation component is placed
     */
    public  void get(Quat4f q1) throws java.rmi.RemoteException;


    /**
     * Places the values of this transform into the single precision matrix
     * argument.
     * @param matrix   the single precision matrix
     */  
    public  void get(Matrix4f matrix) throws java.rmi.RemoteException;

   /**   
     * Places the quaternion equivalent of the normalized rotational
     * component of this transform into the quaternion parameter; 
     * places the translational component into the Vector parameter.
     * @param q1  the quaternion representing the rotation
     * @param t1  the translation component
     * @return  the scale component of this transform
     */ 
    public  float get(Quat4f q1, Vector3f t1) throws java.rmi.RemoteException;

    /**    
     * Places the normalized rotational component of this transform
     * into the matrix parameter; place the translational component
     * into the vector parameter.
     * @param m1  the normalized matrix representing the rotation 
     * @param t1  the translation component 
     * @return  the scale component of this transform
     */  
    public  float get(Matrix3f m1, Vector3f t1) throws java.rmi.RemoteException;

    /**
     * Returns the uniform scale factor of this matrix.
     * If the matrix has non-uniform scale factors, the largest of the 
     * x, y, and z scale factors will be returned.
     * @return  the scale factor of this matrix
     */
    public  double getScale() throws java.rmi.RemoteException;


    /** 
     * Retrieves the translational components of this transform. 
     * @param trans  the vector that will receive the translational component
     */
    public  void get(Vector3f trans) throws java.rmi.RemoteException;

    /**
     * Sets the value of this transform to the inverse of the passed
     * Transform3D parameter.  This method uses the transform type
     * to determine the optimal algorithm for inverting transform t1.
     * @param t1  the transform to be inverted
     * @exception SingularMatrixException thrown if transform t1 is
     * not invertible
     */
    public  void invert(Transform3DRemote t1) throws java.rmi.RemoteException;
    
    /**
     * Inverts this transform in place.  This method uses the transform
     * type to determine the optimal algorithm for inverting this transform.
     * @exception SingularMatrixException thrown if this transform is
     * not invertible
     */
    public  void invert() throws java.rmi.RemoteException;

    /** 
     * Calculates and returns the determinant of this transform.
     * @return  the double precision determinant
     */
     public  double determinant() throws java.rmi.RemoteException;
     
    /**
     * Sets the value of this transform to a scale and translation
     * matrix; the scale is not applied to the translation and all
     * of the matrix values are modified.
     * @param scale the scale factor for the transform
     * @param v1 the translation amount 
     */  
    public  void set(float scale, Vector3f v1) throws java.rmi.RemoteException;

    /** 
     * Sets the value of this transform to a scale and translation matrix;
     * the translation is scaled by the scale factor and all of the 
     * matrix values are modified.
     * @param v1 the translation amount
     * @param scale the scale factor for the transform AND the translation 
     */ 
    public  void set(Vector3f v1, float scale) throws java.rmi.RemoteException;


   /**
     * Multiplies each element of this transform by a scalar.
     * @param scalar  the scalar multiplier
     */
    public  void mul(double scalar) throws java.rmi.RemoteException;

   /**
     * Multiplies each element of transform t1 by a scalar and places
     * the result into this.  Transform t1 is not modified.
     * @param scalar  the scalar multiplier
     * @param t1  the original transform
     */
    public  void mul(double scalar, Transform3DRemote t1)  throws java.rmi.RemoteException;


    /**
     * Sets the value of this transform to the result of multiplying itself
     * with transform t1 (this = this * t1).
     * @param t1 the other transform
     */ 
    public  void mul(Transform3DRemote t1) throws java.rmi.RemoteException;

    /**  
     * Sets the value of this transform to the result of multiplying transform
     * t1 by transform t2 (this = t1*t2).
     * @param t1  the left transform
     * @param t2  the right transform
     */  
    public  void mul(Transform3DRemote t1, Transform3DRemote t2) throws java.rmi.RemoteException;

    /**
     * Multiplies this transform by the inverse of transform t1. The 
     * value is placed into this matrix (this = this*t1^-1).
     * @param t1  the matrix whose inverse is computed.
     */
    public  void mulInverse(Transform3DRemote t1) throws java.rmi.RemoteException;


    /** 
     * Multiplies transform t1 by the inverse of transform t2. The 
     * value is placed into this matrix (this = t1*t2^-1). 
     * @param t1  the left transform in the multiplication 
     * @param t2  the transform whose inverse is computed. 
     */ 
    public  void mulInverse(Transform3DRemote t1, Transform3DRemote t2) throws java.rmi.RemoteException;

    /** 
     * Multiplies transform t1 by the transpose of transform t2 and places
     * the result into this transform (this = t1 * transpose(t2)).
     * @param t1  the transform on the left hand side of the multiplication    
     * @param t2  the transform whose transpose is computed  
     */  
    public  void mulTransposeRight(Transform3DRemote t1, Transform3DRemote t2) throws java.rmi.RemoteException;

 
    /** 
     * Multiplies the transpose of transform t1 by transform t2 and places
     * the result into this matrix (this = transpose(t1) * t2).
     * @param t1  the transform whose transpose is computed
     * @param t2  the transform on the right hand side of the multiplication  
     */  
    public  void mulTransposeLeft(Transform3DRemote t1, Transform3DRemote t2) throws java.rmi.RemoteException;


    /**   
     * Multiplies the transpose of transform t1 by the transpose of
     * transform t2 and places the result into this transform
     * (this = transpose(t1) * transpose(t2)). 
     * @param t1  the transform on the left hand side of the multiplication  
     * @param t2  the transform on the right hand side of the multiplication  
     */  
    public  void mulTransposeBoth(Transform3DRemote t1, Transform3DRemote t2) throws java.rmi.RemoteException;

 
    /**
     * Normalizes the rotational components (upper 3x3) of this matrix
     * in place using a Singular Value Decomposition (SVD).
     * This operation ensures that the column vectors of this matrix
     * are orthogonal to each other.  The primary use of this method
     * is to correct for floating point errors that accumulate over
     * time when concatenating a large number of rotation matrices.
     * Note that the scale of the matrix is not altered by this method.
     */
    public  void normalize() throws java.rmi.RemoteException;

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
    public  void normalize(Transform3DRemote t1) throws java.rmi.RemoteException;

    /**     
     * Normalizes the rotational components (upper 3x3) of this transform 
     * in place using a Cross Product (CP) normalization. 
     * This operation ensures that the column vectors of this matrix
     * are orthogonal to each other.  The primary use of this method
     * is to correct for floating point errors that accumulate over
     * time when concatenating a large number of rotation matrices.
     * Note that the scale of the matrix is not altered by this method.
     */ 
    public  void normalizeCP() throws java.rmi.RemoteException;


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
    public  void normalizeCP(Transform3DRemote t1) throws java.rmi.RemoteException;


    /**
     * Returns true if the L-infinite distance between this matrix
     * and matrix m1 is less than or equal to the epsilon parameter,
     * otherwise returns false.  The L-infinite
     * distance is equal to
     * MAX[i=0,1,2,3 ; j=0,1,2,3 ; abs[(this.m(i,j) - m1.m(i,j)]
     * @param t1  the transform to be compared to this transform
     * @param epsilon  the threshold value
     */  
    public boolean epsilonEquals(Transform3DRemote t1, double epsilon) throws java.rmi.RemoteException;


    /** 
     * Transform the vector vec using this Transform and place the 
     * result into vecOut.
     * @param vec  the single precision vector to be transformed
     * @param vecOut  the vector into which the transformed values are placed
     */
    public  void transform(Vector4f vec, Vector4f vecOut) throws java.rmi.RemoteException;


    /**  
     * Transform the vector vec using this Transform and place the  
     * result back into vec. 
     * @param vec  the single precision vector to be transformed 
     */ 
    public  void transform(Vector4f vec) throws java.rmi.RemoteException;

    /**
     * Transforms the point parameter with this transform and
     * places the result into pointOut.  The fourth element of the
     * point input paramter is assumed to be one.
     * @param point  the input point to be transformed
     * @param pointOut  the transformed point
     */
    public  void transform(Point3f point, Point3f pointOut) throws java.rmi.RemoteException;


    /**
     * Transforms the point parameter with this transform and
     * places the result back into point.  The fourth element of the
     * point input paramter is assumed to be one.
     * @param point  the input point to be transformed
     */
    public  void transform(Point3f point) throws java.rmi.RemoteException;


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
    public  void transform(Vector3f normal, Vector3f normalOut) throws java.rmi.RemoteException;

    /**
     * Transforms the normal parameter by this transform and places the value
     * back into normal.  The fourth element of the normal is assumed to be zero.
     * Note: For correct lighting results, if a transform has uneven scaling 
     * surface normals should transformed by the inverse transpose of 
     * the transform. This the responsibility of the application and is not
     * done automatically by this method.
     * @param normal   the input normal to be transformed
     */
    public  void transform(Vector3f normal) throws java.rmi.RemoteException;


    /**
     * Replaces the upper 3x3 matrix values of this transform with the 
     * values in the matrix m1.
     * @param m1  the matrix that will be the new upper 3x3
     */
    public  void setRotationScale(Matrix3f m1) throws java.rmi.RemoteException;
   
    /**
     *  Scales transform t1 by a Uniform scale matrix with scale 
     *  factor s and then adds transform t2 (this = S*t1 + t2). 
     *  @param s  the scale factor
     *  @param t1 the transform to be scaled
     *  @param t2 the transform to be added
     */
    public  void scaleAdd(double s, Transform3DRemote t1, Transform3DRemote t2) throws java.rmi.RemoteException;


    /**
     *  Scales this transform by a Uniform scale matrix with scale factor 
     *  s and then adds transform t1 (this = S*this + t1).  
     *  @param s  the scale factor
     *  @param t1 the transform to be added
     */
    public  void scaleAdd(double s, Transform3DRemote t1) throws java.rmi.RemoteException;


    /**
     * Gets the upper 3x3 values of this matrix and places them into
     * the matrix m1.
     * @param m1  the matrix that will hold the values
     */
    public  void getRotationScale(Matrix3f m1) throws java.rmi.RemoteException;


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
			double near, double far) throws java.rmi.RemoteException;


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
			    double zNear, double zFar) throws java.rmi.RemoteException;


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
                        double top, double near, double far) throws java.rmi.RemoteException;


    org.jdesktop.lg3d.sg.Transform3D getWrapped() throws java.rmi.RemoteException;
}
