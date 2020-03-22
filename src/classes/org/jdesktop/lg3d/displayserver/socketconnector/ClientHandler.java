/**
 * Project Looking Glass
 *
 * $RCSfile: ClientHandler.java,v $
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
 * $Revision: 1.7 $
 * $Date: 2007-05-02 20:51:54 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.socketconnector;

import java.net.Socket;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.jdesktop.lg3d.displayserver.NodeID;
import org.jdesktop.lg3d.displayserver.DisplayServerAppInterface;
import org.jdesktop.lg3d.displayserver.EventProcessor;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.sg.SceneGraphSetup;
import org.jdesktop.lg3d.sg.BranchGroup;
import org.jdesktop.lg3d.scenemanager.CursorModule;

/**
 *
 * The ClientHandler establishes the communication between the LG client app and
 * the server. The wire protocol is handled by a modular component so that we can
 * use different protocols, the initial protocol uses Java ObjectStreams for
 * expedience, this is not necessarily the long term plan.
 *
 * The conversation with the server is started with a number of bytes.
 *    int action
 *
 * action==NEW_CLIENT
 *    int protocol
 *
 * action==CLIENT_COMMS
 *    long clientID
 * 
 * The protocol indicates which wire protocol this client will use and causes the server
 * to instantiate the correct handler.
 *
 * @author  Paul
 */
public class ClientHandler implements DisplayServerAppInterface {
    
    private java.util.logging.Logger logger = java.util.logging.Logger.getLogger("lg.displayserver");

    ServerConnection serverConnection;
    EventProcessor eventProcessor;
    
    /** Creates a new instance of appconnector */
    public ClientHandler() {
    } 
    
    public void initialize(int frame_interval_time) {
        boolean tryAgain;
        int tryCount = 0;
        eventProcessor = EventProcessor.processor();
        eventProcessor.setClientProcessor();
        
        logger.info("Attempting to contact server");
        
        do {
            tryAgain = false;
            try {
                Socket sendSocket = new Socket();
                sendSocket.setReuseAddress(true);
                InetSocketAddress serverAddress = new InetSocketAddress( "localhost", ServerHandler.LG_SOCKET );
                sendSocket.connect( serverAddress );
                                
                Socket recvSocket = new Socket();
                recvSocket.setReuseAddress(true);
                recvSocket.connect( serverAddress );
                
                serverConnection = new ServerConnection( sendSocket, recvSocket );
            } catch( java.net.ConnectException connE ) {
                tryAgain = true;
                if (tryCount%20==0)
                    logger.warning("Unable to connect to Server, retrying....");
                tryCount++;
                try {
                    Thread.sleep( 1000 );
                } catch( InterruptedException ie ) {
                }
            } catch( IOException ioe ) {
                ioe.printStackTrace();
            }
        } while( tryAgain );
        
        logger.info("Connection established");
        
//        try {
//            Class sgsClass = Class.forName(SceneGraphSetup.getWrapperPackage()+"SceneGraphSetup");
//            SceneGraphSetup sgs = (SceneGraphSetup)sgsClass.newInstance();
//            logger.info("Setup SG using "+sgs);
//            sgs.initialiseClient();
//        } catch( Exception e ) {
//            e.printStackTrace();
//        }
        SceneGraphSetup.initializeClient();
        
    }
    
    public void addFrame3D(org.jdesktop.lg3d.wg.Frame3D frame) {
        serverConnection.sendMessage( new AddFrame3DMessage( frame ));
    }
    
    public void addListener(LgEventListener listener, Class evtClass) {
        eventProcessor.addListener( listener, evtClass );
        serverConnection.sendMessage( new AddEventListenerMessage( evtClass ) );
    }
    
    public void addListener(LgEventListener listener, Class evtClass, Class source) {
        eventProcessor.addListener( listener, evtClass, source );
        serverConnection.sendMessage( new AddEventListenerMessage( evtClass, source ) );
    }
    
    public void addListener(LgEventListener listener, Class evtClass, LgEventSource source) {
        eventProcessor.addListener( listener, evtClass, source );
        serverConnection.sendMessage( new AddEventListenerMessage( evtClass, source ) );
    }
    
    /**
     * Remove the listener
     * @param listener the listener to add
     * @param evtClass the class of the event to listen for
     */
    public void removeListener(LgEventListener listener, Class evtClass ) {
        eventProcessor.removeListener( listener, evtClass );
        throw new RuntimeException("RemoveEventListenerMessage not implemented");
        //serverConnection.sendMessage( new RemoveEventListenerMessage( evtClass ) );
    }

    /**
     * Remove the listener
     * @param listener LgEventListener the listener to add
     * @param evtClass the class of the event to listen for
     * @param source the class of the source to listen to
     */    
    public void removeListener(LgEventListener listener, Class evtClass, Class sourceClass ) {        
        eventProcessor.removeListener( listener, evtClass, sourceClass );        
        throw new RuntimeException("RemoveEventListenerMessage not implemented");
        //serverConnection.sendMessage( new RemoveEventListenerMessage( evtClass, source ) );
    }

