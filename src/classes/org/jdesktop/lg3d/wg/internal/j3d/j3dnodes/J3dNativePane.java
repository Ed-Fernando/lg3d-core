/**
 * Project Looking Glass
 *
 * $RCSfile: J3dNativePane.java,v $
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
 * $Revision: 1.3 $
 * $Date: 2007-04-10 22:58:45 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.internal.j3d.j3dnodes;

/**
 * J3d representation of an X11 window
 */
public class J3dNativePane extends J3dComponent3D {

    private long wid;
    private int nativeWidth;
    private int nativeHeight;
    private float width;
    private float height;
    private boolean isRemwin;

    public J3dNativePane() {
        setCapability(ENABLE_PICK_REPORTING);
    }
    
    public void setWid( long wid ) {
        this.wid = wid;
    }
    
    public long getWid() {
        return wid;
    }
    
    public void setNativeWidth( int width ) {
        this.nativeWidth = width;
    }
    
    public int getNativeWidth() {
        return nativeWidth;
    }
    
    public void setNativeHeight( int height ) {
        this.nativeHeight = height;
    }
    
    public int getNativeHeight() {
        return nativeHeight;
    }

    public void setWidth( float width ) {
        this.width = width;
    }
    
    public float getWidth() {
        return width;
    }
    
    public void setHeight( float height ) {
        this.height = height;
    }
    
    public float getHeight() {
        return height;
    }

    // TODO: for now, we will use userData2 to store 'isRemwin', which is true
    // if the window is a RemWin native window and false if it is an X11 native
    // window. The low-level picker checkForNativePane test should only 
    // pass for X11 native windows.

    public boolean isRemwin () {
	return 	isRemwin;
    }

    public void setIsRemwin (boolean isRemwin) {
	this.isRemwin = isRemwin;
    }
}
