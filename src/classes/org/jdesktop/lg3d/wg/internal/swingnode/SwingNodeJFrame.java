/**
 * Project Looking Glass
 *
 * $RCSfile: SwingNodeJFrame.java,v $
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
 * $Date: 2007-01-04 22:40:23 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.wg.internal.swingnode;

import javax.swing.JFrame;
import org.jdesktop.lg3d.sg.Texture2D;

/**
 * The underlying JWindow used to contain the SwingNode JPanel. This window is
 * never rendered directly, instead it's contents are passed as a texture
 * to the TextureChangedListeners for rendering in SwingNode.
 *
 * This class is an implementation detail and should NOT be instantiated by
 * users.
 *
 * @author paulby
 */
public class SwingNodeJFrame extends JFrame {

    private SwingNodeJFrame.TextureChangedListener listener;
    
    public SwingNodeJFrame() {
        super();
        setName("SwingNodeJFrame");
    }

    public void notifyTextureChangedListeners( Texture2D texture ) {
        if (listener!=null)
            listener.textureChanged(texture);
    }
    
    /**
     * Add a listener which will be notified when the texture changes. This is only
     * called when the texture object itself has changed not when the ImageComponent
     * is updated.
     */
    public void addTextureChangedListener( TextureChangedListener listener ) {
        this.listener = listener;
    }
    
    public interface TextureChangedListener {
        /**
         * Called with the new texture that contains the swing nodes image.
         * Only called when a new texture is created, usually because the size has changed
         */
        public void textureChanged(Texture2D texture);
    }
}
