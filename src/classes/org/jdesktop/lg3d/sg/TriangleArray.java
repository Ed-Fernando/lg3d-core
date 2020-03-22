/**
 * Project Looking Glass
 *
 * $RCSfile: TriangleArray.java,v $
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
 * $Date: 2005-01-20 22:05:40 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg;

/**
 * The TriangleArray object draws the array of vertices as individual
 * triangles.  Each group
 * of three vertices defines a triangle to be drawn.
 */

public class TriangleArray extends GeometryArray {

    // non-public, no parameter constructor
    TriangleArray() {}

    /**
     * Constructs an empty TriangleArray object with the specified
     * number of vertices, and vertex format.
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
     * @exception IllegalArgumentException if vertexCount is less than 3
     * or vertexCount is <i>not</i> a multiple of 3
     */
    public TriangleArray(int vertexCount, int vertexFormat) {
        assert(this.getClass()==TriangleArray.class);
        wrapped = instantiate( SceneGraphSetup.getWrapperPackage()+"TriangleArray", new Class[] {Integer.TYPE, Integer.TYPE}, new Object[] { new Integer(vertexCount), new Integer( vertexFormat ) } );
    }

    /**
     * Constructs an empty TriangleArray object with the specified
     * number of vertices, and vertex format, number of texture coordinate
     * sets, and texture coordinate mapping array.
     *
     * @param vertexCount the number of vertex elements in this array<p>
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
     * <code>texCoordSetMap</code> array is not used.
     *
     * @exception IllegalArgumentException if vertexCount is less than 3
     * or vertexCount is <i>not</i> a multiple of 3
     *
     * @since Java 3D 1.2
     */
    public TriangleArray(int vertexCount,
			 int vertexFormat,
			 int texCoordSetCount,
			 int[] texCoordSetMap) {
        assert(this.getClass()==TriangleArray.class);
        wrapped = instantiate( SceneGraphSetup.getWrapperPackage()+"TriangleArray",
                               new Class[] { Integer.TYPE,
                                             Integer.TYPE,
                                             Integer.TYPE,
                                             int[].class },
                               new Object[] { new Integer(vertexCount), 
                                              new Integer(vertexFormat),
                                              new Integer(texCoordSetCount),
                                              texCoordSetMap });
    }
    
    protected void createWrapped() {
    }
}
