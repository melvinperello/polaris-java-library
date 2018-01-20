package org.afterschoolcreatives.polaris.java.sql;

import org.afterschoolcreatives.polaris.java.sql.builder.QueryBuilder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Jhon Melvin
 */
public class ConnectionManager implements AutoCloseable {

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
    }

    /**
     * manually commit changes.
     *
     * @throws java.sql.SQLException
     */
    public void transactionCommit() throws SQLException {
        this.connection.commit();
    }

    /**
     * Roll backs any active transaction then switches to auto commit mode.
     *
     * @throws java.sql.SQLException
     */
    public void transactionEnd() throws SQLException {
        this.connection.rollback();
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
     * inserts parameters to a prepared statement.
     *
     * @param preparedStatement
     * @param parameters
     * @throws SQLException
     */
    private void insertPreparedParameters(PreparedStatement preparedStatement, Object[] parameters) throws SQLException {
        if (parameters != null) {
            for (int index = 1; index <= parameters.length; index++) {
                preparedStatement.setObject(index, parameters[index - 1]);
            }
        }
    }

    /**
     * Executes the prepared statement then Converts the Result Set into a
     * Result List object so that the Result Set Can be closed immediately.
     *
     * Used this in your SELECT queries.
     *
     * @param preparedStatement
     * @return
     * @throws SQLException
     */
    private DataSet formatResultSet(ResultSet resultSet) throws SQLException {
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
     * Prepared statement optimized for DLL.
     *
     * @param query
     * @param parameters
     * @return
     */
    @SuppressWarnings(value = "SQL_PREPARED_STATEMENT_GENERATED_FROM_NONCONSTANT_STRING")
    public int update(String query, Object... parameters) throws SQLException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = this.connection.prepareStatement(query);
            this.insertPreparedParameters(preparedStatement, parameters);
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
     *
     * @param builder
     * @return
     * @throws java.sql.SQLException
     */
    public int update(QueryBuilder builder) throws SQLException {
        return this.update(builder.getQueryString(), builder.getParameters());
    }

    /**
     * Prepared statement for SELECT Operation.
     *
     * @param query
     * @param parameters
     * @return
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
            this.insertPreparedParameters(preparedStatement, parameters);
            //----------------------------------------------------------------------
            resultSet = preparedStatement.executeQuery();
            DataSet resultList = this.formatResultSet(resultSet);
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
     *
     * @param builder
     * @return
     * @throws java.sql.SQLException
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
