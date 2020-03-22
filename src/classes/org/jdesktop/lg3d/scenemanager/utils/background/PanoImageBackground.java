/**
 * Project Looking Glass
 *
 * $RCSfile: PanoImageBackground.java,v $
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
 * $Revision: 1.32 $
 * $Date: 2006-08-15 20:09:48 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.background;


import java.net.URL;
import java.util.ArrayList;
import javax.vecmath.Vector3f;

import org.jdesktop.lg3d.scenemanager.utils.SceneControl;
import org.jdesktop.lg3d.scenemanager.utils.appcontainer.AppContainer;
import org.jdesktop.lg3d.scenemanager.utils.event.ScreenResolutionChangedEvent;
import org.jdesktop.lg3d.scenemanager.utils.taskbar.Taskbar;
import org.jdesktop.lg3d.sg.Appearance;
import org.jdesktop.lg3d.sg.Shape3D;
import org.jdesktop.lg3d.utils.action.ActionNoArg;
import org.jdesktop.lg3d.utils.c3danimation.NaturalMotionAnimation;
import org.jdesktop.lg3d.utils.eventadapter.GenericEventAdapter;
import org.jdesktop.lg3d.utils.eventadapter.MouseClickedEventAdapter;
import org.jdesktop.lg3d.utils.shape.Disc;
import org.jdesktop.lg3d.utils.shape.FuzzyEdgePanel;
import org.jdesktop.lg3d.utils.shape.PickableRegion;
import org.jdesktop.lg3d.utils.shape.SimpleAppearance;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Cursor3D;
import org.jdesktop.lg3d.wg.Toolkit3D;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventConnector;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.MouseButtonEvent3D;


public class PanoImageBackground extends Background {
    private final float backgroundFarest = -10.0f; // FIXME
    private final float bgImageGap = 0.01f;
    private final float bgMag = 1.1f;
    private final float acMag = 1.3f;
    private final float logoZPos = -5.0f;
    private final float logoSize = 0.6f;
    private final float swayLevel = 1.0f; // TODO move this to constructor
    
    private SceneControl scenemanager;
    private float initWidth;
    private float initHeight;
    private float width;
    private float bgWidth;
    private URL [] filename;
    private BackgroundSwayer swayer;
    private Component3D bgPanelSet;
    private SceneTempZoomer zoomer;
    private Component3D bgPanelGroup;
    private ArrayList<BgPanelComp> bgPanelList;
    private ArrayList<AppContWrapper> appContList;
    private Component3D appContGroup;
    private JavaLogo logo;
    private Component3D leftEdgeSpot;
    private Component3D rightEdgeSpot;
    
    private int initBackground;
    private boolean overviewMode;
    private float bgScale = 1.0f;
    
    public PanoImageBackground(URL [] filename, int initBacground) {
        if (filename == null) {
            throw new IllegalArgumentException("the filename argument cannot be null");
        }
        for (URL fn : filename) {
            if (fn == null) {
                throw new IllegalArgumentException(
                     "one of string in the filename argument is null");
            }
        }
        if (initBackground < 0 || initBackground >= filename.length) {
            throw new IllegalArgumentException(
                     "initBackground out of range");
        }
        this.filename = filename;
	this.initBackground = initBacground;
        
        setName("PanoImageBackground");
    }
    
    public void initialize(final SceneControl scenemanager) {
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
        
        // Setup the Java logo and appContGroup.
        // Do the followings before invoking setupKeyScreenParams()
        logo = new JavaLogo();
        addChild(logo);
        appContGroup = new Component3D();
        appContGroup.setAnimation(new NaturalMotionAnimation(500));
        
        setupKeyScreenParams(initWidth, initHeight);
        float dist = backgroundFarest * 0.9f;
        float bgHeight = bgWidth * initHeight / initWidth;
        
        // Set up the hot spots on the both sides of the screen
        leftEdgeSpot = new EdgeHotSpot(true, bgWidth, bgHeight, dist);
        leftEdgeSpot.setVisible(false);
        addChild(leftEdgeSpot);
        
        rightEdgeSpot = new EdgeHotSpot(false, bgWidth, bgHeight, dist);
        rightEdgeSpot.setVisible(false);
        addChild(rightEdgeSpot);
        
        /*
         * this -+-> appContGroup -*> AppContWrapper(rotator) -> AppContainer
         *       |
         *       +-> BackgroundSwayer -> bgPanelSet 
         *                                  -> SceneTmpZoomer -> bgPanelGroup
         *                                        -*> bgPanelComp -> bgPanel
         */
        // Set up the background
        bgPanelList = new ArrayList<BgPanelComp>();
        
        for (int i = 0; i < filename.length; i++) {
            Shape3D bgPanel = null;
            URL fullname = filename[i];
            try {
                SimpleAppearance app
                    = new SimpleAppearance(
                        1.0f, 1.0f, 1.0f, 1.0f,
                        SimpleAppearance.ENABLE_TEXTURE
                        | SimpleAppearance.NO_GLOSS);
                app.setTexture(fullname);
                bgPanel
                    = new FuzzyEdgePanel(
                        bgWidth * (1.0f + bgImageGap * 0.5f), bgHeight,
                        bgWidth * bgImageGap,
                        bgWidth * bgImageGap * 0.1f,
                        bgWidth * bgImageGap,
                        bgWidth * bgImageGap * 0.1f,
                        app);
                BgPanelComp bgc = new BgPanelComp(i);
                bgc.setMouseEventSource(MouseButtonEvent3D.class, true);
                bgc.addChild(bgPanel);
                bgPanelList.add(bgc);
                logger.fine("Background image loaded: " + fullname);
            } catch (Exception e) {
                if (i == 0) {
                    throw new RuntimeException(
                            "No background file found: " + e);
                }
                if (initBackground >= i) {
                    throw new RuntimeException(
                            "Specified number of background images "
                            + initBackground
                            + " exceeds availablibilty: " + i);
                }
                break;
            }
        }
        
        bgPanelGroup = new Component3D();
        bgPanelGroup.setAnimation(new NaturalMotionAnimation(500));
        float bgXOrig = (bgPanelList.size() -1) * bgWidth * -0.5f;
        Appearance triApp = new SimpleAppearance(1.0f, 0.5f, 0.5f, 0.5f);
        for (int i = 0; i < bgPanelList.size(); i++) {
            Component3D bgc = bgPanelList.get(i);
            bgc.setTranslation(bgXOrig + bgWidth * i, 0.0f, 0.0f);
            bgPanelGroup.addChild(bgc);
            
            if (i == 0) {
                continue;
            }
            Shape3D tri1
                    = new Disc(0.5f, 3,
                    bgWidth * -0.5f, bgHeight * 0.5f, 1.0f,
                    (float)Math.toRadians(-90), triApp);
            bgc.addChild(tri1);
            Shape3D tri2
                    = new Disc(0.5f, 3,
                    bgWidth * -0.5f, bgHeight * -0.5f, 1.0f,
                    (float)Math.toRadians(90), triApp);
            bgc.addChild(tri2);
        }
        
        zoomer = new SceneTempZoomer(logo, 1.0f);
        zoomer.addChild(bgPanelGroup);
        
        bgPanelSet = new Component3D();
        bgPanelSet.setAnimation(new NaturalMotionAnimation(500));
        bgPanelSet.addChild(zoomer);
        
        swayer = new BackgroundSwayer(this, dist * 0.2f, dist * 0.8f, swayLevel);
        swayer.addChild(bgPanelSet);
        addChild(swayer);
        
        // Do the following before adding to the scenegraph in order to
        // avoid flushing.
        int current = scenemanager.getCurrentAppContainer();
        resetBackgroundPos(current);
        
        WindowSwayer wSwayer = new WindowSwayer(this, 1.0f, 0.05f);
        wSwayer.addChild(appContGroup);
        addChild(wSwayer);
        
        // Set up listeners
        LgEventConnector.getLgEventConnector().addListener(
            BgPanelComp.class,
            new MouseClickedEventAdapter(
                new ActionNoArg() {
                    public void performAction(LgEventSource source) {
                        assert(source instanceof BgPanelComp);
                        int selected = ((BgPanelComp)source).getIndex();
                        selected = bgIndexReverseAdjustment(selected);
                        returnFromOverviewMode(selected);
                    }
                }));
        
        LgEventConnector.getLgEventConnector().addListener(
            AppContWrapper.class,
            new MouseClickedEventAdapter(
                new ActionNoArg() {
                    public void performAction(LgEventSource source) {
                    assert(source instanceof AppContWrapper);
                    int selected = ((AppContWrapper)source).getIndex();
                    returnFromOverviewMode(selected);
                }
            }));
        
        LgEventConnector.getLgEventConnector().addListener(
            Taskbar.class,
            new MouseClickedEventAdapter(MouseButtonEvent3D.ButtonId.BUTTON3,
                new ActionNoArg() {
                    public void performAction(LgEventSource source) {
                        if (!overviewMode) {
                            setOverviewMode();
                        } else {
                            int current = scenemanager.getCurrentAppContainer();
                            returnFromOverviewMode(current);
                        }
                }
            }));
        
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
    
    private void changeAppContVis(boolean showAll) {
        if (showAll || overviewMode) {
            for (Component3D acw : appContList) {
                acw.setVisible(true);
            }
        } else {
            int current = scenemanager.getCurrentAppContainer();
            for (AppContWrapper acw : appContList) {
                acw.setVisible(acw.getIndex() == current);
            }
        }
    }
    
    private void setupKeyScreenParams(float w, float h) {
        float scaleW = w / initWidth;
        float scaleH = h / initHeight;
        bgScale = (scaleW > scaleH)?(scaleW):(scaleH);
        // somehow making bgScale smaller than 1.0f screws up the visual
        bgScale = (bgScale < 1.0f)?(1.0f):(bgScale);
        
        width = initWidth * bgScale;
        float fov = Toolkit3D.getToolkit3D().getFieldOfView(w);
        float dist = backgroundFarest * 0.9f;
        float fovTan2 = (float)Math.tan(fov * 0.5f) * 2.0f;
        bgWidth = (width  - dist * fovTan2) * 1.1f;
        
        logo.reposition(logoSize, w, h, logoZPos, fovTan2);
    }
    
    public void setEnabled(boolean enabled) {
        if (enabled) {
            activate();
        } else {
            deactivate();
        }
    }
    
    private void activate() {
        appContList = new ArrayList<AppContWrapper>();
        float acXOrig = (bgPanelList.size() -1) * width * -0.5f * acMag;
        for (int i = 0; i < bgPanelList.size(); i++) {
            AppContainer ac = scenemanager.getAppContainer(i);
            AppContWrapper acw = new AppContWrapper(ac, i);
            acw.setMouseEventSource(MouseButtonEvent3D.class, true);
            int adjLoc = bgIndexAdjustment(i);
            acw.setTranslation(acXOrig + width * acMag * adjLoc, 0.0f, 0.0f);
            appContGroup.addChild(acw);
            appContList.add(acw);
        }
        int current = scenemanager.getCurrentAppContainer();
        if (current >= bgPanelList.size()) {
            scenemanager.swapAppContainer(current, 0);
            current = 0;
        }
        switchBackground(current, 3.0f);
        changeAppContVis(true);
    }
    
    private void deactivate() {
    }
    
    // FIXME -- This function has many duplication of the other part 
    // of the code.  Those duplicates should be consolidated.
    private void changeSize(float w, float h) {
        setupKeyScreenParams(w, h);
        
        // reposition the background
        bgPanelSet.setScale(bgScale);
        if (overviewMode) {
            float scale = 1.0f / bgPanelList.size();
            float bpgScale = scale / (bgMag * bgMag);
            bgPanelGroup.changeScale(bpgScale * bgScale);
        } else {
            resetBackgroundPos(scenemanager.getCurrentAppContainer());
        }
        
        // reposition the app containers
        float acXOrig = (bgPanelList.size() -1) * width * -0.5f * acMag;
        for (AppContWrapper acw : appContList) {
            int adjLoc = bgIndexAdjustment(acw.getIndex());
            acw.setTranslation(acXOrig + width * acMag * adjLoc, 0.0f, 0.0f);
        }
        
        // reposition the hotspots
        Vector3f spotLoc = leftEdgeSpot.getFinalTranslation(new Vector3f());
        leftEdgeSpot.setTranslation(bgWidth * -0.4f, 0.0f, spotLoc.z);
        rightEdgeSpot.setTranslation(bgWidth * 0.4f, 0.0f, spotLoc.z);
    }
    
    private void resetBackgroundPos(int index) {
        int bgIndex = bgIndexAdjustment(index);
        float bgXOrig = (bgPanelList.size() -1) * bgWidth * -0.5f;
        bgPanelGroup.setTranslation(bgWidth * -bgIndex - bgXOrig, 0.0f, 0.0f);
        bgPanelGroup.setScale(bgScale);
        
        float acXOrig = (bgPanelList.size() -1) * width * -0.5f * acMag;
        appContGroup.setTranslation(width * acMag * -bgIndex - acXOrig, 0.0f, 0.0f);
        appContGroup.setScale(1.0f);
    }
    
    private void switchBackground(int index, float magEffect) {
        bgPanelSet.setScale(bgScale * magEffect);
        bgPanelSet.changeScale(bgScale, 3000);
        setupBackground(index);
    }
    
    private void setupBackground(int index) {
        overviewMode = false;
        
        bgPanelGroup.setMouseEventEnabled(false);
        scenemanager.setCurrentAppContainer(index);
        zoomer.setEnabled(true);
        swayer.setEnabled(true);
        for (AppContWrapper acw : appContList) {
            acw.setOverviewMode(false);
        }
        
        int bgIndex = bgIndexAdjustment(index);
        float bgXOrig = (bgPanelList.size() -1) * bgWidth * -0.5f;
        bgPanelGroup.changeTranslation(bgWidth * -bgIndex - bgXOrig, 0.0f, 0.0f, 600);
        bgPanelGroup.changeScale(bgScale, 500);
        float acXOrig = (bgPanelList.size() -1) * width * -0.5f * acMag;
        appContGroup.changeTranslation(width * acMag * -bgIndex - acXOrig, 0.0f, 0.0f, 600);
        appContGroup.changeScale(1.0f, 500);
        
        leftEdgeSpot.setVisible(bgIndex != 0);
        rightEdgeSpot.setVisible(bgIndex != (bgPanelList.size() -1));
    }
    
    private void returnFromOverviewMode(int index) {
        switchBackground(index, 1.1f);
    }
    
    private void setOverviewMode() {
        overviewMode = true;
        changeAppContVis(true);
        
        bgPanelGroup.setMouseEventEnabled(true);
        zoomer.setEnabled(false);
        swayer.setEnabled(false);
        for (AppContWrapper acw : appContList) {
            acw.setOverviewMode(true);
        }
        
        bgPanelSet.setScale(0.95f);
        bgPanelSet.changeScale(1.0f, 2000);
        
        float scale = 1.0f / bgPanelList.size();
        float bpgScale = scale / (bgMag * bgMag);
        float acScale = scale / acMag;
        bgPanelGroup.changeTranslation(0.0f, 0.0f, 0.0f, 500);
        bgPanelGroup.changeScale(bpgScale * bgScale, 500);
        appContGroup.changeTranslation(0.0f, 0.0f, 0.0f, 500);
        appContGroup.changeScale(acScale, 500);
        
        leftEdgeSpot.setVisible(false);
        rightEdgeSpot.setVisible(false);
    }
    
    private int bgIndexAdjustment(int index) {
        return 
            (index + initBackground + bgPanelList.size()) % bgPanelList.size();
    }
    
    private int bgIndexReverseAdjustment(int index) {
        return 
            (index - initBackground + bgPanelList.size()) % bgPanelList.size();
    }
    
    private void switchToNext(boolean left) {
        int current = scenemanager.getCurrentAppContainer();
        current += (left)?(-1):(1);
        int num = bgPanelList.size();
        current += num;
        current %= num;
        changeAppContVis(true);
        switchBackground(current, 0.95f);
    }
    
    private class EdgeHotSpot extends Component3D {
        private EdgeHotSpot(final boolean left, float bgWidth, float bgHeight,
                float dist) {
            // make this tall enough to cover the screen even after screen gets resized
            Shape3D region = new PickableRegion(bgWidth * 0.2f, bgHeight * 10f);
            addChild(region);
            setTranslation(bgWidth * 0.4f * (left?-1:1), 0.0f, dist * 0.9f);
            setCursor(Cursor3D.E_RESIZE_CURSOR);
            addListener(
                    new MouseClickedEventAdapter(
                    new ActionNoArg() {
                public void performAction(LgEventSource source) {
                    switchToNext(left);
                }
            }));
        }
    }
    
    private class AppContWrapper extends WindowRotator {
        private int index;
        private Component3D innerCont;
        
        private AppContWrapper(Component3D innerCont, int index) {
            super(logo);
            
            this.innerCont = innerCont;
            this.index = index;
            addChild(innerCont);
            setOverviewMode(false);
        }
        
        private int getIndex() {
            return index;
        }
        
        private void setOverviewMode(boolean inOverview) {
            if (inOverview) {
                innerCont.setMouseEventEnabled(false);
                setMouseEventSource(MouseButtonEvent3D.class, true);
            } else {
                innerCont.setMouseEventEnabled(true);
                setMouseEventSource(MouseButtonEvent3D.class, false);
            }
        }
        
        public boolean equals(Object obj) {
            AppContWrapper acw = (AppContWrapper)obj;
            
            return (innerCont.equals(acw.innerCont));
        }
    }
    
    private static class BgPanelComp extends Component3D {
        private int index;
        
        private BgPanelComp(int index) {
            this.index = index;
        }
        
        private int getIndex() {
            return index;
        }
    }
}
