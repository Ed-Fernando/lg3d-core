/***************************************************************************
 *   Project Looking Glass
 *
 *   $RCSfile: MenuGroupLinkComponent.java,v $
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
 *   $Date: 2005-12-02 17:06:50 $
 */
package org.jdesktop.lg3d.scenemanager.utils.startmenu.model;

import javax.vecmath.Vector3f;

import org.jdesktop.lg3d.scenemanager.utils.startmenu.action.ChangeGroupAction;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.action.EditMenuGroupLinkAction;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.data.MenuGroup;
import org.jdesktop.lg3d.utils.action.ActionBoolean;
import org.jdesktop.lg3d.utils.c3danimation.NaturalMotionAnimation;
import org.jdesktop.lg3d.utils.eventadapter.MouseClickedEventAdapter;
import org.jdesktop.lg3d.utils.eventadapter.MouseEnteredEventAdapter;
import org.jdesktop.lg3d.wg.Container3D;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.MouseButtonEvent3D;
import org.jdesktop.lg3d.wg.event.MouseEvent3D.ButtonId;

/**
 * Abstract component for a start menu group link
 */
public abstract class MenuGroupLinkComponent extends Container3D {

    /** The local group for the link */
    protected MenuGroup localGroup;

    /** The remote group for the link */
    protected MenuGroup remoteGroup;

    /**
     * Create a group link component for the given local and remote menu groups
     * @param localGroup The local group
     * @param remoteGroup The remote group
     */
    public MenuGroupLinkComponent(MenuGroup localGroup, MenuGroup remoteGroup) {
        this.localGroup= localGroup;
        this.remoteGroup= remoteGroup;

        initialize();

        // TODO: Mouse buttons, etc. should be customizable

        // Switch groups when a group link is clicked on with Button 1
        addListener(new MouseClickedEventAdapter(ButtonId.BUTTON1, new ChangeGroupAction(remoteGroup)));
        // Edit a group link when clicked on with Button 2
        addListener(new MouseClickedEventAdapter(ButtonId.BUTTON2, new EditMenuGroupLinkAction(localGroup, remoteGroup)));

        setMouseEventSource(MouseButtonEvent3D.class, true);

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
     * Construct the menu group link component
     */
    public abstract void initialize();

}
