/**
 * Project Looking Glass
 *
 * $RCSfile: PickShape.java,v $
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
 * $Date: 2004-06-23 18:50:56 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.rmi.rmiclient;
import javax.vecmath.Point4d;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;

/**
 * An abstract class for describing a pick shape that can be used with
 * the BranchGroup and Locale picking methods.
 *
 * @see BranchGroup#pickAll
 * @see Locale#pickAll
 */
public abstract class PickShape {


    javax.media.j3d.PickShape wrapped;
    
    /**
     * Constructs a PickShape object.
     */
    public PickShape() {
    }


}

