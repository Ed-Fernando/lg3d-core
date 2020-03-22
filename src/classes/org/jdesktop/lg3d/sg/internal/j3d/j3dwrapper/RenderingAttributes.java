/**
 * Project Looking Glass
 *
 * $RCSfile: RenderingAttributes.java,v $
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
 * $Date: 2004-06-23 18:50:42 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.j3d.j3dwrapper;

import org.jdesktop.lg3d.sg.internal.wrapper.RenderingAttributesWrapper;

/**
 * The RenderingAttributes object defines common rendering attributes
 * for all primitive types. The rendering attributes are:<p>
 * <ul>
 * <li>Alpha test function - used to compare the alpha test value with
 * each per-pixel alpha value. If the test passes, the pixel is
 * written, otherwise the pixel is not written. The alpha test
 * function is set with the <code>setAlphaTestFunction</code>
 * method. The alpha test
 * function is one of the following:</li><p>
 * <ul>
 * <li>ALWAYS - pixels are always drawn, irrespective of the alpha
 * value. This effectively disables alpha testing. This is
 * the default setting.</li><p>
 *
 * <li>NEVER - pixels are never drawn, irrespective of the alpha
 * value.</li><p>
 *
 * <li>EQUAL - pixels are drawn if the pixel alpha value is equal
 * to the alpha test value.</li><p>
 *
 * <li>NOT_EQUAL - pixels are drawn if the pixel alpha value is
 * not equal to the alpha test value.</li><p>
 * 
 * <li>LESS - pixels are drawn if the pixel alpha value is less
 * than the alpha test value.</li><p>
 * 
 * <li>LESS_OR_EQUAL - pixels are drawn if the pixel alpha value
 * is less than or equal to the alpha test value.</li><p>
 * 
 * <li>GREATER - pixels are drawn if the pixel alpha value is greater
 * than the alpha test value.</li><p>
 * 
 * <li>GREATER_OR_EQUAL - pixels are drawn if the pixel alpha
 * value is greater than or equal to the alpha test value.</li><p>
 * </ul>
 * <li>Alpha test value - the test value used by the alpha test function.
 * This value is compared to the alpha value of each rendered pixel.
 * The alpha test value is set with the <code>setAlphaTestValue</code>
 * method. The default alpha test value is 0.0.</li><p>
 * 
 * <li>Raster operation - the raster operation function for this
 * RenderingAttributes component object. The raster operation is
 * set with the <code>setRasterOp</code> method. The raster operation
 * is enabled or disabled with the <code>setRasterOpEnable</code>
 * method. The raster operation is one of the following:</li><p>
 * <ul>
 * <li>ROP_COPY - DST = SRC. This is the default operation.</li>
 * <li>ROP_XOR - DST = SRC ^ DST.</li><p>
 * </ul>
 * <li>Vertex colors - vertex colors can be ignored for this
 * RenderingAttributes object. This capability is set with the
 * <code>setIgnoreVertexColors</code> method. If 
 * ignoreVertexColors is false, per-vertex colors are used, when 
 * present in the associated geometry objects, taking
 * precedence over the ColoringAttributes color and the
 * specified Material color(s). If ignoreVertexColors is true, per-vertex
 * colors are ignored. In this case, if lighting is enabled, the 
 * Material diffuse color will be used as the object color.
 * if lighting is disabled, the ColoringAttributes color is
 * used. The default value is false.</li><p>
 * 
 * <li>Visibility flag - when set, invisible objects are
 * not rendered (subject to the visibility policy for
 * the current view), but they can be picked or collided with.
 * This flag is set with the <code>setVisible</code>
 * method. By default, the visibility flag is true.</li><p>
 * 
 * <li>Depth buffer - can be enabled or disabled for this
 * RenderingAttributes component object. The 
 * <code>setDepthBufferEnable</code> method enables
 * or disabled the depth buffer. The 
 * <code>setDepthBufferWriteEnable</code> method enables or disables
 * writing the depth buffer for this object. During the transparent
 * rendering pass, this attribute can be overridden by the
 * depthBufferFreezeTransparent attribute in the View
 * object. Transparent objects include BLENDED transparent and
 * antialiased lines and points. Transparent objects do not
 * include opaque objects or primitives rendered with
 * SCREEN_DOOR transparency. By default, the depth buffer
 * is enabled and the depth buffer write is enabled.</li><p>
 * </ul>
 *
 * @see Appearance
 * 
 */
