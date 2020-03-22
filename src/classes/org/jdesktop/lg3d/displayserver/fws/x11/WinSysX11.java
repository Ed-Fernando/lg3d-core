/**
 * Project Looking Glass
 *
 * $RCSfile: WinSysX11.java,v $
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
 * $Revision: 1.32 $
 * $Date: 2007-04-12 23:36:19 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.displayserver.fws.x11;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.AWTEvent;
import java.awt.event.*;
import java.io.InputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import org.jdesktop.lg3d.displayserver.SevereRuntimeError;
import sun.awt.*;
import org.jdesktop.lg3d.displayserver.fws.*;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.displayserver.nativewindow.TiledNativeWindowImage;
import java.util.logging.Level;
import gnu.x11.extension.NotFoundException;
import gnu.x11.extension.Shape;
import gnu.x11.Atom;
import gnu.x11.Display;
import gnu.x11.Drawable;
import gnu.x11.Enum;
import gnu.x11.Window;
import gnu.x11.Pixmap;
import gnu.x11.Colormap;
import gnu.x11.Color;
import gnu.x11.Cursor;
import gnu.x11.GC;
import gnu.x11.event.Event;
import gnu.x11.Input;
import gnu.x11.Rectangle;
import gnu.x11.event.MotionNotify;
import gnu.x11.image.Image;
import gnu.x11.image.Bitmap;
import org.jdesktop.lg3d.scenemanager.CursorModule;
import org.jdesktop.lg3d.displayserver.EventProcessor;
import javax.media.j3d.BranchGroup;
import org.jdesktop.lg3d.wg.Cursor3D;
import org.jdesktop.lg3d.displayserver.ErrorDialog;
import org.jdesktop.lg3d.displayserver.fws.x11.X11CompositeExt;


/**
 * WinSysX11 provides the implementation of the interface between
 * the Display Server and an underlying X11 window system.
 *
 * Note: it is important that you create an instance of this
 * class prior to initializing any other package which uses
 * Xlib, such as AWT.
 */

public class WinSysX11 extends FoundationWinSys {

    // The execution modes of WinSysX11
    static final int MODE_SESSION = 0;
    static final int MODE_APP     = 1;

    // The DamageExtension BadDamage error code
    private static final int BAD_DAMAGE = 0;

    // The X11 window system singleton
    private static WinSysX11 fws = null;

    private ExtensionSet exts;

    // Used by the CookedEventPoller to poll for high-level events
    private EscherApplication cepApplication;
    private Display cepDpy;

    // Used for initialization and to poll for device input events
    private EscherApplication devApplication;
    private Display devDpy;

    // Used by the DamageEventBroker and DamageEventPoller
    private EscherApplication damApplication;
    private Display damDpy;

    // Used by the CursorImageEventBroker
    private EscherApplication ciebApplication;
    private Display ciebDpy;
    
    private Window[] prwins;
    private long[] rootWins;
    private Window[] rootWindows;
    private long[] cowWids;
    private Window[] cowWins;
    private Cursor[] arrowCursors;

    private EventProcessor eventProcessor;
    private BranchGroup mgmtBG;
    private PickEngineX11 pickEngine;
    private LowLevelX11Picker llxp;

    // Event Brokers
    private DamageEventBroker ebDamage;    
    private DeviceEventSource deviceEventSource;    

    // Event Pollers
    private DamageEventPoller epDamage;
    private CookedEventPoller epCooked;

    Atom pickSeqAtom;

    private int executionMode;

    private HashMap<Long, Canvas3D> canvasMap = new HashMap<Long, Canvas3D>();

    /*
    ** Fix 518:
    ** This is a simulacrum of GNOME's default arrow cursor. We must
    ** define this cursor for all root windows in order that apps
    ** which don't define their own window cursors will inherit a
    ** reasonable cursor.
    */

