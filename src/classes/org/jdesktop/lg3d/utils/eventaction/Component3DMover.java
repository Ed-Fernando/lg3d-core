/**
 * Project Looking Glass
 *
 * $RCSfile: Component3DMover.java,v $
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
 * $Revision: 1.11 $
 * $Date: 2006-08-14 23:13:29 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.eventaction;

import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Container3D;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.Component3DManualMoveEvent;
import org.jdesktop.lg3d.wg.event.InputEvent3D.ModifierId;
import org.jdesktop.lg3d.utils.action.ActionFloat3;
import org.jdesktop.lg3d.utils.action.ActionBooleanFloat3;
import org.jdesktop.lg3d.utils.action.ActionInt;
import org.jdesktop.lg3d.utils.eventadapter.MouseDragDistanceAdapter;
import org.jdesktop.lg3d.utils.eventadapter.MousePressedEventAdapter;
import org.jdesktop.lg3d.utils.eventadapter.MouseWheelEventAdapter;


/**
 * Utility class for associating Component3Ds move stimulus with move action
 */

public class Component3DMover implements LgEventListener {
    private ListenerAggregator listeners;
    private Container3D container;
    
    public Component3DMover() {
        this(null);
    }
    
    public Component3DMover(Component3D target) {
        this(target, true);
    }
    
    public Component3DMover(boolean allowZMove) {
        this(null, allowZMove);
    }
    
    public Component3DMover(Component3D target, boolean allowZMove) {
        XYMoverAction xyMoverAction = new XYMoverAction(target);
        listeners 
            = new ListenerAggregator(
                new MousePressedEventAdapter(xyMoverAction),
                new MouseDragDistanceAdapter(xyMoverAction));
        
        if (allowZMove) {
            ZMoverAction zMoverAction = new ZMoverAction(target);
            listeners.addListener(
                new MouseWheelEventAdapter(ModifierId.CTRL, zMoverAction));
        }
    }
    
    public void setContainer(Container3D cont) {
        container = cont;
    }
    
    public void processEvent(LgEvent event) {
        listeners.processEvent(event);
    }
    
    public Class[] getTargetEventClasses() {
        return listeners.getTargetEventClasses();
    }
    
    private class XYMoverAction implements ActionFloat3, ActionBooleanFloat3 {
        private Component3D target;
        private Vector3f origLoc = new Vector3f();
        private Vector3f tmpV3f = new Vector3f();
        private Point3f tmpP3f = new Point3f();
        private float yMax;
        private boolean compMoved;
        
        private XYMoverAction(Component3D target) {
            this.target = target;
        }
    
        public void performAction(LgEventSource source, boolean state, 
                float x, float y, float z) 
        {
            assert(source instanceof Component3D);
            Component3D c3d = target;
            if (c3d == null) {
                c3d = (Component3D)source;
            }
            if (state) { // MOUSE_PRESSED
                yMax = calcYMax(c3d);
                compMoved = false;
                c3d.getFinalTranslation(origLoc);
            } else { // MOUSE_RELEASED -- done with the move, rearrange
                if (compMoved) {
                    c3d.requestParentToRevalidate();
                }
            }
            tmpP3f.set(x, y, z);
            c3d.postEvent(new Component3DManualMoveEvent(state, tmpP3f));
        }
        
        public void performAction(LgEventSource source, float x, float y, float z) {
            // MOUSE_DRAGGED
            assert(source instanceof Component3D);
            Component3D c3d = target;
            if (c3d == null) {
                c3d = (Component3D)source;
            }
            float newX = origLoc.x + x;
            float newY = origLoc.y + y;
            float newZ = origLoc.z + z;
            
            if (!Float.isNaN(yMax) && newY > yMax) {
                newY = yMax;
            }
            c3d.setTranslation(newX, newY, newZ);
            compMoved = true;
        }
                
        private float calcYMax(Component3D comp) {
            if (container == null) {
                return Float.NaN;
            }
            if (comp instanceof Container3D) {
                Component3D deco = ((Container3D)comp).getDecoration();
                if (deco != null && deco.getPreferredSize(tmpV3f).y > 0.0f) {
                    // tmpV3f already holds deco's preferredSize
                } else {
                    comp.getPreferredSize(tmpV3f);
                }
            } else {
                comp.getPreferredSize(tmpV3f);
            }
            float h = tmpV3f.y * 0.5f;
            if (h < 0.0f || container.getPreferredSize(tmpV3f).y < 0.0f) {
                return Float.NaN;
            }
            return container.getPreferredSize(tmpV3f).y * 0.5f - h;
        }
    }
    
    private static class ZMoverAction implements ActionInt {
        private Component3D target;
        private Component3D thisTarget = null;
        private Vector3f tmpV3f = new Vector3f();
        
        private ZMoverAction(Component3D target) {
            this.target = target;
        }
        
        public void performAction(LgEventSource source, int clicks) {
            // MOUSE_WHEEL
            assert(source instanceof Component3D);
            Component3D c3d = target;
            if (c3d == null) {
                c3d = (Component3D)source;
            }
            
            c3d.getTranslation(tmpV3f);
            
            /* FIXME -- make hardcoded values configurable */
            tmpV3f.z -= 0.5f;
            if (clicks > 0) {
                tmpV3f.z *= 0.9f;
            } else if (clicks < 0) {
                tmpV3f.z *= 1.0f / 0.9f;
            }
            tmpV3f.z += 0.5f;
            
            if (tmpV3f.z > -2.0f && tmpV3f.z < 0.2f) {
                c3d.changeTranslation(tmpV3f.x, tmpV3f.y, tmpV3f.z, 100);
            }
        }
    }
}
