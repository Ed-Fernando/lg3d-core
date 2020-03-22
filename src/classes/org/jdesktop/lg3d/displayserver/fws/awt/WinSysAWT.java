/**
 * Project Looking Glass
 *
 * $RCSfile: WinSysAWT.java,v $
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
 * $Revision: 1.10 $
 * $Date: 2008-03-12 14:31:26 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.fws.awt;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Locale;
import javax.media.j3d.Node;
import com.sun.j3d.utils.universe.ViewInfo;
import org.jdesktop.lg3d.displayserver.fws.FoundationWinSys;
import org.jdesktop.lg3d.displayserver.fws.Grab;
import org.jdesktop.lg3d.displayserver.fws.PassiveGrab;
import org.jdesktop.lg3d.displayserver.fws.PassiveGrabButton;
import org.jdesktop.lg3d.displayserver.fws.PassiveGrabKey;
import org.jdesktop.lg3d.displayserver.fws.ActiveGrabPointer;
import org.jdesktop.lg3d.displayserver.fws.ActiveGrabKeyboard;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.displayserver.nativewindow.TiledNativeWindowImage;
import org.jdesktop.lg3d.scenemanager.CursorModule;
import org.jdesktop.lg3d.wg.Cursor3D;
import org.jdesktop.lg3d.displayserver.EventProcessor;
import javax.media.j3d.BranchGroup;
import javax.swing.SwingUtilities;

/**
 * Implementation of the FoundationWinSys with AWT to interface between the
 * Display Server and Java base clients using AWT events.
 *
 * A FoundationWinSys implementation using AWT for use in development when
 * the X11 integration is not required or when the underlying platform is
 * not X based.
 * @author  paulby
 */
public class WinSysAWT extends FoundationWinSys {
    
    private static PickEngineAWT pickEngine;

    /**
     * Creates a new instance of WinSysAWT
     * @param canvas the canvases attached to this FWS
     * @param locale the universe locale of the FWS
     * @param viewInfo the viewing info of the FWS
     */
    public WinSysAWT(Canvas3D[] canvases, Locale locale, ViewInfo viewInfo,
		     BranchGroup mgmtBG, EventProcessor eventProcessor) {

	// if there is already an instance of the FWS then throw an exception
        if (winSys!=null)
            throw new RuntimeException("WinSys should only be instantiated once");
        
        winSys = this;
        this.canvases = canvases;
        this.viewInfo = viewInfo;
        this.locale = locale;

	// create a new picking engine
        pickEngine = new PickEngineAWT ( locale, eventProcessor );

        mgmtBG.addChild( pickEngine );
        
        String robot = System.getProperty("lg.robotmode");
        if (robot!=null) {
            Lg3dRobot recorder = new Lg3dRobot();
            if (robot.equalsIgnoreCase("record")) {
                addMouseMotionListener(recorder);
                addMouseListener(recorder);
                addMouseWheelListener(recorder);
                addKeyListener(recorder);
                recorder.enableRecording(true);
            } else if (robot.equalsIgnoreCase("playback")) {
                recorder.enablePlayback(true);
                addKeyListener(recorder);
            }
        }
    }
    
    public static PickEngineAWT getPickEngineAWT() {
	return pickEngine;
    }

    public Canvas3D getCanvasForRootWin (long win) { 
	return canvases[0];
    }

    /**
     * Add a focus listener to the FWS (not implemented)
     * @param l the focus listener to add
     * @see #removeFocusListener
     * @see #getFocusListeners
     */
    protected void addFocusListener(FocusListener l) {
        throw new RuntimeException("Not Implemented");
    }
    
    /**
     * Add a key listener to the FWS
     * @param l the key listener to add
     * @see #removeKeyListener
     * @see #getKeyListeners
     */
    protected void addKeyListener(KeyListener l) {
        for(int i=0; i<canvases.length; i++)
            canvases[i].addKeyListener(l);
    }
    
