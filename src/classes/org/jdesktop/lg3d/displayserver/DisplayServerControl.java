/**
 * Project Looking Glass
 *
 * $RCSfile: DisplayServerControl.java,v $
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
 * $Revision: 1.52 $
 * $Date: 2007-11-17 03:14:20 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver;


import java.awt.Toolkit;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;
import java.lang.management.CompilationMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.net.URL;
import java.net.MalformedURLException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Comparator;
import java.util.Map;
import org.jdesktop.j3d.utils.scenegraph.transparency.TransparencyOrderController;

import org.jdesktop.lg3d.toolkit.lg3dtoolkit;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Locale;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.media.j3d.View;
import javax.media.j3d.ShaderError;
import javax.media.j3d.ShaderErrorListener;
import javax.vecmath.Point3d;

import com.sun.j3d.utils.universe.Viewer;
import com.sun.j3d.utils.universe.PlatformGeometry;
import com.sun.j3d.utils.universe.ConfigContainer;
import com.sun.j3d.utils.universe.ConfiguredUniverse;
import com.sun.j3d.utils.universe.ViewInfo;
import com.sun.j3d.utils.scenegraph.transparency.TransparencySortGeom;
import org.jdesktop.lg3d.displayserver.net.ResourceURLStreamHandlerFactory;

import org.jdesktop.lg3d.sg.BranchGroup;
import org.jdesktop.lg3d.sg.SceneGraphSetup;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.scenemanager.config.ConfigurationCompleteEvent;
import org.jdesktop.lg3d.sg.internal.wrapper.SceneGraphObjectWrapper;
import org.jdesktop.lg3d.wg.AnimationScheduler;
import org.jdesktop.lg3d.scenemanager.SceneManager;
import org.jdesktop.lg3d.scenemanager.CursorModule;
import org.jdesktop.lg3d.scenemanager.DisplayServerManagerInterface;
import org.jdesktop.lg3d.scenemanager.GestureModule;
import org.jdesktop.lg3d.scenemanager.utils.event.ScreenResolutionChangedEvent;
import org.jdesktop.lg3d.sg.SceneGraphObject;
import org.jdesktop.lg3d.wg.WidgetManager;

/**
 * The main body of the LG Display Server. Although main(String[] args) is not
 * in the class, this class is instantiated either by the AppConnector when
 * running in dev mode or by the ClientHandler
 */
public class DisplayServerControl implements DisplayServerAppInterface, DisplayServerManagerInterface {

    // TODO: TEMPORARY: the ability to select Yup vs. Ydown images is provided in
    // order to test the Yup subimage paths of Java3D, which currently have bugs.
    // Once these are fully debugged, we can toss this switch and choose to always
    // use one image orientation or the other. This is currently set to Ydown mode
    // because that is the only orientation for which Java3D works.

    public static final boolean USE_YUP_IMAGES = true;
   
    /**
     * Locale reference needed to create the "view" portion
     * of the scene graph.
     */
    protected Locale          locale;

    /**
     * Viewer reference needed to create the "view" portion
     * of the scene graph.
     */
    protected Viewer[]        viewer = null;
    
    /** the wrapped configured universe */
    private ConfiguredUniverse wrapped;
    
    /** the cursor module for handling cursors */
    private CursorModule cursorModule;
    
    /** the manager for the scene (the desktop) */
    private SceneManager sceneManager;
    
    /** the information about how the universe if viewed */
    private ViewInfo viewInfo;
    
    /** the Java3D branch group for the scene */
    private javax.media.j3d.BranchGroup mgmtBG;
    
    /** the root branch group */
    private BranchGroup sceneRoot;
    
    /** The platform geometry */
    private BranchGroup platformGeometry = null;
    
    /** The branch group on which to attach cursors */
    private BranchGroup cursorRoot;

    /** the event processor for processing events */
    private EventProcessor eventProcessor;
    
    /** the logger instance */
    private Logger logger = Logger.getLogger("lg.displayserver");
    
    /** the Foundation Window System  */
    private Class  fwsClass;
    private Object fws;
    private Method fwsMethodSetCursorModule;
    
    /** */
    private double preferedWidth;
    private double preferedHeight;
    
    private static URLStreamHandlerFactory factory =
        new ResourceURLStreamHandlerFactory();
   
    static {
        try {
            URL.setURLStreamHandlerFactory(factory);
        } catch (Error e) {
        }
    }
    
    /**
     * Create a new DisplayServerControl, initializing the scene graph
     */
    public DisplayServerControl() {
        SceneGraphSetup.initializeServer();
    }
    
