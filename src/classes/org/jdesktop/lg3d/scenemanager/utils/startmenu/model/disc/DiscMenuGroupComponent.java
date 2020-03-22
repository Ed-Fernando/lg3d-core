/***************************************************************************
 *   Project Looking Glass
 *
 *   $RCSfile: DiscMenuGroupComponent.java,v $
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
 *   $Revision: 1.5 $
 *   $Date: 2006-05-02 07:27:30 $
 */
package org.jdesktop.lg3d.scenemanager.utils.startmenu.model.disc;

import java.util.Enumeration;

import javax.vecmath.Vector3f;

import org.jdesktop.lg3d.scenemanager.utils.startmenu.action.TooltipAction;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.data.MenuGroup;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.model.MenuGroupComponent;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.model.MenuGroupLinkComponent;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.model.MenuItemComponent;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.model.orbit.OrbitLayout;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.util.PopupText;
import org.jdesktop.lg3d.utils.c3danimation.NaturalMotionAnimation;
import org.jdesktop.lg3d.utils.eventadapter.MouseEnteredEventAdapter;
import org.jdesktop.lg3d.utils.shape.Cylinder;
import org.jdesktop.lg3d.utils.shape.GlassyDisc;
import org.jdesktop.lg3d.utils.shape.GlassyRingPanel;
import org.jdesktop.lg3d.utils.shape.SimpleAppearance;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Container3D;

/**
 * 
 */
public class DiscMenuGroupComponent extends MenuGroupComponent {

    /** The tool tip for the menu group */
    private PopupText tooltipText;
    
    private Container3D itemCont;
    
    /**
     * @param menuGroup
     */
    public DiscMenuGroupComponent(MenuGroup menuGroup) {
        super(menuGroup);
    }

    @Override
    public void initialize() {
        tooltipText= new PopupText(menuGroup.getName(), 20);
        tooltipText.setVisible(false);
        addChild(tooltipText);
        
        SimpleAppearance app= new SimpleAppearance(0.6f, 1.0f, 0.6f, 1.0f,
	    SimpleAppearance.DISABLE_CULLING);
        GlassyRingPanel ring= new GlassyRingPanel(0.065f, 0.0275f, 0.005f, app);
        Component3D ringComp= new Component3D();
        ringComp.addChild(ring);
        ringComp.setRotationAxis(1.0f, 0.0f, 0.0f);
        ringComp.setRotationAngle(0.0025f - (float)Math.PI * 0.5f);
        
        Component3D decoComp= new Component3D();
        decoComp.addListener(new MouseEnteredEventAdapter(new TooltipAction(tooltipText, this, 0.015f)));
        decoComp.setMouseEventPropagatable(true);
        decoComp.addChild(ringComp);
        decoComp.setTranslation(-0.006f, -0.0135f, -0.05f);
        decoComp.setRotationAxis(1.0f, 0.0f, 0.0f);
        decoComp.setRotationAngle(0.18f);
        setDecoration(decoComp);

        itemCont= new Container3D();
        itemCont.setPreferredSize(new Vector3f(0.12f, 0.04f, 0.12f));
        itemCont.setLayout(new OrbitLayout(0.75f, -0.24f, (float)Math.PI / -2));
        itemCont.setAnimation(new NaturalMotionAnimation(200));
        addChild(itemCont);
    }
    
    @Override
    public void cycleItems(int clicks) {
        float rad= (float)(Math.PI/-8) * clicks;
        itemCont.changeRotationAngle(itemCont.getFinalRotationAngle() + rad);
        Enumeration itemChildren= itemCont.getAllChildren();
        while(itemChildren.hasMoreElements()) {
            Component3D child= (Component3D)itemChildren.nextElement();
            child.changeRotationAngle(child.getFinalRotationAngle() - rad);
        }
    }
    
    @Override
    public void addChildItem(MenuItemComponent comp) {
        itemCont.addChild(comp);
    }
    
    @Override
    public void addChildGroupLink(MenuGroupLinkComponent comp) {
        itemCont.addChild(comp);
    }
}
