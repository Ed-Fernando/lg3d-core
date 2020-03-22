/**
 * Project Looking Glass
 *
 * $RCSfile: TextureWrapper.java,v $
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
 * $Date: 2004-06-23 18:51:49 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.wrapper;

import javax.vecmath.*;
import java.util.Hashtable;

/**
 * The Texture object is a component object of an Appearance object
 * that defines the texture properties used when texture mapping is
 * enabled. The Texture object is an abstract class and all texture 
 * objects must be created as either a Texture2D object or a
 * Texture3D object.
 * <P>
 * Each Texture object has the following properties:<P>
 * <UL>
 * <LI>Boundary color - the texture boundary color. The texture
 * boundary color is used when the boundaryModeS and boundaryModeT
 * parameters are set to CLAMP or CLAMP_TO_BOUNDARY and if the texture
 * boundary is not specified. </LI><P>
 * <LI>Boundary Width - the texture boundary width. If the texture boundary
 * width is > 0, then all images for all mipmap levels will include boundary
 * texels. The actual texture image for level 0, for example, will be of 
 * dimension (width + 2*boundaryWidth) * (height + 2*boundaryWidth). 
 * The boundary texels will be used when linear filtering is to be applied.
 * </LI><p>
 * <LI>Boundary ModeS and Boundary ModeT - the boundary mode for the
 * S and T coordinates, respectively. The boundary modes are as
 * follows:</LI><P>
 * <UL>
 * <LI>CLAMP - clamps texture coordinates to be in the range [0,1]. 
 * Texture boundary texels or the constant boundary color if boundary width
 * is 0 will be used for U,V values that fall outside this range.</LI><P>
 * <LI>WRAP - repeats the texture by wrapping texture coordinates
 * that are outside the range [0,1]. Only the fractional portion
 * of the texture coordinates is used. The integer portion is
 * discarded</LI><P>
 * <LI>CLAMP_TO_EDGE - clamps texture coordinates such that filtering
 * will not sample a texture boundary texel. Texels at the edge of the 
 * texture will be used instead.</LI><P>
 * <LI>CLAMP_TO_BOUNDARY - clamps texture coordinates such that filtering
 * will sample only texture boundary texels, that is, it will never
 * get some samples from the boundary and some from the edge. This
 * will ensure clean unfiltered boundaries. If the texture does not 
 * have a boundary, that is the boundary width is equal to 0, then the 
 * constant boundary color will be used.</LI></P>
 * </UL>
 * <LI>Image - an image or an array of images for all the mipmap
 * levels. If only one image is provided, the MIPmap mode must be
 * set to BASE_LEVEL.</LI><P>
 * <LI>Magnification filter - the magnification filter function. 
 * Used when the pixel being rendered maps to an area less than or
 * equal to one texel. The magnification filter functions are as
 * follows:</LI><P>
 * <UL>
 * <LI>FASTEST - uses the fastest available method for processing
 * geometry.</LI><P>
 * <LI>NICEST - uses the nicest available method for processing
 * geometry.</LI><P>
 * <LI>BASE_LEVEL_POINT - selects the nearest texel in the base level
 * texture image.</LI><P>
 * <LI>BASE_LEVEL_LINEAR - performs a bilinear interpolation on the four
 * nearest texels in the base level texture image. The texture value T' is
 * computed as follows:</LI><P>
 * <UL>
 * i<sub>0</sub> = trunc(u - 0.5)<P>
 * j<sub>0</sub> = trunc(v - 0.5)<P>
 * i<sub>1</sub> = i<sub>0</sub> + 1<P>
 * j<sub>1</sub> = j<sub>0</sub> + 1<P>
 * a = frac(u - 0.5)<P>
 * b = frac(v - 0.5)<P>
 * T' = (1-a)*(1-b)*T<sub>i<sub>0</sub>j<sub>0</sub></sub> + 
 * a*(1-b)*T<sub>i<sub>1</sub>j<sub>0</sub></sub> +
 * (1-a)*b*T<sub>i<sub>0</sub>j<sub>1</sub></sub> +
 * a*b*T<sub>i<sub>1</sub>j<sub>1</sub></sub><P>
 * </UL>
 * <LI>LINEAR_SHARPEN - sharpens the resulting image by extrapolating
 * from the base level plus one image to the base level image of this 
 * texture object.</LI><P>
 * <LI>LINEAR_SHARPEN_RGB - performs linear sharpen filter for the rgb
 * components only. The alpha component is computed using BASE_LEVEL_LINEAR
 * filter.</LI><P>
 * <LI>LINEAR_SHARPEN_ALPHA - performs linear sharpen filter for the alpha
 * component only. The rgb components are computed using BASE_LEVEL_LINEAR
 * filter.</LI><P>
 * <LI>FILTER4 - applies an application-supplied weight function
 * on the nearest 4x4 texels in the base level texture image. The
 * texture value T' is computed as follows:</LI><P>
 * <UL>
 * <table cellspacing=10>
 * <td>i<sub>1</sub> = trunc(u - 0.5)</td>
 * <td>i<sub>2</sub> = i<sub>1</sub> + 1</td>
 * <td>i<sub>3</sub> = i<sub>2</sub> + 1</td>
 * <td>i<sub>0</sub> = i<sub>1</sub> - 1</td>
 * <tr>
 * <td>j<sub>1</sub> = trunc(v - 0.5)</td>
 * <td>j<sub>3</sub> = j<sub>2</sub> + 1</td>
 * <td>j<sub>2</sub> = j<sub>1</sub> + 1</td>
 * <td>j<sub>0</sub> = j<sub>1</sub> - 1</td>
 * <tr>
 * <td>a = frac(u - 0.5)</td>
 * <tr>
 * <td>b = frac(v - 0.5)</td>
 * </table>
 * f(x) : filter4 function where 0<=x<=2<P>
 * T' = f(1+a) * f(1+b) * T<sub>i<sub>0</sub>j<sub>0</sub></sub> + 
 * f(a) * f(1+b) * T<sub>i<sub>1</sub>j<sub>0</sub></sub> + 
 * f(1-a) * f(1+b) * T<sub>i<sub>2</sub>j<sub>0</sub></sub> + 
 * f(2-a) * f(1+b) * T<sub>i<sub>3</sub>j<sub>0</sub></sub> + <br>
 * f(1+a) * f(b) * T<sub>i<sub>0</sub>j<sub>1</sub></sub> + 
 * f(a) * f(b) * T<sub>i<sub>1</sub>j<sub>1</sub></sub> + 
 * f(1-a) * f(b) * T<sub>i<sub>2</sub>j<sub>1</sub></sub> + 
 * f(2-a) * f(b) * T<sub>i<sub>3</sub>j<sub>1</sub></sub> + <br>
 * f(1+a) * f(1-b) * T<sub>i<sub>0</sub>j<sub>2</sub></sub> + 
 * f(a) * f(1-b) * T<sub>i<sub>1</sub>j<sub>2</sub></sub> + 
 * f(1-a) * f(1-b) * T<sub>i<sub>2</sub>j<sub>2</sub></sub> + 
 * f(2-a) * f(1-b) * T<sub>i<sub>3</sub>j<sub>2</sub></sub> + <br>
 * f(1+a) * f(2-b) * T<sub>i<sub>0</sub>j<sub>3</sub></sub> + 
 * f(a) * f(2-b) * T<sub>i<sub>1</sub>j<sub>3</sub></sub> + 
 * f(1-a) * f(2-b) * T<sub>i<sub>2</sub>j<sub>3</sub></sub> + 
 * f(2-a) * f(2-b) * T<sub>i<sub>3</sub>j<sub>3</sub></sub> <P>
 * </UL>
 * </UL>
 * <LI>Minification filter - the minification filter function. Used
 * when the pixel being rendered maps to an area greater than one
 * texel. The minifaction filter functions are as follows:</LI><P>
 * <UL>
 * <LI>FASTEST - uses the fastest available method for processing
 * geometry.</LI><P>
 * <LI>NICEST - uses the nicest available method for processing
 * geometry.</LI><P>
 * <LI>BASE_LEVEL_POINT - selects the nearest level in the base level
 * texture map.</LI><P>
 *<LI>BASE_LEVEL_LINEAR - performs a bilinear interpolation on the four
 * nearest texels in the base level texture map.</LI><P>
 * <LI>MULTI_LEVEL_POINT - selects the nearest texel in the nearest
 * mipmap.</LI><P>
 * <LI>MULTI_LEVEL_LINEAR - performs trilinear interpolation of texels
 * between four texels each from the two nearest mipmap levels.</LI><P>
 * <LI>FILTER4 - applies an application-supplied weight function
 * on the nearest 4x4 texels in the base level texture image.</LI><P>
 * </UL>
 * <LI>MIPmap mode - the mode used for texture mapping for this
 * object. The mode is one of the following:</LI><P>
 * <UL>
 * <LI>BASE_LEVEL - indicates that this Texture object only has a
 * base-level image. If multiple levels are needed, they will be
 * implicitly computed.</LI><P>
 * <LI>MULTI_LEVEL_MIPMAP - indicates that this Texture object has
 * multiple images. If MIPmap mode is set
 * to MULTI_LEVEL_MIPMAP, images for Base Level through Max Level
 * must be set.</LI><P>
 * </UL>
 * <LI>Format - the data format. The format is one of the
 * following:</LI><P>
 * <UL>
 * <LI>INTENSITY - the texture image contains only texture
 * values.</LI><P>
 * <LI>LUMINANCE - the texture image contains only
 * luminance values.</LI><P>
 * <LI>ALPHA - the texture image contains only alpha
 * values.</LI><P>
 * <LI>LUMINANCE_ALPHA - the texture image contains
 * both luminance and alpha values.</LI><P>
 * <LI>RGB - the texture image contains red, green,
 * and blue values.</LI><P>
 * <LI>RGBA - the texture image contains red, green, blue, and alpha
 * values.</LI><P></UL>
 * <LI>Base Level - specifies the mipmap level to be used when filter
 * specifies BASE_LEVEL_POINT or BASE_LEVEL_LINEAR.</LI><P>
 * <LI>Maximum Level - specifies the maximum level of image that needs to be
 * defined for this texture to be valid. Note, for this texture to be valid,
 * images for Base Level through Maximum Level have to be defined.</LI><P>
 * <LI>Minimum LOD - specifies the minimum of the LOD range. LOD smaller
 * than this value will be clamped to this value.</LI><P>
 * <LI>Maximum LOD - specifies the maximum of the LOD range. LOD larger
 * than this value will be clamped to this value.</LI><P>
 * <LI>LOD offset - specifies the offset to be used in the LOD calculation
 * to compensate for under or over sampled texture images.</LI></P>
 * <LI>Anisotropic Mode - defines how anisotropic filter is applied for
 * this texture object. The anisotropic modes are as follows:</LI><P>
 * <UL>
 * <LI>ANISOTROPIC_NONE - no anisotropic filtering.</LI><P>
 * <LI>ANISOTROPIC_SINGLE_VALUE - applies the degree of anisotropic filter 
 * in both the minification and magnification filters.</LI><P>
 * </UL>
 * <LI>Anisotropic Filter Degree - controls the degree of anisotropy. This
 * property applies to both minification and magnification filtering. 
 * If it is equal to 1.0, then an isotropic filtering as specified in the
 * minification or magnification filter will be used. If it is greater 
 * than 1.0, and the anisotropic mode is equal to ANISOTROPIC_SINGLE_VALUE, 
 * then
 * the degree of anisotropy will also be applied in the filtering.</LI><P>
 * <LI>Sharpen Texture Function - specifies the function of level-of-detail
 * used in combining the texture value computed from the base level image
 * and the texture value computed from the base level plus one image. The
 * final texture value is computed as follows: </LI><P>
 * <UL>
 * T' = ((1 + SharpenFunc(LOD)) * T<sub>BaseLevel</sub>) - (SharpenFunc(LOD) * T<sub>BaseLevel+1</sub>) <P>
 * </UL>
 * <LI>Filter4 Function - specifies the function to be applied to the
 * nearest 4x4 texels.  This property includes samples of the filter
 * function f(x), 0<=x<=2. The number of function values supplied
 * has to be equal to 2<sup>m</sup> + 1 for some integer value of m
 * greater than or equal to 4. </LI><P>
 * </UL>
 *
 * @see Canvas3D#queryProperties
 */