    /**
     * initialize the display server control
     * - initialize the universe
     * - initialize the foundation window system
     * - initialize the view
     * - create the root scene graph
     * - initialize the event processor
     * - initialize the management branch group
     * - add the event processor to the management branch group
     * - add the management branch group to the universe locale
     * - initialize the scene manager
     * @param maxFramesPerSecond the maximum frames per second
     */
    public void initialize(int maxFramesPerSecond) {        
        ConfigContainer universeConfig;
        
        try {
            String configProp = System.getProperty("lg.displayconfigurl");
            URL configURL=null;
            
	    // if there is a lg.displayconfigurl property set then
	    // load the configuration from the URL
            if (configProp!=null) {
                try {
                    configURL = new URL(configProp);
                    logger.config("Display configuration overridden by lg.displayconfigurl property to " + configURL);
                } catch(MalformedURLException mue) {
                    logger.warning("lg.displayconfigurl is malformed " + configProp);
                    configURL = null;
                }
            }
            
            UniverseFactory ufi = PlatformConfig.getConfig().getUniverseFactory();
            logger.config("Using UniverseFactory "+ufi);
            wrapped = ufi.createUniverse(configURL);
            
            wrapped.addShaderErrorListener(
                    new ShaderErrorListener() {
                        public void errorOccurred(ShaderError se) {
                            logger.warning("ShaderError has been caught: " + se.getErrorMessage());
                            se.printVerbose();
                        }});
                        
            new WidgetManager(wrapped);
            
        } catch (Exception e) {
            logger.log(Level.WARNING, "Unable to load display configuration", e);
            wrapped = new com.sun.j3d.utils.universe.ConfiguredUniverse();
        }
        
        eventProcessor = new EventProcessor(wrapped);
                
        logRenderingInfo();
	// create the viewing information
        viewInfo = new ViewInfo(wrapped.getViewer().getView(),
                            ViewInfo.CANVAS_AUTO_UPDATE |
                            ViewInfo.PLATFORM_AUTO_UPDATE);
                
        // Before proceeding the Canvas3D must have been rendered otherwise
        // X11 initialization will fail
        if (PlatformConfig.getConfig().getFoundationWinSys().equals("org.jdesktop.lg3d.displayserver.fws.x11.WinSysX11")) {
            logger.config("Waiting for Canvas3D");
        waitForCanvas3D();
        }
        
        if (System.getProperty("lg.use3dtoolkit", "false").equals("true")) {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            if (toolkit instanceof lg3dtoolkit)
                ((lg3dtoolkit)toolkit).enableLg3d(getClass().getClassLoader());
        }
        
        // create a new management branch group
        mgmtBG = new javax.media.j3d.BranchGroup();
                
        // TODO implement our own sorting framework so apps can specify
        // how they are sorted
//        TransparencySortController.setComparator(wrapped.getViewer().getView(), new SimpleDistanceComparator());
        //TransparencySortController.setComparator(wrapped.getViewer().getView(), new TestDistanceComparator());
        if (!inWonderland())
            mgmtBG.addChild(new TransparencyOrderController(wrapped.getViewer().getView()));
        checkViewConfiguration(Level.INFO);
        

	// get the event processor
        mgmtBG.addChild(eventProcessor);

	// initialize the foundation window system
        initializeFoundationWinSys( wrapped.getViewer().getCanvas3Ds(), 
                                    wrapped.getLocale(), mgmtBG, eventProcessor );
        
        // In normal operation the cursor is hidden by changes in the X server.
        // However this call remains for use in development mode
        hideCursor( wrapped.getViewer().getCanvas3Ds() );
        
	// initialize the univserse and viwing platform default properties
        wrapped.getViewingPlatform().setNominalViewingTransform();
	View view = wrapped.getViewer().getView();
        if (maxFramesPerSecond!=0)
            view.setMinimumFrameCycleTime(1000/maxFramesPerSecond);
	view.setTransparencySortingPolicy(View.TRANSPARENCY_SORT_GEOMETRY);
//	view.setDepthBufferFreezeTransparent(true);
	view.setSceneAntialiasingEnable(PlatformConfig.getConfig().isFullScreenAntiAliasing());

        locale = wrapped.getLocale();

	// create the root branch group and set its capabilities
        sceneRoot = new BranchGroup();
        sceneRoot.setCapability( BranchGroup.ALLOW_CHILDREN_EXTEND );
        sceneRoot.setCapability( BranchGroup.ALLOW_CHILDREN_READ );
        sceneRoot.setCapability( BranchGroup.ALLOW_CHILDREN_WRITE );
        
	// add the root branch group to the scene
        addBranchGraph( sceneRoot );
        
        // Create a wrapper for the view platform transform
//        javax.media.j3d.TransformGroup j3dTG = wrapped.getViewingPlatform().getViewPlatformTransform();
//        TransformGroup tg = new TransformGroup();
//        tg.wrapped = j3dTG;
//        j3dTG.setUserData( tg );
        
        mgmtBG.addChild( new ScreenCaptureBehavior(wrapped.getViewer().getCanvas3Ds() ));

        mgmtBG.addChild(AnimationScheduler.getScheduler());
        
//        mgmtBG.addChild(SpringProcessorBehavior.getSpringProcessorBehavior());
        
        if (isMonitorBehaviorRequired())
            mgmtBG.addChild(new PerformanceMonitorBehavior());
        
	// add the management branch group to the locale
        locale.addBranchGraph(mgmtBG);        
        
	// initialize the scene manager
        initializeSceneManager();
        
//        wrapped.getViewer().getCanvas3D(0).addKeyListener(new java.awt.event.KeyListener() {
//            public void keyPressed(java.awt.event.KeyEvent evt) {
//                System.out.println(evt);
//            }
//            public void keyReleased(java.awt.event.KeyEvent evt) {
//                System.out.println(evt);
//            }
//            public void keyTyped(java.awt.event.KeyEvent evt) {
//                System.out.println(evt);
//            }
//        });
        
        // initialize the Canvas3DSizeChanged event posting
        final Canvas3D canvas3d = wrapped.getCanvas();
        canvas3d.addComponentListener(
            new ComponentAdapter() {
                public void componentResized(ComponentEvent ce) {
                    Component comp = ce.getComponent();
                    float mppx = (float)(canvas3d.getPhysicalWidth() / canvas3d.getWidth());
                    float mppy = (float)(canvas3d.getPhysicalHeight() / canvas3d.getHeight());
                    int newWidth = comp.getWidth();
                    int newHeight = comp.getHeight();
                    logger.config("Canvas3D size changed to " + newWidth + "x" + newHeight);
                    checkViewConfiguration(Level.FINE);
                    postEvent(
                        new ScreenResolutionChangedEvent(
                            newWidth * mppx, newHeight * mppy), 
                        null);
                }
            });
    }
    
    /**
     * Return true if we are running in Wonderland, ie the lg.wonderland property is set
     */
    public static boolean inWonderland() {
        return System.getProperty("lg.wonderland", "false").equals("true") ? true : false;
    }
    
