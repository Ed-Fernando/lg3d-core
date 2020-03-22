/**
 * Project Looking Glass
 *
 * $RCSfile: DrawingSurfaceObjectAWT.java,v $
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
 * $Date: 2004-06-23 18:50:07 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.fws.x11.dso;

import java.awt.Canvas;

/**
 * The DrawingSurfaceObjectAWT class is used to manage the
 * native drawing surface.
 */

public class DrawingSurfaceObjectAWT {
    public native long getAWT();
    native boolean lockAWT(long ds);
    native void unlockAWT(long ds);
    static native void lockGlobal(long awt);
    static native void unlockGlobal(long awt);
    public native long getDrawingSurfaceAWT(Canvas cv, long awt);
    public native long getDrawingSurfaceInfo(long ds);
    static native void freeResource(long awt, long ds, long dsi);
    public native int getDrawingSurfaceWindowIdAWT(Canvas cv, long ds, long dsi,
 					    long display, int screenID,
					    boolean xineramaDisabled);
}
