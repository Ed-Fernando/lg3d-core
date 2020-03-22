/**
 * Project Looking Glass
 *
 * $RCSfile: GlassyDiscContainer.java,v $
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
 * $Revision: 1.11 $
 * $Date: 2006-08-15 19:57:25 $
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
import org.jdesktop.lg3d.utils.shape.GlassyDisc;
import org.jdesktop.lg3d.utils.smoother.XZPolarNaturalVector3fSmoother;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Component3DAnimation;
import org.jdesktop.lg3d.wg.Container3D;
import org.jdesktop.lg3d.wg.LayoutManager3D;
import org.jdesktop.lg3d.wg.event.LgEventSource;


/**
 * still in development...
 */
public class GlassyDiscContainer extends Container3D {
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
    public GlassyDiscContainer(float radius, Color3f color) {
        this(radius, defaultRadiusMargin, defaultDiscHeight, color);
    }
    
    /**
     * 
     */
    public GlassyDiscContainer(float radius, float radiusMargin, float discHeight,
            Color3f color) 
    {
        this(radius, radiusMargin, discHeight, 
                defaultAngleNormal, defaultAngleFocused, 
                0.0f, defaultFarScaling, color);
    }
    
    /**
     * 
     */
    public GlassyDiscContainer(float radius, float radiusMargin, float discHeight,
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
        
        GlassyDisc disc = new GlassyDisc(radius, discHeight, app);
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
    
class ArcLayout implements LayoutManager3D {
    private static final int rotDivMin = 6;
    /** The container being laid out */
    private Container3D cont;
    /** The list of children components */
    private ArrayList<CompInfo> compInfoList = new ArrayList<CompInfo>();
    private float radius;
    private float arcAngle;
    private float spacing;
    private float farScaling;
    private boolean lockRotAtEdge; 
    private float currentRot = 0.0f;
    private Vector3f tmpV3f = new Vector3f();
    
    public ArcLayout(float radius, float arcAngle, float spacing, float farScaling, boolean lockRotAtEdge) {
        this.radius = radius;
        this.arcAngle = arcAngle;
        this.spacing = (float)Math.atan2(0.5 * spacing, radius) * 2.0f;
        this.farScaling = (1.0f / farScaling) - 1.0f;
        this.lockRotAtEdge = lockRotAtEdge;
    }
    
    /**
     * Set the container being laid out
     * @param cont The container to layout
     * @see org.jdesktop.lg3d.wg.LayoutManager3D#setContainer(org.jdesktop.lg3d.wg.Container3D)
     */
    public void setContainer(Container3D cont) {
        this.cont = cont;
    }
    
    public void rotate(int count) {
        int d = rotDivMin;
        if (d < compInfoList.size()) {
            d = compInfoList.size();
        }
        currentRot += arcAngle / d * count;
        adjustCurrentRot();
        cont.revalidate();
    }
    
    private void adjustCurrentRot() {
        if (!lockRotAtEdge) {
            return;
        }
        float arc = calcEndAngle() - currentRot;
        while (currentRot < 0.0f) {
            currentRot += 2.0f * (float)Math.PI;
        }
        while (currentRot > 2.0f * (float)Math.PI) {
            currentRot -= 2.0f * (float)Math.PI;
        }
        if (currentRot > 0.0f && currentRot < 2.0f * (float)Math.PI - arc) {
            if (currentRot < (2.0f * (float)Math.PI - arc) * 0.5f) {
                currentRot = 0.0f;
            } else {
                currentRot = 2.0f * (float)Math.PI - arc;
            }
        }
    }
    
    /**
     * Layout components in the container in an orbit based on the 
     * specified parameters.
     */
    public void layoutContainer() {
        float s = calcScalingFactor();
        
        float angle = currentRot;
        float prevF = 0.5f * (1.0f - (float)Math.cos(angle));
        prevF = 1.0f / (1.0f + s * prevF);
        
        for (CompInfo compInfo : compInfoList) {
            angle += compInfo.angle * prevF;
            float f = 0.5f * (1.0f - (float)Math.cos(angle));
            f = 1.0f / (1.0f + s * f);
            
            float scale = compInfo.origScale * f;
            float r = radius - compInfo.radius * f;
            
            float x = r * (float)Math.sin(angle);
            float y = compInfo.comp.getPreferredSize(tmpV3f).y * scale / 2;
            float z = r * (float)Math.cos(angle);
            compInfo.comp.changeTranslation(x, y, z);
            compInfo.comp.changeScale(scale);
            
            angle += (compInfo.angle + spacing) * f;
            prevF = f;
        }
    }
    
    private float calcTotalAngle() {
        float w = 0.0f;
        
        for (CompInfo compInfo : compInfoList) {
            float scale = compInfo.origScale;
            compInfo.comp.getPreferredSize(tmpV3f);
            float r = (float)Math.hypot(tmpV3f.z, tmpV3f.x) * scale / 2;
            compInfo.radius = r;
            float a = (float)Math.atan2(r, radius - r);
            compInfo.angle = a;
            w += a * 2.0f + spacing;
	}
        
        return w;
    }
    
    private float calcScalingFactor() {
        float w = calcTotalAngle();
        
        float as = currentRot;
        float ae = as + arcAngle;
        float ss = -(float)Math.sin(as);
        float se = -(float)Math.sin(ae);
        float f = 2.0f * (w - ae + as) / (se - ss + ae - as);
        
        if (f < farScaling) {
            f = farScaling;
        }
        return f;
    }
    
    private float calcEndAngle() {
        float s = calcScalingFactor();
        float angle = currentRot;
        float prevF = 0.5f * (1.0f - (float)Math.cos(angle));
        prevF = 1.0f / (1.0f + s * prevF);
        for (CompInfo compInfo : compInfoList) {
            angle += compInfo.angle * prevF;
            float f = 0.5f * (1.0f - (float)Math.cos(angle));
            f = 1.0f / (1.0f + s * f);
            angle += (compInfo.angle + spacing) * f;
            prevF = f;
        }
        return angle;
    }
    
    /**
     * Add a component to the layout. Adding a component causes the spacing
     * of all the components to be recalculated.
     * @param comp The component to add
     * @param constraints An optional <code>Integer</code> specifying the postion to add the component
     * @see org.jdesktop.lg3d.wg.LayoutManager3D#addLayoutComponent(org.jdesktop.lg3d.wg.Component3D, java.lang.Object)
     */
    public void addLayoutComponent(Component3D comp, Object constraints) {
        NaturalMotionAnimation nma = new NaturalMotionAnimation(250);
        nma.setTranslationSmoother(new XZPolarNaturalVector3fSmoother());
        Component3DAnimation origAnim = comp.getAnimation();
        comp.setAnimation(nma);
        synchronized (compInfoList) {
            addCompInfo(constraints, comp, origAnim);
        }
    }

    /**
     * Remove a component from the layout. The spacing of the remaining components
     * is recalculated after removal.
     * @param comp The component to remove
     * @see org.jdesktop.lg3d.wg.LayoutManager3D#removeLayoutComponent(org.jdesktop.lg3d.wg.Component3D)
     */
    public void removeLayoutComponent(Component3D comp) {
        synchronized (compInfoList) {
            CompInfo compInfo = removeCompInfo(comp);
            comp.changeScale(compInfo.origScale);
        }
    }

    /**
     * Rearrange a component in the container.
     */
    public boolean rearrangeLayoutComponent(Component3D comp, Object newConstraints) {
        if (newConstraints != null && newConstraints instanceof Integer) {
            int idx = (Integer)newConstraints;
            synchronized (compInfoList) {
                if (indexOfComplist(comp) == idx) {
                    return false;
                }
                CompInfo compInfo = removeCompInfo(comp);
                compInfoList.add(idx, compInfo);
            }
        } else {
            synchronized (compInfoList) {
                if (indexOfComplist(comp) == compInfoList.size() -1) {
                    return false;
                }
                CompInfo compInfo = removeCompInfo(comp);
                compInfoList.add(compInfo);
            }
        }
        return true;
    }
    
    private void addCompInfo(Object constraints, Component3D comp, 
            Component3DAnimation origAnim) 
    {
        float before = calcEndAngle();
        if (constraints != null && constraints instanceof Integer) {
            compInfoList.add((Integer)constraints, new CompInfo(comp, origAnim));
        } else {
            compInfoList.add(new CompInfo(comp, origAnim));
        }
        float after = calcEndAngle();
        if (compInfoList.size() == 1) {
            // move the component to the center
            currentRot = 0.0f;
        } 
        currentRot -= 0.5f * (after - before);
        
        adjustCurrentRot();
    }
    
    private CompInfo removeCompInfo(Component3D comp) {
        CompInfo ret = null;
        float before = calcEndAngle();
        for (CompInfo compInfo : compInfoList) {
            if (compInfo.comp == comp) {
                compInfoList.remove(compInfo);
                ret = compInfo;
                break;
            }
        }
        float after = calcEndAngle();
        currentRot -= 0.5f * (after - before);
        adjustCurrentRot();
        
        return ret;
    }
    
    private int indexOfComplist(Component3D comp) {
        for (int i = 0; i < compInfoList.size(); i++) {
            CompInfo compInfo = compInfoList.get(i);
            if (compInfo.comp == comp) {
                return i;
            }
        }
        return -1;
    }
    
    private static class CompInfo {
        Component3D comp;
        Component3DAnimation origAnim;
        float origScale;
        float radius;
        float angle;
        
        CompInfo(Component3D comp, Component3DAnimation origAnim) {
            this.comp = comp;
            this.origAnim = origAnim;
            this.origScale = comp.getFinalScale();
        }
    }
 }
 