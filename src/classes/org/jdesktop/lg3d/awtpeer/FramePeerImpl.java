/**
 * Project Looking Glass
 *
 * $RCSfile: FramePeerImpl.java,v $
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
 * $Revision: 1.1 $
 * $Date: 2006-06-30 20:38:46 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.awtpeer;

import java.awt.Component;
import org.jdesktop.lg3d.awt.PeerInterface;
import org.jdesktop.lg3d.awt.Lg3dBackBuffer;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import org.jdesktop.j3d.utils.math.Math3D;
import org.jdesktop.lg3d.displayserver.nativewindow.NativeWindowLookAndFeel;
import org.jdesktop.lg3d.sg.Appearance;
import org.jdesktop.lg3d.sg.ImageComponent2D;
import org.jdesktop.lg3d.sg.Shape3D;
import org.jdesktop.lg3d.sg.Texture;
import org.jdesktop.lg3d.sg.Texture2D;
import org.jdesktop.lg3d.sg.TextureAttributes;
import org.jdesktop.lg3d.sg.Transform3D;
import org.jdesktop.lg3d.utils.action.ActionNoArg;
import org.jdesktop.lg3d.utils.action.AppearanceChangeAction;
import org.jdesktop.lg3d.utils.action.ScaleActionBoolean;
import org.jdesktop.lg3d.utils.eventadapter.MouseClickedEventAdapter;
import org.jdesktop.lg3d.utils.eventadapter.MouseEnteredEventAdapter;
import org.jdesktop.lg3d.utils.shape.FuzzyEdgePanel;
import org.jdesktop.lg3d.utils.shape.GlassyPanel;
import org.jdesktop.lg3d.utils.shape.ImagePanel;
import org.jdesktop.lg3d.utils.shape.RectShadow;
import org.jdesktop.lg3d.utils.shape.SimpleAppearance;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Cursor3D;
import org.jdesktop.lg3d.wg.Frame3D;
import org.jdesktop.lg3d.wg.Thumbnail;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.MouseButtonEvent3D;
import org.jdesktop.lg3d.wg.event.MouseEvent3D;


/**
 * Adds frame features to the BasicPeer, including close button etc
 */
public class FramePeerImpl extends BasicPeer implements PeerInterface {
    private static float buttonSize = 0.005f;
    private static float buttonOnSize = buttonSize * 1.15f;

    private SimpleAppearance closeButtonOffAppearance;
    private SimpleAppearance closeButtonOnAppearance;
    private SimpleAppearance minimizeButtonOffAppearance;
    private SimpleAppearance minimizeButtonOnAppearance;
    
    private Component3D closeButton;
    private Component3D minimizeButton;
    
    public float getDecorationWidth() {
        return 0.005f;
    }
    
    public void createControlButtons() {
        initButtonAppearances();
        
        closeButton 
	    = new Button(buttonSize, closeButtonOffAppearance,
		buttonOnSize, closeButtonOnAppearance);
	closeButton.setCursor(Cursor3D.SMALL_CURSOR);
        closeButton.setTranslation(width3D * 0.5f, height3D * 0.5f, 0.001f);
        closeButton.addListener(
            new MouseClickedEventAdapter(
                new ActionNoArg() {
                    public void performAction(LgEventSource source) {
                        changeEnabled(false);
                    }
                }));
        addChild(closeButton);
        
        minimizeButton 
	    = new Button(buttonSize, minimizeButtonOffAppearance,
		buttonOnSize, minimizeButtonOnAppearance);
	minimizeButton.setCursor(Cursor3D.SMALL_CURSOR);
        minimizeButton.setTranslation(
            width3D * 0.5f - buttonSize * 1.4f, 
            height3D * 0.5f + buttonSize * 0.2f, 0.001f);
        minimizeButton.addListener(
            new MouseClickedEventAdapter(
                new ActionNoArg() {
                    public void performAction(LgEventSource source) {
                        changeVisible(false);
                    }
                }));
        addChild(minimizeButton);

    }

    private void initButtonAppearances() {
	closeButtonOffAppearance 
	    = new ButtonAppearance(
                "resources/images/button/window-close.png", false);
	closeButtonOnAppearance
	    = new ButtonAppearance(
                "resources/images/button/window-close.png", true);
        minimizeButtonOffAppearance
	    = new ButtonAppearance(
                "resources/images/button/window-minimize.png", false);
	minimizeButtonOnAppearance
	    = new ButtonAppearance(
                "resources/images/button/window-minimize.png", true);
    }
    
    void setSize(float width, float height) {
        super.setSize(width,height);
        closeButton.setTranslation(width3D * 0.5f, height3D * 0.5f, 0.001f);
        minimizeButton.setTranslation(
            width3D * 0.5f - buttonSize * 1.4f, 
            height3D * 0.5f + buttonSize * 0.2f, 0.001f);

    }

    boolean hasThumbnail() {
        return true;
    }
}


