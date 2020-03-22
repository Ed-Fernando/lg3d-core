/*
 * $RCSfile: ShaderAttributeArrayWrapper.java,v $
 *
 * Copyright (c) 2005 Sun Microsystems, Inc. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * $Revision: 1.2 $
 * $Date: 2005-06-24 19:48:31 $
 * $State: Exp $
 */

package org.jdesktop.lg3d.sg.internal.wrapper;

import javax.vecmath.*;

/**
 * The ShaderAttributeArrayWrapper object encapsulates a uniform shader
 * attribute whose value is specified explicitly. The shader variable
 * <code>attrName</code> is explicitly set to the specified
 * <code>value</code> during rendering. <code>attrName</code> must be
 * the name of a valid uniform attribute in the shader in which it is
 * used. Otherwise, the attribute name will be ignored and a runtime
 * error may be generated. The <code>value</code> must be an array
 * of one of the allowed classes. The allowed classes are:
 * <code>Integer[]</code>, <code>Float[]</code>, <code>Double[]</code>,
 * <code>Tuple{2,3,4}{i,f,d}[]</code>, <code>Matrix{3,4}{f,d}[]</code>. A
 * ClassCastException will be thrown if a specified <code>value</code>
 * object is not one of the allowed types. Further, the type and length of the
 * value is immutable once a ShaderAttributeArrayWrapper is constructed.
 * Subsequent setValue operations must be called with an array of the
 * same type and length as the one that was used to construct the
 * ShaderAttributeArrayWrapper. Finally, the type of the <code>value</code>
 * object must match the type of the corresponding
 * <code>attrName</code> variable in the shader in which it is
 * used. Otherwise, the shader will not be able to use the attribute
 * and a runtime error may be generated.
 * 
 * @see ShaderAttributeSet
 * @see ShaderProgram
 * 
 * @since Java 3D 1.4
 */

public interface ShaderAttributeArrayWrapper extends ShaderAttributeObjectWrapper {

    // Implement abstract getValue method
    public Object getValue();

    // Implement abstract setValue method
    public void setValue(Object value);


    /**
     * Sets the specified array element of the value of this shader
     * attribute to the specified value.
     * A copy of the object is stored.
     *
     * @param value the new value of the shader attribute
     *
     * @exception NullPointerException if value is null
     *
     * @exception ClassCastException if value is not an instance of
     * the same base class as the individual elements of the array object
     * used to construct this shader attribute object.
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public void setValue(int index, Object value);

    /**
     * Returns the number of elements in the value array.
     *
     * @return the number of elements in the value array
     *
     * @exception CapabilityNotSetException if appropriate capability is 
     * not set and this object is part of live or compiled scene graph
     */
    public int length();

}
