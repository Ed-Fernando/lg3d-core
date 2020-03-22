/**
 * Project Looking Glass
 *
 * $RCSfile: Appearance.java,v $
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
 * $Revision: 1.3 $
 * $Date: 2005-01-20 22:05:43 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.rmi.rmiserver;

import java.util.Hashtable;

/**
 * The Appearance object defines all rendering state that can be set
 * as a component object of a Shape3D node. The rendering state 
 * consists of the following:<p>
 * <ul>
 * <li>Coloring attributes - defines attributes used in color selection
 * and shading. These attributes are defined in a ColoringAttributes
 * object.</li><p>
 * 
 * <li>Line attributes - defines attributes used to define lines, including
 * the pattern, width, and whether antialiasing is to be used. These
 * attributes are defined in a LineAttributes object.</li><p>
 *
 * <li>Point attributes - defines attributes used to define points,
 * including the size and whether antialiasing is to be used. These
 * attributes are defined in a PointAttributes object.</li><p>
 *
 * <li>Polygon attributes - defines the attributes used to define
 * polygons, including culling, rasterization mode (filled, lines,
 * or points), constant offset, offset factor, and whether back
 * back facing normals are flipped. These attributes are defined
 * in a PolygonAttributes object.</li><p>
 *
 * <li>Rendering attributes - defines rendering operations,
 * including the alpha test function and test value, the raster
 * operation, whether vertex colors are ignored, whether invisible
 * objects are rendered, and whether the depth buffer is enabled.
 * These attributes are defined in a RenderingAttributes 
 * object.</li><p>
 *
 * <li>Transparency attributes - defines the attributes that affect
 * transparency of the object, such as the transparency mode 
 * (blended, screen-door), blending function (used in transparency
 * and antialiasing operations), and a blend value that defines
 * the amount of transparency to be applied to this Appearance 
 * component object.</li><p>
 * 
 * <li>Material - defines the appearance of an object under illumination,
 * such as the ambient color, diffuse color, specular color, emissive
 * color, and shininess. These attributes are defined in a Material
 * object.</li><p>
 *
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
 *
 * <li>Texture unit state - array that defines texture state for each
 * of <i>N</i> separate texture units.  This allows multiple textures
 * to be applied to geometry.  Each TextureUnitState object contains a
 * Texture object, TextureAttributes, and TexCoordGeneration object
 * for one texture unit.  If the length of the texture unit state
 * array is greater than 0, then the array is used for all texture
 * state; the individual Texture, TextureAttributes, and
 * TexCoordGeneration objects in this Appearance object are not used
 * and and must not be set by an application. If the length of the
 * texture unit state array is 0, the multi-texture is disabled and
 * the Texture, TextureAttributes, and TexCoordGeneration objects
 * in the Appearance object are used. If the application sets the
 * existing Texture, TextureAttributes, and TexCoordGeneration
 * objects to non-null values, they effectively define the state
 * for texture unit 0. If the TextureUnitState array is set to a
 * non-null, non-empty array, the individual TextureUnitState
 * objects define the state for texture units 0 through <i>n</i>
 * -1. If both the old and new values are set, an exception is thrown.
 *
 * </li>
 * </ul>
 *
 * @see ColoringAttributes
 * @see LineAttributes
 * @see PointAttributes
 * @see PolygonAttributes
 * @see RenderingAttributes
 * @see TransparencyAttributes
 * @see Material
 * @see Texture
 * @see TextureAttributes
 * @see TexCoordGeneration
 * @see TextureUnitState
 */
public class Appearance extends NodeComponent implements AppearanceRemote {

    /**
     * Constructs an Appearance component object using defaults for all
     * state variables. All component object references are initialized 
     * to null.
     */
    public Appearance() throws java.rmi.RemoteException {
    }
    
    protected void createWrapped() {
	wrapped = new org.jdesktop.lg3d.sg.Appearance();
        wrapped.setUserData( this );
    }

