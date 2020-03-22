/**
 * Project Looking Glass
 *
 * $RCSfile: MoveCursor.java,v $
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
 * $Revision: 1.6 $
 * $Date: 2006-03-09 04:04:10 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.cursor;

import javax.vecmath.Vector3f;

import org.jdesktop.lg3d.sg.Appearance;
import org.jdesktop.lg3d.sg.GeometryArray;
import org.jdesktop.lg3d.sg.IndexedTriangleArray;
import org.jdesktop.lg3d.sg.Node;
import org.jdesktop.lg3d.sg.Shape3D;
import org.jdesktop.lg3d.sg.BranchGroup;
import org.jdesktop.lg3d.utils.action.Action;
import org.jdesktop.lg3d.utils.action.ActionBoolean;
import org.jdesktop.lg3d.utils.actionadapter.ActionMulticaster;
import org.jdesktop.lg3d.utils.c3danimation.NaturalMotionAnimation;
import org.jdesktop.lg3d.utils.animation.RotationAnimationBoolean;
import org.jdesktop.lg3d.utils.animation.ScaleAnimationBoolean;
import org.jdesktop.lg3d.utils.c3danimation.ScaleAndRotateChangeVisiblePlugin;
import org.jdesktop.lg3d.utils.eventadapter.MousePressedEventAdapter;
import org.jdesktop.lg3d.utils.shape.OriginTranslation;
import org.jdesktop.lg3d.utils.shape.RoundShadow;
import org.jdesktop.lg3d.utils.shape.SimpleAppearance;
import org.jdesktop.lg3d.utils.shape.Sphere;
import org.jdesktop.lg3d.wg.AnimationGroup;
import org.jdesktop.lg3d.wg.Cursor3D;
import org.jdesktop.lg3d.wg.event.LgEventConnector;
import org.jdesktop.lg3d.wg.event.LgEventSource;


public class MoveCursor extends Cursor3D {
    
    public MoveCursor(String name, float size) {
        super(name);
        BranchGroup bg = new BranchGroup();
        
        setAnimation(new NaturalMotionAnimation(250, new ScaleAndRotateChangeVisiblePlugin(250)));
        setRotationAxis(0.0f, 0.0f, 1.0f);
        
	float centerSize = size * 0.12f;
	float arrowHeight = size * 0.4f;
	float arrowWidth  = size * 0.25f;
	float shadowSize = size * 0.75f;
	float arrowAngleOff = (float)Math.toRadians(30);
	float arrowAngleOn = (float)Math.toRadians(30 + 45);
	float zShift = size * 0.5f;
        
	Appearance app = new SimpleAppearance(0.85f, 0.85f, 1.0f, 0.7f);
        
        Node center = new Sphere(centerSize, app);
        ScaleAnimationBoolean centerAnim 
            = new ScaleAnimationBoolean(1.0f, 1.25f, 100);
        AnimationGroup centerAg = new AnimationGroup(centerAnim);
        centerAg.addChild(center);
        bg.addChild(
            new OriginTranslation(centerAg, new Vector3f(0.0f, 0.0f, zShift)));
        
        Node up = new Triangle(arrowHeight, arrowWidth, Triangle.Dir.UP, app);
        RotationAnimationBoolean upAnim 
            = new RotationAnimationBoolean(
                new Vector3f(-1.0f, 0.0f, 0.0f), arrowAngleOff, arrowAngleOn, 100);
        AnimationGroup upAg = new AnimationGroup(upAnim);
        upAg.addChild(up);
        bg.addChild(
            new OriginTranslation(upAg, new Vector3f(0.0f, size * 0.2f, zShift)));
        
        Node down = new Triangle(arrowHeight, arrowWidth, Triangle.Dir.DOWN, app);
        RotationAnimationBoolean downAnim 
            = new RotationAnimationBoolean(
                new Vector3f(1.0f, 0.0f, 0.0f), arrowAngleOff, arrowAngleOn, 100);
        AnimationGroup downAg = new AnimationGroup(downAnim);
        downAg.addChild(down);
        bg.addChild(
            new OriginTranslation(downAg, new Vector3f(0.0f, size * -0.2f, zShift)));
        
        Node left = new Triangle(arrowHeight, arrowWidth, Triangle.Dir.LEFT, app);
        RotationAnimationBoolean leftAnim 
            = new RotationAnimationBoolean(
                new Vector3f(0.0f, -1.0f, 0.0f), arrowAngleOff, arrowAngleOn, 100);
        AnimationGroup leftAg = new AnimationGroup(leftAnim);
        leftAg.addChild(left);
        bg.addChild(
            new OriginTranslation(leftAg, new Vector3f(size * -0.2f, 0.0f, zShift)));
        
        Node right = new Triangle(arrowHeight, arrowWidth, Triangle.Dir.RIGHT, app);
        RotationAnimationBoolean rightAnim 
            = new RotationAnimationBoolean(
                new Vector3f(0.0f, 1.0f, 0.0f), arrowAngleOff, arrowAngleOn, 100);
        AnimationGroup rightAg = new AnimationGroup(rightAnim);
        rightAg.addChild(right);
        bg.addChild(
            new OriginTranslation(rightAg, new Vector3f(size * 0.2f, 0.0f, zShift)));
        
        Node shadow 
            = new RoundShadow(shadowSize, shadowSize, 12, 0.0f, 0.0f, 0.0f, 0.75f);
        ScaleAnimationBoolean shadowAnim 
            = new ScaleAnimationBoolean(1.0f, 0.8f, 200);
        AnimationGroup shadowAg = new AnimationGroup(shadowAnim);
        shadowAg.addChild(shadow);
        bg.addChild(shadowAg);
        
        LgEventConnector.getLgEventConnector().addListener(
            LgEventSource.ALL_SOURCES,
            new MousePressedEventAdapter((ActionBoolean)
                new ActionMulticaster(
                    new Action[] {
                        centerAnim,
                        upAnim,
                        downAnim,
                        leftAnim,
                        rightAnim,
                        shadowAnim})));
        addChild(bg);
    }
    
    private static class Triangle extends Shape3D {
        private enum Dir {UP, DOWN, RIGHT, LEFT};

	private Triangle(float height, float width, Dir dir, Appearance app) {
	    float[] coords = null;
	    float[] normals = null;
	    switch (dir) {
		case UP:
		    coords = new float[] {
			 0.0f,          height, 0.0f,
			-width * 0.5f,  0.0f,   0.0f,
			 width * 0.5f,  0.0f,   0.0f,
		    };
		    normals = new float[] {
			0.0f, -0.5f,   0.866f,
			0.0f,  0.866f, 0.5f,
		    };
		    break;
		case DOWN:
		    coords = new float[] {
			 0.0f,         -height, 0.0f,
			 width * 0.5f,  0.0f,   0.0f,
			-width * 0.5f,  0.0f,   0.0f,
		    };
		    normals = new float[] {
			0.0f,  0.5f,   0.866f,
			0.0f, -0.866f, 0.5f,
		    };
		    break;
		case RIGHT:
		    coords = new float[] {
			 height,          0.0f, 0.0f,
			 0.0f,    width * 0.5f, 0.0f,
			 0.0f,   -width * 0.5f, 0.0f,
		    };
		    normals = new float[] {
			-0.5f,   0.0f, 0.866f,
			 0.866f, 0.0f, 0.5f,
		    };
		    break;
		case LEFT:
		    coords = new float[] {
			-height,          0.0f, 0.0f,
			 0.0f,   -width * 0.5f, 0.0f,
			 0.0f,    width * 0.5f, 0.0f,
		    };
		    normals = new float[] {
			 0.5f,   0.0f, 0.866f,
			-0.866f, 0.0f, 0.5f,
		    };
		    break;
	    }

	    int[] indices = {
		0, 1, 2,
	    };

	    int[] normalIndices = {
		1, 0, 0,
	    };

	    IndexedTriangleArray geom
		= new IndexedTriangleArray(3,
		    GeometryArray.COORDINATES
		    | GeometryArray.NORMALS,
		    0, null,
		    3);

	    geom.setCoordinates(0, coords);
	    geom.setCoordinateIndices(0, indices);
	    geom.setNormals(0, normals);
	    geom.setNormalIndices(0, normalIndices);

	    setGeometry(geom);
	    setAppearance(app);
	}
    }
}
