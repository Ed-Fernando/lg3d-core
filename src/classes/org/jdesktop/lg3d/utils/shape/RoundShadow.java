/**
 * Project Looking Glass
 *
 * $RCSfile: RoundShadow.java,v $
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
 * $Date: 2004-06-23 18:52:15 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.shape;

import org.jdesktop.lg3d.sg.*;
import javax.vecmath.*;


public class RoundShadow extends Shape3D {

    public RoundShadow(float xSize, float ySize, int n, 
	float xShift, float yShift, float zShift, float alpha) {

	setPickable(false);

	Appearance app = new ShadowAppearance();
	setAppearance(app);

	float[] coords = new float[(1 + n) * 3];
	int[] indices = new int[1 + n + 1];
	float[] colors4 = {
	    0.0f, 0.0f, 0.0f, 0.0f,
	    0.0f, 0.0f, 0.0f, alpha,
	};
	int[] colorIndices = new int[1 + n + 1];

	// center
	coords[0] = 0.0f;
	coords[1] = 0.0f;
	coords[2] = 0.0f;
	indices[0] = 0;
	colorIndices[0] = 1;

	for (int i = 1; i <= n; i++) {
	    float x = (float)Math.cos(2 * Math.PI * i / n) * 0.5f;
	    float y = (float)Math.sin(2 * Math.PI * i / n) * 0.5f;

	    coords[i * 3 + 0] = x * xSize + xShift;
	    coords[i * 3 + 1] = y * ySize + yShift;
	    coords[i * 3 + 2] = zShift;

	    indices[i] = i;
	    colorIndices[i] = 0;
	}
	indices[1 + n] = 1;
	colorIndices[1 + n] = 0;

	IndexedTriangleFanArray geom
	    = new IndexedTriangleFanArray(
		1 + n, 
		GeometryArray.COORDINATES
		| GeometryArray.COLOR_4,
		0, null,
		1 + n + 1,
		new int[] {1 + n + 1});

	geom.setCoordinates(0, coords);
	geom.setCoordinateIndices(0, indices);
	geom.setColors(0, colors4);
	geom.setColorIndices(0, colorIndices);

	setGeometry(geom);
    }
}
