/**
 * Project Looking Glass
 *
 * $RCSfile: Component3DManualMoveEvent.java,v $
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
 * $Revision: 1.5 $
 * $Date: 2006-03-19 03:16:44 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.event;

import javax.vecmath.Point3f;

/**
 * Event generated when the user manually moves a Component3D
 */
public class Component3DManualMoveEvent extends Component3DEvent {
    private boolean started = false;
    private Point3f intersection = new Point3f();
    
    public Component3DManualMoveEvent(boolean started, Point3f intersection) {
        this.started = started;
        this.intersection.set(intersection);
    }
    
    public boolean isStarted() {
        return started;
    }
    
    public Point3f getIntersection(Point3f intersection) {
        intersection.set(this.intersection);
        return intersection;
    }
}
