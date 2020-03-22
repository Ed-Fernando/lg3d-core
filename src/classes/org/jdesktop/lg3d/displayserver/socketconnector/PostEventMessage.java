/**
 * Project Looking Glass
 *
 * $RCSfile: PostEventMessage.java,v $
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
 * $Date: 2005-01-20 22:05:32 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.socketconnector;

import org.jdesktop.lg3d.wg.event.LgEvent;

/**
 *
 * Post an event
 *
 * @author  Paul
 */
public class PostEventMessage extends Message {
    
    private LgEvent event;
    
    /** Creates a new instance of EventMessage */
    public PostEventMessage( LgEvent event ) {
        this.event = event;
    }
    
    public LgEvent getEvent() {
        return event;
    }
    
}
