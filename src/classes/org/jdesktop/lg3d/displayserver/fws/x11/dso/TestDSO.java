/**
 * Project Looking Glass
 *
 * $RCSfile: TestDSO.java,v $
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
 * $Date: 2004-06-23 18:50:07 $
 * $State: Exp $
 */
import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.awt.GraphicsConfiguration;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;

public class TestDSO extends Applet {

    private SimpleUniverse u = null;
    private static Canvas3D canvas = null;
    
    public BranchGroup createSceneGraph() {
	// Create the root of the branch graph
	BranchGroup objRoot = new BranchGroup();

	// Create the TransformGroup node and initialize it to the
	// identity. Enable the TRANSFORM_WRITE capability so that
	// our behavior code can modify it at run time. Add it to
	// the root of the subgraph.
	TransformGroup objTrans = new TransformGroup();
	objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	objRoot.addChild(objTrans);

	// Create a simple Shape3D node; add it to the scene graph.
	objTrans.addChild(new ColorCube(0.4));

	// Create a new Behavior object that will perform the
	// desired operation on the specified transform and add
	// it into the scene graph.
	Transform3D yAxis = new Transform3D();
	Alpha rotationAlpha = new Alpha(-1, 4000);

	RotationInterpolator rotator =
	    new RotationInterpolator(rotationAlpha, objTrans, yAxis,
				     0.0f, (float) Math.PI*2.0f);
	BoundingSphere bounds =
	    new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
	rotator.setSchedulingBounds(bounds);
	objRoot.addChild(rotator);

        // Have Java 3D perform optimizations on this scene graph.
        objRoot.compile();

	return objRoot;
    }

    public TestDSO() {
    }

    public void init() {
	setLayout(new BorderLayout());
        GraphicsConfiguration config =
           SimpleUniverse.getPreferredConfiguration();

	canvas = new Canvas3D(config);
	add("Center", canvas);

	// Create a simple scene and attach it to the virtual universe
	BranchGroup scene = createSceneGraph();
	u = new SimpleUniverse(canvas);

        // This will move the ViewPlatform back a bit so the
        // objects in the scene can be viewed.
        u.getViewingPlatform().setNominalViewingTransform();

	u.addBranchGraph(scene);
    }

    public void destroy() {
	u.cleanup();
    }

    //
    // The following allows TestDSO to be run as an application
    // as well as an applet
    //
    public static void main(String[] args) {
	new MainFrame(new TestDSO(), 256, 256);

	System.loadLibrary("dso");
	org.jdesktop.lg3d.displayserver.fws.x11.dso.DrawingSurfaceObjectAWT dsawt =
		new org.jdesktop.lg3d.displayserver.fws.x11.dso.DrawingSurfaceObjectAWT();
	long jawtObj = dsawt.getAWT();

	// Wait until we are sure the canvas is mapped
	// NOTE: really should  trigger off repaint()
	try {
	    Thread.sleep(5000);
	}
	catch (InterruptedException e) {
	}

	long dsObj = dsawt.getDrawingSurfaceAWT(canvas, jawtObj);
	long dsiObj = dsawt.getDrawingSurfaceInfo(dsObj);
	int wid = dsawt.getDrawingSurfaceWindowIdAWT(canvas, dsObj, dsiObj,
						     0, 0, false);
	System.out.println("wid = " + wid);
    }
}
