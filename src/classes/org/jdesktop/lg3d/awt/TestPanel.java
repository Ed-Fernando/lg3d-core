/**
 * Project Looking Glass
 *
 * $RCSfile: TestPanel.java,v $
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
 * $Date: 2006-06-30 20:37:59 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.awt;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JWindow;

/**
 *
 * @author paulby
 */
class TestPanel extends JPanel implements Lg3dBackBuffer.BufferFlipListener {

    private BufferedImage image;
    private Container container;
    private Insets insets;
    
    public TestPanel(Container container) {
        super();
        this.container = container;
    }
    
    private void setImage(BufferedImage image) {
        System.out.println("**** setImage "+image);
        this.image = image;
        insets = container.getInsets();
        Dimension d = new Dimension(image.getWidth(),image.getHeight());
        if (getPreferredSize()!=d) {
            setSize(d);
            setPreferredSize(d);
            setMinimumSize(d);
            setMaximumSize(d);
            revalidate();
            if (container instanceof JWindow)
                ((JWindow)container).pack();
            else if (container instanceof JFrame)
                ((JFrame)container).pack();
            else
                throw new RuntimeException("Unsupported container "+container);
        }
        repaint();
    }
    
    private boolean red = true;
    public void paint(Graphics g) {
        if (image!=null) {
            g.drawImage(image,0,0,null);
            if(red)
                g.setColor(Color.red);
            else
                g.setColor(Color.blue);
            red=!red;
            g.drawRect(0,0, image.getWidth()-1,image.getHeight()-1);
        }
    }
    
    public void renderBuffer(Lg3dBackBuffer buffer) {
        System.out.println("******* Buffer Flip *******");
        setImage(buffer.getImage());
    }
}
