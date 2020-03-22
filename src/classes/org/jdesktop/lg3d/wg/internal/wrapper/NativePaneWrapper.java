/**
 * Project Looking Glass
 *
 * $RCSfile: NativePaneWrapper.java,v $
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
 * $Date: 2007-04-10 22:58:46 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.internal.wrapper;

/**
 *
 * @author  Paul
 */
public interface NativePaneWrapper extends Component3DWrapper {
    
    public void setWid( long wid );
    
    public long getWid() ;
    
    public void setNativeWidth( int width );
    
    public int getNativeWidth();
    
    public void setNativeHeight( int height );
    
    public int getNativeHeight();

    public void setWidth( float width );
    
    public float getWidth();

    public void setHeight( float height );
    
    public float getHeight();

    public boolean isRemwin ();

    public void setIsRemwin (boolean isRemwin);
}
