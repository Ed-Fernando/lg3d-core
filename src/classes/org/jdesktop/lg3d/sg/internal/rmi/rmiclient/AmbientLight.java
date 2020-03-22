/**
 * Project Looking Glass
 *
 * $RCSfile: AmbientLight.java,v $
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
 * $Revision: 1.2 $
 * $Date: 2004-06-23 18:50:47 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.rmi.rmiclient;

import javax.vecmath.Color3f;

/**
 * An ambient light source object. Ambient light is that light
 * that seems to come from all directions. The AmbientLight object
 * has the same attributes as a Light node, including color,
 * influencing bounds, scopes, and
 * a flag indicating whether this light source is on or off.
 * Ambient reflections do not depend on the orientation or
 * position of a surface. 
 * Ambient light has only an ambient reflection component.
 * It does not have diffuse or specular reflection components.
 * <p>
 * For more information on Java 3D lighting, see the class description
 * for Light.
 * <p>
 *
 * @version 	1.26, 02/04/01 14:56:28
 */

public class AmbientLight extends Light {
    /**
     * Constructs and initializes an ambient light using default parameters.
     */
    public AmbientLight() {
    }
    
    protected void createRemote() {
        try {
            remote = SceneGraphSetup.getSGObjectFactory().newInstance(org.jdesktop.lg3d.sg.internal.rmi.rmiserver.AmbientLight.class);
            setRemote( remote );
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }


    /**
     * Constructs and initializes an ambient light using the specified
     * parameters.
     * @param color the color of the light source.
     */
    public AmbientLight(Color3f color) {
	setColor(color);
    }


    /**
     * Constructs and initializes an ambient light using the specified
     * parameters.
     * @param lightOn flag indicating whether this light is on or off.
     * @param color the color of the light source.
     */
    public AmbientLight(boolean lightOn, Color3f color) {
	setColor(color);
        setEnable(lightOn);
    }

}
