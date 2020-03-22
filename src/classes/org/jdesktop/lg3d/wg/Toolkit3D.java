/**
 * Project Looking Glass
 *
 * $RCSfile: Toolkit3D.java,v $
 *
 * Copyright (c) 2006, Sun Microsystems, Inc., All Rights Reserved
 *
 * Redistributions in source code form must reproduce the above
 * copyright and this condition.
 *
 * The contents of this file are subject to the GNU General Public
 * License, Version 2 (the "License"); you may not use this file
 * except in compliance with the License. A copy of the License is
 * available at http://www.opensource.org/licenses/gpl-license.php.
 *
 * $Revision: 1.10 $
 * $Date: 2007-08-02 22:30:46 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.wg;

import javax.media.j3d.Canvas3D;
import javax.vecmath.Point3f;
import javax.vecmath.Point3d;
import com.sun.j3d.utils.universe.ViewInfo;
import org.jdesktop.lg3d.displayserver.fws.FoundationWinSys;


/**
 * This class is analogous to those of AWT's Toolkit and used to
 * obtain properties related to LG3D graphics rendering.
 */
public final class Toolkit3D {
    // NOTE -- Unlike AWT's Toolkit, this class is NOT meant to be
    // an abstract superclass of all actual implementations of toolkit.
    // Also, different from AWT, applications are NOT discouraged
    // to call any of the methods in this class directly. 
    
    private static Toolkit3D toolkit3d;
    
    private Canvas3D canvas3d;
    private javax.media.j3d.Transform3D junk = new javax.media.j3d.Transform3D();
    private javax.media.j3d.Transform3D eye2vpl = new javax.media.j3d.Transform3D();
    private Point3d eyePos = new Point3d();
    private Point3f eyePosP3f = new Point3f();
    private FoundationWinSys fws;
    
    private boolean inWonderland;

    private float lg_to_wl_width_factor;
    private float lg_to_wl_height_factor;
    
    /* Prevent the user to create an instance of this class 
     * The user is expected to use getToolkit3D for creation */
    private Toolkit3D() {
	inWonderland = System.getProperty("lg.wonderland", "false").equals("true");

	lg_to_wl_width_factor =  inWonderland ? LG_TO_WL_WIDTH_FACTOR  : 1.0f;
	lg_to_wl_height_factor = inWonderland ? LG_TO_WL_HEIGHT_FACTOR : 1.0f;

        fws = FoundationWinSys.getFoundationWinSys();
        canvas3d = fws.getCanvas(0/*TODO*/);
    }
    
    /**
     * Gets the default toolkit. 
     * TODO -- Because of the nature of capabilities this class offer,
     * an object need to be created per view.  New method will
     * be added to obtain one toolkit3d per view, when we start
     * supporting multiple view fully.
     */
    public synchronized static Toolkit3D getToolkit3D() {
        if (toolkit3d == null) {
            toolkit3d = new Toolkit3D();
        }
        return toolkit3d;
    }
    
    /**
     * Gets the virtual screen width in meters for this view.
     */
    public float getScreenWidth() {
        //return (float)canvas3d.getPhysicalWidth();
        return lg_to_wl_width_factor * (float)fws.getViewInfo().getPhysicalWidth(canvas3d);
    }
    
    /**
     * Gets the virtual screen height in meters for this view.
     */
    public float getScreenHeight() {
        //return (float)canvas3d.getPhysicalHeight();
        return lg_to_wl_height_factor * (float)fws.getViewInfo().getPhysicalHeight(canvas3d);
    }
    
    /**
     * Gets the canvas width in in pixels for this view.
     */
    public int getCanvasWidth() {
        return canvas3d.getWidth();
    }
    
    /**
     * Gets the canvas height in pixels for this view.
     */
    public int getCanvasHeight() {
        return canvas3d.getHeight();
    }
    
    /*
    private static final float LG_TO_WL_WIDTH_FACTOR = 5.0f / 0.18570222f;
    private static final float LG_TO_WL_HEIGHT_FACTOR = 4.0f / 0.12333111f;
    */
    private static final float LG_TO_WL_WIDTH_FACTOR = 5.0f / 2.2295556f;
    private static final float LG_TO_WL_HEIGHT_FACTOR = 4.0f / 1.5663333f;
 