public interface TextureWrapper extends NodeComponentWrapper {
 
    /**
     * Sets the boundary mode for the S coordinate in this texture object.
     * @param boundaryModeS the boundary mode for the S coordinate.
     * One of: CLAMP, WRAP, CLAMP_TO_EDGE, or CLAMP_TO_BOUNDARY.
     * @exception RestrictedAccessException if the method is called
     * when this object is part of live or compiled scene graph.
     * @exception IllegalArgumentException if <code>boundaryModeS</code>
     * is a value other than <code>CLAMP</code>, <code>WRAP</code>,
     * <code>CLAMP_TO_EDGE</code>, or <code>CLAMP_TO_BOUNDARY</code>.
     */
    public void setBoundaryModeS(int boundaryModeS) ;

    /**
     * Retrieves the boundary mode for the S coordinate.
     * @return the current boundary mode for the S coordinate.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public int getBoundaryModeS() ;

    /**
     * Sets the boundary mode for the T coordinate in this texture object.
     * @param boundaryModeT the boundary mode for the T coordinate.
     * One of: CLAMP, WRAP, CLAMP_TO_EDGE, or CLAMP_TO_BOUNDARY.
     * @exception RestrictedAccessException if the method is called
     * when this object is part of live or compiled scene graph.
     * @exception IllegalArgumentException if <code>boundaryModeT</code>
     * is a value other than <code>CLAMP</code>, <code>WRAP</code>,
     * <code>CLAMP_TO_EDGE</code>, or <code>CLAMP_TO_BOUNDARY</code>.
     */
    public void setBoundaryModeT(int boundaryModeT) ;

