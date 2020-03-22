/**
 * Project Looking Glass
 *
 * $RCSfile: TileBufferImage.java,v $
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
 * $Date: 2006-03-07 00:17:02 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.nativewindow;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

class TileBufferedImage extends BufferedImage {

    public TileBufferedImage (int width, 
            int height,
            int imageType) {
        super(width, height, imageType);
    }
        
    public WritableRaster copyData(WritableRaster outRaster) {
        if (outRaster == null) {
            return (WritableRaster) getData();
        }
        int width = outRaster.getWidth();
        int height = outRaster.getHeight();
        int startX = outRaster.getMinX();
        int startY = outRaster.getMinY();

        if ((getRaster().getHeight() < height) || (getRaster().getWidth() < width)) {
            height = getRaster().getHeight();
            width = getRaster().getWidth();
        }
        
        Object tdata = null;

        for (int i = startY; i < startY+height; i++)  {
            tdata = this.getRaster().getDataElements(startX,i,width,1,tdata);
            outRaster.setDataElements(startX,i,width,1, tdata);
        }

        return outRaster;
    }
}

