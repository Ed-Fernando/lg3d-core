/*
 * Project Looking Glass
 *
 * $RCSfile: PluggableF3DAnimation.java,v $
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
 * $Date: 2006-06-09 18:36:56 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.appcontainer;

import java.util.EnumSet;
import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.utils.animation.ConcurrentTimedSubAnimationHelper;
import org.jdesktop.lg3d.utils.c3danimation.PluggableC3DAnimation;
import org.jdesktop.lg3d.utils.smoother.FloatTransitionSmoother;
import org.jdesktop.lg3d.utils.smoother.Vector3fTransitionSmoother;
import org.jdesktop.lg3d.utils.smoother.TransitionSmoother;
import org.jdesktop.lg3d.wg.AnimationTarget;
import org.jdesktop.lg3d.wg.Component3DAnimation;
import org.jdesktop.lg3d.wg.Frame3DAnimation;
import org.jdesktop.lg3d.wg.Frame3DAnimationTarget;


/**
 *
 */
public class PluggableF3DAnimation extends Frame3DAnimation {
    private PluggableC3DAnimation pc3dAnim;
    private ChangeEnabledAnimationPlugin ceaPlugin;
    private boolean enabled;
    
    /**
     * Creates a new instance of C3DNaturalMotionAnimation 
     */
    public PluggableF3DAnimation(PluggableC3DAnimation pc3dAnim) {
        this(pc3dAnim, new NullCEAnimationPlugin());
    }
    
    /**
     * Creates a new instance of C3DNaturalMotionAnimation 
     */
    public PluggableF3DAnimation(PluggableC3DAnimation pc3dAnim,
        ChangeEnabledAnimationPlugin ceaPlugin) 
    {
        if (pc3dAnim == null) {
            throw new IllegalArgumentException("the animation argument cannot be null");
        }
        if (ceaPlugin == null) {
            throw new IllegalArgumentException("the plugin argument cannot be null");
        }
        this.pc3dAnim = pc3dAnim;
        this.ceaPlugin = ceaPlugin;
    }
    
    public void setTarget(AnimationTarget animTarget) {
        super.setTarget(animTarget);
        pc3dAnim.setTarget(animTarget);
    }
    
    public AnimationTarget getTarget() {
        return pc3dAnim.getTarget();
    }
    
    public void setRunning(boolean running) {
        pc3dAnim.setRunning(running);
    }
    
    public boolean isRunning() {
        return pc3dAnim.isRunning();
    }
    
    public void setAnimationFinishedEvent(Class endNotificationEvent) {
        pc3dAnim.setAnimationFinishedEvent(endNotificationEvent);
    }
    
    public void initialize() {
        // pc3dAnim.initialize(); has been invoked as a result of 
        // the invocation of super.setTarget()
        
        // shares the same default duration
        ceaPlugin.initialize(this);
    }
    
    public void destroy() {
        pc3dAnim.destroy();
        ceaPlugin.destroy();
    }
    
    public void setTranslationSmoother(Vector3fTransitionSmoother smoother) {
        pc3dAnim.setTranslationSmoother(smoother);
    }
    
    public Vector3fTransitionSmoother getTranslationSmoother() {
        return pc3dAnim.getTranslationSmoother();
    }
    
    public void setRotationAngleSmoother(FloatTransitionSmoother smoother) {
        pc3dAnim.setRotationAngleSmoother(smoother);
    }
    
    public FloatTransitionSmoother getRotationAngleSmoother() {
        return pc3dAnim.getRotationAngleSmoother();
    }
    
    public void setScaleSmoother(FloatTransitionSmoother smoother) {
        pc3dAnim.setScaleSmoother(smoother);
    }
    
    public TransitionSmoother getScaleSmoother() {
        return pc3dAnim.getScaleSmoother();
    }
    
    public void setTransparencySmoother(FloatTransitionSmoother smoother) {
        pc3dAnim.setTransparencySmoother(smoother);
    }
    
    public FloatTransitionSmoother getTransparencySmoother() {
        return pc3dAnim.getTransparencySmoother();
    }
    
    public void doAnimation() {
//        pc3dAnim.doAnimation();
    }
    
    public void startSubAnimation(ConcurrentTimedSubAnimationHelper.SubAnimation item, int duration) {
        pc3dAnim.startSubAnimation(item, duration);
    }
    
    /**
     * Change the translation to the given value over the specified duration
     */
    public void changeTranslation(float x, float y, float z, int duration) {
        pc3dAnim.changeTranslation(x, y, z, duration);
    }
   
    /**
     * Change the rotation to the given value over the specified duration
     */
    public void changeRotationAngle(float angle, int duration) {
        pc3dAnim.changeRotationAngle(angle, duration);
    }
    
    /**
     * Change the rotation to the given value over the specified duration
     */
    public void changeRotationAxis(float x, float y, float z, int duration) {
        pc3dAnim.changeRotationAxis(x, y, z, duration);
    }
    
    /**
     * Change the scale to the given value over the specified duration
     */
    public void changeScale(float scale, int duration) {
        pc3dAnim.changeScale(scale, duration);
    }
    
    /**
     * Change the scale to the given value over the specified duration
     */
    public void changeScale(float x, float y, float z, int duration) {
        throw new UnsupportedOperationException("not implemented");
    }
    
