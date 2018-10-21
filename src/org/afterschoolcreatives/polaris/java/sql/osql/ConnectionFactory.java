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
    protected final ConnectionFactory.Driver connectionDriver;

    /**
     * The Host Address or the IP Address of the database server.
     */
    protected final String host;
    /**
     * The Port in which the server will run.
     */
    protected final String port;
    /**
     * The name of the database to username.
     */
    protected final String database;
    /**
     * Username for the database.
     */
    protected final String username;
    /**
     * Password for the database. since string can be read directly to memory.
     *
     *
     * Strings are immutable in Java and therefore if a password is stored as
     * plain text it will be available in memory until Garbage collector clears
     * it and as Strings are used in String pool for re-usability there are high
     * chances that it will remain in memory for long duration, which is a
     * security threat. Strings are immutable and there is no way that the
     * content of Strings can be changed because any change will produce new
     * String. With an array, the data can be wiped explicitly data after its
     * work is complete. The array can be overwritten and and the password won’t
     * be present anywhere in the system, even before garbage collection.
     */
    protected final char[] password;

    //--------------------------------------------------------------------------
    public static class Builder {

        // Required parameters
        private final Driver driver;
        // Optional parameters - initialized to default values
        private String host = null;
        private String port = null;
        private String username = null;
        private String password = null;
        private String database = null;

        /**
         * Construct.
         *
         * @param driver
         */
        public Builder(Driver driver) {
            this.driver = driver;
        }

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(String port) {
            this.port = port;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder database(String database) {
            this.database = database;
            return this;
        }

        public Builder build() {
            return this;
        }

    }
    //--------------------------------------------------------------------------

    public ConnectionFactory(Builder builder) {
        this.connectionDriver = builder.driver;
        this.host = builder.host;
        this.port = builder.port;
        this.username = builder.username;
        this.password = (builder.password == null) ? null : builder.password.toCharArray();
        this.database = builder.database;
    }

    //--------------------------------------------------------------------------
    // Class Getters
    //--------------------------------------------------------------------------
    public Driver getConnectionDriver() {
        return connectionDriver;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUsername() {
        return username;
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
            throw new NullPointerException("The Connection Driver was not assigned.");
        }
        switch (this.connectionDriver) {
            case MariaDB:
                return "jdbc:mariadb://" + this.host + ":" + this.port + "/" + this.database;
            case MySQL:
                return "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database;
            case PostgreSQL:
                return "jdbc:postgresql://" + this.host + ":" + this.port + "/" + this.database;
            case SQLite:
                return "jdbc:sqlite:" + this.database;
            default:
                throw new UnsupportedOperationException("This connection driver is not supported.");
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
                        this.username,
                        new String(this.password));
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
        ConnectionManager connectionManager = new ConnectionManager(this);
        return connectionManager;
    }

}
