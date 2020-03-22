/**
 * Project Looking Glass
 *
 * $RCSfile: Lg3dContainerPeer.java,v $
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
 * $Date: 2006-06-30 20:37:51 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.awt;

import java.awt.Insets;
import java.awt.Toolkit;

/**
 *
 * @author paulby
 */
public class Lg3dContainerPeer extends Lg3dComponentPeer implements java.awt.peer.ComponentPeer {
    
    private Insets insets = null;

    /** Creates a new instance of Lg3dContainerPeer */
    public Lg3dContainerPeer(java.awt.peer.ComponentPeer realPeer, java.awt.Component frame, Toolkit osToolkit) {
        super(realPeer, frame, osToolkit);
        if (!usePeer) {
            insets = new Insets(0,0,0,0);
        }
    }
    
    public void beginLayout() {
        logger.fine("beginLayout");
        if (usePeer)
            ((Lg3dContainerPeer)realPeer).beginLayout();
//        Rectangle bounds = getBounds();
//        if (bounds!=null)
//            setBufferSize(bounds.width, bounds.height);
    }

    public void beginValidate() {
        logger.fine("beginValidate");
        if (usePeer)
            ((Lg3dContainerPeer)realPeer).beginValidate();
    }

    public void cancelPendingPaint(int param, int param1, int param2, int param3) {
        logger.fine("cancelPendingPaint");
        if (usePeer)
            ((Lg3dContainerPeer)realPeer).cancelPendingPaint(param,param1,param2,param3);
    }

    public boolean isPaintPending() {
        logger.info("isPaintPending");
        if (usePeer)
            return ((Lg3dContainerPeer)realPeer).isPaintPending();
        else
            throw new RuntimeException("Not implemented");
   }

    public void endLayout() {
        logger.fine("endLayout");
        if (usePeer)
            ((Lg3dContainerPeer)realPeer).endLayout();
        
        // TODO - taken from X code
//        if (!paintPending && !paintArea.isEmpty()
//            && !ComponentAccessor.getIgnoreRepaint(target))
//        {
            // if not waiting for native painting repaint damaged area
//            postEvent(new PaintEvent(awtComponent, PaintEvent.PAINT, 
//                                     new Rectangle()));
//        }
        
        logger.info("endLayout should cause a repaint, check impl");
        awtComponent.repaint();
    }

    public void endValidate() {
        logger.fine("endValidate");
        if (usePeer)
            ((Lg3dContainerPeer)realPeer).endValidate();
    }

    public Insets getInsets() {
        logger.fine("getInsets");
        if (usePeer)
            return ((Lg3dContainerPeer)realPeer).getInsets();
        else
            return insets;
    }

    public Insets insets() {
        return getInsets();
    }
    
    public boolean isRestackSupported() {
        logger.info("isRestackSupported");
        if (usePeer)
            return ((Lg3dContainerPeer)realPeer).isRestackSupported();
        else 
            return false;
    }

    public void restack() {
        logger.info("restack");
        if (usePeer)
            ((Lg3dContainerPeer)realPeer).restack();
        else
            throw new RuntimeException("Not Implemented");
    }

}