    /**
     * Retrieves the boundary mode for the T coordinate.
     * @return the current boundary mode for the T coordinate.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public int getBoundaryModeT() ;

    /**
     * Sets the minification filter function.  This
     * function is used when the pixel being rendered maps to an area
     * greater than one texel.
     * @param minFilter the minification filter. One of:
     * FASTEST, NICEST, BASE_LEVEL_POINT, BASE_LEVEL_LINEAR, 
     * MULTI_LEVEL_POINT, MULTI_LEVEL_LINEAR, or FILTER4
     * @exception RestrictedAccessException if the method is called
     * when this object is part of live or compiled scene graph.
     * @exception IllegalArgumentException if <code>minFilter</code>
     * is a value other than <code>FASTEST</code>, <code>NICEST</code>,
     * <code>BASE_LEVEL_POINT</code>, <code>BASE_LEVEL_LINEAR</code>,
     * <code>MULTI_LEVEL_POINT</code>, <code>MULTI_LEVEL_LINEAR</code>, or
     * <code>FILTER4</code>.
     *
     * @see Canvas3D#queryProperties
     */
    public void setMinFilter(int minFilter) ;

    /**
     * Retrieves the minification filter.
     * @return the current minification filter function.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public int getMinFilter() ;

    /**
     * Sets the magnification filter function.  This
     * function is used when the pixel being rendered maps to an area
     * less than or equal to one texel.
     * @param magFilter the magnification filter, one of:
     * FASTEST, NICEST, BASE_LEVEL_POINT, BASE_LEVEL_LINEAR, 
     * LINEAR_SHARPEN, LINEAR_SHARPEN_RGB, LINEAR_SHARPEN_ALPHA, or FILTER4.
     *
     * @exception RestrictedAccessException if the method is called
     * when this object is part of live or compiled scene graph.
     * @exception IllegalArgumentException if <code>magFilter</code>
     * is a value other than <code>FASTEST</code>, <code>NICEST</code>,
     * <code>BASE_LEVEL_POINT</code>, <code>BASE_LEVEL_LINEAR</code>,
     * <code>LINEAR_SHARPEN</code>, <code>LINEAR_SHARPEN_RGB</code>, 
     * <code>LINEAR_SHARPEN_ALPHA</code>,  or
     * <code>FILTER4</code>.
     *
     * @see Canvas3D#queryProperties
     */
    public void setMagFilter(int magFilter) ;

