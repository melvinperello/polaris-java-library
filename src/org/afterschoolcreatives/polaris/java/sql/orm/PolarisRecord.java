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

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.afterschoolcreatives.polaris.java.exceptions.PolarisRuntimeException;
import org.afterschoolcreatives.polaris.java.sql.ConnectionManager;
import org.afterschoolcreatives.polaris.java.sql.DataRow;
import org.afterschoolcreatives.polaris.java.sql.DataSet;
import org.afterschoolcreatives.polaris.java.sql.builder.QueryBuilder;
import org.afterschoolcreatives.polaris.java.sql.orm.annotations.Column;
import org.afterschoolcreatives.polaris.java.sql.orm.annotations.FetchOnly;
import org.afterschoolcreatives.polaris.java.sql.orm.annotations.PrimaryKey;
import org.afterschoolcreatives.polaris.java.sql.orm.annotations.Table;
import org.afterschoolcreatives.polaris.java.util.PolarisWrapper;
import org.afterschoolcreatives.polaris.java.util.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * My First Attempt to create a model. this is the basic implementation of the
 * model interface which will be using reflection to perform basic CRUD
 * operations. This model only supports MariaDB, MySQL and SQLite.
 *
 * @author Jhon Melvin
 */
public class PolarisRecord {

    //--------------------------------------------------------------------------
    // logger instance
    private static final Logger logger = LoggerFactory.getLogger(PolarisRecord.class);
    //--------------------------------------------------------------------------
    // SQL KEY WORDS
    private final static String sqlEscapeCharacter = "`"; // used when using reserved words
    private final static String sqlInsert = "INSERT"; // insert keyword
    private final static String sqlInto = "INTO";
    private final static String sqlValues = "VALUES";
    private final static String sqlUpdate = "UPDATE";
    private final static String sqlSet = "SET";
    private final static String sqlWhere = "WHERE";
    private final static String sqlDelete = "DELETE";
    private final static String sqlFrom = "FROM";
    private final static String sqlSelect = "SELECT";
    private final static String sqlLimit = "LIMIT";
    //--------------------------------------------------------------------------
    // CORE FIELDS.
    //--------------------------------------------------------------------------
    private ArrayList<DatabaseFields> classFields;
    private String databaseTableName;

    //--------------------------------------------------------------------------
    /**
     * Default constructor.
     */
    protected PolarisRecord() {
        this.databaseTableName = null;
        this.classFields = null;
    }

    /**
     * Class identification method GATEWAY to all execution statements.
     */
    private void identityMethod() {
        this.reflect();
    }

    /**
     * Uses reflection to introspect the model.
     */
    private void reflect() throws PolarisRuntimeException {
        /**
         * Use Reflections to get the field data.
         */
        ArrayList<DatabaseFields> locFields = DatabaseFields.reflect(this);
        /**
         * If there is no fields throw an exception.
         */
        if (locFields.isEmpty()) {
            throw new PolarisRuntimeException("No Fields are Annotated and cannot be recognized.");
        }
        /**
         * Get the table name from the first entry.
         */
        String locTableName = locFields.get(0).getTable();

        /**
         * Assign to class variables.
         */
        this.classFields = locFields;
        this.databaseTableName = locTableName;
    }

