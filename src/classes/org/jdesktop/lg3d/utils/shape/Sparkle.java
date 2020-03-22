/**
 * Project Looking Glass
 *
 * $RCSfile: Sparkle.java,v $
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
 * $Date: 2006-08-26 00:25:57 $
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
 * A Shape3D object that has sparkle like appearance.
 */
public class Sparkle extends Shape3D {
    public static int TYPE_NO_GLOW = 2;
    public static int TYPE_CULL_BACK = 4;
    private static Texture texture = null;
    
    
    /**
     * Create a Sparkle shape.
     * 
     * @param  size        The size of this Sparkle object.
     * @param  color       The color of the line.
     */
    public Sparkle(float size, Color4f color) {
        this(size, color, 0);
    }
    
    /**
     * Create a Sparkle shape.
     * 
     * @param  size        The size of this Sparkle object.
     * @param  zBias       The Z directional bias of this object.
     * @param  color       The color of the line.
     */
    public Sparkle(float size, float zBias, Color4f color) {
        this(size, zBias, color, 0);
    }
    
    /**
     * Create a Sparkle shape.
     * 
     * @param  size        The size of this Sparkle object.
     * @param  color       The color of the line.
     * @param  type        The type of the line.  Type could be
     *                     TYPE_NO_GLOW, TYPE_CULL_BACK or a combination of these
     *                     values "OR"ed together.
     */
    public Sparkle(float size, Color4f color, int type) {
        this(size, 0.0f, color, type);
    }
    
    /**
     * Create a Sparkle shape.
     * 
     * @param  size        The size of this Sparkle object.
     * @param  zBias       The Z directional bias of this object.
     * @param  color       The color of the line.
     * @param  type        The type of the line.  Type could be
     *                     TYPE_NO_GLOW, TYPE_CULL_BACK or a combination of these
     *                     values "OR"ed together.
     */
    public Sparkle(float size, float zBias, Color4f color, int type) {
        setCoordinates(size, zBias, color.x, color.y, color.z);
        Appearance app = BlurLine.createAppearance(color.w, (type & TYPE_NO_GLOW) == 0, (type & TYPE_CULL_BACK) > 0);
        if (texture == null) {
            texture = BlurLine.createTexture(
                getClass().getClassLoader().getResource("resources/images/misc/light2.png"));
        }
        app.setTexture(texture);
        setAppearance(app);
    }
    
    private void setCoordinates(float size, float zBias, float red, float green, float blue) {
        float[] coords = new float[] {
                    -0.5f * size, -0.5f * size, zBias,
                    -0.5f * size,  0.5f * size, zBias,
                     0.5f * size, -0.5f * size, zBias,
                     0.5f * size,  0.5f * size, zBias,
                };
        float[] texCoords = new float[] {
                    0.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 0.0f,
                    1.0f, 1.0f,
                };
        float[] colors = new float[] {
                    red, green, blue,
                    red, green, blue,
                    red, green, blue,
                    red, green, blue,
                };
        
        GeometryArray geom 
                = new TriangleStripArray(
                    coords.length / 3,
                    GeometryArray.COORDINATES 
                    | GeometryArray.TEXTURE_COORDINATE_2
                    | GeometryArray.COLOR_3, 
                    new int[] {coords.length / 3});
        geom.setCoordinates(0, coords);
        geom.setTextureCoordinates(0, 0, texCoords);
        geom.setColors(0, colors);
        setGeometry(geom);
        
        setPickable(false);
    }
}