    private static final int ARROW_CURSOR_WIDTH  = 10;
    private static final int ARROW_CURSOR_HEIGHT = 16;
    private static final int ARROW_CURSOR_XHOT   =  1;
    private static final int ARROW_CURSOR_YHOT   =  1;

    private static int[] arrowSourceBits = new int[] {
	0x0000,	0x0002,	0x0006, 0x000e, 0x001e, 0x003e, 0x007e, 0x00fe,
	0x01fe, 0x003e, 0x0036, 0x0062, 0x0060, 0x00c0, 0x00c0, 0x0000 };

    private static int arrowMaskBits[] = new int[] {
	0x0003, 0x0007, 0x000f, 0x001f, 0x003f, 0x007f, 0x00ff, 0x01ff,
	0x03ff, 0x03ff, 0x007f, 0x00f7, 0x00f3, 0x01e0, 0x01e0, 0x00c0 };

    private static boolean inWonderland = false;
    private static String launchLgXserverStr;

    static {
	// This library provides the recommended way of getting
	// the X11 window ID of a Canvas3D. This is the technique
	// that Java3D uses.
        String dsoFile = System.getProperty("lg.libraries.dso", "");
        if (!"".equals(dsoFile)) {
            System.load(dsoFile);
        } else {
            System.loadLibrary("dso");
        }

        String wonderlandStr = System.getProperty("wl.fws.x11.wonderland", "false");
	inWonderland = "true".equals(wonderlandStr);

        launchLgXserverStr = System.getProperty("wl.fws.x11.launchLgXserver", "false");
	
    }

    public WinSysX11(Canvas3D[] canvases, Locale locale, ViewInfo viewInfo,
		     BranchGroup mgmtBG, EventProcessor eventProcessor) {
        if (winSys!=null)
            throw new RuntimeException("WinSys should only be instantiated once");
        
        winSys = this;

	if ("true".equals(launchLgXserverStr)) {
	    System.err.println("Launching LG X server");
	    LgXserver xs = new LgXserver();
	    xs.launch();
	}

        this.viewInfo = viewInfo;
        this.canvases = canvases;
        this.locale = locale;
	this.mgmtBG = mgmtBG;
        this.eventProcessor = eventProcessor;

	String fwsMode = System.getProperty("lg.fws.mode");
	if (fwsMode == null) fwsMode = "session";

	if (fwsMode.compareTo("session") == 0) {
	    executionMode = MODE_SESSION;
	    logger.info("WinSysX11 execution mode = Session");
	} else if (fwsMode.compareTo("app") == 0) {
	    executionMode = MODE_APP;
	    logger.info("WinSysX11 execution mode = App");
        } else {
	    throw new RuntimeException("WinSysX11: Invalid execution mode: " + fwsMode);
	}

	initX11Connections();

        initEventThreads(mgmtBG);

	// create a new picking engine
        pickEngine = new PickEngineX11(locale, eventProcessor, rootWins, this);

	// add the pick engine to the management branch group
        mgmtBG.addChild( pickEngine );

        // Register all of the screen's we will be managing.
	// Note: we must do an Xsync before proceeding, to make
	// sure that the X server is ready to operate in LG3D mode.
	for (int i=0; i < prwins.length; i++) {

	    // Inform the X server which windows are PRWs.
	    exts.lgeExtForDev.registerScreen(prwins[i]);	

	    // Hide the cursors of all X11 windows
	    /*TODO: can't do this for wonderland
	    exts.xfixesExtForDev.hideCursor(rootWindows[i]);
	    exts.xfixesExtForDev.hideCursor(prwins[i]);
	    */

	    // Reparent the PRW into the composite overlay window and raise it
	    prwins[i].reparent(cowWins[i], 0, 0);

	    // Composite-redirect the output of all top-level children
	    // of the root window.
	    //
	    // Note: Because of the way AWT and Java3D initialize
	    // the PRW, the PRW is a *GRANDCHILD* of the root 
	    // window so it is not subject to composite redirection.

	    exts.compExtForDev.redirectSubwindows(rootWindows[i], X11CompositeExt.MANUAL);
	}
	devDpy.check_error(); // Xsync 

        try {
    	    epCooked = new CookedEventPoller(cepDpy, exts, pickEngine, prws, mgmtBG, this);
	} catch (InstantiationException e) {
    	    prn("Exception occurred: " + e);
	    System.exit(1);
	}
	SequencedMouseEvent.setWsx(this);
	SequencedMouseWheelEvent.setWsx(this);
	SequencedKeyEvent.setWsx(this);

        llxp = new LowLevelX11Picker(this, devDpy, deviceEventSource, exts, canvases, prws);

	// Added for behavior mode
        mgmtBG.addChild(llxp);

	// The DS X11 initialization is complete. Put the XS into LG Mode */
	exts.lgeExtForDev.controlLgMode(true);
	devDpy.check_error(); // Xsync
    }

