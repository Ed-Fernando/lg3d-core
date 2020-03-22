/**
 * Project Looking Glass
 *
 * $RCSfile: ShmSegInfo.java,v $
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
 * $Revision: 1.2 $
 * $Date: 2005-01-20 22:05:26 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.displayserver.fws.x11;

public class ShmSegInfo {
    public long shmSegId;   // XID 
    public int shmid;    	// kernel ID
    public long shmAddr;    // Native pointer 
    boolean readOnly;       // Accesibility 
}

