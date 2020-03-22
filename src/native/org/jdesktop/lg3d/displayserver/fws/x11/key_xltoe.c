/*
 * $RCSfile: key_xltoe.c,v $
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
 * $Date: 2005-06-24 19:49:07 $
 * $State: Exp $
 */

/*
** These routines convert Xlib calls which deal with keyboard information
** into equivalent calls into Escher. Since Escher does not implement all
** of the keyboard information routines that Xlib does various routines 
** have been extracted from Xlib and have been placed here and modified
** appropriately.
*/

#include <jni.h>
#include <jawt_md.h>
#include "key_xltoe.h"
#include "X11/X.h"
#include "X11/Xproto.h"
#include "X11/keysym.h"     /* standard X keysyms */

#define EscherDisplayToDpyKeyInfo(env, dpy) \
    ((DpyKeyInfo *)((*env)->GetLongField((env), (dpy), fidDpyUserDataHandle)))

#define AllMods (ShiftMask|LockMask|ControlMask| \
		 Mod1Mask|Mod2Mask|Mod3Mask|Mod4Mask|Mod5Mask)

static DpyKeyInfo *CreateDpyKeyInfo (JNIEnv *env, jobject dpy);
static int GetKeySymArray (JNIEnv *env, DpyKeyInfo *dki, jobject input);
static int InitModMap (JNIEnv *env, DpyKeyInfo *dki, jobject input);
static int GetModifierMapping (JNIEnv *env, DpyKeyInfo *dki, jobject input);
static void ResetModMap (DpyKeyInfo *dki);
static void ComputeMaskFromKeytrans (DpyKeyInfo *dki, struct _XKeytrans *p);
static unsigned _XKeysymToModifiers (DpyKeyInfo *dki, int ks);
static KeySym KeyCodetoKeySym (DpyKeyInfo *dki, KeyCode keycode, int col);
static int _XTranslateKey (DpyKeyInfo *dki, unsigned int keycode,
			   unsigned int modifiers, KeySym *keysym_return);
static void print_dki (DpyKeyInfo *dki);

/*********************************************************************************
**						                                **
**        JNI Initialization Routine			                        **
**						                                **
*********************************************************************************/

#define ESCHER_DISPLAY_CLASS_PATH "gnu/x11/Display"
#define ESCHER_ENUM_CLASS_PATH "gnu/x11/Enum"
#define ESCHER_INPUT_CLASS_PATH "gnu/x11/Input"

static jclass dpyClass;
static jclass enumClass;
static jclass inputClass;
static jclass arrayClass;

static jfieldID fidDpyInput;
static jfieldID fidDpyUserDataHandle;
static jfieldID fidEnumStartCount;
static jfieldID fidInputMinKeycode;
static jfieldID fidInputMaxKeycode;
static jfieldID fidInputKeySyms;    
static jfieldID fidInputKeySymsPerKeyCode;

static jmethodID midEnumNext1;
static jmethodID midInputKeyboardMapping;
static jmethodID midInputModifierMapping;

