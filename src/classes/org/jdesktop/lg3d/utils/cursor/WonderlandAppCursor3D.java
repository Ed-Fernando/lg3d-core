/**
 * Project Looking Glass
 *
 * $RCSfile: WonderlandAppCursor3D.java,v $
 *
 * Copyright (c) 2007, Sun Microsystems, Inc., All Rights Reserved
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
 * $Date: 2007-05-04 01:28:23 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.cursor;

import javax.vecmath.Color4f;

public class WonderlandAppCursor3D extends WigglingCursor {
    private static final String name = "WONDERLAND_APP_CURSOR_3D";

    public WonderlandAppCursor3D (float size) {
        super(name, size, false, new Color4f(0.65f, 1.0f, 0.65f, 1.0f), 230);
    }
}
