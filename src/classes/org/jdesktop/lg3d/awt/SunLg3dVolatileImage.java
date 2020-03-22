/*
 * SunLg3dVolatileImage.java
 *
 * Created on July 11, 2006, 12:36 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jdesktop.lg3d.awt;

import java.awt.*;
import sun.awt.image.SunVolatileImage;

/**
 *
 * @author paulby
 */
public class SunLg3dVolatileImage extends SunVolatileImage {
    
    public SunLg3dVolatileImage(GraphicsConfiguration graphicsConfig,
                            int width, int height, int transparency, 
                            ImageCapabilities caps) {
        super(graphicsConfig,width,height,transparency, caps);
    }
    
    public int validate(GraphicsConfiguration graphicsConfiguration) {
        // The real vaildate paints an opaque white background on the window,
        // so don't call it. Not sure what side effects this will have....
        return IMAGE_OK;
    }
    
}
