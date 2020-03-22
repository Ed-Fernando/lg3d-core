/**
 * Project Looking Glass
 *
 * $RCSfile: GlassyText2D.java,v $
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
 * $Revision: 1.4 $
 * $Date: 2006-05-31 20:58:03 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.shape;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.vecmath.Color4f;
import org.jdesktop.lg3d.sg.Appearance;
import org.jdesktop.lg3d.sg.GeometryArray;
import org.jdesktop.lg3d.sg.IndexedQuadArray;
import org.jdesktop.lg3d.sg.Shape3D;
import org.jdesktop.lg3d.sg.Texture;


public class GlassyText2D extends Shape3D {
    public enum Alignment {LEFT, CENTER, RIGHT};
    public enum LightDirection {TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT};
    
    private static final Font textFont = new Font("Serif", Font.PLAIN, 22);
    private static final Color text2DColor = new Color(0.5f, 0.5f, 0.5f, 0.5f);
    private static final int widthMargin = 1;
    private static final int heightMargin = 1;
    private static final FontMetrics fontMetrics;
    
    private IndexedQuadArray geometry;
    private Appearance appearance;
    private float width;
    private float widthScale;
    private float maxWidth;
    private float prevAdjustedWidth;
    private float height;
    private Alignment alignment;
    private LightDirection lightDirection;
    private boolean vertical;
    
    
    static {
	BufferedImage bi 
	    = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	Graphics2D g2d = (Graphics2D)bi.getGraphics();
	g2d.setFont(textFont);
	fontMetrics = g2d.getFontMetrics();
	g2d.dispose();
    }
    
    public GlassyText2D(String text, float maxWidth, float height,  
            Color4f textColor)
    {
        this(text, maxWidth, height, textColor, 
                LightDirection.TOP_LEFT, Alignment.LEFT);
    }
    
    public GlassyText2D(String text, float maxWidth, float height,  
            Color4f textColor, LightDirection lightDirection, Alignment alignment)
    {
        this(text, maxWidth, height, textColor, 
                lightDirection, alignment, 1.2f);
    }
    
    public GlassyText2D(String text, float maxWidth, float height,  
            Color4f textColor, LightDirection lightDirection, 
            Alignment alignment, float widthScale)
    {
        this(text, maxWidth, height, textColor, 
                lightDirection, alignment, widthScale, false);
    }
    
    public GlassyText2D(String text, float maxWidth, float height,  
            Color4f textColor, LightDirection lightDirection, 
            Alignment alignment, float widthScale, boolean vertical)
    {
        this.maxWidth = maxWidth;
	this.height = height;
        this.alignment = alignment;
        this.widthScale = widthScale;
        this.lightDirection = lightDirection;
        this.vertical = vertical;
        
	appearance = new SimpleAppearance(
		textColor.x, textColor.y, textColor.z, textColor.w,
		SimpleAppearance.ENABLE_TEXTURE
		| SimpleAppearance.DISABLE_CULLING
		);
        
	appearance.setCapability(Appearance.ALLOW_TEXTURE_READ);
        appearance.setCapability(Appearance.ALLOW_TEXTURE_WRITE);
	setAppearance(appearance);

	geometry
	    = new IndexedQuadArray(4, 
		GeometryArray.COORDINATES
		| GeometryArray.TEXTURE_COORDINATE_2
		| GeometryArray.NORMALS,
		1, new int[] {0},
		4);
	geometry.setCapability(GeometryArray.ALLOW_COORDINATE_WRITE);
        geometry.setCapability(GeometryArray.ALLOW_TEXCOORD_WRITE);
        
	setText(text);

	int[] indices = {
	    0, 1, 2, 3,
	};
        
	float[] normals = {
	    0.0f, 0.0f, 1.0f,
	};

	int[] normalIndices = {
	    0, 0, 0, 0,
	};

	geometry.setCoordinateIndices(0, indices);
	geometry.setTextureCoordinateIndices(0, 0, indices );
	geometry.setNormals(0, normals);
	geometry.setNormalIndices(0, normalIndices);
	
        setGeometry(geometry);
    }
    
    public void setWidth(float widthLimit) {
	float w = (width < widthLimit)?(width):(widthLimit);

	if (w == prevAdjustedWidth) {
	    return;
	}
	prevAdjustedWidth = w;
        maxWidth = widthLimit;
        
        float xo = 0.0f;
        switch (alignment) {
            case LEFT:
                break;
            case CENTER:
                xo = -w / 2;
                break;
            case RIGHT:
                xo = -w;
                break;
        }
            
	float[] coords 
	    = (vertical)
	    ?(new float[] {
		0.0f,   -xo,       0.0f,
		0.0f,   -(xo + w), 0.0f,
		height, -(xo + w), 0.0f,
		height, -xo,       0.0f,
	    }):(new float[] {
		xo,     0.0f,   0.0f,
		xo + w, 0.0f,   0.0f,
		xo + w, height, 0.0f,
		xo,     height, 0.0f,
	    });
	geometry.setCoordinates(0, coords);
    }
    
    public void setText(String text) {
        // since the size of image for the texture can change,
        // we recreate the texture object
        int xShift = -1;
        int yShift = -1;
        if (lightDirection == LightDirection.TOP_RIGHT 
                || lightDirection == LightDirection.BOTTOM_RIGHT) 
        {
            xShift = 1;
        }
        if (lightDirection == LightDirection.BOTTOM_LEFT 
                || lightDirection == LightDirection.BOTTOM_RIGHT) 
        {
            yShift = 1;
        }
        GlassyTextTextureGenerator gttg 
                = new GlassyTextTextureGenerator(
                    text, height, widthScale, xShift, yShift);
        Texture texture = gttg.getTexture();
        appearance.setTexture(texture);
        
        width = gttg.getWidth();
        float widthRatio = gttg.getWidthRatio();
        float heightRatio = gttg.getHeightRatio();
        
        float[] texCoords = {
	    0.0f,       heightRatio,
	    widthRatio, heightRatio,
	    widthRatio, 0.0f,
	    0.0f,       0.0f,
	};
        geometry.setTextureCoordinates(0, 0, texCoords);
        
        setWidth(maxWidth);
    }
}


