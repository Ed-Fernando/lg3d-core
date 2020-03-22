/**
 * Project Looking Glass
 *
 * $RCSfile: TransparencyAttributes.java,v $
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
 * $Date: 2004-06-23 18:51:25 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.rmi.rmiserver;

/**
 * The TransparencyAttributes object defines all attributes affecting
 * transparency of the object. The transparency attributes are:<P>
 * <UL>
 * <LI>Transparency mode - defines how transparency is applied to
 * this Appearance component object:</LI><P>
 * <UL>
 * <LI>FASTEST - uses the fastest available method for transparency.</LI><P>
 * <LI>NICEST - uses the nicest available method for transparency.</LI><P>
 * <LI>SCREEN_DOOR - uses screen-door transparency. This is done using 
 * an on/off stipple pattern in which the percentage of transparent pixels 
 * is approximately equal to the value specified by the transparency 
 * parameter.</LI><P>
 * <LI>BLENDED - uses alpha blended transparency. The blend equation is 
 * specified by the srcBlendFunction and dstBlendFunction attributes. 
 * The default equation is:
 * <ul>
 * <code>alpha<sub><font size=-1>src</font></sub>*src +
 * (1-alpha<sub><font size=-1>src</font></sub>)*dst</code>
 * </ul>
 * where <code>alpha<sub><font size=-1>src</font></sub></code> is
 * <code>1-transparency</code>.
 * When this mode is used with a Raster object or with a Geometry
 * that contains per-vertex colors with alpha, the alpha values in
 * the Raster's image or in the Geometry's per-vertex colors are
 * combined with the transparency value in this TransparencyAttributes
 * object to perform blending.  In this case, the alpha value used for
 * blending at each pixel is:
 * <ul>
 * <code>alpha<sub><font size=-1>src</font></sub> =
 * alpha<sub><font size=-1>pix</font></sub> *
 * (1-transparency)</code>.
 * </ul>
 * </LI><P>
 * <LI>NONE - no transparency; opaque object.</LI><P>
 * </UL>
 * <LI>Blend function - used in blended transparency and antialiasing
 * operations. The source function specifies the factor that is 
 * multiplied by the source color. This value is added to the product 
 * of the destination factor and the destination color. The default 
 * source blend function is BLEND_SRC_ALPHA. The source blend function 
 * is one of the following:</LI><P>
 * <UL>
 * <LI>BLEND_ZERO - the blend function is <code>f = 0</code>.</LI>
 * <LI>BLEND_ONE - the blend function is <code>f = 1</code>.</LI>
 * <LI>BLEND_SRC_ALPHA - the blend function is <code>f = 
 * alpha<sub><font size=-1>src</font></sub></code>.</LI>
 * <LI>BLEND_ONE_MINUS_SRC_ALPHA - the blend function is <code>f = 
 * 1 - alpha<sub><font size=-1>src</font></sub></code>.</LI></UL><P>
 * <LI>Blend value - the amount of transparency to be applied to this
 * Appearance component object. The transparency values are in the
 * range [0.0, 1.0], with 0.0 being fully opaque and 1.0 being
 * fully transparent.</LI><P>
 * </UL>
 */
public class TransparencyAttributes extends NodeComponent implements TransparencyAttributesRemote {

    /**
     * Constructs a TransparencyAttributes object with default parameters.
     * The default values are as follows:
     * <ul>
     * transparency mode : <code>NONE</code><br>
     * transparency value : 0.0<br>
     * source blend function : <code>BLEND_SRC_ALPHA</code><br>
     * destination blend function : <code>BLEND_ONE_MINUS_SRC_ALPHA</code><br>
     * </ul>
     */
    public TransparencyAttributes() throws java.rmi.RemoteException {
    }
    
    protected void createWrapped() {
	wrapped = new org.jdesktop.lg3d.sg.TransparencyAttributes();
        wrapped.setUserData( this );
    }

    /**
     * Construct TransparencyAttributes object with specified values.
     * @param tMode the transparency mode
     * @param tVal the transparency value
     * @exception IllegalArgumentException if
     * <code>tMode</code> is a value other than
     * <code>NONE</code>, <code>FASTEST</code>, <code>NICEST</code>, 
     * <code>SCREEN_DOOR</code>, or <code>BLENDED</code>
     * 
     */
    public TransparencyAttributes(int tMode, float tVal) throws java.rmi.RemoteException {
	this(tMode, tVal, BLEND_SRC_ALPHA, BLEND_ONE_MINUS_SRC_ALPHA);
     }

    /**
     * Construct TransparencyAttributes object with specified values.
     * @param tMode the transparency mode
     * @param tVal the transparency value
     * @param srcBlendFunction the blend function to be used for the source
     * color, one of <code>BLEND_ZERO</code>, <code>BLEND_ONE</code>,
     * <code>BLEND_SRC_ALPHA</code>, or <code>BLEND_ONE_MINUS_SRC_ALPHA</code>.
     * @param dstBlendFunction the blend function to be used for the
     * destination
     * color, one of <code>BLEND_ZERO</code>, <code>BLEND_ONE</code>,
     * <code>BLEND_SRC_ALPHA</code>, or <code>BLEND_ONE_MINUS_SRC_ALPHA</code>.
     * @exception IllegalArgumentException if
     * <code>tMode</code> is a value other than
     * <code>NONE</code>, <code>FASTEST</code>, <code>NICEST</code>, 
     * <code>SCREEN_DOOR</code>, or <code>BLENDED</code>
     * @exception IllegalArgumentException if
     * <code>srcBlendFunction</code> or <code>dstBlendFunction</code>
     * is a value other than <code>BLEND_ZERO</code>, <code>BLEND_ONE</code>,
     * <code>BLEND_SRC_ALPHA</code>, or
     * <code>BLEND_ONE_MINUS_SRC_ALPHA</code>.
     *
     * @since Java 3D 1.2
     */
    public TransparencyAttributes(int tMode,
				  float tVal,
				  int srcBlendFunction,
				  int dstBlendFunction) throws java.rmi.RemoteException {
	setTransparencyMode(tMode);
        setTransparency(tVal);
        setSrcBlendFunction(srcBlendFunction);
        setDstBlendFunction(dstBlendFunction);
    }

