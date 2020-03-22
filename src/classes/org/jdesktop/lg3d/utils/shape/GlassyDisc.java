/**
 * Project Looking Glass
 *
 * $RCSfile: GlassyDisc.java,v $
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
 * $Date: 2006-04-25 04:53:54 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.shape;

import org.jdesktop.lg3d.sg.*;
import javax.vecmath.*;

/**
 * A Shape3D object that displays a glassy disc shape.
 */
public class GlassyDisc extends Shape3D {
    private static float defaultEdgeWidth = 0.0005f;
    private static int defaultDivisions = 36;
    
    private IndexedTriangleStripArray geometry;
    private float depth;
    private float edge;
    private int divisions;
    private float xShift;
    private float yShift;
    private float zShift;
    
    /**
     * Create a GlassyDisc object.  The disc's surface will be placed
     * on the Z=0 plane with X=0, Y=0 at the center.
     *
     * @param  radius Radius of the disc.
     * @param  depth  Z depth of the disc.
     * @param  app    Appearance to use.
     */
    public GlassyDisc(float radius, float depth, Appearance app) {
        this(radius, depth, defaultDivisions, app);
    }
    
    /**
     * Create a GlassyDisc object.  The disc's surface will be placed
     * on the Z=0 plane with X=0, Y=0 at the center.
     *
     * @param  radius     Radius of the disc.
     * @param  depth      Z depth of the disc.
     * @param  divisions  Number of divisions along the circumference.
     * @param  app        Appearance to use.
     */
    public GlassyDisc(float radius, float depth, int divisions, Appearance app) {
        this(radius, depth, 
                (depth < defaultEdgeWidth * 4)?(depth * 0.25f):(defaultEdgeWidth), 
                divisions,
                app);
    }
    
    /**
     * Create a GlassyDisc object.  The disc's surface will be placed
     * on the Z=0 plane with X=0, Y=0 at the center.
     *
     * @param  radius     Radius of the disc.
     * @param  depth      Z depth of the disc.
     * @param  edge       Width of the fuzzy area around the edge.
     * @param  divisions  Number of divisions along the circumference.
     * @param  app        Appearance to use.
     */
    public GlassyDisc(float radius, float depth, float edge, int divisions, 
	Appearance app)
    {
        this(radius, depth, edge, divisions, 0.0f, 0.0f, 0.0f, app);
    }
    
