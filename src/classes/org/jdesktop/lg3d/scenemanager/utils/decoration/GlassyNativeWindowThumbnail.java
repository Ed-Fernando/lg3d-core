/**
 * Project Looking Glass
 *
 * $RCSfile: GlassyNativeWindowThumbnail.java,v $
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
 * $Revision: 1.8 $
 * $Date: 2006-08-14 23:47:43 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.decoration;

import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.displayserver.nativewindow.NativeWindow3D;
import org.jdesktop.lg3d.displayserver.nativewindow.NativeWindowLookAndFeel;
import org.jdesktop.lg3d.displayserver.nativewindow.NativeWindowObject;
import org.jdesktop.lg3d.displayserver.nativewindow.TiledNativeWindowImage;
import org.jdesktop.lg3d.sg.Appearance;
import org.jdesktop.lg3d.sg.TransparencyAttributes;
import org.jdesktop.lg3d.utils.shape.GlassyPanel;
import org.jdesktop.lg3d.utils.shape.RectShadow;
import org.jdesktop.lg3d.utils.shape.SimpleAppearance;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Thumbnail;
import org.jdesktop.lg3d.wg.Toolkit3D;


public class GlassyNativeWindowThumbnail extends Thumbnail {
    private final static float thumbnailScale = 0.13f;
    private final static float bodyHeader = 0.003f;
    private final static float bodyFooter = 0.0002f;
    private final static float bodyBorder = 0.001f;
    private final static float bodyDepth  = 0.003f * 2;
    private final static float shadowN = 0.001f * 2;
    private final static float shadowE = 0.002f * 2;
    private final static float shadowS = 0.003f * 2;
    private final static float shadowW = 0.001f * 2;
    private final static float shadowI = 0.001f;
    
    private final static Appearance bodyApp
	= new SimpleAppearance(
	    0.6f, 1.0f, 0.6f, 1.0f,
	    SimpleAppearance.DISABLE_CULLING);

    private final static float layer0 = bodyDepth * -0.02f;
    private final static float layer1 = bodyDepth * -0.01f;
    private final static float layer2 = bodyDepth *  0.00f;
    private final static float layer3 = bodyDepth *  0.01f;
    
    private NativeWindowObject bodyPanel;
    private Component3D panelComp;
    private GlassyPanel bodyDecoration;
    private RectShadow bodyShadow;
    private Vector3f tmpV3f = new Vector3f();
        
    GlassyNativeWindowThumbnail(final NativeWindow3D nativeWindow, 
        TiledNativeWindowImage appImage) 
    {   
        Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
        float width = toolkit3d.widthNativeToPhysical(appImage.getWinWidth());
        float height = toolkit3d.heightNativeToPhysical(appImage.getWinHeight());
        
        // make the depth twice since it looks better in that way
        tmpV3f.set(
                width * thumbnailScale, 
                height * thumbnailScale, 
                bodyDepth * thumbnailScale);
        super.setPreferredSize(tmpV3f);
        
        bodyPanel = new NativeWindowObject(appImage, 0.75f, false, 4);
        bodyPanel.sizeChanged(); // adjust the size and texture coords
        
        panelComp = new Component3D();
        panelComp.addChild(bodyPanel);
        // Be sure to make panelComp unpickable -- it uses NativeWindowObject
        // which is also be used for the "real" window.  Having two pickable
        // NativeWindowObject that represents the same X window may confuse
        // the system.
        panelComp.setPickable(false);
            
        Component3D body = new Component3D();
        
        width = width + bodyBorder * 2;
        height = height + bodyHeader + bodyFooter + bodyBorder * 2;
            
        bodyDecoration = new GlassyPanel(width, height, bodyDepth, bodyApp);
        body.addChild(bodyDecoration);
            
        bodyShadow
            = new RectShadow(
                width, height,
                shadowN, shadowE, shadowS, shadowW, shadowI,
                -bodyDepth,
                0.3f);
        body.addChild(bodyShadow);
            
        body.setTranslation(0.0f, (bodyHeader - bodyFooter) * 0.5f, layer0);
        
        Component3D scalingCont = new Component3D();
        scalingCont.addChild(panelComp);
        scalingCont.addChild(body);
        scalingCont.setScale(thumbnailScale);
        addChild(scalingCont);
    }
        
    public void setSize(float width, float height) {
        tmpV3f.set(
                width * thumbnailScale, 
                height * thumbnailScale, 
                bodyDepth * thumbnailScale);
        super.setPreferredSize(tmpV3f);
        
        bodyPanel.sizeChanged();
        
        float w = width + bodyBorder * 2;
        float h = height + bodyHeader + bodyFooter + bodyBorder * 2;
        
        bodyDecoration.setSize(w, h);
        bodyShadow.setSize(w, h);
    }
}

