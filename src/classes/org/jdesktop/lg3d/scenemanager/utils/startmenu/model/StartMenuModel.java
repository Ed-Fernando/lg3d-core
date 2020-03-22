/***************************************************************************
 *   Project Looking Glass
 *
 *   $RCSfile: StartMenuModel.java,v $
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
 *   $Revision: 1.4 $
 *   $Date: 2006-08-14 23:13:26 $
 */
package org.jdesktop.lg3d.scenemanager.utils.startmenu.model;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdesktop.lg3d.scenemanager.utils.startmenu.data.MenuGroup;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.data.MenuItem;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.event.EditingMenuGroupEvent;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.event.EditingMenuGroupLinkEvent;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.event.EditingMenuItemEvent;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.event.MenuGroupAddedEvent;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.event.MenuGroupChangeEvent;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.event.MenuGroupLinkAddedEvent;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.event.MenuItemAddedEvent;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.event.MenuItemExecutedEvent;
import org.jdesktop.lg3d.utils.action.ActionInt;
import org.jdesktop.lg3d.utils.action.ActionNoArg;
import org.jdesktop.lg3d.utils.c3danimation.NaturalMotionAnimation;
import org.jdesktop.lg3d.utils.eventadapter.MouseClickedEventAdapter;
import org.jdesktop.lg3d.utils.eventadapter.MouseWheelEventAdapter;
import org.jdesktop.lg3d.wg.Container3D;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventConnector;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.MouseEvent3D.ButtonId;

/**
 * The main class for defining a 3D model for the start menu. This class defines
 * methods for constructing menu components and responding to user events.
 * It also provides listeners for model-related events fired by other
 * components. New menu model implementations should define the abstract
 * methods provided by this class, which will be called back to by the listeners.
 * <br/><br/>
 * Given a concrete subclass, the model instantiates itself and its subcomponents
 * using reflection and a common naming sceme:<br/>
 * <ul>
 * <li><code>com.mypackage.MyStartMenuModel extends StartMenuModel</code>
 * <li><code>com.mypackage.MyMenuGroupComponent extends MenuGroupComponent</code>
 * <li><code>com.mypackage.MyMenuGroupLinkComponent extends MenuGroupLinkComponent</code>
 * <li><code>com.mypackage.MyMenuItemComponent extends MenuItemComponent</code>
 * </ul>
 * If this naming pattern is followed for each concrete model implementation, no
 * futher work is necessary to load the model.
 */
public abstract class StartMenuModel extends Container3D {

    protected static final Logger logger = Logger.getLogger("lg.startmenu");

    /** The current group component */
    protected MenuGroupComponent currentGroupComp= null;

    /** The default group component */
    protected MenuGroupComponent defaultGroupComp= null;

    /** The components for menu items */
    protected Map<MenuItem,MenuItemComponent> menuItemComps=
        new HashMap<MenuItem,MenuItemComponent>();

    /** The components for menu groups */
    protected Map<MenuGroup,MenuGroupComponent> menuGroupComps=
        new HashMap<MenuGroup,MenuGroupComponent>();

    /** The components for menu group links */
    protected Map<MenuGroupLinkKey,MenuGroupLinkComponent> menuGroupLinkComps=
        new HashMap<MenuGroupLinkKey,MenuGroupLinkComponent>();

    /** The class of the menu item components */
    private Class itemCompClass;

    /** The class of the menu group components */
    private Class groupCompClass;

    /** The class of the menu group link components */
    private Class groupLinkCompClass;

    protected boolean visible;
    