    /**
     * Inserts a new record to the database using dynamic query. fields that are
     * set or remained null will not be included in the fields upon execution of
     * the statement. In this way the query will be optimized to the needed data
     * only.
     *
     * @return true when there is a generated key false if there is none.
     * @param con Connection Manager that will be used.
     * @throws SQLException if there was an error in inserting the data.
     */
    public boolean insert(ConnectionManager con) throws SQLException {
        /**
         * Reflection.
         */
        //----------------------------------------------------------------------
        // Identification Method.
        this.identityMethod();
        //----------------------------------------------------------------------
        ArrayList<DatabaseFields> fields = this.classFields;
        String tableName = this.databaseTableName;
        //----------------------------------------------------------------------
        // INSERT QUERY PREAMBLE.
        final String startQuery = this.sqlInsert + " " + this.sqlInto
                + " " + sqlEscapeCharacter + tableName + sqlEscapeCharacter
                + " ";
        //----------------------------------------------------------------------
        /**
         * Initialize builders.
         */
        StringBuilder fieldBuilder = new StringBuilder("(");
        StringBuilder valueBuilder = new StringBuilder("(");
        /**
         * Create Parameter Holder.
         */
        ArrayList<Object> queryParameters = new ArrayList<>();
        /**
         * Create Primary Key Holder if any.
         */
        DatabaseFields primaryKeyData = null;

        for (int cursor = 0; cursor < fields.size(); cursor++) {
            DatabaseFields modelData = fields.get(cursor);
            /**
             * Check if primary.
             */
            if (modelData.isPrimaryKey()) {
                primaryKeyData = modelData;
            }

            /**
             * Skip if auto-fill.
             */
            if (modelData.isAutoFill()) {
                continue;
            }

            /**
             * Skip if null.
             */
            if (modelData.isNullValue()) {
                continue; // skip also
            }
            /**
             * Construct Values.
             */
            String fieldName = sqlEscapeCharacter + modelData.getColumnName() + sqlEscapeCharacter;
            String fieldValue = "?";
            // add parameters.
            queryParameters.add(modelData.getFieldValue());
            /**
             * Append to query.
             */
            fieldBuilder.append(fieldName);
            valueBuilder.append(fieldValue);
            /**
             * Append Comma.
             */
            fieldBuilder.append(",");
            valueBuilder.append(",");

        }
        // remove excess comma.
        if (fieldBuilder.charAt(fieldBuilder.length() - 1) == ',') {
            fieldBuilder.deleteCharAt(fieldBuilder.length() - 1);
        }
        if (valueBuilder.charAt(valueBuilder.length() - 1) == ',') {
            valueBuilder.deleteCharAt(valueBuilder.length() - 1);
        }

        /**
         * Close Fields.
         */
        fieldBuilder.append(")"); // close fields
        valueBuilder.append(")"); // close values
        /**
         * Finalize Generated Query.
         */

        final String generatedQuery = startQuery + fieldBuilder.toString() + " " + this.sqlValues + " " + valueBuilder.toString() + ";";
        final String executeQuery = StringTools.clearExtraSpaces(generatedQuery);
        logger.debug(executeQuery);

        /**
         * Execute Query. the generated key will be null if no keys are
         * generated.
         */
        Object generatedKey = con.insert(executeQuery, queryParameters.toArray());
        /**
         * Set the generated key as the ID value of this object.
         */
        if (primaryKeyData != null && generatedKey != null) {
            Object convertedKey = null;
            try {
                Method convert = PolarisWrapper.autoBox(primaryKeyData.getFieldType()).getMethod("valueOf", String.class);
                convertedKey = convert.invoke(null, generatedKey.toString());
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
//                logger.log(Level.WARNING, "Cannot Retrieve Generated Key -> {0}", e.toString());
                return false;
            }
            this.writeValue(this, primaryKeyData.getFieldName(), convertedKey);
        }
        return true;
    }

    /**
     * Write values to this model field.
     *
     * @param fieldName
     * @param value
     */
    private void writeValue(Object object, String fieldName, Object value) {
        try {
            new PropertyDescriptor(fieldName, object.getClass()).getWriteMethod().invoke(object, value);
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException ex) {
            throw new PolarisRuntimeException("Cannot Map Values to this Model: Error Writing on field -> " + fieldName, ex);
        } catch (IllegalArgumentException parameterException) {
            String dataType = "Unknown";
            String className = "Unknown";
            if (value != null) {
                dataType = value.getClass().getName();
            }
            if (object != null) {
                className = object.getClass().getName();
            }
            throw new PolarisRuntimeException("Write Error: Invalid Arguement -> [ Class: "
                    + className + " , Field: "
                    + fieldName + " , Type: " + dataType + " ]",
                    parameterException);
        }
    }

