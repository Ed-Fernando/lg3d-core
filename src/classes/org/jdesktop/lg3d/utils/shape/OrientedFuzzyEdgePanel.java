/**
 * Project Looking Glass
 *
 * $RCSfile: OrientedFuzzyEdgePanel.java,v $
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
 * $Date: 2006-06-07 23:59:15 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.shape;

import org.jdesktop.lg3d.sg.*;

/**
 * A Panel which is always oriented towards the view
 */
public class OrientedFuzzyEdgePanel extends OrientedShape3D {
    private static final float defaultEdge = 0.0005f;
    private FuzzyEdgePanelImpl impl;

    public OrientedFuzzyEdgePanel(float width, float height, Appearance app) {
        this(width, height, defaultEdge, app);
    }
    
    public OrientedFuzzyEdgePanel(float width, float height, float allEdges, 
                                  Appearance app) 
    {
        this(width, height, allEdges, allEdges, allEdges, allEdges, app);
    }
    
    public OrientedFuzzyEdgePanel(float width, float height, 
                                  float northEdge, float eastEdge, 
                                  float southEdge, float westEdge,
                                  Appearance app) 
    {                         
        setAlignmentMode(OrientedShape3D.ROTATE_ABOUT_POINT);
        setRotationPoint(0f, 0f, 0f);
	impl = new FuzzyEdgePanelImpl(width, height, 
                                      northEdge, eastEdge, southEdge, westEdge, 
                                      app, (Shape3D)this);
    }

    public void setSize(float width, float height) {
	impl.setSize(width, height);
    }
    
    public void setSize(float width, float height, 
                        float textureWidthScale, float textureHeightScale) 
    {
	impl.setSize(width, height, textureWidthScale, textureHeightScale);
    }
}


