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
import java.util.ArrayList;
import org.afterschoolcreatives.polaris.java.PolarisException;
import org.afterschoolcreatives.polaris.java.sql.orm.annotations.Column;
import org.afterschoolcreatives.polaris.java.sql.orm.annotations.PrimaryKey;
import org.afterschoolcreatives.polaris.java.sql.orm.annotations.Table;
import org.afterschoolcreatives.polaris.java.sql.orm.annotations.ReadOnly;

/**
 * A Class Data Holder that keeps the important values during reflection scan.
 *
 * @author Jhon Melvin
 */
public class PolarisRecordData {

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
     */
    public static ArrayList<PolarisRecordData> reflect(Model model) {
        
        int primaryKeyCount = 0; // declare 0 pk counts

        ArrayList<PolarisRecordData> fieldAnnotations = new ArrayList<>(10);
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
            PolarisRecordData pma = new PolarisRecordData();
            // read model annotations
            PolarisRecordData.readModelAnnotations(model, pma);
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
                throw new PolarisException("Unable to read field " + field.getName(), e);
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
                        throw new PolarisException("Primary Key Annotation must be only used once.");
                    }
                }
                
                if (annotation instanceof ReadOnly) {
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
     */
    private static void readModelAnnotations(Model model, PolarisRecordData pma) {
        Annotation[] annotations = model.getClass().getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof Table) {
                Table table = (Table) annotation;
                pma.setTable(table.value());
            }
        }
    }
    
}
