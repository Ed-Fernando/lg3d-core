/**
 * Project Looking Glass
 *
 * $RCSfile: FuzzyEdgePanelImpl.java,v $
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
 * $Revision: 1.6 $
 * $Date: 2006-06-07 23:59:15 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.shape;

import org.jdesktop.lg3d.sg.*;


class FuzzyEdgePanelImpl {
    private IndexedGeometryArray geometry;
    private float northEdge, southEdge, eastEdge, westEdge;
    private float height;
    private float width;
    
    public FuzzyEdgePanelImpl(float width, float height, float northEdge,
                          float eastEdge, float southEdge, float westEdge,
                          Appearance app,
                          Shape3D shape) 
    {                          
	this.northEdge = northEdge;
        this.eastEdge = eastEdge;
        this.southEdge = southEdge;
        this.westEdge = westEdge;
        this.width = width;
        this.height = height;
        
        shape.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
        app.setCapability(Appearance.ALLOW_TEXTURE_READ);
        
	shape.setAppearance(app);
        
        int color_config = GeometryArray.COLOR_4;
        if (northEdge == 0.0f && eastEdge == 0.0f 
            && southEdge == 0.0f && westEdge == 0.0f) 
        {
            color_config = GeometryArray.COLOR_3;
        }
	// picking  system has an issue with IndexedTriangleArray
	geometry
	    = new IndexedQuadArray(12, 
		GeometryArray.COORDINATES
		| GeometryArray.TEXTURE_COORDINATE_2
		| GeometryArray.NORMALS
		| color_config,
		1, new int[] {0},
		36);

	geometry.setCapability(GeometryArray.ALLOW_COORDINATE_WRITE);
	geometry.setCapability(GeometryArray.ALLOW_TEXCOORD_WRITE);
	shape.setGeometry(geometry);

	setSize(width, height);

	geometry.setCoordinateIndices(0, indices);
	geometry.setTextureCoordinateIndices(0, 0, indices);
	geometry.setNormals(0, normals);
	geometry.setNormalIndices(0, normalIndices);
        if (color_config == GeometryArray.COLOR_4) {
            geometry.setColors(0, colors4);
        } else {
            geometry.setColors(0, colors3_opaque);
        }
	geometry.setColorIndices(0, colorIndices);
    }
    
    public void setSize(float width, float height) {
        setSize(width, height, 1.0f, 1.0f);
    }
    
    public void setSize(float width, float height,
                        float textureWidthScale, float textureHeightScale) 
    {
	// REMINDER -- think about optimization later
        this.width = width;
        this.height = height;
        
	float w = width;
	float h = height;
        float ne = northEdge;
        float se = southEdge;
        float ee = eastEdge;
        float we = westEdge;
	float w2 = width / 2.0f;
	float h2 = height/ 2.0f;
        
	float[] coords = {
	    -w2 +we, -h2 +se, 0.0f,
	     w2 -ee, -h2 +se, 0.0f,
	     w2 -ee,  h2 -ne, 0.0f,
	    -w2 +we,  h2 -ne, 0.0f,

	    -w2    , -h2 +se, 0.0f,
	    -w2 +we, -h2    , 0.0f,

	     w2 -ee, -h2    , 0.0f,
	     w2    , -h2 +se, 0.0f,

	     w2    ,  h2 -ne, 0.0f,
	     w2 -ee,  h2    , 0.0f,
 
	    -w2 +we,  h2    , 0.0f,
	    -w2    ,  h2 -ne, 0.0f,
	};
        
        float tw = w * textureWidthScale;
        float th = h * textureHeightScale;
        
        // Flipped TC, image is rendered inverted which is correct 
        // for X images
	float[] texCoords = {
                (  +we)/tw, (h -ne)/th,     // 3
                (w -ee)/tw, (h -ne)/th,     // 2
                (w -ee)/tw, (  +se)/th,     // 1
                (  +we)/tw, (  +se)/th,     // 0
                
                (    0)/tw, (h -ne)/th,     //11
                (  +we)/tw, (h    )/th,     //10
                
                (w -ee)/tw, (h    )/th,     // 9
                (w    )/tw, (h -ne)/th,     // 8
                
                (w    )/tw, (  +se)/th,     // 7
                (w -ee)/tw, (    0)/th,     // 6
                
                (  +we)/tw, (    0)/th,     // 5
                (    0)/tw, (  +se)/th,     // 4
        };
        
	geometry.setCoordinates(0, coords);
        geometry.setTextureCoordinates(0, 0, texCoords);
    }
    
    private static int[] indices = {
         0,  1, 2, 3,
         5,  6, 1, 0,
         6,  7, 1, 1,
         7,  8, 2, 1,
         8,  9, 2, 2,
         9, 10, 3, 2,
        10, 11, 3, 3,
        11,  4, 0, 3,
         4,  5, 0, 0,
    };

    private static float[] normals = {
        0.0f, 0.0f, 1.0f,
    };

    private static int[] normalIndices = {
         0, 0, 0, 0,
         0, 0, 0, 0,
         0, 0, 0, 0,
         0, 0, 0, 0,
         0, 0, 0, 0,
         0, 0, 0, 0,
         0, 0, 0, 0,
         0, 0, 0, 0,
         0, 0, 0, 0,
    };

    private static float[] colors4 = {
        1.0f, 1.0f, 1.0f, 0.0f,
        1.0f, 1.0f, 1.0f, 1.0f,
    };

    private static float[] colors3_opaque = {
        1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 1.0f,
    };

    private static int[] colorIndices = {
         1, 1, 1, 1,
         0, 0, 1, 1,
         0, 0, 1, 1,
         0, 0, 1, 1,
         0, 0, 1, 1,
         0, 0, 1, 1,
         0, 0, 1, 1,
         0, 0, 1, 1,
         0, 0, 1, 1,
    };
}