    private void ensureRequiredExtensionsArePresent (Display dpy) {
        boolean glxPresent = false;
	boolean compPresent = false;
	boolean lgePresent = false;
        boolean nvidiaGlx = false;

	Enum extensions = devDpy.extensions();
	while (extensions.more()) {
	    String extName = extensions.next_string();
	    if (extName.compareTo("GLX") == 0) {
    	        glxPresent = true;
	    } else if (extName.compareTo("NV-GLX") == 0) {
    	        glxPresent = true;
		nvidiaGlx = true;
	    } else if (extName.compareTo("Composite") == 0) {
	        compPresent = true;
	    } else if (extName.compareTo("LGE") == 0) {
	        lgePresent = true;
	    }
	}

	if (executionMode == MODE_SESSION) {
	    if (glxPresent && compPresent && lgePresent) {
		return;
	    }
	} else {
	    // Note: Solaris x86 Xvfb doesn't export GLX. We don't
	    // need it for app mode, so don't require it
	    if (compPresent && lgePresent) {
		return;
	    }
	}

        if (!lgePresent) {
            throw new SevereRuntimeError("Internal error: The X server is not exporting the LGE extension");
	}	

	if (!glxPresent) {
            throw new SevereRuntimeError("X configuration error: OpenGL is not installed on the system");
	}

        if (glxPresent && !compPresent && nvidiaGlx) {
            throw new SevereRuntimeError("X configuration error: The X server is not exporting the Composite extension. You must enable the AllowGlxWithComposite option in the Nvidia device section of the xorg.conf file. See Looking Glass configuration instructions for details.");
	} else {
            throw new SevereRuntimeError("X configuration error: The X server is not exporting the Composite extension. Please consult the configuration instructions for your graphics card.");
	}
    }

