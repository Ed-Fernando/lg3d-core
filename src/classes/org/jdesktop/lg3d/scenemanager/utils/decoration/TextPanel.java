/**
 * Project Looking Glass
 *
 * $RCSfile: TextPanel.java,v $
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
 * $Revision: 1.9 $
 * $Date: 2006-05-31 20:58:05 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.scenemanager.utils.decoration;

import javax.vecmath.Color4f;
import org.jdesktop.lg3d.utils.shape.GlassyText2D;


public class TextPanel extends GlassyText2D {
    
    public TextPanel(String text, float widthScale, float maxWidth, 
	float height, int xShift, int yShift, boolean vertical)
    {
        super(text, maxWidth, height, new Color4f(0.6f, 1.0f, 0.6f, 0.6f), 
                GlassyText2D.LightDirection.BOTTOM_RIGHT,
                GlassyText2D.Alignment.LEFT,
                widthScale, vertical);
    }
}


