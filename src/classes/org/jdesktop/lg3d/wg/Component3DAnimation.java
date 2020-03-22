/**
 * Project Looking Glass
 *
 * $RCSfile: Component3DAnimation.java,v $
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
 * $Date: 2006-06-09 18:36:55 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg;

import javax.vecmath.Vector3f;


/**
 * Superclass of all animations for Component3D's
 *
 * @author paulby
 */
public abstract class Component3DAnimation extends Animation {
    /**
     * Change the transparency to the given value over the specified duration
     */
    public abstract void changeTransparency(float transparency, int duration);
    
    /**
     * Change the translation to the given value over the specified duration
     */
    public abstract void changeTranslation(float x, float y, float z, int duration);
   
    /**
     * Change the rotation to the given value over the specified duration
     */
    public abstract void changeRotationAngle(float angle, int duration);
    
    /**
     * Change the rotation to the given value over the specified duration
     */
    public abstract void changeRotationAxis(float x, float y, float z, int duration);
    
    /**
     * Change the scale to the given value over the specified duration
     */
    public abstract void changeScale(float scale, int duration);
    
    /**
     * Change the scale to the given value over the specified duration
     */
    public abstract void changeScale(float x, float y, float z, int duration);
    
    /**
     * Get the final value of the transparency, ie the translation which will
     * be reached when the duration is complete
     */
    public abstract float getFinalTransparency();
    
    /**
     * Get the final value of the translation, ie the translation which will
     * be reached when the duration is complete
     */
    public abstract Vector3f getFinalTranslation(Vector3f translation);
    
    /**
     * Get the current translation
     */
    public abstract float getTransparency();
    
    /**
     * Get the current translation
     */
    public abstract Vector3f getTranslation(Vector3f translation);
    
    /**
     * Get the final value of rotation to which this animation is iterating
     */
    public abstract Vector3f getFinalRotationAxis(Vector3f axis);
    
    /**
     * Get the current rotation
     */
    public abstract Vector3f getRotationAxis(Vector3f axis);
    
    /**
     * Get the final value of rotation to which this animation is iterating
     */
    public abstract float getFinalRotationAngle();
    
    /**
     * Get the current rotation
     */
    public abstract float getRotationAngle();
    
    /**
     * Get the final value of scale to which this animation is iterating
     */
    public abstract float getFinalScale();
    
    /**
     * Get the final value of scale to which this animation is iterating
     */
    public abstract Vector3f getFinalScale(Vector3f scale);
    
    /**
     * Get the current scale
     */
    public abstract float getScale();
    
    /**
     * Get the current scale
     */
    public abstract Vector3f getScale(Vector3f scale);
    
    /**
     * Change the visibility of the app (synonymous with iconize)
     */
    public abstract void changeVisible(boolean visible, int duration);
    
    /**
     * Get the visibility
     */
    public abstract boolean isVisible();
    
    /**
     * Get the visibility to which this animation is iterating
     */
    public abstract boolean isFinalVisible();
    
    /**
     * Get the default duration for this animation
     */
    public abstract int getDefaultTranslationDuration();
    
    /**
     * Get the default duration for this animation
     */
    public abstract int getDefaultRotationAngleDuration();
    
    /**
     * Get the default duration for this animation
     */
    public abstract int getDefaultRotationAxisDuration();

    /**
     * Get the default duration for this animation
     */
    public abstract int getDefaultScaleDuration();
    
    /**
     * Get the default duration for this animation
     */
    public abstract int getDefaultVisibleDuration();
    
    /**
     * Copy the animation status
     */
    public abstract void copyStatusTo(Component3DAnimation anim);
}
