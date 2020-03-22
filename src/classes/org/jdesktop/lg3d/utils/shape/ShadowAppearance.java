/**
 * Project Looking Glass
 *
 * $RCSfile: ShadowAppearance.java,v $
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
 * $Date: 2004-06-23 18:52:15 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.shape;

import org.jdesktop.lg3d.sg.*;
import javax.vecmath.*;


public class ShadowAppearance extends SimpleAppearance {
    public ShadowAppearance() {
	super(0.0f, 0.0f, 0.0f, 1.0f, SimpleAppearance.DISABLE_CULLING);

	RenderingAttributes attr = new RenderingAttributes();
	//attr.setDepthBufferEnable(false);
	//attr.setDepthBufferWriteEnable(false);
	//setRenderingAttributes(attr);
    }
}
