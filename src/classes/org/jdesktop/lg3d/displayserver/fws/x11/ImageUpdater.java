/**
 * Project Looking Glass
 *
 * $RCSfile: ImageUpdater.java,v $
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
 * $Date: 2006-08-25 00:31:49 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.fws.x11;

import java.awt.image.DataBufferUShort;
import java.awt.image.DataBufferInt;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import javax.media.j3d.ImageComponent2D;
import org.jdesktop.lg3d.displayserver.fws.FoundationWinSys;
import org.jdesktop.lg3d.displayserver.fws.WindowContents;
import org.jdesktop.lg3d.displayserver.nativewindow.J3dNativeWindowImageComponent2D;

class ImageUpdater implements ImageComponent2D.Updater 
{
    private boolean loadSubregions = true;

    //  depth
    private static final int DEPTH16 = 16;
    private static final int DEPTH24 = 24;
    
    private FoundationWinSys fws = FoundationWinSys.getFoundationWinSys();

    // TODO: for now, we assume that there is just one tile.
    // Load the entire X11 image into it.

    public synchronized void updateData(ImageComponent2D image, int x, int y, 
			   int width, int height) {

        J3dNativeWindowImageComponent2D nwic2d = (J3dNativeWindowImageComponent2D) image;

        DataBufferUShort dbi16;
        DataBufferInt dbi24;
        short[] shrtAry = null;
        int[] intAry = null;
        //prn("In ImageUpdater:");
	//prn("x = " + x);
	//prn("y = " + y);
	//prn("width  = " + width);
	//prn("height = " + height);
	//prn("image width = " + image.getWidth());
	//prn("image height = " + image.getHeight());

        BufferedImage bi = image.getImage();
	WritableRaster ras = bi.getRaster();
	if(fws.getDefaultVisualDepth() == DEPTH16) {
	    dbi16 = (DataBufferUShort) ras.getDataBuffer();
	    shrtAry = dbi16.getData();
	} else if (fws.getDefaultVisualDepth() == DEPTH24) {
	    dbi24 = (DataBufferInt) ras.getDataBuffer();
	    intAry = dbi24.getData();
	}

	WindowContents wc = (WindowContents) nwic2d.getUserData2();

	if (loadSubregions) {
	    //System.err.println("SUBREGION");
	    if(fws.getDefaultVisualDepth() == DEPTH16) {
	        DamageEventBroker.copyImageRect16(shrtAry, x, y, width, height,
						  x, y, image.getWidth(), 
						  wc.width, wc.height,
						  wc.nativeMemoryPointer);
	    }else if(fws.getDefaultVisualDepth() == DEPTH24) {
		
		//System.err.println("copyImageRect32");
		//System.err.println("x = " + x);
		//System.err.println("y = " + y);
		//System.err.println("width = " + width);
		//System.err.println("height = " + height);
		//System.err.println("image.getWidth = " + image.getWidth());
		//System.err.println("image.getHeight = " + image.getHeight());

	        DamageEventBroker.copyImageRect32(intAry, x, y, width, height,
						  x, y, image.getWidth(),
						  wc.width, wc.height,
						  wc.nativeMemoryPointer);
	    }
 	} else {
	    // TODO: this is now obsolete
            //System.err.println("ENTIRE IMAGE");
	    if(fws.getDefaultVisualDepth() == DEPTH16) {
		DamageEventBroker.copyImageEntire16(shrtAry, image.getWidth(), 
						    wc.width, wc.height, 
						    wc.nativeMemoryPointer);
	    }else if(fws.getDefaultVisualDepth() == DEPTH24) {
		DamageEventBroker.copyImageEntire32(intAry, image.getWidth(), 
						    wc.width, wc.height, 
						    wc.nativeMemoryPointer);
	    }
	}
    }

    private void prn (String str) { System.err.println(str); }
}

