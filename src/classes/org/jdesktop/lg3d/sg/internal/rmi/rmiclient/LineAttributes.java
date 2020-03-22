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
 * $Date: 2005-01-20 22:05:42 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.sg.internal.rmi.rmiclient;

import org.jdesktop.lg3d.sg.internal.wrapper.LineAttributesWrapper;
import org.jdesktop.lg3d.sg.internal.rmi.rmiserver.LineAttributesRemote;

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
        try {
            remote = SceneGraphSetup.getSGObjectFactory().newInstance(org.jdesktop.lg3d.sg.internal.rmi.rmiserver.LineAttributes.class);
            setRemote( remote );
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
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

    /**
     * Sets the line width for this LineAttributes component object.
     * @param lineWidth the width, in pixels, of line primitives
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setLineWidth(float lineWidth) {
       try {
            ((LineAttributesRemote)remote).setLineWidth(lineWidth);
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
    }

    /**
     * Gets the line width for this LineAttributes component object.
     * @return the width, in pixels, of line primitives
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public float getLineWidth() {
       try {
            return ((LineAttributesRemote)remote).getLineWidth();
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
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
       try {
            ((LineAttributesRemote)remote).setLinePattern(linePattern);
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
    }

    /**
     * Gets the line pattern for this LineAttributes component object.
     * @return the line pattern
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public int getLinePattern() {
       try {
            return ((LineAttributesRemote)remote).getLinePattern();
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
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
       try {
            ((LineAttributesRemote)remote).setPatternMask(mask);
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
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
       try {
            return ((LineAttributesRemote)remote).getPatternMask();
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
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
       try {
            ((LineAttributesRemote)remote).setPatternScaleFactor(scaleFactor);
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
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
       try {
            return ((LineAttributesRemote)remote).getPatternScaleFactor();
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
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
       try {
            ((LineAttributesRemote)remote).setLineAntialiasingEnable(state);
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
    }

    /**
     * Retrieves the state of the line antialiasing flag.
     * @return true if line antialiasing is enabled,
     * false if line antialiasing is disabled
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public boolean getLineAntialiasingEnable() {
       try {
            return ((LineAttributesRemote)remote).getLineAntialiasingEnable();
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
    }

}
