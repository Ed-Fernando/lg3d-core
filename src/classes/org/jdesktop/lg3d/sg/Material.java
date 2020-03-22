/**
 * Project Looking Glass
 *
 * $RCSfile: Material.java,v $
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
 * $Date: 2004-06-23 18:50:26 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg;

import org.jdesktop.lg3d.sg.internal.wrapper.MaterialWrapper;
import javax.vecmath.Color3f;

/**
 * The Material object defines the appearance of an object under
 * illumination.
 * If the Material object in an Appearance object is <code>null</code>,
 * lighting is disabled for all nodes that use that Appearance object.
 * <P>
 * The properties that can be set for a Material object are:
 * <P><UL>
 * <LI>Ambient color - the ambient RGB color reflected off the surface
 * of the material. The range of values is 0.0 to 1.0. The default ambient
 * color is (0.2, 0.2, 0.2).<p></LI>
 * <LI>Diffuse color - the RGB color of the material when illuminated.
 * The range of values is 0.0 to 1.0. The default diffuse color is
 * (1.0, 1.0, 1.0).<p></LI>
 * <LI>Specular color - the RGB specular color of the material (highlights).
 * The range of values is 0.0 to 1.0. The default specular color
 * is (1.0, 1.0, 1.0).<p></LI>
 * <LI>Emissive color - the RGB color of the light the material emits, if 
 * any. The range of values is 0.0 to 1.0. The default emissive
 * color is (0.0, 0.0, 0.0).<p></LI>
 * <LI>Shininess - the material's shininess, in the range [1.0, 128.0] 
 * with 1.0 being not shiny and 128.0 being very shiny. Values outside
 * this range are clamped. The default value for the material's
 * shininess is 64.<p></LI>
 * <LI>Color target - the material color target for per-vertex colors,
 * one of: AMBIENT, EMISSIVE, DIFFUSE, SPECULAR, or AMBIENT_AND_DIFFUSE.
 * The default target is DIFFUSE.<p></LI>
 * </UL>
 *
 * The Material object also enables or disables lighting.
 */
public class Material extends NodeComponent {

  /**
   * For material object, specifies that Material allows reading
   * individual component field information.
   */
    public static final int
    ALLOW_COMPONENT_READ = CapabilityBits.MATERIAL_ALLOW_COMPONENT_READ;

  /**
   * For material object, specifies that Material allows reading
   * individual component field information.
   */
    public static final int
    ALLOW_COMPONENT_WRITE = CapabilityBits.MATERIAL_ALLOW_COMPONENT_WRITE;

    /**
     * Specifies that per-vertex colors replace the ambient material color.
     * @see #setColorTarget
     *
     * @since Java 3D 1.3
     */
    public static final int AMBIENT = 0;

    /**
     * Specifies that per-vertex colors replace the emissive material color.
     * @see #setColorTarget
     *
     * @since Java 3D 1.3
     */
    public static final int EMISSIVE = 1;

    /**
     * Specifies that per-vertex colors replace the diffuse material color.
     * This is the default target.
     * @see #setColorTarget
     *
     * @since Java 3D 1.3
     */
    public static final int DIFFUSE = 2;

    /**
     * Specifies that per-vertex colors replace the specular material color.
     * @see #setColorTarget
     *
     * @since Java 3D 1.3
     */
    public static final int SPECULAR = 3;

    /**
     * Specifies that per-vertex colors replace both the ambient and the
     * diffuse material color.
     * @see #setColorTarget
     *
     * @since Java 3D 1.3
     */
    public static final int AMBIENT_AND_DIFFUSE = 4;


    /**
     * Constructs and initializes a Material object using default parameters.
     * The default values are as follows:
     * <ul>
     * lighting enable : true<br>
     * ambient color : (0.2, 0.2, 0.2)<br>
     * emmisive color : (0.0, 0.0, 0.0)<br>
     * diffuse color : (1.0, 1.0, 1.0)<br>
     * specular color : (1.0, 1.0, 1.0)<br>
     * shininess : 64<br>
     * color target : DIFFUSE
     * </ul>
     */
    public Material() {
    }
    
