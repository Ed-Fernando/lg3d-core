/**
 * Project Looking Glass
 *
 * $RCSfile: NativeWindow3D.java,v $
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
 * $Revision: 1.33 $
 * $Date: 2007-04-10 22:58:42 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.nativewindow;

import java.util.logging.Logger;
import javax.media.j3d.Canvas3D;
import javax.vecmath.Point2d;
import javax.vecmath.Vector3f;
import javax.vecmath.Point3d;
import gnu.x11.Visual;

import org.jdesktop.lg3d.displayserver.PlatformConfig;
import org.jdesktop.lg3d.displayserver.fws.FoundationWinSys;
import org.jdesktop.lg3d.displayserver.fws.WindowResizeListener;
import org.jdesktop.lg3d.scenemanager.utils.event.ScreenResolutionChangedEvent;
import org.jdesktop.lg3d.sg.ImageComponent2D;
import org.jdesktop.lg3d.utils.action.ActionBoolean;
import org.jdesktop.lg3d.utils.action.ActionComponent3D;
import org.jdesktop.lg3d.utils.action.ActionNoArg;
import org.jdesktop.lg3d.utils.eventadapter.Component3DManualMoveEventAdapter;
import org.jdesktop.lg3d.utils.eventadapter.Component3DManualResizeEventAdapter;
import org.jdesktop.lg3d.utils.eventadapter.Component3DToFrontEventAdapter;
import org.jdesktop.lg3d.utils.eventadapter.Component3DToBackEventAdapter;
import org.jdesktop.lg3d.utils.eventadapter.GenericEventAdapter;
import org.jdesktop.lg3d.utils.shape.SimpleAppearance;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Frame3D;
import org.jdesktop.lg3d.wg.Cursor3D;
import org.jdesktop.lg3d.wg.Toolkit3D;
import org.jdesktop.lg3d.wg.event.Component3DToFrontEvent;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventConnector;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.MouseEnteredEvent3D;
import org.jdesktop.lg3d.wg.event.MouseMotionEvent3D;
import org.jdesktop.lg3d.wg.event.MouseButtonEvent3D;
import org.jdesktop.lg3d.wg.event.MouseWheelEvent3D;


/**
 * NativeWindow3D provides the anchor for rendering Native window
 * system windows.
 */
