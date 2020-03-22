/**
 * Project Looking Glass
 *
 * $RCSfile: GeometryUpdater.java,v $
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
 * $Date: 2004-06-23 18:50:36 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.j3d.j3dwrapper;


/**
 * The GeometryUpdater interface is used in updating geometry data
 * that is accessed by reference from a live or compiled GeometryArray
 * object.  Applications that wish to modify such data must define a
 * class that implements this interface.  An instance of that class is
 * then passed to the <code>updateData</code> method of the
 * GeometryArray object to be modified.
 *
 * @since Java 3D 1.2
 */

public interface GeometryUpdater {
    /**
     * Updates geometry data that is accessed by reference.
     * This method is called by the updateData method of a
     * GeometryArray object to effect
     * safe updates to vertex data that
     * is referenced by that object.  Applications that wish to modify
     * such data must implement this method and perform all updates
     * within it.
     * <br>
     * NOTE: Applications should <i>not</i> call this method directly.
     *
     * @param geometry the Geometry object being updated.
     * @see GeometryArray#updateData
     */
    public void updateData(Geometry geometry);
}
