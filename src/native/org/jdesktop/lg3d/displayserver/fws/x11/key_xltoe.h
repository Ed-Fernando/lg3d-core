/*

$RCSfile: key_xltoe.h,v $

$Revision: 1.2 $
$Date: 2005-04-14 23:05:40 $
$State: Exp $

Copyright (c) 2005, Sun Microsystems, Inc. 

Permission to use, copy, modify, distribute, and sell this software and its
documentation for any purpose is hereby granted without fee, provided that
the above copyright notice appear in all copies and that both that
copyright notice and this permission notice appear in supporting
documentation

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
OPEN GROUP BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN
AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

Except as contained in this notice, the name of The Open Group shall not be
used in advertising or otherwise to promote the sale, use or other dealings
in this Software without prior written authorization from The Open Group.

*/

#ifndef KEY_XLTOE_H
#define KEY_XLTOE_H

#include "X11/X.h"
#include "X11/Xlib.h"

struct _XKeytrans {
	struct _XKeytrans *next;/* next on list */
	char *string;		/* string to return when the time comes */
	int len;		/* length of string (since NULL is legit)*/
	KeySym key;		/* keysym rebound */
	unsigned int state;	/* modifier state */
	KeySym *modifiers;	/* modifier keysyms you want */
	int mlen;		/* length of modifier list */
};

typedef struct dpy_key_info {
    XModifierKeymap *modifiermap;	/* This server's modifier keymap */
    KeySym lock_meaning;	        /* for XLookupString */
    int keysyms_per_keycode;            /* number of rows */
    unsigned int mode_switch;           /* keyboard group modifiers */
    unsigned int num_lock;              /* keyboard numlock modifiers */
    KeySym *keysyms;
    int min_keycode;                    /* Keycode with the smallest value */
    int max_keycode;                    /* Keycode with the largest value */

    /* I believe this is always NULL because XRebindKeysym is never called */
    struct _XKeytrans *key_bindings;

} DpyKeyInfo;

extern void key_xltoe_InitializeJNI (JNIEnv *env);

extern int key_xltoe_UpdateDpyKeyInfo (JNIEnv *env, jobject dpy);

extern int key_xltoe_XDisplayKeycodes (JNIEnv *env, jobject dpy, int *min_keycode_return, 
				       int *max_keycode_return);

extern KeySym *key_xltoe_GetKeySyms (JNIEnv *env, jobject dpy, int *keySymsPerKeyCode);

extern KeyCode key_xltoe_XKeysymToKeycode (JNIEnv *env, jobject dpy, int ks);

extern KeySym key_xltoe_XKeycodeToKeysym (JNIEnv *env, jobject dpy, KeyCode kc, int col);

extern int key_xltoe_XLookupString (JNIEnv *env, jobject dpy, unsigned int keycode, 
				    unsigned int state, KeySym *keysym_return);

#endif /* ! KEY_XLTOE_H */
