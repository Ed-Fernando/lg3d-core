/**
 * Project Looking Glass
 *
 * $RCSfile: rk4.java,v $
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
 * $Revision: 1.2 $
 * $Date: 2005-12-23 21:49:30 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.springdamper;

import javax.vecmath.Vector3f;

/**
 * Runge-Kutta order 4 integrator NOT COMPLETE
 *
 * @author paulby
 */
class rk4 {
    
    /** Creates a new instance of rk4 */
    public rk4() {
        float TIME_DELTA = 1f;
        
        State state = new State();
        for(float t = 0f; t<=10f; t+=TIME_DELTA) {
            integrate( state, t, TIME_DELTA);
            
            System.out.println(t+" "+state.position+" "+state.velocity);
        }
        
    }
    
    private void integrate( State state, float time, float deltaTime ) {
        Derivative a = evaluate(state, time, 0f, null);
        Derivative b = evaluate(state, time, deltaTime*0.5f, a);
        Derivative c = evaluate(state, time, deltaTime*0.5f, b);
        Derivative d = evaluate(state, time, deltaTime, c);
        
        float dPxdt = 1f/6f * (a.dP.x + 2f*(b.dP.x+c.dP.x)+d.dP.x);
        float dPydt = 1f/6f * (a.dP.y + 2f*(b.dP.y+c.dP.y)+d.dP.y);
        float dPzdt = 1f/6f * (a.dP.z + 2f*(b.dP.z+c.dP.z)+d.dP.z);
        
        float dMxdt = 1f/6f * (a.dM.x + 2f*(b.dM.x+c.dM.x)+d.dM.x);
        float dMydt = 1f/6f * (a.dM.y + 2f*(b.dM.y+c.dM.y)+d.dM.y);
        float dMzdt = 1f/6f * (a.dM.z + 2f*(b.dM.z+c.dM.z)+d.dM.z);
        
        state.position.x += dPxdt * deltaTime;
        state.position.y += dPydt * deltaTime;
        state.position.z += dPzdt * deltaTime;
        
        state.momentum.x += dMxdt * deltaTime;
        state.momentum.y += dMydt * deltaTime;
        state.momentum.z += dMzdt * deltaTime;
        
        state.velocity.x = state.momentum.x * state.invMass;
        state.velocity.y = state.momentum.y * state.invMass;
        state.velocity.z = state.momentum.z * state.invMass;
    }
    
    private Derivative evaluate( final State initState, float time, float deltaTime, final Derivative d ) {
        Derivative ret = new Derivative();
        if (d==null) {
            ret.dP = initState.position;
            ret.dM = calcForce(initState, time);            
        } else {
            State state = new State();
            state.position.x = initState.position.x + d.dP.x * deltaTime;
            state.position.y = initState.position.y + d.dP.y * deltaTime;
            state.position.z = initState.position.z + d.dP.z * deltaTime;
            
            state.momentum.x = initState.momentum.x + d.dM.x * deltaTime;
            state.momentum.y = initState.momentum.y + d.dM.y * deltaTime;
            state.momentum.z = initState.momentum.z + d.dM.z * deltaTime;
            
            ret.dP = state.position;
            ret.dM = calcForce(state, time+deltaTime);
        }
        
        return ret;
    }
    
    private Vector3f calcForce( final State state, float time) {
        Vector3f ret = new Vector3f();
        
        ret.x = 2f;
        
        return ret;
    }
    
    public static void main(String[] args) {
        new rk4();
    }
    
    class State {
        Vector3f position;
        Vector3f momentum;
        Vector3f velocity;
        float mass;
        float invMass;
        
        public State() {
            position = new Vector3f();
            momentum = new Vector3f();
            velocity = new Vector3f();
            
            mass = 1f;
            invMass = 1f/mass;
        }
    }
    
    class Derivative {
        Vector3f dP;
        Vector3f dM;
        
        public Derivative() {
            dP = new Vector3f();
            dM = new Vector3f();
        }
    }
    
}
