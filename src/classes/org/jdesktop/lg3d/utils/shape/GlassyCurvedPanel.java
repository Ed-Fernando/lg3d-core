/**
 * Project Looking Glass
 *
 * $RCSfile: GlassyCurvedPanel.java,v $
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
 * $Revision: 1.7 $
 * $Date: 2006-07-31 23:48:00 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.shape;

import org.jdesktop.lg3d.sg.*;
import javax.vecmath.*;


/**
 * A Shape3D object that displays a curved glassy panel that looks like
 * a "C" shape.
 */
public class GlassyCurvedPanel extends Shape3D {
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
     * Create a GlassyCurvedPanel object.  The shape's foremost surface 
     * will be placed on the Z=0 plane with X=0, Y=0 
     * at the center.
     *
     * @param  radius      Radius of the curved panel.
     * @param  height      Width of the panel.
     * @param  startAngle  The starting angle of this curved panel.
     * @param  endAngle    The ending angle of this curved panel.
     * @param  depth       Z depth of the curved panel.
     * @param  app         Appearance to use.
     */
    public GlassyCurvedPanel(float radius, float height, 
            float startAngle, float endAngle,
            float depth, Appearance app) 
    {
        this(radius, height, startAngle, endAngle, depth, defaultDivisions, app);
    }
    
    /**
     * Create a GlassyCurvedPanel object.  The shape's foremost surface 
     * will be placed on the Z=0 plane with X=0, Y=0 
     * at the center.
     *
     * @param  radius      Radius of the curved panel.
     * @param  height      Width of the panel.
     * @param  startAngle  The starting angle of this curved panel.
     * @param  endAngle    The ending angle of this curved panel.
     * @param  depth       Z depth of the curved panel.
     * @param  divisions   Number of divisions along the circumference.
     * @param  app         Appearance to use.
     */
    public GlassyCurvedPanel(float radius, float height, 
            float startAngle, float endAngle,
            float depth, int divisions, Appearance app) 
    {
        this(radius, height, startAngle, endAngle, depth, defaultEdgeWidth, divisions, app);
    }
    
    /**
     * Create a GlassyCurvedPanel object.  The shape's foremost surface 
     * will be placed on the Z=0 plane with X=0, Y=0 
     * at the center.
     *
     * @param  radius      Radius of the curved panel.
     * @param  height      Width of the panel.
     * @param  startAngle  The starting angle of this curved panel.
     * @param  endAngle    The ending angle of this curved panel.
     * @param  depth       Z depth of the curved panel.
     * @param  edge        Width of the fuzzy area around the edge.
     * @param  divisions   Number of divisions along the circumference.
     * @param  app         Appearance to use.
     */
    public GlassyCurvedPanel(float radius, float height, 
            float startAngle, float endAngle,
            float depth, float edge, int divisions, Appearance app) 
    {
        this(radius, height, height, startAngle, endAngle, depth, edge, divisions, app);
    }
    
    /**
     * Create a GlassyCurvedPanel object.  The shape's foremost surface 
     * will be placed on the Z=0 plane with X=0, Y=0 
     * at the center.
     *
     * @param  radius      Radius of the curved panel.
     * @param  startHeight Width of the panel at the starting angle.
     * @param  endHeight   Width of the panel at the ending angle.
     * @param  startAngle  The starting angle of this curved panel.
     * @param  endAngle    The ending angle of this curved panel.
     * @param  depth       Z depth of the curved panel.
     * @param  edge        Width of the fuzzy area around the edge.
     * @param  divisions   Number of divisions along the circumference.
     * @param  app         Appearance to use.
     */
    public GlassyCurvedPanel(float radius, float startHeight, float endHeight, 
            float startAngle, float endAngle,
            float depth, float edge, int divisions, Appearance app) 
    {
        this(radius, startHeight, endHeight, startAngle, endAngle, depth, edge, divisions, 
                0.0f, 0.0f, 0.0f, app);
    }
    
