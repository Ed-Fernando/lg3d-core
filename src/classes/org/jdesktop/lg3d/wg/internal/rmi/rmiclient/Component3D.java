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
 * $Date: 2005-04-14 23:05:22 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.internal.rmi.rmiclient;

import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.wg.internal.wrapper.Component3DWrapper;
import org.jdesktop.lg3d.wg.internal.wrapper.Cursor3DWrapper;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.wg.event.LgEvent;

import org.jdesktop.lg3d.sg.internal.rmi.rmiclient.SceneGraphSetup;
import org.jdesktop.lg3d.sg.internal.rmi.rmiclient.Transform3D;
import org.jdesktop.lg3d.sg.internal.wrapper.Transform3DWrapper;
import org.jdesktop.lg3d.wg.internal.rmi.rmiserver.Component3DRemote;
import org.jdesktop.lg3d.wg.internal.rmi.rmiserver.Cursor3DRemote;


/**
 * The BranchGroup serves as a pointer to the root of a
 * scene graph branch; BranchGroup objects are the only objects that
 * can be inserted into a Locale's set of objects. A subgraph, rooted
 * by a BranchGroup node can be thought of as a compile unit. The
 * following things may be done with BranchGroup:
 * <P><UL>
 * <LI>A BranchGroup may be compiled by calling its compile method. This causes the
 * entire subgraph to be compiled. If any BranchGroup nodes are contained within the
 * subgraph, they are compiled as well (along with their descendants).</LI>
 * <p>
 * <LI>A BranchGroup may be inserted into a virtual universe by attaching it to a
 * Locale. The entire subgraph is then said to be live.</LI>
 * <p>
 * <LI>A BranchGroup that is contained within another subgraph may be reparented or
 * detached at run time if the appropriate capabilities are set.</LI>
 * </UL>
 * Note that that if a BranchGroup is included in another subgraph, as a child of
 * some other group node, it may not be attached to a Locale.
 *
 * @version 	1.54, 04/01/28 13:11:07188

 */

public class Component3D extends LgBranchGroup implements Component3DWrapper {

    /**
     * Constructs and initializes a new BranchGroup node object.
     */
    public Component3D() {
    }
    
    protected void createRemote() {
        try {            
            remote = SceneGraphSetup.getSGObjectFactory().newComponent3D();
            setRemote( remote );
        } catch( java.rmi.RemoteException re ) {
            throw new RuntimeException( re );
        }
    }
    
    public void addListener(LgEventListener listener, Class evtClass) {
        throw new RuntimeException("Not Implemented, use AppConnector");
    }
    
    public void addListener(LgEventListener listener, Class evtClass, Class source) {
        throw new RuntimeException("Not Implemented, use AppConnector");
    }
    
//    public float getRotationAngle() {
//        try {
//            return ((Component3DRemote)remote).getRotationAngle();
//        } catch( java.rmi.RemoteException rex ) {
//            throw new RuntimeException(rex);
//        }
//    }
//    
//    public float getTargetRotationAngle() {
//        try {
//            return ((Component3DRemote)remote).getTargetRotationAngle();
//        } catch( java.rmi.RemoteException rex ) {
//            throw new RuntimeException(rex);
//        }
//    }
//    
//    public javax.vecmath.Vector3f getRotationAxis() {
//        try {
//            return ((Component3DRemote)remote).getRotationAxis();
//        } catch( java.rmi.RemoteException rex ) {
//            throw new RuntimeException(rex);
//        }
//    }
//    
//    public float getScale() {
//        try {
//            return ((Component3DRemote)remote).getScale();
//        } catch( java.rmi.RemoteException rex ) {
//            throw new RuntimeException(rex);
//        }
//    }
//    
//    public float getTargetScale() {
//        try {
//            return ((Component3DRemote)remote).getTargetScale();
//        } catch( java.rmi.RemoteException rex ) {
//            throw new RuntimeException(rex);
//        }
//    }
//    
//    public javax.vecmath.Vector3f getTranslation() {
//        try {
//            return ((Component3DRemote)remote).getTranslation();
//        } catch( java.rmi.RemoteException rex ) {
//            throw new RuntimeException(rex);
//        }
//    }
//    
//    public javax.vecmath.Vector3f getTargetTranslation() {
//        try {
//            return ((Component3DRemote)remote).getTargetTranslation();
//        } catch( java.rmi.RemoteException rex ) {
//            throw new RuntimeException(rex);
//        }
//    }
    
    public void postEvent(LgEvent event) {
        throw new RuntimeException("Not Implemented, use AppConnector");
    }
    
