/**
 * Project Looking Glass
 *
 * $RCSfile: ConcurrentTimedSubAnimationHelper.java,v $
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
 * $Date: 2006-06-09 23:42:09 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.animation;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.logging.Logger;
import org.jdesktop.lg3d.displayserver.Lg3dSystem;
import org.jdesktop.lg3d.wg.Animation;


/**
 *
 */
public class ConcurrentTimedSubAnimationHelper {
    protected static final Logger logger = Logger.getLogger("lg.animation");
    
    private Animation animation;
    private LinkedList<SubAnimation> list = new LinkedList<SubAnimation>();
    private boolean inAminationIterator = false;
    private LinkedList<SubAnimation> toBeAdded = new LinkedList<SubAnimation>();
    
    /**
     * Creates a new instance of ConcurrentTimedAnimationHelper
     */
    public ConcurrentTimedSubAnimationHelper(Animation animation) {
        if (animation == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
        this.animation = animation;
    }
    
    public synchronized void start(SubAnimation subAnim, int duration) {
        if (subAnim == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
        if (duration < 0) {
            throw new IllegalArgumentException("duration cannot be negative");
        }
        
        if (duration == 0) {
            // immediately perform the animation with the final 
            // duration ratio of 1.0f
            subAnim.doTimedAnimation(1.0f);
            subAnim.setDone();
        } else {
            long currentTime = Lg3dSystem.currentTimeMillis();
            subAnim.start(currentTime, duration);
            if (!list.contains(subAnim)) {
                if (!inAminationIterator) {
                    list.add(subAnim);
                } else {
                    toBeAdded.add(subAnim);
                }
            }
            animation.setRunning(true);
        }
    }
    
    public synchronized void doAnimation() {
        // update the list from the toBeAdded list
        for (SubAnimation subAnim : toBeAdded) {
            // sub animations with duration == 0 has already run -- don't
            // add it to the list
            if (subAnim.getDuration() != 0) {
                if (!list.contains(subAnim)) {
                    list.add(subAnim);
                }
            }
        }

        try {
            inAminationIterator = true;
            ListIterator<SubAnimation> iterator = list.listIterator();
            long currentTime = Lg3dSystem.currentTimeMillis();
            while (iterator.hasNext()) {
                SubAnimation subAnim = iterator.next();
                if (subAnim.doTimedAnimation(currentTime)) {
                    // finished
                    iterator.remove();
                }
            }
        } finally {
            inAminationIterator = false;
        }
        
        if (list.size() == 0 && animation.getTarget() != null) {
            animation.setRunning(false);
        }
    }
    
    public abstract static class SubAnimation {
        private long startTime;
        private int duration;
        private boolean done;
        
        public abstract void doTimedAnimation(float elapsedDuration);
        
        final private void start(long startTime, int duration) {
            this.startTime = startTime;
            this.duration = duration;
            this.done = false;
        }
        
        final private int getDuration() {
            return duration;
        }
        
        final private boolean doTimedAnimation(long currentTime) {
            if (done) {
                return true;
            }
            float elapsedDuration = 1.0f;
            if (duration > 0) {
                elapsedDuration = (float)(currentTime - startTime) / duration;
            }
            if (elapsedDuration >= 1.0f) {
                elapsedDuration = 1.0f;
            }
            doTimedAnimation(elapsedDuration);
            
            return (elapsedDuration == 1.0f);
        }
        
        final private void setDone() {
            done = true;
        }
        
        public int getReaminingDuration() {
            long currentTime = Lg3dSystem.currentTimeMillis();
            int ret = duration - (int)(currentTime - startTime);
            if (ret < 0) {
                ret = 0;
            }
            return ret;
        }
    }
}
