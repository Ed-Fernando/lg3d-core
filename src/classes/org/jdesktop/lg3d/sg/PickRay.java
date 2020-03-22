/**
 * Project Looking Glass
 *
 * $RCSfile: PickRay.java,v $
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
 * $Date: 2004-06-23 18:50:28 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg;

import javax.vecmath.*;
import org.jdesktop.lg3d.sg.internal.wrapper.PickRayWrapper;

/**
 * PickRay is an infinite ray pick shape.  It can
 * be used as an argument to the picking methods in BranchGroup and Locale.
 *
 * @see BranchGroup#pickAll
 * @see Locale#pickAll
 */
public final class PickRay extends PickShape {

    Point3d origin;
    Vector3d direction;
    
    private Point3f originf = new Point3f();

    /**
     * Constructs an empty PickRay.  The origin and direction of the
     * ray are initialized to (0,0,0).
     */
    public PickRay() {
        wrapped = instantiate( SceneGraphSetup.getWrapperPackage()+"PickRay");
    }

    /**
     * Constructs an infinite ray pick shape from the specified
     * parameters.
     * @param origin the origin of the ray.
     * @param direction the direction of the ray.
     */
    public PickRay(Point3f origin, Vector3f direction) {
        wrapped = instantiate( SceneGraphSetup.getWrapperPackage()+"PickRay", 
                               new Class[] {Point3f.class, Point3f.class}, 
                               new Object[] { origin, direction } );
    }

    /**
     * Sets the parameters of this PickRay to the specified values.
     * @param origin the origin of the ray.
     * @param direction the direction of the ray.
     */
    public void set(Point3f origin, Vector3f direction) {
	((PickRayWrapper)wrapped).set( origin, direction );
    }

    /**
     * Retrieves the parameters from this PickRay.
     * @param origin the Point3d object into which the origin will be copied.
     * @param direction the Vector3d object into which the direction
     * will be copied.
     */
    public void get(Point3f origin, Vector3f direction) {
	((PickRayWrapper)wrapped).get( origin, direction );
    }

}