    /**
     * Retrieves the magnification filter.
     * @return the current magnification filter function.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public int getMagFilter() ;

    /**
     * Sets the image for a specified mipmap level. 
     * @param level mipmap level to set: 0 is the base level
     * @param image ImageComponent object containing the texture image
     * for the specified mipmap level
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @exception IllegalArgumentException if an ImageComponent3D is
     * used in a Texture2D object; if an ImageComponent2D is used in a
     * Texture3D object; or if this object is part of a live
     * scene graph and the image being set at this level is not the
     * same size (width, height, depth) as the old image at this
     * level.
     */
    public void setImage(int level, ImageComponentWrapper image) ;

    /**
     * Retrieves the image for a specified mipmap level.
     * @param level mipmap level to get: 0 is the base level
     * @return the ImageComponent object containing the texture image at
     * the specified mipmap level.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public ImageComponentWrapper getImage(int level) ;

    /**
     * Sets the array of images for all mipmap levels.
     * @param images array of ImageComponent objects
     * containing the texture images for all mipmap levels
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @exception IllegalArgumentException if an ImageComponent3D is
     * used in a Texture2D object; if an ImageComponent2D is used in a
     * Texture3D object; if <code>images.length</code> is not equal to
     * the total number of mipmap levels; or if this object is part of
     * a live scene graph and the size of each dimension (width,
     * height, depth) of the image at a given level in the
     * <code>images</code> array is not half the dimension of the
     * previous level.
     *
     * @since Java 3D 1.2
     */
    public void setImages(ImageComponentWrapper[] images) ;

