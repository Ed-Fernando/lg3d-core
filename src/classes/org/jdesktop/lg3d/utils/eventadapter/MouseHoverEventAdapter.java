/**
 * Project Looking Glass
 *
 * $RCSfile: MouseHoverEventAdapter.java,v $
 *
 * Copyright (c) 2006, Sun Microsystems, Inc., All Rights Reserved
 *
 * Redistributions in source code form must reproduce the above
 * copyright and this condition.
 *
 * The contents of this file are subject to the GNU General Public
 * License, Version 2 (the "License"); you may not use this file
 * except in compliance with the License. A copy of the License is
 * available at http://www.opensource.org/licenses/gpl-license.php.
 *
 * $Revision: 1.8 $
 * $Date: 2006-05-05 23:03:44 $
 * $State: Exp $
 */

/*
 * Originally created by the 3D File Manager team member at CMU in 2005
 * lg3d-incubator/src/classes/edu/cmu/sun/controller/HoverEventAdapter.java
 */
/*
* 3D File Manager - Project Looking Glass 
* Copyright Sun Microsystems, 2005
* 
* Project Course in Human-Computer Interaction
* Carnegie Mellon University
* 
* Aditya Chand, Braden Kowitz, Jake Pierson, Jessica Smith
*/
package org.jdesktop.lg3d.utils.eventadapter;

import java.util.Timer;
import java.util.TimerTask;

import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.utils.action.ActionBoolean;
import org.jdesktop.lg3d.wg.event.MouseEnteredEvent3D;
import org.jdesktop.lg3d.wg.event.MouseMotionEvent3D;

/**
 * @author Braden Kowitz, Jessica Smith, Hideya Kawahara
 */
public class MouseHoverEventAdapter implements EventAdapter {

    public static final int DEFAULT_ENTER_DELAY_TIME = 750; // based on Swing's default
    public static final int DEFAULT_EXIT_DELAY_TIME = 0;
    public static final int DEFAULT_LINGER_TIME = 500;
    
    private static Timer timer = new Timer("Mouse hover monitor");
    private static TimerTask warmPeriodTimerTask;
    private static boolean withinWarmPeriod = false;
    
    private TimerTask enterDelayTimerTask;
    private TimerTask exitDelayTimerTask;
    private ActionBoolean action;
    private int enterDelay;
    private int exitDelay;
    private int warmPeriod;
    
    /**
     * Create a MouseHoverEventAdapter.
     *
     * @param action       the action to propagate the event information to.
     * @throws IllegalArgumentException 
     *         if the action is null.
     */
    public MouseHoverEventAdapter(ActionBoolean action) {
        this(DEFAULT_ENTER_DELAY_TIME, action);
    }
    
    /**
      * Create a MouseHoverEventAdapter.
      *
      * @param enterDelay   the delay in millisec until the hover enter event gets fired.
      * @param action       the action to propagate the event information to. 
      * @throws IllegalArgumentException 
      *         if the action is null.
      */
    public MouseHoverEventAdapter(int enterDelay, ActionBoolean action) {
        this(enterDelay, DEFAULT_EXIT_DELAY_TIME, DEFAULT_LINGER_TIME, action);
    }
    
     /**
      * Create a MouseHoverEventAdapter.
      *
      * @param enterDelay   the delay in millisec until the hover enter event gets fired.
      * @param exitDelay    the delay in millisec until the hover exit event gets fired.
      * @param warmPeriod   the period in millisec where the hover event gets fired
      *                     immediately after exiting the previous hover.
      * @param action       the action to propagate the event information to.
      * @throws IllegalArgumentException 
      *         if the action is null.
      */
    public MouseHoverEventAdapter(int enterDelay, int exitDelay, int warmPeriod,
            ActionBoolean action) 
    {
        EventAdapterUtil.throughIAEIfNull("action", action);
        this.action = action;
        this.enterDelay = enterDelay;
        this.exitDelay = exitDelay;
        this.warmPeriod = warmPeriod;
    }
    
    /**
     * Process an incoming event. Invoked when a mouse hover event happends.
     * 
     * @param event  an event of a registered type and from a registerd source.
     */
    public void processEvent(LgEvent e) {
        if (e instanceof MouseEnteredEvent3D) {
            MouseEnteredEvent3D me = (MouseEnteredEvent3D)e;
            LgEventSource source = me.getSource();
            if (me.isEntered()) {
                startTimer(source);
            } else {
                stopTimer(source);
            }
        } else {
            assert(e instanceof MouseMotionEvent3D);
            MouseMotionEvent3D me = (MouseMotionEvent3D)e;
            LgEventSource source = me.getSource();
            restartTimer(source);
        }
    }
        
    private synchronized void startTimer(final LgEventSource source) {
        if (exitDelayTimerTask != null) {
            // the hover exit event has not been fired
            // skip firing the exit/enter pair
            exitDelayTimerTask.cancel(); 
            exitDelayTimerTask = null;
            return;
        }
        enterDelayTimerTask = new TimerTask() {
            public void run() {
                synchronized (MouseHoverEventAdapter.this) {
                    if (source != null) {
                        enterDelayTimerTask = null;
                        action.performAction(source, true);
                    }
                }
            }
        };
        if (withinWarmPeriod || enterDelay <= 0) {
            enterDelayTimerTask.run();
        } else {
            timer.schedule(enterDelayTimerTask, enterDelay);
        }
    }

    private synchronized void stopTimer(final LgEventSource source) {
        if (enterDelayTimerTask != null) { 
            enterDelayTimerTask.cancel();
            enterDelayTimerTask = null;
            return;
        }
        // if the timer has already been fired
        if (warmPeriodTimerTask != null) {
            warmPeriodTimerTask.cancel();
            warmPeriodTimerTask = null;
        }
        withinWarmPeriod = true;
        
        exitDelayTimerTask = new TimerTask() {
            public void run() {
                synchronized (MouseHoverEventAdapter.this) {
                    exitDelayTimerTask = null;
                    action.performAction(source, false);
                    handleWarmPeriod();
                }
            }
        };
        if (exitDelay <= 0) {
            exitDelayTimerTask.run();
        } else {
            timer.schedule(exitDelayTimerTask, exitDelay);
        }
        
                if (warmPeriodTimerTask != null) {
            warmPeriodTimerTask.cancel();
            warmPeriodTimerTask = null;
        }
        withinWarmPeriod = true;
    }
    
    private void handleWarmPeriod() {
        warmPeriodTimerTask = new TimerTask() {
            public void run() {
                synchronized (MouseHoverEventAdapter.this) {
                    warmPeriodTimerTask = null;
                    withinWarmPeriod = false;
                }
            }
        };
        if (warmPeriod <= 0) {
            warmPeriodTimerTask.run();
        } else {
            timer.schedule(warmPeriodTimerTask, warmPeriod);
        }
    }
    
    private synchronized void restartTimer(LgEventSource source) {
        if (enterDelayTimerTask != null) { 
            enterDelayTimerTask.cancel();
            // The following results in creating one TimerTask object per
            // MouseMoveEvent3D, which is not ideal, but shouldn't be
            // too bad.
            startTimer(source);
        }
    }
    
    /**
     * Called by Component3D when attaching this listener to the component
     * in order to obtain the list of LgEvent classes which this listens to.
     * 
     * @return  the list of LgEvent classes which this listener listens to.
     */
    @SuppressWarnings("unchecked")
    public Class<LgEvent>[] getTargetEventClasses() {
        Class[] types = new Class[] {
            MouseEnteredEvent3D.class, MouseMotionEvent3D.class};
        return types;
    }
}
