/**
 * Project Looking Glass
 *
 * $RCSfile: PickConeWrapper.java,v $
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
 * $Revision: 1.2 $
 * $Date: 2004-06-23 18:51:47 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.wrapper;

import javax.vecmath.*;

/**
 *
 * PickCone is the abstract base class of all cone pick shapes.
 *
 *
 *
 * @since Java 3D 1.2
 *
 */
public interface PickConeWrapper extends PickShapeWrapper {
       
    
    /**
     *
     * Gets the origin of this PickCone.
     *
     * @param origin the Point3d object into which the origin will be copied.
     *
     */    
    public void getOrigin(Point3f origin);
            
    /**
     *
     * Gets the direction of this PickCone.
     *
     * @param direction the Vector3d object into which the direction
     *
     * will be copied.
     *
     */    
    public void getDirection(Vector3f direction);
           
    /**
     *
     * Gets the spread angle of this PickCone.
     *
     * @return the spread angle.
     *
     */   
    public float getSpreadAngle();
    
    
    
    /*     
     * Return true if shape intersect with bounds.     
     * The point of intersection is stored in pickPos.     
     */    
    //boolean intersect(BoundsWrapper bounds, Point4d pickPos);
        
}

