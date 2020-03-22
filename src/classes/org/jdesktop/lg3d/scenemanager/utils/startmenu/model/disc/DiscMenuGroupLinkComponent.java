/***************************************************************************
 *   Project Looking Glass
 *
 *   $RCSfile: DiscMenuGroupLinkComponent.java,v $
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
 *   $Date: 2006-02-10 14:18:45 $
 */
package org.jdesktop.lg3d.scenemanager.utils.startmenu.model.disc;

import javax.vecmath.Vector3f;

import org.jdesktop.lg3d.scenemanager.utils.startmenu.action.TooltipAction;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.data.MenuGroup;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.model.MenuGroupLinkComponent;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.model.orbit.OrbitLayout;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.util.PopupText;
import org.jdesktop.lg3d.utils.eventadapter.MouseEnteredEventAdapter;
import org.jdesktop.lg3d.utils.shape.GlassyRingPanel;
import org.jdesktop.lg3d.utils.shape.SimpleAppearance;
import org.jdesktop.lg3d.utils.shape.Sphere;
import org.jdesktop.lg3d.wg.Component3D;

public class DiscMenuGroupLinkComponent extends MenuGroupLinkComponent {

    /** The popup tool tip for the menu group link */
    private PopupText tooltipText;

    /**
     * @param localGroup
     * @param remoteGroup
     */
    public DiscMenuGroupLinkComponent(MenuGroup localGroup, MenuGroup remoteGroup) {
        super(localGroup, remoteGroup);
    }

    /* (non-Javadoc)
     * @see org.jdesktop.lg3d.scenemanager.utils.startmenu.model.MenuGroupLinkComponent#initialize()
     */
    @Override
    public void initialize() {
        setPreferredSize(new Vector3f(0.02f, 0.02f, 0.02f));
        setLayout(new OrbitLayout(0.75f, -0.2f, (float)Math.PI / -2));
        
        tooltipText= new PopupText(remoteGroup.getName(), 18);
        tooltipText.setVisible(false);
        addChild(tooltipText);
        
        float red= 0.6f + ((int)(Math.random() * 3) * 0.1f);
        float green= 0.5f + ((int)(Math.random() * 4) * 0.1f);
        float blue= 0.4f + ((int)(Math.random() * 5) * 0.1f);
        SimpleAppearance app= new SimpleAppearance(red, green, blue, 1.0f,
                SimpleAppearance.DISABLE_CULLING);
        GlassyRingPanel ring= new GlassyRingPanel(0.01f, 0.006f, 0.002f, app);
        Component3D ringComp= new Component3D();
        ringComp.addChild(ring);
        ringComp.setRotationAxis(1.0f, 0.0f, 0.0f);
        ringComp.setRotationAngle(0.0025f - (float)Math.PI * 0.5f);
        
        Component3D comp= new Component3D();
        comp.setMouseEventPropagatable(true);
        comp.addChild(ringComp);
        comp.setTranslation(0.0f, -0.0025f, -0.006f);
        comp.addListener(new MouseEnteredEventAdapter(new TooltipAction(tooltipText, this, 0.0015f)));
        comp.setRotationAxis(1.0f, 0.0f, 0.0f);
        comp.setRotationAngle(0.15f);
        setDecoration(comp);
        
        for(int i= 0; i < 7; i++) {
            Component3D childComp= new Component3D();
            float r= 0.6f + ((int)(Math.random() * 3) * 0.1f);
            float g= 0.5f + ((int)(Math.random() * 4) * 0.1f);
            float b= 0.4f + ((int)(Math.random() * 5) * 0.1f);
            SimpleAppearance childApp= new SimpleAppearance(r, g, b, 0.8f);
            Sphere childSphere= new Sphere(0.001f, Sphere.GENERATE_NORMALS, 16, childApp);
            childComp.addChild(childSphere);
            addChild(childComp);
        }
        
        setRotationAxis(0.0f, 1.0f, 0.0f);
    }

}
