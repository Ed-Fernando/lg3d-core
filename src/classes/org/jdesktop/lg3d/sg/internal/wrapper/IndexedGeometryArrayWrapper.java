/**
 * Project Looking Glass
 *
 * $RCSfile: IndexedGeometryArrayWrapper.java,v $
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
 * $Date: 2004-06-23 18:51:44 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.wrapper;

import javax.vecmath.*;

/**
 * The IndexedGeometryArray object contains separate integer arrays
 * that index into the arrays of positional coordinates, colors,
 * normals, and texture coordinates.  These index arrays specify how
 * vertices are connected to form geometry primitives.  This class is
 * extended to create the various indexed primitive types (e.g.,
 * lines, triangle strips, etc.).
 */

public interface IndexedGeometryArrayWrapper extends GeometryArrayWrapper {

    /**
    * Gets number of indices for this IndexedGeometryArray.
    * @return indexCount the number of indices
    */
    public int getIndexCount();

    /**
     * Sets the valid index count for this IndexedGeometryArray object.
     * This count specifies the number of indexed vertices actually used
     * in rendering or other operations such as picking and collision.
     * This attribute is initialized to <code>indexCount</code>.
     *
     * @param validIndexCount the new valid index count.
     *
     * @exception CapabilityNotSetException if the appropriate capability is
     * not set and this object is part of a live or compiled scene graph
     *
     * @exception IllegalArgumentException if either of the following is true:
     * <ul>
     * <code>validIndexCount < 0</code>, or<br>
     * <code>initialIndexIndex + validIndexCount > indexCount</code><br>
     * </ul>
     *
     * @exception ArrayIndexOutOfBoundsException if any element in the range
     * <code>[initialIndexIndex, initialIndexIndex+validIndexCount-1]</code>
     * in the index array associated with any of the enabled vertex
     * components (coord, color, normal, texcoord) is out of range.
     * An element is out of range if it is less than 0 or is greater
     * than or equal to the number of vertices actually defined for
     * the particular component's array.
     *
     */
    public void setValidIndexCount(int validIndexCount);

    /**
     * Gets the valid index count for this IndexedGeometryArray
     * object.  For geometry strip primitives (subclasses of
     * IndexedGeometryStripArray), the valid index count is defined
     * to be the sum of the stripIndexCounts array.
     *
     * @return the current valid index count
     *
     * @exception CapabilityNotSetException if the appropriate capability is
     * not set and this object is part of a live or compiled scene graph
     *
     */
    public int getValidIndexCount();

    /**
     * Sets the initial index index for this IndexedGeometryArray object.
     * This index specifies the first index within this indexed geometry
     * array that is actually used in rendering or other operations
     * such as picking and collision.  This attribute is initialized
     * to 0.
     *
     * @param initialIndexIndex the new initial index index.
     * @exception CapabilityNotSetException if the appropriate capability is
     * not set and this object is part of a live or compiled scene graph
     * @exception IllegalArgumentException if either of the following is true:
     * <ul>
     * <code>initialIndexIndex < 0</code>, or<br>
     * <code>initialIndexIndex + validIndexCount > indexCount</code><br>
     * </ul>
     *
     * @exception ArrayIndexOutOfBoundsException if any element in the range
     * <code>[initialIndexIndex, initialIndexIndex+validIndexCount-1]</code>
     * in the index array associated with any of the enabled vertex
     * components (coord, color, normal, texcoord) is out of range.
     * An element is out of range if it is less than 0 or is greater
     * than or equal to the number of vertices actually defined for
     * the particular component's array.
     *
     */
    public void setInitialIndexIndex(int initialIndexIndex);

    /**
     * Gets the initial index index for this IndexedGeometryArray object.
     * @return the current initial index index
     * @exception CapabilityNotSetException if the appropriate capability is
     * not set and this object is part of a live or compiled scene graph
     *
     */
    public int getInitialIndexIndex();

    /**
     * This method is not supported for indexed geometry arrays.
     * Indexed primitives use an array of indices to determine how
     * to access the vertex array.
     * The initialIndexIndex attribute can be used to set the starting
     * index within the index arrays.
     *
     * @exception UnsupportedOperationException this method is not supported
     *
     */
    public void setInitialVertexIndex(int initialVertexIndex);

    /**
     * This method is not supported for indexed geometry arrays.
     * Indexed primitives use an array of indices to determine how
     * to access the vertex array.
     *
     * @exception UnsupportedOperationException this method is not supported
     *
     */
    public void setInitialCoordIndex(int initialCoordIndex);

    /**
     * This method is not supported for indexed geometry arrays.
     * Indexed primitives use an array of indices to determine how
     * to access the vertex array.
     *
     * @exception UnsupportedOperationException this method is not supported
     *
     */
    public void setInitialColorIndex(int initialColorIndex);

