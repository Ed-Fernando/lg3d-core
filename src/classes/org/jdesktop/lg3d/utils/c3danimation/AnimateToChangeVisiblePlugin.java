/*
 * Project Looking Glass
 *
 * $RCSfile: AnimateToChangeVisiblePlugin.java,v $
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
 * $Date: 2006-08-14 23:13:32 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.c3danimation;

import org.jdesktop.lg3d.utils.animation.ConcurrentTimedSubAnimationHelper;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Component3DAnimationTarget;
import org.jdesktop.lg3d.utils.smoother.Vector3fTransitionSmoother;
import org.jdesktop.lg3d.utils.smoother.FloatTransitionSmoother;
import org.jdesktop.lg3d.utils.smoother.TransitionSmoother;
import javax.vecmath.Vector3f;

/**
 *
 */
public class AnimateToChangeVisiblePlugin implements ChangeVisibleAnimationPlugin {
    private Component3D origin;
    private Component3D target;
    private boolean twist;
    private PluggableC3DAnimation anim;
    private int defaultDuration;
    private boolean visible;
    private ConcurrentTimedSubAnimationHelper.SubAnimation visAppAnim;
    private Vector3fTransitionSmoother replacingTransSmoother = null;
    private FloatTransitionSmoother replacingAngleSmoother = null;
    private FloatTransitionSmoother replacingScaleSmoother = null;
    private Vector3fTransitionSmoother savedTransSmoother;
    private FloatTransitionSmoother savedAngleSmoother;
    private TransitionSmoother savedScaleSmoother;
    private float savedScale = 1.0f;
    private float savedAngle = 0.0f;
    private Vector3f savedTranslation = null;
    private Vector3f tmpV3f = new Vector3f();
    
    public AnimateToChangeVisiblePlugin(Component3D origin, Component3D target, int duration) {
        this(origin, target, duration, false);
    }
    
    public AnimateToChangeVisiblePlugin(Component3D origin, Component3D target, int duration, boolean twist) {
        if (origin == null) {
            throw new IllegalArgumentException("the origin argument cannot be null");
        }
        this.origin = origin;
        this.target = target;
        this.defaultDuration = duration;
        this.twist = twist;
    }
    
    public AnimateToChangeVisiblePlugin(Component3D origin, Component3D reference, 
        int duration, boolean twist,
        Vector3fTransitionSmoother replacingTransSmoother,
        FloatTransitionSmoother replacingAngleSmoother,
        FloatTransitionSmoother replacingScaleSmoother) 
    {
        this(origin, reference, duration, twist);
        this.replacingTransSmoother = replacingTransSmoother;
        this.replacingAngleSmoother = replacingAngleSmoother;
        this.replacingScaleSmoother = replacingScaleSmoother;
    }
    
    public void initialize(final PluggableC3DAnimation anim) {
        this.anim = anim;
        
        visAppAnim 
            = new ConcurrentTimedSubAnimationHelper.SubAnimation() {
                public void doTimedAnimation(float elapsedDuration) {
                    if (elapsedDuration == 1.0f) {
                        restoreSmoothers();
                        if (!anim.isFinalVisible()) {
                            // make the object invisible after finishing the animation
                            Component3DAnimationTarget animTarget = (Component3DAnimationTarget)anim.getTarget();
                            animTarget.setVisible(false);
                        }
                    }
                }
            };
    }
    
    public void destroy() {
        // do nothing
    }
    
    public boolean isVisible() {
        return ((Component3DAnimationTarget)anim.getTarget()).isVisible();
    }
    
    public boolean isFinalVisible() {
        return visible;
    }
    
