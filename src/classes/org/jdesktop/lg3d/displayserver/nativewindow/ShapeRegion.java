/**
 * Project Looking Glass
 *
 * $RCSfile: ShapeRegion.java,v $
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
 * $Revision: 1.4 $
 * $Date: 2006-03-15 22:49:45 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.nativewindow;

import gnu.x11.Rectangle;
import org.jdesktop.lg3d.sg.Appearance;
import org.jdesktop.lg3d.sg.GeometryArray;
import org.jdesktop.lg3d.sg.IndexedGeometryArray;
import org.jdesktop.lg3d.sg.IndexedQuadArray;
import org.jdesktop.lg3d.sg.RenderingAttributes;
import org.jdesktop.lg3d.sg.Shape3D;
import org.jdesktop.lg3d.utils.shape.SimpleAppearance;


public class ShapeRegion extends Shape3D {
    private static Appearance app = new SimpleAppearance(1.0f, 0.0f, 0.0f, 0.5f);
    //= new SimpleAppearance(0.0f, 0.0f, 0.0f, 0.0f);
    Rectangle rect[];
    
    private IndexedGeometryArray geometry;
    
    public ShapeRegion(float width, float height, Rectangle rec[]) {
        
        RenderingAttributes attr = new RenderingAttributes();
        //attr.setDepthBufferEnable(false);
        attr.setDepthBufferWriteEnable(false);
        app.setRenderingAttributes(attr);
        setAppearance(app);
        rect = rec;
        geometry
        = new IndexedQuadArray(4*rect.length,
        GeometryArray.COORDINATES,
        0, null,
        4*rect.length);
        geometry.setCapability(GeometryArray.ALLOW_COORDINATE_WRITE);
        setGeometry(geometry);
        
        setSize(width, height);
/*
        int[] indices = {
            0, 1, 2, 3,
        };
 
        geometry.setCoordinateIndices(0, indices);
 */
        // settings for allowing picking
        //PickTool.setCapabilities(this, PickTool.INTERSECT_COORD);
    }
    
    public void setSize(float width, float height) {
        // REMINDER -- think about optimization later
    /*
        float w = width / 2.0f;
        float h = height / 2.0f;
     
        float[] coords = {
            -w, -h, 0.0f,
             w, -h, 0.0f,
             w,  h, 0.0f,
            -w,  h, 0.0f,
        };
     */
        creatGeometry();
        //geometry.setCoordinates(0, coords);
    }
    
    public void setRect(Rectangle rec[]) {
        rect = rec;
    }
    
    public void creatGeometry() {
        int indics[] = new int[4*rect.length];
        for(int i=0; i < rect.length; i++){
            int off = i*4;
            float w = rect[i].width / 3000.0f;
            float h = rect[i].height / 3000.0f;
            float x = rect[i].x / 3000.0f;
            float y = rect[i].y / 3000.0f;
            float[] a1={x,y,0};
            float[] a2={x+w ,y,0};
            float[] a3={x+w ,y+h,0};
            float[] a4={x,y+h,0};
            geometry.setCoordinate(off,a1);
            indics[off] = off;
            geometry.setCoordinate(off+1,a2);
            indics[off+1] = off+1;
            geometry.setCoordinate(off+2,a3);
            indics[off+2] = off+2;
            geometry.setCoordinate(off+3,a4);
            indics[off+3] = off+3;
        }
        geometry.setCoordinateIndices(0, indics);
    }
}


