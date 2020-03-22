/**
 * Project Looking Glass
 *
 * $RCSfile: DirectionalLight.java,v $
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
 * $Date: 2004-06-23 18:50:50 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.rmi.rmiclient;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

/**
 * DirectionalLight node.
 *
 * @version     1.36, 02/04/01 14:58:10
 */

/**
 * A DirectionalLight node defines an oriented light with an origin at
 * infinity. It has the same attributes as a Light node, with the
 * addition of a directional vector to specify the direction in which the
 * light shines. A directional light has parallel light rays that travel 
 * in one direction along the specified vector. Directional light contributes
 * to diffuse and specular reflections, which in turn depend on the
 * orientation of an object's surface but not its position. A directional
 * light does not contribute to ambient reflections.
 */

public class DirectionalLight extends Light {
 /**
  * Specifies that the Node allows access to its object's direction
  * information.
  */
  public static final int
    ALLOW_DIRECTION_READ = CapabilityBits.DIRECTIONAL_LIGHT_ALLOW_DIRECTION_READ;

  /**
   * Specifies that the Node allows writing to its object's direction
   * information.
   */
  public static final int
    ALLOW_DIRECTION_WRITE = CapabilityBits.DIRECTIONAL_LIGHT_ALLOW_DIRECTION_WRITE;

    /**
     * Constructs a DirectionalLight node with default parameters.
     * The default values are as follows:
     * <ul>
     * direction : (0,0,-1)<br>
     * </ul>
     */
    public DirectionalLight() {
    }
    
    protected void createRemote() {
        try {
            remote = SceneGraphSetup.getSGObjectFactory().newInstance(org.jdesktop.lg3d.sg.internal.rmi.rmiserver.DirectionalLight.class);
            setRemote( remote );
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }

    /**
     * Constructs and initializes a directional light.
     * @param color the color of the light source
     * @param direction the direction vector pointing from the light
     * to the object
     */
    public DirectionalLight(Color3f color, Vector3f direction) {
        setColor(color);
        setDirection(direction);
    }

    /**
     * Constructs and initializes a directional light.
     * @param lightOn flag indicating whether this light is on or off
     * @param color the color of the light source
     * @param direction the direction vector pointing from the light
     * to the object
     */
    public DirectionalLight(boolean lightOn, Color3f color, Vector3f direction) {
        setEnable(lightOn);
        setColor(color);
        setDirection(direction);
    }

  /**
   * Set light direction.
   * @param direction the new direction
   * @exception CapabilityNotSetException if appropriate capability is
   * not set and this object is part of live or compiled scene graph
   */
    public void setDirection(Vector3f direction) {
        try {
            ((org.jdesktop.lg3d.sg.internal.rmi.rmiserver.DirectionalLight)remote).setDirection(direction);
        } catch(java.rmi.RemoteException rex) {
            throw new RuntimeException(rex);
        }
    }

  /**
   * Set light direction.
   * @param x  the new X direction
   * @param y  the new Y direction
   * @param z  the new Z direction
   * @exception CapabilityNotSetException if appropriate capability is
   * not set and this object is part of live or compiled scene graph
   */
    public void setDirection(float x, float y, float z) {
        try {
            ((org.jdesktop.lg3d.sg.internal.rmi.rmiserver.DirectionalLight)remote).setDirection(x,y,z);
        } catch(java.rmi.RemoteException rex) {
            throw new RuntimeException(rex);
        }
  }

  /**
   * Gets this Light's current direction and places it in the parameter specified.
   * @param direction the vector that will receive this node's direction
   * @exception CapabilityNotSetException if appropriate capability is
   * not set and this object is part of live or compiled scene graph
   */
    public void getDirection(Vector3f direction) {
        try {
            ((org.jdesktop.lg3d.sg.internal.rmi.rmiserver.DirectionalLight)remote).getDirection(direction);
        } catch(java.rmi.RemoteException rex) {
            throw new RuntimeException(rex);
        }
    }


}
