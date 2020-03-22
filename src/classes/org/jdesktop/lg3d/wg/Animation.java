/**
 * Project Looking Glass
 *
 * $RCSfile: Animation.java,v $
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
 * $Date: 2006-08-14 23:47:39 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg;

import java.util.EnumSet;
import java.util.logging.Logger;
import org.jdesktop.lg3d.displayserver.NodeID;
import org.jdesktop.lg3d.displayserver.AppConnectorPrivate;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventSource;

/**
 * Superclass of all animations. Conceptually an animation is an action
 * which acts on an AnimationTarget. However this is not actually a subclass
 * of action.
 *
 * @author paulby
 */
public abstract class Animation implements LgEventSource {
    
    protected static final Logger logger = Logger.getLogger("lg.animation");
    
    /**
     * The target on which this animation acts
     */
    private AnimationTarget target = null;
    
    /**
     * The range to types available for an Animation. Each animation needs to
     * state what parameters it will be animating. An animation can animate
     * multiple parameters.
     */
    protected enum AnimationType { TRANSPARENCY, TRANSFORM };
    
    private boolean running = false;
    private Class animationFinishedEvent = null;
    
    /**
     * Subclasses must override this method to specific which things they
     * will animate.
     *
     * For example
     *
     *  return EnumSet.of(AnimationType.TRANSFORM);
     *
     *  return EnumSet.of(AnimationType.TRANSFORM, AnimationType.TRANSPARENCY);
     */
    protected abstract EnumSet<AnimationType> getAnimationParameters();
        
    /**
     * Set the target for this animation, this is implementation detail not to
     * be called by user.
     * @param target the target for the animation
     */
    public synchronized void setTarget(AnimationTarget target) {
        if (isRunning()) {
            throw new IllegalStateException("Cannot change the target while running");
        }
        if (target == null) {
            setRunning(false);
            destroy();
        }
        this.target = target;
        if (target != null) {
            initialize();
        }
    }
    
    /**
     * Set the class of the event to fire when the animation finishes
     * @param animationFinishedEvent the class of the animation finished event
     */
    public void setAnimationFinishedEvent(Class animationFinishedEvent) {
        if (animationFinishedEvent != null
                && !LgEvent.class.isAssignableFrom(animationFinishedEvent)) {
            throw new IllegalArgumentException(
                "the class of animationFinishedEvent have to be a subclass of LgEvent");
        }
        synchronized (this) {
            this.animationFinishedEvent = animationFinishedEvent;
        }
    }
    
    /**
     * A callback invoked right after the target gets set in order to initizalize
     * this animation and the target's visual before making the target visible.
     */
    public abstract void initialize();
    
    /**
     * A callback invoked right before the target gets reset to null. By the
     * time this is called the Animation is no longer running 
     * (isRunning()==false).
     */
    public abstract void destroy();
    
    /**
     * Return the animation target
     * @return the animation target
     */
    public AnimationTarget getTarget() {
        return target;
    }
    
    /**
     * Start/stop the animation. If there is no animation target set
     * and the argument is true then an IllegalStateException will be thrown
     * @param start true to start, false to stop
     */
    public synchronized void setRunning(boolean start) {
        if (target == null && start) {
            throw new IllegalStateException("Unable to start Animation with null target");
        }
        if (start == isRunning()) {
            return;
        }
        if (start) {
            AnimationScheduler.getScheduler().addAnimation(this);
        } else {
            AnimationScheduler.getScheduler().removeAnimation(this);
            if (animationFinishedEvent != null) {
                try {
                    LgEvent event = (LgEvent)animationFinishedEvent.newInstance();
                    postEvent(event);
                } catch (InstantiationException ie) {
                    logger.warning("Exception while creating an event object: " + ie);
                } catch (IllegalAccessException iae) {
                    logger.warning("Exception while creating an event object: " + iae);
                }
            }
        }
        running = start;
    }
    
    /**
     * Returns true if the animation is running, false otherwise
     */
    public boolean isRunning() {
        return running;
    }
    
    /**
     * Abstract method called by the animation scheduler in which this
     * animation will actually update the animation target.
     */
    protected abstract void doAnimation();
    
    /**
     * Post an event from this animation
     * @param evt the event to post
     */
    public void postEvent(LgEvent evt) {
        AppConnectorPrivate.getAppConnector().postEvent(evt, this);
    }
    
    /**
     * Internal API, not for public use
     *
     * @deprecated will be removed in future
     */
    public NodeID getNodeID() {
        return null;
    }

    /**
     * Internal API, not for public use
     *
     * @deprecated will be removed in future
     */
    public void setNodeID(NodeID nodeID) {
    }
}
