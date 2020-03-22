/**
 * Project Looking Glass
 *
 * $RCSfile: BasicPeer.java,v $
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
 * $Revision: 1.2 $
 * $Date: 2006-08-07 19:02:50 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.awtpeer;

import org.jdesktop.lg3d.awt.Lg3dBackBuffer;
import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.displayserver.nativewindow.NativeWindowLookAndFeel;
import org.jdesktop.lg3d.displayserver.nativewindow.NativeWindowFuzzyEdgePanel;
import org.jdesktop.lg3d.sg.Appearance;
import org.jdesktop.lg3d.sg.Shape3D;
import org.jdesktop.lg3d.sg.Texture;
import org.jdesktop.lg3d.sg.TextureAttributes;
import org.jdesktop.lg3d.sg.PolygonAttributes;
import org.jdesktop.lg3d.utils.action.AppearanceChangeAction;
import org.jdesktop.lg3d.utils.action.ScaleActionBoolean;
import org.jdesktop.lg3d.utils.eventadapter.MouseEnteredEventAdapter;
import org.jdesktop.lg3d.utils.shape.GlassyPanel;
import org.jdesktop.lg3d.utils.shape.ImagePanel;
import org.jdesktop.lg3d.utils.shape.OriginTranslation;
import org.jdesktop.lg3d.utils.shape.RectShadow;
import org.jdesktop.lg3d.utils.shape.SimpleAppearance;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Cursor3D;
import org.jdesktop.lg3d.wg.Thumbnail;
import org.jdesktop.lg3d.wg.Toolkit3D;


/**
 * Provides a basic Peer undecorated window
 */
public abstract class BasicPeer extends PeerBase {
    private static final float bodyDepth = 0.005f;
    private static final float thumbnailScale = 0.1f;
    private float decoWidth;
    private  Appearance bodyApp
	= new SimpleAppearance(
	    0.6f, 1.0f, 0.6f, 1.0f, SimpleAppearance.DISABLE_CULLING);
    private static float shadowN = 0.001f;
    private static float shadowE = 0.0015f;
    private static float shadowS = 0.002f;
    private static float shadowW = 0.001f;
    private static float shadowI = 0.001f;
    
    private Appearance swingAppearance;
    private NativeWindowFuzzyEdgePanel body;
    private GlassyPanel bodyDeco;
    private RectShadow bodyShadow;
    
    public BasicPeer() {
        setName("BasicPeer");
        decoWidth = getDecorationWidth();
        
        Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
        swingOriginOffsetX = toolkit3d.widthPhysicalToNative(toolkit3d.getScreenWidth())/2;
        swingOriginOffsetY = toolkit3d.heightPhysicalToNative(toolkit3d.getScreenHeight())/2;
        
        // Adjust for window decoration
        swingOriginOffsetX -= toolkit3d.widthPhysicalToNative(decoWidth);
        swingOriginOffsetY -= toolkit3d.heightPhysicalToNative(decoWidth);
        
        bodyDeco
            = new GlassyPanel(
		width3D + decoWidth * 2,
                height3D + decoWidth * 2,                 
                bodyDepth, 
                bodyApp);
                    
        bodyShadow
            = new RectShadow(
                width3D + decoWidth * 2,
                height3D + decoWidth * 2,
                shadowN, shadowE, shadowS, shadowW, shadowI,
                -bodyDepth,
                0.2f);
                
        swingAppearance = new Appearance();
        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);
        swingAppearance.setTextureAttributes(texAttr);
        PolygonAttributes pa = new PolygonAttributes();
        pa.setCullFace(PolygonAttributes.CULL_NONE);
        swingAppearance.setPolygonAttributes(pa);
        swingAppearance.setCapability(Appearance.ALLOW_TEXTURE_WRITE);
        
        body = new NativeWindowFuzzyEdgePanel(width3D, height3D, swingAppearance);
        
        Component3D comp = new Component3D();
        Component3D compBody = new Component3D();
        compBody.addChild(new OriginTranslation(body, new Vector3f(0f, 0f, 0.0001f)));
        comp.addChild(compBody);
        comp.addChild(bodyDeco);
        comp.addChild(bodyShadow);
        addChild(comp);
        
        compBody.setCursor(Cursor3D.DEFAULT_CURSOR);
        
        addMouseHandlers(compBody);

        createControlButtons();
                
