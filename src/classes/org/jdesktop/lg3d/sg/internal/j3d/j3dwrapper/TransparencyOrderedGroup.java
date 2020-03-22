/**
 * Project Looking Glass
 *
 * $RCSfile: TransparencyOrderedGroup.java,v $
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
 * $Date: 2006-08-22 20:27:11 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.j3d.j3dwrapper;

import java.util.Arrays;
import java.util.Enumeration;
import org.jdesktop.lg3d.sg.internal.wrapper.BoundsWrapper;
import org.jdesktop.lg3d.sg.internal.wrapper.BranchGroupWrapper;
import org.jdesktop.lg3d.sg.internal.wrapper.NodeWrapper;
import org.jdesktop.lg3d.sg.internal.wrapper.SceneGraphPathWrapper;
import org.jdesktop.lg3d.sg.internal.wrapper.Transform3DWrapper;
import org.jdesktop.lg3d.sg.internal.wrapper.TransparencyOrderedGroupWrapper;

/**
 * Provides the user control over the rendering order of transparent objects
 * in the subgraphs of this Group.
 *
 * Transparent shapes in the 1st child of this group will be rendered before
 * (ie behind) transparent shapes in the 2nd child and so on.
 *
 * Transparent shapes in the same child will be ordered using the standard test
 * of distance to view.
 *
 * @author paulby
 */
public class TransparencyOrderedGroup extends Group implements TransparencyOrderedGroupWrapper {
    public void setChildIndexOrder(int[] childIndexOrder) {
        ((org.jdesktop.j3d.utils.scenegraph.transparency.TransparencyOrderedGroup)wrapped).setChildIndexOrder(childIndexOrder);
    }

    public int[] getChildIndexOrder() {
	return ((org.jdesktop.j3d.utils.scenegraph.transparency.TransparencyOrderedGroup)wrapped).getChildIndexOrder();
    }

    public void addChild(NodeWrapper child, int[] childIndexOrder) {
        assert( child!=null );
	((org.jdesktop.j3d.utils.scenegraph.transparency.TransparencyOrderedGroup)wrapped).addChild( (javax.media.j3d.Node)((Node)child).wrapped, childIndexOrder );
    }

    public void addChild(NodeWrapper child) {
        assert( child!=null );
	((org.jdesktop.j3d.utils.scenegraph.transparency.TransparencyOrderedGroup)wrapped).addChild( (javax.media.j3d.Node)((Node)child).wrapped );
    }

    public void insertChild(NodeWrapper child, int index) {
        assert( child!=null );
	((org.jdesktop.j3d.utils.scenegraph.transparency.TransparencyOrderedGroup)wrapped).insertChild( (javax.media.j3d.Node)((Node)child).wrapped, index );
    }

    public void setChild(NodeWrapper child, int index) {
        assert( child!=null );
	((org.jdesktop.j3d.utils.scenegraph.transparency.TransparencyOrderedGroup)wrapped).setChild( (javax.media.j3d.Node)((Node)child).wrapped, index );
    }

    public void removeChild(NodeWrapper child) {
        assert( child!=null );
	((org.jdesktop.j3d.utils.scenegraph.transparency.TransparencyOrderedGroup)wrapped).removeChild( (javax.media.j3d.Node)((Node)child).wrapped );
    }

    public void removeChild(int index) {
	((org.jdesktop.j3d.utils.scenegraph.transparency.TransparencyOrderedGroup)wrapped).removeChild( index );
    }

    protected void createWrapped() {
        wrapped = new org.jdesktop.j3d.utils.scenegraph.transparency.TransparencyOrderedGroup();
        wrapped.setUserData( this );
    }
}
