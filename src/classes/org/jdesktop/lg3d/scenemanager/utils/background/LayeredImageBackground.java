/**
 * Project Looking Glass
 *
 * $RCSfile: LayeredImageBackground.java,v $
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
 * $Date: 2006-08-16 23:50:17 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.background;


import org.jdesktop.lg3d.scenemanager.utils.SceneControl;
import org.jdesktop.lg3d.scenemanager.utils.appcontainer.AppContainer;
import org.jdesktop.lg3d.scenemanager.utils.event.ScreenResolutionChangedEvent;
import org.jdesktop.lg3d.sg.Shape3D;
import org.jdesktop.lg3d.utils.c3danimation.NaturalMotionAnimation;
import org.jdesktop.lg3d.utils.shape.ImagePanel;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Toolkit3D;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventConnector;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import java.net.URL;


public class LayeredImageBackground extends Background {
    // Set this to false to disable swaying of the background
    // TODO: eventually this should be in a config file
    private final boolean sway = true;
    private final float backgroundFarest = -10.0f; // FIXME
    private final float logoZPos = -5.0f;
    private final float logoSize = 0.6f;
    
    private SceneControl scenemanager;
    private float initWidth;
    private float initHeight;
    private URL[] filenames;
    private float[][] layerHint;
    private Component3D[] bgPanelComps;
    private WindowRotator appContRoot;
    private JavaLogo logo;
    
    public LayeredImageBackground(URL[] filenames, float[][] layerHint) {
        if (filenames == null || layerHint == null) {
            throw new IllegalArgumentException("null argument");
        }
        if (filenames.length != layerHint.length ) {
            throw new IllegalArgumentException("inconsistency in arguments");
        }
        for (float[] hint : layerHint) {
            if (hint.length != 3) {
                throw new IllegalArgumentException(
                    "array dimention is incorrect: " + layerHint);
            }
        }
        this.filenames = filenames;
        this.layerHint = layerHint;
        
        setName("LayeredImageBackground");
    }
    
    public void initialize(SceneControl scenemanager) {
        if (this.scenemanager != null) {
            // the visual has been initialized already
            // just update the AppContainerControl
            this.scenemanager = scenemanager;
            return;
        }
        this.scenemanager = scenemanager;
        Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
        float width = toolkit3d.getScreenWidth();
        float height = toolkit3d.getScreenHeight();
        initWidth = width;
        initHeight = height;
        
        float fov = toolkit3d.getFieldOfView();
        float dist = backgroundFarest * 0.9f;
	float fovTan2 = (float)Math.tan(fov * 0.5f) * 2.0f;
	float bgWidth = (width  - dist * fovTan2) * 1.1f;
	float bgHeight = (height - dist * fovTan2 * height / width) * 1.1f;
        
        // Setup the Java logo
        logo = new JavaLogo();
        addChild(logo);
        
        /*
         * this -+--> WindowSwayer -> appContRoot(SceneRotator) -> AppContainer
         *       |
         *       +-*> BackgroundSwayer -> SceneTmpZoomer -> bgPanelComps[] -> bgPanel
         */
        appContRoot = new WindowRotator(logo);
        WindowSwayer wSwayer = new WindowSwayer(this, 1.0f, 0.05f);
        wSwayer.addChild(appContRoot);
        addChild(wSwayer);
        
        // Setup the layered background images
        bgPanelComps = new Component3D[filenames.length];
        
        for (int i = 0; i < filenames.length; i++) {
            URL file = filenames[i];
            Shape3D bgPanel = null;
            try {
                bgPanel = new ImagePanel(file, bgWidth, bgHeight * layerHint[i][0], false);
            } catch (Exception e) {
                throw new RuntimeException(
		    "failed to initialize background: " + file + ": " + e);
            }
            logger.fine("Background image loaded: " + file);
            
            bgPanelComps[i] = new Component3D();
            bgPanelComps[i].setAnimation(new NaturalMotionAnimation(2000));
            bgPanelComps[i].setTranslation(0.0f, bgHeight * layerHint[i][1], 0.0f);
            bgPanelComps[i].addChild(bgPanel);
            
            SceneTempZoomer zoomer = new SceneTempZoomer(logo, 1.0f - 0.25f * i);
            zoomer.addChild(bgPanelComps[i]);
            
            float swayLevel = layerHint[i][2];
            if (!sway) {
                swayLevel = 0.0f;
            }
            BackgroundSwayer swayer 
                    = new BackgroundSwayer(this, 
                                    dist * 0.2f, dist * (0.8f - 0.05f * i), swayLevel);
            swayer.addChild(zoomer);
            addChild(swayer);
        }
        
        changeSize(width, height);
        
        // Listen for handling the window size change
        LgEventConnector.getLgEventConnector().addListener(
            LgEventSource.ALL_SOURCES,
            new LgEventListener() {
                public void processEvent(final LgEvent event) {
                    ScreenResolutionChangedEvent csce = (ScreenResolutionChangedEvent)event;
                    changeSize(csce.getWidth(), csce.getHeight());
                }
                public Class<LgEvent>[] getTargetEventClasses() {
                    return new Class[] {ScreenResolutionChangedEvent.class};
                }
            });
    }
    
    public void setEnabled(boolean enabled) {
        if (enabled) {
            activate();
        } else {
            deactivate();
        }
    }
    
    private void activate() {
        Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
        float width = toolkit3d.getScreenWidth();
        float height = toolkit3d.getScreenHeight();
        changeSize(width, height);
        
        for (Component3D comp : bgPanelComps) {
            float scale = comp.getFinalScale();
            comp.setScale(scale * 3.0f);
            comp.changeScale(scale, 2000);
        }
        
        int current = scenemanager.getCurrentAppContainer();
        AppContainer ac = scenemanager.getAppContainer(current);
        appContRoot.addChild(ac);
    }
    
    private void deactivate() {
    }
    
    private void changeSize(float width, float height) {
        Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
        float fov = toolkit3d.getFieldOfView(width);
        float dist = backgroundFarest * 0.9f;
        
	float fovTan2 = (float)Math.tan(fov * 0.5f) * 2.0f;
	float bgWidth = (width  - dist * fovTan2) * 1.1f;
	float bgHeight = (height - dist * fovTan2 * height / width) * 1.1f;
        
        float scaleW = width / initWidth;
        float scaleH = height / initHeight;
        float scale = (scaleW > scaleH)?(scaleW):(scaleH);
        
        for (int i = 0; i < bgPanelComps.length; i++) {
            bgPanelComps[i].changeScale(scale, 200);
            bgPanelComps[i].changeTranslation(0.0f, bgHeight * layerHint[i][1], 0.0f, 200);
        }
        
        logo.reposition(logoSize, width, height, logoZPos, fovTan2);
    }
}
