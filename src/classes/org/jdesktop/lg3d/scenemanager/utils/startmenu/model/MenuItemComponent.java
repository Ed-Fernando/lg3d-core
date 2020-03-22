/***************************************************************************
 *   Project Looking Glass
 *
 *   $RCSfile: MenuItemComponent.java,v $
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
 *   $Date: 2005-12-02 17:06:52 $
 */
package org.jdesktop.lg3d.scenemanager.utils.startmenu.model;

import javax.vecmath.Vector3f;

import org.jdesktop.lg3d.scenemanager.utils.startmenu.action.EditMenuItemAction;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.action.ExecuteItemAction;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.data.MenuItem;
import org.jdesktop.lg3d.utils.action.ActionBoolean;
import org.jdesktop.lg3d.utils.c3danimation.NaturalMotionAnimation;
import org.jdesktop.lg3d.utils.eventadapter.MouseClickedEventAdapter;
import org.jdesktop.lg3d.utils.eventadapter.MouseEnteredEventAdapter;
import org.jdesktop.lg3d.wg.Container3D;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.MouseEvent3D.ButtonId;

/**
 * Abstract componenent for a start menu item
 */
public abstract class MenuItemComponent extends Container3D {

    /** The menu item */
    protected MenuItem menuItem;

    /**
     * Create the component for the given menu item
     * @param menuItem The menu item
     */
    public MenuItemComponent(MenuItem menuItem) {
        this.menuItem= menuItem;
        initialize();

        // TODO: Mouse buttons, etc. should be customizable

        // Execute a menu item when clicked on with Button 1
        addListener(new MouseClickedEventAdapter(ButtonId.BUTTON1, new ExecuteItemAction(menuItem)));
        // Edit a menu item when clicked on with Button 2
        addListener(new MouseClickedEventAdapter(ButtonId.BUTTON2, new EditMenuItemAction(menuItem)));

        setAnimation(new NaturalMotionAnimation(300));
        addListener(new MouseEnteredEventAdapter(new ActionBoolean() {
            public void performAction(LgEventSource source, boolean state) {
                if(state) {
                    changeScale(1.1f, 150);
                } else {
                    changeScale(1.0f, 150);
                }
            }
        }));

        setMouseEventPropagatable(true);
    }

    /**
     * Construct the menu item component
     */
    public abstract void initialize();

    /**
     * Get the menu item
     * @return Returns the menu item
     */
    public MenuItem getMenuItem() {
        return menuItem;
    }

}
