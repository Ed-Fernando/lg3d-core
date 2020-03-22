/**
 * Project Looking Glass
 *
 * $RCSfile: ImageComponentRemote.java,v $
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
 * $Date: 2004-06-23 18:51:13 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.rmi.rmiserver;

/**
 * Abstract class that is used to define 2D or 3D ImageComponent
 * classes used in a Java 3D scene graph.  This is used for texture
 * images, background images and raster components of Shape3D nodes.
 *
 * <p>
 * Image data may be passed to this ImageComponent object in
 * one of two ways: by copying the image data into this object or by
 * accessing the image data by reference.
 *
 * <p>
 * <ul>
 * <li>
 * <b>By Copying:</b>
 * By default, the set and get image methods copy the image
 * data into or out of this ImageComponent object.  This is
 * appropriate for many applications, since the application may reuse
 * the RenderedImage object after copying it to the ImageComponent.
 * </li>
 * <li><b>By Reference:</b>
 * A new feature in Java 3D version 1.2 allows image data to
 * be accessed by reference, directly from the RenderedImage object.
 * To use this feature, you need to construct an ImageComponent object
 * with the <code>byReference</code> flag set to <code>true</code>.
 * In this mode, a reference to the input data is saved, but the data
 * itself is not necessarily copied (although it may be, depending on
 * the value of the <code>yUp</code> flag, the format of the
 * ImageComponent, and the format of the RenderedImage).  Image data
 * referenced by an ImageComponent object can only be modified via
 * the updateData method.
 * Applications must exercise care not to violate this rule.  If any
 * referenced RenderedImage is modified outside the updateData method
 * after it has been passed
 * to an ImageComponent object, the results are undefined.
 * Another restriction in by-reference mode is that if the specified
 * RenderedImage is not an instance of BufferedImage, then
 * this ImageComponent cannot be used for readRaster or
 * off-screen rendering operations, since these operations modify
 * the ImageComponent data.
 * </li>
 * </ul>
 *
 * <p>
 * An image component object also specifies whether the orientation of
 * its image data is "y-up" or "y-down" (the default).  Y-up mode
 * causes images to be interpreted as having their origin at the lower
 * left (rather than the default upper left) of a texture or raster
 * image with successive scan lines moving up.  This is more
 * consistent with texture mapping data onto a surface, and maps
 * directly into the the way textures are used in OpenGL and other 3D
 * APIs.  Setting the <code>yUp</code> flag to true in conjunction
 * with setting the <code>byReference</code> flag to true makes it
 * possible for Java 3D to avoid copying the texture map in some
 * cases.
 *
 * <p>
 * Note that all color fields are treated as unsigned values, even though
 * Java does not directly support unsigned variables.  This means, for
 * example, that an ImageComponent using a format of FORMAT_RGB5 can
 * represent red, green, and blue values between 0 and 31, while an
 * ImageComponent using a format of FORMAT_RGB8 can represent color
 * values between 0 and 255.  Even when byte values are used to create a
 * RenderedImage with 8-bit color components, the resulting colors
 * (bytes) are interpreted as if they were unsigned.
 * Values greater than 127 can be assigned to a byte variable using a
 * type cast.  For example:
 * <ul>byteVariable = (byte) intValue; // intValue can be > 127</ul>
 * If intValue is greater than 127, then byteVariable will be negative.  The
 * correct value will be extracted when it is used (by masking off the upper
 * bits).
 */

public interface ImageComponentRemote extends NodeComponentRemote {
    //
    // Pixel format values
    //

  /**
   * Specifies that each pixel contains 3 8-bit channels: one each
   * for red, green, blue. Same as FORMAT_RGB8.
   */
    public static final int FORMAT_RGB = 1;

  /**
   * Specifies that each pixel contains 4 8-bit channels: one each
   * for red, green, blue, alpha. Same as FORMAT_RGBA8.
   */
    public static final int FORMAT_RGBA = 2;

  /**
   * Specifies that each pixel contains 3 8-bit channels: one each
   * for red, green, blue. Same as FORMAT_RGB.
   */
    public static final int FORMAT_RGB8 = FORMAT_RGB;

  /**
   * Specifies that each pixel contains 4 8-bit channels: one each
   * for red, green, blue, alpha. Same as FORMAT_RGBA.
   */
    public static final int FORMAT_RGBA8 = FORMAT_RGBA;