    /**
     * Main Method for update.
     *
     * @param con
     * @param includeNull include null fields in the update this will set the
     * database values to null.
     * @return true if there are records affected false if none.
     * @throws SQLException if failed to execute.
     */
    protected boolean updateMaster(ConnectionManager con, boolean includeNull) throws SQLException {
        /**
         * Reflection.
         */
        //----------------------------------------------------------------------
        // Identification Method.
        this.identityMethod();
        //----------------------------------------------------------------------
        ArrayList<DatabaseFields> fields = this.classFields;
        String tableName = this.databaseTableName;

        /**
         * Create a starting query.
         */
        final String startQuery = this.sqlUpdate
                + " " + sqlEscapeCharacter + tableName + sqlEscapeCharacter
                + " " + this.sqlSet + " ";
        /**
         * Create Parameter Holder.
         */
        ArrayList<Object> queryParameters = new ArrayList<>();
        /**
         * Create Primary Key Holder if any.
         */
        DatabaseFields primaryKeyData = null;

        StringBuilder updateBuilder = new StringBuilder();
        for (int cursor = 0; cursor < fields.size(); cursor++) {
            DatabaseFields modelData = fields.get(cursor);
            /**
             * Check if primary.
             */
            if (modelData.isPrimaryKey()) {
                primaryKeyData = modelData;
                continue; // skip if primary key
            }

            /**
             * Skip if auto-fill.
             */
            if (modelData.isAutoFill()) {
                continue;
            }

            /**
             * Include null ?.
             */
            if (!includeNull) {
                /**
                 * Skip if null.
                 */
                if (modelData.isNullValue()) {
                    continue; // skip also
                }
            }

            /**
             * Construct Values.
             */
            String updateField = sqlEscapeCharacter + modelData.getColumnName() + sqlEscapeCharacter + " = ?";
            /**
             * Add Parameters.
             */
            queryParameters.add(modelData.getFieldValue());

            /**
             * append to query.
             */
            updateBuilder.append(updateField);
            /**
             * Append comma.
             */
            updateBuilder.append(",");
        }

        /**
         * Remove Excess Comma.
         */
        if (updateBuilder.charAt(updateBuilder.length() - 1) == ',') {
            updateBuilder.deleteCharAt(updateBuilder.length() - 1);
        }

        /**
         * Create Where Clause if has primary key.
         */
        if (primaryKeyData == null) {
            throw new PolarisRuntimeException("Cannot update model no field is assigned as primary key or annotated with @PrimaryKey");
        }

        if (primaryKeyData.getFieldValue() == null) {
            throw new PolarisRuntimeException("Cannot update model when primary key value is null");
        }
        /**
         * Created Where Clause.
         */
        String whereClause = " " + this.sqlWhere + " "
                + sqlEscapeCharacter + primaryKeyData.getColumnName() + sqlEscapeCharacter
                + " = ?;";

        /**
         * Where Clause Value.
         */
        queryParameters.add(primaryKeyData.getFieldValue());
        final String generatedQuery = startQuery + updateBuilder.toString() + whereClause;
        final String executeQuery = StringTools.clearExtraSpaces(generatedQuery);
        logger.debug(executeQuery);
        /**
         * Execute Update.
         */
        int res = con.update(executeQuery, queryParameters.toArray());
        /**
         * If Nothing was affected by the update.
         */
        return res != 0;
    }

    /**
     * Updates an object to the database. this update method skips the null
     * fields.
     *
     * @param con
     * @return true if there are records affected false if none.
     * @throws SQLException if failed to execute.
     */
    public boolean update(ConnectionManager con) throws SQLException {
        return updateMaster(con, false);
    }

    /**
     * Updates an object to the database. includes the null values in the
     * update.
     *
     * @param con
     * @return true if there are records affected false if none.
     * @throws SQLException SQLException if failed to execute.
     */
    public boolean updateFull(ConnectionManager con) throws SQLException {
        return updateMaster(con, true);
    }

