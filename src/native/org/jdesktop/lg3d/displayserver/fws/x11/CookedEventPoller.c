/*
 * $RCSfile: CookedEventPoller.c,v $
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
 * $Date: 2006-09-07 20:01:01 $
 * $State: Exp $
 */

#include <errno.h>
#include <poll.h>
#include <jni.h>
#include <jawt_md.h>
#include <X11/Xlib.h>
#include "key_xltoe.h"
#include "org_jdesktop_lg3d_displayserver_fws_x11_CookedEventPoller.h"

JNIEXPORT jstring JNICALL 
Java_org_jdesktop_lg3d_displayserver_fws_x11_CookedEventPoller_XGetDefault
  (JNIEnv *env, jclass clazz, jlong jdpy, jlong jds, jstring jprogram, jstring joption)
{
    Display *dpy = (Display *) jdpy;
    JAWT_DrawingSurface *ds = (JAWT_DrawingSurface *) jds;
    const jbyte *progStr = NULL;
    const jbyte *optStr = NULL;
    char *valueStr = NULL;
    jstring jvalueStr = NULL;
    int lockStatus;
    
    progStr = (*env)->GetStringUTFChars(env, jprogram, NULL);
    if (progStr == NULL) {
	goto Exit;
    }

    optStr = (*env)->GetStringUTFChars(env, joption, NULL);
    if (optStr == NULL) {
	goto Exit;
    }

/*
fprintf(stderr, "Lock\n");
    lockStatus = ds->Lock(ds);
    if ((lockStatus & JAWT_LOCK_ERROR) != 0) {
	fprintf(stderr, "CookedEventPoller.XGetDefault: AWT lock error\n");
	return -1;
    }
    */

    valueStr = XGetDefault(dpy, progStr, optStr);
        
/*
fprintf(stderr, "Unlock\n");
    ds->Unlock(ds);
    */

Exit:

    if (progStr != NULL) {
	(*env)->ReleaseStringUTFChars(env, jprogram, progStr);
    }    

    if (optStr != NULL) {
	(*env)->ReleaseStringUTFChars(env, joption, optStr);
    }

    if (valueStr != NULL) {
	jvalueStr = (*env)->NewStringUTF(env, valueStr);
    }

    return jvalueStr;
}

JNIEXPORT void JNICALL 
Java_org_jdesktop_lg3d_displayserver_fws_x11_CookedEventPoller_nativeInitKey
  (JNIEnv *env, jclass clazz, jobject dpy)
{
    key_xltoe_InitializeJNI(env);

    if (!key_xltoe_UpdateDpyKeyInfo(env, dpy)) {
	fprintf(stderr, "key_xltoe_UpdateDpyKeyInfo failed.\n");
	exit(1);
    }
}

JNIEXPORT jint JNICALL 
Java_org_jdesktop_lg3d_displayserver_fws_x11_CookedEventPoller_keysymToKeycode
  (JNIEnv *env, jobject cepObj, jobject dpy, jint keysym)
{
    KeyCode keycode;

    keycode = key_xltoe_XKeysymToKeycode(env, dpy, keysym);

    return keycode;
}
