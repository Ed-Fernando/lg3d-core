/**
 * Project Looking Glass
 *
 * $RCSfile: RingShadow.java,v $
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
 * $Date: 2006-02-24 01:58:37 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.shape;

import org.jdesktop.lg3d.sg.*;
import javax.vecmath.*;


public class RingShadow extends Shape3D {

    public RingShadow(float radius, float holeRadius, int n, float alpha) {

	setPickable(false);

	Appearance app = new ShadowAppearance();
	setAppearance(app);

	float[] coords = new float[(n + 1) * 2 * 3];
	int[] indices = new int[(n + 1) * 2];
	float[] colors4 = {
	    0.0f, 0.0f, 0.0f, 0.0f,
	    0.0f, 0.0f, 0.0f, alpha,
	};
	int[] colorIndices = new int[(n + 1) * 2];
        
	for (int i = 0; i <= n; i++) {
	    float x = (float)Math.cos(2 * Math.PI * i / n) * 0.5f;
	    float y = (float)Math.sin(2 * Math.PI * i / n) * 0.5f;
            
	    coords[i * 6 + 0] = x * holeRadius;
	    coords[i * 6 + 1] = y * holeRadius;
	    coords[i * 6 + 2] = 0.0f;
            coords[i * 6 + 3] = x * radius;
	    coords[i * 6 + 4] = y * radius;
	    coords[i * 6 + 5] = 0.0f;

	    indices[i * 2 + 0] = i * 2 + 0;
            indices[i * 2 + 1] = i * 2 + 1;
            
	    colorIndices[i * 2 + 0] = 1; // inner
            colorIndices[i * 2 + 1] = 0; // outer
	}
	
	IndexedTriangleStripArray geom
	    = new IndexedTriangleStripArray(
		(n + 1) * 2, 
		GeometryArray.COORDINATES
		| GeometryArray.COLOR_4,
		0, null,
		(n + 1) * 2,
		new int[] {(n + 1) * 2});

	geom.setCoordinates(0, coords);
	geom.setCoordinateIndices(0, indices);
	geom.setColors(0, colors4);
	geom.setColorIndices(0, colorIndices);

	setGeometry(geom);
    }
}
