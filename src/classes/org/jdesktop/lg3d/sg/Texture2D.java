/**
 * Project Looking Glass
 *
 * $RCSfile: Texture2D.java,v $
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
 * $Revision: 1.3 $
 * $Date: 2006-05-04 18:16:58 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg;
import javax.vecmath.*;
import org.jdesktop.lg3d.sg.internal.wrapper.Texture2DWrapper;
import org.jdesktop.lg3d.sg.internal.wrapper.ImageComponent2DWrapper;


/**
 * Texture2D is a subclass of Texture class. It extends Texture
 * class by adding a constructor and a mutator method for
 * setting a 2D texture image.
 * <P>
 * Each Texture2D object has the following properties:<P>
 * <UL>
 * <LI>Magnification filter - the magnification filter function.
 * Used when the pixel being rendered maps to an area less than or
 * equal to one texel. In addition to the magnification filter functions
 * defined in the base Texture class, the following values are
 * supported:</LI><P>
 * <UL>
 * <LI>LINEAR_DETAIL - performs linear sampling in both the base level 
 * texture image and the detail texture image, and combines the two
 * texture values according to the detail texture mode.</LI><P>
 * <LI>LINEAR_DETAIL_RGB - performs linear detail for the rgb
 * components only. The alpha component is computed using BASE_LEVEL_LINEAR
 * filter.</LI><P>
 * <LI>LINEAR_DETAIL_ALPHA - performs linear detail for the alpha
 * component only. The rgb components are computed using BASE_LEVEL_LINEAR
 * filter.</LI><P>
 * </UL>
 * <LI>Detail Texture Image - Detail texture image to be used when the texture
 * magnification filter mode specifies LINEAR_DETAIL, LINEAR_DETAIL_ALPHA, or
 * LINEAR_DETAIL_RGB; if the detail texture images is null, then
 * the texture magnification filter mode will fall back to BASE_LEVEL_LINEAR.
 * </LI><P>
 * <LI>Detail Texture Mode - specifies how the texture image is combined
 * with the detail image. The detail texture modes are as follows: </LI><P>
 * <UL>
 * <LI>DETAIL_ADD</LI><P>
 * <UL>
 * T' = T<sub>texture</sub> + DetailFunc(LOD) * (2 * T<sub>detail</sub> - 1)<P>
 * </UL>
 * <LI>DETAIL_MODULATE</LI><P>
 * <UL>
 * T' = T<sub>texture</sub> * (1 + DetailFunc(LOD) * (2 * T<sub>detail</sub> - 1))<P>
 * </UL>
 * </UL>
 * where T<sub>texture</sub> is the texture value computed from the base level
 * texture image, and T<sub>detail</sub> is the texture value computed from the
 * detail texture image.<P>
 * <LI>Detail Texture Function - specifies the function of level-of-detail
 * used in combining the detail texture with the base level texture of this object.</LI><P>
 * <LI>Detail Texture Level - specifies the number of levels that
 * separate the base level image of this texture object and the detail
 * texture image. This value is used in the linear filter
 * calculation of the detail texture image. Note, detail texture will only
 * be applied to the level 0 of the texture image. Hence, for detail texture
 * to work, base level has to be set to 0.</LI><P>
 * </UL>
 *
 * @see Canvas3D#queryProperties
 */
public class Texture2D extends Texture {

    /**
     * Specifies that this Texture object allows reading its detail
     * texture information (e.g., detail texture image, detail texture mode,
     * detail texture function, detail texture function points count,
     * detail texture level)
     *
     * @since Java 3D 1.3
     */
    public static final int
    ALLOW_DETAIL_TEXTURE_READ = CapabilityBits.TEXTURE2D_ALLOW_DETAIL_TEXTURE_READ;

    /** 
     * Performs linear sampling in both the base level
     * texture image and the detail texture image, and combines the two
     * texture values according to the detail texture mode.
     *
     * @since Java 3D 1.3
     * @see #setMagFilter
     */
    public static final int LINEAR_DETAIL         = 6;

    /**
     * Performs linear detail for the rgb
     * components only. The alpha component is computed using 
     * BASE_LEVEL_LINEAR filter.
     *
     * @since Java 3D 1.3
     * @see #setMagFilter
     */
    public static final int LINEAR_DETAIL_RGB     = 7;

    /**
     * Performs linear detail for the alpha
     * component only. The rgb components are computed using 
     * BASE_LEVEL_LINEAR filter.
     *
     * @since Java 3D 1.3
     * @see #setMagFilter
     */
    public static final int LINEAR_DETAIL_ALPHA   = 8;

    /**
     * Adds the detail texture image to the level 0 image of this texture
     * object
     *
     * @since Java 3D 1.3
     * @see #setDetailTextureMode
     */
    public static final int DETAIL_ADD = 0;

    /**
     * Modulates the detail texture image with the level 0 image of this
     * texture object
     *
     * @since Java 3D 1.3
     * @see #setDetailTextureMode
     */
    public static final int DETAIL_MODULATE = 1;



