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
