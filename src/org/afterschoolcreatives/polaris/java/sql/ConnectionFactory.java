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

import org.afterschoolcreatives.polaris.java.exceptions.PolarisRuntimeException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Class that holds important information to create a connection to the database
 * server. the class also has the ability to create a connection based on the
 * given information.
 *
 * @author Jhon Melvin
 */
public class ConnectionFactory {

    /**
     * Driver Enumeration.
     */
    public enum Driver {
        MariaDB,
        MySQL,
        PostgreSQL,
        /**
         * SQLITE JDBC Driver: https://bitbucket.org/xerial/sqlite-jdbc
         */
        SQLite
    }

    /**
     * Connection Driver.
     */
    private ConnectionFactory.Driver connectionDriver;

    /**
     * Applies the appropriated driver to use the proper JDBC URL.
     *
     * @param connectionDriver
     */
    public void setConnectionDriver(Driver connectionDriver) {
        this.connectionDriver = connectionDriver;
    }

    /**
     * The Host Address or the IP Address of the database server.
     */
    private String host;
    /**
     * The Port in which the server will run.
     */
    private String port;
    /**
     * The name of the database to username.
     */
    private String databaseName;
    /**
     * Username for the database.
     */
    private String username;
    /**
     * Password for the database. since string can be read directly to memory
     */
    private char[] password;

    /**
     * Location of the SQLITE Database.
     */
    private String SQLiteURL;

    /**
     * Default Constructor.
     */
    public ConnectionFactory() {
        this.password = null;
        this.connectionDriver = null;
    }

    /**
     * Set the host.
     *
     * @param host host address
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Register the Port to use.
     *
     * @param port
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * Register the database name to use.
     *
     * @param databaseName
     */
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    /**
     * Set the username for the database.
     *
     * @param user
     */
    public void setUsername(String user) {
        this.username = user;
    }

    /**
     * Strings are immutable in Java and therefore if a password is stored as
     * plain text it will be available in memory until Garbage collector clears
     * it and as Strings are used in String pool for re-usability there are high
     * chances that it will remain in memory for long duration, which is a
     * security threat. Strings are immutable and there is no way that the
     * content of Strings can be changed because any change will produce new
     * String. With an array, the data can be wiped explicitly data after its
     * work is complete. The array can be overwritten and and the password won’t
     * be present anywhere in the system, even before garbage collection.
     *
     * @param password
     */
    public void setPassword(char[] password) {
        this.password = Arrays.copyOf(password, password.length);
    }

    /**
     * Sets a password from a string.
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password.toCharArray();
    }

    /**
     * When using SQLITE set the URL in which the file will be located.
     *
     * @param sqliteUrl
     */
    public void setSQLiteURL(String sqliteUrl) {
        this.SQLiteURL = sqliteUrl;
    }

    //--------------------------------------------------------------------------
    // Class Getters
    //--------------------------------------------------------------------------
    protected String getHost() {
        return host;
    }

    protected String getPort() {
        return port;
    }

    protected String getDatabaseName() {
        return databaseName;
    }

    protected String getUsername() {
        return username;
    }

    protected char[] getPassword() {
        return Arrays.copyOf(this.password, this.password.length);
    }

    protected Driver getConnectionDriver() {
        return connectionDriver;
    }

    protected String getSQLiteURL() {
        return SQLiteURL;
    }

    //--------------------------------------------------------------------------
    // Class Methods
    //--------------------------------------------------------------------------
    /**
     * Create a URL string for a specific SQL Database driver.
     *
     * @return the URL String.
     */
    protected String createUrl() {
        if (this.connectionDriver == null) {
            throw new PolarisRuntimeException("The Connection Driver was not assigned.");
        }
        switch (this.connectionDriver) {
            case MariaDB:
                return "jdbc:mariadb://" + this.host + ":" + this.port + "/" + this.databaseName;
            case MySQL:
                return "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.databaseName;
            case PostgreSQL:
                return "jdbc:postgresql://" + this.host + ":" + this.port + "/" + this.databaseName;
            case SQLite:
                return "jdbc:sqlite:" + this.SQLiteURL;
            default:
                throw new PolarisRuntimeException("No Default JDBC URL. Please Assign a Driver");
        }
    }

    /**
     * Create A Connection from the given information. this method can be
     * override for other sources of connection other than the driver manager.
     *
     * Always set the newly created connection with auto commit on. this way it
     * will reduce locks in the database.
     *
     * @return SQL Connection.
     * @throws SQLException
     */
    public Connection createConnection() throws SQLException {
        //----------------------------------------------------------------------
        // SQLITE Connection Creation.
        //----------------------------------------------------------------------
        if (connectionDriver.equals(ConnectionFactory.Driver.SQLite)) {
            Connection sqliteConnection = DriverManager
                    .getConnection(this.createUrl());
            sqliteConnection.setAutoCommit(true);
            return sqliteConnection;
        }
        //----------------------------------------------------------------------
        // Normal Connection
        //----------------------------------------------------------------------
        Connection newConnection = DriverManager
                .getConnection(this.createUrl(),
                        this.getUsername(),
                        new String(this.getPassword()));
        //----------------------------------------------------------------------
        // All connections must be set to auto commit upon creation.
        newConnection.setAutoCommit(true);
        //----------------------------------------------------------------------
        return newConnection;
    }

    /**
     * Create a connection manager instance.
     *
     * @return
     * @throws SQLException
     */
    public ConnectionManager createConnectionManager() throws SQLException {
        Connection connection = this.createConnection();
        ConnectionManager connectionManager = new ConnectionManager(this.connectionDriver, connection);
        return connectionManager;
    }

}