public class NativeWindow3D extends Frame3D 
    implements WindowResizeListener, NativeWindowMonitor
{
    private static final Logger logger = Logger.getLogger("lg.displayserver");
    protected static Class lookAndFeelClass;
    protected static final int DEPTH16 = 16;
    protected static final int DEPTH24 = 24;
    protected static int CHANGE_TRANSLATION_NO_NATIVE = -1;
    
    private FoundationWinSys fws = FoundationWinSys.getFoundationWinSys();
    protected TiledNativeWindowImage mainWindowImage = null;
    protected NativeWindowLookAndFeel lookAndFeel;
    protected NativeWindowControl nativeWinContl;
    private Canvas3D canvas = null;
    private boolean trackDamageEnabled = false;
    protected boolean decorated;
    protected WindowShape shape;
    protected int depth;
    protected Visual visual = null;
    private boolean moveToInitLoc = true;
    private int minIncWidth ;
    private int minIncHeight;
    private int minWidth ;
    private int minHeight ;    
    private int baseWidth ;
    private int baseHeight ;
    private float maxWidth ;
    private float maxHeight ;    
    private float deltaX = 0.0f;
    private float maxX = 0.0f;
    private float maxY = 0.0f;
    private float minX = 0.0f;
    private float minY = 0.0f;
    private Point3d center3d = new Point3d();
    private Vector3f tmpV3f  = new Vector3f();
    private Point3d tmpP3d = new Point3d();
    private Point2d tmpP2d = new Point2d();
    private javax.media.j3d.Transform3D tmpT3d = new javax.media.j3d.Transform3D();
    protected boolean startMove = false;
    protected boolean startResize = false;
    private float nativeWidth;
    private float nativeHeight;
    protected boolean isRemwin;
    private Cursor3D cursor;
    
    private LgEventListener resolutionListener;
    
    static {
        String lfClassName = PlatformConfig.getConfig().getNativeWinLookAndFeel();
        try {
            lookAndFeelClass = Class.forName(lfClassName);
            logger.info("Native Window Look and Feel class found: " 
                + lookAndFeelClass);
        } catch (Exception e) {
            logger.severe(
                "Failed to initialize Look and Feel class: " + lfClassName);
        }
        
	/* DJ: TODO: remove for now
        LgEventConnector.getLgEventConnector().addListener(NativeWindow3D.class,
            new Component3DToFrontEventAdapter(
                new ActionComponent3D() {
                    public void performAction(LgEventSource source, Component3D sibling) {
                        if (sibling == null) {
                            // perform raise only when a "strong"
                            // to-front event is issued.
                            NativeWindow3D window = (NativeWindow3D)source;
                            window.movedToTop();
                        }
                    }
                }));
	*/
        
	/* DJ: TODO: remove for now
        LgEventConnector.getLgEventConnector().addListener(NativeWindow3D.class,
            new Component3DToBackEventAdapter(
                new ActionComponent3D() {
                    public void performAction(LgEventSource source, Component3D sibling) {
                        if (sibling == null) {
                            // perform raise only when a "strong"
                            // to-front event is issued.
                            NativeWindow3D window = (NativeWindow3D)source;
                            window.movedToBottom();
                        }
                    }
                }));
	*/
        
	/* DJ: TODO: remove for now
        LgEventConnector.getLgEventConnector().addListener(NativeWindow3D.class, 
            new Component3DManualMoveEventAdapter(
        	new ActionBoolean() {        	    
                    public void performAction(LgEventSource source, boolean start) {
                        if (start) {
                            NativeWindow3D window  = (NativeWindow3D)source;
                            window.setStartMove(true);
                        } else {
                            NativeWindow3D window  = (NativeWindow3D)source;
                            window.setStartMove(false);
                            Vector3f tmpV3f = new Vector3f();
                            window.getFinalTranslation(tmpV3f);
                            window.changeNativeLocation(tmpV3f.x, tmpV3f.y);
                        }
                    }
                }
            ));
	*/
        
	/* DJ: TODO: remove for now
        LgEventConnector.getLgEventConnector().addListener(NativeWindow3D.class, 
                new Component3DManualResizeEventAdapter(
            	new ActionBoolean() {        	    
                        public void performAction(LgEventSource source, boolean start) {
                            if (start) {
                                NativeWindow3D window  = (NativeWindow3D)source;
                                window.setStartResize(true);
                            } else {                        	
                                NativeWindow3D window  = (NativeWindow3D)source;
                                window.setStartResize(false);
                                Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
                                window.nativeWinContl.setSize(
                                    toolkit3d.widthPhysicalToNative(window.nativeWidth),
                                    toolkit3d.heightPhysicalToNative(window.nativeHeight));
                            }
                        }
                    }
                ));
	*/
    }
    
    public NativeWindow3D(NativeWindowControl nativeWinContl, 
			  boolean decorated, int depth, Visual visual) 
    {
	this(nativeWinContl, decorated, depth, visual, false);
    }

    /**
     * Creates a native window, the presence of decorations can be
     * controlled via the decorated parameter.
     *
     * @param decorated indicates if the NativeWindow3D should
     * have any decorations
     */
    public NativeWindow3D(NativeWindowControl nativeWinContl, 
			  boolean decorated, int depth, Visual visual, 
			  boolean isRemwin) 
    {
        this.nativeWinContl = nativeWinContl;
        this.decorated = decorated;
        this.depth = depth;
        this.visual = visual;
	this.isRemwin = isRemwin;

        canvas = fws.getCanvasForRootWin(nativeWinContl.getRootWID());
        if (canvas == null) {
            throw new RuntimeException("can not find PseudoRootWindow for root window" +
				       nativeWinContl.getRootWID());
        } else {
            // initialize
            canvas.getImagePlateToVworld(tmpT3d);
            canvas.getPixelLocationInImagePlate(
						canvas.getWidth()/2, 
						canvas.getHeight()/2, center3d );
            tmpT3d.transform(center3d);

	    // DJ HACK:
            center3d.x = 0.0f;
            center3d.y = 0.0f;

            Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
            maxX = (float)center3d.x + (toolkit3d.getScreenWidth() / 2);
            maxY = (float)center3d.y + (toolkit3d.getScreenHeight() / 2);
            minX = (float)center3d.x - (toolkit3d.getScreenWidth() / 2);
            minY = (float)center3d.y - (toolkit3d.getScreenHeight() / 2);
        }

	fwsInit();

        // A kludge in NativePopup3D depends on mouse events posted against
        // NativeWindow3D objects.  Make sure this object becomes a mouse 
        // event source.  See NativePopup3D.initHelper() for details.
        setMouseEventSource(MouseEnteredEvent3D.class, true);

	if (isRemwin) {
	    setMouseEventSource(MouseEnteredEvent3D.class, true);
	    setMouseEventSource(MouseMotionEvent3D.class, true);
	    setMouseEventSource(MouseButtonEvent3D.class, true);
	    setMouseEventSource(MouseWheelEvent3D.class, true);
	}

        resolutionListener = new LgEventListener() {
		public void processEvent(final LgEvent event) {
		    //ScreenResolutionChangedEvent evt = 
		    //    (ScreenResolutionChangedEvent) event;			
		    // reinitialize
		    canvas.getImagePlateToVworld(tmpT3d);
		    canvas.getPixelLocationInImagePlate(
					canvas.getWidth() / 2, 
					canvas.getHeight() / 2,
					center3d);
		    tmpT3d.transform(center3d);
                        
	    // DJ HACK:
            center3d.x = 0.0f;
            center3d.y = 0.0f;

		    Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
		    maxX = (float) center3d.x
			+ (toolkit3d.getScreenWidth() / 2);
		    maxY = (float) center3d.y
			+ (toolkit3d.getScreenHeight() / 2);
		    minX = (float) center3d.x
			- (toolkit3d.getScreenWidth() / 2);
		    minY = (float) center3d.y
			- (toolkit3d.getScreenHeight() / 2);
		    nativeWidth = toolkit3d.widthNativeToPhysical(
					  NativeWindow3D.this.nativeWinContl.getWidth());
		    nativeHeight = toolkit3d.heightNativeToPhysical(
				          NativeWindow3D.this.nativeWinContl.getHeight());
                        
		    getFinalTranslation(tmpV3f);
		    changeNativeLocation(tmpV3f.x, tmpV3f.y);
		}

		public Class<LgEvent>[] getTargetEventClasses() {
		    return new Class[] { ScreenResolutionChangedEvent.class };
		}
                    
	    };
        
                
        LgEventConnector.getLgEventConnector().addListener(
							   LgEventSource.ALL_SOURCES, resolutionListener );
                
	Cursor3D cursor = Cursor3D.get("NATIVE_CURSOR_2D");
    }
    
    protected void fwsInit () {
	fws.trackEventsForNativeWindow(nativeWinContl.getWID());
	fws.addWindowResizeListener(nativeWinContl.getWID(), this);
    }

    protected void fwsTrackDamage () {
	fws.trackDamageForNativeWindow(nativeWinContl.getWID(), mainWindowImage);
	trackDamageEnabled = true;
    }

    protected void fwsUntrackDamage () {
	fws.trackDamageForNativeWindow(nativeWinContl.getWID(), null);
	trackDamageEnabled = false;
    }

    protected void fwsDestroy () {
	fws.destroyNativeWindow(nativeWinContl.getWID());
	fws.removeWindowResizeListener(nativeWinContl.getWID(), this);
    }

    public NativeWindowControl getNativeWindowControl() {
	return nativeWinContl;
    }
    
    protected void createLookAndFeel () {
	assert(mainWindowImage != null);
	assert(lookAndFeelClass != null);
	logger.fine("Instantiating Look & Feel: " + lookAndFeelClass);
        
        try {
            lookAndFeel 
                = (NativeWindowLookAndFeel)lookAndFeelClass.newInstance();
        } catch (Exception rex) {
            logger.severe("Unable to create native Look & Feel " + rex );
            throw new RuntimeException(rex);
        }
        
        lookAndFeel.initialize(this, mainWindowImage, decorated, shape,
			       cursor);
	lookAndFeel.setIsRemwin(isRemwin);
        addChild(lookAndFeel);
    }    
    
    public NativeWindowLookAndFeel getLookAndFeel() {
        return lookAndFeel;
    }
    
    public void nativeSizeChanged(int width, int height) 
	throws IllegalArgumentException
    {
        if (mainWindowImage == null) {
            int icFormat = ImageComponent2D.FORMAT_RGBA;
            Visual vis = null;
            if (depth == DEPTH16) {
                icFormat = ImageComponent2D.FORMAT_RGB5;
                // green mask is needed here 
                vis = visual;
                logger.info("Green Mask : " + visual.green_mask());
            } else if (depth == DEPTH24) {
                // green mask is not needed here 
            } else {
                logger.severe("Unsupported color depth: " + depth);
            }
            mainWindowImage
                = new TiledNativeWindowImage( 
					     fws, nativeWinContl.getWID(), icFormat, 
					     width, height, depth, vis); 
            
            createLookAndFeel();
            
            lookAndFeel.sizeChangedNative(width, height);

	    fwsTrackDamage();
        } else {
            mainWindowImage.sizeChanged(width, height, true);
        }
        lookAndFeel.sizeChangedNative(width, height);
    }
    
    /**
     * WindowResizeListener callback
     */
    public synchronized TiledNativeWindowImage sizeChanged(long wid, int width, int height) 
	throws IllegalArgumentException
    {
        assert(nativeWinContl.getWID() == wid);
        if (startResize || startMove) {
            return mainWindowImage;
        }
        Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
        nativeWidth = toolkit3d.widthNativeToPhysical(width);
        nativeHeight = toolkit3d.heightNativeToPhysical(height);
        
        if (lookAndFeel != null) {
            mainWindowImage.sizeChanged(width, height, true);
            lookAndFeel.sizeChanged3D(
				      toolkit3d.widthNativeToPhysical(width),
				      toolkit3d.heightNativeToPhysical(height));          
        } 
        
	return mainWindowImage;
    }
    
    public void destroyed() {
        if (trackDamageEnabled) {
            trackDamageEnabled = false;
        }
	fwsDestroy(); 
        changeEnabled(false);
        LgEventConnector.getLgEventConnector().removeListener(
							      LgEventSource.ALL_SOURCES, resolutionListener );
        removeAllChildren();
        mainWindowImage.destroy();
    }
    
    // invoked by X11WindowManager
    public void visibilityChanged(boolean visible) {
        if (visible) {
            if (!trackDamageEnabled) {
                /* we should track a new damage when mapping this again.
                   X server destroy the damage when unmapping the window */
		fwsTrackDamage();
            }
            
            if (!isFinalEnabled()) {
                // enable this lazily so that all the info is ready
                setEnabled(true);
            }
            
            int width = nativeWinContl.getWidth();
            int height = nativeWinContl.getHeight();
            int x = nativeWinContl.getX();
            int y = nativeWinContl.getY();
            Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
            nativeWidth = toolkit3d.widthNativeToPhysical(width);
            nativeHeight = toolkit3d.heightNativeToPhysical(height);
            
            if (decorated) {
                if (moveToInitLoc && x == 0 && y == 0) {
                    changeTranslation((float)center3d.x, (float)center3d.y, 0.0f, 0);
                    // the initial case and no default location is given - 
                    // move the window to the center
                    changeNativeLocation((float)center3d.x, (float)center3d.y);
                } else {
                    locationChanged(x, y);
                    if(moveToInitLoc) {
                        // when application set its default x,y not to 0,0 this part
                        // of code get called. setEnabled then at the end call our setTranslation
                        // with (0,0) which will force X application to get wrong x,y and thus
                        // create its popup/menu in wrong place. so we need to send again 
                        // the correct coordination.
                        synchronized (tmpV3f) {
                            getFinalTranslation(tmpV3f);
                            changeNativeLocation(tmpV3f.x, tmpV3f.y);
                        }
                    }
                }
                moveToInitLoc = false;
            } else {
                locationChanged(x, y);
            }
        } else {
            if (trackDamageEnabled) {
		fwsUntrackDamage();
            }
            if (!decorated) {
                setEnabled(false); // FIXME
            }
        }
        if (isFinalVisible() != visible) {
            super.changeVisible(visible);
        }
    }
    
    public void nameChanged(String name) {
        setName(name);
        if (lookAndFeel != null) {
            lookAndFeel.setName(name);
        }
    }
    
    public void locationChanged(int x, int y) {	
	synchronized (tmpP3d) {
            synchronized (tmpT3d) {
                canvas.getImagePlateToVworld(tmpT3d);        
                canvas.getPixelLocationInImagePlate(
			    x + (nativeWinContl.getWidth()/2),
			    y + (nativeWinContl.getHeight()/2), tmpP3d);        
                tmpT3d.transform(tmpP3d);
            }
	    System.err.println("natwin locationChanged: xyz = " + tmpP3d.x + " " + tmpP3d.y + " " + tmpP3d.z);
            if (decorated) {
                getFinalTranslation(tmpV3f);
                changeTranslation((float)tmpP3d.x, (float)tmpP3d.y, tmpV3f.z,
				  CHANGE_TRANSLATION_NO_NATIVE);
            } else {
                changeTranslation((float)tmpP3d.x, (float)tmpP3d.y, 0.0f, 
				  CHANGE_TRANSLATION_NO_NATIVE);
            }
        }
    }
    
    public float getDeltaX() {
        return deltaX;
    }
    
    private void changeNativeLocation(float x, float y) {
        
        /** TODO calcultation for deltaY. this calculatin work for
	 * screens placed in horizontal direction.
	 * change for issue 464 may break multiscreen, need to be tested with
	 * multisceen. 
	 */
        if ((x - nativeWidth / 2) > maxX) {             
            deltaX =  x;
            x = maxX - (nativeWidth / 2);
            deltaX = deltaX -x;            
        } else if ((x + nativeWidth / 2) < minX) {             
            deltaX =  x;
            x = minX + (nativeWidth / 2);
            deltaX = deltaX -x;            
        } else {
            deltaX = 0.0f;
        }
        
        x = x - (float)center3d.x; // make x,y relative to 0
        y = y - (float)center3d.y;
        // move x,y coord to top left corner of the native window.
        x = x - nativeWidth/2;
        y = y + nativeHeight/2;
        
        int nativeX = 0;
        int nativeY = 0;
        synchronized (tmpP3d) {
            synchronized (tmpT3d) {
                canvas.getVworldToImagePlate(tmpT3d);
                tmpP3d.set(x ,y , 0);
                tmpT3d.transform(tmpP3d);
            }
            canvas.getPixelLocationFromImagePlate(tmpP3d, tmpP2d);
            
            //Point p = canvas.getLocationOnScreen();
            nativeX = (int)tmpP2d.x;
            nativeY = (int)tmpP2d.y;
        }
	//        int nativeX 
	//            = toolkit3d.widthPhysicalToNative(
	//                toolkit3d.getScreenWidth() / 2 + (x - nativeWidth / 2)) + p.x;
	//        int nativeY 
	//            = toolkit3d.heightPhysicalToNative(
	//                toolkit3d.getScreenHeight() / 2 - (y + nativeHeight / 2)) + p.y;
        
        nativeWinContl.setLocation(nativeX, nativeY);
    }
    
    public void sizeChanged(int width, int height) {
        // Note: this is no longer necessary
        //fws.updateWindow(wid,width,height);
    }

    /**
     * @see NativeWindowMonitor#ShapeNotify(gnu.x11.Enum)
     */
    public void shapeNotify(gnu.x11.Enum enumerate, int nrect) {
        shape = new WindowShape(enumerate, nrect);		
        logger.info("ShapeNotify -> Nr Rect: " + nrect);
    }
    
    /** 
     * @see NativeWindowMonitor#setWindowProperity(int, int)
     */
    public void setWindowProperety(int minIncWidth, int minIncHeight, 
				   int minWidth, int minHeight, int baseWidth, int baseHeight) {
        this.minIncWidth = minIncWidth;
        this.minIncHeight = minIncHeight;
        this.minWidth = minWidth;
        this.minHeight = minHeight;
        this.baseWidth = baseWidth;
        this.baseHeight = baseHeight;
    }    
    
    /**
     * a callback that is invoked when this window is moved to top by the
     * SceneManager
     */
    public void movedToTop() {
        logger.info("NativeWindow3D.moveToTop() invoked");
        nativeWinContl.moveToTop();
    }
    
    /**
     * a callback that is invoked when this window is moved to bottom by the
     * SceneManager
     */
    public void movedToBottom() {
        logger.info("NativeWindow3D.moveToBottom() invoked");
    }
    
    private void setStartMove(boolean startMove) {
        this.startMove = startMove;
    }
    
    private void setStartResize(boolean startResize) {
        this.startResize = startResize;
    }
    
    public void associateWindow(NativeWindowMonitor nativeWinMonitor) {
        lookAndFeel.associateWindow(nativeWinMonitor);
    }
    
    public void dissociateWindow(NativeWindowMonitor nativeWinMonitor) {
        lookAndFeel.dissociateWindow(nativeWinMonitor);
    }
    
    public boolean isWindowAssociatable(NativeWindowControl nwc) {
        return lookAndFeel.isWindowAssociatable(nwc);
    }
    
    public void associatePopup(Component3D popup) {
        lookAndFeel.associatePopup(popup);
    }
    
    // used by NativeWindow3DS/SEResizers
    public synchronized void changeBodySize(float width, float height) 
	throws IllegalArgumentException
    {
        boolean stopResize = false;
        Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
        if ((minIncWidth != 0) || (minIncHeight != 0)){
            // the following calculation I have taken from TWM window manager.            
            // check width
            if (minIncWidth != 0) {
                if (nativeWidth != width) {
                    maxWidth = (width > toolkit3d.getScreenWidth()) ? 
			toolkit3d.getScreenWidth():width;
                    if (width < toolkit3d.widthNativeToPhysical(minWidth)) {
                        width = toolkit3d.widthNativeToPhysical(minWidth);
                        stopResize = true;
                    }
                    if (width > maxWidth) {
                        width = maxWidth;
                    }
                    int nativeXWidth = ((toolkit3d.widthPhysicalToNative(width)
					 - baseWidth) / minIncWidth * minIncWidth) + baseWidth;
                    width = toolkit3d.widthNativeToPhysical(nativeXWidth);
                }
            }        
            // check height
            if (minIncHeight != 0) {
                if (nativeHeight != height) {
                    maxHeight = (height > toolkit3d.getScreenHeight()) ? 
			toolkit3d.getScreenHeight():height;
                    if (height < toolkit3d.heightNativeToPhysical(minHeight)) {
                        height = toolkit3d.heightNativeToPhysical(minHeight);
                        stopResize = true;
                    }
                    if (height > maxHeight) {
                        height = maxHeight;
                    }
                    int nativeXHeight = ((toolkit3d.heightPhysicalToNative(height)
					  - baseHeight) / minIncHeight * minIncHeight) + baseHeight;
                    height = toolkit3d.heightNativeToPhysical(nativeXHeight);
                }
            }
        }
        
        if (!stopResize) {
	    nativeWidth = width;
	    nativeHeight = height;
            if (trackDamageEnabled) {        	
                if (lookAndFeel != null) {
                    if (!startResize) {
                	nativeWinContl.setSize(
					       toolkit3d.widthPhysicalToNative(width),
					       toolkit3d.heightPhysicalToNative(height));
                    }
                    mainWindowImage.sizeChanged(
						toolkit3d.widthPhysicalToNative(width),
						toolkit3d.heightPhysicalToNative(height), 
						!startResize);
                    lookAndFeel.sizeChanged3D(width, height);
                }
            }
        }
    }
    
    public float getBodyWidth() {
        return nativeWidth;
    }
    
    public float getBodyHeight() {
        return nativeHeight;
    }
    
     public int getBodyNativeWidth() {
	 return nativeWinContl.getWidth();
     }

     public int getBodyNativeHeight() {
	 return nativeWinContl.getHeight();
     }

    public void changeTranslation(float x, float y, float z, int duration) {
        if (duration == CHANGE_TRANSLATION_NO_NATIVE) {
            // a bit hacky way to avoid locationChanged() to call
            // changeNativeLocation() unnecessarily
            super.changeTranslation(x, y, z);
        } else {
            super.changeTranslation(x, y, z, duration);
            if (!startResize && !startMove) {
                changeNativeLocation(x, y);
            }
        }
    }
    
    // invoked by SceneManager
    public void changeVisible(boolean visible, int duration) {
        // nativeWinContl==null could happen while instantiating NativeWindow3D
        if (nativeWinContl != null) {
            nativeWinContl.setVisible(visible);
        }
        if (isFinalVisible() != visible) {
            super.changeVisible(visible, duration);
        }
    }
    
    // invoked by SceneManager
    public void changeEnabled(boolean enabled, int duration) {
        if (enabled) {
            int width = nativeWinContl.getWidth();
            int height = nativeWinContl.getHeight();
            Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
            float widthP = toolkit3d.widthNativeToPhysical(width);
            float heightP = toolkit3d.heightNativeToPhysical(height);
            
            changeBodySize(widthP, heightP);
            nativeSizeChanged(width, height);
        }
        super.changeEnabled(enabled, duration);
    }
    
    protected void setEnabledInternal(boolean enabled) {
        super.setEnabledInternal(enabled);
        
        if (!enabled)
            getNativeWindowControl().destroy();
    }
    
    public Point3d translateXYToVworld(int x, int y, Point3d p3d) {
        javax.media.j3d.Transform3D t3d = new javax.media.j3d.Transform3D();
        canvas.getImagePlateToVworld(t3d);
        canvas.getPixelLocationInImagePlate(x, y, p3d);        
        t3d.transform(p3d);
        return p3d;
    }

    public void restackWindow(NativeWindowMonitor sibWin, int order) {
        switch(order) {
        case 0: // ABOVE
	    if (sibWin == null) return;
            assert(sibWin instanceof Component3D);
            Component3D sibWinComp = (Component3D)sibWin;
            postEvent(new Component3DToFrontEvent(sibWinComp));
            break;
	default:
	    throw new RuntimeException("Unimplemented restack order");
        }
    }
    
    public TiledNativeWindowImage getImage () {
	return mainWindowImage;
    }

    public Cursor3D getNativeCursor () {
	if (lookAndFeel == null) return null;
	// TODO: HACK
	return lookAndFeel.getNativeCursor();
    }

    public SimpleAppearance getDecorationAppearance () {
	if (lookAndFeel == null) return null;
	return lookAndFeel.getDecorationAppearance();
    }
}
