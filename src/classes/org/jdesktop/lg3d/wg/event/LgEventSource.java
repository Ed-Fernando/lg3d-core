/**
 * Project Looking Glass
 *
 * $RCSfile: LgEventSource.java,v $
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
 * $Date: 2005-04-14 23:05:11 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wg.event;

/**
 * Classes that implement this interface can be the source of LgEvents 
 *
 * @author  Paul
 */
public interface LgEventSource {
    /**
     * Used to create a listener for events from any source
     */
    public static final Class ALL_SOURCES = LgEventSource.class;
    
    /**
     * post an event to the system
     * @param evt the event to post
     */
    public void postEvent(LgEvent evt);
}
