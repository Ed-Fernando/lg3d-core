/**
 * Project Looking Glass
 *
 * $RCSfile: mlg3dtoolkit.java,v $
 *
 * Copyright (c) 2007, Sun Microsystems, Inc., All Rights Reserved
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
 * $Date: 2007-05-22 18:33:24 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.awt;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.lg3d.toolkit.lg3dtoolkit;
import org.jdesktop.lg3d.toolkit.NonLGJFrame;
import org.jdesktop.lg3d.toolkit.NonLGJWindow;

/**
 *
 * @author paulby
 */
// ignore warnings against getFontist(), getFontMetrics() and getFontPeer()
@SuppressWarnings("deprecation")
public class mlg3dtoolkit extends apple.awt.CToolkit implements lg3dtoolkit {
    private Logger logger = Logger.getLogger("lg.awt.peer.toolkit");
    private Lg3dToolkitImpl toolkitImpl;
    private boolean lg3dOnly;

    public mlg3dtoolkit() {
	toolkitImpl = new Lg3dToolkitImpl(this);
	lg3dOnly = false;
    }

    /*
     * Implementation on the "lg3dtoolkit" interface...
     */
    public boolean enableLg3d(ClassLoader classLoader) {
	lg3dOnly = toolkitImpl.enableLg3d(classLoader);
	return lg3dOnly;
    }

    public ClassLoader getLg3dClassLoader() {
	return toolkitImpl.getLg3dClassLoader();
    }

    /*
     * Ovveridden methods...
     */
    public java.awt.peer.FramePeer createFrame(java.awt.Frame frame) throws java.awt.HeadlessException {
        if (lg3dOnly && !(frame instanceof NonLGJFrame))
	    return toolkitImpl.createFrame(frame);

        logger.info("createFrame " + frame.getClass());
	return super.createFrame(frame);
   }
    
    public java.awt.peer.WindowPeer createWindow(java.awt.Window window) throws java.awt.HeadlessException {
        if (lg3dOnly && !(window instanceof NonLGJWindow))
	    return toolkitImpl.createWindow(window);

        logger.info("createWindow " + window.getClass());
        return super.createWindow(window);
   }

    public java.awt.peer.LightweightPeer createComponent(java.awt.Component target) {
	java.awt.peer.LightweightPeer lwp = super.createComponent(target);

        if (lg3dOnly && (target instanceof javax.swing.JComponent))
	    return toolkitImpl.createComponent(target, lwp);

	logger.info("createComponent " + target.getClass());
        return lwp;
   }

    public java.awt.Dimension getScreenSize() throws java.awt.HeadlessException {
	if (lg3dOnly)
	    return toolkitImpl.getScreenSize();

        logger.info("getScreenSize");
	return super.getScreenSize();
    }

    public java.awt.peer.DialogPeer createDialog(java.awt.Dialog dialog) throws java.awt.HeadlessException {
	if (lg3dOnly)
	    return toolkitImpl.createDialog(dialog);

        logger.info("createDialog");
	return super.createDialog(dialog);
    }

    static boolean reportedgetSystemEventQueueImpl = false;
    protected java.awt.EventQueue getSystemEventQueueImpl() {
	if (lg3dOnly) {
	    if (!reportedgetSystemEventQueueImpl) {
		logger.info("getSystemEventQueueImpl");
		reportedgetSystemEventQueueImpl = true;
	    }
	}
        return super.getSystemEventQueueImpl();
    }

    public java.awt.peer.KeyboardFocusManagerPeer createKeyboardFocusManagerPeer(java.awt.KeyboardFocusManager manager) {
        java.awt.peer.KeyboardFocusManagerPeer peer = super.createKeyboardFocusManagerPeer(manager);

	if (lg3dOnly)
	    return toolkitImpl.createKeyboardFocusManagerPeer(peer);

        logger.info("createKeyboardFocusManagerPeer");
	return peer;
    }

    /*
     * Pass through methods... (logging added)
     */
    public void beep() {
	logger.info("beep");
	super.beep();
    }

    public java.awt.Image createImage(java.awt.image.ImageProducer imageProducer) {
        logger.info("createImage");
        return super.createImage(imageProducer);
    }

    public java.awt.Image createImage(String str) {
        logger.info("createImage");
        return super.createImage(str);
    }