void key_xltoe_InitializeJNI (JNIEnv *env)
{
    /* 
    ** Initialize Display Fields
    */

    dpyClass = (*env)->FindClass(env, ESCHER_DISPLAY_CLASS_PATH);
    if (dpyClass == NULL) {
	fprintf(stderr, "Cannot find class %s\n", ESCHER_DISPLAY_CLASS_PATH);
	exit(1);
    }

    fidDpyInput = (*env)->GetFieldID(env, dpyClass, "input", "Lgnu/x11/Input;");
    if (fidDpyInput == NULL) {
        fprintf(stderr, "Cannot find field gnu.x11.Display.input\n");
	exit(1);
    }
    
    fidDpyUserDataHandle = (*env)->GetFieldID(env, dpyClass, "userDataHandle", "J");
    if (fidDpyUserDataHandle == NULL) {
        fprintf(stderr, "Cannot find field gnu.x11.Display.userDataHandle\n");
	exit(1);
    }
    
    /* 
    ** Initialize Enum Fields and Methods
    */

    enumClass = (*env)->FindClass(env, ESCHER_ENUM_CLASS_PATH);
    if (enumClass == NULL) {
	fprintf(stderr, "Cannot find class %s\n", ESCHER_ENUM_CLASS_PATH);
	exit(1);
    }

    fidEnumStartCount = (*env)->GetFieldID(env, enumClass, "start_count", "I");
    if (fidEnumStartCount == NULL) {
        fprintf(stderr, "Cannot find field gnu.x11.Enum.start_count\n");
	exit(1);
    }

    midEnumNext1 = (*env)->GetMethodID(env, enumClass, "next1", "()I");
    if (midEnumNext1 == NULL) {
        fprintf(stderr, "Cannot find method gnu.x11.Enum.next1()\n");
	exit(1);
    }

    /* 
    ** Initialize Input Fields and Methods
    */

    inputClass = (*env)->FindClass(env, ESCHER_INPUT_CLASS_PATH);
    if (inputClass == NULL) {
	fprintf(stderr, "Cannot find class %s\n", ESCHER_INPUT_CLASS_PATH);
	exit(1);
    }

    fidInputMinKeycode = (*env)->GetFieldID(env, inputClass, "min_keycode", "I");
    if (fidInputMinKeycode == NULL) {
        fprintf(stderr, "Cannot find field gnu.x11.Input.min_keycode\n");
	exit(1);
    }

    fidInputMaxKeycode = (*env)->GetFieldID(env, inputClass, "max_keycode", "I");
    if (fidInputMaxKeycode == NULL) {
        fprintf(stderr, "Cannot find field gnu.x11.Input.max_keycode\n");
	exit(1);
    }

    fidInputKeySyms = (*env)->GetFieldID(env, inputClass, "keysyms", "[I");
    if (fidInputKeySyms == NULL) {
        fprintf(stderr, "Cannot find field gnu.x11.Input.keysyms\n");
	exit(1);
    }

    fidInputKeySymsPerKeyCode = (*env)->GetFieldID(env, inputClass, 
						   "keysyms_per_keycode", "I");
    if (fidInputKeySymsPerKeyCode == NULL) {
        fprintf(stderr, "Cannot find field gnu.x11.Input.keysyms_per_keycode\n");
	exit(1);
    }

    midInputKeyboardMapping = (*env)->GetMethodID(env, inputClass, "keyboard_mapping", "()V");
    if (midInputKeyboardMapping == NULL) {
        fprintf(stderr, "Cannot find method gnu.x11.Input.keyboard_mapping()\n");
	exit(1);
    }

    midInputModifierMapping = (*env)->GetMethodID(env, inputClass, "modifier_mapping", 
						  "()Lgnu/x11/Enum;");
    if (midInputModifierMapping == NULL) {
        fprintf(stderr, "Cannot find method gnu.x11.Input.modifier_mapping()\n");
	exit(1);
    }
}


/*********************************************************************************
**						                                **
**        Routines for Initializing Keyboard Information                        **
**						                                **
**	  These routines pull the keyboard information out of the               **
**	  Escher display and stash it in a native structure cache               **
**	  attached to the escher display.                                       **
**						                                **
*********************************************************************************/

int
key_xltoe_UpdateDpyKeyInfo (JNIEnv *env, jobject dpy)
{
    DpyKeyInfo *dki = EscherDisplayToDpyKeyInfo(env, dpy);
    KeySym *keysyms;
    jobject input;

    if (dki == NULL) {
	dki = CreateDpyKeyInfo(env, dpy);
    }

    /* Get min/max keycodes */
    input = (*env)->GetObjectField(env, dpy, fidDpyInput);
    dki->min_keycode = (*env)->GetIntField(env, input, fidInputMinKeycode);
    dki->max_keycode = (*env)->GetIntField(env, input, fidInputMaxKeycode);

    /* Fetch the latest keysyms from the X server */
    if (!GetKeySymArray (env, dki, input)) {
	return 0;
    }

    if (!InitModMap(env, dki, input)) {
	return 0;
    }

    /* For debug
    print_dki(dki);
    */

    return 1;
}

/* 
** Fetches keyboard information from the Escher display and stashes 
** it in the DpyKeyInfo structure. Unlike its Xlib counterpart, this
** routine always fully reinitializes the modifier map, so that 
** if it has changed in the Escher display the cache can be properly
** updated.
**
** Xlib Counterpart: _XKeyInitialize
*/

