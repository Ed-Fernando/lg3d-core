/**
 * Project Looking Glass
 *
 * $RCSfile: Fog.java,v $
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
 * $Date: 2005-04-14 23:04:19 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.sg.internal.rmi.rmiclient;

import java.util.Enumeration;
import javax.vecmath.Color3f;

import org.jdesktop.lg3d.sg.internal.rmi.rmiserver.BoundsRemote;
import org.jdesktop.lg3d.sg.internal.rmi.rmiserver.FogRemote;
import org.jdesktop.lg3d.sg.internal.rmi.rmiserver.GroupRemote;
import org.jdesktop.lg3d.sg.internal.wrapper.BoundsWrapper;
import org.jdesktop.lg3d.sg.internal.wrapper.FogWrapper;
import org.jdesktop.lg3d.sg.internal.wrapper.GroupWrapper;

/**
 * The Fog leaf node defines a set of fog parameters common to all
 * types of fog.  These parameters include the fog color and a region
 * of influence in which this Fog node is active.
 * A Fog node also contains a list of Group nodes that specifies the
 * hierarchical scope of this Fog.  If the scope list is empty, then
 * the Fog node has universe scope: all nodes within the region of
 * influence are affected by this Fog node.  If the scope list is
 * non-empty, then only those Leaf nodes under the Group nodes in the
 * scope list are affected by this Fog node (subject to the
 * influencing bounds).
 * <p>
 * If the regions of influence of multiple Fog nodes overlap, the
 * Java 3D system will choose a single set of fog parameters for those
 * objects that lie in the intersection.  This is done in an
 * implementation-dependent manner, but in general, the Fog node that
 * is "closest" to the object is chosen.
 */

public abstract class Fog extends Leaf implements FogWrapper {
    /**
     * Specifies that this Fog node allows read access to its
     * influencing bounds and bounds leaf information.
     */
    public static final int
    ALLOW_INFLUENCING_BOUNDS_READ = CapabilityBits.FOG_ALLOW_INFLUENCING_BOUNDS_READ;

    /**
     * Specifies that this Fog node allows write access to its
     * influencing bounds and bounds leaf information.
     */
    public static final int
    ALLOW_INFLUENCING_BOUNDS_WRITE = CapabilityBits.FOG_ALLOW_INFLUENCING_BOUNDS_WRITE;

    /**
     * Specifies that this Fog node allows read access to its color
     * information.
     */
    public static final int
    ALLOW_COLOR_READ = CapabilityBits.FOG_ALLOW_COLOR_READ;

    /**
     * Specifies that this Fog node allows write access to its color
     * information.
     */
    public static final int
    ALLOW_COLOR_WRITE = CapabilityBits.FOG_ALLOW_COLOR_WRITE;

    /**
     * Specifies that this Fog node allows read access to its scope
     * information at runtime.
     */
    public static final int
    ALLOW_SCOPE_READ = CapabilityBits.FOG_ALLOW_SCOPE_READ;

    /**
     * Specifies that this Fog node allows write access to its scope
     * information at runtime.
     */
    public static final int
    ALLOW_SCOPE_WRITE = CapabilityBits.FOG_ALLOW_SCOPE_WRITE;

    /**
     * Constructs a Fog node with default parameters.  The default
     * values are as follows:
     * <ul>
     * color : black (0,0,0)<br>
     * scope : empty (universe scope)<br>
     * influencing bounds : null<br>
     * influencing bounding leaf : null<br>
     * </ul>
     */
    public Fog() {
	// Just use the defaults
    }

    /**
     * Constructs a Fog node with the specified fog color.
     * @param color the fog color
     */
    public Fog(Color3f color) {
    }

    /**
     * Constructs a Fog node with the specified fog color.
     * @param r the red component of the fog color
     * @param g the green component of the fog color
     * @param b the blue component of the fog color
     */
    public Fog(float r, float g, float b) {
    }

    /**
     * Sets the fog color to the specified color.
     * @param color the new fog color
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setColor(Color3f color) {
        try {
            ((FogRemote)remote).setColor(color);
        } catch(java.rmi.RemoteException rex) {
            throw new RuntimeException(rex);
        }
    }

    /**
     * Sets the fog color to the specified color.
     * @param r the red component of the fog color
     * @param g the green component of the fog color
     * @param b the blue component of the fog color
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void setColor(float r, float g, float b) {
        try {
            ((FogRemote)remote).setColor(r,g,b);
        } catch(java.rmi.RemoteException rex) {
            throw new RuntimeException(rex);
        }
    }

    /**
     * Retrieves the fog color.
     * @param color the vector that will receive the current fog color
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public void getColor(Color3f color) {
        try {
            ((FogRemote)remote).getColor(color);
        } catch(java.rmi.RemoteException rex) {
            throw new RuntimeException(rex);
        }
    }

    /**
     * Sets the Fog's influencing region to the specified bounds.
     * This is used when the influencing bounding leaf is set to null.
     * @param region the bounds that contains the Fog's new influencing region.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */  
    public void setInfluencingBounds(BoundsWrapper region) {
        try {
            ((FogRemote)remote).setInfluencingBounds( (BoundsRemote)((Bounds)region).remote );
        } catch(java.rmi.RemoteException rex) {
            throw new RuntimeException(rex);
        }
    }

    /**  
     * Retrieves the Fog node's influencing bounds.
     * @return this Fog's influencing bounds information
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */  
    public BoundsWrapper getInfluencingBounds() {
        try {
            return Bounds.getLGBounds(((FogRemote)remote).getInfluencingBounds());
        } catch(java.rmi.RemoteException rex) {
            throw new RuntimeException(rex);
        }
    }

