/**
 * Project Looking Glass
 *
 * $RCSfile: Geometry.java,v $
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
 * $Date: 2004-06-23 18:50:23 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg;

/**
 * Geometry is an abstract class that specifies the geometry
 * component information required by a Shape3D node. Geometry objects
 * describe both the geometry and topology of the Shape3D nodes that
 * reference them. Geometry objects consist of four generic geometric
 * types:<P>
 * <UL><LI>Compressed Geometry</LI>
 * <LI>GeometryArray</LI>
 * <LI>Raster</LI>
 * <LI>Text3D</LI>
 * </UL><P>
 * Each of these geometric types defines a visible object or set of
 * objects. A Geometry object is used as a component object of a Shape3D
 * leaf node.
 * 
 */

public abstract class Geometry extends NodeComponent {

    /**
     * Specifies that this Geometry allows intersect operation. 
     */
    public static final int
    ALLOW_INTERSECT = CapabilityBits.GEOMETRY_ALLOW_INTERSECT;

    /**
     * Constructs a new Geometry object.
     */
    public Geometry() {
    }
}