    private void initX11Connections () {

        String displayName = System.getProperty("lg.lgserverdisplay");
	if (displayName == null) {
	    displayName = ":0";
	}
	logger.info("Connecting to LG X Server " + displayName);

        try {
            devApplication = new EscherApplication(new String[] {"--display", displayName}, "DevicePoller");
        } catch (Error err) {
            prn("Failed to connect with LG3D X server.");
            prn("Please check the log file (/var/log/Xorg." 
                    + displayName.substring(1) + ".log) for any X server issue.");
	    System.exit(1);
        }    
	devDpy = devApplication.getDisplay();
	ignoreWinDrawErrors(devDpy);

	ensureRequiredExtensionsArePresent(devDpy);

	cepApplication = new EscherApplication(new String[] {"--display", displayName}, "CookedEventPoller");
	cepDpy = cepApplication.getDisplay();
	ignoreWinDrawErrors(cepDpy);

	damApplication = new EscherApplication(new String[] {"--display", displayName}, "DamageEventBroker");
	damDpy = damApplication.getDisplay();
	ignoreWinDrawErrors(damDpy);

	ciebApplication = new EscherApplication(new String[] {"--display", displayName}, "CursorImageEventBroker");
	ciebDpy = ciebApplication.getDisplay();
	ignoreWinDrawErrors(ciebDpy);

	try {
	    exts = new ExtensionSet(cepDpy, devDpy, damDpy, ciebDpy);
	} catch (NotFoundException nfe) {
            throw new SevereRuntimeError("Cannot initialize X11 extensions");
	}

        ignoreBadDamageError(damDpy, exts);

	// Register all clients with the X server. This will make it so that if
	// the connection is broken for any of these clients the X server will
	// assume that the DS is dead and will clean up accordingly.
	//
	// Also, identify to the X server that CEP is the Event Deliverer client.
	//
	// Also, make all WinSysX11 Escher displays impervious to server grabs.

	exts.lgeExtForCep.registerClient(X11LgeExt.CLIENT_EVENT_DELIVERER);
        exts.xtestExtForCep.grabControl(true);
	cepDpy.check_error();

	// Fix for 615:
        //
        // Session mode reinjects events in the XS directly, bypassing queue freezing.
        // This is because the events have already been subject to queue freezing
	// before they were sent to the picker.
	//
	// App mode, on the other hand, reinjects events using the device's ProcessInputProc 
        // function, so the reinjected events may be subject to queue freezing.
	exts.lgeExtForDev.registerClient(X11LgeExt.CLIENT_PICKER, (executionMode == MODE_SESSION));

        exts.xtestExtForDev.grabControl(true);
	devDpy.check_error();
	exts.lgeExtForDam.registerClient(X11LgeExt.CLIENT_GENERIC);
        exts.xtestExtForDam.grabControl(true);
	damDpy.check_error();
	exts.lgeExtForCieb.registerClient(X11LgeExt.CLIENT_GENERIC);
        exts.xtestExtForCieb.grabControl(true);
	ciebDpy.check_error();
	
        // Make sure that the depth is 24
	// TODO: eventually we need to support depth 16 as well
	// (and possibly other depths).
	defaultVisualDepth = cepDpy.default_depth;
	//prn("defaultVisualDepth = " + defaultVisualDepth);
	if ((defaultVisualDepth != 24) && (defaultVisualDepth != 16)) {
            throw new SevereRuntimeError("You cannot run lg3d-session because the color depth of your graphics device is " + defaultVisualDepth
                                     +"Only 16 and 24 bits are supported."
                                     +"Consult the lg3d-core Getting Started documentation on how to configure your graphics"
                                     +"device color depth to be 16/24 bits.");
	}
	
	Shape shape = null;
	try {
	    shape = new Shape(devDpy);
	} catch (NotFoundException e) {
	    throw new SevereRuntimeError("X server could not load Shape Extension. Maybe" 
		    + "libextmod.so is loaded incorrectly. See the Xorg log file.");
	}

	if (executionMode == MODE_SESSION) {
	    prws = new long[canvases.length];		
	    for (int i=0; i < canvases.length; i++) {
		prws[i] = getCanvasWindowID(canvases[i]);		
	    }

	    prwins = new Window[canvases.length];	
	    rootWins = new long[canvases.length];
	    rootWindows = new Window[canvases.length];
	    cowWids = new long[canvases.length];
	    cowWins = new Window[canvases.length];
	    if (!inWonderland) {
		arrowCursors = new Cursor[canvases.length];
	    }

	    for (int i = 0; i < canvases.length; i++) {
		Window w = new Window(devDpy, (int) prws[i]);
		Window.TreeReply repl = w.tree();
		rootWins[i] = repl.root_id();
		rootWindows[i] = repl.root();
		prwins[i] = new Window((int) prws[i]);
		prwins[i].display = devDpy;
		cowWids[i] = exts.compExtForDev
			.getOverlayWindow(rootWindows[i]);
		cowWins[i] = new Window(devDpy, (int) cowWids[i]);

		Rectangle rects[] = new Rectangle[1];
		Rectangle rect = new Rectangle(0, 0, 0, 0);
		rects[0] = rect;
		shape.combine_rectangles(cowWins[i], Shape.INPUT, 0, 0, rects, Shape.SET,
					 Shape.UN_SORTED);
		canvasMap.put(prws[i], canvases[i]);
	    }	
	    devDpy.check_error();
	}

	if (executionMode == MODE_APP) {
	    rootWins = new long[1];
	    rootWindows = new Window[1];
	    rootWins[0] = devDpy.default_root.id;
	    rootWindows[0] = new Window(devDpy, devDpy.default_root.id);
	    int canvasWidth = canvases[0].getWidth();
	    int canvasHeight = canvases[0].getHeight();
	    Rectangle rect = new Rectangle(0, 0, canvasWidth, canvasHeight);
	    prwins = new Window[1];
	    Window.Attributes attrs = new Window.Attributes();
	    	    
	    prwins[0] = new Window(devDpy.default_root, rect, 0, attrs);
	    prws = new long[1];
	    prws[0] = prwins[0].id;
	    cowWids = new long[1];
	    cowWins = new Window[1];
	    cowWids[0] = exts.compExtForDev.getOverlayWindow(prwins[0]);
	    cowWins[0] = new Window(devDpy, (int) cowWids[0]);
	    Rectangle rects[] = new Rectangle[1];
	    Rectangle rect1 = new Rectangle(0, 0, 0, 0);
	    rects[0] = rect1;
	    shape.combine_rectangles(cowWins[0], 2, 0, 0, rects, Shape.SET,
				     Shape.UN_SORTED);	        
	    prwins[0].map();
	    canvasMap.put(prws[0], canvases[0]);		
	    devDpy.check_error();

	    if (!inWonderland) {
		arrowCursors = new Cursor[1];
	    }
	}

	pickSeqAtom = (Atom) Atom.intern(devDpy, "PickSequenceAtom", false);

	if (!inWonderland) {
	    createArrowCursors();
	}
    }

