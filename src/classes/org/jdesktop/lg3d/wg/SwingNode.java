/**
 * Project Looking Glass
 *
 * $RCSfile: SwingNode.java,v $
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
 * $Revision: 1.16 $
 * $Date: 2007-01-04 22:40:22 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.wg;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import javax.swing.JPanel;
import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.displayserver.nativewindow.NativeWindowFuzzyEdgePanel;
import org.jdesktop.lg3d.sg.Appearance;
import org.jdesktop.lg3d.sg.PolygonAttributes;
import org.jdesktop.lg3d.sg.Texture2D;
import org.jdesktop.lg3d.sg.TextureAttributes;
import org.jdesktop.lg3d.wg.internal.swingnode.SwingNodeJFrame;
import org.jdesktop.j3d.utils.math.Math3D;
import org.jdesktop.lg3d.sg.TransparencyAttributes;


/**
 * A Node to which a Swing JPanel can be added which enables the Swing
 * component to be rendered in the LG environment.
 *
 * @author Paul
 */
public class SwingNode extends Component3D {
    
    private SwingNodeJFrame hiddenFrame;
    private JPanel panel;

    private float localWidth;

    private float localHeight;
    
    private SwingNodeRenderer comp;
    
    /**
     * Create a SwingNode with the default geometry
     */
    public SwingNode() {
        this(null);
    }

    /** Creates a new instance of SwingNode
     * 
     * If geometryUpdater is null the default geometry will be used. 
     */
    public SwingNode(SwingNodeRenderer geometryUpdater) {
        hiddenFrame = new SwingNodeJFrame();
        
        if (geometryUpdater==null)
            comp = new DefaultSwingNodeRenderer();
        else
            comp = geometryUpdater;
        comp.setup(hiddenFrame, this);
        hiddenFrame.addTextureChangedListener(comp);
        
        comp.addMouseHandlers(this);

        addChild(comp);   
        this.setCursor(Cursor3D.MEDIUM_CURSOR);
    }
    
    /**
     * @deprecated use setJPanel
     */
    public void setPanel(JPanel p) {
        setJPanel(p);
    }

    
    private void printHeirarchy(Container c, int depth) {
        if (depth == 0) {
            for (int i=0; i<depth; i++) {
                System.out.print("\t");
            }
            System.out.print(c.getClass().getName());
            System.out.println(" (" + c.isOpaque() + ")");
            c.setBackground(Color.ORANGE);
        }
        for (Component comp : c.getComponents()) {
            for (int i=0; i<depth; i++) {
                System.out.print("\t");
            }
            System.out.print(comp.getClass().getName());
            System.out.println(" (" + comp.isOpaque() + ")");
            comp.setBackground(Color.RED);
            
            if (comp instanceof Container) {
                printHeirarchy((Container)comp, depth+1);
            }
        }
    }
    
    /**
     * Set the swing JPanel that this SwingNode will render
     */
    public void setJPanel(JPanel p) {
        this.panel = p;
        comp.setPanel(p);
        
        hiddenFrame.setContentPane(panel);
        hiddenFrame.pack();

        //dump the heirarchy
        printHeirarchy(hiddenFrame, 0);
        
        //logger.severe("3D size "+panel.getWidth()+" "+panel.getHeight()+"  "+NativePopupLookAndFeel.widthNativeToPhysical(panel.getWidth())+" "+NativePopupLookAndFeel.heightNativeToPhysical(panel.getHeight()));
        hiddenFrame.setVisible(true);
        final Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
        localWidth = toolkit3d.widthNativeToPhysical(panel.getWidth());
        localHeight = toolkit3d.heightNativeToPhysical(panel.getHeight());
        
        panel.addComponentListener( new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent event) {
                localWidth = toolkit3d.widthNativeToPhysical(panel.getWidth());
                localHeight = toolkit3d.heightNativeToPhysical(panel.getHeight());
            }
        });
    }
    
    public JPanel getJPanel() {
        return panel;
    }
           
    /**
     * Returns the width of the panel in 3D space
     */
    public float getLocalWidth() {
        return localWidth;
    }

    /**
     * Return the heigth of the panel in 3D space
     */
    public float getLocalHeight() {
        return localHeight;
    }
 
    class DefaultSwingNodeRenderer extends SwingNodeRenderer {
               
        private Appearance swingAppearance;
        private NativeWindowFuzzyEdgePanel body;
        
        public DefaultSwingNodeRenderer() {
            width3D = 0.08f;
            height3D = 0.06f;

            swingAppearance = new Appearance();
            TextureAttributes texAttr = new TextureAttributes();
            texAttr.setTextureMode(TextureAttributes.REPLACE);
            swingAppearance.setTextureAttributes(texAttr);
            swingAppearance.setCapability(Appearance.ALLOW_TEXTURE_WRITE);
	    swingAppearance.setPolygonAttributes(
		new PolygonAttributes(
		    PolygonAttributes.POLYGON_FILL,
		    PolygonAttributes.CULL_NONE,
		    0.0f, false, 0.0f
		    ));            
            
            swingAppearance.setTransparencyAttributes(
                    new TransparencyAttributes(TransparencyAttributes.FASTEST, 0.8f));
//            Material mat = new Material(new Color3f(1f,0f,0f), new Color3f(1f,0f,0f), new Color3f(1f,0f,0f), new Color3f(1f,0f,0f), 64f);
//            swingAppearance.setMaterial(mat);

            
            body = new NativeWindowFuzzyEdgePanel(width3D, height3D, swingAppearance);
            addChild(body);
        }
        
        public void textureChanged(Texture2D texture) {
            int swingImageWidth = panel.getWidth();
            int swingImageHeight = panel.getHeight();
            float p2width = texture.getWidth();
            float p2height = texture.getHeight();
            Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
            float localWidth = toolkit3d.widthNativeToPhysical(panel.getWidth());
            float localHeight = toolkit3d.heightNativeToPhysical(panel.getHeight());
            
            swingAppearance.setTexture(texture);
            if (localWidth!=width3D || localHeight!=height3D) {
                width3D=localWidth;
                height3D=localHeight;
                body.setSize(width3D, height3D,
                        p2width/(float)swingImageWidth, 
                        p2height/(float)swingImageHeight);
                setPreferredSize(new Vector3f(width3D, height3D, 0f));   
            }
        } 
    }
}
