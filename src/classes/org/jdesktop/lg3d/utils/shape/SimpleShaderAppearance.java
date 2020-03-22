/**
 * Project Looking Glass
 *
 * $RCSfile: SimpleShaderAppearance.java,v $
 *
 * Copyright (c) 2004, Sun Microsystems, Inc., All Rights Reserved
 *
 * Redistributions in source code form must reproduce the above
 * copyright and this condition.
 *
 * The contents of this file are subject to the GNU General Public
 * License, Version 2 (the "License"); you may not use this file
 * except in compliance with the License. A copy of the License is
 * available at http://www.opensource.org/licenses/gpl-license.php.
 *
 * $Revision: 1.1 $
 * $Date: 2006-08-17 18:28:19 $
 * $State: Exp $
 *
 */
package org.jdesktop.lg3d.utils.shape;

import java.net.URL;
import java.io.IOException;
import com.sun.j3d.utils.shader.StringIO;
import org.jdesktop.lg3d.sg.Texture;
import org.jdesktop.lg3d.sg.utils.image.TextureLoader;
import org.jdesktop.lg3d.sg.GLSLShaderProgram;
import org.jdesktop.lg3d.sg.Shader;
import org.jdesktop.lg3d.sg.ShaderAppearance;
import org.jdesktop.lg3d.sg.ShaderProgram;
import org.jdesktop.lg3d.sg.SourceCodeShader;

/**
 * A convenience class to create typical ShaderAppearance object quickly.
 * Still in development...
 */
public class SimpleShaderAppearance extends ShaderAppearance {    
    /**
     * Create a SimpleShaderAppearance.
     */
    public SimpleShaderAppearance(URL vertexShader, URL fragmentShader) 
            throws IOException
    {
        if (vertexShader == null) {
            throw new IllegalArgumentException("the vertexShader argument cannot be null");
        }
        if (fragmentShader == null) {
            throw new IllegalArgumentException("the fragmentShader argument cannot be null");
        }

        String vertexProgram = null;
        String fragmentProgram = null;
        try {
            vertexProgram = StringIO.readFully(vertexShader);
        } catch (IOException ioe) {
            throw new IOException("while loading a vertex shader program from " + vertexShader);
        }
        try {
            fragmentProgram = StringIO.readFully(fragmentShader);
        } catch (IOException ioe) {
            throw new IOException("while loading a fragment shader program from " + fragmentShader);
        }
        
        Shader[] shaders = new Shader[2];
        shaders[0] 
                = new SourceCodeShader(Shader.SHADING_LANGUAGE_GLSL,
                                        Shader.SHADER_TYPE_VERTEX,
                                        vertexProgram);
      
        shaders[1] 
                = new SourceCodeShader(Shader.SHADING_LANGUAGE_GLSL,
                                        Shader.SHADER_TYPE_FRAGMENT,
                                        fragmentProgram);
        
        ShaderProgram shaderProgram = new GLSLShaderProgram();
        shaderProgram.setShaders(shaders);
        setShaderProgram(shaderProgram);
    }
    
    /**
     * Create a SimpleShaderAppearance.
     */
    public SimpleShaderAppearance(URL vertexShader, URL fragmentShader, URL imageUrl)
            throws IOException
    {
        this(vertexShader, fragmentShader);
    } 
    
    /**
     * Set a new texture image to this SimpleAppearance object.
     * In order to avoid a copy, the texture image is loaded in yUp and
     * byReference.  Thus, a regluar image becomes upside-down.
     * The user can compensate this upside-down texture by setting up texture 
     * coordinates to invert the Y direction once again. 
     *
     * @param  imageUrl  the URL that represents the texture image 
     */
    public void setTexture(URL imageUrl) 
        throws IOException
    {
        if (imageUrl == null) {
            throw new IllegalArgumentException("the imageUrl argument cannot be null");
        }
        TextureLoader textureLoader 
            = new TextureLoader(imageUrl, TextureLoader.Y_UP | TextureLoader.BY_REFERENCE, null);
        Texture texture = textureLoader.getTexture();
        if (texture == null) {
            throw new IOException("while loading texture from " + imageUrl);
        }
        texture.setMinFilter(Texture.BASE_LEVEL_LINEAR);
        texture.setMagFilter(Texture.BASE_LEVEL_LINEAR);
        setTexture(texture);
    }
}

