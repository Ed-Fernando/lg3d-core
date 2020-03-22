/**
 * Project Looking Glass
 *
 * $RCSfile: FoundationWinSys.java,v $
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
 * $Revision: 1.12 $
 * $Date: 2007-04-10 22:58:41 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.fws;

import java.lang.reflect.Constructor;
import java.io.IOException;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.ViewInfo;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.displayserver.nativewindow.TiledNativeWindowImage;
import org.jdesktop.lg3d.scenemanager.CursorModule;
import org.jdesktop.lg3d.wg.Cursor3D;
import java.util.logging.Logger;
import org.jdesktop.lg3d.wg.Cursor3D;

/**
 * FoundationWinSys is an abstract class which defines an interface
 * between the Display Server and the underlying window system.
 * This class should be subclassed for specific foundation 
 * window systems.
 */

public abstract class FoundationWinSys {
    
    /** the FoundationWinSys singleton */
    protected static FoundationWinSys winSys;
    /** The list of canvases attached to this FWS */
    protected Canvas3D[] canvases;
    /** The list of pseudo-root window WIDs */
    protected long[] prws;
    /** The universe locale of the FWS */
    protected Locale locale;
    /** Viewing info of the FWS universe */
    protected ViewInfo viewInfo;
    /** default depth */
    protected int defaultVisualDepth;
    /** the logger instance */
    protected Logger logger = Logger.getLogger("lg.fws");
    
    /**
     * Returns the current FoundationWinSys. 
     *
     * The winsys object must have been previously instantiated using
     * the constructor of one of the subclasses
     * @return the FWS singleton
     */
    public static FoundationWinSys getFoundationWinSys() {
        return winSys;
    }
    
    /**
     * get the width of the underlying screen (in pixels).
     * @return the width of the underlying screen
     */
    public abstract int getScreenWidth();

    /**
     * get the height of the underlying screen (in pixels).
     * @return the height of the underlying screen
     */
    public abstract int getScreenHeight();

    /**
     * get the universe Locale of the FWS
     * @return the universe Locale
     */
    public Locale getLocale() {
        return locale;
    }
    
    // TODO: X-specific!!!
    public long getRootWid (int i) { return 0;  }

    /**
     * get the canvas of the pseudo-root window of the given
     * screen number.
     * @param i the screen number
     * @return the canvas of the pseudo-root window
     */
    public Canvas3D getCanvas(int i) {
        return canvases[i];
    }

    /**
     * get the number of Canvas3Ds
     * @return the number of canvases
     */
    public int getNumCanvases() {
        return canvases.length;
    }
    
    /**
     * get the canvas of the given pseudo-root window.
     * @param win pseudo-root window
     * @return the canvas of the pseudo-root window.
     */
    public Canvas3D getWinCanvas(long win) { 
        Canvas3D canvas = null;
        for (int i=0; i < prws.length; i++) {
            if (prws[i] == win) {
                canvas = canvases[i];
            }
	}
        return canvas; 
    }	

    /**
     * get the default depth.
     * @return return the default depth
     */    
    public int getDefaultVisualDepth() {
        return defaultVisualDepth;
    }

    /**
     * get the window ID of the pseudo-root window of the given
     * screen number.
     * @param i the screen number
     * @return the window id of the window in FWS
     */
    public abstract long getCanvasWid(int i);
     
    /**
     * Returns the pseudo-root window canvas which corresponds to
     * the given root window
     *
     * @param    rootWinID   The XID of the root window.
     */
    public abstract Canvas3D getCanvasForRootWin (long rootWinID);

    /**
     * Adds the specified sprite listener so it can listen for window system 
     * events. 
     *
     * @param    l   the cursor image listener
     * @see      org.jdesktop.lg3d.displayserver.fws.CursorImageEventListener
     * @see      #removeCursorImageListener
     * @see      #getCursorImageListeners
     */
    public abstract void addCursorImageListener (CursorImageListener l);
	
    /**
     * Removes the specified cursor image listener so that it no longer receives
     * receives cursor image events. This method performs no function, nor does
		 * it throw an exception, if the listener specified by the argument was not 
     * previously registered.
     *
     * @param    l   the cursor image listener
     * @see      org.jdesktop.lg3d.displayserver.fws.CursorImageEventListener
     * @see      #addCursorImageListener
     * @see      #getCursorImageListeners
     */
    public abstract void removeCursorImageListener(CursorImageListener l);

