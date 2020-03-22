/**
 * Project Looking Glass
 *
 * $RCSfile: SceneManagerBase.java,v $
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
 * $Revision: 1.30 $
 * $Date: 2007-07-22 23:16:39 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils;

import java.awt.event.KeyEvent;
import java.util.logging.Logger;
import org.jdesktop.lg3d.displayserver.PlatformConfig;

import org.jdesktop.lg3d.displayserver.nativewindow.IntegrationModule;
import org.jdesktop.lg3d.displayserver.nativewindow.NativeCursor2D;
import org.jdesktop.lg3d.displayserver.nativewindow.NativePopup3D;
import org.jdesktop.lg3d.scenemanager.CursorModule;
import org.jdesktop.lg3d.scenemanager.DisplayServerManagerInterface;
import org.jdesktop.lg3d.scenemanager.GestureModule;
import org.jdesktop.lg3d.scenemanager.SceneManager;
import org.jdesktop.lg3d.scenemanager.utils.cursormodule.StandardCursorModule;
import org.jdesktop.lg3d.scenemanager.utils.gesture.GestureModuleBase;
import org.jdesktop.lg3d.scenemanager.utils.globallights.GlobalLights;
import org.jdesktop.lg3d.scenemanager.utils.plugin.SceneManagerPlugin;
import org.jdesktop.lg3d.utils.action.ActionBooleanInt;
import org.jdesktop.lg3d.utils.eventadapter.KeyPressedEventAdapter;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Cursor3D;
import org.jdesktop.lg3d.wg.Frame3D;
import org.jdesktop.lg3d.wg.event.InputEvent3D.ModifierId;
import org.jdesktop.lg3d.wg.event.LgEventSource;


/**
 * Utility class for all the 2+1/2 D scene manager implementations.
 */
public abstract class SceneManagerBase implements SceneManager {
    protected static final Logger logger = Logger.getLogger("lg.scenemanager");
    private static float defaultCursorSize = 0.01f;
    private static Cursor3D fallbackCursor = null;
    
    protected DisplayServerManagerInterface displayServer;
    protected Component3D rootContainer;
    protected boolean addDefaultLights = true;
    protected boolean addGestureSupport = true;
    
    /**
     * Initialization method to be implemented in a derived class.
     * Most of initialization should be taken place in this method
     * instead of constructor since some part of the class is not
     * initialized until this method gets invoked.
     */
    protected abstract void initialize();
    
    public abstract void addFrame3D(Frame3D frame3d);
    
    public abstract void removeFrame3D(Frame3D frame3d);
    
    public abstract void addPlugin(SceneManagerPlugin plugin);
    
    public abstract void removePlugin(SceneManagerPlugin plugin);
    
    static {
        String wonderlandStr = System.getProperty("lg.wonderland", "false");
	boolean inWonderland = "true".equals(wonderlandStr);

	if (inWonderland) {
	    defaultCursorSize = 0.02f;
	}
    }

    public final void initialize(final DisplayServerManagerInterface displayServer) {
        this.displayServer = displayServer;
        
        rootContainer = new Component3D();
        rootContainer.setName("GlassySceneManager.rootContainer");
        // be sure to call displayServer's getSceneRoot()
        displayServer.getSceneRoot().addChild(rootContainer);
        
        CursorModule cursorModule = new StandardCursorModule();
        displayServer.setCursorModule(cursorModule);
        
        if (addGestureSupport) {
            GestureModule gestureModule = GestureModuleBase.createGestureModule();
            displayServer.setGestureModule(gestureModule);
        }
        
//        Preferences prefs = LgPreferencesHelper.userNodeForPackage(SceneManager.class);
        
//        setupCursors(prefs);
        setupCursors();
        
        Cursor3D defaultCursor = Cursor3D.DEFAULT_CURSOR;
	if (defaultCursor == null) {
	    logger.warning("No default cursor found.");
	} else {
	    cursorModule.setCursor(defaultCursor);
	}
        rootContainer.setCursor(defaultCursor);
        
//        GlobalLights globalLights = getGlobalLights(prefs);
        if (addDefaultLights) {
            GlobalLights globalLights = new org.jdesktop.lg3d.scenemanager.utils.globallights.StandardGlobalLights();

            if (globalLights != null) {
                globalLights.initialize();
                rootContainer.addChild(globalLights);
            }
        }
        
        // Start native window integration module if necessary
        initializeNativeWindowIntegration();
                
        // Listen for the Print Scene Graph command (Ctrl-P)
        rootContainer.addListener(
            new KeyPressedEventAdapter(ModifierId.CTRL,
                new ActionBooleanInt() {
                    public void performAction(LgEventSource source, 
                            boolean pressed, int key) 
                    {
                        if (pressed && key == KeyEvent.VK_P) {
                            displayServer.printSceneGraph();
			}
		    }
            }));
            
        initialize();
        
//        initializePlugins(prefs);
    }
    
