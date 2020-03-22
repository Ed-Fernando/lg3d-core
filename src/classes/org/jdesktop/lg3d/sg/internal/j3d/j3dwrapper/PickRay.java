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
 * $Date: 2004-06-23 18:50:41 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.j3d.j3dwrapper;

import javax.vecmath.*;

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
	wrapped = new javax.media.j3d.PickRay();
        //wrapped.setUserData( this );
    }

    /**
     * Constructs an infinite ray pick shape from the specified
     * parameters.
     * @param origin the origin of the ray.
     * @param direction the direction of the ray.
     */
    public PickRay(Point3f origin, Vector3f direction) {
	wrapped = new javax.media.j3d.PickRay( new Point3d(origin), 
                                               new Vector3d(direction) );
    }

    /**
     * Sets the parameters of this PickRay to the specified values.
     * @param origin the origin of the ray.
     * @param direction the direction of the ray.
     */
    public void set(Point3f origin, Vector3f direction) {
	((javax.media.j3d.PickRay)wrapped).set( new Point3d(origin), 
                                                new Vector3d(direction) );
    }

    /**
     * Retrieves the parameters from this PickRay.
     * @param origin the Point3d object into which the origin will be copied.
     * @param direction the Vector3d object into which the direction
     * will be copied.
     */
    public void get(Point3f origin, Vector3f direction) {
        Point3d p3d = new Point3d();
        Vector3d v3d = new Vector3d();
	((javax.media.j3d.PickRay)wrapped).get( p3d, v3d );
        
        origin.set( p3d );
        direction.set( v3d );
    }

}