    /**
     * Add a mouse listener to the FWS
     * @param l the mouse listener to add
     * @see #removeMouseListener
     * @see #getMouseListeners
     */
    protected void addMouseListener(MouseListener l) {
        for(int i=0; i<canvases.length; i++)
            canvases[i].addMouseListener(l);
    }
    
    /**
     * Add a mouse motion listener to the FWS
     * @param l the mouse motion listener to add
     * @see #removeMouseMotionListener
     * @see #getMouseMotionListeners
     */
    protected void addMouseMotionListener(MouseMotionListener l) {
        for(int i=0; i<canvases.length; i++)
            canvases[i].addMouseMotionListener(l);
    }
    
    /**
     * Add a mouse wheel listener to the FWS (not implemented)
     * @param l the mouse wheel listener to add
     * @see #removeMouseWheelListener
     * @see #getMouseWheelListeners
     */
    protected void addMouseWheelListener(MouseWheelListener l) {
        for(int i=0; i<canvases.length; i++)
            canvases[i].addMouseWheelListener(l);
    }
    
    /**
     * Add a window resize listener to the FWS (not implemented)
     * @param wid the id of the window to listen to
     * @param l the window resize listener to add
     * @see #removeWindowResizeListener
     * @see #getWindowResizeListener
     */
    public void addWindowResizeListener(long wid, org.jdesktop.lg3d.displayserver.fws.WindowResizeListener l) {
        throw new RuntimeException("Not Implemented");
    }
    
    /**
     * Get the canvas id with the given window id (not implemented)
     * @param wid the id of the window
     * @return the id of the canvas with the give window id
     */
    public long getCanvasWid(int wid) {
        throw new RuntimeException("Not Implemented");
    }
    
    /**
     * Get the list of focus listeners (not implemented)
     * @return the list of focus listeners
     * @see #addFocusListener
     * @see #removeFocusListener
     */
    protected FocusListener[] getFocusListeners() {
        throw new RuntimeException("Not Implemented");
    }
    
    /** Get the list of key listeners (not implemented)
     * @return the list of key listeners
     * @see #addKeyListener
     * @see #removeKeyListener
     */
    protected KeyListener[] getKeyListeners() {
        throw new RuntimeException("Not Implemented");
    }
    
    /**
     * Get the list of mouse listeners (not implemented)
     * @return the list of mouse listeners
     * @see #addMouseListener
     * @see #removeMouseListener
     */
    protected MouseListener[] getMouseListeners() {
        throw new RuntimeException("Not Implemented");
    }
    
    /**
     * Get the list of mouse motion listeners (not implemented)
     * @return the list of mouse motion listeners
     * @see #addMouseMotionListener
     * @see #removeMouseMotionListener
     */
    protected MouseMotionListener[] getMouseMotionListeners() {
        throw new RuntimeException("Not Implemented");
    }
    
    /**
     * Get the list of mouse wheel listeners (not implemented)
     * @return the list of mouse wheel listeners
     * @see #addMouseWheelListener
     * @see #removeMouseWheelListener
     */
    protected MouseWheelListener[] getMouseWheelListeners() {
        throw new RuntimeException("Not Implemented");
    }
    
    /**
     * Get the height of the screen (not implemented)
     * @return the height of the screen
     */
    public int getScreenHeight() {
        throw new RuntimeException("Not Implemented");
    }
    
    /**
     * Get the width of the screen (not implemented)
     * @return the width of the screen
     */
    public int getScreenWidth() {
        throw new RuntimeException("Not Implemented");
    }
    
    /**
     * Get the window resize listener for the given window id (not implemented)
     * @param wid the id of the window to get the listener for
     * @return the listener
     */
    public org.jdesktop.lg3d.displayserver.fws.WindowResizeListener getWindowResizeListener(long wid) {
        throw new RuntimeException("Not Implemented");
    }
    
