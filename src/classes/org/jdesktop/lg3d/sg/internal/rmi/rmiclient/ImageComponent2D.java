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
 * $Date: 2004-06-23 18:50:52 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.rmi.rmiclient;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

import org.jdesktop.lg3d.sg.internal.wrapper.ImageComponent2DWrapper;
import org.jdesktop.lg3d.sg.internal.rmi.rmiserver.ImageComponent2DRemote;

import javax.media.jai.remote.SerializableRenderedImage;

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

public class ImageComponent2D extends ImageComponent implements ImageComponent2DWrapper {

    // non-public, no parameter constructor
    ImageComponent2D() {}
    
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
			    int		height) {

        try {
            remote = SceneGraphSetup.getSGObjectFactory().newImageComponent2D( format, width, height, false, false );
            setRemote( remote );
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
    }

    /**
     * Constructs a 2D image component object using the specified format
     * and BufferedImage.  A copy of the BufferedImage is made.
     *
     * @param format the image component format, one of: FORMAT_RGB,
     * FORMAT_RGBA, etc.
     * @param image the BufferedImage used to create this 2D image component.
     * @exception IllegalArgumentException if format is invalid, or if
     * the width or height of the image are not positive.
     */
//    public ImageComponent2D(int format, BufferedImage image) {
//
//        wrapped = new javax.media.j3d.ImageComponent2D( format, image );
//        wrapped.setUserData( this );
//        try {
//            remote = SceneGraphSetup.getSGObjectFactory().newImageComponent2D( format, new SerializableRenderedImage( image ) );
//            setRemote( remote );
//        } catch( java.rmi.RemoteException rex ) {
//            throw new RuntimeException(rex);
//        }
//    }

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
    public ImageComponent2D(int format, RenderedImage image) {
        try {
            remote = SceneGraphSetup.getSGObjectFactory().newImageComponent2D( format, new SerializableRenderedImage( image ), false, false );
            setRemote( remote );
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
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
			    boolean	yUp) {
        try {
            remote = SceneGraphSetup.getSGObjectFactory().newImageComponent2D( format, width, height, byReference, yUp );
            setRemote( remote );
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
    }

    /**
     * Constructs a 2D image component object using the specified format,
     * BufferedImage, byReference flag, and yUp flag.
     *
     * @param format the image component format, one of: FORMAT_RGB,
     * FORMAT_RGBA, etc.
     * @param image the BufferedImage used to create this 2D image component
     * @param byReference a flag that indicates whether the data is copied
     * into this image component object or is accessed by reference
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
			    BufferedImage image,
			    boolean byReference,
			    boolean yUp) {

        try {
            remote = SceneGraphSetup.getSGObjectFactory().newImageComponent2D( format, new SerializableRenderedImage( image ), byReference, yUp );
            setRemote( remote );
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
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
			    boolean yUp) {
        try {
            remote = SceneGraphSetup.getSGObjectFactory().newImageComponent2D( format, new SerializableRenderedImage( image ), byReference, yUp );
            setRemote( remote );
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
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
//    public void set(BufferedImage image) {
//        ((javax.media.j3d.ImageComponent2D)wrapped).set( new SerializableRenderedImage(image) );
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
    public void set(RenderedImage image) {
        try {
            ((ImageComponent2DRemote)remote).set( new javax.media.jai.remote.SerializableRenderedImage( image ) );
            setRemote( remote );
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
    }

    /**
     * Retrieves the image from this ImageComponent2D object.  If the
     * data access mode is not by-reference, a copy of the image
     * is made.  If the data access mode is by-reference, the
     * reference is returned.
     *
     * @return either a new BufferedImage object created from the data
     * in this image component, or the BufferedImage object referenced
     * by this image component.
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @exception IllegalStateException if the data access mode is
     * by-reference and the image referenced by this ImageComponent2D
     * object is not an instance of BufferedImage.
     */
//    public BufferedImage getImage() {
//        return ((javax.media.j3d.ImageComponent2D)wrapped).getImage();
//    }

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
    public RenderedImage getRenderedImage() {
        //return ((javax.media.j3d.ImageComponent2D)wrapped).getRenderedImage();
        throw new RuntimeException("Not Implemented");
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
    public void setSubImage(RenderedImage image, int width, int height,
				int srcX, int srcY, int dstX, int dstY) {
//        ((javax.media.j3d.ImageComponent2D)wrapped).setSubImage( new javax.media.jai.remote.SerializableRenderedImage(image),
//                                                                 width,
//                                                                 height,
//                                                                 srcX,
//                                                                 srcY,
//                                                                 dstX,
//                                                                 dstY );
                                    throw new RuntimeException("Not Implemented");
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
			   int width, int height) {

//        ((javax.media.j3d.ImageComponent2D)wrapped).updateData( updater,
//                                                                x,y,
//                                                                width,
//                                                                height );
                               throw new RuntimeException("Not Implemented");
    }

    /**
     * The ImageComponent2D.Updater interface is used in updating image data
     * that is accessed by reference from a live or compiled ImageComponent
     * object.  Applications that wish to modify such data must define a
     * class that implements this interface.  An instance of that class is
     * then passed to the <code>updateData</code> method of the
     * ImageComponent object to be modified.
     *
     * @since Java 3D 1.3
     */
    public static interface Updater extends javax.media.j3d.ImageComponent2D.Updater {
	/**
	 * Updates image data that is accessed by reference.
	 * This method is called by the updateData method of an
	 * ImageComponent object to effect
	 * safe updates to image data that
	 * is referenced by that object.  Applications that wish to modify
	 * such data must implement this method and perform all updates
	 * within it.
	 * <br>
	 * NOTE: Applications should <i>not</i> call this method directly.
	 *
	 * @param imageComponent the ImageComponent object being updated.
	 * @param x,y starting offset of the subregion.
	 * @param width width of the subregion.
	 * @param height height of the subregion.
	 *
	 * @see ImageComponent2D#updateData
	 */
	public void updateData(ImageComponent2D imageComponent,
			       int x, int y,
			       int width, int height);
    }

}
