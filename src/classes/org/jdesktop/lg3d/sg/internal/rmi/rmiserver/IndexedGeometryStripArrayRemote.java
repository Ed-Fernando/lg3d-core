/**
 * Project Looking Glass
 *
 * $RCSfile: IndexedGeometryStripArrayRemote.java,v $
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
 * $Revision: 1.3 $
 * $Date: 2004-06-25 04:49:33 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.rmi.rmiserver;

/**
 * The IndexedGeometryStripArray object is an abstract class that is extended for
 * a set of IndexedGeometryArray strip primitives.  These include LINE_STRIP,
 * TRIANGLE_STRIP, and TRIANGLE_FAN.
 */

public interface IndexedGeometryStripArrayRemote extends IndexedGeometryArrayRemote{

  /**
   * Get number of strips in the GeometryStripArray
   * @return numStrips number of strips
   */
    public int getNumStrips() throws java.rmi.RemoteException;

    /**
     * Sets the array of strip index counts.  The length of this
     * array is the number of separate strips.  The elements in this
     * array specify the number of indices for each separate strip.
     * The sum of the elements in this array defines the total number
     * of valid indexed vertices that are rendered (validIndexCount).
     *
     * @param stripIndexCounts array that specifies
     * the count of the number of indices for each separate strip.
     *
     * @exception IllegalArgumentException if
     * <code>initialIndexIndex + validIndexCount > indexCount</code>
     */
    public void setStripIndexCounts(int[] stripIndexCounts) throws java.rmi.RemoteException;

    /**
     * Gets a list of indexCounts for each strip. The list is
     * copied into the specified array. The array must be
     * large enough to hold all of the ints.
     * @param stripIndexCounts an array that will receive indexCounts
     */
    public void getStripIndexCounts(int[] stripIndexCounts) throws java.rmi.RemoteException;

    /**
     * This method is not supported for indexed geometry strip arrays.
     * The sum of the elements in the strip index counts array defines
     * the valid index count.
     *
     * @exception UnsupportedOperationException this method is not supported
     *
     * @since Java 3D 1.3
     */
    public void setValidIndexCount(int validIndexCount) throws java.rmi.RemoteException;

}
