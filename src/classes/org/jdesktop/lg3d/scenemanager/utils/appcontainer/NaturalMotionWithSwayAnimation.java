/*
 * Project Looking Glass
 *
 * $RCSfile: NaturalMotionWithSwayAnimation.java,v $
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
 * $Revision: 1.5 $
 * $Date: 2006-03-28 00:18:52 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.appcontainer;

import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.utils.action.ActionBooleanFloat3;
import org.jdesktop.lg3d.utils.animation.ConcurrentTimedSubAnimationHelper.SubAnimation;
import org.jdesktop.lg3d.utils.c3danimation.ChangeVisibleAnimationPlugin;
import org.jdesktop.lg3d.utils.c3danimation.NaturalMotionAnimation;
import org.jdesktop.lg3d.utils.eventadapter.Component3DManualMoveEventAdapter;
import org.jdesktop.lg3d.utils.smoother.FloatTransitionSmoother;
import org.jdesktop.lg3d.utils.smoother.SpringFloatSmoother;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.event.LgEventSource;


/**
 *
 */
public class NaturalMotionWithSwayAnimation extends NaturalMotionAnimation {
    private static final float swayDegreeDefault = 0.025f; // 2.5cm
    private static final float swayScensitivityDefault = 0.01f; // 1cm
    private static final float swayTargetMinWidth = 0.05f; // 5cm
    
    private FloatTransitionSmoother resilientSmoother;
    private SubAnimation swaySubAnim;
    private Component3DManualMoveEventAdapter manualMoveEventAdapter;
    private Vector3f translation = new Vector3f();
    private float centerX = 0.0f;
    private float centerZ = 0.0f;
    private float centerXSaved;
    private float centerZSaved;
    private float prevX = Float.NaN;
    private float shiftX = 0.0f;
    private float shiftZ = 0.0f;
    private float elapsedTrans = 0.0f;
    private float elapsedAngle = 0.0f;
    private float elapsedSway = 0.0f;
    private float swayDegree = swayDegreeDefault;
    private float swaySensitivity = swayScensitivityDefault;
    private boolean swayRunning = false;
    private Vector3f size = new Vector3f();
    
    
    public NaturalMotionWithSwayAnimation(int defaultDuration) {
        super(defaultDuration);
    }
    
    public NaturalMotionWithSwayAnimation(int defaultDuration, 
        ChangeVisibleAnimationPlugin cvaPlugin) 
    {
        super(defaultDuration, cvaPlugin);
    }
    
    public void setSwayCenter(float x, float z) {
        this.centerX = x;
        this.centerZ = z;
    }
    
    public void initialize() {
        super.initialize();
        
        resilientSmoother = new SpringFloatSmoother();
        swaySubAnim 
            = new SubAnimation() {
                public void doTimedAnimation(float elapsedDuration) {
                    elapsedSway = elapsedDuration;
                    
                    float angle = getRotationAngleSmoother().getValue(elapsedAngle);
                    float angleS = resilientSmoother.getValue(elapsedSway);
                    setRotationAngle(angle + angleS);
                    
                    getTranslationSmoother().getValue(translation, elapsedTrans);
                    updateShiftXZ(angleS);
                    translation.x += shiftX;
                    translation.z += shiftZ;
                    setTranslation(translation);
                    
                    swayRunning = (elapsedSway < 1.0f);
                }
            };
            
        manualMoveEventAdapter = new Component3DManualMoveEventAdapter(
            new ActionBooleanFloat3() {
                public void performAction(LgEventSource source, 
                        boolean started, float x, float y, float z) 
                {
                    assert(source instanceof Component3D);
                    if (started) {
                        centerXSaved = centerX;
                        centerZSaved = centerZ;
                        centerX = x;
                        centerZ = z;
                    } else {
                        centerX = centerXSaved;
                        centerZ = centerZSaved;
                    }
                }
            });
            
        getTarget().addListenerToComponent3D(manualMoveEventAdapter);
    }
    
    private void updateShiftXZ(float angleS) {
       float sx = centerX * (float)Math.cos(angleS) 
                    + centerZ * (float)Math.sin(angleS);
       float sz = centerX * -(float)Math.sin(angleS) 
                    + centerZ * (float)Math.cos(angleS);
       shiftX = centerX - sx;
       shiftZ = centerZ - sz; 
    }
    
    protected SubAnimation initializeTranslationSubAnimation() {
        return 
            new SubAnimation() {
                public void doTimedAnimation(float elapsedDuration) {
                    elapsedTrans = elapsedDuration;
                    
                    getTranslationSmoother().getValue(translation, elapsedTrans);
                    float angleS = resilientSmoother.getValue(elapsedSway);
                    updateShiftXZ(angleS);
                    translation.x += shiftX;
                    translation.z += shiftZ;
                    setTranslation(translation);
                }
            };
    }
    
    protected SubAnimation initializeRotationAngleSubAnimation() {
        return 
            new SubAnimation() {
                public void doTimedAnimation(float elapsedDuration) {
                    elapsedAngle = elapsedDuration;
                    
                    float angle = getRotationAngleSmoother().getValue(elapsedAngle);
                    float angleS = resilientSmoother.getValue(elapsedSway);
                    setRotationAngle(angle + angleS);
                }
            };
    }
    
    public void destroy() {
        getTarget().removeListenerFromComponent3D(manualMoveEventAdapter);
        super.destroy();
    }
    
    public void changeTranslation(float x, float y, float z, int duration) {
        super.changeTranslation(x, y, z, duration);
        
        if (Float.isNaN(prevX)) {
            prevX = x;
            return;
        }
        
        float dx = x - prevX;
        prevX = x;
        if (dx < -swaySensitivity) dx = -swaySensitivity;
        if (dx >  swaySensitivity) dx =  swaySensitivity;
        dx /= swaySensitivity;
        
        float targetWidth = getTarget().getComponent3DPreferredSize(size).x;
        if (targetWidth < swayTargetMinWidth) {
            targetWidth = swayTargetMinWidth;
        }
        resilientSmoother.setTargetValue(swayDegree * dx * 4 / targetWidth);
        startSubAnimation(swaySubAnim, 1000);
    }
}
