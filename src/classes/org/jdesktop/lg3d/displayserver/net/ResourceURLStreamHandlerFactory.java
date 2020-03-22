/**
 * Project Looking Glass
 *
 * $RCSfile: ResourceURLStreamHandlerFactory.java,v $
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
 * $Date: 2006-04-19 17:23:22 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.net;

import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

/**
 *
 * @author paulby
 */
public class ResourceURLStreamHandlerFactory implements URLStreamHandlerFactory {
    
    public URLStreamHandler createURLStreamHandler(String p) {
        // Returning null will cause the vm to search for other handlers
        ResourceURLStreamHandler h = null;
        
//        System.out.println("createURLStreamHandler "+p+"---------------------");
        
        if (p.equalsIgnoreCase("resource")) {
            h = new ResourceURLStreamHandler();
        }
        return h;
    }
    
}
