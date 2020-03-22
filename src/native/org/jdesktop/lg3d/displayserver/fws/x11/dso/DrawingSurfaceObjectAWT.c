/*
 * $RCSfile: DrawingSurfaceObjectAWT.c,v $
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
 * $Revision: 1.2 $
 * $Date: 2004-06-23 03:58:49 $
 * $State: Exp $
 */

/*
 * Portions of this code were derived from work done by the Blackdown
 * group (www.blackdown.org), who did the initial Linux implementation
 * of the Java 3D API.
 */

#include <stdio.h>
#include <stdlib.h>
#include "org_jdesktop_lg3d_displayserver_fws_x11_dso_DrawingSurfaceObjectAWT.h"
#include <jawt_md.h>

#ifdef DEBUG
/* Uncomment the following for VERBOSE debug messages */
/* #define VERBOSE */
#endif /* DEBUG */


#if defined(SOLARIS) || defined(__linux__)
#pragma weak JAWT_GetAWT
#endif

#ifdef SOLARIS
#ifdef PANORAMIX
#pragma weak XDgaGetXineramaInfo

/*
 * The following include file contains the definitions of unsupported,
 * undocumented data structures and functions used to implement Xinerama.
 * They are also used by the Solaris implementations of OpenGL and AWT.  This
 * is an interim solution until they are made part of the X Window System
 * standard or replaced with a fully supported API.
 */
#include "panoramiXext.h"
#endif /* PANORAMIX */
#endif /* SOLARIS */

JNIEXPORT jlong JNICALL
Java_org_jdesktop_lg3d_displayserver_fws_x11_dso_DrawingSurfaceObjectAWT_getAWT
    (JNIEnv *env, jobject obj)
{
    JAWT *awt;

    awt = (JAWT*) malloc(sizeof(JAWT));
    if (awt == NULL) {
	 fprintf(stderr, "malloc failed\n");
	 return 0;
    }

    awt->version = JAWT_VERSION_1_4;

    if (JAWT_GetAWT(env, awt) == JNI_FALSE) {
	fprintf(stderr, "AWT not found\n");
	return 0;
    }
    return (jlong)awt;
}

JNIEXPORT
jlong JNICALL 
Java_org_jdesktop_lg3d_displayserver_fws_x11_dso_DrawingSurfaceObjectAWT_getDrawingSurfaceAWT(
    JNIEnv *env,
    jobject obj,
    jobject canvas,
    jlong awtObj)
{
    JAWT *awt = (JAWT*) awtObj;
    JAWT_DrawingSurface *ds;

    ds = awt->GetDrawingSurface(env, canvas);
    if (ds == NULL) {
	fprintf(stderr, "NULL drawing surface\n");
	return 0;
    }
    return (jlong)ds;
}


JNIEXPORT
jint JNICALL 
Java_org_jdesktop_lg3d_displayserver_fws_x11_dso_DrawingSurfaceObjectAWT_getDrawingSurfaceWindowIdAWT(
    JNIEnv *env,
    jobject obj,
    jobject canvas,
    jlong dsObj,
    jlong dsiObj,
    jlong display,
    jint screen,
    jboolean xineramaDisabled)
{
    JAWT_DrawingSurface *ds = (JAWT_DrawingSurface*) dsObj;
    JAWT_DrawingSurfaceInfo *dsi = (JAWT_DrawingSurfaceInfo *) dsiObj;
    jint window;

#ifdef WIN32
    JAWT_Win32DrawingSurfaceInfo *wds = 
	(JAWT_Win32DrawingSurfaceInfo*) dsi->platformInfo;
    window = (jint)wds->hdc;
#endif /* WIN32 */

#ifdef SOLARIS
    JAWT_X11DrawingSurfaceInfo *xds = 
	(JAWT_X11DrawingSurfaceInfo*) dsi->platformInfo;
    window = (jint)xds->drawable;

#ifdef PANORAMIX
    if (xineramaDisabled) {
	XineramaInfo xineramaInfo;

#ifdef VERBOSE
	fprintf(stderr, "getDrawingSurfaceWindowIdAWT: Xinerama disabled\n");
#endif /* VERBOSE */

	/*
	 * The existence of the weak symbol XDgaGetXineramaInfo is checked in
	 * the native MasterControl.initializeJ3D(); execution will not get
	 * here if it is unbound.
	 */
	if (XDgaGetXineramaInfo((Display *)display,
				xds->drawable, &xineramaInfo)) {

	    /* return Xinerama subwid instead of primary Xinerama wid */
	    window = (jint)xineramaInfo.subs[screen].wid; 

#ifdef VERBOSE
	    fprintf(stderr,
		    "  subwid for display %d screen %d window %d: %d\n",
		    (Display *)display, screen, xds->drawable, window);
#endif /* VERBOSE */
	}
	else {
	    window = (jint)xds->drawable;
	    fprintf(stderr, "Get Xinerama subwid, screen %d failed\n", screen);
	}
    }
#endif /* PANORAMIX */
#endif /* SOLARIS */

#ifdef __linux__
    JAWT_X11DrawingSurfaceInfo *xds = 
	(JAWT_X11DrawingSurfaceInfo*) dsi->platformInfo;
    window = (jint)xds->drawable;
#endif /* __linux__ */

    /*
     * Don't free DrawingSurfaceInfo here, otherwise
     * HDC will free in windows JDK1.4 and window
     * is invalid.
     */
    ds->env = env;
    ds->Unlock(ds);

    return window;
}



