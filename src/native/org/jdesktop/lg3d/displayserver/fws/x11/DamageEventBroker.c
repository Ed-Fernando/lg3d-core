/*
 * $RCSfile: DamageEventBroker.c,v $
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
 * $Revision: 1.8 $
 * $Date: 2006-03-27 21:44:38 $
 * $State: Exp $
 */

#include <jni.h>
#include <X11/Xlib.h>
#include "org_jdesktop_lg3d_displayserver_fws_x11_DamageEventBroker.h"


#ifdef DEBUG

#include <fcntl.h>

#define DAMAGE_IMAGE_LOG_FILENAME "/var/tmp/lgdamage.log"

static int fd = -1;

static void
logImageRect32(int srcX, int srcY, int srcImageWidth, int srcImageHeight, 
	       long srcImageData)
{
    int srcLineBytes = srcImageWidth << 2;
    unsigned char *pSrc8;
    int row, col;
    int depth = 32;

    if (fd == -1) {
	fd = open(DAMAGE_IMAGE_LOG_FILENAME, O_WRONLY|O_CREAT);
	if (fd < 0) {
	    perror("logImageRect32: log file open failure");
	    exit(1);
	}
    }

    /* Log image dimensions */
    write(fd, &depth, 4);
    write(fd, &srcX, 4);
    write(fd, &srcY, 4);
    write(fd, &srcImageWidth, 4);
    write(fd, &srcImageHeight, 4);

    /* Log image body */
    pSrc8 = (unsigned char *) srcImageData;
    pSrc8 += srcLineBytes * srcY;
    for (row = 0; row < srcImageHeight-srcY; row++) {
	unsigned int *pSrc32 = (unsigned int *) pSrc8;
	for (col = srcX; col < srcImageWidth; col++) {
	    unsigned int pixel = *(pSrc32 + col);
	    write(fd, &pixel, 4); 
	}
        pSrc8 += srcLineBytes;
    } 
}

static void
logImageRect16 (int srcX, int srcY, int srcImageWidth, int srcImageHeight, 
		long srcImageData)
{
    int srcLineBytes = srcImageWidth << 1;
    unsigned char *pSrc8;
    int row, col;
    int depth = 16;

    if (fd == -1) {
	fd = open(DAMAGE_IMAGE_LOG_FILENAME, O_WRONLY);
	if (fd < 0) {
	    perror("logImageRect32: log file open failure");
	    exit(1);
	}
    }

    /* Log image dimensions */
    write(fd, &depth, 4);
    write(fd, &srcX, 4);
    write(fd, &srcY, 4);
    write(fd, &srcImageWidth, 4);
    write(fd, &srcImageHeight, 4);

    /* Log image body */
    pSrc8 = (unsigned char *) srcImageData;
    pSrc8 += srcLineBytes * srcY;
    for (row = 0; row < srcImageHeight-srcY; row++) {
	unsigned short *pSrc16 = (unsigned short *) pSrc8;
	for (col = srcX; col < srcImageWidth; col++) {
	    unsigned int pixel = *(pSrc16 + col);
	    write(fd, &pixel, 4); /* 16b -> 32b */
	}
        pSrc8 += srcLineBytes;
    } 
}

#endif /* DEBUG */

JNIEXPORT void JNICALL 
Java_org_jdesktop_lg3d_displayserver_fws_x11_DamageEventBroker_copyImageEntire32
  (JNIEnv *env, jclass debClass, jintArray intAry, jint dstImageWidth, 
   jint srcImageWidth, jint srcImageHeight, jlong srcImageData)
{
    int srcLineBytes = srcImageWidth << 2;
    int dstLineBytes = dstImageWidth << 2;
    unsigned char *pSrc, *pDst;
    int row;

    /*
    fprintf(stderr, "Copying Entire Ximage into Java image\n");
    fprintf(stderr, "srcData = %d (0x%x)\n", srcImageData, srcImageData);
    fprintf(stderr, "src image w,h  = %d,%d\n", srcImageWidth, srcImageHeight);
    fprintf(stderr, "slb = %d, dlb = %d\n", srcLineBytes, dstLineBytes);
    */

#ifdef DEBUG
    logImageRect32(0, 0, srcImageWidth, srcImageHeight, srcImageData);
#endif /* DEBUG */

    pDst = (unsigned char *)(*env)->GetPrimitiveArrayCritical(env, intAry, NULL);
    pSrc = (unsigned char *) srcImageData;

    for (row = 0; row < srcImageHeight; row++) {
	memcpy(pDst, pSrc, srcLineBytes);
        pSrc += srcLineBytes;
	pDst += dstLineBytes;
    } 

    (*env)->ReleasePrimitiveArrayCritical(env, intAry, (void *)pDst, 0);
}