    /**
     * Constructs and initializes a new material object using the specified
     * parameters. Lighting is enabled by default.
     * @param ambientColor the material's ambient color
     * @param emissiveColor the material's emissive color
     * @param diffuseColor the material's diffuse color when illuminated by a
     * light
     * @param specularColor the material's specular color when illuminated
     * to generate a highlight
     * @param shininess the material's shininess in the
     * range [1.0, 128.0] with 1.0 being not shiny and 128.0 being very shiny.
     * Values outside this range are clamped.
     */
    public Material(Color3f ambientColor,
		    Color3f emissiveColor,
		    Color3f diffuseColor,
		    Color3f specularColor,
		    float shininess) {
        setAmbientColor(ambientColor);
        setEmissiveColor(emissiveColor);
        setDiffuseColor(diffuseColor);
        setSpecularColor(specularColor);
        setShininess(shininess);
    }

    protected void createWrapped() {
        wrapped = instantiate( SceneGraphSetup.getWrapperPackage()+"Material" );
    }

    /**
     * Sets this material's ambient color.
     * This specifies how much ambient light is reflected by
     * the surface.
     * The ambient color in this Material object may be overridden by
     * per-vertex colors in some cases.  If vertex colors are present
     * in the geometry, and lighting is enabled, and the colorTarget
     * is either AMBIENT or AMBIENT_AND_DIFFUSE, and vertex colors are
     * not being ignored, then the vertex colors are used in place of
     * this Material's ambient color in the lighting equation.
     *
     * @param color the material's ambient color
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @see RenderingAttributes#setIgnoreVertexColors
     * @see #setColorTarget
     */
    public void setAmbientColor(Color3f color) {
	((MaterialWrapper)wrapped).setAmbientColor( color );
    }

    /**
     * Sets this material's ambient color.
     * This specifies how much ambient light is reflected by
     * the surface.  
     * The ambient color in this Material object may be overridden by
     * per-vertex colors in some cases.  If vertex colors are present
     * in the geometry, and lighting is enabled, and the colorTarget
     * is either AMBIENT or AMBIENT_AND_DIFFUSE, and vertex colors are
     * not being ignored, then the vertex colors are used in place of
     * this Material's ambient color in the lighting equation.
     *
     * @param r the new ambient color's red component
     * @param g the new ambient color's green component
     * @param b the new ambient color's blue component
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @see RenderingAttributes#setIgnoreVertexColors
     * @see #setColorTarget
     */
    public void setAmbientColor(float r, float g, float b) {
	((MaterialWrapper)wrapped).setAmbientColor( r,g,b );
    }

    /**
     * Retrieves this material's ambient color.
     * @param color that will contain the material's ambient color
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void getAmbientColor(Color3f color) {
	((MaterialWrapper)wrapped).getAmbientColor( color );
    }

    /**
     * Sets this material's emissive color.
     * This is the color of light, if any, that the material emits.
     * The emissive color in this Material object may be overridden by
     * per-vertex colors in some cases.  If vertex colors are present
     * in the geometry, and lighting is enabled, and the colorTarget
     * is EMISSIVE, and vertex colors are
     * not being ignored, then the vertex colors are used in place of
     * this Material's emissive color in the lighting equation.
     *
     * @param color the new emissive color
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @see RenderingAttributes#setIgnoreVertexColors
     * @see #setColorTarget
     */
    public void setEmissiveColor(Color3f color) {
	((MaterialWrapper)wrapped).setEmissiveColor( color );
    }

