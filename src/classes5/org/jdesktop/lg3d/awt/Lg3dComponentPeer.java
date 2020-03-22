/**
 * Project Looking Glass
 *
 * $RCSfile: Lg3dComponentPeer.java,v $
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
 * $Date: 2007-03-22 08:38:20 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.awt;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.BufferCapabilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.FocusEvent;
import sun.awt.AppContext;
import sun.awt.SunToolkit;
import java.util.logging.Logger;
import java.awt.peer.*;
import javax.swing.SwingUtilities;
import org.jdesktop.lg3d.toolkit.lg3dtoolkit;
import org.jdesktop.lg3d.toolkit.NonLGJFrame;
import org.jdesktop.lg3d.toolkit.NonLGJWindow;



/**
 *
 * @author paulby
 */
public class Lg3dComponentPeer implements java.awt.peer.ComponentPeer, SceneManagerListener {
    
    protected Logger logger = Logger.getLogger("lg.awt.peer");
    protected ComponentPeer realPeer;
    protected Lg3dGraphics2D graphics=null;
    
    protected TestPanel testPanel;
    
    protected Lg3dBackBuffer backBuffer;
    
    protected boolean usePeer = false;
    
    protected java.awt.Component awtComponent;
    
    private Font font = null;
    private Color color = null;
    private Color backgroundColor = null;
    
    private Toolkit osToolkit;
    
    private KeyboardFocusManager keyboardFocusManager;
    
    private int mouseX=0;
    private int mouseY=0;
    
    private Container f;
    
    private boolean gotFocus = false;
    
    protected PeerInterface peer;
    
    private boolean debug = false;
    
