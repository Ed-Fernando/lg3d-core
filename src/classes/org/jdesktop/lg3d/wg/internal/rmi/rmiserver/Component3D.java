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
 * $Revision: 1.4 $
 * $Date: 2005-04-14 23:05:26 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.internal.rmi.rmiserver;

import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.displayserver.LgNodeManager;
import org.jdesktop.lg3d.sg.internal.rmi.rmiserver.SGObjectFactoryImpl;
import org.jdesktop.lg3d.sg.internal.rmi.rmiserver.Transform3DRemote;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.wg.event.LgEvent;


/**
 * @author  Paul
 */
public class Component3D extends LgBranchGroup implements Component3DRemote {

    /** Creates a new instance of LgBranchGroup */

    public Component3D() throws java.rmi.RemoteException {
    }
    
    protected void createWrapped() {
        wrapped = new org.jdesktop.lg3d.wg.Component3D();
        wrapped.setUserData( this );
    }
        
    public void addListener(LgEventListener listener, Class evtClass) throws java.rmi.RemoteException {
        throw new RuntimeException("Not Implemented, use AppConnector");
    }
    
    public void addListener(LgEventListener listener, Class evtClass, Class source) throws java.rmi.RemoteException {
        throw new RuntimeException("Not Implemented, use AppConnector");
    }
    
//    public String getCursor() throws java.rmi.RemoteException {
//        return ((org.jdesktop.lg3d.wg.Component3D)wrapped).getCursor();
//    }
    
    /**
     * Sets the transform component of this TransformGroup to the value of
     * the passed transform.
     * @param t1 the transform to be copied
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     * @exception BadTransformException if the transform is not affine.
     */
    public void setTransform(Transform3DRemote t1) throws java.rmi.RemoteException {
        // kludge to gain acccess to non-public setTransform method in Component3D
        LgNodeManager.c3dAccessHelper.setTransform((org.jdesktop.lg3d.wg.Component3D)wrapped, (org.jdesktop.lg3d.sg.Transform3D)SGObjectFactoryImpl.getFactoryImpl().getLocal(t1).getWrapped() );
    }

//    public float getRotationAngle() throws java.rmi.RemoteException {
//        return ((org.jdesktop.lg3d.wg.Component3D)wrapped).getRotationAngle();
//    }
//    
//    public float getTargetRotationAngle() throws java.rmi.RemoteException {
//        return ((org.jdesktop.lg3d.wg.Component3D)wrapped).getFinalRotationAngle();
//    }
//    
//    public javax.vecmath.Vector3f getRotationAxis() throws java.rmi.RemoteException {
//       throw new RuntimeException("to be removed");
//    }
//    
//    public float getScale() throws java.rmi.RemoteException {
//        return ((org.jdesktop.lg3d.wg.Component3D)wrapped).getScale();
//    }
//    
//    public float getTargetScale() throws java.rmi.RemoteException {
//        throw new RuntimeException("to be removed");
//    }
//    
//    public javax.vecmath.Vector3f getTranslation() throws java.rmi.RemoteException {
//        throw new RuntimeException("to be removed");
//    }
//    
//    public javax.vecmath.Vector3f getTargetTranslation() throws java.rmi.RemoteException {
//        throw new RuntimeException("to be removed");
//    }
    
    public void postEvent(LgEvent event) throws java.rmi.RemoteException {
        throw new RuntimeException("Not Implemented, use AppConnector");
    }
    
    public void setCapabilities() throws java.rmi.RemoteException {
        // ((org.jdesktop.lg3d.wg.Component3D)wrapped).setCapabilities();
        LgNodeManager.c3dAccessHelper.setCapabilities((org.jdesktop.lg3d.wg.Component3D)wrapped);
    }
    
//    public void setCursor(String cursorName) throws java.rmi.RemoteException {
//        ((org.jdesktop.lg3d.wg.Component3D)wrapped).setCursor(cursorName);
//    }
    
//    public void setRotationAngle(float angle) throws java.rmi.RemoteException {
//        ((org.jdesktop.lg3d.wg.Component3D)wrapped).setRotationAngle(angle);
//    }
//    
//    public void changeRotationAngle(float angle) throws java.rmi.RemoteException {
//        ((org.jdesktop.lg3d.wg.Component3D)wrapped).changeRotationAngle(angle);
//    }
//    
//    public void changeRotationAngle(float angle, int duration) throws java.rmi.RemoteException {
//        ((org.jdesktop.lg3d.wg.Component3D)wrapped).changeRotationAngle(angle, duration);
//    }
//    
//    public void setRotationAxis(javax.vecmath.Vector3f axis) throws java.rmi.RemoteException {
////        ((org.jdesktop.lg3d.wg.Component3D)wrapped).setRotationAxis(axis);
//    }
//    
//    public void setRotationAxis(float x, float y, float z) throws java.rmi.RemoteException {
//        ((org.jdesktop.lg3d.wg.Component3D)wrapped).setRotationAxis(x,y,z);
//    }
//    
//    public void setScale(float scale) throws java.rmi.RemoteException {
//        ((org.jdesktop.lg3d.wg.Component3D)wrapped).setScale(scale);
//    }
//    
//    public void changeScale(float scale) throws java.rmi.RemoteException {
//        ((org.jdesktop.lg3d.wg.Component3D)wrapped).changeScale(scale);
//    }
//    
//    public void changeScale(float scale, int duration) throws java.rmi.RemoteException {
//        ((org.jdesktop.lg3d.wg.Component3D)wrapped).changeScale(scale, duration);
//    }
    
