/**
 * Project Looking Glass
 *
 * $RCSfile: OrderedGroup.java,v $
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
 * $Date: 2004-06-23 20:55:36 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.j3d.j3dwrapper;

import org.jdesktop.lg3d.sg.internal.wrapper.OrderedGroupWrapper;

public class OrderedGroup extends Group implements OrderedGroupWrapper {
    
    /** Creates a new instance of OrderedGroup */
    public OrderedGroup() {
    }
    
    protected void createWrapped() {
        wrapped = new javax.media.j3d.OrderedGroup();
        wrapped.setUserData(this);
    }
    
    public void addChild(org.jdesktop.lg3d.sg.internal.wrapper.NodeWrapper child, int[] childIndexOrder) {
        ((javax.media.j3d.OrderedGroup)wrapped).addChild( (javax.media.j3d.Node)child.getWrapped(), childIndexOrder );
    }
    
    public int[] getChildIndexOrder() {
        return ((javax.media.j3d.OrderedGroup)wrapped).getChildIndexOrder();
    }
    
    public void setChildIndexOrder(int[] childIndexOrder) {
        ((javax.media.j3d.OrderedGroup)wrapped).setChildIndexOrder(childIndexOrder);
    }
    
}
