/**
 * Project Looking Glass
 *
 * $RCSfile: J3dLgBranchGroup.java,v $
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
 * $Revision: 1.8 $
 * $Date: 2007-04-18 01:11:28 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.internal.j3d.j3dnodes;

import java.util.ArrayList;
import org.jdesktop.lg3d.displayserver.NodeID;
import org.jdesktop.j3d.utils.scenegraph.traverser.TreeScan;
import org.jdesktop.j3d.utils.scenegraph.traverser.NodeChangeProcessor;
import com.sun.j3d.utils.picking.PickTool;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventConnector;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.MouseEvent3D;
import org.jdesktop.lg3d.wg.event.MouseButtonEvent3D;
import org.jdesktop.lg3d.wg.event.MouseWheelEvent3D;
import org.jdesktop.lg3d.wg.event.MouseEnteredEvent3D;
import org.jdesktop.lg3d.wg.event.MouseMotionEvent3D;
import org.jdesktop.lg3d.wg.event.MouseMovedEvent3D;
import org.jdesktop.lg3d.wg.event.MouseDraggedEvent3D;
import javax.media.j3d.Node;
import javax.media.j3d.Shape3D;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.RestrictedAccessException;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * The root class for all higher level looking glass components
 *
 * @author  Paul
 */
public class J3dLgBranchGroup extends javax.media.j3d.BranchGroup implements LgEventSource {
    
    protected static final Logger logger = Logger.getLogger("lg.wg");
    
    private ArrayList<LgEventListener> listeners;

    @SuppressWarnings("deprecation") // ignore warnings against usage of Morph
    private static Class[] targetsForSetCapabilities = {
        Shape3D.class, javax.media.j3d.Morph.class
    };
    
    private int mouseEventSourceMask = 0;
    private boolean isMouseEventEnabled = true;
    private boolean isMouseEventPropagatable = false;
    private boolean isKeyEventSource = false;
    
    private NodeID nodeID;

    /** Creates a new instance of LgBranchGroup */
    public J3dLgBranchGroup() {
        super();
        setCapability( javax.media.j3d.BranchGroup.ENABLE_PICK_REPORTING );
    }
    
    public void setMouseEventSource(Class eventClass, boolean enabled) {
        int mask = getMouseEventMask(eventClass);
        if (enabled) {
            mouseEventSourceMask |= mask;
        } else {
            mouseEventSourceMask &= ~mask;
        }
    }
    
    public boolean isMouseEventSource(Class eventClass) {
        return isMouseEventSource(getMouseEventMask(eventClass));
    }
    
    public void setMouseEventEnabled(boolean isMouseEventEnabled) {
        this.isMouseEventEnabled = isMouseEventEnabled;
    }
    
    public boolean isMouseEventEnabled() {
        return isMouseEventEnabled;
    }
    
    public void setMouseEventPropagatable(boolean isMouseEventPropagatable) {
        this.isMouseEventPropagatable = isMouseEventPropagatable;
    }
    
    public boolean isMouseEventPropagatable() {
        return isMouseEventPropagatable;
    }
    
    public void setKeyEventSource(boolean isKeyEventSource) {
        this.isKeyEventSource = isKeyEventSource;
    }
    
    public boolean isKeyEventSource() {
        return isKeyEventSource;
    }
    
    public void setNodeID( NodeID nodeID ) {
        this.nodeID = nodeID;
    }
    
    public NodeID getNodeID() {
        return nodeID;
    }
    
    public void setCapabilities() {
        setCapabilities(this);
    }
    
    /**
     * Register the specified listener to this component.
     *
     * Note events are only delivered to nodes which are live.
     *
     * @param listener LgEventListener the listener to add
     */
    public void addListener(LgEventListener listener) {
        // Attempt to solve events holding references to detached scene graph
        if (listeners==null)
            listeners = new ArrayList();
        listeners.add(listener);
        
//        if (isLive()) {
            LgEventConnector.getLgEventConnector().addListener(this, listener);
//        }
    }
    
