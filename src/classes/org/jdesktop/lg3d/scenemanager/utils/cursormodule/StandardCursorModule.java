/**
 * Project Looking Glass
 *
 * $RCSfile: StandardCursorModule.java,v $
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
 * $Revision: 1.20 $
 * $Date: 2007-07-21 01:34:31 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.cursormodule;


import java.util.logging.Logger;
import javax.media.j3d.Canvas3D;
import javax.vecmath.Vector3f;
import javax.vecmath.Point3d;
import com.sun.j3d.utils.universe.ViewInfo;

import org.jdesktop.lg3d.scenemanager.CursorModule;
import org.jdesktop.lg3d.displayserver.fws.FoundationWinSys;
import org.jdesktop.lg3d.displayserver.fws.MouseEventNodeInfo;
import org.jdesktop.lg3d.sg.BranchGroup;
import org.jdesktop.lg3d.sg.TransformGroup;
import org.jdesktop.lg3d.sg.Transform3D;
import org.jdesktop.lg3d.wg.Cursor3D;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventConnector;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.wg.event.LgEventSource;


/**
 *
 * @author  Paul
 */
public class StandardCursorModule implements CursorModule {
    protected static final Logger logger = Logger.getLogger("lg.scenemanager");
    private static final float cursorDistFurthest = 0.7f;
    public static final float cursorDistMargin = 0.05f;
    
    private BranchGroup cursorRoot;
    private TransformGroup cursorPosTG;
    private Cursor3D currentCursor;
    private Transform3D tmpT3d = new Transform3D();
    private Vector3f tmpV3f = new Vector3f();
    private Point3d tmpP3d = new Point3d();
    protected ViewInfo viewInfo;
    private boolean hideCursor = false;
    
    // Variables for setCursorPosition. They are class scope so we
    // don't cause excessive GC
    private javax.media.j3d.Transform3D ip2vpl = new javax.media.j3d.Transform3D();
    private javax.media.j3d.Transform3D junk = new javax.media.j3d.Transform3D();
    private javax.media.j3d.Transform3D eye2vpl = new javax.media.j3d.Transform3D();    
    private Point3d pixelInIP = new Point3d();
    // End setCursorPosition variables.
    
    public StandardCursorModule() {
        viewInfo = FoundationWinSys.getFoundationWinSys().getViewInfo();
        
        initHideEventHandler();
    }

    /**
     * Adds the cursor
     * to the set of available cursors
     */
    public synchronized void addCursor(Cursor3D cursor) {
        cursor.setCapability(BranchGroup.ALLOW_DETACH);
        cursorRoot.addChild(cursor);
        logger.fine("Added cursor " + cursor.getName());
    }
      
    /**
     * Return the current cursor
     */
    public Cursor3D getCursor () {
	return currentCursor;
    }

    /**
     * Set the current cursor, making the previous cursor invisible
     */
    public synchronized void setCursor(Cursor3D cursor) {
        if (cursor == currentCursor || hideCursor) {
            return;
        }
        
        if (cursorRoot.indexOfChild(cursor)==-1)
            addCursor(cursor);
        
        if (currentCursor != null) {
            currentCursor.changeVisible(false);
        }
        if (cursor != null) {
            cursor.changeVisible(true);
        }
        currentCursor = cursor;
    }
    
    public synchronized void setModuleRoot(BranchGroup root) {
        assert(cursorRoot==null);       // This should only be called once
        cursorRoot = new BranchGroup();
        cursorRoot.setName("CursorModule");
        cursorRoot.setCapability( BranchGroup.ALLOW_CHILDREN_EXTEND );
        cursorRoot.setCapability( BranchGroup.ALLOW_CHILDREN_READ );
        cursorRoot.setCapability( BranchGroup.ALLOW_CHILDREN_WRITE );
        
        cursorPosTG = new TransformGroup(); 
        cursorPosTG.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
        cursorPosTG.setCapability( TransformGroup.ALLOW_TRANSFORM_READ );
        
        cursorPosTG.addChild( cursorRoot );
        root.addChild( cursorPosTG );
    }
    
