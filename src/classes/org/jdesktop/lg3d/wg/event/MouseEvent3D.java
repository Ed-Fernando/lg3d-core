/**
 * Project Looking Glass
 *
 * $RCSfile: MouseEvent3D.java,v $
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
 * $Revision: 1.16 $
 * $Date: 2007-04-10 22:58:45 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.event;


import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.InputEvent;
import javax.media.j3d.Node;
import javax.media.j3d.Canvas3D;
import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;

import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.internal.j3d.j3dnodes.J3dComponent3D;
import org.jdesktop.lg3d.wg.internal.j3d.j3dnodes.J3dNativePane;
import org.jdesktop.lg3d.wg.internal.wrapper.Component3DWrapper;
import org.jdesktop.lg3d.displayserver.fws.FoundationWinSys;
import org.jdesktop.lg3d.displayserver.fws.MouseEventNodeInfo;
import org.jdesktop.lg3d.sg.Transform3D;


/**
 * The abstract and super class for all the mouse events.
 */
public abstract class MouseEvent3D extends InputEvent3D {
    public enum ButtonId {NOBUTTON, BUTTON1, BUTTON2, BUTTON3};
    
    /** Destination nodes and associated info */
    transient MouseEventNodeInfo nodeInfo;
    /** the encapsulated mouse event */
    transient MouseEvent awtEvent;
    /** the id of the event */
    private int eventID;
    /** the point where the drag finished, in the plane of the picked object */
    private Point3f cursorPos;
    
    /**
     * Convert MouseEvent into a MouseEvent3D and change the event id to newID
     * @param evt the MouseEvent
     * @param newID the new id of the event
     * @param nodeInfo destination node info for the event
     * @param cursorPos the coordinates of the cursor (in virtual world coordinates)
     */
    public MouseEvent3D(MouseEvent awtEvent, int newID, MouseEventNodeInfo nodeInfo, 
			Vector3f cursorPos) {
        this.nodeInfo = nodeInfo;
        this.awtEvent = awtEvent;
	this.eventID = newID;
        this.cursorPos = (cursorPos == null)?(null):(new Point3f(cursorPos));
    }
    
    InputEvent getAwtEvent() {
        return awtEvent;
    }
    
    /**
     * Returns the type eventID of this event.
     * This is an implementation detail and shouldn't be called by the user.
     * @return the type id of the event
     */
    int getID() {
	return eventID;
    }
    
    /**
     * Returns which, if any, of the mouse buttons has changed state.
     * @return one of the following enums: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3.
     */
    public ButtonId getButton() {
        ButtonId ret = ButtonId.NOBUTTON;
        
        int button = awtEvent.getButton();
        switch (button) {
            case MouseEvent.BUTTON1:
                ret = ButtonId.BUTTON1;
                break;
            case MouseEvent.BUTTON2:
                ret = ButtonId.BUTTON2;
                break;
            case MouseEvent.BUTTON3:
                ret = ButtonId.BUTTON3;
                break;
            default:
                assert(button == MouseEvent.NOBUTTON);
        }
        
	return ret;
    }
    
    //
    // LG3D specific methods from here...
    //
    
    /**
     * Return the node intersected by the pick ray associated with this
     * mouse event.  The distance index is used to specify one of 
     * a set of intersectiong nodes where 0 is the closest node.
     * @param distanceIndex the index of the destination node
     * @return the intersecting node
     */
    public Node getIntersectedNode(int distanceIndex) {
        if (nodeInfo != null) {
            if (distanceIndex < 0) {
                distanceIndex = nodeInfo.getNumNodes();
            }
            return nodeInfo.getNode(distanceIndex);
	}
        return null;
    }
    
    /**
     * Count the number of nodes
     * @return the number of nodes
     */
    public int numIntersectedNodes() {
        if (nodeInfo != null) {
            return nodeInfo.getNumNodes();
        }
        return 0;
    }
    
