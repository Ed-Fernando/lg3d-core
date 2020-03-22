/**
 * Project Looking Glass
 *
 * $RCSfile: Frame3D.java,v $
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
 * $Date: 2005-04-14 23:05:23 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.internal.rmi.rmiclient;

import org.jdesktop.lg3d.wg.internal.wrapper.Frame3DWrapper;
import org.jdesktop.lg3d.wg.internal.wrapper.Component3DWrapper;

import org.jdesktop.lg3d.sg.internal.rmi.rmiclient.SceneGraphSetup;
import org.jdesktop.lg3d.wg.internal.rmi.rmiserver.Frame3DRemote;
import org.jdesktop.lg3d.wg.internal.rmi.rmiserver.Component3DRemote;

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

public class Frame3D extends Container3D implements Frame3DWrapper {

    /**
     * Constructs and initializes a new BranchGroup node object.
     */
    public Frame3D() {
    }
    
    protected void createRemote() {
        try {            
            remote = SceneGraphSetup.getSGObjectFactory().newFrame3D();
            setRemote( remote );
        } catch( java.rmi.RemoteException re ) {
            throw new RuntimeException( re );
        }
    }
    
    public void setActive(boolean active) {
        try {
            ((Frame3DRemote)remote).setActive(active);
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
    }
    
    public boolean getActive() {
        try {
            return ((Frame3DRemote)remote).isActive();
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
    }
    
    public void setThumbnail(Component3DWrapper thumbnail) {
        try {
            Component3DRemote thumbnailRemote
                = (thumbnail == null)
                    ?(null)
                    :(Component3DRemote)(((Component3D)thumbnail).remote);
            ((Frame3DRemote)remote).setThumbnail(thumbnailRemote);
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException(rex);
        }
    }
    
    public Component3DWrapper getThumbnail() {
        try {
            return (Component3D)getLocal(((Frame3DRemote)remote).getThumbnail());
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }
    
    public float getScreenWidth() {
        try {
            return ((Frame3DRemote)remote).getScreenWidth();
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }
    
    public float getScreenHeight() {
        try {
            return ((Frame3DRemote)remote).getScreenHeight();
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }
}
