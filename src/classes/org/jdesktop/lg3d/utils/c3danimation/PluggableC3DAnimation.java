/*
 * Project Looking Glass
 *
 * $RCSfile: PluggableC3DAnimation.java,v $
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
 * $Revision: 1.9 $
 * $Date: 2006-08-14 23:47:44 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.c3danimation;

import java.util.EnumSet;
import javax.vecmath.Vector3f;
import javax.vecmath.AxisAngle4f;
import org.jdesktop.lg3d.utils.animation.ConcurrentTimedSubAnimationHelper;
import org.jdesktop.lg3d.utils.animation.ConcurrentTimedSubAnimationHelper.SubAnimation;
import org.jdesktop.lg3d.utils.smoother.FloatTransitionSmoother;
import org.jdesktop.lg3d.utils.smoother.Vector3fTransitionSmoother;
import org.jdesktop.lg3d.utils.smoother.TransitionSmoother;
import org.jdesktop.lg3d.wg.Component3DAnimation;
import org.jdesktop.lg3d.wg.Component3DAnimationTarget;


/**
 *
 */
public class PluggableC3DAnimation extends Component3DAnimation {
    private int defaultDuration;
    
    private ConcurrentTimedSubAnimationHelper animHelper;
    private SubAnimation translationAnim;
    private SubAnimation rotationAnim;
    private SubAnimation scaleAnim;
    private SubAnimation transparencyAnim;
    private Vector3fTransitionSmoother translationSmoother;
    private FloatTransitionSmoother rotationSmoother;
    private FloatTransitionSmoother scaleSmoother;
    private Vector3fTransitionSmoother scaleV3fSmoother;
    private FloatTransitionSmoother transparencySmoother;
    private ChangeVisibleAnimationPlugin cvaPlugin;
    
    private Vector3f translation = new Vector3f();
    private AxisAngle4f rotation = new AxisAngle4f(1.0f, 0.0f, 0.0f, 0.0f);
    private Vector3f scaleV3f = new Vector3f(1.0f, 1.0f, 1.0f);
    private Vector3f changeTrans = new Vector3f();
    
    
    /**
     * Creates a new instance of PluggableC3DAnimation
     */
    public PluggableC3DAnimation(int defaultDuration, 
            Vector3fTransitionSmoother translationSmoother,
            FloatTransitionSmoother rotationSmoother,
            FloatTransitionSmoother scaleSmoother,
            FloatTransitionSmoother transparencySmoother) 
    {
        this(defaultDuration, 
            translationSmoother, rotationSmoother, scaleSmoother, transparencySmoother,
            new NullCVAnimationPlugin());
    }
    
    /**
     * Creates a new instance of PluggableC3DAnimation
     */
    public PluggableC3DAnimation(int defaultDuration, 
            Vector3fTransitionSmoother translationSmoother,
            FloatTransitionSmoother rotationSmoother,
            Vector3fTransitionSmoother scaleV3fSmoother,
            FloatTransitionSmoother transparencySmoother) 
    {
        this(defaultDuration, 
            translationSmoother, rotationSmoother, scaleV3fSmoother, transparencySmoother,
            new NullCVAnimationPlugin());
    }
    
    /**
     * Creates a new instance of PluggableC3DAnimation
     */
    public PluggableC3DAnimation(int defaultDuration, 
            Vector3fTransitionSmoother translationSmoother,
            FloatTransitionSmoother rotationSmoother,
            FloatTransitionSmoother scaleSmoother,
            FloatTransitionSmoother transparencySmoother,
            ChangeVisibleAnimationPlugin cvaPlugin) 
    {
        this(defaultDuration, 
            translationSmoother, rotationSmoother, 
            (TransitionSmoother)scaleSmoother, transparencySmoother, cvaPlugin);
    }
    
    /**
     * Creates a new instance of PluggableC3DAnimation
     */
    public PluggableC3DAnimation(int defaultDuration, 
            Vector3fTransitionSmoother translationSmoother,
            FloatTransitionSmoother rotationSmoother,
            Vector3fTransitionSmoother scaleV3fSmoother,
            FloatTransitionSmoother transparencySmoother,
            ChangeVisibleAnimationPlugin cvaPlugin) 
    {
        this(defaultDuration, 
            translationSmoother, rotationSmoother, 
            (TransitionSmoother)scaleV3fSmoother, transparencySmoother, cvaPlugin);
    }
    
