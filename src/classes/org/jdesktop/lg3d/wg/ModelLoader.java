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
 * $Date: 2007-03-08 09:33:48 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg;

import java.net.URL;
import org.jdesktop.lg3d.wg.internal.wrapper.ModelLoaderWrapper;

import javax.vecmath.Vector3f;
import javax.vecmath.Matrix4f;

/**
 *
 * Provides a mechanism to load geometry etc from a file.
 * Supports many formats using the Java 3D loader mechansim
 *
 * @author  paulby
 */
public class ModelLoader extends Component3D {
    
    /** Creates a new instance of Loader */
    public ModelLoader( String path, String filename, Class loaderClass ) {
        this( path, filename, loaderClass, null );
    }
    
    /** Creates a new instance of Loader */
    public ModelLoader( URL base, URL filename, Class loaderClass ) {
        this( base, filename, loaderClass, null );
    }
    
    /**
     * Create a new ModelLoader node and load the model
     *
     * @param path path to model files
     * @param filename the model file to load
     * @param loaderClass the loader to instantiate to load the model
     * @param matrix static transformation of the model
     */
    public ModelLoader( String path, String filename, Class loaderClass, Matrix4f matrix ) {
        ((ModelLoaderWrapper)wrapped).load( path, filename, loaderClass, matrix );
    }
    
    /**
     * Create a new ModelLoader node and load the model
     *
     * @param base base URL for model files
     * @param filename the model file to load
     * @param loaderClass the loader to instantiate to load the model
     * @param matrix static transformation of the model
     */
    public ModelLoader( URL base, URL filename, Class loaderClass, Matrix4f matrix ) {
        ((ModelLoaderWrapper)wrapped).load( base, filename, loaderClass, matrix );
    }
    
    /**
     * Resize the model so that if fits within this sphere
     */
    public void resize( Vector3f center, float radius ) {
        ((ModelLoaderWrapper)wrapped).resize(center,radius);
    }
    
    public Object getNamedObject( String name ) {
        return ((ModelLoaderWrapper)wrapped).getNamedObject( name );
    }
    
    /**
     * Implementation detail, do not call from user code.
     * May be removed in future.
     */
    protected void createWrapped() {
        wrapped = instantiate("ModelLoader");
    }
}