public class RenderingAttributes extends NodeComponent implements RenderingAttributesWrapper {
    /**
     * Specifies that this RenderingAttributes object
     * allows reading its alpha test value component information.
     */
    public static final int
    ALLOW_ALPHA_TEST_VALUE_READ = CapabilityBits.RENDERING_ATTRIBUTES_ALLOW_ALPHA_TEST_VALUE_READ;

    /**
     * Specifies that this RenderingAttributes object
     * allows writing its alpha test value component information.
     */
    public static final int
    ALLOW_ALPHA_TEST_VALUE_WRITE = CapabilityBits.RENDERING_ATTRIBUTES_ALLOW_ALPHA_TEST_VALUE_WRITE;

    /**
     * Specifies that this RenderingAttributes object
     * allows reading its alpha test function component information.
     */
    public static final int
    ALLOW_ALPHA_TEST_FUNCTION_READ = CapabilityBits.RENDERING_ATTRIBUTES_ALLOW_ALPHA_TEST_FUNCTION_READ;

    /**
     * Specifies that this RenderingAttributes object
     * allows writing its alpha test function component information.
     */
    public static final int
    ALLOW_ALPHA_TEST_FUNCTION_WRITE = CapabilityBits.RENDERING_ATTRIBUTES_ALLOW_ALPHA_TEST_FUNCTION_WRITE;

    /**
     * Specifies that this RenderingAttributes object
     * allows reading its depth buffer enable and depth buffer write enable
     * component information.
     */
    public static final int
    ALLOW_DEPTH_ENABLE_READ = CapabilityBits.RENDERING_ATTRIBUTES_ALLOW_DEPTH_ENABLE_READ;

    /**
     * Specifies that this RenderingAttributes object
     * allows writing its depth buffer enable and depth buffer write enable
     * component information.
     *
     * @since Java 3D 1.3
     */
    public static final int ALLOW_DEPTH_ENABLE_WRITE =
    CapabilityBits.RENDERING_ATTRIBUTES_ALLOW_DEPTH_ENABLE_WRITE;

    /**
     * Specifies that this RenderingAttributes object
     * allows reading its visibility information.
     *
     * @since Java 3D 1.2
     */
    public static final int ALLOW_VISIBLE_READ =
    CapabilityBits.RENDERING_ATTRIBUTES_ALLOW_VISIBLE_READ;

    /**
     * Specifies that this RenderingAttributes object
     * allows writing its visibility information.
     *
     * @since Java 3D 1.2
     */
    public static final int ALLOW_VISIBLE_WRITE =
    CapabilityBits.RENDERING_ATTRIBUTES_ALLOW_VISIBLE_WRITE;

    /**
     * Specifies that this RenderingAttributes object
     * allows reading its ignore vertex colors information.
     *
     * @since Java 3D 1.2
     */
    public static final int ALLOW_IGNORE_VERTEX_COLORS_READ =
    CapabilityBits.RENDERING_ATTRIBUTES_ALLOW_IGNORE_VERTEX_COLORS_READ;

    /**
     * Specifies that this RenderingAttributes object
     * allows writing its ignore vertex colors information.
     *
     * @since Java 3D 1.2
     */
    public static final int ALLOW_IGNORE_VERTEX_COLORS_WRITE =
    CapabilityBits.RENDERING_ATTRIBUTES_ALLOW_IGNORE_VERTEX_COLORS_WRITE;

    /**
     * Specifies that this RenderingAttributes object
     * allows reading its raster operation information.
     *
     * @since Java 3D 1.2
     */
    public static final int ALLOW_RASTER_OP_READ =
    CapabilityBits.RENDERING_ATTRIBUTES_ALLOW_RASTER_OP_READ;

