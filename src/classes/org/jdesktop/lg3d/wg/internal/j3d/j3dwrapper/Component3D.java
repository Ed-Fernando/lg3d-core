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
 * $Revision: 1.5 $
 * $Date: 2005-06-24 19:48:47 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.internal.j3d.j3dwrapper;

import org.jdesktop.lg3d.sg.internal.wrapper.NodeWrapper;
import org.jdesktop.lg3d.sg.internal.wrapper.BranchGroupWrapper;
import org.jdesktop.lg3d.sg.internal.wrapper.GroupWrapper;
import org.jdesktop.lg3d.displayserver.AppConnectorPrivate;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.wg.event.LgEvent;
import javax.media.j3d.*;
import javax.vecmath.*;
import org.jdesktop.lg3d.wg.internal.wrapper.Component3DWrapper;
import org.jdesktop.lg3d.wg.internal.wrapper.Cursor3DWrapper;
import java.util.Enumeration;
import java.util.logging.Logger;
import org.jdesktop.lg3d.wg.internal.j3d.j3dnodes.J3dComponent3D;
import org.jdesktop.lg3d.wg.internal.j3d.j3dnodes.J3dCursor3D;
import java.util.Vector;


/**
 * Component3D object is a top level object for widget development.
 *
 * It supports visual rendering by adding scene graph node children 
 * and event handling.
 */
public class Component3D extends LgBranchGroup implements Component3DWrapper, LgEventSource {
    protected static final Logger logger
	= Logger.getLogger("lg.wg");

    /**
     * Creates a new instance of Component3D.
     * 
     * By default, it is visible (i.e. getVisible() returns 'true')
     * and it doesn't have any asscociated cursor.
     */
    public Component3D() {
	this(true);
    }

    Component3D(boolean visible) {
	setVisible(visible);
    }
    
    protected void createWrapped() {
        wrapped = new J3dComponent3D();
        wrapped.setUserData(this);
    }

    /**
     * Sets the capabilities of scene graph nodes under this
     * Component3D correctly so that this component can be picked.
     * Typically invoked once after finishing adding all the scene graph nodes.
     * 
     * REMINDER -- automate this ?
     */
    public void setCapabilities() {
	super.setCapabilities();
    }

    /**
     * Post an event into the system with this Component3D object 
     * as the source of the event.
     */
    public void postEvent(LgEvent event) {
        AppConnectorPrivate.getAppConnector().postEvent(event, this);
    }
    
//    public int getNodeID() {
//        return nodeID;
//    }
    
    /**
     * Add a listener which is called whenever events of the specified
     * class are posted from this object
     */    
    public void addListener( LgEventListener listener, Class evtClass ) {
        AppConnectorPrivate.getAppConnector().addListener( listener, evtClass, this );
    }
    
    /**
     * Register a listener for events of class evtClass which are generated
     * from any source of the specified class.
     *
     * To listen for an event from any source pass LgEventSource.ALL_SOURCES as
     * the source
     */
    public void addListener( LgEventListener listener, Class evtClass, Class source ) {
        assert( LgEventSource.class.isAssignableFrom(source) );
        AppConnectorPrivate.getAppConnector().addListener( listener, evtClass, source );
    }
    
    /**
     * Sets a cursor name associated with this component.
     */
    public void setCursor(Cursor3DWrapper cursor) {
        if (cursor == null) {
            ((J3dComponent3D)wrapped).setCursor(null);
        } else {
            ((J3dComponent3D)wrapped).setCursor((J3dCursor3D)((Cursor3D)cursor).getWrapped());
        }
    }

    /**
     * Returns the cursor name associated with this component.
     */
    public Cursor3DWrapper getCursor() {
        J3dCursor3D cursor = ((J3dComponent3D)wrapped).getCursor();
        if (cursor == null) {
            return null;
        }
	return (Cursor3DWrapper)cursor.getUserData();
    }
    
    /**
     * Prompts the layout manager to lay out this component.
     */
    public void requestParentToRevalidate() {
        ((J3dComponent3D)wrapped).requestParentToRevalidate();
    }
    
