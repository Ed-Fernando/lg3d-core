/**
 * Project Looking Glass
 *
 * $RCSfile: RestrictedAccessException.java,v $
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
 * $Date: 2004-06-23 18:50:29 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg;

/**
 * Indicates an attempt to access or modify a state variable
 * without permission to do so.  For example, invoking a set
 * method for a state variable that is currently read-only.
 */
public class RestrictedAccessException extends RuntimeException {

/**
 * Create the exception object with default values.
 */
  public RestrictedAccessException(){
  }

/**
 * Create the exception object that outputs a message.
 * @param str the message string to be output.
 */
  public RestrictedAccessException(String str) {

    super(str);
  }

}
