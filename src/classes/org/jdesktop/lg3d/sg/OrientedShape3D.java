/**
 * Project Looking Glass
 *
 * $RCSfile: OrientedShape3D.java,v $
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
 * $Date: 2005-06-24 19:48:21 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg;

import javax.media.j3d.CapabilityNotSetException;
import javax.vecmath.*;
import org.jdesktop.lg3d.sg.internal.wrapper.OrientedShape3DWrapper;

/**
 * The OrientedShape3D leaf node is a Shape3D node that is oriented
 * along a specified axis or about a specified point.  It defines an
 * alignment mode and a rotation point or axis.  This will cause
 * the local +<i>z</i> axis of the object to point at the viewer's eye
 * position. This is done regardless of the transforms above this
 * OrientedShape3D node in the scene graph.  It optionally defines a
 * scale value along with a constant scale enable flag that causes
 * this node to be scale invariant, subject only to its scale.
 *
 * <p>
 * OrientedShape3D is similar in functionality to the Billboard
 * behavior, but OrientedShape3D nodes will orient themselves
 * correctly for each view, and they can be used within a SharedGroup.
 *
 * <p>
 * If the alignment mode is ROTATE_ABOUT_AXIS, then the rotation will be
 * around the specified axis.  If the alignment mode is
 * ROTATE_ABOUT_POINT, then the rotation will be about the specified
 * point, with an additional rotation to align the +<i>y</i> axis of the
 * TransformGroup with the +<i>y</i> axis in the View.
 *
 * <p>
 * If the constant scale enable flag is set, the object will be drawn
 * the same size in absolute screen coordinates (meters), regardless
 * of the following: any transforms above this OrientedShape3D node in
 * the scene graph, the view scale, the window scale, or the effects
 * of perspective correction.  This is done by scaling the geometry
 * about the local origin of this node, such that 1 unit in local
 * coordinates is equal to the number of meters specified by this
 * node's scale value.  If the constant scale enable flag is set to
 * false, then the scale is not used.  The default scale is 1.0
 * meters.
 *
 * <p>
 * OrientedShape3D nodes are ideal for drawing screen-aligned text or
 * for drawing roughly-symmetrical objects.  A typical use might
 * consist of a quadrilateral that contains a texture of a tree.
 *
 */

public class OrientedShape3D extends Shape3D {

    /**
     * Specifies that rotation should be about the specified axis.
     * @see #setAlignmentMode
     */
    public static final int ROTATE_ABOUT_AXIS = 0;

    /**
     * Specifies that rotation should be about the specified point and
     * that the children's Y-axis should match the view object's Y-axis.
     * @see #setAlignmentMode
     */
    public static final int ROTATE_ABOUT_POINT = 1;

    /**
     * Specifies that no rotation is done.  The OrientedShape3D will
     * not be aligned to the view.
     * @see #setAlignmentMode
    */
    public static final int ROTATE_NONE = 2;


    /**
     * Specifies that this OrientedShape3D node
     * allows reading its alignment mode information.
     */
    public static final int ALLOW_MODE_READ =
	CapabilityBits.ORIENTED_SHAPE3D_ALLOW_MODE_READ;

    /**
     * Specifies that this OrientedShape3D node
     * allows writing its alignment mode information.
     */
    public static final int ALLOW_MODE_WRITE =
	CapabilityBits.ORIENTED_SHAPE3D_ALLOW_MODE_WRITE;

    /**
     * Specifies that this OrientedShape3D node
     * allows reading its alignment axis information.
     */
    public static final int ALLOW_AXIS_READ =
	CapabilityBits.ORIENTED_SHAPE3D_ALLOW_AXIS_READ;

    /**
     * Specifies that this OrientedShape3D node
     * allows writing its alignment axis information.
     */
    public static final int ALLOW_AXIS_WRITE =
	CapabilityBits.ORIENTED_SHAPE3D_ALLOW_AXIS_WRITE;

    /**
     * Specifies that this OrientedShape3D node
     * allows reading its rotation point information.
     */
    public static final int ALLOW_POINT_READ =
	CapabilityBits.ORIENTED_SHAPE3D_ALLOW_POINT_READ;

    /**
     * Specifies that this OrientedShape3D node
     * allows writing its rotation point information.
     */
    public static final int ALLOW_POINT_WRITE =
	CapabilityBits.ORIENTED_SHAPE3D_ALLOW_POINT_WRITE;

    /**
     * Specifies that this OrientedShape3D node
     * allows reading its scale and constant scale enable information.
     */
    public static final int ALLOW_SCALE_READ =
	CapabilityBits.ORIENTED_SHAPE3D_ALLOW_SCALE_READ;