    /**
     * Deletes an object to the database using the primary key. the field
     * annotated with PrimaryKey must not be null in order to execute the
     * operation.
     *
     * @param con
     * @return true if there was an entry deleted. false if does not affect any
     * record.
     * @throws SQLException if failed to execute.
     */
    public boolean delete(ConnectionManager con) throws SQLException {
        /**
         * Reflection.
         */
        //----------------------------------------------------------------------
        // Identification Method.
        this.identityMethod();
        //----------------------------------------------------------------------
        ArrayList<DatabaseFields> fields = this.classFields;
        String tableName = this.databaseTableName;

        /**
         * Create a starting query.
         */
        final String startQuery = this.sqlDelete
                + " " + this.sqlFrom
                + " " + sqlEscapeCharacter + tableName + sqlEscapeCharacter;

        /**
         * Create Primary Key Holder if any.
         */
        DatabaseFields primaryKeyData = null;

        for (int cursor = 0; cursor < fields.size(); cursor++) {
            DatabaseFields modelData = fields.get(cursor);
            /**
             * Check if primary.
             */
            if (modelData.isPrimaryKey()) {
                primaryKeyData = modelData;
                break; // skip if primary key
            }
        }

        /**
         * Check Primary Key.
         */
        if (primaryKeyData == null) {
            throw new PolarisRuntimeException("Cannot Execute Delete: No Field is Annotated as Primary Key.");
        }

        if (primaryKeyData.getFieldValue() == null) {
            throw new PolarisRuntimeException("Cannot Execute Delete: Primary Key Value is Null.");
        }

        /**
         * Created Where Clause.
         */
        String whereClause = " " + this.sqlWhere + " "
                + sqlEscapeCharacter + primaryKeyData.getColumnName() + sqlEscapeCharacter
                + " = ?;";

        final String generatedQuery = startQuery + whereClause;
        final String executeQuery = StringTools.clearExtraSpaces(generatedQuery);
        logger.debug(executeQuery);

        int res = con.update(executeQuery, primaryKeyData.getFieldValue());
        /**
         * If Nothing was affected by the update.
         */
        return res != 0;
    }

    //--------------------------------------------------------------------------
    // sqlSelect METHODS.
    //--------------------------------------------------------------------------
    public boolean find(ConnectionManager con, Object id) throws SQLException {
        /**
         * Reflection.
         */
        //----------------------------------------------------------------------
        // Identification Method.
        this.identityMethod();
        //----------------------------------------------------------------------
        ArrayList<DatabaseFields> fields = this.classFields;
        String tableName = this.databaseTableName;

        /**
         * Create a starting query.
         */
        final String startQuery = this.sqlSelect
                + " * "
                + " " + this.sqlFrom
                + " " + sqlEscapeCharacter + tableName + sqlEscapeCharacter;

        /**
         * Create Primary Key Holder if any.
         */
        DatabaseFields primaryKeyData = null;

        for (int cursor = 0; cursor < fields.size(); cursor++) {
            DatabaseFields modelData = fields.get(cursor);
            /**
             * Check if primary.
             */
            if (modelData.isPrimaryKey()) {
                primaryKeyData = modelData;
                break; // skip if primary key
            }
        }

        /**
         * Check Primary Key.
         */
        if (primaryKeyData == null) {
            throw new PolarisRuntimeException("Cannot Retrieve Records: No Field is Annotated as Primary Key.");
        }

        /**
         * Created Where Clause.
         */
        String whereClause = " " + this.sqlWhere + " "
                + sqlEscapeCharacter + primaryKeyData.getColumnName() + sqlEscapeCharacter
                + " = ? " + this.sqlLimit + " 1;";

        final String generatedQuery = startQuery + whereClause;
        final String executeQuery = StringTools.clearExtraSpaces(generatedQuery);
        logger.debug(executeQuery);

        // Execute Statement
        DataRow dr = con.fetchFirst(executeQuery, id);

        // Check if Empty return false
        if (dr.isEmpty()) {
            logger.trace("Result is empty.");
            return false;
        }

        /**
         * Map The Data.
         */
        for (DatabaseFields field : fields) {
            Object value = dr.get(field.getColumnName());
            this.writeValue(this, field.getFieldName(), value);
        }

        return true;
    }