    /**
     * Default constructor. This attaches the model listeners
     * and then calls <code>initialize()</code> for the
     * specific model sub-class.
     */
    public StartMenuModel() {
        // Calculate component class names
        String modelClassName= getClass().getName();
        String modelPrefix= modelClassName.substring(0,
                modelClassName.indexOf("StartMenuModel",
                        modelClassName.lastIndexOf('.')));
        logger.log(Level.CONFIG, "Loading model classes for " + modelPrefix);
        try {
            itemCompClass= Class.forName(modelPrefix + "MenuItemComponent");
            groupCompClass= Class.forName(modelPrefix + "MenuGroupComponent");
            groupLinkCompClass= Class.forName(modelPrefix + "MenuGroupLinkComponent");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not load component classes", e);
            e.printStackTrace();
        }

        // Process new items added to the menu
        LgEventConnector.getLgEventConnector().addListener(
                LgEventSource.ALL_SOURCES,
                new LgEventListener() {
                    public void processEvent(LgEvent evt) {
                        MenuItemAddedEvent event= (MenuItemAddedEvent)evt;
                        addMenuItem(event.getItem(), event.getParentGroup());
                    }
                    public Class<LgEvent>[] getTargetEventClasses() {
                        return new Class[]{ MenuItemAddedEvent.class };
                    }
                });

        // Process new groups added to the menu
        LgEventConnector.getLgEventConnector().addListener(
                LgEventSource.ALL_SOURCES,
                new LgEventListener() {
                    public void processEvent(LgEvent evt) {
                        MenuGroupAddedEvent event= (MenuGroupAddedEvent)evt;
                        addMenuGroup(event.getGroup(), event.isDefaultGroup());
                    }
                    public Class<LgEvent>[] getTargetEventClasses() {
                        return new Class[]{ MenuGroupAddedEvent.class };
                    }
                });

        // Process new group links added to the menu
        LgEventConnector.getLgEventConnector().addListener(
                LgEventSource.ALL_SOURCES,
                new LgEventListener() {
                    public void processEvent(LgEvent evt) {
                        MenuGroupLinkAddedEvent event= (MenuGroupLinkAddedEvent)evt;
                        addMenuGroupLink(event.getLocalGroup(), event.getRemoteGroup());
                    }
                    public Class<LgEvent>[] getTargetEventClasses() {
                        return new Class[]{ MenuGroupLinkAddedEvent.class };
                    }
                });

        // TODO: Buttons should be configurable
        // Listen for mouse scroll events and cycle the menu items
        LgEventConnector.getLgEventConnector().addListener(MenuGroupComponent.class,
                new MouseWheelEventAdapter(new ActionInt() {
                    public void performAction(LgEventSource source, int value) {
                        cycleItems(value);
                    }
                }));

        // Listen for menu item execution events
        LgEventConnector.getLgEventConnector().addListener(LgEventSource.ALL_SOURCES,
                new LgEventListener() {
                    public void processEvent(LgEvent evt) {
                        MenuItemExecutedEvent event= (MenuItemExecutedEvent)evt;
                        menuItemExecuted(event.getItem());
                    }
                    public Class<LgEvent>[] getTargetEventClasses() {
                        return new Class[]{ MenuItemExecutedEvent.class };
                    }
                });

        // Listen for change of current menu group events
        LgEventConnector.getLgEventConnector().addListener(LgEventSource.ALL_SOURCES,
                new LgEventListener() {
                    public void processEvent(LgEvent evt) {
                        MenuGroupChangeEvent event= (MenuGroupChangeEvent)evt;
                        changeGroup(event.getNewGroup());
                    }
                    public Class<LgEvent>[] getTargetEventClasses() {
                        return new Class[]{ MenuGroupChangeEvent.class };
                    }
                });

        // Listen for editing menu group events
        LgEventConnector.getLgEventConnector().addListener(LgEventSource.ALL_SOURCES,
                new LgEventListener() {
                    public void processEvent(LgEvent evt) {
                        EditingMenuGroupEvent event= (EditingMenuGroupEvent)evt;
                        editMenuGroup(event.getGroup());
                    }
                    public Class<LgEvent>[] getTargetEventClasses() {
                        return new Class[]{ EditingMenuGroupEvent.class };
                    }
                });

        // Listen for editing menu group link events
        LgEventConnector.getLgEventConnector().addListener(LgEventSource.ALL_SOURCES,
                new LgEventListener() {
                    public void processEvent(LgEvent evt) {
                        EditingMenuGroupLinkEvent event= (EditingMenuGroupLinkEvent)evt;
                        editMenuGroupLink(event.getLocalGroup(), event.getRemoteGroup());
                    }
                    public Class<LgEvent>[] getTargetEventClasses() {
                        return new Class[]{ EditingMenuGroupLinkEvent.class };
                    }
                });

        // Listen for editing menu item events
        LgEventConnector.getLgEventConnector().addListener(LgEventSource.ALL_SOURCES,
                new LgEventListener() {
                    public void processEvent(LgEvent evt) {
                        EditingMenuItemEvent event= (EditingMenuItemEvent)evt;
                        editMenuItem(event.getItem());
                    }
                    public Class<LgEvent>[] getTargetEventClasses() {
                        return new Class[]{ EditingMenuItemEvent.class };
                    }
                });
    }

