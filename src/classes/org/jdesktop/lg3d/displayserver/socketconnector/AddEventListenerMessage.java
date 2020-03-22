/**
 * Project Looking Glass
 *
 * $RCSfile: AddEventListenerMessage.java,v $
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
 * $Date: 2007-03-07 23:14:27 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.socketconnector;

import java.util.logging.Logger;
import org.jdesktop.lg3d.displayserver.LgNodeManager;
import org.jdesktop.lg3d.displayserver.NodeID;
import org.jdesktop.lg3d.wg.Component3D;
import org.jdesktop.lg3d.wg.event.LgEventSource;;

/**
 * Add an event listener
 *
 * @author  Paul
 */
public class AddEventListenerMessage extends Message {
    /** the logger instance */
    protected static final Logger logger = Logger.getLogger("lg.event");
    
    private Class evtClass;
    private Class sourceClass;
    private NodeID sourceObj;
    
    public enum SourceType { SOURCE_UNDEFINED, SOURCE_CLASS, SOURCE_OBJECT };
    
    private SourceType sourceType;
    
    /** Creates a new instance of AddEventListenerMessage */
    public AddEventListenerMessage( Class evtClass ) {
        this.evtClass = evtClass;
        sourceType = SourceType.SOURCE_UNDEFINED;
    }
    
    public AddEventListenerMessage( Class evtClass, Class sourceClass ) {
        this.evtClass = evtClass;
        this.sourceClass = sourceClass;
        sourceType = SourceType.SOURCE_CLASS;
    }
    
    public AddEventListenerMessage( Class evtClass, LgEventSource sourceObj ) {
        this.evtClass = evtClass;
        // this.sourceObj = sourceObj.getNodeID();
        this.sourceObj = null;
        if (sourceObj instanceof Component3D) {
            this.sourceObj = LgNodeManager.c3dAccessHelper.getNodeID((Component3D)sourceObj);
        } else {
//            logger.warning("Event source without getNodeID()? " + sourceObj);
        }
        
        sourceType = SourceType.SOURCE_OBJECT;
    }
    
    public Class getEventClass() {
        return evtClass;
    }
    
    public Class getSourceClass() {
        return sourceClass;
    }
    
    public NodeID getSourceNodeID() {
        return sourceObj;
    }
    
    public SourceType getSourceType() {
        return sourceType;
    }
}
