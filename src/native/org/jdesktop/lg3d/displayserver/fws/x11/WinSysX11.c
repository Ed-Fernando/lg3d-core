/*
 * $RCSfile: WinSysX11.c,v $
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
 * $Revision: 1.5 $
 * $Date: 2005-04-14 23:05:40 $
 * $State: Exp $
 */

#include <jawt_md.h>
#include <X11/Xlib.h>
#include "org_jdesktop_lg3d_displayserver_fws_x11_WinSysX11.h"

static char *dpyNameFormat = ":%d.0";

/* TODO: upgrade for multiscreen */

JNIEXPORT jlong JNICALL 
Java_org_jdesktop_lg3d_displayserver_fws_x11_WinSysX11_getAWT 
  (JNIEnv *env, jobject winsysx11)
{
    JAWT *pAwt;

    pAwt = (JAWT *) malloc(sizeof(JAWT));
    if (pAwt == NULL) {
	return 0;
    }

    pAwt->version = JAWT_VERSION_1_4;
    if (JAWT_GetAWT(env, pAwt) == JNI_FALSE) {
	free(pAwt);
	return 0;
    }

    return (jlong) pAwt;
}

JNIEXPORT jlong JNICALL 
Java_org_jdesktop_lg3d_displayserver_fws_x11_WinSysX11_getAWTDrawingSurface 
  (JNIEnv *env, jobject winsysx11, jlong jawt, jobject canvas)
{
    JAWT *pAwt = (JAWT *) jawt;
    JAWT_DrawingSurface *ds;

    ds = pAwt->GetDrawingSurface(env, canvas);
    if (ds == NULL) {
	return 0;
    }

    return (jlong) ds;
}

/*
** Opens a connection to an X11 display, with 0 as the default screen.
*/

JNIEXPORT jlong JNICALL 
Java_org_jdesktop_lg3d_displayserver_fws_x11_WinSysX11_openDisplay
  (JNIEnv *env, jclass clazz, jstring displayName, jlong jds)
{
    Display *dpy;
    JAWT_DrawingSurface *ds = (JAWT_DrawingSurface *) jds;
    const jbyte *dpyName;
    int lockStatus;

    dpyName = (*env)->GetStringUTFChars(env, displayName, NULL);
    if (dpyName == NULL) {
	/* OutOfMemory error has been already thrown */
	return -1;
    }

/*
fprintf(stderr, "Lock\n");
    lockStatus = ds->Lock(ds);
    if ((lockStatus & JAWT_LOCK_ERROR) != 0) {
	fprintf(stderr, "WinSysX11.openDisplay: AWT lock error\n");
	return -1;
    }
    */

    /* fprintf(stderr, "Opening display: %s\n", dpyName); */
    if ((dpy = XOpenDisplay((const char *)dpyName)) == NULL) {
/*
fprintf(stderr, "Unlock\n");
	ds->Unlock(ds);
	*/
	(*env)->ReleaseStringUTFChars(env, displayName, dpyName);
	(void)fprintf(stderr, "cannot open display %s\n", dpyName);
	return -1;
    }
    /* fprintf(stderr, "Display opened\n"); */

/*
fprintf(stderr, "Unlock\n");
    ds->Unlock(ds);
    */

    return (jlong) dpy;
}


/*
** Closes an X11 display.
*/

JNIEXPORT void JNICALL 
Java_org_jdesktop_lg3d_displayserver_fws_x11_WinSysX11_closeDisplay
  (JNIEnv *env, jclass clazz, jlong jdpy, jlong jds)
{
    Display *dpy = (Display *) jdpy;
    JAWT_DrawingSurface *ds = (JAWT_DrawingSurface *) jds;
    int lockStatus;

/*
fprintf(stderr, "Lock\n");
    lockStatus = ds->Lock(ds);
    if ((lockStatus & JAWT_LOCK_ERROR) != 0) {
	fprintf(stderr, "WinSysX11.closeDisplay: AWT lock error\n");
	return -1;
    }
    */

    XCloseDisplay(dpy);

/*
fprintf(stderr, "Unlock\n");
    ds->Unlock(ds);
    */
}

