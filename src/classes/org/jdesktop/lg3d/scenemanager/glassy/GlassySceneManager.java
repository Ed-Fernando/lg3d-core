/**
 * Project Looking Glass
 *
 * $RCSfile: GlassySceneManager.java,v $
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
 * $Date: 2006-08-14 23:13:22 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.glassy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Logger;

import org.jdesktop.lg3d.scenemanager.utils.SceneControl;
import org.jdesktop.lg3d.scenemanager.utils.SceneManagerBase;
import org.jdesktop.lg3d.scenemanager.utils.appcontainer.AppContainer;
import org.jdesktop.lg3d.scenemanager.utils.appcontainer.StandardAppContainer;
import org.jdesktop.lg3d.scenemanager.utils.background.Background;
import org.jdesktop.lg3d.scenemanager.utils.event.BackgroundChangeRequestEvent;
import org.jdesktop.lg3d.scenemanager.utils.plugin.SceneManagerPlugin;
import org.jdesktop.lg3d.scenemanager.utils.plugin.SceneManagerPluginConfig;
import org.jdesktop.lg3d.wg.Frame3D;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventConnector;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.wg.event.MouseButtonEvent3D;
import org.jdesktop.lg3d.wg.event.LgEventSource;


/**
 * The default LG Scenemanage
 */
public class GlassySceneManager extends SceneManagerBase {
    protected static final Logger logger = Logger.getLogger("lg.scenemanager");

    private Class appContainerClass;
    private ArrayList<AppContainer> appContainers = new ArrayList<AppContainer>();
    private int currentAppContainer = 0;
    private SceneControl sceneControl;
    private Background currentBackground;
    private HashSet<SceneManagerPlugin> plugins = new HashSet<SceneManagerPlugin>();
    
    @Override
    protected void initialize() {                
        sceneControl = new SceneControlImp();
        
        // set the very initial AppContainer
        appContainerClass = StandardAppContainer.class;
        getSceneRoot().addChild(getAppContainer(0));
        
        initializePluginListener();
        
        LgEventConnector.getLgEventConnector().addListener(
            LgEventSource.ALL_SOURCES, new LgEventListener() {
                public void processEvent(final LgEvent event) {
                    BackgroundChangeRequestEvent bgcre = (BackgroundChangeRequestEvent)event;
                    Background background = bgcre.getBackground();
                    setBackground(background);
                }

                public Class<LgEvent>[] getTargetEventClasses() {
                    return new Class[] {BackgroundChangeRequestEvent.class};
                }
            }); 
    }
    
    private SceneControl getSceneControl() {
        return sceneControl;
    }
    
    private void setBackground(Background background) {
        if (currentBackground != null) {
            currentBackground.detach();
            currentBackground.setEnabled(false);
        }
        currentBackground = background;
        currentBackground.initialize(getSceneControl());
        // Make the background event source so that background clicking
        // action generates events originated from the background object.
        // Such events may be used by other part of the SceneManager.
        currentBackground.setMouseEventSource(MouseButtonEvent3D.class, true);
        currentBackground.setEnabled(true);
        // FIXME -- should getSceneRoot's return type be Container3D?
        getSceneRoot().addChild(currentBackground);
    }
    
    private AppContainer getAppContainer(int index) {
       int size = appContainers.size();
       if (index >= size) {
           for (int i = size; i <= index; i++) {
               appContainers.add(null);
           }
       }
       assert(appContainers.size() > index);
       AppContainer ac = appContainers.get(index);
       if (ac == null) {
           try {
               ac = (AppContainer)appContainerClass.newInstance();
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
       
       return ac;
    }
   
    /*
     * Note -- this doesn't add the AppContainer to the scenegraph.
     * It is caller's responsibility to do it using getAppContainer().
     */
    private void setCurrentAppContainer(int index) {
	if (index < 0 || index > appContainers.size()) {
	    throw new IllegalArgumentException("illegal index: " + index);
	}
	for (AppContainer ac : appContainers) {
	    ac.setEnabled(false);
	}
	appContainers.get(index).setEnabled(true);
	currentAppContainer = index;
    }

    private int getCurrentAppContainer() {
	return currentAppContainer;
    }

    private void swapAppContainer(int indexA, int indexB) {
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
    
    @Override
    public void addFrame3D(Frame3D frame3d) {
        appContainers.get(currentAppContainer).addFrame3D(frame3d);
    }

    @Override
    public void removeFrame3D(Frame3D frame3d) {
        appContainers.get(currentAppContainer).removeFrame3D(frame3d);
    }
    
    private void initializePluginListener() {
        LgEventConnector.getLgEventConnector().addListener(
                LgEventSource.ALL_SOURCES,
                new PluginConfigListener());
    }
    
    @Override
    public void addPlugin(SceneManagerPlugin plugin) {
        if (plugins.contains(plugin)) {
            if (plugin.isRemovable()) {
                removePlugin(plugin);
            } else {
                throw new IllegalArgumentException(plugin 
                        + " plugin is already registerd and is not removable");
            }
        }
        
        plugin.initialize(getSceneControl());
        if (plugin.getPluginRoot() != null) {
            getSceneRoot().addChild(plugin.getPluginRoot());
        }
        plugins.add(plugin);
    }
    
    @Override
    public void removePlugin(SceneManagerPlugin plugin) {
        if (plugins.contains(plugin) && plugin.isRemovable()) {
            if (plugin.getPluginRoot() != null) {
                getSceneRoot().removeChild(plugin.getPluginRoot());
            }
            plugin.destroy();
            plugins.remove(plugin);
        }
    }
    
    private class SceneControlImp implements SceneControl {

        public SceneControlImp() {
        }
	
        public AppContainer getAppContainer(int index) {
            return GlassySceneManager.this.getAppContainer(index);
        }

        public void setCurrentAppContainer(int index) {
            GlassySceneManager.this.setCurrentAppContainer(index);
        }

        public void swapAppContainer(int indexA, int indexB) {
            GlassySceneManager.this.swapAppContainer(indexA, indexB);
        }

        public void setBackground(Background background) {
            GlassySceneManager.this.setBackground(background);
        }

        public int getCurrentAppContainer() {
            return GlassySceneManager.this.getCurrentAppContainer();
        }
    }
    
    private class PluginConfigListener implements LgEventListener {
        public void processEvent(LgEvent evt) {
            SceneManagerPluginConfig config = (SceneManagerPluginConfig)evt;
            SceneManagerPlugin plugin = config.createPlugin();
            if (plugin != null) {
                addPlugin(plugin);
            }
        }
        
        public Class[] getTargetEventClasses() {
            return new Class[]{ SceneManagerPluginConfig.class };
        }
    }
}
