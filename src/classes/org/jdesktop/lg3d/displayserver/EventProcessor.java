/**
 * Project Looking Glass
 *
 * $RCSfile: EventProcessor.java,v $
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
 * $Revision: 1.16 $
 * $Date: 2007-05-23 18:19:30 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver;
import com.sun.j3d.utils.universe.ConfiguredUniverse;
import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.logging.Level;
import javax.media.j3d.Canvas3D;
import javax.swing.SwingUtilities;
import org.jdesktop.lg3d.wg.event.LgEvent;
import org.jdesktop.lg3d.wg.event.LgEventListener;
import org.jdesktop.lg3d.wg.event.LgEventSource;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.vecmath.Point3d;

/**
 * Handles distribution of events throughout the system
 *
 * This class is thread safe. Any changes made to the set of events
 * during an event processing cycle will not appear in the event
 * list until the next cycle.
 *
 * This is a Java 3D Behavior because many listeners make scene graph
 * changes and we need to sychronise those changes with other systems
 * such as the Animation and Pick engines.
 *
 */
public class EventProcessor extends Behavior {
    /** the logger instance */
    private static Logger logger = Logger.getLogger("lg.event");
    
    private static boolean detailLog = (logger.getLevel()!=null && logger.getLevel().intValue()<=Level.FINE.intValue()) ? true : false;
    
    // At this stage we are experimenting between having the event processor
    // frame synchronous and having it free running. To switch between these
    // two modes have this class extend either Behavior or Thread
    // and uncomment the relevant constructor.
    
    // The clients event processor is always a thread as the client does
    // not have a behavior scheduler.

    /** the hash of stored events &lt;Class,LinkedList&gt; */
    private HashMap<Class,LinkedList> eventStore;
    /** the EventProcessor singleton */
    private static EventProcessor eventProcessor=null;
    /** the busy status of the EventProcessor */
    private int busy = 0;
    /** 
     * the linked list of listeners to change the status of (add or remove)
     * at the next available moment
     */
    private LinkedList<ChangeObject> changeList = new LinkedList();
    /** the linked list of posted events to process in the next cycle */
    private LinkedBlockingQueue<LgEvent> postList = new LinkedBlockingQueue();    
    
    /** the behaviour condition to wake up after 0 elapsed frames */
    private WakeupOnElapsedFrames wakeupCondition = new WakeupOnElapsedFrames( 0, true );
    
    private static final Object ALL_SOURCES = new Object();

    private int visibleCanvasCount = 0;
    private Object canvasCountSync = new Object();
    
    private BehaviorDriver behaviorDriver;
    
    // Behavior constructor
    public EventProcessor(ConfiguredUniverse universe) {
        eventStore = new HashMap();
        eventProcessor = this;
        setName("EventProcessor");
        
        behaviorDriver = new BehaviorDriver(this);
        behaviorDriver.start();
        
        final Canvas3D[] canvases = universe.getViewer().getCanvas3Ds();
        for(Canvas3D c : canvases) {
            c.addHierarchyListener(new HierarchyListener() {
                public void hierarchyChanged(HierarchyEvent e) {
                    switch( e.getID() ) {
                        case HierarchyEvent.HIERARCHY_CHANGED :
                            synchronized(canvasCountSync) {
                                visibleCanvasCount=0;
                                for(Canvas3D c : canvases)
                                    if (c.isShowing())
                                        visibleCanvasCount++;
                                if (visibleCanvasCount==0)
                                    behaviorDriver.enable(true);
                                else 
                                    behaviorDriver.enable(false);
                            }
                            break;
                    }
                }
            });
            
            if (c.isShowing()) {
                synchronized(canvasCountSync) {
                    visibleCanvasCount++;
                }
            }
        }
        
        
        synchronized(canvasCountSync) {
//            System.out.println("____________________ VisibleCanvasCount "+visibleCanvasCount);
            if (visibleCanvasCount==0)
                behaviorDriver.enable(true);
        }
    }
        
