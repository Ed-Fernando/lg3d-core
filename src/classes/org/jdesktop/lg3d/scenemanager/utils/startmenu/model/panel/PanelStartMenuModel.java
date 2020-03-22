/***************************************************************************
 *   Project Looking Glass
 *
 *   $RCSfile: PanelStartMenuModel.java,v $
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
 *   $Date: 2006-05-09 08:52:36 $
 */
package org.jdesktop.lg3d.scenemanager.utils.startmenu.model.panel;

import org.jdesktop.lg3d.scenemanager.utils.startmenu.model.StartMenuModel;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.model.MenuGroupComponent;
import org.jdesktop.lg3d.utils.c3danimation.NaturalMotionAnimation;
import org.jdesktop.lg3d.utils.shape.PickableRegion;
import org.jdesktop.lg3d.wg.Component3D;

/**
 * A glassy panel style menu. A menu group is represented as a vertical
 * column of panels, each carrying its own menu item or group link.
 * The menu is cycled through vertically, similar to an elevator or
 * dumb-waiter.
 */
public class PanelStartMenuModel extends StartMenuModel {
    private PickableRegion pickableRegion;
    private float prevPickableRegionHeight = 0f;
    
    @Override
    public void initialize() {
        setAnimation(new NaturalMotionAnimation(300));
        setRotationAxis(0.0f, 1.0f, 0.0f);
        setRotationAngle((float)Math.toRadians(40));
        setScale(0.1f);
        changeVisible(false);
                
        // PickableRegion provides a large area for MOUSE_ENTER/MOUSE_EXIT 
        pickableRegion = new PickableRegion(0.045f, 0.02f, 0.02f, -0.01f, -0.01f);
        Component3D c3d = new Component3D();
        c3d.addChild(pickableRegion);
        addChild(c3d);
    }

    @Override
    public void setPickableRegionSize(MenuGroupComponent currentGroupComp) {
        if (currentGroupComp == null) {
            if (pickableRegion != null) {
                pickableRegion.setSize(0.05f, 0.01f, 0.0f, 0.005f, 0.0f);
            }
            return;
        }
        PanelMenuGroupComponent pmgc = (PanelMenuGroupComponent)currentGroupComp;        
        float height = pmgc.getComponentCount() * 0.013f + 0.01f;
        float h = (prevPickableRegionHeight > height)?(prevPickableRegionHeight):(height);
        prevPickableRegionHeight = height;
        
        pickableRegion.setSize(0.05f, h + 0.01f, 0.02f, h * 0.5f, -0.01f);
    }
}
