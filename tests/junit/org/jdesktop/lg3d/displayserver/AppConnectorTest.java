/**
 * Project Looking Glass
 *
 * $RCSfile: AppConnectorTest.java,v $
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
 * $Date: 2004-06-23 20:55:35 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver;
import java.util.logging.Logger;

import java.util.logging.LogManager;
import junit.framework.*;



import junit.framework.*;

import org.jdesktop.lg3d.displayserver.event.LgEvent;

import org.jdesktop.lg3d.displayserver.event.LgEventListener;

import org.jdesktop.lg3d.displayserver.event.LgEventSource;

import org.jdesktop.lg3d.wg.Frame3D;
import org.jdesktop.lg3d.sg.Transform3D;

/**
 *
 * @author paulby
 */
public class AppConnectorTest extends TestCase {
    
    protected AppConnector clientAppConnector;
    
    public AppConnectorTest(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(AppConnectorTest.class);
        return suite;
    }

    protected void setUp() throws java.lang.Exception {
        clientAppConnector = AppConnector.getAppConnector(false);
    }

    protected void tearDown() throws java.lang.Exception {
    }

    /**
     * Test of getAppConnector method, of class org.jdesktop.lg.displayserver.AppConnector.
     */
//    public void testGetAppConnector() {
//
//        System.out.println("testGetAppConnector");
//        
//        // TODO add your test code below by replacing the default call to fail.
//        fail("The test case is empty.");
//    }
//
    /**
     * Test of getThreadGroup method, of class org.jdesktop.lg.displayserver.AppConnector.
     */
//    public void testGetThreadGroup() {
//
//        System.out.println("testGetThreadGroup");
//        
//        // TODO add your test code below by replacing the default call to fail.
//        fail("The test case is empty.");
//    }

    /**
     * Test of addFrame3D method, of class org.jdesktop.lg.displayserver.AppConnector.
     */
    public void testAddFrame3D() {

        System.out.println("testAddFrame3D");
        
        Transform3D t3d = new Transform3D();
        Frame3D f = new Frame3D();
        f.setCapability( Frame3D.ALLOW_LOCAL_TO_VWORLD_READ);
        clientAppConnector.addFrame3D(f);
        
        f.getLocalToVworld(t3d);
        Assert.assertEquals(new Transform3D(), t3d);
    }

    /**
     * Test of addListener method, of class org.jdesktop.lg.displayserver.AppConnector.
     */
//    public void testAddListener() {
//
//        System.out.println("testAddListener");
//        
//        // TODO add your test code below by replacing the default call to fail.
//        fail("The test case is empty.");
//    }

    /**
     * Test of postEvent method, of class org.jdesktop.lg.displayserver.AppConnector.
     */
//    public void testPostEvent() {
//
//        System.out.println("testPostEvent");
//        
//        // TODO add your test code below by replacing the default call to fail.
//        fail("The test case is empty.");
//    }

    /**
     * Test of getNodeID method, of class org.jdesktop.lg.displayserver.AppConnector.
     */
    public void testGetNodeID() {

        System.out.println("testGetNodeID");
        
        // Not much of a test, but the actual ID will vary
        // depending on whether we are running client/server or not
        Assert.assertNotNull(clientAppConnector.getNodeID());
    }

    /**
     * Test of initialise method, of class org.jdesktop.lg.displayserver.AppConnector.
     */
//    public void testInitialise() {
//
//        System.out.println("testInitialise");
//        
//        // TODO add your test code below by replacing the default call to fail.
//        fail("The test case is empty.");
//    }

    /**
     * Test of addCursor3D method, of class org.jdesktop.lg.displayserver.AppConnector.
     */
//    public void testAddCursor3D() {
//
//        System.out.println("testAddCursor3D");
//        
//        // TODO add your test code below by replacing the default call to fail.
//        fail("The test case is empty.");
//    }

    /**
     * Test of removeCursor3D method, of class org.jdesktop.lg.displayserver.AppConnector.
     */
//    public void testRemoveCursor3D() {
//
//        System.out.println("testRemoveCursor3D");
//        
//        // TODO add your test code below by replacing the default call to fail.
//        fail("The test case is empty.");
//    }

    /**
     * Test of removeFrame3D method, of class org.jdesktop.lg.displayserver.AppConnector.
     */
//    public void testRemoveFrame3D() {
//
//        System.out.println("testRemoveFrame3D");
//        
//        // TODO add your test code below by replacing the default call to fail.
//        fail("The test case is empty.");
//    }

    /**
     * Test of isServer method, of class org.jdesktop.lg.displayserver.AppConnector.
     */
//    public void testIsServer() {
//
//        System.out.println("testIsServer");
//        
//        // TODO add your test code below by replacing the default call to fail.
//        fail("The test case is empty.");
//    }
    
    // TODO add test methods here, they have to start with 'test' name.
    // for example:
    // public void testHello() {}
    
    
}
