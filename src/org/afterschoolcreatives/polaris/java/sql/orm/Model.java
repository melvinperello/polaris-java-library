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
package org.afterschoolcreatives.polaris.java.sql.orm;

import java.sql.SQLException;
import java.util.List;
import org.afterschoolcreatives.polaris.java.sql.ConnectionManager;
import org.afterschoolcreatives.polaris.java.sql.builder.QueryBuilder;

/**
 * Specification for model sub classes.
 *
 * @author Jhon Melvin
 */
public interface Model {

    /**
     * Inserts a record.
     *
     * @param con
     * @return
     * @throws java.sql.SQLException
     */
    boolean insert(ConnectionManager con) throws SQLException;

    /**
     * updates a record.
     *
     * @param con
     * @return
     * @throws java.sql.SQLException
     */
    boolean update(ConnectionManager con) throws SQLException;

    /**
     * updates a record.
     *
     * @param con
     * @return
     * @throws java.sql.SQLException
     */
    boolean updateFull(ConnectionManager con) throws SQLException;

    /**
     * Delete record.
     *
     * @param con
     * @return
     * @throws java.sql.SQLException
     */
    boolean delete(ConnectionManager con) throws SQLException;

    /**
     * Find an instance using the primary key.
     *
     * @param con
     * @param id
     * @return
     * @throws java.sql.SQLException
     */
    boolean find(ConnectionManager con, Object id) throws SQLException;

    /**
     * Find an instance using an SQL statement. the result will be truncated
     * only to the first result. A LIMIT 1 or similar method should be added. to
     * optimize the query. even without the LIMIT constraint only the first
     * result will be mapped.
     *
     * @param con
     * @param builder
     * @return
     * @throws java.sql.SQLException
     */
    boolean findQuery(ConnectionManager con, QueryBuilder builder) throws SQLException;

    /**
     * returns a list of instance that matches the query. this will not write
     * anything on the caller instance.
     *
     * @param <T>
     * @param con
     * @param builder
     * @return
     * @throws java.sql.SQLException
     */
    <T> List<T> findMany(ConnectionManager con, QueryBuilder builder) throws SQLException;

}
