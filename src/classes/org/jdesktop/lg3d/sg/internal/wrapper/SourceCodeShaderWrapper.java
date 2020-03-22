/*
 * $RCSfile: SourceCodeShaderWrapper.java,v $
 *
 * Copyright (c) 2005 Sun Microsystems, Inc. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * $Revision: 1.2 $
 * $Date: 2005-06-24 19:48:34 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.sg.internal.wrapper;

/**
 * The SourceCodeShaderWrapper object is a shader that is defined using
 * text-based source code. It is used to define the source code for
 * both vertex and fragment shaders. The currently supported shading
 * languages are Cg and GLSL.
 * 
 * @see ShaderProgram
 */

public interface SourceCodeShaderWrapper extends ShaderWrapper {

    /**
     * Retrieves the shader source string from this shader object.
     *
     * @return the shader source string.
     */
    public String getShaderSource();


}
