/*
 * $RCSfile: GLSLShaderProgramWrapper.java,v $
 *
 * Copyright (c) 2005 Sun Microsystems, Inc. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * $Revision: 1.2 $
 * $Date: 2005-06-24 19:48:30 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.sg.internal.wrapper;

/**
 * The GLSLShaderProgramWrapper object is a concrete implementation of a
 * ShaderProgram node component for the OpenGL GLSL shading language.
 * 
 * @see SourceCodeShader
 */

public interface GLSLShaderProgramWrapper extends ShaderProgramWrapper {

    // Implement abstract setVertexAttrNames method (inherit javadoc from parent class)
    public void setVertexAttrNames(String[] vertexAttrNames);

    // Implement abstract getVertexAttrNames method (inherit javadoc from parent class)
    public String[] getVertexAttrNames();

    // Implement abstract setShaderAttrNames method (inherit javadoc from parent class)
    public void setShaderAttrNames(String[] shaderAttrNames);

    // Implement abstract getShaderAttrNames method (inherit javadoc from parent class)
    public String[] getShaderAttrNames();

    /**
     * Copies the specified array of shaders into this shader
     * program. This method makes a shallow copy of the array. The
     * array of shaders may be null or empty (0 length), but the
     * elements of the array must be non-null. The shading language of
     * each shader in the array must be
     * <code>SHADING_LANGUAGE_GLSL</code>. Each shader in the array must
     * be a SourceCodeShader.
     *
     * @param shaders array of Shader objects to be copied into this
     * ShaderProgram
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @exception IllegalArgumentException if the shading language of
     * any shader in the shaders array is <em>not</em>
     * <code>SHADING_LANGUAGE_GLSL</code>.
     *
     * @exception ClassCastException if any shader in the shaders
     * array is <em>not</em> a SourceCodeShader.
     *
     * @exception NullPointerException if any element in the
     * shaders array is null.
     */
    public void setShaders(ShaderWrapper[] shaders);

    // Implement abstract getShaders method (inherit javadoc from parent class)
    public ShaderWrapper[] getShaders();

}
