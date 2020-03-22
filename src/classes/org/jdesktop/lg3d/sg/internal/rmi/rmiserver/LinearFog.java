/**
 * Project Looking Glass
 *
 * $RCSfile: LinearFog.java,v $
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
 * $Date: 2005-04-14 23:04:21 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.sg.internal.rmi.rmiserver;

import javax.vecmath.Color3f;

/**
 * The LinearFog leaf node defines fog distance parameters for
 * linear fog.
 * LinearFog extends the Fog node by adding a pair of distance values,
 * in Z, at which the fog should start obscuring the scene and should maximally
 * obscure the scene.
 * <P>
 * The front and back fog distances are defined in the local coordinate system of
 * the node, but the actual fog equation will ideally take place in eye
 * coordinates.
 * <P>
 * The linear fog blending factor, f, is computed as follows:
 * <P><UL>
 * f = backDistance - z / backDistance - frontDistance<P>
 * where
 * <ul>z is the distance from the viewpoint.<br>
 * frontDistance is the distance at which fog starts obscuring objects.<br>
 * backDistance is the distance at which fog totally obscurs objects.
 * </ul><P></UL>
 */
public class LinearFog extends Fog implements LinearFogRemote {
    /**
     * Specifies that this LinearFog node allows read access to its distance
     * information.
     */
    public static final int
    ALLOW_DISTANCE_READ = CapabilityBits.LINEAR_FOG_ALLOW_DISTANCE_READ;

    /**
     * Specifies that this LinearFog node allows write access to its distance
     * information.
     */
    public static final int
    ALLOW_DISTANCE_WRITE = CapabilityBits.LINEAR_FOG_ALLOW_DISTANCE_WRITE;

    /**
     * Constructs a LinearFog node with default parameters.
     * The default values are as follows:
     * <ul>
     * front distance : 0.1<br>
     * back distance : 1.0<br>
     * </ul>
     */
    public LinearFog() throws java.rmi.RemoteException {
	// Just use the defaults
    }

    protected void createWrapped() {
        wrapped = new org.jdesktop.lg3d.sg.LinearFog();
        wrapped.setUserData( this );
    }
    
    /**
     * Constructs a LinearFog node with the specified fog color.
     * @param color the fog color
     */
    public LinearFog(Color3f color) throws java.rmi.RemoteException {
	setColor(color);
    }

    /**
     * Constructs a LinearFog node with the specified fog color and distances.
     * @param color the fog color
     * @param frontDistance the front   distance for the fog
     * @param backDistance the back   distance for the fog
     */
    public LinearFog(Color3f color, float frontDistance, float backDistance) throws java.rmi.RemoteException {
	setColor(color);
        setFrontDistance(frontDistance);
        setBackDistance(backDistance);
    }

    /**
     * Constructs a LinearFog node with the specified fog color.
     * @param r the red component of the fog color
     * @param g the green component of the fog color
     * @param b the blue component of the fog color
     */
    public LinearFog(float r, float g, float b) throws java.rmi.RemoteException {
	setColor(r,g,b);
    }

    /**
     * Constructs a LinearFog node with the specified fog color and distances.
     * @param r the red component of the fog color
     * @param g the green component of the fog color
     * @param b the blue component of the fog color
     * @param frontDistance the front   distance for the fog
     * @param backDistance the back   distance for the fog
     */
    public LinearFog(float r, float g, float b,
		     float frontDistance, float backDistance) throws java.rmi.RemoteException {
	setColor(r,g,b);
        setFrontDistance(frontDistance);
        setBackDistance(backDistance);
    }

    /**
     * Sets front distance for fog.
     * @param frontDistance the distance at which fog starts obscuring objects
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setFrontDistance(float frontDistance) throws java.rmi.RemoteException {
    	((org.jdesktop.lg3d.sg.LinearFog)wrapped).setFrontDistance(frontDistance);
    }

    /**
     * Gets front distance for fog.
     * @return  the distance at which fog starts obscuring objects
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public float getFrontDistance() throws java.rmi.RemoteException {
    	return (float)((org.jdesktop.lg3d.sg.LinearFog)wrapped).getFrontDistance();
    }

    /**
     * Sets back distance for fog.
     * @param backDistance the distance at which fog totally obscurs objects
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setBackDistance(float backDistance) throws java.rmi.RemoteException {
    	((org.jdesktop.lg3d.sg.LinearFog)wrapped).setBackDistance(backDistance);
    }

    /**
     * Gets back distance for fog.
     * @return the distance at which fog totally obscurs objects
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public float getBackDistance() throws java.rmi.RemoteException {
    	return (float)((org.jdesktop.lg3d.sg.LinearFog)wrapped).getBackDistance();
    }

}
