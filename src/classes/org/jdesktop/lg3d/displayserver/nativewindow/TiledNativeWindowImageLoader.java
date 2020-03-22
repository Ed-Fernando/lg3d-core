/**
 * Project Looking Glass
 *
 * $RCSfile: TiledNativeWindowImageLoader.java,v $
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
 * $Date: 2005-01-20 22:05:30 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.nativewindow;

import org.jdesktop.lg3d.sg.ImageComponent2D;
import org.jdesktop.lg3d.displayserver.fws.WindowContents;

/** 
 * Used by the TiledNativeWindowImage.invokeUpdaterForAllTiles method.
 */

public interface TiledNativeWindowImageLoader {
    
    /**
     * Requests the given loader to load the given image tile with the 
     * rectangle (srcX, srcY, width, height) from the given foundation
     * window system image. 
     */
    public void loadTile (ImageComponent2D tile, int srcX, int srcY, 
			  int width, int height, WindowContents winContents);
}
