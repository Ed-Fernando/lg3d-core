/*
 * $RCSfile: ShaderProgramWrapper.java,v $
 *
 * Copyright (c) 2005 Sun Microsystems, Inc. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * $Revision: 1.2 $
 * $Date: 2005-06-24 19:48:33 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.sg.internal.wrapper;

/**
 * The ShaderProgramWrapper node component object is the abstract base class
 * for programmable shader programs. Each concrete instance of a
 * ShaderProgramWrapper is a container for a set of Shader objects. The set
 * of Shaders contained in the ShaderProgramWrapper is a complete program for
 * the Graphics Pipeline Unit (GPU) of the graphics accelerator. It is
 * specified using the shader language defined by the
 * ShaderProgramWrapper. The currently defined shader languages are: Cg and
 * GLSL.
 * 
 * <p>
 * NOTE: Applications should <i>not</i> extend this class.
 * 
 * @see Shader
 * @see ShaderAppearance#setShaderProgram
 */

public interface ShaderProgramWrapper extends NodeComponentWrapper {


    /**
     * Sets the vertex attribute names array for this ShaderProgramWrapper
     * object. Each element in the array specifies the shader
     * attribute name that is bound to the corresponding numbered
     * vertex attribute within a GeometryArray object that uses this
     * shader program. Array element 0 specifies the name of
     * GeometryArray vertex attribute 0, array element 1 specifies the
     * name of GeometryArray vertex attribute 1, and so forth.
     * 
     * @param vertexAttrNames array of vertex attribute names for this
     * shader program. A copy of this array is made.
     * 
     * @exception RestrictedAccessException if the method is called
     * when this object is part of live or compiled scene graph.
     */
    public void setVertexAttrNames(String[] vertexAttrNames);

    /**
     * Retrieves the vertex attribute names array from this
     * GeometryArray object.
     * 
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     * 
     * @return a copy of this ShaderProgramWrapper's array of vertex attribute names.
     */
    public String[] getVertexAttrNames();


    /**
     * Sets the shader attribute names array for this ShaderProgramWrapper
     * object. Each element in the array specifies a shader
     * attribute name that may be set via a ShaderAttribute object.
     * Only those attributes whose names that appear in the shader
     * attribute names array can be set for a given shader program.
     * 
     * <p>
     * TODO: finish this.
     * 
     * @param shaderAttrNames array of shader attribute names for this
     * shader program. A copy of this array is made.
     * 
     * @exception RestrictedAccessException if the method is called
     * when this object is part of live or compiled scene graph.
     */
    public void setShaderAttrNames(String[] shaderAttrNames);

    /**
     * Retrieves the shader attribute names array from this
     * GeometryArray object.
     * 
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     * 
     * @return a copy of this ShaderProgramWrapper's array of shader attribute names.
     */
    public String[] getShaderAttrNames();


    /**
     * Copies the specified array of shaders into this shader
     * program. This method makes a shallow copy of the array. The
     * array of shaders may be null or empty (0 length), but the
     * elements of the array must be non-null. The shading
     * language of each shader in the array must match the
     * subclass. Subclasses may impose additional restrictions.
     * 
     * @param shaders array of Shader objects to be copied into this
     * ShaderProgramWrapper
     * 
     * @exception RestrictedAccessException if the method is called
     * when this object is part of live or compiled scene graph.
     * 
     * @exception IllegalArgumentException if the shading language of
     * any shader in the shaders array doesn't match the type of the
     * subclass.
     */
    public void setShaders(ShaderWrapper[] shaders);
    
    /**
     * Retrieves the array of shaders from this shader program. A
     * shallow copy of the array is returned. The return value may
     * be null.
     * 
     * @return a copy of this ShaderProgramWrapper's array of Shader objects
     * 
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public ShaderWrapper[] getShaders();

}
