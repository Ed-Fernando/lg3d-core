/***************************************************************************
 *   Project Looking Glass
 *
 *   $RCSfile: DiscMenuItemComponent.java,v $
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
 *   $Date: 2006-04-19 17:23:26 $
 */
package org.jdesktop.lg3d.scenemanager.utils.startmenu.model.disc;

import java.util.logging.Level;
import org.jdesktop.lg3d.displayserver.LgClassLoader;

import org.jdesktop.lg3d.scenemanager.utils.startmenu.action.TooltipAction;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.data.MenuItem;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.model.MenuItemComponent;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.util.PopupText;
import org.jdesktop.lg3d.utils.component.Pseudo3DIcon;
import org.jdesktop.lg3d.utils.eventadapter.MouseEnteredEventAdapter;
import org.jdesktop.lg3d.utils.shape.SimpleAppearance;
import org.jdesktop.lg3d.utils.shape.Sphere;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Tapp;

public class DiscMenuItemComponent extends MenuItemComponent {

    private PopupText tooltipText;

    /**
     * @param menuItem
     */
    public DiscMenuItemComponent(MenuItem menuItem) {
        super(menuItem);
    }

    @Override
    public void initialize() {
        tooltipText= new PopupText(menuItem.getName(), 18);
        tooltipText.setVisible(false);
        addChild(tooltipText);
        addListener(new MouseEnteredEventAdapter(new TooltipAction(tooltipText, this, 0.005f)));
        setMouseEventPropagatable(true);

        switch(menuItem.getDisplayResource().getType()) {
        case ICON:
            try {
                Pseudo3DIcon icon= new Pseudo3DIcon(
                        menuItem.getDisplayResource().getUrl());
                addChild(icon);
            } catch (Exception e) {
                logger.log(Level.WARNING, "Error loading icon for " + menuItem.getName(), e);
                createDefaultIcon(this);
            }
            break;
        case TAPP:
            try {
                ClassLoader classLoader = menuItem.getClassLoader();
                Class tappClass= classLoader.loadClass(menuItem.getDisplayResource().getClassname());
                Tapp tapp= (Tapp)tappClass.newInstance();
                addChild(tapp);
            } catch (Exception e) {
                logger.log(Level.WARNING, "Error loading tapp class for " + menuItem.getName(), e);
                createDefaultIcon(this);
            }
            break;
        case DEFAULT:
        default:
            createDefaultIcon(this);
        }
    }

    private void createDefaultIcon(Component3D iconComp) {
        float red= 0.6f + ((int)(Math.random() * 3) * 0.1f);
        float green= 0.5f + ((int)(Math.random() * 4) * 0.1f);
        float blue= 0.4f + ((int)(Math.random() * 5) * 0.1f);
        SimpleAppearance app= new SimpleAppearance(red, green, blue, 0.9f);
        Sphere sphere= new Sphere(0.005f, Sphere.GENERATE_NORMALS, 32, app);
        Component3D sphereComp= new Component3D();
        sphereComp.addChild(sphere);
        iconComp.addChild(sphereComp);
    }

}
