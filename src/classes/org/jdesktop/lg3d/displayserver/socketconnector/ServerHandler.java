/**
 * Project Looking Glass
 *
 * $RCSfile: ServerHandler.java,v $
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
 * $Revision: 1.13 $
 * $Date: 2007-03-21 18:07:51 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.socketconnector;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.displayserver.DisplayServerAppInterface;
import org.jdesktop.lg3d.displayserver.LgNodeManager;
import org.jdesktop.lg3d.displayserver.AppConnectorPrivate;
import org.jdesktop.lg3d.displayserver.SevereRuntimeError;
import org.jdesktop.lg3d.displayserver.PlatformConfig;
import org.jdesktop.lg3d.wg.Component3D;

import org.jdesktop.lg3d.wg.Frame3D;

/**
 *
 * The server side handler for clients.
 * A JavaClientManager thread will be started for each client that connects to the
 * server.
 *
 * Each client is given a unique clientID, clientID are reused once a client disconnects
 *
 * @author  Paul
 */
public class ServerHandler {
    
    private Logger logger = Logger.getLogger("lg.socketconnector");

    public static final int LG_SOCKET = PlatformConfig.getConfig().getLgServerPort();
    private HashMap clientManagers;
    private int nextClientID = 50;
    
    public static final int JAVA_CLIENT_PROTOCOL = 1;
    public static final int CPP_CLIENT_PROTOCOL = 2;
    
    public static final int NEW_CLIENT = 3;     // Establishing a new client
    public static final int CLIENT_COMMS = 4;   // Setup comms to existing client
    
    private DisplayServerAppInterface displayServer;
        
    /** Creates a new instance of ServerHandler */
    public ServerHandler() {
        try {
//            System.setProperty("lgremoteserver", "true" );
//            ConnectionManager connectionManager = new ConnectionManager();
            clientManagers = new HashMap();
            
            displayServer = AppConnectorPrivate.getAppConnector(true);
            logger.info("Starting DisplayServer...");
//            connectionManager.start();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Unhandled exception, see /var/tmp/lgserver.log for details.", ex);
        } catch (Error err) {
            logger.log(Level.SEVERE, "Unhandled error, see /var/tmp/lgserver.log for details.", err);
            //System.exit(1);
        }
        
//        try {
//            connectionManager.join();
//        } catch( InterruptedException inte ) {
//            inte.printStackTrace();
//        }
    }
    
    class ConnectionManager extends Thread {
        private ServerSocket serverSocket;
        
        public ConnectionManager() {
            super( AppConnectorPrivate.getThreadGroup(), "LG-Connection Manager" );
            try {
                serverSocket = new ServerSocket( LG_SOCKET );
            } catch (IOException ioe ) {
                throw new SevereRuntimeError("Unable to listen on Socket " + LG_SOCKET + ". "
                        + "Make sure to terminate all the other LG3D processes. ", ioe);
            }
       }
        
        public void run() {
            Socket incoming;
            int clientAction;
            
            logger.info("Server started...");
            while( true ) {
                try {
                    InputStream in;
                    logger.info("Waiting for client connection...");
                    incoming = serverSocket.accept();
                    logger.info("Got Client...");
                    
                    in = incoming.getInputStream();
                    clientAction = in.read();
                    
                    if (clientAction==NEW_CLIENT) {
                        logger.fine("NEW_CLIENT");
                        int clientProtocol = in.read();

                        if (clientProtocol==JAVA_CLIENT_PROTOCOL) {                   
                            clientManagers.put( new Integer(nextClientID), new JavaClientManager( incoming, nextClientID ) );
                            nextClientID++;
                        } else
                            logger.severe("Unsupported client protocol");
                    } else if (clientAction==CLIENT_COMMS) {
                        logger.fine("CLIENT_COMMS");
                        int clientID = in.read();
                        
                        // TODO bullet proof this
                        JavaClientManager clientManager = (JavaClientManager)clientManagers.get( new Integer(clientID) );
                        clientManager.addSendSocket( incoming );
                    }
                } catch( IOException ioe ) {
                    ioe.printStackTrace();
                    logger.info("Attempting recovery");
                }
            }
        }
    }
    
    class JavaClientManager extends Thread implements LgEventListener {
        private ObjectInputStream recvIn;
        private ObjectOutputStream recvOut;
        private ObjectInputStream sendIn;
        private ObjectOutputStream sendOut;
        private int clientID;
        
        /**
         * List of frames attached by this client
         */
        private LinkedList<Frame3D> frameList;
        
        /**
         * List of listeners added by this client
         */
        private LinkedList<LgEventListener> listenerList;
        