    public java.awt.Image createImage(java.net.URL uRL) {
        logger.info("createImage");
        return super.createImage(uRL);
    }

    public java.awt.Image createImage(byte[] values, int param, int param2) {
        logger.info("createImage");
        return super.createImage(values,param,param2);
    }

    public java.awt.peer.PopupMenuPeer createPopupMenu(java.awt.PopupMenu popupMenu) throws java.awt.HeadlessException {
        logger.info("createPopupMenu");
        return super.createPopupMenu(popupMenu);
    }

    public java.awt.image.ColorModel getColorModel() throws java.awt.HeadlessException {
        logger.info("getColorModel");
        return super.getColorModel();
    }
    
    @SuppressWarnings("deprecation") // getFontist has been deprecated
    public String[] getFontList() {
        logger.info("getFontList");
        return super.getFontList();
    }

    @SuppressWarnings("deprecation") // getFontMetrics has been deprecated
    public java.awt.FontMetrics getFontMetrics(java.awt.Font font) {
        logger.info("getFontMetrics");
        return super.getFontMetrics(font);
    }
    
    @SuppressWarnings("deprecation") // getFontPeer has been deprecated
    public java.awt.peer.FontPeer getFontPeer(String str, int param) {
        logger.info("getFontPeer");
        return super.getFontPeer(str,param);
    }

    public java.awt.Image getImage(String str) {
        logger.info("getImage");
        return super.getImage(str);
    }

    public java.awt.Image getImage(java.net.URL uRL) {
        logger.info("getImage");
        return super.getImage(uRL);
    }

    public java.awt.PrintJob getPrintJob(java.awt.Frame frame, String str, java.util.Properties properties) {
        logger.info("getPrintJob");
        return super.getPrintJob(frame,str,properties);
    }

    public int getScreenResolution() throws java.awt.HeadlessException {
        logger.info("getScreenResolution");
        return super.getScreenResolution();
    }

    public java.awt.datatransfer.Clipboard getSystemClipboard() throws java.awt.HeadlessException {
        logger.info("getSystemClipboard");
        return super.getSystemClipboard();
    }

    public boolean prepareImage(java.awt.Image image, int param, int param2, java.awt.image.ImageObserver imageObserver) {
        logger.info("prepareImage");
        return super.prepareImage(image,param, param2, imageObserver);
    }

    public void sync() {
        logger.info("sync");
        super.sync();
    }
    
    public int checkImage(java.awt.Image image, int width, int height, java.awt.image.ImageObserver imageObserver) {
        logger.info("checkImage");
        return super.checkImage(image,width,height,imageObserver);
    }
    
    /*
     * TODO: Not Implemented methods that log warnings - need to be fixed.
     */
    public void grab(java.awt.Window grabbedWindow) {
        if (lg3dOnly) logger.warning("grab not implemented");
	else	      super.grab(grabbedWindow);
    }

    public void ungrab(java.awt.Window grabbedWindow) {
        if (lg3dOnly) logger.warning("ungrab not implemented");
	else	      super.ungrab(grabbedWindow);
    }
    
    /*
     * Not Implemented methods that will throw a RuntimeException
     */
    public java.awt.peer.ButtonPeer createButton(java.awt.Button button) throws java.awt.HeadlessException {
        logger.info("createButton");
        if (lg3dOnly) throw new RuntimeException("Not Implemented");
        return super.createButton(button);
    }

    public java.awt.peer.CanvasPeer createCanvas(java.awt.Canvas canvas) {
        logger.info("createCanvas");
        if (lg3dOnly) throw new RuntimeException("Not Implemented");
        return super.createCanvas(canvas);
    }

    public java.awt.peer.CheckboxPeer createCheckbox(java.awt.Checkbox checkbox) throws java.awt.HeadlessException {
        logger.info("createCheckbox");
        if (lg3dOnly) throw new RuntimeException("Not Implemented");
        return super.createCheckbox(checkbox);
    }

    public java.awt.peer.CheckboxMenuItemPeer createCheckboxMenuItem(java.awt.CheckboxMenuItem checkboxMenuItem) throws java.awt.HeadlessException {
        logger.info("createCheckboxMenuItem");
        if (lg3dOnly) throw new RuntimeException("Not Implemented");
        return super.createCheckboxMenuItem(checkboxMenuItem);
    }

