/**
 * Project Looking Glass
 *
 * $RCSfile: Light.java,v $
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
 * $Date: 2004-06-23 18:50:39 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.j3d.j3dwrapper;

import javax.vecmath.Color3f;
import java.util.Enumeration;

import org.jdesktop.lg3d.sg.internal.wrapper.*;

/**
 * Light node.
 *
 * @version     1.63, 02/04/01 14:57:45
 */

/**
 * The Light leaf node is an abstract class that defines a set of 
 * parameters common to all
 * types of light.  These parameters include the light color, an enable
 * flag, and a region of influence in which this Light node is active.
 * A Light node also contains a list of Group nodes that specifies the
 * hierarchical scope of this Light.  If the scope list is empty, 
 * the Light node has universe scope: all nodes within the region of
 * influence are affected by this Light node.  If the scope list is
 * non-empty, only those Leaf nodes under the Group nodes in the
 * scope list are affected by this Light node (subject to the
 * influencing bounds).
 * <p>
 * The light in a scene may come from several light sources that can
 * be individually defined. Some of the light in a scene may
 * come from a specific direction, known as a directional light,
 * from a specific position, known as a point light, or
 * from no particular direction or source as with ambient light.
 * <p>
 * Java 3D supports an arbitrary number of lights. However, the number
 * of lights that can be active within the region of influence is
 * implementation-dependent and cannot be defined here.
 * <p>
 * <b>Light Color</b>
 * <p>
 * The Java 3D lighting model approximates the way light works in
 * the real world. Light is defined in terms of the red, green, and 
 * blue components that combine to create the light color. The
 * three color components represent the amount of light emitted
 * by the source.
 * <p>
 * Each of the three colors is represented by a
 * floating point value that ranges from 0.0 to 1.0. A combination
 * of the three colors such as (1.0, 1.0, 1.0), representing
 * the red, green, and blue color values respectively, creates a white
 * light with maximum brightness. A combination such as (0.0, 0.0,
 * 0.0) creates no light (black). Values between the minimum and
 * maximum values of the range produce corresponding brightness
 * and colors. For example, a combination of (0.5, 0.5, 0.5)
 * produces a 50% grey light. A combination of (1.0, 1.0, 0.0),
 * red and green but no blue, produces a yellow light.
 * <p>
 * If a scene has multiple lights and all lights illuminate an object,
 * the effect of the light on the object is the sum of the 
 * lights. For example, in a scene with two lights, if the first 
 * light emits (R<sub>1</sub>, G<sub>1</sub>, B<sub>1</sub>) and 
 * the second light emits (R<sub>2</sub>, G<sub>2</sub>, 
 * B<sub>2</sub>), the components are added together giving 
 * (R<sub>1</sub>+R<sub>2</sub>, G<sub>1</sub>+G<sub>2</sub>, 
 * B<sub>1</sub>+B<sub>2</sub>).
 * If the sums of any of the color values is greater than 1.0,
 * brighter than the maximum brightness permitted, the color value is
 * clamped to 1.0.
 * <p>
 * <b>Material Colors</b>
 * <p>
 * In the Java 3D lighting model, the light sources have an effect
 * on the scene only when there are object surfaces to absorb or
 * reflect the light. Java 3D approximates an object's color
 * by calculating the percentage of red, green, and blue light
 * the object reflects. An object with a surface color of pure green
 * absorbs all of the red and blue light that strikes it and
 * reflects all of the green light. Viewing the object in a
 * white light, the green color is reflected and you see a green
 * object. However, if the green object is viewed in a red light,
 * all of the red light is absorbed and the object appears black.
 * <p>
 * The surface of each object in the scene has
 * certain material properties that define how light affects its
 * appearance. The object might reflect light in various ways, 
 * depending on the object's surface type. The object
 * might even emit its own light. The Java 3D lighting model specifies 
 * these material properties as five independent components: emitted
 * color, ambient color, diffuse color, specular color, and shininess.
 * All of these properties are computed independently, then added 
 * together to define how the surface of the object appears under
 * light (an exception is Ambient light, which does not contribute 
 * to specular reflection). The material properties are defined 
 * in the Material class.
 * <p>
 * <b>Influencing Bounds</b>
 * <p>
 * Since a scene may be quite large, as large as the universe for
 * example, it is often reasonable to limit the influence of lighting
 * to a region that is within viewing range. There is no reason
 * to waste all that computing power on illuminating objects that
 * are too far away to be viewed. In Java 3D, the influencing bounds
 * is defined by a Bounds object or a BoundingLeaf object. It should
 * be noted that a BoundingLeaf object overrides a Bounds object,
 * should both objects be set.
 * <p>
 * A Bounds object represents a convex, closed volume. Bounds
 * defines three different types of containing 
 * volumes: an axis-aligned-box volume, a spherical volume, and a 
 * bounding polytope. A BoundingLeaf object also specifies a region
 * of influence, but allows an application to specify a bounding 
 * region in one coordinate system (the local coordinate system of 
 * the BoundingLeaf node) other than the local coordinate
 * system of the node that references the bounds (the Light).
 * <p>
 * <b>Limiting the Scope</b>
 * <p>
 * In addition to limiting the lighting calculations to a given
 * region of a scene, lighting can also be limited to groups of
 * nodes, defined by a Group object. This is known as "scoping."
 * All nodes attached to a Group node define a <i>list of scopes</i>.
 * Methods in the Light class permit the setting, addition, insertion, 
 * removal, and enumeration of nodes in the list of scopes.
 * <p>
 * <b>Two-sided Lighting of Polygons</b>
 * <p>
 * Java 3D performs lighting calculations for all polygons, whether
 * they are front-facing or back-facing. Since most polygon objects
 * are constructed with the front face in mind, the back-facing
 * portion may not be correctly illuminated. For example, a sphere
 * with part of the face cut away so you can see its inside.
 * You might want to have the inside surface lit as well as the
 * outside surface and you mught also want to define a different
 * Material description to reduce shininess, specular color, etc.
 * <p>
 * For more information, see the "Face culling" and "Back-facing
 * normal flip" descriptions in the PolygonAttributes class
 * description.
 * <p>
 * <b>Turning on the Lights</b>
 * <p>
 * Lighting needs to be explicitly enabled with the setEnable method
 * or with the lightOn parameter in the constructor
 * before any of the child light sources have any effect on illuminating
 * the scene. The child lights may also be enabled or disabled individually.
 * <p>
 * If lighting is not enabled, the current color of an
 * object in the scene is simply mapped onto the object, and none of
 * the lighting equations regarding Material properties, such as ambient 
 * color, diffuse color, specular color, and shininess, are performed.
 * However, an object's emitted color, if specified and enabled, will
 * still affect that object's appearance.
 * <p>
 * To disable lighting, call setEnable with <code>false</code> as
 * the argument.
 * 
 * @see Material
 * @see Bounds
 * @see BoundingLeaf
 * @see Group
 * @see PolygonAttributes
 */