    /**
     * Static method which returns the current
     * EventProcessor, creating a new one if there isn't one
     * @return the current event processor
     */
    public static EventProcessor processor() {
        if (eventProcessor==null)
            throw new RuntimeException("EventProcessor not initialised");
        
        return eventProcessor;
    }
    
    /**
     * Indicates this is a client event processor.
     *
     * Server event processor is the default. 
     */
    public void setClientProcessor() {
        ClientThread t = new ClientThread(this);
        t.start();
    }
    
    /**
     * initialize the event processor, setting the
     * scheduling bounds of the behavior to encapsulate
     * the entire system, and setting the wake up condition
     * to wake up after the next frame
     */
    public void initialize() {
        setSchedulingBounds( new BoundingSphere( new Point3d(), Double.POSITIVE_INFINITY )); 
        wakeupOn(wakeupCondition);
    }
    
    /**
     * process the list of posted messages
     * and then set the wakeup condition to wake up
     * after the next frame
     * @param e (unused)
     */
    public void processStimulus( java.util.Enumeration e ) {
        LinkedList<ListenerObject> list;
        LgEvent evt=null;
        Class evtClass;

        try {
            evt = postList.poll();
            while(evt!=null) {
                list=null;

                synchronized(changeList) {
                if (changeList.size()!=0)                
                    processChangeList();            
                }
                evtClass = evt.getClass();

                while(evtClass!=Object.class) {
                    // Get the list of listeners for this class of event
                    list = eventStore.get( evtClass );
                    processEventList(list, evt);
                    evtClass = evtClass.getSuperclass();
                }        
                evt = postList.poll();
            }
        } catch(Exception ex) {
            
        } catch(Error er) {
            logger.log(Level.SEVERE, "Error caught in EventProcessor : "+er, er);
            er.printStackTrace();
        }
        
        // If e is null we are being called from the BehaviorDriver so
        // we can't called wakeupOn
        if (e!=null)        
        wakeupOn(wakeupCondition);
    }
           
    /**
     * Post a looking glass event, originating from source.
     * This will be processed after the next frame.
     * @param evt the event the post
     * @param source the source of the event
     */
    public void postEvent( LgEvent evt, LgEventSource source ) {
        // Don't call setSource with null, the source defaults to null
        // in new events and if we call setSource with null we may overwrite
        // the sourceClass for events we received accross the network
        if (source != null) {
//            evt.setSource( source );
            lgEventAccessHelper.setSource(evt, source);
        }
        if (detailLog)
            logger.fine("Posting event "+evt);
        try {
            postList.put( evt );
        } catch(InterruptedException ie) {
            logger.severe("Interrupted Exception");
        }
    }
    
    /**
     * Process oustanding events
     * @param list the list of listeners to process
     * @param evt the fired event
     */
    private void processEventList(LinkedList<ListenerObject> list,
                                  LgEvent evt ) {
        if (list!=null) {
            if (detailLog) {
                logger.finer("Processing "+evt+"  "+list.size() );
                logger.finest("Event source "+evt.getSource());
                logger.finest("Event sourceClass "+evt.getSourceClass());
            }
            for( ListenerObject listenerObj : list ) {
                if (detailLog) {
                    logger.finest("Checking Listener "+listenerObj);
                    logger.finest("Listener sourceClass "+listenerObj.sourceClass);
                    logger.finest("Listener sourceObject "+listenerObj.getSourceObject());
                }
                try {
                    if (listenerObj.sourceClass == null 
                            || listenerObj.sourceClass.equals(LgEventSource.class)) {

                        if (detailLog) {
                            logger.fine("Listener sourceObject "+listenerObj.getSourceObject());
                        }

                        if (listenerObj.getSourceObject() == ALL_SOURCES) {
                            if (detailLog)
                                logger.fine("Deliver event "+evt+"\nto listener " + listenerObj.getListener() + "\nsource = NO_SOURCE");
                            listenerObj.getListener().processEvent( evt );
                        } else if (listenerObj.getSourceObject() == evt.getSource()) {
                            if (detailLog)
                                logger.fine("Deliver event "+evt+"\nto listener " + listenerObj.getListener() + "\nsource = " + evt.getSource());
                            listenerObj.getListener().processEvent( evt );
                        }
                    } else if (evt.getSource() != null && 
                               listenerObj.sourceClass.isAssignableFrom( evt.getSource().getClass() )) {
                        if (detailLog)
                            logger.fine("Deliver event "+evt+"\nto listener " + listenerObj.getListener() +  "\nsourceClass "+listenerObj.sourceClass+"\nsource = " + evt.getSource());
                        listenerObj.getListener().processEvent( evt );
                    }
                    if  (listenerObj.getSourceObject()==null) {
                        // Source object has been gc, remove the listenerObj
                        list.remove(listenerObj);
                        logger.info("Removed Listener with null source");
                    }
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Exception caught in the EventProcessor", e);
                }
            }
        } else
            if (detailLog)
                logger.finer("No listeners for "+evt.getClass() );
    }

