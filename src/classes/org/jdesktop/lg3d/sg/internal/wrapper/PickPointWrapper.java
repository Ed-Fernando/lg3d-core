/**
 * Project Looking Glass
 *
 * $RCSfile: PickPointWrapper.java,v $
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
 * $Date: 2004-06-23 18:51:47 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.wrapper;

import javax.vecmath.*;

/**
 * PickPoint is a pick shape defined as a single point.  It can
 * be used as an argument to the picking methods in BranchGroup and Locale.
 *
 * @see BranchGroup#pickAll
 * @see Locale#pickAll
 */
public interface PickPointWrapper extends PickShapeWrapper {

    /**
     * Sets the position of this PickPoint to the specified value.
     * @param location the new pick point.
     */
    public void set(Point3f location);

    /**
     * Gets the position of this PickPoint.
     * @return the current pick point.
     */
    public void get(Point3f location);
}