    /** Creates a new instance of Lg3dFramePeer */
    public Lg3dComponentPeer(java.awt.peer.ComponentPeer realPeer, java.awt.Component component, Toolkit osToolkit) {
        this.awtComponent = component;
        this.osToolkit = osToolkit;
        //logger.severe("Real peer "+realPeer.getClass());
        //logger.info("Awt component "+component);
        
        keyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        
        // TODO is this required ? I don't think so
        // component.peer = this;
        
        if (usePeer)
            this.realPeer = realPeer;
                
        String peerClass;
        
        if (this instanceof SwingNodePeer) {
            // SwingNodePeer is a subclass of Lg3dFramePeer so must
            // appear first in this if/else block.
            peerClass = "org.jdesktop.lg3d.awtpeer.SwingNodePeerImpl";        
        } else if (this instanceof Lg3dFramePeer) {
            if (debug)
                f = new NonLGJFrame("Test");
            peerClass = "org.jdesktop.lg3d.awtpeer.FramePeerImpl";
        } else if (this instanceof Lg3dWindowPeer) {
            if (debug)
                f = new NonLGJWindow();
            peerClass = "org.jdesktop.lg3d.awtpeer.WindowPeerImpl";
        } else if (this instanceof Lg3dHeavyWeightWindowPeer) {
            if (debug)
                f = new NonLGJWindow();
            peerClass = "org.jdesktop.lg3d.displayserver.HeavyWeightWindowPeerImpl";
        } else
            throw new RuntimeException("Unsupported peer "+this);
        if (debug)
            testPanel = new TestPanel(f);

        logger.info("PeerClass "+peerClass);
        ClassLoader classLoader = ((lg3dtoolkit)Toolkit.getDefaultToolkit()).getLg3dClassLoader();
        try {
            Class clazz = classLoader.loadClass(peerClass);
            peer = (PeerInterface)clazz.newInstance();
            peer.setComponent(awtComponent);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        peer.setEnabled(true);
        peer.addSceneManagerListener(this);
        
	Rectangle r = component.getBounds();
	setBounds(r.x, r.y, r.width, r.height, SET_BOUNDS);

//        if (debug) {
//            f.addMouseMotionListener(new MouseMotionListener() {
//               public void mouseMoved(MouseEvent evt) {
//                   repostEvent(evt,false);
//                   mouseX=evt.getX();
//                   mouseY=evt.getY();
//                   if (!gotFocus) {
//                       awtComponent.requestFocus();
//                       gotFocus = true;
//                   }
//               } 
//
//               public void mouseDragged(MouseEvent evt) {
//                   repostEvent(evt,false);
//               }
//            });
//        
//            f.addMouseListener(new MouseListener() {
//
//                public void mouseClicked(MouseEvent evt) {
//                    repostEvent(evt, true);
//                }
//
//                public void mousePressed(MouseEvent evt) {
//                    repostEvent(evt,true);
//                }
//
//                public void mouseReleased(MouseEvent evt) {
//                    repostEvent(evt,true);
//                }
//
//                public void mouseExited(MouseEvent evt) {                
//                   //repostEvent(evt,false);
//                }
//
//                public void mouseEntered(MouseEvent evt) {                
//                   //repostEvent(evt,false);
//                }
//            });
//
//            f.addKeyListener(new KeyListener() {
//                public void keyPressed(KeyEvent evt) {
//                    repostEvent(evt);
//                }
//
//                public void keyReleased(KeyEvent evt) {
//                    repostEvent(evt);
//                }
//
//                public void keyTyped(KeyEvent evt) {
//                    repostEvent(evt);
//                }
//            });
//
//            // TODO f.addMouseWheelListener.....
//
//            if (f instanceof JFrame) {
//                ((JFrame)f).getContentPane().setLayout(new BorderLayout());
//                ((JFrame)f).getContentPane().add(BorderLayout.CENTER,testPanel);
//                ((JFrame)f).validate();
//                ((JFrame)f).setLocation(component.getLocation());
//                ((JFrame)f).pack();
//            } else {
//                ((JWindow)f).getContentPane().setLayout(new BorderLayout());
//                ((JWindow)f).getContentPane().add(BorderLayout.CENTER,testPanel);
//                ((JWindow)f).validate();
//                ((JWindow)f).setLocation(component.getLocation());
//                ((JWindow)f).pack();            
//            }
//            //f.setVisible(true);
//        }
    }
    
    /**
     * For debug only
     */
    private void repostEvent(MouseEvent evt, boolean button) {
        MouseEvent newEvt;
        Insets insets = f.getInsets();
        
        button = true;
        
        if (button)
            newEvt = new MouseEvent( awtComponent, 
                                            evt.getID(), 
                                            evt.getWhen(),
                                            evt.getModifiersEx(), 
                                            evt.getX()-insets.left, 
                                            evt.getY()-insets.top, 
                                            evt.getClickCount(), 
                                            evt.isPopupTrigger(), 
                                            evt.getButton() );
        else
            newEvt = new MouseEvent( awtComponent, 
                                            evt.getID(), 
                                            evt.getWhen(),
                                            evt.getModifiersEx(), 
                                            evt.getX()-insets.left, 
                                            evt.getY()-insets.top, 
                                            evt.getClickCount(), 
                                            evt.isPopupTrigger() );
        // XXX: osToolkit.getEventQueue().postEvent(newEvt);
        ((SunToolkit)osToolkit).postEvent(AppContext.getAppContext(), newEvt);
        //logger.info("Posting "+newEvt);
    }
    
    /**
     * For debug only
     */
    private void repostEvent(KeyEvent evt) {
        KeyEvent newEvt = new KeyEvent(awtComponent, 
                evt.getID(), 
                evt.getWhen(), 
                evt.getModifiersEx(), 
                evt.getKeyCode(), 
                evt.getKeyChar(), 
                evt.getKeyLocation());
        //osToolkit.getEventQueue().postEvent(newEvt);
        //logger.info("Posting "+newEvt);
        keyboardFocusManager.dispatchEvent(newEvt);
    }

    public boolean canDetermineObscurity() {
        logger.info("canDetermineObscurity");
        if (usePeer)
            return realPeer.canDetermineObscurity();
        else
            return true;
    }

    public int checkImage(Image image, int width, int height, java.awt.image.ImageObserver imageObserver) {
        // Complete
        logger.finer("checkImage");
        if (usePeer)
            return realPeer.checkImage(image,width,height,imageObserver);
        else
            return osToolkit.checkImage(image,width,height,imageObserver);
    }

    public void coalescePaintEvent(java.awt.event.PaintEvent paintEvent) {
        logger.fine("coalescePaintEvent");
        if (usePeer)
            realPeer.coalescePaintEvent(paintEvent);
    }

    public void createBuffers(int param, BufferCapabilities bufferCapabilities) throws AWTException {
        logger.info("createBuffers");
        if (usePeer)
            realPeer.createBuffers(param,bufferCapabilities);
    }

    public Image createImage(java.awt.image.ImageProducer imageProducer) {
        // Complete
        logger.finer("createImage");
        if (usePeer)
            return realPeer.createImage(imageProducer);
        else
            return osToolkit.createImage(imageProducer);
    }

    public Image createImage(int width, int height) {
        logger.info("createImage");
        if (usePeer)
            return realPeer.createImage(width,height);
        else
            throw new RuntimeException("Not Implemented");

    }

    public java.awt.image.VolatileImage createVolatileImage(int width, int height) {
        logger.info("createVolatileImage");
        if (usePeer)
            return realPeer.createVolatileImage(width,height);
        else
            return new Lg3dVolatileImage(width,height);
    }

    public void destroyBuffers() {
        logger.info("destroyBuffers");
        if (usePeer)
            realPeer.destroyBuffers();
        throw new RuntimeException("Not Implemented");
        
    }

    public void dispose() {
        logger.info("dispose");
        if (usePeer)
            realPeer.dispose(); 
//        logger.severe("Lg3dComponentPeer dispose() Not Implemented");
        peer.setEnabled(false);
    }

    public void flip(BufferCapabilities.FlipContents flipContents) {
        logger.info("flip");
        if (usePeer)
            realPeer.flip(flipContents);
        backBuffer.contentsChanged();
   }

    public Image getBackBuffer() {
        logger.info("getBackBuffer");
        return backBuffer.getImage();
    }

    public Rectangle getBounds() {
        logger.info("getBounds");
        if (usePeer)
            return realPeer.getBounds();
        else
            return awtComponent.getBounds();
    }

    public java.awt.image.ColorModel getColorModel() {
        // Complete
        logger.finer("getColorModel");
        if (usePeer)
            return realPeer.getColorModel();
        else
            return osToolkit.getColorModel();
    }
    
    @SuppressWarnings("deprecation") // ignore warnings against getFontMetrics()
    public FontMetrics getFontMetrics(Font font) {
        // Complete
        logger.finer("getFontMetrics");
        if (usePeer)
            return realPeer.getFontMetrics(font);
        else
            return osToolkit.getFontMetrics(font);
    }

    public Graphics getGraphics() {
        logger.finer("getGraphics");
        //return realPeer.getGraphics();
        if (graphics==null)
            return null;
        return graphics.create();
    }

    public GraphicsConfiguration getGraphicsConfiguration() {
        logger.info("getGraphicsConfiguration");
        if (usePeer)
            return realPeer.getGraphicsConfiguration();
        else
            throw new RuntimeException("Not Implemented");
    }

    public Point getLocationOnScreen() {
        Point loc;
        if (usePeer)
            loc = realPeer.getLocationOnScreen();
        else
            loc = awtComponent.getBounds().getLocation();
        logger.finer("getLocationOnScreen "+loc);
        return loc;
    }

    public Dimension getMinimumSize() {
        logger.finer("getMinimumSize");
        if (usePeer)
            return realPeer.getMinimumSize();
        else
            throw new RuntimeException("Not Implemented");
    }

    public Dimension getPreferredSize() {
        logger.fine("getPreferredSize");
        if (usePeer)
            return realPeer.getPreferredSize();
        else
            throw new RuntimeException("Not Implemented");
    }

    public Toolkit getToolkit() {
        logger.finer("getToolkit");
        if (usePeer)
            return realPeer.getToolkit();
        else 
            return Toolkit.getDefaultToolkit();
    }

    private static Lg3dMethod processEventMethod =
		new Lg3dMethod(Component.class, "processEvent", AWTEvent.class);

    public void handleEvent(AWTEvent aWTEvent) {
        logger.info("handleEvent "+aWTEvent);
        if (usePeer) {
            realPeer.handleEvent(aWTEvent);
            return;
        }
        
	processEventMethod.invoke(awtComponent, aWTEvent);
    }

    public boolean handlesWheelScrolling() {
        logger.fine("handlesWheelScrolling");
        if (usePeer)
            return realPeer.handlesWheelScrolling();
        else
            return false;
    }

    public boolean isFocusable() {
        logger.info("isFocusable");
        if (usePeer)
            return realPeer.isFocusable();
        else
            return true;
    }

    public boolean isObscured() {
        logger.info("isObscured");
        if (usePeer)
            return realPeer.isObscured();
        else
            return false;
    }

    public boolean isReparentSupported() {
        logger.info("isReparentSupported");
        if (usePeer)
            return realPeer.isReparentSupported();
        else
            return false;
            
    }

    public void layout() {
        logger.info("layout");
        if (usePeer)
            realPeer.layout();
        else
            awtComponent.doLayout();
    }

    public void paint(Graphics graphics) {
        logger.info("paint");
        if (usePeer)
            realPeer.paint(graphics);
        else
            awtComponent.paint(graphics);
        backBuffer.contentsChanged();
    }

    public boolean prepareImage(Image image, int w, int h, java.awt.image.ImageObserver imageObserver) {
        logger.info("prepareImage");
        if (usePeer)
            return realPeer.prepareImage(image,w,h, imageObserver);
        else
            throw new RuntimeException("Not Implemented");
    }

    public void print(Graphics graphics) {
        logger.info("print");
        if (usePeer)
            realPeer.print(graphics);
        else
            throw new RuntimeException("Not Implemented");
    }

    public void repaint(long tm, int x, int y, int width, int height) {
        logger.warning("repaint");
        if (usePeer)
            realPeer.repaint(tm,x,y,width,height);
        else
            awtComponent.repaint();
    }

    public void reparent(java.awt.peer.ContainerPeer containerPeer) {
        logger.warning("reparent");
        if (usePeer)
            realPeer.reparent(containerPeer);
        else
            throw new RuntimeException("Not Implemented");
    }

    private static Lg3dMethod setGlobalFocusOwnerMethod =
		new Lg3dMethod(KeyboardFocusManager.class, "setGlobalFocusOwner", Component.class);

    public boolean requestFocus(Component lightweightChild, boolean temporary, boolean focusedWindowChangeAllowed, long time) {
        logger.info("requestFocus not fully implemented");
//        if (usePeer)
//            return realPeer.requestFocus(component,temporary,focusedWindowChangeAllowed,time);
//        else
//            logger.severe("requestFocus Not Implemented "+component);
//                
        // Experimental code, probably an attempt to get flashing cursor to work
//        Toolkit.getDefaultToolkit().getEventQueue().postEvent(new FocusEvent(lightweightChild,FocusEvent.FOCUS_GAINED,temporary));
//        if (awtComponent!=lightweightChild)
//            lightweightChild.requestFocus();
        
	setGlobalFocusOwnerMethod.invoke(keyboardFocusManager, lightweightChild);
        
        return true;
    }
       
    public void setBackground(Color color) {
        logger.info("setBackground");
        if (usePeer)
            realPeer.setBackground(color);
        else
            graphics.setBackground(color);
    }

    public void setBounds(int x, int y, int width, int height, int op) {
//        if (this instanceof Lg3dWindowPeer)
//            logger.warning("*************************** WINDOW ****************");
//        logger.warning("setBounds "+x+","+y+"   "+width+","+height);
        setBufferSize(width,height);
        peer.setLocation(x,y);
        
        if (usePeer)
            realPeer.setBounds(x,y,width,height,op);
    }

    public void setEnabled(boolean param) {
        logger.info("setEnabled");
        if (usePeer)
            realPeer.setEnabled(param);
        else
            f.setEnabled(param);
    }

    public void setFont(Font font) {
        // Complete
        logger.info("setFont");
        if (usePeer)
            realPeer.setFont(font);
        else
            graphics.setFont(font);
    }

    public void setForeground(Color color) {
        logger.info("setForeground");
        if (usePeer)
            realPeer.setForeground(color);
        else
            graphics.setColor(color);
    }

    public void setVisible(boolean visible) {
        logger.info("setVisible "+visible);
        if (usePeer)
            realPeer.setVisible(visible);
        else {
            peer.setVisible(visible);
            if (visible) {
                Graphics g = getGraphics();  // If setBounds has not been called graphics will be null
                if (g!=null)
                    awtComponent.paint(g);          // This will eventually call paintImage, which will call backBuffer.contentsChanged()
            }
            if (debug)
                f.setVisible(visible);
        }
    }

    public void updateCursorImmediately() {
        logger.finer("updateCursorImmediately");
        if (usePeer)
            realPeer.updateCursorImmediately();
        else {
            // TODO figure out how to get the Swing cursor image
            // TODO remove insets in real code
            if (debug) {
                Insets insets = f.getInsets();
                Component comp = SwingUtilities.getDeepestComponentAt(awtComponent, mouseX-insets.left, mouseY-insets.top);
                if (comp!=null) {
                    Cursor cursor = comp.getCursor();
                    f.setCursor(cursor);
                }
            }
        }
    }

    void setBufferSize(int width, int height) {
        if (width==0 || height==0)
            return;
        
        if (backBuffer==null) {
            backBuffer = new Lg3dBackBuffer(width,height);
            if (debug)
                backBuffer.addBufferFlipListener(testPanel);
            graphics = new Lg3dGraphics2D(backBuffer);
            backBuffer.addBufferResizeListener(graphics);
            backBuffer.addBufferFlipListener(peer);
            backBuffer.addBufferResizeListener(peer);
        } else {
            boolean wasResized = backBuffer.ensureSize(width, height);
            if (wasResized)
                awtComponent.repaint();
        }
                
    }    
    
    /******************************************
     * SceneManagerListener implemenation
     ******************************************/

    public void windowMoved(int x, int y) {
        awtComponent.setLocation(x,y);
    }
    
    /******************************************
     * End SceneManagerListener implemenation
     ******************************************/
    
    /**
     * DEPRECATED:  Replaced by setVisible(boolean).
     */
    public void show() {
        setVisible(true);
    }
    
    /**
     * DEPRECATED:  Replaced by setVisible(boolean).
     */
    public void hide() {
        setVisible(false);
    }
    
    /**
     * DEPRECATED:  Replaced by getPreferredSize().
     */
    public Dimension preferredSize() {
        return getPreferredSize();
    }

    /**
     * DEPRECATED:  Replaced by getMinimumSize().
     */
    public Dimension minimumSize() {
        return getMinimumSize();
    }

    /**
     * DEPRECATED:  Replaced by setEnabled(boolean).
     */
    public void disable() {
        setEnabled(false);
    }

    /**
     * DEPRECATED:  Replaced by setEnabled(boolean).
     */
    public void enable() {
        setEnabled(true);
    }

    /**
     * DEPRECATED:  Replaced by setBounds(int, int, int, int).
     */
   public void reshape(int x, int y, int width, int height) {
        setBounds(x,y,width, height, SET_BOUNDS );
    }

   
}