    private PluggableC3DAnimation(int defaultDuration, 
        Vector3fTransitionSmoother translationSmoother,
        FloatTransitionSmoother rotationSmoother,
        TransitionSmoother scaleSmoother,
        FloatTransitionSmoother transparencySmoother,
        ChangeVisibleAnimationPlugin cvaPlugin) 
    {
        if (translationSmoother == null 
                || rotationSmoother == null 
                || scaleSmoother == null
                || transparencySmoother == null) 
        {
            throw new IllegalArgumentException("the smoother argument cannot be null");
        }
        if (cvaPlugin == null) {
            throw new IllegalArgumentException("the plugin argument cannot be null");
        }
        
        this.defaultDuration = defaultDuration;
        this.translationSmoother = translationSmoother;
        this.rotationSmoother = rotationSmoother;
        this.transparencySmoother = transparencySmoother;
        this.cvaPlugin = cvaPlugin;
        
        if (scaleSmoother instanceof FloatTransitionSmoother) {
            this.scaleSmoother = (FloatTransitionSmoother)scaleSmoother;
        } else if (scaleSmoother instanceof Vector3fTransitionSmoother) {
            this.scaleV3fSmoother = (Vector3fTransitionSmoother)scaleSmoother;
        } else {
            throw new IllegalArgumentException("illegal class of scaleSmoother: " 
                    + scaleSmoother.getClass());
        }
    }
    
    
    public void initialize() {
        animHelper = new ConcurrentTimedSubAnimationHelper(this);
        
        translationAnim = initializeTranslationSubAnimation();        
        rotationAnim = initializeRotationAngleSubAnimation();
        scaleAnim = initializeScaleSubAnimation();
        transparencyAnim = initializeTransparencySubAnimation();
            
        cvaPlugin.initialize(this);
    }
    
    protected SubAnimation initializeTranslationSubAnimation() {
        return 
            new SubAnimation() {
                public void doTimedAnimation(float elapsedDuration) {
                    getTranslationSmoother().getValue(translation, elapsedDuration);
                    setTranslation(translation);
                }
            };
    }
    
    protected SubAnimation initializeRotationAngleSubAnimation() {
        return 
            new SubAnimation() {
                public void doTimedAnimation(float elapsedDuration) {
                    float angle = getRotationAngleSmoother().getValue(elapsedDuration);
                    setRotationAngle(angle);
                }
            };
    }
    
    protected SubAnimation initializeScaleSubAnimation() {
        return
            new SubAnimation() {
                public void doTimedAnimation(float elapsedDuration) {
                    TransitionSmoother ssmoother = getScaleSmoother();
                    if (ssmoother instanceof FloatTransitionSmoother) {
                        float scale = ((FloatTransitionSmoother)ssmoother).getValue(elapsedDuration);
                        setScale(scale);
                    } else {
                        assert(ssmoother instanceof Vector3fTransitionSmoother);
                        ((Vector3fTransitionSmoother)ssmoother).getValue(scaleV3f, elapsedDuration);
                        setScale(scaleV3f);
                    }
                }
            };
    }
    
    protected SubAnimation initializeTransparencySubAnimation() {
        return 
            new SubAnimation() {
                public void doTimedAnimation(float elapsedDuration) {
                    float transparency = transparencySmoother.getValue(elapsedDuration);
                    setTransparency(transparency);
                }
            };
    }
    
    public Component3DAnimationTarget getTarget() {
        return (Component3DAnimationTarget)super.getTarget();
    }
    
    protected void setTranslation(Vector3f translation) {
        getTarget().setTranslation(translation);
    }
    
    protected void setRotationAngle(float angle) {
        rotation.angle = angle;
        getTarget().setRotation(rotation);
    }
    
    protected void setScale(float scale) {
        getTarget().setScale(scale);
    }
    
    protected void setScale(Vector3f scale) {
        getTarget().setScale(scale);
    }
    
    protected void setTransparency(float transparency) {
        getTarget().setTransparency(transparency);
    }
    
    public void doAnimation() {
        animHelper.doAnimation();
    }
    
    public void startSubAnimation(SubAnimation item, int duration) {
        animHelper.start(item, duration);
    }
    
    public void destroy() {
        cvaPlugin.destroy();
    }
    
