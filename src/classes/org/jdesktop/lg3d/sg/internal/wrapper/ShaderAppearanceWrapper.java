/*
 * $RCSfile: ShaderAppearanceWrapper.java,v $
 *
 * Copyright (c) 2005 Sun Microsystems, Inc. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * $Revision: 1.2 $
 * $Date: 2005-06-24 19:48:31 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.sg.internal.wrapper;

import java.util.Hashtable;

/**
 * <p>The ShaderAppearanceWrapper object defines programmable shading attributes
 * that can be set as a component object of a Shape3D node. The
 * ShaderAppearanceWrapper rendering state adds the following attributes in
 * addition to those defined by Appearance:</p>
 * 
 * <ul>
 * <li>Shader program - specifies the shader program...</li>
 * 
 * <p></p>
 * <li>Shader attribute set - specifies the shader parameters, both as
 * explicit attributes and as implicit bindings to Java 3D
 * state...</li>
 * </ul>
 * 
 * <p>The ShaderAppearanceWrapper object modifies the definition of some of the
 * attributes in Appearance:</p>
 * 
 * <ul>
 * <li>Coloring attributes - XXXXX</li>
 * 
 * <p></p>
 * <li>Line attributes - XXXXX</li>
 * 
 * <p></p>
 * <li>Point attributes - XXXXX</li>
 * 
 * <p></p>
 * <li>Polygon attributes - XXXXX</li>
 * 
 * <p></p>
 * <li>Rendering attributes - XXXXX</li>
 * 
 * <p></p>
 * <li>Transparency attributes - XXXXX</li>
 * 
 * <p></p>
 * <li>Material - XXXXX</li>
 * 
 * <p></p>
 * <li>Texture - XXXXX</li>
 * 
 * <p></p>
 * <li>Texture attributes - XXXXX</li>
 * 
 * <p></p>
 * <li>Texture coordinate generation - XXXXX</li>
 * 
 * <p></p>
 * <li>Texture unit state - XXXXX</li>
 * </ul>
 * 
 * @see ShaderProgram
 * @see ShaderAttributeSet
 */
public interface ShaderAppearanceWrapper extends AppearanceWrapper {

    /**
     * Sets the ShaderProgram object to the specified object.  Setting it to
     * null causes a default pass-through shader to be used ???
     *
     * @param shaderProgram object that specifies the desired shader program
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setShaderProgram(ShaderProgramWrapper shaderProgram);


    /**
     * Retrieves the current ShaderProgram object.
     *
     * @return the ShaderProgram object
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public ShaderProgramWrapper getShaderProgram();


    /**
     * Sets the ShaderAttributeSet object to the specified object.  Setting it to
     * null is equivalent to specifying an empty set of attributes.
     *
     * @param shaderAttributeSet object that specifies the desired shader attributes
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setShaderAttributeSet(ShaderAttributeSetWrapper shaderAttributeSet);


    /**
     * Retrieves the current ShaderAttributeSet object.
     *
     * @return the ShaderAttributeSet object
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public ShaderAttributeSetWrapper getShaderAttributeSet();

}
