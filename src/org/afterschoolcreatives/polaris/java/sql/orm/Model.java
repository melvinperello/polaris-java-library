package org.afterschoolcreatives.polaris.java.sql.orm;

import java.util.List;

/**
 *
 * @author Jhon Melvin
 */
public interface Model {

    boolean insert();

    boolean update();

    /**
     * True if updated. False if inserted. NULL for failed.
     *
     * @return
     */
    Boolean upsert();

    boolean delete();

    boolean find(Object id);

    boolean findQuery(String sql);

    List findMany(String sql);

}