    /**
     * Returns an array of all the cursor image listeners registered for this 
     * window system.
     *
     * @return all of this window system's <code>CursorImageListener</code>s
     *         or an empty array if no cursor image
     *         listeners are currently registered
     *
     * @see      #addCursorImageListener
     * @see      #removeCursorImageListener
     */
    public abstract CursorImageListener[] getCursorImageListeners();

    /**
     * Registers the specified window resize listener so it can listen for
     * resize events for a particular window. Only one listener per window 
     * may be registered. If there already is a registered listener for the
     * particular window, it is replaced with the specified listener.
     *
     * @param    wid the id of the window to listen to
     * @param    l   the window resize listener
     * @see      #removeWindowResizeListener
     * @see      #getWindowResizeListener
     */
    public abstract void addWindowResizeListener (long wid, WindowResizeListener l);
	
    /**
     * Removes the specified window resize listener so that it no longer
     * receives window resize events for the given window. This method performs 
     * no function, nor does it throw an exception, if the listener specified
     * by the argument was not previously registered.
     *
     * @param    wid the id of the window to listen to
     * @param    l   the window resize listener
     * @see      java.awt.event.WindowResizeListener
     * @see      #addWindowResizeListener
     * @see      #getWindowResizeListener
     */
    public abstract void removeWindowResizeListener (long wid, WindowResizeListener l);

    /**
     * Returns the window resize listener which has been registered for a
     * particular window.
     *
     * @param    wid the id of the window to listen to
     * @return the window's <code>WindowResizeListener</code>s or null if no
     * window resize listener is currently registered for this window
     *
     * @see      #addWindowResizeListener
     * @see      #removeWindowResizeListener
     */
    public abstract WindowResizeListener getWindowResizeListener (long wid);

    /**
     * This allows the FWS to generate enter and exit events as the pointer is
     * moved in and out of the native window. It also allows the FWS to provide 
     * click-to-front behavior for the interior of the native window.
     *
     * @param wid The window ID of the native window
     */
    public abstract void trackEventsForNativeWindow (long wid); 

    /**
     * Undoes the effect of trackEventsForNativeWindow
     *
     * @param wid The window ID of the native window for which to stop tracking
     * enter and exit events.
     */
    public abstract void untrackEventsForNativeWindow (long wid); 

    /**
     * Associates a native window with an image. Damage to the window will be 
     * tracked and damaged areas will be updated in the image. If image is null, 
     * the association is removed and damage tracking for this window will cease.
     *
     * Damage should be tracked only when the window is mapped. Therefore, 
     * after the window is mapped, you can call this method with a non-null 
     * image.And, prior to unmapping the window, you must call this method 
     * a null image.
     *    
     * Note: the client must not use the userData of the image. This is used
     * by the foundation window system.
     *
     * @param    wid   The window ID of the native window
     * @param    image Image in which damaged areas are to be updated
     */
    public abstract void trackDamageForNativeWindow (long wid, 
						     TiledNativeWindowImage image);

    /** 
     * Post damage of a rectangular region to the given window.
     * Called after a native window is resized to ensure that we
     * fetch the most recent contents window image into the most recent
     * Java3D image. Note: If the DS native window doesn't make this call
     * there can be a race condition which causes some popup menus to be
     * all black (see issue 214).
     */ 

     public void damageWindowRectangle (long wid, int x, int y, int w, int h) {}

    /**
     * This method should be called when a native window is destroyed. It frees 
     * client-side resources for the native window, such as damage tracking resources.
     *
     * @param    wid   Window which has been destroyed.
     */
    public abstract void destroyNativeWindow (long wid);

    /** 
     * get the viewing info of the universe
     * @return the viewing info of the universe
     */
    public ViewInfo getViewInfo() {
        return viewInfo;
    }

    public abstract void setCursorModule (CursorModule cursorModule);

    public abstract CursorModule getCursorModule ();

    // Manage active grabs
    public abstract void grabPointer (Node grabNode, Cursor3D cursor);
    public abstract void ungrabPointer ();
    public abstract void grabKeyboard (Node grabNode);
    public abstract void ungrabKeyboard ();

    // Manage passive grabs
    public abstract void grabButton (int buttonNum, long modifiers, Node grabNode, 
				     Cursor3D cursor);
    public abstract void ungrabButton (int buttonNum, long modifiers, Node grabNode);
    public abstract void grabKey (int keySym, long modifiers, Node grabNode);
    public abstract void ungrabKey (int keySym, long modifiers, Node grabNode);

    // Used by appshare
    public void compositeTopLevelWindows (Object fwsInstance) {}
}