        if (hasThumbnail()) {
            setThumbnail(new SwingThumbnail(width3D, height3D, null));
        } else {
            setThumbnail(null);
        }
    }
    
    
        
    public void bufferResized(Lg3dBackBuffer buffer) {
        //Call super to create the new texture
        super.bufferResized(buffer);
        swingAppearance.setTexture(swingTexture);
        
        Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
        setSize(toolkit3d.widthNativeToPhysical(swingImageWidth),
                toolkit3d.heightNativeToPhysical(swingImageHeight));
        body.setSize(width3D, height3D, 
                p2swingImageWidth/(float)swingImageWidth, 
                p2swingImageHeight/(float)swingImageHeight);   
    }
    
    void setSize(float width, float height) {
        width3D = width;
        height3D = height;
        // Lg3d components have their origin at their center so when a Swing window is resized
        // we need to move the window so that the swing origin of the top left remains at the
        // correct location.
        Vector3f currentLoc = new Vector3f();
        Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
        currentLoc.x = toolkit3d.widthNativeToPhysical(swingX-swingOriginOffsetX)+width3D/2;
        currentLoc.y = toolkit3d.heightNativeToPhysical(swingOriginOffsetY-swingY)-height3D/2;
        currentLoc.z = 0f;
        if (isLive())
            changeTranslation(currentLoc);
        else
            changeTranslation(currentLoc,0);    

        
        bodyDeco.setSize(width3D+decoWidth * 2,height3D+decoWidth * 2);
        bodyShadow.setSize(width3D+decoWidth * 2,height3D+decoWidth * 2);
        setPreferredSize(new Vector3f(width3D, height3D, bodyDepth));        
    }
        
        
    abstract float getDecorationWidth();
    
    abstract void createControlButtons();
    
    abstract boolean hasThumbnail();
    
    class ButtonAppearance extends SimpleAppearance {
        ButtonAppearance(String filename, boolean on) {
	    super(0.0f, 0.0f, 0.0f, 0.0f,
		SimpleAppearance.DISABLE_CULLING
		| SimpleAppearance.ENABLE_TEXTURE);

	    if (on) {
		setColor(1.0f, 0.6f, 0.6f, 0.8f);
	    } else {
		setColor(0.6f, 1.0f, 0.6f, 0.6f);
	    }
            try {
                setTexture(this.getClass().getClassLoader().getResource(filename));
            } catch (Exception e) {
                throw new RuntimeException(
                    "failed to initilaze window button: " + e);
            }
	}
    }

    class Button extends Component3D {
        Button(float size, Appearance app) {
	    this(size, app, size, app);
	}

        Button(float sizeOff, Appearance appOff,
	    float sizeOn, Appearance appOn)
	{
	    Shape3D shape = new ImagePanel(sizeOff, sizeOff);
	    shape.setAppearance(appOff);
	    addChild(shape);
	    if (appOff != appOn) {
                addListener(
                    new MouseEnteredEventAdapter(
                        new AppearanceChangeAction(shape, appOn)));
	    }
	    if (sizeOff != sizeOn) {
                addListener(
                    new MouseEnteredEventAdapter(
                        new ScaleActionBoolean(this, sizeOn/sizeOff, 100)));
	    }
	}
    }
    
    // FIXME -- not fully implemented yet
    private class SwingThumbnail extends Thumbnail {
        private SwingThumbnail(float width, float height, Texture tex) {
            // since the width and height values are not set correctly
            // when this method is invoked, manually set those here to
            // get a reasonable visual of the thumbnail.
            width = 0.1f;
            height = 0.1f;
            GlassyPanel thumbnailDeco
                = new GlassyPanel(
                    width + decoWidth * 2,
                    height + decoWidth * 2, 
                    bodyDepth * 2, 
                    bodyApp);
            
            Shape3D bodyShadow
                = new RectShadow(
                    width + decoWidth * 2,
                    height + decoWidth * 2,
                    shadowN * 2, shadowE * 2, shadowS * 2, shadowW * 2, 
                    shadowI,
                    -bodyDepth * 2,
                    0.3f);
            
            SimpleAppearance tnApp 
                = new SimpleAppearance(1.0f, 1.0f, 1.0f, 0.75f,
                    SimpleAppearance.ENABLE_TEXTURE 
                        | SimpleAppearance.DISABLE_CULLING);
            tnApp.setTexture(tex);
            
            NativeWindowFuzzyEdgePanel body 
                = new NativeWindowFuzzyEdgePanel(width, height, tnApp);
            
            Component3D comp = new Component3D();
            comp.addChild(thumbnailDeco);
            comp.addChild(bodyShadow);
            comp.addChild(body);
            comp.setScale(thumbnailScale); // scale down the size
            addChild(comp);
            
            setPreferredSize(
                    new Vector3f(width * thumbnailScale, height * thumbnailScale, 
                        bodyDepth * thumbnailScale));
        }
    }
}


