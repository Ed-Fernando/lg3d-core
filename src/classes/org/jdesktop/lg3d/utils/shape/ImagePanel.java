/**
 * Project Looking Glass
 *
 * $RCSfile: ImagePanel.java,v $
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
 * $Revision: 1.10 $
 * $Date: 2006-06-16 20:41:14 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.shape;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import javax.media.j3d.IndexedGeometryArray;
import org.jdesktop.lg3d.sg.Appearance;

import org.jdesktop.lg3d.sg.GeometryArray;
import org.jdesktop.lg3d.sg.IndexedQuadArray;
import org.jdesktop.lg3d.sg.Shape3D;

/**
 * A Panel for displaying images (textures).
 */
public class ImagePanel extends Shape3D {

    protected IndexedQuadArray geom;
    
    public ImagePanel(float width, float height) {
	this(width, height, true);
    }

    public ImagePanel(URL imageUrl, float width, float height) 
        throws FileNotFoundException, IOException
    {
	this(imageUrl, width, height, true);
    }
    
    public ImagePanel(URL imageUrl, float width, float height,
        boolean shiny) throws FileNotFoundException, IOException
    {
        this(imageUrl, width, height, shiny, 1.0f);
    }
     
    public ImagePanel(URL imageUrl, float width, float height,
        boolean shiny, float alpha) throws FileNotFoundException, IOException
    {
	this(width, height, shiny);

	SimpleAppearance app 
	    = new SimpleAppearance(
		1.0f, 1.0f, 1.0f, alpha,
		SimpleAppearance.ENABLE_TEXTURE
		| ((shiny)?(0):(SimpleAppearance.NO_GLOSS))
		| SimpleAppearance.DISABLE_CULLING
		);
	app.setTexture(imageUrl);
	setAppearance(app);
    }
    
    protected ImagePanel(float width, float height, boolean shiny) {
	float w = width / 2.0f;
	float h = height / 2.0f;

	float[] coords = {
	    -w, -h, 0.0f,
	     w, -h, 0.0f,
	     w,  h, 0.0f,
	    -w,  h, 0.0f,
	};

	geom = new IndexedQuadArray(4, 
		GeometryArray.COORDINATES
		| GeometryArray.TEXTURE_COORDINATE_2
		| ((shiny)?(GeometryArray.NORMALS):(0)),
		1, new int[] {0},
		4);
        
        geom.setCapability(GeometryArray.ALLOW_COORDINATE_WRITE);
	geom.setCoordinates(0, coords);
	geom.setCoordinateIndices(0, indices);
	geom.setTextureCoordinates(0, 0, texCoords);
	geom.setTextureCoordinateIndices(0, 0, indices);
	if (shiny) {
	    geom.setNormals(0, normals);
	    geom.setNormalIndices(0, normalIndices);
	}
        
        geom.setCapability(IndexedGeometryArray.ALLOW_TEXCOORD_INDEX_WRITE);

	setGeometry(geom);
    }
    
    public void setSize(float width, float height) {
	// REMINDER -- think about optimization later
        
	float w = width / 2.0f;
	float h = height / 2.0f;

	float[] coords = {
	    -w, -h, 0.0f,
	     w, -h, 0.0f,
	     w,  h, 0.0f,
	    -w,  h, 0.0f,
	};

	geom.setCoordinates(0, coords);
    }
    
    public void setAppearance(Appearance app) {
        if (app!=null && app.getTexture()!=null && !app.getTexture().getImage(0).isYUp()) {
            logger.warning("Using Y down image");
            geom.setTextureCoordinateIndices(0,0,new int[] {3,2,1,0});
            Thread.dumpStack();
        }
        super.setAppearance(app);
    }

    private static int[] indices = {
        0, 1, 2, 3,
    };

    private static float[] texCoords = {
        0.0f, 1.0f,
        1.0f, 1.0f,
        1.0f, 0.0f,
        0.0f, 0.0f,
    };

    private static float[] normals = {
        0.0f, 0.0f, 1.0f,
    };

    private static int[] normalIndices = {
        0, 0, 0, 0,
    };
}