public abstract class Light extends Leaf implements LightWrapper {

    /**
     * Constructs a Light node with default parameters.  The default
     * values are as follows:
     * <ul>
     * enable flag : true<br>
     * color : white (1,1,1)<br>
     * scope : empty (universe scope)<br>
     * influencing bounds : null<br>
     * influencing bounding leaf : null<br>
     * </ul>
     */
    Light() {
    }

    /**
     * Constructs and initializes a Light node using the specified color.
     * @param color the color of the light source
     */
    Light(Color3f color) {
    }

    /**
     * Constructs and initializes a Light node using the specified enable
     * flag and color.
     * @param lightOn flag indicating whether this light is on or off
     * @param color the color of the light source
     */
    Light(boolean lightOn, Color3f color) {
    }

    /**
     * Turns the light on or off.
     * @param state true or false to set light on or off
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setEnable(boolean state) {
	((javax.media.j3d.Light)wrapped).setEnable( state );
    }

    /**
     * Retrieves this Light's current state (on/off).
     * @return this node's current state (on/off)
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public boolean getEnable() {
	return ((javax.media.j3d.Light)wrapped).getEnable();
    }

    /**
     * Sets the Light's current color.
     * @param color the value of this node's new color
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setColor(Color3f color) {
	((javax.media.j3d.Light)wrapped).setColor( color );
    }

    /**
     * Gets this Light's current color and places it in the parameter specified.
     * @param color the vector that will receive this node's color
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void getColor(Color3f color) {
	((javax.media.j3d.Light)wrapped).getColor( color );
    }

    /**
     * Replaces the node at the specified index in this Light node's
     * list of scopes with the specified Group node.
     * By default, Light nodes are scoped only by their influencing
     * bounds.  This allows them to be further scoped by a list of
     * nodes in the hierarchy.
     * @param scope the Group node to be stored at the specified index.
     * @param index the index of the Group node to be replaced.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     * @exception RestrictedAccessException if the specified group node
     * is part of a compiled scene graph
     */
    public void setScope(GroupWrapper scope, int index) {
	((javax.media.j3d.Light)wrapped).setScope( (javax.media.j3d.Group)((Group)scope).wrapped, index );
    }


