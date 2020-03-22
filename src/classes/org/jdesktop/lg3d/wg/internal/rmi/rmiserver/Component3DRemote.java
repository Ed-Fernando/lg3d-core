/**
 * Project Looking Glass
 *
 * $RCSfile: Component3DRemote.java,v $
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
 * $Revision: 1.4 $
 * $Date: 2005-04-14 23:05:26 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.internal.rmi.rmiserver;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.wg.event.LgEvent;
import javax.vecmath.*;
import org.jdesktop.lg3d.sg.internal.rmi.rmiserver.Transform3DRemote;


/**
 * Component3D object is a top level object for widget development.
 *
 * It supports visual rendering by adding scene graph node children 
 * and event handling.
 */
public interface Component3DRemote extends LgBranchGroupRemote {

    /**
     * Sets the capabilities of scene graph nodes under this
     * Component3D correctly so that this component can be picked.
     * Typically invoked once after finishing adding all the scene graph nodes.
     * 
     * REMINDER -- automate this ?
     */
    public void setCapabilities() throws java.rmi.RemoteException;

    /**
     * Post an event into the system with this Component3D object 
     * as the source of the event.
     */
    public void postEvent(LgEvent event) throws java.rmi.RemoteException;
    
//    public int getNodeID() {
//        return nodeID;
//    }
    
    /**
     * Add a listener which is called whenever events of the specified
     * class are posted from this object
     */    
    public void addListener( LgEventListener listener, Class evtClass ) throws java.rmi.RemoteException;
    
    /**
     * Register a listener for events of class evtClass which are generated
     * from any source of the specified class.
     *
     * To listen for an event from any source pass LgEventSource.ALL_SOURCES as
     * the source
     */
    public void addListener( LgEventListener listener, Class evtClass, Class source ) throws java.rmi.RemoteException;
    
    /**
     * Sets a cursor associated with this component.
     */
    public void setCursor(Cursor3DRemote cursor) throws java.rmi.RemoteException;

    /**
     * Returns the cursor associated with this component.
     */
    public Cursor3DRemote getCursor() throws java.rmi.RemoteException;

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
    public void setSizeHint(float width, float height, float depth) throws java.rmi.RemoteException;

    public Vector3f getSizeHint(Vector3f ret) throws java.rmi.RemoteException;
    
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
    public void setVisible(boolean visible) throws java.rmi.RemoteException;
//    public void changeVisible(boolean visible) throws java.rmi.RemoteException;
//    public void changeVisible(boolean visible, int duration) throws java.rmi.RemoteException;

    public boolean isVisible() throws java.rmi.RemoteException;
//    public boolean getTargetVisible() throws java.rmi.RemoteException;
    
    /**
     * Sets the transform component of this TransformGroup to the value of
     * the passed transform.
     * @param t1 the transform to be copied
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     * @exception BadTransformException if the transform is not affine.
     */
    public void setTransform(Transform3DRemote t1) throws java.rmi.RemoteException;

//    /**
//     * Moves this component to a new location.
//     * 
//     * The center of the new location is specified by the x, y and
//     * z parameters in the coordinate space of this component's parent.
//     */
//    public void setTranslation(float x, float y, float z) throws java.rmi.RemoteException;
//    public void changeTranslation(float x, float y, float z) throws java.rmi.RemoteException;
//    public void changeTranslation(float x, float y, float z, int duration) throws java.rmi.RemoteException;
//
//    /**
//     * Moves this component to a new location. 
//     * 
//     * The center of the new location is specified by the loc Vector3f
//     * object in the coordinate space of this component's parent.
//     */
//    public void setTranslation(Vector3f loc) throws java.rmi.RemoteException;
//    public void changeTranslation(Vector3f loc) throws java.rmi.RemoteException;
//    public void changeTranslation(Vector3f loc, int duration) throws java.rmi.RemoteException;
//
//    /**
//     * Sets an axis that is referenced in future rotational actions.
//     */
//    public void setRotationAxis(float x, float y, float z) throws java.rmi.RemoteException;
//
//    /**
//     * Sets an axis that is referenced in future rotational actions.
//     */
//    public void setRotationAxis(Vector3f axis) throws java.rmi.RemoteException;
//
//    /**
//     * Sets an angle of rotation around the previously specified axis.
//     * The angle is specified in radians.
//     *
//     * @param angle Angle in radians
//     */
//    public void setRotationAngle(float angle) throws java.rmi.RemoteException;
//    public void changeRotationAngle(float angle) throws java.rmi.RemoteException;
//    public void changeRotationAngle(float angle, int duration) throws java.rmi.RemoteException;
//    
//    /**
//     * Sets scaling factor of this component.
//     */
//    public void setScale(float scale) throws java.rmi.RemoteException;
//    public void changeScale(float scale) throws java.rmi.RemoteException;
//    public void changeScale(float scale, int duration) throws java.rmi.RemoteException;
//    
//    /**
//     * Returns this component's location.
//     * 
//     * The return value represents the center of location 
//     * in the coordinate space of this component's parent.
//     */
//    public Vector3f getTranslation() throws java.rmi.RemoteException;
//    public Vector3f getTargetTranslation() throws java.rmi.RemoteException;
//
//    public float getRotationAngle() throws java.rmi.RemoteException;
//    public float getTargetRotationAngle() throws java.rmi.RemoteException;
//
//    public Vector3f getRotationAxis() throws java.rmi.RemoteException;
//
    /**
     * Returns scaling factor of this component.
     */
//    public float getScale() throws java.rmi.RemoteException;
//    public float getTargetScale() throws java.rmi.RemoteException;
    
    public void requestParentToRevalidate() throws java.rmi.RemoteException;
}
