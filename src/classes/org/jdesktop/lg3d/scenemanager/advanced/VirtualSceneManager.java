/**
 * Project Looking Glass
 *
 * $RCSfile: VirtualSceneManager.java,v $
 *
 * Copyright (c) 2005, Sun Microsystems, Inc., All Rights Reserved
 *
 * Redistributions in source code form must reproduce the above
 * copyright and this condition.
 *
 * The contents of this file are subject to the GNU General Public
 * License, Version 2 (the "License"); you may not use this file
 * except in compliance with the License. A copy of the License is
 * available at http://www.opensource.org/licenses/gpl-license.php.
 *
 * $Revision: 1.8 $
 * $Date: 2006-04-27 06:37:55 $
 * $State: Exp $
 */
/** Copyright (c) 2005 Amir Bukhari
 *
 * Permission to use, copy, modify, distribute, and sell this software and its
 * documentation for any purpose is hereby granted without fee, provided that
 * the above copyright notice appear in all copies and that both that
 * copyright notice and this permission notice appear in supporting
 * documentation, and that the name of Amir Bukhari not be used in
 * advertising or publicity pertaining to distribution of the software without
 * specific, written prior permission.  Amir Bukhari makes no
 * representations about the suitability of this software for any purpose.  It
 * is provided "as is" without express or implied warranty.
 *
 * AMIR BUKHARI DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE,
 * INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS, IN NO
 * EVENT SHALL AMIR BUKHARI BE LIABLE FOR ANY SPECIAL, INDIRECT OR
 * CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE,
 * DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER
 * TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 * PERFORMANCE OF THIS SOFTWARE.
 */

package org.jdesktop.lg3d.scenemanager.advanced;

import java.util.ArrayList;
import java.util.HashMap;
import javax.vecmath.Vector3f;

import org.jdesktop.lg3d.scenemanager.advanced.event.CurrentVirtualSceneEvent;
import org.jdesktop.lg3d.scenemanager.advanced.layout.SceneLayoutManager;
import org.jdesktop.lg3d.scenemanager.advanced.layout.HorizontalSceneLayout;
import org.jdesktop.lg3d.scenemanager.utils.appcontainer.AppContainer;
import org.jdesktop.lg3d.scenemanager.utils.background.Background;
import org.jdesktop.lg3d.scenemanager.utils.event.ScreenResolutionChangedEvent;
import org.jdesktop.lg3d.scenemanager.utils.taskbar.Taskbar;
import org.jdesktop.lg3d.utils.action.ActionNoArg;
import org.jdesktop.lg3d.utils.c3danimation.NaturalMotionAnimationFactory;
import org.jdesktop.lg3d.utils.component.Pseudo3DIcon;
import org.jdesktop.lg3d.utils.eventadapter.MouseClickedEventAdapter;
import org.jdesktop.lg3d.utils.layoutmanager.HorizontalLayout;
import org.jdesktop.lg3d.wg.Container3D;
import org.jdesktop.lg3d.wg.Cursor3D;
import org.jdesktop.lg3d.wg.Toolkit3D;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventConnector;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.MouseButtonEvent3D;
import java.net.URL;

/**
 *
 * @author bukhari
 */
class VirtualSceneManager {
    //private static String[] fixlist = { "VSCENE1", "VSCENE2", "VSCENE3"};
    private static final float SPACE = 20.0f;
    private static float iconSpacing = 0.0005f;
    private ArrayList<String> list = new ArrayList<String>();
    private ArrayList<Scene> contList = new ArrayList<Scene>();
    HashMap<String, Scene> virtSceneList = new HashMap<String, Scene>();
    private AdvancedSceneManager sceneManager;
    private int numOfDesktops = 0;
    private String currentScene = null;
    private SceneLayoutManager layoutManager = null;
    private Container3D vdesktopChanger;

