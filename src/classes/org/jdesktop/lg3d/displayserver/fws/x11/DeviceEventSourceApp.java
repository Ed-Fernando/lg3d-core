/**
 * Project Looking Glass
 *
 * $RCSfile: DeviceEventSourceApp.java,v $
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
 * $Revision: 1.10 $
 * $Date: 2006-09-11 20:56:23 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.fws.x11;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;
import javax.media.j3d.Canvas3D;
import gnu.x11.Data;
import gnu.x11.Display;
import gnu.x11.Enum;
import gnu.x11.Window;
import gnu.x11.event.*;
import gnu.x11.extension.NotFoundException;
import gnu.x11.event.Input;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.KeyListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.KeyEvent;
import java.awt.AWTEvent;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.security.PrivilegedActionException;

import java.lang.reflect.*;

/**
 * DeviceEventSourceApp is an EventBroker which provides raw device events
 * to the Picker. These events are acquired from AWT event listeners. This
 * event source is used when LG is running as an application within a host 
 * window system.
 */

class DeviceEventSourceApp extends DeviceEventSource
    implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener
{
    private Display cepDpy;
        
    // The screen absolute position of the last motion event received.
    // TODO: derive from display
    private Integer scrAbsPositionLock = new Integer(0);
    private int lastScrAbsX = 1280/2;
    private int lastScrAbsY = 1024/2;
    
    /*
     * These two HashMap store the content of the keymap.csv file...
     */
    private HashMap<Integer, Integer> AWTKeyCodes = null;
    private HashMap<Character, Integer> AWTKeyChars = null;
    
    /*
     * The bData Field is stored, for better performances.
     * Here, we're using an illegal case (the bDataField can't be ok if it's null) to show that it's uninitialized
     */
    private Field bDataField = null;
    private boolean bDataOk = true;
    
    private static final int KEYCODE_BACKSPACE = 22;

    private static final int KEYSYM_SHIFT_L   = 0xffe1;
    private static final int KEYSYM_SHIFT_R   = 0xffe2;
    private static final int KEYSYM_CONTROL_L = 0xffe3;
    private static final int KEYSYM_CONTROL_R = 0xffe4;
    private static final int KEYSYM_META_L    = 0xffe7;
    private static final int KEYSYM_META_R    = 0xffe8;
    private static final int KEYSYM_ALT_L     = 0xffe9;
    private static final int KEYSYM_ALT_R     = 0xffeA;
    private static final int KEYSYM_SUPER_L   = 0xffeb;
    private static final int KEYSYM_SUPER_R   = 0xffec;

    private boolean keyCodesKnown = false;
    private int keyCodeShiftL;
    private int keyCodeShiftR;
    private int keyCodeControlL;
    private int keyCodeControlR;
    private int keyCodeMetaL;
    private int keyCodeMetaR;
    private int keyCodeAltL;
    private int keyCodeAltR;
    private int keyCodeSuperL;
    private int keyCodeSuperR;

    private int modifierState = 0;

    private static final int SHIFT_L_DOWN   = 0x1;
    private static final int SHIFT_R_DOWN   = 0x2;
    private static final int CONTROL_L_DOWN = 0x4;
    private static final int CONTROL_R_DOWN = 0x8;
    private static final int META_L_DOWN    = 0x10;
    private static final int META_R_DOWN    = 0x20;
    private static final int ALT_L_DOWN     = 0x40;
    private static final int ALT_R_DOWN     = 0x80;
    private static final int SUPER_L_DOWN   = 0x100;
    private static final int SUPER_R_DOWN   = 0x200;

    // The current modifier state of the user keyboard
    private int kbdState = 0;

    public static final int KEYCODE_BYTE_OFFSET = 52;

