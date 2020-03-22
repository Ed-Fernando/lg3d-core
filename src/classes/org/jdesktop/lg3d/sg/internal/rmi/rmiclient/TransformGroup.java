/**
 * Project Looking Glass
 *
 * $RCSfile: TransformGroup.java,v $
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
 * $Date: 2004-06-23 18:50:59 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.rmi.rmiclient;

import org.jdesktop.lg3d.sg.internal.wrapper.TransformGroupWrapper;
import org.jdesktop.lg3d.sg.internal.wrapper.Transform3DWrapper;
import org.jdesktop.lg3d.sg.internal.rmi.rmiserver.TransformGroupRemote;
import org.jdesktop.lg3d.sg.internal.rmi.rmiserver.Transform3DRemote;

/**
 * Group node that contains a transform. The TransformGroup node 
 * specifies a single spatial transformation, via a Transform3D
 * object, that can position, orient, and scale all of its children.
 * <P>
 * The specified transformation must be affine. Further, if the 
 * TransformGroup node is used as an ancestor of a ViewPlatform node 
 * in the scene graph, the transformation must be congruent-only 
 * rotations, translations, and uniform scales are allowed in
 * a direct path from a Locale to a ViewPlatform node.
 * <P>
 * Note: Even though arbitrary affine transformations are
 * allowed, better performance will result if all matrices
 * within a branch graph are congruent, containing only rotations
 * translation, and uniform scale.
 * <P>
 * The effects of transformations in the scene graph are cumulative. 
 * The concatenation of the transformations of each TransformGroup in 
 * a direct path from the Locale to a Leaf node defines a composite 
 * model transformation (CMT) that takes points in that Leaf node's 
 * local coordinates and transforms them into Virtual World (Vworld) 
 * coordinates. This composite transformation is used to
 * transform points, normals, and distances into Vworld coordinates. 
 * Points are transformed by the CMT. Normals are transformed by the 
 * inverse-transpose of the CMT. Distances are transformed by the scale 
 * of the CMT. In the case of a transformation containing a nonuniform 
 * scale or shear, the maximum scale value in
 * any direction is used. This ensures, for example, that a transformed 
 * bounding sphere, which is specified as a point and a radius, 
 * continues to enclose all objects that are also transformed using 
 * a nonuniform scale.
 * <P>
 */

public class TransformGroup extends Group implements TransformGroupWrapper {

    /**
     * Constructs and initializes a TransformGroup using an
     * identity transform.
     */
    public TransformGroup() {
    }
    
    protected void createRemote() {
        try {
            remote = SceneGraphSetup.getSGObjectFactory().newInstance(org.jdesktop.lg3d.sg.internal.rmi.rmiserver.TransformGroup.class);
            setRemote( remote );
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }

    /**
     * Constructs and initializes a TransformGroup from
     * the Transform passed.
     * @param t1 the transform3D object
     * @exception BadTransformException if the transform is not affine.
     */
    public TransformGroup(Transform3DWrapper t1) {
        setTransform(t1);
    }

    /**
     * Sets the transform component of this TransformGroup to the value of
     * the passed transform.
     * @param t1 the transform to be copied
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     * @exception BadTransformException if the transform is not affine.
     */
    public void setTransform(Transform3DWrapper t1) {
        try {
            ((TransformGroupRemote)remote).setTransform( ((Transform3D)t1).remote );
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }

  /**
   * Copies the transform component of this TransformGroup into
   * the passed transform object.
   * @param t1 the transform object to be copied into
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
   */
    public void getTransform(Transform3DWrapper t1) {
        try {
            ((TransformGroupRemote)remote).getTransform( ((Transform3D)t1).remote );
        } catch( java.rmi.RemoteException rex ) {
            throw new RuntimeException( rex );
        }
    }

}
