/**
 * Project Looking Glass
 *
 * $RCSfile: EventTestApp.java,v $
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
 * $Date: 2005-04-14 23:03:56 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.apps.test;


import org.jdesktop.lg3d.sg.*;
import org.jdesktop.lg3d.wg.*;

import org.jdesktop.lg3d.wg.Frame3D;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventConnector;
import org.jdesktop.lg3d.wg.event.MouseMotionEvent3D;
import org.jdesktop.lg3d.utils.shape.ColorCube;


public class EventTestApp {

    static {
        //System.getProperties().setProperty("lgremoteclient", "true");
    }
    
    public Component3D createSceneGraph() {
	// Create the root of the branch graph
	Component3D objRoot = new Container3D();
        final Component3D lgBg = new Component3D();
        lgBg.setUserData("lgBg");
        final Container3D lgBg2 = new Container3D();
        lgBg2.setUserData("lgBg2");
        
	// Create the TransformGroup node and initialize it to the
	// identity. Enable the TRANSFORM_WRITE capability so that
	// our behavior code can modify it at run time. Add it to
	// the root of the subgraph.
	TransformGroup objTrans = new TransformGroup();
	objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	lgBg.addChild(objTrans);

	// Create a simple Shape3D node; add it to the scene graph.
	objTrans.addChild(new ColorCube(0.4));

	// Create a new Behavior object that will perform the
	// desired operation on the specified transform and add
	// it into the scene graph.
	Transform3D yAxis = new Transform3D();
//	Alpha rotationAlpha = new Alpha(-1, 4000);
//
//	RotationInterpolator rotator =
//	    new RotationInterpolator(rotationAlpha, objTrans, yAxis,
//				     0.0f, (float) Math.PI*2.0f);
//	BoundingSphere bounds =
//	    new BoundingSphere(new Point3f(0.0f,0.0f,0.0f), 100.0f);
//	rotator.setSchedulingBounds(bounds);
//	lgBg.addChild(rotator);
        
        lgBg2.addChild( lgBg );
        objRoot.addChild( lgBg2 );

	return objRoot;
    }

    public EventTestApp() {
        init();
    }

    public void init() {
        // Frame3D MUST be created before any scene graph objects
	Frame3D app = new Frame3D();
        
	Component3D scene = createSceneGraph();
        configureListeners( app );
        
        //testCursor( u );

	app.addChild(scene);
        app.changeEnabled( true );
        app.changeVisible(true);
        
        System.out.println("Posting event");
        app.postEvent( new TestEvent( 10 ));
    }
    
    private void configureListeners( Frame3D app ) {
        LgEventConnector.getLgEventConnector().addListener(
            LgEventSource.ALL_SOURCES,
            new LgEventListener() {
                public void processEvent( LgEvent evt ) {
                    assert( evt.getClass()==TestEvent.class );
                    System.out.println("Got event "+evt);
                }
                public Class<LgEvent>[] getTargetEventClasses() {
                    return new Class[] {TestEvent.class};
                }
            });
        
        LgEventConnector.getLgEventConnector().addListener(
            LgEventSource.ALL_SOURCES,
            new LgEventListener() {
                public void processEvent( LgEvent evt ) {
                    assert( evt.getClass()==TestEvent.class );
                    System.out.println("Got event "+evt);
                }
                public Class<LgEvent>[] getTargetEventClasses() {
                    return new Class[] {MouseMotionEvent3D.class};
                }
            });
    }
    
//    void testCursor( DisplayManager dm ) {
//        CursorModule cursorModule = dm.getCursorModule();
//        
//        BranchGroup cursor = new BranchGroup();
//        cursor.addChild( new ColorCube(0.02));
//        
//        cursorModule.addCursor( cursor );
//    }

    //
    // The following allows HelloUniverse to be run as an application
    // as well as an applet
    //
    public static void main(String[] args) {
	new EventTestApp();
    }
    
}

    class TestEvent extends LgEvent {
        int id;
        
        public TestEvent( int id ) {
            this.id = id;
        }
    }