    // TODO: windows sometimes appear and disappear so fast that 
    // LG ends up sending some requests after a window has been 
    // destroyed. As a simple work around the LG Display Server
    // is simply going to tell Escher to ignore troublesome requests.
    // But we should eventually see if there is a better way to work around.
    private void ignoreWinDrawErrors (Display dpy) {
	dpy.ignore_error(gnu.x11.Error.BAD_WINDOW);
	dpy.ignore_error(gnu.x11.Error.BAD_DRAWABLE);
    }

    // TODO: see comment for previous method. BadDamage needs to be
    // ignored as well
    private void ignoreBadDamageError (Display damDpy, ExtensionSet exts) {
	int damageEventErrorCode = exts.damageExtForDam.first_error + BAD_DAMAGE;
	damDpy.ignore_error(damageEventErrorCode);
    }


    private Color getDefaultScreenBlackColor (Display dpy) {
	Colormap defCmap = dpy.default_colormap;
	return defCmap.alloc_color(0, 0, 0);
    }

    private void initEventThreads (BranchGroup mgmtBG) {

	ebDamage = new DamageEventBroker(damDpy, exts); 
        ebDamage.start();

	if (executionMode == MODE_SESSION) {
	    deviceEventSource = new DeviceEventSourceSession(devDpy, prwins, canvases); 
	} else {
	    deviceEventSource = new DeviceEventSourceApp(devDpy, cepDpy, prwins, canvases); 
	}

        try {
    	    epDamage = new DamageEventPoller(damDpy, ebDamage, exts, prws);
	    ebDamage.setDamageNotifyEventCode(epDamage.getDamageNotifyEventCode());
	} catch (InstantiationException e) {
    	    prn("Exception occurred: " + e);
	    System.exit(1);
	}
    }

    private static final int ERR_BUF_LEN = 64;

    // Given a Canvas3D, returns its X11 window ID 
    // This uses the recommended way of getting the X11 window ID 
    // of a Canvas3D. This is the technique that Java3D uses.

