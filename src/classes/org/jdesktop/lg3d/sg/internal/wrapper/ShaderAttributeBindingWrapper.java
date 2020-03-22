/*
 * $RCSfile: ShaderAttributeBindingWrapper.java,v $
 *
 * Copyright (c) 2005 Sun Microsystems, Inc. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * $Revision: 1.2 $
 * $Date: 2005-06-24 19:48:32 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.sg.internal.wrapper;

import javax.vecmath.*;

/**
 * The ShaderAttributeBindingWrapper object encapsulates a uniform attribute
 * whose value is bound to a Java&nbsp;3D system attribute. The
 * shader variable <code>attrName</code> is implicitly set to the
 * value of the corresponding Java&nbsp;3D system attribute
 * <code>j3dAttrName</code> during rendering. <code>attrName</code>
 * must be the name of a valid uniform attribute in the shader in
 * which it is used. Otherwise, the attribute name will be ignored and
 * a runtime error may be generated. <code>j3dAttrName</code> must be
 * the name of a predefined Java&nbsp;3D system attribute. An
 * IllegalArgumentException will be thrown if the specified
 * <code>j3dAttrName</code> is not one of the predefined system
 * attributes. Further, the type of the <code>j3dAttrName</code>
 * attribute must match the type of the corresponding
 * <code>attrName</code> variable in the shader in which it is
 * used. Otherwise, the shader will not be able to use the attribute
 * and a runtime error may be generated.
 * 
 * <p>
 * Following is the list of predefined Java&nbsp;3D system attributes:<br>
 * 
 * <ul>
 * <font color="#ff0000"><i>TODO: replace the following with
 * the real system attributes table</i></font><br>
 * <table BORDER=1 CELLSPACING=2 CELLPADDING=2>
 * <tr>
 * <td><b>Name</b></td>
 * <td><b>Type</b></td>
 * <td><b>Description</b></td>
 * </tr>
 * <tr>
 * <td><code>something</code></td>
 * <td>Float</td>
 * <td>This is something (of course)</td>
 * </tr>
 * <tr>
 * <td><code>somethingElse</code></td>
 * <td>Tuple3f</td>
 * <td>This is something else</td>
 * </tr>
 * </table>
 * </ul>
 * 
 * <p>
 * Depending on the shading language (and profile) being used, several
 * Java 3D state attributes are automatically made available to the
 * shader program as pre-defined uniform attributes. The application
 * doesn't need to do anything to pass these attributes in to the
 * shader program. The implementation of each shader language (e.g.,
 * Cg, GLSL) defines its own mapping from Java 3D attribute to uniform
 * variable name.
 * 
 * <p>
 * A list of these attributes for each shader language can be found in
 * the concrete subclass of ShaderProgram for that shader language.
 * 
 * <p>
 * <font color="#ff0000"><i>NOTE: This class is not yet
 * implemented.</i></font><br>
 * 
 * @see ShaderAttributeSet
 * @see ShaderProgram
 */

public interface ShaderAttributeBindingWrapper extends ShaderAttributeWrapper {


    /**
     * Retrieves the name of the Java 3D system attribute that is bound to this
     * shader attribute.
     *
     * @return the name of the Java 3D system attribute that is bound to this
     * shader attribute
     */
    public String getJ3DAttributeName();

}
