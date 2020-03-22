/**
 * Project Looking Glass
 *
 * $RCSfile: SimpleAppearance.java,v $
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
 * $Revision: 1.17 $
 * $Date: 2006-08-23 19:32:46 $
 * $State: Exp $
 *
 */
package org.jdesktop.lg3d.utils.shape;

import java.net.URL;
import java.io.IOException;
import java.util.logging.Logger;
import org.jdesktop.lg3d.sg.Appearance;
import org.jdesktop.lg3d.sg.Material;
import org.jdesktop.lg3d.sg.PolygonAttributes;
import org.jdesktop.lg3d.sg.Texture;
import org.jdesktop.lg3d.sg.TextureAttributes;
import org.jdesktop.lg3d.sg.TransparencyAttributes;
import org.jdesktop.lg3d.sg.utils.image.TextureLoader;

/**
 * A convenience class to create typical Appearance object quickly.
 * 
 * It set up parameters like ambientColor, emissiveColor, diffuseColor, 
 * specularColor, shininess and others to a "reasonable" value for
 * general use.
 * 
 * Capabilities are set to allow ambient and diffuse colors, and 
 * transparency value can be altered.
 *
 * When texture is used,
 * in order to avoid a copy, the texture image is loaded in yUp and
 * byReference.  Thus, a regluar image becomes upside-down.
 * The user can compensate this upside-down texture by setting up texture 
 * coordinates to invert the Y direction once again. 
 */
public class SimpleAppearance extends Appearance {
    // TODO -- use 1.5's enum?
    public static final int NO_GLOSS = 1;
    public static final int ENABLE_TEXTURE = 2;
    public static final int DISABLE_CULLING = 4;
    public static final int DEST_BLEND_ONE = 8;
    private static final Logger logger = Logger.getLogger("lg.sg");
    
    private boolean noGloss;
    private boolean glow;
    
    /**
     * Create a SimpleAppearance object with the given color and type.
     *
     * @param  red    the red color value
     * @param  green  the green color value
     * @param  blue   the blue color value
     * @param  type   a ORed value of any combination of the followings: 
     *                NO_GLOSS, ENABLE_TEXTURE and DISABLE_CULLING.
     */
    public SimpleAppearance(float red, float green, float blue, int type){
	noGloss = (type & NO_GLOSS) > 0;
        glow = (type & DEST_BLEND_ONE) > 0;
        
	// setup capabilities for allowing color changes
	setCapability(Appearance.ALLOW_MATERIAL_READ);
	setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_READ);

	setColor(red, green, blue);

	if ((type & ENABLE_TEXTURE) > 0) {
	    TextureAttributes texAttr = new TextureAttributes();
	    texAttr.setTextureMode(TextureAttributes.MODULATE);
	    setTextureAttributes(texAttr);
            setCapability(Appearance.ALLOW_TEXTURE_READ);
            setCapability(Appearance.ALLOW_TEXTURE_WRITE);
	}

