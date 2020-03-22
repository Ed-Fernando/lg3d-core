/**
 * Project Looking Glass
 *
 * $RCSfile: NodeChangeProcessor.java,v $
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
 * $Date: 2005-04-14 23:04:23 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.utils.traverser;

/**
 *
 * @author  paulby
 * @version
 */
public abstract class NodeChangeProcessor implements ProcessNodeInterface {
    
    public boolean processNode(org.jdesktop.lg3d.sg.Node node) {
        return changeNode( node );
    }
    
    abstract public boolean changeNode( org.jdesktop.lg3d.sg.Node node );
}
