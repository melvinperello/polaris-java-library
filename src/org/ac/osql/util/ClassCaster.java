/**
 *
 * Objective SQL Project - Afterschool Creatives "Captivating Creativity"
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
package org.ac.osql.util;

/**
 *
 * @author Jhon Melvin
 */
public class ClassCaster {

    public final static Object dataCast(Object object, Class type)
            throws ClassCastException, NumberFormatException, NullPointerException {

        /**
         * Check if null. return null object does not need casting.
         */
        if (object == null) {
            return null;
        }

        /**
         * check if type is null.
         */
        if (type == null) {
            throw new NullPointerException("Class type cannot be null, please specify a type to run auto cast");
        }

        /**
         * Check if primitive then wrap it to a Wrapper Class counterpart.
         */
        if (type.isPrimitive()) {
            type = ClassCaster.primitiveWrapper(type);
        }

        /**
         * check if same data type.
         */
        if (object.getClass().equals(type)) {
            System.out.println("same type skip auto cast.");
            return object;
        }
        //----------------------------------------------------------------------
        // If not same block.
        //----------------------------------------------------------------------
        /**
         * Convert to data to common data String type.
         */
        String objectValue = String.valueOf(object);

        /**
         * Java Data Types.
         */
        if (type.equals(Boolean.class)) {
            object = Boolean.valueOf(objectValue);
        } else if (type.equals(Byte.class)) {
            object = Byte.valueOf(objectValue);
        } else if (type.equals(Character.class)) {
            /**
             *
             */
            if (objectValue.length() > 1) {
                throw new ClassCastException("Cannot cast to character String has more than 1 character.");
            }
            if (objectValue.isEmpty()) {
                object = Character.MIN_VALUE;
            }
            if (objectValue.length() == 1) {
                object = objectValue.charAt(0);
            }
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
            throw new UnsupportedDataTypeException(type);
        }
        return object;
    }

    public static void main(String[] args) {
        String b = "true";
        Object a = dataCast(b, boolean.class);
        System.out.println(a.getClass());
        System.out.println(a);
    }

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
            throw new UnknownPrimitiveTypeException();
        }
    }

    public static class UnknownPrimitiveTypeException extends RuntimeException {

        public UnknownPrimitiveTypeException() {
            super("Unknown Primitive Type !");
        }

    }

    public static class UnsupportedDataTypeException extends RuntimeException {

        public UnsupportedDataTypeException(Class dataType) {
            super("Data type -> [ " + dataType.getName() + " ] is not supported for data casting.");
        }

    }
}
