/**
 * Project Looking Glass
 *
 * $RCSfile: TriangleFanArrayWrapper.java,v $
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
 * $Date: 2004-06-23 18:51:51 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.wrapper;


/**
 * The TriangleFanArray object draws an array of vertices as a set of
 * connected triangle fans.  An array of per-strip
 * vertex counts specifies where the separate strips (fans) appear
 * in the vertex array.  For every strip in the set,
 * each vertex, beginning with the third vertex in the array,
 * defines a triangle to be drawn using the current vertex,
 * the previous vertex and the first vertex.  This can be thought of
 * as a collection of convex polygons.
 */

public interface TriangleFanArrayWrapper extends GeometryStripArrayWrapper {

}