    /**
     * Specifies that this OrientedShape3D node
     * allows writing its scale and constant scale enable information.
     */
    public static final int ALLOW_SCALE_WRITE =
	CapabilityBits.ORIENTED_SHAPE3D_ALLOW_SCALE_WRITE;


    /**
     * Constructs an OrientedShape3D node with default parameters.
     * The default values are as follows:
     * <ul>
     * alignment mode : ROTATE_ABOUT_AXIS<br>
     * alignment axis : Y-axis (0,1,0)<br>
     * rotation point : (0,0,1)<br>
     * constant scale enable : false<br>
     * scale : 1.0<br>
     *</ul>
     */
    public OrientedShape3D() {
	super();
    }


    /**
     * Constructs an OrientedShape3D node with the specified geometry
     * component, appearance component, mode, and axis.
     * The specified axis must not be parallel to the <i>Z</i>
     * axis--(0,0,<i>z</i>) for any value of <i>z</i>.  It is not
     * possible for the +<i>Z</i> axis to point at the viewer's eye
     * position by rotating about itself.  The target transform will
     * be set to the identity if the axis is (0,0,<i>z</i>).
     *
     * @param geometry the geometry component with which to initialize
     * this shape node
     * @param appearance the appearance component of the shape node
     * @param mode alignment mode, one of: ROTATE_ABOUT_AXIS,
     * ROTATE_ABOUT_POINT, or ROTATE_NONE
     * @param axis the ray about which the OrientedShape3D rotates
     */
    public OrientedShape3D(Geometry geometry,
			   Appearance appearance,
			   int mode,
			   Vector3f axis) {

	super(geometry, appearance);
        setAlignmentMode(mode);
        setAlignmentAxis(axis);
    }

    /**
     * Constructs an OrientedShape3D node with the specified geometry
     * component, appearance component, mode, and rotation point.
     *
     * @param geometry the geometry component with which to initialize
     * this shape node
     * @param appearance the appearance component of the shape node
     * @param mode alignment mode, one of: ROTATE_ABOUT_AXIS,
     * ROTATE_ABOUT_POINT, or ROTATE_NONE
     * @param point the position about which the OrientedShape3D rotates
     */
    public OrientedShape3D(Geometry geometry,
			   Appearance appearance,
			   int mode,
			   Point3f point) {

	super(geometry, appearance);
        setAlignmentMode(mode);
        setRotationPoint(point);

    }


    /**
     * Constructs an OrientedShape3D node with the specified geometry
     * component, appearance component, mode, axis, constant scale
     * enable flag, and scale
     * The specified axis must not be parallel to the <i>Z</i>
     * axis--(0,0,<i>z</i>) for any value of <i>z</i>.  It is not
     * possible for the +<i>Z</i> axis to point at the viewer's eye
     * position by rotating about itself.  The target transform will
     * be set to the identity if the axis is (0,0,<i>z</i>).
     *
     * @param geometry the geometry component with which to initialize
     * this shape node
     * @param appearance the appearance component of the shape node
     * @param mode alignment mode, one of: ROTATE_ABOUT_AXIS,
     * ROTATE_ABOUT_POINT, or ROTATE_NONE
     * @param axis the ray about which the OrientedShape3D rotates
     * @param constantScaleEnable a flag indicating whether to enable
     * constant scale
     * @param scale scale value used when constant scale is enabled
     */
    public OrientedShape3D(Geometry geometry,
			   Appearance appearance,
			   int mode,
			   Vector3f axis,
			   boolean constantScaleEnable,
			   float scale) {

	super(geometry, appearance);

        setAlignmentMode(mode);
        setAlignmentAxis(axis);
        setConstantScaleEnable(constantScaleEnable);
        setScale(scale);
    }

    /**
     * Constructs an OrientedShape3D node with the specified geometry
     * component, appearance component, mode, and rotation point.
     *
     * @param geometry the geometry component with which to initialize
     * this shape node
     * @param appearance the appearance component of the shape node
     * @param mode alignment mode, one of: ROTATE_ABOUT_AXIS,
     * ROTATE_ABOUT_POINT, or ROTATE_NONE
     * @param point the position about which the OrientedShape3D rotates
     * @param constantScaleEnable a flag indicating whether to enable
     * constant scale
     * @param scale scale value used when constant scale is enabled
     */
    public OrientedShape3D(Geometry geometry,
			   Appearance appearance,
			   int mode,
			   Point3f point,
			   boolean constantScaleEnable,
			   float scale) {

	super(geometry, appearance);

        setAlignmentMode(mode);
        setRotationPoint(point);
        setConstantScaleEnable(constantScaleEnable);
        setScale(scale);
    }


