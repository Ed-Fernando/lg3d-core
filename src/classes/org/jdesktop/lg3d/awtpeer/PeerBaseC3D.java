/**
 * Project Looking Glass
 *
 * $RCSfile: PeerBaseC3D.java,v $
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
 * $Date: 2006-06-30 20:38:47 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.awtpeer;

import java.awt.Component;
import org.jdesktop.lg3d.awt.PeerInterface;
import org.jdesktop.lg3d.awt.Lg3dBackBuffer;
import java.awt.Point;
import org.jdesktop.lg3d.awt.SceneManagerListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import javax.swing.SwingUtilities;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import org.jdesktop.j3d.utils.math.Math3D;
import org.jdesktop.lg3d.sg.ImageComponent2D;
import org.jdesktop.lg3d.sg.Texture2D;
import org.jdesktop.lg3d.sg.Transform3D;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Toolkit3D;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.wg.event.MouseButtonEvent3D;
import org.jdesktop.lg3d.wg.event.MouseDraggedEvent3D;
import org.jdesktop.lg3d.wg.event.MouseEnteredEvent3D;
import org.jdesktop.lg3d.wg.event.MouseEvent3D;
import org.jdesktop.lg3d.wg.event.MouseMotionEvent3D;


public abstract class PeerBaseC3D extends Component3D implements PeerInterface {

    protected Texture2D swingTexture = null;
    protected ImageComponent2D swingImageComponent = null;
    protected BufferedImage p2SwingImage=null;    // Power of 2 image
    protected int swingImageWidth;        // Width of original image
    protected int swingImageHeight;
    protected int p2swingImageWidth;      // Power of 2 Width of original image
    protected int p2swingImageHeight;
    protected int swingX=-1;
    protected int swingY=-1;
    private SceneManagerListener sceneManagerListener = null;
    protected float width3D;
    protected float height3D;
    protected int swingOriginOffsetX;
    protected int swingOriginOffsetY;
                
    protected Component component;

    public void renderBuffer(Lg3dBackBuffer buffer) {
        logger.warning("**** renderBuffer "+swingTexture);
        BufferedImage newImage = buffer.getImage();
        if (p2SwingImage==null ||
               newImage.getWidth()>p2SwingImage.getWidth() ||
               newImage.getHeight()>p2SwingImage.getHeight()) {
            bufferResized(buffer);
        } else {
            p2SwingImage.setData(newImage.getRaster());
            swingImageComponent.set(p2SwingImage);   
        }
    }
    
    /**
     * Set the awt component for which this is the peer
     */
    public void setComponent(Component component) {
        this.component = component;
    }
    
    public void setTitle(String str) {
        setName(str);
        // TODO provide visual representation of title
    }
    
    /**
     * Change the location of the window
     */
    public void setLocation(int x, int y) {
        if (swingX==x && swingY ==y)
            return;
        
        // Swing and Lg3d window origins are not the same
        
        swingX = x;
        swingY = y;

        Vector3f currentLoc = new Vector3f();
        getTranslation(currentLoc);
//        System.err.println("OriginOffset X "+swingOriginOffsetX);
//        System.err.println("OriginOffset Y "+swingOriginOffsetY);
//        System.err.println(" X "+swingX);
//        System.err.println(" Y "+swingY);
//        System.err.println(" Width3D "+width3D);
//        System.err.println(" Height3D "+height3D);
        Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
        currentLoc.x = toolkit3d.widthNativeToPhysical(swingX-swingOriginOffsetX)+width3D/2;
        currentLoc.y = toolkit3d.heightNativeToPhysical(swingOriginOffsetY-swingY)-height3D/2;
        currentLoc.z = 0.5f;

        logger.warning("Swing:set "+swingX+", "+swingY+"  "+ currentLoc);
        
        if (isLive())
            changeTranslation(currentLoc,0);
        else
            changeTranslation(currentLoc,0);
    }

    public void changeTranslation(float x, float y, float z, int duration) {
        super.changeTranslation(x,y,z,duration);
         
        // TODO Need to project this point onto the z==0 plane
        Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
        swingX = swingOriginOffsetX + toolkit3d.widthPhysicalToNative(x-width3D/2);
        swingY = swingOriginOffsetY - toolkit3d.heightPhysicalToNative(y+height3D/2);
        logger.warning("Swing:change "+swingX+", "+swingY+"  "+(toolkit3d.widthNativeToPhysical(swingX-swingOriginOffsetX)+width3D/2)+"  "+(toolkit3d.heightNativeToPhysical(swingOriginOffsetY-swingY)-height3D/2));
        sceneManagerListener.windowMoved(swingX, swingY);
    }
    
    public void addSceneManagerListener(SceneManagerListener listener) {
        if (sceneManagerListener!=null)
            throw new RuntimeException("Listener can only be set once");
        sceneManagerListener = listener;
    }

    public void bufferResized(Lg3dBackBuffer buffer) {
        BufferedImage image = buffer.getImage();
        swingImageWidth = image.getWidth();
        swingImageHeight = image.getHeight();
//        logger.warning("BufferResized "+swingImageWidth+", "+swingImageHeight);
        p2swingImageWidth = getPowerOfTwoUpperBound(swingImageWidth);
        p2swingImageHeight = getPowerOfTwoUpperBound(swingImageHeight);
                
        swingTexture = new Texture2D(Texture2D.BASE_LEVEL, 
                                     Texture2D.RGB, 
                                     p2swingImageWidth, 
                                     p2swingImageHeight );
        swingTexture.setMagFilter(Texture2D.LINEAR_SHARPEN);
        swingImageComponent = new ImageComponent2D(ImageComponent2D.FORMAT_RGB, 
                                        p2swingImageWidth, 
                                        p2swingImageHeight,
                                        false,
                                        true);
        swingImageComponent.setCapability(ImageComponent2D.ALLOW_IMAGE_WRITE);
        p2SwingImage = new BufferedImage(p2swingImageWidth, 
                                       p2swingImageHeight,
                                       BufferedImage.TYPE_INT_RGB);
        swingTexture.setImage(0, swingImageComponent);
        p2SwingImage.setData(buffer.getImage().getRaster());
        swingImageComponent.set(p2SwingImage);        
   }
        
    protected int getPowerOfTwoUpperBound(int value) {

        if (value < 1)
            return value;

        int powerValue = 1;

        for (;;) {
            powerValue *= 2;
            if (value < powerValue) {
                return powerValue;
            }
        }
    }
    
    /** This routine is duplicated in SwingNode. If you find any bugs here please
     * be sure to fix them in SwingNode as well.
     */
    // surppress warning against enterEvt.createSwingEvent()
    @SuppressWarnings("deprecation") // ignore warnings against createSwingEvent()
    protected void addMouseHandlers(Component3D comp) {
         comp.addListener(
            new LgEventListener() {
                public void processEvent(LgEvent evt) {
                    if (!isVisible())
                        return;
                    //assert(evt instanceof MouseEvent3D);
                    MouseEvent3D mevt = (MouseEvent3D)evt;
                    //logger.severe("PeerBaseC3D processEvent "+evt);
                
                    Point swingPos = calcPositionInPanel((mevt).getIntersection(new Point3f()));
                    MouseEvent swingEvent = mevt.createSwingEvent(component, swingPos);
                    component.dispatchEvent(swingEvent);
                }
                public Class<LgEvent>[] getTargetEventClasses() {
                    return new Class[] {MouseButtonEvent3D.class, 
                                        MouseMotionEvent3D.class, 
                                        MouseDraggedEvent3D.class};
                }
            });
            
        // Mouse enter drives focus of Swing apps
        comp.addListener(
            new LgEventListener() {
                public void processEvent(LgEvent evt) {
                    //logger.warning("Enter event");
                    if (!isVisible())
                        return;
                    MouseEnteredEvent3D enterEvt = (MouseEnteredEvent3D)evt;
                    //Lg3dFocusManager focusMgr = Lg3dFocusManager.getFocusManager();
                    if (enterEvt.isEntered()) {
                        logger.info("Mouse Entered");
                        Point swingPos = calcPositionInPanel((enterEvt).getIntersection(new Point3f()));
                        component.dispatchEvent(enterEvt.createSwingEvent(component, swingPos));
                        java.awt.Window opposite = null;
                        component.dispatchEvent(new WindowEvent((java.awt.Window)component, WindowEvent.WINDOW_GAINED_FOCUS, opposite));
                        component.dispatchEvent(new WindowEvent((java.awt.Window)component, WindowEvent.WINDOW_ACTIVATED, opposite));
                        //focusMgr.setCurrentFocusedWindow((java.awt.Window)component);
                    } else {
                        java.awt.Window opposite = SwingUtilities.getWindowAncestor(enterEvt.getAWTComponent());
                        //logger.info("TODO Should we give focus back to Canvas3D ? ");         // TODO implement opposite in MouseEnteredEvent3D
                        component.dispatchEvent(new WindowEvent((java.awt.Window)component, WindowEvent.WINDOW_LOST_FOCUS, opposite));
                        component.dispatchEvent(new WindowEvent((java.awt.Window)component, WindowEvent.WINDOW_DEACTIVATED, opposite));
                        
                        // Hack until we get lg3d focus manager
                        // always give focus back to the canvas3d
                        component.dispatchEvent(new WindowEvent(opposite, WindowEvent.WINDOW_GAINED_FOCUS, (java.awt.Window)component));
                        component.dispatchEvent(new WindowEvent(opposite, WindowEvent.WINDOW_ACTIVATED, (java.awt.Window)component));                        
                    }
                }
                public Class<LgEvent>[] getTargetEventClasses() {
                    return new Class[] {MouseEnteredEvent3D.class};
                }
            });
        
    }
    
    /**
     * Calc the position of the 3D point in the coordinate system
     * of the JPanel
     *
     * This routine is duplicated in SwingNode. If you find any bugs here please
     * be sure to fix them in SwingNode as well.
     */
    Point calcPositionInPanel(Point3f p3f) {
        // First calculate the actual coordinates of the corners of
        // the panel in VW.

        Transform3D t3d = new Transform3D();
        getLocalToVworld(t3d);

        Point3f p1 = new Point3f( -width3D/2f, height3D/2f, 0f);
        Point3f p2 = new Point3f( width3D/2f, height3D/2f, 0f);

        t3d.transform(p1);
        t3d.transform(p2);

//        logger.severe("Corner p1 "+p1);
//        logger.severe("Corner p2 "+p2);
//        logger.severe("Intersection "+p3f);

        // Now calculate the x and y coords relative to the panel

        float y = Math3D.pointLineDistance(p1,p2,p3f);

        p2 = new Point3f( -width3D/2f, -height3D/2f, 0f);
        t3d.transform(p2);

        float x = Math3D.pointLineDistance(p1,p2,p3f);

        //logger.severe("XY "+x+" "+y);
        //logger.severe("XY "+(x/width3D)*component.getWidth()+" "+(y/height3D)*component.getHeight());
        return new Point((int)((x/width3D)*component.getWidth()),(int)((y/height3D)*component.getHeight()));
    }
    
    abstract float getDecorationWidth();
    
    abstract void createControlButtons();
    
}


