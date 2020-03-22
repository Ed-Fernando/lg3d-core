/**
 * Project Looking Glass
 *
 * $RCSfile: AdvancedSceneManager.java,v $
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
 * $Revision: 1.14 $
 * $Date: 2006-08-14 23:47:43 $
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
import java.util.HashSet;

import org.jdesktop.lg3d.scenemanager.utils.SceneControl;
import org.jdesktop.lg3d.scenemanager.utils.SceneManagerBase;
import org.jdesktop.lg3d.scenemanager.utils.appcontainer.AppContainer;
import org.jdesktop.lg3d.scenemanager.utils.appcontainer.StandardAppContainer;
import org.jdesktop.lg3d.scenemanager.utils.background.Background;
import org.jdesktop.lg3d.scenemanager.utils.event.BackgroundChangeRequestEvent;
import org.jdesktop.lg3d.scenemanager.utils.plugin.SceneManagerPlugin;
import org.jdesktop.lg3d.scenemanager.utils.plugin.SceneManagerPluginConfig;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Container3D;
import org.jdesktop.lg3d.wg.Frame3D;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventConnector;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.wg.event.LgEventSource;


/**
 * The Simple Scenemanage
 */
public class AdvancedSceneManager extends SceneManagerBase {
    private Class appContainerClass;
    private VirtualSceneManager virtualSceneManager;
    private HashMap<String,ArrayList<AppContainer>> vsceneList = 
	new HashMap<String,ArrayList<AppContainer>>();
    private HashMap<String,AdvancedSceneControlImp> implentList = 
	new HashMap<String,AdvancedSceneControlImp>();
    private HashSet<SceneManagerPlugin> plugins = 
        new HashSet<SceneManagerPlugin>();
    
    @Override
    protected void initialize() {
	virtualSceneManager = new VirtualSceneManager(this);
	virtualSceneManager.initializeDefaultLayout();
	for (String vscene : getScenes(null)) {
            ArrayList<AppContainer> appContainers = new ArrayList<AppContainer>();
	    vsceneList.put(vscene, appContainers);
	}

        // TODO:
        for (String vscene : getScenes(null)) {
            AdvancedSceneControlImp imple = new AdvancedSceneControlImp(vscene);
            implentList.put(vscene, imple);
            // It is taskbar's or a background manager responsibility to set the background.
    	    // TODO: this should be from config file.
//            AdvancedGlassyTaskbar taskbar = new AdvancedGlassyTaskbar();            
//            taskbar.initialize(imple);
//            setTaskbar(vscene, taskbar);
//            taskbar.initializeAfter();
        }
        
        String vscene = virtualSceneManager.getCurrentVScene();
	setCurrentVScene(vscene);
        
        // set the very initial AppContainer
        appContainerClass = StandardAppContainer.class;
        getSceneRoot().addChild(getAppContainer(0));
        
//	LgEventConnector.getLgEventConnector().addListener(
//		LgEventSource.ALL_SOURCES, new LgEventListener() {
//		    public void processEvent(final LgEvent event) {
//			GetVirtualScenesRequestEvent evt = (GetVirtualScenesRequestEvent)event;
//			VirtualSceneListsEvent list = new VirtualSceneListsEvent(evt.getScene(),
//				AdvancedSceneManager.this.virtualSceneManager
//					.getVirtualVScenes());
//			AppConnectorPrivate.getAppConnector().postEvent(list,
//				null);
//		    }
//
//		    public Class<LgEvent>[] getTargetEventClasses() {
//			return new Class[] { GetVirtualScenesRequestEvent.class };
//		    }
//		});
        
        initializePluginListener();
        
        LgEventConnector.getLgEventConnector().addListener(
		LgEventSource.ALL_SOURCES, new LgEventListener() {
		    public void processEvent(final LgEvent event) {
			BackgroundChangeRequestEvent bgcre = (BackgroundChangeRequestEvent) event;
			Background bg = bgcre.getBackground();
			//String desktopName = findSceneFromEventSource((Container3D)event.getSource());
			//assert ( desktopName!=null);
			// REMINDER: use getParent()
			AdvancedSceneControlImp imple = 
			    implentList.get(virtualSceneManager.getCurrentVScene());
			bg.initialize(imple);
			AdvancedSceneManager.this.
			setBackground(imple.getScene(), bg);
		    }

		    public Class<LgEvent>[] getTargetEventClasses() {
			return new Class[] { BackgroundChangeRequestEvent.class };
		    }
		});
    }
    
    /**
     * 
     * @return a list of Desktop's names.
     */
    String[] getScenes(String scene) {
	return virtualSceneManager.getVirtualVScene();
    }
    