    /**
     * remove the focus listener (not implemented)
     * @param l the listener to remove
     * @see #addFocusListener
     * @see #getFocusListeners
     */
    protected void removeFocusListener(FocusListener l) {
        throw new RuntimeException("Not Implemented");
    }
    
    /**
     * remove the key listener (not implemented)
     * @param l the listener to remove
     * @see #addKeyListener
     * @see #getFocusListeners
     */
    protected void removeKeyListener(KeyListener l) {
        throw new RuntimeException("Not Implemented");
    }
    
    /**
     * remove the mouse listener (not implemented)
     * @param l the listener to remove
     * @see #addMouseListener
     * @see #getMouseListeners
     */
    protected void removeMouseListener(MouseListener l) {
        throw new RuntimeException("Not Implemented");
    }
    
    /**
     * remove the mouse motion listener (not implemented)
     * @param l the listener to remove
     * @see #addMouseMotionListener
     * @see #getMouseMotionListeners
     */
    protected void removeMouseMotionListener(MouseMotionListener l) {
        throw new RuntimeException("Not Implemented");
    }
    
    /**
     * remove the mouse wheel listener (not implemented)
     * @param l the listener to remove
     * @see #addMouseWheelListener
     * @see #getMouseWheelListeners
     */
    protected void removeMouseWheelListener(MouseWheelListener l) {
        throw new RuntimeException("Not Implemented");
    }
    
    /**
     * remove the window resize listener (not implemented)
     * @param wid the id of the window the listener is listening to
     * @param l the listener to remove
     * @see #addWindowResizeListener
     * @see #getWindowResizeListener
     */
    public void removeWindowResizeListener(long wid, org.jdesktop.lg3d.displayserver.fws.WindowResizeListener l) {
        throw new RuntimeException("Not Implemented");
    }
    
    /**
     * add a cursor image listener (not implemented)
     * @param l the listener to add
     * @see #removeCursorImageListener
     * @see #getCursorImageListeners
     */
    public void addCursorImageListener(org.jdesktop.lg3d.displayserver.fws.CursorImageListener l) {
        throw new RuntimeException("Not Implemented");
    }
    
    /**
     * get the list of cursor image listeners (not implemented)
     * @return the list of listeners
     * @see #addCursorImageListener
     * @see #removeCursorImageListeners
     */
    public org.jdesktop.lg3d.displayserver.fws.CursorImageListener[] getCursorImageListeners() {
        throw new RuntimeException("Not Implemented");
    }
    
    /**
     * check if the cursor is visible (not implemented)
     * @return true if the cursor is visible, false if not
     * @see #setCursorVisible
     */
    public boolean isCursorVisible() {
        throw new RuntimeException("Not Implemented");
    }
    
    /**
     * remove the cursor image listener (not implemented)
     * @param l the listener to remove
     * @see #addCursorImageListener
     * @see #getCursorImageListeners
     */
    public void removeCursorImageListener(org.jdesktop.lg3d.displayserver.fws.CursorImageListener l) {
        throw new RuntimeException("Not Implemented");
    }
    
    /**
     * Set the cursor's visibility (not implemented)
     * @param visible true - make the cursor visible, false - hide the cursor
     * @see #isCursorVisible
     */
    public void setCursorVisible(boolean visible) {
        throw new RuntimeException("Not Implemented");
    }
    
    /**
     * This allows the FWS to generate enter and exit events as the pointer is
     * moved in and out of the native window. It also allows the FWS to provide 
     * click-to-front behavior for the interior of the native window.
     *
     * @param wid The window ID of the native window
     */
    public void trackEventsForNativeWindow (long wid) {
        logger.warning("trackEnterExitForNativeWindow should not be called on WinSysAWT, assuming development mode...");
    }

    public void untrackEventsForNativeWindow (long wid) {
        logger.warning("untrackEnterExitForNativeWindow should not be called on WinSysAWT, assuming development mode...");
    }

    /**
     * @param wid The window ID of the native window
     */
    public void trackButtonPressForNativeWindow (long wid, Component3D enterExitListener) {
        logger.warning("trackEnterExitForNativeWindow should not be called on WinSysAWT, assuming development mode...");
    }