    /**
     * Retrieves the array of images for all mipmap levels.
     * @return the array of ImageComponent objects for this Texture.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.2
     */
    public ImageComponentWrapper[] getImages() ;

    /**
     * Retrieves the format of this Texture object.
     * @return the format of this Texture object.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.2
     */
    public int getFormat() ;
    /**
     * Retrieves the width of this Texture object.
     * @return the width of this Texture object.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.2
     */
    public int getWidth() ;

    /**
     * Retrieves the height of this Texture object.
     * @return the height of this Texture object.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.2
     */
    public int getHeight() ;

    /**
     * Retrieves the width of the boundary of this Texture object.
     * @return the width of the boundary of this Texture object.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.3
     */
    public int getBoundaryWidth() ;
    /**
     * Retrieves the number of mipmap levels needed for this Texture object.
     * @return (maximum Level - base Level + 1) 
     * if <code>mipMapMode</code> is
     * <code>MULTI_LEVEL_MIPMAP</code>; otherwise it returns 1.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.2
     */
    public int numMipMapLevels() ;

    /**
     * Sets mipmap mode for texture mapping for this texture object.  
     * @param mipMapMode the new mipmap mode for this object.  One of:
     * BASE_LEVEL or MULTI_LEVEL_MIPMAP.
     * @exception RestrictedAccessException if the method is called
     * when this object is part of live or compiled scene graph.
     * @exception IllegalArgumentException if <code>mipMapMode</code>
     * is a value other than <code>BASE_LEVEL</code> or 
     * <code>MULTI_LEVEL_MIPMAP</code>.
     */
    public void setMipMapMode(int mipMapMode) ;

