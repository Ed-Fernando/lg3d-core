/***************************************************************************
 *   Project Looking Glass
 *
 *   $RCSfile: PanelMenuGroupComponent.java,v $
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
 *   $Date: 2006-05-05 20:05:40 $
 */
package org.jdesktop.lg3d.scenemanager.utils.startmenu.model.panel;

import javax.vecmath.Vector3f;

import org.jdesktop.lg3d.scenemanager.utils.startmenu.data.MenuGroup;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.model.MenuGroupComponent;
import org.jdesktop.lg3d.wg.Component3D;

/**
 * 
 */
public class PanelMenuGroupComponent extends MenuGroupComponent {
    
    private VerticalLayout layout;
    
    public PanelMenuGroupComponent(MenuGroup menuGroup) {
        super(menuGroup);
    }

    /* (non-Javadoc)
     * @see org.jdesktop.lg3d.scenemanager.utils.startmenu.model.MenuGroupComponent#initialize()
     */
    @Override
    public void initialize() {
        setPreferredSize(new Vector3f(0.0f, 0.0f, 0.0f));
        layout= new VerticalLayout(VerticalLayout.AlignmentType.BOTTOM, 0.0f);
        setLayout(layout);
    }
    
    @Override
    public void cycleItems(int clicks) {
        if (clicks > 0) {
            for (int i= 0; i < clicks; i++) {
                Component3D comp= layout.getTopComponent();
                rearrangeChildLayout(comp, layout.getComponentCount() -1);
            }
        } else {
            clicks = -clicks;
            for (int i= 0; i < clicks; i++) {
                Component3D comp= layout.getBottomComponent();
                rearrangeChildLayout(comp, 0);
            }
        }
    }
    
    public float getComponentCount() {
        return layout.getComponentCount();
    }
}