    /**
     * Create a GlassyCurvedPanel object.  The shape's foremost surface 
     * will be placed on the Z=zShift plane with X=xShift, Y=yShift 
     * at the center.
     *
     * @param  radius      Radius of the curved panel.
     * @param  startHeight Width of the panel at the starting angle.
     * @param  endHeight   Width of the panel at the ending angle.
     * @param  startAngle  The starting angle of this curved panel.
     * @param  endAngle    The ending angle of this curved panel.
     * @param  depth       Z depth of the curved panel.
     * @param  edge        Width of the fuzzy area around the edge.
     * @param  divisions   Number of divisions along the circumference.
     * @param  xShift      Coordinate bias toward the X direction.
     * @param  yShift      Coordinate bias toward the Y direction.
     * @param  zShift      Coordinate bias toward the X direction.
     * @param  app         Appearance to use.
     */
    public GlassyCurvedPanel(float radius, float startHeight, float endHeight, 
            float startAngle, float endAngle, 
            float depth, float edge, int divisions, 
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
	float d = depth;
	float e = edge;
        
        // GlassyDisc is constructed using IndexedTriangleStripArray
        // coords[ 0*(n+1) to  1*(n+1) -1] are the inner coords at z == -d
        // coords[ 1*(n+1) to  2*(n+1) -1] are the inner coords at z == -d +e
        // coords[ 2*(n+1) to  3*(n+1) -1] are the inner coords at z == -e
        // coords[ 3*(n+1) to  4*(n+1) -1] are the inner coords at z == 0
        // coords[ 4*(n+1) to  5*(n+1) -1] are the coords at z == -d
        // coords[ 5*(n+1) to  6*(n+1) -1] are the coords at z == -d +e
        // coords[ 6*(n+1) to  7*(n+1) -1] are the coords at z == -e
        // coords[ 7*(n+1) to  8*(n+1) -1] are the coords at z == 0
        // coords[ 8*(n+1) to  9*(n+1) -1] are the surface coords at z == 0
        // coords[ 9*(n+1) to 10*(n+1) -1] are the surface inner coords at z == 0
        // coords[10*(n+1) + 0 to 10*(n+1) + 3] are the start edge inner coords
        // coords[10*(n+1) + 4 to 10*(n+1) + 7] are the end edge inner coords
        float[] coords = new float[(10 * (n + 1) + 4*2) * 3];
        float[] normals = new float[(2 * (n + 1) + 1 + 2) * 3];
        
        for (int i = 0; i <= n; i++) {
            float p = (float)i / n;
            float hd = startHeight * (1.0f - p) + endHeight * p;
            float ri = radius - hd;
            float ro = radius;
            double a = startAngle * (1.0f - p) + endAngle * p;
	    float x = (float)Math.cos(a);
	    float y = (float)Math.sin(a);
            
            coords[(0*(n+1) + i)*3 + 0] = x * ri + xShift;
            coords[(0*(n+1) + i)*3 + 1] = y * ri + yShift;
            coords[(0*(n+1) + i)*3 + 2] = -d + zShift;
            
            coords[(1*(n+1) + i)*3 + 0] = x * ri + xShift;
            coords[(1*(n+1) + i)*3 + 1] = y * ri + yShift;
            coords[(1*(n+1) + i)*3 + 2] = -d +e + zShift;
            
            coords[(2*(n+1) + i)*3 + 0] = x * ri + xShift;
            coords[(2*(n+1) + i)*3 + 1] = y * ri + yShift;
            coords[(2*(n+1) + i)*3 + 2] = -e + zShift;
            
            coords[(3*(n+1) + i)*3 + 0] = x * ri + xShift;
            coords[(3*(n+1) + i)*3 + 1] = y * ri + yShift;
            coords[(3*(n+1) + i)*3 + 2] = zShift;
            
            coords[(4*(n+1) + i)*3 + 0] = x * (ri + e) + xShift;
            coords[(4*(n+1) + i)*3 + 1] = y * (ri + e) + yShift;
            coords[(4*(n+1) + i)*3 + 2] = zShift;
            
            coords[(5*(n+1) + i)*3 + 0] = x * ro + xShift;
            coords[(5*(n+1) + i)*3 + 1] = y * ro + yShift;
            coords[(5*(n+1) + i)*3 + 2] = -d + zShift;
            
            coords[(6*(n+1) + i)*3 + 0] = x * ro + xShift;
            coords[(6*(n+1) + i)*3 + 1] = y * ro + yShift;
            coords[(6*(n+1) + i)*3 + 2] = -d +e + zShift;
            
            coords[(7*(n+1) + i)*3 + 0] = x * ro + xShift;
            coords[(7*(n+1) + i)*3 + 1] = y * ro + yShift;
            coords[(7*(n+1) + i)*3 + 2] = -e + zShift;
            
            coords[(8*(n+1) + i)*3 + 0] = x * ro + xShift;
            coords[(8*(n+1) + i)*3 + 1] = y * ro + yShift;
            coords[(8*(n+1) + i)*3 + 2] = zShift;
            
            coords[(9*(n+1) + i)*3 + 0] = x * (ro - e) + xShift;
            coords[(9*(n+1) + i)*3 + 1] = y * (ro - e) + yShift;
            coords[(9*(n+1) + i)*3 + 2] = zShift;
             
            
            normals[(0*(n+1) + i)*3 + 0] = -x;
            normals[(0*(n+1) + i)*3 + 1] = -y;
            normals[(0*(n+1) + i)*3 + 2] = 0.0f;
            
            normals[(1*(n+1) + i)*3 + 0] = x;
            normals[(1*(n+1) + i)*3 + 1] = y;
            normals[(1*(n+1) + i)*3 + 2] = 0.0f;
            
            if (i == 0 || i == n ) {
                int ix = (i == 0)?(0):(1);
                coords[(10*(n+1) + ix*4)*3 + 0] = x * (ro - e) + xShift;
                coords[(10*(n+1) + ix*4)*3 + 1] = y * (ro - e) + yShift;
                coords[(10*(n+1) + ix*4)*3 + 2] = -e + zShift;
                coords[(10*(n+1) + ix*4)*3 + 3] = x * (ri + e) + xShift;
                coords[(10*(n+1) + ix*4)*3 + 4] = y * (ri + e) + yShift;
                coords[(10*(n+1) + ix*4)*3 + 5] = -e + zShift;
                coords[(10*(n+1) + ix*4)*3 + 6] = x * (ro - e) + xShift;
                coords[(10*(n+1) + ix*4)*3 + 7] = y * (ro - e) + yShift;
                coords[(10*(n+1) + ix*4)*3 + 8] = -d + e + zShift;
                coords[(10*(n+1) + ix*4)*3 + 9] = x * (ri + e) + xShift;
                coords[(10*(n+1) + ix*4)*3 +10] = y * (ri + e) + yShift;
                coords[(10*(n+1) + ix*4)*3 +11] = -d + e + zShift;
                
                if (ix == 0) {
                    normals[(2*(n+1)+1)*3 + 0] =  y;
                    normals[(2*(n+1)+1)*3 + 1] = -x;
                    normals[(2*(n+1)+1)*3 + 2] = 0.0f;
                } else {
                    normals[(2*(n+1)+2)*3 + 0] = -y;
                    normals[(2*(n+1)+2)*3 + 1] =  x;
                    normals[(2*(n+1)+2)*3 + 2] = 0.0f;
                }
            }
        }
        
        normals[(2*(n+1))*3 + 0] = 0.0f;
        normals[(2*(n+1))*3 + 1] = 0.0f;
        normals[(2*(n+1))*3 + 2] = 1.0f;
        
        float[] colors4 = {
	    0.0f, 0.0f, 0.0f, 0.00f,
	    1.0f, 1.0f, 1.0f, 0.75f,
	    1.0f, 1.0f, 1.0f, 0.40f,
	};
        
	int[] indices = new int[(n+1) * 2 * (4+1) * 2 + 12*2];
	int[] colorIndices = new int[indices.length];
	int[] normalIndices = new int[indices.length];
        
        for (int i = 0; i <= n; i++) {            
            int k;
            
            k = 0*(n+1) + i*2;
	    indices[k + 0] =  1*(n+1) + i;
            indices[k + 1] =  0*(n+1) + i;
            colorIndices[k + 0] = 1;
            colorIndices[k + 1] = 0;
            normalIndices[k + 0] = i;
            normalIndices[k + 1] = i;
            
            k = 2*(n+1) + i*2;
            indices[k + 0] =  2*(n+1) + i;
            indices[k + 1] =  1*(n+1) + i;
            colorIndices[k + 0] = 1;
            colorIndices[k + 1] = 1;
            normalIndices[k + 0] = i;
            normalIndices[k + 1] = i;
            
            k = 4*(n+1) + i*2;
            indices[k + 0] =  3*(n+1) + i;
            indices[k + 1] =  2*(n+1) + i;
            colorIndices[k + 0] = 0;
            colorIndices[k + 1] = 1;
            normalIndices[k + 0] = i;
            normalIndices[k + 1] = i;
            
            
            k = 6*(n+1) + i*2;
            indices[k + 0] =  6*(n+1) + i;
            indices[k + 1] =  5*(n+1) + i;
            colorIndices[k + 0] = 1;
            colorIndices[k + 1] = 0;
            normalIndices[k + 0] = n + i;
            normalIndices[k + 1] = n + i;
            
            k = 8*(n+1) + i*2;
            indices[k + 0] =  7*(n+1) + i;
            indices[k + 1] =  6*(n+1) + i;
            colorIndices[k + 0] = 1;
            colorIndices[k + 1] = 1;
            normalIndices[k + 0] = n + i;
            normalIndices[k + 1] = n + i;
            
            k = 10*(n+1) + i*2;
            indices[k + 0] =  8*(n+1) + i;
            indices[k + 1] =  7*(n+1) + i;
            colorIndices[k + 0] = 0;
            colorIndices[k + 1] = 1;
            normalIndices[k + 0] = n + i;
            normalIndices[k + 1] = n + i;
            
            
            k = 12*(n+1) + i*2;
            indices[k + 0] =  4*(n+1) + i;
            indices[k + 1] =  3*(n+1) + i;
            colorIndices[k + 0] = 2;
            colorIndices[k + 1] = 2;
            normalIndices[k + 0] = 2*(n+1);
            normalIndices[k + 1] = 2*(n+1);
            
            k = 14*(n+1) + i*2;
            indices[k + 0] =  9*(n+1) + i;
            indices[k + 1] =  8*(n+1) + i;
            colorIndices[k + 0] = 2;
            colorIndices[k + 1] = 2;
            normalIndices[k + 0] = 2*(n+1);
            normalIndices[k + 1] = 2*(n+1);
            
            k = 16*(n+1) + i*2;
            indices[k + 0] =  4*(n+1) + i;
            indices[k + 1] =  9*(n+1) + i;
            colorIndices[k + 0] = 2;
            colorIndices[k + 1] = 2;
            normalIndices[k + 0] = 2*(n+1);
            normalIndices[k + 1] = 2*(n+1);
	}
        
        for (int ix = 0; ix < 2; ix++) {
            int k = 18*(n+1) + 12*ix;
            indices[k + 0] = (10*(n+1) + ix*4) + 0;
            indices[k + 1] = (10*(n+1) + ix*4) + 1;
            indices[k + 2] = (10*(n+1) + ix*4) + 2;
            indices[k + 3] = (10*(n+1) + ix*4) + 3;
            
            indices[k + 4] = ( 0*(n+1) + ix*n);
            indices[k + 5] = (10*(n+1) + ix*4) + 1;
            indices[k + 6] = ( 3*(n+1) + ix*n);
            indices[k + 7] = (10*(n+1) + ix*4) + 0;
            indices[k + 8] = ( 8*(n+1) + ix*n);
            indices[k + 9] = (10*(n+1) + ix*4) + 2;
            indices[k +10] = ( 5*(n+1) + ix*n);
            indices[k +11] = ( 0*(n+1) + ix*n);
            
            colorIndices[k + 0] = 1;
            colorIndices[k + 1] = 1;
            colorIndices[k + 2] = 1;
            colorIndices[k + 3] = 1;
            
            colorIndices[k + 4] = 0;
            colorIndices[k + 5] = 1;
            colorIndices[k + 6] = 0;
            colorIndices[k + 7] = 1;
            colorIndices[k + 8] = 0;
            colorIndices[k + 9] = 1;
            colorIndices[k +10] = 0;
            colorIndices[k +11] = 0;
            
            for (int j = 0; j < 12; j++) {
                normalIndices[k + j] = 2*(n+1)+1 + ix;
            }
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
                    12, 12,
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

