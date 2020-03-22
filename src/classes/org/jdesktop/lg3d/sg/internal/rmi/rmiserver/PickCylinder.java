/**
 * Project Looking Glass
 *
 * $RCSfile: PickCylinder.java,v $
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
 * $Date: 2004-06-23 18:51:18 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.rmi.rmiserver;

import javax.vecmath.*;

/**
 * PickCylinder is the abstract base class of all cylindrical pick shapes.
 *
 * @since Java 3D 1.2
 */
public abstract class PickCylinder extends PickShape {

    Point3d origin;
    Vector3d direction;
    double radius;

    private Point3f originf = new Point3f();


    /**
     * Gets the origin point of this cylinder object.
     * @param origin the Point3d object into which the origin
     * point will be copied
     */
     public void getOrigin(Point3f origin) {
	((org.jdesktop.lg3d.sg.PickCylinder)wrapped).getOrigin( origin );
     }

    /**
     * Gets the radius of this cylinder object
     * @return the radius in radians
     */
    public float getRadius() {
	return 	(float)((org.jdesktop.lg3d.sg.PickCylinder)wrapped).getRadius();

    }

    /**
     * Gets the direction of this cylinder.
     * @param direction the Vector3d object into which the direction
     * will be copied
     */
    public void getDirection(Vector3f direction) {
	((org.jdesktop.lg3d.sg.PickCylinder)wrapped).getDirection( direction );
    }

    /*
     * Return true if shape intersect with bounds.
     * The point of intersection is stored in pickPos.
     */
    abstract boolean intersect(Bounds bounds, Point4d pickPos);

}
