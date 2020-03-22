/**
 * Project Looking Glass
 *
 * $RCSfile: OrientedShape3D.java,v $
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
 * $Date: 2005-06-24 19:48:26 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.j3d.j3dwrapper;

import org.jdesktop.lg3d.sg.internal.wrapper.OrientedShape3DWrapper;

/**
 *
 * @author paulby
 */
public class OrientedShape3D extends Shape3D implements OrientedShape3DWrapper {
    
    /** Creates a new instance of OrientedShape3D */
    public OrientedShape3D() {
        super();
    }

    protected void createWrapped() {
        wrapped = new javax.media.j3d.OrientedShape3D();
        wrapped.setUserData( this );
    }
    
    public void getAlignmentAxis(javax.vecmath.Vector3f axis) {
	((javax.media.j3d.OrientedShape3D)wrapped).getAlignmentAxis(axis);
    }

    public int getAlignmentMode() {
	return ((javax.media.j3d.OrientedShape3D)wrapped).getAlignmentMode();
    }

    public boolean getConstantScaleEnable() {
	return ((javax.media.j3d.OrientedShape3D)wrapped).getConstantScaleEnable();
    }

    public void getRotationPoint(javax.vecmath.Point3f point) {
	((javax.media.j3d.OrientedShape3D)wrapped).getRotationPoint(point);
    }

    public float getScale() {
	return (float)((javax.media.j3d.OrientedShape3D)wrapped).getScale();
    }

    public void setAlignmentAxis(javax.vecmath.Vector3f axis) {
	((javax.media.j3d.OrientedShape3D)wrapped).setAlignmentAxis(axis);
    }

    public void setAlignmentAxis(float x, float y, float z) {
	((javax.media.j3d.OrientedShape3D)wrapped).setAlignmentAxis(x,y,z);
    }

    public void setAlignmentMode(int mode) {
	((javax.media.j3d.OrientedShape3D)wrapped).setAlignmentMode(mode);
    }

    public void setConstantScaleEnable(boolean constantScaleEnable) {
	((javax.media.j3d.OrientedShape3D)wrapped).setConstantScaleEnable(constantScaleEnable);
    }

    public void setRotationPoint(javax.vecmath.Point3f point) {
	((javax.media.j3d.OrientedShape3D)wrapped).setRotationPoint(point);
    }

    public void setRotationPoint(float x, float y, float z) {
	((javax.media.j3d.OrientedShape3D)wrapped).setRotationPoint(x,y, z);
    }

    public void setScale(float scale) {
	((javax.media.j3d.OrientedShape3D)wrapped).setScale(scale);
    }
    
}