        public JavaClientManager( Socket recvSocket, int clientID ) {
            super( AppConnectorPrivate.getThreadGroup(), "LG-JavaClientManager "+clientID );
            this.clientID = clientID;
            frameList = new LinkedList<Frame3D>();
            listenerList = new LinkedList<LgEventListener>();
            try {
                recvIn = new ObjectInputStream(recvSocket.getInputStream());
                recvOut = new ObjectOutputStream(recvSocket.getOutputStream());
                logger.fine("Writing client id "+clientID);
                recvOut.writeInt( clientID );
                recvOut.flush();
            } catch( IOException ioe ) {
                ioe.printStackTrace();
            }
        }
        
        public void addSendSocket( Socket sendSocket ) {
            try {
                sendIn = new ObjectInputStream(sendSocket.getInputStream());
                sendOut = new ObjectOutputStream(sendSocket.getOutputStream());
            } catch( IOException ioe ) {
                ioe.printStackTrace();
            }
            
            this.start();
       }
        
        public void run() {
            Message message;
            boolean finished = false;
            try {
                while( !finished ) {
                    logger.fine("Waiting for message...");
                    message = (Message)recvIn.readObject();
                    logger.fine("Got message "+message);
                    if (message instanceof PostEventMessage)
                        handleMessage( (PostEventMessage)message );
                    else if (message instanceof AddEventListenerMessage)
                        handleMessage( (AddEventListenerMessage)message );
                    else if (message instanceof AddFrame3DMessage)
                        handleMessage( (AddFrame3DMessage)message );
                    else if (message instanceof GetNodeIDMessage)
                        handleMessage( (GetNodeIDMessage)message );
                 }
            } catch( java.io.EOFException eofe ) {
                logger.info("Client "+clientID+" detached");
                cleanup();
                finished = true;
            } catch( IOException ioe ) {
                ioe.printStackTrace();
            } catch( ClassNotFoundException cnfe ) {
                cnfe.printStackTrace();
            }
        }
        
        private void handleMessage( PostEventMessage message ) {
            logger.finer("Client "+clientID+" posting "+message.getEvent() );
            displayServer.postEvent( message.getEvent(), null );
        }
        
        private void handleMessage( AddEventListenerMessage message ) {
            switch( message.getSourceType() ) {
                case SOURCE_UNDEFINED:
                    logger.finer("Client "+clientID+" adding listener "+message.getEventClass() );
                    displayServer.addListener( this, message.getEventClass() );
                    break;
                case SOURCE_CLASS:
                    logger.finer("Client "+clientID+" adding listener "+message.getEventClass()+"  "+message.getSourceClass() );
                    displayServer.addListener( this, message.getEventClass(), message.getSourceClass() );
                    break;
                case SOURCE_OBJECT:
                    Component3D source = LgNodeManager.getLgNodeManager().getNode(message.getSourceNodeID());
                    logger.finer("Client "+clientID+" adding listener "+message.getEventClass()+"  "+source );
                    displayServer.addListener( this, message.getEventClass(), source );
                    break;
            }
        }
        
        private void handleMessage( AddFrame3DMessage message ) {
            logger.finer("Adding Frame3D "+message);
            Component3D bg = (Component3D)LgNodeManager.getLgNodeManager().getNode(message.getNodeID());
            
            if (bg==null) {
                logger.severe("Unable to find Frame3D "+message.getNodeID());
                return;
            }
            
            if (!(bg instanceof Frame3D)) {
                logger.severe("Invalid Frame3D "+bg.getClass().getName());
                return;
            }
            
            frameList.add( (Frame3D)bg );
            displayServer.addFrame3D( (Frame3D)bg );
        }
        
        private void handleMessage( GetNodeIDMessage message ) {
            logger.finer("Generating nodeID for client "+clientID );
            try {
                recvOut.writeObject( message.createResult( displayServer.getNodeID(clientID)));
            } catch( IOException ioe ) {
                ioe.printStackTrace();
            }
        }
        
        /**
         * Cleanup all client data in the server
         */
        private void cleanup() {
            // TODO cleanup cursors
            // TODO cleanup listeners
            
            // Remove all the Frame3D's
            for(Frame3D f : frameList)
                displayServer.removeFrame3D(f);
            
            frameList.clear();
        }
        
        /**
         * Listener method from EventProcessor
         */
        public void processEvent(LgEvent evt) {
            logger.fine("Client "+clientID+" received event "+evt);
            sendMessage( new PostEventMessage( evt ) );
        }
        
        void sendMessage( Message message ) {
            logger.fine("Sending message "+message);
            try {
                sendOut.writeObject( message );
            } catch( IOException ioe ) {
                ioe.printStackTrace();
            }
        }
        
        public Class<LgEvent>[] getTargetEventClasses() {
            // This is not used.  Listener registration is done calling
            // displayServer.addListener() directly.  The event type to 
            // listen to is obtained by message.getEventClass().
            throw new RuntimeException("should not be called");
        }
    }
}