    public DeviceEventSourceApp (Display devDpy, Display cepDpy, Window[] prwins, Canvas3D[] canvases) {
	super(devDpy, prwins, canvases);
	this.cepDpy = cepDpy;
	
	canvases[0].addMouseListener(this);
	canvases[0].addMouseMotionListener(this);
	canvases[0].addMouseWheelListener(this);
	canvases[0].addKeyListener(this);
        canvases[0].setFocusTraversalKeysEnabled(false);

        initLgServerKeymap(devDpy);
    }

    // Load the keymap/modifiermap of the User Display into the LG Server.
    private void initLgServerKeymap (Display lgDpy) {
        final String userDisplayName = ":0";
	EscherApplication userApplication = new EscherApplication(new String[] {"--display", userDisplayName}, 
								  "UserDisplay");
	Display userDpy = userApplication.getDisplay();
		
	// Verify that the min/max number of keycodes are the same for the two displays
	if (lgDpy.input.min_keycode != userDpy.input.min_keycode ||
	    lgDpy.input.max_keycode != userDpy.input.max_keycode) {
		throw new RuntimeException("Min/max number of keycodes differ for LG and User display");
	}

	// Load the user display keymap into the LG server
	userDpy.input.keyboard_mapping();

	lgDpy.input.change_keyboard_mapping(userDpy.input.min_keycode,
					    userDpy.input.keysyms_per_keycode,
					    userDpy.input.keysyms);
	    
	// Load the user display modifier map into the LG server
	Enum modmap = userDpy.input.modifier_mapping();
	int keycodesPerMod = modmap.start_count / 8;
	//System.err.println("keycodesPerMod = " + keycodesPerMod);
	//System.err.println("len modmap = " + modmap.data.length);
        byte[] modmapData = new byte[keycodesPerMod * 8];
	int i = 0;
        while (modmap.more()) {
		modmapData[i++] = (byte) modmap.next1();
	}
	lgDpy.input.set_modifier_mapping(keycodesPerMod, modmapData);
	lgDpy.check_error();
    }

    private void calcStateFromEventModifiers (InputEvent e) {
	int modifiers = e.getModifiersEx();
	kbdState = 0;

	if ((modifiers & InputEvent.SHIFT_DOWN_MASK) != 0) {
	    kbdState |= gnu.x11.Input.SHIFT_MASK;
	}
	if ((modifiers & InputEvent.CTRL_DOWN_MASK) != 0) {
	    kbdState |= gnu.x11.Input.CONTROL_MASK;
	}
	if ((modifiers & InputEvent.META_DOWN_MASK) != 0) {
	    kbdState |= gnu.x11.Input.META_MASK;
	}
	if ((modifiers & InputEvent.ALT_GRAPH_DOWN_MASK) != 0) {
	    kbdState |= gnu.x11.Input.SUPER_MASK;
	}
    }

