/**
 * Project Looking Glass
 *
 * $RCSfile: LineAttributes.java,v $
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
 * $Date: 2005-01-20 22:05:41 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.sg.internal.j3d.j3dwrapper;

import org.jdesktop.lg3d.sg.internal.wrapper.LineAttributesWrapper;

/**
 * The LineAttributes object defines all rendering state that can be set
 * as a component object of a Shape3D node.
 * The line attributes that can be defined are:<P>
 * <UL><LI>Pattern - specifies the pattern used to draw the line:<p>
 * <ul>
 * <li>PATTERN_SOLID - draws a solid line with no pattern. This is
 * the default.</li>
 * <p>
 * <li>PATTERN_DASH - draws dashed lines. Ideally, these will be drawn with
 * a repeating pattern of 8 pixels on and 8 pixels off.</li>
 * <p>
 * <li>PATTERN_DOT - draws dotted lines. Ideally, these will be drawn with
 * a repeating pattern of 1 pixel on and 7 pixels off.</li>
 * <p>
 * <li>PATTERN_DASH_DOT - draws dashed-dotted lines. Ideally, these will be
 * drawn with a repeating pattern of 7 pixels on, 4 pixels off, 1 pixel on, 
 * and 4 pixels off.</li>
 * <p>
 * <li>PATTERN_USER_DEFINED - draws lines with a user-defined line pattern.
 * See "User-defined Line Patterns," below.</li><p>
 * </ul>
 * <p>
 * <LI>Antialiasing (enabled or disabled). By default, antialiasing
 * is disabled.</LI>
 * <p>
 * <p>
 * If antialiasing is enabled, the lines are considered transparent
 * for rendering purposes.  They are rendered with all the other transparent 
 * objects and adhere to the other transparency settings such as the
 * View transparency sorting policy and the View depth buffer freeze 
 * transparent enable.
 * </p> 
 * <LI>Width (in pixels). The default is a line width of one pixel.
 * </LI></UL><p>
 *
 * <b>User-defined Line Patterns</b>
 * <p>
 * A user-defined line pattern is specified with a pattern mask and 
 * an optional scale factor.
 * <p>
 * The Pattern Mask<p>
 *
 * The pattern is specified
 * using a 16-bit mask that specifies on and off segments. Bit 0 in 
 * the pattern mask corresponds to the first pixel of the line or line 
 * strip primitive. A value of 1 for a bit in the pattern mask indicates 
 * that the corresponding pixel is drawn, while a value of 0
 * indicates that the corresponding pixel is not drawn. After all 16 bits 
 * in the pattern are used, the pattern is repeated.
 * <p>
 * For example, a mask of 0x00ff defines a dashed line with a repeating
 * pattern of 8 pixels on followed by 8 pixels off. A value of 0x0101 
 * defines a a dotted line with a repeating pattern of 1 pixel on and 7 
 * pixels off.
 * <p>
 * The pattern continues around individual line segments of a line strip
 * primitive. It is restarted at the beginning of each new line strip. 
 * For line array primitives, the pattern is restarted at the beginning 
 * of each line.
 * <p>
 * The Scale Factor
 * <p>
 * The pattern is multiplied by the scale factor such that each bit in
 * the pattern mask corresponds to that many consecutive pixels.
 * For example, a scale factor of 3 applied to a pattern mask of 0x001f 
 * would produce a repeating pattern of 15 pixels on followed by 33
 * pixels off. The valid range for this attribute is [1,15]. Values
 * outside this range are clamped.<p>
 * 
 * @see Appearance
 * @see View
 */
public class LineAttributes extends NodeComponent implements LineAttributesWrapper {

    /**
     * Specifies that this LineAttributes object allows reading its
     * line width information.
     */
    public static final int
    ALLOW_WIDTH_READ = CapabilityBits.LINE_ATTRIBUTES_ALLOW_WIDTH_READ;

    /**
     * Specifies that this LineAttributes object allows writing its
     * line width information.
     */
    public static final int
    ALLOW_WIDTH_WRITE = CapabilityBits.LINE_ATTRIBUTES_ALLOW_WIDTH_WRITE;