    /**
     * Put information about the rendering system in the log
     */
    private void logRenderingInfo() {
        Map vuMap = wrapped.getProperties();
        Map c3dMap = wrapped.getViewer().getView().getCanvas3D(0).queryProperties();
        
        StringBuffer logMsg = new StringBuffer("\n");
        logMsg.append("\tJava 3D version          : " + vuMap.get("j3d.version") + "\n");
        logMsg.append("\tJava 3D vendor           : " + vuMap.get("j3d.vendor") + "\n");
        logMsg.append("\tJava 3D specification.version : " + vuMap.get("j3d.specification.version") + "\n");
        logMsg.append("\tJava 3D specification.vendor : " + vuMap.get("j3d.specification.vendor") + "\n");
        logMsg.append("\tJava 3D renderer         : " + vuMap.get("j3d.renderer") + "\n");
        logMsg.append("\tRenderer version         : " + c3dMap.get("native.version") + "\n");
        logMsg.append("\n");
        logMsg.append("\tdoubleBufferAvailable    : " + c3dMap.get("doubleBufferAvailable") + "\n");
        logMsg.append("\tstereoAvailable          : " + c3dMap.get("stereoAvailable") + "\n");
        logMsg.append("\tsceneAntialiasingAvailable : " + c3dMap.get("sceneAntialiasingAvailable") + "\n");
        logMsg.append("\tsceneAntialiasingNumPasses : " + c3dMap.get("sceneAntialiasingNumPasses") + "\n");
        logMsg.append("\ttextureColorTableSize    : " + c3dMap.get("textureColorTableSize") + "\n");
        logMsg.append("\ttextureEnvCombineAvailable : " + c3dMap.get("textureEnvCombineAvailable") + "\n");
        logMsg.append("\ttextureCombineDot3Available : " + c3dMap.get("textureCombineDot3Available") + "\n");
        logMsg.append("\ttextureCombineSubtractAvailable : " + c3dMap.get("textureCombineSubtractAvailable") + "\n");
        logMsg.append("\ttexture3DAvailable       : " + c3dMap.get("texture3DAvailable") + "\n");
        logMsg.append("\ttextureCubeMapAvailable  : " + c3dMap.get("textureCubeMapAvailable") + "\n");
        logMsg.append("\ttextureSharpenAvailable  : " + c3dMap.get("textureSharpenAvailable") + "\n");
        logMsg.append("\ttextureDetailAvailable   : " + c3dMap.get("textureDetailAvailable") + "\n");
        logMsg.append("\ttextureFilter4Available  : " + c3dMap.get("textureFilter4Available") + "\n");
        logMsg.append("\ttextureAnisotropicFilterDegreeMax : " + c3dMap.get("textureAnisotropicFilterDegreeMax") + "\n");
        logMsg.append("\ttextureBoundaryWidthMax  : " + c3dMap.get("textureBoundaryWidthMax") + "\n");
        logMsg.append("\ttextureWidthMax          : " + c3dMap.get("textureWidthMax") + "\n");
        logMsg.append("\ttextureHeightMax         : " + c3dMap.get("textureHeightMax") + "\n");
        logMsg.append("\ttextureLodOffsetAvailable : " + c3dMap.get("textureLodOffsetAvailable") + "\n");
        logMsg.append("\ttextureLodRangeAvailable : " + c3dMap.get("textureLodRangeAvailable") + "\n");
        logMsg.append("\ttextureUnitStateMax      : " + c3dMap.get("textureUnitStateMax") + "\n");
        logMsg.append("\tcompressedGeometry       : " + c3dMap.get("compressedGeometry.majorVersionNumber") + "."
                                                      + c3dMap.get("compressedGeometry.minorVersionNumber") + "."
                                                      + c3dMap.get("compressedGeometry.minorMinorVersionNumber") + "\n");
        
        logger.info(logMsg.toString());
    }

    /**
     * Instantiates and initializes the SceneManager after finishing up 
     * initialization of DisplayServerImpl completely.
     * This part is isolated from the above constructor in order to avoid
     * a possible recursion in case of running everything in a single VM 
     * (see comments for AppConnector.getAppConnector()).
     */
    private void initializeSceneManager() {
//        Preferences prefs = LgPreferencesHelper.userNodeForPackage(SceneManager.class);
//        String smName = prefs.get("class", null);
//        if (smName == null) {
//             logger.severe("No SceneManager specified, exiting");
//             System.exit(1);
//        }
        
        String smName = null;
        try {
            ConfigControl configControl = ConfigControl.getConfigControl();
            sceneManager = configControl.createSceneManager();
            if (sceneManager == null) {
                logger.severe("No SceneManager, exiting");
                System.exit(1);
            }
            smName = sceneManager.getClass().getName();
            logger.info("Initialising Scene Manager " + smName);
	    sceneManager.initialize(this);
            
            processAllConfig();
            
	} catch (Exception ex) {
	    logger.log(Level.SEVERE, "Failed to instantiate SceneManager: "+ smName, ex);
	}
    }
    
    /**
     * Process all the configurate information, don't return until ConfigurationCompleteEvent
     * is received
     */
    private void processAllConfig() {
        // Wait until all config events have been processed
        final Semaphore complete = new Semaphore(0);
        AppConnectorPrivate.getAppConnector().addListener(new LgEventListener() {        
            public void processEvent(LgEvent evt) {
                logger.fine("Release lock, received ConfigurationCompleteEvent");
                synchronized(complete) {
                    complete.release();
                }
            }
            public Class<LgEvent>[] getTargetEventClasses() {
                return new Class[] {ConfigurationCompleteEvent.class};
            }
        }, ConfigurationCompleteEvent.class);
            
        ConfigControl configControl = ConfigControl.getConfigControl();
        configControl.processConfig();
            
        try {
            logger.fine("Acquiring lock, waiting for ConfigurationCompleteEvent");
            complete.acquire();
            logger.info("Start-up configuration completed...");
        } catch(InterruptedException e) {
            logger.warning("Interrupted waiting for Configuration to complete");
        }
    }
    
