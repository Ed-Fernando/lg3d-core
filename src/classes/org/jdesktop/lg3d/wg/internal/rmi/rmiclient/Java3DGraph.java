/**
 * Project Looking Glass
 *
 * $RCSfile: Java3DGraph.java,v $
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
 * $Date: 2005-01-20 22:06:17 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.internal.rmi.rmiclient;

import java.util.logging.Level;
import org.jdesktop.lg3d.wg.internal.wrapper.Java3DGraphWrapper;
import javax.vecmath.Vector3f;
import javax.vecmath.Matrix4f;

/**
 *
 * @author  paulby
 */
public class Java3DGraph extends Component3D implements Java3DGraphWrapper {
        
    protected void createWrapped() {
        throw new RuntimeException("Not Implemented, this should only be used with the SceneManager.");
    }
    
    public void addJ3dChild(javax.media.j3d.BranchGroup bg) {
        throw new RuntimeException("Not Implemented");
    }
    
}
