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
package org.afterschoolcreatives.polaris.java.sql.osql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jhon Melvin
 */
public class ConnectionManager implements AutoCloseable {

    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(ConnectionManager.class);

    /**
     * package Protected string formatter.
     *
     * @param string
     * @return
     */
    private static String removeDuplicateSpaces(String string) {
        return string.replaceAll("\\s+", " ").trim();
    }

    /**
     * Connection instance to manage.
     */
    private final Connection connection;

    // @deprecated the whole connection factory object will be passed instead.
    // private final ConnectionFactory.Driver connectionDriver;
    // @deprecated Please manually add the escape character in the column.
    // private final String sqlReserveEscape;
    private final ConnectionFactory connectionFactory;

    /**
     * Constructor with passed connection instance.
     *
     * @param connectionDriver
     * @param connection
     */
    ConnectionManager(ConnectionFactory connectionFactory) throws SQLException {
//        this.connectionDriver = connectionFactory.getConnectionDriver();
        this.connection = connectionFactory.createConnection();
//        this.connectionDriver = connectionFactory.getConnectionDriver();
        /**
         * Identify proper escape character for SQL.
         */
//        this.sqlReserveEscape = 
        this.connectionFactory = connectionFactory;
    }

//    /**
//     * Must be run on the constructor.
//     *
//     * @param connectionDriver
//     * @return
//     * @deprecated Please manually add the escape character in the column.
//     */
//    private String checkSqlReserveEscape(ConnectionFactory.Driver connectionDriver) {
//        switch (connectionDriver) {
//            case SQLite:
//            case MariaDB:
//            case MySQL:
//                return "`";
//            case PostgreSQL:
//                return "\"";
//            default:
//                return "";
//        }
//    }
//    public String getSqlReserveEscape() {
//        return this.sqlReserveEscape;
//    }
//    public ConnectionFactory.Driver getConnectionDriver() {
//        return this.connectionDriver;
//    }
    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    //--------------------------------------------------------------------------
    // Implemented Methods.
    //--------------------------------------------------------------------------
    @Override
    public void close() throws SQLException {
        // if not auto commit
        // rollback before closing
        if (!this.connection.getAutoCommit()) {
            this.connection.rollback();
        }
        this.connection.close();
    }

    //--------------------------------------------------------------------------
    // Transaction Methods.
    //--------------------------------------------------------------------------
    /**
     * rollback any changes then switch to manual commit mode.
     *
     * @throws java.sql.SQLException
     */
    public void transactionStart() throws SQLException {
        this.connection.rollback();
        this.connection.setAutoCommit(false);
    }

    /**
     * manually rollback changes.
     *
     * @throws java.sql.SQLException
     */
    public void transactionRollBack() throws SQLException {
        this.connection.rollback();
        this.connection.setAutoCommit(true);
    }

    /**
     * manually commit changes.
     *
     * @throws java.sql.SQLException
     */
    public void transactionCommit() throws SQLException {
        this.connection.commit();
        this.connection.setAutoCommit(true);
    }

    //--------------------------------------------------------------------------
    // State Check Methods.
    //--------------------------------------------------------------------------
    /**
     * Checks whether this connection manager's connection is open.
     *
     * @return
     * @throws java.sql.SQLException
     */
    public boolean isOpen() throws SQLException {
        return !this.connection.isClosed();
    }

    //--------------------------------------------------------------------------
    // Class Methods.
    //--------------------------------------------------------------------------
    /**
     * PRIVATE STATIC METHOD.
     *
     * inserts parameters to a prepared statement.
     *
     * @param preparedStatement
     * @param parameters
     * @throws SQLException
     */
    private static void insertPreparedParameters(PreparedStatement preparedStatement, Object[] parameters) throws SQLException {
        if (parameters != null) {
            for (int index = 1; index <= parameters.length; index++) {
                preparedStatement.setObject(index, parameters[index - 1]);
            }
        }
    }

    /**
     * PRIVATE STATIC METHOD.
     *
     * Executes the prepared statement then Converts the Result Set into a
     * Result List object so that the Result Set Can be closed immediately.
     *
     * Used this in your SELECT queries.
     *
     * @param preparedStatement
     * @return
     * @throws SQLException
     */
    private static DataSet formatResultSet(ResultSet resultSet) throws SQLException {
        try {
            /**
             * The difference of their performance is obvious. LinkedList is
             * faster in add and remove, but slower in get. Based on the
             * complexity table and testing results, we can figure out when to
             * use ArrayList or LinkedList. In brief, LinkedList should be
             * preferred if: there are no large number of random access of
             * element there are a large number of add/remove operations
             *
             * source: https://dzone.com/articles/arraylist-vs-linkedlist-vs
             */
            List<DataRow> dataSet = new LinkedList<>();
            // iterate over the result set
            while (resultSet.next()) {
                //--------------------------------------------------------------
                // Get Column Count
                int rowSize = resultSet.getMetaData().getColumnCount();
                //--------------------------------------------------------------
                /**
                 * Data Row with HashMap Implementation.
                 */
                Map<String, Object> rowData = new HashMap<>(rowSize);
                // fill up the row with the data.
                for (int index = 1; index <= rowSize; index++) {
                    // iterate over this row data
                    //----------------------------------------------------------
                    // Get Column Name
                    String columnName = resultSet.getMetaData().getColumnLabel(index);
                    //--------------------------------------------------------------
                    // get column value
                    Object columnValue = resultSet.getObject(index);
                    // add to row.
                    rowData.put(columnName, columnValue);
                }
                //--------------------------------------------------------------
                // add to dataSet.
                dataSet.add(new DataRow(rowData));
                //--------------------------------------------------------------
            }
            //------------------------------------------------------------------
            return new DataSet(dataSet); // return the data set.
        } finally {
            //------------------------------------------------------------------
            // Close ResultSet
            if (resultSet != null) {
                resultSet.close(); // always close the result set after reading 
            }
            //------------------------------------------------------------------
        }

    }

