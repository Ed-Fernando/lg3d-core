/**
 * Project Looking Glass
 *
 * $RCSfile: ExponentialFog.java,v $
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
 * $Date: 2005-04-14 23:04:19 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.rmi.rmiclient;

import javax.vecmath.Color3f;
import org.jdesktop.lg3d.sg.internal.wrapper.ExponentialFogWrapper;

/**
 * The ExponentialFog leaf node extends the Fog leaf node by adding a
 * fog density that is used as the exponent of the fog equation. The
 * density is defined in the local coordinate system of the node, but
 * the actual fog equation will ideally take place in eye coordinates.
 * <P>
 * The fog blending factor, f, is computed as follows:
 * <P><UL>
 * f = e<sup>-(density * z)</sup><P>
 * where
 * <ul>z is the distance from the viewpoint.<br>
 * density is the density of the fog.<P></ul></UL>
 * 
 * In addition to specifying the fog density, ExponentialFog lets you
 * specify the fog color, which is represented by R, G, and B
 * color values, where a color of (0,0,0) represents black.
 */
public class ExponentialFog extends Fog implements ExponentialFogWrapper {
    /**
     * Specifies that this ExponentialFog node allows read access to its
     * density information.
     */
    public static final int
    ALLOW_DENSITY_READ = CapabilityBits.EXPONENTIAL_FOG_ALLOW_DENSITY_READ;

    /**
     * Specifies that this ExponentialFog node allows write access to its
     * density information.
     */
    public static final int
    ALLOW_DENSITY_WRITE = CapabilityBits.EXPONENTIAL_FOG_ALLOW_DENSITY_WRITE;

    /**
     * Constructs an ExponentialFog node with default parameters.
     * The default values are as follows:
     * <ul>
     * density : 1.0<br>
     * </ul>
     */
    public ExponentialFog() {
	// Just use the defaults
    }

    protected void createRemote() {
        try {
            remote = SceneGraphSetup.getSGObjectFactory().newInstance(org.jdesktop.lg3d.sg.internal.rmi.rmiserver.ExponentialFog.class);
            setRemote( remote );
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }

    /**
     * Constructs an ExponentialFog node with the specified fog color.
     * @param color the fog color
     */
    public ExponentialFog(Color3f color) {
	setColor(color);
    }

    /**
     * Constructs an ExponentialFog node with the specified fog color
     * and density.
     * @param color the fog color
     * @param density the density of the fog
     */
    public ExponentialFog(Color3f color, float density) {
	setColor(color);
        setDensity(density);
    }

    /**
     * Constructs an ExponentialFog node with the specified fog color.
     * @param r the red component of the fog color
     * @param g the green component of the fog color
     * @param b the blue component of the fog color
     */
    public ExponentialFog(float r, float g, float b) {
	setColor(r,g,b);
    }

    /**
     * Constructs an ExponentialFog node with the specified fog color
     * and density.
     * @param r the red component of the fog color
     * @param g the green component of the fog color
     * @param b the blue component of the fog color
     * @param density the density of the fog
     */
    public ExponentialFog(float r, float g, float b, float density) {
	setColor(r,g,b);
        setDensity(density);
    }

    /**
     * Sets fog density.
     * @param density the new density of this fog
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setDensity(float density) {
        try {
            ((org.jdesktop.lg3d.sg.internal.rmi.rmiserver.ExponentialFog)remote).setDensity(density);
        } catch(java.rmi.RemoteException rex) {
            throw new RuntimeException(rex);
        }
    }

    /**
     * Gets fog density.
     * @return the density of this fog
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public float getDensity() {
        try {
            return ((org.jdesktop.lg3d.sg.internal.rmi.rmiserver.ExponentialFog)remote).getDensity();
        } catch(java.rmi.RemoteException rex) {
            throw new RuntimeException(rex);
        }
    }

}
