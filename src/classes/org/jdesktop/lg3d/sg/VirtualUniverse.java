/**
 * Project Looking Glass
 *
 * $RCSfile: VirtualUniverse.java,v $
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
 * $Date: 2005-04-14 23:04:17 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg;

import java.util.HashSet;

/**
 * Unlike all the other sg classes this is not implemented as a direct
 * mapping of the Java 3D class. Looking Glass uses Java 3D's ConfiguredUniverse
 * directly so never instantiates an sg universe.
 *
 * This class is here to provide access to the proposed Java 3D 1.4 scene graph
 * change listeners.
 *
 * @author Paul
 */
public class VirtualUniverse {
    
    private static VirtualUniverse virtualUniverse = new VirtualUniverse();
    
    // Set of graph change listeners
    private HashSet<GraphChangeListener> graphChangeListeners = null;

    /** Creates a new instance of VirtualUniverse */
    VirtualUniverse() {
    }
    
    public static VirtualUniverse getVirtualUniverse() {
        return virtualUniverse;
    }
    
    /**
     * Add a graph change listener
     */
    public void addListener(GraphChangeListener listener) {
        if (graphChangeListeners==null)
            graphChangeListeners = new HashSet<GraphChangeListener>();
        
        graphChangeListeners.add(listener);
    }
    
    /**
     * Remove the specified graph change listener. If the listener is
     * not in the set of listeners this method simply returns.
     */
    public void removeListener(GraphChangeListener listener) {
        if (graphChangeListeners==null)
            return;
        
        graphChangeListeners.remove(listener);
    }
    
    public void generateGraphChangeEvent(boolean add, Object parent, BranchGroup child) {
        if (add)
            for(GraphChangeListener listener : graphChangeListeners)
                listener.addChild(parent,child);
        else
            for(GraphChangeListener listener : graphChangeListeners)
                listener.removeChild(parent,child);
    }
    
}
