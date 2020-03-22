/**
 * Project Looking Glass
 *
 * $RCSfile: ExtendableLine.java,v $
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
 * $Date: 2005-08-09 02:04:38 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.shape;

import javax.vecmath.Point3f;

import org.jdesktop.lg3d.sg.Appearance;
import org.jdesktop.lg3d.sg.ColoringAttributes;
import org.jdesktop.lg3d.sg.LineArray;
import org.jdesktop.lg3d.sg.LineAttributes;
import org.jdesktop.lg3d.sg.Shape3D;
import org.jdesktop.lg3d.sg.TransparencyAttributes;

/**
 * A shape which renders a line to which vertices can be added
 */
public class ExtendableLine extends Shape3D {
    private static final int LINE_SIZE = 50;
    private LineArray currentLine;
    private int currentSize = 0;
     
    public ExtendableLine() {
        newLine();
        setCapability( Shape3D.ALLOW_GEOMETRY_WRITE );
        
//      logger.severe("New Extendable Line");
        
        Appearance app = new Appearance();
        LineAttributes lineAttr = new LineAttributes();
        lineAttr.setLineWidth(3f);
        app.setLineAttributes(lineAttr);
        ColoringAttributes colorAttr = new ColoringAttributes();
        colorAttr.setColor(1f,0f,0f);
        app.setColoringAttributes(colorAttr);
        TransparencyAttributes ta = new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.6f);
        app.setTransparencyAttributes(ta);
        setAppearance(app);
    }
    
    public void addVertex(Point3f vertex) {
        if (currentSize==LINE_SIZE) newLine();
        
//      logger.severe("Add vertex "+vertex);
       
        currentLine.setCoordinate( currentSize, vertex);
        currentSize++;
        
        if (currentSize%2==0) {
            currentLine.setValidVertexCount(currentSize);
            
            if (currentSize==LINE_SIZE) newLine();
            currentLine.setCoordinate( currentSize, vertex);
            currentSize++;                
        }
    }
    
    private void newLine() {
        currentLine = new LineArray(LINE_SIZE, LineArray.COORDINATES);
        currentLine.setCapability( LineArray.ALLOW_COORDINATE_WRITE );
        currentLine.setCapability( LineArray.ALLOW_COUNT_WRITE );
        currentSize=0;
        currentLine.setValidVertexCount(currentSize);
        this.addGeometry(currentLine);
    }
    
    public void removeAllGeometries() {
        super.removeAllGeometries();
        newLine();
    }
}