    AdvancedSceneControlImp[] getSceneControls() {
	return implentList.values().toArray(new AdvancedSceneControlImp[implentList.size()]);
    }
    
    private AppContainer getAppContainer(int index) {	
	return getAppContainer(virtualSceneManager.getCurrentVScene(), index);
    }
    
    AppContainer getAppContainer(String scene, int index) {
	ArrayList<AppContainer> appContainers = vsceneList.get(scene);
        int size = appContainers.size();
	if (index >= size) {
	    for (int i = size; i <= index; i++) {
		appContainers.add(null);
	    }
	}
	assert (appContainers.size() > index);
	AppContainer ac = appContainers.get(index);
	if (ac == null) {
	    try {
		ac = (AppContainer) appContainerClass.newInstance();
	    } catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException(
			"failed to instantiate app container: "
				+ appContainerClass + ": " + e);
	    }
	    ac.initialize();
	    appContainers.set(index, ac);
	}
	ac.detach(); // be sure to detach the container from the previous owner
	ac.setTranslation(0.0f, 0.0f, 0.0f);
	ac.setScale(1.0f);
	ac.setRotationAngle(0.0f);
	AppContainer acont = virtualSceneManager.getVSceneAppContainer(
		    scene);
	if (acont == null) {
	    virtualSceneManager.setVSceneAppContainer(scene, 
	        	ac);
	}
	return ac;
    }

    /**
     * Note -- this doesn't add the AppContainer to the scenegraph.
     * It is caller's responsibility to do it via getAppContainer().
     */
    void setCurrentAppContainer(String scene, int index) {
	ArrayList<AppContainer> appContainers = vsceneList.get(scene);
	if (index < 0 || index > appContainers.size()) {
	    throw new IllegalArgumentException("illegal index: " + index);
	}
	for (AppContainer ac : appContainers) {
	    ac.setEnabled(false);
	}
	appContainers.get(index).setEnabled(true);
        virtualSceneManager.setVSceneAppContainer(scene, 
        	appContainers.get(index));
    }
        
    private int getAppContainerIndex(AppContainer appCont, String scene) {
	if (appCont == null) {
	    return 0;
	}
	
        ArrayList<AppContainer> appContainers = vsceneList.get(scene);
        int index = appContainers.indexOf(appCont);        
        return index;
    }
    
    int getCurrentAppContainer(String scene) {
	AppContainer appCont = virtualSceneManager.getVSceneAppContainer(scene);        
	int index = getAppContainerIndex(appCont, scene);
        if (index == -1) {
            return 0;
        }
        System.out.println("currentAC: " + scene + ": " +index);
        return index;
    }

    void swapAppContainer(String scene, int indexA, int indexB) {
	ArrayList<AppContainer> appContainers = vsceneList.get(scene);
        if (indexA < 0 || indexA >= appContainers.size() || indexB < 0
		|| indexB >= appContainers.size()) {
	    throw new IllegalArgumentException("illegal index: " + indexA
		    + ", " + indexB);
	}
	if (indexA == indexB) {
	    return;
	}
	AppContainer contA = appContainers.get(indexA);
	AppContainer contB = appContainers.get(indexB);
	appContainers.set(indexA, contB);
	appContainers.set(indexB, contA);
    }
    
    AdvancedSceneControlImp getSceneControl(String vscene) {
	if (vscene != null) {
            return implentList.get(vscene);
	} else {
	    return implentList.get(virtualSceneManager.getCurrentVScene());
	}
    }
    
    void addComponentToScene(AdvancedSceneControlImp sceneControl, Component3D comp3d) {
	virtualSceneManager.getVirtualSceneRoot(sceneControl.getScene()).addChild(comp3d);
    }
    

    /*
    private void setTaskbar(String scene, Taskbar taskbar) {
	virtualSceneManager.setTaskbar(scene, taskbar);
    }
    */
    
    void setBackground(String scene, Background background) {
	virtualSceneManager.setBackground(scene, background);
    }
    
    /**
     * 
     * @param desktop
     */
    private void setCurrentVScene(String scene) {
	virtualSceneManager.setCurrentVScene(scene);
    }

    @Override
    public void addFrame3D(Frame3D frame3d) {
	String scene = (String)frame3d.getProperty("SCENENAME");
	AppContainer cont = null;
	if (scene == null) {
	    scene = virtualSceneManager.getCurrentVScene();
	    frame3d.setProperty("SCENENAME", scene);
	    cont = virtualSceneManager.getVSceneAppContainer(scene);
	    cont.addFrame3D(frame3d);
	} else {
	    cont = virtualSceneManager.getVSceneAppContainer(scene);
	    cont.addFrame3D(frame3d);
	}
	
//	Taskbar taskbar = virtualSceneManager.getVDesktopTaskbar(scene);
//	if (taskbar != null) {
//	    Component3D tn = frame3d.getThumbnail();
//	    if (tn != null) {
//		tn.setScale(0.0f);
//		taskbar.addThumbnail(tn);
//	    }
//	}
    }
    
    @Override
    public void removeFrame3D(Frame3D frame3d) {
	String scene = (String)frame3d.getProperty("SCENENAME");
        assert( scene != null);
	AppContainer cont = null;
	cont = virtualSceneManager.getVSceneAppContainer(scene);
	cont.removeFrame3D(frame3d);
//	Taskbar taskbar = virtualSceneManager.getVDesktopTaskbar(scene);
//	if (taskbar != null) {
//	    Component3D tn = frame3d.getThumbnail();
//	    if (tn != null) {
//                taskbar.removeThumbnail(tn);
//            }
//	}
    }
    
    private void initializePluginListener() {
        LgEventConnector.getLgEventConnector().addListener(
                LgEventSource.ALL_SOURCES,
                new AdvancedPluginConfigListener());
    }
    
    @Override
    public void addPlugin(SceneManagerPlugin plugin) {
        addPlugin(plugin, null);
    }
    
    @Override
    public void removePlugin(SceneManagerPlugin plugin) {
        removePlugin(plugin, null);
    }
    
    private void addPlugin(SceneManagerPlugin plugin, String scene) {
        if (scene == null) {
	    scene = virtualSceneManager.getCurrentVScene();
	}
        if (plugins.contains(plugin)) {
            if (plugin.isRemovable()) {
                removePlugin(plugin, scene);
            } else {
                throw new IllegalArgumentException(plugin + " plugin is already registerd and is not removable");
            }
        }
        SceneControl sc = implentList.get(scene);
        plugin.initialize(sc);
        if (plugin.getPluginRoot() != null) {
            virtualSceneManager.getVirtualSceneRoot(scene).addChild(
                    plugin.getPluginRoot());
        }
        plugins.add(plugin);
    }
    
    private void removePlugin(SceneManagerPlugin plugin, String scene) {
        if (scene == null) {
	    scene = virtualSceneManager.getCurrentVScene();
	}
        if (plugins.contains(plugin) && plugin.isRemovable()) {
            if (plugin.getPluginRoot() != null)
                virtualSceneManager.getVirtualSceneRoot(scene).removeChild(
                        plugin.getPluginRoot());
            plugin.destroy();
            plugins.remove(plugin);
        }
    }
    
    private class AdvancedPluginConfigListener implements LgEventListener {
        
        public void processEvent(LgEvent evt) {
            SceneManagerPluginConfig config = (SceneManagerPluginConfig)evt;
            if(config.isSingletonPlugin()) {
                SceneManagerPlugin plugin = config.createPlugin();
                addPlugin(plugin, null);
            } else {
                for (String scene : getScenes(null)) {
                    SceneManagerPlugin plugin = config.createPlugin();
                    addPlugin(plugin, scene);
                }
            }
        }
        
        public Class[] getTargetEventClasses() {
            return new Class[] { SceneManagerPluginConfig.class };
        }
    }
    
    class AdvancedSceneControlImp implements AdvancedSceneControl {

	private String scene;

	public AdvancedSceneControlImp(String scene) {
	    this.scene = scene;
	}

	public AppContainer getAppContainer(int index) {
	    return AdvancedSceneManager.this.getAppContainer(scene, index);
	}

	public void setCurrentAppContainer(int index) {
	    AdvancedSceneManager.this.setCurrentAppContainer(scene, index);
	}

	public void swapAppContainer(int indexA, int indexB) {
	    AdvancedSceneManager.this.swapAppContainer(scene, indexA, indexB);
	}

	public void setBackground(Background background) {
	    AdvancedSceneManager.this.setBackground(scene, background);
	}

	public int getCurrentAppContainer() {
	    return AdvancedSceneManager.this.getCurrentAppContainer(scene);
	}

	String getScene() {
	    return scene;
	}

	public AdvancedSceneControl[] getSceneControls() {
	    return AdvancedSceneManager.this.getSceneControls();
	}

	public void addComponent3DTo(AdvancedSceneControl sceneCntrl,
		Component3D comp3d) {
	    AdvancedSceneManager.this.addComponentToScene((AdvancedSceneControlImp)sceneCntrl
		    , comp3d);
	}

	public AdvancedSceneControlImp getCurrentSceneControl() {
	    return AdvancedSceneManager.this.getSceneControl(null);
	}
    }
}
