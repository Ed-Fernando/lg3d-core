/**
 * Project Looking Glass
 *
 * $RCSfile: AnimationGroup.java,v $
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
 * $Revision: 1.4 $
 * $Date: 2006-03-28 01:59:54 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg;

import java.util.Enumeration;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.sg.Node;
import org.jdesktop.lg3d.sg.Transform3D;
import org.jdesktop.lg3d.sg.TransformGroup;

/**
 * A scene graph node which connects animations into the scene.
 * This class should be used to allow uniformation/synchronized 
 * animation for different nodes within a scene.
 *
 * @author Paul
 */
public class AnimationGroup extends TransformGroup {
    
    /** the animation of the group */
    private Animation animation = null;
    /** the target of the animation */
    private AnimationGroupTarget target = new AnimationGroupTarget();
    /** a reference to the transparency manager */
    private TransparencyManager transparencyManager;
    
    /**
     * Creates a new instance of AnimationGroup, setting
     * the capability of this node to ALLOW_TRANSFORM_WRITE
     * by default
     */
    public AnimationGroup() {
        setCapability(ALLOW_TRANSFORM_WRITE);
    }
    
    /**
     * Creates a new instance of AnimationGroup, setting
     * the capability of this node to ALLOW_TRANSFORM_WRITE
     * by default, and the default animation
     * @param animation the animation which will operate on this group
     */
    public AnimationGroup(Animation animation) {
        setCapability(ALLOW_TRANSFORM_WRITE);
        setAnimation(animation);
    }
    
    /**
     * Set the animation which will operate on this AnimationGroup.
     * @param newAnimation the animation
     */
    public void setAnimation(Animation newAnimation) {        
        // Disable old animation
        if (animation != null) {
            animation.setTarget(null);
        }
        this.animation = newAnimation;
        if (animation != null) {
            animation.setTarget(target);
            if (animation.getAnimationParameters().contains(Animation.AnimationType.TRANSPARENCY) && transparencyManager==null) {
                createTransparencyManager();
            }
        }
    }
        
    /**
     * Return the animation cting on this group or null if there is no 
     * animation.
     * @param the animation
     */
    public Animation getAnimation() {
        return animation;
    }
    
    /**
     * This method should not be called, it will throw an 
     * UnsupportedOperationException. Use setAnimation to attach an
     * animation to this group.
     */
    public void setTransform(Transform3D t3d) {
        throw new UnsupportedOperationException("This method should not be called.");
    }
    
    /**
     * Set the transformation of this AnimationGroup
     * @param t3d the transformation
     */
    public void setTransformInternal(Transform3D t3d) {
        super.setTransform(t3d);
    }

    /**
     * add a child node to the AnimationGroup
     * @param child the child node to add
     */
    public void addChild(Node child) {
        // TODO This will change when we move to Java3D 1.4
        if (transparencyManager!=null)
            transparencyManager.addGraph(child);
        super.addChild(child);
    }

    /**
     * get the transparency manager for the AnimationGroup
     * @return the transparency manager
     */    
    TransparencyManager getTransparencyManager() {
        return transparencyManager;
    }
    
    /**
     * Called when this node has been removed from the scene.
     */
    void removeTransparencyManager() {
        if (transparencyManager!=null) {
            transparencyManager.clear();
            transparencyManager = null;
        }
    }

    /**
     * create the transparency manager. This must be called 
     * before the animation is started.
     */    
    void createTransparencyManager() {
        if (transparencyManager==null) {
            if (isLive()) {
                logger.severe("Unable to createTransparencyManager on live AnimationGroup");
                return;
            }
            
            transparencyManager = new TransparencyManager();
            Enumeration e = getAllChildren();
            while(e != null && e.hasMoreElements()) {
                transparencyManager.addGraph((Node)e.nextElement());
            }
        }
    }

    /**
     * Define the operations that the AnimationGroup can perform
     */
    class AnimationGroupTarget implements AnimationTarget {
	/** the transform of the AnimationGroup */
        private Transform3D t3d = new Transform3D();
	/** the transparency of the AnimationGroup */
        private float transparency;
	/** the position of the AnimationGroup */
        private Vector3f position = new Vector3f();
	/** the rotation of the AnimationGroup */
        private AxisAngle4f rotation = new AxisAngle4f();
	/** the scale of the AnimationGroup */
        private float scale;

	/** Create a new instance of an AnimationGroupTarget */
        public AnimationGroupTarget() {
        }
        
	/**
	 * get the transparency of the AnimationGroup
	 * @return the transparency
	 */	
        public float getTransparency() {
            return transparency;
        }
        
	/**
	 * set the internal transformation of the AnimationGroup
	 * @param t3d the transformation
	 */	
        private void internalSetTransform(Transform3D t3d) {
            AnimationGroup.this.setTransformInternal(t3d);
        }
        
