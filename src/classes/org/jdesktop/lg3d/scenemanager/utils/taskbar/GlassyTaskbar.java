/**
 * Project Looking Glass
 *
 * $RCSfile: GlassyTaskbar.java,v $
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
 * $Date: 2007-01-29 18:18:44 $A
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.taskbar;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.jdesktop.lg3d.scenemanager.utils.SceneControl;
import org.jdesktop.lg3d.scenemanager.utils.appcontainer.NaturalMotionF3DAnimationFactory;
import org.jdesktop.lg3d.scenemanager.utils.background.Background;
import org.jdesktop.lg3d.scenemanager.utils.background.LayeredImageBackground;
import org.jdesktop.lg3d.scenemanager.utils.background.ModelBackground;
import org.jdesktop.lg3d.scenemanager.utils.background.PanoImageBackground;
import org.jdesktop.lg3d.scenemanager.utils.event.BackgroundChangeRequestEvent;
import org.jdesktop.lg3d.scenemanager.utils.event.ScreenResolutionChangedEvent;
import org.jdesktop.lg3d.sg.Appearance;
import org.jdesktop.lg3d.sg.utils.transparency.TransparencyOrderedGroup;
import org.jdesktop.lg3d.utils.action.ActionNoArg;
import org.jdesktop.lg3d.utils.c3danimation.NaturalMotionAnimation;
import org.jdesktop.lg3d.utils.c3danimation.NaturalMotionAnimationFactory;
import org.jdesktop.lg3d.utils.component.Pseudo3DIcon;
import org.jdesktop.lg3d.utils.eventadapter.MouseClickedEventAdapter;
import org.jdesktop.lg3d.utils.shape.GlassyPanel;
import org.jdesktop.lg3d.utils.shape.SimpleAppearance;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Container3D;
import org.jdesktop.lg3d.wg.Toolkit3D;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventConnector;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.MouseButtonEvent3D;
import org.jdesktop.lg3d.wg.event.MouseEvent3D.ButtonId;
import java.net.URL;

public class GlassyTaskbar extends Taskbar {
    private static float barHeight = 0.025f;
    private static float barDepth = 0.0025f;
    private static float barZ = -0.04f;
    private static float thumbnailZ = -0.01f;
    private static float iconSpacing = 0.0025f;
    private static Appearance barApp
	= new SimpleAppearance(
	    0.6f, 1.0f, 0.6f, 1.0f,
	    SimpleAppearance.DISABLE_CULLING);
    
    private GlassyPanel bottomBar;
    private Container3D appThumbnails;
    private Container3D shortcuts;
    private Container3D themes;
    
    public GlassyTaskbar() {
    }
    
    public void initialize(SceneControl sceneControl) {
        super.initialize();
        setName("GlassyTaskBar");
        setMouseEventSource(MouseButtonEvent3D.class, true);
        
        Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
        // If the Canvas is not visible yet screensize might be 0,0, which causes NonAffine transforms
        final float width = (toolkit3d.getScreenWidth()!=0f) ? toolkit3d.getScreenWidth() : 100f;
        final float height = (toolkit3d.getScreenHeight()!=0f) ? toolkit3d.getScreenHeight() : 100f;
                
	setPreferredSize(new Vector3f(width, barHeight, barHeight));
	bottomBar
	    = new GlassyPanel(
		width, 
		barHeight,
		barDepth, 
                barDepth * 0.1f,
		barApp);
        
	Component3D bottomBarComp = new Component3D();
	bottomBarComp.addChild(bottomBar);
	bottomBarComp.setRotationAxis(1.0f, 0.0f, 0.0f);
	bottomBarComp.setRotationAngle((float)Math.toRadians(-90));
	bottomBarComp.setTranslation(0.0f, barHeight * -0.52f, barHeight * -0.3f);
        bottomBarComp.setName("BottomBarComp");
	Container3D deco = new Container3D();
	deco.addChild(bottomBarComp);
        deco.setName("TaskbarDeco");
        
	shortcuts = new Container3D();
        shortcuts.setPreferredSize(new Vector3f(width, barHeight, barHeight));//FIXME
	shortcuts.setLayout(
            new HorizontalReorderableLayout(
                HorizontalLayout.AlignmentType.LEFT, iconSpacing, 
                new NaturalMotionF3DAnimationFactory(150)));
        
        // Listen for Tapps and add them to the toolbar
        LgEventConnector.getLgEventConnector().addListener(
            LgEventSource.ALL_SOURCES,
            new LgEventListener() {
                public void processEvent(final LgEvent evt) {
                    TaskbarItemConfig config = (TaskbarItemConfig)evt;
                    addTaskbarItem(config.createItem(), config.getItemIndex());
                }
                public Class<LgEvent>[] getTargetEventClasses() {
                    return new Class[] {TaskbarItemConfig.class};
                }
            });
        
	themes = new Container3D();
        themes.setPreferredSize(new Vector3f(width, barHeight, barHeight));//FIXME
	themes.setLayout(
            new HorizontalReorderableLayout(
                HorizontalLayout.AlignmentType.RIGHT, iconSpacing,
                new NaturalMotionF3DAnimationFactory(150)));
        
        appThumbnails = new Container3D();
        appThumbnails.setPreferredSize(new Vector3f(width, barHeight, barHeight));//FIXME
	appThumbnails.setLayout(
            new ThumbnailLayout(ThumbnailLayout.AlignmentType.CENTER, 0.002f,
                (float)Math.toRadians(-45), this));
        appThumbnails.setTranslation(0.0f, 0.0f, thumbnailZ);
        
        Component3D c3d = new Component3D();
        TransparencyOrderedGroup tog = new TransparencyOrderedGroup();
        tog.addChild(deco);
	tog.addChild(shortcuts);
        tog.addChild(themes);
        tog.addChild(appThumbnails);
        c3d.addChild(tog);
        addChild(c3d);
        
	setRotationAxis(1.0f, 0.0f, 0.0f);
        setRotationAngle((float)Math.toRadians(-360));
        changeRotationAngle((float)Math.toRadians(5));
        setTranslation(0.0f, height * -0.6f, 0.0f);
        changeTranslation(0.0f, height * -0.5f + barHeight * 0.5f, barZ, 2000);
        
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
            
        initHideEventHandler(height);
    }
    
    private abstract class BackgroundIcon extends Pseudo3DIcon {
        protected Background background = null;
        private BackgroundIcon(URL filename) {
            super(filename);
            addListener(
                new MouseClickedEventAdapter(
                new ActionNoArg() {
                    public void performAction(LgEventSource source) {
                        select();
                    }
            }));
        }
        public void select() {
            if (background == null) {
                // All this complications are for performing background
                // initialization lazily when selected...
                background = initBackground();
            }
            GlassyTaskbar.this.postEvent(new BackgroundChangeRequestEvent(background));
        }
        protected abstract Background initBackground();
    }
    
    @Override
    public void addThumbnail(Component3D thumbnail) {
        if (thumbnail == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
        appThumbnails.addChild(thumbnail);
    }
    
    @Override
    public void removeThumbnail(Component3D thumbnail) {
        appThumbnails.removeChild(thumbnail);
    }
    
    @Override
    public void addTaskbarItem(Component3D item, int index) {
        if(item == null) {
            throw new IllegalArgumentException("Taskbar item cannot be null");
        }
        item.addListener(new MouseClickedEventAdapter(ButtonId.BUTTON3, 
                true, null, new RemoveTaskbarItemAction(item)));
        
        Container3D targetContainer = shortcuts;
        if (index < 0) {
            targetContainer = themes;
            index = themes.numChildren() - index;
        }
        if (index > targetContainer.numChildren()) {
            index = targetContainer.numChildren();
        } else if (index < 0) {
            index = 0;
        }
        targetContainer.addChild(item, index);
    }
    
    @Override
    public void removeTaskbarItem(Component3D item) {
        shortcuts.removeChild(item);
    }
    
    private class RemoveTaskbarItemAction implements ActionNoArg {
        private Component3D item;
        public RemoveTaskbarItemAction(Component3D item) {
            this.item= item;
        }
        public void performAction(LgEventSource source) {
            removeTaskbarItem(item);
        }
    }
    
    private void changeSize(float width, float height) {
        setPreferredSize(new Vector3f(width, barHeight, barHeight));
	bottomBar.setSize(width, barHeight);
        changeTranslation(0.0f, height * -0.5f + barHeight * 0.5f, barZ, 200);
        
        shortcuts.setPreferredSize(new Vector3f(width, barHeight, barHeight));//FIXME
        themes.setPreferredSize(new Vector3f(width, barHeight, barHeight));//FIXME
        appThumbnails.setPreferredSize(new Vector3f(width, barHeight, barHeight));//FIXME
        
        System.out.println("GlassyTaskBar sizeChange");
        System.out.println(width+"  "+height);
        System.out.println((height * -0.5f + barHeight * 0.5f)+" "+ barZ);
    }
    
    private void initHideEventHandler(final float height) {
        // Listen for handling the hide event
        setAnimation(new NaturalMotionAnimation(3000));
        LgEventConnector.getLgEventConnector().addListener(
            LgEventSource.ALL_SOURCES,
            new LgEventListener() {
                public void processEvent(final LgEvent evt) {
                    changeTranslation(0.0f, height * -0.5f - barHeight * 5.0f, barZ);
                }
                public Class<LgEvent>[] getTargetEventClasses() {
                    return new Class[] {HideEvent.class};
                }
            });
    }
            
    public static class HideEvent extends LgEvent {
        // just a tag class
    }
}

