/**
 * Project Looking Glass
 *
 * $RCSfile: SwayingSimpleImageBackground.java,v $
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
 * $Revision: 1.20 $
 * $Date: 2006-08-15 20:09:48 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.background;


import java.net.URL;
import org.jdesktop.lg3d.sg.Shape3D;
import org.jdesktop.lg3d.scenemanager.utils.SceneControl;
import org.jdesktop.lg3d.scenemanager.utils.appcontainer.AppContainer;
import org.jdesktop.lg3d.scenemanager.utils.event.ScreenResolutionChangedEvent;
import org.jdesktop.lg3d.utils.c3danimation.NaturalMotionAnimation;
import org.jdesktop.lg3d.utils.shape.ImagePanel;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Toolkit3D;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventConnector;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.wg.event.LgEventSource;


/**
 * A swaying simple image background implementation,
 */
public class SwayingSimpleImageBackground extends Background {
    private final float backgroundFarest = -10.0f; // FIXME
    private final float logoZPos = -5.0f;
    private final float logoSize = 0.6f;
    private final float swayLevel = 1.0f; // TODO move this to constructor
    
    private float initWidth;
    private float initHeight;
    private URL urlFilename;
    private SceneControl scenemanager;
    private Component3D bgPanelComp;
    private Component3D wSwayer;
    private JavaLogo logo;
    
     public SwayingSimpleImageBackground(URL urlFilename) {
        if (urlFilename == null) {
            throw new IllegalArgumentException("null argument");
        }
        this.urlFilename = urlFilename;
        
        setName("SwayingSimpleImageBackground");
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
        initWidth = toolkit3d.getScreenWidth();
        initHeight = toolkit3d.getScreenHeight();
        
        float dist = backgroundFarest * 0.9f;
        float fov = toolkit3d.getFieldOfView();
        float fovTan2 = (float)Math.tan(fov * 0.5f) * 2.0f;
        float bgWidth = (initWidth  - dist * fovTan2) * 1.1f;
        float bgHeight = (initHeight - dist * fovTan2 * initHeight / initWidth) * 1.1f;
        
        Shape3D bgPanel = null;
        try {
            bgPanel = new ImagePanel(urlFilename, bgWidth, bgHeight, false);
        } catch (Exception e) {
            throw new RuntimeException("failed to initialize background: " + e);
        }
        logger.fine("Background image loaded: " + urlFilename);
        
        bgPanelComp = new Component3D();
        bgPanelComp.setAnimation(new NaturalMotionAnimation(2000));
        // Do the following before adding to the scenegraph in order to
        // avoid flushing.
        bgPanelComp.setScale(3.0f);
        bgPanelComp.addChild(bgPanel);
        
        // Setup the Java logo
        logo = new JavaLogo();
        addChild(logo);
        
        SceneTempZoomer zoomer = new SceneTempZoomer(logo, 1.0f);
        zoomer.addChild(bgPanelComp);
        
        Component3D swayer 
                = new BackgroundSwayer(this, dist * 0.2f, dist * 0.8f, swayLevel);
        swayer.addChild(zoomer);
        addChild(swayer);
        
        Component3D rotator = new WindowRotator(logo);
        wSwayer = new WindowSwayer(this, 1.0f, 0.05f);
        wSwayer.addChild(rotator);
        addChild(wSwayer);
        
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
    
    private void changeSize(float width, float height) {
        float fov = Toolkit3D.getToolkit3D().getFieldOfView(width);
        float dist = backgroundFarest * 0.9f;
        
        float fovTan2 = (float)Math.tan(fov * 0.5f) * 2.0f;
        float bgWidth = (width  - dist * fovTan2) * 1.1f;
        float bgHeight = (height - dist * fovTan2 * height / width) * 1.1f;
        
        float scaleW = width / initWidth;
        float scaleH = height / initHeight;
        float scale = (scaleW > scaleH)?(scaleW):(scaleH);
        
        bgPanelComp.changeScale(scale, 200);
        
        logo.reposition(logoSize, width, height, logoZPos, fovTan2);
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
        
        float scale = bgPanelComp.getFinalScale();
        bgPanelComp.setScale(scale * 3.0f);
        bgPanelComp.changeScale(scale * 1.0f);
        
        int current = scenemanager.getCurrentAppContainer();
        AppContainer ac = scenemanager.getAppContainer(current);
        wSwayer.addChild(ac);
    }
    
    private void deactivate() {
    }
}