    private void initializeNativeWindowIntegration() {
        String className = PlatformConfig.getConfig().getNativeWinIntegration();
        if (className==null || className.length()<1)
            return;
        
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
    
    public Component3D getSceneRoot() {
        return rootContainer;
    }
        
    private void setupCursors() {
	// TODO
	// This is horrible, need to create a better mechanism for the SceneManager to
	// initialize the default cursors
	// Possibly through the configuration file
	Cursor3D.DEFAULT_CURSOR    = new org.jdesktop.lg3d.utils.cursor.WigglingCursor("DEFAULT_CURSOR", defaultCursorSize);
	Cursor3D.MEDIUM_CURSOR     = new org.jdesktop.lg3d.utils.cursor.WigglingCursor("MEDIUM_CURSOR", 0.0075f);
	Cursor3D.SMALL_CURSOR      = new org.jdesktop.lg3d.utils.cursor.WigglingCursor("SMALL_CURSOR", 0.005f);
	Cursor3D.MOVE_CURSOR       = new org.jdesktop.lg3d.utils.cursor.MoveCursor("MOVE_CURSOR", 0.01f);
	Cursor3D.SMALL_MOVE_CURSOR = new org.jdesktop.lg3d.utils.cursor.MoveCursor("SMALL_MOVE_CURSOR", 0.0075f);
	Cursor3D.N_RESIZE_CURSOR   = new org.jdesktop.lg3d.utils.cursor.ResizeCursor("N_RESIZE_CURSOR", 0.01f);
	Cursor3D.S_RESIZE_CURSOR   = new org.jdesktop.lg3d.utils.cursor.ResizeCursor("S_RESIZE_CURSOR", 0.01f);
	Cursor3D.E_RESIZE_CURSOR   = new org.jdesktop.lg3d.utils.cursor.ResizeCursor("E_RESIZE_CURSOR", 0.01f);
	Cursor3D.W_RESIZE_CURSOR   = new org.jdesktop.lg3d.utils.cursor.ResizeCursor("W_RESIZE_CURSOR", 0.01f);
	Cursor3D.SE_RESIZE_CURSOR  = new org.jdesktop.lg3d.utils.cursor.ResizeCursor("SE_RESIZE_CURSOR", 0.01f);
	Cursor3D.SW_RESIZE_CURSOR  = new org.jdesktop.lg3d.utils.cursor.ResizeCursor("SW_RESIZE_CURSOR", 0.01f);
	Cursor3D.NE_RESIZE_CURSOR  = new org.jdesktop.lg3d.utils.cursor.ResizeCursor("NE_RESIZE_CURSOR", 0.01f);
	Cursor3D.NW_RESIZE_CURSOR  = new org.jdesktop.lg3d.utils.cursor.ResizeCursor("NW_RESIZE_CURSOR", 0.01f);
	Cursor3D.NULL_CURSOR       = new org.jdesktop.lg3d.utils.cursor.NullCursor("NULL_CURSOR", 0.01f);

	// Cursors for Wonderland
	Cursor3D.MOVE_CURSOR_WL       = new org.jdesktop.lg3d.utils.cursor.MoveCursor("MOVE_CURSOR_WL", 0.025f);
	Cursor3D.SE_RESIZE_CURSOR_WL  = new org.jdesktop.lg3d.utils.cursor.ResizeCursor("SE_RESIZE_CURSOR_WL", 0.025f);
	Cursor3D.MOVE_Z_CURSOR_WL     = new org.jdesktop.lg3d.utils.cursor.WonderlandMoveZCursor("MOVE_Z_CURSOR_WL", 0.025f);
	Cursor3D.ROTATE_Y_CURSOR_WL   = new org.jdesktop.lg3d.utils.cursor.WonderlandRotateYCursor("ROTATE_Y_CURSOR_WL", 0.025f);
    }
    
//    private void setupCursors(Preferences prefs) {
//	Cursor3D.DEFAULT_CURSOR     = createCursor(prefs, "DEFAULT_CURSOR");
//        Cursor3D.MEDIUM_CURSOR      = createCursor(prefs, "MEDIUM_CURSOR");
//        Cursor3D.SMALL_CURSOR       = createCursor(prefs, "SMALL_CURSOR");
//        Cursor3D.NULL_CURSOR        = createCursor(prefs, "NULL_CURSOR");
//        
//        Cursor3D.MOVE_CURSOR        = createCursor(prefs, "MOVE_CURSOR");
//        Cursor3D.SMALL_MOVE_CURSOR  = createCursor(prefs, "SMALL_MOVE_CURSOR");
//        
//        Cursor3D.N_RESIZE_CURSOR    = createCursor(prefs, "N_RESIZE_CURSOR");
//        Cursor3D.S_RESIZE_CURSOR    = createCursor(prefs, "S_RESIZE_CURSOR");
//        Cursor3D.E_RESIZE_CURSOR    = createCursor(prefs, "E_RESIZE_CURSOR");
//        Cursor3D.W_RESIZE_CURSOR    = createCursor(prefs, "W_RESIZE_CURSOR");
//        Cursor3D.SE_RESIZE_CURSOR   = createCursor(prefs, "SE_RESIZE_CURSOR");
//        Cursor3D.SW_RESIZE_CURSOR   = createCursor(prefs, "SW_RESIZE_CURSOR");
//        Cursor3D.NE_RESIZE_CURSOR   = createCursor(prefs, "NE_RESIZE_CURSOR");
//        Cursor3D.NW_RESIZE_CURSOR   = createCursor(prefs, "NW_RESIZE_CURSOR");
//    }
//    
//    private Cursor3D createCursor(Preferences prefs, String name) {
//        Preferences cursorPrefs = prefs.node("cursors");
//        String cursorInfo = cursorPrefs.get(name, null);
//        if (cursorInfo == null) {
//            logger.warning("Cannot find cursor info: " + name);
//            return getFallbackCursor();
//        }
//        String[] cursorInfoElem = cursorInfo.split("\\s*,\\s*");
//        if (cursorInfoElem.length == 0 || cursorInfoElem.length > 2) {
//            logger.warning("Illegal cursor info format: " + name + ": " + cursorInfo);
//            return getFallbackCursor();
//        }
//        String cursorClass = cursorInfoElem[0];
//        float cursorSize = defaultCursorSize;
//        if (cursorInfoElem.length >= 2) {
//            String cursorSizeStr = cursorInfoElem[1];
//            try {
//                cursorSize = Float.parseFloat(cursorSizeStr);
//            } catch (NumberFormatException nfe) {
//                logger.warning("Illegal cursor size info: " + name + ": " + cursorSizeStr);
//            }
//        }
//        
//        Cursor3D ret = null;
//        try {
//            Class clazz = Class.forName(cursorClass);
//            Constructor<Cursor3D> ctr = clazz.getConstructor(String.class, float.class);
//            ret = ctr.newInstance(name, cursorSize);
//        } catch (Exception ex) {
//            logger.warning("Failed to instantiate cursor: " + name + ": " + ex);
//            return getFallbackCursor();
//        }
//        return ret;
//    }
//    
//    private Cursor3D getFallbackCursor() {
//        if (fallbackCursor != null) {
//            return fallbackCursor;
//        }
//        
//        SimpleAppearance cursorApp 
//            = new SimpleAppearance(1.0f, 0.0f, 0.0f, 0.5f);
//        Sphere cursorBody 
//            = new Sphere(0.002f, Sphere.GENERATE_NORMALS, 12, cursorApp);
//        
//        fallbackCursor = new Cursor3D("Fallback Cursor", cursorBody);
//        fallbackCursor.setPreferredSize(new Vector3f(0.004f, 0.004f, 0.004f));
//        
//        return fallbackCursor;
//    }
//    
//    private GlobalLights getGlobalLights(Preferences prefs) {
//        String glClass = prefs.get("globallights", null);
//                
//        if (glClass == null) {
//            logger.warning("Globallights info is broken");
//            return null;
//        }
//        GlobalLights ret = null;
//        try {
//            Class clazz = Class.forName(glClass);
//            ret = (GlobalLights)clazz.newInstance();
//        } catch (Exception ex) {
//            logger.warning("Failed to instantiate globallights: " + ex);
//            return null;
//        }
//        
//        return ret;
//    }
//    
//    private void initializePlugins(Preferences prefs) {
//        Preferences pluginsPrefs = prefs.node("plugins");
//        
//        String[] plugins = null;
//        try {
//            plugins = pluginsPrefs.keys();
//        } catch (BackingStoreException bse) {
//            logger.warning("Failed to obtain SceneManagerPlugin info: " + bse);
//            return;
//        }
//        
//        for (String plugin : plugins) {
//            String pluginClass = pluginsPrefs.get(plugin, null);
//            if (pluginClass == null) {
//                logger.warning("Preferences info is broken for SceneManagerPlugin: " + plugin);
//                continue;
//            }
//            logger.info("Initializing: " + pluginClass);
//            try {
//                Class clazz = Class.forName(pluginClass);
//                SceneManagerPlugin pluginObj = (SceneManagerPlugin)clazz.newInstance();
//                addPlugin(pluginObj);
//            } catch (Exception ex) {
//                logger.warning("Failed to instantiate SceneManagerPlugin: " + plugin + ": " + ex);
//            }
//        }
//    }
}
