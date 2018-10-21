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

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.afterschoolcreatives.polaris.java.sql.osql.util.BeanPropertyAccessor;
import org.afterschoolcreatives.polaris.java.sql.osql.util.TypeCaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jhon Melvin
 */
public abstract class ObjectiveTable {

    /**
     * Class Logger.
     */
    private final static Logger LOG = LoggerFactory.getLogger(ObjectiveTable.class);

    //--------------------------------------------------------------------------
    private final static Map<Class, TableInformation> CACHED_TABLE_INFORMATION = new HashMap<>();

    /**
     * Gets a table from the cache.
     *
     * @param <T>
     * @param tableClass
     * @return
     */
    public static synchronized <T extends ObjectiveTable> T getTable(Class tableClass) {
        if (tableClass == null) {
            throw new NullPointerException("Table Class must not be null");
        }
        TableInformation tableInfo = CACHED_TABLE_INFORMATION.getOrDefault(tableClass, null);
        if (tableInfo == null) {
            tableInfo = new TableInformation(tableClass);
            CACHED_TABLE_INFORMATION.put(tableClass, tableInfo);
        } else {
            LOG.debug(tableInfo.getOwnerClass().getName() + " -> Table Information already cached, pulling information.");
        }

        try {
            ObjectiveTable table = (ObjectiveTable) tableClass.newInstance();
            table.setTableInfo(tableInfo);
            return (T) table;
        } catch (InstantiationException ex) {
            throw new RuntimeException("Failed to create Instance !", ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException("Failed to access Default Constructor", ex);
        }
    }
    //--------------------------------------------------------------------------

    /**
     * When using SQL reserver words, you may want to use the escape character.
     */
//    private String sqlReserveEscape;
    /**
     * Reflected table information.
     */
    private TableInformation tableInfo;

    /**
     * Package protected setter. this will allow Objective Sequel instance to
     * modify the value after creation.
     *
     * @param tableInfo
     */
    private void setTableInfo(TableInformation tableInfo) {
        this.tableInfo = tableInfo;
    }

    /**
     * Default Constructor.
     */
    public ObjectiveTable() {
        //
    }

    /**
     * Throw common exceptions for reflective operations.
     *
     * @param reflectionException
     */
    public void throwCommonExceptions(Exception reflectionException) {
        try {
            throw reflectionException;
        } catch (IntrospectionException ex) {
            /**
             * if an exception occurs during introspection
             */
//                LOG.error("IntrospectionException", ex);
            throw new RuntimeException("An exception has occured while inspecting the field", ex);
        } catch (IllegalAccessException ex) {
            /**
             * if this Method object is enforcing Java language access control
             * and the underlying method is inaccessible.
             */
//                LOG.error("IllegalAccessException", ex);
            throw new RuntimeException("The method is not accessible, make it public maybe ?", ex);
        } catch (IllegalArgumentException ex) {
            /**
             * if the method is an instance method and the specified object
             * argument is not an instance of the class or interface declaring
             * the underlying method (or of a subclass or implementor thereof);
             * if the number of actual and formal parameters differ; if an
             * unwrapping conversion for primitive arguments fails; or if, after
             * possible unwrapping, a parameter value cannot be converted to the
             * corresponding formal parameter type by a method invocation
             * conversion.
             */
//                LOG.error("IllegalArgumentException", ex);
            throw new RuntimeException("Cannot invoke the method with incorrect arguments", ex);
        } catch (InvocationTargetException ex) {
            /**
             * If the method itself throws an exception.
             */
//                LOG.error("InvocationTargetException", ex);
            throw new RuntimeException("The method throws an exception.", ex);
        } catch (Exception e) {
            /**
             * Unknown exception.
             */
            throw new RuntimeException("An [Unknown Exception] was caught in common exception catcher.", e);
        }
    }

    /**
     * Performs an INSERT Query, dynamically inserts data, skips null values in
     * insert.
     *
     * @param con
     * @return true if there is a generated key.
     * @throws SQLException
     */
    public boolean insert(ConnectionManager con) throws SQLException {
        //----------------------------------------------------------------------
        // Dynamic Reserve Word Escape.
//        this.sqlReserveEscape = con.getSqlReserveEscape();
        //----------------------------------------------------------------------
        // get table name.
        final String tableName = this.tableInfo.getTableName();
        //----------------------------------------------------------------------
        // check field values
        final List<String> constructFields = new ArrayList<>();
        final List<Object> insertParameters = new ArrayList<>();
        for (TableInformation.Column entityField : this.tableInfo.getTableColumns()) {
            //------------------------------------------------------------------
            String fieldName = entityField.getFieldName();
            //------------------------------------------------------------------
            //            /**
            //             * If the values is for fetching only skip this.
            //             */
            //            if (entityField.isGeneratedValue()) {
            //                continue;
            //            }
            //------------------------------------------------------------------

            try {
                Object value = BeanPropertyAccessor.readMethod(this, fieldName);
                //--------------------------------------------------------------
                // CONSTRAINTS
                //--------------------------------------------------------------
                if (value == null) {
                    // ALLOW NULL ID IN INSERT UNLESS NULL RESTRICTED
                    //                    if (entityField.isId()) {
                    //                        throw new Id.NullIdException(this.tableInfo.getOwnerClass(), fieldName);
                    //                    }
                    if (entityField.isNullRestricted()) {
                        throw new RuntimeException(this.tableInfo.getOwnerClass() + "->" + fieldName + " IS NULL RESTRICTED");
                    } else {
                        continue;
                    }
                } else {
                    //----------------------------------------------------------
                    // Length Constraint.
                    //----------------------------------------------------------
                    if (value instanceof String) {
                        if (entityField.getLength() != 0) {
                            String str = String.valueOf(value);
                            if (str.length() > entityField.getLength()) {
                                if (entityField.isTruncated()) {
                                    value = str.substring(0, (int) entityField.getLength());
                                } else {
                                    throw new RuntimeException(this.tableInfo.getOwnerClass() + "->" + fieldName + " VIOLATES LENGTH CONSTRAINT");
                                }
                            }
                        }
                    }
                    //----------------------------------------------------------
                }
                //--------------------------------------------------------------
                constructFields.add(entityField.getColumnName());
                insertParameters.add(value);
            } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                this.throwCommonExceptions(e);
            }
        }
        //----------------------------------------------------------------------
        // May 18, 2018
        // Dear Diary,
        // What the FUCK !!!!
        // Insert statements having return values, ok !!! I was amazed.
        //----------------------------------------------------------------------
        String postgresReturn = "";
        if (con.getConnectionFactory().getConnectionDriver()
                .equals(ConnectionFactory.Driver.PostgreSQL)) {
            /**
             * FOR POSTGRES DATABASE TO RETURN GENERATED KEYS.
             */
            postgresReturn = " RETURNING "
                    /* + sqlReserveEscape*/
                    + this.tableInfo.getIdColumn().getColumnName() /* + sqlReserveEscape*/;
        }
        //----------------------------------------------------------------------
        final String generatedQuery = this.constructInsertQuery(tableName, constructFields.toArray(new String[constructFields.size()]))
                + postgresReturn + ";";
        LOG.info("[{}] [Query] -> Constructed . . .", this.getClass().getName());
        /**
         * Execute Query. the generated key will be null if no keys are
         * generated.
         */
        Object generatedKey = con.insert(generatedQuery, insertParameters.toArray());
        //----------------------------------------------------------------------
        if (this.tableInfo.getIdColumn() != null && generatedKey != null) {
            try {
                if (!generatedKey.getClass().equals(this.tableInfo.getIdColumn().getFieldType())) {
                    LOG.warn("[TYPE MISMATCH] Retrieved Value -> {} [{}] does not match {}->{} [{}]", generatedKey, generatedKey.getClass(), this.getClass().getName(), this.tableInfo.getIdColumn().getFieldName(), this.tableInfo.getIdColumn().getFieldType());
                    generatedKey = TypeCaster.autoCast(generatedKey, this.tableInfo.getIdColumn().getFieldType());
                }
                BeanPropertyAccessor.writeMethod(this, this.tableInfo.getIdColumn().getFieldName(), generatedKey);
            } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                this.throwCommonExceptions(ex);
            }
            return true;
        } else {
            return false;
        }
    }

    protected String constructInsertQuery(String tableName, String[] constructorFields) {
        //----------------------------------------------------------------------
        final String insertPreamble = "INSERT INTO"
                + " " + /*sqlReserveEscape +*/ tableName /*+ sqlReserveEscape*/
                + " ";
        //----------------------------------------------------------------------
        final StringBuilder constructorBuilder = new StringBuilder("(");
        final StringBuilder paramBuilder = new StringBuilder("(");
        for (String constructorField : constructorFields) {
            String fieldName = /*sqlReserveEscape +*/ constructorField/* + sqlReserveEscape*/;
            constructorBuilder.append(fieldName);
            constructorBuilder.append(",");
            //
            paramBuilder.append("?");
            paramBuilder.append(",");
        }
        if (constructorBuilder.charAt(constructorBuilder.length() - 1) == ',') {
            constructorBuilder.deleteCharAt(constructorBuilder.length() - 1);
        }
        //
        if (paramBuilder.charAt(paramBuilder.length() - 1) == ',') {
            paramBuilder.deleteCharAt(paramBuilder.length() - 1);
        }
        //
        constructorBuilder.append(")");
        paramBuilder.append(")");
        //
        final String insertConstructor = constructorBuilder.toString();
        //
        final String insertParam = paramBuilder.toString();
        //----------------------------------------------------------------------
        return insertPreamble + insertConstructor + " VALUES " + insertParam;
    }

    public boolean update(ConnectionManager con) throws SQLException {
        //----------------------------------------------------------------------
        // Dynamic Reserve Word Escape.
        //this.sqlReserveEscape = con.getSqlReserveEscape();
        //----------------------------------------------------------------------
        // get table name.
        final String tableName = this.tableInfo.getTableName();
        //----------------------------------------------------------------------
        final List<String> updatedFields = new ArrayList<>();
        final List<Object> updatedParameters = new ArrayList<>();
        Object idValue = null;
        for (TableInformation.Column entityField : this.tableInfo.getTableColumns()) {
            //------------------------------------------------------------------
            //            /**
            //             * Skip Generated Value only if its not an ID.
            //             */
            //            if (entityField.isGeneratedValue() && !entityField.isId()) {
            //                continue;
            //            }
            //------------------------------------------------------------------

            try {
                String fieldName = entityField.getFieldName();
                Object value = BeanPropertyAccessor.readMethod(this, fieldName);

                //--------------------------------------------------------------
                // CONSTRAINTS
                //--------------------------------------------------------------
                if (value == null) {
                    if (entityField.isId()) {
                        throw new RuntimeException(this.tableInfo.getOwnerClass() + "->" + fieldName + " MUST NOT BE NULL FOR UPDATE");
                    } else if (entityField.isNullRestricted()) {
                        throw new RuntimeException(this.tableInfo.getOwnerClass() + "->" + fieldName + " IS NULL RESTRICTED");
                    }
                } else {
                    //----------------------------------------------------------
                    // Length Constraint.
                    //----------------------------------------------------------
                    if (value instanceof String) {
                        if (entityField.getLength() != 0) {
                            String str = String.valueOf(value);
                            if (str.length() > entityField.getLength()) {
                                if (entityField.isTruncated()) {
                                    value = str.substring(0, (int) entityField.getLength());
                                } else {
                                    throw new RuntimeException(this.tableInfo.getOwnerClass() + "->" + fieldName + " VIOLATES LENGTH CONSTRAINT");
                                }
                            }
                        }
                    }
                    //----------------------------------------------------------
                    if (entityField.isId()) {
                        idValue = value;
                    }
                }
                //--------------------------------------------------------------
                updatedFields.add(entityField.getColumnName());
                updatedParameters.add(value);
            } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                this.throwCommonExceptions(e);
            }

        }

        /**
         * Created Where Clause.
         */
        updatedParameters.add(idValue);
        final String generatedQuery = this.constructUpdateQuery(tableName,
                updatedFields.toArray(new String[updatedFields.size()]),
                this.tableInfo.getIdColumn().getColumnName());
        LOG.info("[{}] [Query] -> Constructed . . .", this.getClass().getName());
        int affectedRecords = con.update(generatedQuery, updatedParameters.toArray());
        LOG.debug("{} [Affected Rows] -> {}", this.getClass().getName(), affectedRecords);
        return affectedRecords != 0;
    }

    protected String constructUpdateQuery(String tableName, String[] constructorFields, String idField) {
        String updatePreamble = "UPDATED"
                + " " + /*sqlReserveEscape +*/ tableName /*+ sqlReserveEscape*/
                + " " + "SET" + " ";

        final StringBuilder updateBuilder = new StringBuilder();
        for (String constructorField : constructorFields) {
            String updateField = /*sqlReserveEscape +*/ constructorField /*+ sqlReserveEscape*/ + " = ?";
            updateBuilder.append(updateField);
            updateBuilder.append(",");
        }

        if (updateBuilder.charAt(updateBuilder.length() - 1) == ',') {
            updateBuilder.deleteCharAt(updateBuilder.length() - 1);
        }
        final String updateBody = updateBuilder.toString();

        //
        final String whereClause = " " + "WHERE" + " "
                /* + sqlReserveEscape */ + idField /*+ sqlReserveEscape*/
                + " = ?;";

        return updatePreamble + updateBody + whereClause;
    }

    /**
     * Deletes an entry, requires ROW.ID.
     *
     * @param con
     * @return
     * @throws SQLException
     */
    public boolean delete(ConnectionManager con) throws SQLException {
        //----------------------------------------------------------------------
        // Dynamic Reserve Word Escape.
        // this.sqlReserveEscape = con.getSqlReserveEscape();
        //----------------------------------------------------------------------
        // get table name.
        final String tableName = this.tableInfo.getTableName();
        //----------------------------------------------------------------------
        final String idColumn = this.tableInfo.getIdColumn().getColumnName();
        final String idField = this.tableInfo.getIdColumn().getFieldName();
        Object value = null;
        try {
            value = BeanPropertyAccessor.readMethod(this, idField);
        } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            this.throwCommonExceptions(e);
        }
        if (value == null) {
            throw new RuntimeException(this.tableInfo.getOwnerClass() + "->" + idField + " MUST NOT BE NULL FOR DELETE");
        }

        final String generatedQuery = this.constructDeleteQuery(tableName, idColumn);
        LOG.info("[{}] [Query] -> Constructed . . .", this.getClass().getName());
        int affectedRecords = con.update(generatedQuery, value);
        LOG.info("[{}] [Affected Rows] -> {}", this.getClass().getName(), affectedRecords);
        return affectedRecords != 0;
    }

    protected String constructDeleteQuery(String tableName, String idColumn) {
        final String startQuery = "DELETE"
                + " " + "FROM"
                + " " /*+ sqlReserveEscape */ + tableName /*+ sqlReserveEscape*/;

        final String whereClause = " " + "WHERE" + " "
                /*+ sqlReserveEscape */ + idColumn /*+ sqlReserveEscape*/
                + " = ?;";

        return startQuery + whereClause;
    }

    /**
     * Contains the result set of a fetch query.
     */
    private List<ObjectiveTable> dataSet;

    /**
     * Gets the result set.
     *
     * @return
     */
    public ObjectiveTable[] getDataSet() {
        return dataSet.toArray(new ObjectiveTable[this.dataSet.size()]);
    }

    /**
     * Execute a SELECT ObjectiveQuery.
     *
     * @param con An open Connection Manager.
     * @param query Objective ObjectiveQuery.
     * @param parameters
     * @return this will return true if there is a result.
     * @throws SQLException Database Exception.
     */
    public boolean fetch(ConnectionManager con, String query, Object... parameters) throws SQLException {
        // create list holder
        this.dataSet = new LinkedList<>();
        LOG.info("[{}] [Query] -> Received . . .", this.getClass().getName());
        /**
         * Run the objective query in objective mode. so that if it has escape
         * character it will be replaced.
         */
        DataSet ds = con.fetch(query, parameters);
        // Check if Empty return false
        if (ds.isEmpty()) {
            LOG.info("[{}] [Query] -> Has no results.", this.getClass().getName());
            return false; // return an empty list
        }

        /**
         * Iterate all over the results
         */
        for (DataRow dataRow : ds.read()) {
            // Check if Empty skip this row
            if (dataRow.isEmpty()) {
                continue;
            }
            // create a row holder
            ObjectiveTable row = null;
            try {
                row = (ObjectiveTable) this.getClass().newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                throw new RuntimeException("Cannot Create Model Instance, is there a public and default constructor ?", ex);
            }

            /**
             * Map The Data.
             */
            for (TableInformation.Column field : this.tableInfo.getTableColumns()) {
                String fieldName = field.getFieldName();
                Class fieldType = field.getFieldType();
                Object value = dataRow.get(field.getColumnName());
                try {
                    //----------------------------------------------------------
                    if (value != null) {
                        // If the Class of the value and the field where it will be wrtten
                        // does not match use Type Caster to cast it.
                        if (!value.getClass().equals(fieldType)) {
                            LOG.warn("[TYPE MISMATCH] Retrieved Value -> {} [{}] does not match {}->{} [{}]", value, value.getClass(), this.getClass().getName(), fieldName, fieldType);
                            // if the Class Type is not supported for casting
                            // this will throw an UnsupportedOperationException
                            value = TypeCaster.autoCast(value, fieldType);
                        }
                    }
                    //----------------------------------------------------------
                    // writes the value to the field.
                    BeanPropertyAccessor.writeMethod(row, fieldName, value);
                } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    this.throwCommonExceptions(e);
                }
            }
            this.dataSet.add(row);
        }

        return true;
    }

}
