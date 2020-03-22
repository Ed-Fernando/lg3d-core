/**
 * Project Looking Glass
 *
 * $RCSfile: NativeWindowLookAndFeel.java,v $
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
 * $Revision: 1.11 $
 * $Date: 2007-04-10 22:58:42 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.nativewindow;

import java.util.logging.Logger;
import org.jdesktop.lg3d.utils.shape.SimpleAppearance;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Cursor3D;


/**
 * Superclass for various implementations of the L&F of native X11
 * applications in LG3D.
 * 
 */
public abstract class NativeWindowLookAndFeel extends Component3D {
    protected static final Logger logger = Logger.getLogger("lg.wg");
    
    /**
     * Called by NativeWindow3D to give the look and feel the opportunity to
     * build the scene graph representing this window.
     *
     * This is called before root is attached to the scene graph, this call
     * should set any capabilities required on root for changes once it is
     * live
     */
    public abstract void initialize(NativeWindow3D nativeWindow,
        TiledNativeWindowImage appImage, boolean decorated, WindowShape shape,
        Cursor3D cursor);
    
    /**
     * Indicates that the size of the NativeWindowImage has changed
     */
    public abstract void sizeChangedNative( int width, int height );
    
    /**
     * Called by NativeWindow3D when the 3D size is changed
     */
    public abstract void sizeChanged3D( float width, float height );
    
    public abstract boolean isWindowAssociatable(NativeWindowControl nwc);
    
    /**
     * Associate another Native window, these are usually dialogs etc.
     */
    public abstract void associateWindow(NativeWindowMonitor nativeWinMonitor);
    
    /**
     * Dissociate a Native window, which is already associated
     */
    public abstract void dissociateWindow(NativeWindowMonitor nativeWinMonitor);
    
    /**
     * Associate another Native window, these are usually dialogs etc.
     */
    public abstract void associatePopup(Component3D popup); 

    /* TODO: eventually support these two correctly for all look and feels (i.e.
     classes which instantiate NativeWindowObject */

    public boolean isRemwin () { return false; }
    public void setIsRemwin (boolean isRemwin) {}

    public Cursor3D getNativeCursor () { return null; }
    public Component3D getNativeWindowBodyComponent () { return null; }
    public SimpleAppearance getDecorationAppearance () { return null; }
}