    /**
     * Sets this material's emissive color.
     * This is the color of light, if any, that the material emits.
     * The emissive color in this Material object may be overridden by
     * per-vertex colors in some cases.  If vertex colors are present
     * in the geometry, and lighting is enabled, and the colorTarget
     * is EMISSIVE, and vertex colors are
     * not being ignored, then the vertex colors are used in place of
     * this Material's emissive color in the lighting equation.
     *
     * @param r the new emissive color's red component
     * @param g the new emissive color's green component
     * @param b the new emissive color's blue component
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @see RenderingAttributes#setIgnoreVertexColors
     * @see #setColorTarget
     */
    public void setEmissiveColor(float r, float g, float b) {
	((MaterialWrapper)wrapped).setEmissiveColor( r,g,b );
    }

    /**
     * Retrieves this material's emissive color and stores it in the
     * argument provided.
     * @param color the vector that will receive this material's emissive color
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void getEmissiveColor(Color3f color) {
	((MaterialWrapper)wrapped).getEmissiveColor( color );
    }

    /**
     * Sets this material's diffuse color.
     * This is the color of the material when illuminated by a light source.
     * The diffuse color in this Material object may be overridden by
     * per-vertex colors in some cases.  If vertex colors are present
     * in the geometry, and lighting is enabled, and the colorTarget
     * is either DIFFUSE or AMBIENT_AND_DIFFUSE, and vertex colors are
     * not being ignored, then the vertex colors are used in place of
     * this Material's diffuse color in the lighting equation.
     *
     * @param color the new diffuse color
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @see RenderingAttributes#setIgnoreVertexColors
     * @see #setColorTarget
     */
    public void setDiffuseColor(Color3f color) {
	((MaterialWrapper)wrapped).setDiffuseColor( color );
    }

    /**
     * Sets this material's diffuse color.
     * This is the color of the material when illuminated by a light source.
     * The diffuse color in this Material object may be overridden by
     * per-vertex colors in some cases.  If vertex colors are present
     * in the geometry, and lighting is enabled, and the colorTarget
     * is either DIFFUSE or AMBIENT_AND_DIFFUSE, and vertex colors are
     * not being ignored, then the vertex colors are used in place of
     * this Material's diffuse color in the lighting equation.
     *
     * @param r the new diffuse color's red component
     * @param g the new diffuse color's green component
     * @param b the new diffuse color's blue component
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @see RenderingAttributes#setIgnoreVertexColors
     * @see #setColorTarget
     */
    public void setDiffuseColor(float r, float g, float b) {
	((MaterialWrapper)wrapped).setDiffuseColor( r,g,b );
    }

    /**
     * Sets this material's diffuse color plus alpha.
     * This is the color of the material when illuminated by a light source.
     * The diffuse color in this Material object may be overridden by
     * per-vertex colors in some cases.  If vertex colors are present
     * in the geometry, and lighting is enabled, and the colorTarget
     * is either DIFFUSE or AMBIENT_AND_DIFFUSE, and vertex colors are
     * not being ignored, then the vertex colors are used in place of
     * this Material's diffuse color in the lighting equation.
     *
     * @param r the new diffuse color's red component
     * @param g the new diffuse color's green component
     * @param b the new diffuse color's blue component
     * @param a the alpha component used to set transparency
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @see RenderingAttributes#setIgnoreVertexColors
     * @see #setColorTarget
     */
    public void setDiffuseColor(float r, float g, float b, float a) {
	((MaterialWrapper)wrapped).setDiffuseColor( r,g,b,a );
    }

    /**
     * Retrieves this material's diffuse color.
     * @param color the vector that will receive this material's diffuse color
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void getDiffuseColor(Color3f color) {
	((MaterialWrapper)wrapped).getDiffuseColor( color );
    }

    /**
     * Sets this material's specular color.
     * This is the specular highlight color of the material.
     * The specular color in this Material object may be overridden by
     * per-vertex colors in some cases.  If vertex colors are present
     * in the geometry, and lighting is enabled, and the colorTarget
     * is SPECULAR, and vertex colors are
     * not being ignored, then the vertex colors are used in place of
     * this Material's specular color in the lighting equation.
     *
     * @param color the new specular color
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @see RenderingAttributes#setIgnoreVertexColors
     * @see #setColorTarget
     */
    public void setSpecularColor(Color3f color) {
	((MaterialWrapper)wrapped).setSpecularColor( color );
    }