static DpyKeyInfo *
CreateDpyKeyInfo (JNIEnv *env, jobject dpy)
{
    DpyKeyInfo *dki;

    dki = (DpyKeyInfo *) malloc(sizeof(DpyKeyInfo));    
    if (dki == NULL) {
	return NULL;
    }

    dki->modifiermap = NULL;	
    dki->keysyms = NULL;
    dki->key_bindings = NULL;

    (*env)->SetLongField(env, dpy, fidDpyUserDataHandle, dki);

    return dki;
}

/*
** Fetches the keysyms array and related information from the X server.
**
** XLib Counterpart: XGetKeyboardMapping
*/

static int
GetKeySymArray (JNIEnv *env, DpyKeyInfo *dki, jobject input)
{
    unsigned long nkeysyms;
    KeySym *mapping, *pkeysym;
    jintArray keysymsAry;
    jint *pksa;
    int i;

    /* Destroy the keysyms array */
    if (dki->keysyms != NULL) {
	free(dki->keysyms);
	dki->keysyms = NULL;
    }

    /* Fetch the keyboard mapping from the X server */
    (*env)->CallVoidMethod(env, input, midInputKeyboardMapping);

    /*
    ** Read information out of the Escher Input object
    */

    keysymsAry = (jintArray)(*env)->GetObjectField(env, input, fidInputKeySyms);    
    if (keysymsAry == NULL) {
	goto Error;
    }

    nkeysyms = (*env)->GetArrayLength(env, keysymsAry);
    if (nkeysyms <= 0) {
	goto Error;
    }

    mapping = (KeySym *) malloc ((unsigned)(nkeysyms * sizeof(KeySym)));
    if (mapping == NULL) {
	goto Error;
    }

    pksa = (jint *) (*env)->GetPrimitiveArrayCritical(env, keysymsAry, 0);
    pkeysym = &mapping[0];
    for (i = 0; i < nkeysyms; i++, pkeysym++, pksa++) {
	*pkeysym = *pksa;
    }    
    (*env)->ReleasePrimitiveArrayCritical(env, keysymsAry, pksa, JNI_ABORT);

    dki->keysyms = mapping;
    dki->keysyms_per_keycode = (*env)->GetIntField(env, input, fidInputKeySymsPerKeyCode);

    return 1;

Error:
    dki->keysyms = NULL;
    dki->keysyms_per_keycode = 0;

    return 0;
}

/*
** Fetches the modifier mapping from the X server and recalculates
** derived information.
**
** XLib Counterpart: InitModMap
*/

static int
InitModMap (JNIEnv *env, DpyKeyInfo *dki, jobject input)
{
    XModifierKeymap *map;

    /* Destroy the modifier map */
    if (dki->modifiermap != NULL) {
	free(dki->modifiermap->modifiermap);
	free(dki->modifiermap);
	dki->modifiermap = NULL;
    }

    if (!GetModifierMapping(env, dki, input)) {
	return 0;
    }

    if (dki->keysyms != NULL) {
	ResetModMap(dki);
    }

    return 1;
}

/*
** Fetches the modifier mapping from the X server.
**
** XLib Counterpart: XGetModifierMapping
*/

static int
GetModifierMapping (JNIEnv *env, DpyKeyInfo *dki, jobject input)
{
    jobject enumObj;
    int numKeyCodes;
    int i;
    unsigned char *pkc;

    /* Fetch the modifier mapping from the X server */
    enumObj = (*env)->CallObjectMethod(env, input, midInputModifierMapping);
    if (enumObj == NULL) {
	return 0;
    }

    /* Allocated the native modifier map */
    dki->modifiermap = (XModifierKeymap *) malloc(sizeof(XModifierKeymap));
    if (dki->modifiermap == NULL) {
	return 0;
    }

    /* Get the number of keycodes in the modifier map */
    numKeyCodes = (*env)->GetIntField(env, enumObj, fidEnumStartCount);
    dki->modifiermap->max_keypermod = numKeyCodes / 8;

    /* Allocate the native array for the modifier map */
    dki->modifiermap->modifiermap = (KeyCode *) malloc(numKeyCodes * sizeof(KeyCode));
    if (dki->modifiermap->modifiermap == NULL) {
	free(dki->modifiermap);
	dki->modifiermap = 0;
	return 0;
    }

    /* Copy modifier map keycodes from Escher */
    pkc = dki->modifiermap->modifiermap;
    for (i = 0; i < numKeyCodes; i++) { 	       
	int keyCode = (*env)->CallIntMethod(env, enumObj, midEnumNext1);
	*pkc++ = (unsigned char) keyCode;
    }

    return 1;
}