    /**
     * Specifies that this RenderingAttributes object
     * allows writing its raster operation information.
     *
     * @since Java 3D 1.2
     */
    public static final int ALLOW_RASTER_OP_WRITE =
    CapabilityBits.RENDERING_ATTRIBUTES_ALLOW_RASTER_OP_WRITE;

    /**
     * Indicates pixels are always drawn irrespective of alpha value.
     * This effectively disables alpha testing.
     */
    public static final int ALWAYS = 0;

    /**
     * Indicates pixels are never drawn irrespective of alpha value.
     */
    public static final int NEVER = 1;

    /**
     * Indicates pixels are  drawn if pixel alpha value is equal 
     * to alpha test value.
     */
    public static final int EQUAL = 2;

    /**
     * Indicates pixels are  drawn if pixel alpha value is not equal
     * to alpha test value.
     */
    public static final int NOT_EQUAL = 3;

    /**
     * Indicates pixels are  drawn if pixel alpha value is less 
     * than alpha test value.
     */
    public static final int LESS = 4;

    /**
     * Indicates pixels are  drawn if pixel alpha value is less
     * than or equal to alpha test value.
     */
    public static final int LESS_OR_EQUAL = 5;

    /**
     * Indicates pixels are  drawn if pixel alpha value is greater
     * than alpha test value.
     */
    public static final int GREATER = 6;

    /**
     * Indicates pixels are  drawn if pixel alpha value is greater
     * than or equal to alpha test value.
     */
    public static final int GREATER_OR_EQUAL = 7;


//    public static final int ROP_CLEAR = 0x0;
//    public static final int ROP_AND = 0x1;
//    public static final int ROP_AND_REVERSE = 0x2;

    /**
     * Raster operation: <code>DST = SRC</code>.
     * @see #setRasterOp
     * @since Java 3D 1.2
     */
    public static final int ROP_COPY = 0x3;

//    public static final int ROP_AND_INVERTED = 0x4;
//    public static final int ROP_NOOP = 0x5;

    /**
     * Raster operation: <code>DST = SRC ^ DST</code>.
     * @see #setRasterOp
     * @since Java 3D 1.2
     */
    public static final int ROP_XOR = 0x6;

//    public static final int ROP_OR = 0x7;
//    public static final int ROP_NOR = 0x8;
//    public static final int ROP_EQUIV = 0x9;
//    public static final int ROP_INVERT = 0xA;
//    public static final int ROP_OR_REVERSE = 0xB;
//    public static final int ROP_COPY_INVERTED = 0xC;
//    public static final int ROP_OR_INVERTED = 0xD;
//    public static final int ROP_NAND = 0xE;
//    public static final int ROP_SET = 0xF;


    /**
     * Constructs a RenderingAttributes object with default parameters.
     * The default values are as follows:
     * <ul>
     * depth buffer enable : true<br>
     * depth buffer write enable : true<br>
     * alpha test function : ALWAYS<br>
     * alpha test value : 0.0<br>
     * visible : true<br>
     * ignore vertex colors : false<br>
     * raster operation enable : false<br>
     * raster operation : ROP_COPY<br>
     * </ul>
     */
    public RenderingAttributes() {
    }
    
    protected void createWrapped() {
	wrapped = new javax.media.j3d.RenderingAttributes();
        wrapped.setUserData( this );
    }

    /**
     * Constructs a RenderingAttributes object with specified values.
     * @param depthBufferEnable a flag to turn depth buffer on/off
     * @param depthBufferWriteEnable a flag to to make depth buffer
     * read/write or read only
     * @param alphaTestValue the alpha test reference value
     * @param alphaTestFunction the function for comparing alpha values
     */
    public RenderingAttributes(boolean depthBufferEnable,
			       boolean depthBufferWriteEnable,
			       float alphaTestValue,
			       int alphaTestFunction){

	this(depthBufferEnable, depthBufferWriteEnable, alphaTestValue,
	     alphaTestFunction, true, false, false, ROP_COPY);
    }

