/**
 * Project Looking Glass
 *
 * $RCSfile: WidgetManager.java,v $
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
 * $Date: 2006-08-14 23:13:23 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.GraphStructureChangeListener;
import javax.media.j3d.Node;
import javax.media.j3d.VirtualUniverse;
import org.jdesktop.j3d.utils.scenegraph.traverser.ProcessNodeInterface;
import org.jdesktop.j3d.utils.scenegraph.traverser.TreeScan;

import org.jdesktop.lg3d.wg.internal.j3d.j3dnodes.J3dComponent3D;
import org.jdesktop.lg3d.wg.internal.wrapper.Component3DWrapper;

/**
 * This is an implementation private class. T
 * his should NEVER be called by user code
 *
 * @author paulby
 */
public class WidgetManager {
    
    private static WidgetManager widgetManager = null;
    
    /** Creates a new instance of WidgetManager 
     *
     * The widget manager notifies Components by calling setLive
     * when they are added or removed from the live graph. The component is
     * then responsible for cleaning up any resources so that it can be gc'ed 
     * if it goes out of scope. Care should be taked when cleaning up resources
     * not to loose too much information as the component can be made live again.
     */
    public WidgetManager(VirtualUniverse virtualUniverse) {
        widgetManager = this;
        virtualUniverse.addGraphStructureChangeListener(new ChangeListener());
    }
    
    private static class ChangeListener implements GraphStructureChangeListener {
        public void branchGroupAdded(Object parent, BranchGroup branchGroup) {
            TreeScan.findNode(branchGroup, J3dComponent3D.class, new ProcessNodeInterface() {
                public boolean processNode(Node node) {
                    if (node instanceof J3dComponent3D) {
                        //System.out.println("Node "+node);
                        if (node.getUserData()==null)
                            return true;
                        Component3DWrapper compW = (Component3DWrapper)((J3dComponent3D)node).getUserData();
                        //System.out.println("CompW "+compW);
                        Object comp = compW.getUserData();
                        if (comp instanceof Component3D) {
                            ((Component3D)comp).setLive(true);
                        }
                    }
                    return true;    // Continue scanning
                }
            }, false, true);
        }
        
        public void branchGroupRemoved(Object parent, BranchGroup branchGroup) {
            TreeScan.findNode(branchGroup, J3dComponent3D.class, new ProcessNodeInterface() {
                public boolean processNode(Node node) {
                    if (node instanceof J3dComponent3D) {
                        //System.out.println("Node "+node);
                        if (node.getUserData()==null)
                            return true;
                        Component3DWrapper compW = (Component3DWrapper)((J3dComponent3D)node).getUserData();
                        //System.out.println("CompW "+compW);
                        Object comp = compW.getUserData();
                        if (comp instanceof Component3D) {
                            //System.out.println("Comp "+comp);
                            ((Component3D)comp).setLive(false);
                        }
                    }
                    return true;    // Continue scanning
                }
            }, false, true);
        }

        public void branchGroupMoved(Object oldParent, Object newParent, BranchGroup branchGroup) {
        }
    }
    
}
