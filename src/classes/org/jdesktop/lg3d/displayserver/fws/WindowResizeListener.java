/**
 * Project Looking Glass
 *
 * $RCSfile: WindowResizeListener.java,v $
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
 * $Date: 2006-05-23 18:32:34 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.fws;

import javax.media.j3d.*;
import org.jdesktop.lg3d.displayserver.nativewindow.TiledNativeWindowImage;

/**
 * WindowResizeListener defines a listener object that can be registered with
 * instances of DamageEventBroker. This listener defines a method which 
 * DamageEventBroker can call whenever it detects that the dimensions of 
 * a window have changed.
 */

public interface WindowResizeListener {

    /**
     * This method is called whenever DamageEventBroker detects that the size
     * of a window has changed. This method is expected to allocate a new image
     * of type TiledNativeWindowImage of the new size, associate the tiles of
     * this image with the window's various texture tiles, and return the image.
     *
     * After calling this method, and before making the associated image
     * textures visible, the caller must call TiledNativeWindowImage.
     * waitFilled() on the returned image. This is because, after this method
     * returns, the FWS will begin loading the latest window contents into the
     * image and into its associated texture tiles. The client must wait for
     * the the waitFilled method to return attaching the texture tiles to the
     * Appearance of a Shape3D. This will prevent the user from seeing garbage
     * texels in the newly created tiles of an enlarged window.
     *
     * @param wid the window ID of the window
     * @param newWidth the new window width
     * @param newHeight the new window height
     *
     * @return a new image of the new size
     *
     * @throws java.lang.IllegalArgumentException
     *    If newWidth or newHeight are <= 0.
     *
     * @see      #addWindowResizeListener
     * @see      #removeWindowResizeListener
     */

    TiledNativeWindowImage sizeChanged (long wid, int newWidth, int newHeight)
	throws IllegalArgumentException;
}