    /**
     * Sets the Fog's influencing region to the specified bounding leaf.
     * When set to a value other than null, this overrides the influencing
     * bounds object.
     * @param region the bounding leaf node used to specify the Fog
     * node's new influencing region.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */  
//    public void setInfluencingBoundingLeaf(BoundingLeaf region) {
//    }

    /**  
     * Retrieves the Fog node's influencing bounding leaf.
     * @return this Fog's influencing bounding leaf information
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */  
//    public BoundingLeaf getInfluencingBoundingLeaf() {
//    }


    /**
     * Replaces the node at the specified index in this Fog node's
     * list of scopes with the specified Group node.
     * By default, Fog nodes are scoped only by their influencing
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
        try {
            ((FogRemote)remote).setScope( (GroupRemote)((Group)scope).remote, index );
        } catch(java.rmi.RemoteException rex) {
            throw new RuntimeException(rex);
        }
    }


    /**
     * Retrieves the Group node at the specified index from this Fog node's
     * list of scopes.
     * @param index the index of the Group node to be returned.
     * @return the Group node at the specified index.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public GroupWrapper getScope(int index) {
        try {
            return (Group)getLocal(((FogRemote)remote).getScope(index));
        } catch(java.rmi.RemoteException rex) {
            throw new RuntimeException(rex);
        }
    }


    /**
     * Inserts the specified Group node into this Fog node's
     * list of scopes at the specified index.
     * By default, Fog nodes are scoped only by their influencing
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
        try {
            ((FogRemote)remote).insertScope( (GroupRemote)((Group)scope).remote, index );
        } catch(java.rmi.RemoteException rex) {
            throw new RuntimeException(rex);
        }
    }


    /**
     * Removes the node at the specified index from this Fog node's
     * list of scopes.  If this operation causes the list of scopes to
     * become empty, then this Fog will have universe scope: all nodes
     * within the region of influence will be affected by this Fog node.
     * @param index the index of the Group node to be removed.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     * @exception RestrictedAccessException if the group node at the
     * specified index is part of a compiled scene graph
     */
    public void removeScope(int index) {
        try {
            ((FogRemote)remote).removeScope(index);
        } catch(java.rmi.RemoteException rex) {
            throw new RuntimeException(rex);
        }
    }
  

    /**
     * Returns an enumeration of this Fog node's list of scopes.
     * @return an Enumeration object containing all nodes in this Fog node's
     * list of scopes.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public Enumeration getAllScopes() {
        try {
            Enumeration e = ((FogRemote)remote).getAllScopes();
            java.util.Vector v = new java.util.Vector();
            while(e.hasMoreElements())
                v.add( getLocal(((GroupRemote)e.nextElement())) );

            return v.elements();
        } catch(java.rmi.RemoteException rex) {
            throw new RuntimeException(rex);
        }
    }


    /**
     * Appends the specified Group node to this Fog node's list of scopes.
     * By default, Fog nodes are scoped only by their influencing
     * bounds.  This allows them to be further scoped by a list of
     * nodes in the hierarchy.
     * @param scope the Group node to be appended.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     * @exception RestrictedAccessException if the specified group node
     * is part of a compiled scene graph
     */
    public void addScope(GroupWrapper scope) {
        try {
            ((FogRemote)remote).addScope( (GroupRemote)((Group)scope).remote );
        } catch(java.rmi.RemoteException rex) {
            throw new RuntimeException(rex);
        }
    }

  
    /**
     * Returns the number of nodes in this Fog node's list of scopes.
     * If this number is 0, then the list of scopes is empty and this
     * Fog node has universe scope: all nodes within the region of
     * influence are affected by this Fog node.
     * @return the number of nodes in this Fog node's list of scopes.
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     */
    public int numScopes() {
        try {
            return ((FogRemote)remote).numScopes();
        } catch(java.rmi.RemoteException rex) {
            throw new RuntimeException(rex);
        }
     }


    /**
     * Retrieves the index of the specified Group node in this
     * Fog node's list of scopes.
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
        try {
            return ((FogRemote)remote).indexOfScope( (GroupRemote)((Group)scope).remote);
        } catch(java.rmi.RemoteException rex) {
            throw new RuntimeException(rex);
        }
     }


    /**
     * Removes the specified Group node from this Fog
     * node's list of scopes.  If the specified object is not in the
     * list, the list is not modified.  If this operation causes the
     * list of scopes to become empty, then this Fog
     * will have universe scope: all nodes within the region of
     * influence will be affected by this Fog node.
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
        try {
            ((FogRemote)remote).removeScope((GroupRemote)((Group)scope).remote);
        } catch(java.rmi.RemoteException rex) {
            throw new RuntimeException(rex);
        }
    }


    /**
     * Removes all Group nodes from this Fog node's
     * list of scopes.  The Fog node will then have
     * universe scope: all nodes within the region of influence will
     * be affected by this Fog node.
     *
     * @exception CapabilityNotSetException if appropriate capability is
     * not set and this object is part of live or compiled scene graph
     * @exception RestrictedAccessException if any group node in this
     * node's list of scopes is part of a compiled scene graph
     *
     * @since Java 3D 1.3
     */
    public void removeAllScopes() {
        try {
            ((FogRemote)remote).removeAllScopes();
        } catch(java.rmi.RemoteException rex) {
            throw new RuntimeException(rex);
        }
    }

}
