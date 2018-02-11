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

import org.afterschoolcreatives.polaris.java.sql.builder.QueryBuilder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jhon Melvin
 */
public class ConnectionManager implements AutoCloseable {

    private static final Logger LOGGER = Logger.getLogger(ConnectionManager.class.getName());

    /**
     * Connection instance to manage.
     */
    private final Connection connection;

    /**
     * Constructor with passed connection instance.
     *
     * @param connection
     */
    public ConnectionManager(Connection connection) {
        this.connection = connection;
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

    public void closeQuietly() {
        try {
            this.close();
        } catch (SQLException e) {
            // ignore error.
            LOGGER.log(Level.WARNING, "Cannot close quietly -> {0}", e.toString());
        }
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

    /**
     * Starts a transaction ignoring SQL Exception.
     *
     * @return true if no exception and false if there is one.
     */
    public boolean transactionStartQuietly() {
        try {
            this.transactionStart();
        } catch (SQLException ex) {
            // ignore exception.
            LOGGER.log(Level.WARNING, "Cannot start transaction quietly -> {0}", ex.toString());
            return false;
        }
        return true;
    }

    /**
     * Roll backs a transaction ignoring SQL Exception
     *
     * @return true if no exception and false if there is one.
     */
    public boolean transactionRollBackQuietly() {
        try {
            this.transactionRollBack();
        } catch (SQLException ex) {
            // ignore exception.
            LOGGER.log(Level.WARNING, "Cannot rollback quietly -> {0}", ex.toString());
            return false;
        }
        return true;
    }

    /**
     * Commits a transaction ignoring SQL Exception
     *
     * @return true if no exception and false if there is one.
     */
    public boolean transactionCommitQuietly() {
        try {
            this.transactionCommit();
        } catch (SQLException ex) {
            // ignore exception.
            LOGGER.log(Level.WARNING, "Cannot commit quietly -> {0}", ex.toString());
            return false;
        }
        return true;

    }

    //--------------------------------------------------------------------------
    // State Check Methods.
    //--------------------------------------------------------------------------
    /**
     * Checks whether this connection manager's connection is open.
     *
     * @return
     */
    public boolean isOpen() {
        try {
            return !this.connection.isClosed();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Cannot determine connection status -> {0}", e.toString());
            return false;
        }
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
            // create blank data set.
            DataSet dataSet = new DataSet();
            // iterate over the result set
            while (resultSet.next()) {
                //--------------------------------------------------------------
                // Get Column Count
                int rowSize = resultSet.getMetaData().getColumnCount();
                //--------------------------------------------------------------
                // create data row with initial capacity.
                DataRow row = new DataRow(rowSize);
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
                    row.put(columnName, columnValue);
                }
                //--------------------------------------------------------------
                // add to dataSet.
                dataSet.add(row);
                //--------------------------------------------------------------
            }
            //------------------------------------------------------------------
            return dataSet; // return the data set.
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
     * @param query SQL Statement.
     * @param parameters Parameters.
     * @return The first insert Auto Generated keys if there is any. NONE if
     * there is no readable generated keys.
     * @throws SQLException Failure to insert.
     */
    @SuppressWarnings(value = "SQL_PREPARED_STATEMENT_GENERATED_FROM_NONCONSTANT_STRING")
    public <T> T insert(String query, Object... parameters) throws SQLException {
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
                LOGGER.log(Level.SEVERE, "Failed to Insert", gkEx);
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
     * Execute insert using a query builder.
     *
     * @see ConnectionManager#insert(java.lang.String, java.lang.Object...)
     * @param builder
     * @return
     * @throws SQLException
     */
    public <T> T insert(QueryBuilder builder) throws SQLException {
        return this.insert(builder.getQueryString(), builder.getParameters());
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
     * Execute update using a query builder.
     *
     * @see ConnectionManager#update(java.lang.String, java.lang.Object...)
     * @param builder
     * @return
     * @throws SQLException
     */
    public int update(QueryBuilder builder) throws SQLException {
        return this.update(builder.getQueryString(), builder.getParameters());
    }

    /**
     * Execute SELECT operations.
     *
     * @param query SQL Statement.
     * @param parameters parameters
     * @return Data Set Object containing the results.
     */
    @SuppressWarnings(value = "SQL_PREPARED_STATEMENT_GENERATED_FROM_NONCONSTANT_STRING")
    public DataSet fetch(String query, Object... parameters) throws SQLException {
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

    /**
     * Execute Fetch Operations.
     *
     * @see ConnectionManager#fetch(java.lang.String, java.lang.Object...)
     *
     * @param builder
     * @return
     * @throws SQLException
     */
    public DataSet fetch(QueryBuilder builder) throws SQLException {
        return this.fetch(builder.getQueryString(), builder.getParameters());
    }

    /**
     * Execute a fetch for the first result only
     *
     * @param query
     * @param parameters
     * @return
     * @throws SQLException
     */
    public DataRow fetchFirst(String query, Object... parameters) throws SQLException {
        DataSet ds = this.fetch(query, parameters);
        if (ds.isEmpty()) {
            return new DataRow(0); // return a blank Data Row
        } else {
            return ds.get(0); // return the first result.
        }
    }

    /**
     * Execute a fetch for the first result only
     *
     * @param builder
     * @return
     * @throws SQLException
     */
    public DataRow fetchFirst(QueryBuilder builder) throws SQLException {
        return this.fetchFirst(builder.getQueryString(), builder.getParameters());
    }

}
