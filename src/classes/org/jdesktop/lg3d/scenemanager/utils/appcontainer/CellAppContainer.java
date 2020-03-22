/**
 * Project Looking Glass
 *
 * $RCSfile: CellAppContainer.java,v $
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
 * $Revision: 1.1 $
 * $Date: 2007-04-10 22:57:09 $
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


public class CellAppContainer extends AppContainer {

    private static boolean first = true;

    private static final float zSpacing = 0.02f;
    private static final int defaultAnimDuration = 500;
    private static final float translucencyNormal = 0.25f;
    private static final float translucencyHighlight = 0.0f;
    private static final float translucencyLowlight = 0.8f;
    private static final int translucencyAnimDuration = 750;
    private static boolean parkAtSideByMoving 
            = System.getProperty("lg.scenemanager.parkingByMoving", "false").equals("true");
    
    private float contWidth;
    private float contHeight;
    private LayoutManager3D mainLayout;
    
    public CellAppContainer() {
        this(zSpacing);
    }
    
    public CellAppContainer(float spacing) {
        setName("CellAppContainer");
	mainLayout = new ZLayeredMovableLayout(spacing);
    }
    
    public CellAppContainer(LayoutManager3D layout) {
        setName("CellAppContainer");
	// TODO: DJ hack	mainLayout = layout;
    }
    
    public void initialize () {
        Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
	initialize(toolkit3d.getScreenWidth(), toolkit3d.getScreenHeight());
    }

    // TODO:need a way of calculating width, height
    public void initialize (float contWidth, float contHeight) {
        final Container3D mainContainer = this;
        Container3D deco = new Container3D();
        
        mainContainer.setPreferredSize(new Vector3f(contWidth, contHeight, 0.0f/*FIXME*/));
        mainContainer.setLayout(mainLayout);
        
        setDecoration(deco);
        
        LgEventConnector connector = LgEventConnector.getLgEventConnector();
        
        
        // FIXME -- be careful when adding global listeners to this
        // container, since this container can get instantiated multiple times,
        // that can result in duplicated actions.  This 'first' static boolean
        // is a short-term workaround to avoid such a situation.
        if (first) {
	    first = false;
            
        connector.addListener(Frame3D.class,
            new MouseClickedEventAdapter(
                new GenericEventPostAction(Component3DToFrontEvent.class)));
        
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
        
	} // end of if (first)
                
	// TODO: still need this?
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
        
	System.err.println("&&&&&&&&&&&&&&& Add Frame3D to CellAppContainer");

        frame3d.setMouseEventSource(MouseEnteredEvent3D.class, true);
        
	/*
        frame3d.addListener(
            new Component3DVisualAppearanceEventAdapter(
                new TransparencyActionNoArg(frame3d, 
                     translucencyNormal, translucencyAnimDuration),
                new TransparencyActionNoArg(frame3d, 
                     translucencyHighlight, translucencyAnimDuration),
                new TransparencyActionNoArg(frame3d, 
                     translucencyLowlight, translucencyAnimDuration)));
	*/

        frame3d.setRotationAxis(0.0f, 1.0f, 0.0f);

	/*
        frame3d.setTransparency(1.0f);
        frame3d.changeTransparency(translucencyNormal); // fade in to the space
        frame3d.setScale(0.0f);
        frame3d.changeScale(1.0f); // scale up
        */
        frame3d.setTransparency(0.0f);
        frame3d.setScale(1.0f);

        addChild(frame3d);
        postEvent(new Frame3DAddedEvent(frame3d));
    }
    
    public void removeFrame3D(Frame3D frame3d) {
        // Try to remove the frame3d from all the inner containers
        removeChild(frame3d);
        postEvent(new Frame3DRemovedEvent(frame3d));
    }
    
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }
    
    private void changeSize(float width, float height) {
        contWidth = width;
        contHeight = height;
        setPreferredSize(new Vector3f(contWidth, contHeight, 0.0f/*FIXME*/));
    }
}


