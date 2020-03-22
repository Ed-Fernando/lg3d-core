/** Copyright (c) 2004 Amir Bukhari
 *
 * Permission to use, copy, modify, distribute, and sell this software and its
 * documentation for any purpose is hereby granted without fee, provided that
 * the above copyright notice appear in all copies and that both that
 * copyright notice and this permission notice appear in supporting
 * documentation, and that the name of Amir Bukhari not be used in
 * advertising or publicity pertaining to distribution of the software without
 * specific, written prior permission.  Amir Bukhari makes no
 * representations about the suitability of this software for any purpose.  It
 * is provided "as is" without express or implied warranty.
 *
 * AMIR BUKHARI DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE,
 * INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS, IN NO
 * EVENT SHALL AMIR BUKHARI BE LIABLE FOR ANY SPECIAL, INDIRECT OR
 * CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE,
 * DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER
 * TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 * PERFORMANCE OF THIS SOFTWARE.
 */
/**
 * Project Looking Glass
 *
 * $RCSfile: NativePopup3D.java,v $
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
 * $Revision: 1.22 $
 * $Date: 2007-04-10 22:58:42 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.nativewindow;

import java.lang.ref.WeakReference;
import java.util.logging.Logger;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import gnu.x11.Visual;

import org.jdesktop.lg3d.displayserver.fws.FoundationWinSys;
import org.jdesktop.lg3d.displayserver.fws.WindowResizeListener;
import org.jdesktop.lg3d.sg.ImageComponent2D;
import org.jdesktop.lg3d.utils.action.ActionBoolean;
import org.jdesktop.lg3d.utils.eventadapter.MouseEnteredEventAdapter;
import org.jdesktop.lg3d.utils.c3danimation.NaturalMotionAnimation;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Container3D;
import org.jdesktop.lg3d.wg.Toolkit3D;
import org.jdesktop.lg3d.wg.Cursor3D;
import org.jdesktop.lg3d.wg.event.Component3DToFrontEvent;
import org.jdesktop.lg3d.wg.event.LgEventConnector;
import org.jdesktop.lg3d.wg.event.LgEventSource;


/**
 * NativePopup3D provides the anchor for rendering native pop-up windows.
 *
 * FIXME -- most of the code is copyed from NativeWindow3D.  Once the code
 * got settled, we should eliminate duplications as much as possible.
 */
