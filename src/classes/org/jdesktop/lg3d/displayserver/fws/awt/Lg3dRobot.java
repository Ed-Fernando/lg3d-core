/*
 * EventRecorder.java
 *
 * Created on June 2, 2005, 8:27 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.jdesktop.lg3d.displayserver.fws.awt;

import java.awt.AWTException;
import java.awt.Insets;
import java.awt.Robot;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import javax.swing.SwingUtilities;


/**
 *
 * Record events for later replay using java.awt.Robot
 *
 * Controlled by property lg.robot with 2 settings
 *      playback - plays a recorded session
 *      record - records a session
 *
 * During either playback or record key F1 will stop
 *
 * @author paulby
 */
public class Lg3dRobot implements KeyListener, MouseMotionListener, MouseListener, MouseWheelListener {
    
    private long lastEventTime = getCurrentTime();
    private PrintStream outStream=null;
    private DataInputStream inStream=null;
    private int version = 1;
    private Thread replayThread = null;
    private ArrayList<EventRecord> replayRecords=null;
    private boolean stopPlayback = false;
    
    /** Creates a new instance of EventRecorder */
    public Lg3dRobot() {
    }
    
    public void enableRecording(boolean enabled) {
        try {
            if (enabled) {
                String filename = System.getProperty("lg.robotfile", "test.lgbot");
                outStream = new PrintStream(new BufferedOutputStream(new FileOutputStream(filename)));
                outStream.println("VERSION "+version);
            } else {
                if (outStream!=null)
                    outStream.close();
                outStream = null;
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    public void enablePlayback(boolean enabled) {
        try {
                String filename = System.getProperty("lg.robotfile", "test.lgbot");
            inStream = new DataInputStream(new BufferedInputStream(new FileInputStream(filename)));
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        
        if (inStream!=null) {
            Runnable replay = new Runnable() {
                public void run() {
                    replayEvents();
                }
            };
            replayThread = new Thread(replay);
            replayThread.start();
        }
    }
    
    private void replayEvents() {
        EventRecord er;
        Robot robot = null;
        replayRecords = new ArrayList<EventRecord>();
        try {
            robot = new Robot();
        } catch(AWTException awte) {
            awte.printStackTrace();
            return;
        }
        robot.setAutoWaitForIdle(true);
        try {
                Reader r = new BufferedReader(new InputStreamReader(inStream));
                StreamTokenizer tok = new StreamTokenizer(r);
                nextTokenString(tok);        // VERSION
                nextTokenInt(tok);           // actual version number
                do {
                    er=readRecord(tok);
                    if (er!=null) {
                        replayRecords.add(er);
                        //er.replayEvent(robot);
                    }
                } while(er!=null);
                // Playback is finished
                inStream.close();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        
        int nextRecord = 0;
        while(!stopPlayback) {
            EventRecord r = replayRecords.get(nextRecord);
            if (r instanceof LoopRecord) {
                nextRecord = ((LoopRecord)r).getLoopTo();
            } else {
                r.replayEvent(robot);
                nextRecord++;
            }
        }
        
        replayThread=null;
    }
    
    protected void finalize() throws Throwable {
        if (outStream!=null)
            outStream.close();
    }
    
    public void keyPressed(java.awt.event.KeyEvent keyEvent) {
        System.out.println(keyEvent);
        if (keyEvent.getKeyCode()==keyEvent.VK_F1) {     // STOP
            if (replayThread!=null)
                stopPlayback=true;
            else {
                if (outStream!=null) {
                    outStream.close();
                    outStream=null;
                }
            }
        } else if (keyEvent.getKeyCode()==keyEvent.VK_F3) {
            if (outStream!=null)
                outStream.println("// Loop tag");
        }
    }

    public void keyReleased(java.awt.event.KeyEvent keyEvent) {
    }

    public void keyTyped(java.awt.event.KeyEvent keyEvent) {
    }

    public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
    }

    public void mouseDragged(java.awt.event.MouseEvent mouseEvent) {
        saveEvent(new MouseMoveRecord(getTimeDiff(), mouseEvent.getX(),  mouseEvent.getY()));
    }

    public void mouseEntered(java.awt.event.MouseEvent mouseEvent) {
    }

    public void mouseExited(java.awt.event.MouseEvent mouseEvent) {
    }

    public void mouseMoved(java.awt.event.MouseEvent mouseEvent) {
        Insets insets = SwingUtilities.windowForComponent(mouseEvent.getComponent()).getInsets();
        saveEvent(new MouseMoveRecord(getTimeDiff(), mouseEvent.getX()+insets.left,  mouseEvent.getY()+insets.top));
    }

    public void mousePressed(java.awt.event.MouseEvent mouseEvent) {
        saveEvent(new MouseButtonRecord(getTimeDiff(), true,mouseEvent.getButton()));
    }

    public void mouseReleased(java.awt.event.MouseEvent mouseEvent) {
        saveEvent(new MouseButtonRecord(getTimeDiff(), false,mouseEvent.getButton()));
    }
    
    public void mouseWheelMoved(java.awt.event.MouseWheelEvent e) {
        saveEvent(new MouseWheelRecord(getTimeDiff(), e.getWheelRotation()));
    }
    
    private void saveEvent(EventRecord event) {
        if (outStream==null)
            return;
        try {
            event.writeRecord(outStream);
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    private long getTimeDiff() {
        long currentTime = getCurrentTime();        
        long ret = currentTime-lastEventTime;
        lastEventTime = currentTime;
        return ret;
    }
    
    /**
     * Returns the current time in ms
     */
    private long getCurrentTime() {
        return System.nanoTime()/1000000l;
    }
    
    private EventRecord readRecord(DataInputStream in) throws IOException {
        EventRecord ret=null;
        try {
            int type = in.readInt();
            switch(type) {
                case EventRecord.MOUSE_MOVE:
                    ret = new MouseMoveRecord();
                    break;
                case EventRecord.MOUSE_BUTTON:
                    ret = new MouseButtonRecord();
                    break;
                default:
                    throw new IOException("Unrecognised Record "+type);
            }
            ret.readRecord(in);
        } catch(EOFException eofe) {
            ret = null;
        }

        return ret;
    }
    
    private EventRecord readRecord(StreamTokenizer tok) {
        EventRecord ret = null;
        try {
            int eventType = nextTokenInt(tok);  // Consume event type
            nextTokenString(tok);               // Consume event type name
            switch(eventType) {
                case EventRecord.MOUSE_MOVE:
                    ret = new MouseMoveRecord();
                    break;
                case EventRecord.MOUSE_BUTTON:
                    ret = new MouseButtonRecord();
                    break;
                case EventRecord.MOUSE_WHEEL:
                    ret = new MouseWheelRecord();
                    break;
                case EventRecord.LOOP:
                    ret = new LoopRecord();
                    break;
                default:
                    throw new IOException("Unrecognised Record "+eventType);
            }
            ret.readRecord(tok);
        } catch(EOFException eofe) {
            ret = null;
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return ret;
    }
    
    private int nextTokenInt(StreamTokenizer tok) throws IOException{
        int type = tok.nextToken();
        //System.out.println("Token "+tok.sval+" "+tok.nval);
        if (type==StreamTokenizer.TT_EOF)
            throw new EOFException();
        
        if (type!=StreamTokenizer.TT_NUMBER) {
            System.err.println("Error loading lgbot file, expected integer at line "+tok.lineno());
        }
        
        return (int)tok.nval;
    }
    
    private String nextTokenString(StreamTokenizer tok) throws IOException{
        int type = tok.nextToken();
        //System.out.println("Token "+tok.sval+" "+tok.nval);
        if (type==StreamTokenizer.TT_EOF)
            throw new EOFException();
        
        if (type!=StreamTokenizer.TT_WORD) {
            System.err.println("Error loading lgbot file, expected string at line "+tok.lineno());
        }
        
        return tok.sval;
    }

    abstract class EventRecord {
        
        static final int MOUSE_MOVE = 0;
        static final int MOUSE_BUTTON = 1;
        static final int MOUSE_WHEEL = 2;
        static final int KEY = 3;
        static final int LOOP = 4;
        
        final String[] eventNames = new String[] {
            "MOVE",
            "BUTTON",
            "WHEEL",
            "KEY",
            "LOOP"
        };
        
        protected int timeDelay;
        
        EventRecord() {
            timeDelay=0;
        }
        EventRecord(long timeDelay) {
            this.timeDelay = (int)timeDelay;
        }
        
        abstract void writeRecord(PrintStream out) throws IOException;
        
        abstract void readRecord(DataInputStream in) throws IOException;
        
        abstract void readRecord(StreamTokenizer tok) throws IOException;
        
        abstract void replayEvent(Robot robot);
        
    }
    
    class MouseMoveRecord extends EventRecord {
        int x;
        int y;
        
        MouseMoveRecord() {
        }
        
        MouseMoveRecord(long timeDelay, int x, int y) {
            super(timeDelay);
            this.x = x;
            this.y = y;
        }
        
        void postEvent(java.awt.Robot robot) {
            robot.delay(timeDelay);
            robot.mouseMove(x,y);
        }
        
        void writeRecord(PrintStream out) throws IOException {
//            out.writeInt(MOUSE_MOVE);
//            out.writeInt(timeDelay);
//            out.writeInt(x);
//            out.writeInt(y);
            out.println(MOUSE_MOVE+" "+eventNames[MOUSE_MOVE]+" "+timeDelay+" "+x+" "+y);
        }
        
        void readRecord(DataInputStream in) throws IOException {
            timeDelay = in.readInt();
            x = in.readInt();
            y = in.readInt();
        }
        
        void readRecord(StreamTokenizer tok) throws IOException {
            timeDelay = nextTokenInt(tok);
            x = nextTokenInt(tok);
            y = nextTokenInt(tok);
        }
        
        void replayEvent(Robot robot) {
            robot.delay(timeDelay);
            robot.mouseMove(x,y);
        }
        
        public String toString() {
            return "MouseMove "+x+","+y;
        }

    }
    
    class MouseWheelRecord extends EventRecord {
        
        int scrollAmount;
        
        MouseWheelRecord() {
        }
        
        MouseWheelRecord(long timeDelay, int amount) {
            super(timeDelay);
            this.scrollAmount = amount;
        }
        
        void replayEvent(java.awt.Robot robot) {
            robot.delay(timeDelay);
            robot.mouseWheel(scrollAmount);
        }
        
        void writeRecord(PrintStream out) throws IOException {
//            out.writeInt(MOUSE_WHEEL);
//            out.writeInt(timeDelay);
//            out.writeInt(scrollAmount);
            out.println(MOUSE_WHEEL+" "+eventNames[MOUSE_WHEEL]+" "+timeDelay+" "+scrollAmount);
        }
        
        void readRecord(DataInputStream in) throws IOException {
            timeDelay = in.readInt();
            scrollAmount = in.readInt();
        }
        
        void readRecord(StreamTokenizer tok) throws IOException {
            timeDelay = nextTokenInt(tok);
            scrollAmount = nextTokenInt(tok);
        }
        
        public String toString() {
            return "MouseWheel "+scrollAmount;
        }
        
    }
    
    
    class MouseButtonRecord extends EventRecord {
        
        boolean wasPress;
        int mask;
        
        MouseButtonRecord() {
        }
        
        MouseButtonRecord(long timeDelay, boolean wasPress, int button) {
            super(timeDelay);
            this.wasPress = wasPress;
            if (button==MouseEvent.BUTTON1) {
                mask = MouseEvent.BUTTON1_MASK;
            } else if (button==MouseEvent.BUTTON2) {
                mask = MouseEvent.BUTTON2_MASK;
            } else if (button==MouseEvent.BUTTON3) {
                mask = MouseEvent.BUTTON3_MASK;
            }
        }
        
        void writeRecord(PrintStream out) throws IOException {
//            out.writeInt(MOUSE_BUTTON);
//            out.writeInt(timeDelay);
//            out.writeBoolean(wasPress);
//            out.writeInt(mask);
            out.print(MOUSE_BUTTON+" "+eventNames[MOUSE_BUTTON]+" "+timeDelay);
            if (wasPress)
                out.print(" press");
            else
                out.print(" release");
            out.println(" "+mask);
         }
        
        void readRecord(DataInputStream in) throws IOException {
            timeDelay = in.readInt();
            wasPress = in.readBoolean();
            mask = in.readInt();
         }
        
        void readRecord(StreamTokenizer tok) throws IOException {
            //System.out.println("ButtonRead");
            timeDelay = nextTokenInt(tok);
            String state = nextTokenString(tok);
            mask = nextTokenInt(tok);
            
            if (state.equals("press"))
                wasPress = true;
            else
                wasPress = false;
        }
        
        void replayEvent(Robot robot) {
            
            robot.delay(timeDelay);
            if (wasPress) {
                robot.mousePress(mask);
            } else {
                robot.mouseRelease(mask);
            }
        }
        
        public String toString() {
            if (wasPress)
                return "ButtonPress "+mask;
            else
                return "ButtonRelease "+mask;
        }
    }
    
    class LoopRecord extends EventRecord {
        
        private int loopTo;
        
        public LoopRecord() {            
        }
        
        void writeRecord(PrintStream out) throws IOException {
            // TODO
         }
        
        void readRecord(DataInputStream in) throws IOException {
        }
        
        void readRecord(StreamTokenizer tok) throws IOException {
            //System.out.println("ButtonRead");
            timeDelay = nextTokenInt(tok);
            loopTo = nextTokenInt(tok);
        }
        void replayEvent(Robot robot) {
            // Nothing to do
        }
        
        public String toString() {
            return "Loop ";
        }
        
        public int getLoopTo() {
            return loopTo;
        }
    }
}