    public void setSizeHint(float width, float height, float depth) throws java.rmi.RemoteException {
        ((org.jdesktop.lg3d.wg.Component3D)wrapped).setPreferredSize(new Vector3f(width,height,depth));
    }
    
    public Vector3f getSizeHint(Vector3f ret) throws java.rmi.RemoteException {
        return ((org.jdesktop.lg3d.wg.Component3D)wrapped).getPreferredSize(ret);
    }
    
    public void setTranslation(javax.vecmath.Vector3f loc) throws java.rmi.RemoteException {
//        ((org.jdesktop.lg3d.wg.Component3D)wrapped).setTranslation(loc);
    }
    
    public void changeTranslation(javax.vecmath.Vector3f loc) throws java.rmi.RemoteException {
        ((org.jdesktop.lg3d.wg.Component3D)wrapped).changeTranslation(loc);
    }
    
    public void changeTranslation(javax.vecmath.Vector3f loc, int duration) throws java.rmi.RemoteException {
        ((org.jdesktop.lg3d.wg.Component3D)wrapped).changeTranslation(loc, duration);
    }
    
    public void setTranslation(float x, float y, float z) throws java.rmi.RemoteException {
        ((org.jdesktop.lg3d.wg.Component3D)wrapped).setTranslation(x,y,z);
    }
    
    public void changeTranslation(float x, float y, float z) throws java.rmi.RemoteException {
        ((org.jdesktop.lg3d.wg.Component3D)wrapped).changeTranslation(x,y,z);
    }
    
    public void changeTranslation(float x, float y, float z, int duration) throws java.rmi.RemoteException {
        ((org.jdesktop.lg3d.wg.Component3D)wrapped).changeTranslation(x,y,z, duration);
    }
    
    public void setVisible(boolean visible) throws java.rmi.RemoteException {
        ((org.jdesktop.lg3d.wg.Component3D)wrapped).setVisible(visible);
    }
    
//    public void changeVisible(boolean visible) throws java.rmi.RemoteException {
//        ((org.jdesktop.lg3d.wg.Component3D)wrapped).changeVisible(visible);
//    }
//    
//    public void changeVisible(boolean visible, int duration) throws java.rmi.RemoteException {
//        ((org.jdesktop.lg3d.wg.Component3D)wrapped).changeVisible(visible, duration);
//    }
    
    public boolean isVisible() throws java.rmi.RemoteException {
	return ((org.jdesktop.lg3d.wg.Component3D)wrapped).isVisible();
    }
    
    public Cursor3DRemote getCursor() throws java.rmi.RemoteException {
        org.jdesktop.lg3d.wg.Cursor3D cursor 
                = ((org.jdesktop.lg3d.wg.Component3D)wrapped).getCursor();
        if (cursor == null) {
            return null;
        }
	return (Cursor3D)cursor.getUserData();
    }
    
    public void setCursor(Cursor3DRemote cursor) throws java.rmi.RemoteException {
        if (cursor==null)
            ((org.jdesktop.lg3d.wg.Component3D)wrapped).setCursor((org.jdesktop.lg3d.wg.Cursor3D)null);
        else
            ((org.jdesktop.lg3d.wg.Component3D)wrapped).setCursor((org.jdesktop.lg3d.wg.Cursor3D)((Cursor3D)cursor).wrapped );
    }
    
    public void requestParentToRevalidate() throws java.rmi.RemoteException {
	((org.jdesktop.lg3d.wg.Component3D)wrapped).requestParentToRevalidate();
    }
}

