/**
 * Project Looking Glass
 *
 * $RCSfile: Cursor3D.java,v $
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
 * $Date: 2004-06-23 18:52:27 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.internal.j3d.j3dwrapper;


import org.jdesktop.lg3d.wg.internal.wrapper.Cursor3DWrapper;
import org.jdesktop.lg3d.wg.internal.j3d.j3dnodes.J3dCursor3D;


/**
 * Component3D object is a top level object for widget development.
 *
 * It supports visual rendering by adding scene graph node children 
 * and event handling.
 */
public class Cursor3D extends Component3D implements Cursor3DWrapper {

    /**
     * Creates a new instance of Component3D.
     * 
     * By default, it is visible (i.e. getVisible() returns 'ture')
     * and it doesn't have any asscociated cursor.
     */
    public Cursor3D() {
    }
    
    protected void createWrapped() {
        wrapped = new J3dCursor3D();
        wrapped.setUserData(this);
    }

    public void setName(String cursorName) {
        ((J3dCursor3D)wrapped).setName(cursorName);
    }
    
    public String getName() {
        return ((J3dCursor3D)wrapped).getName();
    }
}
