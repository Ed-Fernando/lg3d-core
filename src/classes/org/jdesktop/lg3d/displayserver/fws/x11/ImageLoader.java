/**
 * Project Looking Glass
 *
 * $RCSfile: ImageLoader.java,v $
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
 * $Revision: 1.1 $
 * $Date: 2006-03-07 00:16:58 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.fws.x11;

import org.jdesktop.lg3d.displayserver.fws.FoundationWinSys;
import org.jdesktop.lg3d.displayserver.fws.WindowContents;
import org.jdesktop.lg3d.displayserver.nativewindow.TiledNativeWindowImageLoader;
import org.jdesktop.lg3d.displayserver.nativewindow.J3dNativeWindowImageComponent2D;

class ImageLoader implements TiledNativeWindowImageLoader 
{
    private ImageUpdater updater;

    public ImageLoader (ImageUpdater updater) {
	this.updater = updater;
    }

    public void loadTile (org.jdesktop.lg3d.sg.ImageComponent2D tile, int srcX, int srcY, 
			  int width, int height, WindowContents winContents) {

        org.jdesktop.lg3d.sg.internal.j3d.j3dwrapper.ImageComponent2D jzic2d = 
            (org.jdesktop.lg3d.sg.internal.j3d.j3dwrapper.ImageComponent2D) tile.getWrapped();
        J3dNativeWindowImageComponent2D nwic2d = 
		(J3dNativeWindowImageComponent2D) jzic2d.getWrapped();
		    
	nwic2d.setUserData2(winContents);
	//System.err.println("Before ic2d.updateData");
	//System.err.println("srcX   = " + srcX);
	//System.err.println("srcY   = " + srcY);
	//System.err.println("width  = " + width);
	//System.err.println("height = " + height);

	// just update the visible region on the image. this case happend when
	// resizing occure.
	if (srcX < 0) {
	    srcX = 0;
	}
	if (srcY < 0) {
	    srcY = 0;
	}
	
	int w = nwic2d.getWidth();
	int h = nwic2d.getHeight();
	if (((srcX + width) > w)) {
	    width = w - srcX;
	}
	
	if (((srcY + height) > h)) {
	    height = h - srcY;
	}
//	// For debug
//	int w = nwic2d.getWidth();
//	int h = nwic2d.getHeight();
//	if ((srcX < 0) || (srcY < 0) || ((srcX + width) > w) || ((srcY + height) > h)) {
//	    System.err.println("ImageLoader.loadTile: nwic2.updateData is may going to fail");
//	    System.err.println("srcX   = " + srcX);
//	    System.err.println("srcY   = " + srcY);
//	    System.err.println("width  = " + width);
//	    System.err.println("height = " + height);
//	    System.err.println("w      = " + w);
//	    System.err.println("h      = " + h);
//      }
	
	nwic2d.updateData(updater, srcX, srcY, width, height);
    }
}
