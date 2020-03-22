/*
 * $RCSfile: Shader.java,v $
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

import org.jdesktop.lg3d.sg.internal.wrapper.ShaderWrapper;

/**
 * The Shader object is the abstract base class for programmable
 * shader code. Currently, only text-based source code shaders are
 * supported, so the only subclass of Shader is SourceCodeShader. We
 * leave open the possibility for binary (object code) shaders in the
 * future.
 *
 * <p>
 * Each instance of a Shader object allows an application to specify
 * the source code used in programming the Graphics Pipeline Unit
 * (GPU) of the graphics accelerator. A Shader object is constructed
 * with modes that specify the <i>shading language</i> and the
 * <i>shader type</i>.
 *
 * <p>
 * The shading language specifies the language and runtime environment
 * used to program the GPU. The currently defined shading languages
 * are GLSL (also known as the OpenGL 2.0 shading language) and
 * Cg. Note that not all shading languages are supported on all
 * platforms. It is up to the application or utility to query whether
 * a particular shading language is supported before using it. The
 * value of the <code>shadingLanguage</code> mode is one of:
 * <code>SHADING_LANGUAGE_GLSL</code> or
 * <code>SHADING_LANGUAGE_CG</code>.
 *
 *<p>
 * The shader type specifies whether the shader is a <i>vertex
 * shader</i> or a <i>fragment shader</i>. A vertex shader replaces
 * the fixed-function graphics pipeline for vertex operations
 * (transformation and lighting). A fragment shader replaces the
 * fixed-function graphics pipeline for fragment shading operations
 * (texture mapping, texture application, coloring, shading, and so
 * forth). The value of the <code>shaderType</code> mode is one of:
 * <code>SHADER_TYPE_VERTEX</code> or
 * <code>SHADER_TYPE_FRAGMENT</code>.
 *
 * <p>
 * Both the shading language and shader type are immutable modes of
 * the Shader object.
 *
 * <p>
 * NOTE: Applications should <i>not</i> extend this class.
 *
 * @see ShaderProgram
 * @see Canvas3D#isShadingLanguageSupported
 *
 * @since Java 3D 1.4
 */

public abstract class Shader extends NodeComponent {


    /**
     * This constant indicates the GLSL shading language. It is one
     * of the possible values of the shadingLanguage parameter.
     */
    public static final int SHADING_LANGUAGE_GLSL = 1;

    /**
     * This constant indicates the Cg shading language. It is one
     * of the possible values of the shadingLanguage parameter.
     */
    public static final int SHADING_LANGUAGE_CG = 2;


    /**
     * This constant indicates that the shader type is a vertex
     * shader.  It is one of the possible values of the shaderType
     * parameter.
     */
    public static final int SHADER_TYPE_VERTEX = 1;

    /**
     * This constant indicates that the shader type is a fragment
     * shader.  It is one of the possible values of the shaderType
     * parameter.
     */
    public static final int SHADER_TYPE_FRAGMENT = 2;


    /**
     * Not a public constructor, for internal use
     */
    Shader() {
    }

    /**
     * Returns the shading language of this shader.
     *
     * @return the  shading language of this shader, one of:
     * <code>SHADING_LANGUAGE_GLSL</code> or
     * <code>SHADING_LANGUAGE_CG</code>.
     */
    public int getShadingLanguage() {
	return ((ShaderWrapper)wrapped).getShadingLanguage();
    }

    /**
     * Returns the type of this shader.
     *
     * @return the shader type, one of:
     * <code>SHADER_TYPE_VERTEX</code> or
     * <code>SHADER_TYPE_FRAGMENT</code>.
     */
    public int getShaderType() {
	return ((ShaderWrapper)wrapped).getShaderType();
    }
}

