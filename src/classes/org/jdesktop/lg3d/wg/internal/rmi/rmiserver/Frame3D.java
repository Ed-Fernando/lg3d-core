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
 * $Date: 2005-04-14 23:05:27 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.internal.rmi.rmiserver;



import java.awt.event.*;

import java.util.HashSet;
import org.jdesktop.lg3d.sg.internal.rmi.rmiserver.Node;
import org.jdesktop.lg3d.sg.internal.rmi.rmiserver.NodeRemote;
import org.jdesktop.lg3d.sg.internal.rmi.rmiserver.BranchGroupRemote;
import org.jdesktop.lg3d.wg.internal.rmi.rmiserver.Component3DRemote;
import org.jdesktop.lg3d.wg.event.LgEventSource;

public class Frame3D extends Container3D implements Frame3DRemote {

    public Frame3D() throws java.rmi.RemoteException {
    }
    
    protected void createWrapped() {
        wrapped = new org.jdesktop.lg3d.wg.Frame3D();
        wrapped.setUserData( this );
    }
            
    public boolean isActive() throws java.rmi.RemoteException {
        return ((org.jdesktop.lg3d.wg.Frame3D)wrapped).isEnabled();
    }
    
    public void setActive(boolean active) throws java.rmi.RemoteException {
        ((org.jdesktop.lg3d.wg.Frame3D)wrapped).setEnabled(active);
    }
    
    public void setThumbnail(Component3DRemote thumbnail) throws java.rmi.RemoteException {
        org.jdesktop.lg3d.wg.Component3D thumbnailWrapped 
            = (thumbnail == null)
                ?(null)
                :(org.jdesktop.lg3d.wg.Component3D)((Component3D)thumbnail).getWrapped();
	((org.jdesktop.lg3d.wg.Frame3D)wrapped).setThumbnail((org.jdesktop.lg3d.wg.Thumbnail)thumbnailWrapped);
    }
    
    public Component3DRemote getThumbnail() throws java.rmi.RemoteException {
        org.jdesktop.lg3d.wg.Component3D thumbnail = ((org.jdesktop.lg3d.wg.Frame3D)wrapped).getThumbnail();
	return (thumbnail == null)?(null):((Component3D)thumbnail.getUserData());
    }
    
    public float getScreenWidth() throws java.rmi.RemoteException {
        return ((org.jdesktop.lg3d.wg.Frame3D)wrapped).getScreenWidth();
    }
    
    public float getScreenHeight() throws java.rmi.RemoteException {
        return ((org.jdesktop.lg3d.wg.Frame3D)wrapped).getScreenHeight();
    }
}

