/**
 * Project Looking Glass
 *
 * $RCSfile: HelloUniverseApp.java,v $
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
 * $Revision: 1.5 $
 * $Date: 2006-03-14 23:34:07 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.apps.test;

import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.sg.Alpha;
import org.jdesktop.lg3d.sg.Transform3D;
import org.jdesktop.lg3d.sg.TransformGroup;
import org.jdesktop.lg3d.utils.action.RotateActionBoolean;
import org.jdesktop.lg3d.utils.action.ScaleActionBoolean;
import org.jdesktop.lg3d.utils.eventadapter.MouseEnteredEventAdapter;
import org.jdesktop.lg3d.utils.eventadapter.MousePressedEventAdapter;
import org.jdesktop.lg3d.utils.shape.ColorCube;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Container3D;
import org.jdesktop.lg3d.wg.Cursor3D;
import org.jdesktop.lg3d.wg.Frame3D;


public class HelloUniverseApp {
    
    private int instance = -2;
    
    @SuppressWarnings("deprecation") // ignore warnings against RotationInterpolator
    public Component3D createSceneGraph() {
	// Create the root of the branch graph
        final Component3D comp = new Component3D();
        comp.setName("Inner component");

        final Container3D container = new Container3D();
        container.setName("Outer container");

	// Create the TransformGroup node and initialize it to the
	// identity. Enable the TRANSFORM_WRITE capability so that
	// our behavior code can modify it at run time. Add it to
	// the root of the subgraph.
	TransformGroup objTrans = new TransformGroup();
	objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        TransformGroup objPos = new TransformGroup();
        objPos.addChild(objTrans);
	comp.addChild(objPos);
        Vector3f pos = getPosition();
        comp.changeTranslation(pos);
        
	// Create a simple Shape3D node; add it to the scene graph.
	objTrans.addChild(new ColorCube(0.01f));
        
	// Create a new Behavior object that will perform the
	// desired operation on the specified transform and add
	// it into the scene graph.
	Transform3D yAxis = new Transform3D();
	Alpha rotationAlpha = new Alpha(-1, 4000);
	org.jdesktop.lg3d.sg.RotationInterpolator rotator =
	    new org.jdesktop.lg3d.sg.RotationInterpolator(rotationAlpha, objTrans, yAxis,
				     0.0f, (float) Math.PI*2.0f);

	comp.addChild(rotator);
        container.addChild( comp );
        
        comp.setRotationAxis(1.0f, 0.0f, 1.0f);       
        comp.addListener(
            new MousePressedEventAdapter(
                new RotateActionBoolean(comp, (float)Math.toRadians(45), 200)));

        comp.addListener(
            new MouseEnteredEventAdapter(
                new ScaleActionBoolean(comp, 1.5f, 200)));
            
        addComponents( container );
        
	return container;
    }
    
    /**
     * Give subclasses the abilitity to add more components to the root
     * container
     */
    protected void addComponents( Container3D container ) {
    }
    
    private Vector3f getPosition() {
        
        Vector3f pos = new Vector3f();
        if (instance<4)
            pos.set( 0.02f*instance, 0f, 0f);
        
        System.out.println("Position "+pos);
        
        return pos;
    }

    public HelloUniverseApp() {
        this(new String[0]);
    }
    
    public HelloUniverseApp(String args[]) {
        
        if (args.length>0) {
            try {
                instance = Integer.parseInt(args[0]);
            } catch(Exception e ) {
                e.printStackTrace();
            }
        }
        
        init();
    }

    public void init() {
	Frame3D app = new Frame3D();

        Component3D scene = createSceneGraph();
        
        Cursor3D testCursor = Cursor3D.get("TEST_CURSOR");
        
	app.addChild(scene);
        app.setPreferredSize(new Vector3f(0.02f, 0.02f, 0.02f));
	app.setCursor(testCursor);
        app.changeEnabled(true);
        app.changeVisible(true);
    }
    
    public static void main(String[] args) {
	new HelloUniverseApp( args );
    }
}

