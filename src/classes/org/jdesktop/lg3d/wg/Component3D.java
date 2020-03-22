/**
 * Project Looking Glass
 *
 * $RCSfile: Component3D.java,v $
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
 * $Revision: 1.23 $
 * $Date: 2006-09-20 00:30:31 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.sg.Node;
import org.jdesktop.lg3d.sg.internal.wrapper.NodeWrapper;
import org.jdesktop.lg3d.sg.internal.wrapper.BranchGroupWrapper;
import org.jdesktop.lg3d.wg.internal.wrapper.Component3DWrapper;
import org.jdesktop.lg3d.wg.internal.wrapper.Cursor3DWrapper;
import org.jdesktop.lg3d.displayserver.AppConnectorPrivate;
import org.jdesktop.lg3d.displayserver.LgNodeManager;
import org.jdesktop.lg3d.displayserver.NodeID;
import org.jdesktop.lg3d.sg.Transform3D;
import org.jdesktop.lg3d.wg.internal.wrapper.LgBranchGroupWrapper;
import org.jdesktop.lg3d.sg.BranchGroup;
import org.jdesktop.lg3d.wg.event.LgEventConnector;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.wg.event.LgEvent;



/**
 * Component3D object is a top level object for widget development.
 *
 * It supports visual rendering by adding scene graph node children 
 * and event handling.
 */
public class Component3D extends BranchGroup implements LgEventSource {
    
    protected static final Logger logger = Logger.getLogger("lg.wg");
    
    private NodeID nodeID;
    
    Component3DAnimation c3dAnimation;
    
    TransparencyManager transparencyManager;
    
    /** List of all listeners for this Component3D. */
    private ArrayList<LgEventListener> listeners = null;
    
    /**
     * Creates a new instance of Component3D.
     * 
     * By default, it is visible (i.e. isVisible() returns 'ture')
     * and it doesn't have any asscociated cursor.
     */
    public Component3D() {
	this(true);
    }

    /**
     * This constructor is introduced in order to aoivd possible flashing
     * of a Frame3D object, whose default visibility is false, whereas 
     * Component3D's is true.  Thus, doing setVisible(true) in the constructor
     * of Frame3D can results in a flash of the Frame3D object.
     */
    Component3D(boolean visible) {
        nodeID = AppConnectorPrivate.getAppConnector().getNodeID();
        LgNodeManager.getLgNodeManager().addNode( this );
        ((LgBranchGroupWrapper)wrapped).setNodeID( nodeID );
        
        // set the default animation
        setAnimation(new NullFrame3DAnimation());
	setVisible(visible);
    }
    
    /**
     * Post an event into the system with this Component3D object 
     * as the source of the event.
     *
     * Note events are only delivered to nodes which are live.
     */
    public void postEvent(LgEvent event) {
        AppConnectorPrivate.getAppConnector().postEvent(event, this);
    }
    
    /**
     * If true this component can generate mouse events
     *
     * When a mouse event is captured by the system it searches the scene graph path
     * for the first Component3D that can generate the event. The event is then
     * posted with the component3D as the source.
     * The eventClass argument specifies the kind of mouse event to check
     * if this component is sensitive for.
     */
    public boolean isMouseEventSource(Class eventClass) {
        return ((LgBranchGroupWrapper)wrapped).isMouseEventSource(eventClass);
    }
    
    /**
     * If true this component can generate mouse events
     *
     * This method should never be called directly by users, instead use
     * setMouseEventSource in Component3D
     *
     * When a mouse event is captured by the system it searches the scene graph path
     * for the first Component3D that can generate the event. The event is then
     * posted with the component3D as the source.
     * The eventClass argument specifies the kind of mouse event this component
     * is sensitive for.
     */
    public void setMouseEventSource(Class eventClass, boolean enabled) {
        ((LgBranchGroupWrapper)wrapped).setMouseEventSource(eventClass, enabled);
    }
    
