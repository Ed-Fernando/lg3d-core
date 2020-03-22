/**
 * Project Looking Glass
 *
 * $RCSfile: WindowShape.java,v $
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
 * $Date: 2006-08-14 23:13:20 $
 * $State: Exp $
 */
/** Copyright (c) 2004 Amir Bukhari
 *
 * Permission to use, copy, modify, distribute, and sell this software and its
 * documentation for any purpose is hereby granted without fee, provided that
 * the above copyright notice appear in all copies and that both that
 * copyright notice and this permission notice appear in supporting
 * documentation, and that the name of Amir Bukhari not be used in
 * advertising or publicity pertaining to distribution of the software without
 * specific, written prior permission.  Amir Bukhari makes no
 * representations about the suitability of this software for any purpose.  It
 * is provided "as is" without express or implied warranty.
 *
 * AMIR BUKHARI DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE,
 * INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS, IN NO
 * EVENT SHALL AMIR BUKHARI BE LIABLE FOR ANY SPECIAL, INDIRECT OR
 * CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE,
 * DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER
 * TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 * PERFORMANCE OF THIS SOFTWARE.
 */
package org.jdesktop.lg3d.displayserver.nativewindow;

import org.jdesktop.lg3d.sg.Geometry;
import org.jdesktop.lg3d.sg.GeometryArray;
import org.jdesktop.lg3d.sg.IndexedGeometryArray;
import org.jdesktop.lg3d.sg.IndexedQuadArray;
import org.jdesktop.lg3d.wg.Toolkit3D;

import gnu.x11.Enum;
import gnu.x11.Rectangle;

/**
 * @author bukhari
 */
public class WindowShape {
    private Enum rectEnum;
    private int rectCount;
    private float width;
    private float height;
    private float textureWidthScale;
    private float textureHeightScale;
    
    IndexedGeometryArray stGeometry;
    public WindowShape(){}
    
    /**
     *
     * TODO this is very X specific, we need to explore adding
     * a level of abstraction for the contructor and the
     * X11 Enum
     *
     * @param enum
     */
    public WindowShape(Enum enumerate, int nrect) {
        rectEnum = enumerate;
        rectCount = nrect;
    }
    
    public void setSize(float width,
    float height,
    float textureWidthScale,
    float textureHeightScale ) {
        this.textureWidthScale = textureWidthScale;
        this.textureHeightScale = textureHeightScale;
        this.height = height;
        this.width = width;
    }
    
    Geometry getGeometry() {
        int i=0;
        IndexedGeometryArray geometry = new IndexedQuadArray(4*rectCount,
                                                GeometryArray.COORDINATES |
                                                GeometryArray.TEXTURE_COORDINATE_2,
                                                1, new int[] {0},
                                                4*rectCount);
        
        geometry.setCapability(GeometryArray.ALLOW_COORDINATE_WRITE);
        geometry.setCapability(GeometryArray.ALLOW_TEXCOORD_WRITE);
        
        
        int indics[] = new int[4*rectCount];
        Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
        while(rectEnum.more()){
            int off = i*4;
            Rectangle rect = (Rectangle) rectEnum.next();
            float w = toolkit3d.widthNativeToPhysical(rect.width);
            float h = toolkit3d.heightNativeToPhysical(rect.height);
            float w1 = width * textureWidthScale;
            float h1 = height * textureHeightScale;
            
            float xcoord = toolkit3d.widthNativeToPhysical(rect.x);
            float ycoord1 = height - (toolkit3d.heightNativeToPhysical(rect.y));
            float ycoord2 = ycoord1 - h;
            
            // for texture coordinate
            float x1 = xcoord , x2 = x1 + w;
            float y1 = toolkit3d.heightNativeToPhysical(rect.y) , y2 = y1+h;
            // set geometry coordinates
            float[] a1={xcoord,ycoord1,0};
            float[] a2={xcoord,ycoord2,0};
            float[] a3={xcoord+w,ycoord2,0};
            float[] a4={xcoord+w,ycoord1,0};
            
            
                /*float[] t1={x,y};
                float[] t2={x+w,y};
                float[] t3={x+w,y+h};
                float[] t4={x,y+h};
                float[] t1={x1/w1,(h1-y1)/h1};
                float[] t2={(x2+1)/w1, (h1-y1)/h1};
                float[] t3={(x2+1)/w,(h1-(y2+1))/h1};
                float[] t4={x1/w1,(h1-(y2+1))/h1};*/
            // set texture coordinates
            float[] t1={x1/w1,(y1/h1)};
            float[] t2={x1/w1,(y2/h1)};
            float[] t3={x2/w1,(y2/h1)};
            float[] t4={x2/w1,(y1/h1)};
            
            
            geometry.setCoordinate(off,a1);
            geometry.setTextureCoordinate(0,off,t1);
            indics[off] = off;
            geometry.setCoordinate(off+1,a2);
            geometry.setTextureCoordinate(0,off+1,t2);
            indics[off+1] = off+1;
            geometry.setCoordinate(off+2,a3);
            geometry.setTextureCoordinate(0,off+2,t3);
            indics[off+2] = off+2;
            geometry.setCoordinate(off+3,a4);
            geometry.setTextureCoordinate(0,off+3,t4);
            indics[off+3] = off+3;
            i++;
        }
        geometry.setCoordinateIndices(0, indics);
        geometry.setTextureCoordinateIndices(0, 0, indics);
        stGeometry = geometry;
        return geometry;
    }
    
    public Geometry getLastGeometry() {
        return stGeometry;
    }
}
