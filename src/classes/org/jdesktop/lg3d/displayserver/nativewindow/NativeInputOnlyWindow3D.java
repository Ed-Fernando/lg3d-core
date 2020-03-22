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
 * $RCSfile: NativeInputOnlyWindow3D.java,v $
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
 * $Revision: 1.11 $
 * $Date: 2006-09-26 23:13:41 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.nativewindow;

import java.lang.ref.WeakReference;
import java.util.logging.Logger;
import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.sg.BranchGroup;
import org.jdesktop.lg3d.sg.Transform3D;
import org.jdesktop.lg3d.sg.TransformGroup;
import org.jdesktop.lg3d.utils.shape.PickableRegion;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Container3D;
import org.jdesktop.lg3d.wg.Cursor3D;
import org.jdesktop.lg3d.wg.Toolkit3D;


/**
 * NativeInputOnlyWindow3D provides the anchor for rendering Native InputOnly 
 * windows.
 * 
 * @author Amir Bukhari
 */
public class NativeInputOnlyWindow3D extends Container3D implements
        NativeWindowMonitor {
    protected static final Logger logger = Logger.getLogger("lg.displayserver");
    
    protected NativeWindowControl nativeWinContl;
    protected boolean decorated = true;
    protected NativeInputOnlyWindow3D nativeInputOnlyWindow;
    private NativePane bg;
    private PickableRegion pickRegion;

    private TransformGroup center;
    private int oldX = -1;
    private int oldY = -1;
    private boolean associatedWithParent = false;
    private boolean ignoreParent = false;
    private WeakReference<NativeWindow3D> parentNativeWindow = null;
    private Vector3f tmpV3f = new Vector3f();
    
    /**
     * Creates a native inputonly window.
     * 
     * @param nativeWinContl
     */
    public NativeInputOnlyWindow3D(NativeWindowControl nativeWinContl) {
        this(nativeWinContl, false);
    }

    /**
     * Creates a native inputonly window.
     * 
     * @param nativeWinContl
     * @param decorated
     */
    public NativeInputOnlyWindow3D(NativeWindowControl nativeWinContl,
            boolean decorated) {
        this.nativeWinContl = nativeWinContl;
        this.decorated = decorated;
        nativeInputOnlyWindow = this;
        parentNativeWindow = NativePopup3D.parentNativeWindowCandidate;
        if(parentNativeWindow.get() == null) {
            ignoreParent = true;
        }
    }

    public NativeWindowControl getNativeWindowControl() {
        return nativeWinContl;
    }

    private void createLookAndFeel() {
        Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
        addChild(setupComponents(
                toolkit3d.widthNativeToPhysical(nativeWinContl.getWidth()),
                toolkit3d.heightNativeToPhysical(nativeWinContl.getHeight())));
    }

    private Container3D setupComponents(float width, float height) {
        Container3D rootCont = new Container3D();

        Component3D panelComp = new Component3D();
        Component3D pickable = new Component3D();
        pickRegion = new PickableRegion(width, height);
        pickable.addChild(pickRegion);
        createPane(nativeWinContl.getWID(), nativeWinContl.getWidth(),
                nativeWinContl.getHeight());

        pickable.setTranslation(0.0f, 0.0f, 0.0f);
        bg.addChild(pickable);
        center = new TransformGroup();
        center.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        center.addChild(bg);
        Transform3D tr = new Transform3D();
        tr.set(1.0f, new Vector3f(0.0f, 0.0f, 0.0f));
        center.setTransform(tr);
        panelComp.addChild(center);
        panelComp.setCursor(Cursor3D.get("NATIVE_CURSOR_2D"));
        rootCont.addChild(panelComp);

        return rootCont;
    }

    public void createPane(long WID, int width, int height) {
        Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
        bg = new NativePane();
        bg.setWid(WID);
        bg.setNativeWidth(width);
        bg.setNativeHeight(height);
        bg.setWidth(toolkit3d.widthNativeToPhysical(width));
        bg.setHeight(toolkit3d.heightNativeToPhysical(height));
        bg.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
        bg.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        bg.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        bg.setCapability(BranchGroup.ALLOW_DETACH);

    }

    public void nativeSizeChanged(int width, int height) {
        if (!associatedWithParent) {
            createLookAndFeel();
        } else {
            sizeChanged(width, height);
        }
    }

    public void setSize(float width, float height, float depth) {
        tmpV3f.set(width, height, depth);
        super.setPreferredSize(tmpV3f);
    }

    // methods from NativeWindowMonitor

    public void destroyed() {
        logger.fine("inputonly destroyed: " + nativeWinContl.getWID());
        if(parentNativeWindow.get() == null) {
            return;
        }
        detach();
    }

    public void visibilityChanged(boolean visible) {
        if (visible) {
            int width = nativeWinContl.getWidth();
            int height = nativeWinContl.getHeight();
            int x = nativeWinContl.getX();
            int y = nativeWinContl.getY();

            logger.fine("activate InputOnly");
            Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
            setSize(
                toolkit3d.widthNativeToPhysical(width),
                toolkit3d.heightNativeToPhysical(height),
                toolkit3d.widthNativeToPhysical(10));
            nativeSizeChanged(width, height);
            if (!associatedWithParent && !ignoreParent) {
                parentNativeWindow.get().associatePopup(this);
                associatedWithParent = true;
            }else if(ignoreParent) {
                associatedWithParent = true;
            }
            changeVisible(true);
            changeScale(1.0f);
            changeRotationAngle(0.0f);
            locationChanged(x,y);

        } else {
            changeVisible(false);
        }
    }

    public void sizeChanged(int w, int h) {
        getPreferredSize(tmpV3f);
        Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
        tmpV3f.x = toolkit3d.widthNativeToPhysical(w);
        tmpV3f.y = toolkit3d.heightNativeToPhysical(h);
        super.setPreferredSize(tmpV3f);
        pickRegion.setSize(
                toolkit3d.widthNativeToPhysical(w),
                toolkit3d.heightNativeToPhysical(h));
        bg.setNativeWidth(w);
        bg.setNativeHeight(h);
        bg.setWidth(toolkit3d.widthNativeToPhysical(w));
        bg.setHeight(toolkit3d.heightNativeToPhysical(h));
    }

    public void locationChanged(int x, int y) {
        if(parentNativeWindow.get() == null) {
            return;
        }
        getPreferredSize(tmpV3f);
        Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
        float dx = (toolkit3d.widthNativeToPhysical(x) - toolkit3d.getScreenWidth() / 2)
                + tmpV3f.x / 2;
        float dy = (toolkit3d.getScreenHeight() / 2 - toolkit3d.heightNativeToPhysical(y))
                - tmpV3f.y / 2;

        getTranslation(tmpV3f);
        changeTranslation(dx, dy, tmpV3f.z);
    }

    public void locationChanged(float x, float y) {
        if(parentNativeWindow.get() == null) {
            return;
        }
        getPreferredSize(tmpV3f);
        Toolkit3D toolkit3d = Toolkit3D.getToolkit3D();
        int nativeX = toolkit3d.widthPhysicalToNative(toolkit3d.getScreenWidth() / 2
                        + (x - tmpV3f.x / 2));
        int nativeY = toolkit3d.widthPhysicalToNative(toolkit3d.getScreenHeight() / 2
                        - (y + tmpV3f.y / 2));
        if ((oldX != nativeX) || (oldY != nativeY)) {
            oldX = nativeX;
            oldY = nativeY;
            nativeWinContl.setLocation(nativeX, nativeY);
        }
    }

    public void nameChanged(String name) {
        // TODO Auto-generated method stub
    }

    public void shapeNotify(gnu.x11.Enum enumerate, int nrect) {
        // TODO Auto-generated method stub
    }
    
    /* (non-Javadoc)
     * @see org.jdesktop.lg3d.displayserver.nativewindow.NativeWindowMonitor#setWindowProperity(int, int)
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
        /*
        switch(order) {
        case 0: // ABOVE
            this.postEvent(new Component3DToFrontEvent());
            break;
	default:
	    throw new RuntimeException("Unimplemented restack order");
        }
	*/
    }
}
