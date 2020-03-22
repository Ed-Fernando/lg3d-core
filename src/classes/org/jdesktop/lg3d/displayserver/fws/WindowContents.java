/**
 * Project Looking Glass
 *
 * $RCSfile: WindowContents.java,v $
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
 * $Date: 2005-01-20 22:05:19 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.displayserver.fws;

/**
 * An object which describes an image which contains the 
 * contents of a native window.
 */

public class WindowContents {
    public int width;                // Width of image
    public int height;               // Height of image
    public int bytesPerLine;         // Number of bytes per scanline
    public int nativeMemorySize;     // Number of bytes of native image data memory
    public long nativeMemoryPointer; // Pointer to native image data memory
}
