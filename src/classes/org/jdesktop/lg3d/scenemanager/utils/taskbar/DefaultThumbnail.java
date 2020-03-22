/**
 * Project Looking Glass
 *
 * $RCSfile: DefaultThumbnail.java,v $
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
 * $Revision: 1.4 $
 * $Date: 2006-03-11 00:52:44 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.taskbar;

import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.sg.Appearance;
import org.jdesktop.lg3d.sg.Shape3D;
import org.jdesktop.lg3d.utils.shape.FuzzyEdgePanel;
import org.jdesktop.lg3d.utils.shape.GlassyPanel;
import org.jdesktop.lg3d.utils.shape.RectShadow;
import org.jdesktop.lg3d.utils.shape.SimpleAppearance;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Thumbnail;


public class DefaultThumbnail extends Thumbnail {
    private static final float thumbnailScale = 0.13f;
    private static final float bodyDepth = 0.02f * thumbnailScale;
    private static final float shadowN = 0.001f * thumbnailScale;
    private static final float shadowE = 0.0015f * thumbnailScale;
    private static final float shadowS = 0.002f * thumbnailScale;
    private static final float shadowW = 0.001f * thumbnailScale;
    private static final float shadowI = 0.001f * thumbnailScale;
    private static final Appearance[] bodyApps
	= new Appearance[] {
            new SimpleAppearance(0.6f, 1.0f, 1.0f, 1.0f, SimpleAppearance.DISABLE_CULLING),
            new SimpleAppearance(1.0f, 0.6f, 1.0f, 1.0f, SimpleAppearance.DISABLE_CULLING),
            new SimpleAppearance(1.0f, 0.8f, 0.6f, 1.0f, SimpleAppearance.DISABLE_CULLING),
    };
    private static int appIndex = -1;
    
    public DefaultThumbnail(Vector3f sizeHint, String name) {
        float width = sizeHint.x * thumbnailScale;
        float height = sizeHint.y * thumbnailScale;
        if (width < 0.013f) {
            width = 0.013f;
        }
        if (height < 0.01f) {
            height = 0.01f;
        }
        
        if (name != null) {
            appIndex = name.hashCode();
            if (appIndex < 0) appIndex = -appIndex;
        } else {
            appIndex++;
        }
        appIndex %= bodyApps.length;
        
        GlassyPanel thumbnailDeco
            = new GlassyPanel(width, height, bodyDepth, bodyApps[appIndex]);
            
        Shape3D bodyShadow
            = new RectShadow(
                width, height,
                shadowN * 2, shadowE * 2, shadowS * 2, shadowW * 2, 
                shadowI,
                -bodyDepth,
                0.3f);
        
        Appearance app = new SimpleAppearance(1.0f, 1.0f, 1.0f, 0.5f);
        
//        FuzzyEdgePanel body 
//            = new FuzzyEdgePanel(width, height, ???, app);
            
        addChild(thumbnailDeco);
        addChild(bodyShadow);
//        addChild(body);
        
        setPreferredSize(new Vector3f(width, height, bodyDepth));
    }
}


