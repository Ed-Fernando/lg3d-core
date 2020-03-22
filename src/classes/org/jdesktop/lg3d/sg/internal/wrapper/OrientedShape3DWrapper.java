/**
 * Project Looking Glass
 *
 * $RCSfile: OrientedShape3DWrapper.java,v $
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
 * $Date: 2005-06-24 19:48:31 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.wrapper;

import javax.vecmath.*;

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

public interface OrientedShape3DWrapper extends Shape3DWrapper {

    /**
     * Sets the alignment mode.
     *
     * @param mode alignment mode, one of: ROTATE_ABOUT_AXIS,
     * ROTATE_ABOUT_POINT, or ROTATE_NONE
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public void setAlignmentMode(int mode);


    /**
     * Retrieves the alignment mode.
     *
     * @return one of: ROTATE_ABOUT_AXIS, ROTATE_ABOUT_POINT,
     * or ROTATE_NONE
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public int getAlignmentMode();


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
    public void setAlignmentAxis(Vector3f axis);


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
    public void setAlignmentAxis(float x, float y, float z);


    /**
     * Retrieves the alignment axis of this OrientedShape3D node,
     * and copies it into the specified vector.
     *
     * @param axis the vector that will contain the alignment axis
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public void getAlignmentAxis(Vector3f axis);

    /**
     * Sets the new rotation point.  This is the point about which the
     * OrientedShape3D rotates when the mode is ROTATE_ABOUT_POINT.
     *
     * @param point the new rotation point
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public void setRotationPoint(Point3f point);


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
    public void setRotationPoint(float x, float y, float z);


    /**
     * Retrieves the rotation point of this OrientedShape3D node,
     * and copies it into the specified vector.
     *
     * @param point the point that will contain the rotation point
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public void getRotationPoint(Point3f point);


    /**
     * Sets the constant scale enable flag.
     *
     * @param constantScaleEnable a flag indicating whether to enable
     * constant scale
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public void setConstantScaleEnable(boolean constantScaleEnable);


    /**
     * Retrieves the constant scale enable flag.
     *
     * @return the current constant scale enable flag
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public boolean getConstantScaleEnable();


    /**
     * Sets the scale for this OrientedShape3D.  This scale is used when
     * the constant scale enable flag is set to true.
     *
     * @param scale the scale value
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public void setScale(float scale);


    /**
     * Retrieves the scale value for this OrientedShape3D.
     *
     * @return the current scale value
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public float getScale();

}
