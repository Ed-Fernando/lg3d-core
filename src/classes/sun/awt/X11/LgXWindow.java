/**
 * Project Looking Glass
 *
 * $RCSfile: LgXWindow.java,v $
 *
 * Copyright (c) 2006, Sun Microsystems, Inc., All Rights Reserved
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
 * $Date: 2008-01-31 03:15:11 $
 * $State: Exp $
 *
 */
package sun.awt.X11;

import java.util.Hashtable;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.media.j3d.Canvas3D;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public class LgXWindow {

    private static Hashtable<Integer, Character> jkeycode2UCSHash = new Hashtable<Integer, Character>();

    private static final boolean jdk60Interface;
    private static Method convertKeysymMethod = null;

    static {
	Class clazz;
	try {
	    clazz = Class.forName("sun.awt.X11.XKeysym");
	} catch (ClassNotFoundException ex) {
	    throw new RuntimeException(ex);
	}
	try {
	    convertKeysymMethod = clazz.getMethod("convertKeysym",
						  new Class[] { long.class, int.class});
	} catch (NoSuchMethodException ex) {}
	if (convertKeysymMethod == null) {
	    jdk60Interface = true;
	    try {
		    convertKeysymMethod = clazz.getMethod("convertKeysym", long.class);
	    } catch (NoSuchMethodException ex) {}
	    if (convertKeysymMethod == null) {
		throw new RuntimeException("Cannot find proper XKeysym.convertKeysym method");
	    }
	} else {
	    jdk60Interface = false;
	}
    }

    public LgXWindow () {}

    public int translateKeyEvent (LgXTranslatedKeyEvent[] keyEvents,
				  int xcode, int keycode, int state, long time) {
	LgXKeyEvent ev = new LgXKeyEvent(xcode, keycode, state, time);
	return translateKeyEvent(keyEvents, ev, state);
    }

    public int translateKeyEvent (LgXTranslatedKeyEvent[] keyEvents, XKeyEvent ev,
				  int state) {
        int keycode = java.awt.event.KeyEvent.VK_UNDEFINED;
        long keysym[] = new long[2];
        char unicodeKey = 0;
        keysym[0] = 0;
	int numKeyEvents;

	// TODO: eventually need to do an XInputMethodLookupString
	// in order to handle localized 3D key events.

        if(ev.get_type() == XConstants.KeyPress) {
            keysym[0] = XKeysym.getKeysym(ev);
	    Object result;
	    try {
		if (jdk60Interface) {
		    result = convertKeysymMethod.invoke(null, keysym[0]);
		} else {
		    result = convertKeysymMethod.invoke(null, keysym[0], state);
		}
		unicodeKey = ((Character)result).charValue();
	    } catch (IllegalAccessException ex) {
		throw new RuntimeException(ex);
	    } catch (InvocationTargetException ex) {
		throw new RuntimeException(ex);
	    }
        }

        // Keysym should be converted to Unicode, if possible and necessary,
        // and Java KeyEvent keycode should be calculated.
        // For press we should post pressed & typed Java events;
        // for release - released event.

        XKeysym.Keysym2JavaKeycode jkc = XKeysym.getJavaKeycode(ev);
        if( jkc == null ) {
            jkc = new XKeysym.Keysym2JavaKeycode(java.awt.event.KeyEvent.VK_UNDEFINED,
						 java.awt.event.KeyEvent.KEY_LOCATION_UNKNOWN);
        }

	if(ev.get_type() == XConstants.KeyRelease) {
	    // We obtain keysym from IM and derive unicodeKey from it for
	    // KeyPress only. After that, we cache this unicode character
	    // for Java keycode and now retrieve it.
	    Character c = jkeycode2UCSHash.get(jkc.getJavaKeycode());
	    unicodeKey = c == null ? 0 : c.charValue();
	}

	keycode = jkc.getJavaKeycode();
	if (ev.get_type() == XConstants.KeyPress) {
	    keyEvents[0].id = KeyEvent.KEY_PRESSED;
	} else {
	    keyEvents[0].id = KeyEvent.KEY_RELEASED;
	}
	keyEvents[0].when = XToolkit.nowMillisUTC_offset(ev.get_time());
	keyEvents[0].modifiers = getModifiers(ev.get_state(), 0, keycode);
	keyEvents[0].keyCode = keycode;
	keyEvents[0].keyChar = (unicodeKey == 0 ? java.awt.event.KeyEvent.CHAR_UNDEFINED : unicodeKey);
	keyEvents[0].keyLocation = jkc.getKeyLocation();
	numKeyEvents = 1;

	if((ev.get_type() == XConstants.KeyPress) && unicodeKey > 0 ) {
	    keycode = java.awt.event.KeyEvent.VK_UNDEFINED;
	    keyEvents[1].id = KeyEvent.KEY_TYPED;
	    keyEvents[1].when = XToolkit.nowMillisUTC_offset(ev.get_time());
	    keyEvents[1].modifiers = getModifiers(ev.get_state(), 0, keycode);
	    keyEvents[1].keyCode = keycode;
	    keyEvents[1].keyChar = unicodeKey;
	    keyEvents[1].keyLocation = java.awt.event.KeyEvent.KEY_LOCATION_UNKNOWN;
	    numKeyEvents++;

	    // cache character for later use with KeyRelease.
	    jkeycode2UCSHash.put(jkc.getJavaKeycode(), unicodeKey);
	}

	return numKeyEvents;
    }

    static int getModifiers(int state, int button, int keyCode) {
        int modifiers = 0;

        if (((state & XlibWrapper.ShiftMask) != 0) ^ (keyCode == KeyEvent.VK_SHIFT)) {
            modifiers |= InputEvent.SHIFT_DOWN_MASK;
        }
        if (((state & XlibWrapper.ControlMask) != 0) ^ (keyCode == KeyEvent.VK_CONTROL)) {
            modifiers |= InputEvent.CTRL_DOWN_MASK;
        }
        if (((state & XToolkit.metaMask) != 0) ^ (keyCode == KeyEvent.VK_META)) {
            modifiers |= InputEvent.META_DOWN_MASK;
        }
        if (((state & XToolkit.altMask) != 0) ^ (keyCode == KeyEvent.VK_ALT)) {
            modifiers |= InputEvent.ALT_DOWN_MASK;
        }
        if (((state & XToolkit.modeSwitchMask) != 0) ^ (keyCode == KeyEvent.VK_ALT_GRAPH)) {
            modifiers |= InputEvent.ALT_GRAPH_DOWN_MASK;
        }
        if (((state & XlibWrapper.Button1Mask) != 0) ^ (button == MouseEvent.BUTTON1)) {
            modifiers |= InputEvent.BUTTON1_DOWN_MASK;
        }
        if (((state & XlibWrapper.Button2Mask) != 0) ^ (button == MouseEvent.BUTTON2)) {
            modifiers |= InputEvent.BUTTON2_DOWN_MASK;
        }
        if (((state & XlibWrapper.Button3Mask) != 0) ^ (button == MouseEvent.BUTTON3)) {
            modifiers |= InputEvent.BUTTON3_DOWN_MASK;
        }
        return modifiers;
    }

    public static long nowMillisUTC_offset(long server_offset) {
	return XToolkit.nowMillisUTC_offset(server_offset);
    }
}