    public void setTranslationSmoother(Vector3fTransitionSmoother smoother) {
        if (smoother == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
        Vector3f v3f = new Vector3f();
        smoother.setInternalValue(getTranslation(v3f));
        smoother.setTargetValue(getFinalTranslation(v3f));
        translationSmoother = smoother;
    }
    
    public Vector3fTransitionSmoother getTranslationSmoother() {
        return translationSmoother;
    }
    
    public void setRotationAngleSmoother(FloatTransitionSmoother smoother) {
        if (smoother == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
        smoother.setInternalValue(getRotationAngle());
        smoother.setTargetValue(getFinalRotationAngle());
        rotationSmoother = smoother;
    }
    
    public FloatTransitionSmoother getRotationAngleSmoother() {
        return rotationSmoother;
    }
    
    public void setScaleSmoother(FloatTransitionSmoother smoother) {
        if (smoother == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
        smoother.setInternalValue(getScale());
        smoother.setTargetValue(getFinalScale());
        scaleSmoother = smoother;
        scaleV3fSmoother = null;
    }
    
    public void setScaleSmoother(Vector3fTransitionSmoother smoother) {
        if (smoother == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
        getScale(scaleV3f);
        smoother.setInternalValue(scaleV3f);
        getFinalScale(scaleV3f);
        smoother.setTargetValue(scaleV3f);
        scaleV3fSmoother = smoother;
        scaleSmoother = null;
    }
    
    public TransitionSmoother getScaleSmoother() {
        if (scaleSmoother != null) {
            return scaleSmoother;
        } else {
            assert(scaleV3fSmoother != null);
            return scaleV3fSmoother;
        }
    }
    
    public void setTransparencySmoother(FloatTransitionSmoother smoother) {
        if (smoother == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
        smoother.setInternalValue(getTransparency());
        smoother.setTargetValue(getFinalTransparency());
        transparencySmoother = smoother;
    }
    
    public FloatTransitionSmoother getTransparencySmoother() {
        return transparencySmoother;
    }
    
    /**
     * Change the translation to the given value over the specified duration
     */
    public void changeTranslation(float x, float y, float z, int duration) {
        changeTrans.set(x, y, z);
        translationSmoother.setTargetValue(changeTrans);
        startSubAnimation(translationAnim, duration);
    }
   
    /**
     * Change the rotation angle to the given value over the specified duration
     */
    public void changeRotationAngle(float angle, int duration) {
        rotationSmoother.setTargetValue(angle);
        startSubAnimation(rotationAnim, duration);
    }
    
    /**
     * Change the rotation axis to the given value over the specified duration.
     * Note that the new axis takes effect when changeRotationAngle() is invoked. 
     * By calling this itself doesn't cause any visual change.
     */
    public void changeRotationAxis(float x, float y, float z, int duration) {
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
//        getTarget().setRotation(rotation);
    }
    
    /**
     * Change the scale to the given value over the specified duration
     */
    public void changeScale(float scale, int duration) {
        if (scaleSmoother != null) {
            scaleSmoother.setTargetValue(scale);
        } else {
            assert(scaleV3fSmoother != null);
            scaleV3f.set(scale, scale, scale);
            scaleV3fSmoother.setTargetValue(scaleV3f);
        }
        startSubAnimation(scaleAnim, duration);
    }
    
    /**
     * Change the scale to the given value over the specified duration
     */
    public void changeScale(float x, float y, float z, int duration) {
        if (scaleV3fSmoother == null) {
            throw new RuntimeException("cannot specify non-uniform scale with FloatTransitionSmoother");
        }
        scaleV3f.set(x, y, z);
        scaleV3fSmoother.setTargetValue(scaleV3f);
        startSubAnimation(scaleAnim, duration);
    }
    
    /**
     * Change the transparency to the given value over the specified duration
     */
    public void changeTransparency(float transparency, int duration) {
        transparencySmoother.setTargetValue(transparency);
        startSubAnimation(transparencyAnim, duration);
    }
     
    /**
     * Change the visibility of the app (synonymous with iconize)
     */
    public void changeVisible(boolean visible, int duration) {
        cvaPlugin.changeVisible(visible, duration);
    }
    
    /**
     * Get the final value of the translation, ie the translation which will
     * be reached when the duration is complete
     */
    public Vector3f getFinalTranslation(Vector3f trans) {
        return translationSmoother.getFinalValue(trans);
    }
    
    /**
     * Get the current translation
     */
    public Vector3f getTranslation(Vector3f trans) {
        return translationSmoother.getLatestValue(trans);
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
        return rotationSmoother.getFinalValue();
    }
    
    /**
     * Get the current rotation
     */
    public float getRotationAngle() {
        return rotationSmoother.getLatestValue();
    }
    
    /**
     * Get the final value of scale to which this animation is iterating
     */
    public Vector3f getFinalScale(Vector3f scaleV3f) {
        if (scaleV3fSmoother == null) {
            throw new RuntimeException("cannot get non-uniform scale from FloatTransitionSmoother");
        }
        return scaleV3fSmoother.getFinalValue(scaleV3f);
    }
    
    /**
     * Get the final value of scale to which this animation is iterating
     */
    public float getFinalScale() {
        return scaleSmoother.getFinalValue();
    }
    
    /**
     * Get the current scale
     */
    public Vector3f getScale(Vector3f scaleV3f) {
        if (scaleV3fSmoother == null) {
            throw new RuntimeException("cannot get non-uniform scale from FloatTransitionSmoother");
        }
        return scaleV3fSmoother.getLatestValue(scaleV3f);
    }
    
    /**
     * Get the current scale
     */
    public float getScale() {
        return scaleSmoother.getLatestValue();
    }
    
    /**
     * Get the final value of the translation, ie the translation which will
     * be reached when the duration is complete
     */
    public float getFinalTransparency() {
        return transparencySmoother.getFinalValue();
    }
    
    /**
     * Get the current translation
     */
    public float getTransparency() {
        return transparencySmoother.getLatestValue();
    }
    
    /**
     * Get the visibility to which this animation is iterating
     */
    public boolean isFinalVisible() {
        return cvaPlugin.isFinalVisible();
    }
    
    /**
     * Get the visibility
     */
    public boolean isVisible() {
        return cvaPlugin.isVisible();
    }
    
    /**
     * Get the default duration for this animation
     */
    public int getDefaultTranslationDuration() {
        return defaultDuration;
    }
    
    /**
     * Get the default duration for this animation
     */
    public int getDefaultRotationAngleDuration() {
        return defaultDuration;
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
        return defaultDuration;
    }
    
    /**
     * Get the default duration for this animation
     */
    public int getDefaultVisibleDuration() {
        return cvaPlugin.getDefaultVisibleDuration();
    }
    
    public void copyStatusTo(Component3DAnimation anim) {
        // I prefered the name and behavior to be copyStatusFrom,
        // but I ended up using this since I needed to access some internal
        // info to obtain the current animation status (whereas I can set
        // animation status using public methods only).
        Vector3f tmp = new Vector3f();
        anim.changeTranslation(getTranslation(tmp).x, tmp.y, tmp.z, 0);
        anim.changeTranslation(getFinalTranslation(tmp).x, tmp.y, tmp.z, translationAnim.getReaminingDuration());
        // no animation for rotation axis
        anim.changeRotationAxis(getFinalRotationAxis(tmp).x, tmp.y, tmp.z, 0);
        anim.changeRotationAngle(getRotationAngle(), 0);
        anim.changeRotationAngle(getFinalRotationAngle(), rotationAnim.getReaminingDuration());
        anim.changeScale(getScale(), 0);
        anim.changeScale(getFinalScale(), scaleAnim.getReaminingDuration());
        anim.changeTransparency(getTransparency(), 0);
        anim.changeTransparency(getFinalTransparency(), transparencyAnim.getReaminingDuration());
        
        // TODO -- is the following OK?
        anim.changeVisible(isFinalVisible(), 0);        
    }
    
    protected EnumSet<AnimationType> getAnimationParameters() {
        return EnumSet.of(AnimationType.TRANSPARENCY, AnimationType.TRANSFORM);
    }
    
    private static class NullCVAnimationPlugin implements ChangeVisibleAnimationPlugin {
        private Component3DAnimationTarget animTarget;
        
        public void initialize(PluggableC3DAnimation anim) {
            animTarget = (Component3DAnimationTarget)anim.getTarget();
        }
        
        public void destroy() {
            animTarget = null;
        }
        
        public boolean isVisible() {
            return animTarget.isVisible();
        }
        
        public boolean isFinalVisible() {
            return isVisible();
        }
        
        public void changeVisible(boolean visible, int duration) {
            animTarget.setVisible(visible);
        }
        
        public int getDefaultVisibleDuration() {
            return 0;
        }
    }
}