    /**
     * Remove the listener
     * @param listener the listener to add
     * @param evtClass the class of the event to listen for
     * @param source the source object to listen to
     */    
    public void removeListener(LgEventListener listener, Class evtClass, Object sourceObject ) {        
        eventProcessor.removeListener( listener, evtClass, sourceObject );   
        throw new RuntimeException("RemoveEventListenerMessage not implemented");
        //serverConnection.sendMessage( new RemoveEventListenerMessage( evtClass, source ) );
    }

    public void postEvent(LgEvent event, LgEventSource source) {
        // TODO local optimisation 
        // local events should be handled directly if there are any listeners and 
        // the server should not return any events generated by this client to this client
        //eventProcessor.postEvent( event, source );
        serverConnection.sendMessage( new PostEventMessage( event ) );
    }    
            
    public NodeID getNodeID(int clientID) {
        Message result = serverConnection.sendMessageGetResult( new GetNodeIDMessage() );
        if (result instanceof GetNodeIDMessage.GotNodeIDMessage) {
            return ((GetNodeIDMessage.GotNodeIDMessage)result).getNodeID();
        } else
            throw new RuntimeException("Unexpected result from GetNodeIDMessage "+result.getClass());
    }
    
    public void addCursor3D(org.jdesktop.lg3d.wg.Cursor3D cursor) {
    }
    
    public void removeCursor3D(org.jdesktop.lg3d.wg.Cursor3D cursor) {
    }
    
    public BranchGroup getCursorRoot () {
	return null;
    }

    public CursorModule getCursorModule () {
	return null;
    }

    public void removeFrame3D(org.jdesktop.lg3d.wg.Frame3D app) {
    }
    
    public void printWonderlandSceneGraph () {
    }

    public void printSceneGraph () {
        throw new RuntimeException("printSceneGraph is not implemented on the client side");
    }
    
    class ServerConnection extends Thread {
        /** 
         * Two streams, send and recv. 
         * The send stream is for sending messages to the server (and reading acknowledgements)
         * The recv stream is for receiving messages from the server (and send acknowledgements)
         */
        
        ObjectInputStream sendIn;
        ObjectOutputStream sendOut;
        ObjectInputStream recvIn;
        ObjectOutputStream recvOut;
        int clientID;
        
        public ServerConnection( Socket sendSocket, Socket recvSocket ) {
            try {
                sendSocket.getOutputStream().write(ServerHandler.NEW_CLIENT);
                sendSocket.getOutputStream().write(ServerHandler.JAVA_CLIENT_PROTOCOL);
                
                sendOut = new ObjectOutputStream( sendSocket.getOutputStream() );
                sendIn = new ObjectInputStream( sendSocket.getInputStream() );
                
                logger.finer("Reading client id");
                // Get the clients id
                clientID = sendIn.readInt();
                logger.finer("Client "+clientID );
                setName("ServerConnection "+clientID);  // Set the threads name
                                
                recvSocket.getOutputStream().write( ServerHandler.CLIENT_COMMS );
                recvSocket.getOutputStream().write( clientID );
                recvOut = new ObjectOutputStream( recvSocket.getOutputStream() );
                recvIn = new ObjectInputStream( recvSocket.getInputStream() );
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
                    logger.finer("Waiting for message...");
                    message = (Message)recvIn.readObject();
                    logger.finer("Received message "+message);
                    if (message instanceof PostEventMessage)
                        handleMessage( (PostEventMessage)message );
                }
            } catch( java.io.EOFException eofe ) {
                logger.info("Server has disconnected");
                finished = true;
            } catch( IOException ioe ) {
                ioe.printStackTrace();
            } catch( ClassNotFoundException cnfe ) {
                cnfe.printStackTrace();
            }
        }
        
        private void handleMessage( PostEventMessage message ) {
            eventProcessor.postEvent( message.getEvent(), null );
        }

        void sendMessage( Message message ) {
            logger.finer("Sending message "+message);
            try {
                sendOut.writeObject( message );
            } catch( IOException ioe ) {
                ioe.printStackTrace();
            }
        }
        
        /**
         * Send a message and wait for the result
         * Answer is returned
         */
        Message sendMessageGetResult( Message sendMessage ) {
            logger.finer("Sending message "+sendMessage);
            Message result=null;
            try {
                sendOut.writeObject( sendMessage );
                result = (Message)sendIn.readObject();
            } catch( IOException ioe ) {
                throw new RuntimeException( ioe );
            } catch( ClassNotFoundException cnfe ) {
                throw new RuntimeException(cnfe);
            }
            return result;
        }
        
    }
}
