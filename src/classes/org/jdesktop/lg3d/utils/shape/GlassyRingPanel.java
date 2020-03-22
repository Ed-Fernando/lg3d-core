/**
 * Project Looking Glass
 *
 * $RCSfile: GlassyRingPanel.java,v $
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
 * $Revision: 1.5 $
 * $Date: 2006-07-31 23:50:14 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.shape;

import org.jdesktop.lg3d.sg.*;
import javax.vecmath.*;


/**
 * A Shape3D object that displays a "O" shaped glassy panel.
 */
public class GlassyRingPanel extends Shape3D {
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
     * Create a GlassyRingPanel object.  The shape's foremost surface 
     * will be placed on the Z=0 plane with X=0, Y=0 
     * at the center.
     *
     * @param  radius      Radius of the ring.
     * @param  height      Width of the ring.
     * @param  depth       Z depth of the ring.
     * @param  app         Appearance to use.
     */
    public GlassyRingPanel(float radius, float height, 
            float depth, Appearance app) 
    {
        this(radius, height, depth, defaultDivisions, app);
    }
    
    /**
     * Create a GlassyRingPanel object.  The shape's foremost surface 
     * will be placed on the Z=0 plane with X=0, Y=0 
     * at the center.
     *
     * @param  radius      Radius of the ring.
     * @param  height      Width of the ring.
     * @param  depth       Z depth of the ring.
     * @param  cxShift     Coordinate bias of the center hole toward the X direction.
     * @param  cyShift     Coordinate bias of the center hole toward the Y direction.
     * @param  app         Appearance to use.
     */
    public GlassyRingPanel(float radius, float height, 
            float depth, float cxShift, float cyShift, Appearance app) 
    {
        this(radius, height, depth, defaultDivisions, cxShift, cyShift, app);
    }
    
    /**
     * Create a GlassyRingPanel object.  The shape's foremost surface 
     * will be placed on the Z=0 plane with X=0, Y=0 
     * at the center.
     *
     * @param  radius      Radius of the ring.
     * @param  height      Width of the ring.
     * @param  depth       Z depth of the ring.
     * @param  divisions   Number of divisions along the circumference.
     * @param  app         Appearance to use.
     */
    public GlassyRingPanel(float radius, float height, 
            float depth, int divisions, Appearance app) 
    {
        this(radius, height, depth, defaultEdgeWidth, divisions, app);
    }
    
    /**
     * Create a GlassyRingPanel object.  The shape's foremost surface 
     * will be placed on the Z=0 plane with X=0, Y=0 
     * at the center.
     *
     * @param  radius      Radius of the ring.
     * @param  height      Width of the ring.
     * @param  depth       Z depth of the ring.
     * @param  divisions   Number of divisions along the circumference.
     * @param  cxShift     Coordinate bias of the center hole toward the X direction.
     * @param  cyShift     Coordinate bias of the center hole toward the Y direction.
     * @param  app         Appearance to use.
     */
    public GlassyRingPanel(float radius, float height, 
            float depth, int divisions, float cxShift, float cyShift, Appearance app) 
    {
        this(radius, height, depth, defaultEdgeWidth, divisions, 
                0.0f, 0.0f, 0.0f, cxShift, cyShift, app);
    }
    
    /**
     * Create a GlassyRingPanel object.  The shape's foremost surface 
     * will be placed on the Z=0 plane with X=0, Y=0 
     * at the center.
     *
     * @param  radius      Radius of the ring.
     * @param  height      Width of the ring.
     * @param  depth       Z depth of the ring.
     * @param  edge        Width of the fuzzy area around the edge.
     * @param  divisions   Number of divisions along the circumference.
     * @param  app         Appearance to use.
     */
    public GlassyRingPanel(float radius, float height, 
            float depth, float edge, int divisions, Appearance app) 
    {
        this(radius, height, depth, edge, divisions, 
                0.0f, 0.0f, 0.0f, 0.0f, 0.0f, app);
    }
    