    /**
     * This method is not supported for indexed geometry arrays.
     * Indexed primitives use an array of indices to determine how
     * to access the vertex array.
     *
     * @exception UnsupportedOperationException this method is not supported
     *
     */
    public void setInitialNormalIndex(int initialNormalIndex);

    /**
     * This method is not supported for indexed geometry arrays.
     * Indexed primitives use an array of indices to determine how
     * to access the vertex array.
     *
     * @exception UnsupportedOperationException this method is not supported
     *
     */
    public void setInitialTexCoordIndex(int texCoordSet,
					int initialTexCoordIndex);

    /**
     * This method is not supported for indexed geometry arrays.
     * Indexed primitives use an array of indices to determine how
     * to access the vertex array.
     * The validIndexCount attribute can be used to set the number of
     * valid indexed vertices rendered.
     *
     * @exception UnsupportedOperationException this method is not supported
     *
     */
    public void setValidVertexCount(int validVertexCount);


    /**
     * Sets the coordinate index associated with the vertex at
     * the specified index for this object.
     * @param index the vertex index
     * @param coordinateIndex the new coordinate index
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @exception ArrayIndexOutOfBoundsException if index is less than 0
     * or is greater than or equal to indexCount
     *
     * @exception ArrayIndexOutOfBoundsException if index is in the range
     * <code>[initialIndexIndex, initialIndexIndex+validIndexCount-1]</code>
     * and the specified coordinateIndex is out of range.  The
     * coordinateIndex is out of range if it is less than 0 or is
     * greater than or equal to the number of vertices actually
     * defined for the coordinate array.
     */
  public void setCoordinateIndex(int index, int coordinateIndex);

    /**
     * Sets the coordinate indices associated with the vertices starting at
     * the specified index for this object.
     * @param index the vertex index
     * @param coordinateIndices an array of coordinate indices
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @exception ArrayIndexOutOfBoundsException if index is less than 0
     * or is greater than or equal to indexCount
     *
     * @exception ArrayIndexOutOfBoundsException if any element of the
     * coordinateIndices array whose destination position is in the range
     * <code>[initialIndexIndex, initialIndexIndex+validIndexCount-1]</code>
     * is out of range.  An element is out of range if it is less than 0
     * or is greater than or equal to the number of vertices actually
     * defined for the coordinate array.
     */
  public void setCoordinateIndices(int index, int coordinateIndices[]);

    /**
     * Sets the color index associated with the vertex at
     * the specified index for this object.
     * @param index the vertex index
     * @param colorIndex the new color index
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @exception ArrayIndexOutOfBoundsException if index is less than 0
     * or is greater than or equal to indexCount
     *
     * @exception ArrayIndexOutOfBoundsException if index is in the range
     * <code>[initialIndexIndex, initialIndexIndex+validIndexCount-1]</code>
     * and the specified colorIndex is out of range.  The
     * colorIndex is out of range if it is less than 0 or is
     * greater than or equal to the number of vertices actually
     * defined for the color array.
     */
  public void setColorIndex(int index, int colorIndex);

    /**
     * Sets the color indices associated with the vertices starting at
     * the specified index for this object.
     * @param index the vertex index
     * @param colorIndices an array of color indices
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @exception ArrayIndexOutOfBoundsException if index is less than 0
     * or is greater than or equal to indexCount
     *
     * @exception ArrayIndexOutOfBoundsException if any element of the
     * colorIndices array whose destination position is in the range
     * <code>[initialIndexIndex, initialIndexIndex+validIndexCount-1]</code>
     * is out of range.  An element is out of range if it is less than 0
     * or is greater than or equal to the number of vertices actually
     * defined for the color array.
     */
  public void setColorIndices(int index, int colorIndices[]);

    /**
     * Sets the normal index associated with the vertex at
     * the specified index for this object.
     * @param index the vertex index
     * @param normalIndex the new normal index
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @exception ArrayIndexOutOfBoundsException if index is less than 0
     * or is greater than or equal to indexCount
     *
     * @exception ArrayIndexOutOfBoundsException if index is in the range
     * <code>[initialIndexIndex, initialIndexIndex+validIndexCount-1]</code>
     * and the specified normalIndex is out of range.  The
     * normalIndex is out of range if it is less than 0 or is
     * greater than or equal to the number of vertices actually
     * defined for the normal array.
     */
  public void setNormalIndex(int index, int normalIndex);

    /**
     * Sets the normal indices associated with the vertices starting at
     * the specified index for this object.
     * @param index the vertex index
     * @param normalIndices an array of normal indices
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @exception ArrayIndexOutOfBoundsException if index is less than 0
     * or is greater than or equal to indexCount
     *
     * @exception ArrayIndexOutOfBoundsException if any element of the
     * normalIndices array whose destination position is in the range
     * <code>[initialIndexIndex, initialIndexIndex+validIndexCount-1]</code>
     * is out of range.  An element is out of range if it is less than 0
     * or is greater than or equal to the number of vertices actually
     * defined for the normal array.
     */
  public void setNormalIndices(int index, int normalIndices[]);

