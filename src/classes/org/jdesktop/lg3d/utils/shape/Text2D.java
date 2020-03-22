/**
 * Project Looking Glass
 *
 * $RCSfile: Text2D.java,v $
 *
 * Copyright (c) 2005, Sun Microsystems, Inc., All Rights Reserved
 *
 * Redistributions in source code form must reproduce the above
 * copyright and this condition.
 *
 * The contents of this file are subject to the GNU General Public
 * License, Version 2 (the "License"); you may not use this file
 * except in compliance with the License. A copy of the License is
 * available at http://www.opensource.org/licenses/gpl-license.php.
 *
 * $Revision: 1.5 $
 * $Date: 2006-07-07 12:08:32 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.shape;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.awt.image.DataBufferInt;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.Hashtable;

import org.jdesktop.lg3d.sg.*;
import javax.vecmath.*;

/**
 * A Text2D object is a representation of a string as a texture mapped
 * rectangle.  The texture for the rectangle shows the string as rendered in
 * the specified color with a transparent background.  The appearance of the
 * characters is specified using the font indicated by the font name, size
 * and style (see java.awt.Font).  The approximate height of the rendered
 * string will be the font size times the rectangle scale factor, which has a
 * default value of 1/256.  For example, a 12 point font will produce
 * characters that are about 12/256 = 0.047 meters tall. The lower left
 * corner of the rectangle is located at (0,0,0) with the height
 * extending along the positive y-axis and the width extending along the
 * positive x-axis.
 */
public class Text2D extends Shape3D {

    // This table caches FontMetrics objects to avoid the huge cost
    // of re-retrieving metrics for a font we've already seen.
    private static Hashtable metricsTable = new Hashtable(); 
    float rectangleScaleFactor = 1f/256f;

    Color3f   color = new Color3f();
    String    fontName;
    int       fontSize, fontStyle;
    String text;


    /**
     * Creates a Shape3D object which holds a
     * rectangle that is texture-mapped with an image that has
     * the specified text written with the specified font
     * parameters.
     *
     * @param text The string to be written into the texture map.
     * @param color The color of the text string.
     * @param fontName The name of the Java font to be used for
     *  the text string.
     * @param fontSize The size of the Java font to be used.
     * @param fontStyle The style of the Java font to be used.
     */
    public Text2D(String text, Color3f color, String fontName,
		  int fontSize, int fontStyle) {

        this.color.set(color);
        this.fontName = fontName;
        this.fontSize = fontSize;
        this.fontStyle = fontStyle;
	this.text = text;

        updateText2D(text, color, fontName, fontSize, fontStyle);
    }
     /**
     * Creates a Shape3D object which holds a
     * rectangle that is texture-mapped with an image that has
     * the specified text written with the specified font
     * parameters.
     *
     * @param text The string to be written into the texture map.
     * @param color The color of the text string.
     * @param font The object of the Java font to be used for 
     * genrating texture
     */
     public Text2D(String text, Color3f color, Font font) {
        
	 this(text,color,font.getFontName (),font.getSize (),font.getStyle ());
    }

    /*
     * Changes text of this Text2D to 'text'. All other
     * parameters (color, fontName, fontSize, fontStyle
     * remain the same.
     * @param text The string to be set.
     */
    public void setString(String text){
	this.text = text;
	
	Texture tex = getAppearance().getTexture();
	int width = tex.getWidth();
	int height = tex.getHeight();	

        ImageComponent imageComponent = setupImage(text, color, fontName,
                                          fontSize, fontStyle);
	if ((imageComponent.getWidth() == width) &&
	    (imageComponent.getHeight() == height)) {
	    tex.setImage(0, imageComponent);
	} else {
	    Texture2D newTex = setupTexture(imageComponent);
	    // Copy texture attributes except those related to
	    // mipmap since Texture only set base imageComponent. 

	    newTex.setBoundaryModeS(tex.getBoundaryModeS());
	    newTex.setBoundaryModeT(tex.getBoundaryModeT());
	    newTex.setMinFilter(tex.getMinFilter());
	    newTex.setMagFilter(tex.getMagFilter());      
	    newTex.setEnable(tex.getEnable());
	    newTex.setAnisotropicFilterMode(tex.getAnisotropicFilterMode());
	    newTex.setAnisotropicFilterDegree(tex.getAnisotropicFilterDegree());
	    int pcount = tex.getFilter4FuncPointsCount();
	    if (pcount > 0) {
		float weights[] = new float[pcount];
		tex.getFilter4Func(weights);
		newTex.setFilter4Func(weights);
	    }
	    Color4f c = new Color4f();
	    tex.getBoundaryColor(c);
	    newTex.setBoundaryColor(c);      
	    newTex.setUserData(tex.getUserData());
	    getAppearance().setTexture(newTex);
	}
    }