    /* 
    ** If there were modifier keys pressed when the LG window lost focus
    ** and they are no longer pressed when we come back we need to
    ** manually "unpress" them.
    **
    ** TODO: Note: this does not handle the inverse case where the modifier 
    ** key was pressed outside the LG window (There is a P4 bug posted against
    ** this).
    */
    private void syncLgModifierState (InputEvent e) {
	int keycode;

        if ((modifierState & (SHIFT_L_DOWN | SHIFT_R_DOWN)) != 0) {
	    Data data = new Data(32);
	    if ((kbdState & gnu.x11.Input.SHIFT_MASK) == 0) {
	        //System.err.println("*********** Send Shift Release\n");
		if        ((modifierState & SHIFT_L_DOWN) != 0) {
		    keycode = keyCodeShiftL;
		    modifierState &= ~SHIFT_L_DOWN;
		} else if ((modifierState & SHIFT_R_DOWN) != 0) {
		    keycode = keyCodeShiftR;
		    modifierState &= ~SHIFT_R_DOWN;
		} else { // 
		    throw new RuntimeException("Illegal keyboard modifier state");
		}
		writeKeyData(data, KeyRelease.CODE, keycode, time(e.getWhen()), kbdState);
		enqueueEvent(new KeyRelease(devDpy, data.data));
	    }
	}

        if ((modifierState & (CONTROL_L_DOWN | CONTROL_R_DOWN)) != 0) {
	    Data data = new Data(32);
	    if ((kbdState & gnu.x11.Input.CONTROL_MASK) == 0) {
		//System.err.println("*********** Send Control Release\n");
		if        ((modifierState & CONTROL_L_DOWN) != 0) {
		    keycode = keyCodeControlL;
		    modifierState &= ~CONTROL_L_DOWN;
		} else if ((modifierState & CONTROL_R_DOWN) != 0) {
		    keycode = keyCodeControlR;
		    modifierState &= ~CONTROL_R_DOWN;
		} else { // 
		    throw new RuntimeException("Illegal keyboard modifier state");
		}
		writeKeyData(data, KeyRelease.CODE, keycode, time(e.getWhen()), kbdState);
		enqueueEvent(new KeyRelease(devDpy, data.data));
	    }
	}


        if ((modifierState & (META_L_DOWN | META_R_DOWN)) != 0) {
	    Data data = new Data(32);
	    if ((kbdState & gnu.x11.Input.META_MASK) == 0) {
		//System.err.println("*********** Send Meta Release\n");
		if        ((modifierState & META_L_DOWN) != 0) {
		    keycode = keyCodeMetaL;
		    modifierState &= ~META_L_DOWN;
		} else if ((modifierState & META_R_DOWN) != 0) {
		    keycode = keyCodeMetaR;
		    modifierState &= ~META_R_DOWN;
		} else { // 
		    throw new RuntimeException("Illegal keyboard modifier state");
		}
		writeKeyData(data, KeyRelease.CODE, keycode, time(e.getWhen()), kbdState);
		enqueueEvent(new KeyRelease(devDpy, data.data));
	    }
	}

        if ((modifierState & (ALT_L_DOWN | ALT_R_DOWN)) != 0) {
	    Data data = new Data(32);
	    if ((kbdState & gnu.x11.Input.ALT_MASK) == 0) {
		//System.err.println("*********** Send Alt Release\n");
		if        ((modifierState & ALT_L_DOWN) != 0) {
		    keycode = keyCodeMetaL;
		    modifierState &= ~ALT_L_DOWN;
		} else if ((modifierState & ALT_R_DOWN) != 0) {
		    keycode = keyCodeMetaR;
		    modifierState &= ~ALT_R_DOWN;
		} else { // 
		    throw new RuntimeException("Illegal keyboard modifier state");
		}
		writeKeyData(data, KeyRelease.CODE, keycode, time(e.getWhen()), kbdState);
		enqueueEvent(new KeyRelease(devDpy, data.data));
	    }
	}

        if ((modifierState & (SUPER_L_DOWN | SUPER_R_DOWN)) != 0) {
	    Data data = new Data(32);
	    if ((kbdState & gnu.x11.Input.SUPER_MASK) == 0) {
		//System.err.println("*********** Send Super Release\n");
		if        ((modifierState & SUPER_L_DOWN) != 0) {
		    keycode = keyCodeSuperL;
		    modifierState &= ~SUPER_L_DOWN;
		} else if ((modifierState & SUPER_R_DOWN) != 0) {
		    keycode = keyCodeSuperR;
		    modifierState &= ~SUPER_R_DOWN;
		} else { // 
		    throw new RuntimeException("Illegal keyboard modifier state");
		}
		writeKeyData(data, KeyRelease.CODE, keycode, time(e.getWhen()), kbdState);
		enqueueEvent(new KeyRelease(devDpy, data.data));
	    }
	}
    }

    public void mouseClicked(MouseEvent e) {
	// No-op
    }
    
    public void mouseEntered(MouseEvent e) {
	// No-op
    }

    public void mouseExited(MouseEvent e) {
	// No-op
    }

