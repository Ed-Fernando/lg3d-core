/**
 * Project Looking Glass
 *
 * $RCSfile: Spring.java,v $
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
 * $Date: 2005-10-14 21:03:14 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.springdamper;

/**
 *
 * @author paulby
 */
public class Spring {
    
    SprungComponentInterface comp1;
    SprungComponentInterface comp2;
    float k;            // Tensile Spring constant nm
    float d;            // Damping Force
    float restLength;   // Rest length of spring (meters)
    
    /** Creates a new instance of Spring */
    public Spring(SprungComponentInterface comp1, SprungComponentInterface comp2, float restLength) {
        this.comp1 = comp1;
        this.comp2 = comp2;
        this.restLength = restLength;
        
        k=0.5f;
        d=0.9f;
        
        SpringProcessor.getSpringProcessor().addSpring(this);
    }
    
}