JNIEXPORT void JNICALL 
Java_org_jdesktop_lg3d_displayserver_fws_x11_DamageEventBroker_copyImageRect32
  (JNIEnv *env, jclass debClass, jintArray intAry, jint dstX, jint dstY,
   jint dstW, jint dstH, jint srcX, jint srcY, jint dstImageWidth, 
   jint srcImageWidth, jint srcImageHeight, jlong srcImageData)
{
    int srcLineBytes = srcImageWidth << 2;
    int dstLineBytes = dstImageWidth << 2;
    int widthBytes = dstW << 2;
    unsigned char *pSrc, *pDst;
    int row;

    /*
    fprintf(stderr, "copyImageRect32: dst x,y,w,h = %d %d %d %d\n",
	    dstX, dstY, dstW, dstH);
    fprintf(stderr, "src x,y = %d, %d\n", srcX, srcY);
    fprintf(stderr, "src image w,h  = %d,%d\n", srcImageWidth, srcImageHeight);
    fprintf(stderr, "slb = %d, dlb = %d\n", srcLineBytes, dstLineBytes);
    fprintf(stderr, "widthBytes = %d\n", widthBytes);
    */

    if (widthBytes < 0) {
        return;
    }

#ifdef DEBUG
    logImageRect32(srcX, srcY, srcImageWidth, srcImageHeight, srcImageData);
#endif /* DEBUG */

    pDst = (unsigned char *)(*env)->GetPrimitiveArrayCritical(env, intAry, NULL);
    pDst += dstLineBytes * dstY + (dstX << 2);
    pSrc = (unsigned char *) srcImageData;
    pSrc += srcLineBytes * srcY + (srcX << 2);

    for (row = 0; row < dstH; row++) {
	memcpy(pDst, pSrc, widthBytes);
	pSrc += srcLineBytes;
	pDst += dstLineBytes;
    } 

    (*env)->ReleasePrimitiveArrayCritical(env, intAry, (void *)pDst, 0);
}

JNIEXPORT void JNICALL 
Java_org_jdesktop_lg3d_displayserver_fws_x11_DamageEventBroker_copyImageEntire16
  (JNIEnv *env, jclass debClass, jshortArray shrtAry, jint dstImageWidth, 
   jint srcImageWidth, jint srcImageHeight, jlong srcImageData)
{
    int srcLineBytes = srcImageWidth << 1;
    int dstLineBytes = dstImageWidth << 1;
    unsigned char *pSrc, *pDst;
    int row;

    /*
    fprintf(stderr, "Copying Entire Ximage into Java image\n");
    fprintf(stderr, "src image w,h = %d,%d\n", srcImageWidth, srcImageHeight);
    fprintf(stderr, "slb = %d, dlb = %d\n", srcLineBytes, dstLineBytes);
    */

#ifdef DEBUG
    logImageRect16(0, 0, srcImageWidth, srcImageHeight, srcImageData);
#endif /* DEBUG */

    pDst = (unsigned char *)(*env)->GetPrimitiveArrayCritical(env, shrtAry, NULL);
    pSrc = (unsigned char *) srcImageData;

    for (row = 0; row < srcImageHeight; row++) {
	memcpy(pDst, pSrc, srcLineBytes);
        pSrc += srcLineBytes;
	pDst += dstLineBytes;
    } 

    (*env)->ReleasePrimitiveArrayCritical(env, shrtAry, (void *)pDst, 0);
}

JNIEXPORT void JNICALL 
Java_org_jdesktop_lg3d_displayserver_fws_x11_DamageEventBroker_copyImageRect16
  (JNIEnv *env, jclass debClass, jshortArray shrtAry, jint dstX, jint dstY,
   jint dstW, jint dstH, jint srcX, jint srcY, jint dstImageWidth, 
   jint srcImageWidth, jint srcImageHeight, jlong srcImageData)
{
    int srcLineBytes = srcImageWidth << 1;
    int dstLineBytes = dstImageWidth << 1;
    int widthBytes = dstW << 1;
    unsigned char *pSrc, *pDst;
    int row;

    if (widthBytes < 0) {
        return;
    }

#ifdef DEBUG
    logImageRect16(srcX, srcY, srcImageWidth, srcImageHeight, srcImageData);
#endif /* DEBUG */

    pDst = (unsigned char *)(*env)->GetPrimitiveArrayCritical(env, shrtAry, NULL);
    pDst += dstLineBytes * dstY + (dstX << 1);
    pSrc = (unsigned char *) srcImageData;
    pSrc += srcLineBytes * srcY + (srcX << 1);

    for (row = 0; row < dstH; row++) {
	memcpy(pDst, pSrc, widthBytes);
	pSrc += srcLineBytes;
	pDst += dstLineBytes;
    } 

    (*env)->ReleasePrimitiveArrayCritical(env, shrtAry, (void *)pDst, 0);
}

