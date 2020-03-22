/*
 * Project Looking Glass
 *
 * $RCSfile: NullFrame3DAnimation.java,v $
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
 * $Revision: 1.3 $
 * $Date: 2006-06-09 18:36:54 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg;

import java.util.EnumSet;
import javax.vecmath.Vector3f;
import javax.vecmath.AxisAngle4f;


/**
 *
 */
class NullFrame3DAnimation extends Frame3DAnimation {
    
    private Vector3f translation = new Vector3f();
    private AxisAngle4f rotation = new AxisAngle4f(1.0f, 0.0f, 0.0f, 0.0f);
    private Vector3f scale = new Vector3f(1.0f, 1.0f, 1.0f);
    
    /**
     * Creates a new instance of NullContainer3DAnimation 
     */
    public NullFrame3DAnimation() {
    }
    
    public void initialize() {
        // nothing to do
    }
    
    public void destroy() {
        // nothing to do
    }
    
    public void doAnimation() {
        // no animation
    }
    
    /**
     * Change the translation to the given value over the specified duration
     */
    public void changeTranslation(float x, float y, float z, int duration) {
        translation.set(x, y, z);
        getTarget().setTranslation(translation);
    }
   
    /**
     * Change the rotation to the given value over the specified duration
     */
    public void changeRotationAngle(float angle, int duration) {
        rotation.angle = angle;
        getTarget().setRotation(rotation);
    }
    
    /**
     * Change the rotation to the given value over the specified duration
     */
    public void changeRotationAxis(float x, float y, float z, int duration) {
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
        getTarget().setRotation(rotation);
    }
    
    /**
     * Change the scale to the given value over the specified duration
     */
    public void changeScale(float s, int duration) {
        scale.set(s, s, s);
        getTarget().setScale(s /* do not put 'scale' here*/);
    }
    
    /**
     * Change the scale to the given value over the specified duration
     */
    public void changeScale(float x, float y, float z, int duration) {
        scale.set(x, y, z);
        getTarget().setScale(scale);
    }
    
    /**
     * Change the transparency to the given value over the specified duration
     */
    public void changeTransparency(float transparency, int duration) {
        getTarget().setTransparency(transparency);
    }
    
    /**
     * Change the visibility of the app (synonymous with iconize)
     */
    public void changeVisible(boolean visible, int duration) {
        assert(getTarget() instanceof Component3DAnimationTarget);
        ((Component3DAnimationTarget)getTarget()).setVisible(visible);
    }
    
    /**
     * Enable/disable the app (synonymous with close)
     */
    public void changeEnabled(boolean enabled, int duration) {
        assert(getTarget() instanceof Frame3DAnimationTarget);
        ((Frame3DAnimationTarget)getTarget()).setEnabled(enabled);
    }
    
    /**
     * Get the final value of the translation, ie the translation which will
     * be reached when the duration is complete
     */
    public Vector3f getFinalTranslation(Vector3f translation) {
        return getTranslation(translation);
    }
    
    /**
     * Get the current translation
     */
    public Vector3f getTranslation(Vector3f translation) {
        translation.set(this.translation);
        return translation;
    }
    
    /**
     * Get the final value of rotation to which this animation is iterating
     */
    public Vector3f getFinalRotationAxis(Vector3f axis) {
        return getRotationAxis(axis);
    }
    
    /**
     * Get the current rotation
     */
    public Vector3f getRotationAxis(Vector3f axis) {
        axis.x = rotation.x;
        axis.y = rotation.y;
        axis.z = rotation.z;
        return axis;
    }
    
    /**
     * Get the current rotation
     */
    public float getFinalRotationAngle() {
        return getRotationAngle();
    }
    
    /**
     * Get the current rotation
     */
    public float getRotationAngle() {
        return rotation.angle;
    }
    
    /**
     * Get the final value of scale to which this animation is iterating
     */
    public Vector3f getFinalScale(Vector3f scale) {
        return getScale(scale);
    }
    
    /**
     * Get the final value of scale to which this animation is iterating
     */
    public float getFinalScale() {
        return getScale();
    }
    
    /**
     * Get the current scale
     */
    public Vector3f getScale(Vector3f scale) {
        getTarget().getScale(scale);
        return scale;
    }
    
    /**
     * Get the current scale
     */
    public float getScale() {
        return getTarget().getScale();
    }
    
    /**
     * Get the current scale
     */
    public float getFinalTransparency() {
        return getTransparency();
    }
    
    /**
     * Get the current scale
     */
    public float getTransparency() {
        return getTarget().getTransparency();
    }
    
    
    /**
     * Get the visibility to which this animation is iterating
     */
    public boolean isFinalVisible() {
        return isVisible();
    }
    
    /**
     * Get the final value of visibility to which this animation is iterating
     */
    public boolean isVisible() {
        assert(getTarget() instanceof Component3DAnimationTarget);
        return ((Component3DAnimationTarget)getTarget()).isVisible();
    }
    
    /**
     * Get the enabled to which this animation is iterating
     */
    public boolean isFinalEnabled() {
        return isEnabled();
    }
    
    /**
     * Get the current enabled status
     */
    public boolean isEnabled() {
        assert(getTarget() instanceof Frame3DAnimationTarget);
        return ((Frame3DAnimationTarget)getTarget()).isEnabled();
    }
    
    /**
     * Get the default duration for this animation
     */
    public int getDefaultTranslationDuration() {
        return 0;
    }
    
    /**
     * Get the default duration for this animation
     */
    public int getDefaultRotationAngleDuration() {
        return 0;
    }
    
    /**
     * Get the default duration for this animation
     */
    public int getDefaultRotationAxisDuration() {
        return 0;
    }
    
    /**
     * Get the default duration for this animation
     */
    public int getDefaultScaleDuration() {
        return 0;
    }
    
    /**
     * Get the default duration for this animation
     */
    public int getDefaultVisibleDuration() {
        return 0;
    }
    
    /**
     * Get the default duration for this animation
     */
    public int getDefaultEnabledDuration() {
        return 0;
    }
    
    public void copyStatusTo(Component3DAnimation anim) {
        Vector3f tmp = new Vector3f();
        anim.changeTranslation(getFinalTranslation(tmp).x, tmp.y, tmp.z, 0);
        anim.changeRotationAxis(getFinalRotationAxis(tmp).x, tmp.y, tmp.z, 0);
        // no animation for rotation axis
        anim.changeRotationAngle(getFinalRotationAngle(), 0);
        anim.changeScale(getScale(), 0);
        anim.changeTransparency(getFinalTransparency(), 0);
    }
    
    protected java.util.EnumSet<AnimationType> getAnimationParameters() {
        return EnumSet.of(AnimationType.TRANSFORM, AnimationType.TRANSPARENCY);
    }
}
