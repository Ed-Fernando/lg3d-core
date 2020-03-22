/***************************************************************************
 *   Project Looking Glass
 *
 *   $RCSfile: MenuGroupComponent.java,v $
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
 *   $Revision: 1.1 $
 *   $Date: 2005-12-02 17:06:51 $
 */
package org.jdesktop.lg3d.scenemanager.utils.startmenu.model;

import org.jdesktop.lg3d.scenemanager.utils.startmenu.action.EditMenuGroupAction;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.data.MenuGroup;
import org.jdesktop.lg3d.utils.eventadapter.MouseClickedEventAdapter;
import org.jdesktop.lg3d.wg.Container3D;
import org.jdesktop.lg3d.wg.event.MouseWheelEvent3D;
import org.jdesktop.lg3d.wg.event.MouseEvent3D.ButtonId;

/**
 * Abstract component for a start menu group
 */
public abstract class MenuGroupComponent extends Container3D {
    
    /** The menu group */
    protected MenuGroup menuGroup;
    
    /**
     * Create the component for the given menu group
     * @param menuGroup The menu group
     */
    public MenuGroupComponent(MenuGroup menuGroup) {
        this.menuGroup= menuGroup;

        initialize();
        
        // TODO: Mouse buttons, etc. should be customizable
        
        // Edit a menu group when clicked on with Button 2
        addListener(new MouseClickedEventAdapter(ButtonId.BUTTON2, new EditMenuGroupAction(menuGroup)));
        
        setMouseEventSource(MouseWheelEvent3D.class, true);
    }
    
    /**
     * Construct the menu group component
     */
    public abstract void initialize();
    
    /**
     * Get the menu group
     * @return The menu group
     */
    public MenuGroup getMenuGroup() {
        return menuGroup;
    }
    
    /**
     * Add a menu item component to this group component. The default action
     * is to simply add the item as a child component, but sub-classes may
     * override this if they desire more complex behavior.
     * @param comp The menu item component
     */
    public void addChildItem(MenuItemComponent comp) {
        addChild(comp);
    }
    
    /**
     * Add a menu group link component to this group component. The default
     * action is to simply add the group link as a child component, but
     * sub-classes may override this if they desire more complex behaivor. 
     * @param comp
     */
    public void addChildGroupLink(MenuGroupLinkComponent comp) {
        addChild(comp);
    }
    
    /**
     * Cycle the items and group links for this group.
     * The default implementation does nothing.
     * @param clicks The number of increments to cycle
     */
    public void cycleItems(int clicks) {
        
    }
    
}
