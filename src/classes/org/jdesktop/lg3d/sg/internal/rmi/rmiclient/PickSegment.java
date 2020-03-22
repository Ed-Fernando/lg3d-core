/**
 * Project Looking Glass
 *
 * $RCSfile: PickSegment.java,v $
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
 * $Date: 2004-06-23 18:50:56 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.rmi.rmiclient;

import javax.vecmath.*;

/**
 * PickSegment is a line segment pick shape.  It can
 * be used as an argument to the picking methods in BranchGroup and Locale.
 *
 * @see BranchGroup#pickAll
 * @see Locale#pickAll
 */
public final class PickSegment extends PickShape {

    /**
     * Constructs a line segment pick shape from the specified
     * parameters.
     * @param start the start point of the line segment.
     * @param end the end point of the line segment.
     */
    public PickSegment(Point3f start, Point3f end) {
	wrapped = new javax.media.j3d.PickSegment( new Point3d(start), 
                                                   new Point3d(end));
    }


    /**
     * Sets the parameters of this PickSegment to the specified values.
     * @param start the start point of the line segment.
     * @param end the end point of the line segment.
     */
    public void set(Point3f start, Point3f end) {
	((javax.media.j3d.PickSegment)wrapped).set( new Point3d(start),
                                                    new Point3d(end));
    }


    /**
     * Gets the parameters from this PickSegment.
     * @param start the Point3d object into which the start
     * point will be copied.
     * @param end the Point3d object into which the end point
     * will be copied.
     */
    public void get(Point3f start, Point3f end) {
        Point3d startd = new Point3d();
        Point3d endd = new Point3d();
        
	((javax.media.j3d.PickSegment)wrapped).get( startd, endd );
        start.set(startd);
        end.set(endd);
    }

}
