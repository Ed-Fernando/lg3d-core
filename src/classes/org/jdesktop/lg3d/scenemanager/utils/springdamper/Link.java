/*
 * Link.java
 *
 * Created on 25 October 2005, 12:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jdesktop.lg3d.scenemanager.utils.springdamper;

import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.scenemanager.utils.springdamper.rk4.State;

/**
 *
 * Currently Unused, part of the unfinished rk4 resolver.
 *
 * @author paulby
 */
abstract class Link {
    
    protected State stateA;
    protected State stateB;
    
    protected Vector3f forceA;
    protected Vector3f forceB;
    
    private float lastTime = Float.NEGATIVE_INFINITY;
    
    /** Creates a new instance of Link */
    public Link(State stateA, State stateB) {
    }
    
    public Vector3f getForce(State state, float time) {
        if (time!=lastTime) {
            calcForces(time);
            lastTime = time;
        }
        
        if (state==stateA) {
            return forceA;
        } else if (state==stateB) {
            return forceB;
        } else
            throw new RuntimeException("Invalid State");
    }
    
    protected abstract void calcForces(float time);
}
