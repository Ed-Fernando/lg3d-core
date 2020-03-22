/*
 * $RCSfile: ShaderAppearance.java,v $
 *
 * Copyright (c) 2005 Sun Microsystems, Inc. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * $Revision: 1.2 $
 * $Date: 2005-06-24 19:48:22 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.sg;
import org.jdesktop.lg3d.sg.internal.wrapper.ShaderAppearanceWrapper;
import org.jdesktop.lg3d.sg.internal.wrapper.ShaderAttributeSetWrapper;
import org.jdesktop.lg3d.sg.internal.wrapper.ShaderProgramWrapper;

/**
 * <p>The ShaderAppearance object defines programmable shading attributes
 * that can be set as a component object of a Shape3D node. The
 * ShaderAppearance rendering state adds the following attributes in
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
 * <p>The ShaderAppearance object modifies the definition of some of the
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
 *
 */
public class ShaderAppearance extends Appearance {
    /**
     * Specifies that this ShaderAppearance object allows reading its
     * ShaderProgram component information.
     */
    public static final int
	ALLOW_SHADER_PROGRAM_READ =
	CapabilityBits.SHADER_APPEARANCE_ALLOW_SHADER_PROGRAM_READ;

    /**
     * Specifies that this ShaderAppearance object allows writing its
     * ShaderProgram component information.
     */
    public static final int
	ALLOW_SHADER_PROGRAM_WRITE =
	CapabilityBits.SHADER_APPEARANCE_ALLOW_SHADER_PROGRAM_WRITE;

    /**
     * Specifies that this ShaderAppearance object allows reading its
     * ShaderAttributeSet component information.
     */
    public static final int
	ALLOW_SHADER_ATTRIBUTE_SET_READ =
	CapabilityBits.SHADER_APPEARANCE_ALLOW_SHADER_ATTRIBUTE_SET_READ;

    /**
     * Specifies that this ShaderAppearance object allows writing its
     * ShaderAttributeSet component information.
     */
    public static final int
	ALLOW_SHADER_ATTRIBUTE_SET_WRITE =
	CapabilityBits.SHADER_APPEARANCE_ALLOW_SHADER_ATTRIBUTE_SET_WRITE;


    /**
     * Constructs a ShaderAppearance component object using defaults for all
     * state variables. All component object references are initialized
     * to null.
     */
    public ShaderAppearance() {
	// Just use default values
    }

    public void createWrapped() {
        wrapped = instantiate( SceneGraphSetup.getWrapperPackage()+"ShaderAppearance" );
    }

    /**
     * Sets the ShaderProgram object to the specified object.  Setting it to
     * null causes a default pass-through shader to be used ???
     *
     * @param shaderProgram object that specifies the desired shader program
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setShaderProgram(ShaderProgram shaderProgram) {
        if (shaderProgram==null)
            ((ShaderAppearanceWrapper)wrapped).setShaderProgram(null);
        else
            ((ShaderAppearanceWrapper)wrapped).setShaderProgram((ShaderProgramWrapper)shaderProgram.getWrapped());
    }


    /**
     * Retrieves the current ShaderProgram object.
     *
     * @return the ShaderProgram object
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public ShaderProgram getShaderProgram() {
        ShaderProgramWrapper w = ((ShaderAppearanceWrapper)wrapped).getShaderProgram();
        
        if (w==null)
            return null;
        
        return (ShaderProgram)w.getUserData();
    }


    /**
     * Sets the ShaderAttributeSet object to the specified object.  Setting it to
     * null is equivalent to specifying an empty set of attributes.
     *
     * @param shaderAttributeSet object that specifies the desired shader attributes
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setShaderAttributeSet(ShaderAttributeSet shaderAttributeSet) {
        if (shaderAttributeSet==null)
            ((ShaderAppearanceWrapper)wrapped).setShaderAttributeSet(null);
        else
            ((ShaderAppearanceWrapper)wrapped).setShaderAttributeSet((ShaderAttributeSetWrapper)shaderAttributeSet.getWrapped());
    }


    /**
     * Retrieves the current ShaderAttributeSet object.
     *
     * @return the ShaderAttributeSet object
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public ShaderAttributeSet getShaderAttributeSet() {
        ShaderAttributeSetWrapper w = ((ShaderAppearanceWrapper)wrapped).getShaderAttributeSet();
        
        if (w==null)
            return null;
        
        return (ShaderAttributeSet)w.getUserData();
   }

}
