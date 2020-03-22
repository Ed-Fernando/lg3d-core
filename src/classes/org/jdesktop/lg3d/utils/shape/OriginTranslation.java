/**
 * Project Looking Glass
 *
 * $RCSfile: OriginTranslation.java,v $
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
 * $Date: 2005-04-14 23:04:54 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.shape;

import org.jdesktop.lg3d.sg.Node;
import org.jdesktop.lg3d.sg.Transform3D;
import org.jdesktop.lg3d.sg.TransformGroup;
import javax.vecmath.Vector3f;


/**
 * A simple convenience class to move the origin of a given node. 
 * The argument is typically a Shape3D object and the origin is typically
 * positioned at its center.  This class is a subclass of TransformGroup
 * and performs the following operation:
 * <code>
 *   Transform3D t3d = new Transform3D();
 *   t3d.setTranslation(trans);
 *   setTransform(t3d);
 *   addChild(node);
 * </code>
 * The translation value is immutable after once created.
 * This allows Java 3D to perform some optimization. 
 * Those who want more sophisticated transform against a node should 
 * check out the usage of TransformGroup and Transform3D.
 */
public class OriginTranslation extends TransformGroup {
    /** 
     * Creates a new instance of OriginTranslation 
     *
     * @param node   a node whose position it translated.
     * @param trans  a vector that indicates the translation.
     */
    public OriginTranslation(Node node, Vector3f trans) {
        Transform3D t3d = new Transform3D();
        t3d.setTranslation(trans);
        super.setTransform(t3d);
        addChild(node);
    }
    
    /**
     * The transform component of this TransformGroup is immutable,
     * and this method throws an UnsupportedOperationException.
     * 
     * @param t1  the transform object to be copied into
     * @throws UnsupportedOperationException
     */
    public void setTransform(Transform3D t1) {
        throw new UnsupportedOperationException(
            "The transform component of OriginTranslation is immutable");
    }
}
