/**
 * Project Looking Glass
 *
 * $RCSfile: StandardGlobalLights.java,v $
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
 * $Date: 2007-01-29 18:18:43 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.globallights;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;

import org.jdesktop.lg3d.sg.AmbientLight;
import org.jdesktop.lg3d.sg.BoundingSphere;
import org.jdesktop.lg3d.sg.DirectionalLight;
import org.jdesktop.lg3d.wg.Component3D;


public class StandardGlobalLights extends GlobalLights {
    
    public void initialize() {
        setName("StandardGlobalLights");
        
        BoundingSphere bounds
	    = new BoundingSphere(
		new Point3f(0.0f, 0.0f, 0.0f), Float.POSITIVE_INFINITY);

        Component3D top = new Component3D();
        top.setName("StandardGlobalLights");
        
	// Set up the global lights
	Color3f ambientColor = new Color3f(0.4f, 0.4f, 0.4f);
	Color3f light1Color = new Color3f(0.7f, 0.7f, 0.6f);
	Vector3f light1Direction  = new Vector3f(1.0f, -1.0f, -2.0f);
	Color3f light2Color = new Color3f(0.2f, 0.2f, 0.3f);
	Vector3f light2Direction  = new Vector3f(-1.0f, 1.0f, 0.0f);

	AmbientLight ambientLightNode = new AmbientLight(ambientColor);
	ambientLightNode.setInfluencingBounds(bounds);
	top.addChild(ambientLightNode);

	DirectionalLight light1
	    = new DirectionalLight(light1Color, light1Direction);
	light1.setInfluencingBounds(bounds);
	top.addChild(light1);

	DirectionalLight light2
	    = new DirectionalLight(light2Color, light2Direction);
	light2.setInfluencingBounds(bounds);
	top.addChild(light2);
        
        addChild(top);
    }
}