	/**
	 * get the translation of the AnimationGroup
	 * @param pos the vector to store the data in
	 * @return the vector translation (for chaining)
	 */	
        public javax.vecmath.Vector3f getTranslation(javax.vecmath.Vector3f pos) {
            if (pos == null) {
                throw new IllegalArgumentException("argument cannot be null");
            }
            pos.set(position);
            return pos;
        }

	/**
	 * get the rotation of the AnimationGroup
	 * @param rot the AxisAngle4f to store the rotaiton in
	 * @return the rotation (for chaining)
	 */	
        public javax.vecmath.AxisAngle4f getRotation(javax.vecmath.AxisAngle4f rot) {
            if (rot == null) {
                throw new IllegalArgumentException("argument cannot be null");
            }
            rot.set(rotation);
            return rot;
        }

	/**
	 * get the scale of the AnimationGroup
	 * @return the current scaling
	 */	
        public float getScale() {
            return scale;
        }

	/**
	 * get the axis independent scaling of the AnimationGroup
	 * @param scale the vector scaling to store the data in
	 * @return the vector scaling (for chaining)
	 */	
        public Vector3f getScale(Vector3f scale) {
            if (scale == null) {
                throw new IllegalArgumentException("argument cannot be null");
            }
            throw new RuntimeException("Not Implemented");
        }

	/**
	 * set the transparency of the AnimationGroup
	 * @param transparency the transparency
	 */	
        public void setTransparency(float transparency) {
            assert(!Float.isNaN(transparency));
            this.transparency = transparency;
            transparencyManager.modifyTransparency(transparency);
        }

	/**
	 * set the translation of the AnimationGroup
	 * @param pos the new translation
	 */	
        public void setTranslation(Vector3f pos) {
            if (pos == null) {
                throw new IllegalArgumentException("argument cannot be null");
            }
            assert(!Float.isNaN(pos.x));
            assert(!Float.isNaN(pos.y));
            assert(!Float.isNaN(pos.z));
            
            position.set(pos);  
            t3d.setTranslation(position);
            internalSetTransform(t3d);
        }

	/**
	 * set the rotation of the AnimationGroup
	 * @param the new rotation
	 */	
        public void setRotation(AxisAngle4f rot) {
            if (rot == null) {
                throw new IllegalArgumentException("argument cannot be null");
            }
            assert(!Float.isNaN(rot.x));
            assert(!Float.isNaN(rot.y));
            assert(!Float.isNaN(rot.z));
            assert(!Float.isNaN(rot.angle));
            
            rotation.set(rot);
            t3d.setRotation(rotation);
            internalSetTransform(t3d);
        }

	/**
	 * set the uniform scale of the AnimationGroup
	 * @param scale the uniform scale
	 */	
        public void setScale(float scale) {
            assert(!Float.isNaN(scale));
            this.scale = scale;
            t3d.setScale(scale);
            internalSetTransform(t3d);
        }

	/**
	 * set the axis independent scale of the AnimationGroup
	 * @param scale the new scale
	 */	
        public void setScale(Vector3f scale) {
            if (scale == null) {
                throw new IllegalArgumentException("argument cannot be null");
            }
            assert(!Float.isNaN(scale.x));
            assert(!Float.isNaN(scale.y));
            assert(!Float.isNaN(scale.z));
            throw new RuntimeException("Not Implemented");
        }

	/**
	 * Utility method to set all the animation targets at once
	 * (not implemented)
	 */	
        public void set(AxisAngle4f rot, Vector3f translation, Vector3f scale) {
            if (rot == null || translation == null || scale == null) {
                throw new IllegalArgumentException("argument cannot be null");
            }
            assert(!Float.isNaN(rot.x));
            assert(!Float.isNaN(rot.y));
            assert(!Float.isNaN(rot.z));
            assert(!Float.isNaN(rot.angle));
            assert(!Float.isNaN(translation.x));
            assert(!Float.isNaN(translation.y));
            assert(!Float.isNaN(translation.z));
            assert(!Float.isNaN(scale.x));
            assert(!Float.isNaN(scale.y));
            assert(!Float.isNaN(scale.z));
            
            throw new RuntimeException("Not Implemented");
        }

	/**
	 * Ulitity method to set all the animation targets
	 * @param rot the new rotation
	 * @param translation the new translation
	 * @param scale the new uniform scale
	 */	
        public void set(AxisAngle4f rot, Vector3f translation, float scale) {
            if (rot == null || translation == null) {
                throw new IllegalArgumentException("argument cannot be null");
            }
            assert(!Float.isNaN(rot.x));
            assert(!Float.isNaN(rot.y));
            assert(!Float.isNaN(rot.z));
            assert(!Float.isNaN(rot.angle));
            assert(!Float.isNaN(translation.x));
            assert(!Float.isNaN(translation.y));
            assert(!Float.isNaN(translation.z));
            assert(!Float.isNaN(scale));
            
            rotation.set(rot);
            position.set(translation);
            this.scale = scale;
            
            t3d.setRotation(rotation);
            t3d.setScale(scale);
            t3d.setTranslation(position);
            internalSetTransform(t3d);
        }
    }
}