    /**
     * Specifies that this LineAttributes object allows reading its
     * line pattern information.
     */
    public static final int
    ALLOW_PATTERN_READ = CapabilityBits.LINE_ATTRIBUTES_ALLOW_PATTERN_READ;

    /**
     * Specifies that this LineAttributes object allows writing its
     * line pattern information.
     */
    public static final int
    ALLOW_PATTERN_WRITE = CapabilityBits.LINE_ATTRIBUTES_ALLOW_PATTERN_WRITE;

    /**
     * Specifies that this LineAttributes object allows reading its
     * line antialiasing flag.
     */
    public static final int
    ALLOW_ANTIALIASING_READ = CapabilityBits.LINE_ATTRIBUTES_ALLOW_ANTIALIASING_READ;

    /**
     * Specifies that this LineAttributes object allows writing its
     * line antialiasing flag.
     */
    public static final int
    ALLOW_ANTIALIASING_WRITE = CapabilityBits.LINE_ATTRIBUTES_ALLOW_ANTIALIASING_WRITE;


    /**
     * Draw solid lines with no pattern.
     * @see #setLinePattern
     */
    public static final int PATTERN_SOLID = 0;

    /**
     * Draw dashed lines.  Ideally, these will be drawn with
     * a repeating pattern of 8 pixels on and 8 pixels off.
     * @see #setLinePattern
     */
    public static final int PATTERN_DASH = 1;

    /**
     * Draw dotted lines.  Ideally, these will be drawn with
     * a repeating pattern of 1 pixel on and 7 pixels off.
     * @see #setLinePattern
     */
    public static final int PATTERN_DOT = 2;

    /**
     * Draw dashed-dotted lines.  Ideally, these will be drawn with
     * a repeating pattern of 7 pixels on, 4 pixels off, 1 pixel on,
     * and 4 pixels off.
     * @see #setLinePattern
     */
    public static final int PATTERN_DASH_DOT = 3;

    /**
     * Draw lines with a user-defined line pattern.  The line pattern
     * is specified with a pattern mask and scale factor.
     * @see #setLinePattern
     * @see #setPatternMask
     * @see #setPatternScaleFactor
     *
     * @since Java 3D 1.2
     */
    public static final int PATTERN_USER_DEFINED = 4;


    /**
     * Constructs a LineAttributes object with default parameters.
     * The default values are as follows:
     * <ul>
     * line width : 1<br>
     * line pattern : PATTERN_SOLID<br>
     * pattern mask : 0xffff<br>
     * pattern scale factor : 1<br>
     * line antialiasing : false<br>
     * </ul>
     */
     public LineAttributes(){
     }

    /**
     * Constructs a LineAttributes object with specified values.
     * @param lineWidth the width of lines in pixels
     * @param linePattern the line pattern, one of PATTERN_SOLID,
     * PATTERN_DASH, PATTERN_DOT, or PATTERN_DASH_DOT
     * @param lineAntialiasing flag to set line antialising ON or OFF
     */
     public LineAttributes(float lineWidth, int linePattern,
			   boolean lineAntialiasing){

         setLineWidth(lineWidth);
         setLinePattern(linePattern);
         setLineAntialiasingEnable(lineAntialiasing);
     }

