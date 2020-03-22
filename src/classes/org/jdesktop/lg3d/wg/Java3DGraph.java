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
 * $Revision: 1.3 $
 * $Date: 2005-04-14 23:05:01 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg;

import java.util.Enumeration;
import org.jdesktop.lg3d.sg.Node;
import org.jdesktop.lg3d.wg.internal.wrapper.Java3DGraphWrapper;

/**
 *
 * Allows a Java3D branchgroup to be added to the LG scenegraph.
 *
 * This is only valid for apps running within the display server.
 *
 * THIS IS EXPERIMENTAL AND MAY BE REMOVED FROM THE FINAL API
 *
 * @author  paulby
 */
public class Java3DGraph extends Component3D {
    /**
     * This is not supported
     *
     * Throws RuntimeException("Invalid Operation")
     */
    public void addChild( Node n ) {
        throw new RuntimeException("Invalid operation, use addJ3dChild");                
    }

    /**
     * Add this branchgroup, the necessary capabilities for picking will
     * be set automatically.
     */
    public void addJ3dChild( javax.media.j3d.BranchGroup bg ) {
        ((Java3DGraphWrapper)wrapped).addJ3dChild(bg);
    }
    
    /**
     * This will always return null
     */
    public Node getChild(int index) {
        return null;    
    }
    
    /**
     * This will always return null
     */
    public Enumeration getAllChildren() {
        return null;       
    }
    
    /**
     * Always returns 0 children
     */
    public int numChildren() {
        return 0;
        
    }
    
    // TODO overload all group methods to make this a leaf
    
    /**
     * Implementation detail, do not call from user code.
     * May be removed in future.
     */
    protected void createWrapped() {
        wrapped = instantiate("Java3DGraph");
    }
}