    private long getCanvasWindowID (Canvas3D canvas) {
	org.jdesktop.lg3d.displayserver.fws.x11.dso.DrawingSurfaceObjectAWT dsawt = 
		new org.jdesktop.lg3d.displayserver.fws.x11.dso.DrawingSurfaceObjectAWT();
	long jawtObj = dsawt.getAWT();
	long dsObj = dsawt.getDrawingSurfaceAWT(canvas, jawtObj);
	long dsiObj = dsawt.getDrawingSurfaceInfo(dsObj);
	int wid = dsawt.getDrawingSurfaceWindowIdAWT(canvas, dsObj, dsiObj,
						     0, 0, false);
	return wid;
    }

    public int getScreenWidth() { return canvases[0].getWidth(); }
    public int getScreenHeight() { return canvases[0].getHeight(); }

    public long getRootWid (int i) {
	return rootWins[i];
    }

    public Canvas3D getCanvas(int i) {
	return canvases[i]; 
    }

    public int getNumCanvases() {
	return canvases.length;
    }

    public long getCanvasWid(int i) { 
	if (i >= prws.length) return 0;
	return prws[i]; 
    }

    public long getCanvasWinForRootWin(long win) { 
        for (int i=0; i < prws.length; i++) {
            if (rootWins[i] == win) {
                return prws[i];
            }
	}		
        return 0;
    }

    public Canvas3D getCanvasForRootWin(long wid) { 
        for (int i=0; i < prws.length; i++) {
            if (rootWins[i] == wid) {
                return canvases[i];
            }
	}		
        return null;
    }


    public Canvas3D getCanvasForRootWin (long rootWid, long prwwin) {
	long prw = getCanvasWinForRootWin(rootWid);
	Canvas3D canvas = canvasMap.get(prw);

	// TODO: DJ: I'm wondering if this is really necessary. For what
	// types of events does this get called?
	if (canvas == null) {
	    // This can happen when a window on a non-LG screen has grabbed
	    // an input device.
	    canvas = getWinCanvas(prw);
	    if (canvas == null) {
	    	throw new RuntimeException("Cannot find canvas for root window " + rootWid);
	    }
	    return canvas;
	}

	return canvas;
    }

    public void addCursorImageListener (CursorImageListener l) {
	epCooked.getCursorImageEventBroker().addListener(l);
    }
	
    public void removeCursorImageListener(CursorImageListener l) {
	epCooked.getCursorImageEventBroker().removeListener(l);
    }

    public CursorImageListener[] getCursorImageListeners() {
        return epCooked.getCursorImageEventBroker().getListeners();
    }

    public void addWindowResizeListener (long wid, WindowResizeListener l) {
	ebDamage.addWindowResizeListener(wid, l);
    }
	
    public void removeWindowResizeListener (long wid, WindowResizeListener l) {
	ebDamage.removeWindowResizeListener(wid, l);
    }

    public WindowResizeListener getWindowResizeListener (long wid) {
	return ebDamage.getWindowResizeListener(wid);
    }

    public void trackEventsForNativeWindow (long wid) {
	Window cepWin = new Window((int)wid);
	cepWin.display = cepDpy;

        // Register interest in enter/exit events. This allows WSX to know when
	// the pointer enters or exits the "native region." The native region is the
        // union of the viewable shapes of all X11 windows.
	//
	// Also, register interest in button events. This allows the user to click 
        // a button on a native window and to have the button event be delivered to 
        // both interested X11 clients and 3D event listeners. For example, the scene 
	// manager will listen for left button press events on X11 windows and will 
        // bring a window to the top of the stack if this happens.

	cepWin.select_input(Event.ENTER_WINDOW_MASK | Event.LEAVE_WINDOW_MASK);
	cepDpy.check_error();

        pickEngine.trackNativeWindow(wid, cepWin);
    }

    public void untrackEventsForNativeWindow (long wid) {
        pickEngine.untrackNativeWindow(wid);
    }

    public void damageWindowRectangle (long wid, int x, int y, int w, int h) {
	ebDamage.damageWindowRectangle(wid, x, y, w, h);
    }

