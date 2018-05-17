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

import java.lang.annotation.Annotation;
import java.sql.SQLException;
import java.util.List;
import org.afterschoolcreatives.polaris.java.exceptions.PolarisReflectionException;
import org.afterschoolcreatives.polaris.java.reflection.PolarisAnnotatedClass;
import org.afterschoolcreatives.polaris.java.sql.ConnectionManager;
import org.afterschoolcreatives.polaris.java.sql.builder.QueryBuilder;
import org.afterschoolcreatives.polaris.java.sql.orm.annotations.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jhon Melvin
 */
public class PolarisEntity implements PolarisEntityInterface {

    //--------------------------------------------------------------------------
    // LOG INSTANCE.
    private static final Logger LOG = LoggerFactory.getLogger(PolarisEntity.class);
    //--------------------------------------------------------------------------
    // SQL KEY WORDS.
    private final static String SQL_ESCAPE = "`"; // used when using reserved words
    private final static String SQL_INSERT = "INSERT"; // insert keyword
    private final static String SQL_INTO = "INTO";
    private final static String SQL_VALUES = "VALUES";
    private final static String SQL_UPDATE = "UPDATE";
    private final static String SQL_SET = "SET";
    private final static String SQL_WHERE = "WHERE";
    private final static String SQL_DELETE = "DELETE";
    private final static String SQL_FROM = "FROM";
    private final static String SQL_SELECT = "SELECT";
    private final static String SQL_LIMIT = "LIMIT";
    //--------------------------------------------------------------------------
    // CORE DATA.
    private PolarisAnnotatedClass annotatedStructure;
    private String databaseTableName;
    private String lastQuery;
    private List resultSet;

    //--------------------------------------------------------------------------
    /**
     * PUBLIC CONSTRUCTOR.
     */
    public PolarisEntity() {
        this.annotatedStructure = null;
        this.lastQuery = "";
        this.resultSet = null;

    }

    private void polarisRecordingProcess() {
        LOG.debug("INVOKED: Polaris Recording Process");
        this.reflectionProcess();
        this.polarizingProcess();
    }

    /**
     * [01] REFLECTION PROCESS.
     *
     *
     * Gathering of Information about the class.
     */
    private void reflectionProcess() {
        LOG.debug("INVOKED: Reflection Process");
        if (this.annotatedStructure == null) {
            LOG.debug("[ WORK ] Reflection Process");
            this.annotatedStructure = new PolarisAnnotatedClass(this.getClass());
        } else {
            LOG.debug("[ DONE ] Reflection Process");
        }
    }

    /**
     * [02] POLARIZING PROCESS.
     *
     *
     * Converting the class information into a Polaris readable format.
     */
    private void polarizingProcess() {
        LOG.debug("INVOKED: Polarizing Process");
        // Get Table Name
        if (this.databaseTableName == null) {
            LOG.debug("[ WORK ] Polarizing Process");
            for (Annotation classAnnotation : this.annotatedStructure.getClassAnnotations()) {
                //--------------------------------------------------------------
                // Search Table Annotation.
                if (classAnnotation instanceof Table) {
                    Table table = (Table) classAnnotation;
                    this.databaseTableName = table.value();
                    break;
                }
                //--------------------------------------------------------------
            }
            if (this.databaseTableName == null) {
                throw new PolarisReflectionException.MissingAnnotationException(Table.class.getName() + " was not found");
            }
        } else {
            LOG.debug("[ DONE ] Polarizing Process");
        }
    }

    @Override
    public boolean insert(ConnectionManager con) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean update(ConnectionManager con) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean updateFull(ConnectionManager con) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean delete(ConnectionManager con) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean find(ConnectionManager con, Object id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean findQuery(ConnectionManager con, QueryBuilder builder) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @param <T>
     * @param con
     * @param builder
     * @return
     * @throws SQLException
     */
    @Override
    public <T> List<T> findMany(ConnectionManager con, QueryBuilder builder) throws SQLException {
        this.findSet(con, builder);
        return this.getResultSet();
    }

    @Override
    public boolean findSet(ConnectionManager con, QueryBuilder builder) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Get the last executed query by this entity instance.
     *
     * @return A String SQL Statement.
     */
    @Override
    public String getLastQuery() {
        return this.lastQuery;
    }

    @Override
    public <T> T mirror() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Gets the list of the latest result set stored from invoking findSet
     * method.
     *
     * @param <T>
     * @return
     */
    @Override
    public <T> List<T> getResultSet() {
        return this.resultSet;
    }

}
