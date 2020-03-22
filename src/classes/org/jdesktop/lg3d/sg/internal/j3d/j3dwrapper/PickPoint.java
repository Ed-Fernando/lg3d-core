/**
 * Project Looking Glass
 *
 * $RCSfile: PickPoint.java,v $
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
 * PickPoint is a pick shape defined as a single point.  It can
 * be used as an argument to the picking methods in BranchGroup and Locale.
 *
 * @see BranchGroup#pickAll
 * @see Locale#pickAll
 */
public final class PickPoint extends PickShape {

    Point3d location;

    private Point3f locationf = new Point3f();

    /**
     * Constructs a PickPoint using a default point.
     * The point is initialized to (0,0,0).
     */
    private PickPoint() {
	location = new Point3d();
    }

    /**
     * Constructs a PickPoint from the specified parameter.
     * @param location the pick point.
     */
    private PickPoint(Point3d location) {
	this.location = new Point3d(location);
    }

    /**
     * Sets the position of this PickPoint to the specified value.
     * @param location the new pick point.
     */
    private void set(Point3d location) {
	this.location.x = location.x;
	this.location.y = location.y;
	this.location.z = location.z;
    }

    /**
     * Gets the position of this PickPoint.
     * @return the current pick point.
     */
    private void get(Point3d location) {
	location.x = this.location.x;
	location.y = this.location.y;
	location.z = this.location.z;
    }
}