    protected void createWrapped() {
        wrapped = instantiate( SceneGraphSetup.getWrapperPackage()+"OrientedShape3D");
    }

    /**
     * Sets the alignment mode.
     *
     * @param mode alignment mode, one of: ROTATE_ABOUT_AXIS,
     * ROTATE_ABOUT_POINT, or ROTATE_NONE
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public void setAlignmentMode(int mode) {
        ((OrientedShape3DWrapper)wrapped).setAlignmentMode(mode);
    }


    /**
     * Retrieves the alignment mode.
     *
     * @return one of: ROTATE_ABOUT_AXIS, ROTATE_ABOUT_POINT,
     * or ROTATE_NONE
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public int getAlignmentMode() {
        return ((OrientedShape3DWrapper)wrapped).getAlignmentMode();
    }


    /**
     * Sets the new alignment axis.  This is the ray about which this
     * OrientedShape3D rotates when the mode is ROTATE_ABOUT_AXIS.
     * The specified axis must not be parallel to the <i>Z</i>
     * axis--(0,0,<i>z</i>) for any value of <i>z</i>.  It is not
     * possible for the +<i>Z</i> axis to point at the viewer's eye
     * position by rotating about itself.  The target transform will
     * be set to the identity if the axis is (0,0,<i>z</i>).
     *
     * @param axis the new alignment axis
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public void setAlignmentAxis(Vector3f axis) {
        ((OrientedShape3DWrapper)wrapped).setAlignmentAxis(axis);
    }


    /**
     * Sets the new alignment axis.  This is the ray about which this
     * OrientedShape3D rotates when the mode is ROTATE_ABOUT_AXIS.
     * The specified axis must not be parallel to the <i>Z</i>
     * axis--(0,0,<i>z</i>) for any value of <i>z</i>.  It is not
     * possible for the +<i>Z</i> axis to point at the viewer's eye
     * position by rotating about itself.  The target transform will
     * be set to the identity if the axis is (0,0,<i>z</i>).
     *
     * @param x the x component of the alignment axis
     * @param y the y component of the alignment axis
     * @param z the z component of the alignment axis
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public void setAlignmentAxis(float x, float y, float z) {
        ((OrientedShape3DWrapper)wrapped).setAlignmentAxis(x,y,z);
    }


    /**
     * Retrieves the alignment axis of this OrientedShape3D node,
     * and copies it into the specified vector.
     *
     * @param axis the vector that will contain the alignment axis
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public void getAlignmentAxis(Vector3f axis)  {
        ((OrientedShape3DWrapper)wrapped).getAlignmentAxis(axis);
    }

    /**
     * Sets the new rotation point.  This is the point about which the
     * OrientedShape3D rotates when the mode is ROTATE_ABOUT_POINT.
     *
     * @param point the new rotation point
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public void setRotationPoint(Point3f point) {
        ((OrientedShape3DWrapper)wrapped).setRotationPoint(point);
    }


    /**
     * Sets the new rotation point.  This is the point about which the
     * OrientedShape3D rotates when the mode is ROTATE_ABOUT_POINT.
     *
     * @param x the x component of the rotation point
     * @param y the y component of the rotation point
     * @param z the z component of the rotation point
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public void setRotationPoint(float x, float y, float z) {
        ((OrientedShape3DWrapper)wrapped).setRotationPoint(x,y,z);
    }


    /**
     * Retrieves the rotation point of this OrientedShape3D node,
     * and copies it into the specified vector.
     *
     * @param point the point that will contain the rotation point
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public void getRotationPoint(Point3f point)  {
        ((OrientedShape3DWrapper)wrapped).getRotationPoint(point);
    }


    /**
     * Sets the constant scale enable flag.
     *
     * @param constantScaleEnable a flag indicating whether to enable
     * constant scale
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public void setConstantScaleEnable(boolean constantScaleEnable) {
        ((OrientedShape3DWrapper)wrapped).setConstantScaleEnable(constantScaleEnable);
    }


    /**
     * Retrieves the constant scale enable flag.
     *
     * @return the current constant scale enable flag
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public boolean getConstantScaleEnable() {
        return ((OrientedShape3DWrapper)wrapped).getConstantScaleEnable();
    }


    /**
     * Sets the scale for this OrientedShape3D.  This scale is used when
     * the constant scale enable flag is set to true.
     *
     * @param scale the scale value
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public void setScale(float scale) {
        ((OrientedShape3DWrapper)wrapped).setScale(scale);
    }


    /**
     * Retrieves the scale value for this OrientedShape3D.
     *
     * @return the current scale value
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public float getScale() {
        return ((OrientedShape3DWrapper)wrapped).getScale();
    }

}
