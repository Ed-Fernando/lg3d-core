/**
 * Project Looking Glass
 *
 * $RCSfile: NativeWindowFuzzyEdgePanel.java,v $
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
 * $Date: 2006-06-06 01:23:01 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.nativewindow;

import org.jdesktop.lg3d.sg.*;


public class NativeWindowFuzzyEdgePanel extends Shape3D {
    public enum OriginType { CENTER, TOP_LEFT };
    private static final float defaultEdge = 0.0005f;
    
    private IndexedGeometryArray geometry;
    private float northEdge, southEdge, eastEdge, westEdge;
    private float height;
    private float width;
    private float textureHeightScale;
    private float textureWidthScale;
    private OriginType origin;
    private boolean yUp;
    private boolean isNative;
    
    public NativeWindowFuzzyEdgePanel(float width, float height, Appearance app) {
        this(width, height, defaultEdge, defaultEdge, defaultEdge, defaultEdge, 
                app, OriginType.CENTER, true, false);
    }
    
    public NativeWindowFuzzyEdgePanel(float width, float height, 
                          float northEdge, float eastEdge, 
                          float southEdge, float westEdge,
                          Appearance app,
                          OriginType origin,
                          boolean yUp,
                          boolean isNative) 
    {                          
	this.northEdge = northEdge;
        this.eastEdge = eastEdge;
        this.southEdge = southEdge;
        this.westEdge = westEdge;
        this.width = width;
        this.height = height;
        this.origin = origin;
        this.yUp = yUp;
        this.isNative = isNative;
        
        setCapability(Shape3D.ALLOW_APPEARANCE_READ);
        app.setCapability(Appearance.ALLOW_TEXTURE_READ);
        
	setAppearance(app);
        
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
	setGeometry(geometry);

	setSize(width, height, 1f, 1f);

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

    public void setHeight(float height, float textureHeightScale) {
        setSize(width, height, textureWidthScale, textureHeightScale);
    }
    
    public void setWidth(float width, float textureWidthScale) {
        setSize( width, height, textureWidthScale, textureHeightScale);
    }
    
    public void setSize(float width, float height, 
                        float textureWidthScale, float textureHeightScale) 
    {
	// REMINDER -- think about optimization later
        this.width = width;
        this.height = height;
        this.textureWidthScale = textureWidthScale;
        this.textureHeightScale = textureHeightScale;
        
	float w = width;
	float h = height;
        float ne = northEdge;
        float se = southEdge;
        float ee = eastEdge;
        float we = westEdge;
	float w2 = width / 2.0f;
	float h2 = height/ 2.0f;
        
        float xo = 0f;
        float yo = 0f;

        if (origin == OriginType.TOP_LEFT) {
            xo = w2;      // X Origin 0 for origin in center, w2 for left hand edge
            yo = -h2;     // Y Origin 0 for origin in center -h2 for origin at top
        }
        
        float tw = w * textureWidthScale;
        float th = h * textureHeightScale;
        
	float[] coords = {
	    xo+ -w2 +we, yo+ -h2 +se, 0.0f,
	    xo+  w2 -ee, yo+ -h2 +se, 0.0f,
	    xo+  w2 -ee, yo+  h2 -ne, 0.0f,
	    xo+ -w2 +we, yo+  h2 -ne, 0.0f,

	    xo+ -w2    , yo+ -h2 +se, 0.0f,
	    xo+ -w2 +we, yo+ -h2    , 0.0f,

	    xo+  w2 -ee, yo+ -h2    , 0.0f,
	    xo+  w2    , yo+ -h2 +se, 0.0f,

	    xo+  w2    , yo+  h2 -ne, 0.0f,
	    xo+  w2 -ee, yo+  h2    , 0.0f,
 
	    xo+ -w2 +we, yo+  h2    , 0.0f,
	    xo+ -w2    , yo+  h2 -ne, 0.0f,
	};

	// REMINDER -- read reminder bellow
        if ((tw == 0) && (th == 0)) {
	    tw = 1.0f;
	    th = 1.0f;
	}
	
        // Ydown (yUp == false) images require the texture coordinates 
        // to be translated toward the upper left corner because that is where
        // the significant part of the texture is. (In Yup images the significant
        // part of the texture is in the lower left corner).
        float tho = (isNative && !yUp)?((th - h)/th):(0.0f);
        
        // Flipped TC, image is rendered inverted which is correct 
        // for X images
	float[] texCoords = 
            (yUp)
            ?(new float[] {
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

            })
            :(new float[] {
                (  +we)/tw, tho + (  +se)/th,     // 0
                (w -ee)/tw, tho + (  +se)/th,     // 1
                (w -ee)/tw, tho + (h -ne)/th,     // 2
                (  +we)/tw, tho + (h -ne)/th,     // 3
                
                (    0)/tw, tho + (  +se)/th,     // 4
                (  +we)/tw, tho + (    0)/th,     // 5
                
                (w -ee)/tw, tho + (    0)/th,     // 6
                (w    )/tw, tho + (  +se)/th,     // 7
                
                (w    )/tw, tho + (h -ne)/th,     // 8
                (w -ee)/tw, tho + (h    )/th,     // 9
                
                (  +we)/tw, tho + (h    )/th,     //10
                (    0)/tw, tho + (h -ne)/th,     //11
            });
        
	geometry.setCoordinates(0, coords);
        
	// REMINDER -- this a bit hack for native window.
	// the reason of this hack is to not to update the texture coordinate
	// during resize operation. NativeWindowObject class will pass textureWidthScale,
	// textureHeightScale as 0, to ignore texture coordinate.
	if ((textureWidthScale != 0) && (textureHeightScale != 0)) {
            geometry.setTextureCoordinates(0, 0, texCoords);
	}    
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