    /**
     * Initialize the Foundation Win Sys (FWS)
     * @param canvas the list of canvases to construct the FWS with
     * @param locale the Java3D locale to construct the FWS with
     */
    private void initializeFoundationWinSys( javax.media.j3d.Canvas3D[] canvas,
                                     javax.media.j3d.Locale locale, 
				     javax.media.j3d.BranchGroup mgmtBG, 
				     EventProcessor eventProcessor ) {
        String fwsName = PlatformConfig.getConfig().getFoundationWinSys();
        assert(viewInfo!=null);
	try {
	    fwsClass = Class.forName(fwsName);
            logger.info("Initialising Foundation WinSys "+fwsName);
            Constructor c = fwsClass.getConstructor( 
		new Class[] { javax.media.j3d.Canvas3D[].class,
			      javax.media.j3d.Locale.class,
			      ViewInfo.class,
			      javax.media.j3d.BranchGroup.class,
			      EventProcessor.class} );
            fws = c.newInstance( new Object[] {canvas, locale, viewInfo, mgmtBG, eventProcessor} );

	    Class cmClass = Class.forName("org.jdesktop.lg3d.scenemanager.CursorModule");
	    fwsMethodSetCursorModule = fwsClass.getMethod("setCursorModule", cmClass);

	} catch (Exception ex) {
            if (ex.getMessage() == null || ex.getMessage().equals("")) {
                ex.printStackTrace();
                // Get the error dialog to show up
                throw new RuntimeException();        
            } else {
                logger.log(Level.SEVERE, "Failed to instantiate FoundationWinSys: "+ fwsName, ex);
                ex.printStackTrace();
                throw new RuntimeException("Failed to instantiate FoundationWinSys: "
                    + fwsName + ": " + ex);
            }
	}
    }
    
    /**
     * Hide the platform cursor
     * @param canvases the list of canvases to set a null cursor for
     */
    private void hideCursor(Canvas3D[] canvases) {
        // any better way?
        BufferedImage bi =
            new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Cursor nullCursor = 
            Toolkit.getDefaultToolkit().createCustomCursor(bi,
                new Point(0, 0),"nullCursor");
        for (Canvas3D c : canvases) {
            c.setCursor(nullCursor);
        }
    }
    
    /**
     * Returns only when the canvas3d has rendered it's first frame, which
     * guarantees it's x11 information is valid
     */
    private void waitForCanvas3D() {
        Semaphore canvas3dInitialized = new Semaphore(0, false);
        
        javax.media.j3d.BranchGroup bg = new javax.media.j3d.BranchGroup();
        bg.setCapability(javax.media.j3d.BranchGroup.ALLOW_DETACH);
        bg.addChild(new Canvas3dCheck(canvas3dInitialized));
        wrapped.getLocale().addBranchGraph(bg);
        // Set the background black in order to reduce flashing while resizing.
        // This needs to be done before the canvas gets rendered in order to
        // avoid an unnecessary re-rendering.
        wrapped.getCanvas().setBackground(Color.BLACK);
        
        try {
            canvas3dInitialized.acquire();
        } catch( InterruptedException iex ) {
            throw new RuntimeException(iex);
        }
        bg.detach();
    }
    
    private void checkViewConfiguration(Level logLevel) {
	Point3d center3d = new Point3d();
	javax.media.j3d.Transform3D tmpT3d = new javax.media.j3d.Transform3D();
	javax.media.j3d.Canvas3D[] canvses = wrapped.getViewer().getCanvas3Ds();
	
	// find farest +x in the view
	canvses[0].getImagePlateToVworld(tmpT3d);
	canvses[0].getPixelLocationInImagePlate(canvses[0].getWidth()/2, 
		canvses[0].getHeight()/2, center3d );
	tmpT3d.transform(center3d);
	double xplus = center3d.x + (canvses[0].getPhysicalWidth()/2.0d);
	for (int i=1; i<canvses.length; i++) {
	    center3d = new Point3d();
	    canvses[i].getImagePlateToVworld(tmpT3d);
	    canvses[i].getPixelLocationInImagePlate(canvses[i].getWidth()/2, 
			canvses[i].getHeight()/2, center3d );
	    tmpT3d.transform(center3d);
	    double x1 =  center3d.x + (canvses[i].getPhysicalWidth()/2.0d);
	    if (x1 > xplus) {
		xplus = x1;
	    }
	}
	
	// find farest -x in the view
	center3d = new Point3d();
	canvses[0].getImagePlateToVworld(tmpT3d);
	canvses[0].getPixelLocationInImagePlate(canvses[0].getWidth()/2, 
		canvses[0].getHeight()/2, center3d );
	tmpT3d.transform(center3d);
	double xminus = center3d.x - (canvses[0].getPhysicalWidth()/2.0d);
	for (int i=1; i<canvses.length; i++) {
	    center3d = new Point3d();
	    canvses[i].getImagePlateToVworld(tmpT3d);
	    canvses[i].getPixelLocationInImagePlate(canvses[i].getWidth()/2, 
			canvses[i].getHeight()/2, center3d );
	    tmpT3d.transform(center3d);
	    double x1 =  center3d.x - (canvses[i].getPhysicalWidth()/2.0d);
	    if (x1 < xminus) {
		xminus = x1;
	    }
	}
	preferedWidth = xplus - xminus;
	
	// find farest +y in the view
	canvses[0].getImagePlateToVworld(tmpT3d);
	canvses[0].getPixelLocationInImagePlate(canvses[0].getWidth()/2, 
		canvses[0].getHeight()/2, center3d );
	tmpT3d.transform(center3d);
	double yplus = center3d.y + (canvses[0].getPhysicalHeight()/2.0d);
	for (int i=1; i<canvses.length; i++) {
	    center3d = new Point3d();
	    canvses[i].getImagePlateToVworld(tmpT3d);
	    canvses[i].getPixelLocationInImagePlate(canvses[i].getWidth()/2, 
			canvses[i].getHeight()/2, center3d );
	    tmpT3d.transform(center3d);
	    double y1 =  center3d.y + (canvses[i].getPhysicalHeight()/2.0d);
	    if (y1 > yplus) {
		yplus = y1;
	    }
	}
	
	// find farest -y in the view
	center3d = new Point3d();
	canvses[0].getImagePlateToVworld(tmpT3d);
	canvses[0].getPixelLocationInImagePlate(canvses[0].getWidth()/2, 
		canvses[0].getHeight()/2, center3d );
	tmpT3d.transform(center3d);
	double yminus = center3d.y - (canvses[0].getPhysicalHeight()/2.0d);
	for (int i=1; i<canvses.length; i++) {
	    center3d = new Point3d();
	    canvses[i].getImagePlateToVworld(tmpT3d);
	    canvses[i].getPixelLocationInImagePlate(canvses[i].getWidth()/2, 
			canvses[i].getHeight()/2, center3d );
	    tmpT3d.transform(center3d);
	    double y1 =  center3d.y - (canvses[i].getPhysicalHeight()/2.0d);
	    if (y1 < yminus) {
		yminus = y1;
	    }
	}
	preferedHeight = yplus - yminus;
        
        StringBuffer viewConfLog = new StringBuffer("View Configuration:\n");
        viewConfLog.append("\tPreferedWidth     : " + preferedWidth + ", " + (float)wrapped.getCanvas().getPhysicalWidth() + "\n");
        viewConfLog.append("\tPreferedHeight    : " + preferedHeight + ", " + (float)wrapped.getCanvas().getPhysicalHeight() + "\n");
	for (int i=0; i<canvses.length; i++) {
	    canvses[i].getLeftManualEyeInImagePlate(center3d);
	    viewConfLog.append("\tLeftEye           : " + center3d + "\n");
	    canvses[i].getRightManualEyeInImagePlate(center3d);
	    viewConfLog.append("\tRightEye          : " + center3d + "\n");
	}
        viewConfLog.append("\tField Of View "+Math.toDegrees(wrapped.getViewer().getView().getFieldOfView())+"\n");
        logger.log(logLevel, viewConfLog.toString());
    }    
    
