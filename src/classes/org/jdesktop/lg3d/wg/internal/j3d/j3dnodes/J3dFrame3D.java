/**
 * Project Looking Glass
 *
 * $RCSfile: J3dFrame3D.java,v $
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
 * $Date: 2005-06-24 19:48:46 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.internal.j3d.j3dnodes;

import java.lang.reflect.Constructor;
import java.util.logging.Level;
import org.jdesktop.lg3d.displayserver.fws.FoundationWinSys;
import javax.media.j3d.Canvas3D;

/**
 * Component3D object is a top level object for widget development.
 *
 * It supports visual rendering by adding scene graph node children 
 * and event handling.
 */
public class J3dFrame3D extends J3dContainer3D {
    
    private J3dComponent3D thumbnail;
    
    public J3dFrame3D() {
        if (System.getProperty("lg.physics")!=null) {
            //physicsData = new OdePhysics(this);
            try {
                Class c = Class.forName("org.jdesktop.lg3d.wg.internal.j3d.j3dnodes.OdePhysics");
                Constructor con = c.getConstructor(new Class[] {J3dComponent3D.class});
                physicsData = (PhysicsData)con.newInstance(new Object[] {this});
            } catch(Exception e) {
                logger.log(Level.SEVERE, "Unable to instantiate physics system : "+e.getMessage(), e);
            }
        }
    }
    
    public void setThumbnail(J3dComponent3D thumbnail) {
        this.thumbnail = thumbnail;
    }
    
    public J3dComponent3D getThumbnail() {
        return thumbnail;
    }
    
    public float getScreenWidth() {
        Canvas3D canvas3d
            = FoundationWinSys.getFoundationWinSys().getCanvas(0);
        return (float)canvas3d.getPhysicalWidth();
    }
    
    public float getScreenHeight() {
        Canvas3D canvas3d 
            = FoundationWinSys.getFoundationWinSys().getCanvas(0);
        return (float)canvas3d.getPhysicalHeight();
    }
}