    /**
     * Create a GlassyDisc object.  The disc's surface will be placed
     * on the Z=zShift plane with X=xShift, Y=yShift at the center.
     *
     * @param  radius     Radius of the disc.
     * @param  depth      Z depth of the disc.
     * @param  edge       Width of the fuzzy area around the edge.
     * @param  divisions  Number of divisions along the circumference.
     * @param  xShift     Coordinate bias toward the X direction.
     * @param  yShift     Coordinate bias toward the Y direction.
     * @param  zShift     Coordinate bias toward the X direction.
     * @param  app        Appearance to use.
     */
    public GlassyDisc(float radius, float depth, float edge, int divisions, 
        float xShift, float yShift, float zShift, Appearance app)
    {
        // FIXME -- the implementation is not efficient, but works for now
	this.depth = depth;
	this.edge = edge;
        this.divisions = divisions;
        this.xShift = xShift;
        this.yShift = yShift;
        this.zShift = zShift;
        
	setAppearance(app);
        
        int n = divisions;
	float r = radius;
	float d = depth;
	float e = edge;
        
        // GlassyDisc is constructed using IndexedTriangleStripArray
        // coords[ 0*n to 1*n -1] are the coords at z == -d
        // coords[ 1*n to 2*n -1] are the coords at z == -d +e
        // coords[ 2*n to 3*n -1] are the coords at z == -e
        // coords[ 3*n to 4*n -1] are the coords at z == 0
        // coords[ 4*n to 5*n -1] are the inner coords at z == 0
        // coords[ 5*n ] is the center
        float[] coords = new float[(5 * n + 1) * 3];
        float[] normals = new float[(n + 1) * 3]; // normals[n*3] -> front
        
        for (int i = 0; i < n; i++) {
            double a = 2 * Math.PI * i / n;
	    float x = (float)Math.cos(a);
	    float y = (float)Math.sin(a);
            
            coords[(0*n + i)*3 + 0] = x * r + xShift;
            coords[(0*n + i)*3 + 1] = y * r + yShift;
            coords[(0*n + i)*3 + 2] = -d + zShift;
            
            coords[(1*n + i)*3 + 0] = x * r + xShift;
            coords[(1*n + i)*3 + 1] = y * r + yShift;
            coords[(1*n + i)*3 + 2] = -d +e + zShift;
            
            coords[(2*n + i)*3 + 0] = x * r + xShift;
            coords[(2*n + i)*3 + 1] = y * r + yShift;
            coords[(2*n + i)*3 + 2] = -e + zShift;
            
            coords[(3*n + i)*3 + 0] = x * r + xShift;
            coords[(3*n + i)*3 + 1] = y * r + yShift;
            coords[(3*n + i)*3 + 2] = zShift;
            
            coords[(4*n + i)*3 + 0] = x * (r - e) + xShift;
            coords[(4*n + i)*3 + 1] = y * (r - e) + yShift;
            coords[(4*n + i)*3 + 2] = zShift;
            
            normals[i*3 + 0] = x;
            normals[i*3 + 1] = y;
            normals[i*3 + 2] = 0.0f;
        }
        coords[(5*n)*3 + 0] = xShift;
        coords[(5*n)*3 + 1] = yShift;
        coords[(5*n)*3 + 2] = zShift;
        
        normals[n*3 + 0] = 0.0f;
        normals[n*3 + 1] = 0.0f;
        normals[n*3 + 2] = 1.0f;
        
        float[] colors4 = {
	    0.0f, 0.0f, 0.0f, 0.00f,
	    1.0f, 1.0f, 1.0f, 0.75f,
	    1.0f, 1.0f, 1.0f, 0.35f,
	};
        
	int[] indices = new int[(n + 1) * 2 * 5];
	int[] colorIndices = new int[indices.length];
	int[] normalIndices = new int[indices.length];
        
        for (int i = 0; i < n + 1; i++) {
            int j = (i < n)?(i):(0);
            int k;
            
            k = 0*(n+1) + i*2;
	    indices[k + 0] =  1*n + j;
            indices[k + 1] =  0*n + j;
            colorIndices[k + 0] = 1;
            colorIndices[k + 1] = 0;
            normalIndices[k + 0] = j;
            normalIndices[k + 1] = j;
            
            k = 2*(n+1) + i*2;
            indices[k + 0] =  2*n + j;
            indices[k + 1] =  1*n + j;
            colorIndices[k + 0] = 1;
            colorIndices[k + 1] = 1;
            normalIndices[k + 0] = j;
            normalIndices[k + 1] = j;
            
            k = 4*(n+1) + i*2;
            indices[k + 0] =  3*n + j;
            indices[k + 1] =  2*n + j;
            colorIndices[k + 0] = 0;
            colorIndices[k + 1] = 1;
            normalIndices[k + 0] = j;
            normalIndices[k + 1] = j;
            
            k = 6*(n+1) + i*2;
            indices[k + 0] =  4*n + j;
            indices[k + 1] =  3*n + j;
            colorIndices[k + 0] = 2;
            colorIndices[k + 1] = 0;
            normalIndices[k + 0] = n;
            normalIndices[k + 1] = n;
            
            k = 8*(n+1) + i*2;
            indices[k + 0] =  5*n;
            indices[k + 1] =  4*n + j;
            colorIndices[k + 0] = 2;
            colorIndices[k + 1] = 2;
            normalIndices[k + 0] = n;
            normalIndices[k + 1] = n;
	}
        
        geometry
	    = new IndexedTriangleStripArray(
		coords.length / 3, 
		GeometryArray.COORDINATES
		| GeometryArray.NORMALS
		| GeometryArray.COLOR_4,
		0, null,
		indices.length,
		new int[] {
                    (n + 1) * 2, (n + 1) * 2, (n + 1) * 2, (n + 1) * 2, (n + 1) * 2,
                });
        
	geometry.setCapability(GeometryArray.ALLOW_COORDINATE_WRITE);
        geometry.setCoordinates(0, coords);
	geometry.setCoordinateIndices(0, indices);
	geometry.setNormals(0, normals);
	geometry.setNormalIndices(0, normalIndices);
	geometry.setColors(0, colors4);
	geometry.setColorIndices(0, colorIndices);

        setGeometry(geometry);
    }
}

