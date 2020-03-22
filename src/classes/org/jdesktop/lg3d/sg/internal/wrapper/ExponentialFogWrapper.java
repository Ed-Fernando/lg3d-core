/**
 * Project Looking Glass
 *
 * $RCSfile: ExponentialFogWrapper.java,v $
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
 * $Date: 2005-04-14 23:04:22 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.wrapper;

import javax.vecmath.Color3f;

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
public interface ExponentialFogWrapper extends FogWrapper {

    /**
     * Sets fog density.
     * @param density the new density of this fog
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setDensity(float density);

    /**
     * Gets fog density.
     * @return the density of this fog
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public float getDensity();

}
