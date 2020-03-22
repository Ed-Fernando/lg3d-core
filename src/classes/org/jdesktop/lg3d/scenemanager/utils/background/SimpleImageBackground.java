/**
 * Project Looking Glass
 *
 * $RCSfile: SimpleImageBackground.java,v $
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
 * $Revision: 1.18 $
 * $Date: 2006-05-29 14:09:55 $
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
 * A simple example background implementation, which demonstrates a few
 * key points for implementing a background.
 */
public class SimpleImageBackground extends Background {
    private final float backgroundFarest = -10.0f; // FIXME
    private SceneControl scenemanager;
    private Component3D bgPanelComp;
    private URL urlFilename;
    private float initWidth;
    private float initHeight;
    
    /**
     * The constructor.  As it can be invoked while the system is starting
     * up and SceneManager is not fully initialized, it is recommended
     * not to do significant work in the constructor.  Once the SceneManager
     * gets fully up, the initialize() method will be invoked, where 
     * the main initialization of the background will be taken place.
     */
    public SimpleImageBackground(URL urlFilename) {
        if (urlFilename == null) {
            throw new IllegalArgumentException("null argument");
        }
        this.urlFilename = urlFilename;
        
        setName("SimpleImageBackground");
    }
    
    /**
     * Invoked by the SceneManager (possibly indirectly) only once in
     * the lifecycle of this object in order to full initialize it. 
     * Note that this background object will be attached to the scenegraph
     * when it gets activated after the activate() invocation.  Note that
     * when reuse of the same background object occurs, only the activate()
     * method is reinvoked.
     */
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
	bgPanelComp.setTranslation(0.0f, 0.0f, dist);
	bgPanelComp.addChild(bgPanel);
        
        // Do the following before adding to the scenegraph in order to
        // avoid flushing.
        bgPanelComp.setScale(3.0f);
        addChild(bgPanelComp);
        
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
        float scaleW = width / initWidth;
        float scaleH = height / initHeight;
        float scale = (scaleW > scaleH)?(scaleW):(scaleH);
        
        bgPanelComp.changeScale(scale, 200);
    }
    
    /**
     * Make the background ready to use.  Especially, sets up the
     * AppContainers. This method is invoked before this background gets
     * activated by being attached to the scenegraph.  When the same background
     * object is reused, e.g., for switching multiple backgrounds back and
     * forth, only the activate() method is invoked (the initialize() method
     * will only invoked once). 
     * TODO -- do we need deactivate() method?
     * It is background's responsibility to attach the current AppContainer
     * to the scenegraph.  Since this background object will be attached to
     * the scenegraph, it can be done by simply attaching the AppContainer to
     * this object.  A background is allowed to position the AppContainer 
     * wherever it desires.  Actually one background can host multiple 
     * AppContainers and position them in the way it likes.  It can initiate
     * switching active AppContainer by invoking SceneManager's 
     * setCurrentAppContainer() method.
     * In this simple example, only one AppContainer is obtained and it is
     * left at the default position, which is (0,0,0).
     */
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
        addChild(ac);
    }
    
    private void deactivate() {
    }
}
