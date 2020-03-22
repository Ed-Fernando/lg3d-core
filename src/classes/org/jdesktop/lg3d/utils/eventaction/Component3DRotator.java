/**
 * Project Looking Glass
 *
 * $RCSfile: Component3DRotator.java,v $
 *
 * Copyright (c) 2006, Sun Microsystems, Inc., All Rights Reserved
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
 * $Date: 2006-09-14 16:06:45 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.eventaction;

import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Container3D;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.Component3DManualMoveEvent;
import org.jdesktop.lg3d.wg.event.InputEvent3D.ModifierId;
import org.jdesktop.lg3d.wg.event.MouseEvent3D.ButtonId;
import org.jdesktop.lg3d.utils.action.ActionFloat3;
import org.jdesktop.lg3d.utils.action.ActionBooleanFloat3;
import org.jdesktop.lg3d.utils.eventadapter.MouseDragDistanceAdapter;
import org.jdesktop.lg3d.utils.eventadapter.MousePressedEventAdapter;


/**
 * Utility class for associating <code>Component3D</code>s move stimulus with
 * rotation action
 */

public class Component3DRotator implements LgEventListener {
    private ListenerAggregator listeners;
    private Container3D container;
    
    /**
     * Builds a <code>Component3DRotator</code> with a <code>null</code> target
     * that listens to <code>BUTTON1</code>
     * events.
     */
    public Component3DRotator() {
        this(null, null);
    }
    
    /**
     * Builds a <code>Component3DRotator</code> that listens to
     * <code>BUTTON1</code> events.
     * @param target The object that is rotated on drag events.
     */
    public Component3DRotator(Component3D target) {
        this(target, null);
    }
    
    /** Builds a <code>Component3DRotator</code> with a <code>null</code> target.
     * @param modifier Either a <code>BUTTON<i>n</i></code> value to indicate
     * the button that triggers drag events, or a value indicating a modifier
     * key to be held down during a drag event.
     */
    public Component3DRotator(ModifierId modifier) {
        this(null, modifier);
    }
 
    /** Creates a <code>Component3DRotator</code>.
     * @param target The object that is rotated on drag events.
     * @param modifier Either a <code>BUTTON<i>n</i></code> value to indicate
     * the button that triggers drag events, or a value indicating a modifier
     * key to be held down during a drag event.
     */
    public Component3DRotator(Component3D target, ModifierId modifier) {
        RotatorAction rotatorAction = new RotatorAction(target);

        if (modifier == ModifierId.BUTTON1 || modifier == null) {
            listeners 
                = new ListenerAggregator(
                    new MousePressedEventAdapter(ButtonId.BUTTON1, null, rotatorAction),
                    new MouseDragDistanceAdapter(ButtonId.BUTTON1,
                                                 null,
                                                 rotatorAction));
        } else if (modifier == ModifierId.BUTTON2) {
            listeners 
                = new ListenerAggregator(
                    new MousePressedEventAdapter(ButtonId.BUTTON2, null, rotatorAction),
                    new MouseDragDistanceAdapter(ButtonId.BUTTON2,
                                                 null,
                                                 rotatorAction));
        } else if (modifier == ModifierId.BUTTON3) {
            listeners 
                = new ListenerAggregator(
                    new MousePressedEventAdapter(ButtonId.BUTTON3, null, rotatorAction),
                    new MouseDragDistanceAdapter(ButtonId.BUTTON3,
                                                 null,
                                                 rotatorAction));
        } else {
            listeners 
                = new ListenerAggregator(
                    new MousePressedEventAdapter(ButtonId.BUTTON1,
                                                 modifier,
                                                 rotatorAction),
                    new MouseDragDistanceAdapter(ButtonId.BUTTON1,
                                                 modifier,
                                                 rotatorAction));
        }
    }
    
    /** What does this do?
     * The container field doesn't seem to be used anywhere else.
     */
    public void setContainer(Container3D cont) {
        container = cont;
    }
    
    public void processEvent(LgEvent event) {
        listeners.processEvent(event);
    }
    
    public Class[] getTargetEventClasses() {
        return listeners.getTargetEventClasses();
    }
    
    private static class RotatorAction implements ActionFloat3, ActionBooleanFloat3 {
        private Component3D target;
        private AxisAngle4f aa4f = new AxisAngle4f();
        private Quat4f q4f = new Quat4f();
        private Matrix4f m4f = new Matrix4f();
        private Matrix4f tmpM4f = new Matrix4f();
        private Vector3f tmpV3f = new Vector3f();
        private Point3f tmpP3f = new Point3f();
        private float prevX, prevY;
        private float dist;
        private boolean firstTime = false;
        
        private RotatorAction(Component3D target) {
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
            tmpP3f.set(x, y, z);
            if (state) { // MOUSE_PRESSED
                prevX = 0;
                prevY = 0;
                firstTime = true;
                
                c3d.getTranslationTo(null, tmpV3f);
                tmpV3f.sub(tmpP3f);
                dist = tmpV3f.length();
                if (dist < 0.01f) {
                    dist = 0.01f;
                }
            } else { // MOUSE_RELEASED -- done with the move
                //
            }
            c3d.postEvent(new Component3DManualMoveEvent(state, tmpP3f));
        }
        
        public void performAction(LgEventSource source, float x, float y, float z) {
            // MOUSE_DRAGGED
            assert(source instanceof Component3D);
            Component3D c3d = target;
            if (c3d == null) {
                c3d = (Component3D)source;
            }
            
            // FIXME -- somehow need the following treadment in order to 
            // avoid jumpy angle change...
            if (firstTime) {
                firstTime = false;
                prevX = x;
                prevY = y;
                return;
            }
            
            float dx = (x - prevX);
            float dy = (y - prevY);
            
            dx /= dist;
            dy /= dist;
            prevX = x;
            prevY = y;
            
            // FIXME -- optimize the calculation later
            c3d.getFinalRotationAxis(tmpV3f);
            float a = c3d.getFinalRotationAngle();
            aa4f.set(tmpV3f, a);
            m4f.rotX(-dy);
            tmpM4f.rotY(dx);
            m4f.mul(tmpM4f);
            tmpM4f.set(aa4f);
            m4f.mul(tmpM4f);
            m4f.get(q4f);
            aa4f.set(q4f);
            
            c3d.setRotationAxis(aa4f.x, aa4f.y, aa4f.z);
            c3d.setRotationAngle(aa4f.angle);
        }
    }
}