    /**
     * Constructs a RenderingAttributes object with specified values
     * @param depthBufferEnable a flag to turn depth buffer on/off
     * @param depthBufferWriteEnable a flag to make depth buffer
     * read/write or read only
     * @param alphaTestValue the alpha test reference value
     * @param alphaTestFunction the function for comparing alpha values
     * @param visible a flag that specifies whether the object is visible
     * @param ignoreVertexColors a flag to enable or disable
     * the ignoring of per-vertex colors
     * @param rasterOpEnable a flag that specifies whether logical
     * raster operations are enabled for this RenderingAttributes object.
     * This disables all alpha blending operations.
     * @param rasterOp the logical raster operation, one of ROP_COPY or
     * ROP_XOR.
     *
     * @since Java 3D 1.2
     */
    public RenderingAttributes(boolean depthBufferEnable,
			       boolean depthBufferWriteEnable,
			       float alphaTestValue,
			       int alphaTestFunction,
			       boolean visible,
			       boolean ignoreVertexColors,
			       boolean rasterOpEnable,
			       int rasterOp) {
	
	setDepthBufferEnable(depthBufferEnable);
	setDepthBufferWriteEnable(depthBufferWriteEnable);
	setAlphaTestValue(alphaTestValue);
	setAlphaTestFunction(alphaTestFunction);
	setVisible(visible);
	setIgnoreVertexColors(ignoreVertexColors);
	setRasterOpEnable(rasterOpEnable);
	setRasterOp(rasterOp);
    }

    /**
     * Enables or disables depth buffer mode for this RenderingAttributes
     * component object.
     * @param state true or false to enable or disable depth buffer mode
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public void setDepthBufferEnable(boolean state){
	((javax.media.j3d.RenderingAttributes)wrapped).setDepthBufferEnable( state );
    }

    /**
     * Retrieves the state of zBuffer Enable flag
     * @return true if depth buffer mode is enabled, false
     * if depth buffer mode is disabled
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public boolean getDepthBufferEnable(){
	return ((javax.media.j3d.RenderingAttributes)wrapped).getDepthBufferEnable();
    }

    /**
     * Enables or disables writing the depth buffer for this object.
     * During the transparent rendering pass,
     * this attribute can be overridden by
     * the depthBufferFreezeTransparent attribute in the View object.
     * @param state true or false to enable or disable depth buffer Write mode
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     * @see View#setDepthBufferFreezeTransparent
     */
    public void setDepthBufferWriteEnable(boolean state) {
	((javax.media.j3d.RenderingAttributes)wrapped).setDepthBufferWriteEnable( state );
    }

    /**
     * Retrieves the state of Depth Buffer Write Enable flag.
     * @return true if depth buffer is writable, false
     * if depth buffer is read-only
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public boolean getDepthBufferWriteEnable(){
	return ((javax.media.j3d.RenderingAttributes)wrapped).getDepthBufferWriteEnable();
    }

    /**
     * Set alpha test value used by alpha test function.  This value is
     * compared to the alpha value of each rendered pixel.
     * @param value the alpha test value
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public void setAlphaTestValue(float value){
	((javax.media.j3d.RenderingAttributes)wrapped).setAlphaTestValue( value );
    }

    /**
     * Retrieves the alpha test value.
     * @return the alpha test value.
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public float getAlphaTestValue(){
	return ((javax.media.j3d.RenderingAttributes)wrapped).getAlphaTestValue();
    }

    /**
     * Set alpha test function.  This function is used to compare the
     * alpha test value with each per-pixel alpha value.  If the test
     * passes, the pixel is written otherwise the pixel is not
     * written.
     * @param function the new alpha test function.  One of
     * ALWAYS, NEVER, EQUAL, NOT_EQUAL, LESS, LESS_OR_EQUAL, GREATER,
     * GREATER_OR_EQUAL
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public void setAlphaTestFunction(int function){
	((javax.media.j3d.RenderingAttributes)wrapped).setAlphaTestFunction( function );
    }

    /**
     * Retrieves current alpha test function.
     * @return the current alpha test function
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public int getAlphaTestFunction(){
	return ((javax.media.j3d.RenderingAttributes)wrapped).getAlphaTestFunction();
    }

    /**
     * Sets the visibility flag for this RenderingAttributes
     * component object.  Invisible objects are not rendered (subject to
     * the visibility policy for the current view), but they can be picked
     * or collided with. The default value is true.
     * @param visible true or false to enable or disable visibility
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     *
     * @see View#setVisibilityPolicy
     *
     * @since Java 3D 1.2
     */
    public void setVisible(boolean visible) {
	((javax.media.j3d.RenderingAttributes)wrapped).setVisible( visible );
    }

