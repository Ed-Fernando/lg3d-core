/**
 * Project Looking Glass
 *
 * $RCSfile: TimedAnimation.java,v $
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
 * $Date: 2006-04-25 23:05:53 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.animation;

import java.util.EnumSet;
import java.util.logging.Level;
import org.jdesktop.lg3d.displayserver.Lg3dSystem;
import org.jdesktop.lg3d.utils.eventadapter.EventAdapter;
import org.jdesktop.lg3d.utils.smoother.FloatTransitionSmoother;
import org.jdesktop.lg3d.wg.Animation;
import org.jdesktop.lg3d.wg.event.LgEvent;


/**
 */
public abstract class TimedAnimation extends Animation {
    
    public final static int LOOP_FOREVER = -1;
    
    /** 
     * REPEAT keep repeating the animation once triggered
     *        elapsed duration will change thus 0->1, 0->1....
     * OSCILLATE repeat the animation once triggered
     *        elapsed duration will change thus 0->1->0->1....
     */
    public enum LoopType { ONCE, REPEAT, OSCILLATE };
    
    private int duration;
    private int loopCount; // -1 run forever
    private LoopType loopType;
    
    private long startTime;
    /**
     * The time at which this event should end
     * -1 run forever (RUN_FOREVER)
     * 0 stopped
     */
    private long endTime = 0;
    private int currentLoopCount;
    
    /**
     * A single shot timed animation. It will run once for the specificed duration
     */
    public TimedAnimation(int duration) {
        this(duration, 1, LoopType.ONCE);
    }
    
    /**
     * @param duration the duration of the animation in ms
     * @param loopCount number of times the loop should repeat, use
     *         LOOP_FOREVER to loop continuously
     * @param loopType specifies if and how the animation should loop
     */
    public TimedAnimation(int duration, int loopCount, LoopType loopType) {
        if (duration < 0) {
            throw new IllegalArgumentException("duration cannot be negative");
        }
        if (loopCount < 1 && loopCount != LOOP_FOREVER) {
            throw new IllegalArgumentException("loopCount cannot be a negative value (except LOOP_FOREVER)");
        }
        if (loopType == null) {
            throw new IllegalArgumentException("loopType cannot be null");
        }
        this.duration = duration;
        this.loopCount = loopCount;
        this.loopType = loopType;
        // eventType keeps the initial value, null
    }
    
    public void setRunning(boolean running) {
        if (running) {
            currentLoopCount = 1; // the count is 1 origin
            startTime = Lg3dSystem.currentTimeMillis();
            endTime = startTime + duration;
        }
        super.setRunning(running);
    }
    
    /**
     * Return the current loop count
     */
    public int getCurrentLoopCount() {
        return currentLoopCount;
    }
    
    /**
     * Called by the animation scheduler
     */
    protected final void doAnimation() {
        if (endTime == 0) { // if already done
            return;
        }
        long currentTime = Lg3dSystem.currentTimeMillis();
        if (getTarget() != null) {
            float elapsedDuration;
            if (currentTime >= endTime) {
                elapsedDuration = 1f;
            } else {
                assert(duration > 0);
                elapsedDuration = (currentTime - startTime) / (float)duration;
            }
            doTimedAnimation(elapsedDuration);
        }
        if (currentTime >= endTime) {
            if (loopCount!=-1 && currentLoopCount >= loopCount) {
                setRunning(false);
                endTime = 0;
            } else {
                currentLoopCount++;
                startTime = Lg3dSystem.currentTimeMillis();
                endTime = startTime + duration;
                if (loopType == LoopType.REPEAT) {
                    endTime = currentTime + duration;
                } else if (loopType == LoopType.OSCILLATE) {
                    throw new RuntimeException("TODO LoopType.OSCILLATE not implemented at the moment");
                }
            }
        }
    }
    
    /**
     * Called each frame so the animation can actually operate on
     * it's target
     *
     * @param elapsedDuration 0-1.0 amount of duration elapsed
     */
    public abstract void doTimedAnimation(float elapsedDuration);
    
    protected java.util.EnumSet<org.jdesktop.lg3d.wg.Animation.AnimationType> getAnimationParameters() {
        return EnumSet.of(AnimationType.TRANSFORM);
    }

}
