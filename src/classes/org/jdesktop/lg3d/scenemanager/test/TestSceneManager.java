/**
 * Project Looking Glass
 *
 * $RCSfile: TestSceneManager.java,v $
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
 * $Revision: 1.8 $
 * $Date: 2007-01-04 22:40:20 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.test;

import java.util.logging.Logger;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.displayserver.PlatformConfig;
import org.jdesktop.lg3d.displayserver.nativewindow.IntegrationModule;
import org.jdesktop.lg3d.displayserver.nativewindow.NativeCursor2D;
import org.jdesktop.lg3d.displayserver.nativewindow.NativePopup3D;

import org.jdesktop.lg3d.scenemanager.CursorModule;
import org.jdesktop.lg3d.scenemanager.DisplayServerManagerInterface;
import org.jdesktop.lg3d.scenemanager.SceneManager;
import org.jdesktop.lg3d.scenemanager.utils.cursormodule.StandardCursorModule;
import org.jdesktop.lg3d.sg.AmbientLight;
import org.jdesktop.lg3d.sg.BoundingSphere;
import org.jdesktop.lg3d.sg.BranchGroup;
import org.jdesktop.lg3d.sg.DirectionalLight;
import org.jdesktop.lg3d.utils.cursor.WigglingCursor;
import org.jdesktop.lg3d.utils.shape.ColorCube;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Cursor3D;
import org.jdesktop.lg3d.wg.Frame3D;
import org.jdesktop.lg3d.wg.Toolkit3D;


/**
 *
 */
public class TestSceneManager implements SceneManager {
    protected static final Logger logger = Logger.getLogger("lg.scenemanager");
    protected DisplayServerManagerInterface displayServer;
    protected CursorModule cursorModule;

    public TestSceneManager() {
    }

    public void initialize(DisplayServerManagerInterface displayServer) {
        this.displayServer = displayServer;
        cursorModule = new StandardCursorModule();
        displayServer.setCursorModule(cursorModule);
	Cursor3D.DEFAULT_CURSOR = new WigglingCursor("DEFAULT_CURSOR", 0.01f, true);
	cursorModule.setCursor(Cursor3D.DEFAULT_CURSOR);
        
        BranchGroup bg = new BranchGroup();
        
        Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
        float x = toolkit3d.getScreenWidth() * 0.4f;
        float y = toolkit3d.getScreenHeight() * 0.4f;
        
        Component3D testObj00 = new Component3D();
        testObj00.addChild(new ColorCube(0.002f));
        testObj00.setTranslation(-x, -y, 0.0f);
        bg.addChild(testObj00);
                
        Component3D testObj01 = new Component3D();
        testObj01.addChild(new ColorCube(0.002f));
        testObj01.setTranslation(-x, y, 0.0f);
        bg.addChild(testObj01);
        
        Component3D testObj10 = new Component3D();
        testObj10.addChild(new ColorCube(0.002f));
        testObj10.setTranslation(x, -y, 0.0f);
        bg.addChild(testObj10);
        
        Component3D testObj11 = new Component3D();
        testObj11.addChild(new ColorCube(0.002f));
        testObj11.setTranslation(x, y, 0.0f);
        bg.addChild(testObj11);
        
        BranchGroup lightBG = new BranchGroup();        
        AmbientLight ambLight = new AmbientLight( new Color3f(0.3f, 0.3f, 0.3f) );
        ambLight.setInfluencingBounds( new BoundingSphere( new Point3f(), Float.POSITIVE_INFINITY ));
        lightBG.addChild(ambLight);
        
        DirectionalLight dirLight1 = new DirectionalLight( new Color3f( 0.9f, 0.9f, 0.8f ), new Vector3f( -1f, -1f, -1f ));
        dirLight1.setInfluencingBounds( new BoundingSphere( new Point3f(), Float.POSITIVE_INFINITY ));
        lightBG.addChild( dirLight1 );
        
        bg.addChild(lightBG);
        
        displayServer.getSceneRoot().addChild(bg);
        
        initializeNativeWindowIntegration();        
    }

    public void addFrame3D(Frame3D frame3d) {
        System.out.println("WARNING addFrame3D");
        displayServer.getSceneRoot().addChild(frame3d);
    }
    
    public void removeFrame3D(Frame3D frame3d) {
        displayServer.getSceneRoot().removeChild(frame3d);
    }
    
   private void initializeNativeWindowIntegration() {
        String className = PlatformConfig.getConfig().getNativeWinIntegration();
        try {
            Class clazz = Class.forName(className);
            IntegrationModule module = (IntegrationModule)clazz.newInstance();
            module.initialize();
            logger.info("Native Window Integration module initialized: "
                + module);
        } catch (Exception ex) {
            logger.severe("Failed to initialize native integration module: "
                + className + ": " + ex);
        }
        
        // Initializes and registers the "NATIVE_CURSOR_2D" cursor
        new NativeCursor2D();
        // FIXME -- for now, perform the following initialization here
        NativePopup3D.initHelper();
    }
}

