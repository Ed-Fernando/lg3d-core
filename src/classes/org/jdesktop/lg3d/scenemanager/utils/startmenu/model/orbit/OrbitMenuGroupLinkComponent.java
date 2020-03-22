/***************************************************************************
 *   Project Looking Glass
 *
 *   $RCSfile: OrbitMenuGroupLinkComponent.java,v $
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
 *   $Date: 2005-12-02 17:06:41 $
 */
package org.jdesktop.lg3d.scenemanager.utils.startmenu.model.orbit;

import javax.vecmath.Vector3f;

import org.jdesktop.lg3d.scenemanager.utils.startmenu.action.TooltipAction;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.data.MenuGroup;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.model.MenuGroupLinkComponent;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.util.PopupText;
import org.jdesktop.lg3d.utils.eventadapter.MouseEnteredEventAdapter;
import org.jdesktop.lg3d.utils.shape.SimpleAppearance;
import org.jdesktop.lg3d.utils.shape.Sphere;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Container3D;

/**
 * A menu group link component for the orbit start menu model
 */
public class OrbitMenuGroupLinkComponent extends MenuGroupLinkComponent {
    
    /** The popup tool tip for the menu group link */
    private PopupText tooltipText;

    /**
     * Create the group link component for the given local and remote groups
     * @param localGroup The local menu group
     * @param remoteGroup The remote menu group
     */
    public OrbitMenuGroupLinkComponent(MenuGroup localGroup, MenuGroup remoteGroup) {
        super(localGroup, remoteGroup);
    }

    /**
     * Construct a mini "planet" with items orbiting it to
     * represent a link to another group
     */
    @Override
    public void initialize() {
        tooltipText= new PopupText(remoteGroup.getName(), 18);
        tooltipText.setVisible(false);
        addChild(tooltipText);
        
        Container3D cont= new Container3D();
        cont.setPreferredSize(new Vector3f(0.025f, 0.0125f, 0.025f));
        cont.setLayout(new OrbitLayout(0.5f));
        SimpleAppearance centerApp= new SimpleAppearance(0.6f, 0.8f, 0.6f, 0.9f);
        Sphere center= new Sphere(0.005f, Sphere.GENERATE_NORMALS, 16, centerApp);
        Component3D centerComp= new Component3D();
        centerComp.addChild(center);
        centerComp.addListener(new MouseEnteredEventAdapter(new TooltipAction(tooltipText, this, 0.005f)));
        centerComp.setMouseEventPropagatable(true);
        cont.setDecoration(centerComp);
        
        int children= 8;
        for(int currChild= 0; currChild < children; currChild++) {
            Component3D childComp= new Component3D();
            float red= 0.6f + ((int)(Math.random() * 3) * 0.1f);
            float green= 0.5f + ((int)(Math.random() * 4) * 0.1f);
            float blue= 0.4f + ((int)(Math.random() * 5) * 0.1f);
            SimpleAppearance childApp= new SimpleAppearance(red, green, blue, 0.8f);
            Sphere childSphere= new Sphere(0.001f, Sphere.GENERATE_NORMALS, 16, childApp);
            childComp.addChild(childSphere);
            cont.addChild(childComp);
        }
        
        addChild(cont);
    }

}
