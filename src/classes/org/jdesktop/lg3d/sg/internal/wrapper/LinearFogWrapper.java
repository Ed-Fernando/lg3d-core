/**
 * Project Looking Glass
 *
 * $RCSfile: LinearFogWrapper.java,v $
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
public interface LinearFogWrapper extends FogWrapper {
 
    /**
     * Sets front distance for fog.
     * @param frontDistance the distance at which fog starts obscuring objects
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setFrontDistance(float frontDistance);

    /**
     * Gets front distance for fog.
     * @return  the distance at which fog starts obscuring objects
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public float getFrontDistance();

    /**
     * Sets back distance for fog.
     * @param backDistance the distance at which fog totally obscurs objects
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setBackDistance(float backDistance);

    /**
     * Gets back distance for fog.
     * @return the distance at which fog totally obscurs objects
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public float getBackDistance();

}