    /**
     * Used to add Nodes to the geometry side (as opposed to the view side)
     * of the scene graph.  This is a short cut to getting the Locale object
     * and calling that object's addBranchGraph() method.
     *
     * @param bg the BranchGroup to attach to this Universe's Locale.
     */
    private void addBranchGraph(BranchGroup bg) {
        locale.addBranchGraph( getImplementationNode(bg) );
    }
    
    /**
     * get the module controlling the cursors
     * @param the cursor control module
     */
    public CursorModule getCursorModule() {
        return cursorModule;
    }
    
    /**
     * set the viewing platform's geometry
     * @param geom the viewing platform geometry
     * @see #setViewPlatformTransform
     */
    void setPlatformGeometry( PlatformGeometry geom ) {
        wrapped.getViewingPlatform().setPlatformGeometry( geom );
    }
	
    /**
     * add a Frame3D to the universe
     * @param app the frame to add
     * @see #removeFrame3D
     */
    public void addFrame3D(org.jdesktop.lg3d.wg.Frame3D app) {
        sceneManager.addFrame3D( app );
    }
    
    /**
     * remove a Frame3D from the universe
     * @param frame the frame to remove
     * @see #addFrame3D
     */
    public void removeFrame3D(org.jdesktop.lg3d.wg.Frame3D frame) {
        sceneManager.removeFrame3D(frame);
    }
    
    /**
     * get the BranchGroup that the Scene manager uses as the root
     * @return the root BranchGroup
     */
    public BranchGroup getSceneRoot() {
        return sceneRoot;
    }

    /**
     * set the viewing platform transformation
     * REMINDER -- review the API -- should use getViewPlatformTransform()?
     * @param viewTransform the transformation of the viewing platform
     * @see #setPlatformGeometry
     */
    public void setViewPlatformTransform(org.jdesktop.lg3d.sg.Transform3D viewTransform) {
	// a lame way, but cannot access viewTransform.wrapped...
	float[] matrix = new float[16];
	viewTransform.get(matrix);
	javax.media.j3d.Transform3D j3dT3d = new javax.media.j3d.Transform3D(matrix);
        wrapped.getViewingPlatform().getViewPlatformTransform().setTransform(j3dT3d);
    }
    
    /**
     * add a listener to the event processor
     * @param listener the listener to add
     * @param evtClass the class of event to listen for
     */
    public void addListener(LgEventListener listener, Class evtClass) {
        eventProcessor.addListener( listener, evtClass );
    }
    
    /**
     * add a listener to the event processor
     * @param listener the listener to add
     * @param evtClass the class of event to listen for
     * @param source the class of the source objects to listen to
     */
    public void addListener(LgEventListener listener, Class evtClass, Class source) {
        eventProcessor.addListener( listener, evtClass, source );
    }
    
    /**
     * add a listener to the event processor
     * @param listener the listener to add
     * @param evtClass the class of event to listen for
     * @param source the source objects to listen to
     */
    public void addListener(LgEventListener listener, Class evtClass, LgEventSource source) {
        eventProcessor.addListener( listener, evtClass, source );
    }
    
    /**
     * Remove the listener
     * @param listener the listener to add
     * @param evtClass the class of the event to listen for
     */
    public void removeListener(LgEventListener listener, Class evtClass ) {
        eventProcessor.removeListener( listener, evtClass );
    }

