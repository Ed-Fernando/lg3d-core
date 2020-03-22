/**
 * Project Looking Glass
 *
 * $RCSfile: SwichablePseudo3DShortcut.java,v $
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
 * $Revision: 1.3 $
 * $Date: 2006-08-17 18:28:19 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.utils.component;

import java.net.URL;
import org.jdesktop.lg3d.utils.action.AppLaunchAction;
import org.jdesktop.lg3d.utils.eventadapter.MouseClickedEventAdapter;

/**
 *
 * @author radeczka
 */
public class SwichablePseudo3DShortcut extends SwichablePseudo3DIcon { 
    
    /** Creates a new instance of Swichable3DPseudoShortcut */
    public SwichablePseudo3DShortcut(URL downOnFilename,URL upOnFilename, String command, ClassLoader classLoader) {
     super(downOnFilename,upOnFilename);
      addListener(
            new MouseClickedEventAdapter(new AppLaunchAction(command, classLoader)));
    }
    
}