    /**
     * If false this component and the branch graph under this component will
     * not generate any mouse events.  The default is true.
     */
    public void setMouseEventEnabled(boolean enabled) {
        ((LgBranchGroupWrapper)wrapped).setMouseEventEnabled(enabled);
    }
    
    /**
     * Check if mouse events from this branch graph is enabled.
     */
    public boolean isMouseEventEnabled() {
        return ((LgBranchGroupWrapper)wrapped).isMouseEventEnabled();
    }
    
    /**
     * If true, mouse events originated from this object get propagated
     * toward the components resides higher in the scene graph.
     * The default is false.
     */
    public void setMouseEventPropagatable(boolean enabled) {
        ((LgBranchGroupWrapper)wrapped).setMouseEventPropagatable(enabled);
    }
    
    /**
     * Check if mouse events from this object is propagatable.
     */
    public boolean isMouseEventPropagatable() {
        return ((LgBranchGroupWrapper)wrapped).isMouseEventPropagatable();
    }
    
    /**
     * If true this component can generate key events.
     */
    public boolean isKeyEventSource() {
        return ((LgBranchGroupWrapper)wrapped).isKeyEventSource();
    }
    
    /**
     * If true this component can generate mouse events
     */
    public void setKeyEventSource(boolean keyEventSource) {
        ((LgBranchGroupWrapper)wrapped).setKeyEventSource(keyEventSource);
    }
    
    /**
     * Sets the capabilities of this BranchGroup and all its children correctly.
     */
    void setCapabilities() {
        ((LgBranchGroupWrapper)wrapped).setCapabilities();
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
        
        if (isLive()) {
            LgEventConnector.getLgEventConnector().addListener(this, listener);
        }
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
        
        if (isLive()) {
            LgEventConnector.getLgEventConnector().removeListener(this, listener);
        } 
    }
    
    /**
     * Sets a cursor associated with this component.
     */
    public void setCursor(Cursor3D cursor) {
        if (cursor == null) {
            ((Component3DWrapper)wrapped).setCursor(null);
        } else {
            ((Component3DWrapper)wrapped).setCursor((Cursor3DWrapper)cursor.getWrapped());
        }
    }
    
    /**
     * Returns the cursor associated with this component.
     */
    public Cursor3D getCursor() {
        Cursor3DWrapper cursor = ((Component3DWrapper)wrapped).getCursor();
        if (cursor == null) {
            return null;
        } else {
            return (Cursor3D)cursor.getUserData();
        }
    }

    /**
     * Sets the preferred size of this component.
     *
     * The size is merely a hint for layout maanger
     * and no default action will be taken except storing the information. 
     *
     * REMINDER -- use of BoundingBox?
     * REMINDER -- associating this with setCollisionBounds()?
     * @param preferredSize the preferred size
     */
    public void setPreferredSize(Vector3f preferredSize) {
        ((Component3DWrapper)wrapped).setSizeHint(preferredSize.x, preferredSize.y, preferredSize.z);
    }
    
