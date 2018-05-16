/**
 *
 * Polaris Java Library - Afterschool Creatives "Captivating Creativity"
 *
 * Copyright 2018 Jhon Melvin Perello
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package org.afterschoolcreatives.polaris.java.reflection;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

/**
 *
 * @author Jhon Melvin
 */
public class PolarisReflection {

    public static PropertyDescriptor getPropertyDescriptor(Class javaClass, String propertyName) throws IntrospectionException {
        return new PropertyDescriptor(propertyName, javaClass);
    }

    /**
     * Invokes the write method for a field in a given object. [calls the setter
     * for a field in a given object.]
     *
     * @param hostObject The object instance.
     * @param propertyName the property of the object instance.
     * @param propertyValue the value to write in the field of the object
     * instance.
     * @return the host object write method return value.
     * @throws IntrospectionException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws java.security.PrivilegedActionException
     */
    public static Object invokePropertyWriteMethod(Object hostObject, String propertyName, Object propertyValue)
            throws IntrospectionException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, PrivilegedActionException {
        PropertyDescriptor propDescriptor = getPropertyDescriptor(hostObject.getClass(), propertyName);
        Method writeMethod = propDescriptor.getWriteMethod();
        //----------------------------------------------------------------------
        if (writeMethod == null) {
            throw new NullPointerException("Write method does not exist");
        }
        //----------------------------------------------------------------------
        // Make the write method accessible.
        if (!writeMethod.isAccessible()) {
            try {
                AccessController.doPrivileged((PrivilegedExceptionAction<Object>) () -> {
                    writeMethod.setAccessible(true);
                    return null;
                });
            } catch (PrivilegedActionException ex) {
                throw ex;
            }
        }
        // invoke method
        return writeMethod.invoke(hostObject, propertyValue);
    }

    /**
     * Invokes the read method for a field in a given object. [calls the getter
     * for a field in an object.]
     *
     * @param hostObject The object instance.
     * @param propertyName the property of the object instance.
     * @return the host object read method return value.
     * @throws IntrospectionException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws java.security.PrivilegedActionException
     */
    public static Object invokePropertyReadMethod(Object hostObject, String propertyName) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, PrivilegedActionException {
        PropertyDescriptor propDescriptor = getPropertyDescriptor(hostObject.getClass(), propertyName);
        Method readMethod = propDescriptor.getReadMethod();
        //----------------------------------------------------------------------
        // Make the write method accessible.
        if (!readMethod.isAccessible()) {
            try {
                AccessController.doPrivileged((PrivilegedExceptionAction<Object>) () -> {
                    readMethod.setAccessible(true);
                    return null;
                });
            } catch (PrivilegedActionException ex) {
                throw ex;
            }
        }
        // invoke method
        return readMethod.invoke(hostObject);
    }
}
