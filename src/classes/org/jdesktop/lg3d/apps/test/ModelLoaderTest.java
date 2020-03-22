/**
 * Project Looking Glass
 *
 * $RCSfile: ModelLoaderTest.java,v $
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
 * $Date: 2005-04-14 23:03:56 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.apps.test;


import org.jdesktop.lg3d.wg.Container3D;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.ModelLoader;
import org.jdesktop.lg3d.sg.*;
import javax.vecmath.Vector3f;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Matrix4f;
import org.jdesktop.lg3d.utils.eventaction.Component3DMover;
/**
 *
 * @author  paulby
 */
public class ModelLoaderTest extends HelloUniverseApp {
    
    /** Creates a new instance of CursorTest */
    public ModelLoaderTest() {
        super(new String[0]);
    }
    
    /**
     * Give subclasses the abilitity to add more components to the root
     * container
     */
    protected void addComponents( Container3D container ) {
        Transform3D t3d = new Transform3D();
        t3d.setTranslation(new Vector3f(0f, 0f, 0f));
        TransformGroup pos = new TransformGroup(t3d);
        
        ModelLoader model = null;
        try {
            Matrix4f changeAxis = new Matrix4f();
            Matrix4f m4f = new Matrix4f();
            
            m4f.rotY( (float)Math.toRadians(150) );
            changeAxis.rotX( (float)Math.toRadians(-90));
            changeAxis.mul(m4f,changeAxis);
            
            // Bioware Models
            String file = "Mummy_Priest.mdl";
            //String file = "Fire_Elemental.mdl";
            //String file = "Giant_Spider.mdl";
            //String file = "Flesh_Golem.mdl";
            
//            model = new ModelLoader("../resources/models", "Castle.j3f", org.jdesktop.media.j3d.utils.loaderwrappers.J3fLoader.class );
            model = new ModelLoader("../resources/models", "galleon.obj", com.sun.j3d.loaders.objectfile.ObjectFile.class );
//            model = new ModelLoader("../resources/models/Never_Winter_Nights/Bioware", 
//                                    file, 
//                                    Class.forName("net.sf.nwn.loader.NWNLoader"),
//                                    changeAxis);
//            model = new ModelLoader("../resources/models/Never_Winter_Nights/nwvault", 
//                                    "Halfling.mdl", 
//                                    Class.forName("net.sf.nwn.loader.NWNLoader"),
//                                    changeAxis);
        } catch( Exception e ) {
            e.printStackTrace();
        }
        
        model.resize( new Vector3f(), 0.03f );
        pos.addChild( model );
        
        model.addListener(new Component3DMover());
        
        Component3D comp = new Component3D();
        comp.addChild( pos );
                
        container.addChild(comp);
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new ModelLoaderTest();
    }
    
}