    /**
     * Create a GlassyRingPanel object.  The shape's foremost surface 
     * will be placed on the Z=zShift plane with X=xShift, Y=yShift 
     * at the center.
     *
     * @param  radius      Radius of the ring.
     * @param  height      Width of the ring.
     * @param  depth       Z depth of the ring.
     * @param  edge        Width of the fuzzy area around the edge.
     * @param  divisions   Number of divisions along the circumference.
     * @param  xShift      Coordinate bias toward the X direction.
     * @param  yShift      Coordinate bias toward the Y direction.
     * @param  zShift      Coordinate bias toward the X direction.
     * @param  cxShift     Coordinate bias of the center hole toward the X direction.
     * @param  cyShift     Coordinate bias of the center hole toward the Y direction.
     * @param  app         Appearance to use.
     */
    public GlassyRingPanel(float radius, float height,
            float depth, float edge, int divisions, 
            float xShift, float yShift, float zShift, 
            float cxShift, float cyShift, Appearance app)
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
	float d = depth;
	float e = edge;
        
        // GlassyDisc is constructed using IndexedTriangleStripArray
        // coords[ 0*n to  1*n -1] are the inner coords at z == -d
        // coords[ 1*n to  2*n -1] are the inner coords at z == -d +e
        // coords[ 2*n to  3*n -1] are the inner coords at z == -e
        // coords[ 3*n to  4*n -1] are the inner coords at z == 0
        // coords[ 4*n to  5*n -1] are the coords at z == -d
        // coords[ 5*n to  6*n -1] are the coords at z == -d +e
        // coords[ 6*n to  7*n -1] are the coords at z == -e
        // coords[ 7*n to  8*n -1] are the coords at z == 0
        // coords[ 8*n to  9*n -1] are the surface coords at z == 0
        // coords[ 9*n to 10*n -1] are the surface inner coords at z == 0
        // coords[10*n + 0 to 10*n + 3] are the start edge inner coords
        // coords[10*n + 4 to 10*n + 7] are the end edge inner coords
        float[] coords = new float[(10 * n) * 3];
        float[] normals = new float[(2 * n + 1) * 3];
        
        for (int i = 0; i < n; i++) {
            float ri = radius - height;
            float ro = radius;
            double a = 2 * Math.PI * i / n;
	    float x = (float)Math.cos(a);
	    float y = (float)Math.sin(a);
            
            coords[(0*n + i)*3 + 0] = x * ri + xShift + cxShift;
            coords[(0*n + i)*3 + 1] = y * ri + yShift + cyShift;
            coords[(0*n + i)*3 + 2] = -d + zShift;
            
            coords[(1*n + i)*3 + 0] = x * ri + xShift + cxShift;
            coords[(1*n + i)*3 + 1] = y * ri + yShift + cyShift;
            coords[(1*n + i)*3 + 2] = -d +e + zShift;
            
            coords[(2*n + i)*3 + 0] = x * ri + xShift + cxShift;
            coords[(2*n + i)*3 + 1] = y * ri + yShift + cyShift;
            coords[(2*n + i)*3 + 2] = -e + zShift;
            
            coords[(3*n + i)*3 + 0] = x * ri + xShift + cxShift;
            coords[(3*n + i)*3 + 1] = y * ri + yShift + cyShift;
            coords[(3*n + i)*3 + 2] = zShift;
            
            coords[(4*n + i)*3 + 0] = x * (ri + e) + xShift + cxShift;
            coords[(4*n + i)*3 + 1] = y * (ri + e) + yShift + cyShift;
            coords[(4*n + i)*3 + 2] = zShift;
            
            coords[(5*n + i)*3 + 0] = x * ro + xShift;
            coords[(5*n + i)*3 + 1] = y * ro + yShift;
            coords[(5*n + i)*3 + 2] = -d + zShift;
            
            coords[(6*n + i)*3 + 0] = x * ro + xShift;
            coords[(6*n + i)*3 + 1] = y * ro + yShift;
            coords[(6*n + i)*3 + 2] = -d +e + zShift;
            
            coords[(7*n + i)*3 + 0] = x * ro + xShift;
            coords[(7*n + i)*3 + 1] = y * ro + yShift;
            coords[(7*n + i)*3 + 2] = -e + zShift;
            
            coords[(8*n + i)*3 + 0] = x * ro + xShift;
            coords[(8*n + i)*3 + 1] = y * ro + yShift;
            coords[(8*n + i)*3 + 2] = zShift;
            
            coords[(9*n + i)*3 + 0] = x * (ro - e) + xShift;
            coords[(9*n + i)*3 + 1] = y * (ro - e) + yShift;
            coords[(9*n + i)*3 + 2] = zShift;
             
            
            normals[(0*n + i)*3 + 0] = -x;
            normals[(0*n + i)*3 + 1] = -y;
            normals[(0*n + i)*3 + 2] = 0.0f;
            
            normals[(1*n + i)*3 + 0] = x;
            normals[(1*n + i)*3 + 1] = y;
            normals[(1*n + i)*3 + 2] = 0.0f;
        }
        