    public void trackDamageForNativeWindow (long wid, TiledNativeWindowImage image) 
    {
	ebDamage.trackDamageForWindow(wid, image);
    }

    public void destroyNativeWindow (long wid) {
	ebDamage.destroyWindow(wid);
        pickEngine.untrackNativeWindow(wid);
	pickEngine.destroyWindow(wid);
    }

    public void setCursorModule (CursorModule cursorModule) {
	pickEngine.setCursorModule(cursorModule);
	llxp.setCursorModule(cursorModule);
    }

    public CursorModule getCursorModule () {
	return pickEngine.getCursorModule();
    }

    public void grabPointer (Node grabNode, Cursor3D cursor) {
	logger.fine("Enter WSX.grabPointer, grabNode = " + grabNode);

	GrabProxyWin gpw = new GrabProxyWin(cepDpy, grabNode, true);

	int status = gpw.grab_pointer(false, ~0, Input.ASYNC_POINTER,
				      Input.ASYNC_KEYBOARD, Window.NONE, Cursor.NONE, 
				      devDpy.CURRENT_TIME);
	if (status != Window.SUCCESS) {
	    switch (status) {
	    case Window.ALREADY_GRABBED:
		throw new RuntimeException("X GrabPointer error: already grabbed");
	    case Window.INVALID_TIME:
		throw new RuntimeException("X GrabPointer internal error: invalid time");
	    case Window.NOT_VIEWABLE:
		throw new RuntimeException("X GrabPointer internal error: not viewable");
	    case Window.FROZEN:
		throw new RuntimeException("X GrabPointer internal error: frozen");
	    default:
		throw new RuntimeException("X GrabPointer internal error: unrecognized error");
	    }
	}
    }

    public void ungrabPointer () {
// TODO: what cleans up the gpw that was created for grabPointer?
	logger.fine("Enter WSX.ungrabPointer");
	devDpy.input.ungrab_pointer(devDpy.CURRENT_TIME);
    }

    public void grabKeyboard (Node grabNode) {
	logger.fine("Enter WSX.grabKeyboard, grabNode = " + grabNode);

	GrabProxyWin gpw = new GrabProxyWin(cepDpy, grabNode, true);

	int status = gpw.grab_keyboard(false, Input.ASYNC_POINTER, Input.ASYNC_KEYBOARD, 
				       devDpy.CURRENT_TIME);
	if (status != Window.SUCCESS) {
	    switch (status) {
	    case Window.ALREADY_GRABBED:
		throw new RuntimeException("X GrabKeyboard error: already grabbed");
	    case Window.INVALID_TIME:
		throw new RuntimeException("X GrabKeyboard internal error: invalid time");
	    case Window.NOT_VIEWABLE:
		throw new RuntimeException("X GrabKeyboard internal error: not viewable");
	    case Window.FROZEN:
		throw new RuntimeException("X GrabKeyboard internal error: frozen");
	    default:
		throw new RuntimeException("X GrabKeyboard internal error: unrecognized error");
	    }
	}
    }

    public void ungrabKeyboard () {
// TODO: what cleans up the gpw that was created for grabPointer?
	logger.fine("Enter WSX.ungrabKeyboard");
	devDpy.input.ungrab_keyboard(devDpy.CURRENT_TIME);
    }

    public void grabButton (int buttonNum, long modifiers, Node grabNode, 
	                       Cursor3D cursor) {
	logger.fine("Enter WSX.grabButton, buttonNum = " + buttonNum + ", modifiers = " + 
		    modifiers + ", grabNode = " + grabNode);
	// TODO: define a passive button grab on grabNode
    }

    public void ungrabButton (int buttonNum, long modifiers, Node grabNode) {
	logger.fine("Enter WSX.ungrabButton");
	// TODO: remove a passive button grab on grabNode
    }

