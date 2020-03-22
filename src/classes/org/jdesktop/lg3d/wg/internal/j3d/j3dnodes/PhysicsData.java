/**
 * Project Looking Glass
 *
 * $RCSfile: PhysicsData.java,v $
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
 * $Date: 2004-09-10 23:29:57 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.internal.j3d.j3dnodes;

import org.jdesktop.lg3d.wg.internal.j3d.j3dnodes.*;

/**
 * Prototype for adding physics to Component3Ds for layout
 *
 * @author  paulby
 */
public abstract class PhysicsData { 
    
    /**
     * Called by Component3D when the component is moved
     */
    abstract void set( TransformOpGroup trans );
    
    /**
     * Set the size of the box for collision detection etc.
     *
     * Only boxes are supported at the moment
     */
    abstract void setBoxSize( float width, float height, float depth );
}