    /**
     * Initialization method called once when the menu is
     * loaded. This method may be optionally overridden
     * in sub-classes to provide any necessary
     * initialization, such as adding custom event listeners.
     */
    public abstract void initialize();
    
    /**
     * Invoked whenever the model's pickable redion size needs to be changed.
     */
    public abstract void setPickableRegionSize(MenuGroupComponent currentGroupComp);
    
    /**
     * Get the component for the menu item. A menu item component
     * represents an individual executable link in the menu, and is
     * always a leaf node.
     * @param item The menu item
     * @return The menu item component
     */
    public MenuItemComponent getMenuItemComponent(MenuItem item) {
        logger.log(Level.FINER, "Getting component for menu item " + item);
        MenuItemComponent comp= menuItemComps.get(item);
        if(comp == null) {
            try {
                Constructor cons= itemCompClass.getConstructor(
                        new Class[]{ item.getClass() });
                comp = (MenuItemComponent)cons.newInstance(new Object[] { item });
                comp.setAnimation(new NaturalMotionAnimation(400));
                menuItemComps.put(item, comp);
            } catch (Exception e) {
                logger.log(Level.WARNING, "Error instantiating item component " + item.getName(), e);
                e.printStackTrace();
            }
        }

        return comp;
    }

    /**
     * Get the component for the menu group. A menu group component
     * represents a category or folder in the menu, and should be
     * responsible for the items and links to other groups contained within.
     * @param group The menu group
     * @return The menu group component
     */
    public MenuGroupComponent getMenuGroupComponent(MenuGroup group) {
        logger.log(Level.FINER, "Getting component for menu item " + group);
        MenuGroupComponent comp= menuGroupComps.get(group);
        if(comp == null) {
            try {
                Constructor cons= groupCompClass.getConstructor(
                        new Class[]{ group.getClass() });
                comp= (MenuGroupComponent)cons.newInstance(new Object[] { group });
                comp.setAnimation(new NaturalMotionAnimation(400));
                menuGroupComps.put(group, comp);
            } catch (Exception e) {
                logger.log(Level.WARNING, "Error instantiating group component " + group.getName(), e);
                e.printStackTrace();
            }
        }

        return comp;
    }

    /**
     * Get the component for the link between two groups. A menu group
     * link component represents the link from one menu group or category to another
     * adjacent group in the graph.
     * @param localGroup The local group
     * @param remoteGroup The remote group
     * @return The group link component
     */
    public MenuGroupLinkComponent getMenuGroupLinkComponent(MenuGroup localGroup, MenuGroup remoteGroup) {
        logger.log(Level.FINE, "Getting component for menu group link " + localGroup + " -> " + remoteGroup);
        MenuGroupLinkKey key= new MenuGroupLinkKey(localGroup, remoteGroup);
        MenuGroupLinkComponent comp= menuGroupLinkComps.get(key);
        if(comp == null) {
            try {
                Constructor cons= groupLinkCompClass.getConstructor(
                        new Class[]{ localGroup.getClass(), remoteGroup.getClass() });
                comp= (MenuGroupLinkComponent)cons.newInstance(
                        new Object[] { localGroup, remoteGroup });
                comp.setAnimation(new NaturalMotionAnimation(400));
                menuGroupLinkComps.put(key, comp);
            } catch (Exception e) {
                logger.log(Level.WARNING, "Error instantiating group link component " + localGroup.getName() + " -> " + remoteGroup.getName(), e);
                e.printStackTrace();
            }
        }

        return comp;
    }