    public synchronized void removeCursor(Cursor3D cursor) {
        if (currentCursor==cursor) {
            logger.severe("Attempt to remove the current cursor");
            return;
        }
        
        logger.fine("Removing cursor "+cursor.getName());
        cursor.detach();
    }
    
    public Vector3f cursorPositionInVworld (Vector3f cursorPos, int awtX, int awtY, 
			  Canvas3D canvas3d, float distance) {
        
        return cursorPositionInVworldInternal(cursorPos, awtX, awtY, canvas3d, distance, cursorDistMargin);
    }
    
    public Vector3f setCursorPosition(Vector3f cursorPos, int awtX, int awtY, 
			  Canvas3D canvas3d, float distance) {
        
	Vector3f v = cursorPositionInVworldInternal(cursorPos, awtX, awtY, canvas3d, distance, cursorDistMargin);
        synchronized (tmpT3d) {
            tmpT3d.set(1f, v);
            cursorPosTG.setTransform(tmpT3d);        
        }
        return v;
    }
    
    private synchronized Vector3f cursorPositionInVworldInternal (Vector3f cursorPos, int awtX, int awtY, 
        		  Canvas3D canvas3d, float distance, float margin) {
        
        if (Float.isNaN(distance) || distance > cursorDistFurthest) {
            // Didn't hit anything or hit something positioned too far
            // -- use the default z value for the cursor position so that
            // the cursor can be seen.
            distance = cursorDistFurthest;
        }
        
        canvas3d.getPixelLocationInImagePlate( awtX, awtY, pixelInIP );
        
        viewInfo.getImagePlateToViewPlatform( canvas3d, ip2vpl, junk );
        viewInfo.getEyeToViewPlatform( canvas3d, eye2vpl, junk );
        
        ip2vpl.transform( pixelInIP );
        // pixelInIp is now pixel in ViewPlatform coords
        
//        System.out.println(eye2vpl);
        Point3d eye = tmpP3d;
        eye.set(0.0, 0.0, 0.0);
        eye2vpl.transform(eye);
        
        cursorPos.x = (float)(pixelInIP.x-eye.x);
        cursorPos.y = (float)(pixelInIP.y-eye.y);
        cursorPos.z = (float)(pixelInIP.z-eye.z);
//        System.out.println(v.length());
//        System.out.println("PixelInIP "+pixelInIP);
//        System.out.println("Distance "+distance);
        
        tmpV3f.set(0f,0f,(float)eye.z); // eye origin
        cursorPos.normalize();
        cursorPos.scaleAdd(distance - margin, tmpV3f);
        
        return cursorPos;
    }
    
    public Vector3f dragPointInVworld(Vector3f dragPoint, 
            int awtX, int awtY, Canvas3D canvas3d, 
            MouseEventNodeInfo draggedNodeInfo, MouseEventNodeInfo pickedNodeInfo) {
        
        float distance = (draggedNodeInfo == null)?(Float.NaN):(draggedNodeInfo.getEyeDistance());
        // get the position without cursor mergin.
        cursorPositionInVworldInternal(dragPoint, awtX, awtY, canvas3d, distance, 0.0f);
        
        return dragPoint;
    }
    
    private void initHideEventHandler() {
        // Listen for handling the hide event
        LgEventConnector.getLgEventConnector().addListener(
            LgEventSource.ALL_SOURCES,
            new LgEventListener() {
                public void processEvent(final LgEvent evt) {
                    currentCursor.setVisible(false);
                    hideCursor = true;
                }
                public Class<LgEvent>[] getTargetEventClasses() {
                    return new Class[] {HideEvent.class};
                }
            });
    }
    
    public static class HideEvent extends LgEvent {
        // just a tag class
    }
}
