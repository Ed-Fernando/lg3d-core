/**
 * Project Looking Glass
 *
 * $RCSfile: SceneGraphSetup.java,v $
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
 * $Date: 2004-06-26 01:03:07 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg.internal.rmi.rmiclient;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.jdesktop.lg3d.displayserver.LgConfig;
import org.jdesktop.lg3d.sg.internal.rmi.rmiserver.SceneGraphSetupRemote;
import org.jdesktop.lg3d.sg.internal.rmi.rmiserver.SGObjectFactoryRemote;
import org.jdesktop.lg3d.sg.internal.rmi.rmiserver.SGObjectFactoryImpl;

/**
 *
 * This is used to setup both the client and server side rmi information.
 *
 * @author  paulby
 */
public class SceneGraphSetup extends org.jdesktop.lg3d.sg.SceneGraphSetup {
    
    private static SGObjectFactoryRemote sgObjectFactory;
   // private static SceneGraphSetupRemote remote;
    
    public void initialiseClient() {
        logger.fine("RMI CLIENT SETUP");
         try {
            logger.fine("Established remote connection");
            sgObjectFactory = (SGObjectFactoryRemote)Naming.lookup("//localhost:"+LgConfig.getConfig().getRmiPort()+"/RmiSG");
        } catch( Exception e ) {
            e.printStackTrace();
        }
        logger.fine("Got RemoteFactory "+sgObjectFactory);
   }
    
    public void initialiseServer() {
        logger.fine("RMI SERVER SETUP");
        try {
            sgObjectFactory = SGObjectFactoryImpl.getSGObjectFactory();
            Naming.rebind("//localhost:"+LgConfig.getConfig().getRmiPort()+"/RmiSG", sgObjectFactory );
        } catch( java.net.MalformedURLException mue ) {
            mue.printStackTrace();
        } catch( java.rmi.RemoteException rex ) {
            rex.printStackTrace();
            System.exit(1);
        }
    }
    
    public static SGObjectFactoryRemote getSGObjectFactory() {
        return sgObjectFactory;
    }

    public SGObjectFactoryRemote getRemoteSGObjectFactory() throws java.rmi.RemoteException {
        return sgObjectFactory;
    }
    
    class SceneGraphSetupRemoteImpl extends UnicastRemoteObject implements SceneGraphSetupRemote {
        
        public SceneGraphSetupRemoteImpl() throws java.rmi.RemoteException {
        }
        
        public SGObjectFactoryRemote getRemoteSGObjectFactory() throws java.rmi.RemoteException {
            return sgObjectFactory;
        }
        
    }
}
