/**
 * Project Looking Glass
 *
 * $RCSfile: Container3DRemote.java,v $
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
 * $Date: 2006-01-31 04:30:37 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.internal.rmi.rmiserver;

/**
 *
 * @author  Paul
 */
public interface Container3DRemote extends Component3DRemote {
    public void setDecoration(Component3DRemote component3D) throws java.rmi.RemoteException;
    public Component3DRemote getDecoration() throws java.rmi.RemoteException;
}