    /**
     * process the changes in the listeners
     *
     * This method must be called from within a synchronized(changelist) block
     */
    private void processChangeList() {        
            for(ChangeObject c : changeList) {           
                if (c.addObject) {               
                    c.list.add( c.listener );   
                    if (detailLog)
                        logger.finest("Adding listener "+c.listener);
                } else                
                    c.list.remove( c.listener );            
            }        
            changeList.clear();
        }


    /**
     * Add a listener which is called whenever events of the specified
     * class are posted
     * @param listener the listener to add
     * @param evtClass the class of the event to listen for
     */    
    public void addListener( LgEventListener listener, Class evtClass ) {        
        addListener( listener, evtClass, null, null );        
    }

    /**
     * Register a listener for events of class evtClass which are generated
     * from the specified source object
     * @param listener the listener to add
     * @param evtClass the class of the event to listen for
     * @param source the source object to listen to
     */
    public void addListener( LgEventListener listener, Class evtClass, Object source ) {
        addListener( listener, evtClass, null, source );
    }

    /**
     * Register a listener for events of class evtClass which are generated
     * from any source of the specified class.
     *
     * In order to support listening for events on a superclass of an event
     * this method automatically adds listeners for evtClass and all it's
     * superclasses
     * @param listener the listener to add
     * @param evtClass the class of the event to listen for
     * @param source the class of the source to listen to
     */
    public void addListener( LgEventListener listener, Class evtClass, Class source ) {
        addListener( listener, evtClass, source, null );
    }
    
    /**
     * Register a listener for events of class evtClass which are generated
     * from any source of the specified class.
     *
     * @param listener the listener to add
     * @param evtClass the class of the event to listen for
     * @param sourceClass the class of the source to listen to
     *                     (should be null if source object is set)
     * @param sourceObject the source object to listen to
     *                     (should be null if source class is set)
     */
    private void addListener( LgEventListener listener, Class evtClass, Class sourceClass, Object sourceObject ) {
        assert( listener!=null );
        if (detailLog)
            logger.fine("Adding Listener for event "+evtClass+"\nlistener "+listener+"\nsourceClass "+sourceClass+"\nsourceObject "+sourceObject);
        
        synchronized(changeList) {
            LinkedList list = eventStore.get(evtClass);
            if (list==null) {
                list = new LinkedList();
                eventStore.put(evtClass, list);
            }
        
        // Part of issue 381 fix. If the user provides a source object that is
        // later collected by gc the reference will become null. So we have to
        // differentiate between the users meaning of null (ie events from all sources)
        // which becomes the NO_SOURCE object, and null sources caused by gc
        if (sourceObject==null)
            sourceObject=ALL_SOURCES;
        ListenerObject listenerObj = new ListenerObject(listener, evtClass, sourceClass, sourceObject);
            changeList.add( new ChangeObject( true, listenerObj, list));
        }
   }

    /**
     * Remove the listener
     * @param listener the listener to add
     * @param evtClass the class of the event to listen for
     */
    public void removeListener( LgEventListener listener, Class evtClass ) {
        removeListener( listener, evtClass, null, null );
    }