    /**
     * Sets the material object to the specified object.
     * Setting it to null disables lighting.
     * @param material object that specifies the desired material
     * properties
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public void setMaterial(MaterialRemote material) throws java.rmi.RemoteException {
        if (material==null)
            ((org.jdesktop.lg3d.sg.Appearance)wrapped).setMaterial( null );
        else
            ((org.jdesktop.lg3d.sg.Appearance)wrapped).setMaterial( (org.jdesktop.lg3d.sg.Material)getLocal(material).wrapped );
    }

    /**
     * Retrieves the current material object.
     * @return the material object
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public MaterialRemote getMaterial() throws java.rmi.RemoteException {
	org.jdesktop.lg3d.sg.SceneGraphObject obj = ((org.jdesktop.lg3d.sg.Appearance)wrapped).getMaterial();
        
        if (obj==null)
            return null;
        
        return (Material)obj.getUserData();
    }

    /**
     * Sets the coloringAttributes object to the specified object.
     * Setting it to null will result in default attribute usage.
     * @param coloringAttributes object that specifies the desired
     * coloringAttributes parameters
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public void setColoringAttributes(ColoringAttributesRemote coloringAttributes)  throws java.rmi.RemoteException {
        if (coloringAttributes==null)
            ((org.jdesktop.lg3d.sg.Appearance)wrapped).setColoringAttributes( null );
        else    
            ((org.jdesktop.lg3d.sg.Appearance)wrapped).setColoringAttributes( (org.jdesktop.lg3d.sg.ColoringAttributes)getLocal(coloringAttributes).wrapped );
    }

    /**
     * Retrieves the current coloringAttributes object.
     * @return the coloringAttributes object
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public ColoringAttributesRemote getColoringAttributes()  throws java.rmi.RemoteException {
	org.jdesktop.lg3d.sg.SceneGraphObject obj = ((org.jdesktop.lg3d.sg.Appearance)wrapped).getColoringAttributes();
        
        if (obj==null)
            return null;
        
        return (ColoringAttributes)obj.getUserData();
    }

    /**
     * Sets the lineAttributes object to the specified object.
     * Setting it to null will result in default attribute usage.
     * @param lineAttributes object that specifies the desired
     * lineAttributes parameters
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public void setLineAttributes(LineAttributesRemote lineAttributes) {
        if (lineAttributes==null)
            ((org.jdesktop.lg3d.sg.Appearance)wrapped).setLineAttributes( null );
        else    
            ((org.jdesktop.lg3d.sg.Appearance)wrapped).setLineAttributes( (org.jdesktop.lg3d.sg.LineAttributes)((LineAttributes)lineAttributes).wrapped );
    }

    /**
     * Retrieves the current lineAttributes object.
     * @return the lineAttributes object
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public LineAttributes getLineAttributes() {
	org.jdesktop.lg3d.sg.SceneGraphObject obj = ((org.jdesktop.lg3d.sg.Appearance)wrapped).getLineAttributes();
        
        if (obj==null)
            return null;
        
        return (LineAttributes)obj.getUserData();
    }
    
    /**
     * Sets the transparencyAttributes object to the specified object.
     * Setting it to null will result in default attribute usage.
     * @param transparencyAttributes object that specifies the desired
     * transparencyAttributes parameters
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public void setTransparencyAttributes(TransparencyAttributesRemote transparencyAttributes)  throws java.rmi.RemoteException {
        if (transparencyAttributes==null)
            ((org.jdesktop.lg3d.sg.Appearance)wrapped).setTransparencyAttributes( null );
        else
            ((org.jdesktop.lg3d.sg.Appearance)wrapped).setTransparencyAttributes( (org.jdesktop.lg3d.sg.TransparencyAttributes)getLocal(transparencyAttributes).wrapped );
    }

    /**
     * Retrieves the current transparencyAttributes object.
     * @return the transparencyAttributes object
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public TransparencyAttributesRemote getTransparencyAttributes()  throws java.rmi.RemoteException {
	org.jdesktop.lg3d.sg.SceneGraphObject obj = ((org.jdesktop.lg3d.sg.Appearance)wrapped).getTransparencyAttributes();
        
        if (obj==null)
            return null;
        
        return (TransparencyAttributesRemote)obj.getUserData();
    }

    /**
     * Sets the renderingAttributes object to the specified object.
     * Setting it to null will result in default attribute usage.
     * @param renderingAttributes object that specifies the desired
     * renderingAttributes parameters
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public void setRenderingAttributes(RenderingAttributesRemote renderingAttributes)  throws java.rmi.RemoteException {
        if (renderingAttributes==null)
            ((org.jdesktop.lg3d.sg.Appearance)wrapped).setRenderingAttributes( null );
        else
            ((org.jdesktop.lg3d.sg.Appearance)wrapped).setRenderingAttributes( (org.jdesktop.lg3d.sg.RenderingAttributes)getLocal(renderingAttributes).wrapped );
    }

    /**
     * Retrieves the current renderingAttributes object.
     * @return the renderingAttributes object
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public RenderingAttributesRemote getRenderingAttributes()  throws java.rmi.RemoteException {
	org.jdesktop.lg3d.sg.SceneGraphObject obj = ((org.jdesktop.lg3d.sg.Appearance)wrapped).getRenderingAttributes();
        
        if (obj==null)
            return null;
        
        return (RenderingAttributesRemote)obj.getUserData();
    }

    /**
     * Sets the polygonAttributes object to the specified object.
     * Setting it to null will result in default attribute usage.
     * @param polygonAttributes object that specifies the desired 
     * polygonAttributes parameters
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public void setPolygonAttributes(PolygonAttributesRemote polygonAttributes)  throws java.rmi.RemoteException {
        if (polygonAttributes==null)
            ((org.jdesktop.lg3d.sg.Appearance)wrapped).setPolygonAttributes( null );
        else
            ((org.jdesktop.lg3d.sg.Appearance)wrapped).setPolygonAttributes( (org.jdesktop.lg3d.sg.PolygonAttributes)getLocal(polygonAttributes).wrapped );
    }

    /**
     * Retrieves the current polygonAttributes object.
     * @return the polygonAttributes object
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public PolygonAttributesRemote getPolygonAttributes()  throws java.rmi.RemoteException {
	org.jdesktop.lg3d.sg.SceneGraphObject obj = ((org.jdesktop.lg3d.sg.Appearance)wrapped).getPolygonAttributes();
        
        if (obj==null)
            return null;
        
        return (PolygonAttributesRemote)obj.getUserData();
    }

    /**
     * Sets the texture object to the specified object.
     * Setting it to null disables texture mapping.
     *
     * <p>
     * Applications must not set individual texture component objects
     * (texture, textureAttributes, or texCoordGeneration) and
     * the texture unit state array in the same Appearance object.
     * Doing so will result in an exception being thrown.
     *
     * @param texture object that specifies the desired texture
     * map and texture parameters
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     *
     * @exception IllegalStateException if the specified texture
     * object is non-null and the texture unit state array in this
     * appearance object is already non-null.
     */
    public void setTexture(TextureRemote texture)  throws java.rmi.RemoteException {
        if (texture==null)
            ((org.jdesktop.lg3d.sg.Appearance)wrapped).setTexture( null );
        else
            ((org.jdesktop.lg3d.sg.Appearance)wrapped).setTexture( (org.jdesktop.lg3d.sg.Texture)getLocal(texture).wrapped );
    }