    public boolean findQuery(ConnectionManager con, QueryBuilder builder) throws SQLException {
        /**
         * Reflection.
         */
        //----------------------------------------------------------------------
        // Identification Method.
        this.identityMethod();
        //----------------------------------------------------------------------
        ArrayList<DatabaseFields> fields = this.classFields;

        // Execute Statement
        DataRow dr = con.fetchFirst(builder.getQueryString(), builder.getParameters());

        // Check if Empty return false
        if (dr.isEmpty()) {
            logger.trace("Result is empty.");
            return false;
        }

        /**
         * Map The Data.
         */
        for (DatabaseFields field : fields) {
            Object value = dr.get(field.getColumnName());
            this.writeValue(this, field.getFieldName(), value);
        }

        return true;
    }

    public <T> List<T> findMany(ConnectionManager con, QueryBuilder builder) throws SQLException {
        // create list holder
        List<T> list = new ArrayList<>();

        /**
         * Reflection.
         */
        //----------------------------------------------------------------------
        // Identification Method.
        this.identityMethod();
        //----------------------------------------------------------------------
        ArrayList<DatabaseFields> fields = this.classFields;

        // get results
        DataSet ds = con.fetch(builder.getQueryString(), builder.getParameters());

        // Check if Empty return false
        if (ds.isEmpty()) {
            logger.trace("Result is empty.");
            return list; // return an empty list
        }

        /**
         * Iterate all over the results
         */
        for (DataRow dataRow : ds) {
            // Check if Empty skip this row
            if (dataRow.isEmpty()) {
                continue;
            }
            // create a row holder
            T row = null;
            try {
                row = (T) this.getClass().newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                throw new PolarisRuntimeException("Cannot Create Model Instance, is there a public and default constructor ?", ex);
            }

            /**
             * Map The Data.
             */
            for (DatabaseFields field : fields) {
                Object value = dataRow.get(field.getColumnName());
                this.writeValue(row, field.getFieldName(), value);
            }

            list.add(row);

        }

        return list; // return the list
    }

    //--------------------------------------------------------------------------
    // INNER CLASS REFLECTION DATA
    // This class is only exclusive to be used inside Polaris Record.
    //--------------------------------------------------------------------------
    /**
     * A Class Data Holder that keeps the important values during reflection
     * scan.
     *
     *
     */
    private static class DatabaseFields {

        /**
         * flag if this field is a primary key.
         */
        private boolean primaryKey;
        /**
         * Column name in the database.
         */
        private String columnName;
        /**
         * Variable name in Java.
         */
        private String fieldName;
        /**
         * Value.
         */
        private Object fieldValue;
        /**
         * Data Type of this field.
         */
        private Class fieldType;
        /**
         * Name of the table which owns this field.
         */
        private String table;

        /**
         * Auto generated value.
         */
        private boolean autoFill;

        public boolean isAutoFill() {
            return autoFill;
        }

        public void setAutoFill(boolean autoFill) {
            this.autoFill = autoFill;
        }

        /**
         * Checks whether this field is a primary key.
         *
         * @return
         */
        public boolean isPrimaryKey() {
            return primaryKey;
        }

        /**
         * Set this field as primary key.
         *
         * @param primaryKey
         */
        public void setPrimaryKey(boolean primaryKey) {
            this.primaryKey = primaryKey;
        }

        /**
         * Get Column Name.
         *
         * @return
         */
        public String getColumnName() {
            return columnName;
        }

        /**
         * Set Column Name.
         *
         * @param columnName
         */
        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        /**
         * Get Field Name.
         *
         * @return
         */
        public String getFieldName() {
            return fieldName;
        }

        /**
         * Set Field Name.
         *
         * @param fieldName
         */
        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        /**
         * Get Field Value.
         *
         * @return
         */
        public Object getFieldValue() {
            return fieldValue;
        }

