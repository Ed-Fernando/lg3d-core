/***************************************************************************
 *   Project Looking Glass
 *
 *   $RCSfile: OrbitLayout.java,v $
 *   
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *   
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *   
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the
 *   Free Software Foundation, Inc.,
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *   
 *   Author: Colin M. Bullock
 *   cmbullock@gmail.com
 *   
 *   $Revision: 1.1 $
 *   $Date: 2005-12-02 17:06:43 $
 */
package org.jdesktop.lg3d.scenemanager.utils.startmenu.model.orbit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.vecmath.Vector3f;

import org.jdesktop.lg3d.utils.c3danimation.Component3DAnimationFactory;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.Component3DAnimation;
import org.jdesktop.lg3d.wg.Container3D;
import org.jdesktop.lg3d.wg.LayoutManager3D;
import org.jdesktop.lg3d.wg.event.Component3DParkedEvent;

/**
 * This Layout Manager arranges components in an orbit around the origin 
 * of the container. Currently, components are layed out only in a
 * circular orbit, but a future revision should support elliptic orbits
 * as well. The <code>inclineAngle</code> and <code>offsetAngle</code>
 * parameters allows the control of rotation in two of the three planes
 * (respectively, the xy-plane and the xz-plane). 
 */
public class OrbitLayout implements LayoutManager3D {
    
    private static Logger logger = Logger.getLogger("lg.orbitlayout");
    
    private static final int defaultAnimDuration= 800;
    
    /** The container being laid out */
    private Container3D cont;
    
    /** The list of children components */
    private List<Component3D> compList= new ArrayList<Component3D>();
    
    /** Storage of previous info for each child component */
    private Map<Component3D, PrevCompInfo> prevCompInfoMap= new HashMap<Component3D, PrevCompInfo>();
    
    // TODO: Default incline angle should probably be 0
    /** The radians of rotation ccw in the xy-plane */
    private float inclineAngle= (float)Math.PI / 8;
    
    // TODO: Default offset angle should probably be 0
    /** The radians of rotation ccw on the xz-plane */
    private float offsetAngle= (float)Math.PI / 6;
    
    /** The ratio of the radius of orbit to the width of the container */
    private float radiusScale= 0.5f;
    
    /** The axis for rotation of child components about the origin */
    private Vector3f rotationAxis= new Vector3f();
    
    /** The component animation factory */
    private Component3DAnimationFactory compAnimFactory;
    
    private Vector3f tmpV3f= new Vector3f();
    
    /**
     * Default constructor. This creates an orbit layout with a 60% radius,
     * a 22.5&deg; incline and a 30&deg; rotation offset.
     */
    // TODO: Default offset and incline should be 0 deg
    public OrbitLayout() {
    }
    
    /**
     * Create an orbit layout with the specified radius scale and the
     * rest of the geometry taken from the defaults.
     * @param radiusScale The radius-to-container-width ratio
     */
    public OrbitLayout(float radiusScale) {
        this.radiusScale= radiusScale;
    }
    
    /**
     * Create a new orbit layout with the specified geometry
     * @param radiusScale The radius-to-container-width ratio
     * @param inclineAngle The xy-plane rotation
     * @param offsetAngle The xz-plane rotation
     */
    public OrbitLayout(float radiusScale, float inclineAngle, float offsetAngle) {
        this.inclineAngle= inclineAngle;
        this.radiusScale= radiusScale;
        this.offsetAngle= offsetAngle;
    }
    
    /**
     * Create a new orbit layout with the specified geometry and 
     * the provided animation factory
     * @param radiusScale The radius-to-container-width ratio
     * @param inclineAngle The xy-plane rotation
     * @param offsetAngle The xz-plane rotation
     * @param compAnimFactory The animation factory
     */
    public OrbitLayout(float radiusScale, float inclineAngle, float offsetAngle,
            Component3DAnimationFactory compAnimFactory) {
        this(radiusScale, inclineAngle, offsetAngle); 
        this.compAnimFactory= compAnimFactory;
    }

    /**
     * Set the container being laid out
     * @param cont The container to layout
     * @see org.jdesktop.lg3d.wg.LayoutManager3D#setContainer(org.jdesktop.lg3d.wg.Container3D)
     */
    public void setContainer(Container3D cont) {
        this.cont= cont;
    }

