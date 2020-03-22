/**
 * Project Looking Glass
 *
 * $RCSfile: ModelBackground.java,v $
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
 * $Revision: 1.9 $
 * $Date: 2006-03-24 10:36:42 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.background;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import org.jdesktop.lg3d.scenemanager.utils.SceneControl;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.ModelLoader;
import org.jdesktop.lg3d.sg.BranchGroup;


/**
 * A background that uses a 3D model instead of a 2D image
 *
 * @author  paulby
 */
public class ModelBackground extends Background {
    private SceneControl scenemanager;
    
    /** Creates a new instance of ModelBackground */
    public ModelBackground() {
    }
    
    
    public void initialize(SceneControl scenemanager) {
        if (this.scenemanager != null) {
            // the visual has been initialized already
            // just update the AppContainerControl
            this.scenemanager = scenemanager;
            return;
        }
        ModelLoader model;
        try {
            //model = new ModelLoader( "../resources/models/pinguin", "pinguin.3DS", Class.forName("com.realvue.sim.ui.loader.java3d.max3ds.Loader3DS") );
            model = new ModelLoader( "../resources/models/pinguin", "pinguin.j3f", Class.forName("org.jdesktop.j3d.utils.loaders.wrappers.J3fLoader") );
            //model = new ModelLoader( "../resources/models", "Castle.j3f", Class.forName("org.jdesktop.j3d.utils.loaders.wrappers.J3fLoader") );

            // Resize the model so that it fits in the sphere specified
            model.resize( new Vector3f(0f,0f,-0.3f), 0.5f );
        } catch( ClassNotFoundException cnfe ) {
            throw new RuntimeException(cnfe);
        }
        
        BranchGroup bg = new BranchGroup();
        bg.addChild( model );
        //nwnTest(bg);
        Component3D comp = new Component3D();
        
        comp.addChild( bg );
        this.addChild( comp );
        setName("ModelBackground");
    }
    
    /**
     * Test for the NWN models
     */
    private void nwnTest( BranchGroup bg ) {
        Matrix4f m4f = new Matrix4f();
        m4f.rotX( (float)Math.toRadians(-90) );
        
        Matrix4f rotZ = new Matrix4f();
        rotZ.rotZ( (float)Math.toRadians(180) );
        m4f.mul( rotZ );
        
        try {
            ModelLoader model1 = new ModelLoader( "../resources/models/Never_Winter_Nights/Bioware", 
                                                  "Mummy_Priest.mdl",
                                                  Class.forName("net.sf.nwn.loader.NWNLoader"),
                                                  m4f);

            model1.resize( new Vector3f(0f,-0.07f,-0.3f), 0.05f );

           
            m4f.setIdentity();
            m4f.rotX( (float)Math.toRadians(-90) );
            rotZ.rotZ( (float)Math.toRadians(200) );
            m4f.mul( rotZ );
            ModelLoader model2 = new ModelLoader( "../resources/models/Never_Winter_Nights/Bioware", 
                                                  "Mummy_Fighter2.mdl",
                                                  Class.forName("net.sf.nwn.loader.NWNLoader"),
                                                  m4f);

            model2.resize( new Vector3f(-0.05f,-0.07f,-0.3f), 0.05f );

            m4f.setIdentity();
            m4f.rotX( (float)Math.toRadians(-90) );
            rotZ.rotZ( (float)Math.toRadians(160) );
            m4f.mul( rotZ );
            ModelLoader model3 = new ModelLoader( "../resources/models/Never_Winter_Nights/Bioware", 
                                                  "Mummy_Fighter3.mdl",
                                                  Class.forName("net.sf.nwn.loader.NWNLoader"),
                                                  m4f);

            model3.resize( new Vector3f(0.05f,-0.07f,-0.3f), 0.05f );
            
            // The scaling of the model seems to have strange effects on the
            // particle system so this does not render correctly.
//            m4f.setIdentity();
//            m4f.rotX( (float)Math.toRadians(-90) );
//            rotZ.rotZ( (float)Math.toRadians(160) );
//            m4f.mul( rotZ );
//            ModelLoader model4 = new ModelLoader( "../resources/models/Never_Winter_Nights/Bioware", 
//                                                  "Fire_Elemental.mdl",
//                                                  Class.forName("net.sf.nwn.loader.NWNLoader"),
//                                                  m4f);

//            model4.resize( new Vector3f(0f,0f,-0.3f), 0.05f );

            bg.addChild( model1 );
            bg.addChild( model2 );
            bg.addChild( model3 );
            
//            bg.addChild( model4 );
        } catch( ClassNotFoundException cnfe ) {
            throw new RuntimeException(cnfe);
        }
    }
    
    public void setEnabled(boolean enabled) {
        if (enabled) {
            activate();
        } else {
            deactivate();
        }
    }
    
    private void activate() {        
    }
    
    private void deactivate() {
    }
}
