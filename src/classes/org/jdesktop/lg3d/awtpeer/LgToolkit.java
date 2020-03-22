/**
 * Project Looking Glass
 *
 * $RCSfile: LgToolkit.java,v $
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
 * $Date: 2006-06-30 20:38:47 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.awtpeer;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Window;
import javax.media.j3d.Canvas3D;
import javax.swing.SwingUtilities;
import org.jdesktop.lg3d.displayserver.fws.FoundationWinSys;

/**
 *
 * @author paulby
 */
public class LgToolkit implements org.jdesktop.lg3d.awt.ToolkitInterface {
    
    /** Creates a new instance of LgToolkit */
    public LgToolkit() {
    }
    
    public Dimension getScreenSize() {
        Canvas3D canvas3d = FoundationWinSys.getFoundationWinSys().getCanvas(0);
        return canvas3d.getParent().getSize();
    }
}