    /**
     * Retrieves current mipmap mode.
     * @return current mipmap mode of this texture object.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public int getMipMapMode() ;

    /**
     * Enables or disables texture mapping for this
     * appearance component object.
     * @param state true or false to enable or disable texture mapping
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setEnable(boolean state) ;

    /**
     * Retrieves the state of the texture enable flag.
     * @return true if texture mapping is enabled,
     * false if texture mapping is disabled
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public boolean getEnable() ;
	    
    /**
     * Sets the texture boundary color for this texture object.  The
     * texture boundary color is used when boundaryModeS or boundaryModeT
     * is set to CLAMP or CLAMP_TO_BOUNDARY and if texture boundary is not
     * specified.
     * @param boundaryColor the new texture boundary color.
     * @exception RestrictedAccessException if the method is called
     * when this object is part of live or compiled scene graph.
     */
    public void setBoundaryColor(Color4f boundaryColor) ;

    /**
     * Sets the texture boundary color for this texture object.  The
     * texture boundary color is used when boundaryModeS or boundaryModeT
     * is set to CLAMP or CLAMP_TO_BOUNDARY and if texture boundary is not
     * specified.
     * @param r the red component of the color.
     * @param g the green component of the color.
     * @param b the blue component of the color.
     * @param a the alpha component of the color.
     * @exception RestrictedAccessException if the method is called
     * when this object is part of live or compiled scene graph.
     */
    public void setBoundaryColor(float r, float g, float b, float a) ;

    /**
     * Retrieves the texture boundary color for this texture object.
     * @param boundaryColor the vector that will receive the
     * current texture boundary color.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void getBoundaryColor(Color4f boundaryColor) ;
    /**
     * Specifies the base level for this texture object.
     * @param baseLevel index of the lowest defined mipmap level.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     * @exception IllegalArgumentException if specified baseLevel < 0, or
     * if baseLevel > maximumLevel
     *
     * @since Java 3D 1.3
     * @see Canvas3D#queryProperties
     */
    public void setBaseLevel(int baseLevel) ;

    /**
     * Retrieves the base level for this texture object.
     * @return base level for this texture object.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.3
     */
    public int getBaseLevel() ;

    /**
     * Specifies the maximum level for this texture object.
     * @param maximumLevel index of the highest defined mipmap level.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     * @exception IllegalArgumentException if specified 
     * maximumLevel < baseLevel, or
     * if maximumLevel > <code>log<sub><font size=-2>2</font></sub>(max(width,height))</code>
     *
     * @since Java 3D 1.3
     * @see Canvas3D#queryProperties
     */
    public void setMaximumLevel(int maximumLevel) ;

    /**
     * Retrieves the maximum level for this texture object.
     * @return maximum level for this texture object.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.3
     */
    public int getMaximumLevel() ;

    /**
     * Specifies the minimum level-of-detail for this texture object.
     * @param minimumLod the minimum level-of-detail.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     * @exception IllegalArgumentException if specified lod > maximum lod
     *
     * @since Java 3D 1.3
     * @see Canvas3D#queryProperties
     */
    public void setMinimumLOD(float minimumLod) ;

    /**
     * Retrieves the minimum level-of-detail for this texture object.
     * @return the minimum level-of-detail
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.3
     */
    public float getMinimumLOD() ;

    /**
     * Specifies the maximum level-of-detail for this texture object.
     * @param maximumLod the maximum level-of-detail.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     * @exception IllegalArgumentException if specified lod < minimum lod
     *
     * @since Java 3D 1.3
     * @see Canvas3D#queryProperties
     */
    public void setMaximumLOD(float maximumLod) ;

    /**
     * Retrieves the maximum level-of-detail for this texture object.
     * @return the maximum level-of-detail
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.3
     */
    public float getMaximumLOD() ;

    /**
     * Specifies the LOD offset for this texture object.
     * @param s the s component of the LOD offset
     * @param t the t component of the LOD offset
     * @param r the r component of the LOD offset
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.3
     * @see Canvas3D#queryProperties
     */
    public void setLodOffset(float s, float t, float r) ;

