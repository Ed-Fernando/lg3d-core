/**
 * Project Looking Glass
 *
 * $RCSfile: EventBroker.java,v $
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
 * $Date: 2005-11-09 20:12:19 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.fws.x11;

import java.util.*;
import gnu.x11.event.Event;
import org.jdesktop.lg3d.displayserver.AppConnectorPrivate;

/**
 * EventBroker is a class which defines the shared interface
 * of all event brokers. An event broker consists of an event queue plus
 * an event distribution thread. Event brokers can call listeners, update 
 * images, and perform other operations. 
 */

class EventBroker extends Thread
{
    protected LinkedList<Event> eventQueue = new LinkedList();
    protected int max = 1024;

    public EventBroker() {
        super(AppConnectorPrivate.getThreadGroup(), "EventBroker");
        setName( trimClassName( this.getClass().getName() ));
    }
    
    private String trimClassName( String classname ) {
        return classname.substring( classname.lastIndexOf(".")+1, classname.length() );
    }
    
    /**
     * Adds an event to the end of this broker's event queue.
     */
    public synchronized void enqueue (Event event) {
        try {
            while (eventQueue.size() == max) { wait(); }
            eventQueue.add(event);
            notifyAll();
        } catch (InterruptedException ie) {
	    System.out.println("Error: EventBroker.enqueue: interrupted, exception = " + ie);
        }
    }

    /**
     * Removes the next event from the front of this broker's event queue.
     */
    public synchronized Event dequeue () {
      Event event = null;

      try {
          while (eventQueue.size() == 0) { wait(); }
          event = eventQueue.removeFirst();
          notifyAll();
      } catch (InterruptedException ie) {
          System.out.println("Error: EventBroker.dequeue: interrupted, exception = " + ie);
      }

      return event;
    }

    /**
     * Waits until one or more events are in the event queue.
     */
    public synchronized void waitForEvent () {
      try {
          while (eventQueue.size() == 0) { wait(); }
      } catch (InterruptedException ie) {
          System.out.println("Error: EventBroker.waitForEvent: interrupted, exception = " + ie);
      }
    }

    public synchronized boolean isEmpty() {
	return eventQueue.size() == 0;
    }

    public synchronized boolean isFull() {
        return eventQueue.size() == max;
    }

    public synchronized int size() {
	return eventQueue.size();
    }

}