    /**
     * Remove the listener
     * @param listener LgEventListener the listener to add
     * @param evtClass the class of the event to listen for
     * @param source the class of the source to listen to
     */    
    public void removeListener( LgEventListener listener, Class evtClass, Class sourceClass ) {        
        removeListener( listener, evtClass, sourceClass, null );        
    }

    /**
     * Remove the listener
     * @param listener the listener to add
     * @param evtClass the class of the event to listen for
     * @param source the source object to listen to
     */    
    public void removeListener( LgEventListener listener, Class evtClass, Object sourceObject ) {        
        removeListener( listener, evtClass, null, sourceObject );        
    }

    /**
     * Actual implmenetation of removeListener, either sourceClass
     * or sourceObject should be null
     * @param listener the listener to add
     * @param evtClass the class of the event to listen for
     * @param sourceClass the class of the source to listen to
     *                     (should be null if source object is set)
     * @param sourceObject the source object to listen to
     *                     (should be null if source class is set)
     */
    private void removeListener( LgEventListener listener, 
                                 Class evtClass, 
                                 Class sourceClass, 
                                 Object sourceObject ) {        
        if (eventStore.get( evtClass )==null)            
            return;
        
        assert( listener!=null );
        if (detailLog)
            logger.fine("Removing Listener for event "+evtClass+"  "+listener);
        
        synchronized(changeList) {
            LinkedList list = eventStore.get(evtClass);
            if (list==null) {
                list = new LinkedList();
                eventStore.put(evtClass, list);
        }
        ListenerObject listenerObj = new ListenerObject(listener, evtClass, sourceClass, sourceObject);
            changeList.add( new ChangeObject( false, listenerObj, list));
        }

    }

    /**
     * The pending listener object change
     */
    class ChangeObject {        
	/** true - add the listener, false - remove the listener */
        boolean addObject;
	/** the listener object */
        ListenerObject listener;
	/** the linked list to be added to/removed from */
        LinkedList list;                

	/**
	 * Create a new listener change object
	 * @param add true - add the listener to the list
	 *            false - remove the listener from the list
	 * @param listener the listener object to add/remove
	 * @param list the linked list to add/remove the listener to/from
	 */
        public ChangeObject( boolean add, ListenerObject listener, LinkedList list ) {            
            this.addObject = add;            
            this.listener = listener;            
            this.list = list;            
        }        
        
        public String toString() {
            return new String(Boolean.toString(addObject)+" "+listener+" "+list);
        }
    }

    /**
     * The listener holder object. It holds the listener, and the event type
     * to listen to, and the source class/object 
     */
    class ListenerObject {        
	/** the LG event listener object */
        //private WeakReference<LgEventListener> listener;        
        private LgEventListener listener;        
	/** the class of message to listen to */
        Class eventClass;        
	/** the class of sources to listen to */
        Class sourceClass;        
	/** the source object to listen to */
        private WeakReference<Object> sourceObject;

	/**
	 * Create a new listener object
	 * @param listener the lg event listener
	 * @param eventClass the class of message to listen to
	 */
        public ListenerObject( LgEventListener listener, Class eventClass ) {            
            this( listener, eventClass, null, null );            
        }

	/**
	 * Create a new listener object
	 * @param listener the lg event listener
	 * @param eventClass the class of message to listen to
	 * @param sourceClass the class of sources to listen to
	 */
        public ListenerObject( LgEventListener listener, Class eventClass, Class sourceClass ) {            
            this( listener, eventClass, sourceClass, null );            

        }

	/**
	 * Create a new listener object
	 * @param listener the lg event listener
	 * @param eventClass the class of message to listen to
	 * @param sourceObject the source object to listen to
	 */
        public ListenerObject( LgEventListener listener, Class eventClass, Object sourceObject ) {            
            this( listener, eventClass, null, sourceObject );            
        }

