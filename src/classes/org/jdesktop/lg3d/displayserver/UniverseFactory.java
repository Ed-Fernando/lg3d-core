/**
 * Project Looking Glass
 *
 * $RCSfile: UniverseFactory.java,v $
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
 * $Date: 2007-05-23 18:48:49 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver;

import com.sun.j3d.utils.universe.ConfiguredUniverse;
import java.net.URL;
import javax.media.j3d.Canvas3D;

/**
 *
 * @author paulby
 */
public abstract class UniverseFactory {
    
    protected static UniverseListener listener=null;
    
    /**
     * Create the universe.
     *
     * Implentors of this method must call listener.universeCreate before
     * returning
     */
    public abstract ConfiguredUniverse createUniverse(URL configURL);
    
    public static void addUniverseListener(UniverseListener l) {
        listener = l;
    }

    public interface UniverseListener {
        public void universeCreated( Canvas3D[] canvas );
    }
}
