/**
 * Project Looking Glass
 *
 * $RCSfile: J3dCursor3D.java,v $
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
 * $Date: 2004-06-23 18:52:23 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.internal.j3d.j3dnodes;

/**
  */
public class J3dCursor3D extends J3dComponent3D {

    private String name;
    
    public J3dCursor3D() {
        super(false);
    }
    
    public void setName( String cursorName ) {
        name = cursorName;
    }
    
    public String getName() {
        return name;
    }
}