    // Note: the compiler can probably optimize a modulo by a power-of-two
    // into a shift, but today I'm paranoid. Use one bit less than the MAXINT
    // range in order to keep the timestamps positive (I'm not sure this is
    // necessary but it seems safer).
    private static final long LONG_TO_INT_MODULO_MASK = (1L << (Integer.SIZE-1)) - 1;

    private int time (long when) {
	// Fast way to calculate (when % Integer.MAX_VALUE)
	return (int)(when & LONG_TO_INT_MODULO_MASK);
    }

    // For Multi-click Debug
    // int releaseTime = 0;
        
    public void mousePressed(MouseEvent e) {
	Data data = new Data(32);
	calcStateFromEventModifiers(e);
        int time = time(e.getWhen());

	/* For Multi-click Debug
        if (releaseTime != 0) {
	    int delta = time - releaseTime;
	    System.err.println("releaseTime = " + releaseTime);
	    System.err.println("time = " + time);
	    System.err.println("delta = " + delta);
	    System.err.println();
	}
	*/

	writeButtonData(data, ButtonPress.CODE, e.getButton(), time, kbdState);
	enqueueEvent(new ButtonPress(devDpy, data.data));
    }

    public void mouseReleased(MouseEvent e) {
	Data data = new Data(32);
	calcStateFromEventModifiers(e);
        int time = time(e.getWhen());

	// For Multi-click Debug
	//releaseTime = time;

	writeButtonData(data, ButtonRelease.CODE, e.getButton(), time, kbdState);
	enqueueEvent(new ButtonRelease(devDpy, data.data));
    }

    public void mouseDragged(MouseEvent e) {
	mouseMoved(e);
    }

    public void mouseMoved(MouseEvent e) {
	Data data = new Data(32);
	writeMotionData(data, time(e.getWhen()), e.getX(), e.getY());
	enqueueEvent(new MotionNotify(devDpy, data.data));
    }
    
    public void mouseWheelMoved(MouseWheelEvent e) {
	Data data;	    

	// Note: awt always issues mouse wheel events with:
	//     scrollType = WHEEL_UNIT_SCROLL
	//     scrollAmount = 3
	//     unitsToScroll = scrollAmount * wheelRotation
	// So we can ignore these members.

	calcStateFromEventModifiers(e);
	int wheelRotation = e.getWheelRotation();
	int button = (wheelRotation == -1) ? 4 : 5;

	// First we send a ButtonPress event
	data = new Data(32);
	writeButtonData(data, ButtonPress.CODE, button, time(e.getWhen()), kbdState);
	enqueueEvent(new ButtonPress(devDpy, data.data));

	// Then we send the corresponding ButtonRelease event
	data = new Data(32);
	writeButtonData(data, ButtonRelease.CODE, button, time(e.getWhen()), kbdState);
	enqueueEvent(new ButtonRelease(devDpy, data.data));
    }
    
    // Note: Since we are going to be sending the keycode to the LG X Server
    // it is important that we use a display which is connected to 
    // the LG X Server. The display of the CookedEventPoller is connected
    // to the LG X Server and it's keyboard data has already been initialized.

