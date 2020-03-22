/**
 * Project Looking Glass
 *
 * $RCSfile: NativeBacksideWindow3D.java,v $
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
 * $Date: 2006-06-08 01:17:16 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.nativewindow;

import gnu.x11.Visual;


/**
 * NativeBacksideWinow3D is for the use as a window on the backside.
 * This ignores the reposition initiated by the native side in order to
 * keep the window at the center of the backside.
 */
public class NativeBacksideWindow3D extends NativePopup3D {
    private boolean inDestroyed = false;
    
    /**
     * Creates a native window, the presence of decorations can be
     * controlled via the decorated parameter.
     *
     * @param decorated indicates if the NativeWindow3D should
     * have any decorations
     */
    public NativeBacksideWindow3D(NativeWindowControl nativeWinContl, 
        boolean decorated, int depth, Visual visual, 
        NativeWindow3D preDefinedParent, int fuzzyEdgeWidth, boolean shadow) 
    {
        super(nativeWinContl, decorated, depth, visual, preDefinedParent, fuzzyEdgeWidth, shadow);
    }
    
    public void locationChanged(int x, int y) {
        // ignore in order to keep this window at the center of the backside
        // of the associated window.
    }
    
    public void destroyed() {
        inDestroyed = true;
        super.destroyed();
        inDestroyed = false;
    }
    
    public void detach() {
        // Do not detach when invoked as a result of the 
        // X11Client.destroyNotify() call (which calles this.destroyed()).
        if (!inDestroyed) {
            super.detach();
        }
    }
}