    /**
     * get the preferred size of the Component3D
     * @param ret the Vector3f to store the size in (cannot be null)
     * @return the preferred size (for chaining)
     */
    public Vector3f getPreferredSize(Vector3f ret) {
        if (ret == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
        return ((Component3DWrapper)wrapped).getSizeHint(ret);
    }
    
    /**
     * Shows or hides this component depending on the value of parameter 
     * 'appearance'.
     */
    void setVisibleInternal(boolean visible) {
	((Component3DWrapper)wrapped).setVisible(visible);
    }
    
    /**
     * Shows or hides this component depending on the value of parameter 
     * 'visible'.
     */
    boolean isVisibleInternal() {
	return ((Component3DWrapper)wrapped).isVisible();
    }
    
    /**
     * Shows or hides this component depending on the value of parameter 
     * 'appearance'.
     */
    public void setVisible(boolean visible) {
	changeVisible(visible, 0);
    }
    
    /**
     * Shows or hides this component depending on the value of parameter 
     * 'visible'.
     */
    public boolean isVisible() {
	assert(c3dAnimation != null);
        return c3dAnimation.isVisible();
    }
    
    /**
     * Shows or hides this component depending on the value of parameter 
     * 'visible'.
     */
    public boolean isFinalVisible() {
	assert(c3dAnimation != null);
        return c3dAnimation.isFinalVisible();
    }
    
    /**
     * Requests to shows or hides this component depending on the value 
     * of parameter 'visible'.
     */
    public void changeVisible(boolean visible) {
        assert(c3dAnimation != null);
        changeVisible(visible, c3dAnimation.getDefaultVisibleDuration());
    }
    
    /**
     * Requests to shows or hides this component depending on the value 
     * of parameter 'visible' within the specified duration.
     */
    public void changeVisible(boolean visible, int duration) {
        assert(c3dAnimation != null);
        
        boolean prevVisible = isFinalVisible();
        c3dAnimation.changeVisible(visible, duration);
        // do container layout after making the change
        if (prevVisible != visible) {
            requestParentToRevalidate();
        }
    }
    
    private void setCapabilitiesIfNeeded(Node child) {
        // Invoke setCapabilities() automatically when adding a non-live child 
        // to a live parent (this).  This may results in redundant invocations,
        // but it won't cause a performance issue since the cost is minor
        // compared to adding a BranchGraph to a scenegraph. 
        // We could have set capabilities in the constructors of
        // Shape3D and Morph classes, but didn't do it in order to keep
        // behavioral compatibility with those of Java 3D.  We'll reconsider
        // this approach if the current approach resulted in performance hit.
        //
        // FIXME -- somehow adding "&& isLive()" misses some condition to
        // set the capabilities.
        if (child instanceof Component3D /*&& isLive()*/ && !child.isLive()) {
            ((Component3D)child).setCapabilities();
        }
    }
    
    /**
     * Appends the specified Component3D object to this component's 
     * list of children.
     *
     * @param child  the Component3D object to move to this container.
     * @param index  at which location to insert. 
     *     The index must be a value greater than or equal to 0 
     *     and less than or equal to numChildren().
     */
    public void addChild(Node child) {
        // TODO This will change when we move to Java3D 1.4
        if (transparencyManager != null) {
            transparencyManager.addGraph(child);
        }
        setCapabilitiesIfNeeded(child);
	((Component3DWrapper)wrapped).addChild((NodeWrapper)child.getWrapped());
    }

    /**
     * Replaces the Component3D object at the specified index 
     * in this component's list of children with the specified component.
     *
     * @param child  the Component3D object to move to this container.
     * @param index  at which location to insert. 
     *     The index must be a value greater than or equal to 0 
     *     and less than or equal to numChildren().
     */
    public void setChild(Node child, int index) {
        if (transparencyManager != null) {
            Node oldChild = getChild(index);
            if(oldChild != null) {
                transparencyManager.removeGraph(oldChild);
            }
            transparencyManager.addGraph(child);
        }
        setCapabilitiesIfNeeded(child);
	((Component3DWrapper)wrapped).setChild((NodeWrapper)child.getWrapped(), index);
    }

    /**
     * Inserts the specified Component3D object in this component's list 
     * of children at the specified index.
     *
     * @param child  the Component3D object to move to this container.
     * @param index  at which location to insert. 
     *     The index must be a value greater than or equal to 0 
     *     and less than or equal to numChildren().
     */
    public void insertChild(Node child, int index) {
        if (transparencyManager != null) {
            transparencyManager.addGraph(child);
        }
        setCapabilitiesIfNeeded(child);
	((Component3DWrapper)wrapped).insertChild((NodeWrapper)child.getWrapped(), index);
    }
    
    /**
     * Moves the specified Component3D object from its existing location
     * to the end of this component's list of children.
     *
     * @param child  the Component3D object to move to this container.
     */
    public void moveTo(BranchGroup child) {
        if(transparencyManager != null) {
            transparencyManager.addGraph(child);
        }
        ((Component3DWrapper)wrapped).moveTo((BranchGroupWrapper)child.getWrapped());
    }
    
    /**
     * Removes the specified child node from this component's list
     * of children. If the specified object is not in the list, the
     * list is not modified.
     */
    public void removeChild(Node child) {
        if(transparencyManager != null) {
            transparencyManager.removeGraph(child);
        }
        ((Component3DWrapper)wrapped).removeChild((NodeWrapper)child.getWrapped());
    }

    /**
     * Removes the child node at the specified index from this component's
     * list of children.
     */
    public void removeChild(int index) {
        if(transparencyManager != null)  {
            Node child = getChild(index);
            transparencyManager.removeGraph(child);
        }
        ((Component3DWrapper)wrapped).removeChild(index);
    }

    /**
     * Removes all children from this component.
     */
    public void removeAllChildren() {
        if(transparencyManager != null) {
            int num = numChildren();
            for (int i = 0; i < num; i++) {
                Node child = getChild(i);
                transparencyManager.removeGraph(child);
            }
        }
        ((Component3DWrapper)wrapped).removeAllChildren();
    }

    /**
     * Returns the child node at the specified index in this component's
     * list of children with the specified child.
     */
    public Node getChild(int index) {
	return (Node)((Component3DWrapper)wrapped).getChild(index).getUserData();
    }

    /**
     * Returns an Enumeration object of this component's list of children.
     */
    public Enumeration getAllChildren() {
	//return ((Component3DWrapper)wrapped).getAllChildren();
        Enumeration e = ((Component3DWrapper)wrapped).getAllChildren();
        Vector retList = new Vector();
        NodeWrapper node;
        
        while(e.hasMoreElements()) {
            //System.out.println( e.nextElement() );
            node = (NodeWrapper)e.nextElement();
            if (node!=null)
                retList.add( node.getUserData() );
            else
                retList.add(null);
        }
        
        return retList.elements();
    }

    /**
     * Returns a count of this component's children.
     */
    public int numChildren() {
	return ((Component3DWrapper)wrapped).numChildren();
    }

    /**
     * Retrieves the index of the specified child node in this component's
     * list of children.
     */
    public int indexOfChild(Node child) {
	return ((Component3DWrapper)wrapped).indexOfChild((NodeWrapper)child.getWrapped());
    }
    


    /**
     * Translates this component to a new location. 
     * 
     * The center of the new location is specified by the loc Vector3f
     * object in the coordinate space of this component's parent.
     */
    public void setTranslation(float x, float y, float z) {
        changeTranslation(x, y, z, 0);
    }

    /**
     * Requests to move this component to a new location.
     * 
     * The center of the new location is specified by the x, y and
     * z parameters in the coordinate space of this component's parent.
     *
     * Calls changeTranslation(float x, float y, float z, int duration) to
     * actually perform operation.
     *     
     */
    public void changeTranslation(float x, float y, float z) {
        changeTranslation(x, y, z, c3dAnimation.getDefaultTranslationDuration());
    }
    
    /**
     * Requests to move this component to a new location
     * within the specified duration. 
     * 
     * The center of the new location is specified by the x, y and
     * z parameters in the coordinate space of this component's parent.
     *
     * Calls changeTranslation(float x, float y, float z, int duration) to
     * actually perform operation.
     *     
     */
    public void changeTranslation(Vector3f loc, int duration) {
        changeTranslation(loc.x, loc.y, loc.z, duration);
    }
    
    /**
     * Requests to move this component to a new location
     * within the specified duration. 
     * 
     * The center of the new location is specified by the x, y and
     * z parameters in the coordinate space of this component's parent.
     *     
     * Calls changeTranslation(float x, float y, float z, int duration) to
     * actually perform operation.
     *     
     */
    public void changeTranslation(Vector3f loc) {
        changeTranslation(loc.x, loc.y, loc.z);
    }

    /**
     * Requests to move this component to a new location
     * within the specified duration. 
     * 
     * The center of the new location is specified by the x, y and
     * z parameters in the coordinate space of this component's parent.
     *     
     */
    public void changeTranslation(float x, float y, float z, int duration) {
	assert(c3dAnimation != null);
        if (duration < 0) {
            throw new IllegalArgumentException("duration cannot be negative");
        }
        c3dAnimation.changeTranslation(x, y, z, duration);
    }
    
    /**
     * Returns this component's location.
     * 
     * The return value represents the center of location 
     * in the coordinate space of this component's parent.
     */
    public Vector3f getTranslation(Vector3f loc) {
        assert(c3dAnimation != null);
        if (loc == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
	return c3dAnimation.getTranslation(loc);
    }
    
    /**
     * Returns this component's final location.
     * 
     * The return value represents the final location specified
     * by the last changeTranslation() invocation.
     */
    public Vector3f getFinalTranslation(Vector3f loc) {
        assert(c3dAnimation != null);
        if (loc == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
        return c3dAnimation.getFinalTranslation(loc);
    }

    /**
     * Sets an axis that is referenced in future rotational actions.
     */
    public void setRotationAxis(float x, float y, float z) {
        changeRotationAxis(x, y, z, 0);
    }

    /**
     * Requests to set the axis of rotation around with the specified duration.
     */
    public void changeRotationAxis(Vector3f axis, int duration) {
        changeRotationAxis(axis.x, axis.y, axis.z, duration);
     }
        
    /**
     * Requests to set the axis of rotation around with the specified duration.
     */
    public void changeRotationAxis(Vector3f axis) {
        assert(c3dAnimation != null);
        changeRotationAxis(axis.x, axis.y, axis.z, c3dAnimation.getDefaultRotationAxisDuration());
     }

    /**
     * Requests to set the axis of rotation around with the specified duration.
     */
    public void changeRotationAxis(float x, float y, float z, int duration) {
	assert(c3dAnimation != null);
        if (duration < 0) {
            throw new IllegalArgumentException("duration cannot be negative");
        }
        c3dAnimation.changeRotationAxis(x, y, z, duration);
    }

    /**
     * Sets an angle of rotation around the previously specified axis.
     * The angle is specified in radian.
     */
    public void setRotationAngle(float angle) {
	changeRotationAngle(angle, 0);
    }
    
    /**
     * Requests to set an angle of rotation around the previously
     * specified axis.
     * The angle is specified in radian.
     */
    public void changeRotationAngle(float angle) {
	changeRotationAngle(angle, c3dAnimation.getDefaultRotationAngleDuration());
    }
    
    /**
     * Requests to set an angle of rotation around the previously
     * specified axis within the specified duration.
     * The angle is specified in radian.
     */
    public void changeRotationAngle(float angle, int duration) {
	assert(c3dAnimation != null);
        if (duration < 0) {
            throw new IllegalArgumentException("duration cannot be negative");
        }
        c3dAnimation.changeRotationAngle(angle, duration);
    }
    
    /**
     * Returns this component's rotation angle.
     * The angle is given in radian.
     */
    public float getRotationAngle() {
        assert(c3dAnimation != null);
        return c3dAnimation.getRotationAngle();
    }
    
    /**
     * Returns this component's rotation angle.
     * 
     * The return value represents the target anlge specified
     * by the last changeRotationAngle() invocation.
     * The angle is given in radian.
     */
    public float getFinalRotationAngle() {
        assert(c3dAnimation != null);
        return c3dAnimation.getFinalRotationAngle();
    }
    
    /**
     * Returns an axis that is referenced in future rotational actions
     * against this component.
     */
    public Vector3f getRotationAxis(Vector3f axis) {
        assert(c3dAnimation != null);
        if (axis == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
        return c3dAnimation.getRotationAxis(axis);
    }
    
    /**
     * Returns an axis that is referenced in future rotational actions
     * against this component.
     * The return value represents the target axis specified
     * by the last changeRotationAxis() invocation.
     */
    public Vector3f getFinalRotationAxis(Vector3f axis) {
        assert(c3dAnimation != null);
        if (axis == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
        return c3dAnimation.getFinalRotationAxis(axis);
    }
    
    /**
     * Sets scaling factor of this component.
     */
    public void setScale(float scale) {
	changeScale(scale, 0);
    }
    
    /**
     * Requests to set scaling factor of this component.
     */
    public void changeScale(float scale) {
	changeScale(scale, c3dAnimation.getDefaultScaleDuration());
    }
    
    /**
     * Requests to set scaling factor of this component
     * within the specified duration.
     */
    public void changeScale(float scale, int duration) {
	assert(c3dAnimation != null);
        if (duration < 0) {
            throw new IllegalArgumentException("duration cannot be negative");
        }
        c3dAnimation.changeScale(scale, duration);
    }
    
    /**
     * Returns scaling factor of this component.
     */
    public float getScale() {
        assert(c3dAnimation != null);
        return c3dAnimation.getScale();
    }
    
    /**
     * Returns final scaling factor of this component.
     *
     * The return value represents the final scaling factor specified
     * by the last changeScale() invocation.
     *
     * If the scale is non-uniform this method will log a warning and will
     * return the average of the 3 axis of scale.
     */
    public float getFinalScale() {
        assert(c3dAnimation != null);
        return c3dAnimation.getFinalScale();
    }
    
    /**
     * Change scaling factor of this component over specified duration
     */
    public void changeScale(Vector3f scale, int duration) {
        if (scale == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
        changeScale(scale.x, scale.y, scale.z, duration);
    }

    /**
     * Sets scaling factor of this component.
     */
    public void setScale(float x,float y,float z) {
	changeScale(x, y, z, 0);
    }

    /**
     * Change scaling factor of this component over specified duration
     */
    public void changeScale(float x, float y, float z, int duration) {
	assert(c3dAnimation != null);
        if (duration < 0) {
            throw new IllegalArgumentException("duration cannot be negative");
        }
        c3dAnimation.changeScale(x, y, z, duration);
    }
    
    /**
     * Get the scaling factor of this component
     * @param scale the Vector3f to store the scaling factor (cannot be null)
     * @return the scaling factor (for chaining)
     */
    public Vector3f getScale(Vector3f scale) {
        assert(c3dAnimation != null);
        if (scale == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
        return c3dAnimation.getScale(scale);
    }
    
    /**
     * Returns final scaling factor of this component.
     *
     * The return value represents the final scaling factor specified
     * by the last changeScale() invocation.
     */
    public Vector3f getFinalScale(Vector3f ret) {
        assert(c3dAnimation != null);
        return c3dAnimation.getFinalScale(ret);
    }
    
    /**
     * Sets scaling factor of this component.
     */
    public void setTransparency(float transparency) {
	changeTransparency(transparency, 0);
    }
    
    /**
     * Requests to set scaling factor of this component.
     */
    public void changeTransparency(float transparency) {
	changeTransparency(transparency, c3dAnimation.getDefaultScaleDuration());
    }
    
    /**
     * Requests to set scaling factor of this component
     * within the specified duration.
     */
    public void changeTransparency(float transparency, int duration) {
	assert(c3dAnimation != null);
        if (duration < 0) {
            throw new IllegalArgumentException("duration cannot be negative");
        }
        c3dAnimation.changeTransparency(transparency, duration);
    }
    
    /**
     * Returns scaling factor of this component.
     */
    public float getTransparency() {
        assert(c3dAnimation != null);
        return c3dAnimation.getTransparency();
    }
    
    /**
     * Returns final scaling factor of this component.
     *
     * The return value represents the final scaling factor specified
     * by the last changeScale() invocation.
     *
     * If the scale is non-uniform this method will log a warning and will
     * return the average of the 3 axis of scale.
     */
    public float getFinalTransparency() {
        assert(c3dAnimation != null);
        return c3dAnimation.getFinalTransparency();
    }
    
    /**
     * Set the Component3D animation, this controls the 'look and feel' of
     * position, rotation and scale changes for this component.
     *
     * If an animation is already present it will be disabled and the new
     * animation set in it's place. The previous animation is stopped dead
     * and it's final values are NOT copied to the new animation.
     *
     */
    public void setAnimation(Component3DAnimation animation) {
        setAnimationInternal(animation, new Component3DAnimationTarget());
    }
    
    synchronized void setAnimationInternal(Component3DAnimation animation, Component3DAnimationTarget animTarget) {
        Component3DAnimation prevC3dAnim = null;
        
        if (animation == null) {
            animation = new NullFrame3DAnimation();
        }
        
        // stop the animations first before working on them
        animation.setRunning(false);
        if (c3dAnimation != null) {
            c3dAnimation.setRunning(false);
        }
        
        prevC3dAnim = c3dAnimation;
        c3dAnimation = animation;
        animTarget.setTargetComponent3D(this);
        c3dAnimation.setTarget(animTarget);
        
        if (prevC3dAnim != null) {
            // do the best job to migrate all the on-going animaiton sequences
            prevC3dAnim.copyStatusTo(c3dAnimation);
            
            // do the following before setting null to prevC3dAnim
            Component3DAnimationTarget prevAnimTarget = (Component3DAnimationTarget)prevC3dAnim.getTarget();
            prevAnimTarget.setTargetComponent3D(null);
            prevC3dAnim.setTarget(null);
        }
        
        if (c3dAnimation.getAnimationParameters().contains(Animation.AnimationType.TRANSPARENCY) && transparencyManager == null) {
            createTransparencyManager();
        }
        
        c3dAnimation.setRunning(true);
    }
    
    /**
     * Called when this node has been removed from the scene.
     */
    void removeTransparencyManager() {
        if (transparencyManager!=null) {
            transparencyManager.clear();
            transparencyManager = null;
        }
    }
    
    void createTransparencyManager() {
        if (transparencyManager==null) {
            transparencyManager = new TransparencyManager();
            boolean wasVisible = isVisibleInternal();
            setVisibleInternal(false);
            Enumeration e = getAllChildren();
            while(e != null && e.hasMoreElements()) {
                transparencyManager.addGraph((Node)e.nextElement());
            }
            setVisibleInternal(wasVisible);            
        }
    }
    
    /**
     * Returns the current Scene Manager animation for this component3D
     */
    public Component3DAnimation getAnimation() {
        Component3DAnimation ret = c3dAnimation;
        if (ret instanceof NullFrame3DAnimation) {
            ret = null;
        }
        return ret;
    }
    
    /**
     * Set the component tranform, used by Component3DAnimationTarget
     */
    void setTransform(Transform3D transform) {
        //System.out.println(transform.getType()+"  "+(transform.getType() & Transform3D.AFFINE));
        
        // Fix for bug 501
        
        // Java 3D bug, transforms are not allways correctly classified so check for NaN ourselves, j3d issue 253
//        if ((transform.getType() & Transform3D.AFFINE)!=Transform3D.AFFINE) {
        if (isInfOrNaN(transform)) {
            Exception e = new RuntimeException("Non affine transform");
            logger.log(Level.SEVERE, "Non affine transform being set, this will result in incorrect rendering.", e);
        }
//        if (this instanceof org.jdesktop.lg3d.displayserver.awtpeer.FramePeerImpl)
//            System.out.println(transform);
        ((Component3DWrapper)wrapped).setTransform(transform.getWrapped());
    }

    float[] mat = new float[16];    
    private final boolean isInfOrNaN(Transform3D transform) {
        transform.get(mat);
        for (int i = 0; i < 16; i++) {
            if (Float.isNaN(mat[i]) || Float.isInfinite(mat[i])) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get the translation from this component to the specified component.
     *
     * @param comp  the target Component3D object
     * @param ret   the Vector3f object where the result is put and which
     *     is returned by this method.
     */
    public Vector3f getTranslationTo(Component3D comp, Vector3f ret) {
        Transform3D t3d = new Transform3D();
        Vector3f v3f = new Vector3f();
        getLocalToVworld(t3d);
        t3d.get(v3f);
        if (comp != null) {
            comp.getLocalToVworld(t3d);
            t3d.get(ret);
        } else {
            ret.set(0.0f, 0.0f, 0.0f);
        }
        ret.sub(v3f);
        return ret;
    }
    
    /**
     * Prompts the layout manager of the parent to lay out this component.
     */
    public void requestParentToRevalidate() {
        ((Component3DWrapper)wrapped).requestParentToRevalidate();
    }
    
    public String toString() {
        return super.toString() + " " + getNodeID();    
    }
   
    TransparencyManager getTransparencyManager() {
        return transparencyManager;
    }
    
    /**
     * Make whatever changes are necessary to make this node live or not live
     * from the lg3d point of view. This has no impact on the Java 3D 
     * liveness of the node. This is an implementation detail and should NEVER 
     * be called from user code.
     *
     * This is called just before the node is added to the scene graph and just
     * after it's removed from the graph.
     */
    void setLive(boolean isLive) {
        if (isLive) {
            if (listeners!=null) {
                for(LgEventListener listener : listeners) {
                    LgEventConnector.getLgEventConnector().addListener(this, listener);    
                }
            }
        } else {
            if (listeners!=null) {
                for(LgEventListener listener : listeners) {
                    LgEventConnector.getLgEventConnector().removeListener(this, listener);  
                }
            }
        }
    }
    
    /**
     * Implementation detail, do not call from user code.
     * May be removed in future.
     */
    protected void createWrapped() {
        wrapped = instantiate("Component3D");
        wrapped.setUserData(this);
    }
    
    /**
     * Implementation detail, do not call from user code
     */
    private NodeID getNodeID() {
        return nodeID;
    }
    
    /**
     * Implementation detail, do not call from user code
     */
    private void setNodeID(NodeID nodeID) {
        this.nodeID = nodeID;
        ((LgBranchGroupWrapper)wrapped).setNodeID( nodeID );
        LgNodeManager.getLgNodeManager().addNode( this );
    }
    
    
    /**
     * A hack to allow EventProcessor, which resides out side of this package,
     * to access the setSource() method.  We needed to keep the method package
     * private since it is an implementation detail and shouldn't be exposed
     * to application developers.
     */
    static {
        LgNodeManager.setComponent3DAccessHelper(new LgNodeManagerHelper());
    }
    
    /*
     * The following is a kludge to let the EventProcessor invoke setSource()
     * while hiding the method from the public API.
     */
    private static class LgNodeManagerHelper 
        implements LgNodeManager.Component3DAccessHelper 
    {
//        public void createWrapped(Component3D comp) {
//            comp.createWrapped();
//        }
        
        public NodeID getNodeID(Component3D comp) {
            return comp.getNodeID();
        }
        
        public void setNodeID(Component3D comp, NodeID nodeID) {
            comp.setNodeID(nodeID);
        }
        
        public void setCapabilities(Component3D comp) {
            comp.setCapabilities();
        }
        
        public void setTransform(Component3D comp, Transform3D transform) {
            comp.setTransform(transform);
        }

    }
}

