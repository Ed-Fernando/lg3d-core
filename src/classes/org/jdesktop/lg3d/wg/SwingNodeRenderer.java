/**
 * Project Looking Glass
 *
 * $RCSfile: SwingNodeRenderer.java,v $
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
 * $Date: 2006-05-24 23:27:36 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg;

import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import javax.vecmath.Point3f;
import org.jdesktop.j3d.utils.math.Math3D;
import org.jdesktop.lg3d.sg.Group;
import org.jdesktop.lg3d.sg.Texture2D;
import org.jdesktop.lg3d.sg.Transform3D;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.wg.event.MouseButtonEvent3D;
import org.jdesktop.lg3d.wg.event.MouseDraggedEvent3D;
import org.jdesktop.lg3d.wg.event.MouseEnteredEvent3D;
import org.jdesktop.lg3d.wg.event.MouseEvent3D;
import org.jdesktop.lg3d.wg.event.MouseMotionEvent3D;
import org.jdesktop.lg3d.wg.internal.swingnode.SwingNodeJFrame;

/**
 * Parent class for all Swing Node geometry control. Users who wish to provide their own geometry
 * on which the Swing texture is rendered should subclass this.
 *
 * @author paulby
 */
public abstract class SwingNodeRenderer extends Group implements SwingNodeJFrame.TextureChangedListener {
    protected float width3D;
    protected float height3D;
    protected JPanel panel;
    private JFrame hiddenFrame;
    private SwingNode swingNode;
    
    void setup(JFrame hiddenFrame, SwingNode swingNode) {
        this.hiddenFrame = hiddenFrame;
        this.swingNode = swingNode;
    }
    
    void setPanel(JPanel panel) {
        this.panel = panel;
    }
    
    public abstract void textureChanged(Texture2D texture);
    
    /**  This routine is duplicated in PeerBase. If you find any bugs here please
     * be sure to fix them in PeerBase as well.
     */
    @SuppressWarnings("deprecation") // ignore warnings against createSwingEvent(), getPeer() and createSwingEvent()
    public void addMouseHandlers(Component3D comp) {
        comp.addListener(
                new LgEventListener() {
            public void processEvent(LgEvent evt) {
                if (!swingNode.isVisible())
                    return;
                //assert(evt instanceof MouseEvent3D);
                MouseEvent3D mevt = (MouseEvent3D)evt;
                
                Point swingPos = calcPositionInPanel((mevt).getIntersection(new Point3f()));
                MouseEvent swingEvent = mevt.createSwingEvent(hiddenFrame, swingPos);
                //logger.warning("PeerBase processEvent "+swingEvent);
                hiddenFrame.dispatchEvent(swingEvent);
            }
            public Class<LgEvent>[] getTargetEventClasses() {
                return new Class[] {MouseButtonEvent3D.class,
                MouseMotionEvent3D.class,
                MouseDraggedEvent3D.class};
            }
        });
        
        comp.addListener(
                new LgEventListener() {
            public void processEvent(LgEvent evt) {
                //logger.warning("Enter event");
                if (!swingNode.isVisible())
                    return;
                MouseEnteredEvent3D enterEvt = (MouseEnteredEvent3D)evt;
                if (enterEvt.isEntered()) {
                    logger.fine("Mouse Entered "+enterEvt.getAWTComponent());
                    Point swingPos = calcPositionInPanel((enterEvt).getIntersection(new Point3f()));
                    logger.fine("Peer "+hiddenFrame.getPeer());
                    hiddenFrame.dispatchEvent(enterEvt.createSwingEvent(hiddenFrame, swingPos));
                    hiddenFrame.dispatchEvent(new WindowEvent((java.awt.Window)hiddenFrame, WindowEvent.WINDOW_GAINED_FOCUS, (java.awt.Window)null));
                    hiddenFrame.dispatchEvent(new WindowEvent((java.awt.Window)hiddenFrame, WindowEvent.WINDOW_ACTIVATED, (java.awt.Window)null));
                } else {
                    java.awt.Window opposite = SwingUtilities.getWindowAncestor(enterEvt.getAWTComponent());
                    //logger.warning("TODO Should we give focus back to Canvas3D ?");
                    hiddenFrame.dispatchEvent(new WindowEvent((java.awt.Window)hiddenFrame, WindowEvent.WINDOW_LOST_FOCUS, opposite));
                    hiddenFrame.dispatchEvent(new WindowEvent((java.awt.Window)hiddenFrame, WindowEvent.WINDOW_DEACTIVATED, opposite));
                    
                    // SwingNodes don't have a valid parent so force it to be the Canvas3D
//                    if (opposite==null) {
//                        
//                    }
//                    
//                    logger.warning("AWTComponnet "+enterEvt.getAWTComponent()+"\n"+"Ancestor "+opposite);
//                    if (opposite==null) {
//                        Component comp = enterEvt.getAWTComponent();
//                        do {
//                            Component parent = comp.getParent();
//                            logger.warning("Parent "+parent);
//                            comp = parent;
//                        } while(comp!=null);
//                    }
                    // Hack until we get lg3d focus manager
                    // always give focus back to the canvas3d
//                    if (opposite!=null) {
                        hiddenFrame.dispatchEvent(new WindowEvent(opposite, WindowEvent.WINDOW_GAINED_FOCUS, (java.awt.Window)hiddenFrame));
                        hiddenFrame.dispatchEvent(new WindowEvent(opposite, WindowEvent.WINDOW_ACTIVATED, (java.awt.Window)hiddenFrame));
//                    }
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
     * This routine is duplicated in PeerBase. If you find any bugs here please
     * be sure to fix them in PeerBase as well.
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
        return new Point((int)((x/width3D)*hiddenFrame.getWidth()),(int)((y/height3D)*hiddenFrame.getHeight()));
    }
    
}