/*
** Recalculates keyboard information that is derived from both
** the keysym array and the modifier mapping.
**
** XLib Counterpart: ResetModMap
*/

static void
ResetModMap (DpyKeyInfo *dki)
{
    XModifierKeymap *map;
    int i, j, n;
    KeySym sym;
    struct _XKeytrans *p;

    map = dki->modifiermap;

    /* If any Lock key contains Caps_Lock, then interpret as Caps_Lock,
     * else if any contains Shift_Lock, then interpret as Shift_Lock,
     * else ignore Lock altogether.
     */
    dki->lock_meaning = NoSymbol;
    /* Lock modifiers are in the second row of the matrix */
    n = 2 * map->max_keypermod;
    for (i = map->max_keypermod; i < n; i++) {
	for (j = 0; j < dki->keysyms_per_keycode; j++) {
	    sym = KeyCodetoKeySym(dki, map->modifiermap[i], j);
	    if (sym == XK_Caps_Lock) {
		dki->lock_meaning = XK_Caps_Lock;
		break;
	    } else if (sym == XK_Shift_Lock) {
		dki->lock_meaning = XK_Shift_Lock;
	    }
	    else if (sym == XK_ISO_Lock) {
		dki->lock_meaning = XK_Caps_Lock;
		break;
	    }
	}
    }

    /* Now find any Mod<n> modifier acting as the Group or Numlock modifier */
    dki->mode_switch = 0;
    dki->num_lock = 0;
    n *= 4;
    for (i = 3*map->max_keypermod; i < n; i++) {
	for (j = 0; j < dki->keysyms_per_keycode; j++) {
	    sym = KeyCodetoKeySym(dki, map->modifiermap[i], j);
	    if (sym == XK_Mode_switch)
		dki->mode_switch |= 1 << (i / map->max_keypermod);
	    if (sym == XK_Num_Lock)
		dki->num_lock |= 1 << (i / map->max_keypermod);
	}
    }

    for (p = dki->key_bindings; p; p = p->next)
	ComputeMaskFromKeytrans(dki, p);
}

/*
** given a list of modifiers, computes the mask necessary for later matching.
** This routine must lookup the key in the Keymap and then search to see
** what modifier it is bound to, if any.  Sets the AnyModifier bit if it
** can't map some keysym to a modifier.
**
** Xlib Counterpart: ComputeMaskFromKeytrans
*/

static void
ComputeMaskFromKeytrans (DpyKeyInfo *dki, struct _XKeytrans *p)
{
    int i;

    p->state = AnyModifier;
    for (i = 0; i < p->mlen; i++) {
	/* TODO: XKB not yet supported 
	p->state|= XkbKeysymToModifiers(dki,p->modifiers[i]);
	*/
	p->state|= _XKeysymToModifiers(dki,p->modifiers[i]);
    }
    p->state &= AllMods;
}

/*
** Xlib Counterpart: _XKeysymToModifiers
*/

static unsigned
_XKeysymToModifiers (DpyKeyInfo *dki, int ks)
{
    CARD8 code,mods;
    KeySym *kmax;
    KeySym *k;
    XModifierKeymap *m;

    kmax = dki->keysyms + 
	   (dki->max_keycode - dki->min_keycode + 1) * dki->keysyms_per_keycode;
    k = dki->keysyms;
    m = dki->modifiermap;
    mods= 0;
    while (k<kmax) {
	if (*k == ks ) {
	    int j = m->max_keypermod<<3;

	    code=(((k-dki->keysyms)/dki->keysyms_per_keycode)+dki->min_keycode);

	    while (--j >= 0) {
		if (code == m->modifiermap[j])
		    mods|= (1<<(j/m->max_keypermod));
	    }
	}
	k++;
    }
    return mods;
}