    protected void createWrapped() {
	wrapped = new javax.media.j3d.LineAttributes();
        wrapped.setUserData( this );
    }
    /**
     * Sets the line width for this LineAttributes component object.
     * @param lineWidth the width, in pixels, of line primitives
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setLineWidth(float lineWidth) {
        ((javax.media.j3d.LineAttributes)wrapped).setLineWidth(lineWidth);
    }

    /**
     * Gets the line width for this LineAttributes component object.
     * @return the width, in pixels, of line primitives
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public float getLineWidth() {
        return ((javax.media.j3d.LineAttributes)wrapped).getLineWidth();
    }

    /**
     * Sets the line pattern for this LineAttributes component object.
     * @param linePattern the line pattern to be used, one of:
     * PATTERN_SOLID, PATTERN_DASH, PATTERN_DOT, PATTERN_DASH_DOT, or
     * PATTERN_USER_DEFINED.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setLinePattern(int linePattern) {
        ((javax.media.j3d.LineAttributes)wrapped).setLinePattern(linePattern);
    }

    /**
     * Gets the line pattern for this LineAttributes component object.
     * @return the line pattern
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public int getLinePattern() {
        return ((javax.media.j3d.LineAttributes)wrapped).getLinePattern();
    }


    /**
     * Sets the line pattern mask to the specified value.  This is
     * used when the linePattern attribute is set to
     * PATTERN_USER_DEFINED.  In this mode, the pattern is specified
     * using a 16-bit mask that specifies on and off segments.  Bit 0
     * in the pattern mask corresponds to the first pixel of the line
     * or line strip primitive.  A value of 1 for a bit in the pattern
     * mask indicates that the corresponding pixel is drawn, while a
     * value of 0 indicates that the corresponding pixel is not drawn.
     * After all 16 bits in the pattern are used, the pattern is
     * repeated.  For example, a mask of 0x00ff defines a dashed line
     * with a repeating pattern of 8 pixels on followed by 8 pixels
     * off.  A value of 0x0101 defines a a dotted line with a
     * repeating pattern of 1 pixel on and 7 pixels off
     * <p>
     * The pattern continues around individual line segments of a line
     * strip primitive.  It is restarted at the beginning of each new
     * line strip.  For line array primitives, the pattern is
     * restarted at the beginning of each line.
     * @param mask the new line pattern mask
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     * @see #setPatternScaleFactor
     *
     * @since Java 3D 1.2
     */
    public void setPatternMask(int mask) {
        ((javax.media.j3d.LineAttributes)wrapped).setPatternMask(mask);
    }


    /**
     * Retrieves the line pattern mask.
     * @return the line pattern mask
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.2
     */
    public int getPatternMask() {
        return ((javax.media.j3d.LineAttributes)wrapped).getPatternMask();
    }


    /**
     * Sets the line pattern scale factor to the specified value.
     * This is used in conjunction with the patternMask when the
     * linePattern attribute is set to PATTERN_USER_DEFINED.  The
     * pattern is multiplied by the scale factor such that each bit in
     * the pattern mask corresponds to that many consecutive pixels.
     * For example, a scale factor of 3 applied to a pattern mask of
     * 0x001f would produce a repeating pattern of 15 pixels on
     * followed by 33 pixels off. The valid range for this attribute
     * is [1,15].  Values outside this range are clamped.
     * @param scaleFactor the new line pattern scale factor
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     * @see #setPatternMask
     *
     * @since Java 3D 1.2
     */
    public void setPatternScaleFactor(int scaleFactor) {
        ((javax.media.j3d.LineAttributes)wrapped).setPatternScaleFactor(scaleFactor);
    }


    /**
     * Retrieves the line pattern scale factor.
     * @return the line pattern scale factor
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.2
     */
    public int getPatternScaleFactor() {
        return ((javax.media.j3d.LineAttributes)wrapped).getPatternScaleFactor();
    }


    /**
     * Enables or disables line antialiasing
     * for this LineAttributes component object.
     * <p>
     * If antialiasing is enabled, the lines are considered transparent
     * for rendering purposes.  They are rendered with all the other 
     * transparent objects and adhere to the other transparency settings 
     * such as the View transparency sorting policy and the View depth buffer 
     * freeze transparent enable.
     * </p> 
     * @param state true or false to enable or disable line antialiasing
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     * @see View
     */
    public void setLineAntialiasingEnable(boolean state) {
        ((javax.media.j3d.LineAttributes)wrapped).setLineAntialiasingEnable(state);
    }

    /**
     * Retrieves the state of the line antialiasing flag.
     * @return true if line antialiasing is enabled,
     * false if line antialiasing is disabled
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public boolean getLineAntialiasingEnable() {
        return ((javax.media.j3d.LineAttributes)wrapped).getLineAntialiasingEnable();
    }

}
