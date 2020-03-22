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
 * $Date: 2007-04-10 22:58:42 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.nativewindow;

import org.jdesktop.lg3d.sg.Group;
import org.jdesktop.lg3d.wg.internal.j3d.j3dnodes.J3dNativePane;
import org.jdesktop.lg3d.wg.internal.wrapper.NativePaneWrapper;

/**
 * This is the root of the actual Native window geometry, it does
 * not contain the 3D window decorations.
 *
 * This class is used in the LowLevelPicker to determine when the
 * mouse is over screen space being rendered by X.
 *
 * @author  paulby
 */
public class NativePane extends Group {
    
    /** Creates a new instance of NativePane */
    public NativePane() {
        setCapability(NativePane.ENABLE_PICK_REPORTING);
        setPickable(true);
    }
    
    protected void createWrapped() {
        wrapped = instantiate("org.jdesktop.lg3d.wg.internal.j3d.j3dwrapper.NativePane");
        wrapped.setUserData(this);
    }
    
    public void setWid( long wid ) {
        ((NativePaneWrapper)wrapped).setWid(wid);
    }
    
    public long getWid() {
         return ((NativePaneWrapper)wrapped).getWid();
    }
    
    public void setNativeWidth( int width ) {
         ((NativePaneWrapper)wrapped).setNativeWidth(width);
    }
    
    public int getNativeWidth() {
        return ((NativePaneWrapper)wrapped).getNativeWidth();
    }
    
    public void setNativeHeight( int height ) {
        ((NativePaneWrapper)wrapped).setNativeHeight(height);
    }
    
    public int getNativeHeight() {
        return ((NativePaneWrapper)wrapped).getNativeHeight();
    }

    public void setWidth( float width ) {
         ((NativePaneWrapper)wrapped).setWidth(width);
    }
    
    public float getWidth() {
        return ((NativePaneWrapper)wrapped).getWidth();
    }

    public void setHeight( float height ) {
         ((NativePaneWrapper)wrapped).setHeight(height);
    }
    
    public float getHeight() {
        return ((NativePaneWrapper)wrapped).getHeight();
    }

    public boolean isRemwin () {
	return ((NativePaneWrapper)wrapped).isRemwin();
    }

    public void setIsRemwin (boolean isRemwin) {
	((NativePaneWrapper)wrapped).setIsRemwin(isRemwin);
    }
}