    /**
     * Constructs a texture object using default values.
     *
     * The default values are as follows:
     * <ul>
     * detail texture image: null<br>
     * detail texture mode: DETAIL_MODULATE<br>
     * detail texture func: null<br>
     * detail texture level: 2<br>
     * </ul>
     * <p>
     * Note that the default constructor creates a texture object with
     * a width and height of 0 and is, therefore, not useful.
     */
    public Texture2D() {
//        assert( this.getClass().equals(Texture2D.class) );
        wrapped = instantiate( SceneGraphSetup.getWrapperPackage()+"Texture2D");
    }

  /**
   * Constructs an empty Texture2D object with specified mipmapMode
   * format, width and height. Image at base level must be set by 
   * the application using 'setImage' method. If mipmapMode is
   * set to MULTI_LEVEL_MIPMAP, images for base level through maximum level
   * must be set.
   * @param mipMapMode type of mipmap for this Texture: One of
   * BASE_LEVEL, MULTI_LEVEL_MIPMAP.
   * @param format data format of Textures saved in this object.
   * One of INTENSITY, LUMINANCE, ALPHA, LUMINANCE_ALPHA, RGB, RGBA.
   * @param width width of image at level 0. Must be power of 2.
   * @param height height of image at level 0. Must be power of 2.
   * @exception IllegalArgumentException if width or height are NOT
   * power of 2 OR invalid format/mipmapMode is specified.
   */
    public Texture2D(
	    int		mipMapMode,
	    int		format,
	    int		width,
	    int		height){
                this (mipMapMode, format, width, height, 0 );
    }


    /**
     * Constructs an empty Texture2D object with specified mipMapMode,
     * format, width, height, and boundaryWidth.
     * Defaults are used for all other
     * parameters.  If <code>mipMapMode</code> is set to
     * <code>BASE_LEVEL</code>, then the image at level 0 must be set
     * by the application (using either the <code>setImage</code> or
     * <code>setImages</code> method). If <code>mipMapMode</code> is
     * set to <code>MULTI_LEVEL_MIPMAP</code>, then images for levels
     * Base Level through Maximum Level must be set.
     *
     * @param mipMapMode type of mipmap for this Texture: one of
     * BASE_LEVEL, MULTI_LEVEL_MIPMAP
     * @param format data format of Textures saved in this object.
     * One of INTENSITY, LUMINANCE, ALPHA, LUMINANCE_ALPHA, RGB, RGBA
     * @param width width of image at level 0. Must be power of 2. This
     * does not include the width of the boundary.
     * @param height height of image at level 0. Must be power of 2. This
     * does not include the width of the boundary.
     * @param boundaryWidth width of the boundary.
     * @exception IllegalArgumentException if width or height are not a
     * power of 2, if an invalid format or mipMapMode is specified, or
     * if the boundaryWidth < 0
     *
     * @since Java 3D 1.3
     */
    public Texture2D(int          mipMapMode,
                   int          format,
                   int          width,
                   int          height,
                   int          boundaryWidth) {
//         assert( this.getClass().equals(Texture2D.class) );
         wrapped = instantiate( SceneGraphSetup.getWrapperPackage()+"Texture2D",
                                new Class[] { Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE },
                                new Object[] { new Integer(mipMapMode), new Integer(format), new Integer(width), new Integer(height), new Integer(boundaryWidth) } );
    }

    protected void createWrapped() {
    }
    
    /**
     * Sets the magnification filter function.  This
     * function is used when the pixel being rendered maps to an area
     * less than or equal to one texel.
     * @param magFilter the magnification filter, one of:
     * FASTEST, NICEST, BASE_LEVEL_POINT, BASE_LEVEL_LINEAR, 
     * LINEAR_DETAIL, LINEAR_DETAIL_RGB, LINEAR_DETAIL_ALPHA,
     * LINEAR_SHARPEN, LINEAR_SHARPEN_RGB, LINEAR_SHARPEN_ALPHA, or FILTER4.
     *
     * @exception RestrictedAccessException if the method is called
     * when this object is part of live or compiled scene graph.
     * @exception IllegalArgumentException if <code>minFilter</code>
     * is a value other than <code>FASTEST</code>, <code>NICEST</code>,
     * <code>BASE_LEVEL_POINT</code>, <code>BASE_LEVEL_LINEAR</code>,
     * <code>LINEAR_DETAIL</code>, <code>LINEAR_DETAIL_RGB</code>, 
     * <code>LINEAR_DETAIL_ALPHA</code>, 
     * <code>LINEAR_SHARPEN</code>, <code>LINEAR_SHARPEN_RGB</code>, 
     * <code>LINEAR_SHARPEN_ALPHA</code>,  or
     * <code>FILTER4</code>.
     *
     * @see Canvas3D#queryProperties
     *
     * @since Java 3D 1.3
     */
    public void setMagFilter(int magFilter) {
	((Texture2DWrapper)wrapped).setMagFilter( magFilter );
    }

