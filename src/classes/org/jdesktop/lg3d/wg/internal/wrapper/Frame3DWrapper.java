/**
 * Project Looking Glass
 *
 * $RCSfile: Frame3DWrapper.java,v $
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
 * $Date: 2005-01-20 22:06:23 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.internal.wrapper;

/**
 *
 * @author  Paul
 */
public interface Frame3DWrapper extends Container3DWrapper {
    
     public void setActive(boolean active);
     
     public boolean getActive();
   
     public void setThumbnail(Component3DWrapper thumbnail);
     
     public Component3DWrapper getThumbnail();
     
     public float getScreenWidth();
     
     public float getScreenHeight();
}