    /**
     * Retrieves the current texture object.
     * @return the texture object
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public TextureRemote getTexture()  throws java.rmi.RemoteException {
	org.jdesktop.lg3d.sg.SceneGraphObject obj = ((org.jdesktop.lg3d.sg.Appearance)wrapped).getTexture();
        
        if (obj==null)
            return null;
        
        return (TextureRemote)obj.getUserData();
    }

    /**
     * Sets the textureAttributes object to the specified object.
     * Setting it to null will result in default attribute usage.
     *
     * <p>
     * Applications must not set individual texture component objects
     * (texture, textureAttributes, or texCoordGeneration) and
     * the texture unit state array in the same Appearance object.
     * Doing so will result in an exception being thrown.
     *
     * @param textureAttributes object that specifies the desired
     * textureAttributes map and textureAttributes parameters
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     *
     * @exception IllegalStateException if the specified textureAttributes
     * object is non-null and the texture unit state array in this
     * appearance object is already non-null.
     */
    public void setTextureAttributes(TextureAttributesRemote textureAttributes)  throws java.rmi.RemoteException {
        if (textureAttributes==null)
            ((org.jdesktop.lg3d.sg.Appearance)wrapped).setTextureAttributes( null );
        else
            ((org.jdesktop.lg3d.sg.Appearance)wrapped).setTextureAttributes( (org.jdesktop.lg3d.sg.TextureAttributes)getLocal(textureAttributes).wrapped );
    }