    /**
     * Sets the detail texture image for this texture object.
     * @param detailTexture ImageComponent2D object containing the
     * detail texture image.
     * @exception RestrictedAccessException if the method is called
     * when this object is part of live or compiled scene graph.
     *
     * @since Java 3D 1.3
     * @see Canvas3D#queryProperties
     */
    public void setDetailImage(ImageComponent2D detailTexture) {
        ((Texture2DWrapper)wrapped).setDetailImage( (ImageComponent2DWrapper)detailTexture.wrapped );
    }

    /**
     * Retrieves the detail texture image for this texture object.
     * @return ImageComponent2D object containing the detail texture image.
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.3
     */
    public ImageComponent2D getDetailImage() {
        return (ImageComponent2D)((Texture2DWrapper)wrapped).getDetailImage().getUserData();
    }

    /**
     * Sets the detail texture mode for this texture object.
     * @param mode detail texture mode. One of: DETAIL_ADD or DETAIL_MODULATE
     *
     * @exception IllegalArgumentException if
     * <code>mode</code> is a value other than
     * <code>DETAIL_ADD</code>, or <code>DETAIL_MODULATE</code>
     * @exception RestrictedAccessException if the method is called
     * when this object is part of live or compiled scene graph.
     *
     * @since Java 3D 1.3
     * @see Canvas3D#queryProperties
     */
    public void setDetailTextureMode(int mode) {
        ((Texture2DWrapper)wrapped).setDetailTextureMode( mode );
    }

    /**
     * Retrieves the detail texture mode for this texture object.
     * @return the detail texture mode.
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.3
     */
    public int getDetailTextureMode() {
       return ((Texture2DWrapper)wrapped).getDetailTextureMode();
    }

    /**
     * Sets the detail texture level for this texture object.
     * @param level the detail texture level.
     *
     * @exception IllegalArgumentException if <code>level</code> < 0
     * @exception RestrictedAccessException if the method is called
     * when this object is part of live or compiled scene graph.
     *
     * @since Java 3D 1.3
     * @see Canvas3D#queryProperties
     */
    public void setDetailTextureLevel(int level) {
        ((Texture2DWrapper)wrapped).setDetailTextureLevel( level );
    }

    /**
     * Retrieves the detail texture level for this texture object.
     * @return the detail texture level.
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.3
     */
    public int getDetailTextureLevel() {
       return ((Texture2DWrapper)wrapped).getDetailTextureLevel();
    }

    /**
     * sets the detail texture LOD function for this texture object.
     * @param lod array containing the level-of-detail values.
     * @param pts array containing the function values for the corresponding
     * level-of-detail values.
     *
     * @exception IllegalStateException if the length of <code>lod</code>
     * does not match the length of <code>pts</code>
     * @exception RestrictedAccessException if the method is called
     * when this object is part of live or compiled scene graph.
     *
     * @since Java 3D 1.3
     * @see Canvas3D#queryProperties
     */
    public void setDetailTextureFunc(float[] lod, float[] pts) {
        ((Texture2DWrapper)wrapped).setDetailTextureFunc( lod, pts );
    }

    /**
     * sets the detail texture LOD function for this texture object.
     * The Point2f x,y values are defined as follows: x is the lod value,
     * y is the corresponding function value.
     *
     * @param pts array of Point2f containing the lod as well as the
     * corresponding function value.
     *
     * @exception RestrictedAccessException if the method is called
     * when this object is part of live or compiled scene graph.
     *
     * @since Java 3D 1.3
     * @see Canvas3D#queryProperties
     */
    public void setDetailTextureFunc(Point2f[] pts) {
        ((Texture2DWrapper)wrapped).setDetailTextureFunc( pts );
    }

    /**
     * Gets the number of points in the detail texture LOD function for this
     * texture object.
     *
     * @return the number of points in the detail texture LOD function.
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.3
     */
    public int getDetailTextureFuncPointsCount() {
        return ((Texture2DWrapper)wrapped).getDetailTextureFuncPointsCount();
    }

    /**
     * Copies the array of detail texture LOD function points into the
     * specified arrays. The arrays must be large enough to hold all the
     * points.
     *
     * @param lod the array to receive the level-of-detail values.
     * @param pts the array to receive the function values for the
     * corresponding level-of-detail values.
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.3
     */
    public void getDetailTextureFunc(float[] lod, float[] pts) {
        ((Texture2DWrapper)wrapped).getDetailTextureFunc( lod, pts );
    }

    /**
     * Copies the array of detail texture LOD function points including
     * the lod values and the corresponding function values into the
     * specified array. The array must be large enough to hold all the points.
     * The individual array elements must be allocated by the caller as well.
     *
     * @param pts the array to receive the detail texture LOD function points
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.3
     */
    public void getDetailTextureFunc(Point2f[] pts) {
        ((Texture2DWrapper)wrapped).setDetailTextureFunc( pts );
    }

}

