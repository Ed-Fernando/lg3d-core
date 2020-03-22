/**
 * Project Looking Glass
 *
 * $RCSfile: OdeEngine.java,v $
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
 * $Date: 2005-04-14 23:05:16 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.internal.j3d.j3dnodes;

import java.util.Enumeration;
import java.util.logging.Logger;

import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.wg.internal.j3d.j3dnodes.*;
import org.odejava.Geom;
import org.odejava.GeomPlane;
import org.odejava.Odejava;
import org.odejava.Space;
import org.odejava.World;
import org.odejava.collision.Contact;
import org.odejava.collision.JavaCollision;
import org.odejava.display.BoundDisplayObject;
import org.odejava.display.DisplayBin;
import org.odejava.ode.Ode;

/**
 *
 * @author  paulby
 */
public class OdeEngine extends BranchGroup {
    
    private static OdeEngine engine;
    private World world;
    private DisplayBin displayBin;
    private Space space;
    
    private Logger logger = Logger.getLogger( "lg.physics" );
    
    private JavaCollision collision;
    private Contact contact;
    private int groundId;
    
    /** Creates a new instance of OdeEngine */
    public OdeEngine() {
        if (engine!=null) {
            logger.severe("A physics engine already exists");
            throw new RuntimeException("Attempt to create multiple OdeEngine's");
        } 
    
        System.loadLibrary("odejava");
        
        logger.config("Odejava version " + org.odejava.ode.Ode.ODEJAVA_VERSION);
        world = new World();
        //world.setGravity( 0f, -9.81f, 0f );
        world.setGravity( 0f, -0.2f, 0f );
        // Set max interactions per step (bigger is more accurate, but slower)
        world.setStepInteractions(10);
        // Set step size (smaller is more accurate, but slower)
        world.setStepSize(0.05f);
        
        displayBin = new DisplayBin();
        space = new Space();

        createGround();
        
        collision = new JavaCollision();
        contact = new Contact(
                        collision.getContactIntBuffer(),
                        collision.getContactFloatBuffer());

        this.addChild( new PhysicsStepper() );
        
        logger.info("Added PhyscicsStepper");
        engine = this;
    }
    
    static OdeEngine getEngine() {
        if (engine==null)
            new OdeEngine();
        
        return engine;
    }
    
    World getWorld() {
        return world;
    }
    
    private void createGround() {
        GeomPlane groundGeom = new GeomPlane(0f, 1f, 0f, -0.1f);
        groundId = groundGeom.getNativeAddr();
        space.add(groundGeom);
    }
    
    /**
     * Add a BoundDisplayObject to the display bin
     */
    void add( BoundDisplayObject bdo ) {
        synchronized(displayBin) {
            logger.info("Adding BoundDisplayObject "+bdo);
            displayBin.add( bdo );
            space.addGeom( (Geom)bdo.getOdeTransformable() );
            }
    }
    
    /**
     * Step simulation ahead.
     *
     */
    public void step() {
        // Collide objects in given space
        collision.collide(space);
        
        // Read & modify contact information
        iterateContacts();
        
        // Add all contacts to contact jointGroup
        collision.applyContacts();
        world.quickStep();
        
//        Iterator<BoundDisplayObject> it = displayBin.iterator();
//        while(it.hasNext()) {
//            logger.fine( it.next().getOdeTransformable().getPosition().toString() );
//        }
        
        synchronized(displayBin) {
            displayBin.updateAll();
        }
    }
    
	/*
     * Iterate contacts, read and modify
     */
    private void iterateContacts() {
            float depth = 0;
            Vector3f pos = new Vector3f();
            Vector3f normal = new Vector3f();
            for (int i = 0; i < collision.getContactCount(); i++) {
                    contact.setIndex(i);

                    // Use default surface contact values for any geom that hits
                    // ground
                    if ((contact.getGeomID1() == groundId)
                            || (contact.getGeomID2() == groundId)) {
                            contact.setMode(Ode.dContactBounce | Ode.dContactApprox1);
                            contact.setBounce(0.24f);
                            contact.setBounceVel(0.2f);
                            contact.setMu(1f);
                    }

                    // Check if contact with chassis
//                    if ((contact.getGeomID1() != groundId)) {
//                            // Found interesting contact
//                            contact.getPosition(pos);
//                            contact.getNormal(normal);
//                            contact.setMode(Ode.dContactBounce | Ode.dContactApprox1);
//                            contact.setBounce(0.24f);
//                            contact.setBounceVel(0.2f);
//                            contact.setMu(0f);
//                            logger.fine(
//                                    "A: "
//                                            + i
//                                            + " C3D hits geom "
//                                            + contact.getGeomID2()
//                                            + "\n  d="
//                                            + contact.getDepth()
//                                            + "\n  pos="
//                                            + pos
//                                            + "\n  normal="
//                                            + normal);
//                    }

//                    // Check if contact with chassis
//                    if ((contact.getGeomID2() == boxId)) {
//                            // Found interesting contact
//                            contact.getPosition(pos);
//                            contact.getNormal(normal);
//                            // If chassis hits any geom make it bounce hard
//                            contact.setMode(Ode.dContactBounce | Ode.dContactApprox1);
//                            contact.setBounce(1.25f);
//                            contact.setBounceVel(0.2f);
//                            contact.setMu(0f);
//
//                            System.err.println(
//                                    "B: "
//                                            + i
//                                            + " Box hits geom "
//                                            + contact.getGeomID1()
//                                            + "\n  d="
//                                            + contact.getDepth()
//                                            + "\n  pos="
//                                            + pos
//                                            + "\n  normal="
//                                            + normal);
//                    }
            }
    }
        
    /**
     * Passive physics behavior
     */
    class PhysicsStepper extends Behavior {
        WakeupOnElapsedFrames wakeup;
        public void initialize() {
            setSchedulingBounds( new BoundingSphere( new Point3d(),
                                                     Double.POSITIVE_INFINITY ));
            wakeup = new WakeupOnElapsedFrames(0, true);
            wakeupOn(wakeup);
        }
        
        public void processStimulus( Enumeration e ) {
//            step();
//            wakeupOn(wakeup);
        }
        
        
    }
    
}
