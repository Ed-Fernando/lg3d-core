/**
 * Project Looking Glass
 *
 * $RCSfile: PickBounds.java,v $
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
 * $Date: 2004-06-23 18:50:40 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.j3d.j3dwrapper;

import org.jdesktop.lg3d.sg.internal.wrapper.PickBoundsWrapper;
import org.jdesktop.lg3d.sg.internal.wrapper.BoundsWrapper;

import javax.vecmath.*;



/**
 * PickBounds is a finite pick shape defined with a Bounds object.  It can
 * be used as an argument to the picking methods in BranchGroup and Locale.
 *
 * @see BranchGroup#pickAll
 * @see Locale#pickAll
 */

public final class PickBounds extends PickShape implements PickBoundsWrapper {
    
    Bounds bounds;
      
    /**
     * Constructs an empty PickBounds.  The bounds object is set to null.
     */
    
    public PickBounds() {
        
        bounds = null;
        wrapped = new javax.media.j3d.PickBounds();
        
    }
    
    
    
    /**
     *
     * Constructs a PickBounds from the specified bounds object.
     *
     * @param boundsObject the bounds of this PickBounds.
     *
     */   
    public PickBounds(BoundsWrapper boundsObject) {        
        bounds = (Bounds)boundsObject;       
        wrapped = new javax.media.j3d.PickBounds( bounds.wrapped );         
    }
    
    
    
    
    
    /**
     *
     * Sets the bounds object of this PickBounds to the specified object.
     *
     * @param boundsObject the new bounds of this PickBounds.
     *
     */
    
    public void set(BoundsWrapper boundsObject) {        
        bounds = (Bounds)boundsObject;       
        ((javax.media.j3d.PickBounds)wrapped).set( bounds.wrapped );       
    }
    
    
    
    /**
     *
     * Gets the bounds object from this PickBounds.
     *
     * @return the bounds.
     *
     */
    
    public BoundsWrapper get() {        
        return bounds;
        
    }
        
    /*
     
     * Return true if shape intersect with bounds.
     
     * The point of intersection is stored in pickPos.
     
     */
    
    //    final boolean intersect(Bounds bounds, Point3f pickPos) {
    
    //	return bounds.intersect(this.bounds, pickPos);
    
    //    }
    
    
    
    //    public Point3f getStartPoint() {
    
    //	return bounds.getCenter();
    
    //    }
    
    
    
}