    /**
     * Add a new item to the menu.
     * @param item The menu item
     */
    public void addMenuItem(MenuItem item, MenuGroup parentGroup) {
        if(menuItemComps.get(item) == null) {
            MenuGroupComponent groupComp= getMenuGroupComponent(parentGroup);
            MenuItemComponent itemComp= getMenuItemComponent(item);
            groupComp.addChildItem(itemComp);
        }
    }

    /**
     * Add a new group to the menu.
     * @param group The menu group
     */
    public void addMenuGroup(MenuGroup group, boolean isDefaultGroup) {
        if(menuGroupComps.get(group) == null) {
            MenuGroupComponent comp= getMenuGroupComponent(group);
            if(defaultGroupComp == null || isDefaultGroup) {
                defaultGroupComp= comp;

                addChild(comp);
                currentGroupComp = comp;
            }
        }
    }

    /**
     * Utility method to add a non-default group.
     * @param group The menu group
     */
    public void addMenuGroup(MenuGroup group) {
        addMenuGroup(group, false);
    }

    /**
     * Add a new link between two menu groups
     * @param localGroup The local menu group
     * @param remoteGroup The remote menu group
     */
    public void addMenuGroupLink(MenuGroup localGroup, MenuGroup remoteGroup) {
        MenuGroupLinkKey key= new MenuGroupLinkKey(localGroup, remoteGroup);
        if(menuGroupLinkComps.get(key) == null) {
            MenuGroupComponent groupComp= getMenuGroupComponent(localGroup);
            MenuGroupLinkComponent groupLinkComp= getMenuGroupLinkComponent(localGroup, remoteGroup);
            groupComp.addChildGroupLink(groupLinkComp);
        }
    }

    /**
     * Edit a menu item. This method should handle the necessary dialogs
     * for editing or deleting a menu item.
     * @param item The menu item
     * FIXME: Not implemented
     */
    public void editMenuItem(MenuItem item) {
        logger.log(Level.FINE, "Editing menu item " + item);
        // TODO Dialog for editing a menu item
        throw new RuntimeException("Not Implemented");
    }

    /**
     * Edit a menu group. This method should handle the necessary dialogs
     * for editing or deleting a menu group.
     * @param group The menu group
     * FIXME: Not implemented
     */
    public void editMenuGroup(MenuGroup group) {
        logger.log(Level.FINE, "Editing menu group " + group);
        // TODO Dialog for editing a menu group
        throw new RuntimeException("Not Implemented");
    }

    /**
     * Edit a menu group link. This method should handle the necessary dialogs
     * for editing or deleting a menu group link.
     * @param localGroup The local group
     * @param remoteGroup The remote group
     * FIXME: Not implemented
     */
    public void editMenuGroupLink(MenuGroup localGroup, MenuGroup remoteGroup) {
        logger.log(Level.FINE, "Editing menu group link " + localGroup + " -> " + remoteGroup);
        // TODO Dialog for editing a menu group link
        throw new RuntimeException("Not Implemented");
    }

    /**
     * Edit the menu properties. This method should handle the necessary dialogs
     * for editing menu properties, such as data file location and model plugin.
     * FIXME: Not Implemented
     */
    public void editMenuProperties() {
        logger.log(Level.FINE, "Editing start menu properties");
        // TODO Dialog for editing the start menu properties
        throw new RuntimeException("Not Implemented");
    }

