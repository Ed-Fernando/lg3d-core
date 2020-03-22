/**
 * Project Looking Glass
 *
 * $RCSfile: Pseudo3DShortcut.java,v $
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
 * $Revision: 1.6 $
 * $Date: 2006-08-17 18:28:18 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.utils.component;

import java.net.URL;
import org.jdesktop.lg3d.utils.action.AppLaunchAction;
import org.jdesktop.lg3d.utils.eventadapter.MouseClickedEventAdapter;


public class Pseudo3DShortcut extends Pseudo3DIcon {
    
    public Pseudo3DShortcut(URL imageUrl, String command, ClassLoader classLoader) {
        super(imageUrl);
        addListener(
            new MouseClickedEventAdapter(new AppLaunchAction(command, classLoader)));
    }
    
    /**
     * @deprecated use contructor with URL for image name
     */
    public Pseudo3DShortcut(String imageName, String command, ClassLoader classLoader) {
        this(Pseudo3DShortcut.class.getClassLoader().getResource(imageName), command, classLoader);
    }
}