    /**
     * Retrieves the visibility flag for this RenderingAttributes object.
     * @return true if the object is visible; false
     * if the object is invisible.
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.2
     */
    public boolean getVisible() {
	return ((javax.media.j3d.RenderingAttributes)wrapped).getVisible();
    }

    /**
     * Sets a flag that indicates whether vertex colors are ignored
     * for this RenderingAttributes object.  If
     * <code>ignoreVertexColors</code> is false, per-vertex
     * colors are used, when present in the associated Geometry
     * objects, taking precedence over the ColoringAttributes color
     * and the specified Material color(s).  If <code>ignoreVertexColors</code>
     * is true, per-vertex colors are ignored.  In this case, if
     * lighting is enabled, the Material diffuse color will be
     * used as the object color.  If lighting is disabled, the
     * ColoringAttributes color will be used.  The default value is false.
     *
     * @param ignoreVertexColors true or false to enable or disable
     * the ignoring of per-vertex colors
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     *
     * @see ColoringAttributes
     * @see Material
     *
     * @since Java 3D 1.2
     */
    public void setIgnoreVertexColors(boolean ignoreVertexColors) {
	((javax.media.j3d.RenderingAttributes)wrapped).setIgnoreVertexColors( ignoreVertexColors );
    }

    /**
     * Retrieves the ignoreVertexColors flag for this
     * RenderingAttributes object.
     * @return true if per-vertex colors are ignored; false
     * if per-vertex colors are used.
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.2
     */
    public boolean getIgnoreVertexColors() {
	return ((javax.media.j3d.RenderingAttributes)wrapped).getIgnoreVertexColors();
    }

    /**
     * Sets the rasterOp enable flag for this RenderingAttributes
     * component object.  When set to true, this enables logical
     * raster operations as specified by the setRasterOp method.
     * Enabling raster operations effectively disables alpha blending,
     * which is used for transparency and antialiasing.  Raster
     * operations, especially XOR mode, are primarily useful when
     * rendering to the front buffer in immediate mode.  Most
     * applications will not wish to enable this mode.
     *
     * @param rasterOpEnable true or false to enable or disable
     * raster operations
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     *
     * @see #setRasterOp
     *
     * @since Java 3D 1.2
     */
    public void setRasterOpEnable(boolean rasterOpEnable) {
	((javax.media.j3d.RenderingAttributes)wrapped).setRasterOpEnable( rasterOpEnable );
    }

    /**
     * Retrieves the rasterOp enable flag for this RenderingAttributes
     * object.
     * @return true if raster operations are enabled; false
     * if raster operations are disabled.
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.2
     */
    public boolean getRasterOpEnable() {
	return ((javax.media.j3d.RenderingAttributes)wrapped).getRasterOpEnable();
    }

    /**
     * Sets the raster operation function for this RenderingAttributes
     * component object.
     *
     * @param rasterOp the logical raster operation, one of ROP_COPY or
     * ROP_XOR
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.2
     */
    public void setRasterOp(int rasterOp) {
	((javax.media.j3d.RenderingAttributes)wrapped).setRasterOp( rasterOp );
    }

    /**
     * Retrieves the current raster operation for this RenderingAttributes
     * object.
     * @return one of ROP_COPY or ROP_XOR.
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.2
     */
    public int getRasterOp() {
	return ((javax.media.j3d.RenderingAttributes)wrapped).getRasterOp();
    }

}