    /**
     * Retrieves the Group node at the specified index from this Light node's
     * list of scopes.
     * @param index the index of the Group node to be returned.
     * @return the Group node at the specified index.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public GroupWrapper getScope(int index) {
	return (GroupWrapper)((javax.media.j3d.Light)wrapped).getScope(index).getUserData();
    }


    /**
     * Inserts the specified Group node into this Light node's
     * list of scopes at the specified index.
     * By default, Light nodes are scoped only by their influencing
     * bounds.  This allows them to be further scoped by a list of
     * nodes in the hierarchy.
     * @param scope the Group node to be inserted at the specified index.
     * @param index the index at which the Group node is inserted.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     * @exception RestrictedAccessException if the specified group node
     * is part of a compiled scene graph
     */
    public void insertScope(GroupWrapper scope, int index) {
	((javax.media.j3d.Light)wrapped).insertScope( (javax.media.j3d.Group)((Group)scope).wrapped, index );
    }


    /**
     * Removes the node at the specified index from this Light node's
     * list of scopes.  If this operation causes the list of scopes to
     * become empty, then this Light will have universe scope: all nodes
     * within the region of influence will be affected by this Light node.
     * @param index the index of the Group node to be removed.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     * @exception RestrictedAccessException if the group node at the
     * specified index is part of a compiled scene graph
     */
    public void removeScope(int index) {
	((javax.media.j3d.Light)wrapped).removeScope( index );
    }


    /**
     * Returns an enumeration of this Light node's list of scopes.
     * @return an Enumeration object containing all nodes in this Light node's
     * list of scopes.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public Enumeration getAllScopes() {
	Enumeration e = ((javax.media.j3d.Light)wrapped).getAllScopes();
        java.util.Vector v = new java.util.Vector();
        while(e.hasMoreElements())
            v.add( ((javax.media.j3d.Group)e.nextElement()).getUserData() );
        
        return v.elements();
    }


    /**
     * Appends the specified Group node to this Light node's list of scopes.
     * By default, Light nodes are scoped only by their influencing
     * bounds.  This allows them to be further scoped by a list of
     * nodes in the hierarchy.
     * @param scope the Group node to be appended.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     * @exception RestrictedAccessException if the specified group node
     * is part of a compiled scene graph
     */
    public void addScope(GroupWrapper scope) {
	((javax.media.j3d.Light)wrapped).addScope( (javax.media.j3d.Group)((Group)scope).wrapped );
    }