    /**
     * Remove the listener
     * @param listener LgEventListener the listener to add
     * @param evtClass the class of the event to listen for
     * @param source the class of the source to listen to
     */    
    public void removeListener(LgEventListener listener, Class evtClass, Class sourceClass ) {        
        eventProcessor.removeListener( listener, evtClass, sourceClass );        
    }

    /**
     * Remove the listener
     * @param listener the listener to add
     * @param evtClass the class of the event to listen for
     * @param source the source object to listen to
     */    
    public void removeListener(LgEventListener listener, Class evtClass, Object sourceObject ) {        
        eventProcessor.removeListener( listener, evtClass, sourceObject );        
    }

    /**
     * Post an event to be processed by the event processor
     * @param event the event to post
     * @param source the source of the event
     */
    public void postEvent(LgEvent event, LgEventSource source) {
        eventProcessor.postEvent( event, source );
    }
    
    /**
     * get a unique nodeID for the specified client
     * @param clientID the id of the client
     * @return the unique nodeID for the client
     */
    public NodeID getNodeID(int clientID) {
        if (clientID == Integer.MIN_VALUE)
            throw new RuntimeException("Invalid request, clientID is undefined");
        
        return new NodeID(clientID);       
    }
    
    /**
     * set the cursor module
     * @param cursorModule the new CursorModule
     * @see #addCursor3D
     * @see #removeCursor3D
     */
    public void setCursorModule(CursorModule cursorModule) {
        this.cursorModule = cursorModule;
        
        if (platformGeometry == null) createPlatformGeometry();
        
        cursorRoot = new BranchGroup();
        cursorRoot.setCapability( BranchGroup.ALLOW_CHILDREN_EXTEND );
        cursorRoot.setCapability( BranchGroup.ALLOW_CHILDREN_READ );
        cursorRoot.setCapability( BranchGroup.ALLOW_CHILDREN_WRITE );
        cursorRoot.setPickable(false);

        cursorModule.setModuleRoot(cursorRoot);
        platformGeometry.addChild( cursorRoot );
       
	try {
	    fwsMethodSetCursorModule.invoke(fws, cursorModule);
	} catch (Exception ex) {
	    logger.log(Level.SEVERE, "Failed to attach cursor module to foundation window system: ", ex);
            ex.printStackTrace();
	    throw new RuntimeException("Failed to attach cursor module to foundation window system: "
		+ ": " + ex);
	}
    }
    
    public void setGestureModule(GestureModule gestureModule) {
        if (platformGeometry==null) createPlatformGeometry();
        
        BranchGroup gestureRoot = new BranchGroup();
        gestureRoot.setCapability( BranchGroup.ALLOW_CHILDREN_EXTEND );
        gestureRoot.setCapability( BranchGroup.ALLOW_CHILDREN_READ );
        gestureRoot.setCapability( BranchGroup.ALLOW_CHILDREN_WRITE );
        gestureRoot.setPickable(false);

        gestureModule.setModuleRoot(gestureRoot);
        platformGeometry.addChild( gestureRoot );
    }

    private void createPlatformGeometry() {
        assert( platformGeometry==null );
        
        PlatformGeometry j3dPG = new PlatformGeometry();
        j3dPG.setPickable(false);
        platformGeometry = new BranchGroup();
        platformGeometry.setCapability( BranchGroup.ALLOW_CHILDREN_EXTEND );
        platformGeometry.setCapability( BranchGroup.ALLOW_CHILDREN_READ );
        platformGeometry.setCapability( BranchGroup.ALLOW_CHILDREN_WRITE );
        platformGeometry.setCapability( BranchGroup.ALLOW_DETACH );
                
        j3dPG.addChild( getImplementationNode(platformGeometry) );
        
        wrapped.getViewingPlatform().setPlatformGeometry( j3dPG );        
    }
        
    /**
     * add a cursor to the cursor module
     * @param cursor the cursor to add
     * @see #setCursorModule
     * @see #removeCursor3D
     */
    public void addCursor3D(org.jdesktop.lg3d.wg.Cursor3D cursor) {
        cursorModule.addCursor(cursor);
    }
    
    /**
     * remove a cursor from the cursor module
     * @param cursor the cursor to remove
     * @see #setCursorModule
     * @see #addCursor3D
     */
    public void removeCursor3D(org.jdesktop.lg3d.wg.Cursor3D cursor) {
        cursorModule.removeCursor(cursor);
    }
    
    /**
     * Return the branch group on which to attach cursors
     */
    public BranchGroup getCursorRoot () {
	return cursorRoot;
    }

    /**
     * get the  underlying Java 3D node. This is an implementation
     * specific call and should only be made by the display server
     * @param lgBg the LGBranchGroup to get the Java3D version of
     * @return the Java3D version of the LGBranchGroup
     */
    private javax.media.j3d.BranchGroup getImplementationNode(BranchGroup lgBg) {
        Object obj;
        SceneGraphObjectWrapper w = lgBg.getWrapped();
        javax.media.j3d.BranchGroup ret = null;

        while (ret == null) {
            obj = w.getWrapped();
            if (obj == null)
                throw new RuntimeException("Can't resolve implementation node");
            else if (obj instanceof SceneGraphObjectWrapper)
                w = (SceneGraphObjectWrapper)obj;
            else if (obj instanceof javax.media.j3d.BranchGroup)
                ret = (javax.media.j3d.BranchGroup)obj;
        }

        return ret;
    }
    
    
    /**
     * Behaviour to check if the Canvas3D has been rendered
     * to ensure the FWS is working correctly
     */
    class Canvas3dCheck extends Behavior {
        
	/** Is the canvas running? */
        private Semaphore canvas3dRunning;
        
	/**
	 * Create a new checker for the Canvas3D
	 * @param canvas3DRunning is the canvas3D running?
	 */
        public Canvas3dCheck(Semaphore canvas3dRunning) {
            this.canvas3dRunning = canvas3dRunning;
            setSchedulingBounds(new javax.media.j3d.BoundingSphere(new Point3d(), Double.POSITIVE_INFINITY));
        }
        