    public void setCapabilities() {
        try {
            ((Component3DRemote)remote).setCapabilities();
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
    }
    
    public void setCursor(Cursor3DWrapper cursor) {
        try {
            if (cursor==null)
                ((Component3DRemote)remote).setCursor(null);
            else
                ((Component3DRemote)remote).setCursor((Cursor3DRemote)((Cursor3D)cursor).remote);
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
    }
    
    public Cursor3DWrapper getCursor() {
        try {
            return (Cursor3D)getLocal(((Component3DRemote)remote).getCursor());
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
    }
    
//    public void setRotationAngle(float angle) {
//        try {
//            ((Component3DRemote)remote).setRotationAngle(angle);
//        } catch( java.rmi.RemoteException rex ) {
//            throw new RuntimeException(rex);
//        }
//    }
//    
//    public void changeRotationAngle(float angle) {
//        try {
//            ((Component3DRemote)remote).changeRotationAngle(angle);
//        } catch( java.rmi.RemoteException rex ) {
//            throw new RuntimeException(rex);
//        }
//    }
//    
//    public void changeRotationAngle(float angle, int duration) {
//        try {
//            ((Component3DRemote)remote).changeRotationAngle(angle, duration);
//        } catch( java.rmi.RemoteException rex ) {
//            throw new RuntimeException(rex);
//        }
//    }
//    
//    public void setRotationAxis(javax.vecmath.Vector3f axis) {
//        try {
//            ((Component3DRemote)remote).setRotationAxis(axis);
//        } catch( java.rmi.RemoteException rex ) {
//            throw new RuntimeException(rex);
//        }
//    }
//    
//    public void setRotationAxis(float x, float y, float z) {
//        try {
//            ((Component3DRemote)remote).setRotationAxis(x,y,z);
//        } catch( java.rmi.RemoteException rex ) {
//            throw new RuntimeException(rex);
//        }
//    }
//    
//    public void setScale(float scale) {
//        try {
//            ((Component3DRemote)remote).setScale(scale);
//        } catch( java.rmi.RemoteException rex ) {
//            throw new RuntimeException(rex);
//        }
//    }
//    
//    public void changeScale(float scale) {
//        try {
//            ((Component3DRemote)remote).changeScale(scale);
//        } catch( java.rmi.RemoteException rex ) {
//            throw new RuntimeException(rex);
//        }
//    }
//    
//    public void changeScale(float scale, int duration) {
//        try {
//            ((Component3DRemote)remote).changeScale(scale, duration);
//        } catch( java.rmi.RemoteException rex ) {
//            throw new RuntimeException(rex);
//        }
//    }
    
    public void setSizeHint(float width, float height, float depth) {
        try {
            ((Component3DRemote)remote).setSizeHint(width, height, depth);
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
    }
    
    public Vector3f getSizeHint(Vector3f ret) {
        try {
            return ((Component3DRemote)remote).getSizeHint(ret);
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
    }
//    public void setTranslation(javax.vecmath.Vector3f loc) {
//        try {
//            ((Component3DRemote)remote).setTranslation(loc);
//        } catch( java.rmi.RemoteException rex ) {
//            throw new RuntimeException(rex);
//        }
//    }
//    
//    public void changeTranslation(javax.vecmath.Vector3f loc) {
//        try {
//            ((Component3DRemote)remote).changeTranslation(loc);
//        } catch( java.rmi.RemoteException rex ) {
//            throw new RuntimeException(rex);
//        }
//    }
//    
//    public void changeTranslation(javax.vecmath.Vector3f loc, int duration) {
//        try {
//            ((Component3DRemote)remote).changeTranslation(loc, duration);
//        } catch( java.rmi.RemoteException rex ) {
//            throw new RuntimeException(rex);
//        }
//    }
//    
//    public void setTranslation(float x, float y, float z) {
//        try {
//            ((Component3DRemote)remote).setTranslation(x,y,z);
//        } catch( java.rmi.RemoteException rex ) {
//            throw new RuntimeException(rex);
//        }
//    }
//    
//    public void changeTranslation(float x, float y, float z) {
//        try {
//            ((Component3DRemote)remote).changeTranslation(x,y,z);
//        } catch( java.rmi.RemoteException rex ) {
//            throw new RuntimeException(rex);
//        }
//    }
//    
//    public void changeTranslation(float x, float y, float z, int duration) {
//        try {
//            ((Component3DRemote)remote).changeTranslation(x,y,z, duration);
//        } catch( java.rmi.RemoteException rex ) {
//            throw new RuntimeException(rex);
//        }
//    }
    
    public void setVisible(boolean visible) {
        try {
            ((Component3DRemote)remote).setVisible(visible);
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
    }
    
//    public void changeVisible(boolean visible) {
//        try {
//            ((Component3DRemote)remote).changeVisible(visible);
//        } catch( java.rmi.RemoteException rex ) {
//            throw new RuntimeException(rex);
//        }
//    }
//    
//    public void changeVisible(boolean visible, int duration) {
//        try {
//            ((Component3DRemote)remote).changeVisible(visible, duration);
//        } catch( java.rmi.RemoteException rex ) {
//            throw new RuntimeException(rex);
//        }
//    }
    
    public boolean isVisible() {
        try {
            return ((Component3DRemote)remote).isVisible();
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
    }
    
//    public boolean getTargetVisible() {
//        try {
//            return ((Component3DRemote)remote).getTargetVisible();
//        } catch( java.rmi.RemoteException rex ) {
//            throw new RuntimeException(rex);
//        }
//    }
    
    public void setTransform(Transform3DWrapper transform) {
        try {
            ((Component3DRemote)remote).setTransform( ((Transform3D)transform).remote );
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }

    public void addAnimationTarget(org.jdesktop.lg3d.sg.internal.wrapper.TransformGroupWrapper tg) {
        throw new RuntimeException("Not implemented yet");
    }

    public void removeAnimationTarget(org.jdesktop.lg3d.sg.internal.wrapper.TransformGroupWrapper tg) {
        throw new RuntimeException("Not implemented yet");
    }
    
    public void requestParentToRevalidate() {
        try {
            ((Component3DRemote)remote).requestParentToRevalidate();
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
    }
}