public class NativePopup3D extends Container3D 
    implements WindowResizeListener, NativeWindowMonitor
{
    private static final Logger logger = Logger.getLogger("lg.displayserver");
    private static final int DEPTH16 = 16;
    private static final int DEPTH24 = 24;
    
    private FoundationWinSys fws = FoundationWinSys.getFoundationWinSys();
    private TiledNativeWindowImage mainWindowImage = null;
    protected NativePopupLookAndFeel lookAndFeel;
    private NativeWindowControl nativeWinContl;
    private boolean trackDamageEnabled = false;
    private WindowShape shape;
    private int depth;
    private Visual visual = null;
    protected NativeWindow3D preDefinedParent;
    protected NativeWindow3D associatedParent;
    private int fuzzyEdgeWidth;
    private boolean shadow;
    protected Vector3f tmpV3f = new Vector3f();
    private boolean isRemwin;
    private Cursor3D cursor;
    
    
    // FIXME -- would this be the right place to locate the following code?
    /*
     * The following code is for associating popup window with 
     * a "reasonable" application window (NativeWindow3D).
     * We keep track of the last NativeWindow3D the mouse entered. 
     * It seems to work OK.  We may need to evolve this heuristic in future.
     */
    static WeakReference<NativeWindow3D> parentNativeWindowCandidate = new WeakReference(null);
    public static void initHelper() {
        LgEventConnector.getLgEventConnector().addListener(NativeWindow3D.class,
            new MouseEnteredEventAdapter(
                new ActionBoolean() {
                    public void performAction(LgEventSource source, boolean enter) {
                        if (enter) {
                            parentNativeWindowCandidate = new WeakReference((NativeWindow3D)source);
                        }
                    }
                }
            ));
    }
    
    public NativePopup3D(NativeWindowControl nativeWinContl, 
        boolean decorated, int depth, Visual visual, 
        NativeWindow3D preDefinedParent, int fuzzyEdgeWidth, boolean shadow) 
    {
	this(nativeWinContl, decorated, depth, visual, preDefinedParent, fuzzyEdgeWidth,
	     shadow, false);
    }

    /**
     * Creates a native window, the presence of decorations can be
     * controlled via the decorated parameter.
     *
     * @param decorated indicates if the NativeWindow3D should
     * have any decorations
     */
    public NativePopup3D(NativeWindowControl nativeWinContl, 
        boolean decorated, int depth, Visual visual, 
        NativeWindow3D preDefinedParent, int fuzzyEdgeWidth, boolean shadow,
        boolean isRemwin) 
    {
        this.nativeWinContl = nativeWinContl;
        this.depth = depth;
        this.visual = visual;
        this.preDefinedParent = preDefinedParent;
        this.fuzzyEdgeWidth = fuzzyEdgeWidth;
        this.shadow = shadow;
	this.isRemwin = isRemwin;

	fwsInit();

        associatedParent = preDefinedParent;
        if (associatedParent == null && parentNativeWindowCandidate.get() == null) {
            logger.warning("Popup occured while parentNativeWindowCandidate is null");
            nativeWinContl.destroy();
        }
        
        if (associatedParent == null) {
            associatedParent = parentNativeWindowCandidate.get();
	    // For some apps, such as staroffice, the parent candidate is null here.
	    if (associatedParent != null) {
		associatedParent.associatePopup(this);
	    } else {
		logger.warning("Popup occured while parentNativeWindowCandidate is null");
		nativeWinContl.destroy();
	    }
        }
        
        setAnimation(new NaturalMotionAnimation(500));
        setVisible(false);

	Cursor3D cursor = Cursor3D.get("NATIVE_CURSOR_2D");
    }

    protected void fwsInit () {
        fws.addWindowResizeListener(nativeWinContl.getWID(), this);
	fws.trackEventsForNativeWindow(nativeWinContl.getWID());
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
    
    private void createLookAndFeel (Cursor3D cursor) {
        assert(mainWindowImage != null);
        // FIXME -- not to hardcode
        lookAndFeel 
            = new org.jdesktop.lg3d.scenemanager.utils.decoration.SimpleNativePopupLookAndFeel();
        lookAndFeel.initialize(this, mainWindowImage, false, shape, fuzzyEdgeWidth, shadow,
			       cursor);
	lookAndFeel.setIsRemwin(isRemwin);
        addChild(lookAndFeel);
    }    
    
    public void nativeSizeChanged(int width, int height) {

        if (width <= 0 || height <= 0) {
	    mainWindowImage = null;
	    fwsUntrackDamage();
	    return;
        }

        if (mainWindowImage == null) {
            int icFormat = ImageComponent2D.FORMAT_RGB;
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
            
            createLookAndFeel(cursor);
            
            lookAndFeel.sizeChangedNative(width, height);
	    fwsTrackDamage();
        } else {
            mainWindowImage.sizeChanged(width, height, true);            
        }
        lookAndFeel.sizeChangedNative(width, height);
    }
    
    public void changeTranslation(float x, float y, float z, int duration) {
	super.changeTranslation(x, y, z, duration);
	if (associatedParent == null) return;
        synchronized (tmpV3f) {
            associatedParent.getFinalTranslation(tmpV3f);
            changeNativeLocation(x + tmpV3f.x, y + tmpV3f.y);
        }
    }
    
    /**
     * WindowResizeListener callback
     */
    public synchronized TiledNativeWindowImage sizeChanged(long wid, int width, int height) 
	throws IllegalArgumentException
    {
        assert(nativeWinContl.getWID() == wid);
        
        Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
        synchronized (tmpV3f) {
            getPreferredSize(tmpV3f);
            tmpV3f.x = toolkit3d.widthNativeToPhysical(width);
            tmpV3f.y = toolkit3d.heightNativeToPhysical(height);
            setPreferredSize(tmpV3f);
        }
        if (lookAndFeel != null) {
            lookAndFeel.sizeChanged3D(
                toolkit3d.widthNativeToPhysical(width),
                toolkit3d.heightNativeToPhysical(height),
		width, height);
        }
        
        // restore the old location. resize doesn't change the location of X Window!
        locationChanged(nativeWinContl.getX(), nativeWinContl.getY());
        
	return mainWindowImage;
    }
    
    public void destroyed() {
        if (trackDamageEnabled) {
            trackDamageEnabled = false;
        }
	fwsDestroy();
        mainWindowImage.destroy();
        detach();
    }
    
    public void visibilityChanged(boolean visible) {
        if (visible) {
            if (!trackDamageEnabled) {
                /* we should track a new damage when mapping this again.
                   X server destroy the damage when unmapping the window */
		fwsTrackDamage();
            }
            
            int width = nativeWinContl.getWidth();
            int height = nativeWinContl.getHeight();
            int x = nativeWinContl.getX();
            int y = nativeWinContl.getY();
            Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
            
            synchronized (tmpV3f) {
                tmpV3f.set(
                    toolkit3d.widthNativeToPhysical(width), 
                    toolkit3d.heightNativeToPhysical(height),
                    toolkit3d.widthNativeToPhysical(10));
                setPreferredSize(tmpV3f);
            }
            nativeSizeChanged(width, height);
            locationChanged(x, y);
            
            // make sure to fade in from completely transparent
            setTransparency(0.75f);

	    // Fix for 423: Hideya originally wanted popups to have a slightly 
	    // transparent look. But this is causing the StarOffice slide show
	    // to show a bit of what is behind it. We decided to abandon the
	    // slight transparency.
            //changeTransparency(0.05f);
            changeTransparency(0.0f);
        } else {
            if (trackDamageEnabled) {
		fwsUntrackDamage();
            }
            changeTransparency(0.75f);
        }
	System.err.println("NP3D: call super.changeVisible");
        super.changeVisible(visible);
    }
    
    public void nameChanged(String name) {
        setName(name);
    }
    
    public synchronized void locationChanged(int x, int y) {
        if (preDefinedParent != null) {
            // FIXME -- always place this associated window at the center of
            // the parent window. 
            changeTranslation(0.0f, 0.0f, -0.003f/*FIXME*/, 0);
            return;
        }
	if (associatedParent == null) return;
        synchronized (tmpV3f) {
            getPreferredSize(tmpV3f);
            Point3d p3d = new Point3d();
            associatedParent.translateXYToVworld(x, y, p3d);
            Vector3f parentLoc = associatedParent.getFinalTranslation(new Vector3f());
            float dx = (float)p3d.x + (tmpV3f.x / 2) - parentLoc.x 
        	     + associatedParent.getDeltaX();
            float dy = (float)p3d.y - (tmpV3f.y / 2) - parentLoc.y;
            super.changeTranslation(dx, dy, 0.0f, 0);
        }
    }
    
    private void changeNativeLocation(float x, float y) {
        synchronized (tmpV3f) {
            getPreferredSize(tmpV3f);
            Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
            int nativeX 
                = toolkit3d.widthPhysicalToNative(
                    toolkit3d.getScreenWidth() / 2 + (x - tmpV3f.x / 2));
            int nativeY 
                = toolkit3d.heightPhysicalToNative(
                    Toolkit3D.getToolkit3D().getScreenHeight() / 2 - (y + tmpV3f.y / 2)); 
        
            nativeWinContl.setLocation(nativeX, nativeY);
        }
    }
    
    public void sizeChanged(int width, int height) {
        // Note: this is no longer necessary
    	//fws.updateWindow(nativeWinContl.getWID(), width, height);
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
        
    }
    
    public void associateWindow(NativeWindowMonitor nativeWinMonitor) {
        throw new RuntimeException("shouldn't be invoked");
    }
    
    public boolean isWindowAssociatable(NativeWindowControl nativeWinControl) {
        throw new RuntimeException("shouldn't be invoked");
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

    public int getBodyNativeWidth() {
        return nativeWinContl.getWidth();
    }
    
    public int getBodyNativeHeight() {
        return nativeWinContl.getHeight();
    }
    
    public TiledNativeWindowImage getImage () {
	return mainWindowImage;
    }

    public Cursor3D getNativeCursor () {
	if (lookAndFeel == null) return null;
	// TODO: HACK
	return lookAndFeel.getNativeCursor();
    }
}