    /**
     * Change the scale to the given value over the specified duration
     */
    public void changeTransparency(float scale, int duration) {
        pc3dAnim.changeTransparency(scale, duration);
    }
    
    /**
     * Change the visibility of the app (synonymous with iconize)
     */
    public void changeVisible(boolean visible, int duration) {
        pc3dAnim.changeVisible(visible, duration);
    }
    
    /**
     * Change the enabled state of the app
     */
    public void changeEnabled(boolean enabled, int duration) {
        this.enabled = enabled;
        ceaPlugin.changeEnabled(enabled, duration);
    }
    
    /**
     * Get the final value of the translation, ie the translation which will
     * be reached when the duration is complete
     */
    public Vector3f getFinalTranslation(Vector3f trans) {
        return pc3dAnim.getFinalTranslation(trans);
    }
    
    /**
     * Get the current translation
     */
    public Vector3f getTranslation(Vector3f trans) {
        return pc3dAnim.getTranslation(trans);
    }
    
    /**
     * Get the final value of rotation to which this animation is iterating
     */
    public Vector3f getFinalRotationAxis(Vector3f axis) {
        return pc3dAnim.getFinalRotationAxis(axis);
    }
    
    /**
     * Get the current rotation
     */
    public Vector3f getRotationAxis(Vector3f axis) {
        return pc3dAnim.getRotationAxis(axis);
    }
    
    /**
     * Get the current rotation
     */
    public float getFinalRotationAngle() {
        return pc3dAnim.getFinalRotationAngle();
    }
    
    /**
     * Get the current rotation
     */
    public float getRotationAngle() {
        return pc3dAnim.getRotationAngle();
    }
    
    /**
     * Get the final value of scale to which this animation is iterating
     */
    public Vector3f getFinalScale(Vector3f scale) {
        throw new UnsupportedOperationException("not implemented");
    }
    
    /**
     * Get the final value of scale to which this animation is iterating
     */
    public float getFinalScale() {
        return pc3dAnim.getFinalScale();
    }
    
    /**
     * Get the current scale
     */
    public Vector3f getScale(Vector3f scale) {
        throw new UnsupportedOperationException("not implemented");
    }
    
    /**
     * Get the current scale
     */
    public float getScale() {
        return pc3dAnim.getScale();
    }
    
    /**
     * Get the current scale
     */
    public float getTransparency() {
        return pc3dAnim.getTransparency();
    }
    
    /**
     * Get the current scale
     */
    public float getFinalTransparency() {
        return pc3dAnim.getFinalTransparency();
    }
    
    /**
     * Get the visibility to which this animation is iterating
     */
    public boolean isFinalVisible() {
        return pc3dAnim.isFinalVisible();
    }
    
    /**
     * Get the visibility
     */
    public boolean isVisible() {
        return pc3dAnim.isVisible();
    }
    
    /**
     * Get the enabled status to which this animation is iterating
     */
    public boolean isFinalEnabled() {
        return enabled;
    }
    
    /**
     * Get the enabled status
     */
    public boolean isEnabled() {
        return ((Frame3DAnimationTarget)getTarget()).isEnabled();
    }
    
    /**
     * Get the default duration for this animation
     */
    public int getDefaultTranslationDuration() {
        return pc3dAnim.getDefaultTranslationDuration();
    }
    
    /**
     * Get the default duration for this animation
     */
    public int getDefaultRotationAngleDuration() {
        return pc3dAnim.getDefaultRotationAngleDuration();
    }
    
    /**
     * Get the default duration for this animation
     */
    public int getDefaultRotationAxisDuration() {
        return pc3dAnim.getDefaultRotationAxisDuration();
    }
    
    /**
     * Get the default duration for this animation
     */
    public int getDefaultScaleDuration() {
        return pc3dAnim.getDefaultScaleDuration();
    }
    
    /**
     * Get the default duration for this animation
     */
    public int getDefaultVisibleDuration() {
        return pc3dAnim.getDefaultVisibleDuration();
    }
    
    /**
     * Get the default duration for this animation
     */
    public int getDefaultEnabledDuration() {
        return ceaPlugin.getDefaultEnabledDuration();
    }
    
    public void copyStatusTo(Component3DAnimation anim) {
        pc3dAnim.copyStatusTo(anim);
        
        // TODO -- is the following OK?
        if (anim instanceof Frame3DAnimation) {
            ((Frame3DAnimation)anim).changeEnabled(isFinalEnabled(), 0);
        }
    }
    
    protected java.util.EnumSet<org.jdesktop.lg3d.wg.Animation.AnimationType> getAnimationParameters() {
        return EnumSet.of(AnimationType.TRANSPARENCY, AnimationType.TRANSFORM);
    }
    
    private static class NullCEAnimationPlugin implements ChangeEnabledAnimationPlugin {
        private Frame3DAnimationTarget animTarget;
        
        public void initialize(PluggableF3DAnimation anim) {
            animTarget = (Frame3DAnimationTarget)anim.getTarget();
        }
        
        public void destroy() {
            animTarget = null;
        }
        
        public void changeEnabled(boolean enabled, int duration) {
            animTarget.setEnabled(enabled);
        }
        
        public int getDefaultEnabledDuration() {
            return 0;
        }
    }
}
