/** Copyright (c) 2004 Amir Bukhari
 *
 * Permission to use, copy, modify, distribute, and sell this software and its
 * documentation for any purpose is hereby granted without fee, provided that
 * the above copyright notice appear in all copies and that both that
 * copyright notice and this permission notice appear in supporting
 * documentation, and that the name of Amir Bukhari not be used in
 * advertising or publicity pertaining to distribution of the software without
 * specific, written prior permission.  Amir Bukhari makes no
 * representations about the suitability of this software for any purpose.  It
 * is provided "as is" without express or implied warranty.
 *
 * AMIR BUKHARI DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE,
 * INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS, IN NO
 * EVENT SHALL AMIR BUKHARI BE LIABLE FOR ANY SPECIAL, INDIRECT OR
 * CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE,
 * DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER
 * TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 * PERFORMANCE OF THIS SOFTWARE.
 */
/**
 * Project Looking Glass
 *
 * $RCSfile: NativePopupLookAndFeel.java,v $
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
 * $Revision: 1.7 $
 * $Date: 2007-04-10 22:58:42 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.nativewindow;

import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Frame3D;
import org.jdesktop.lg3d.wg.Cursor3D;
import java.util.logging.Logger;

/**
 * Superclass for various implementations of the L&F of native X11
 * applications in LG3D.
 * 
 */
public abstract class NativePopupLookAndFeel extends Component3D {
    protected static final Logger logger = Logger.getLogger("lg.wg");
    
    /**
     * Called by NativeWindow3D to give the look and feel the opportunity to
     * build the scene graph representing this window.
     *
     * This is called before root is attached to the scene graph, this call
     * should set any capabilities required on root for changes once it is
     * live
     */
    public abstract void initialize(NativePopup3D nativeWindow,
        TiledNativeWindowImage appImage, boolean decorated, WindowShape shape,
        int fuzzyEdgeWidth, boolean shadow, Cursor3D cursor);
    
    /**
     * Indicates that the size of the NativeWindowImage has changed
     */
    public abstract void sizeChangedNative(int width, int height);
    
    /**
     * Called by NativeWindow3D when the 3D size is changed
     */
    public abstract void sizeChanged3D(float width, float height, 
				       int nativeWidth, int nativeHeight);

    public boolean isRemwin () { return false; }
    public void setIsRemwin (boolean isRemwin) {}

    public Cursor3D getNativeCursor () { return null; }
    public Component3D getNativeWindowBodyComponent () { return null; }
}
