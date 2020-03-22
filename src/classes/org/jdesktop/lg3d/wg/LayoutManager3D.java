/**
 * Project Looking Glass
 *
 * $RCSfile: LayoutManager3D.java,v $
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
 * $Date: 2005-04-14 23:05:01 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg;


/**
 * Defines the interface for classes that know how to lay out Container3Ds.
 * All the method invocations are synchronized by the associated Container3D.
 */
public interface LayoutManager3D {
    /**
     * Set the target container.  Invoked with a valid target Container3D
     * object when this layout is set to the container, and with null when
     * the layout is removed from the container. 
     *
     * @param parent the container to be laid out
     */
    public void setContainer(Container3D cont);
    
    /**
     * Lays out the associated container.
     */
    public void layoutContainer();
    
    /**
     * Adds the specified component to the layout, using the specified 
     * constraint object.
     *
     * @param comp the component to be added
     * @param constraints where/how the component is added to the layout.
     */
    public void addLayoutComponent(Component3D comp, Object constraints);
    
    /**
     * Removes the specified component from the layout.
     *
     * @param comp the component to be removed
     */
    public void removeLayoutComponent(Component3D comp);
    
    /**
     * Rearrange the specified component in the layout to a new location,
     * using the specified newConstraints object.
     *
     * @param comp the component to be rearranged
     * @param newConstraints where/how the component is added to the layout.
     * @return true if rearrrangement is taken place and layoutContainer()
     * needs to be invoked by the associated container.
     */
    public boolean rearrangeLayoutComponent(Component3D comp, Object newConstraints);
}