    /**
     * Sets this material's specular color.
     * This is the specular highlight color of the material.
     * The specular color in this Material object may be overridden by
     * per-vertex colors in some cases.  If vertex colors are present
     * in the geometry, and lighting is enabled, and the colorTarget
     * is SPECULAR, and vertex colors are
     * not being ignored, then the vertex colors are used in place of
     * this Material's specular color in the lighting equation.
     *
     * @param r the new specular color's red component
     * @param g the new specular color's green component
     * @param b the new specular color's blue component
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @see RenderingAttributes#setIgnoreVertexColors
     * @see #setColorTarget
     */
    public void setSpecularColor(float r, float g, float b) {
	((MaterialWrapper)wrapped).setSpecularColor( r,g,b );
    }

    /**
     * Retrieves this material's specular color.
     * @param color the vector that will receive this material's specular color
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void getSpecularColor(Color3f color) {
	((MaterialWrapper)wrapped).getSpecularColor( color );
    }

    /**
     * Sets this material's shininess.
     * This specifies a material specular scattering exponent, or
     * shininess.  It takes a floating point number in the range [1.0, 128.0]
     * with 1.0 being not shiny and 128.0 being very shiny.
     * Values outside this range are clamped.
     * @param shininess the material's shininess
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setShininess(float shininess) {
	((MaterialWrapper)wrapped).setShininess( shininess );
    }

    /**
     * Retrieves this material's shininess.
     * @return the material's shininess
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public float getShininess() {
	return ((MaterialWrapper)wrapped).getShininess();
    }

    /**
     * Enables or disables lighting for this appearance component object.
     * @param state true or false to enable or disable lighting
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public void setLightingEnable(boolean state) {
	((MaterialWrapper)wrapped).setLightingEnable( state );
    }

    /**
     * Retrieves the state of the lighting enable flag.
     * @return true if lighting is enabled, false if lighting is disabled
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public boolean getLightingEnable() {
	return ((MaterialWrapper)wrapped).getLightingEnable();
    }

    /**
     * Sets the color target for per-vertex colors.  When lighting is
     * enabled and per-vertex colors are present (and not ignored) in
     * the geometry for a given Shape3D node, those per-vertex colors
     * are used in place of the specified material color(s) for this
     * Material object.  The color target is ignored when lighting is
     * disabled or when per-vertex colors are not used.
     * The ColorInterpolator behavior also uses the color target to
     * determine which color in the associated Material is modified.
     * The default target is DIFFUSE.
     *
     * @param colorTarget one of: AMBIENT, EMISSIVE, DIFFUSE, SPECULAR, or
     * AMBIENT_AND_DIFFUSE.
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @see RenderingAttributes#setIgnoreVertexColors
     * @see ColorInterpolator
     *
     * @since Java 3D 1.3
     */
    public void setColorTarget(int colorTarget) {
	((MaterialWrapper)wrapped).setColorTarget( colorTarget );
    }

    /**
     * Retrieves the current color target for this material.
     *
     * @return one of: AMBIENT, EMISSIVE, DIFFUSE, SPECULAR, or
     * AMBIENT_AND_DIFFUSE.
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.3
     */
    public int getColorTarget() {
	return ((MaterialWrapper)wrapped).getColorTarget();
    }

    /**
     * Returns a String representation of this Materials values.
     * If the scene graph is live only those values with their
     * Capability read bit set will be displayed.
     */
    public String toString() {
	return ((MaterialWrapper)wrapped).toString();
    }

}
