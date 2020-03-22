/**
 * Project Looking Glass
 *
 * $RCSfile: OdePhysics.java,v $
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
 * $Revision: 1.3 $
 * $Date: 2005-04-14 23:05:16 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.wg.internal.j3d.j3dnodes;

import javax.media.j3d.Transform3D;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import org.odejava.display.DisplayTransformable;
import org.odejava.display.BoundDisplayObject;
import org.odejava.Body;
import org.odejava.GeomBox;
import org.odejava.PlaceableGeom;
import org.jdesktop.lg3d.wg.internal.j3d.j3dnodes.*;

/**
 *
 * @author  paulby
 */
public class OdePhysics extends PhysicsData implements DisplayTransformable {
    
    private J3dComponent3D c3d;
    private Transform3D t3d = new Transform3D();
    private Body body;
    private OdeEngine engine;
    private PlaceableGeom geom;
    
    /** Creates a new instance of OdePhysics */
    OdePhysics(J3dComponent3D component3D) {
        this.c3d = component3D;
        engine = OdeEngine.getEngine();
        body = new Body( "C3D_"+component3D.toString(), engine.getWorld(), geom );
    }
    
    /**
     * Called by Ode to set the Transform for this object
     */
    public void setTransform( javax.vecmath.Vector3f position,
                              javax.vecmath.Quat4f quaternion) {
        t3d.set(quaternion);
        t3d.setTranslation(position);

        c3d.setTransform(t3d);        
    }
    
    /**
     * Called by Component3D when the component is moved
     */
    void set( TransformOpGroup trans ) {
        Quat4f quat = new Quat4f();
        Vector3f v3f = new Vector3f();
        
        trans.get( quat, v3f );
        
        geom.setQuaternion( quat );
        geom.setPosition( v3f );
    }
    
    void setBoxSize( float width, float height, float depth ) {
        if (geom!=null)
            throw new RuntimeException("Size change not supported yet");
        geom = new GeomBox( width, height, depth ); 
        body.addGeom( geom );
        engine.add( new BoundDisplayObject( this, geom ));
        body.adjustMass(1f);
    }
}
