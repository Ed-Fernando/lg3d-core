/***************************************************************************
 *   Project Looking Glass
 *
 *   $RCSfile: DiscStartMenuModel.java,v $
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
 *   $Revision: 1.3 $
 *   $Date: 2006-05-02 07:27:30 $
 */
package org.jdesktop.lg3d.scenemanager.utils.startmenu.model.disc;

import org.jdesktop.lg3d.scenemanager.utils.startmenu.model.StartMenuModel;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.model.MenuGroupComponent;
import org.jdesktop.lg3d.utils.c3danimation.NaturalMotionAnimation;
import org.jdesktop.lg3d.utils.shape.PickableRegion;
import org.jdesktop.lg3d.wg.Component3D;

/**
 * A disc-style menu. Each menu group is represented by a disc or plate,
 * on which the items and group links are arranged. Each link to
 * a group is itself a small plate with its own items, simlutating
 * an infinately recursive stack of plates. Items are cycled through
 * by spinning the disc like a lazy-susan.
 */
public class DiscStartMenuModel extends StartMenuModel {

    @Override
    public void initialize() {
        setAnimation(new NaturalMotionAnimation(300));
        setScale(0.1f);
        changeVisible(false);
        
        // FIXME -- ad-hoc parameters...
        // PickableRegion provides a large area for MOUSE_ENTER/MOUSE_EXIT 
        PickableRegion pickableRegion = new PickableRegion(0.12f, 0.12f);
        Component3D c3d = new Component3D();
        c3d.addChild(pickableRegion);
        c3d.setRotationAxis(1.0f, 0.0f, 0.0f);
        c3d.setRotationAngle((float)Math.toRadians(-75));
        c3d.setTranslation(0.0f, -0.02f, -0.03f);
        addChild(c3d);
    }

    @Override
    public void changeVisible(boolean visible, boolean redoAnim) {
        this.visible = visible;
        if (visible) {
            if (redoAnim) {
                setScale(0.15f);
            }
            changeScale(0.8f);
            changeTranslation(0.045f, 0.05f, 0.04f);
            setMouseEventEnabled(true);
        } else {
            changeScale(0.15f, 500);
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