    public void grabKey (int keySym, long modifiers, Node grabNode) {
	logger.fine("Enter WSX.grabKey, keySym = " + keySym + ", modifiers = " + 
		    modifiers + ", grabNode = " + grabNode);
        int keyCode = devDpy.input.keysym_to_keycode(keySym);	    
	// TODO: define a passive key grab on grabNode
    }

    public void ungrabKey (int keySym, long modifiers, Node grabNode) {
	logger.fine("Enter WSX.ungrabKey");
        int keyCode = devDpy.input.keysym_to_keycode(keySym);	    
	// TODO: remove a passive key grab on grabNode
    }

    // Constructs a synthetic motion event with coordinates of the last
    // pointer position and injects it into the pipeline going to the Picker.
    // This forces a recomputation in the X Server of the current sprite window
    // (for delivery of keyboard events) and also forces a recomputation in the
    // Event Deliverer of the current pointer object. It also causes the necessary
    // 3D enter/exit events to be sent.

    public void enqueueMoveToLastPosition () {
	deviceEventSource.enqueueMoveToLastPosition();
    }

    private Pixmap createPixmapFromBitmapData (Display dpy, Window w, int[] data, 
					       int width, int height, 
					       int fg, int bg, int depth) {

	Pixmap pix = new Pixmap(w, width, height, depth);

	GC.Values gcv = new GC.Values();
	gcv.set_foreground(fg);
	gcv.set_background(bg);
	GC gc = new GC(pix, gcv);

	Image image = new Bitmap(dpy, width, height);
	int nextByte = 0;
	for (int i = 0; i < data.length; i++) {
	    int word = data[i];
	    image.data[nextByte++] = (byte)((word >> 24) & 0xff);
	    image.data[nextByte++] = (byte)((word >> 16) & 0xff);
	    image.data[nextByte++] = (byte)((word >>  8) & 0xff);
	    image.data[nextByte++] = (byte)(word & 0xff);
	}

	pix.put_small_image(gc, image, 0, height, 0, 0);

	return pix;
    }

    private Cursor createArrowCursor (Display dpy, Window rootwin) {

	int white = dpy.default_screen.white_pixel();
	int black = dpy.default_screen.black_pixel();

	Pixmap sourceBitmap, maskBitmap;

	sourceBitmap = createPixmapFromBitmapData(dpy, rootwin, arrowSourceBits, 
				ARROW_CURSOR_WIDTH, ARROW_CURSOR_HEIGHT, 
				white, black, 1);

	maskBitmap   = createPixmapFromBitmapData(dpy, rootwin, arrowMaskBits, 	
			ARROW_CURSOR_WIDTH, ARROW_CURSOR_HEIGHT,
				white, black, 1);

	Cursor cursor = new Cursor(sourceBitmap, maskBitmap, 0, 0,
				0, 0, 0, Short.MAX_VALUE, Short.MAX_VALUE, Short.MAX_VALUE,
				ARROW_CURSOR_XHOT, ARROW_CURSOR_YHOT);

	return cursor;
    }

    public void createArrowCursors () {

	// Assign the arrow cursor to all PRWs
	Window.Attributes winAttrs = new Window.Attributes();
	for (int i=0; i < prwins.length; i++) {
	    arrowCursors[i] = createArrowCursor(devDpy, rootWindows[i]);
	    winAttrs.set_cursor(arrowCursors[i]);
	    rootWindows[i].change_attributes(winAttrs);
	}
	devDpy.check_error();
    }

    public void compositeTopLevelWindows (Object fwsInstance) {
	Display dpy = (Display) fwsInstance;

	X11CompositeExt compExt = null;
	try {
	    compExt = new X11CompositeExt(dpy);
	} catch (Exception ex) {
	    System.err.println("Failed to access composite extension");
	    throw new RuntimeException("Failed to access composite extension");
	}

	compExt.redirectSubwindows(dpy.default_root, X11CompositeExt.MANUAL);
	dpy.check_error(); // Xsync 
    }

    private static void prn (String str) { System.err.println(str); }
}
