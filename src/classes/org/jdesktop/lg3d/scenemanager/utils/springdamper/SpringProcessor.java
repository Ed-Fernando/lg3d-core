/**
 * Project Looking Glass
 *
 * $RCSfile: SpringProcessor.java,v $
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
 * $Date: 2006-04-21 23:37:03 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.springdamper;

import java.util.ArrayList;
import java.util.Enumeration;
import javax.media.j3d.Behavior;
import javax.vecmath.Vector3f;

/**
 *
 * @author paulby
 */
class SpringProcessor {
    
    private ArrayList<Spring> springs = null;
    
    private Vector3f comp1Pos;
    private Vector3f comp2Pos;
    private Vector3f comp1Vel;
    private Vector3f comp2Vel;
    private Vector3f separation;
    private Vector3f velocityDiff;
    private Vector3f force;
    
    private boolean isRunning = false;
    
    private static final float STABLE = 1.0E-8f;
    
    private static SpringProcessor springProcessor = null;
    
    /** Creates a new instance of SpringProcessor */
    public SpringProcessor() {
        comp1Pos = new Vector3f();
        comp2Pos = new Vector3f();
        separation = new Vector3f();
        velocityDiff = new Vector3f();
        force = new Vector3f();
        springs = new ArrayList();
    }
    
    void calcForces() { 
        float checkStable = Float.NEGATIVE_INFINITY;
        Vector3f tmp1 = new Vector3f();
        Vector3f tmp2 = new Vector3f();
        float EPSILON = 1e-20f;
        synchronized(springs) {
            for(Spring s : springs) {
                if (s.comp1.isLive() && s.comp2.isLive()) {
                    separation.sub(s.comp1.getSpringTranslation(tmp1), s.comp2.getSpringTranslation(tmp2));
                    velocityDiff.sub(s.comp1.getVelocity(tmp1), s.comp2.getVelocity(tmp2));
                    float distance = separation.length();

                    if (distance==0f) {
                        force.x = -(s.k * (distance - s.restLength) + s.d) * EPSILON;
                        force.y = -(s.k * (distance - s.restLength) + s.d) * EPSILON;
                        force.z = -(s.k * (distance - s.restLength) + s.d) * EPSILON;
                    } else {
                        force.x = -(s.k * (distance - s.restLength) + s.d * ((velocityDiff.x*separation.x)/distance)) * (separation.x/distance);
                        force.y = -(s.k * (distance - s.restLength) + s.d * ((velocityDiff.y*separation.y)/distance)) * (separation.y/distance);
                        force.z = -(s.k * (distance - s.restLength) + s.d * ((velocityDiff.z*separation.z)/distance)) * (separation.z/distance);
                    }

                    if (!s.comp1.isLocked())
                        s.comp1.addForce(force);

                    if (!s.comp2.isLocked()) {
                        force.negate();
                        s.comp2.addForce(force);
                    }

                    float vdls = velocityDiff.lengthSquared();
                    if (vdls!=0f)
                        checkStable = Math.max(checkStable, vdls);
                    
//                    System.err.println("Spring "+(s.k * (distance - s.restLength)));
//                    System.err.println("Damper "+(s.d * ((velocityDiff.z*separation.z)/distance)) * (separation.z/distance));
//                    System.err.println("Sep "+separation+" velDiff "+velocityDiff+" Dist "+distance+"  force "+force);
//                    System.err.println("VelDiffLength "+velocityDiff.lengthSquared());
//                    System.err.println("CheckStable "+checkStable + " "+(checkStable<STABLE));
//                    System.err.println();

                }
            }
            if (checkStable>=0f && checkStable<STABLE) {
                isRunning = false;
                SpringProcessorBehavior.getSpringProcessorBehavior().postId(SpringProcessorBehavior.STOP);
            }
        }
    }
    
    void applyForces(float timeStep, boolean render) {
        for(Spring s : springs) {
            s.comp1.step(timeStep, render);
            s.comp2.step(timeStep, render);
            
        }
    }
    
    void addSpring(Spring spring) {
        // Sycnronized with calcForces so we don't erroneously conclude that
        // we are stable when a new spring is added mid cycle
        synchronized(springs) {
            springs.add(spring);

            if (!isRunning) {
                isRunning = true;
                SpringProcessorBehavior.getSpringProcessorBehavior().postId(SpringProcessorBehavior.START);
            }
        }
    }
    
    public static SpringProcessor getSpringProcessor() {
        if (springProcessor==null)
            springProcessor = new SpringProcessor();
        
        return springProcessor;
    }
    

}
