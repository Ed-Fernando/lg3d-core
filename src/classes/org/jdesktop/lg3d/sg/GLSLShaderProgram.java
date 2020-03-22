/*
 * $RCSfile: GLSLShaderProgram.java,v $
 *
 * Copyright (c) 2005 Sun Microsystems, Inc. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * $Revision: 1.2 $
 * $Date: 2005-06-24 19:48:20 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.sg;
import org.jdesktop.lg3d.sg.internal.wrapper.GLSLShaderProgramWrapper;
import org.jdesktop.lg3d.sg.internal.wrapper.ShaderWrapper;

/**
 * The GLSLShaderProgram object is a concrete implementation of a
 * ShaderProgram node component for the OpenGL GLSL shading language.
 *
 * @see SourceCodeShader
 *
 * @since Java 3D 1.4
 */

public class GLSLShaderProgram extends ShaderProgram {

    /**
     * Constructs a GLSL shader program node component.
     *
     * <br>
     * TODO: ADD MORE DOCUMENTATION HERE.
     */
    public GLSLShaderProgram() {
    }
    
    public void createWrapped() {
        wrapped = instantiate(SceneGraphSetup.getWrapperPackage()+"GLSLShaderProgram");
    }

    // Implement abstract setVertexAttrNames method (inherit javadoc from parent class)
    public void setVertexAttrNames(String[] vertexAttrNames) {
        ((GLSLShaderProgramWrapper)wrapped).setVertexAttrNames(vertexAttrNames);
    }

    // Implement abstract getVertexAttrNames method (inherit javadoc from parent class)
    public String[] getVertexAttrNames() {
	throw new RuntimeException("not implemented");
    }

    // Implement abstract setShaderAttrNames method (inherit javadoc from parent class)
    public void setShaderAttrNames(String[] shaderAttrNames) {
        ((GLSLShaderProgramWrapper)wrapped).setShaderAttrNames(shaderAttrNames);
    }

    // Implement abstract getShaderAttrNames method (inherit javadoc from parent class)
    public String[] getShaderAttrNames() {
	throw new RuntimeException("not implemented");
    }

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
    public void setShaders(Shader[] shaders) {
        ShaderWrapper[] shaderWrappers = new ShaderWrapper[shaders.length];
        for(int i=0; i<shaders.length; i++)
            shaderWrappers[i]=(ShaderWrapper)shaders[i].wrapped;
                    
 	((GLSLShaderProgramWrapper)wrapped).setShaders(shaderWrappers);
    }

    // Implement abstract getShaders method (inherit javadoc from parent class)
    public Shader[] getShaders() {
        ShaderWrapper[] shaderWrappers = ((GLSLShaderProgramWrapper)wrapped).getShaders();
        Shader[] shaders = new Shader[shaderWrappers.length];
        
        for(int i=0; i<shaders.length; i++)
            shaders[i] = (Shader)shaderWrappers[i].getUserData();
        
        return shaders;
    }

}
