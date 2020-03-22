/**
 * Project Looking Glass
 *
 * $RCSfile: TransformOpGroup.java,v $
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
 * $Revision: 1.5 $
 * $Date: 2005-06-24 19:48:47 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.internal.j3d.j3dnodes;

import javax.media.j3d.*;
import javax.vecmath.*;


class TransformOpGroup extends TransformGroup {
    private final Transform3D tr3d = new Transform3D();
    private final Vector3f trans = new Vector3f();
    private final AxisAngle4f aa4f = new AxisAngle4f();
    private float scale = 1.0f;
    private Quat4f quat4f = new Quat4f();

    public TransformOpGroup() {
	setCapability(ALLOW_CHILDREN_READ);
	setCapability(ALLOW_CHILDREN_WRITE);
	setCapability(ALLOW_CHILDREN_EXTEND);
	setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        setCapability(ALLOW_LOCAL_TO_VWORLD_READ);
        setCapability(ALLOW_PARENT_READ);
    }

    public void setTranslation(Vector3f pos) {
	trans.set(pos);
	tr3d.set(quat4f, trans, scale);
	setTransform(tr3d);
    }

    public void setTranslation(float x, float y, float z) {
	trans.x = x;
	trans.y = y;
	trans.z = z;
	tr3d.set(quat4f, trans, scale);
	setTransform(tr3d);
    }

    public Vector3f getTranslation() {
	return new Vector3f(trans);
    }

    public void setScale(float scale) {
 	this.scale = scale;
	tr3d.set(quat4f, trans, scale);
	setTransform(tr3d);
    }

    public float getScale() {
	return scale;
    }

    public void setRotationAxis(float x, float y, float z) {
	aa4f.x = x;
	aa4f.y = y;
	aa4f.z = z;
    }

    public void setRotationAxis(Vector3f axis) {
	aa4f.x = axis.x;
	aa4f.y = axis.y;
	aa4f.z = axis.z;
    }

    public void setRotationAngle(float angle) {
	aa4f.angle = angle;
        quat4f.set(aa4f);
	tr3d.set(quat4f, trans, scale);
	setTransform(tr3d);
    }

    public Vector3f getRotationAxis() {
	return new Vector3f(aa4f.x, aa4f.y, aa4f.z);
    }

    public float getRotationAngle() {
	return aa4f.angle;
    }
    
    void get( Quat4f quat, Vector3f v3f ) {
        tr3d.get( quat, v3f );
    }
}