    public java.awt.peer.ChoicePeer createChoice(java.awt.Choice choice) throws java.awt.HeadlessException {
        logger.info("createChoice");
        if (lg3dOnly) throw new RuntimeException("Not Implemented");
        return super.createChoice(choice);
    }

    public java.awt.dnd.peer.DragSourceContextPeer createDragSourceContextPeer(java.awt.dnd.DragGestureEvent dragGestureEvent) throws java.awt.dnd.InvalidDnDOperationException {
        logger.info("createDragSourceContextPeer");
        if (lg3dOnly) throw new RuntimeException("Not Implemented");
        return super.createDragSourceContextPeer(dragGestureEvent);
    }

    public java.awt.peer.FileDialogPeer createFileDialog(java.awt.FileDialog fileDialog) throws java.awt.HeadlessException {
        logger.info("createFileDialog");
        if (lg3dOnly) throw new RuntimeException("Not Implemented");
        return super.createFileDialog(fileDialog);
    }

    public java.awt.peer.LabelPeer createLabel(java.awt.Label label) throws java.awt.HeadlessException {
        logger.info("createLabel");
        if (lg3dOnly) throw new RuntimeException("Not Implemented");
        return super.createLabel(label);
    }

    public java.awt.peer.ListPeer createList(java.awt.List list) throws java.awt.HeadlessException {
        logger.info("createList");
        if (lg3dOnly) throw new RuntimeException("Not Implemented");
        return super.createList(list);
    }

    public java.awt.peer.MenuPeer createMenu(java.awt.Menu menu) throws java.awt.HeadlessException {
        logger.info("createMenu");
        if (lg3dOnly) throw new RuntimeException("Not Implemented");
        return super.createMenu(menu);
    }

    public java.awt.peer.MenuBarPeer createMenuBar(java.awt.MenuBar menuBar) throws java.awt.HeadlessException {
        logger.info("createMenuBar");
        if (lg3dOnly) throw new RuntimeException("Not Implemented");
        return super.createMenuBar(menuBar);
    }

    public java.awt.peer.MenuItemPeer createMenuItem(java.awt.MenuItem menuItem) throws java.awt.HeadlessException {
        logger.info("createMenuItem");
        if (lg3dOnly) throw new RuntimeException("Not Implemented");
        return super.createMenuItem(menuItem);
    }

    public java.awt.peer.PanelPeer createPanel(java.awt.Panel panel) {
        logger.info("createPanel");
        if (lg3dOnly) throw new RuntimeException("Not Implemented");
        return super.createPanel(panel);
    }

    public java.awt.peer.ScrollPanePeer createScrollPane(java.awt.ScrollPane scrollPane) throws java.awt.HeadlessException {
        logger.info("createScrollPane");
        if (lg3dOnly) throw new RuntimeException("Not Implemented");
        return super.createScrollPane(scrollPane);
    }

    public java.awt.peer.ScrollbarPeer createScrollbar(java.awt.Scrollbar scrollbar) throws java.awt.HeadlessException {
        logger.info("createScrollbar");
        if (lg3dOnly) throw new RuntimeException("Not Implemented");
        return super.createScrollbar(scrollbar);
    }

    public java.awt.peer.TextAreaPeer createTextArea(java.awt.TextArea textArea) throws java.awt.HeadlessException {
        logger.info("createTextArea");
        if (lg3dOnly) throw new RuntimeException("Not Implemented");
        return super.createTextArea(textArea);
    }

    public java.awt.peer.TextFieldPeer createTextField(java.awt.TextField textField) throws java.awt.HeadlessException {
        logger.info("createTextField");
        if (lg3dOnly) throw new RuntimeException("Not Implemented");
        return super.createTextField(textField);
    }

    public java.util.Map<java.awt.font.TextAttribute, Object> mapInputMethodHighlight(java.awt.im.InputMethodHighlight inputMethodHighlight) {
        logger.info("mapInputMethodHighlight");
        if (lg3dOnly) throw new RuntimeException("Not Implemented");
        return (java.util.Map<java.awt.font.TextAttribute, Object>)super.mapInputMethodHighlight(inputMethodHighlight);
    }
}