  /**
   * Specifies that each pixel contains 3 5-bit channels: one each
   * for red, green, blue.
   */
    public static final int FORMAT_RGB5 = 3;

  /**
   * Specifies that each pixel contains 3 5-bit channels: one each
   * for red, green, blue and 1 1-bit channel for alpha.
   */
    public static final int FORMAT_RGB5_A1 = 4;

  /**
   * Specifies that each pixel contains 3 4-bit channels: one each
   * for red, green, blue.
   */
    public static final int FORMAT_RGB4 = 5;

  /**
   * Specifies that each pixel contains 4 4-bit channels: one each
   * for red, green, blue, alpha.
   */
    public static final int FORMAT_RGBA4 = 6;

  /**
   * Specifies that each pixel contains 2 4-bit channels: one each
   * for luminance and alpha.
   */
    public static final int FORMAT_LUM4_ALPHA4 = 7;

  /**
   * Specifies that each pixel contains 2 8-bit channels: one each
   * for luminance and alpha.
   */
    public static final int FORMAT_LUM8_ALPHA8 = 8;

  /**
   * Specifies that each pixel contains 2 3-bit channels: one each
   * for red, green, and 1 2-bit channel for blue.
   */
    public static final int FORMAT_R3_G3_B2 = 9;

  /**
   * Specifies that each pixel contains 1 8-bit channel: it can be
   * used for only luminance or only alpha or only intensity.
   */
    public static final int FORMAT_CHANNEL8 = 10;

  // Internal variable for checking validity of formats
  // Change this if any more formats are added or removed
    static final int FORMAT_TOTAL = 10;

    /**
     * Specifies that this ImageComponent object allows reading its
     * size component information (width, height, and depth).
     */
    public static final int
    ALLOW_SIZE_READ = CapabilityBits.IMAGE_COMPONENT_ALLOW_SIZE_READ;

    /**
     * Specifies that this ImageComponent object allows reading its
     * format component information.
     */
    public static final int
    ALLOW_FORMAT_READ = CapabilityBits.IMAGE_COMPONENT_ALLOW_FORMAT_READ;

    /**
     * Specifies that this ImageComponent object allows reading its
     * image component information.
     */
    public static final int
    ALLOW_IMAGE_READ = CapabilityBits.IMAGE_COMPONENT_ALLOW_IMAGE_READ;

    /**
     * Specifies that this ImageComponent object allows writing its
     * image component information.
     *
     * @since Java 3D 1.3
     */
    public static final int
    ALLOW_IMAGE_WRITE = CapabilityBits.IMAGE_COMPONENT_ALLOW_IMAGE_WRITE;


    /**
     * Retrieves the width of this image component object.
     * @return the width of this image component object
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */  
    public int getWidth() throws java.rmi.RemoteException;

    /**
     * Retrieves the height of this image component object.
     * @return the height of this image component object
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */  
    public int getHeight() throws java.rmi.RemoteException;

    /**
     * Retrieves the format of this image component object.
     * @return the format of this image component object
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */  
    public int getFormat() throws java.rmi.RemoteException;


    /**
     * Retrieves the data access mode for this ImageComponent object.
     *
     * @return <code>true</code> if the data access mode for this
     * ImageComponent object is by-reference;
     * <code>false</code> if the data access mode is by-copying.
     *
     * @since Java 3D 1.2
     */
    public boolean isByReference() throws java.rmi.RemoteException;


    /**
     * Sets the y-orientation of this ImageComponent object to
     * y-up or y-down.
     *
     * @param yUp a flag that indicates the y-orientation of this image
     * component.  If yUp is set to true, the origin of the image is
     * the lower left; otherwise, the origin of the image is the upper
     * left.
     *
     * @exception RestrictedAccessException if the method is called
     * when this object is part of live or compiled scene graph.
     *
     * @since Java 3D 1.2
     */
    public void setYUp(boolean yUp) throws java.rmi.RemoteException;


    /**
     * Retrieves the y-orientation for this ImageComponent object.
     *
     * @return <code>true</code> if the y-orientation of this
     * ImageComponent object is y-up; <code>false</code> if the
     * y-orientation of this ImageComponent object is y-down.
     *
     * @since Java 3D 1.2
     */
    public boolean isYUp() throws java.rmi.RemoteException;

}