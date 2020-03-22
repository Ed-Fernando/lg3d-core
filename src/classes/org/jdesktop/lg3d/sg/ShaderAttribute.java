/*
 * $RCSfile: ShaderAttribute.java,v $
 *
 * Copyright (c) 2005 Sun Microsystems, Inc. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * $Revision: 1.2 $
 * $Date: 2005-06-24 19:48:23 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.sg;
import org.jdesktop.lg3d.sg.internal.wrapper.ShaderAttributeWrapper;

/**
 * The ShaderAttribute object encapsulates a uniform attribute for a
 * shader programs.  Uniform attributes (variables) are those
 * attributes whose values are constant during the rendering of a
 * primitive. Their values may change from primitive to primitive, but
 * are constant for each vertex (for vertex shaders) or fragment (for
 * fragment shaders) of a single primitive. Examples of uniform
 * attributes include a transformation matrix, a texture map, lights,
 * lookup tables, etc.
 *
 * <p>
 * There are two ways in which values can be specified for uniform
 * attributes: explicitly, by providing a value; and implicitly, by
 * defining a binding between a Java 3D system attribute and a uniform
 * attribute. This functionality is provided by two subclasses of
 * ShaderAttribute as follows:
 *
 * <ul>
 * <li>ShaderAttributeObject, in which attributes are expressed as
 * <code>(attrName,&nbsp;value)</code> pairs, is used for explicitly
 * defined attributes</li>
 * <li>ShaderAttributeBinding, in which attributes are expressed as
 * <code>(attrName,&nbsp;j3dAttrName)</code> pairs, is used for
 * implicitly defined, automatically tracked attributes</li>
 * </ul>
 *
 * @see ShaderAttributeSet
 * @see ShaderProgram
 */

public abstract class ShaderAttribute extends NodeComponent {
    /**
     * Name of the shader attribute (immutable)
     */
    String attrName;

    /**
     * Package scope constructor
     */
    ShaderAttribute(String attrName) {
	this.attrName = attrName;
    }

    /**
     * Retrieves the name of this shader attribute.
     *
     * @return the name of this shader attribute
     */
    public String getAttributeName() {
	return ((ShaderAttributeWrapper)wrapped).getAttributeName();
    }

}