	/**
	 * Create a new listener object
	 * @param listener the lg event listener
	 * @param eventClass the class of message to listen to
	 * @param sourceClass the class of sources to listen to
	 * 		(must be null if sourceObject is set)
	 * @param sourceObject the source object to listen to
	 * 		(must be null if sourceClass is set)
	 */
        ListenerObject( LgEventListener listener, Class eventClass, Class sourceClass, Object sourceObject ) {           
            assert(sourceClass == null || sourceObject == null || sourceObject == ALL_SOURCES);
            this.listener = listener;
            this.eventClass = eventClass;
            this.sourceClass = sourceClass;
            this.sourceObject = new WeakReference(sourceObject);
        }

	/**
	 * Compares this ListenerObject to the given object to see if they are
	 * equal
	 * @param obj the object to compare to
	 * @return true the ListenerObjects are equal
	 * 	   false obj is not a ListenerObject or they are equal
	 */
        public boolean equals( Object obj ) {            
            if ( !(obj instanceof ListenerObject))                
                return false;

            ListenerObject tmp = (ListenerObject)obj;
            //if (tmp.listener.get() == this.listener.get() &&            
            if (tmp.listener == this.listener &&            
                tmp.eventClass == this.eventClass &&            
                tmp.sourceClass == this.sourceClass &&            
                tmp.sourceObject.get() == this.sourceObject.get() )
                return true;
            return false;           
        }
        
        public LgEventListener getListener() {
            return listener;
        }
        
        public Object getSourceObject() {
            return sourceObject.get();
        }
        
	/**
	 * Returns a string reference to the ListenerObject
	 * @param the string reference of the listener object
	 */
        public String toString() {
            return listener+" "+eventClass+" "+sourceClass+" "+sourceObject;
        }
    }
    
    /**
     * Client processes do not run behavior scheduler so we have
     * a thread that processes the event queues
     */
    class ClientThread extends Thread {
        
	/** The event process of this thread */
        private EventProcessor evtProc;
        
	/**
	 * create a new client thread (daemon), adding it to the
	 * LG Thread group
	 * @param evtProc the event process
	 */
        public ClientThread( EventProcessor evtProc ) {
            super( AppConnectorPrivate.getThreadGroup(), "LG-EventProcessor" );
            setDaemon(true);
            this.evtProc = evtProc;
        }
        
	/**
	 * run while the thread is active
	 * processing the events added to the postList and
	 * adding and removing listeners
	 */
        public void run() {
            logger.info("Event Processor Running");

            LinkedList<ListenerObject> list;
            LgEvent evt=null;

            while(true) {
                list=null;
                try {
                    evt = postList.take();
                    if (changeList.size()!=0)                
                        processChangeList();            

                    // Get the list of listeners for this class of event
                    // NB The class must match exactly
                    list = eventStore.get( evt.getClass() );
                } catch( InterruptedException inte ) {
                    list = null;
                }
                processEventList(list, evt);
            }
        }
    }
    
    /**
     * When the Canvas3D is not visible the Java 3D Behavior scheduler does not run.
     * This thread is used in this case so ensure that the event queues are
     * processed.   
     */
    class BehaviorDriver extends Thread {
        
        private Behavior behavior;
        private boolean enabled=false;
        
        public BehaviorDriver(Behavior behavior) {
            this.behavior = behavior;
        }
        
        public synchronized void enable(boolean enable) {
            System.out.println("BehaviorDriver.enbale("+enable);
            enabled=enable;
            this.notify();
        }
        
        public void run(){
            while(true) {
                synchronized(this) {
                    while (!enabled) {
                        try {
                            this.wait();
                        } catch (InterruptedException ex) {
                        }
                    }
                }   
                
                behavior.processStimulus(null);
                try {
                    sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    /*
     * The following is a kludge to let the EventProcessor invoke 
     * LgEvent.setSource() while hiding the method from the public API.
     */
    public interface LgEventAccessHelper {
        public void setSource(LgEvent event, LgEventSource source);
    }
    
    private static LgEventAccessHelper lgEventAccessHelper;
    
    public static void setLgEventAccessHelper(LgEventAccessHelper helper) {
        lgEventAccessHelper = helper;
    }
}