/*********************************************************************************
**						                                **
**        Routines for Acquiring Keyboard Information                           **
**						                                **
**	  These routines are used by keyboard.c to get information about        **
**	  the key mapping, modifier mapping, etc. These routines are            **
**        identical to their Xlib counterparts, with the exception that         **
**	  the information is stored in the native structure cache instead       **
**        directly in the display object itself.				**
**						                                **
*********************************************************************************/

/*
** Xlib Counterpart: XDisplayKeycodes
*/

int
key_xltoe_XDisplayKeycodes (JNIEnv *env, jobject dpy, int *min_keycode_return, 
			    int *max_keycode_return)
{
    DpyKeyInfo *dki = EscherDisplayToDpyKeyInfo(env, dpy);

    *min_keycode_return = dki->min_keycode;
    *max_keycode_return = dki->max_keycode;

    return 1;
}

/*
** Returns the enire keysyms array. Also returns the number of keysyms per
** keycode.
**
** Xlib Counterpart: Somewhat like XGetKeyboardMapping, except returns the entire
** keysyms array instead of a specified range.
*/

KeySym *
key_xltoe_GetKeySyms (JNIEnv *env, jobject dpy, int *keySymsPerKeyCode) 
{
    DpyKeyInfo *dki = EscherDisplayToDpyKeyInfo(env, dpy);
    *keySymsPerKeyCode = dki->keysyms_per_keycode;

    return dki->keysyms;
}

/*
** Xlib Counterpart: XKeysymToKeycode
*/

KeyCode
key_xltoe_XKeysymToKeycode (JNIEnv *env, jobject dpy, int ks)
{
    DpyKeyInfo *dki = EscherDisplayToDpyKeyInfo(env, dpy);
    int i, j;

    for (j = 0; j < dki->keysyms_per_keycode; j++) {
	for (i = dki->min_keycode; i <= dki->max_keycode; i++) {
	    if (KeyCodetoKeySym(dki, (KeyCode) i, j) == ks)
		return i;
	}
    }
    return 0;
}

/*
** Xlib Counterpart: KeyCodeToKeysym
*/

static KeySym
KeyCodetoKeySym (DpyKeyInfo *dki, KeyCode keycode, int col)
{
    int per = dki->keysyms_per_keycode;
    KeySym *syms;
    KeySym lsym, usym;

    if ((col < 0) || ((col >= per) && (col > 3)) ||
	((int)keycode < dki->min_keycode) || ((int)keycode > dki->max_keycode))
      return NoSymbol;

    syms = &dki->keysyms[(keycode - dki->min_keycode) * per];
    if (col < 4) {
	if (col > 1) {
	    while ((per > 2) && (syms[per - 1] == NoSymbol))
		per--;
	    if (per < 3)
		col -= 2;
	}
	if ((per <= (col|1)) || (syms[col|1] == NoSymbol)) {
	    XConvertCase(syms[col&~1], &lsym, &usym);
	    if (!(col & 1))
		return lsym;
	    else if (usym == lsym)
		return NoSymbol;
	    else
		return usym;
	}
    }
    return syms[col];
}

/*
** Xlib Counterpart: XKeycodeToKeysym
*/

KeySym
key_xltoe_XKeycodeToKeysym (JNIEnv *env, jobject dpy, KeyCode kc, jint col)
{
    DpyKeyInfo *dki = EscherDisplayToDpyKeyInfo(env, dpy);

    return KeyCodetoKeySym(dki, kc, col);
}

/*
** This is a stripped down version of XLookupString. Since keyboard.c doesn't
** use the returned string buffer for the keysym we don't need to calculate it.
**
** Xlib Counterpart: XLookupString
*/

int
key_xltoe_XLookupString (JNIEnv *env, jobject dpy, unsigned int keycode, 
			 unsigned int state, KeySym *keysym_return)
{
    DpyKeyInfo *dki = EscherDisplayToDpyKeyInfo(env, dpy);
    KeySym symbol;

    if (!_XTranslateKey(dki, keycode, state, keysym_return)) {
	return 0;
    }

    return 1;
}

/*
** Translates the keycode and modifiers into a keysym.
**
** Xlib Counterpart: _XTranslateKey
*/

