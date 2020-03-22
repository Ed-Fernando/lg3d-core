/**
 * Project Looking Glass
 *
 * $RCSfile: J3dContainer3D.java,v $
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
 * $Date: 2006-01-31 04:30:34 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.internal.j3d.j3dnodes;

/**
 * Component3D object is a top level object for widget development.
 *
 * It supports visual rendering by adding scene graph node children 
 * and event handling.
 */
public class J3dContainer3D extends J3dComponent3D {
    private J3dComponent3D decoration = null;
    
    public void setDecoration(J3dComponent3D deco) {
	if (decoration != null) {
	    getDecoGroup().removeChild(decoration);
	}
	decoration = deco;
	if (decoration != null) {
	    getDecoGroup().addChild(decoration);
	}
    }
    
    public J3dComponent3D getDecoration() {
	return decoration;      
    }
}
