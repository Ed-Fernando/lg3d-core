/***************************************************************************
 *   Project Looking Glass
 *
 *   $RCSfile: PanelMenuGroupLinkComponent.java,v $
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
 *   $Date: 2006-06-08 20:52:51 $
 */

package org.jdesktop.lg3d.scenemanager.utils.startmenu.model.panel;

import java.awt.Color;
import java.awt.Font;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import org.jdesktop.lg3d.scenemanager.utils.startmenu.data.MenuGroup;
import org.jdesktop.lg3d.scenemanager.utils.startmenu.model.MenuGroupLinkComponent;
import org.jdesktop.lg3d.utils.action.ActionBoolean;
import org.jdesktop.lg3d.utils.eventadapter.MouseEnteredEventAdapter;
import org.jdesktop.lg3d.utils.layoutmanager.HorizontalLayout;
import org.jdesktop.lg3d.utils.shape.GlassyPanel;
import org.jdesktop.lg3d.utils.shape.OriginTranslation;
import org.jdesktop.lg3d.utils.shape.PickableRegion;
import org.jdesktop.lg3d.utils.shape.SimpleAppearance;
import org.jdesktop.lg3d.utils.shape.Sphere;
import org.jdesktop.lg3d.utils.shape.Text2D;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Container3D;
import org.jdesktop.lg3d.wg.event.LgEventSource;

/**
 *
 */
public class PanelMenuGroupLinkComponent extends MenuGroupLinkComponent {

    public PanelMenuGroupLinkComponent(MenuGroup localGroup, MenuGroup remoteGroup) {
        super(localGroup, remoteGroup);
    }

    @Override
    public void initialize() {
        setPreferredSize(new Vector3f(0.045f, 0.013f, 0.015f));
        setLayout(new HorizontalLayout(HorizontalLayout.AlignmentType.LEFT, 0.0025f));

        SimpleAppearance panelApp=
            new SimpleAppearance(0.6f, 1.0f, 0.6f, 1.0f,
                SimpleAppearance.DISABLE_CULLING);
        GlassyPanel panel= new GlassyPanel(0.045f, 0.015f, 0.001f, panelApp);
        Component3D panelComp= new Component3D();
        panelComp.addChild(panel);
        panelComp.setRotationAxis(1.0f, 0.0f, 0.0f);
        panelComp.setRotationAngle(-1.11f);
        panelComp.setTranslation(0.0f, 0.015f * -0.51f, 0.015f * -0.3f);
        Component3D deco = new Component3D();
        deco.addChild(panelComp);
        deco.setTranslation(0.005f, 0.0f, -0.01f);
        PickableRegion pr = new PickableRegion(0.045f, 0.015f);
        deco.addChild(pr);
        setDecoration(deco);
        
        Component3D iconComp= new Component3D();
        iconComp.setPreferredSize(new Vector3f(0.006f, 0.006f, 0.006f));
        float red= 0.6f + ((int)(Math.random() * 3) * 0.1f);
        float green= 0.5f + ((int)(Math.random() * 4) * 0.1f);
        float blue= 0.4f + ((int)(Math.random() * 5) * 0.1f);
        SimpleAppearance app= new SimpleAppearance(red, green, blue, 0.9f);
        Sphere sphere= new Sphere(0.003f, Sphere.GENERATE_NORMALS, 32, app);
        iconComp.addChild(sphere);
        addChild(iconComp);

        Component3D textComp= new Component3D();
        textComp.setPreferredSize(new Vector3f(0.0f, 0.0f, 0.0f));
        Text2D text= new Text2D(remoteGroup.getName(), new Color3f(1.0f, 1.0f, 1.0f),
                "SansSerif", 16, Font.BOLD);
        text.setRectangleScaleFactor(1.0f / 4096.0f);
        textComp.addChild(text);

        Text2D textShadow= new Text2D(remoteGroup.getName(), new Color3f(0.3f, 0.3f, 0.3f),
                "SansSerif", 16, Font.BOLD);
        textShadow.setRectangleScaleFactor(1.0f / 4096.0f);
        textComp.addChild(new OriginTranslation(textShadow, new Vector3f(0.0003f, -0.0003f, -0.0001f)));

        addChild(textComp);
        addListener(new MouseEnteredEventAdapter(new ActionBoolean() {
            public void performAction(LgEventSource source, boolean state) {
                Vector3f trans= getFinalTranslation(new Vector3f());
                if(state) {
                    changeTranslation(trans.x, trans.y, 0.005f, 150);
                } else {
                    changeTranslation(trans.x, trans.y, 0.0f, 150);
                }
            }
        }));
    }

}