    /**
     * 
     * @param sceneManager
     */
    VirtualSceneManager(AdvancedSceneManager sceneManager) {
	this.sceneManager = sceneManager;
	list.add("SCENE1");
	list.add("SCENE2");
	list.add("SCENE3");
	currentScene = list.get(0);
        Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
	vdesktopChanger = new Container3D();
	vdesktopChanger.setPreferredSize(new Vector3f(toolkit3d.getScreenWidth(), 
        	toolkit3d.getScreenHeight(), 0));
	vdesktopChanger.setLayout(
            new HorizontalLayout(
                HorizontalLayout.AlignmentType.LEFT, iconSpacing,
                new NaturalMotionAnimationFactory(150)));
	for (String vdesktop :list) {
	    Container3D rootContainer = new Container3D();
	    rootContainer.setPreferredSize(new Vector3f(
		    toolkit3d.getScreenWidth(), toolkit3d.getScreenHeight(), 0.0f));
	    rootContainer.setCursor(Cursor3D.DEFAULT_CURSOR);
	    Scene dsk = new Scene(vdesktop, rootContainer);
	    virtSceneList.put(vdesktop, dsk);
	    contList.add(dsk);
	    sceneManager.getSceneRoot().addChild(rootContainer);
	    numOfDesktops++;
	    vdesktopChanger.addChild(
                    new SceneChanger(getClass().getClassLoader().getResource("resources/images/icon/hoover.png"),
                        vdesktop));
	}
	vdesktopChanger.setTranslation(iconSpacing, toolkit3d.getScreenHeight() - 0.015f,0);
	sceneManager.getSceneRoot().addChild(vdesktopChanger);
	
	// Listen for handling the window size change
        LgEventConnector.getLgEventConnector().addListener(
            LgEventSource.ALL_SOURCES,
            new LgEventListener() {
                public void processEvent(final LgEvent event) {
                    ScreenResolutionChangedEvent csce = (ScreenResolutionChangedEvent)event;
                    vdesktopChanger.setPreferredSize(new Vector3f(csce.getWidth(), 
                	    csce.getHeight(), 0));
                    vdesktopChanger.setTranslation(iconSpacing,csce.getHeight() - 0.015f,0);
                }
                public Class<LgEvent>[] getTargetEventClasses() {
                    return new Class[] {ScreenResolutionChangedEvent.class};
                }
            });
    }

    void initializeDefaultLayout() {
	HorizontalSceneLayout layout = new HorizontalSceneLayout();
	for (Scene comp : virtSceneList.values()) {
	    layout.addLayoutScene(comp.rootContainer, null);
	}
	layout
		.setDefaultScene(new Container3D[] { virtSceneList
			.get(list.get(0)).rootContainer });
	layoutManager = layout;
	layoutManager.layoutScene();
    }

    /**
     * 
     * @param name
     * @param constraints
     * @return
     */
    boolean createVirtualScene(String name, Object constraints) {

	if (virtSceneList.get(name) == null) {
	    Container3D rootContainer = new Container3D();
            Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
	    rootContainer.setPreferredSize(new Vector3f(
		    toolkit3d.getScreenWidth(), toolkit3d.getScreenHeight(), 0.0f));
	    rootContainer.setCursor(Cursor3D.DEFAULT_CURSOR);
	    virtSceneList.put(name, new Scene(name, rootContainer));
	    list.add(name);
	    sceneManager.getSceneRoot().addChild(rootContainer);
	    layoutManager.addLayoutScene(rootContainer, null);
	    numOfDesktops++;
	    return true;
	}
	return false;
    }
    
    Container3D getVirtualSceneRoot(String sceneName) {
        return virtSceneList.get(sceneName).rootContainer;
    }

    /**
     * 
     * @param vdektop
     * @param taskbar Taskbar to add
     */
    void setTaskbar(String vscene, Taskbar taskbar) {
	assert (taskbar != null);
	Scene desktop = virtSceneList.get(vscene);
	if (desktop == null) {
	    throw new RuntimeException("Invalid Scene name");
	}
	System.err.println("taskbar: " + taskbar);
	desktop.taskbar = taskbar;
	desktop.rootContainer.addChild(taskbar);
    }

