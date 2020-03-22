/**
 * Project Looking Glass
 *
 * $RCSfile: J3dNativeWindowImageComponent2D.java,v $
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
 * $Date: 2004-09-10 23:29:44 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.nativewindow;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

/**
 *
 * @author  paulby
 */
public class J3dNativeWindowImageComponent2D extends javax.media.j3d.ImageComponent2D {
    
    private Object userData2;
    
    /** Creates a new instance of J3dNativeWindowImageComponent2D */
    public J3dNativeWindowImageComponent2D(int		format,
                                           int		width,
                                           int		height) {
        super(format,width,height);
    }
    
    public J3dNativeWindowImageComponent2D(int format,
                                           RenderedImage image,
                                           boolean byReference,
                                           boolean yUp ) {
        super(format,image,byReference,yUp);
    }

    public J3dNativeWindowImageComponent2D(int format,
                                           int width, 
                                           int height,
                                           boolean byReference,
                                           boolean yUp ) {
        super( format,width,height,byReference,yUp );
    }
    
    public void setUserData2( Object userData2 ) {
        this.userData2 = userData2;
    }
    
    public Object getUserData2() {
        return userData2;
    }
    
}
