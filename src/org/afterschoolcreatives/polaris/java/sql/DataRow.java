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
package org.afterschoolcreatives.polaris.java.sql;

import java.util.HashMap;

/**
 *
 * @author Jhon Melvin
 */
public class DataRow extends HashMap<String, Object> {

    /**
     * Serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Disallow usage for this constructor.
     */
    private DataRow() {
        // no - op
    }

    /**
     * Default constructor with initial capacity.
     *
     * @param initialCapacity
     */
    public DataRow(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Gets the value and cast it to the required data type.
     *
     * @param <T> data type.
     * @param key key
     * @return value
     */
    public <T> T getValue(String key) {
        return (T) super.get(key);
    }

    /**
     *
     * @param key
     * @return
     */
    @Override
    public Object get(Object key) {
        return super.get(key); //To change body of generated methods, choose Tools | Templates.
    }

}