    //private static final float LG_TO_WL_WIDTH_FACTOR = 20.0f;
    //private static final float LG_TO_WL_HEIGHT_FACTOR = 20.0f;
    //private static final float LG_TO_WL_WIDTH_FACTOR = 10.0f;
    //private static final float LG_TO_WL_HEIGHT_FACTOR = 10.0f;
    //private static final float LG_TO_WL_WIDTH_FACTOR = 1.0f;
    //private static final float LG_TO_WL_HEIGHT_FACTOR = 1.0f;

    /**
     * Converts the pixel native width into the physical width in meters
     */
    public float widthNativeToPhysical(int nativeWidth) {
	// DJ: TODO: HACK
	if (inWonderland) {
	    return (float)nativeWidth / 1280.0f * 5.0f;
	} else {
	    return nativeWidth * getScreenWidth() / getCanvasWidth();
	}
    }
    
    /**
     * Converts the pixel native height into the physical height in meters
     */
    public float heightNativeToPhysical(int nativeHeight) {
	// DJ: TODO: HACK
	if (inWonderland) {
	    return (float) nativeHeight / 1024.0f * 4.0f;
	} else {
	    return nativeHeight * getScreenHeight() / getCanvasHeight();
	}
    }
    
    /**
     * Converts the physical width in meters into the pixel native width
     */
    public int widthPhysicalToNative(float physicalWidth) {
	// DJ: TODO: HACK
	//if (inWonderland) {
	//    // First convert from WL coords to LG coords
	//    physicalWidth /= LG_TO_WL_WIDTH_FACTOR;
	//}

	if (inWonderland) {
	    return (int)(physicalWidth * 1280.0f / 5.0f);
	} else {
	    return (int)(physicalWidth * getCanvasWidth() / getScreenWidth());
	}
    }
    
    /**
     * Converts the physical height in meters into the pixel native height
     */
    public int heightPhysicalToNative(float physicalHeight) {
	// DJ: TODO: HACK
	//if (inWonderland) {
	//    // First convert from WL coords to LG coords
	//    physicalHeight /= LG_TO_WL_HEIGHT_FACTOR;
	//}

	if (inWonderland) {
	    return (int)(physicalHeight * 1024.0f / 4.0f);
	} else {
	    return (int)(physicalHeight * getCanvasHeight() / getScreenHeight());
	}
    }
    
    /**
     * Returns the eye position in the virtual world.
     */
    public Point3f getEyePositionInVworld(Point3f ret) {
        ViewInfo viewInfo = fws.getViewInfo();
        
        synchronized (eyePos) {
            viewInfo.getEyeToViewPlatform(canvas3d, eye2vpl, junk);
            eyePos.set(0.0, 0.0, 0.0);
            eye2vpl.transform(eyePos);
        
            ret.set((float)eyePos.x, (float)eyePos.y, (float)eyePos.z);
        }
        return ret;
    }
    
    /**
     * Returns the field of view of the universe (in terms of screen width).
     * 
     * @return float the field of view of the universe
     */
    public float getFieldOfView() {
        return getFieldOfView(getScreenWidth());
    }
    
    /**
     * Returns the field of view of the universe (in terms of screen width).
     * Calculate the result using the givin width.
     * This method is used when object's position need to be changed to new
     * position before the screen size if fully updated.
     * 
     * @return float the field of view of the universe
     */
    public float getFieldOfView(float width) {
        // The value View.getFieldOfView() returns is not valid when the
        // Canvas3D's window eyepoint policy is set to other than 
        // RELATIVE_TO_FIELD_OF_VIEW.  We use RELATIVE_TO_COEXISTENCE/WINDOW.
        
        float fov = 0.0f;
        synchronized (eyePos) {
            getEyePositionInVworld(eyePosP3f);
            if (eyePos.z == 0.0) {
                throw new InternalError("sysPos.z == 0?");
            }
            fov = (float)Math.atan(width * 0.5f / eyePos.z) * 2.0f;
        }
        return fov;
    }
}
