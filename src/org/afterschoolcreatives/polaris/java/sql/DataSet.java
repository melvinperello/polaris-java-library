package org.afterschoolcreatives.polaris.java.sql;

import java.util.ArrayList;

/**
 *
 * @author Jhon Melvin
 */
public class DataSet extends ArrayList<DataRow> {

    /**
     * Initial Capacity.
     */
    private final static int INITIAL_CAPACITY = 50;

    /**
     * Serialization.
     */
    private static final long serialVersionUID = 1L;

    public DataSet() {
        super(DataSet.INITIAL_CAPACITY); // initial capacity.
    }

}
