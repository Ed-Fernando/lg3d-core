/**
 * Project Looking Glass
 *
 * $RCSfile: ColorCube.java,v $
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
 * $Revision: 1.2 $
 * $Date: 2004-06-23 18:52:12 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.shape;

import org.jdesktop.lg3d.sg.*;

/**
 * Simple color-per-vertex cube with a different color for each face
 */
public class ColorCube extends Shape3D {
    private static final float[] verts = {
    // front face
	 1.0f, -1.0f,  1.0f,
	 1.0f,  1.0f,  1.0f,
	-1.0f,  1.0f,  1.0f,
	-1.0f, -1.0f,  1.0f,
    // back face
	-1.0f, -1.0f, -1.0f,
	-1.0f,  1.0f, -1.0f,
	 1.0f,  1.0f, -1.0f,
	 1.0f, -1.0f, -1.0f,
    // right face
	 1.0f, -1.0f, -1.0f,
	 1.0f,  1.0f, -1.0f,
	 1.0f,  1.0f,  1.0f,
	 1.0f, -1.0f,  1.0f,
    // left face
	-1.0f, -1.0f,  1.0f,
	-1.0f,  1.0f,  1.0f,
	-1.0f,  1.0f, -1.0f,
	-1.0f, -1.0f, -1.0f,
    // top face
	 1.0f,  1.0f,  1.0f,
	 1.0f,  1.0f, -1.0f,
	-1.0f,  1.0f, -1.0f,
	-1.0f,  1.0f,  1.0f,
    // bottom face
	-1.0f, -1.0f,  1.0f,
	-1.0f, -1.0f, -1.0f,
	 1.0f, -1.0f, -1.0f,
	 1.0f, -1.0f,  1.0f,
    };

    private static final float[] colors = {
    // front face (red)
	1.0f, 0.0f, 0.0f,
	1.0f, 0.0f, 0.0f,
	1.0f, 0.0f, 0.0f,
	1.0f, 0.0f, 0.0f,
    // back face (green)
	0.0f, 1.0f, 0.0f,
	0.0f, 1.0f, 0.0f,
	0.0f, 1.0f, 0.0f,
	0.0f, 1.0f, 0.0f,
    // right face (blue)
	0.0f, 0.0f, 1.0f,
	0.0f, 0.0f, 1.0f,
	0.0f, 0.0f, 1.0f,
	0.0f, 0.0f, 1.0f,
    // left face (yellow)
	1.0f, 1.0f, 0.0f,
	1.0f, 1.0f, 0.0f,
	1.0f, 1.0f, 0.0f,
	1.0f, 1.0f, 0.0f,
    // top face (magenta)
	1.0f, 0.0f, 1.0f,
	1.0f, 0.0f, 1.0f,
	1.0f, 0.0f, 1.0f,
	1.0f, 0.0f, 1.0f,
    // bottom face (cyan)
	0.0f, 1.0f, 1.0f,
	0.0f, 1.0f, 1.0f,
	0.0f, 1.0f, 1.0f,
	0.0f, 1.0f, 1.0f,
    };

    /**
     * Constructs a color cube with unit scale.  The corners of the
     * color cube are [-1,-1,-1] and [1,1,1].
     */
    public ColorCube() {
	QuadArray cube = new QuadArray(24, QuadArray.COORDINATES | 
		QuadArray.COLOR_3);

	cube.setCoordinates(0, verts);
	cube.setColors(0, colors);

	this.setGeometry(cube);
    }

    /**
     * Constructs a color cube with the specified scale.  The corners of the
     * color cube are [-scale,-scale,-scale] and [scale,scale,scale].
     * @param scale the scale of the cube
     */
    public ColorCube(double scale) {
	QuadArray cube = new QuadArray(24, QuadArray.COORDINATES | 
		QuadArray.COLOR_3);

	float scaledVerts[] = new float[verts.length];
	for (int i = 0; i < verts.length; i++)
	    scaledVerts[i] = verts[i] * (float)scale;

	cube.setCoordinates(0, scaledVerts);
	cube.setColors(0, colors);

	this.setGeometry(cube);
    }
}
