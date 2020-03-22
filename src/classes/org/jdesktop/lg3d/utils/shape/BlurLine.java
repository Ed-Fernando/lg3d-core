/**
 * Project Looking Glass
 *
 * $RCSfile: BlurLine.java,v $
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
 * $Revision: 1.1 $
 * $Date: 2006-08-26 00:25:55 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.shape;

import java.net.URL;
import javax.vecmath.Color4f;
import org.jdesktop.lg3d.sg.Appearance;
import org.jdesktop.lg3d.sg.GeometryArray;
import org.jdesktop.lg3d.sg.PolygonAttributes;
import org.jdesktop.lg3d.sg.Shape3D;
import org.jdesktop.lg3d.sg.Texture;
import org.jdesktop.lg3d.sg.TextureAttributes;
import org.jdesktop.lg3d.sg.TransparencyAttributes;
import org.jdesktop.lg3d.sg.TriangleStripArray;
import org.jdesktop.lg3d.sg.utils.image.TextureLoader;

/**
 * BlurLine Shape3D object.
 *
 * Based on nu.koidelab.cosmo.wg.decorate.LightDecoration by fumi
 */
public class BlurLine extends Shape3D {
    public static int TYPE_NO_ROUNDING = 1;
    public static int TYPE_GLOW = 2;
    public static int TYPE_CULL_BACK = 4;
    private static float DEFAULT_WIDTH = 0.001f;
    private static Texture texture = null;
    
    /**
     * Create a BlurLine shape with the default width.
     * 
     * @param  lineVertex  A set of vertexes of the line.
     * @param  color       The color of the line.
     */
    public BlurLine(float[] lineVertex, Color4f color) {
        this(lineVertex, color, DEFAULT_WIDTH);
    }
    
    /**
     * Create a BlurLine shape.
     * 
     * @param  lineVertex  A set of vertexes of the line.
     * @param  color       The color of the line.
     * @param  width       The width of the line.
     */
    public BlurLine(float[] lineVertex, Color4f color, float width) {
        this(lineVertex, color, width, 0);
    }
    
    /**
     * Create a BlurLine shape with the default width.
     * 
     * @param  lineVertex  A set of vertexes of the line.
     * @param  color       The color of the line.
     * @param  type        The type of the line.  Type could be TYPE_NO_ROUNDING, 
     *                     TYPE_GLOW, TYPE_CULL_BACK or a combination of these
     *                     values "OR"ed together.
     */
    public BlurLine(float[] lineVertex, Color4f color, int type) {
        this(lineVertex, color, DEFAULT_WIDTH, type);
    }
    
    /**
     * Create a BlurLine shape.
     * 
     * @param  lineVertex  A set of vertexes of the line.
     * @param  color       The color of the line.
     * @param  width       The width of the line.
     * @param  type        The type of the line.  Type could be TYPE_NO_ROUNDING, 
     *                     TYPE_GLOW, TYPE_CULL_BACK or a combination of these
     *                     values "OR"ed together.
     */
    public BlurLine(float[] lineVertex, Color4f color, float width, int type) {
        setCoordinates(lineVertex, 0.5f * width, color.x, color.y, color.z, (type &  TYPE_NO_ROUNDING) == 0);
        Appearance app = createAppearance(color.w, (type & TYPE_GLOW) > 0, (type & TYPE_CULL_BACK) > 0);
        if (texture == null) {
            texture = createTexture(
                getClass().getClassLoader().getResource("resources/images/misc/light3.png"));
        }
        app.setTexture(texture);
        setAppearance(app);
    }
    
    private void setCoordinates(float[] lineVertex, float width, float red, float green, float blue, boolean rounding) {
        if (lineVertex == null) {
            throw new IllegalArgumentException("the lineVertex argument cannot be null");
        }
        if (lineVertex.length < 2 * 3) {
            throw new IllegalArgumentException("the size of lineVertex argument cannot be less than 6");
        }
        if (lineVertex.length % 3 != 0) {
            throw new IllegalArgumentException("the size of lineVertex needs to be multiply of 3");
        }
        
        int lineVertexCount = lineVertex.length / 3;
        if (lineVertexCount > 2 && rounding) {
            lineVertex = smoothLineVertex(lineVertex, width);
            lineVertexCount = lineVertex.length / 3;
        }
        
        int vertexCount = lineVertexCount * 2;
        float[] coords = new float[vertexCount * 3];
        float[] texCoords = new float[vertexCount * 2];
        float[] colors = new float[vertexCount * 3];
        
        for (int i = 0; i < lineVertexCount; i++) {
            int a = (i > 0)?(i - 1):(0);
            int b = (i < lineVertexCount - 1)?(i + 1):(lineVertexCount - 1);
            
            float wx = lineVertex[3 * b + 0] - lineVertex[3 * a + 0];
            float wy = lineVertex[3 * b + 1] - lineVertex[3 * a + 1];
            float f = width / (float)Math.sqrt(wx * wx + wy * wy);
            
            coords[6 * i + 0] = lineVertex[3 * i + 0] - wy * f;
            coords[6 * i + 1] = lineVertex[3 * i + 1] + wx * f;
            coords[6 * i + 2] = lineVertex[3 * i + 2];
            
            coords[6 * i + 3] = lineVertex[3 * i + 0] + wy * f;
            coords[6 * i + 4] = lineVertex[3 * i + 1] - wx * f;
            coords[6 * i + 5] = lineVertex[3 * i + 2];
            
            texCoords[4 * i + 0] = 0.5f;
            texCoords[4 * i + 1] = 0.0f;
            
            texCoords[4 * i + 2] = 0.5f;
            texCoords[4 * i + 3] = 1.0f;
            
            colors[6 * i + 0] = red;
            colors[6 * i + 1] = green;
            colors[6 * i + 2] = blue;
            
            colors[6 * i + 3] = red;
            colors[6 * i + 4] = green;
            colors[6 * i + 5] = blue;
        }
        texCoords[0] = 0.0f;
        texCoords[2] = 0.0f;
        texCoords[4 * (lineVertexCount - 1) + 0] = 1.0f;
        texCoords[4 * (lineVertexCount - 1) + 2] = 1.0f;
                
        GeometryArray geom 
                = new TriangleStripArray(
                    vertexCount,
                    GeometryArray.COORDINATES 
                    | GeometryArray.TEXTURE_COORDINATE_2
                    | GeometryArray.COLOR_3, 
                    new int[] {vertexCount});
        geom.setCoordinates(0, coords);
        geom.setTextureCoordinates(0, 0, texCoords);
        geom.setColors(0, colors);
        setGeometry(geom);
    }
    
