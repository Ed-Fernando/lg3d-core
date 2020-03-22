/**
 * Project Looking Glass
 *
 * $RCSfile: CursorTest.java,v $
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
 * $Date: 2004-06-23 18:49:51 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.apps.test;


import org.jdesktop.lg3d.wg.Container3D;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.sg.*;
import org.jdesktop.lg3d.utils.shape.ColorCube;
import javax.vecmath.Vector3f;
/**
 *
 * @author  paulby
 */
public class CursorTest extends HelloUniverseApp {
    
    /** Creates a new instance of CursorTest */
    public CursorTest() {
        super(new String[0]);
    }
    
    /**
     * Give subclasses the abilitity to add more components to the root
     * container
     */
    protected void addComponents( Container3D container ) {
        Transform3D t3d = new Transform3D();
        t3d.setTranslation(new Vector3f(0.02f, 0f, 0f));
        TransformGroup pos = new TransformGroup(t3d);
        
	Transform3D orient = new Transform3D();
        orient.rotY(Math.toRadians(35));
        TransformGroup tg = new TransformGroup(orient);
        tg.addChild( new ColorCube(0.02f) );
        
        Component3D comp = new Component3D();
        pos.addChild(tg);
        comp.addChild(pos);
        
        container.addChild(comp);
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new CursorTest();
    }
    
}
