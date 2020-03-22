/**
 * Project Looking Glass
 *
 * $RCSfile: X11Client.java,v $
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
 * $Revision: 1.22 $
 * $Date: 2007-04-10 23:25:09 $
 * $State: Exp $
 */
/*
    This file is derived from escher-0.2.2/gnu/app/puppet/Client.java
    of Escher 0.2.2.  Here is the copyright notice of the original file:

Copyright (c) 2000-2004, Stephen Tse
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:

    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above
      copyright notice, this list of conditions and the following
      disclaimer in the documentation and/or other materials provided
      with the distribution.
    * Neither the name of the organization nor the names of its
      contributors may be used to endorse or promote products derived
      from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.jdesktop.lg3d.displayserver.nativewindow.x11;

import gnu.x11.Atom;
import gnu.x11.Display;
import gnu.x11.Enum;
import gnu.x11.Visual;
import gnu.x11.Window;
import gnu.x11.event.ConfigureNotify;
import gnu.x11.event.Event;
import gnu.x11.extension.NotFoundException;
import java.util.logging.Logger;
import org.jdesktop.lg3d.displayserver.nativewindow.NativeInputOnlyWindow3D;
import org.jdesktop.lg3d.displayserver.nativewindow.NativeWindowControl;
import org.jdesktop.lg3d.displayserver.nativewindow.NativeWindowMonitor;
import org.jdesktop.lg3d.displayserver.nativewindow.NativeWindow3D;
import org.jdesktop.lg3d.displayserver.nativewindow.NativePopup3D;
import org.jdesktop.lg3d.displayserver.nativewindow.NativeBacksideWindow3D;


final class X11Client extends Window implements NativeWindowControl {
    private static final Logger logger = Logger.getLogger("lg.x11");
    static final int UNMANAGED = 0;
    static final int NORMAL    = 1;
    static final int HIDDEN    = 2;
    static final int NO_FOCUS  = 3;
    static final int DESTROYED = 4;
    
    private static X11WindowAssociator windowAssociator = new X11WindowAssociator();
    
    private String name;
    int    state;
    Window.AttributesReply attributes;
    WMClassHint classHint;
    WMSizeHints sizeHints;    
    
    private boolean isMappedBefore = false; // need by X11WM.maprequest
    private boolean isMapped = false;
    private X11WindowManager wm;
    private NativeWindowMonitor nativeWinMonitor;
    private int winClass = -1;
    private Atom netWindowType;
    private Enum shapeRect;
    private int rectNum;
    private int visualID = -1;
    private int depth;    
    private int minIncWidth = 0;
    private int minIncHeight = 0;
    private int minWidth = 0;
    private int minHeight = 0;
    private int maxWidth = 0;
    private int maxHeight = 0;
    private int baseWidth = 0;
    private int baseHeight = 0;
    private int oldX = Integer.MIN_VALUE;
    private int oldY = Integer.MIN_VALUE;
    private int borderWidth = 0;
    private int unmaximizedWidth;
    private int unmaximizedHeight;
    private int unmaximizedX;
    private int unmaximizedY;
    
    // window features
    private boolean maximizable = true;
    private boolean maximized = false;
    private boolean minimizable = true;
    private boolean closeable = true;
    private boolean resizable = true;
    private boolean normalWindow = true;
    private boolean decorated = true; // need by splash window, which is not popup!!
    private boolean dockable = true;
    
    private X11Client associatedPrimaryWindow = null;
    
    
    X11Client(X11WindowManager x11wm, int id) {
	super(x11wm.getDisplay(), id);
        wm = x11wm;
    }
    
    private void initWindowProperety() {
        
        sizeHints = this.wm_normal_hints();
        if (sizeHints == null) {
	    nativeWinMonitor.setWindowProperety(1, 1, 1, 1, 0, 0);
	    return;
	}

        if ((sizeHints.flags() & WMSizeHints.PMIN_SIZE_MASK) != 0) {
            minWidth = sizeHints.min_width();
            minHeight = sizeHints.min_height();
        } else if ((sizeHints.flags() & WMSizeHints.PBASE_SIZE_MASK) != 0) {
            minWidth = sizeHints.read4(92); //base_width
            minHeight = sizeHints.read4(96);//base_hight
        } else {
            minWidth = minHeight = 1;
        }
        if ((minWidth == 0) || minHeight == 0) {
            minWidth = minHeight = 1;
        }
        if ((sizeHints.flags() & WMSizeHints.PBASE_SIZE_MASK) != 0) {
            baseWidth = sizeHints.read4(92); //base_width
            baseHeight = sizeHints.read4(96);//base_hight            
        } else if ((sizeHints.flags() & WMSizeHints.PMIN_SIZE_MASK) != 0) {
            baseWidth = sizeHints.min_width();
            baseHeight = sizeHints.min_height();
        } else {
            baseWidth = baseHeight = 0;
        }
        
        if ((sizeHints.flags() & WMSizeHints.PMAX_SIZE_MASK) != 0) {
            maxWidth = sizeHints.max_width(); 
            maxHeight = sizeHints.max_height();            
        } else {
            maxWidth = maxHeight = 0;
        }
        
        if (sizeHints.presizeIncrement()) {
            minIncWidth = sizeHints.inc_width();
            minIncHeight = sizeHints.inc_height();            
        } else {
            minIncWidth = 1;
            minIncHeight = 1;
        }
        if ((minIncWidth == 0) || minIncHeight == 0) {
            minIncWidth = minIncHeight = 1;
        }
        nativeWinMonitor.setWindowProperety(minIncWidth, 
                minIncHeight, minWidth, minHeight, baseWidth, baseHeight);
    }
    
    void initWindow3DRepresentation(boolean decorated, boolean inputOnly,
        int depth, Visual visual) 
    {    
        this.depth = depth;            
        
        // avoid recreation of 3D object each time mapNotify by X11WM received.
        if (nativeWinMonitor != null) {
            return;
        }
        
        logger.fine("X11Client created: "
                + id + ": "
                + ((name == null)?(""):("\"" + name + "\" "))
                + ((classHint == null)?("[] "):(classHint.toString() + " "))
                + width + "x" + height + "+" + x + "+" + y + " "
                + ((inputOnly)?"(inputonly) ":" ") 
                + ((!decorated)?"(non-decorated) ":" "));
        
        if (!inputOnly) {
            if (decorated) {
                // check if this window is configured to be associated with
                // another window (i.e. a backside window of another window)
                associatedPrimaryWindow = windowAssociator.getAssociatedWindow(this);
                
                if (associatedPrimaryWindow != null) {
                    NativeWindow3D nwm = (NativeWindow3D)(associatedPrimaryWindow.nativeWinMonitor);
                    if (nwm.isWindowAssociatable(this)) {
                        nativeWinMonitor 
                            = new NativeBacksideWindow3D(this, decorated, depth, visual, nwm, 4, false);
                        nwm.associateWindow(nativeWinMonitor);
                    } else {
                        nativeWinMonitor = new NativeWindow3D(this, decorated, depth, visual);
                    }
                } else {
                    // normal case
                    nativeWinMonitor = new NativeWindow3D(this, decorated, depth, visual);
                }
            } else {
                nativeWinMonitor = new NativePopup3D(this, decorated, depth, visual, null, 1, true);
            }
        } else {
            // we support only non-decorated one.
            nativeWinMonitor = new NativeInputOnlyWindow3D(this, false);
        }
    }
    
    public static Object intern(X11WindowManager x11wm, int id) {
	Object value = x11wm.getDisplay().resources.get(new Integer(id));
	if (value != null && value instanceof X11Client) {
	    return value;
	}
	return new X11Client(x11wm, id);
    }

    private static final String [] STATE_STRINGS = {
	"unmanaged",
	"normal",
	"hidden",
	"no-focus",
        "destroyed",
    };
    
    public String toString () {
	return "#X11Client " 
	    + ((name == null)?(""):("\"" + name + "\" "))
	    + ((classHint == null)?("("):(classHint.toString() + " ("))
	    + STATE_STRINGS[state] + ") "
	    + super.toString();
    }

    void createNotify() {
        // anything to do?
    }

    void mapNotify() {
        if (winClass == 2) {
            logger.fine("Mapping InputOnly : " + this);
        } else {
            logger.fine("Mapping InputOutput : " + this);
        }
        /* kludge: as in X11WindowManager we receive wrong mapNotify. */
        if (nativeWinMonitor == null) {
            return;
        }
        setMapped(true);
        // now we can initialize SizeHints structure
        initWindowProperety();
        nativeWinMonitor.visibilityChanged(true);
        borderWidth = this.geometry().border_width();
    }

    void moveAndSizeWindow() {
        if (nativeWinMonitor == null) {
            return;
        }
        if (isMapped()) {
	    if (oldX != x || oldY != y) {
		nativeWinMonitor.locationChanged(x, y);
		oldX = x;
		oldY = y;
	    }
	}	
    }

    public void restackWindow(X11Client sibWin, int order) {
        if (nativeWinMonitor == null) {
            return;
        }
        switch(order) {
        case Window.Changes.ABOVE:
            NativeWindowMonitor sibNwm = (sibWin != null)?(sibWin.nativeWinMonitor):(null);
            nativeWinMonitor.restackWindow(sibNwm, order);
	    break;
	default:
	    throw new RuntimeException("Unimplemented restack order");
        }
    }

    void unmapNotify() {
        if (nativeWinMonitor == null) {
            return;
        }
        if (isMapped()) {
            if (associatedPrimaryWindow != null) {
                // An window attached to the backside of another window gets
                // disabled as a result of the following dissociateWindow()
                // call.  Since we may want to keep the window visible for a
                // while (i.e. while transition animation), we don't call
                // visibilityChanged(false) against such a window.
                NativeWindow3D nwm = (NativeWindow3D)(associatedPrimaryWindow.nativeWinMonitor);
                nwm.dissociateWindow(nativeWinMonitor);
            } else {
                nativeWinMonitor.visibilityChanged(false);
            }
            setMapped(false);
        }
        setMappedBefore(false);
    }

    void destroyNotify() {
        if (nativeWinMonitor == null) {
            return;
        }
        nativeWinMonitor.destroyed();
        windowAssociator.removeAllRules(this); // Remove all rules associated with this window
    }
    
    // methods from NativeWindowControl
    
    public long getWID() {
        return id & 0xffffFFFF; // cancel sign extension
    }
    
    public void setVisible(boolean visible) {
        if (state == DESTROYED) {
            return;
        }
        if (visible) {
            map();
            display.flush();
        } else {
            unmap();
            display.flush();
        }
    }
    
    public boolean isMaximized () {
        return maximized;
    }
    
    public void setMaximized (boolean maximize) {
        if (maximizable) {
            if (maximize) {
                unmaximizedWidth = getWidth();
                unmaximizedHeight = getHeight();
                unmaximizedX = getX();
                unmaximizedY = getY();
                setSize(wm.getWindowWidthMax(), wm.getWindowHeightMax());
                setLocation(0, 0);
                nativeWinMonitor.locationChanged(0, 0);
                maximized = true;
            } else {
                setSize(unmaximizedWidth, unmaximizedHeight);
                setLocation(unmaximizedX, unmaximizedY);
                nativeWinMonitor.locationChanged(unmaximizedX, unmaximizedY);
                maximized = false;
            }
        }
    }
    
    public void destroy() {
        if (state == DESTROYED) {
            return;
        }
        super.delete();
//        super.destroy();
        display.flush();
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
        if (nativeWinMonitor != null) {
            nativeWinMonitor.nameChanged(name);
        }
    }
    
    public void setSize(int w, int h) {
        if (state == DESTROYED) {
            return;
        }
        maximized = false;
        resize(w, h);
	display.flush();
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public void setLocation(int x, int y) {
        if (state == DESTROYED) {
            return;
        }
        move(x, y);
        display.flush();
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public void setInputFocus() {
        if (isMappedBefore()) {
            this.set_input_focus();
            display.check_error();
        }
    }
    
    public void shapeNotify(Enum enumerate, int nrect) {
        if (nativeWinMonitor != null) {
            nativeWinMonitor.shapeNotify(enumerate, nrect);
        } else {
            shapeRect = enumerate;
            rectNum = nrect;
        }
    }
    
    int getWinClass() {
    	if (winClass == -1) {
	    if (attributes == null) {
                this.attributes = this.attributes();
            }
            winClass = attributes.read2(12);
	}
        return winClass;
    }

    int getVisualID() {
    	if (visualID == -1) {
    	    if (attributes == null) {
                this.attributes = this.attributes();
            }
    	    visualID = attributes.read4(8);
    	}
        return visualID;
    }
    
    /**
     * @return Returns the isMappedBefore.
     */
    boolean isMappedBefore() {
        return isMappedBefore;
    }
    
    /**
     * @param isMappedBefore The isMappedBefore to set.
     */
    void setMappedBefore(boolean isMappedBefore) {
        this.isMappedBefore = isMappedBefore;
    }

    /**
     * @return Returns the isMapped.
     */
    boolean isMapped() {
        return isMapped;
    }
    
    /**
     * @param isMapped The isMapped to set.
     */
    void setMapped(boolean isMapped) {
        this.isMapped = isMapped;
    }
    
    public int getNetWindowType() {
        return netWindowType.id;
    }
    public void setNetWindowType(Atom netWindowType) {
        this.netWindowType = netWindowType;
    }
    
    
    public boolean isCloseable() {
    	return closeable;
    }
    
    public void setCloseable(boolean closeable) {
    	this.closeable = closeable;
    }
    
    public boolean isMaximizable() {
    	return maximizable;
    }
    
    public void setMaximizable(boolean maximizable) {
    	this.maximizable = maximizable;
    }
    
    public boolean isMinimizable() {
	return minimizable;
    }
    
    public void setMinimizable(boolean minimizable) {
    	this.minimizable = minimizable;
    }
    
    public boolean isResizable() {
    	return resizable;
    }
    
    public void setResizable(boolean resizable) {
    	this.resizable = resizable;
    }
    
    
    public boolean isNormalWindow() {
    	return normalWindow;
    }
    
    public void setNormalWindow(boolean normalWindow) {
    	this.normalWindow = normalWindow;
    }
    
    
    public boolean isDecorated() {
    	return decorated;
    }
    
    public void setDecorated(boolean decorated) {
    	this.decorated = decorated;
    }
    
    public boolean isDockable() {
        return dockable;
    }
    
    public void setDockable(boolean dockable) {
        this.dockable = dockable;
    }
    
    /**
     * return the root window ID for this native window.
     * @return root window ID
     */
     public long getRootWID() {        
	 TreeReply treeReply = tree();
	 return treeReply.root_id();
     }
    
    public void setWindowAssociation(String subWinCls, String subWinName, String subWinTitlePattern) {
        windowAssociator.addRule(this, subWinCls, subWinName, subWinTitlePattern);
    }
    
    public void setWindowAssociation(
            String targetWinResCls, String targetWinResName, String targetWinTitlePattern, 
            String subWinResCls, String subWinResName, String subWinTitlePattern) 
    {
        windowAssociator.addRule(
                targetWinResCls, targetWinResName, targetWinTitlePattern, 
                subWinResCls, subWinResName, subWinTitlePattern);
    }

    public void moveToTop() {
	this.raise();
    }
}

