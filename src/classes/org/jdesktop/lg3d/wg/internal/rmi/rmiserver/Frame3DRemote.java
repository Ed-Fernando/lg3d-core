/**
 * Project Looking Glass
 *
 * $RCSfile: Frame3DRemote.java,v $
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
 * $Revision: 1.3 $
 * $Date: 2005-01-20 22:06:20 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.internal.rmi.rmiserver;

import org.jdesktop.lg3d.wg.internal.wrapper.Component3DWrapper;

/**
 *
 * @author  Paul
 */
public interface Frame3DRemote extends Container3DRemote {
    
     public void setActive(boolean active) throws java.rmi.RemoteException;
     
     public boolean isActive() throws java.rmi.RemoteException;
     
     public void setThumbnail(Component3DRemote thumbnail) throws java.rmi.RemoteException;
     
     public Component3DRemote getThumbnail() throws java.rmi.RemoteException;
     
     public float getScreenWidth() throws java.rmi.RemoteException;
     
     public float getScreenHeight() throws java.rmi.RemoteException;
}
