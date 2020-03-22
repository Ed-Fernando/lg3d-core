/**
 * Project Looking Glass
 *
 * $RCSfile: Lg3dBackBuffer.java,v $
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
 * $Date: 2006-06-30 20:37:50 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.awt;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.Graphics2D;

/**
 *
 * @author paulby
 */
public class Lg3dBackBuffer {
    
    private BufferedImage backImage;
    private ArrayList<BufferFlipListener> flipListener = new ArrayList();
    private ArrayList<BufferResizeListener> resizeListener = new ArrayList();
    
    /**
     * Creates a new instance of Lg3dBackBuffer 
     */
    public Lg3dBackBuffer(int width, int height) {
        backImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }
    
    public void addBufferFlipListener(BufferFlipListener listener) {
        if (listener==null)
            throw new RuntimeException("Illegal listener");
        flipListener.add(listener);
    }
    
    public void addBufferResizeListener(BufferResizeListener listener) {
        if (listener==null)
            throw new RuntimeException("Illegal listener");
        resizeListener.add(listener);
    }
    
    /**
     * Ensure the back buffer is this size, resize if necessary
     *
     * Returns true if the buffer was resized
     */
    public boolean ensureSize(int width, int height) {
        if (backImage.getWidth()!=width || backImage.getHeight()!=height) { 
            //System.out.println("Image changed <---------------------------");
            BufferedImage oldImage = backImage;
            backImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
            backImage.setData(oldImage.getData());   
            notifyResize();
            return true;
        }
        return false;
    }
    
    private void notifyResize() {
        for(BufferResizeListener listener : resizeListener)
            listener.bufferResized(this);
    }
    
    /**
     * Inform the buffer that it's contents have changed, this will result in the buffer being
     * rendered.
     */
    public synchronized void contentsChanged() {
        //System.err.println("**** Render Image "+backImage.getWidth()+","+backImage.getHeight());
        for(BufferFlipListener listener : flipListener)
            listener.renderBuffer(this);
    }
    
    public BufferedImage getImage() {
        return backImage;
    }
    
    /**
     * Create and return a new graphics2D
     */
    public Graphics2D createGraphics() {
        return backImage.createGraphics();
    }
    
    public interface BufferFlipListener {
        public void renderBuffer(Lg3dBackBuffer buffer);
    }
    
    public interface BufferResizeListener {
        public void bufferResized(Lg3dBackBuffer buffer);
    }
    
}
