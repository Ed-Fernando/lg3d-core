/***************************************************************************
 *   Project Looking Glass
 *
 *   $RCSfile: StartMenu.java,v $
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the
 *   Free Software Foundation, Inc.,
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 *   Author: Colin M. Bullock
 *   cmbullock@gmail.com
 *
 *   $Revision: 1.10 $
 *   $Date: 2006-05-09 08:52:35 $
 */
package org.jdesktop.lg3d.scenemanager.utils.startmenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.prefs.Preferences;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.vecmath.Vector3f;

import org.jdesktop.lg3d.scenemanager.utils.SceneControl;
import org.jdesktop.lg3d.scenemanager.utils.plugin.SceneManagerPlugin;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.data.MenuGroup;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.data.MenuItem;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.data.StartMenuData;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.event.MenuGroupAddedEvent;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.event.MenuGroupLinkAddedEvent;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.event.MenuItemAddedEvent;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.model.MenuGroupComponent;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.model.MenuGroupLinkComponent;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.model.MenuItemComponent;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.model.StartMenuModel;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.model.panel.PanelStartMenuModel;
import org.jdesktop.lg3d.scenemanager.utils.taskbar.TaskbarItemConfig;
import org.jdesktop.lg3d.utils.action.ActionBoolean;
import org.jdesktop.lg3d.utils.action.ActionNoArg;
import org.jdesktop.lg3d.utils.eventadapter.MouseClickedEventAdapter;
import org.jdesktop.lg3d.utils.eventadapter.MouseHoverEventAdapter;
import org.jdesktop.lg3d.utils.prefs.LgPreferencesHelper;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Tapp;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventConnector;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.MouseEvent3D.ButtonId;


/**
 * Main class for the start menu plugin. This loads the configuration
 * object, instantiates the selected 3D model, and initializes
 * the data object and configuration listeners.
 */
public class StartMenu implements SceneManagerPlugin {

    private static Logger logger = Logger.getLogger("lg.startmenu");

    /** The start menu data structure */
    private StartMenuData menuData;

    /** The start menu 3D interface */
    private StartMenuModel menuModel;

    /** The config file url */
    private String configUrl= null;

    // Used to allow asynchronous loading of links and their parent group
    private HashMap<String,ArrayList<MenuGroup>> groupLinkQueue=
        new HashMap<String,ArrayList<MenuGroup>>();

    // Used to allow asynchronous loading of items and their parent group
    private HashMap<String,ArrayList<MenuItem>> itemQueue=
        new HashMap<String,ArrayList<MenuItem>>();

    // Listeners for configuration events
    private LgEventListener startMenuConfigListener;
    private LgEventListener menuItemConfigListener;
    private LgEventListener menuGroupConfigListener;

    /**
     * The default constructor.
     */
    public StartMenu() {

    }

    /**
     * Get the menu data structure.
     * @return The menu data
     */
    public StartMenuData getMenuData() {
        return menuData;
    }

    /**
     * Get the menu interface component.
     * @return The menu model
     */
    public StartMenuModel getMenuModel() {
        return menuModel;
    }

    /**
     * This signifies that the menu plugin may be removed at runtime.
     * @return <code>true</code>
     * @see SceneManagerPlugin#isRemovable()
     */
    public boolean isRemovable() {
        return true;
    }

    /**
     * Get the root container for this plugin. Since the link to
     * the start menu is handled by the taskbar, there is no
     * root container.
     */
    public Component3D getPluginRoot() {
        return null;
    }

    /**
     * Initialize the start menu. This first loads the start menu
     * configuration file, then parses the menu data file and populates the
     * menu with groups and items. Finally the 3D interface model is created
     * and initialized.
     */
    public void initialize(SceneControl sceneControl) {
        logger.info("Initializing Start Menu...");
        
        Preferences prefs = LgPreferencesHelper.userNodeForPackage(getClass());
        
        // Initialize menu data (will be loaded from config events)
        menuData = new StartMenuData();
        
//        // Inizialize menu model from the Preferences
//        String modelClassName = prefs.get("model.class", null);
//        
//        try {
//            Class menuModelClass = Class.forName(modelClassName);
//            if(StartMenuModel.class.isAssignableFrom(menuModelClass)) {
//                menuModel= (StartMenuModel)menuModelClass.newInstance();
//            } else {
//                logger.warning("Invalid Start Menu model class: " + modelClassName);
//                logger.warning("Using default model");
//                menuModel= new PanelStartMenuModel();
//            }
//            logger.info("Initilizing Start Menu model: " + modelClassName);
//            menuModel.initialize();
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, "Failed to initialize Start Menu model: " + modelClassName, e);
//        }