	if ((type & DISABLE_CULLING) > 0) {
	    setPolygonAttributes(
		new PolygonAttributes(
		    PolygonAttributes.POLYGON_FILL,
		    PolygonAttributes.CULL_NONE,
		    0.0f, false, 0.0f
		    ));
	}
    }
    
    /**
     * Create a SimpleAppearance object with the given color and type.
     *
     * @param  red    the red color value
     * @param  green  the green color value
     * @param  blue   the blue color value
     * @param  alpha  the alpha value
     * @param  type   a ORed value of any combination of the followings: 
     *                NO_GLOSS, ENABLE_TEXTURE and DISABLE_CULLING.
     */
    public SimpleAppearance(float red, float green, float blue, float alpha,
	int type)
    {
	this(red, green, blue, type);
	setAlpha(alpha);
    }
    
     /**
     * Create a SimpleAppearance object with the given color and type.
     *
     * @param  red    the red color value
     * @param  green  the green color value
     * @param  blue   the blue color value
     */
    public SimpleAppearance(float red, float green, float blue) {
	this(red, green, blue, 0);
    }
    
    /**
     * Create a SimpleAppearance object with the given color and type.
     *
     * @param  red    the red color value
     * @param  green  the green color value
     * @param  blue   the blue color value
     * @param  alpha  the alpha value
     */
    public SimpleAppearance(float red, float green, float blue, float alpha) {
	this(red, green, blue, alpha, 0);
    }
    
    /**
     * Create an SimpleAppearance object that is associated with the given
     * image as a texture.
     * In order to avoid a copy, the texture image is loaded in yUp and
     * byReference.  Thus, a regluar image becomes upside-down.
     * The user can compensate this upside-down texture by setting up texture 
     * coordinates to invert the Y direction once again. 
     *
     * @param  imageUrl  the URL that represents the texture image 
     */
    public SimpleAppearance(URL imageUrl) 
        throws IOException
    {
	this(imageUrl, 0);
    }
    
    /**
     * Create an SimpleAppearance object that is associated with the given
     * image as a texture.
     * In order to avoid a copy, the texture image is loaded in yUp and
     * byReference.  Thus, a regluar image becomes upside-down.
     * The user can compensate this upside-down texture by setting up texture 
     * coordinates to invert the Y direction once again. 
     *
     * @param  imageUrl  the URL that represents the texture image 
     * @param  type      a ORed value of any combination of the followings: 
     *                   NO_GLOSS, ENABLE_TEXTURE and DISABLE_CULLING.
     */
    public SimpleAppearance(URL imageUrl, int type)
        throws IOException
    {
        this(imageUrl, 1.0f, 1.0f, 1.0f, type);
    }
    
    /**
     * Create an SimpleAppearance object that is associated with the given
     * image as a texture.
     * In order to avoid a copy, the texture image is loaded in yUp and
     * byReference.  Thus, a regluar image becomes upside-down.
     * The user can compensate this upside-down texture by setting up texture 
     * coordinates to invert the Y direction once again. 
     *
     * @param  imageUrl  the URL that represents the texture image 
     * @param  red    the red color value
     * @param  green  the green color value
     * @param  blue   the blue color value
     * @param  type   a ORed value of any combination of the followings: 
     *                NO_GLOSS, ENABLE_TEXTURE and DISABLE_CULLING.
     */
    public SimpleAppearance(URL imageUrl, 
            float red, float green, float blue, int type) 
        throws IOException
    {
        this(red, green, blue, type | ENABLE_TEXTURE);
        setTexture(imageUrl);
    }
    
    /**
     * Create an SimpleAppearance object that is associated with the given
     * image as a texture.
     * In order to avoid a copy, the texture image is loaded in yUp and
     * byReference.  Thus, a regluar image becomes upside-down.
     * The user can compensate this upside-down texture by setting up texture 
     * coordinates to invert the Y direction once again. 
     *
     * @param  imageUrl  the URL that represents the texture image 
     * @param  red    the red color value
     * @param  green  the green color value
     * @param  blue   the blue color value
     * @param  alpha  the alpha value
     * @param  type   a ORed value of any combination of the followings: 
     *                NO_GLOSS, ENABLE_TEXTURE and DISABLE_CULLING.
     */
    public SimpleAppearance(URL imageUrl, 
            float red, float green, float blue, float alpha, int type) 
        throws IOException
    {
        this(imageUrl, red, green, blue, type);
        setAlpha(alpha);
    }
    
    /**
     * Set the color of this SimpleAppearance object.
     *
     * @param  red    the red color value
     * @param  green  the green color value
     * @param  blue   the blue color value
     * @param  alpha  the alpha value
     */
    public void setColor(float red, float green, float blue, float alpha) {
	setColor(red, green, blue);
	setAlpha(alpha);
    }
    
    /**
     * Set the color of this SimpleAppearance object.
     *
     * @param  red    the red color value
     * @param  green  the green color value
     * @param  blue   the blue color value
     */
    public void setColor(float red, float green, float blue) {
	Material mat = getMaterial();
	if (mat == null) {
	    mat = new Material();
	    mat.setCapability(Material.ALLOW_COMPONENT_WRITE);
	}
	mat.setAmbientColor(red, green, blue);
	mat.setDiffuseColor(red, green, blue);
	if (noGloss) {
	    mat.setSpecularColor(red, green, blue);
	}

	setMaterial(mat);
    }
    
    /**
     * Set the alpha of this SimpleAppearance object.
     *
     * @param  alpha  the alpha value
     */
    public void setAlpha(float alpha) {
	float trans = 1.0f - alpha;
	TransparencyAttributes ta = getTransparencyAttributes();
	if (ta == null) {
            
            int dstBlendFunction 
                = (glow)
                    ?(TransparencyAttributes.BLEND_ONE)
                    :(TransparencyAttributes.BLEND_ONE_MINUS_SRC_ALPHA);
            ta = 
                new TransparencyAttributes(
                    TransparencyAttributes.BLENDED, 
                    1.0f,
                    TransparencyAttributes.BLEND_SRC_ALPHA,
                    dstBlendFunction);
	    ta.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
	    setTransparencyAttributes(ta);
	}
	ta.setTransparency(trans);
    }
    
    /**
     * Set a new texture image to this SimpleAppearance object.
     * In order to avoid a copy, the texture image is loaded in yUp and
     * byReference.  Thus, a regluar image becomes upside-down.
     * The user can compensate this upside-down texture by setting up texture 
     * coordinates to invert the Y direction once again. 
     *
     * @param  imageUrl  the URL that represents the texture image 
     */
    public void setTexture(URL imageUrl) 
        throws IOException
    {
        if (imageUrl == null) {
            throw new IllegalArgumentException("the imageUrl argument cannot be null");
        }
        TextureLoader textureLoader 
            = new TextureLoader(imageUrl, TextureLoader.Y_UP | TextureLoader.BY_REFERENCE, null);
        Texture texture = textureLoader.getTexture();
        if (texture == null) {
            throw new IOException("while loading texture from " + imageUrl);
        }
        texture.setMinFilter(Texture.BASE_LEVEL_LINEAR);
        texture.setMagFilter(Texture.BASE_LEVEL_LINEAR);
        setTexture(texture);
    }
}

