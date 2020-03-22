/**
 * Project Looking Glass
 *
 * $RCSfile: IndexedGeometryStripArray.java,v $
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
 * $Date: 2004-06-25 04:49:31 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg;

import org.jdesktop.lg3d.sg.internal.wrapper.IndexedGeometryStripArrayWrapper;

/**
 * The IndexedGeometryStripArray object is an abstract class that is extended for
 * a set of IndexedGeometryArray strip primitives.  These include LINE_STRIP,
 * TRIANGLE_STRIP, and TRIANGLE_FAN.
 */

public abstract class IndexedGeometryStripArray extends IndexedGeometryArray {

    // non-public, no parameter constructor
    IndexedGeometryStripArray() {}

    /**
     * Constructs an empty IndexedGeometryStripArray object with the specified
     * number of vertices, vertex format, number of indices, and
     * array of per-strip index counts.
     * @param vertexCount the number of vertex elements in this array
     * @param vertexFormat a mask indicating which components are
     * present in each vertex.  This is specified as one or more
     * individual flags that are bitwise "OR"ed together to describe
     * the per-vertex data.
     * The flags include: COORDINATES, to signal the inclusion of
     * vertex positions--always present; NORMALS, to signal 
     * the inclusion of per vertex normals; one of COLOR_3,
     * COLOR_4, to signal the inclusion of per vertex
     * colors (without or with color information); and one of 
     * TEXTURE_COORDINATE_2, TEXTURE_COORDINATE_3 or TEXTURE_COORDINATE_4, 
     * to signal the
     * inclusion of per-vertex texture coordinates 2D, 3D or 4D.
     * @param indexCount the number of indices in this object.  This
     * count is the maximum number of vertices that will be rendered.
     * @param stripIndexCounts array that specifies
     * the count of the number of indices for each separate strip.
     * The length of this array is the number of separate strips.
     * The sum of the elements in this array defines the total number
     * of valid indexed vertices that are rendered (validIndexCount).
     *
     * @exception IllegalArgumentException if
     * <code>validIndexCount > indexCount</code>
     * @deprecated Not in WSG
     */
    IndexedGeometryStripArray(int vertexCount,
				     int vertexFormat,
				     int indexCount,
				     int[] stripIndexCounts) {
	
    }

    /**
     * Constructs an empty IndexedGeometryStripArray object with the specified
     * number of vertices, vertex format, number of texture coordinate
     * sets, texture coordinate mapping array, number of indices, and
     * array of per-strip index counts.
     *
     * @param vertexCount the number of vertex elements in this object<p>
     *
     * @param vertexFormat a mask indicating which components are
     * present in each vertex.  This is specified as one or more
     * individual flags that are bitwise "OR"ed together to describe
     * the per-vertex data.
     * The flags include: COORDINATES, to signal the inclusion of
     * vertex positions--always present; NORMALS, to signal 
     * the inclusion of per vertex normals; one of COLOR_3,
     * COLOR_4, to signal the inclusion of per vertex
     * colors (without or with color information); and one of 
     * TEXTURE_COORDINATE_2, TEXTURE_COORDINATE_3 or TEXTURE_COORDINATE_4, 
     * to signal the
     * inclusion of per-vertex texture coordinates 2D, 3D or 4D.<p>
     *
     * @param texCoordSetCount the number of texture coordinate sets
     * in this GeometryArray object.  If <code>vertexFormat</code>
     * does not include one of <code>TEXTURE_COORDINATE_2</code>,
     * <code>TEXTURE_COORDINATE_3</code> or
     * <code>TEXTURE_COORDINATE_4</code>, the
     * <code>texCoordSetCount</code> parameter is not used.<p>
     *
     * @param texCoordSetMap an array that maps texture coordinate
     * sets to texture units.  The array is indexed by texture unit
     * number for each texture unit in the associated Appearance
     * object.  The values in the array specify the texture coordinate
     * set within this GeometryArray object that maps to the
     * corresponding texture
     * unit.  All elements within the array must be less than
     * <code>texCoordSetCount</code>.  A negative value specifies that
     * no texture coordinate set maps to the texture unit
     * corresponding to the index.  If there are more texture units in
     * any associated Appearance object than elements in the mapping
     * array, the extra elements are assumed to be -1.  The same
     * texture coordinate set may be used for more than one texture
     * unit.  Each texture unit in every associated Appearance must
     * have a valid source of texture coordinates: either a
     * non-negative texture coordinate set must be specified in the
     * mapping array or texture coordinate generation must be enabled.
     * Texture coordinate generation will take precedence for those
     * texture units for which a texture coordinate set is specified
     * and texture coordinate generation is enabled.  If
     * <code>vertexFormat</code> does not include one of
     * <code>TEXTURE_COORDINATE_2</code>,
     * <code>TEXTURE_COORDINATE_3</code> or
     * <code>TEXTURE_COORDINATE_4</code>, the
     * <code>texCoordSetMap</code> array is not used.<p>
     *
     * @param indexCount the number of indices in this object.  This
     * count is the maximum number of vertices that will be rendered.<p>
     *
     * @param stripIndexCounts array that specifies
     * the count of the number of indices for each separate strip.
     * The length of this array is the number of separate strips.
     * The sum of the elements in this array defines the total number
     * of valid indexed vertices that are rendered (validIndexCount).
     *
     * @exception IllegalArgumentException if
     * <code>validIndexCount > indexCount</code>
     *
     * @since Java 3D 1.2
     * @deprecated Not in WSG
     */
    IndexedGeometryStripArray(int vertexCount,
				     int vertexFormat,
				     int texCoordSetCount,
				     int[] texCoordSetMap,
				     int indexCount,
				     int[] stripIndexCounts) {
	
    }

  /**
   * Get number of strips in the GeometryStripArray
   * @return numStrips number of strips
   */
    public int getNumStrips(){
	return ((IndexedGeometryStripArrayWrapper)wrapped).getNumStrips();
    }

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
    public void setStripIndexCounts(int[] stripIndexCounts) {
        ((IndexedGeometryStripArrayWrapper)wrapped).setStripIndexCounts(stripIndexCounts);
    }

    /**
     * Gets a list of indexCounts for each strip. The list is
     * copied into the specified array. The array must be
     * large enough to hold all of the ints.
     * @param stripIndexCounts an array that will receive indexCounts
     */
    public void getStripIndexCounts(int[] stripIndexCounts) {
        ((IndexedGeometryStripArrayWrapper)wrapped).getStripIndexCounts(stripIndexCounts);
    }

    /**
     * This method is not supported for indexed geometry strip arrays.
     * The sum of the elements in the strip index counts array defines
     * the valid index count.
     *
     * @exception UnsupportedOperationException this method is not supported
     *
     * @since Java 3D 1.3
     */
    public void setValidIndexCount(int validIndexCount) {
	throw new UnsupportedOperationException();
    }

}