    /**
     * Layout a container in an orbit based on the specified geometry.
     * Each component is positioned evenly [ 2n&pi;/N units ] around a
     * circle of given radius centered at (0,0,0). Each component is
     * positioned at an altitude angle determined by the position
     * of the component and the <code>inclineAngle</code>, resulting
     * in 'tilt' and 'offset-rotation' to the orbit. This method also sets
     * the correct rotation axis for the components to spin about
     * the center. This calculates the second point defining the
     * line through (0,0,0) that is perpedicular to the plane of orbit.
     * @see org.jdesktop.lg3d.wg.LayoutManager3D#layoutContainer()
     */
    // TODO: This should be parameterized to handle elliptic as well as circular orbits
    public void layoutContainer() {
        rotationAxis= new Vector3f((float)(Math.sin(-inclineAngle) * Math.cos(offsetAngle)), 
                (float)Math.cos(inclineAngle), 
                (float)(Math.sin(offsetAngle) * Math.sin(inclineAngle)));
        cont.setRotationAxis(rotationAxis.x, rotationAxis.y, rotationAxis.z);
        
        int totalCompCount= compList.size();
        int currentCompCount= 0;
        logger.log(Level.FINER, "Laying out " + totalCompCount + " components...");
        float radius= radiusScale * cont.getPreferredSize(tmpV3f).x * 0.5f;
        float altitude= radius * (float)Math.tan(inclineAngle);
        for(Component3D comp : compList) {
            float radians= ((float)(2 * currentCompCount * Math.PI) / totalCompCount);
            
            tmpV3f.x= radius * (float)Math.cos(radians);
            tmpV3f.y= altitude * (float)Math.cos(radians + offsetAngle);
            tmpV3f.z= radius * (float)Math.sin(radians);
            
            logger.log(Level.FINEST, "Positioning component " + currentCompCount + " at " + "(" + tmpV3f.x + "," + tmpV3f.y + "," + tmpV3f.z + ")");
            comp.changeTranslation(tmpV3f, defaultAnimDuration);
            comp.setRotationAxis(rotationAxis.x, rotationAxis.y, rotationAxis.z);
            currentCompCount++;
        }
    }

    /**
     * Add a component to the layout. Adding a component causes the spacing
     * of all the components to be recalculated.
     * @param comp The component to add
     * @param constraints An optional <code>Integer</code> specifying the postion to add the component
     * @see org.jdesktop.lg3d.wg.LayoutManager3D#addLayoutComponent(org.jdesktop.lg3d.wg.Component3D, java.lang.Object)
     */
    public void addLayoutComponent(Component3D comp, Object constraints) {
        PrevCompInfo prevCompInfo= new PrevCompInfo();
        comp.getFinalTranslation(tmpV3f);
        prevCompInfo.prevX= tmpV3f.x;
        prevCompInfo.prevY= tmpV3f.y;
        prevCompInfo.prevZ= tmpV3f.z;
        comp.getRotationAxis(prevCompInfo.prevRotationAxis);
        if(compAnimFactory != null) {
            prevCompInfo.prevAnim= comp.getAnimation();
            comp.setAnimation(compAnimFactory.createInstance());
        }
        prevCompInfoMap.put(comp, prevCompInfo);
        
        int totalSize= compList.size();
        int index= totalSize;
        if(constraints != null && constraints instanceof Integer) {
            index= (Integer)constraints;
            if(index >= 0 && index < totalSize) {
                compList.add(index, comp);
            } else {
                index= totalSize;
                compList.add(comp);
            }
        } else {
            compList.add(comp);
        }
        
        comp.postEvent(new Component3DParkedEvent(true));
    }

    /**
     * Remove a component from the layout. The spacing of the remaining components
     * is recalculated after removal.
     * @param comp The component to remove
     * @see org.jdesktop.lg3d.wg.LayoutManager3D#removeLayoutComponent(org.jdesktop.lg3d.wg.Component3D)
     */
    public void removeLayoutComponent(Component3D comp) {
        compList.remove(comp);
        PrevCompInfo compInfo= prevCompInfoMap.get(comp);
        assert(compInfo != null);
        
        if(compAnimFactory != null) {
            comp.setAnimation(compInfo.prevAnim);
        }
        
        comp.setRotationAxis(compInfo.prevRotationAxis.x, compInfo.prevRotationAxis.y, compInfo.prevRotationAxis.z);
        comp.changeTranslation(compInfo.prevX, compInfo.prevY, compInfo.prevZ, defaultAnimDuration);
        comp.postEvent(new Component3DParkedEvent(false));
    }

    /**
     * Rearrange a component in the container. Currently this is an empty implementation
     * that does not cause a call to <code>layoutContainer()</code>.
     * @see org.jdesktop.lg3d.wg.LayoutManager3D#rearrangeLayoutComponent(org.jdesktop.lg3d.wg.Component3D, java.lang.Object)
     */
    public boolean rearrangeLayoutComponent(Component3D comp, Object newConstraints) {
        logger.log(Level.FINER, "");
        return false;
    }
    
    /**
     * Private utility class used by orbit layout to remember a components
     * previous information before addition to the container. This
     * allows a component to be replaced in its original position
     * (and with the correct animation, should the orbit layout use
     * a custom animation factory) after it is removed from the container.
     */
    private static class PrevCompInfo {
        
        float prevX= Float.NaN;
        
        float prevY= Float.NaN;
        
        float prevZ= Float.NaN;
        
        Vector3f prevRotationAxis= new Vector3f(0.0f, 0.0f, 0.0f);
        
        Component3DAnimation prevAnim= null;
        
    }
    
}
