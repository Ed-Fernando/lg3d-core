/**
 * Project Looking Glass
 *
 * $RCSfile: CursorImageListener.java,v $
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
 * $Revision: 1.6 $
 * $Date: 2005-01-20 22:05:18 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.fws;

import org.jdesktop.lg3d.sg.ImageComponent2D;

/**
 * CursorImageListener defines a listener object that can be registered with
 * instances of FoundationWinSys. This listener defines a method which is
 * called whenever the foundation window system's current cursor image and/or 
 * hotspot changes.
 */

public interface CursorImageListener {

    /**
     * This method is called whenever the foundation window system's current 
     * cursor image has changed.
     *
     * Note: the only time the hotspot can change is when a new cursor image is
     * defined.
     *
     * @param image the foundation window system's currently displayed cursor
     * image. The width and height have been rounded up to the nearest power
     * of two so that a texture can easily be created with the image.
     * @param actualWidth The actual width of the cursor image
     * (not rounded to a power of two)
     * @param actualHeight The actual height of the cursor image
     * (not rounded to a power of two)
     * @param hotX the screen absolute X coordinate of the current hot spot
     * (relative to the origin of the cursor image).
     * @param hotY the screen absolute Y coordinate of the current hot spot
     * (relative to the origin of the cursor image).
     */
    public void imageChanged (ImageComponent2D image, int actualWidth, int actualHeight, int hotX, int hotY);
}