    /**
     * Sets the size hint of this component using Dimension3D.
     *
     * The size is merely a hint and no default action will be 
     * taken except storing the information.  Subclasses of Component3D
     * can override this method and implement desired action.
     * 
     * REMINDER -- use of (immutable version of) BoundingBox?
     * REMINDER -- associating this with setCollisionBounds()?
     */
//    public void setSize(Dimension3D size) {
//	this.size = size;
//    }

    /**
     * Sets the size hint of this component using float values.
     *
     * The is a convenience method. It will create a Dimension3D object
     * from the given argument and invokes setSize(Dimension3D size)
     * method.
     */
    public void setSizeHint(float width, float height, float depth) {
        ((J3dComponent3D)wrapped).setSizeHint(width, height, depth);
    }
    
    public Vector3f getSizeHint(Vector3f ret) {
        return ((J3dComponent3D)wrapped).getSizeHint(ret);
    }
    
    /**
     * Returns the size hint of a component using a BoundingBox object.
     */
//    public Dimension3D getSize() {
//	return size;
//    }

    /**
     * Shows or hides this component depending on the value of parameter 
     * 'visible'.
     */
    public void setVisible(boolean visible) {
	((J3dComponent3D)wrapped).setVisible(visible);
    }
    
    public boolean isVisible() {
        return ((J3dComponent3D)wrapped).isVisible();
    }
    
    /**
     * Appends the specified child node to this component's list of children.
     */ 
    public void addChild(NodeWrapper child) {
	//checkChildClass(child);
	((J3dComponent3D)wrapped).addChildLG((javax.media.j3d.Node)child.getWrapped());
    }

    /**
     * Replaces the child node at the specified index in this component's
     * list of children with the specified child.
     */ 
    public void setChild(NodeWrapper child, int index) {
	//checkChildClass(child);
	((J3dComponent3D)wrapped).setChildLG((javax.media.j3d.Node)child.getWrapped(), index);
    }

    /**
     * Inserts the specified child node in this component's list of
     * children at the specified index.
     */
    public void insertChild(NodeWrapper child, int index) {
        //checkChildClass(child);
	((J3dComponent3D)wrapped).insertChildLG((javax.media.j3d.Node)child.getWrapped(), index);
    }

    /**
     * Moves the specified branch group node from its existing location
     * to the end of this component's list of children.
     */
    public void moveTo(BranchGroupWrapper child) {
	//checkChildClass(child);
	((J3dComponent3D)wrapped).moveTo((javax.media.j3d.BranchGroup)child.getWrapped());
    }

    /**
     * Removes the specified child node from this component's list
     * of children. If the specified object is not in the list, the
     * list is not modified.
     */
    public void removeChild(NodeWrapper child) {
	((J3dComponent3D)wrapped).removeChild((javax.media.j3d.Node)child.getWrapped());
    }

    /**
     * Removes the child node at the specified index from this component's
     * list of children.
     */
    public void removeChild(int index) {
	((J3dComponent3D)wrapped).removeChildLG(index);
    }

    /**
     * Removes all children from this component.
     */
    public void removeAllChildren() {
	((J3dComponent3D)wrapped).removeAllChildren();
    }

    /**
     * Replaces the child node at the specified index in this component's
     * list of children with the specified child.
     */
    public NodeWrapper getChild(int index) {
	return (NodeWrapper)((J3dComponent3D)wrapped).getChildLG(index).getUserData();
    }

    /**
     * Returns an Enumeration object of this component's list of children.
     */
    public Enumeration getAllChildren() {
        Enumeration e = ((J3dComponent3D)wrapped).getAllChildrenLG();
        Vector retList = new Vector();
        javax.media.j3d.Node node;
        
        while(e.hasMoreElements()) {
            node = (javax.media.j3d.Node)e.nextElement();
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
	return ((J3dComponent3D)wrapped).numChildrenLG();
    }

    /**
     * Retrieves the index of the specified child node in this component's
     * list of children.
     */
    public int indexOfChild(NodeWrapper child) {
	return ((J3dComponent3D)wrapped).indexOfChildLG((javax.media.j3d.Node)child.getWrapped());
    }

    public void setTransform(org.jdesktop.lg3d.sg.internal.wrapper.Transform3DWrapper t3d) {
        ((J3dComponent3D)wrapped).setTransform((javax.media.j3d.Transform3D)((org.jdesktop.lg3d.sg.internal.j3d.j3dwrapper.Transform3D)t3d).getWrapped());
    }
}
