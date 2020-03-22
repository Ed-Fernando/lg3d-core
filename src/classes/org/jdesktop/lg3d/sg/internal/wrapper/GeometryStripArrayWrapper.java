/**
 * Project Looking Glass
 *
 * $RCSfile: GeometryStripArrayWrapper.java,v $
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
 * $Date: 2004-06-23 18:51:43 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.wrapper;

/**
 * The GeometryStripArray object is an abstract class that is extended for
 * a set of GeometryArray strip primitives.  These include LINE_STRIP,
 * TRIANGLE_STRIP, and TRIANGLE_FAN. In addition to specifying the array
 * of vertex elements, which is inherited from GeometryArray, the
 * GeometryStripArray class specifies the number of strips and an
 * array of per-strip vertex counts that specify where the separate strips
 * appear in the vertex array.
 */

public interface GeometryStripArrayWrapper extends GeometryArrayWrapper {

    /**
     * Get number of strips in the GeometryStripArray.
     * @return numStrips number of strips
     */
    public int getNumStrips() ;

    /**
     * Sets the array of strip vertex counts.  The length of this
     * array is the number of separate strips.  The elements in this
     * array specify the number of vertices for each separate strip.
     * The sum of the elements in this array defines the total number
     * of valid vertices that are rendered (validVertexCount).
     *
     * @param stripVertexCounts array that specifies
     * the count of the number of vertices for each separate strip.
     *
     * @exception IllegalArgumentException if any of the following are
     * true:
     * <ul>
     * <code>initialVertexIndex + validVertexCount > vertexCount</code>,<br>
     * <code>initialCoordIndex + validVertexCount > vertexCount</code>,<br>
     * <code>initialColorIndex + validVertexCount > vertexCount</code>,<br>
     * <code>initialNormalIndex + validVertexCount > vertexCount</code>,<br>
     * <code>initialTexCoordIndex + validVertexCount > vertexCount</code>
     * </ul>
     * <p>
     *
     * @exception ArrayIndexOutOfBoundsException if the geometry data format
     * is <code>BY_REFERENCE</code> and any the following
     * are true for non-null array references:
     * <ul>
     * <code>CoordRef.length</code> < <i>num_words</i> *
     * (<code>initialCoordIndex + validVertexCount</code>)<br>
     * <code>ColorRef.length</code> < <i>num_words</i> *
     * (<code>initialColorIndex + validVertexCount</code>)<br>
     * <code>NormalRef.length</code> < <i>num_words</i> *
     * (<code>initialNormalIndex + validVertexCount</code>)<br>
     * <code>TexCoordRef.length</code> < <i>num_words</i> *
     * (<code>initialTexCoordIndex + validVertexCount</code>)<br>
     * </ul>
     * where <i>num_words</i> depends on which variant of
     * <code>set</code><i>Array</i><code>Ref</code> is used.
     *
     * @since Java 3D 1.3
     */
    public void setStripVertexCounts(int[] stripVertexCounts) ;

    /**
     * Get a list of vertexCounts for each strip. The list is copied
     * into the specified array. The array must be large enough to hold 
     * all of the ints.
     * @param stripVertexCounts an array that will receive vertexCounts
     */
    public void getStripVertexCounts(int[] stripVertexCounts) ;

    /**
     * This method is not supported for geometry strip arrays.
     * The sum of the elements in the strip vertex counts array
     * defines the valid vertex count.
     *
     * @exception UnsupportedOperationException this method is not supported
     *
     * @since Java 3D 1.3
     */
    public void setValidVertexCount(int validVertexCount) ;

}
