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
 * $Date: 2005-06-24 19:48:29 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.rmi.rmiclient;

import org.jdesktop.lg3d.sg.internal.rmi.rmiserver.OrientedShape3DRemote;
import org.jdesktop.lg3d.sg.internal.wrapper.OrientedShape3DWrapper;

/**
 *
 * @author paulby
 */
public class OrientedShape3D extends Shape3D implements OrientedShape3DWrapper {
    
    /** Creates a new instance of OrientedShape3D */
    public OrientedShape3D() {
        try {
            remote = SceneGraphSetup.getSGObjectFactory().newInstance(org.jdesktop.lg3d.sg.internal.rmi.rmiserver.OrientedShape3D.class);
            setRemote( remote );
        } catch( java.rmi.RemoteException re ) {
            throw new RuntimeException(re);
        }
    }
    
    public void getAlignmentAxis(javax.vecmath.Vector3f axis) {
        try {
            ((OrientedShape3DRemote)remote).getAlignmentAxis(axis);
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }

    public int getAlignmentMode() {
	try {
            return ((OrientedShape3DRemote)remote).getAlignmentMode();
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }

    public boolean getConstantScaleEnable() {
	try {
            return ((OrientedShape3DRemote)remote).getConstantScaleEnable();
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }

    public void getRotationPoint(javax.vecmath.Point3f point) {
	try {
            ((OrientedShape3DRemote)remote).getRotationPoint(point);
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }

    public float getScale() {
	try {
            return (float)((OrientedShape3DRemote)remote).getScale();
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }

    public void setAlignmentAxis(javax.vecmath.Vector3f axis) {
	try {
            ((OrientedShape3DRemote)remote).setAlignmentAxis(axis);
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }

    public void setAlignmentAxis(float x, float y, float z) {
	try {
            ((OrientedShape3DRemote)remote).setAlignmentAxis(x,y,z);
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }

    public void setAlignmentMode(int mode) {
	try {
            ((OrientedShape3DRemote)remote).setAlignmentMode(mode);
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }

    public void setConstantScaleEnable(boolean constantScaleEnable) {
	try {
            ((OrientedShape3DRemote)remote).setConstantScaleEnable(constantScaleEnable);
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }

    public void setRotationPoint(javax.vecmath.Point3f point) {
	try {
            ((OrientedShape3DRemote)remote).setRotationPoint(point);
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }

    public void setRotationPoint(float x, float y, float z) {
	try {
            ((OrientedShape3DRemote)remote).setRotationPoint(x,y, z);
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }

    public void setScale(float scale) {
	try {
            ((OrientedShape3DRemote)remote).setScale(scale);
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }
    
}
