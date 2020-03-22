/**
 * Project Looking Glass
 *
 * $RCSfile: AnimationScheduler.java,v $
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
 * $Revision: 1.6 $
 * $Date: 2006-08-14 23:13:23 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg;

import java.util.LinkedList;
import java.util.logging.Logger;
import java.util.Enumeration;
import java.util.logging.Level;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.vecmath.Point3d;

/**
 * The AnimationScheduler tracks and maintains the animations running. It
 * is a singleton implementation, therefore it should be used via
 * <code>AnimationSchedular.getScheduler().add/removeAnimation(...);</code>.
 * This reduces the need to implement and register individual behaviors for
 * every animation, and centralises the control of the animations.
 * Every frame animations are added and removed from the schedular and any
 * registered animations are called.
 *
 * @author paulby
 */
public class AnimationScheduler extends Behavior {
    
    /** the animation logger */
    protected static final Logger logger = Logger.getLogger("lg.animation");
    /** the singleton instance of the scheduler */
    private static AnimationScheduler scheduler = new AnimationScheduler();
    
    /** the wakeup condition - after every frame */
    private WakeupCondition wakeup = new WakeupOnElapsedFrames(0);
    /** the linked list of animations currently in the list */
    private LinkedList<Animation> animations = new LinkedList<Animation>();
    /** the linked list of animations waiting to be added or removed */
    private LinkedList<AnimationAddRemove> animationAddRemove = new LinkedList<AnimationAddRemove>();
    
    /**
     * Use static getScheduler() to get the scheduler
     */
    private AnimationScheduler() {
    }
    
    /**
     * get the singleton instance of the scheduler
     * @param get AnimationSchedular singleton
     */
    public static AnimationScheduler getScheduler() {
        return scheduler;
    }
    
    /**
     * initialize the AnimationSchedular, specifying the sphere of influence
     * to include everything, and setting the wakeup condition
     */
    public void initialize() {
        logger.config("Animation System Started");
        setSchedulingBounds(new BoundingSphere(new Point3d(), Double.POSITIVE_INFINITY));
        wakeupOn(wakeup);
    }

    /**
     * Add/Remove any pending animations to the list, and then
     * call the doAnimation of each registered animation
     * @param criteria the stimulus criteria
     */
    public void processStimulus(Enumeration criteria) {
        synchronized(animationAddRemove) {
            for (AnimationAddRemove anim : animationAddRemove) {
                if (anim.add) {
                    animations.add(anim.animation);
                }
                else { 
                    animations.remove(anim.animation);
                }
            }
            animationAddRemove.clear();
        }
        
        for (Animation anim : animations) {
            try {
                anim.doAnimation();
            } catch( Exception e) {
                logger.log(Level.SEVERE, "Animation threw "+e, e);
            } catch( Error error) {
                logger.log(Level.SEVERE, "Animation threw "+error, error);                
            }
        }
        
        wakeupOn(wakeup);
    }
    
    /**
     * add an animation to the schedular. This is added after the next
     * frame has elapsed
     * @param animation the animation to add
     */
    void addAnimation(Animation animation) {
        synchronized(animationAddRemove) {
            animationAddRemove.add(new AnimationAddRemove(animation, true));
        }
    }
    
    /**
     * remove the animation from the schedular. This is removed after the
     * next frame has elapsed
     * @param animation the animation to remove
     */
    void removeAnimation(Animation animation) {
        synchronized(animationAddRemove) {
            animationAddRemove.add(new AnimationAddRemove(animation, false));
        }
    }
    
    /**
     * A wrapper class for the animations to be added an removed
     */
    private static class AnimationAddRemove {
	/** the animation */
        Animation animation;
	/** whether to add or remove the animation */
        boolean add;
        
	/**
	 * create a new wrapper for the add/remove animation task
	 * @param animation the animation to add or remove
	 * @param add true - add the animation, false - remove the animation
	 */
        public AnimationAddRemove( Animation animation, boolean add ) {
            this.animation = animation;
            this.add = add;
        }
    }
}
