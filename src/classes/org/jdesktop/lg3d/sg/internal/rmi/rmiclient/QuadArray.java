/**
 * Project Looking Glass
 *
 * $RCSfile: QuadArray.java,v $
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
 * $Date: 2004-06-23 18:50:57 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.rmi.rmiclient;

import org.jdesktop.lg3d.sg.internal.wrapper.QuadArrayWrapper;

/**
 * The QuadArray object draws the array of vertices as individual
 * quadrilaterals.  Each group
 * of four vertices defines a quadrilateral to be drawn.
 */

public class QuadArray extends GeometryArray implements QuadArrayWrapper {

    QuadArray() {
    }

    /**
     * Constructs an empty QuadArray object with the specified
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
     * @exception IllegalArgumentException if vertexCount is less than 4
     * or vertexCount is <i>not</i> a multiple of 4
     */
    public QuadArray(int vertexCount, int vertexFormat) {
        try {
            remote = SceneGraphSetup.getSGObjectFactory().newQuadArray( vertexCount, vertexFormat );
            setRemote( remote );
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }

    /**
     * Constructs an empty QuadArray object with the specified
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
     * <code>TEXTURE_COORDINATE_3 or
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
     * @exception IllegalArgumentException if vertexCount is less than 4
     * or vertexCount is <i>not</i> a multiple of 4
     *
     * @since Java 3D 1.2
     */
    public QuadArray(int vertexCount,
		     int vertexFormat,
		     int texCoordSetCount,
		     int[] texCoordSetMap) {

        try {
            remote = SceneGraphSetup.getSGObjectFactory().newQuadArray( vertexCount, 
                                                    vertexFormat,
                                                    texCoordSetCount,
                                                    texCoordSetMap );
            setRemote( remote );
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }

    protected void createRemote() {
    }
}