static int
_XTranslateKey (DpyKeyInfo *dki, unsigned int keycode,
		unsigned int modifiers, KeySym *keysym_return)
{
    int per;
    KeySym *syms;
    KeySym sym, lsym, usym;

    if (((int)keycode < dki->min_keycode) || ((int)keycode > dki->max_keycode))
    {
	*keysym_return = NoSymbol;
	return 1;
    }

    per = dki->keysyms_per_keycode;
    syms = &dki->keysyms[(keycode - dki->min_keycode) * per];
    while ((per > 2) && (syms[per - 1] == NoSymbol))
	per--;
    if ((per > 2) && (modifiers & dki->mode_switch)) {
	syms += 2;
	per -= 2;
    }
    if ((modifiers & dki->num_lock) &&
	(per > 1 && (IsKeypadKey(syms[1]) || IsPrivateKeypadKey(syms[1])))) {
	if ((modifiers & ShiftMask) ||
	    ((modifiers & LockMask) && (dki->lock_meaning == XK_Shift_Lock)))
	    *keysym_return = syms[0];
	else
	    *keysym_return = syms[1];
    } else if (!(modifiers & ShiftMask) &&
	(!(modifiers & LockMask) || (dki->lock_meaning == NoSymbol))) {
	if ((per == 1) || (syms[1] == NoSymbol))
	    XConvertCase(syms[0], keysym_return, &usym);
	else
	    *keysym_return = syms[0];
    } else if (!(modifiers & LockMask) ||
	       (dki->lock_meaning != XK_Caps_Lock)) {
	if ((per == 1) || ((usym = syms[1]) == NoSymbol))
	    XConvertCase(syms[0], &lsym, &usym);
	*keysym_return = usym;
    } else {
	if ((per == 1) || ((sym = syms[1]) == NoSymbol))
	    sym = syms[0];
	XConvertCase(sym, &lsym, &usym);
	if (!(modifiers & ShiftMask) && (sym != syms[0]) &&
	    ((sym != usym) || (lsym == usym)))
	    XConvertCase(syms[0], &lsym, &usym);
	*keysym_return = usym;
    }
    if (*keysym_return == XK_VoidSymbol)
	*keysym_return = NoSymbol;

    return 1;
}

/*********************************************************************************
**						                                **
**        Debug Routines	                                                **
**						                                **
*********************************************************************************/

static void
print_dki (DpyKeyInfo *dki)
{
    XModifierKeymap *modmap;
    KeyCode *modifierKeys;
    KeySym *keysym;
    int keysymCount;
    int i, k;
    
    fprintf(stderr, "Display Keyboard Info\n\n");

    fprintf(stderr, "min_keycode = %d\n", dki->min_keycode);
    fprintf(stderr, "max_keycode = %d\n", dki->max_keycode);
    fprintf(stderr, "\n");

    fprintf(stderr, "lock_meaning = %d\n", dki->lock_meaning);
    fprintf(stderr, "mode_switch = %d\n", dki->mode_switch);
    fprintf(stderr, "num_lock = %d\n", dki->num_lock);
    fprintf(stderr, "\n");

    modmap = dki->modifiermap;
    fprintf(stderr, "max_keypermod = %d\n", modmap->max_keypermod);

    modifierKeys = modmap->modifiermap;
    for (i = 0; i < 8; i++) {
	fprintf(stderr, "keycodes for modifier %d:\t", i);
	for (k = 0; k < modmap->max_keypermod; k++) {
	    fprintf(stderr, "%d\t", *modifierKeys++);
	}
	fprintf(stderr, "\n");
    }

    fprintf(stderr, "\n");

    fprintf(stderr, "keysyms_per_keycode = %d\n", dki->keysyms_per_keycode);

    keysymCount = dki->max_keycode - dki->min_keycode + 1;
    keysym = dki->keysyms;
    for (i = 0; i < keysymCount; i++) {
	fprintf(stderr, "keysyms for modifier %d:\t", i);
        for (k = 0; k < dki->keysyms_per_keycode; k++) {
	    fprintf(stderr, "%d\t", *keysym++);
	}
	fprintf(stderr, "\n");
    }
}