    /**
     * track the damage (the image parts that need repainting) of a window
     * (should not be called for WinSysAWT)
     * @param wid the id of the window to track
     * @param image the image of the window
     */
    public void trackDamageForNativeWindow (long wid, TiledNativeWindowImage image) {
        //throw new RuntimeException("Illegal call, trackDamageForWindow should never be called on WinSysAWT");
        logger.warning("trackDamageForNativeWindow should not be called on WinSysAWT, assuming development mode...");
    }
    
    public void destroyNativeWindow (long wid) {
        logger.warning("destroyNativeWindow should not be called on WinSysAWT, assuming development mode...");
    }

    public void setCursorModule (CursorModule cursorModule) {
        pickEngine.setCursorModule(cursorModule);
    }

    public CursorModule getCursorModule () {
	return pickEngine.getCursorModule();
    }

    public void grabPointer (Node grabNode, Cursor3D cursor) {
	logger.fine("Enter WSX.grabPointer, grabNode = " + grabNode);

	Grab grab = new ActiveGrabPointer(grabNode, cursor);

	synchronized(pickEngine.activeGrabState) {
	    boolean succeeded = pickEngine.activeGrabState.grabEnable(true, grab, false);
	    if (!succeeded) {
		throw new RuntimeException("AWT GrabPointer error: already grabbed");
	    }
	}
    }

    public void ungrabPointer () {
	logger.fine("Enter WSAWT.ungrabPointer");
	synchronized(pickEngine.activeGrabState) {
	    pickEngine.activeGrabState.grabDisable(true);
	}	
    }

    public void grabKeyboard (Node grabNode) {
	logger.fine("Enter WSAWT.grabKeyboard, grabNode = " + grabNode);

	Grab grab = new ActiveGrabKeyboard(grabNode);

	synchronized(pickEngine.activeGrabState) {
	    boolean succeeded = pickEngine.activeGrabState.grabEnable(false, grab, false);
	    if (!succeeded) {
		throw new RuntimeException("AWT GrabKeyboard error: already grabbed");
	    }
	}
    }

    public void ungrabKeyboard () {
	logger.fine("Enter WSAWT.ungrabKeyboard");
	synchronized(pickEngine.activeGrabState) {
	    pickEngine.activeGrabState.grabDisable(true);
	}	
    }

    public void grabButton (int buttonNum, long modifiers, Node grabNode, 
	                       Cursor3D cursor) {
	logger.fine("Enter WSAWT.grabButton, buttonNum = " + buttonNum + ", modifiers = " + 
		    modifiers + ", grabNode = " + grabNode);
	PassiveGrabButton grab = new PassiveGrabButton(buttonNum, modifiers, grabNode, cursor);
	PassiveGrab.add(grab);
    }

    public void ungrabButton (int buttonNum, long modifiers, Node grabNode) {
	logger.fine("Enter WSAWT.ungrabButton");
	PassiveGrabButton grab = new PassiveGrabButton(buttonNum, modifiers, grabNode, null);
	PassiveGrab.delete(grab);
    }

    public void grabKey (int keySym, long modifiers, Node grabNode) {
	logger.fine("Enter WSAWT.grabKey, keySym = " + keySym + ", modifiers = " + 
		    modifiers + ", grabNode = " + grabNode);
	// TODO: should this be the keyCode or the keySym??
	PassiveGrabKey grab = new PassiveGrabKey(keySym, modifiers, grabNode);
	PassiveGrab.add(grab);
    }

    public void ungrabKey (int keySym, long modifiers, Node grabNode) {
	logger.fine("Enter WSAWT.ungrabKey");
	// TODO: should this be the keyCode or the keySym??
	PassiveGrabKey grab = new PassiveGrabKey(keySym, modifiers, grabNode);
	PassiveGrab.delete(grab);
    }
}

