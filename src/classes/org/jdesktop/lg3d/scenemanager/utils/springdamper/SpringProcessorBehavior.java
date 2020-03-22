/*
 * SpringProcessorBehavior.java
 *
 * Created on September 28, 2005, 9:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jdesktop.lg3d.scenemanager.utils.springdamper;

import java.util.Enumeration;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnBehaviorPost;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.media.j3d.WakeupOr;
import javax.vecmath.Point3d;

/**
 *
 * @author paulby
 */
public class SpringProcessorBehavior extends Behavior {
    
    private WakeupCondition currentCondition;
    private WakeupCondition waitingCondition;
    private WakeupCondition activeCondition;
    private static SpringProcessorBehavior behavior = null;
    
    private SpringProcessor processor = null;
    
    public static final int START = 1;
    public static final int STOP = 2;
    public static final int STEP = 3;
    
    private boolean singleStep = false;
    
    SpringProcessorBehavior() {
        waitingCondition = new WakeupOnBehaviorPost(this, 0);
        activeCondition = new WakeupOr(new WakeupCriterion[] {
            (WakeupCriterion)waitingCondition,
            new WakeupOnElapsedFrames(0)
        });
        
        currentCondition = waitingCondition;
        setSchedulingBounds(new BoundingSphere(new Point3d(),Double.POSITIVE_INFINITY));
    }
    
    public static SpringProcessorBehavior getSpringProcessorBehavior() {
        if (behavior==null) {        
            behavior = new SpringProcessorBehavior();
        }
        
        return behavior;
    }
    
    /**
     * Don't run the simulation continually, rather rely on user calling postId(STEP)
     */
    public void singleStep(boolean singleStep) {
        this.singleStep = singleStep;
    }
    
    public void initialize() {
        wakeupOn(currentCondition);
    }
    
    private void handleWakeup( WakeupOnBehaviorPost wakeup ) {
        if (wakeup.getTriggeringPostId()==START) {
            if (!singleStep)
                currentCondition = activeCondition;
            if (processor==null)
                processor = SpringProcessor.getSpringProcessor();
        } else if (wakeup.getTriggeringPostId()==STOP)
            currentCondition = waitingCondition;
        else if (wakeup.getTriggeringPostId()==STEP)
            // Debug code
            handleWakeup(new WakeupOnElapsedFrames(0));
    }
    
    private void handleWakeup( WakeupOnElapsedFrames wakeup) {
        final int LOOP_COUNT = 5;
        final float minimumCycleTime = 1f/30f;
        final float timeStep = minimumCycleTime/(float)LOOP_COUNT;
        
        for(int i=0; i<LOOP_COUNT; i++) {
            processor.calcForces();
            
            // Apply the forces but only render at the end of the loop
            processor.applyForces(timeStep, (i==LOOP_COUNT-1));
        }
    }

    public void processStimulus(Enumeration e) {
        WakeupCondition wakeup;
        
        while(e.hasMoreElements()) {
            wakeup = (WakeupCondition)e.nextElement();
            
            if (wakeup instanceof WakeupOnBehaviorPost) {
                handleWakeup((WakeupOnBehaviorPost)wakeup);
            } else if (wakeup instanceof WakeupOnElapsedFrames) {
                handleWakeup((WakeupOnElapsedFrames)wakeup);
            }
        }
        
        wakeupOn(currentCondition);
    }
}