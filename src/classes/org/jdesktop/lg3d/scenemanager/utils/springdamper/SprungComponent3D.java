/**
 * Project Looking Glass
 *
 * $RCSfile: SprungComponent3D.java,v $
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
 * $Revision: 1.3 $
 * $Date: 2005-12-23 21:44:48 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.springdamper;

import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.wg.Component3D;

/**
 *
 * @author paulby
 */

public class SprungComponent3D extends Component3D implements SprungComponentInterface {
    
    private Vector3f velocity;
    private Vector3f acceleration;
    private Vector3f forces;
    private boolean locked;
    private float mass;
    private float invMass;
    private Vector3f translation;
    
    private final static int PROCESSOR_CHANGE = -500;
    
    /** Creates a new SprungComponent3D with a mass of 1kg */
    public SprungComponent3D() {
        mass = 1f;
        invMass = 1f/mass;
        locked = false;
        
        velocity = new Vector3f();
        acceleration = new Vector3f();
        forces = new Vector3f();
        translation = new Vector3f();
    }
    
    public boolean isLocked() {
        return locked;
    }
    
    public void setLocked( boolean locked ) {
        this.locked = locked;
    }
    
    public void addForce(Vector3f force) {
        forces.add(force);
    }
    
    public void changeTranslation(float x, float y, float z, int duration) {
        // Unless duration is PROCESSOR_CHANGE this is a user invoked call
        // and we need to restart the spring behavior
        if (duration==PROCESSOR_CHANGE) {
            duration = 0;
        } else 
            SpringProcessorBehavior.getSpringProcessorBehavior().postId(SpringProcessorBehavior.START);
       
        super.changeTranslation(x,y,z,duration);
        translation.set(x,y,z);
    }
    
    /**
     * Step through the simulation, only change the rendered position of
     * the object is render is true in order to reduce overhead
     */
    public void step(float timeDelta, boolean render) {
        if (locked)
            return;
        acceleration.scale(invMass, forces);
        velocity.scale(timeDelta, acceleration);
        translation.scaleAdd(timeDelta, velocity, translation);
        
        if (render)
            changeTranslation(translation.x, translation.y, translation.z, PROCESSOR_CHANGE);
    }

    public Vector3f getSpringTranslation(Vector3f translation) {
        translation.set(this.translation);
        
        return translation;
    }

    public Vector3f getVelocity(Vector3f velocity) {
        velocity.set(this.velocity);
        
        return velocity;
    }
    
}
