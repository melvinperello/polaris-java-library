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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.afterschoolcreatives.polaris.java.PolarisException;
import org.afterschoolcreatives.polaris.java.sql.ConnectionManager;
import org.afterschoolcreatives.polaris.java.util.PolarisWrapper;
import org.afterschoolcreatives.polaris.java.util.StringTools;

/**
 * The Most basic implementation of the model interface.
 *
 * Supports the following Databases by default:
 * <ul>
 * <li>MySQL</li>
 * <li>MariaDB</li>
 * <li>SQLite</li>
 * </ul>
 *
 * @author Jhon Melvin
 */
public class PolarisRecord implements Model {

    protected String INSERT_INTO = "INSERT INTO"; // insert keyword
    protected String BACKTICK = "`"; // used when using reserved words
    protected String VALUES = "VALUES";
    protected String UPDATE = "UPDATE";
    protected String SET = "SET";

    /**
     * Reflected Table Name.
     */
    private String classTableName;
    /**
     * Reflected Fields.
     */
    private ArrayList<PolarisRecordData> classFields;

    /**
     * Uses reflection to introspect the model.
     */
    private void reflect() {
        /**
         * Use Reflections to get the field data.
         */
        ArrayList<PolarisRecordData> locFields = PolarisRecordData.reflect(this);
        /**
         * If there is no fields throw an exception.
         */
        if (locFields.isEmpty()) {
            throw new PolarisException("No Declared Fields");
        }
        /**
         * Get the table name from the first entry.
         */
        String locTableName = locFields.get(0).getTable();

        /**
         * Assign to class variables.
         */
        this.classFields = locFields;
        this.classTableName = locTableName;
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
    @Override
    public boolean insert(ConnectionManager con) throws SQLException {
        /**
         * Reflection.
         */
        this.reflect();
        ArrayList<PolarisRecordData> fields = this.classFields;
        String tableName = this.classTableName;

        /**
         * Create a starting query.
         */
        final String startQuery = this.INSERT_INTO
                + " " + this.BACKTICK + tableName + this.BACKTICK
                + " ";

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
        PolarisRecordData primaryKeyData = null;

        for (int cursor = 0; cursor < fields.size(); cursor++) {
            PolarisRecordData modelData = fields.get(cursor);
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
            String fieldName = this.BACKTICK + modelData.getColumnName() + this.BACKTICK;
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
        final String generatedQuery = startQuery + fieldBuilder.toString() + " " + this.VALUES + " " + valueBuilder.toString() + ";";
        System.out.println(generatedQuery);
        /**
         * Execute Query. the generated key will be null if no keys are
         * generated.
         */
        Object generatedKey = con.insert(StringTools.clearExtraSpaces(generatedQuery), queryParameters.toArray());
        /**
         * Set the generated key as the ID value of this object.
         */
        if (primaryKeyData != null && generatedKey != null) {
            try {
                Method convert = PolarisWrapper.autoBox(primaryKeyData.getFieldType()).getMethod("valueOf", String.class);
                Object convertedKey = convert.invoke(null, generatedKey.toString());
                // this has no return value.
                new PropertyDescriptor(primaryKeyData.getFieldName(), this.getClass()).getWriteMethod().invoke(this, convertedKey);
            } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                // cannot retrieve generated key
                /**
                 * Logging Engine will be added.
                 */
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean update(ConnectionManager con) throws SQLException {
        /**
         * Reflection.
         */
        this.reflect();
        ArrayList<PolarisRecordData> fields = this.classFields;
        String tableName = this.classTableName;

        /**
         * Create a starting query.
         */
        final String startQuery = this.UPDATE
                + " " + this.BACKTICK + tableName + this.BACKTICK
                + " " + this.SET + " ";

        return true;
    }

    @Override
    public boolean delete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean find(Object id
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean findQuery(String sql
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List findMany(String sql
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
