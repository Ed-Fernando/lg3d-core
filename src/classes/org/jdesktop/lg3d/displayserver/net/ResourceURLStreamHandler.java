/**
 * Project Looking Glass
 *
 * $RCSfile: ResourceURLStreamHandler.java,v $
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
 * $Date: 2006-04-19 17:23:21 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.displayserver.net;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 *
 * @author paulby
 */
public class ResourceURLStreamHandler extends URLStreamHandler {
    
    public URLConnection openConnection(URL u) throws IOException {
        return new ResourceURLConnection(u);
    }
    
}
