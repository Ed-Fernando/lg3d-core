/*
 * $RCSfile: ShaderAttributeValueWrapper.java,v $
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



/**
 * The ShaderAttributeValueWrapper object encapsulates a uniform shader
 * attribute whose value is specified explicitly. The shader variable
 * <code>attrName</code> is explicitly set to the specified
 * <code>value</code> during rendering. <code>attrName</code> must be
 * the name of a valid uniform attribute in the shader in which it is
 * used. Otherwise, the attribute name will be ignored and a runtime
 * error may be generated. The <code>value</code> must be an instance
 * of one of the allowed classes. The allowed classes are:
 * <code>Integer</code>, <code>Float</code>, <code>Double</code>,
 * <code>Tuple{2,3,4}{i,f,d}</code>, <code>Matrix{3,4}{f,d}</code>. A
 * ClassCastException will be thrown if a specified <code>value</code>
 * object is not one of the allowed types. Further, the type of the
 * value is immutable once a ShaderAttributeValueWrapper is constructed.
 * Subsequent setValue operations must be called with an object of the
 * same type as the one that was used to construct the
 * ShaderAttributeValueWrapper. Finally, the type of the <code>value</code>
 * object must match the type of the corresponding
 * <code>attrName</code> variable in the shader in which it is
 * used. Otherwise, the shader will not be able to use the attribute
 * and a runtime error may be generated.
 * 
 * @see ShaderAttributeSet
 * @see ShaderProgram
 */

public interface ShaderAttributeValueWrapper extends ShaderAttributeObjectWrapper {

    // Implement abstract getValue method
    public Object getValue();

    // Implement abstract setValue method
    public void setValue(Object value);

}