        /**
         * Set field value.
         *
         * @param fieldValue
         */
        public void setFieldValue(Object fieldValue) {
            this.fieldValue = fieldValue;
        }

        /**
         * Get the table name of this field.
         *
         * @return
         */
        public String getTable() {
            return table;
        }

        /**
         * Set the table name of this field.
         *
         * @param table
         */
        public void setTable(String table) {
            this.table = table;
        }

        /**
         * Gets the data type of this field.
         *
         * @return
         */
        public Class getFieldType() {
            return fieldType;
        }

        /**
         * Set the data type of this field.
         *
         * @param fieldType
         */
        public void setFieldType(Class fieldType) {
            this.fieldType = fieldType;
        }

        /**
         * If this field is empty.
         *
         * @return
         */
        public boolean isNullValue() {
            return this.fieldValue == null;
        }

        //--------------------------------------------------------------------------
        // Static Methods.
        //--------------------------------------------------------------------------
        /**
         * Uses reflections to get the fields annotated with polaris.
         *
         * @param model an object model.
         * @return list of fields with values and specifications.
         *
         */
        public static ArrayList<DatabaseFields> reflect(PolarisRecord model) {
            String tableName = null;
//            String tableName = null;
            int primaryKeyCount = 0; // declare 0 pk counts

            ArrayList<DatabaseFields> fieldAnnotations = new ArrayList<>(10);
            // get the declared fields
            Field[] fields = model.getClass().getDeclaredFields();
            // read declared fields.
            for (Field field : fields) {
                // get annotation for that field
                Annotation[] annotations = field.getAnnotations();
                if (annotations.length == 0) {
                    // ignore this field
                    continue;
                }
                // create model annotation
                DatabaseFields pma = new DatabaseFields();
                /**
                 * Read Model Annotations.
                 */
                if (tableName == null) {
                    DatabaseFields.readModelAnnotations(model, pma);
                    /**
                     * on the first run of read model annotations the table name
                     * should already be fetched.
                     */
                    tableName = pma.getTable();
                } else {
                    // only execute the model reflection once.
                    pma.setTable(tableName);
                }

                // read field name
                pma.setFieldName(field.getName());
                // read field value
                try {
                    Object value = new PropertyDescriptor(field.getName(), model.getClass()).getReadMethod().invoke(model);
                    pma.setFieldValue(value);
                } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    /**
                     * Throw runtime error.
                     */
                    throw new PolarisRuntimeException("Unable to set the generated key." + field.getName(), e);
                }
                /**
                 * Get the data type.
                 */

                pma.setFieldType(field.getType());
                // read annotations
                for (Annotation annotation : annotations) {
                    // get column name
                    if (annotation instanceof Column) {
                        Column col = (Column) annotation;
                        pma.setColumnName(col.value());
                    }
                    // if primary key
                    if (annotation instanceof PrimaryKey) {
                        pma.setPrimaryKey(true);
                        primaryKeyCount++;
                        // if declared pk's are more than 1
                        if (primaryKeyCount > 1) {
                            /**
                             * Throw error if primary key is more than one.
                             */
                            throw new PolarisRuntimeException("Primary Key Annotation must be only used once.");
                        }
                    }

                    if (annotation instanceof FetchOnly) {
                        pma.setAutoFill(true);
                    }
                }
                /**
                 * Add To List.
                 */
                fieldAnnotations.add(pma);
            }
            return fieldAnnotations;
        }

        /**
         * Reads the annotations of a model.
         *
         * @param model
         *
         */
        private static void readModelAnnotations(PolarisRecord model, DatabaseFields pma) {
            Annotation[] annotations = model.getClass().getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Table) {
                    Table table = (Table) annotation;
                    pma.setTable(table.value());
                }
            }
        }

    } // end of polaris record data
    //--------------------------------------------------------------------------
    // END OF INNER CLASS REFLECTION DATA
    //--------------------------------------------------------------------------

} // END OF POLARIS RECORD.