    /**
     * 
     * @param vdektop
     * @param background Background
     */
    void setBackground(String vscene, Background background) {
	System.err.println("background: " + background);
	assert (background != null);

	Scene scene = virtSceneList.get(vscene);
	if (scene == null) {
	    throw new RuntimeException("Invalid Scene name");
	}

	if (scene.background != null) {
	    scene.background.detach();
	    scene.background.setEnabled(false);
	}
	background.initialize(sceneManager.getSceneControl(vscene));
	background.setMouseEventSource(MouseButtonEvent3D.class, true);
	background.setEnabled(true);
	scene.background = background;
	scene.rootContainer.addChild(background);
    }

    /**
     * 
     * @param vdektop
     */
    void setCurrentVScene(String vscene) {
	assert (vscene != null);
	System.err.println("setCurrentScene: " + vscene);
	Scene desktop = virtSceneList.get(vscene);
	if (desktop == null) {
	    throw new RuntimeException("Invalid Scene name");
	}
	Scene current = virtSceneList.get(currentScene);
	if (current != desktop) {
	    layoutManager.setDefaultScene(new Container3D[] { desktop.rootContainer});
	    currentScene = vscene;
	    layoutManager.layoutScene();
	}
    }

    /**
     * 
     * @return
     */
    String getCurrentVScene() {
	return currentScene;
    }

    String isContainerRoot(Container3D cont) {
	for (Scene vscene: contList) {
	    if (cont == vscene.rootContainer) {
		return vscene.name;
	    }
	}
	return null;
    }
    
    /**
     * 
     * @return
     */
    String[] getVirtualVScene() {
	return list.toArray(new String[list.size()]);
    }

    /**
     * 
     * @param vdektop
     * @return
     */
    AppContainer getVSceneAppContainer(String vscene) {
	Scene scene = virtSceneList.get(vscene);
	if (scene == null) {
	    throw new RuntimeException("Invalid Scene name");
	}
	return scene.appContianer;
    }

    /**
     * 
     * @param vdektop
     * @param appCont
     */
    void setVSceneAppContainer(String vscene, AppContainer appCont) {
	Scene desktop = virtSceneList.get(vscene);
	if (desktop == null) {
	    throw new RuntimeException("Invalid Scene name");
	}
	desktop.appContianer = appCont;
    }

    /**
     * 
     * @param vdektop
     * @return
     */
    Taskbar getVDesktopTaskbar(String vscene) {
	Scene desktop = virtSceneList.get(vscene);
	if (desktop == null) {
	    throw new RuntimeException("Invalid Scene name");
	}
	return desktop.taskbar;
    }

    class Scene {
	String name;
	Container3D rootContainer;
	AppContainer appContianer = null;
	Background background;
	Taskbar taskbar;

	public Scene(String name, Container3D rootContainer) {
	    this.name = name;
	    this.rootContainer = rootContainer;
	}

    }

    SceneLayoutManager getLayoutManager() {
	return layoutManager;
    }

    void setLayoutManager(SceneLayoutManager layoutManager) {
	this.layoutManager = layoutManager;
	for (Scene comp : virtSceneList.values()) {
	    this.layoutManager.addLayoutScene(comp.rootContainer, null);
	}
	this.layoutManager
		.setDefaultScene(new Container3D[] { virtSceneList
			.get(currentScene).rootContainer });
    }
    
    class SceneChanger extends Pseudo3DIcon {
	
        String vscene;
        private SceneChanger(URL imageUrl, String vscene) {
            super(imageUrl);
            this.vscene = vscene;
            addListener(
                new MouseClickedEventAdapter(
                new ActionNoArg() {
                    public void performAction(LgEventSource source) {
                        select();
                    }
            }));            
        }
        public void select() {
            assert( vscene != null );            
            setCurrentVScene(vscene);    
            this.postEvent(new CurrentVirtualSceneEvent(sceneManager.getSceneControl(vscene)));
        }
    }
}
