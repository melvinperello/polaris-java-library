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
package org.afterschoolcreatives.polaris.java.util;

import org.afterschoolcreatives.polaris.java.exceptions.PolarisRuntimeException;

/**
 * For Reflective Operations.
 *
 * @author Jhon Melvin
 */
public class PolarisWrapper {

    /**
     * Automatically Wraps a primitive data type to its specific wrapper class.
     *
     * @param type
     * @return
     */
    public final static Class autoBox(Class type) {
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
        } else if (type.equals(void.class)) {
            return Void.class;
        }

        throw new PolarisRuntimeException("Unknown Primitive Type");
    }

}
