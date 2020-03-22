/**
 * Project Looking Glass
 *
 * $RCSfile: GraphChangeListener.java,v $
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
 * $Date: 2005-04-14 23:04:16 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg;

/**
 * This is the listener interface for monitoring scene graph structure 
 * changes. The only thing that can be changed at runtime that effects the
 * structure is the addition and removal of BranchGroups into the graph.
 *
 * This interface can be used to listen for BranchGroups being added to the
 * graph using addChild. The semantics are that this listener is called BEFORE
 * the BranchGroup is added, ie the child is NOT live.
 *
 * For removeChild the semantics are that the listener is called AFTER the has
 * been removed, so again the child is NOT live.
 *
 * Operations such as moveTo and setChild will result in two calls to this 
 * interface. Firstly removeChild will be called, then addChild.
 *
 * @author Paul
 */
public interface GraphChangeListener {
    
    /**
     * Called just before a child BranchGroup is added to the parent.
     * The parent object can either be a Locale or a Group
     */
    public void addChild(Object parent, BranchGroup child);
    
    /**
     * Called just after a child BranchGroup is removed from the parent.
     * The parent object can either be a Locale or a Group
     */
    public void removeChild(Object parent, BranchGroup child);
    
}
