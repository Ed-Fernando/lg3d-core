/**
 * Project Looking Glass
 *
 * $RCSfile: ThumbnailLayout.java,v $
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
 * $Revision: 1.8 $
 * $Date: 2006-09-26 23:13:44 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.taskbar;

import java.util.ArrayList;
import java.util.HashMap;
import javax.vecmath.Vector3f;
import org.jdesktop.lg3d.scenemanager.utils.appcontainer.NaturalMotionF3DAnimationFactory;
import org.jdesktop.lg3d.utils.action.ActionBoolean;
import org.jdesktop.lg3d.utils.action.ActionNoArg;
import org.jdesktop.lg3d.utils.eventadapter.Component3DHighlightEventAdapter;
import org.jdesktop.lg3d.utils.eventadapter.MouseClickedEventAdapter;
import org.jdesktop.lg3d.utils.eventadapter.MouseEnteredEventAdapter;
import org.jdesktop.lg3d.scenemanager.utils.taskbar.HorizontalLayout.AlignmentType;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Container3D;
import org.jdesktop.lg3d.wg.Cursor3D;
import org.jdesktop.lg3d.wg.Frame3D;
import org.jdesktop.lg3d.wg.Thumbnail;
import org.jdesktop.lg3d.wg.event.Component3DToFrontEvent;
import org.jdesktop.lg3d.wg.event.Component3DVisualAppearanceEvent;
import org.jdesktop.lg3d.wg.event.Component3DVisualAppearanceEvent.VisualAppearanceType;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.MouseEvent3D.ButtonId;


/**
 * Layout manager for thumbnails on the taskbar
 */
public class ThumbnailLayout extends HorizontalReorderableLayout {
    private float tiltAngle;
    private float tiltAngleCos;
    private float tiltAngleSin;
    // FIXME -- it is inefficient to have one more HashMap, 
    // but works for now...
    private HashMap<Component3D, AuxInfo> managedComps 
        = new HashMap<Component3D, AuxInfo>();
    private Component3DHighlightEventAdapter highlightAdapter;
    private MouseEnteredEventAdapter enteredAdapter;
    private MouseClickedEventAdapter clickedAdapter;
    private MouseClickedEventAdapter clicked3Adapter;
    private Vector3f tmpV3f = new Vector3f();
    private boolean inFadingMode = false;
    
    
    public ThumbnailLayout(AlignmentType policy, float spacing, float tiltAngle, 
        Component3D topCompForFadingDeactivation) 
    {
        super(policy, spacing, new NaturalMotionF3DAnimationFactory(200));
        
        this.tiltAngle = tiltAngle;
        this.tiltAngleCos = (float)Math.abs(Math.cos(tiltAngle));
        this.tiltAngleSin = (float)Math.abs(Math.sin(tiltAngle));
        
        highlightAdapter = new Component3DHighlightEventAdapter(
            new ActionBoolean() {
                public void performAction(LgEventSource source, boolean flag) {
                    assert(source instanceof Thumbnail);
                    Thumbnail tn = (Thumbnail)source;
                    highlightThumbnail(tn, flag);
                }
            });
        
        enteredAdapter = new MouseEnteredEventAdapter(
            new ActionBoolean() {
                public void performAction(LgEventSource source, 
                    boolean flag) 
                {
                    assert(source instanceof Thumbnail);
                    Thumbnail tn = (Thumbnail)source;
                    VisualAppearanceType appearance 
                        = (flag)?(VisualAppearanceType.HIGHLIGHT):(VisualAppearanceType.NORMAL);
                    tn.postEvent(new Component3DVisualAppearanceEvent(appearance));
                    Frame3D f3d = tn.getBody();
                    appearance 
                        = (flag)?(VisualAppearanceType.HIGHLIGHT):(VisualAppearanceType.LOWLIGHT);
                    assert(f3d != null);
                    if (f3d.isFinalVisible()) { 
                        f3d.postEvent(new Component3DVisualAppearanceEvent(appearance));
                    }
                    enterIntoFadingMode(f3d);
               }
            });
            
        clickedAdapter = new MouseClickedEventAdapter(
            new ActionNoArg() {
                public void performAction(LgEventSource source) {
                    assert(source instanceof Thumbnail);
                    Thumbnail tn = (Thumbnail)source;
                    Frame3D f3d = tn.getBody();
                    assert(f3d != null);
                    f3d.changeVisible(true);
                    f3d.postEvent(new Component3DToFrontEvent());
                }
            });
            
        clicked3Adapter = new MouseClickedEventAdapter(ButtonId.BUTTON3,
            new ActionNoArg() {
                public void performAction(LgEventSource source) {
                    assert(source instanceof Thumbnail);
                    Thumbnail tn = (Thumbnail)source;
                    Frame3D f3d = tn.getBody();
                    assert(f3d != null);
                    f3d.changeEnabled(false);
                }
            });
            
        topCompForFadingDeactivation.addListener(
            new MouseEnteredEventAdapter(
                new ActionBoolean() {
                    public void performAction(LgEventSource source, 
                        boolean flag) 
                    {
                        if (!flag) { // mouse exit
                            exitFromFadingMode();
                        }
                    }
               }));
    }
    