	/**
	 * initialize the Canvas3DCheck, setting the wakeup condition
	 * to wake up after 1 frame has been drawn
	 */
        public void initialize() {
            wakeupOn(new WakeupOnElapsedFrames(1));
        }
       
	/**
	 * release the semaphore when the frame has been successfully rendered
	 * @param en (not used)
	 */
        public void processStimulus(Enumeration en) {
            canvas3dRunning.release();
        }
    }

    public static final int PRINT_SCENE_ROOT_NODE                 = 0;
    public static final int PRINT_SCENE_ROOT_NODE_WRAPPED         = 1;
    public static final int PRINT_SCENE_ROOT_NODE_WRAPPED_WRAPPED = 2;

    private void printSceneNode (int sgWhich, SceneGraphObject sgo, int level) {
	Object printObj = null;

	switch (sgWhich) {
	case PRINT_SCENE_ROOT_NODE:
	    printObj = sgo;
	    break;
	case PRINT_SCENE_ROOT_NODE_WRAPPED:
            if (sgo == null) break;
            printObj = sgo.getWrapped();
	    break;
	case PRINT_SCENE_ROOT_NODE_WRAPPED_WRAPPED:
            if (sgo == null) break;
	    SceneGraphObjectWrapper wrapped = (SceneGraphObjectWrapper) sgo.getWrapped();
	    if (wrapped == null) break;
            printObj = wrapped.getWrapped();
	    break;
	}
	
	System.err.print(level + ": ");
	for (int i = 0; i < level; i++) {
	    System.err.print("    ");
	}
        System.err.println(printObj);

	if ((sgo instanceof org.jdesktop.lg3d.sg.Group)) {
	    if (((org.jdesktop.lg3d.sg.Group)sgo).getCapability(org.jdesktop.lg3d.sg.Group.ALLOW_CHILDREN_READ)) {
		Enumeration e = ((org.jdesktop.lg3d.sg.Group)sgo).getAllChildren();
		if (e != null) {
		    while (e.hasMoreElements()) {
			SceneGraphObject child = (org.jdesktop.lg3d.sg.SceneGraphObject) e.nextElement();
			printSceneNode(sgWhich, child, level+1);
		    }
		}
	    } else {
		System.err.println("*** Can't Read Group Children ***");
	    }
	}
    }

    public void printScene (int sgWhich) {

	System.err.print("*************** Scene Graph ");
	switch (sgWhich) {
	case PRINT_SCENE_ROOT_NODE:
	    System.err.println("Nodes");
	    break;
	case PRINT_SCENE_ROOT_NODE_WRAPPED:
	    System.err.println("Node Wrappeds");
	    break;
	case PRINT_SCENE_ROOT_NODE_WRAPPED_WRAPPED:
	    System.err.println("Node Wrapped.Wrappeds");
	    break;
	}

	printSceneNode(sgWhich, sceneRoot, 0);
    }

    public void printSceneGraph () {
	System.err.println("------------------------------------------------------------");
	printScene(PRINT_SCENE_ROOT_NODE);
	System.err.println("------------------------------------------------------------");
	printScene(PRINT_SCENE_ROOT_NODE_WRAPPED);
	System.err.println("------------------------------------------------------------");
	printScene(PRINT_SCENE_ROOT_NODE_WRAPPED_WRAPPED);
	System.err.println("------------------------------------------------------------");
    }
    
    public void printSceneNode (javax.media.j3d.SceneGraphObject sgo, int level) {
	//System.err.println("Enter printSceneNode");
	Object printObj = sgo;

	System.err.print(level + ": ");
	for (int i = 0; i < level; i++) {
	    System.err.print("    ");
	}
        System.err.println(printObj);

	/* For debug
	if (sgo instanceof javax.media.j3d.TransformGroup) {
	    javax.media.j3d.TransformGroup tg = (javax.media.j3d.TransformGroup) sgo;
	    javax.media.j3d.Transform3D t3d = new javax.media.j3d.Transform3D();
	    tg.getTransform(t3d);
	    for (int i = 0; i < level+1; i++) {
		System.err.print("    ");
	    }
	    System.err.println("xform = " + t3d);
	}
	*/

	if (sgo instanceof javax.media.j3d.Group) {
	    javax.media.j3d.Group group = (javax.media.j3d.Group) sgo;
	    //System.err.println("group = " + group);
	    if (group.getCapability(javax.media.j3d.Group.ALLOW_CHILDREN_READ)) {
		//System.err.println("group = " + group);
		Enumeration e = group.getAllChildren();
		if (e != null) {
		    while (e.hasMoreElements()) {
			javax.media.j3d.SceneGraphObject child = 
			    (javax.media.j3d.SceneGraphObject) e.nextElement();
			printSceneNode(child, level+1);
		    }
		}
	    } else {
		System.err.println("*** Can't Read Group Children ***");
	    }
	}
    }

    public void printWonderlandSceneGraph () {
	org.jdesktop.lg3d.wg.Java3DGraph j3dGraph = 
	    (org.jdesktop.lg3d.wg.Java3DGraph) sceneRoot.getChild(1);
	//System.err.println("j3dGraph = " + j3dGraph);
	org.jdesktop.lg3d.wg.internal.j3d.j3dnodes.Java3DGraph j3dGraphNode =
	    (org.jdesktop.lg3d.wg.internal.j3d.j3dnodes.Java3DGraph) 
	    j3dGraph.getWrapped().getWrapped();
	//System.err.println("j3dGraphNode = " + j3dGraphNode);
	javax.media.j3d.Group j3dGroup = (javax.media.j3d.Group) j3dGraphNode.getChild(1);
	//System.err.println("j3dGroup = " + j3dGroup);

	printSceneNode(j3dGroup, 0);
    }
    
