/**
 * Project Looking Glass
 *
 * $RCSfile: WigglingCursor.java,v $
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
 * $Revision: 1.8 $
 * $Date: 2007-05-04 16:18:36 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.cursor;

import javax.vecmath.Vector3f;
import javax.vecmath.Color4f;
import org.jdesktop.lg3d.sg.Appearance;
import org.jdesktop.lg3d.sg.GeometryArray;
import org.jdesktop.lg3d.sg.IndexedTriangleArray;
import org.jdesktop.lg3d.sg.Shape3D;
import org.jdesktop.lg3d.utils.action.ScaleActionBoolean;
import org.jdesktop.lg3d.utils.actionadapter.ActionMulticaster;
import org.jdesktop.lg3d.utils.actionadapter.Float2Adder;
import org.jdesktop.lg3d.utils.actionadapter.Float2Differ;
import org.jdesktop.lg3d.utils.actionadapter.Float2Scaler;
import org.jdesktop.lg3d.utils.animation.SpringRotationAnimationFloat;
import org.jdesktop.lg3d.utils.animation.OscillatingRotationAnimationFloat;
import org.jdesktop.lg3d.utils.c3danimation.NaturalMotionAnimation;
import org.jdesktop.lg3d.utils.c3danimation.ScaleChangeVisiblePlugin;
import org.jdesktop.lg3d.utils.eventadapter.MouseMovedEventAdapter;
import org.jdesktop.lg3d.utils.eventadapter.MousePressedEventAdapter;
import org.jdesktop.lg3d.utils.shape.RoundShadow;
import org.jdesktop.lg3d.utils.shape.SimpleAppearance;
import org.jdesktop.lg3d.wg.AnimationGroup;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Cursor3D;
import org.jdesktop.lg3d.wg.event.LgEventConnector;
import org.jdesktop.lg3d.wg.event.LgEventSource;


public class WigglingCursor extends Cursor3D {
    private static final float sizeThresholdToShowShadow = 0.01f;
    private static final float wiggleBase = (float)Math.toRadians(50);
    
    private float angleBase;

    public WigglingCursor(String name, float size) {
        this(name, size, (size >= sizeThresholdToShowShadow), new Color4f(0.85f, 0.85f, 1.0f, 0.7f), 230f);
    }
    
    public WigglingCursor(String name, float size, boolean drawShadow) {
        this(name, size, drawShadow, new Color4f(0.85f, 0.85f, 1.0f, 0.7f), (float)Math.toRadians(230));
    }
    
    public WigglingCursor(String name, float size, boolean drawShadow, Color4f color, float angle) {
        super(name);
        setAnimation(new NaturalMotionAnimation(250, new ScaleChangeVisiblePlugin(250)));
        
	Appearance app = new SimpleAppearance(color.x, color.y, color.z, color.w);
	Shape3D shape = new CursorBody(size, app);
        
	angleBase = (float)Math.toRadians(angle);
        OscillatingRotationAnimationFloat shakeAnim 
            = new OscillatingRotationAnimationFloat(new Vector3f(0.0f, 1.0f, 0.0f), wiggleBase, 4000, 300);
        SpringRotationAnimationFloat rotAnim 
            = new SpringRotationAnimationFloat(new Vector3f(1.0f, 0.0f, -1.0f), angleBase, 1000);
        
        LgEventConnector.getLgEventConnector().addListener(
            LgEventSource.ALL_SOURCES,
            new MouseMovedEventAdapter(
                new Float2Differ(
                    new ActionMulticaster(
                        new Float2Scaler(2000f, 2000f, (float)Math.toRadians(180),
                            new Float2Adder(shakeAnim)),
                        new Float2Scaler(-500f, 500f, (float)Math.toRadians(60),
                            new Float2Adder(rotAnim))))));
        
        AnimationGroup shakeAg = new AnimationGroup(shakeAnim);
        shakeAg.addChild(shape);
        AnimationGroup rotAg = new AnimationGroup(rotAnim);
        rotAg.addChild(shakeAg);
        
	Component3D body = new Component3D();
        body.addChild(rotAg);
        
        LgEventConnector.getLgEventConnector().addListener(
            LgEventSource.ALL_SOURCES,
            new MousePressedEventAdapter(
                new ScaleActionBoolean(body, 0.9f, 200)));
	
	if (drawShadow) {
            float shadowSizeX = size * 0.8f;
            float shadowSizeY = size * 0.4f;
            float shadowShiftX = size * 0.25f;
	    
	    body.addChild(
		new RoundShadow(
		    shadowSizeX, shadowSizeY, 12,
		    shadowShiftX, 0.0f, 0.0f, 0.5f));
	}
        
        addChild(body);
    }

    private static class CursorBody extends Shape3D {
	private CursorBody(float size, Appearance app) {
	    float[] coords = {
		 0.0f,          0.0f,       0.0f,
		 0.25f * size, -1 * size,  -0.1444f * size,
		 0.0f  * size, -1 * size,   0.2887f * size,
		-0.25f * size, -1 * size,  -0.1444f * size,
	    };

	    int[] indices = {
		2, 1, 0,
		3, 2, 0,
		1, 3, 0,
		1, 2, 3,
	    };

	    float[] normals = {
		 0.8571609f,  0.14286014f,  0.49483946f,
		-0.8571609f,  0.14286016f,  0.49483946f,
		 0.0f,        0.14286920f, -0.98974156f,
		 0.0f,       -1.0f,         0.0f,
	    };

	    int[] normalIndices = {
		0, 0, 0,
		1, 1, 1,
		2, 2, 2,
		3, 3, 3,
	    };

	    IndexedTriangleArray geom
		= new IndexedTriangleArray(4,
		    GeometryArray.COORDINATES
		    | GeometryArray.NORMALS,
		    0, null,
		    12);

	    geom.setCoordinates(0, coords);
	    geom.setCoordinateIndices(0, indices);
	    geom.setNormals(0, normals);
	    geom.setNormalIndices(0, normalIndices);

	    setGeometry(geom);
	    setAppearance(app);
	}
    }
}
