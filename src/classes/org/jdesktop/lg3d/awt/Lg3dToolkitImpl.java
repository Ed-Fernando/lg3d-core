/**
 * Project Looking Glass
 *
 * $RCSfile: Lg3dToolkitImpl.java,v $
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
 * $Date: 2007-03-22 08:38:16 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.awt;

import java.awt.Toolkit;
import java.awt.Component;
import java.awt.Window;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import org.jdesktop.lg3d.awt.Lg3dFocusManagerPeer;
import org.jdesktop.lg3d.toolkit.lg3dtoolkit;

/**
 *
 * @author paulby
 */
public class Lg3dToolkitImpl implements lg3dtoolkit {
    
    private Logger logger = Logger.getLogger("lg.awt.peer.toolkit");
    
    private boolean lg3dOnly = false;
    private ClassLoader classLoader = null;
    private boolean defaultLWPopupEnabledKey;

    private Toolkit osToolkit;
    private ToolkitInterface lgToolkit = null;
    
    public Lg3dToolkitImpl(Toolkit osToolkit) {
        this.osToolkit = osToolkit;
	logger.info("Using LgToolkit = " + osToolkit.getClass().getName());
    }
    
    /*
     * Implementation on the "lg3dtoolkit" interface...
     */
    public boolean enableLg3d(ClassLoader classLoader) {
        try {
            Class lgToolkitClass = classLoader.loadClass("org.jdesktop.lg3d.awtpeer.LgToolkit");
            lgToolkit = (ToolkitInterface)lgToolkitClass.newInstance();
	    this.classLoader = classLoader;

	    java.awt.KeyboardFocusManager.setCurrentKeyboardFocusManager(
					new Lg3dKeyboardFocusManager());

	    defaultLWPopupEnabledKey =
		Boolean.valueOf(System.getProperty(
		    "org.jdesktop.lg3d.awt.defaultLWPopupEnabledKey", "true"));

	    javax.swing.JPopupMenu.setDefaultLightWeightPopupEnabled(
						    defaultLWPopupEnabledKey);

	    lg3dOnly = true;
	    logger.info("Enabling LgToolkit");
        } catch(Exception e) {
            logger.log(Level.SEVERE,"Unable to create LgToolkit ",e);
        }

	return lg3dOnly;
    }
    
    public ClassLoader getLg3dClassLoader() {
        return classLoader;
    }

    /*
     * Ovveridden methods...
     */
    public java.awt.peer.FramePeer createFrame(java.awt.Frame frame) throws java.awt.HeadlessException {
        logger.info("@override: createFrame " + frame.getClass());

        if (frame instanceof org.jdesktop.lg3d.wg.internal.swingnode.SwingNodeJFrame)
            return new SwingNodePeer(null, frame, osToolkit);
        
        return new Lg3dFramePeer(null, frame, osToolkit);
    }
    
    private static Lg3dClass javaxSwingPopupClass =
		    new Lg3dClass(javax.swing.Popup.class);

    public java.awt.peer.WindowPeer createWindow(java.awt.Window window) throws java.awt.HeadlessException {
	logger.info("@override: createWindow " + window.getClass());

	if (javaxSwingPopupClass.isInstance(window, "HeavyWeightWindow"))
	    return new Lg3dHeavyWeightWindowPeer(null, window, osToolkit);
	else
	    return new Lg3dWindowPeer(null, window, osToolkit);
    }

    private static Lg3dField forceHeavyWeightPopupKeyField
	= new Lg3dField(javax.swing.PopupFactory.class, "forceHeavyWeightPopupKey");

    public java.awt.peer.LightweightPeer createComponent(Component target, java.awt.peer.LightweightPeer lwp) {
        if (!defaultLWPopupEnabledKey && (target instanceof JComponent)) {
	    logger.info("@override: createComponent " + target.getClass());
	    ((JComponent)target).putClientProperty(forceHeavyWeightPopupKeyField.get(null), Boolean.TRUE);
	} else
	    logger.info("createComponent " + target.getClass());

	return lwp;
    }

    public java.awt.Dimension getScreenSize() throws java.awt.HeadlessException {
        logger.info("@override: getScreenSize");

        if (lgToolkit == null)
            throw new NullPointerException("lgToolkit");

        return lgToolkit.getScreenSize();
    }

    public java.awt.peer.DialogPeer createDialog(java.awt.Dialog dialog) throws java.awt.HeadlessException {
        logger.info("@override: createDialog");
        return new Lg3dDialogPeer(null, dialog, osToolkit);
    }

    java.awt.peer.KeyboardFocusManagerPeer createKeyboardFocusManagerPeer(java.awt.peer.KeyboardFocusManagerPeer peer) {
        logger.info("@override: createKeyboardFocusManagerPeer");
        return new Lg3dFocusManagerPeer(peer);
    }
}
