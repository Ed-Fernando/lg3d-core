/**
 * Project Looking Glass
 *
 * $RCSfile: LeafWrapper.java,v $
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
 * $Date: 2004-06-23 18:51:46 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.wrapper;

/**
 * The Leaf node is an abstract class for all scene graph nodes that
 * have no children.
 * Leaf nodes specify lights, geometry, and sounds. They specify special
 * linking and instancing capabilities for sharing scene graphs and
 * provide a view platform for positioning and orienting a view in the
 * virtual world.
 * <p>
 * NOTE: Applications should <i>not</i> extend this class directly.
 *
 * @version 	1.38, 02/04/01 14:56:32
 */

public interface LeafWrapper extends NodeWrapper {

}
