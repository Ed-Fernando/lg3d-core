/**
 * Project Looking Glass
 *
 * $RCSfile: GlassyPanel.java,v $
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
 * $Revision: 1.10 $
 * $Date: 2006-04-25 04:53:55 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.shape;

import org.jdesktop.lg3d.sg.*;
import javax.vecmath.*;


/**
 * A Shape3D object that displays a glassy panel.
 */
public class GlassyPanel extends Shape3D {
    private static float defaultEdgeWidth = 0.0005f;
    
    private IndexedGeometryArray geometry;
    private float depth;
    private float edge;
    private float xShift;
    private float yShift;
    private float zShift;
    
    /**
     * Create a GlassyRingPanel object.  The shape's foremost surface 
     * will be placed on the Z=0 plane with X=0, Y=0 
     * at the center.
     *
     * @param  width       Width of the panel.
     * @param  height      Height of the panel.
     * @param  depth       Z depth of the panel.
     * @param  app         Appearance to use.
     */
    public GlassyPanel(float width, float height, float depth, Appearance app) {
        this(width, height, depth, defaultEdgeWidth, app);
    }
    
    /**
     * Create a GlassyRingPanel object.  The shape's foremost surface 
     * will be placed on the Z=0 plane with X=0, Y=0 
     * at the center.
     *
     * @param  width       Width of the panel.
     * @param  height      Height of the panel.
     * @param  depth       Z depth of the panel.
     * @param  edge        Width of the fuzzy area around the edge.
     * @param  app         Appearance to use.
     */
    public GlassyPanel(float width, float height, float depth, float edge,
	Appearance app)
    {
        this(width, height, depth, edge, 0.0f, 0.0f, 0.0f, app);
    }
    
    /**
     * Create a GlassyRingPanel object.  The shape's foremost surface 
     * will be placed on the Z=zShift plane with X=xShift, Y=yShift 
     * at the center.
     *
     * @param  width       Width of the panel.
     * @param  height      Height of the panel.
     * @param  depth       Z depth of the panel.
     * @param  edge        Width of the fuzzy area around the edge.
     * @param  xShift      Coordinate bias toward the X direction.
     * @param  yShift      Coordinate bias toward the Y direction.
     * @param  zShift      Coordinate bias toward the X direction.
     * @param  app         Appearance to use.
     */
    public GlassyPanel(float width, float height, float depth, float edge,
        float xShift, float yShift, float zShift, Appearance app)
    {
        // GlassyPanel is constructed using IndexedQuadArray
        // coords[0     to 3*4 -1]   are of the rect at z == 0
        // coords[3*4   to 3*4*2 -1] are of the rect at z == -e
        // coords[3*4*2 to 3*4*3 -1] are of the rect at z == -d +e
        // coords[3*4*3 to 3*4*4 -1] are of the rect at z == -d
        // coords[3*4*4 to 3*4*5 -1] are of the inner rect at z == 0
        
	this.depth = depth;
	this.edge = edge;
        this.xShift = xShift;
        this.yShift = yShift;
        this.zShift = zShift;
        
	setAppearance(app);

	geometry
	    = new IndexedQuadArray(20, 
		GeometryArray.COORDINATES
		| GeometryArray.NORMALS
		| GeometryArray.COLOR_4,
		0, null,
		indices.length);
	geometry.setCapability(GeometryArray.ALLOW_COORDINATE_WRITE);

	setSize(width, height);

	geometry.setCoordinateIndices(0, indices);
	geometry.setNormals(0, normals);
	geometry.setNormalIndices(0, normalIndices);
	geometry.setColors(0, colors4);
	geometry.setColorIndices(0, colorIndices);

        setGeometry(geometry);
    }

    public void setSize(float width, float height) {
	// REMINDER -- think about optimization later

	float w = width / 2.0f;
	float h = height / 2.0f;
	float d = depth;
	float e = edge;

	float[] coords = {
	    xShift -w, yShift -h, zShift,
	    xShift +w, yShift -h, zShift,
	    xShift +w, yShift +h, zShift,
	    xShift -w, yShift +h, zShift,

	    xShift -w, yShift -h, zShift -e,
	    xShift +w, yShift -h, zShift -e,
	    xShift +w, yShift +h, zShift -e,
	    xShift -w, yShift +h, zShift -e,

	    xShift -w, yShift -h, zShift +e -d,
	    xShift +w, yShift -h, zShift +e -d,
	    xShift +w, yShift +h, zShift +e -d,
	    xShift -w, yShift +h, zShift +e -d,

	    xShift -w, yShift -h, zShift -d,
	    xShift +w, yShift -h, zShift -d,
	    xShift +w, yShift +h, zShift -d,
	    xShift -w, yShift +h, zShift -d,

	    xShift -w +e, yShift -h +e, zShift,
	    xShift +w -e, yShift -h +e, zShift,
	    xShift +w -e, yShift +h -e, zShift,
	    xShift -w +e, yShift +h -e, zShift,
	};

	geometry.setCoordinates(0, coords);
    }
    
    private static int[] indices = {
    	// top
    	3, 2, 6, 7,
    	7, 6, 10, 11,
       	11, 10, 14, 15,
        
        // bottom
        1, 0, 4, 5,
        5, 4, 8, 9,
        9, 8, 12, 13,
        
        // left
        0, 3, 7, 4,
        4, 7, 11, 8,
        8, 11, 15, 12,

        // right
        2, 1, 5, 6, 
        6, 5, 9, 10, 
        10, 9, 13, 14,

        // front
        0, 1, 17, 16,
        1, 2, 18, 17,
        2, 3, 19, 18,
        3, 0, 16, 19,
        16, 17, 18, 19,
    };

    private final float[] colors4 = {
        0.0f, 0.0f, 0.0f, 0.00f,
        1.0f, 1.0f, 1.0f, 0.75f,
        1.0f, 1.0f, 1.0f, 0.35f,
    };

    private final int[] colorIndices = {
        0, 0, 1, 1,
        1, 1, 1, 1,
        1, 1, 0, 0,

        0, 0, 1, 1,
        1, 1, 1, 1,
        1, 1, 0, 0,

        0, 0, 1, 1,
        1, 1, 1, 1,
        1, 1, 0, 0,
	    
        0, 0, 1, 1,
        1, 1, 1, 1,
        1, 1, 0, 0,

        0, 0, 2, 2,
        0, 0, 2, 2,
        0, 0, 2, 2,
        0, 0, 2, 2,
        2, 2, 2, 2,
    };

    private final float[] normals = {
         0.0f,  1.0f,  0.0f, // top
         0.0f, -1.0f,  0.0f, // bottom
        -1.0f,  0.0f,  0.0f, // left
         1.0f,  0.0f,  0.0f, // right
         0.0f,  0.0f,  1.0f, // front
    };

    private final int[] normalIndices = {
        0, 0, 0, 0,
        0, 0, 0, 0,
        0, 0, 0, 0,

        1, 1, 1, 1,
        1, 1, 1, 1,
        1, 1, 1, 1,

        2, 2, 2, 2,
        2, 2, 2, 2,
        2, 2, 2, 2,

        3, 3, 3, 3,
        3, 3, 3, 3,
        3, 3, 3, 3,

        4, 4, 4, 4,
        4, 4, 4, 4,
        4, 4, 4, 4,
        4, 4, 4, 4,
        4, 4, 4, 4,
    };
}

