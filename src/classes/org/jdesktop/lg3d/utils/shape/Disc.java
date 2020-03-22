/**
 * Project Looking Glass
 *
 * $RCSfile: Disc.java,v $
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
 * $Date: 2006-05-31 20:58:03 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.shape;

import org.jdesktop.lg3d.sg.*;
import javax.vecmath.*;


public class Disc extends Shape3D {

    public Disc(float radius, int n, Appearance app) {
        this(radius, n, 0.0f, 0.0f, 0.0f, 0.0f, app);
    }
    
    public Disc(float radius, int n, float xShift, float yShift, float zShift,
        float start, Appearance app) 
    {

        setCapability(Shape3D.ALLOW_APPEARANCE_READ);
        if (app != null) {
            app.setCapability(Appearance.ALLOW_TEXTURE_READ);
            setAppearance(app);
        }
        
	float[] coords = new float[(1 + n) * 3];
	int[] indices = new int[1 + n + 1];
        float[] texCoords = new float[(1 + n) * 2];
        float[] normals = {
	    0.0f, 0.0f, 1.0f,
	};
	int[] normalIndices = new int[1 + n + 1];
        
	// center
	coords[0] = xShift;
	coords[1] = yShift;
	coords[2] = zShift;
        texCoords[0] = 0.5f;
        texCoords[1] = 0.5f;
	indices[0] = 0;
        normalIndices[0] = 0;
	
	for (int i = 1; i <= n; i++) {
	    float x = (float)Math.cos(start + 2 * Math.PI * i / n) * 0.5f;
	    float y = (float)Math.sin(start + 2 * Math.PI * i / n) * 0.5f;

	    coords[i * 3 + 0] = xShift + x * radius;
	    coords[i * 3 + 1] = yShift + y * radius;
	    coords[i * 3 + 2] = zShift;

            texCoords[i * 2 + 0] = 0.5f + x;
	    texCoords[i * 2 + 1] = 0.5f - y;
	    indices[i] = i;
            normalIndices[i] = 0;
	}
        indices[1 + n] = 1;
	normalIndices[1 + n] = 0;

	IndexedTriangleFanArray geom
	    = new IndexedTriangleFanArray(
		1 + n, 
		GeometryArray.COORDINATES
		| GeometryArray.TEXTURE_COORDINATE_2
                | GeometryArray.NORMALS,
		1, new int[] {0},
		1 + n + 1,
		new int[] {1 + n + 1});

	geom.setCoordinates(0, coords);
        geom.setCoordinateIndices(0, indices);
        geom.setTextureCoordinates(0, 0, texCoords);
	geom.setTextureCoordinateIndices(0, 0, indices);
	geom.setNormals(0, normals);
	geom.setNormalIndices(0, normalIndices);
        
	setGeometry(geom);
    }
}
