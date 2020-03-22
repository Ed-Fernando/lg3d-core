/**
 * Project Looking Glass
 *
 * $RCSfile: Component3DToBackEvent.java,v $
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
 * $Revision: 1.4 $
 * $Date: 2006-02-27 19:59:58 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.event;


import org.jdesktop.lg3d.wg.Component3D;


/**
 *
 */
public class Component3DToBackEvent extends Component3DEvent {
    private Component3D siblingComponent;
    
    /**
     * Request to move the component to the bottom of the stack.
     */
    public Component3DToBackEvent() {
        this(null);
    }
    
    /**
     * Request to move the component in front of the siblingComponent.
     * Move to the bottom of the stack if siblingComponent is null.
     */
    public Component3DToBackEvent(Component3D siblingComponent) {
        this.siblingComponent = siblingComponent;
    }
    
    /**
     * Get the sibling component.
     */
    public Component3D getSibling() {
        return siblingComponent;
    }
}