    /**
     * Sets the transparency mode for this
     * appearance component object.
     * @param transparencyMode the transparency mode to be used, one of
     * <code>NONE</code>, <code>FASTEST</code>, <code>NICEST</code>, 
     * <code>SCREEN_DOOR</code>, or <code>BLENDED</code>
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     * @exception IllegalArgumentException if
     * <code>transparencyMode</code> is a value other than
     * <code>NONE</code>, <code>FASTEST</code>, <code>NICEST</code>, 
     * <code>SCREEN_DOOR</code>, or <code>BLENDED</code>
     */
    public void setTransparencyMode(int transparencyMode) throws java.rmi.RemoteException {
        ((org.jdesktop.lg3d.sg.TransparencyAttributes)wrapped).setTransparencyMode(transparencyMode);
    }



    /**
     * Gets the transparency mode for this
     * appearance component object.
     * @return transparencyMode the transparency mode
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public int getTransparencyMode() throws java.rmi.RemoteException {
	return ((org.jdesktop.lg3d.sg.TransparencyAttributes)wrapped).getTransparencyMode();
    }

    /**
     * Sets this appearance's transparency.
     * @param transparency the appearance's transparency
     * in the range [0.0, 1.0] with 0.0 being
     * fully opaque and 1.0 being fully transparent
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setTransparency(float transparency) throws java.rmi.RemoteException {
        ((org.jdesktop.lg3d.sg.TransparencyAttributes)wrapped).setTransparency(transparency);
    }
    

    /**
     * Retrieves this appearance's transparency.
     * @return the appearance's transparency
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public float getTransparency() throws java.rmi.RemoteException {
	return ((org.jdesktop.lg3d.sg.TransparencyAttributes)wrapped).getTransparency();
    }

    /**
     * Sets the source blend function used in blended transparency
     * and antialiasing operations.  The source function specifies the
     * factor that is multiplied by the source color; this value is
     * added to the product of the destination factor and the
     * destination color.  The default source blend function is
     * <code>BLEND_SRC_ALPHA</code>.
     *
     * @param blendFunction the blend function to be used for the source
     * color, one of <code>BLEND_ZERO</code>, <code>BLEND_ONE</code>,
     * <code>BLEND_SRC_ALPHA</code>, or <code>BLEND_ONE_MINUS_SRC_ALPHA</code>.
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     * @exception IllegalArgumentException if
     * <code>blendFunction</code>
     * is a value other than <code>BLEND_ZERO</code>, 
     * <code>BLEND_ONE</code>,
     * <code>BLEND_SRC_ALPHA</code>, or
     * <code>BLEND_ONE_MINUS_SRC_ALPHA</code>.
     *
     * @since Java 3D 1.2
     */
    public void setSrcBlendFunction(int blendFunction) throws java.rmi.RemoteException {
        ((org.jdesktop.lg3d.sg.TransparencyAttributes)wrapped).setSrcBlendFunction(blendFunction);
    }



    /**
     * Gets the source blend function for this
     * TransparencyAttributes object.
     * @return the source blend function.
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.2
     */
    public int getSrcBlendFunction() throws java.rmi.RemoteException {
	return ((org.jdesktop.lg3d.sg.TransparencyAttributes)wrapped).getSrcBlendFunction();
    }

    /**
     * Sets the destination blend function used in blended transparency
     * and antialiasing operations.  The destination function specifies the
     * factor that is multiplied by the destination color; this value is
     * added to the product of the source factor and the
     * source color.  The default destination blend function is
     * <code>BLEND_ONE_MINUS_SRC_ALPHA</code>.
     *
     * @param blendFunction the blend function to be used for the destination
     * color, one of <code>BLEND_ZERO</code>, <code>BLEND_ONE</code>,
     * <code>BLEND_SRC_ALPHA</code>, or <code>BLEND_ONE_MINUS_SRC_ALPHA</code>.
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     * @exception IllegalArgumentException if
     * <code>blendFunction</code>
     * is a value other than <code>BLEND_ZERO</code>, 
     * <code>BLEND_ONE</code>,
     * <code>BLEND_SRC_ALPHA</code>, or
     * <code>BLEND_ONE_MINUS_SRC_ALPHA</code>.
     *
     * @since Java 3D 1.2
     */
    public void setDstBlendFunction(int blendFunction) throws java.rmi.RemoteException {
        ((org.jdesktop.lg3d.sg.TransparencyAttributes)wrapped).setDstBlendFunction(blendFunction);
    }



    /**
     * Gets the destination blend function for this
     * TransparencyAttributes object.
     * @return the destination blend function.
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.2
     */
    public int getDstBlendFunction() throws java.rmi.RemoteException {
	return ((org.jdesktop.lg3d.sg.TransparencyAttributes)wrapped).getDstBlendFunction();
    }

}
