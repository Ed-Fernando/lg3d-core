/**
 * Project Looking Glass
 *
 * $RCSfile: Component3DAnimationTarget.java,v $
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
 * $Revision: 1.12 $
 * $Date: 2007-04-11 20:13:43 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg;

import java.lang.ref.WeakReference;
import java.util.logging.Logger;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.sg.Transform3D;
import org.jdesktop.lg3d.wg.event.LgEventListener;


/**
 * Animation Target for the Component3D animations, only a single
 * Component3DAnimationTarget can be active at any one time per Component, 
 * the system will enforce this policy.
 *
 * Objects of this class are managed internally to this package. 
 *
 */
public class Component3DAnimationTarget implements AnimationTarget {

    // TODO: DJ HACK
    private static boolean inWonderland = false;

    /** the animation logger */
    protected static final Logger logger = Logger.getLogger("lg.animation");
    
    /** the target component for the animation */
    WeakReference<Component3D> targetComponent;
    /** the animation transformation */
    private Transform3D t3d = new Transform3D();
    /** the animation transparency */
    private float transparency;
    /** the animation position */
    private Vector3f position = new Vector3f();
    /** the animation scale */
    private Vector3f scale = new Vector3f(1.0f, 1.0f, 1.0f);
    /** the animation rotation */
    private Quat4f rotation = new Quat4f(); 
    
    static {
	// TODO: DJ HACK
        String wonderlandStr = System.getProperty("wl.fws.x11.wonderland", "false");
	inWonderland = "true".equals(wonderlandStr);
    }

    /** create a new Component3DAnimationTarget */
    Component3DAnimationTarget() {
        
    }
    
    /**
     * get the transparency of the animation
     * @return the transparency
     */
    public float getTransparency() {
        checkLive();
        return transparency;
    }

    /**
     * get the translation of the animation
     * @param pos the Vector3f to set the position in (cannot be null)
     * @return the translation (for chaining)
     */
    public Vector3f getTranslation(Vector3f pos) {
        if (pos == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
        checkLive();
        pos.set(position);
        return pos;
    }

    /**
     * get the rotation of the animation
     * @param rot the AxisAngle4f to set the rotation in (cannot be null)
     * @return the rotation (for chaining)
     */
    public AxisAngle4f getRotation(AxisAngle4f rot) {
        if (rot == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
        checkLive();
        rot.set(rotation);
        return rot;
    }
    
    /**
     * get the uniform scale of the animation
     * @return the uniform scale
     */
    public float getScale() {
        checkLive();
        if (scale.x != scale.y || scale.x != scale.z) {
            return Float.NaN;
        }
        return scale.x;
    }
    
    /**
     * get the scale of the animation
     * @param scale the Vector3f to set the scale in (cannot be null)
     * @return the animation scale (for chaining)
     */
    public Vector3f getScale(Vector3f scale) {
        if (scale == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
        checkLive();
        scale.set(this.scale);
        return scale;
    }

    /**
     * set the transparency of the animation. This sets the transparency
     * of the animation target component.
     * @param transparency the transparency
     */
    public void setTransparency(float transparency) {
        checkLive();
        this.transparency = transparency;
        // We need the following null check -- the transparency manager gets
        // removed when the target component is removed from the scenegraph,
        // while animaiton against that component is still happening.

	// TODO: DJ HACK
	if (!inWonderland) {
	    if(targetComponent.get().getTransparencyManager() != null) {
		targetComponent.get().getTransparencyManager().modifyTransparency(transparency);
	    }
	}
   }
   
    /**
     * set the translation of the animation. This sets the translation
     * of the animation target component.
     * @param pos the position (cannot be null)
     */
    public void setTranslation(Vector3f pos) {
        if (pos == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
        
        checkLive();
        position.set(pos);  
        t3d.setTranslation(position);
        targetComponent.get().setTransform(t3d);
    }

    /**
     * set the rotation of the animation. This sets the rotation of the
     * animation target component.
     * @param rot the rotation (cannot be null)
     */
    public void setRotation(AxisAngle4f rot) {
        if (rot == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
        
        checkLive();
        rotation.set(rot);
        t3d.setRotation(rotation);
        targetComponent.get().setTransform(t3d);
    }

    /**
     * set the uniform scale of the animation. This sets the uniform
     * scale of the animation target component.
     * @param scale the scale
     */
    public void setScale(float scale) {
        checkLive();
        this.scale.set(scale, scale, scale);
        t3d.setScale(scale);
        targetComponent.get().setTransform(t3d);
    }
    
    /**
     * set the scale of the animation. This sets the scale of the
     * animation target component
     * @param scale the scale (cannot be null)
     */
    public void setScale(Vector3f scale) {
        if (scale == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
        
        checkLive();
        this.scale.set(scale);
        t3d.setScale(scale);
        targetComponent.get().setTransform(t3d);
    }
    
    /**
     * utility method to set the rotation, translation and scale of
     * the animation. This sets the rotation, translation and scale of
     * the animation target component.
     * @param rot the rotation (cannot be null)
     * @param pos the translation (cannot be null)
     * @param scale the scale (cannot be null)
     */
    public void set(AxisAngle4f rot, Vector3f pos, Vector3f scale) {
        if (rot == null || pos == null || scale == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
        
        checkLive();
        rotation.set(rot);
        position.set(pos);
        this.scale.set(scale);
        t3d.set(rotation, position, 1.0f);
        t3d.setScale(scale);
    }
    
    /**
     * utility method to set the rotation, translation and uniform scale
     * of the animation. This sets the rotation, translation and uniform
     * scale of the target component.
     * @param rot the rotation (cannot be null)
     * @param pos the translation (cannot be null)
     * @param scale the uniform scale
     */
    public void set(AxisAngle4f rot, Vector3f pos, float scale) {
        if (rot == null || pos == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
        
        checkLive();
        rotation.set(rot);
        position.set(pos);
        this.scale.set(scale, scale, scale);
        t3d.set(rotation, position, scale);
    }
    
    /**
     * set the visibility of the animation target
     * @param visible true to set the animation target component visible, 
     * false invisible
     */
    public void setVisible(boolean visible) {
        checkLive();
        targetComponent.get().setVisibleInternal(visible);
    }
    
    /**
     * Get the visibility of the animation target component
     * @return true the component is visible, else false
     */
    public boolean isVisible() {
        checkLive();
        return targetComponent.get().isVisibleInternal();
    }
    
    // Ulility methods
    /**
     * Gets the preferred size of the animation target component
     * @return the preferred size
     */
    public Vector3f getComponent3DPreferredSize(Vector3f ret) {
        return targetComponent.get().getPreferredSize(ret);
    }
    
    /**
     * Add the specified event listener to the animation target component.
     */
    public void addListenerToComponent3D(LgEventListener listener) {
        targetComponent.get().addListener(listener);
    }
    
    /**
     * Removes the specified event listener from the animation target component.
     */
    public void removeListenerFromComponent3D(LgEventListener listener) {
        targetComponent.get().removeListener(listener);
    }
    
    /**
     * When live the target is attached to the underlying transform group.
     * When not live it is detached from the TransformGroup and any calls to
     * it's get/set functions will throw an exception
     */    
    void checkLive() {
        if (targetComponent == null) {
            throw new IllegalStateException(
                "BAD ACCESS, attempting to set/get data for a non-live animation target");
        }
    }
    
    /**
     * set the target Component3D for this animation to operate on
     * @param target the target Component3D
     */
    void setTargetComponent3D(Component3D target) {
        targetComponent = new WeakReference(target);
    }
    
}
