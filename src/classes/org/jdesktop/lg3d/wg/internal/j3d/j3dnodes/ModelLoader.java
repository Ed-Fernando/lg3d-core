/**
 * Project Looking Glass
 *
 * $RCSfile: ModelLoader.java,v $
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
 * $Date: 2007-03-08 09:33:49 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.internal.j3d.j3dnodes;

import java.util.logging.Level;
import java.io.File;
import com.sun.j3d.loaders.Loader;
import com.sun.j3d.loaders.Scene;
import java.net.URL;
import javax.vecmath.Vector3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Matrix3f;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;

import java.util.logging.Logger;
/**
 *
 * @author  paulby
 */
public class ModelLoader extends J3dComponent3D {
    
    private Logger logger = Logger.getLogger("lg.wg");
    private TransformGroup tg;
    private Scene scene;
        
    public void load( String path, String filename, Class loaderClass, 
                      Matrix4f matrix ) {
        logger.info("ModelLoader "+loaderClass);
        
        Loader j3dLoader;
        
        try {
            j3dLoader = (Loader)loaderClass.newInstance();
        } catch( Exception e ) {
            throw new RuntimeException("Unable to instantiate loader " + e);
        }
        
        j3dLoader.setBasePath( path );
        
        try {
            scene = j3dLoader.load(path + File.separatorChar + filename);
        } catch( Exception ex ) {
            logger.log(Level.WARNING, "Loader failed: " + ex, ex);
            return;
        }

        tg = new TransformGroup();
        if (matrix!=null) {
            Transform3D t3d = new Transform3D( matrix );
            tg.setTransform(t3d);
        }
        tg.addChild( scene.getSceneGroup() );
        this.addChild( tg );
        
        setCapabilities();
        
        logger.info("Added Scene ");    
    }
    
    public void load( URL baseURL, URL filename, Class loaderClass, 
                      Matrix4f matrix ) {
        logger.info("ModelLoader "+loaderClass);
        
        Loader j3dLoader;
        
        try {
            j3dLoader = (Loader)loaderClass.newInstance();
        } catch( Exception e ) {
            throw new RuntimeException("Unable to instantiate loader " + e);
        }
        
        j3dLoader.setBaseUrl( baseURL );
        
        try {
            scene = j3dLoader.load(filename);
        } catch( Exception ex ) {
            logger.log(Level.WARNING, "Loader failed: " + ex, ex);
            return;
        }

        tg = new TransformGroup();
        if (matrix!=null) {
            Transform3D t3d = new Transform3D( matrix );
            tg.setTransform(t3d);
        }
        tg.addChild( scene.getSceneGroup() );
        this.addChild( tg );
        
        setCapabilities();
        
        logger.info("Added Scene ");    
    }
    
    public Object getNamedObject(String name) {
        Object namedObject = null;
        if ( scene.getNamedObjects() != null ) {
            namedObject = scene.getNamedObjects().get(name);
        }
        return namedObject;
    }
    
    public void resize( Vector3f center, float radius ) {
        BoundingSphere bounds;
        
        Bounds b = getBounds();
        if (b instanceof BoundingSphere)
            bounds = (BoundingSphere)b;
        else {
            logger.warning("Resize failed, bounds not Sphere");
            return;
        }
        
        logger.finer("============= Original Bounds "+bounds);
        
        Matrix3f m3f = new Matrix3f();
        m3f.setIdentity();
        Transform3D t3d = new Transform3D(m3f, center, (float)(radius/bounds.getRadius()));
        tg.setTransform(t3d);
        
        logger.finer("============= New Bounds "+getBounds());
            
    }
}