    class TestDistanceComparator implements Comparator {

        private javax.media.j3d.Transform3D trans1;
        private javax.media.j3d.Transform3D trans2;
        private Point3d p1;
        private Point3d p2;
        
        /** Creates a new instance of BoundsZDistanceComparator
         * Sorts objects on the z of the center of the bounds 
         */
        public TestDistanceComparator() {
            trans1 = new javax.media.j3d.Transform3D();
            trans2 = new javax.media.j3d.Transform3D();
            p1 = new Point3d();
            p2 = new Point3d();
        }

        /**
         * Compares its two arguments for order. Returns a negative integer, zero, 
         * or a positive integer as the first argument is less than (closer to the viewer), 
         * equal to, or greater than (further from the viewer) the second argument.
         *
         * The compare method will be called with 2 objects of type
         * TransparencySortGeom and it's result should indicate which object is
         * closer to the viewer. Object1 < Object2 if it is to be considered closer
         * and rendered after.
         *
         * @param o1 TransparencySortGeom object 1
         * @param o2 TransparencySortGeom object 2
         * 
         */
        public int compare(Object o1, Object o2) {
            TransparencySortGeom t1 = (TransparencySortGeom)o1;
            TransparencySortGeom t2 = (TransparencySortGeom)o2;
            
            t1.getLocalToVWorld(trans1);
            t2.getLocalToVWorld(trans2);
            p1.set(0,0,0);
            p2.set(0,0,0);
            trans1.transform(p1);
            trans2.transform(p2);
            
            double f = p1.z - p2.z;
            if (f < 0) {
                return 1;
            }
            if (f == 0) {
                return 0;
            }
            return -1;
        }
    }
    
    class MemoryMonitor extends PerformanceMonitor {
        private MemoryMXBean memoryBean;
        private Logger perfLogger = Logger.getLogger("lg.performance.memory");
       
        public MemoryMonitor() {
            memoryBean = ManagementFactory.getMemoryMXBean();
        }
        
        public void report() {
            perfLogger.info("Heap "+memoryBean.getHeapMemoryUsage());
        }
    }
    
    class CompilationMonitor extends PerformanceMonitor {
        private CompilationMXBean monitorBean;
        private Logger perfLogger = Logger.getLogger("lg.performance.compilation");
       
        public CompilationMonitor() {
            monitorBean = ManagementFactory.getCompilationMXBean();
        }
        
        public void report() {
            perfLogger.info("TotalCompilationTime "+monitorBean.getTotalCompilationTime());
        }
    }
    
    public boolean isMonitorBehaviorRequired() {
        
        Logger log1 = Logger.getLogger("lg.performance.memory");
        Logger log2 = Logger.getLogger("lg.performance.compilation");
        Logger log3 = Logger.getLogger("lg.performance.fps");
        
        if (log1.getLevel()!=null && log1.getLevel()!=Level.OFF ||
            log2.getLevel()!=null && log2.getLevel()!=Level.OFF ||
            log3.getLevel()!=null && log3.getLevel()!=Level.OFF )
            return true;

        return false;
    }
        
    class PerformanceMonitorBehavior extends Behavior {
        
        private WakeupCondition wakeup = new WakeupOnElapsedFrames(0);
        
        private long shortestFrameInterval = Long.MAX_VALUE;
        private long longestFrameIntercal = 0;
        private int frameCount = 0;
        private long startTime;
        private long lastFrameStart = 0;
        private Logger perfLogger = Logger.getLogger("lg.performance.fps");
        
        private final static long reportInterval = 10 * 1000000000L; // Every 10 seconds
        private long nextReportTime=0;
        
        private ArrayList<PerformanceMonitor> monitors = new ArrayList();
        
        private boolean reportFPS = false;
        
        public PerformanceMonitorBehavior() {
            setSchedulingBounds(new BoundingSphere(new Point3d(), Double.POSITIVE_INFINITY));
            
            if (Logger.getLogger("lg.performance.memory").getLevel()!=Level.OFF)
                monitors.add(new MemoryMonitor());
            if (Logger.getLogger("lg.performance.compilation").getLevel()!=Level.OFF)
                monitors.add(new CompilationMonitor());           
            if (Logger.getLogger("lg.performance.fps").getLevel()!=Level.OFF)
                reportFPS = true;
        }
        
        public void initialize() {
            wakeupOn(wakeup);
        }
        
        /**
         * Returns true if the performance monitor behavior is required
         */
        
        public void processStimulus(Enumeration e) {
            frameCount++;
            
            long t = System.nanoTime();
            
            if (lastFrameStart!=0) {
                long diff = t-lastFrameStart;
                if (diff>longestFrameIntercal)
                    longestFrameIntercal = diff;
                if (diff<shortestFrameInterval)
                    shortestFrameInterval = diff;
                if (t>nextReportTime) {
                    if (reportFPS)
                        perfLogger.info("Frame times Max : "+(longestFrameIntercal/1000000L)+"ms  Min : "+(shortestFrameInterval/1000000L)+"ms  Avg : "+((t-startTime)/1000000L/frameCount)+"ms  Avg fps : "+(frameCount/((t-startTime)/1000000000f)));
                    longestFrameIntercal = 0;
                    shortestFrameInterval = Long.MAX_VALUE;
                    nextReportTime = t+reportInterval;
                    
                    for(PerformanceMonitor monitor : monitors)
                        monitor.report();
                }
            } else {
                startTime = System.nanoTime();
//                wakeup = new WakeupOnElapsedFrames(0);
                nextReportTime = t+reportInterval;
            }
            lastFrameStart = t;
            
            wakeupOn(wakeup);
        }
        
    }
    
    abstract class PerformanceMonitor {
        public abstract void report();
    }
}
