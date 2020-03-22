/*
 * $RCSfile: SourceCodeShader.java,v $
 *
 * Copyright (c) 2005 Sun Microsystems, Inc. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * $Revision: 1.2 $
 * $Date: 2005-06-24 19:48:28 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.sg.internal.j3d.j3dwrapper;

import org.jdesktop.lg3d.sg.internal.wrapper.SourceCodeShaderWrapper;

/**
 * The SourceCodeShader object is a shader that is defined using
 * text-based source code. It is used to define the source code for
 * both vertex and fragment shaders. The currently supported shading
 * languages are Cg and GLSL.
 *
 * @see ShaderProgram
 *
 */

public class SourceCodeShader extends Shader implements SourceCodeShaderWrapper {

    /**
     * Constructs a new shader object of the specified shading
     * language and shader type from the specified source string.
     *
     * @param shadingLanguage the specified shading language, one of:
     * <code>SHADING_LANGUAGE_GLSL</code> or
     * <code>SHADING_LANGUAGE_CG</code>.
     *
     * @param shaderType the shader type, one of:
     * <code>SHADER_TYPE_VERTEX</code> or
     * <code>SHADER_TYPE_FRAGMENT</code>.
     *
     * @param shaderSource the shader source code
     */

    public SourceCodeShader(int shadingLanguage, int shaderType, String shaderSource) {
        wrapped = new javax.media.j3d.SourceCodeShader(shadingLanguage, shaderType, shaderSource);
        wrapped.setUserData(this);
    }

    /**
     * Retrieves the shader source string from this shader object.
     *
     * @return the shader source string.
     */
    public String getShaderSource() {
	return ((javax.media.j3d.SourceCodeShader)wrapped).getShaderSource();
    }


    public void createWrapped() {
    }
    

}
