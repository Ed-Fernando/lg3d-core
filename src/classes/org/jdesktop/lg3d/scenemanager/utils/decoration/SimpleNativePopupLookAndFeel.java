/**
 * Project Looking Glass
 *
 * $RCSfile: SimpleNativePopupLookAndFeel.java,v $
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
 * $Date: 2007-04-10 22:58:44 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.decoration;

import javax.vecmath.Point3f;
import org.jdesktop.lg3d.displayserver.nativewindow.NativePopup3D;
import org.jdesktop.lg3d.displayserver.nativewindow.NativePopupLookAndFeel;
import org.jdesktop.lg3d.displayserver.nativewindow.NativeWindowObject;
import org.jdesktop.lg3d.displayserver.nativewindow.TiledNativeWindowImage;
import org.jdesktop.lg3d.displayserver.nativewindow.WindowShape;
import org.jdesktop.lg3d.sg.BoundingBox;
import org.jdesktop.lg3d.utils.shape.RectShadow;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Container3D;
import org.jdesktop.lg3d.wg.Cursor3D;
import org.jdesktop.lg3d.wg.Toolkit3D;


public class SimpleNativePopupLookAndFeel extends NativePopupLookAndFeel {
    private static final float shadowN = 0.001f;
    private static final float shadowE = 0.002f;
    private static final float shadowS = 0.003f;
    private static final float shadowW = 0.001f;
    private static final float shadowI = 0.001f;
    
    private NativePopup3D nativeWindow;
    private TiledNativeWindowImage appImage;
    private WindowShape windowShape;
    private Cursor3D nativeCursor;
    
    private Component3D body;
    Component3D panelComp;
    private NativeWindowObject bodyPanel;
    private RectShadow bodyShadow;
    
    public void initialize(NativePopup3D nativeWindow,
        TiledNativeWindowImage appImage, boolean decorated, WindowShape shape, 
        int fuzzyEdgeWidth, boolean shadow, Cursor3D cursor) 
    {
        this.nativeWindow = nativeWindow;
        this.appImage = appImage;
        this.windowShape = shape;
        
        Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
        setupComponents(
            toolkit3d.widthNativeToPhysical(appImage.getWinWidth()), 
            toolkit3d.heightNativeToPhysical(appImage.getWinHeight()),
            nativeWindow.getNativeWindowControl().getName(),
            fuzzyEdgeWidth, shadow, cursor);
    }
        
    private void setupComponents(float width, float height, String title, 
            int fuzzyEdgeWidth, boolean shadow, Cursor3D cursor) 
    {
        panelComp = new Component3D();
        bodyPanel = new NativeWindowObject(appImage, 1.0f, true, fuzzyEdgeWidth);
	panelComp.addChild(bodyPanel);
        
        if (shadow) {
            bodyShadow
                = new RectShadow(
                    0, 0,
                    shadowN, shadowE, shadowS, shadowW, shadowI,
                    0.0f,
                    0.3f);
            panelComp.addChild(bodyShadow);
        }
        
	nativeCursor = cursor;
        setCursor(nativeCursor);
        addChild(panelComp);
    }
    
    public Cursor3D getNativeCursor () {
	return nativeCursor;
    }

    public Component3D getNativeWindowBodyComponent () {
	return panelComp;
    }

    public boolean isRemwin () {
	return 	bodyPanel.isRemwin();
    }

    public void setIsRemwin (boolean isRemwin) {
	bodyPanel.setIsRemwin(isRemwin);
    }

    private void setSize(float width, float height) {
	bodyPanel.sizeChanged();
        if (bodyShadow != null) {
            bodyShadow.setSize(width, height);
        }
    }
    
    /**
     * The size of the native window image has changed so resize the
     * geometry to accomodate
     */
    public void sizeChangedNative(int nativeWidth, int nativeHeight) {
        Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
        setSize(
            toolkit3d.widthNativeToPhysical(nativeWidth),
            toolkit3d.heightNativeToPhysical(nativeHeight));
    }
    
    /**
     * Called by NativeWindow3D when the 3D size is changed
     */
    public void sizeChanged3D(float width, float height, 
			      int nativeWidth, int nativeHeight) 
	throws IllegalArgumentException
    {
        Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
        appImage.sizeChanged(nativeWidth, nativeHeight, true);
        setSize(width, height);
    }
}

