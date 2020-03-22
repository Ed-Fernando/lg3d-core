/**
 * Project Looking Glass
 *
 * $RCSfile: StandardAppContainer.java,v $
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
 * $Revision: 1.29 $
 * $Date: 2007-03-19 23:56:58 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.appcontainer;


import javax.vecmath.Vector3f;

import org.jdesktop.lg3d.scenemanager.utils.background.Background;
import org.jdesktop.lg3d.scenemanager.utils.event.Component3DGestureMoveLeftEvent;
import org.jdesktop.lg3d.scenemanager.utils.event.Component3DGestureMoveRightEvent;
import org.jdesktop.lg3d.scenemanager.utils.event.Frame3DAddedEvent;
import org.jdesktop.lg3d.scenemanager.utils.event.Frame3DAnimationFinishedEvent;
import org.jdesktop.lg3d.scenemanager.utils.event.Frame3DRemovedEvent;
import org.jdesktop.lg3d.scenemanager.utils.event.ScreenResolutionChangedEvent;
import org.jdesktop.lg3d.scenemanager.utils.taskbar.DefaultThumbnail;
import org.jdesktop.lg3d.utils.action.ActionBoolean;
import org.jdesktop.lg3d.utils.action.ActionComponent3D;
import org.jdesktop.lg3d.utils.action.ActionFloat2;
import org.jdesktop.lg3d.utils.action.ActionNoArg;
import org.jdesktop.lg3d.utils.action.Component3DGroupMigrationAction;
import org.jdesktop.lg3d.utils.action.Component3DMigrationAction;
import org.jdesktop.lg3d.utils.action.GenericEventPostAction;
import org.jdesktop.lg3d.utils.action.TransparencyActionNoArg;
import org.jdesktop.lg3d.utils.actionadapter.ActionSwitcher;
import org.jdesktop.lg3d.utils.actionadapter.BooleanToNoArgConverter;
import org.jdesktop.lg3d.utils.c3danimation.AnimateToChangeVisiblePlugin;
import org.jdesktop.lg3d.utils.eventadapter.Component3DManualMoveEventAdapter;
import org.jdesktop.lg3d.utils.eventadapter.Component3DToFrontEventAdapter;
import org.jdesktop.lg3d.utils.eventadapter.Component3DVisualAppearanceEventAdapter;
import org.jdesktop.lg3d.utils.eventadapter.GenericEventAdapter;
import org.jdesktop.lg3d.utils.eventadapter.MouseClickedEventAdapter;
import org.jdesktop.lg3d.utils.eventadapter.MouseEnteredEventAdapter;
import org.jdesktop.lg3d.utils.layoutmanager.BookshelfLayout;
import org.jdesktop.lg3d.utils.layoutmanager.HallwayLayout;
import org.jdesktop.lg3d.utils.smoother.YAcceleratingVector3fSmoother;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Container3D;
import org.jdesktop.lg3d.wg.Frame3D;
import org.jdesktop.lg3d.wg.Frame3DAnimation;
import org.jdesktop.lg3d.wg.LayoutManager3D;
import org.jdesktop.lg3d.wg.Thumbnail;
import org.jdesktop.lg3d.wg.Toolkit3D;
import org.jdesktop.lg3d.wg.event.Component3DToBackEvent;
import org.jdesktop.lg3d.wg.event.Component3DToFrontEvent;
import org.jdesktop.lg3d.wg.event.Component3DVisualAppearanceEvent;
import org.jdesktop.lg3d.wg.event.Component3DVisualAppearanceEvent.VisualAppearanceType;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventConnector;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.MouseEnteredEvent3D;
import org.jdesktop.lg3d.wg.event.MouseEvent3D;


public class StandardAppContainer extends AppContainer {

    private static boolean first = true;

    private static final float zSpacing = 0.02f;
    private static final float shelfViewAngle = (float)Math.toRadians(90);
    private static final float shelfViewZ = -0.05f;
    private static final float shelfViewXSpacing = 0.02f;
    private static final float hallwayViewAngle = (float)Math.toRadians(90);
    private static final float hallwayViewPanelAngle = shelfViewAngle;
    private static final float hallwayViewZ = -0.04f;
    private static final float hallwayViewZSpacing = 0.06f;
    private static final int defaultAnimDuration = 500;
    private static final float translucencyNormal = 0.25f;
    private static final float translucencyHighlight = 0.0f;
    private static final float translucencyLowlight = 0.8f;
    private static final int translucencyAnimDuration = 750;
    private static boolean parkAtSideByMoving 
            = System.getProperty("lg.scenemanager.parkingByMoving", "false").equals("true");
    
    private Container3D shelfViewContainerL;    
    private Container3D shelfViewContainerR;
    private Container3D leftsideContainer;
    private Container3D rightsideContainer;
    private ActionSwitcher bgEventSwitch;
    private float contWidth;
    private float contHeight;
    private LayoutManager3D mainLayout;
    
    public StandardAppContainer() {
        this(zSpacing);
    }
    
    public StandardAppContainer(float spacing) {
        setName("StandardAppContainer");
	mainLayout = new ZLayeredMovableLayout(spacing);
    }
    
    public StandardAppContainer(LayoutManager3D layout) {
        setName("StandardAppContainer");
	mainLayout = layout;
    }
    
    public void initialize() {
        final Container3D mainContainer = this;
        Container3D deco = new Container3D();
        Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
        contWidth = toolkit3d.getScreenWidth();
        contHeight = toolkit3d.getScreenHeight();
        
        mainContainer.setPreferredSize(new Vector3f(contWidth, contHeight, 0.0f/*FIXME*/));
        mainContainer.setLayout(mainLayout);
        
        shelfViewContainerL = new Container3D();
        shelfViewContainerL.setPreferredSize(new Vector3f(contWidth, contHeight, 0.0f/*FIXME*/));
        shelfViewContainerL.setLayout(
            new BookshelfLayout(true,
                shelfViewAngle, shelfViewZ, shelfViewXSpacing));
        deco.addChild(shelfViewContainerL);
        
        shelfViewContainerR = new Container3D();
        shelfViewContainerR.setPreferredSize(new Vector3f(contWidth, contHeight, 0.0f/*FIXME*/));
        shelfViewContainerR.setLayout(
            new BookshelfLayout(false, 
                shelfViewAngle, shelfViewZ, shelfViewXSpacing));
        deco.addChild(shelfViewContainerR);
        
        leftsideContainer = new Container3D();
        leftsideContainer.setPreferredSize(new Vector3f(contWidth, contHeight, 0.0f/*FIXME*/));
        leftsideContainer.setLayout(
            new HallwayLayout(true, hallwayViewAngle, hallwayViewPanelAngle, 
                                    hallwayViewZ, hallwayViewZSpacing));
        deco.addChild(leftsideContainer);
        
        rightsideContainer = new Container3D();
        rightsideContainer.setPreferredSize(new Vector3f(contWidth, contHeight, 0.0f/*FIXME*/));
        rightsideContainer.setLayout(
            new HallwayLayout(false, 
                hallwayViewAngle, hallwayViewPanelAngle, 
                                    hallwayViewZ, hallwayViewZSpacing));
        deco.addChild(rightsideContainer);
        
        setDecoration(deco);
        
        bgEventSwitch = new ActionSwitcher(
            new VerticalActionSplitter(
                new Component3DGroupMigrationAction(
                    new Container3D[] {
                        mainContainer, shelfViewContainerR},
                    shelfViewContainerL),
                new Component3DGroupMigrationAction(
                    new Container3D[] {
                        mainContainer, shelfViewContainerL},
                    shelfViewContainerR)));
        
        LgEventConnector connector = LgEventConnector.getLgEventConnector();
        
        connector.addListener(Background.class,
            new MouseClickedEventAdapter(MouseEvent3D.ButtonId.BUTTON3,
                (ActionFloat2)bgEventSwitch));
        
        // FIXME -- be careful when adding global listeners to this
        // container, since this container can get instantiated multiple times,
        // that can result in duplicated actions.  This 'first' static boolean
        // is a short-term workaround to avoid such a situation.
        if (first) {
	    first = false;
            
        if (parkAtSideByMoving) {
            connector.addListener(Frame3D.class,
                new Component3DManualMoveEventAdapter(
                    new BooleanToNoArgConverter(false,
                        new ComponentSideMigrationAction(mainContainer, 
                            leftsideContainer, rightsideContainer))));
        }
        
        /** Handle Gesture events */
        connector.addListener(Frame3D.class,
            new GenericEventAdapter(Component3DGestureMoveLeftEvent.class, 
                new Component3DSideMigrationLeftAction(mainContainer, 
                    leftsideContainer)));
        
        connector.addListener(Frame3D.class,
            new GenericEventAdapter(Component3DGestureMoveRightEvent.class, 
                new Component3DSideMigrationRightAction(mainContainer, 
                    rightsideContainer)));
        /** End Gesture events */
        
        connector.addListener(Frame3D.class,
            new MouseClickedEventAdapter(
                new GenericEventPostAction(Component3DToFrontEvent.class)));
        
        connector.addListener(Thumbnail.class, 
            new MouseClickedEventAdapter(
                new ActionNoArg() {
                    public void performAction(LgEventSource source) {
                        assert(source instanceof Thumbnail);
                        Thumbnail thumbnail = (Thumbnail)source;
                        Frame3D body = thumbnail.getBody();
                        assert(body != null);
                        body.postEvent(new Component3DToFrontEvent());
                    }
                }));
        
        final Component3DMigrationAction migrationAction
            = new Component3DMigrationAction(
                new Container3D[] {
                    shelfViewContainerL,
                    shelfViewContainerR,
                    leftsideContainer,
                    rightsideContainer},
                mainContainer);
                
        connector.addListener(Frame3D.class,
            new Component3DToFrontEventAdapter(
                new ActionComponent3D() {
                    public void performAction(LgEventSource source, Component3D sibling) {
                        assert(source instanceof Frame3D);
                        // FIXME -- the sibling argument is not treated fully yet.
                        // The following condifion check is for fixing Issue 418.
                        if (sibling == null) {
                            // perform the migration only when a "strong"
                            // to-front event is issued.
                            migrationAction.performAction(source);
                        }
                        Frame3D f3d = (Frame3D)source;
                        if (mainContainer.rearrangeChildLayout(f3d, null)) {
                            // do nothing for now...
                        }
                    }
                }));
                
        connector.addListener(Frame3D.class,
            new GenericEventAdapter(Component3DToBackEvent.class,
                new ActionNoArg() {
                    public void performAction(LgEventSource source) {
                        assert(source instanceof Frame3D);
                        Frame3D f3d = (Frame3D)source;
                        if (mainContainer.rearrangeChildLayout(f3d, Integer.MAX_VALUE)) {
                            // do nothing for now...
                        }
                    }
                }));
        
        connector.addListener(Frame3D.class,
            new MouseEnteredEventAdapter(
                new ActionBoolean() {
                    public void performAction(LgEventSource source, boolean flag) {
                        assert(source instanceof Frame3D);
                        Frame3D f3d = (Frame3D)source;
                        VisualAppearanceType appearance 
                            = (flag)?(VisualAppearanceType.HIGHLIGHT):(VisualAppearanceType.NORMAL);
                        f3d.postEvent(new Component3DVisualAppearanceEvent(appearance));
                        Thumbnail tn = f3d.getThumbnail();
                        if (tn != null) {
                            tn.postEvent(new Component3DVisualAppearanceEvent(appearance));
                        }
                    }
                }));
                
	} // end of if (first)
                
        // Listen for handling the window size change
        connector.addListener(
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
    
    public void addFrame3D(Frame3D frame3d) {
        Thumbnail tn = frame3d.getThumbnail();
        if (tn == Thumbnail.DEFAULT) {
            Vector3f size = frame3d.getPreferredSize(new Vector3f());
            tn = new DefaultThumbnail(size, frame3d.getName());
            frame3d.setThumbnail(tn);
        }
        
        frame3d.setMouseEventSource(MouseEnteredEvent3D.class, true);
        
        frame3d.addListener(
            new Component3DVisualAppearanceEventAdapter(
                new TransparencyActionNoArg(frame3d, 
                     translucencyNormal, translucencyAnimDuration),
                new TransparencyActionNoArg(frame3d, 
                     translucencyHighlight, translucencyAnimDuration),
                new TransparencyActionNoArg(frame3d, 
                     translucencyLowlight, translucencyAnimDuration)));
        
        Frame3DAnimation f3da
            = new PluggableF3DAnimation(
                new NaturalMotionWithSwayAnimation(
                    defaultAnimDuration, 
                    new AnimateToChangeVisiblePlugin(
                        this, tn, 500, true, new YAcceleratingVector3fSmoother(), null, null)),
                new FlyAwayChangeEnabledPlugin(contWidth, contHeight, 500));
        f3da.setAnimationFinishedEvent(Frame3DAnimationFinishedEvent.class);
        frame3d.setAnimation(f3da);
        
        frame3d.setRotationAxis(0.0f, 1.0f, 0.0f);
        frame3d.setTransparency(1.0f);
        frame3d.changeTransparency(translucencyNormal); // fade in to the space
        frame3d.setScale(0.0f);
        frame3d.changeScale(1.0f); // scale up
        
        addChild(frame3d);
        postEvent(new Frame3DAddedEvent(frame3d));
    }
    
    public void removeFrame3D(Frame3D frame3d) {
        // Try to remove the frame3d from all the inner containers
        removeChild(frame3d);
        shelfViewContainerL.removeChild(frame3d);
        shelfViewContainerR.removeChild(frame3d);
        leftsideContainer.removeChild(frame3d);
        rightsideContainer.removeChild(frame3d);
        postEvent(new Frame3DRemovedEvent(frame3d));
    }
    
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        bgEventSwitch.setEnabled(enabled);
    }
    
    private static class VerticalActionSplitter implements ActionFloat2 {
        private ActionNoArg actionL;
        private ActionNoArg actionR;

        public VerticalActionSplitter(ActionNoArg actionL, ActionNoArg actionR) 
        {
            this.actionL = actionL;
            this.actionR = actionR;
        }
    
        public void performAction(LgEventSource source, float x, float y) {
            if (x <= 0.0f) {
                actionL.performAction(source);
            } else {
                actionR.performAction(source);
            }
        }
    }
    
    private void changeSize(float width, float height) {
        contWidth = width;
        contHeight = height;
        setPreferredSize(new Vector3f(contWidth, contHeight, 0.0f/*FIXME*/));
        shelfViewContainerL.setPreferredSize(new Vector3f(contWidth, contHeight, 0.0f/*FIXME*/));
        shelfViewContainerR.setPreferredSize(new Vector3f(contWidth, contHeight, 0.0f/*FIXME*/));
        leftsideContainer.setPreferredSize(new Vector3f(contWidth, contHeight, 0.0f/*FIXME*/));
        rightsideContainer.setPreferredSize(new Vector3f(contWidth, contHeight, 0.0f/*FIXME*/));
    }
}