JNIEXPORT
jlong JNICALL 
Java_org_jdesktop_lg3d_displayserver_fws_x11_dso_DrawingSurfaceObjectAWT_getDrawingSurfaceInfo(
    JNIEnv *env,
    jobject obj,
    jlong dsObj)
{
    JAWT_DrawingSurface *ds = (JAWT_DrawingSurface*) dsObj;
    JAWT_DrawingSurfaceInfo *dsi;
    jint lock;

    ds->env = env;
    lock = ds->Lock(ds);
    if ((lock & JAWT_LOCK_ERROR) != 0) {
	 fprintf(stderr, "Error locking surface\n");
	 return 0;
    }

    dsi = ds->GetDrawingSurfaceInfo(ds);

    if (dsi == NULL) {
	 fprintf(stderr, "Error GetDrawingSurfaceInfo\n");
	 ds->Unlock(ds);
	 return 0;
    }
    return (jlong)dsi;
}

JNIEXPORT
jboolean JNICALL Java_org_jdesktop_lg3d_displayserver_fws_x11_dso_DrawingSurfaceObjectAWT_lockAWT(
    JNIEnv *env,
    jobject obj,
    jlong drawingSurface)
{
    JAWT_DrawingSurface *ds = (JAWT_DrawingSurface*)drawingSurface;
    jint lock;

    ds->env = env;
    lock = ds->Lock(ds);

    if ((lock & JAWT_LOCK_ERROR) != 0) {
        return JNI_FALSE;
    } else if ((lock & JAWT_LOCK_SURFACE_CHANGED) != 0) {
	ds->Unlock(ds);
        return JNI_FALSE;
    } else {
        return JNI_TRUE;
    }
}

JNIEXPORT
void JNICALL Java_org_jdesktop_lg3d_displayserver_fws_x11_dso_DrawingSurfaceObjectAWT_unlockAWT(
    JNIEnv *env,
    jobject obj,
    jlong drawingSurface)
{
    JAWT_DrawingSurface *ds = (JAWT_DrawingSurface*)drawingSurface;
    ds->env = env;
    ds->Unlock(ds);
}


JNIEXPORT
void JNICALL Java_org_jdesktop_lg3d_displayserver_fws_x11_dso_DrawingSurfaceObjectAWT_lockGlobal(
    JNIEnv *env,
    jclass obj,
    jlong awt)
{
    ((JAWT *) awt)->Lock(env);
}


JNIEXPORT
void JNICALL Java_org_jdesktop_lg3d_displayserver_fws_x11_dso_DrawingSurfaceObjectAWT_unlockGlobal(
    JNIEnv *env,
    jclass obj,
    jlong awt)
{
    ((JAWT *) awt)->Unlock(env);
}



JNIEXPORT
void JNICALL Java_org_jdesktop_lg3d_displayserver_fws_x11_dso_DrawingSurfaceObjectAWT_freeResource(
    JNIEnv *env,
    jobject obj,
    jlong awtObj,
    jlong drawingSurface,
    jlong drawingSurfaceInfo)
{
    JAWT *awt = (JAWT*) awtObj;
    JAWT_DrawingSurface *ds = (JAWT_DrawingSurface*)drawingSurface;
    JAWT_DrawingSurfaceInfo *dsi = (JAWT_DrawingSurfaceInfo *) drawingSurfaceInfo;
    
    ds->env = env;
    ds->FreeDrawingSurfaceInfo(dsi);
    awt->FreeDrawingSurface(ds);
}
