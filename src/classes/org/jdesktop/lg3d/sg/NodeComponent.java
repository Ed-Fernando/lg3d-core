/**
 * Project Looking Glass
 *
 * $RCSfile: NodeComponent.java,v $
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
 * $Date: 2004-06-23 18:50:27 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg;
import java.util.Hashtable;

/**
 * NodeComponent is a common superclass for all scene graph node
 * component objects such as: Geometry, Appearance, Material, Texture, etc.
 */
public abstract class NodeComponent extends SceneGraphObject {

    /**
     * Constructs a NodeComponent object with default parameters.
     * The default values are as follows:
     * <ul>
     * duplicate on clone tree : false<br>
     * </ul>
     */
    public NodeComponent() {
    }

}