    /**
     * Sets the texture coordinate index associated with the vertex at
     * the specified index in the specified texture coordinate set
     * for this object.
     *
     * @param texCoordSet texture coordinate set in this geometry array
     * @param index the vertex index
     * @param texCoordIndex the new texture coordinate index
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @exception ArrayIndexOutOfBoundsException if neither of the
     * <code>TEXTURE_COORDINATE</code> bits are set in the
     * <code>vertexFormat</code> or if the index or
     * texCoordSet is out of range.
     *
     * @exception ArrayIndexOutOfBoundsException if index is in the range
     * <code>[initialIndexIndex, initialIndexIndex+validIndexCount-1]</code>
     * and the specified texCoordIndex is out of range.  The
     * texCoordIndex is out of range if it is less than 0 or is
     * greater than or equal to the number of vertices actually
     * defined for the texture coordinate array.
     *
     */
    public void setTextureCoordinateIndex(int texCoordSet,
					  int index,
					  int texCoordIndex);

    /**
     * Sets the texture coordinate indices associated with the vertices
     * starting at the specified index in the specified texture coordinate set
     * for this object.
     *
     * @param texCoordSet texture coordinate set in this geometry array
     * @param index the vertex index
     * @param texCoordIndices an array of texture coordinate indices
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @exception ArrayIndexOutOfBoundsException if neither of the
     * <code>TEXTURE_COORDINATE</code> bits are set in the
     * <code>vertexFormat</code> or if the index or
     * texCoordSet is out of range.
     *
     * @exception ArrayIndexOutOfBoundsException if any element of the
     * texCoordIndices array whose destination position is in the range
     * <code>[initialIndexIndex, initialIndexIndex+validIndexCount-1]</code>
     * is out of range.  An element is out of range if it is less than 0
     * or is greater than or equal to the number of vertices actually
     * defined for the texture coordinate array.
     *
     */
    public void setTextureCoordinateIndices(int texCoordSet,
					    int index,
					    int texCoordIndices[]);

  /**
   * Retrieves the coordinate index associated with the vertex at
   * the specified index for this object.
   * @param index the vertex index
   * @return the coordinate index
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
   */
  public int getCoordinateIndex(int index);

  /**
   * Retrieves the coordinate indices associated with the vertices starting at
   * the specified index for this object.
   * @param index the vertex index
   * @param coordinateIndices array that will receive the coordinate indices
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
   */
  public void getCoordinateIndices(int index, int coordinateIndices[]);

  /**
   * Retrieves the color index associated with the vertex at
   * the specified index for this object.
   * @param index the vertex index
   * @return the color index
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
   */
  public int getColorIndex(int index);

  /**
   * Retrieves the color indices associated with the vertices starting at
   * the specified index for this object. The color indicies are
   * copied into the specified array. The array must be large enough
   * to hold all of the indices.
   * @param index the vertex index
   * @param colorIndices array that will receive the color indices
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
   */
  public void getColorIndices(int index, int colorIndices[]);

  /**
   * Retrieves the normal index associated with the vertex at
   * the specified index for this object.
   * @param index the vertex index
   * @return the normal index
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
   */
  public int getNormalIndex(int index);

  /**
   * Retrieves the normal indices associated with the vertices starting at
   * the specified index for this object. The normal indicies are
   * copied into the specified array. The array must be large enough
   * to hold all of the normal indicies.
   * 
   * @param index the vertex index
   * @param normalIndices array that will receive the normal indices
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
   */
  public void getNormalIndices(int index, int normalIndices[]);

    /**
     * Retrieves the texture coordinate index associated with the vertex at
     * the specified index in the specified texture coordinate set
     * for this object.
     *
     * @param texCoordSet texture coordinate set in this geometry array
     * @param index the vertex index
     *
     * @return the texture coordinate index
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @exception ArrayIndexOutOfBoundsException if neither of the
     * <code>TEXTURE_COORDINATE</code> bits are set in the
     * <code>vertexFormat</code> or if the index or
     * texCoordSet is out of range.
     *
     */
    public int getTextureCoordinateIndex(int texCoordSet, int index);


    /**
     * Retrieves the texture coordinate indices associated with the vertices
     * starting at the specified index in the specified texture coordinate set
     * for this object. The texture
     * coordinate indices are copied into the specified array. The array
     * must be large enough to hold all of the indices.
     *
     * @param texCoordSet texture coordinate set in this geometry array
     * @param index the vertex index
     * @param texCoordIndices array that will receive the texture coordinate 
     * indices
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @exception ArrayIndexOutOfBoundsException if neither of the
     * <code>TEXTURE_COORDINATE</code> bits are set in the
     * <code>vertexFormat</code> or if the index or
     * texCoordSet is out of range.
     *
     */
    public void getTextureCoordinateIndices(int texCoordSet,
					    int index,
					    int texCoordIndices[]);

}
