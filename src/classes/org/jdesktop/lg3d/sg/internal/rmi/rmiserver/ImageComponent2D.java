/**
 * Project Looking Glass
 *
 * $RCSfile: ImageComponent2D.java,v $
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
 * $Date: 2004-06-23 18:51:12 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.rmi.rmiserver;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

/**
 * This class defines a 2D image component.  This is used for texture
 * images, background images and raster components of Shape3D nodes.
 * Prior to Java 3D 1.2, only BufferedImage objects could be used as the
 * input to an ImageComponent2D object.  As of Java 3D 1.2, an
 * ImageComponent2D accepts any RenderedImage object (BufferedImage is
 * an implementation of the RenderedImage interface).  The methods
 * that set/get a BufferedImage object are left in for compatibility.
 * The new methods that set/get a RenderedImage are a superset of the
 * old methods.  In particular, the two set methods in the following
 * example are equivalent:
 *
 * <p>
 * <ul>
 * <code>
 * BufferedImage bi;<br>
 * RenderedImage ri = bi;<br>
 * ImageComponent2D ic;<br>
 * <p>
 * // Set the image to the specified BufferedImage<br>
 * ic.set(bi);<br>
 * <p>
 * // Set the image to the specified RenderedImage<br>
 * ic.set(ri);<br>
 * </code>
 * </ul>
 *
 */

public class ImageComponent2D extends ImageComponent implements ImageComponent2DRemote{

    // non-public, no parameter constructor
    ImageComponent2D() throws java.rmi.RemoteException {}
    
    /**
     * Constructs a 2D image component object using the specified
     * format, width, and height, and a null image.
     *
     * @param format the image component format, one of: FORMAT_RGB,
     * FORMAT_RGBA, etc.
     * @param width the number of columns of pixels in this image component
     * object
     * @param height the number of rows of pixels in this image component
     * object
     * @exception IllegalArgumentException if format is invalid, or if
     * width or height are not positive.
     */
    public ImageComponent2D(int		format,
			    int		width,
			    int		height) throws java.rmi.RemoteException {

        wrapped = new org.jdesktop.lg3d.sg.ImageComponent2D( format, width, height );
        wrapped.setUserData( this );
    }

    /**
     * Constructs a 2D image component object using the specified format
     * and RenderedImage.  A copy of the RenderedImage is made.
     *
     * @param format the image component format, one of: FORMAT_RGB,
     * FORMAT_RGBA, etc.
     * @param image the RenderedImage used to create this 2D image component
     * @exception IllegalArgumentException if format is invalid, or if
     * the width or height of the image are not positive.
     *
     * @since Java 3D 1.2
     */
    public ImageComponent2D(int format, RenderedImage image) throws java.rmi.RemoteException {


        wrapped = new org.jdesktop.lg3d.sg.ImageComponent2D( format, image );
        wrapped.setUserData( this );
   }

    /**
     * Constructs a 2D image component object using the specified
     * format, width, height, byReference flag, and yUp flag, and
     * a null image.
     *
     * @param format the image component format, one of: FORMAT_RGB,
     * FORMAT_RGBA, etc.
     * @param width the number of columns of pixels in this image component
     * object
     * @param height the number of rows of pixels in this image component
     * object
     * @param byReference a flag that indicates whether the data is copied
     * into this image component object or is accessed by reference.
     * @param yUp a flag that indicates the y-orientation of this image
     * component.  If yUp is set to true, the origin of the image is
     * the lower left; otherwise, the origin of the image is the upper
     * left.
     * @exception IllegalArgumentException if format is invalid, or if
     * width or height are not positive.
     *
     * @since Java 3D 1.2
     */
    public ImageComponent2D(int		format,
			    int		width,
			    int		height,
			    boolean	byReference,
			    boolean	yUp) throws java.rmi.RemoteException {


         wrapped = new org.jdesktop.lg3d.sg.ImageComponent2D( format, 
                                                         width, 
                                                         height,
                                                         byReference,
                                                         yUp );
        wrapped.setUserData( this );
    }


    /**
     * Constructs a 2D image component object using the specified format,
     * RenderedImage, byReference flag, and yUp flag.
     *
     * @param format the image component format, one of: FORMAT_RGB,
     * FORMAT_RGBA, etc.
     * @param image the RenderedImage used to create this 2D image component
     * @param byReference a flag that indicates whether the data is copied
     * into this image component object or is accessed by reference.
     * @param yUp a flag that indicates the y-orientation of this image
     * component.  If yUp is set to true, the origin of the image is
     * the lower left; otherwise, the origin of the image is the upper
     * left.
     * @exception IllegalArgumentException if format is invalid, or if
     * the width or height of the image are not positive.
     *
     * @since Java 3D 1.2
     */
    public ImageComponent2D(int format,
			    RenderedImage image,
			    boolean byReference,
			    boolean yUp) throws java.rmi.RemoteException {


        wrapped = new org.jdesktop.lg3d.sg.ImageComponent2D( format, 
                                                        image,
                                                        byReference,
                                                        yUp );
        wrapped.setUserData( this );
    }

    /**
     * Sets this image component to the specified BufferedImage
     * object.  If the data access mode is not by-reference, then the
     * BufferedImage data is copied into this object.  If
     * the data access mode is by-reference, then a reference to the
     * BufferedImage is saved, but the data is not necessarily
     * copied.
     *
     * @param image BufferedImage object containing the image.
     * The format and size must be the same as the current format in this
     * ImageComponent2D object.
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     */
//    public void set(BufferedImage image) throws java.rmi.RemoteException {
//        ((org.jdesktop.lg3d.sg.ImageComponent2D)wrapped).set( image );
//    }