    /**
     * Retrieves the current textureAttributes object.
     * @return the textureAttributes object
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public TextureAttributesRemote getTextureAttributes()  throws java.rmi.RemoteException {
	org.jdesktop.lg3d.sg.SceneGraphObject obj = ((org.jdesktop.lg3d.sg.Appearance)wrapped).getTextureAttributes();
        
        if (obj==null)
            return null;
        
        return (TextureAttributesRemote)obj.getUserData();
    }

    /**
     * Sets the texture unit state array for this appearance object to the
     * specified array.  A shallow copy of the array of references to
     * the TextureUnitState objects is made.  If the specified array
     * is null or if the length of the array is 0, multi-texture is
     * disabled.  Within the array, a null TextureUnitState element
     * disables the corresponding texture unit.
     *
     * <p>
     * Applications must not set individual texture component objects
     * (texture, textureAttributes, or texCoordGeneration) and
     * the texture unit state array in the same Appearance object.
     * Doing so will result in an exception being thrown.
     *
     * @param stateArray array of TextureUnitState objects that
     * specify the desired texture state for each unit.  The length of
     * this array specifies the maximum number of texture units that
     * will be used by this appearance object.  The texture units are
     * numbered from <code>0</code> through
     * <code>stateArray.length-1</code>.
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     *
     * @exception IllegalStateException if the specified array is
     * non-null and any of the texture object, textureAttributes
     * object, or texCoordGeneration object in this appearance object
     * is already non-null.
     *
     * @since Java 3D 1.2
     */
//    public void setTextureUnitState(TextureUnitState[] stateArray) {
//	if (isLiveOrCompiled())
//	  if (!this.getCapability(ALLOW_TEXTURE_UNIT_STATE_WRITE))
//		throw new CapabilityNotSetException(J3dI18N.getString("Appearance20"));
//
//	((AppearanceRetained)this.retained).setTextureUnitState(stateArray);
//    }

    /**
     * Sets the texture unit state object at the specified index
     * within the texture unit state array to the specified object.
     * If the specified object is null, the corresponding texture unit
     * is disabled.  The index must be within the range
     * <code>[0,&nbsp;stateArray.length-1]</code>.
     *
     * @param index the array index of the object to be set
     *
     * @param state new texture unit state object
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     * @exception NullPointerException if the texture unit state array is
     * null.
     * @exception ArrayIndexOutOfBoundsException if <code>index >=
     * stateArray.length</code>.
     *
     * @since Java 3D 1.2
     */
//    public void setTextureUnitState(int index, TextureUnitState state) {
//	if (isLiveOrCompiled())
//	  if (!this.getCapability(ALLOW_TEXTURE_UNIT_STATE_WRITE))
//		throw new CapabilityNotSetException(J3dI18N.getString("Appearance20"));
//
//	((AppearanceRetained)this.retained).setTextureUnitState(index, state);
//    }

    /**
     * Retrieves the array of texture unit state objects from this
     * Appearance object.  A shallow copy of the array of references to
     * the TextureUnitState objects is returned.
     *
     * @return the array of texture unit state objects
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.2
     */
//    public TextureUnitState[] getTextureUnitState() {
//	if (isLiveOrCompiled())
//	  if (!this.getCapability(ALLOW_TEXTURE_UNIT_STATE_READ))
//		throw new CapabilityNotSetException(J3dI18N.getString("Appearance21"));
//
//	return ((AppearanceRetained)this.retained).getTextureUnitState();
//    }

    /**
     * Retrieves the texture unit state object at the specified
     * index within the texture unit state array.  The index must be
     * within the range <code>[0,&nbsp;stateArray.length-1]</code>.
     *
     * @param index the array index of the object to be retrieved
     *
     * @return the texture unit state object at the specified index
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.2
     */
//    public TextureUnitState getTextureUnitState(int index) {
//	org.jdesktop.lg3d.sg.SceneGraphObject obj = ((org.jdesktop.lg3d.sg.Appearance)wrapped).getTextureUnitState( index );
//        
//        if (obj==null)
//            return null;
//        
//        return (TextureUnitState)obj.getUserData();
//    }

    /**
     * Retrieves the length of the texture unit state array from
     * this appearance object.  The length of this array specifies the
     * maximum number of texture units that will be used by this
     * appearance object.  If the array is null, a count of 0 is
     * returned.
     *
     * @return the length of the texture unit state array
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.2
     */
//    public int getTextureUnitCount() {
//	return ((org.jdesktop.lg3d.sg.Appearance)wrapped).getTextureUnitCount();
//    }

}
