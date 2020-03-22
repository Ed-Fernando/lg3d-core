/***************************************************************************
 *   Project Looking Glass
 *
 *   $RCSfile: OrbitMenuGroupComponent.java,v $
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

import java.util.Enumeration;

import javax.vecmath.Vector3f;

import org.jdesktop.lg3d.scenemanager.utils.startmenu.action.TooltipAction;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.data.MenuGroup;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.model.MenuGroupComponent;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.model.MenuGroupLinkComponent;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.model.MenuItemComponent;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.util.PopupText;
import org.jdesktop.lg3d.utils.c3danimation.NaturalMotionAnimation;
import org.jdesktop.lg3d.utils.eventadapter.MouseEnteredEventAdapter;
import org.jdesktop.lg3d.utils.shape.SimpleAppearance;
import org.jdesktop.lg3d.utils.shape.Sphere;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Container3D;

/**
 * A menu group component for the orbital start menu model. The
 * component is constructed as a central "planet" with menu
 * items and group links in orbit around it.
 */
public class OrbitMenuGroupComponent extends MenuGroupComponent {
    
    /** The tool tip for the menu group */
    private PopupText tooltipText;
    
    /** The container for the menu items of this group */
    private Container3D itemCont;
    
    /** The container for the links from this group to other groups */
    private Container3D groupLinkCont;
    
    /**
     * Construct a menu group component for the given group
     * @param menuGroup The menu group
     */
    public OrbitMenuGroupComponent(MenuGroup menuGroup) {
        super(menuGroup);
    }

    /**
     * Construct and initialize the menu group component
     */
    @Override
    public void initialize() {
        Vector3f sizeV3f= new Vector3f(0.1f, 0.05f, 0.1f);
        setPreferredSize(sizeV3f);
        
        // Create the tooltip from the group name and the description (if there is a description)
//      if(menuGroup.getDesc() == null || "".equals(menuGroup.getDesc())) {
            tooltipText= new PopupText(menuGroup.getName(), 20);
//      } else {
//          tooltipText= new PopupText(menuGroup.getName() + "\n" + menuGroup.getDesc(), 16);
//      }
        tooltipText.setVisible(false);
        addChild(tooltipText);
        
        // Create the central "planet" (randomly colored, for the time being)
        float red= 0.6f + ((int)(Math.random() * 3) * 0.1f);
        float green= 0.5f + ((int)(Math.random() * 4) * 0.1f);
        float blue= 0.4f + ((int)(Math.random() * 5) * 0.1f);
        SimpleAppearance app= new SimpleAppearance(red, green, blue, 0.9f);
        Sphere sphere= new Sphere(0.015f, Sphere.GENERATE_NORMALS, 32, app);
        Component3D sphereComp= new Component3D();
        sphereComp.addListener(new MouseEnteredEventAdapter(new TooltipAction(tooltipText, sphereComp, 0.015f)));
        sphereComp.setMouseEventPropagatable(true);
        sphereComp.addChild(sphere);
        addChild(sphereComp);
        
        itemCont= new Container3D();
        itemCont.setPreferredSize(sizeV3f);
        itemCont.setLayout(new OrbitLayout(0.5f));
        itemCont.setAnimation(new NaturalMotionAnimation(200));
        
        groupLinkCont= new Container3D();
        groupLinkCont.setPreferredSize(sizeV3f);
        groupLinkCont.setLayout(new OrbitLayout(0.9f, (float)Math.PI/8, (float)Math.PI/4));
        groupLinkCont.setAnimation(new NaturalMotionAnimation(200));
        
        addChild(itemCont);
        addChild(groupLinkCont);
    }
    
    /**
     * Add a menu item component to this group
     */
    @Override
    public void addChildItem(MenuItemComponent comp) {
        itemCont.addChild(comp);
    }
    
    /**
     * Add a menu group link component to this group
     */
    @Override
    public void addChildGroupLink(MenuGroupLinkComponent comp) {
        groupLinkCont.addChild(comp);
    }
    
    /**
     * Cycle through the menu items and links
     * @param rad The radians to rotate
     */
    @Override
    public void cycleItems(int clicks) {
        float rad= (float)(Math.PI/-8) * clicks;
        itemCont.changeRotationAngle(itemCont.getFinalRotationAngle() + rad);
        groupLinkCont.changeRotationAngle(groupLinkCont.getFinalRotationAngle() + rad);
        Enumeration itemChildren= itemCont.getAllChildren();
        while(itemChildren.hasMoreElements()) {
            MenuItemComponent child= (MenuItemComponent)itemChildren.nextElement();
            child.changeRotationAngle(child.getFinalRotationAngle() - rad);
        }
        Enumeration linkChildren= groupLinkCont.getAllChildren();
        while(linkChildren.hasMoreElements()) {
            MenuGroupLinkComponent child= (MenuGroupLinkComponent)linkChildren.nextElement();
            child.changeRotationAngle(child.getFinalRotationAngle() - rad);
        }
    }
}