    private void enterIntoFadingMode(Frame3D exclude) {
        if (inFadingMode) {
            return;
        }
        inFadingMode = true;
        
        for (Component3D comp : getManagedComponents()) {
            Thumbnail tn = (Thumbnail)comp;
            Frame3D f3d = tn.getBody();
            assert(f3d != null);
            if (f3d.isFinalVisible() && f3d != exclude) { 
                f3d.postEvent(
                    new Component3DVisualAppearanceEvent(
                        VisualAppearanceType.LOWLIGHT));
            }
        }
    }
    
    private void exitFromFadingMode() {
        if (!inFadingMode) {
            return;
        }
        inFadingMode = false;
        
        for (Component3D comp : getManagedComponents()) {
            Thumbnail tn = (Thumbnail)comp;
            Frame3D f3d = tn.getBody();
            assert(f3d != null);
            if (f3d.isFinalVisible()) { 
                f3d.postEvent(
                    new Component3DVisualAppearanceEvent(
                        VisualAppearanceType.NORMAL));
            }
        }
    }
    
    private void highlightThumbnail(Thumbnail tn, boolean flag) {
        AuxInfo ai;
        
        // The removal of the listeners in RemoveLayoutComponent is asychronous
        // so we could be called by a listener even through tn has been removed
        // from managedComp.
        synchronized(managedComps) {
            ai = managedComps.get(tn);
        }
        if (ai==null)
            return;
        
        if (flag) {
            tn.changeScale(1.2f);
            tn.changeRotationAngle(0.0f);
            tn.changeTranslation(ai.xPosHighlight, ai.yPosHighlight, ai.zPosHighlight);
        } else {
            tn.changeScale(1.0f);
            tn.changeRotationAngle(tiltAngle);
            tn.changeTranslation(ai.xPosNormal, ai.yPosNormal, ai.zPosNormal);
        }
    }
    
    public void layoutContainer() {
        Container3D cont = getTargetContainer();
        AlignmentType policy = getAlignmentPolicy();
        float spacing = getSpacing();
        ArrayList<Component3D> compList = getManagedComponents();
        Component3D movingComp = getComponentToSkipLayout();
        
        cont.getPreferredSize(tmpV3f);
	float width = tmpV3f.x;
	float height = tmpV3f.y;
	float depth = tmpV3f.z;

	float x = 0.0f;
	float y = height * -0.5f;
	float z = depth * 0.3f;// FIXME -- should be 0.5f but doesn't look good
        
	switch (policy) {
	    case CENTER: {
		x = getTotalWidth(cont, compList, spacing) * -0.5f; 
		break;
	    }
	    case LEFT: {
		x = width * -0.5f + spacing;
		break;
	    }
	    case RIGHT: {
		x = width * 0.5f + spacing - getTotalWidth(cont, compList, spacing);
		break;
	    }
	}
        
        for (Component3D comp : compList) {
            comp.getPreferredSize(tmpV3f);
            float cw = tmpV3f.x * 0.5f;
            float ch = tmpV3f.y * 0.5f;
            float w = cw * tiltAngleCos;
            float cy = y + ch;
            float cz = z - cw * tiltAngleSin;
            
	    x += w;
            if (comp != movingComp) {
                // don't try to arrange the component being moved manually
                comp.changeTranslation(x, cy, cz);
            }
            
            AuxInfo ai = managedComps.get(comp);
            ai.xPosNormal = x;
            ai.xPosHighlight = x;
            ai.yPosNormal = cy;
            ai.yPosHighlight = cy + depth * 0.1f;
            ai.zPosNormal = cz;
            ai.zPosHighlight = cz + depth * 0.5f;
            
            x += w + spacing;
	}
    }

    private float getTotalWidth(Container3D cont, 
        ArrayList<Component3D> compList, float spacing) 
    {
	float totalWidth = spacing;
        
        for (Component3D comp : compList) {
	    totalWidth += comp.getPreferredSize(tmpV3f).x * tiltAngleCos + spacing;
	}
	return totalWidth;
    }
    
    public void addLayoutComponent(Component3D comp, Object constraints) {
        super.addLayoutComponent(comp, constraints);
        
        if (!(comp instanceof Thumbnail)) {
            throw new IllegalArgumentException(
                    "this layout only supports Thumbnail");
        }
        
        comp.addListener(highlightAdapter);
        comp.addListener(enteredAdapter);
        comp.addListener(clickedAdapter);
        comp.addListener(clicked3Adapter);
        
        AuxInfo ai = new AuxInfo();
        managedComps.put(comp, ai);
        
//        comp.setTranslation(1.0f, 0.0f, 0.0f);
        comp.setScale(1.0f);
        comp.setRotationAxis(0.0f, 1.0f, 0.0f);
        comp.setRotationAngle(tiltAngle);
        comp.setCursor(Cursor3D.SMALL_CURSOR);
    }
    
    public void removeLayoutComponent(Component3D comp) {
        super.removeLayoutComponent(comp);
        
        comp.removeListener(highlightAdapter);
        comp.removeListener(enteredAdapter);
        comp.removeListener(clickedAdapter);
        comp.removeListener(clicked3Adapter);
        
        synchronized(managedComps) {
            AuxInfo ai = managedComps.remove(comp);
        }
    }
    
    private static class AuxInfo {
        private float xPosNormal = Float.NaN;
        private float xPosHighlight = Float.NaN;
        private float yPosNormal = Float.NaN;
        private float yPosHighlight = Float.NaN;
        private float zPosNormal = Float.NaN;
        private float zPosHighlight = Float.NaN;
    }
}

