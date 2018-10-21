/**
 *
 * Objective SQL - Afterschool Creatives "Captivating Creativity"
 *
 *
 * Copyright 2018 Jhon Melvin Nieto Perello
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
 * Contact Us:
 * Facebook: www.facebook.com/afterschoolcreatives
 * Google Mail: afterschoolcreatives@gmail.com
 *
 */
package org.afterschoolcreatives.polaris.java.sql.osql.util;

/**
 *
 * @author Jhon Melvin
 */
public class TypeCaster {

    /**
     * Casts an object to a specific type.
     *
     * @param object
     * @param type
     * @return
     */
    public final static Object autoCast(Object object, Class type) {

        if (object == null) {
            return null;
        }
        // type must not be null.
        if (type == null) {
            throw new NullPointerException("Class type cannot be null, please specify a type to run auto cast");
        }
        // wrap data if primitive
        if (type.isPrimitive()) {
            type = TypeCaster.primitiveWrapper(type);
        }
        // if same data type no casting needed.
        if (object.getClass().equals(type)) {
            return object;
        }
        //----------------------------------------------------------------------
        // Casting Operation.
        //----------------------------------------------------------------------
        // Convert value to string.
        String objectValue = String.valueOf(object);

        if (type.equals(Boolean.class)) {
            object = Boolean.valueOf(objectValue);
        } else if (type.equals(Byte.class)) {
            object = Byte.valueOf(objectValue);
        } else if (type.equals(Character.class)) {
            if (objectValue.length() > 1) {
                throw new ClassCastException("Value [" + objectValue + "] cannot be casted to [char] length > 1");
            } else if (objectValue.isEmpty()) {
                object = Character.MIN_VALUE;
            } else if (objectValue.length() == 1) {
                object = objectValue.charAt(0);
            } else {
                throw new UnsupportedOperationException("Unexpected else-if block executed");
            }
            /**
             * Number Class.
             */
        } else if (type.equals(Double.class)) {
            object = Double.valueOf(objectValue);
        } else if (type.equals(Float.class)) {
            object = Float.valueOf(objectValue);
        } else if (type.equals(Integer.class)) {
            object = Integer.valueOf(objectValue);
        } else if (type.equals(Long.class)) {
            object = Long.valueOf(objectValue);
        } else if (type.equals(Short.class)) {
            object = Short.valueOf(objectValue);
        } else {
            throw new UnsupportedOperationException("Data type -> [ " + type.getName() + " ] is not supported for data casting.");
        }
        return object;
    }

    /**
     * Wraps a primitive data type into its Wrapper Class.
     *
     * @param type Primitive Data.
     * @return Wrapper Class.
     */
    public final static Class primitiveWrapper(Class type) {
        if (!type.isPrimitive()) {
            return type;
        }

        if (type.equals(boolean.class)) {
            return Boolean.class;
        } else if (type.equals(byte.class)) {
            return Byte.class;
        } else if (type.equals(char.class)) {
            return Character.class;
        } else if (type.equals(double.class)) {
            return Double.class;
        } else if (type.equals(float.class)) {
            return Float.class;
        } else if (type.equals(int.class)) {
            return Integer.class;
        } else if (type.equals(long.class)) {
            return Long.class;
        } else if (type.equals(short.class)) {
            return Short.class;
            /**
             * void is not a data type.
             */
        } /*else if (type.equals(void.class)) {
            return Void.class;
        } */ else {
            throw new UnsupportedOperationException("Unknown Primitive Type !");
        }
    }

}