    /**
     * Remove the specified listener from this component.
     *
     * @param listener LgEventListener the listener to remove
     */
    public void removeListener(LgEventListener listener) {
        // Attempt to solve events holding references to detached scene graph
        if (listeners==null) {
            logger.warning("Attempt to remove listener that was not added");
        } else {
            listeners.remove(listener);
        }
        
//        if (isLive()) {
            LgEventConnector.getLgEventConnector().removeListener(this, listener);
//        } 
    }
    
    @SuppressWarnings("deprecation")
    void setCapabilities(BranchGroup j3dBG) {
        TreeScan.findNode(j3dBG, 
	    new Class[]{ Node.class }, 
	    new NodeChangeProcessor() {
		public boolean changeNode(Node node) {
                    try {
                        if (node instanceof Shape3D ||
                            node instanceof javax.media.j3d.Morph)
                            PickTool.setCapabilities(node, PickTool.INTERSECT_COORD);
                        node.setCapability(Node.ALLOW_PARENT_READ);
                    } catch (RestrictedAccessException rae) {
                        // ignore - shared geometry which is already alive
                        // can cause this exception happen
                    }
                    return true;
		}
	    },
	    false, true);
    }
    
    /**
     * The followings are located here for now in order not to include in
     * the public API.  Maybe should be in the PickEngine.
     */
    private static final int BITMASK_MOUSE_BUTTON_EVENT_3D  = 0x01;
    private static final int BITMASK_MOUSE_WHEEL_EVENT_3D   = 0x02;
    private static final int BITMASK_MOUSE_ENTERED_EVENT_3D = 0x04;
    private static final int BITMASK_MOUSE_MOVED_EVENT_3D   = 0x10;
    private static final int BITMASK_MOUSE_DRAGGED_EVENT_3D = 0x20;
    private static final int BITMASK_MOUSE_MOTION_EVENT_3D  
        = BITMASK_MOUSE_MOVED_EVENT_3D 
        | BITMASK_MOUSE_DRAGGED_EVENT_3D;
    private static final int BITMASK_MOUSE_EVENT_3D
        = BITMASK_MOUSE_BUTTON_EVENT_3D
        | BITMASK_MOUSE_WHEEL_EVENT_3D
        | BITMASK_MOUSE_ENTERED_EVENT_3D
        | BITMASK_MOUSE_MOTION_EVENT_3D;
    private static HashMap<Class, Integer> mouseEventTable = new HashMap();
    static {
        mouseEventTable.put(MouseButtonEvent3D.class,  BITMASK_MOUSE_BUTTON_EVENT_3D);
        mouseEventTable.put(MouseWheelEvent3D.class,   BITMASK_MOUSE_WHEEL_EVENT_3D);
        mouseEventTable.put(MouseEnteredEvent3D.class, BITMASK_MOUSE_ENTERED_EVENT_3D);
        mouseEventTable.put(MouseMovedEvent3D.class,   BITMASK_MOUSE_MOVED_EVENT_3D);
        mouseEventTable.put(MouseDraggedEvent3D.class, BITMASK_MOUSE_DRAGGED_EVENT_3D);
        mouseEventTable.put(MouseMotionEvent3D.class,  BITMASK_MOUSE_MOTION_EVENT_3D);
        mouseEventTable.put(MouseEvent3D.class,        BITMASK_MOUSE_EVENT_3D);
    }
    
    /* to be used by PickEngine only */
    public static int getMouseEventMask(Class mouseEventClass) {
        int ret = mouseEventTable.get(mouseEventClass);
        assert(ret > 0);
        return ret;
    }
    
    /* to be used by PickEngine only */
    public boolean isMouseEventSource(int testMask) {
        return ((mouseEventSourceMask & testMask) >= testMask);
    }
    
    /* to be used by PickEngine only */
    public boolean isMouseEventSource() {
        return (mouseEventSourceMask > 0);
    }

    public void postEvent(LgEvent evt) {
        LgEventConnector.getLgEventConnector().postEvent(evt, this);
    }
}

