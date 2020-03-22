/**
 * Project Looking Glass
 *
 * $RCSfile: GlassyRingPanelContainer.java,v $
 *
 * Copyright (c) 2004, Sun Microsystems, Inc., All Rights Reserved
 *
 * Redistributions in source code form must reproduce the above
 * copyright and this condition.
 *
 * The contents of this file are subject to the GNU General Public
 * License, Version 2 (the "License"); you may not use this file
 * except in compliance with the License. A copy of the License is
 * available at http://www.opensource.org/licenses/gpl-license.php.
 *
 * $Revision: 1.1 $
 * $Date: 2006-07-31 23:50:15 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.component;

import java.util.ArrayList;
import java.util.logging.Logger;
import javax.vecmath.Vector3f;
import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import org.jdesktop.lg3d.utils.action.ActionInt;
import org.jdesktop.lg3d.utils.action.RotateActionBoolean;
import org.jdesktop.lg3d.utils.c3danimation.NaturalMotionAnimation;
import org.jdesktop.lg3d.utils.eventadapter.MouseHoverEventAdapter;
import org.jdesktop.lg3d.utils.eventadapter.MouseWheelEventAdapter;
import org.jdesktop.lg3d.utils.shape.SimpleAppearance;
import org.jdesktop.lg3d.utils.shape.OriginTranslation;
import org.jdesktop.lg3d.utils.shape.GlassyBentText2D;
import org.jdesktop.lg3d.utils.shape.GlassyRingPanel;
import org.jdesktop.lg3d.utils.smoother.XZPolarNaturalVector3fSmoother;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Component3DAnimation;
import org.jdesktop.lg3d.wg.Container3D;
import org.jdesktop.lg3d.wg.LayoutManager3D;
import org.jdesktop.lg3d.wg.event.LgEventSource;


/**
 * still in development...
 */
public class GlassyRingPanelContainer extends Container3D {
    private static Logger logger = Logger.getLogger("lg.utils");
    
    private static final float defaultRadiusMargin = 0.002f;
    private static final float defaultDiscHeight = 0.005f;
    private static final float defaultAngleNormal = (float)Math.toRadians(5);
    private static final float defaultAngleFocused = (float)Math.toRadians(10);
    private static final float defaultFarScaling = 0.75f;
    
    private float radius;
    private float discHeight;
    private Color3f color = new Color3f();
    private float angleNormal;
    private float angleFocused;
    private float farScaling;
    
    private Component3D deco = new Component3D();
    private GlassyBentText2D titleShape = null;
    private ArcLayout arcLayout;
    private Vector3f tmpV3f= new Vector3f();
    
    /**
     * 
     */
    public GlassyRingPanelContainer(float radius, Color3f color) {
        this(radius, defaultRadiusMargin, defaultDiscHeight, color);
    }
    
    /**
     * 
     */
    public GlassyRingPanelContainer(float radius, float radiusMargin, float discHeight,
            Color3f color) 
    {
        this(radius, radiusMargin, discHeight, 
                defaultAngleNormal, defaultAngleFocused, 
                0.0f, defaultFarScaling, color);
    }
    
    /**
     * 
     */
    public GlassyRingPanelContainer(float radius, float radiusMargin, float discHeight,
            float angleNormal, float angleFocused, float spacing,
            float farScaling, Color3f color) 
    {
        this.radius = radius;
        this.discHeight = discHeight;
        this.color.set(color);
        this.angleNormal = angleNormal;
        this.angleFocused = angleFocused;
        this.farScaling = farScaling;
        
        arcLayout 
                = new ArcLayout(
                    radius - radiusMargin, 
                    (float)Math.toRadians(360 - 45), 
                    spacing, farScaling, false);
        setLayout(arcLayout);
        
        SimpleAppearance app
                = new SimpleAppearance(
                    color.x, color.y, color.z, 0.75f, 
                    SimpleAppearance.DISABLE_CULLING);
        
        GlassyRingPanel disc 
                = new GlassyRingPanel(
                    radius, radius * 0.5f, discHeight, 
                    0.0f, radius * 0.5f * (1.0f - farScaling), app);
        deco.addChild(disc);
        deco.setRotationAxis(1.0f, 0.0f, 0.0f);
        deco.setRotationAngle((float)Math.toRadians(-90));
        
        setDecoration(deco);
        
        setAnimation(new NaturalMotionAnimation(200));
        setRotationAxis(1.0f, 0.0f, 0.0f);
        setRotationAngle(angleNormal);
        
        addListener(
            new MouseHoverEventAdapter(0, 250, 0,
                new RotateActionBoolean(this, angleFocused)));
        
        addListener(
            new MouseWheelEventAdapter(
                new ActionInt() {
                    public void performAction(LgEventSource source, int value) {
                        arcLayout.rotate(-value);
                    }
                }));
        
        setMouseEventPropagatable(true);
    }
    
    @Override
    public void setName(String title) {
        super.setName(title);
        if (titleShape == null) {
            titleShape = new GlassyBentText2D(
                title, radius, 
                (float)Math.toRadians(90 * 3), 
                (float)Math.toRadians(90), 
                discHeight, 
                new Color4f(color.x, color.y, color.z, 0.75f),
                GlassyBentText2D.LightDirection.BOTTOM_RIGHT, 
                GlassyBentText2D.Alignment.CENTER);
            deco.addChild(
                new OriginTranslation(titleShape, 
                    new Vector3f(0.0f, 0.0f, -discHeight)));
        } else {
            titleShape.setText(title);
        }
    }
}
    