        // Listen for start menu config events
        startMenuConfigListener= new LgEventListener() {
            public void processEvent(LgEvent evt) {
                StartMenuConfig config= (StartMenuConfig)evt;
                try {
                    Class menuModelClass= Class.forName(config.getMenuModelClass());
                    if(StartMenuModel.class.isAssignableFrom(menuModelClass)) {
                        menuModel= (StartMenuModel)menuModelClass.newInstance();
                    } else {
                        logger.log(Level.WARNING, "Invalid menu model class: " + config.getMenuModelClass());
                        logger.log(Level.WARNING, "Using default model");
                        menuModel= new PanelStartMenuModel();
                    }
                    logger.log(Level.CONFIG, "Initilizing start menu model model: " + config.getMenuModelClass());
                    menuModel.initialize();
                } catch(Exception e) {
                    logger.log(Level.SEVERE, "Could not load menu model class " + config.getMenuModelClass(), e);
                }
            }
            public Class[] getTargetEventClasses() {
                return new Class[]{ StartMenuConfig.class };
            }
        };
        LgEventConnector.getLgEventConnector().addListener(
                LgEventSource.ALL_SOURCES, startMenuConfigListener);
        
        
        // Listen for menu item configuration events
        menuItemConfigListener= new LgEventListener() {
                public void processEvent(LgEvent evt) {
                    StartMenuItemConfig config= (StartMenuItemConfig)evt;
                    MenuItem item= config.createItem();
                    logger.log(Level.FINE, "Handling item configuration event: " + item.getName()+" url="+item.getDisplayResource().getUrl());
                    MenuGroup parentGroup= menuData.getGroup(config.getMenuGroup());
                    if(parentGroup != null) { // Group has already been configured
                        parentGroup.addItem(item);
                        // Post item added event for model
                        LgEventConnector.getLgEventConnector().postEvent(
                                new MenuItemAddedEvent(item, parentGroup), null);
                    } else { // Wait for parent group to be configured
                        ArrayList<MenuItem> items= itemQueue.get(config.getMenuGroup());
                        if(items == null) {
                            items= new ArrayList<MenuItem>();
                            itemQueue.put(config.getMenuGroup(), items);
                        }
                        items.add(item);
                    }
                }

                public Class<LgEvent>[] getTargetEventClasses() {
                    return new Class[]{ StartMenuItemConfig.class };
                }
            };
        LgEventConnector.getLgEventConnector().addListener(
                LgEventSource.ALL_SOURCES, menuItemConfigListener);

        // Listen for menu group configuration events
        menuGroupConfigListener= new LgEventListener() {
                public void processEvent(LgEvent evt) {
                    StartMenuGroupConfig config= (StartMenuGroupConfig)evt;
                    MenuGroup group= config.createGroup();
                    logger.log(Level.FINE, "Handling group config event: " + group.getName());
                    menuData.addGroup(group, config.isDefaultGroup());

                    // Post group added event for model
                    LgEventConnector.getLgEventConnector().postEvent(
                            new MenuGroupAddedEvent(group, config.isDefaultGroup()), null);

                    // Process group links
                    for(String groupLink : config.getGroupLinks()) {
                        MenuGroup remoteGroup= menuData.getGroup(groupLink);
                        if(remoteGroup != null) {
                            menuData.linkGroups(group, remoteGroup);
                            // Post group link added event for model
                            LgEventConnector.getLgEventConnector().postEvent(
                                    new MenuGroupLinkAddedEvent(group, remoteGroup), null);
                        } else { // Remote group has not been created yet
                            ArrayList<MenuGroup> groupLinks= groupLinkQueue.get(groupLink);
                            if(groupLinks == null) {
                                groupLinks= new ArrayList<MenuGroup>();
                                groupLinkQueue.put(groupLink, groupLinks);
                            }
                            groupLinks.add(group);
                        }
                    }

                    // Process links in queue waiting on this group
                    ArrayList<MenuGroup> groupLinks= groupLinkQueue.remove(group.getName());
                    if(groupLinks != null) {
                        for(MenuGroup remoteGroup : groupLinks) {
                            menuData.linkGroups(remoteGroup, group);
                            // Post group link added event for model
                        LgEventConnector.getLgEventConnector().postEvent(
                                    new MenuGroupLinkAddedEvent(remoteGroup, group), null);
                        }
                    }

                    // Process items in queue waiting on this group
                    ArrayList<MenuItem> items= itemQueue.remove(group.getName());
                    if(items != null) {
                        for(MenuItem item : items) {
                            group.addItem(item);
                            // Post item added event for model
                            LgEventConnector.getLgEventConnector().postEvent(
                                    new MenuItemAddedEvent(item, group), null);
                        }
                    }
                }

                public Class<LgEvent>[] getTargetEventClasses() {
                    return new Class[]{ StartMenuGroupConfig.class };
                }
            };
        LgEventConnector.getLgEventConnector().addListener(
                LgEventSource.ALL_SOURCES, menuGroupConfigListener);

        // Post the taskbar item event
        LgEventConnector.getLgEventConnector().postEvent(
                new TaskbarItemConfig() {
                    @Override
                    public Tapp createItem() {
                        final Tapp tapp= new Tapp();
                        tapp.setPreferredSize(new Vector3f(0.0175f, 0.0f, 0.015f));
                        tapp.addChild(menuModel);
                        tapp.addListener(
                            new MouseHoverEventAdapter(0, 500, 0,
                                new ActionBoolean() {
                                    public void performAction(LgEventSource source, boolean entered) {
                                        if (entered && !menuModel.isVisible()) {
                                            menuModel.changeVisible(true, false);
                                        } else if (!entered && menuModel.isVisible()) {
                                            menuModel.changeVisible(false, false);
                                        }
                                    }
                                }));
                        return tapp;
                    }
                    @Override
                    public int getItemIndex() {
                        return 0;
                    }
                }, null);
    }

    /**
     * Unload and destroy the start menu plugin.
     * @see SceneManagerPlugin#destroy()
     */
    public void destroy() {
        menuModel.hideMenu();
        LgEventConnector.getLgEventConnector().removeListener(
                LgEventSource.ALL_SOURCES, menuItemConfigListener);
        LgEventConnector.getLgEventConnector().removeListener(
                LgEventSource.ALL_SOURCES, menuGroupConfigListener);
    }

}
