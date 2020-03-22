/**
 * Project Looking Glass
 *
 * $RCSfile: ImageComponentWrapper.java,v $
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
 * $Date: 2004-06-23 18:51:44 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.wrapper;

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

public interface ImageComponentWrapper extends NodeComponentWrapper {

    /**
     * Retrieves the width of this image component object.
     * @return the width of this image component object
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */  
    public int getWidth() ;

    /**
     * Retrieves the height of this image component object.
     * @return the height of this image component object
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */  
    public int getHeight() ;

    /**
     * Retrieves the format of this image component object.
     * @return the format of this image component object
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */  
    public int getFormat() ;


    /**
     * Retrieves the data access mode for this ImageComponent object.
     *
     * @return <code>true</code> if the data access mode for this
     * ImageComponent object is by-reference;
     * <code>false</code> if the data access mode is by-copying.
     *
     * @since Java 3D 1.2
     */
    public boolean isByReference() ;


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
    public void setYUp(boolean yUp) ;


    /**
     * Retrieves the y-orientation for this ImageComponent object.
     *
     * @return <code>true</code> if the y-orientation of this
     * ImageComponent object is y-up; <code>false</code> if the
     * y-orientation of this ImageComponent object is y-down.
     *
     * @since Java 3D 1.2
     */
    public boolean isYUp() ;

}
