/***************************************************************************
 *   Project Looking Glass
 *
 *   $RCSfile: OrbitStartMenuModel.java,v $
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
 *   $Revision: 1.2 $
 *   $Date: 2006-05-02 07:27:31 $
 */
package org.jdesktop.lg3d.scenemanager.utils.startmenu.model.orbit;

import org.jdesktop.lg3d.scenemanager.utils.startmenu.model.StartMenuModel;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.model.MenuGroupComponent;
import org.jdesktop.lg3d.utils.c3danimation.NaturalMotionAnimation;
import org.jdesktop.lg3d.utils.shape.PickableRegion;
import org.jdesktop.lg3d.wg.Component3D;

/**
 * An orbital-style start menu. A menu group is represented as a "planet"
 * with separate rings of items and links to other groups orbiting
 * around it. Each menu item is represented as a "moon" of the main
 * group, and each group link is itself a small "planet", with
 * its own orbit. When the menu is cycled through, the items and
 * links spin around the main planet within their orbit path.
 */
public class OrbitStartMenuModel extends StartMenuModel {

    @Override
    public void initialize() {
        setAnimation(new NaturalMotionAnimation(300));
        setScale(0.1f);
        changeVisible(false);
        
        // FIXME -- ad-hoc parameters...
        // PickableRegion provides a large area for MOUSE_ENTER/MOUSE_EXIT 
        PickableRegion pickableRegion = new PickableRegion(0.14f, 0.06f, -0.02f, -0.01f, -0.07f);
        Component3D c3d = new Component3D();
        c3d.addChild(pickableRegion);
        c3d.setRotationAxis(0.0f, 0.0f, 1.0f);
        c3d.setRotationAngle((float)Math.toRadians(30));
        addChild(c3d);
    }

    @Override
    public void changeVisible(boolean visible, boolean redoAnim) {
        this.visible = visible;
        if (visible) {
            if (redoAnim) {
                setScale(0.25f);
            }
            changeScale(1.0f);
            changeTranslation(0.045f, 0.04f, 0.04f);
            setMouseEventEnabled(true);
        } else {
            changeScale(0.25f, 500);
            changeTranslation(-0.0045f, 0.006f, 0.0f);
            if (redoAnim) {
                setRotationAngle((float)Math.PI * -2);
                changeRotationAngle(0.0f, 1000);
            }
            setMouseEventEnabled(false);
        }
    }
    
    @Override
    public void setPickableRegionSize(MenuGroupComponent currentGroupComp) {
        // nothing to do
    }
}