    private void updateText2D(String text, Color3f color, String fontName,
                  int fontSize, int fontStyle) {
        ImageComponent imageComponent = setupImage(text, color, fontName,
                                          fontSize, fontStyle);

        Texture2D t2d = setupTexture(imageComponent);

	QuadArray rect = setupGeometry(imageComponent.getWidth(), 
		imageComponent.getHeight());
	setGeometry(rect);

    	Appearance appearance = setupAppearance(t2d);
	setAppearance(appearance);
    }


    /**
     * Sets the scale factor used in converting the image width/height
     * to width/height values in 3D.
     *
     * @param newScaleFactor The new scale factor.
     */
    public void setRectangleScaleFactor(float newScaleFactor) {
	rectangleScaleFactor = newScaleFactor;
	updateText2D(text, color, fontName, fontSize, fontStyle);
    }

    /**
     * Gets the current scale factor being used in converting the image
     * width/height to width/height values in 3D.
     *
     * @return The current scale factor.
     */
    public float getRectangleScaleFactor() {
	return rectangleScaleFactor;
    }
    
    /**
     * Create the ImageComponent and Texture object.
     */
    private Texture2D setupTexture(ImageComponent imageComponent) {
	Texture2D t2d = new Texture2D(Texture2D.BASE_LEVEL,
				      Texture.RGBA,
				      imageComponent.getWidth(),
				      imageComponent.getHeight());
	t2d.setMinFilter(t2d.BASE_LEVEL_LINEAR);
	t2d.setMagFilter(t2d.BASE_LEVEL_LINEAR);
	t2d.setImage(0, imageComponent);
	t2d.setEnable(true);
	t2d.setCapability(Texture.ALLOW_IMAGE_WRITE);
	t2d.setCapability(Texture.ALLOW_SIZE_READ);
	t2d.setCapability(Texture.ALLOW_ENABLE_READ);
	t2d.setCapability(Texture.ALLOW_BOUNDARY_MODE_READ);
	t2d.setCapability(Texture.ALLOW_FILTER_READ); 
	t2d.setCapability(Texture.ALLOW_BOUNDARY_COLOR_READ); 
	t2d.setCapability(Texture.ALLOW_ANISOTROPIC_FILTER_READ); 
	t2d.setCapability(Texture.ALLOW_FILTER4_READ);
	return t2d;
    }

