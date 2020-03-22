/**
 * Project Looking Glass
 *
 * $RCSfile: PickableRegion.java,v $
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
 * $Date: 2006-05-02 07:27:33 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.shape;

import org.jdesktop.lg3d.sg.*;
import javax.vecmath.*;


public class PickableRegion extends Shape3D {
    private static Appearance app 
	//= new SimpleAppearance(1.0f, 0.0f, 0.0f, 0.5f);
	= new SimpleAppearance(0.0f, 0.0f, 0.0f, 0.0f);
    
    private float xShift;
    private float yShift;
    private float zShift;
    
    static {
	RenderingAttributes attr = new RenderingAttributes();
	//attr.setDepthBufferEnable(false);
	attr.setDepthBufferWriteEnable(false);
	app.setRenderingAttributes(attr);
    }

    private IndexedGeometryArray geometry;

    /**
     * Create a PickableRegion of the specificed width and height, the
     * center of the region will be at 0,0,0
     */
    public PickableRegion(float width, float height) {
        this(width, height, 0.0f, 0.0f, 0.0f);
    }
       
    /**
     * Create a PickableRegion of the specificed width and height, the
     * center of the region will be at xShift,yShift,zShift
     */
    public PickableRegion(float width, float height, 
        float xShift, float yShift, float zShift) 
    {
        this.xShift = xShift;
        this.yShift = yShift;
        this.zShift = zShift;
        
	setAppearance(app);

	geometry
	    = new IndexedQuadArray(4, 
		GeometryArray.COORDINATES,
		0, null,
		4);
	geometry.setCapability(GeometryArray.ALLOW_COORDINATE_WRITE);
	setGeometry(geometry);

	setSize(width, height);

	geometry.setCoordinateIndices(0, indices);

	// settings for allowing picking
        //PickTool.setCapabilities(this, PickTool.INTERSECT_COORD);
    }
    
    public void setSize(float width, float height, float xShift, float yShift, float zShift) {
        this.xShift = xShift;
        this.yShift = yShift;
        this.zShift = zShift;
        setSize(width, height);
    }
    
    public void setSize(float width, float height) {
	// REMINDER -- think about optimization later

	float w = width / 2.0f;
	float h = height / 2.0f;

	float[] coords = {
	    -w + xShift, -h + yShift, zShift,
	     w + xShift, -h + yShift, zShift,
	     w + xShift,  h + yShift, zShift,
	    -w + xShift,  h + yShift, zShift,
	};

	geometry.setCoordinates(0, coords);
    }
    
    private static int[] indices = {
        0, 1, 2, 3,
    };
}


