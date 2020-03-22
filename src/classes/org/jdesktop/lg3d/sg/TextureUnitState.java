/**
 * Project Looking Glass
 *
 * $RCSfile: TextureUnitState.java,v $
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
 * $Revision: 1.2 $
 * $Date: 2004-06-23 18:50:31 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg;

import java.util.Hashtable;

/**
 * The TextureUnitState object defines all texture mapping state for a
 * single texture unit.  An appearance object contains an array of
 * texture unit state objects to define the state for multiple texture
 * mapping units.  The texture unit state consists of the
 * following:
 *
 * <p>
 * <ul>
 * <li>Texture - defines the texture image and filtering
 * parameters used when texture mapping is enabled. These attributes
 * are defined in a Texture object.</li><p>
 *
 * <li>Texture attributes - defines the attributes that apply to
 * texture mapping, such as the texture mode, texture transform,
 * blend color, and perspective correction mode. These attributes
 * are defined in a TextureAttributes object.</li><p>
 *
 * <li>Texture coordinate generation - defines the attributes
 * that apply to texture coordinate generation, such as whether
 * texture coordinate generation is enabled, coordinate format
 * (2D or 3D coordinates), coordinate generation mode (object
 * linear, eye linear, or spherical reflection mapping), and the
 * R, S, and T coordinate plane equations. These attributes
 * are defined in a TexCoordGeneration object.</li><p>
 * </ul>
 *
 * @see Appearance
 * @see Texture
 * @see TextureAttributes
 * @see TexCoordGeneration
 *
 * @since Java 3D 1.2
 */
public class TextureUnitState extends NodeComponent {

    /**
     * Specifies that this TextureUnitState object allows reading its
     * texture, texture attribute, or texture coordinate generation
     * component information.
     */
    public static final int ALLOW_STATE_READ =
	CapabilityBits.TEXTURE_UNIT_STATE_ALLOW_STATE_READ;

    /**
     * Specifies that this TextureUnitState object allows writing its
     * texture, texture attribute, or texture coordinate generation
     * component information.
     */
    public static final int ALLOW_STATE_WRITE =
	CapabilityBits.TEXTURE_UNIT_STATE_ALLOW_STATE_WRITE;


    /**
     * Constructs a TextureUnitState component object using defaults for all
     * state variables. All component object references are initialized
     * to null.
     */
    public TextureUnitState() {
	// Just use default values
    }

    /**
     * Constructs a TextureUnitState component object using the specified
     * component objects.
     *
     * @param texture object that specifies the desired texture
     * map and texture parameters
     * @param textureAttributes object that specifies the desired
     * texture attributes
     * @param texCoordGeneration object that specifies the texture coordinate
     * generation parameters
     */
    public TextureUnitState(Texture texture,
			     TextureAttributes textureAttributes,
			     TexCoordGeneration texCoordGeneration) {

	set( texture, textureAttributes, texCoordGeneration );
    }

    /**
     * Creates the retained mode TextureUnitStateRetained object that this
     * TextureUnitState component object will point to.
     */
    protected void createWrapped() {
	wrapped = instantiate("TextureUnitState");
    }

    /**
     * Sets the texture, texture attributes, and texture coordinate
     * generation components in this TextureUnitState object to the
     * specified component objects.
     *
     * @param texture object that specifies the desired texture
     * map and texture parameters
     * @param textureAttributes object that specifies the desired
     * texture attributes
     * @param texCoordGeneration object that specifies the texture coordinate
     * generation parameters
     */
    public void set(Texture texture,
		    TextureAttributes textureAttributes,
		    TexCoordGeneration texCoordGeneration) {

	throw new RuntimeException("Not Implemented");
    }

    /**
     * Sets the texture object to the specified object.
     * Setting it to null disables texture mapping for the
     * texture unit corresponding to this TextureUnitState object.
     * @param texture object that specifies the desired texture
     * map and texture parameters
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setTexture(Texture texture) {
	throw new RuntimeException("Not Implemented");
    }

    /**
     * Retrieves the current texture object.
     * @return the texture object
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public Texture getTexture() {
	throw new RuntimeException("Not Implemented");
    }

    /**
     * Sets the textureAttributes object to the specified object.
     * Setting it to null will result in default attribute usage for the.
     * texture unit corresponding to this TextureUnitState object.
     * @param textureAttributes object that specifies the desired
     * texture attributes
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setTextureAttributes(TextureAttributes textureAttributes) {
	throw new RuntimeException("Not Implemented");
    }

    /**
     * Retrieves the current textureAttributes object.
     * @return the textureAttributes object
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public TextureAttributes getTextureAttributes() {
	throw new RuntimeException("Not Implemented");
    }

    /**
     * Sets the texCoordGeneration object to the specified object.
     * Setting it to null disables texture coordinate generation for the
     * texture unit corresponding to this TextureUnitState object.
     * @param texCoordGeneration object that specifies the texture coordinate
     * generation parameters
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setTexCoordGeneration(TexCoordGeneration texCoordGeneration) {
	throw new RuntimeException("Not Implemented");
    }

    /**
     * Retrieves the current texCoordGeneration object.
     * @return the texCoordGeneration object
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public TexCoordGeneration getTexCoordGeneration() {
	throw new RuntimeException("Not Implemented");
    }
}