    private float[] smoothLineVertex(float[] lineVertex, float width) {
        int lineVertexCount = lineVertex.length / 3;
        if (lineVertexCount <= 2) {
            return lineVertex;
        }
        
        int modLineVCount = 3 * ((2 + 3 * (lineVertexCount - 2)));
        float[] modLine = new float[modLineVCount];
        modLine[0] = lineVertex[0];
        modLine[1] = lineVertex[1];
        modLine[2] = lineVertex[2];
        for (int i = 1; i < lineVertexCount - 1; i++) {
            float wx1 = lineVertex[3 * (i - 1) + 0] - lineVertex[3 * i + 0];
            float wy1 = lineVertex[3 * (i - 1) + 1] - lineVertex[3 * i + 1];
            float f1 = 1.5f * width / (float)Math.sqrt(wx1 * wx1 + wy1 * wy1);
            
            float wx2 = lineVertex[3 * (i + 1) + 0] - lineVertex[3 * i + 0];
            float wy2 = lineVertex[3 * (i + 1) + 1] - lineVertex[3 * i + 1];
            float f2 = 1.5f * width / (float)Math.sqrt(wx2 * wx2 + wy2 * wy2);
            
            modLine[3 * (3 * i - 2) + 0] = lineVertex[3 * i + 0] + wx1 * f1;
            modLine[3 * (3 * i - 2) + 1] = lineVertex[3 * i + 1] + wy1 * f1;
            modLine[3 * (3 * i - 2) + 2] = lineVertex[3 * i + 2];
            
            modLine[3 * (3 * i - 1) + 0] = lineVertex[3 * i + 0] + 0.333f * (wx1 * f1 + wx2 * f2);
            modLine[3 * (3 * i - 1) + 1] = lineVertex[3 * i + 1] + 0.333f * (wy1 * f1 + wy2 * f2);
            modLine[3 * (3 * i - 1) + 2] = lineVertex[3 * i + 2];
            
            modLine[3 * (3 * i + 0) + 0] = lineVertex[3 * i + 0] + wx2 * f2;
            modLine[3 * (3 * i + 0) + 1] = lineVertex[3 * i + 1] + wy2 * f2;
            modLine[3 * (3 * i + 0) + 2] = lineVertex[3 * i + 2];
        }
        int i = 3 * (1 + 3 * (lineVertexCount - 2));
        modLine[i + 0] = lineVertex[3 * (lineVertexCount - 1) + 0];
        modLine[i + 1] = lineVertex[3 * (lineVertexCount - 1) + 1];
        modLine[i + 2] = lineVertex[3 * (lineVertexCount - 1) + 2];
        
        return modLine;
    }
    
    static Appearance createAppearance(float alpha, boolean glow, boolean cullback) {
        Appearance app = new Appearance();
        
        int dstBlendFunction 
                = (glow)
                    ?(TransparencyAttributes.BLEND_ONE)
                    :(TransparencyAttributes.BLEND_ONE_MINUS_SRC_ALPHA);
        
        TransparencyAttributes ta = 
            new TransparencyAttributes(
                TransparencyAttributes.BLENDED, 
                1.0f - alpha,
                TransparencyAttributes.BLEND_SRC_ALPHA,
                dstBlendFunction);
        ta.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
        app.setTransparencyAttributes(ta);
        
        TextureAttributes texa = new TextureAttributes();
        texa.setTextureMode(TextureAttributes.MODULATE);
        app.setTextureAttributes(texa);
        
        int cullFace 
                = (cullback)
                    ?(PolygonAttributes.CULL_BACK)
                    :(PolygonAttributes.CULL_NONE);
        app.setPolygonAttributes(
            new PolygonAttributes(
                PolygonAttributes.POLYGON_FILL, 
                cullFace,
                0.0f, false, 0.0f));
        
        app.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_READ);
        
        return app;
    }
    
    static Texture createTexture(URL imageUrl) {
        TextureLoader textureLoader 
            = new TextureLoader(imageUrl, TextureLoader.Y_UP | TextureLoader.BY_REFERENCE, null);
        Texture texture = textureLoader.getTexture();
        if (texture == null) {
            throw new RuntimeException("Texture image for BlurLine is not found: " + imageUrl);
        }
        texture.setMinFilter(Texture.BASE_LEVEL_LINEAR);
        texture.setMagFilter(Texture.BASE_LEVEL_LINEAR);

        return texture;
    }
}
