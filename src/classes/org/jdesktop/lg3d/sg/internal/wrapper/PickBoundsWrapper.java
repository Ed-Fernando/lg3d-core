/**
 * Project Looking Glass
 *
 * $RCSfile: PickBoundsWrapper.java,v $
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
 * $Date: 2004-06-23 18:51:46 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.wrapper;

import javax.vecmath.*;



/**
 * PickBounds is a finite pick shape defined with a Bounds object.  It can
 * be used as an argument to the picking methods in BranchGroup and Locale.
 *
 * @see BranchGroup#pickAll
 * @see Locale#pickAll
 */

public interface PickBoundsWrapper extends PickShapeWrapper {

   /**
     * Sets the bounds object of this PickBounds to the specified object.
     * @param boundsObject the new bounds of this PickBounds.
     */

    public void set(BoundsWrapper boundsObject);

  

    /**

     * Gets the bounds object from this PickBounds.

     * @return the bounds.

     */

    public BoundsWrapper get();



    /*

     * Return true if shape intersect with bounds.

     * The point of intersection is stored in pickPos.

     */

//    final boolean intersect(Bounds bounds, Point3f pickPos) {

//	return bounds.intersect(this.bounds, pickPos);

//    }

    

//    public Point3f getStartPoint() {

//	return bounds.getCenter();

//    }    



}

