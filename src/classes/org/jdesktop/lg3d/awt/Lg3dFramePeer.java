/**
 * Project Looking Glass
 *
 * $RCSfile: Lg3dFramePeer.java,v $
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
 * $Date: 2006-08-15 23:57:41 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.awt;
import java.awt.Image;
import java.awt.MenuBar;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.peer.*;

/**
 *
 * @author paulby
 */
public class Lg3dFramePeer extends Lg3dWindowPeer implements java.awt.peer.FramePeer {

    private int state;
    private Rectangle maximizedBounds;
    
    /** Creates a new instance of Lg3dFramePeer */
    public Lg3dFramePeer(java.awt.peer.FramePeer realPeer, java.awt.Component frame, Toolkit osToolkit) {
        super(realPeer, frame, osToolkit);    
    }

    public void setBoundsPrivate(int x, int y, int width, int height) {
        logger.info("setBoundsPrivate");
        if (usePeer)
            ((FramePeer)realPeer).setBoundsPrivate(x,y,width,height);
        else
            throw new RuntimeException("Not Implemented");
    }

    // Removed in Mustang b86
    public void setIconImage(Image image) {
//        logger.info("setIconImage");
//        if (usePeer)
//            ((FramePeer)realPeer).setIconImage(image);
//        else
            throw new RuntimeException("Not Implemented");
    }

    public void setMaximizedBounds(Rectangle rectangle) {
        logger.info("setMaximizedBounds "+rectangle);
        if (usePeer)
            ((FramePeer)realPeer).setMaximizedBounds(rectangle);
        
        // TODO in the jdk peers the bounds are passed to the native peer.
    }

    public void setMenuBar(MenuBar menuBar) {
        logger.info("setMenuBar");
        if (usePeer)
            ((FramePeer)realPeer).setMenuBar(menuBar);
        else
            throw new RuntimeException("Not Implemented");
    }

    public void setResizable(boolean param) {
        logger.info("setResizable");
        if (usePeer)
            ((FramePeer)realPeer).setResizable(param);
        else
            throw new RuntimeException("Not Implemented");
    }

    public void setState(int param) {
        logger.finer("setState");
        if (usePeer)
            ((FramePeer)realPeer).setState(param);
        else
            this.state = param;
    }
    
    public int getState() {
        logger.finer("getState");
        if (usePeer)
            return ((FramePeer)realPeer).getState();
        else
            return state;
    }


    public void setTitle(String str) {
        logger.info("setTitle");
        if (usePeer)
            ((FramePeer)realPeer).setTitle(str);
        peer.setTitle(str);
    }

    public Rectangle getBoundsPrivate() {
        throw new RuntimeException("Not Implemented");
    }


}