    /**
     * Callback for menu item execution. The method is called after the item
     * has been started in a new thread, and should be responsible for
     * any animations or clean up in response.
     * @param item The item being executed
     */
    public void menuItemExecuted(MenuItem item) {
        logger.log(Level.FINE, "Executing menu item " + item);
        hideMenu();
    }

    /**
     * Cycle through the items in the current menu group. What exactly
     * 'cycle' means is dependant on the implementation and style
     * of the particular model. The default implementation delegates
     * this action to the current group component.
     * @param clicks
     */
    public void cycleItems(int clicks) {
        logger.log(Level.FINE, "Cyclying items");
        currentGroupComp.cycleItems(clicks);
    }

    /**
     * Change the menu group currently displayed.
     * @param newGroup The group to change to
     */
    public void changeGroup(MenuGroup newGroup) {
        logger.log(Level.FINE, "Changing menu groups");

        // Hide and disable current items and groups
        if(currentGroupComp != null) {
            removeChild(currentGroupComp);
        }

        // Change current group
        currentGroupComp= getMenuGroupComponent(newGroup);
        addChild(currentGroupComp);
        changeVisible(true);
    }

    /**
     * Hide the start menu to the taskbar.
     */
    public void hideMenu() {
        logger.log(Level.FINE, "Hiding start menu");
        changeVisible(false);
    }

    /**
     * Raise the start menu from the taskbar.
     */
    public void raiseMenu() {
        logger.log(Level.INFO, "Raising start menu");

        if(currentGroupComp == null) { // First time raiseMenu is called
            logger.log(Level.FINE, "No current group; using default group");
            currentGroupComp= defaultGroupComp;
        }

        changeVisible(true);
    }

    /**
     * Equivalent to <code>changeVisible(visible, true)</code>
     * @param visible <code>true</code> to raise the menu, or <code>false</code> to hide it
     * @see initialize(boolean, boolean)
     */
    public void changeVisible(boolean visible) {
        changeVisible(visible, true);
    }

    /**
     * Change the visiblity of the menu (raise or hide it). The
     * second parameter controls whether or not the additional
     * raise/hide animations should be performed.
     * @param visible <code>true</code> to raise the menu, or <code>false</code> to hide it
     * @param redoAnim <code>true</code> to perform additional animations
     */
    public void changeVisible(boolean visible, boolean redoAnim) {
        this.visible = visible;
        if (visible) {
            if (redoAnim) {
                setScale(0.25f);
            }
            changeScale(1.0f);
            changeTranslation(-0.005f, 0.015f, 0.02f);
            setMouseEventEnabled(true);
            
            // reset the pickable region size
            setPickableRegionSize(currentGroupComp);
        } else {
            changeScale(0.25f, 500);
            changeTranslation(-0.005f, 0.0f, 0.0f);
            if (redoAnim) {
                setRotationAngle((float)(Math.toRadians(40) - Math.PI * 2));
                changeRotationAngle((float)Math.toRadians(40), 1000);
            }
            setMouseEventEnabled(false);
            setPickableRegionSize(null);
        }
    }

    /**
     * Check whether or not the menu is currently visible (raised)
     * @return <code>true</code> if the menu is raised
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Allows subclass models to set the internal visibility state
     * without performing the associated animations.
     * @param visible The visibility state
     */
    protected void setVisibleInteral(boolean visible) {
        this.visible= visible;
    }

    /**
     * Utility class used to map a group link (which consists of two groups)
     * to a single component
     */
    protected static class MenuGroupLinkKey {

        private MenuGroup one;

        private MenuGroup two;

        public MenuGroupLinkKey(MenuGroup one, MenuGroup two) {
            this.one= one;
            this.two= two;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj != null && obj instanceof MenuGroupLinkKey) {
                MenuGroupLinkKey other= (MenuGroupLinkKey)obj;
                return other.one.equals(this.one) && other.two.equals(this.two);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return (one.getName() + "<->" + two.getName()).hashCode();
        }

    }

}
