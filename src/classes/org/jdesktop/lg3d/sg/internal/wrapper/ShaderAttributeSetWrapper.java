/*
 * $RCSfile: ShaderAttributeSetWrapper.java,v $
 *
 * Copyright (c) 2005 Sun Microsystems, Inc. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * $Revision: 1.2 $
 * $Date: 2005-06-24 19:48:32 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.sg.internal.wrapper;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import javax.vecmath.*;

/**
 * The ShaderAttributeSetWrapper object provides uniform attributes to shader
 * programs. Uniform attributes (variables) are those attributes whose
 * values are constant during the rendering of a primitive. Their
 * values may change from primitive to primitive, but are constant for
 * each vertex (for vertex shaders) or fragment (for fragment shaders)
 * of a single primitive. Examples of uniform attributes include a
 * transformation matrix, a texture map, lights, lookup tables, etc.
 * The ShaderAttributeSetWrapper object contains a set of ShaderAttribute
 * objects. Each ShaderAttribute object defines the value of a single
 * uniform shader variable. The set of attributes is unique with respect
 * to attribute names: no two attributes in the set will have the same
 * name.
 * 
 * <p>
 * There are two ways in which values can be specified for uniform
 * attributes: explicitly, by providing a value; and implicitly, by
 * defining a binding between a Java 3D system attribute and a uniform
 * attribute. This functionality is provided by two subclasses of
 * ShaderAttribute: ShaderAttributeObject, which is used to specify
 * explicitly defined attributes; and ShaderAttributeBinding, which is
 * used to specify implicitly defined, automatically tracked attributes.
 * 
 * <p>
 * Depending on the shading language (and profile) being used, several
 * Java 3D state attributes are automatically made available to the
 * shader program as pre-defined uniform attributes. The application
 * doesn't need to do anything to pass these attributes in to the
 * shader program. The implementation of each shader language (e.g.,
 * Cg, GLSL) defines its own bindings from Java 3D attribute to uniform
 * variable name. A list of these attributes for each shader language
 * can be found in the concrete subclass of ShaderProgram for that
 * shader language.
 * 
 * @see ShaderAttribute
 * @see ShaderProgram
 * @see ShaderAppearance#setShaderAttributeSet
 * 
 * @since Java 3D 1.4
 */

public interface ShaderAttributeSetWrapper extends NodeComponentWrapper {

    //
    // Methods for dealing with the (name, value) pairs for explicit
    // attributes
    //

    /**
     * Adds the specified shader attribute to the attributes set.
     * The newly specified attribute replaces an attribute with the
     * same name, if one already exists in the attributes set.
     *
     * @param attr the shader attribute to be added to the set
     *
     * @exception NullPointerException if attr is null
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public void put(ShaderAttributeWrapper attr);

    /**
     * Retrieves the shader attribute with the specified
     * <code>attrName</code> from the attributes set. If attrName does
     * not exist in the attributes set, null is returned.
     *
     * @param attrName the name of the shader attribute to be retrieved
     *
     * @exception NullPointerException if attrName is null
     *
     * @return a the shader attribute associated with the specified
     * attribute name, or null if the name is not in the attributes
     * set
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public ShaderAttributeWrapper get(String attrName);
    /**
     * Removes the shader attribute with the specified
     * <code>attrName</code> from the attributes set. If attrName does
     * not exist in the attributes set then nothing happens.
     *
     * @param attrName the name of the shader attribute to be removed
     *
     * @exception NullPointerException if attrName is null
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public void remove(String attrName);
    /**
     * Removes the specified shader attribute from the attributes
     * set. If the attribute does not exist in the attributes set then
     * nothing happens. Note that this method will <i>not</i> remove a
     * shader object other than the one specified, even if it has the
     * same name as the specified attribute. Applications that wish to
     * remove an attribute by name should use
     * <code>removeAttribute(String)</code>.
     *
     * @param attr the shader attribute to be removed
     *
     * @exception NullPointerException if attr is null
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public void remove(ShaderAttributeWrapper attr);

    /**
     * Removes all shader attributes from the attributes set. The
     * attributes set will be empty following this call.
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public void clear();

    /**
     * Returns a shallow copy of the attributes set.
     *
     * @return a shallow copy of the attributes set
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public ShaderAttributeWrapper[] getAll();

    /**
     * Returns the number of elements in the attributes set.
     *
     * @return the number of elements in the attributes set
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public int size();

}
