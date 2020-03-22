/**
 * Project Looking Glass
 *
 * $RCSfile: ColoringAttributesRemote.java,v $
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
 * $Date: 2004-06-23 18:51:06 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.rmi.rmiserver;

import javax.vecmath.Color3f;

/**
 * The ColoringAttributes object defines attributes used in
 * color selection and shading model.
 *
 * <p>
 * <b>Color</b>
 * <p>
 * The <code>setColor</code> methods set the current intrinsic red, green, and
 * blue color values of this ColoringAttributes component object.
 * This color is only used for unlit geometry. If lighting is enabled, 
 * the material colors are used in the lighting equation to produce
 * the final color.  When vertex colors are present in unlit
 * geometry, those vertex colors are used in place of this
 * ColoringAttributes color, unless the vertex colors are ignored.
 * <p>
 * There are two variations on the <code>setColor</code> methods, one
 * that takes a Color3f and one that takes three floats. No alpha
 * value is allowed (it's automatically set to 1.0). The float values
 * range between 0.0 and 1.0, with 1.0 being full intensity of the
 * color. A color value of (1.0, 1.0, 1.0) is white.
 * <p>
 * <b>Shading Model</b>
 * <p>
 * The <code>setShadeModel</code> method sets the shade model for this 
 * ColoringAttributes component object. The shade model may be one of 
 * the following:<p>
 * <ul>
 * <li>FASTEST - use the fastest available method for shading. This
 * shading mode maps to whatever shading model the Java 3D implementor
 * defines as the "fastest," which may be hardware-dependent.</li>
 * <p>
 * <li>NICEST - use the nicest (highest quality) available method 
 * for shading. This shading mode maps to whatever shading model
 * the Java 3D implementor defines as the "nicest," shading
 * model, which may be hardware-dependent.</li>
 * <p>
 * <li>SHADE_FLAT -  use the flat shading model. This shading model
 * does not interpolate color across the primitive.
 * The primitive is drawn with a single color
 * and the color of one vertex of the primitive is duplicated 
 * across all the vertices of the primitive.</li>
 * <p>
 * <li>SHADE_GOURAUD - use the Gouraud (smooth) shading model.
 * This shading model smoothly interpolates the color at each vertex 
 * across the primitive.
 * The primitive is drawn with many different colors
 * and the color at each vertex is treated individually. For lines,
 * the colors along the line segment are interpolated between
 * the vertex colors. This is the default shade model if no other
 * is specified.</li>
 * <p></ul>
 *
 * @see Appearance
 */
public interface ColoringAttributesRemote extends NodeComponentRemote {
    /**
     * Specifies that this ColoringAttributes object allows
     * reading its color component information.
     */
    public static final int
    ALLOW_COLOR_READ = CapabilityBits.COLORING_ATTRIBUTES_ALLOW_COLOR_READ;

    /**
     * Specifies that this ColoringAttributes object allows
     * writing its color component information.
     */
    public static final int
    ALLOW_COLOR_WRITE = CapabilityBits.COLORING_ATTRIBUTES_ALLOW_COLOR_WRITE;

    /**
     * Specifies that this ColoringAttributes object allows
     * reading its shade model component information.
     */
    public static final int
    ALLOW_SHADE_MODEL_READ = CapabilityBits.COLORING_ATTRIBUTES_ALLOW_SHADE_MODEL_READ;

    /**
     * Specifies that this ColoringAttributes object allows
     * writing its shade model component information.
     */
    public static final int
    ALLOW_SHADE_MODEL_WRITE = CapabilityBits.COLORING_ATTRIBUTES_ALLOW_SHADE_MODEL_WRITE;

    /**
     * Use the fastest available method for shading.
     */
    public static final int FASTEST            = 0;
    /**
     * Use the nicest available method for shading.
     */
    public static final int NICEST             = 1;

    /**
     * Do not interpolate color across the primitive.
     */
    public static final int SHADE_FLAT   = 2;
    /**
     * Smoothly interpolate the color at each vertex across the primitive.
     */
    public static final int SHADE_GOURAUD = 3;

    /**
     * Sets the intrinsic color of this ColoringAttributes
     * component object.  This color is only used for unlit geometry;
     * if lighting is enabled, then the material colors are used in the
     * lighting equation to produce the final color.
     * When vertex colors are present in unlit geometry, those
     * vertex colors are used in place of this ColoringAttributes color
     * unless the vertex colors are ignored.
     * @param color the color that is used when lighting is disabled
     * or when material is null
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     * @see Material
     * @see RenderingAttributes#setIgnoreVertexColors
     */
    public void setColor(Color3f color) throws java.rmi.RemoteException;

    /**
     * Sets the intrinsic color of this ColoringAttributes
     * component object.  This color is only used for unlit geometry;
     * if lighting is enabled, then the material colors are used in the
     * lighting equation to produce the final color.
     * When vertex colors are present in unlit geometry, those
     * vertex colors are used in place of this ColoringAttributes color
     * unless the vertex colors are ignored.
     * @param r the red component of the color
     * @param g the green component of the color
     * @param b the blue component of the color
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     * @see Material
     * @see RenderingAttributes#setIgnoreVertexColors
     */
    public void setColor(float r, float g, float b) throws java.rmi.RemoteException;

    /**
     * Gets the intrinsic color of this ColoringAttributes
     * component object.
     * @param color the vector that will receive color
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public void getColor(Color3f color) throws java.rmi.RemoteException;

    /**
     * Sets the shade mode for this ColoringAttributes component object.
     * @param shadeModel the shade mode to be used; one of FASTEST,
     * NICEST, SHADE_FLAT, or SHADE_GOURAUD
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public void setShadeModel(int shadeModel) throws java.rmi.RemoteException;

    /**
     * Gets the shade mode for this ColoringAttributes component object.
     * @return shadeModel the shade mode
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public int getShadeModel() throws java.rmi.RemoteException;


}