    /**
     * Sets this image component to the specified RenderedImage
     * object.  If the data access mode is not by-reference, the
     * RenderedImage data is copied into this object.  If
     * the data access mode is by-reference, a reference to the
     * RenderedImage is saved, but the data is not necessarily
     * copied.
     *
     * @param image RenderedImage object containing the image.
     * The format and size must be the same as the current format in this
     * ImageComponent2D object.
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.2
     */
    public void set(javax.media.jai.remote.SerializableRenderedImage image) throws java.rmi.RemoteException {
        ((org.jdesktop.lg3d.sg.ImageComponent2D)wrapped).set( image );
    }


    /**
     * Retrieves the image from this ImageComponent2D object.  If the
     * data access mode is not by-reference, a copy of the image
     * is made.  If the data access mode is by-reference, the
     * reference is returned.
     *
     * @return either a new RenderedImage object created from the data
     * in this image component, or the RenderedImage object referenced
     * by this image component.
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.2
     */
    public javax.media.jai.remote.SerializableRenderedImage getRenderedImage() throws java.rmi.RemoteException {
        return new javax.media.jai.remote.SerializableRenderedImage(((org.jdesktop.lg3d.sg.ImageComponent2D)wrapped).getRenderedImage());
    }


    /**
     * Modifies a contiguous subregion of the image component.
     * Block of data of dimension (width * height)
     * starting at the offset (srcX, srcY) of the specified 
     * RenderedImage object will be copied into the image component
     * starting at the offset (dstX, dstY) of the ImageComponent2D object.
     * The RenderedImage object must be of the same format as the current 
     * format of this object.
     * This method can only be used if the data access mode is
     * by-copy. If it is by-reference, see updateData().
     *
     * @param image RenderedImage object containing the subimage.
     * @param width width of the subregion.
     * @param height height of the subregion.
     * @param srcX,&nbsp;srcY starting offset of the subregion in the 
     * specified image.
     * @param dstX,&nbsp;dstY starting offset of the subregion in the image 
     * component of this object.
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     * @exception IllegalStateException if the data access mode is
     * <code>BY_REFERENCE</code>.
     * @exception IllegalArgumentException if <code>width</code> or 
     * <code>height</code> of
     * the subregion exceeds the dimension of the image of this object.
     * @exception IllegalArgumentException if <code>dstX</code> < 0, or
     * (<code>dstX</code> + <code>width</code>) > width of this object, or 
     * <code>dstY</code> < 0, or
     * (<code>dstY</code> + <code>height</code>) > height of this object.
     * @exception IllegalArgumentException if <code>srcX</code> < 0, or
     * (<code>srcX</code> + <code>width</code>) > width of the RenderedImage
     * object containing the subimage, or 
     * <code>srcY</code> < 0, or
     * (<code>srcY</code> + <code>height</code>) > height of the 
     * RenderedImage object containing the subimage.
     *
     * @since Java 3D 1.3
     */
    public void setSubImage(javax.media.jai.remote.SerializableRenderedImage image, int width, int height, int srcX, int srcY, int dstX, int dstY) throws java.rmi.RemoteException {
        ((org.jdesktop.lg3d.sg.ImageComponent2D)wrapped).setSubImage( image,
                                                                 width,
                                                                 height,
                                                                 srcX,
                                                                 srcY,
                                                                 dstX,
                                                                 dstY );
    }

    /**
     * Updates image data that is accessed by reference.
     * This method calls the updateData method of the specified
     * ImageComponent2D.Updater object to synchronize updates to the
     * image data that is referenced by this ImageComponent2D object.
     * Applications that wish to modify such data must perform all
     * updates via this method.
     * <p>
     * The data to be modified has to be within the boundary of the
     * subregion
     * specified by the offset (x, y) and the dimension (width*height).
     * It is illegal to modify data outside this boundary.
     * If any referenced data is modified outisde the updateData
     * method, or any data outside the specified boundary is modified,
     * the results are undefined.
     * <p>
     * @param updater object whose updateData callback method will be
     * called to update the data referenced by this ImageComponent2D object.
     * @param x,y starting offset of the subregion.
     * @param width width of the subregion.
     * @param height height of the subregion.
     *
     * @exception CapabilityNotSetException if the appropriate capability
     * is not set, and this object is part of a live or compiled scene graph
     * @exception IllegalStateException if the data access mode is
     * <code>BY_COPY</code>.
     * @exception IllegalArgumentException if <code>width</code> or
     * <code>height</code> of
     * the subregion exceeds the dimension of the image of this object.
     * @exception IllegalArgumentException if <code>x</code> < 0, or
     * (<code>x</code> + <code>width</code>) > width of this object, or
     * <code>y</code> < 0, or
     * (<code>y</code> + <code>height</code>) > height of this object.
     *
     * @since Java 3D 1.3
     */
    public void updateData(Updater updater, 
			   int x, int y,
			   int width, int height) throws java.rmi.RemoteException {

        ((org.jdesktop.lg3d.sg.ImageComponent2D)wrapped).updateData( updater,
                                                                x,y,
                                                                width,
                                                                height );
    }
    
    protected void createWrapped() {
    }
    
}
