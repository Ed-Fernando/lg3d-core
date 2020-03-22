/**
 * Project Looking Glass
 *
 * $RCSfile: Tapp.java,v $
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
 * $Date: 2005-04-14 23:05:02 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg;

/**
 * Tiny Application
 *
 * These are small applications that can appear anywhere in the scene, things
 * such as taskbar items, clocks, and animated/interactive 'icons'
 *
 * @author  paulby
 */
public class Tapp extends Frame3D {
    
    /** Creates a new instance of Tapp
     * Unlike a frame3D Tapps are visible by default.
     */
    public Tapp() {
        super();
        changeVisible(true);
    }
    
    public void setActive( boolean active ) {
        throw new RuntimeException("Not Implemented");
    }
    
}
