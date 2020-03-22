/**
 * Project Looking Glass
 *
 * $RCSfile: GlassyBentText2D.java,v $
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
 * $Date: 2006-06-29 23:09:58 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.shape;

import javax.vecmath.Color4f;
import org.jdesktop.lg3d.sg.Appearance;
import org.jdesktop.lg3d.sg.GeometryArray;
import org.jdesktop.lg3d.sg.IndexedTriangleStripArray;
import org.jdesktop.lg3d.sg.Shape3D;
import org.jdesktop.lg3d.sg.Texture;


public class GlassyBentText2D extends Shape3D {
    public enum Alignment {LEFT, CENTER, RIGHT};
    public enum LightDirection {TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT};
    
    private IndexedTriangleStripArray geometry;
    private Appearance appearance;
    private float angle;
    private float radius;
    private float widthScale;
    private float startAngle;
    private float maxAngle;
    private float prevAdjustedAngle;
    private float height;
    private Alignment alignment;
    private int divisions;
    private LightDirection lightDirection;

    
    public GlassyBentText2D(String text, float radius, float startAngle, 
            float maxAngle, float height, Color4f textColor)
    {
        this(text, radius, startAngle, maxAngle, height, textColor, 
                LightDirection.TOP_LEFT, Alignment.LEFT);
    }
    
    public GlassyBentText2D(String text, float radius, float startAngle, 
            float maxAngle, float height, Color4f textColor, 
            LightDirection lightDirection, Alignment alignment)
    {
        this(text, radius, startAngle, maxAngle, height, textColor, 
                lightDirection, alignment, 12, 1.2f);
    }
    
    public GlassyBentText2D(String text, float radius, float startAngle, float maxAngle, float height, 
            Color4f textColor, LightDirection lightDirection, Alignment alignment, 
            int divisions, float widthScale)
    {
        this.radius = radius;
        this.startAngle = startAngle;
        this.maxAngle = maxAngle;
	this.height = height;
        this.alignment = alignment;
        this.divisions = divisions;
        this.widthScale = widthScale;
        this.lightDirection = lightDirection;
        
	appearance = new SimpleAppearance(
		textColor.x, textColor.y, textColor.z, textColor.w,
		SimpleAppearance.ENABLE_TEXTURE
		| SimpleAppearance.DISABLE_CULLING
		);
        
	appearance.setCapability(Appearance.ALLOW_TEXTURE_READ);
        appearance.setCapability(Appearance.ALLOW_TEXTURE_WRITE);
	setAppearance(appearance);
        
        int n = divisions;
	geometry
	    = new IndexedTriangleStripArray(
                2 * n, 
		GeometryArray.COORDINATES
		| GeometryArray.TEXTURE_COORDINATE_2
		| GeometryArray.NORMALS,
                1, new int[] {0},
		2 * n, new int[] {2 * n});
	geometry.setCapability(GeometryArray.ALLOW_COORDINATE_WRITE);
        geometry.setCapability(GeometryArray.ALLOW_TEXCOORD_WRITE);
        
	setText(text);
        
	int[] indices = new int[2 * n];
        int[] normalIndices = new int[indices.length];
        for (int i = 0; i < n; i++) {
            indices[2 * i + 0] = n + i;
            indices[2 * i + 1] = i;
            
            normalIndices[2 * i + 0] = i;
            normalIndices[2 * i + 1] = i;
        }
        
	geometry.setCoordinateIndices(0, indices);
	geometry.setTextureCoordinateIndices(0, 0, indices);
	geometry.setNormalIndices(0, normalIndices);
	
        setGeometry(geometry);
    }
    

    public void setWidth(float angleLimit) {
	float a = (angle < angleLimit)?(angle):(angleLimit);

	if (a == prevAdjustedAngle) {
	    return;
	}
	prevAdjustedAngle = a;
        maxAngle = angleLimit;
        
        float ao = startAngle;
        switch (alignment) {
            case LEFT:
                break;
            case CENTER:
                ao -= a / 2;
                break;
            case RIGHT:
                ao -= a;
                break;
        }
            
        int n = divisions;
	float[] coords = new float[2 * n * 3];
        float[] normals = new float[n * 3];
        for (int i = 0; i < n; i++) {
            float p = (float)i / (n - 1);
            float b = ao + a * p;
            float x = (float)Math.cos(b);
            float y = (float)Math.sin(b);
            
            coords[(0 * n + i) * 3 + 0] = x * radius;
            coords[(0 * n + i) * 3 + 1] = y * radius;
            coords[(0 * n + i) * 3 + 2] = 0.0f;
            
            coords[(1 * n + i) * 3 + 0] = x * radius;
            coords[(1 * n + i) * 3 + 1] = y * radius;
            coords[(1 * n + i) * 3 + 2] = height;
            
            normals[i * 3 + 0] = x;
            normals[i * 3 + 1] = y;
            normals[i * 3 + 2] = 0.0f;
        }
	geometry.setCoordinates(0, coords);
        geometry.setNormals(0, normals);
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
        
        float width = gttg.getWidth();
        angle = width / radius;
        
        float widthRatio = gttg.getWidthRatio();
        float heightRatio = gttg.getHeightRatio();
        
        int n = divisions;
	float[] texCoords = new float[2 * n * 2];
        for (int i = 0; i < n; i++) {
            float p = (float)i / (n - 1);
            
            texCoords[(0 * n + i) * 2 + 0] = widthRatio * p;
            texCoords[(0 * n + i) * 2 + 1] = heightRatio;
            
            texCoords[(1 * n + i) * 2 + 0] = widthRatio * p;
            texCoords[(1 * n + i) * 2 + 1] = 0.0f;
        }
        geometry.setTextureCoordinates(0, 0, texCoords);
        
        setWidth(maxAngle);
    }
}

