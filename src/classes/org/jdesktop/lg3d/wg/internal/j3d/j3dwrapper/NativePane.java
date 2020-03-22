/**
 * Project Looking Glass
 *
 * $RCSfile: NativePane.java,v $
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
 * $Revision: 1.5 $
 * $Date: 2007-04-10 22:58:46 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.internal.j3d.j3dwrapper;


import org.jdesktop.lg3d.wg.internal.wrapper.NativePaneWrapper;
import org.jdesktop.lg3d.wg.internal.j3d.j3dnodes.J3dNativePane;


/**
 * NativeWindow3D provides the anchor for rendering Native window
 * system windows.
 */
public class NativePane extends Component3D implements NativePaneWrapper {

    public NativePane() {
    }
    
    protected void createWrapped() { 
        wrapped = new J3dNativePane();
        wrapped.setUserData(this);
    }
    
    public void setWid( long wid ) {
        ((J3dNativePane)wrapped).setWid(wid);
    }
    
    public long getWid() {
        return ((J3dNativePane)wrapped).getWid();
    }
    
    public void setNativeWidth( int width ) {
        ((J3dNativePane)wrapped).setNativeWidth(width);
    }
    
    public int getNativeWidth() {
        return ((J3dNativePane)wrapped).getNativeWidth();
    }
    
    public void setNativeHeight( int height ) {
        ((J3dNativePane)wrapped).setNativeHeight(height);
    }
    
    public int getNativeHeight() {
        return ((J3dNativePane)wrapped).getNativeHeight();
    }
    
    public void setWidth( float width ) {
        ((J3dNativePane)wrapped).setWidth(width);
    }

    public float getWidth() {
        return ((J3dNativePane)wrapped).getWidth();
    }

    public void setHeight( float height ) {
        ((J3dNativePane)wrapped).setHeight(height);
    }

    public float getHeight() {
        return ((J3dNativePane)wrapped).getHeight();
    }

    public boolean isRemwin () {
	return ((J3dNativePane)wrapped).isRemwin();
    }

    public void setIsRemwin (boolean isRemwin) {
	((J3dNativePane)wrapped).setIsRemwin(isRemwin);
    }
}