    /**
     * Get the point intersected by the pick ray associated with this
     * mouse event.  The distance index is used to specify one of 
     * a set of intersection points where 0 is the closest node.
     * @param ret the Point3f object where to store the result
     * @param distanceIndex the index of the destination node.
     * @return the intersection point of the destination node
     * (in virtual world coordinates) or null is it is outside the bounds
     */
    private Point3f getIntersection(Point3f ret, int distanceIndex) {
        if (ret == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
        if (nodeInfo != null) {
            ret.set(nodeInfo.getPointVW(distanceIndex));
	} else {
            ret.set(Float.NaN, Float.NaN, Float.NaN);
	}
        return ret;
    }

    /**
     * Get the point intersected by the pick ray associated with this
     * mouse event.
     * The returned value may be null if the event did not occur over a 3D object.
     * @param ret the Point3f object where to store the result
     * @return the intersection point for this event
     */
    public Point3f getIntersection(Point3f ret) {
	return getIntersection(ret, 0);
    }
    
    /**
     * Get the intersection point for this event in the local coordinate space
     * relative to the event source object.
     * The returned Potint3f object may holds NaNs as the coordinate 
     * if the event did not occur over a 3D object.
     * @param ret the Vector3f object where to store the result
     * @return the intersection point for this event
     */
    public Point3f getLocalIntersection(Point3f ret) {
        if (ret == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
	getIntersection(ret, 0);
        
        return translateToLocalCoord(ret);
    }
    
    /**
     * Translates the given coordinate into the local coordinate space
     * of the event source, if the source is non-null.
     */
    Point3f translateToLocalCoord(Point3f ret) {
        LgEventSource source = getSource();
        if (source != null) {
            assert(source instanceof Component3D);
            Component3D c3d = (Component3D)source;
            if (c3d.isLive()) {
                Transform3D t3d = new Transform3D();
                c3d.getLocalToVworld(t3d);
                t3d.invert();
                t3d.transform(ret);
            }
        }
        return ret;
    }
    
    /**
     * Return the Component3D at the specified index in the node info 
     * where 0 is the closest intersected node. The method searches for the first
     * Component3D that is a subclass of componentClass. The distanceIndex relates
     * to the distance of the picked object from the eye, index 0 is the object closest
     * to the user and subsequent objects are further away.
     * @param distanceIndex the index of the pick
     * @return the Component3D at the give pick index, or null if no Component3D
     *  was picked.
     */
    public Component3D getIntersectedComponent3D(int distanceIndex) {
        if (nodeInfo != null) {
	    Node node = nodeInfo.getNode(distanceIndex);
	    while (node != null) {
                // The J3dNative pane extends J3dComponent3D but 
		// NativePane extends Group, which causes a class cast exception
		// NativePane has no reason to extend Component3D so we check
		// here to avoid the exception.
		if (node instanceof J3dComponent3D && !(node instanceof J3dNativePane)) {
		    return (Component3D)((Component3DWrapper)node.getUserData()).getUserData();    
		}
		node = node.getParent();
	    }
        }
        return null;
    }

    /**
     * Return the Component3D at the specified index in the node info 
     * where 0 is the closest intersected node. The method searches for the first
     * Component3D that is a subclass of componentClass. The distanceIndex relates
     * to the distance of the picked object from the eye, index 0 is the object closest
     * to the user and subsequent objects are further away.
     * @param distanceIndex the index of the pick
     * @param componentClass class of the component to find
     * @return the Component3D at the give pick index, or null if no Component3D
     *  of the correct class was found.
     */
    public Component3D getIntersectedComponent3D(int distanceIndex, Class componentClass) {
        if (nodeInfo != null) {
	    Node node = nodeInfo.getNode(distanceIndex);
	    while (node != null) {
		// The J3dNative pane extends J3dComponent3D but 
		// NativePane extends Group, which causes a class cast exception
		// NativePane has no reason to extend Component3D so we check
		// here to avoid the exception.
		if (node instanceof J3dComponent3D && !(node instanceof J3dNativePane)) {
		    Component3D c3d = (Component3D)((Component3DWrapper)node.getUserData()).getUserData();   
		    if (componentClass.isAssignableFrom(c3d.getClass())) {
			return c3d;
		    }
		}
		node = node.getParent();
	    }
        }
        return null;
    }

    /**
     * Returns the X position in terms of the ImagePlate coordinate.
     * @return the x position
     */
    public float getImagePlateX() {
        Canvas3D canvas3d = FoundationWinSys.getFoundationWinSys().getCanvas(0);
        float w = canvas3d.getWidth();
        float pw = (float)canvas3d.getPhysicalWidth();
	return ((awtEvent.getX() - w * 0.5f) * pw / w);
    }

    /**
     * Returns the Y position in terms of the ImagePlate coordinate.
     * @return the y position
     */
    public float getImagePlateY() {
        Canvas3D canvas3d = FoundationWinSys.getFoundationWinSys().getCanvas(0);
        float h = canvas3d.getHeight();
        float ph = (float)canvas3d.getPhysicalHeight();
	return ((-awtEvent.getY() + h * 0.5f) * ph / h);
    }
    
    /**
     * Returns the cursor position in 3 space
     */
    public Point3f getCursorPosition(Point3f ret) {
        if (ret == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
        ret.set(cursorPos);
        return ret;
    }
    
    /**
     * Returns the cursor position in 3 space
     */
    public Point3f getLocalCursorPosition(Point3f ret) {
        if (ret == null) {
            throw new IllegalArgumentException("argument cannot be null");
        }
        ret.set(cursorPos);
        return translateToLocalCoord(ret);
    }
    
    /**
     * This is an implementation detail and should not be invoked by the user.
     *
     * @deprecated will be removed in future
     */
    public java.awt.event.MouseEvent createSwingEvent(Component comp, Point mousePos) {
        //System.out.println("SEVERE " +evt.getX()+" "+evt.getY());
        return new MouseEvent(comp, 
                            eventID, 
                            awtEvent.getWhen(), 
                            awtEvent.getModifiers(), 
                            (int)mousePos.getX(), 
                            (int)mousePos.getY(), 
                            awtEvent.getClickCount(),
                            awtEvent.isPopupTrigger(),
                            awtEvent.getButton());
    }
    
    /**
     * Returns the AWT component that originated this event.
     *
     * @deprecated internal use only
     */
    public java.awt.Component getAWTComponent() {
        return awtEvent.getComponent();
    }

    private String idToString() {
	String desc = "unknown";
	switch (getID()) {
	    case MouseEvent.MOUSE_PRESSED:
		desc = "MOUSE_PRESSED";
		break;
	    case MouseEvent.MOUSE_RELEASED:
		desc = "MOUSE_RELEASED";
		break;
	    case MouseEvent.MOUSE_CLICKED:
		desc = "MOUSE_CLICKED";
		break;
	    case MouseEvent.MOUSE_ENTERED:
		desc = "MOUSE_ENTERED";
		break;
	    case MouseEvent.MOUSE_EXITED:
		desc = "MOUSE_EXITED";
		break;
    	    case MouseEvent.MOUSE_DRAGGED:
		desc = "MOUSE_DRAGGED";
		break;
    	    case MouseEvent.MOUSE_MOVED:
		desc = "MOUSE_MOVED";
		break;
    	    case MouseEvent.MOUSE_WHEEL:
		desc = "MOUSE_WHEEL";
		break;
	}
	return "MouseEvent3D " + desc;
    }
    
    /**
     * Returns a string representation of the object.
     */
    public String toString () {
	String str = "eventID=" + idToString();

 	str += ",nodeInfo=" + nodeInfo;
	str += ",awtEvent=" + awtEvent;
	str += ",cursorPos=" + cursorPos;

	return str;
    }

    // TODO: HACK
    public MouseEvent getMouseEvent () {
	return awtEvent;
    }
}
