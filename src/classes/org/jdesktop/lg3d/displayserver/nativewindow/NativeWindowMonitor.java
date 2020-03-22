/**
 * Project Looking Glass
 *
 * $RCSfile: NativeWindowMonitor.java,v $
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
 * $Revision: 1.7 $
 * $Date: 2006-02-24 22:14:08 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.nativewindow;


/**
 * Still work in progress.  This provides only basic features at this moment
 *
 * This provides a level of abstraction between the the native window manager 
 * and the native window implementation.
 *
 */
public interface NativeWindowMonitor {
    /**
     * Notification that the application has changed it's window size
     */
    public void sizeChanged(int w, int h);
    
    /**
     * Notification that the visibilty has been changed, for example
     * the app has been iconified
     *
     * Windows that are not visible will be unmapped. Invisible windows
     * no longer render.
     */
    public void visibilityChanged(boolean visible);
    
    /**
     * Notification that the X window has been moved by the application
     */
    public void locationChanged(int x, int y);
    
    /**
     * Notification that the name has changed
     *
     * In X11 the name is the window title.
     */
    public void nameChanged(String name);
    
    /**
     * Invoked when native window is destroyed
     */
    public void destroyed();
    
    /**
     * TODO Enum is X11 specific, this needs to be abstracted
     */
    public void shapeNotify(gnu.x11.Enum enumerate, int nrect);
    
    /**
     * 
     */
    public void setWindowProperety(int minIncWidth, int minIncHeight,
            int minWidth, int minHeight, int baseWidth, int baseHeight);
    
    /**
     *
     */
    public void associateWindow(NativeWindowMonitor nativeWinMonitor);
    
    /**
     *
     */
    public boolean isWindowAssociatable(NativeWindowControl nwc);

    public void restackWindow(NativeWindowMonitor sibwin, int order);
}