    public void changeVisible(boolean visible, int duration) {
        if (this.visible == visible && duration != 0) {
            return;
        }
        this.visible = visible;
        Component3DAnimationTarget animTarget = (Component3DAnimationTarget)anim.getTarget();
        if (animTarget.isVisible() == visible) {
            return;
        }
        
        if (visible) {
            if (savedTranslation == null) { // if this is very first time
                // save the initial configuration
                savedScale = anim.getFinalScale();
                savedAngle = anim.getFinalRotationAngle();
                savedTranslation = anim.getFinalTranslation(new Vector3f());
            }
            
            // before making this visible, update the translation to match
            // the current position of referenced object
            if (target != null) {
                float tps = target.getPreferredSize(tmpV3f).x;
                float tfs = target.getFinalScale();
                float ops = animTarget.getComponent3DPreferredSize(tmpV3f).x;
                float scale = (ops != 0.0f)?(tps * tfs / ops):(0.1f);
                
                float angle = target.getFinalRotationAngle();
                if (twist) {
                    angle += (float)Math.toRadians(360);
                }
                if (target.isLive()) {
                    origin.getTranslationTo(target, tmpV3f);
                    anim.changeTranslation(tmpV3f.x, tmpV3f.y, tmpV3f.z, 0);
                    anim.changeScale(scale, 0);
                    anim.changeRotationAngle(angle, 0);
                }
            }
            
            // make the object visible before starting the animation
            animTarget.setVisible(true);
            
            replaceSmoothers();
            
            anim.changeScale(savedScale, duration);
            anim.changeRotationAngle(savedAngle, duration);
            anim.changeTranslation(savedTranslation.x, savedTranslation.y, savedTranslation.z, duration);
        } else {
            savedScale = anim.getFinalScale();
            savedAngle = anim.getFinalRotationAngle();
            if (savedTranslation == null) {
                savedTranslation = new Vector3f();
            }
            anim.getFinalTranslation(savedTranslation);
            
            float scale = 1.0f;
            float angle = 0.0f;
            tmpV3f.set(0.0f, 0.0f, 0.0f);
            if (target != null) {
                float tps = target.getPreferredSize(tmpV3f).x;
                float tfs = target.getFinalScale();
                float ops = animTarget.getComponent3DPreferredSize(tmpV3f).x;
                
                scale = (ops != 0.0f)?(tps * tfs / ops):(0.1f);
                
                angle += target.getFinalRotationAngle();
                try {
                    origin.getTranslationTo(target, tmpV3f);
                } catch (RuntimeException ex) {
                    // ignore -- make the best effort in carrying on the animation
                }
            }
            if (twist) {
                angle -= (float)Math.toRadians(360);
            }
            
            replaceSmoothers();
            
            anim.changeTranslation(tmpV3f.x, tmpV3f.y, tmpV3f.z, duration);
            anim.changeScale(scale, duration);
            anim.changeRotationAngle(angle, duration);
        }
        anim.startSubAnimation(visAppAnim, duration);
    }
    
    private void replaceSmoothers() {
        if (replacingTransSmoother != null) {
            savedTransSmoother = anim.getTranslationSmoother();
            anim.setTranslationSmoother(replacingTransSmoother);
        }
        if (replacingAngleSmoother != null) {
            savedAngleSmoother = anim.getRotationAngleSmoother();
            anim.setRotationAngleSmoother(replacingAngleSmoother);
        }
        if (replacingScaleSmoother != null) {
            savedScaleSmoother = anim.getScaleSmoother();
            anim.setScaleSmoother(replacingScaleSmoother);
        }
    }
    
    private void restoreSmoothers() {
        if (savedTransSmoother != null) {
            anim.setTranslationSmoother(savedTransSmoother);
            savedTransSmoother = null;
        }
        if (savedAngleSmoother != null) {
            anim.setRotationAngleSmoother(savedAngleSmoother);
            savedAngleSmoother = null;
        }
        if (savedScaleSmoother != null) {
            if (savedScaleSmoother instanceof FloatTransitionSmoother) {
                anim.setScaleSmoother((FloatTransitionSmoother)savedScaleSmoother);
            } else {
                assert(savedScaleSmoother instanceof Vector3fTransitionSmoother);
                anim.setScaleSmoother((Vector3fTransitionSmoother)savedScaleSmoother);
            }
            savedScaleSmoother = null;
        }
    }
    
    public int getDefaultVisibleDuration() {
        return defaultDuration;
    }
}