    private int translateAwtKeyCodeToLgDisplayKeyCode (KeyEvent e) {
        int awtKeyCode = e.getKeyCode();
        int keysym;
	    
        if (AWTKeyCodes == null) {
            AWTKeyCodes = new HashMap<Integer, Integer>();
            AWTKeyChars = new HashMap<Character, Integer>();
            BufferedReader br = null;
            try {
                System.out.println(System.getProperty("lg.etcdir") + "/lg3d/keymap.csv");
                br = new BufferedReader(new FileReader(System.getProperty("lg.etcdir") + "/lg3d/keymap.csv"));
                String line = br.readLine();
                String FileKeyName, FileKeyChar;
                Integer FileKeySym, FileKeyCode;
                String[] arrayLine;
                while (line != null) {
                    arrayLine = line.split(";", 3);
                    FileKeyName = arrayLine[0];
                    FileKeySym = Integer.valueOf(arrayLine[1]);
                    if (FileKeyName.length() > 0) {
                        try {
                            FileKeyCode = KeyEvent.class.getDeclaredField(FileKeyName).getInt(e);
                            AWTKeyCodes.put(FileKeyCode, FileKeySym);
                        } catch (IllegalArgumentException ex) {
                            ex.printStackTrace();
                        } catch (SecurityException ex) {
                            ex.printStackTrace();
                        } catch (IllegalAccessException ex) {
                            ex.printStackTrace();
                        } catch (NoSuchFieldException ex) {
                            System.out.println("Unknown AWT key : " + FileKeyName);
                            ex.printStackTrace();
                        }
                    }
                    FileKeyChar = arrayLine[2];
                    if (FileKeyChar.length() == 1) {
                        AWTKeyChars.put(FileKeyChar.charAt(0), FileKeySym);
                    }
                    line = br.readLine();
                }
            } catch (IOException ioe) {
                System.out.println("Unable to read the keymap");
                ioe.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException ioe) {
                        //
                    }
                }
            }
        }
        if (AWTKeyCodes.containsKey(awtKeyCode)) {
            keysym = AWTKeyCodes.get(awtKeyCode);
        } else {
            if (AWTKeyChars.containsKey(e.getKeyChar())) {
                keysym = AWTKeyChars.get(e.getKeyChar());
            } else {
                keysym = 0;
            }
        }
        
	/*
        case KeyEvent.VK_NUMPAD0        :
    	    keysym = XK_KP_0;		
	    break;		
        case KeyEvent.VK_NUMPAD1        :
    	    keysym = XK_KP_1;		
	    break;		
        case KeyEvent.VK_NUMPAD2        :
    	    keysym = XK_KP_2;		
    	    keysym = 0;		
	    break;		
        case KeyEvent.VK_NUMPAD3        :
    	    keysym = XK_KP_3;		
    	    keysym = 0;		
	    break;		
        case KeyEvent.VK_NUMPAD4        :
    	    keysym = XK_KP_4;		
    	    keysym = 0;		
	    break;		
        case KeyEvent.VK_NUMPAD5        :
    	    keysym = XK_KP_5;		
    	    keysym = 0;		
	    break;		
        case KeyEvent.VK_NUMPAD6        :
    	    keysym = XK_KP_6;		
    	    keysym = 0;		
	    break;		
        case KeyEvent.VK_NUMPAD7        :
    	    keysym = 0;		
    	    keysym = XK_KP_7;		
	    break;		
        case KeyEvent.VK_NUMPAD8        :
    	    keysym = 0;		
    	    keysym = XK_KP_8;		
	    break;		
        case KeyEvent.VK_NUMPAD9        :
    	    keysym = 0;		
    	    keysym = XK_KP_9;		
	    break;		
        case KeyEvent.VK_BACK_QUOTE     :
    	    keysym = XK_quoteright;		
	    break;		
        case KeyEvent.VK_QUOTE          :
    	    keysym = XK_quoteleft;		
	    break;
	}*/

        if (keysym == 0) {
            throw new RuntimeException("Couldn't translate AWT keyCode = " + awtKeyCode);
	}

	int keycode = CookedEventPoller.keysymToKeycode(cepDpy, keysym);

	return keycode;
    }
    
    public int translateAwtEventToLgDisplayKeyCode(KeyEvent e) {
        // Handle the two valid cases : !bDataOk => no usable bData OR bDataField != null => usable bData
        if (bDataOk == false) {
            return translateAwtKeyCodeToLgDisplayKeyCode(e);
        } else if (bDataField != null) {
            try {
                return Integer.valueOf(((byte[]) bDataField.get(e))[KEYCODE_BYTE_OFFSET]);
            } catch (IllegalAccessException iae) {
                return translateAwtKeyCodeToLgDisplayKeyCode(e);
            }
        }
        // Remember : no need to set bDataOk to true
        if (bDataField == null) {
            try {
                bDataField = (Field)AccessController.doPrivileged(new PrivilegedExceptionAction() {
                    public Object run() throws IllegalAccessException, NoSuchMethodException {
                        Field f;
                        try {
                            f = AWTEvent.class.getDeclaredField("bdata");
                            f.setAccessible(true);
                        } catch (NoSuchFieldException e) {
                            f = null;
                        }
                        return f;
                    }
                });
            } catch (Exception except) {
                bDataField = null;
            }
            if (bDataField == null) {
                bDataOk = false;
            }
        }
        try {
            if (bDataField.get(e) == null) {
                bDataOk = false;
                return translateAwtKeyCodeToLgDisplayKeyCode(e);
            }
            return Integer.valueOf(((byte[]) bDataField.get(e))[KEYCODE_BYTE_OFFSET]);
        } catch (IllegalAccessException iae) {
            bDataOk = false;
            return translateAwtKeyCodeToLgDisplayKeyCode(e);
        }
    }

    // Shift-Backspace is broken in Xvfb because it doesn't initialize
    // XKB properly. Fortunately, shift-Backspace should have the same 
    // action as an unshifted backspace, so we forcibly release the shift 
    // key, send an unshifted backspace, and then automatically press the 
    // shift key again (this way we don't mess up the handling of other 
    // shifted keys).
        
    private void enqueueShiftBackspacePress (int state, long when) {
	Data data;
	int t = time(when);

	// The state with the shift mask released
	int unshiftedState = state & ~gnu.x11.Input.SHIFT_MASK;

	// Send a Shift release
	data = new Data(32);
	writeKeyData(data, KeyRelease.CODE, keyCodeShiftL, t, unshiftedState);
	enqueueEvent(new KeyRelease(devDpy, data.data));

	// Send the unshifted Backspace press
	data = new Data(32);
	writeKeyData(data, KeyPress.CODE, KEYCODE_BACKSPACE, t, unshiftedState);
	enqueueEvent(new KeyPress(devDpy, data.data));

	// Send a Shift press
	data = new Data(32);
	writeKeyData(data, KeyPress.CODE, keyCodeShiftL, t, kbdState);
	enqueueEvent(new KeyPress(devDpy, data.data));
    }

    private void enqueueKeyPress (int keycode, int state, long when) {

	// Workaround for an Xvfb XKB bug
	if  (keycode == KEYCODE_BACKSPACE && 
	    (state & gnu.x11.Input.SHIFT_MASK) != 0) {
	    enqueueShiftBackspacePress(kbdState, when);
	    return;
	}
	
	Data data = new Data(32);
	writeKeyData(data, KeyPress.CODE, keycode, time(when), kbdState);
	enqueueEvent(new KeyPress(devDpy, data.data));
    }

    private void enqueueKeyRelease (int keycode, int state, long when) {
	Data data = new Data(32);
	writeKeyData(data, KeyRelease.CODE, keycode, time(when), kbdState);
	enqueueEvent(new KeyRelease(devDpy, data.data));
    }

    public void keyPressed(KeyEvent e) {

	// TODO: I had problems doing this in the constructor. Figure this out.
	if (!keyCodesKnown) {
	    keyCodeShiftL = CookedEventPoller.keysymToKeycode(cepDpy, KEYSYM_SHIFT_L);
	    keyCodeShiftR = CookedEventPoller.keysymToKeycode(cepDpy, KEYSYM_SHIFT_R);
	    keyCodeControlL = CookedEventPoller.keysymToKeycode(cepDpy, KEYSYM_CONTROL_L);
	    keyCodeControlR = CookedEventPoller.keysymToKeycode(cepDpy, KEYSYM_CONTROL_R);
	    keyCodeMetaL = CookedEventPoller.keysymToKeycode(cepDpy, KEYSYM_META_L);
	    keyCodeMetaR = CookedEventPoller.keysymToKeycode(cepDpy, KEYSYM_META_R);
	    keyCodeAltL = CookedEventPoller.keysymToKeycode(cepDpy, KEYSYM_ALT_L);
	    keyCodeAltR = CookedEventPoller.keysymToKeycode(cepDpy, KEYSYM_ALT_R);
	    keyCodeSuperL = CookedEventPoller.keysymToKeycode(cepDpy, KEYSYM_SUPER_L);
	    keyCodeSuperR = CookedEventPoller.keysymToKeycode(cepDpy, KEYSYM_SUPER_R);
	    keyCodesKnown = true;
	}

        int keycode = translateAwtEventToLgDisplayKeyCode(e);
	//System.err.println("keycode = " + keycode);
	calcStateFromEventModifiers(e);
	syncLgModifierState(e);
	enqueueKeyPress(keycode, kbdState, e.getWhen());

	if        (keycode == keyCodeShiftL) {
	    modifierState |= SHIFT_L_DOWN;
	} else if (keycode == keyCodeShiftR) {
	    modifierState |= SHIFT_R_DOWN;
	} else if (keycode == keyCodeControlL) {
	    modifierState |= CONTROL_L_DOWN;
	} else if (keycode == keyCodeControlR) {
	    modifierState |= CONTROL_R_DOWN;
	} else if (keycode == keyCodeMetaL) {
	    modifierState |= META_L_DOWN;
	} else if (keycode == keyCodeMetaR) {
	    modifierState |= META_R_DOWN;
	} else if (keycode == keyCodeAltL) {
	    modifierState |= ALT_L_DOWN;
	} else if (keycode == keyCodeAltR) {
	    modifierState |= ALT_R_DOWN;
	} else if (keycode == keyCodeSuperL) {
	    modifierState |= SUPER_L_DOWN;
	} else if (keycode == keyCodeSuperR) {
	    modifierState |= SUPER_R_DOWN;
	}
    }

    public void keyReleased(KeyEvent e) {
	calcStateFromEventModifiers(e);
        int keycode = translateAwtEventToLgDisplayKeyCode(e);
	enqueueKeyRelease(keycode, kbdState, e.getWhen());

	if        (keycode == keyCodeShiftL) {
	    modifierState &= ~SHIFT_L_DOWN;
	} else if (keycode == keyCodeShiftR) {
	    modifierState &= ~SHIFT_R_DOWN;
	} else if (keycode == keyCodeControlL) {
	    modifierState &= ~CONTROL_L_DOWN;
	} else if (keycode == keyCodeControlR) {
	    modifierState &= ~CONTROL_R_DOWN;
	} else if (keycode == keyCodeMetaL) {
	    modifierState &= ~META_L_DOWN;
	} else if (keycode == keyCodeMetaR) {
	    modifierState &= ~META_R_DOWN;
	} else if (keycode == keyCodeAltL) {
	    modifierState &= ~ALT_L_DOWN;
	} else if (keycode == keyCodeAltR) {
	    modifierState &= ~ALT_R_DOWN;
	} else if (keycode == keyCodeSuperL) {
	    modifierState &= ~SUPER_L_DOWN;
	} else if (keycode == keyCodeSuperR) {
	    modifierState &= ~SUPER_R_DOWN;
	}
    }

    public void keyTyped(KeyEvent e) {
        // No-op
    }

    private void enqueueEvent (Input event) {
	DeviceEvent devEvent;

	devEvent = new DeviceEvent(devDpy, event);
	devEvent.window_offset = 12;

	devEvent.setCanvas(canvases[0]);
	devEvent.setPrwin(prwins[0]);

	trackLastPosition(devEvent);
	
	enqueue(devEvent);
    }
}



