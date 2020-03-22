/**
 * Project Looking Glass
 *
 * $RCSfile: RectShadow.java,v $
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
 * $Date: 2006-02-08 02:40:16 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.shape;

import org.jdesktop.lg3d.sg.*;
import javax.vecmath.*;


public class RectShadow extends Shape3D {
    private static float sin00 = 0.00000000f;
    private static float sin23 = 0.38268343f;
    private static float sin45 = 0.70710677f;
    private static float sin68 = 0.92387950f;
    private static float sin90 = 1.00000000f;

    private IndexedGeometryArray geometry;
    private float north;
    private float east;
    private float south;
    private float west;
    private float inner;
    private float zShift;


    public RectShadow(float width, float height,
	float north, float east, float south, float west, float inner,
	float zShift, float alpha)
    {
	this.north = north;
	this.east = east;
	this.south = south;
	this.west = west;
	this.inner = inner;
	this.zShift = zShift;

	setPickable(false);

	Appearance app = new ShadowAppearance();
	setAppearance(app);

	geometry
	    = new IndexedQuadArray(
		28,
		GeometryArray.COORDINATES
		| GeometryArray.COLOR_4,
		0, null,
		64);
	geometry.setCapability(GeometryArray.ALLOW_COORDINATE_WRITE);
	setGeometry(geometry);

        float[] colors4 = {
            0.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.0f, alpha,
        };

	setSize(width, height);
        
	geometry.setCoordinateIndices(0, indices);
	geometry.setColors(0, colors4);
	geometry.setColorIndices(0, colorIndices);
    }

    public void setSize(float width, float height) {
	// REMINDER -- think about optimization later
	float w = width / 2.0f;
	float h = height / 2.0f;
	float bn = north;
	float be = east;
	float bs = south;
	float bw = west;
	float bi = inner;
	float z = zShift;

	float[] coords = {
	    -w, -h,  z,
	     w, -h,  z,
	     w,  h,  z,
	    -w,  h,  z,

	    -w -bw * sin90, -h -bs * sin00,  z,
	    -w -bw * sin68, -h -bs * sin23,  z,
	    -w -bw * sin45, -h -bs * sin45,  z,
	    -w -bw * sin23, -h -bs * sin68,  z,
	    -w -bw * sin00, -h -bs * sin90,  z,

	     w +be * sin00, -h -bs * sin90,  z,
	     w +be * sin23, -h -bs * sin68,  z,
	     w +be * sin45, -h -bs * sin45,  z,
	     w +be * sin68, -h -bs * sin23,  z,
	     w +be * sin90, -h -bs * sin00,  z,

	     w +be * sin90,  h +bn * sin00,  z,
	     w +be * sin68,  h +bn * sin23,  z,
	     w +be * sin45,  h +bn * sin45,  z,
	     w +be * sin23,  h +bn * sin68,  z,
	     w +be * sin00,  h +bn * sin90,  z,

	    -w -bw * sin00,  h +bn * sin90,  z,
	    -w -bw * sin23,  h +bn * sin68,  z,
	    -w -bw * sin45,  h +bn * sin45,  z,
	    -w -bw * sin68,  h +bn * sin23,  z,
	    -w -bw * sin90,  h +bn * sin00,  z,

	    -w +bi, -h +bi,  z,
	     w -bi, -h +bi,  z,
	     w -bi,  h -bi,  z,
	    -w +bi,  h -bi,  z,
	};

	geometry.setCoordinates(0, coords);
    }
    
    static private int[] indices = {
         0,  4,  5,  6,
         0,  6,  7,  8,

         0,  8,  9,  1,

         1,  9, 10 ,11,
         1, 11, 12, 13,

         1, 13, 14,  2,

         2, 14, 15, 16,
         2, 16, 17, 18,

         2, 18, 19,  3,

         3, 19, 20, 21,
         3, 21, 22, 23,

         3, 23,  4,  0,

         0,  1, 25, 24,
         1,  2, 26, 25,
         2,  3, 27, 26,
         3,  0, 24, 27,
    };

    static private int[] colorIndices = {
        1, 0, 0, 0,
        1, 0, 0, 0,
        1, 0, 0, 1,

        1, 0, 0, 0,
        1, 0, 0, 0,
        1, 0, 0, 1,

        1, 0, 0, 0,
        1, 0, 0, 0,
        1, 0, 0, 1,

        1, 0, 0, 0,
        1, 0, 0, 0,
        1, 0, 0, 1,

        1, 1, 0, 0,
        1, 1, 0, 0,
        1, 1, 0, 0,
        1, 1, 0, 0,
    };
}
