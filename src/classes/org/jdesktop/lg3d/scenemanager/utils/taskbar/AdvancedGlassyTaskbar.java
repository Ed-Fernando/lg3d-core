/**
 * Project Looking Glass
 *
 * $RCSfile: AdvancedGlassyTaskbar.java,v $
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
 * $Date: 2006-08-14 23:13:32 $A
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.taskbar;

import java.util.ArrayList;
import java.util.HashMap;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.jdesktop.lg3d.scenemanager.advanced.AdvancedSceneControl;
import org.jdesktop.lg3d.scenemanager.advanced.event.CurrentVirtualSceneEvent;
import org.jdesktop.lg3d.scenemanager.utils.SceneControl;
import org.jdesktop.lg3d.scenemanager.utils.appcontainer.NaturalMotionF3DAnimationFactory;
import org.jdesktop.lg3d.scenemanager.utils.background.Background;
import org.jdesktop.lg3d.scenemanager.utils.background.LayeredImageBackground;
import org.jdesktop.lg3d.scenemanager.utils.background.ModelBackground;
import org.jdesktop.lg3d.scenemanager.utils.background.PanoImageBackground;
import org.jdesktop.lg3d.scenemanager.utils.background.SimpleImageBackground;
import org.jdesktop.lg3d.scenemanager.utils.event.BackgroundChangeRequestEvent;
import org.jdesktop.lg3d.scenemanager.utils.event.ScreenResolutionChangedEvent;
import org.jdesktop.lg3d.sg.Appearance;
import org.jdesktop.lg3d.sg.BoundingBox;
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


public class AdvancedGlassyTaskbar extends Taskbar {
    private static float barHeight = 0.034f;
    private static float barDepth = 0.002f;
    private float barZ = -0.04f;
    private static float thumbnailZ = -0.023f;
    private static float iconSpacing = 0.0025f;
    private static Appearance barApp
	= new SimpleAppearance(
	    0.6f, 1.0f, 0.6f, 1.0f,
	    SimpleAppearance.DISABLE_CULLING);
    
    Container3D shortcuts;    
    private SceneControl sceneControl;    
    
    // used when runng advanced scene manager
    AdvancedSceneControl sceneMng;
    ArrayList<TasbarInstance> taskbarInstList = new ArrayList<TasbarInstance>();
    private HashMap<AdvancedSceneControl, TasbarInstance> taskbarList = 
	new HashMap<AdvancedSceneControl,TasbarInstance>();
    TasbarInstance currentTaskbar;
  
    public AdvancedGlassyTaskbar() {
    }
    
    TasbarInstance createTaskbarInstance(AdvancedSceneControl sceneCntrl) {	
	TasbarInstance tskbar = new TasbarInstance();
	taskbarInstList.add(tskbar);
	tskbar.cont = new Container3D();
	currentTaskbar = tskbar;
	tskbar.cont.setName("GlassyTaskBar");
	tskbar.cont.setMouseEventSource(MouseButtonEvent3D.class, true);
        Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
        float fov = toolkit3d.getFieldOfView();
        float fovTan2 = (float)Math.tan(fov * 0.5f) * 2.0f;
	
        final float width = toolkit3d.getScreenWidth();
        final float height = toolkit3d.getScreenHeight();
        //float w = width - barZ * fovTan2  ;
        float eyeZ = width / fovTan2;
        barZ = -0.02f / fovTan2; 
        float height1 =  eyeZ * fovTan2 * height / width;
        //barZ = -(width - 0.02f)/fovTan2;
        logger.info("AdvancedGlassyTaskbar: Z delta " + height + "x" + height1);
        //Point3d eye = scenemanager.getEyePosition();
        //barZ = barZ*(float)eye.z/0.46f; // 0.46 default eye Z distance
        //barHeight = barHeight*(float)eye.z/0.46f; this doesn't work
        tskbar.cont.setPreferredSize(new Vector3f(width, barHeight, barHeight));
        tskbar.bottomBar
	    = new GlassyPanel(
		width - 0.02f, 
		barHeight,
		barDepth, 
                barDepth * 0.1f,
		barApp);
        
        // Move the bounds of the bottomBar back so that the transparency sorting
        // in Java 3D renders it first and the icons second.
        // This also requires a bug fix which is in Java 3D 1.3.2
        tskbar.bottomBar.setBoundsAutoCompute(false);
        tskbar.bottomBar.setBounds(
                new BoundingBox(
                    new Point3f(-0.5f * width, -0.5f * barHeight, -0.5f), 
                    new Point3f( 0.5f * width,  0.5f * barHeight,  0.0f)));

	Component3D bottomBarComp = new Component3D();
	bottomBarComp.addChild(tskbar.bottomBar);
	bottomBarComp.setRotationAxis(1.0f, 0.0f, 0.0f);
	bottomBarComp.setRotationAngle((float)Math.toRadians(-90));
	bottomBarComp.setTranslation(0.0f, barHeight * -0.51f, barHeight * -0.3f);
        bottomBarComp.setName("BottomBarComp");
	Container3D deco = new Container3D();
	deco.addChild(bottomBarComp);
        deco.setName("TaskbarDeco");
        tskbar.cont.setDecoration(deco);
        
        // TODO -- the following configurations are to be done via
        // a configuration file.
        if (shortcuts == null) {
	    shortcuts = new Container3D();
	    shortcuts.setPreferredSize(new Vector3f(width - 0.02f, barHeight,
		    barHeight));//FIXME
	    shortcuts.setLayout(new HorizontalReorderableLayout(
		    HorizontalLayout.AlignmentType.LEFT, iconSpacing,
		    new NaturalMotionF3DAnimationFactory(150)));
	    
	}
        
        tskbar.themes = new Container3D();
        tskbar.themes.setPreferredSize(new Vector3f(width - 0.02f, barHeight, barHeight));//FIXME
        tskbar.themes.setLayout(
            new HorizontalLayout(
                HorizontalLayout.AlignmentType.RIGHT, iconSpacing,
                new NaturalMotionAnimationFactory(150)));
        
        // FIXME -- the following line is to be removed
        initBackgrounds(tskbar.themes,sceneCntrl);
        
        tskbar.cont.addChild(tskbar.themes);
        
        tskbar.appThumbnails = new Container3D();
        tskbar.appThumbnails.setPreferredSize(new Vector3f(width - 0.02f, barHeight, barHeight));//FIXME
        tskbar.appThumbnails.setLayout(
            new ThumbnailLayout(ThumbnailLayout.AlignmentType.CENTER, 0.002f,
                (float)Math.toRadians(-45), this));
        tskbar.appThumbnails.setTranslation(0.0f, -0.002f, thumbnailZ);
        tskbar.cont.addChild(tskbar.appThumbnails);
        
        tskbar.cont.setRotationAxis(1.0f, 0.0f, 0.0f);
        tskbar.cont.setRotationAngle((float)Math.toRadians(-360));
        
        tskbar.cont.changeRotationAngle((float)Math.toRadians(5));
        
        tskbar.cont.setTranslation(0.0f, height * -0.6f, 0.0f);
        tskbar.cont.changeTranslation(0.0f, height * -0.5f + barHeight * 0.75f, 0.0f, 2000);
        
	return tskbar;
    }
    public void initialize(SceneControl sceneControl) {
        super.initialize();
        if (sceneControl instanceof AdvancedSceneControl) {
            sceneMng = (AdvancedSceneControl)sceneControl;
            for (AdvancedSceneControl sceneCntrl : sceneMng.getSceneControls()) {
        	TasbarInstance tskbar = createTaskbarInstance(sceneCntrl);
                taskbarList.put(sceneCntrl,tskbar);
        	sceneMng.addComponent3DTo(sceneCntrl, tskbar.cont);        	
    	    }
            currentTaskbar = taskbarList.get(sceneMng.getCurrentSceneControl());
            currentTaskbar.cont.addChild(shortcuts);
            // Listen for handling the current virtual scene change
            LgEventConnector.getLgEventConnector().addListener(
                LgEventSource.ALL_SOURCES,
                new LgEventListener() {
                    public void processEvent(final LgEvent event) {
                        CurrentVirtualSceneEvent cvscene = (CurrentVirtualSceneEvent)event;
                        currentTaskbar = taskbarList.get(cvscene.getCurrentSceneControl());
                        shortcuts.detach();
                        currentTaskbar.cont.addChild(shortcuts);
                    }
                    public Class<LgEvent>[] getTargetEventClasses() {
                        return new Class[] {CurrentVirtualSceneEvent.class};
                    }
                }); 
        } else {
            this.sceneControl = sceneControl;
            currentTaskbar = createTaskbarInstance(null);
            currentTaskbar.cont.addChild(shortcuts);
            addChild(currentTaskbar.cont);
        }
        
        // Listen for Tapps and add them to the toolbar
        LgEventConnector.getLgEventConnector().addListener(
            LgEventSource.ALL_SOURCES,
            new LgEventListener() {
                public void processEvent( final LgEvent evt ) {
                    TaskbarItemConfig config = (TaskbarItemConfig)evt;
                    addTaskbarItem(config.createItem(), config.getItemIndex());
                }
                public Class<LgEvent>[] getTargetEventClasses() {
                    return new Class[] {TaskbarItemConfig.class};
                }
            });
        
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
        
        final float height = Toolkit3D.getToolkit3D().getScreenHeight();
        initHideEventHandler(height);        
    }
    
    private void initBackgrounds(Container3D themes, AdvancedSceneControl sceneCntrl) {
        BackgroundIcon initialBackground;        
		
        themes.addChild(
            initialBackground =
            new BackgroundIcon(this.getClass().getClassLoader().getResource("resources/images/icon/star.png")) {
                    protected Background initBackground() {
                        if (System.getProperty("lg.3dbackground")==null)
                            return new SimpleImageBackground(
                               this.getClass().getClassLoader().getResource( "resources/images/background/DreamLakeReflections.jpg"));
                        else
                            return new ModelBackground();
                    }
                });
        themes.addChild(
                new BackgroundIcon(this.getClass().getClassLoader().getResource("resources/images/icon/hoover.png")) {
                        protected Background initBackground() {
                            return new PanoImageBackground(
                                new  URL [] {
                                    this.getClass().getClassLoader().getResource("resources/images/background/Stanford-0.jpg"),
                                    this.getClass().getClassLoader().getResource("resources/images/background/Stanford-1.jpg"), 
                                    this.getClass().getClassLoader().getResource("resources/images/background/Stanford-2.jpg"), 
                                    this.getClass().getClassLoader().getResource("resources/images/background/Stanford-3.jpg"),
                                },
                                2);
                        }
                    });
    	themes.addChild(
                new BackgroundIcon(this.getClass().getClassLoader().getResource("resources/images/icon/leaf.png")) {
                        protected Background initBackground() {
                            return new LayeredImageBackground(
                                new URL[] {
                                    getClass().getClassLoader().getResource("resources/images/background/Leaves_and_Sky-0.jpg"),
                                    getClass().getClassLoader().getResource("resources/images/background/Leaves_and_Sky-1.png"),
                                },
                                new float[][] {
                                    {1.2f, -0.1f, -1.5f}, 
                                    {0.9f,  0.1f,  1.0f},
                                }
                            );
                        }
                    });
            themes.addChild(
                initialBackground =
                new BackgroundIcon(this.getClass().getClassLoader().getResource("resources/images/icon/star.png")) {
                        protected Background initBackground() {
                            if (System.getProperty("lg.3dbackground")==null)
                                return new LayeredImageBackground(
                                    new URL[] {
                                        getClass().getClassLoader().getResource("resources/images/background/GrandCanyon-0.jpg"),
                                        getClass().getClassLoader().getResource("resources/images/background/GrandCanyon-1.png"),
                                    },
                                    new float[][] {
                                        {1.0f, 0.0f, 1.0f}, 
                                        {0.5f, -0.22f, 1.2f},
                                    }
                                );
                            else
                                return new ModelBackground();
                        }
                    });
            if (sceneControl != null) {
                sceneControl.setBackground(initialBackground.initBackground());
            } else {
        	sceneCntrl.setBackground(initialBackground.initBackground());
            }
    }
    
    public void initializeAfter() {
	//bmih.init();
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
            AdvancedGlassyTaskbar.this.postEvent(new BackgroundChangeRequestEvent(background));
        }
        protected abstract Background initBackground();
    }
    
//    class DesktopChanger extends Pseudo3DIcon {
//	
//        protected String[] desktops = null;        
//        private DesktopChanger(String filename) {
//            super(filename);
//            desktops = scenemanager.getVirtualDesktops();
//            addListener(
//                new MouseClickedEventAdapter(
//                new ActionNoArg() {
//                    public void performAction(LgEventSource source) {
//                        select();
//                    }
//            }));            
//        }
//        public void select() {
//            assert( desktops != null );
//            
//            System.out.println("current desktop " + (currentDesktop+1) + " : " + desktops.length);
//            scenemanager.setCurrentDesktop(desktops[currentDesktop]);
//            currentDesktop++;
//            if (currentDesktop == desktops.length) {
//        	currentDesktop = 0;                
//            }
//            System.out.println("next desktop " + currentDesktop);
//        }
//    }
    
    @Override
    public void addThumbnail(Component3D thumbnail) {
        if (thumbnail == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
        currentTaskbar.appThumbnails.addChild(thumbnail);
    }
    
    @Override
    public void removeThumbnail(Component3D thumbnail) {
	currentTaskbar.appThumbnails.removeChild(thumbnail);
    }
    
    @Override
    public void addTaskbarItem(Component3D item, int index) {
        if(item == null) {
            throw new IllegalArgumentException("Taskbar item cannot be null");
        }
        item.addListener(new MouseClickedEventAdapter(ButtonId.BUTTON3, 
                true, null, new RemoveTaskbarItemAction(item)));
        if(index < 0 || index > shortcuts.numChildren()) {
            index= shortcuts.numChildren();
        }
        shortcuts.addChild(item, index);
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
	for (TasbarInstance taskbar: taskbarInstList) {
	    taskbar.cont.setPreferredSize(new Vector3f(width - 0.02f, barHeight, barHeight));
	    taskbar.bottomBar.setSize(width - 0.02f, barHeight);
	    taskbar.cont.changeTranslation(0.0f, height * -0.5f + barHeight * 0.75f, 0.0f, 200);
        
            shortcuts.setPreferredSize(new Vector3f(width - 0.02f, barHeight, barHeight));//FIXME
            taskbar.themes.setPreferredSize(new Vector3f(width - 0.02f, barHeight, barHeight));//FIXME
            taskbar.appThumbnails.setPreferredSize(new Vector3f(width - 0.02f, barHeight, barHeight));//FIXME
	}
    }
    
    private void initHideEventHandler(final float height) {
        // Listen for handling the hide event
        setAnimation(new NaturalMotionAnimation(3000));
        LgEventConnector.getLgEventConnector().addListener(
            LgEventSource.ALL_SOURCES,
            new LgEventListener() {
                public void processEvent(final LgEvent evt) {
                    currentTaskbar.cont.changeTranslation(0.0f, height * -0.5f - barHeight * 0.5f, barZ);
                }
                public Class<LgEvent>[] getTargetEventClasses() {
                    return new Class[] {HideEvent.class};
                }
            });
    }
            
    public static class HideEvent extends LgEvent {
        // just a tag class
    }
    
    @Override
    public Component3D getPluginRoot() {
	if (sceneControl != null) {
	    return this;
	} 
        return null;
    }
    
    static class TasbarInstance {
	Container3D cont;
	Container3D appThumbnails;
	GlassyPanel bottomBar;
	Container3D themes;
    }
}