    /**
     * Specifies the LOD offset for this texture object.
     * @param offset the LOD offset
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.3
     * @see Canvas3D#queryProperties
     */
    public void setLodOffset(Tuple3f offset) ;

    /**
     * Retrieves the LOD offset for this texture object.
     * @param offset the vector that will receive the
     * current LOD offset.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.3
     */
    public void getLodOffset(Tuple3f offset) ;

    /**
     * Specifies the anisotropic filter mode for this texture object.
     * @param mode the anisotropic filter mode. One of
     * ANISOTROPIC_NONE or ANISOTROPIC_SINGLE_VALUE.
     * @exception RestrictedAccessException if the method is called
     * when this object is part of live or compiled scene graph.
     * @exception IllegalArgumentException if
     * <code>mode</code> is a value other than
     * <code>ANISOTROPIC_NONE</code> or <code>ANISOTROPIC_SINGLE_VALUE</code>
     *
     * @since Java 3D 1.3
     * @see Canvas3D#queryProperties
     */
    public void setAnisotropicFilterMode(int mode) ;

    /**
     * Retrieves the anisotropic filter mode for this texture object.
     * @return the currrent anisotropic filter mode of this texture object.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.3
     */
    public int getAnisotropicFilterMode() ;

    /**
     * Specifies the degree of anisotropy to be
     * used when the anisotropic filter mode specifies 
     * ANISOTROPIC_SINGLE_VALUE.
     * @param degree degree of anisotropy
     * @exception RestrictedAccessException if the method is called
     * when this object is part of live or compiled scene graph.
     * @exception IllegalArgumentException if
     * <code>degree</code> < 1.0 or
     * <code>degree</code> > the maximum degree of anisotropy.
     *
     * @since Java 3D 1.3
     * @see Canvas3D#queryProperties
     */
    public void setAnisotropicFilterDegree(float degree) ;

    /**
     * Retrieves the anisotropic filter degree for this texture object.
     * @return the current degree of anisotropy of this texture object
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.3
     */
    public float getAnisotropicFilterDegree() ;
  
    /**
     * sets the sharpen texture LOD function for this texture object.
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
    public void setSharpenTextureFunc(float[] lod, float[] pts) ;

    /**
     * sets the sharpen texture LOD function for this texture object.
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
    public void setSharpenTextureFunc(Point2f[] pts) ;

    /**
     * Gets the number of points in the sharpen texture LOD function for this
     * texture object.
     *
     * @return the number of points in the sharpen texture LOD function.
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.3
     */
    public int getSharpenTextureFuncPointsCount() ;
   
    /**
     * Copies the array of sharpen texture LOD function points into the
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
    public void getSharpenTextureFunc(float[] lod, float[] pts) ;

    /**
     * Copies the array of sharpen texture LOD function points including
     * the lod values and the corresponding function values into the
     * specified array. The array must be large enough to hold all the points.
     * The individual array elements must be allocated by the caller as well.
     *
     * @param pts the array to receive the sharpen texture LOD function points
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.3
     */
    public void getSharpenTextureFunc(Point2f[] pts) ;
     
    /**
     * sets the filter4 function for this texture object.
     * @param weights array containing samples of the filter4 function.
     *
     * @exception IllegalArgumentException if the length of 
     * <code>weight</code> < 4
     * @exception RestrictedAccessException if the method is called
     * when this object is part of live or compiled scene graph.
     *
     * @since Java 3D 1.3
     * @see Canvas3D#queryProperties
     */
    public void setFilter4Func(float[] weights);

    /**
     * Retrieves the number of filter4 function values for this
     * texture object.
     *
     * @return the number of filter4 function values
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.3
     */
    public int getFilter4FuncPointsCount() ;

    /**
     * Copies the array of filter4 function values into the specified
     * array. The array must be large enough to hold all the values.
     *
     * @param weights the array to receive the function values.
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.3
     */
    public void getFilter4Func(float[] weights) ;

}
