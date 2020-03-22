/**
 * Project Looking Glass
 *
 * $RCSfile: ModelLoaderWrapper.java,v $
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
 * $Revision: 1.4 $
 * $Date: 2007-03-08 09:33:51 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.internal.wrapper;

import java.net.URL;
import javax.vecmath.Vector3f;
import javax.vecmath.Matrix4f;

/**
 *
 * @author  paulby
 */
public interface ModelLoaderWrapper {
    public void load( String path, String filename, Class loaderClass, Matrix4f matrix );
    
    public void load( URL base, URL filename, Class loaderClass, Matrix4f matrix );
    
    public void resize( Vector3f center, float radius );
    
    public Object getNamedObject( String name );
}