    /**
     * Inserts a new Record to the database.
     *
     * @param <T>
     * @param query SQL Statement.
     * @param parameters Parameters.
     * @return The first insert Auto Generated keys if there is any. NONE if
     * there is no readable generated keys.
     * @throws SQLException Failure to insert.
     */
    @SuppressWarnings(value = "SQL_PREPARED_STATEMENT_GENERATED_FROM_NONCONSTANT_STRING")
    public <T> T insert(String query, Object... parameters) throws SQLException {
        //----------------------------------------------------------------------
        // Sanitize String
        query = removeDuplicateSpaces(query);
        //----------------------------------------------------------------------
        LOG.info("[{}] -> {}", this.getClass().getSimpleName(), query);
        //----------------------------------------------------------------------
        PreparedStatement preparedStatement = null;
        try {
            // added return generated key constant
            preparedStatement = this.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ConnectionManager.insertPreparedParameters(preparedStatement, parameters);
            //------------------------------------------------------------------
            preparedStatement.executeUpdate(); // ignore results
            //------------------------------------------------------------------
            // Get Generated Keys.
            //------------------------------------------------------------------
            ResultSet gkSet = null; // create a holder for the generated keys.
            try {
                gkSet = preparedStatement.getGeneratedKeys(); // get generated keys
                if (gkSet.next()) {
                    // check if there is a key
                    Object generatedKey = gkSet.getObject(1); // get the first index
                    return (T) generatedKey; // return the generated key
                } else {
                    return null; // if no generated keys return null
                }
            } catch (SQLException gkEx) {
                LOG.error("Failed to fetch generated keys", gkEx);
                // return null for exception in fetching keys this was caught and will not affect the insert method.
                // this means that even the generated keys was not fetch the insert result will not be affected
                return null;
            } finally {
                // close the result set if necessary.
                if (gkSet != null) {
                    gkSet.close();
                }
            }
            //------------------------------------------------------------------
            // END Get Generated Keys.
            //------------------------------------------------------------------
        } finally {
            //--------------------------------------------------------------
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            //--------------------------------------------------------------
        }
    }

    /**
     * Execute Data Manipulation Language (DML) and Data Definition Language
     * (DDL) using this method.
     *
     * <strong> Operations </strong>
     * <ul>
     * <li>INSERT – Inserts data into a table</li>
     * <li>UPDATE – Updates existing data into a table </li>
     * <li>DELETE – Deletes all records from a table</li>
     * <li>CREATE – Creates objects in the database</li>
     * <li>ALTER – Alters objects of the database</li>
     * <li>DROP – Deletes objects of the database</li>
     * <li>TRUNCATE – Deletes all records from a table and resets table identity
     * to initial value.</li>
     * </ul>
     *
     * Note: SELECT is also supported but use fetch method instead to get the
     * result set object. this method only returns the number of affected rows
     * by the operation.
     *
     * @param query SQL Statement
     * @param parameters parameters
     * @return an integer representing the number of affected objects.
     * @throws SQLException if the operation has failed.
     */
    @SuppressWarnings(value = "SQL_PREPARED_STATEMENT_GENERATED_FROM_NONCONSTANT_STRING")
    public int update(String query, Object... parameters) throws SQLException {
        //----------------------------------------------------------------------
        // Sanitize String
        query = removeDuplicateSpaces(query);
        //----------------------------------------------------------------------
        LOG.info("[{}] -> {}", this.getClass().getSimpleName(), query);
        //----------------------------------------------------------------------
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = this.connection.prepareStatement(query);
            ConnectionManager.insertPreparedParameters(preparedStatement, parameters);
            //------------------------------------------------------------------
            int result = preparedStatement.executeUpdate();
            //------------------------------------------------------------------
            return result;
        } finally {
            //--------------------------------------------------------------
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            //--------------------------------------------------------------
        }
    }

    /**
     * Execute SELECT operations.
     *
     * @param query SQL Statement.
     * @param parameters parameters
     * @return Data Set Object containing the results.
     * @throws java.sql.SQLException
     */
    @SuppressWarnings(value = "SQL_PREPARED_STATEMENT_GENERATED_FROM_NONCONSTANT_STRING")
    public DataSet fetch(String query, Object... parameters) throws SQLException {
        //----------------------------------------------------------------------
        // Sanitize String
        query = removeDuplicateSpaces(query);
        //----------------------------------------------------------------------
        LOG.info("[{}] -> {}", this.getClass().getSimpleName(), query);
        //----------------------------------------------------------------------
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = this.connection.prepareStatement(query/*,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY*/);
            //----------------------------------------------------------------------
            ConnectionManager.insertPreparedParameters(preparedStatement, parameters);
            //----------------------------------------------------------------------
            resultSet = preparedStatement.executeQuery();
            DataSet resultList = ConnectionManager.formatResultSet(resultSet);
            //----------------------------------------------------------------------
            return resultList;
        } finally {
            //--------------------------------------------------------------
            // close result set
            if (resultSet != null) {
                resultSet.close();
            }
            // close statement.
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            //--------------------------------------------------------------
        }
    }

}
