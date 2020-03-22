/*
 * $RCSfile: LightPipeWindow.c,v $
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
 * $Date: 2005-01-20 22:06:29 $
 * $State: Exp $
 */

#include <sys/ipc.h>
#include <sys/shm.h>
#include <jni.h>
#include <jawt_md.h>
#include <X11/Xlib.h>
#include <X11/Xutil.h>
#include <X11/extensions/XShm.h>
#include "org_jdesktop_lg3d_displayserver_fws_x11_LightPipeWindow.h"

JNIEXPORT jint JNICALL Java_org_jdesktop_lg3d_displayserver_fws_x11_LightPipeWindow_shmget
  (JNIEnv *env, jobject jobj, jint imageSize)
{
    int shmid;

    shmid = shmget(IPC_PRIVATE, imageSize, (IPC_CREAT|0600));
    return shmid;
}

JNIEXPORT jlong JNICALL Java_org_jdesktop_lg3d_displayserver_fws_x11_LightPipeWindow_shmat
  (JNIEnv *env, jobject jobj, jint shmid)
{
    long shmAddr;

    shmAddr = (long) shmat (shmid, 0, 0);

    return shmAddr;
}

JNIEXPORT jlong JNICALL Java_org_jdesktop_lg3d_displayserver_fws_x11_LightPipeWindow_shmdt
  (JNIEnv *env, jobject jobj, jlong shmAddr)
{
    shmdt((char *)shmAddr);
}

JNIEXPORT jlong JNICALL Java_org_jdesktop_lg3d_displayserver_fws_x11_LightPipeWindow_shmctl
  (JNIEnv *env, jobject jobj, jint shmid)
{
    shmctl(shmid, IPC_RMID, 0);
}