    /**
     * Creates a ImageComponent2D of the correct dimensions for the
     * given font attributes.  Draw the given text into the image in
     * the given color.  The background of the image is transparent
     * (alpha = 0).
     */
    @SuppressWarnings("deprecation") // ignore warnings against getFontMetrics()
    private ImageComponent setupImage(String text, Color3f color,
				     String fontName,
				     int fontSize, int fontStyle) {
	Toolkit toolkit = Toolkit.getDefaultToolkit();
	Font font = new java.awt.Font(fontName, fontStyle, fontSize);

	FontMetrics metrics;
	if ((metrics = (FontMetrics)metricsTable.get(font)) == null) {
	    metrics = toolkit.getFontMetrics(font);
	    metricsTable.put(font, metrics);
	}
	int width = metrics.stringWidth(text);
	int descent = metrics.getMaxDescent();
	int ascent = metrics.getMaxAscent();
	int leading = metrics.getLeading();
	int height = descent + ascent;

	// Need to make width/height powers of 2 because of Java3d texture
	// size restrictions
	int pow = 1;
	for (int i = 1; i < 32; ++i) {
	    pow *= 2;
	    if (width <= pow)
		break;
	}
	width = Math.max (width, pow);
	pow = 1;
	for (int i = 1; i < 32; ++i) {
	    pow *= 2;
	    if (height <= pow)
		break;
	}
	height = Math.max (height, pow);

	// For now, jdk 1.2 only handles ARGB format, not the RGBA we want
	BufferedImage bImage = new BufferedImage(width, height,
						 BufferedImage.TYPE_4BYTE_ABGR);
	Graphics offscreenGraphics = bImage.createGraphics();

	// First, erase the background to the text panel - set alpha to 0
	Color myFill = new Color(0f, 0f, 0f, 0f);
	offscreenGraphics.setColor(myFill);
	offscreenGraphics.fillRect(0, 0, width, height);

	// Next, set desired text properties (font, color) and draw String
	offscreenGraphics.setFont(font);
	Color myTextColor = new Color(color.x, color.y, color.z, 1f);
	offscreenGraphics.setColor(myTextColor);
	offscreenGraphics.drawString(text, 0, height - descent);

	ImageComponent imageComponent =
	    new ImageComponent2D(ImageComponent.FORMAT_RGBA, 
				 bImage, true, true);

	imageComponent.setCapability(ImageComponent.ALLOW_SIZE_READ);

	return imageComponent;
    }

    /**
     * Creates a rectangle of the given width and height and sets up
     * texture coordinates to map the text image onto the whole surface
     * of the rectangle (the rectangle is the same size as the text image)
     */
    private QuadArray setupGeometry(int width, int height) {
	float zPosition = 0f;
	float rectWidth = (float)width * rectangleScaleFactor;
	float rectHeight = (float)height * rectangleScaleFactor;
	float[] verts1 = {
	    rectWidth, 0f, zPosition,
	    rectWidth, rectHeight, zPosition,
	    0f, rectHeight, zPosition,
	    0f, 0f, zPosition
	};
	float[] texCoords = {
	    0f, 0f,
	    0f, -1f,
	    (-1f), -1f,
	    (-1f), 0f
	};
	
	QuadArray rect = new QuadArray(4, QuadArray.COORDINATES |
				       QuadArray.TEXTURE_COORDINATE_2);
	rect.setCoordinates(0, verts1);
	rect.setTextureCoordinates(0, 0, texCoords);
	
	return rect;
    }

    /**
     * Creates Appearance for this Shape3D.  This sets transparency
     * for the object (we want the text to be "floating" in space,
     * so only the text itself should be non-transparent.  Also, the
     * appearance disables lighting for the object; the text will
     * simply be colored, not lit.
     */
    private Appearance setupAppearance(Texture2D t2d) {
	TransparencyAttributes transp = new TransparencyAttributes();
	transp.setTransparencyMode(TransparencyAttributes.BLENDED);
	transp.setTransparency(0f);
	Appearance appearance = new Appearance();
        appearance.setCapability(Appearance.ALLOW_TEXTURE_WRITE);
	appearance.setTransparencyAttributes(transp);
	appearance.setTexture(t2d);

	Material m = new Material();
	m.setLightingEnable(false);
	appearance.setMaterial(m);
	
	return appearance;
    }

    /**
     * Returns the text string
     *
     * @since Java 3D 1.2.1
     */
    public String getString() {
	return text;
    }

    /**
     * Returns the color of the text
     *
     * @since Java 3D 1.2.1
     */
    public Color3f getColor() {
	return color;
    }

    /**
     * Returns the font
     *
     * @since Java 3D 1.2.1
     */
    public String getFontName() {
	return fontName;
    }

    /**
     * Returns the font size
     *
     * @since Java 3D 1.2.1
     */
    public int getFontSize() {
	return fontSize;
    }
    
    /**
     * Returns the font style
     *
     * @since Java 3D 1.2.1
     */
    public int getFontStyle() {
	return fontStyle;
    }
    
}
