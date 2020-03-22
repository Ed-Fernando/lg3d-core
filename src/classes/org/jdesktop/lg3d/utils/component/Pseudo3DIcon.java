/**
 * Project Looking Glass
 *
 * $RCSfile: Pseudo3DIcon.java,v $
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
 * $Revision: 1.9 $
 * $Date: 2006-08-14 23:47:40 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.component;

import java.net.URL;
import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.utils.action.ScaleActionBoolean;
import org.jdesktop.lg3d.utils.action.TranslateActionBoolean;
import org.jdesktop.lg3d.utils.actionadapter.ActionMulticaster;
import org.jdesktop.lg3d.utils.c3danimation.NaturalMotionAnimation;
import org.jdesktop.lg3d.utils.eventadapter.MouseEnteredEventAdapter;
import org.jdesktop.lg3d.utils.eventadapter.MousePressedEventAdapter;
import org.jdesktop.lg3d.utils.shape.ImagePanel;
import org.jdesktop.lg3d.utils.shape.SimpleAppearance;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Container3D;


/**
 * By convention icons are 1cm square
 */
public class Pseudo3DIcon extends Component3D {
    private static final float DEFAULT_SIZE = 0.01f; // 1cm
    private static final int DEFAULT_ANIM_DURATION = 150;
    
    private Vector3f mouseEnteredTrans;
    
    public Pseudo3DIcon(URL imageUrl) {
        if (imageUrl == null) {
            throw new IllegalArgumentException("the imageUrl argument cannot be null");
        }
        float size = DEFAULT_SIZE;
        mouseEnteredTrans = new Vector3f(0.0f, size * 0.15f, 0.0f);
	setPreferredSize(new Vector3f(size, size, size));

	Component3D bodyComp = new Component3D();
        bodyComp.setAnimation(new NaturalMotionAnimation(DEFAULT_ANIM_DURATION));
	ImagePanel body = new ImagePanel(size, size);
	SimpleAppearance bodyApp 
	    = new SimpleAppearance(
		1.0f, 1.0f, 1.0f, 0.95f,
		SimpleAppearance.ENABLE_TEXTURE);
        try {
            bodyApp.setTexture(imageUrl);
        } catch (Exception e) {
            throw new RuntimeException("failed to initialize texture: " + imageUrl + "\n" + e);
        }
	body.setAppearance(bodyApp);
	bodyComp.addChild(body);
	bodyComp.setTranslation(0.0f, 0.0f, size * 0.6f);
	addChild(bodyComp);
        
        bodyComp.setMouseEventPropagatable(true);
        setMouseEventPropagatable(true);
        
	Component3D shadowComp = new Component3D();
	ImagePanel shadow = new ImagePanel(size, size);
	SimpleAppearance shadowApp 
	    = new SimpleAppearance(
		0.0f, 0.0f, 0.0f, 0.3f,
		SimpleAppearance.ENABLE_TEXTURE
		| SimpleAppearance.NO_GLOSS);
        
	shadowApp.setTexture(bodyApp.getTexture());
	shadow.setAppearance(shadowApp);
	shadowComp.addChild(shadow);
	shadowComp.setRotationAxis(1.0f, 0.0f, 0.0f);
	shadowComp.setRotationAngle((float)Math.toRadians(-90));
	shadowComp.setTranslation(0.0f, size * -0.5f, 0.0f);
	addChild(shadowComp);
        
        addListener(
            new MouseEnteredEventAdapter(
                new ActionMulticaster(
                    new TranslateActionBoolean(bodyComp, mouseEnteredTrans),
                    new ScaleActionBoolean(bodyComp, 1.15f),
                    new ScaleActionBoolean(shadowComp, 1.15f))));
        
        addListener(
            new MousePressedEventAdapter(
                new ScaleActionBoolean(bodyComp, 1.05f, 100)));
    }
    
    public Pseudo3DIcon(String imageName) {
        this(Pseudo3DIcon.class.getClassLoader().getResource(imageName));
    }
}

