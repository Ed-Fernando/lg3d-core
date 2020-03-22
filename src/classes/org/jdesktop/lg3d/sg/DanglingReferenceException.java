/**
 * Project Looking Glass
 *
 * $RCSfile: DanglingReferenceException.java,v $
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
 * $Date: 2004-06-23 18:50:22 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.sg;

/**
 * During a <code>cloneTree</code> call an updated reference was requested
 * for a node that did not get cloned.  This happens when a sub-graph is
 * duplicated via <code>cloneTree</code> and has at least one Leaf node
 * that contains a reference to a Node that has no corresponding node in
 * the cloned sub-graph. This results in two Leaf nodes wanting to share
 * access to the same Node.
 * <P>
 * If dangling references are to be allowed during the cloneTree call,
 * <code>cloneTree</code> should be called with the
 * <code>allowDanglingReferences</code> parameter set to <code>true</code>.
 * @see Node#cloneTree
 * @deprecated Not in WSG
 */
class DanglingReferenceException extends RuntimeException {

    /**
     * Create the exception object with default values.
     */
    DanglingReferenceException() {
    }

    /**
     * Create the exception object that outputs message.
     * @param str the message string to be output.
     */
    DanglingReferenceException(String str) {
	super(str);
    }

}
