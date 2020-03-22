/**
 * Project Looking Glass
 *
 * $RCSfile: Lg3dVolatileImage.java,v $
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
 * $Date: 2006-06-30 20:37:57 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.awt;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.ImageCapabilities;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;

/**
 *
 * @author paulby
 */
public class Lg3dVolatileImage extends VolatileImage {
    
    private BufferedImage image;
    
    /** Creates a new instance of Lg3dVolatileImage */
    Lg3dVolatileImage(int width, int height) {
        image = new BufferedImage(width,height, BufferedImage.TYPE_INT_ARGB);
    }

    public boolean contentsLost() {
        return false;
    }

    public Graphics2D createGraphics() {
        return image.createGraphics();
    }

    public ImageCapabilities getCapabilities() {
        throw new RuntimeException("Not Implemented");
    }

    public int getHeight() {
        return image.getHeight();
    }

    public int getHeight(java.awt.image.ImageObserver imageObserver) {
        return image.getHeight(imageObserver);
    }

    public Object getProperty(String str, java.awt.image.ImageObserver imageObserver) {
        return image.getProperty(str, imageObserver);
    }

    public java.awt.image.BufferedImage getSnapshot() {
        BufferedImage ret = new BufferedImage(image.getWidth(),image.getWidth(), BufferedImage.TYPE_INT_ARGB);
        ret.setData(image.getData());
        return ret;
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getWidth(java.awt.image.ImageObserver imageObserver) {
        return image.getWidth(imageObserver);
    }

    public int validate(GraphicsConfiguration graphicsConfiguration) {
        return IMAGE_OK;
    }
    
}
