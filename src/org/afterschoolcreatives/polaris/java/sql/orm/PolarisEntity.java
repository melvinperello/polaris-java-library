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
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;
import org.afterschoolcreatives.polaris.java.exceptions.PolarisReflectionException;
import org.afterschoolcreatives.polaris.java.reflection.PolarisAnnotatedClass;
import org.afterschoolcreatives.polaris.java.sql.ConnectionManager;
import org.afterschoolcreatives.polaris.java.sql.builder.QueryBuilder;
import org.afterschoolcreatives.polaris.java.sql.orm.annotations.Column;
import org.afterschoolcreatives.polaris.java.sql.orm.annotations.FetchOnly;
import org.afterschoolcreatives.polaris.java.sql.orm.annotations.Limit;
import org.afterschoolcreatives.polaris.java.sql.orm.annotations.Nullable;
import org.afterschoolcreatives.polaris.java.sql.orm.annotations.PrimaryKey;
import org.afterschoolcreatives.polaris.java.sql.orm.annotations.Table;
import org.afterschoolcreatives.polaris.java.sql.orm.annotations.Unsigned;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jhon Melvin
 */
public class PolarisEntity implements PolarisEntityStructure {

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

    private PolarisEntityInformation entityInformation;
    private String lastQuery;
    private List resultSet;

    //--------------------------------------------------------------------------
    /**
     * PUBLIC CONSTRUCTOR.
     */
    public PolarisEntity() {
        // Create Entity Information Holder.
        this.entityInformation = null;
        this.lastQuery = "";
        this.resultSet = null;
    }

    private void polarisEntityMapping() {
        LOG.debug("");
        LOG.debug("");
        LOG.debug("[ RUN ] Polaris Entity Mapping Process");
        this.reflectionProcess();
        this.polarizationProcess();
    }

    /**
     * [01] REFLECTION PROCESS.
     *
     *
     * Gathering of Information about the class.
     */
    private void reflectionProcess() {
        LOG.debug("[ RUN ] 01 - Reflection Process");
        //----------------------------------------------------------------------
        if (this.entityInformation == null) {
            LOG.debug("[ X ] Reflection Process: creating entity information.");
            this.entityInformation = new PolarisEntityInformation();
        } else {
            LOG.debug("[ / ] Reflection Process: entity information already exists.");
        }
        //----------------------------------------------------------------------

        if (this.entityInformation.getAnnotatedStructure() == null) {
            LOG.debug("[ X ] Reflection Process: running annotation reader.");
            this.entityInformation.setAnnotatedStructure(new PolarisAnnotatedClass(this.getClass()));
        } else {
            LOG.debug("[ / ] Reflection Process: annotation structure already identified.");
        }
    }

    /**
     * [02] POLARIZING PROCESS.
     *
     *
     * Converting the class information into a Polaris readable format.
     */
    private void polarizationProcess() {
        LOG.debug("[ RUN ] 02 - Polarization Process");
        //----------------------------------------------------------------------
        // Get Table Name.
        //----------------------------------------------------------------------
        if (this.entityInformation.getEntityName() == null) {
            LOG.debug("[ X ] Polarization Process: finding table name.");
            for (Annotation classAnnotation : this.entityInformation.getAnnotatedStructure().getClassAnnotations()) {
                //--------------------------------------------------------------
                // Search Table Annotation.
                if (classAnnotation instanceof Table) {
                    Table table = (Table) classAnnotation;
                    this.entityInformation.setEntityName(table.value());
                    break;
                }
                //--------------------------------------------------------------
            }
            if (this.entityInformation.getEntityName() == null) {
                throw new PolarisReflectionException.MissingAnnotationException(Table.class.getName() + " was not found");
            }
        } else {
            LOG.debug("[ / ] Polarization Process: table name already stored.");
        }
        //----------------------------------------------------------------------
        // Get Table Data.
        //----------------------------------------------------------------------
        LOG.debug("[ X ] Polarization Process: checking table fields");
        for (Field annotatedField : this.entityInformation.getAnnotatedStructure().getAnnotatedFields()) {
            PolarisEntityInformation.EntityField entityField = new PolarisEntityInformation.EntityField();
            entityField.setField(annotatedField);
            LOG.debug("Field found: {}", annotatedField.getName());
            LOG.debug("\tType: {}", annotatedField.getType());
            for (Annotation annotation : annotatedField.getAnnotations()) {
                LOG.debug("\t\tAnnotation found: {}", annotation.annotationType().getName());
                if (annotation instanceof Column) {
                    Column column = (Column) annotation;
                    entityField.setColumnName(column.value());
                } else if (annotation instanceof PrimaryKey) {
                    entityField.setPrimaryKey(true);
                } else if (annotation instanceof FetchOnly) {
                    entityField.setFetchOnly(true);
                } else if (annotation instanceof Unsigned) {
                    entityField.setUnsigned(true);
                } else if (annotation instanceof Nullable) {
                    Nullable nullable = (Nullable) annotation;
                    entityField.setNullMode(nullable.value());
                } else if (annotation instanceof Limit) {
                    Limit limit = (Limit) annotation;
                    entityField.setLength(limit.length());
                    entityField.setLimitApprehensionMode(limit.apprehension());
                } else {
                    // IGNORE ANNOTATION
                }
            }
        }

    }

    @Override
    public boolean insert(ConnectionManager con) throws SQLException {
        this.polarisEntityMapping();

        return true;
    }

    @Override
    public boolean update(ConnectionManager con) throws SQLException {
        this.polarisEntityMapping();

        return true;
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
    public <T> T createCopy() {
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
