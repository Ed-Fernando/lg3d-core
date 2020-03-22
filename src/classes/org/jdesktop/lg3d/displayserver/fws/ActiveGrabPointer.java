/**
 * Project Looking Glass
 *
 * $RCSfile: ActiveGrabPointer.java,v $
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
 * $Date: 2005-06-24 19:47:52 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.displayserver.fws;

import javax.media.j3d.Node;
import org.jdesktop.lg3d.wg.Cursor3D;

public class ActiveGrabPointer extends Grab {

    private Cursor3D cursor;

    public ActiveGrabPointer (Node grabNode, Cursor3D cursor) {
	super(grabNode);
	this.cursor = cursor;
    }

    public void setCursor (Cursor3D cursor) { 
	this.cursor = cursor;
    }

    public Cursor3D getCursor () { return cursor; }
}
