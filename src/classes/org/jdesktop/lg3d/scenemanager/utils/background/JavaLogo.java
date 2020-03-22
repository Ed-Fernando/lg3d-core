/**
 * Project Looking Glass
 *
 * $RCSfile: JavaLogo.java,v $
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
 * $Revision: 1.7 $
 * $Date: 2006-08-15 19:17:15 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.background;


import javax.vecmath.Vector3f;

import org.jdesktop.lg3d.sg.Node;
import org.jdesktop.lg3d.sg.Shape3D;
import org.jdesktop.lg3d.utils.actionadapter.Float2Adder;
import org.jdesktop.lg3d.utils.actionadapter.Float2Differ;
import org.jdesktop.lg3d.utils.actionadapter.Float2Scaler;
import org.jdesktop.lg3d.utils.animation.SpringRotationAnimationFloat;
import org.jdesktop.lg3d.utils.eventadapter.MouseMovedEventAdapter;
import org.jdesktop.lg3d.utils.shape.ImagePanel;
import org.jdesktop.lg3d.utils.shape.OriginTranslation;
import org.jdesktop.lg3d.utils.shape.PickableRegion;
import org.jdesktop.lg3d.wg.AnimationGroup;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.event.LgEventConnector;
import org.jdesktop.lg3d.wg.event.LgEventSource;


public class JavaLogo extends Component3D {
    private static final int origHeight = 1331;
    private static final int origWidth = 649;
    
    public JavaLogo() {
        setName("JavaLogo");
        /*
         * this -*> origin translation -> animation group -> logo shape
         */
	setupPartial(0, 649, 453,   0, 870,    45.0f, (float)Math.toRadians(30));
	setupPartial(1, 632, 400,   2, 442,    60.0f, (float)Math.toRadians(60));
	setupPartial(2, 260, 450, 178,   6,  3000.0f, (float)Math.toRadians(360 * 10));
	setupPartial(3, 268, 324, 256, 170, -5000.0f, (float)Math.toRadians(360 * 10));
        
        float width = (float)origWidth/origHeight;
        float height = 1.0f;
        
	addChild(
            new OriginTranslation(
                new PickableRegion(width, height),
                new Vector3f(0.0f, 0.0f, 0.1f)));
        
        setPreferredSize(new Vector3f(width, height, 0.0f));
    }
    
    public void reposition(float logoSize,
            float screenWidth, float screenHeight, float logoZPos, 
            float screenHalfOfTanOfFov) 
    {
        float logoX = (screenWidth  - logoZPos * screenHalfOfTanOfFov - logoSize) * 0.5f;
	float logoY = (screenHeight - logoZPos * screenHalfOfTanOfFov * screenHeight / screenWidth - logoSize) * 0.5f;
        
        setTranslation(logoX, logoY, logoZPos);
        setScale(logoSize);
    }
    
    private void setupPartial(int i, float w, float h, 
        float x, float y, float sensitivity, float limit) 
    {
        // normalize the size
        w /= origHeight;
        h /= origHeight;
        x /= origHeight;
        y /= origHeight;
        
        AnimationGroup ag = new AnimationGroup();
	String filename 
            = "resources/images/background/Java-logo-" + i + ".png";
        
	try {
	    Shape3D logo = new ImagePanel(
                    this.getClass().getClassLoader().getResource(filename), 
                    w, h, true, 0.6f);
	    ag.addChild(logo);
	} catch (Exception e) {
	    logger.warning("failed to read image file: " + e);
	}
        // move (0,0) to the center of the logo
	x += (w - (float)origWidth/origHeight) * 0.5f;
	y = (1.0f - h) * 0.5f - y;
	Node otag = new OriginTranslation(ag, new Vector3f(x, y, 0.0f));
        
        SpringRotationAnimationFloat anim 
            = new SpringRotationAnimationFloat(new Vector3f(0.0f, 1.0f, 0.0f), 4000);
        ag.setAnimation(anim);
        LgEventConnector.getLgEventConnector().addListener(
            LgEventSource.ALL_SOURCES,
            new MouseMovedEventAdapter(
                new Float2Differ(
                    new Float2Scaler(sensitivity, 0.0f, limit,
                        new Float2Adder(anim)))));
        
        addChild(otag);
    }
}