        normals[(2*n)*3 + 0] = 0.0f;
        normals[(2*n)*3 + 1] = 0.0f;
        normals[(2*n)*3 + 2] = 1.0f;
        
        float[] colors4 = {
	    0.0f, 0.0f, 0.0f, 0.00f,
	    1.0f, 1.0f, 1.0f, 0.75f,
	    1.0f, 1.0f, 1.0f, 0.40f,
	};
        
	int[] indices = new int[(n+1) * 2 * (4+1) * 2];
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
            indices[k + 0] =  6*n + j;
            indices[k + 1] =  5*n + j;
            colorIndices[k + 0] = 1;
            colorIndices[k + 1] = 0;
            normalIndices[k + 0] = n + j;
            normalIndices[k + 1] = n + j;
            
            k = 8*(n+1) + i*2;
            indices[k + 0] =  7*n + j;
            indices[k + 1] =  6*n + j;
            colorIndices[k + 0] = 1;
            colorIndices[k + 1] = 1;
            normalIndices[k + 0] = n + j;
            normalIndices[k + 1] = n + j;
            
            k = 10*(n+1) + i*2;
            indices[k + 0] =  8*n + j;
            indices[k + 1] =  7*n + j;
            colorIndices[k + 0] = 0;
            colorIndices[k + 1] = 1;
            normalIndices[k + 0] = n + j;
            normalIndices[k + 1] = n + j;
            
            
            k = 12*(n+1) + i*2;
            indices[k + 0] =  4*n + j;
            indices[k + 1] =  3*n + j;
            colorIndices[k + 0] = 2;
            colorIndices[k + 1] = 2;
            normalIndices[k + 0] = 2*n;
            normalIndices[k + 1] = 2*n;
            
            k = 14*(n+1) + i*2;
            indices[k + 0] =  9*n + j;
            indices[k + 1] =  8*n + j;
            colorIndices[k + 0] = 2;
            colorIndices[k + 1] = 2;
            normalIndices[k + 0] = 2*n;
            normalIndices[k + 1] = 2*n;
            
            k = 16*(n+1) + i*2;
            indices[k + 0] =  4*n + j;
            indices[k + 1] =  9*n + j;
            colorIndices[k + 0] = 2;
            colorIndices[k + 1] = 2;
            normalIndices[k + 0] = 2*n;
            normalIndices[k + 1] = 2*n;
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
                    (n + 1) * 2, (n + 1) * 2, (n + 1) * 2, 
                    (n + 1) * 2, (n + 1) * 2, (n + 1) * 2,
                    (n + 1) * 2, (n + 1) * 2, (n + 1) * 2,
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