    /**
     * Returns the number of nodes in this Light node's list of scopes.
     * If this number is 0, then the list of scopes is empty and this
     * Light node has universe scope: all nodes within the region of
     * influence are affected by this Light node.
     * @return the number of nodes in this Light node's list of scopes.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public int numScopes() {
	return ((javax.media.j3d.Light)wrapped).numScopes();
    }


    /**
     * Retrieves the index of the specified Group node in this
     * Light node's list of scopes.
     *
     * @param scope the Group node to be looked up.
     * @return the index of the specified Group node;
     * returns -1 if the object is not in the list.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     *
     * @since Java 3D 1.3
     */
    public int indexOfScope(GroupWrapper scope) {
	return ((javax.media.j3d.Light)wrapped).indexOfScope( (javax.media.j3d.Group)((Group)scope).wrapped );
    }


    /**
     * Removes the specified Group node from this Light
     * node's list of scopes.  If the specified object is not in the
     * list, the list is not modified.  If this operation causes the
     * list of scopes to become empty, then this Light
     * will have universe scope: all nodes within the region of
     * influence will be affected by this Light node.
     *
     * @param scope the Group node to be removed.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     * @exception RestrictedAccessException if the specified group node
     * is part of a compiled scene graph
     *
     * @since Java 3D 1.3
     */
    public void removeScope(GroupWrapper scope) {
	((javax.media.j3d.Light)wrapped).removeScope( (javax.media.j3d.Group)((Group)scope).wrapped );
    }


    /**
     * Removes all Group nodes from this Light node's
     * list of scopes.  The Light node will then have
     * universe scope: all nodes within the region of influence will
     * be affected by this Light node.
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     * @exception RestrictedAccessException if any group node in this
     * node's list of scopes is part of a compiled scene graph
     *
     * @since Java 3D 1.3
     */
    public void removeAllScopes() {
	((javax.media.j3d.Light)wrapped).removeAllScopes();
    }


    /**
     * Sets the Light's influencing region to the specified bounds.
     * This is used when the influencing bounding leaf is set to null.
     * @param region the bounds that contains the Light's new influencing
     * region.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setInfluencingBounds(BoundsWrapper region) {
    	((javax.media.j3d.Light)wrapped).setInfluencingBounds( ((Bounds)region).wrapped );
    }

    /**
     * Retrieves the Light node's influencing bounds.
     * @return this Light's influencing bounds information
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public BoundsWrapper getInfluencingBounds() {
    	javax.media.j3d.Bounds j3dBounds = ((javax.media.j3d.Light)wrapped).getInfluencingBounds();
        
        return (BoundsWrapper)Bounds.getLGBounds( j3dBounds );
    }

    /**
     * Sets the Light's influencing region to the specified bounding leaf.
     * When set to a value other than null, this overrides the influencing
     * bounds object.
     * @param region the bounding leaf node used to specify the Light
     * node's new influencing region.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
//    public void setInfluencingBoundingLeaf(BoundingLeaf region) {
//    	if (isLiveOrCompiled())
//	    if(!this.getCapability(ALLOW_INFLUENCING_BOUNDS_WRITE))
//	    	throw new CapabilityNotSetException(J3dI18N.getString("Light11"));
//
//	if (isLive())
//	    ((LightRetained)this.retained).setInfluencingBoundingLeaf(region);
//	else
//	    ((LightRetained)this.retained).initInfluencingBoundingLeaf(region);
//    }

    /**
     * Retrieves the Light node's influencing bounding leaf.
     * @return this Light's influencing bounding leaf information
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
//    public BoundingLeaf getInfluencingBoundingLeaf() {
//    	if (isLiveOrCompiled())
//	    if(!this.getCapability(ALLOW_INFLUENCING_BOUNDS_READ))
//	    	throw new CapabilityNotSetException(J3dI18N.getString("Light12"));
//
//	return ((LightRetained)this.retained).getInfluencingBoundingLeaf();
//    }


}
