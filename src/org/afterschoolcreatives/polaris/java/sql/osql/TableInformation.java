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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.afterschoolcreatives.polaris.java.sql.osql.annotations.Id;
import org.afterschoolcreatives.polaris.java.sql.osql.annotations.Table;
import org.afterschoolcreatives.polaris.java.sql.osql.util.AnnotationReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jhon Melvin
 */
class TableInformation {

    private final static Logger LOG = LoggerFactory.getLogger(TableInformation.class);

    public TableInformation(Class ownerClass) {
        this.ownerClass = ownerClass;
        this.identify();
        LOG.info("{} [Table Constructed]", ownerClass.getName());
    }

    /**
     * Runs sequence of operations to identify the table structure.
     */
    private void identify() {
        this.readAnnotations();
        this.translate();
    }


    /*
    #===========================================================================
    # 01. Reflective Block.
    #===========================================================================
     */
    private final Class ownerClass;
    private Annotation[] classAnnotations;
//    private Constructor[] annotatedConstructors;
    private Field[] annotatedFields;
//    private Method[] annotatedMethods;

    public Class getOwnerClass() {
        return ownerClass;
    }

    private void readAnnotations() {
        this.classAnnotations = AnnotationReader.getClassAnnotations(this.ownerClass);
//        this.annotatedConstructors = AnnotationReader.getAnnotatedConstructors(this.ownerClass);
        this.annotatedFields = AnnotationReader.getAnnotatedFields(this.ownerClass);
//        this.annotatedMethods = AnnotationReader.getAnnotatedMethods(this.ownerClass);
    }

    /*
    #===========================================================================
    # 02. Table Information Data.
    #===========================================================================
     */
    /**
     * Column Class for data fields of this table data.
     */
    public static class Column {

        /**
         * Java Field Name.
         */
        private String fieldName;
        /**
         * Java Field Type Class.
         */
        private Class fieldType;
        /**
         * Database Column Name.
         */
        private String columnName;
        /**
         * Set as primary key or ID of the Table.
         */
        private boolean id;
        /**
         * Null not allowed.
         */
        private boolean nullRestricted;
        //----------------------------------------------------------------------
        /**
         * Max length for this column.
         */
        private long length;
        /**
         * If the length was exceeded should be truncated ? or throw an error.
         */
        private boolean truncated;

        /**
         * Default constructor to initialize values.
         */
        public Column() {
            this.fieldName = null;
            this.fieldType = null;
            this.columnName = null;
            this.id = false;
            this.nullRestricted = false;
            this.length = 0;
            this.truncated = false;
        }

        //----------------------------------------------------------------------
        // Getters.
        //----------------------------------------------------------------------
        public String getFieldName() {
            return fieldName;
        }

        public Class getFieldType() {
            return fieldType;
        }

        public String getColumnName() {
            return columnName;
        }

        public boolean isId() {
            return id;
        }

        public boolean isNullRestricted() {
            return nullRestricted;
        }

        public long getLength() {
            return length;
        }

        public boolean isTruncated() {
            return truncated;
        }

        //----------------------------------------------------------------------
        // Setters.
        //----------------------------------------------------------------------
        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public void setFieldType(Class fieldType) {
            this.fieldType = fieldType;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public void setId(boolean id) {
            this.id = id;
        }

        public void setNullRestricted(boolean nullRestricted) {
            this.nullRestricted = nullRestricted;
        }

        public void setLength(long length) {
            this.length = length;
        }

        public void setTruncated(boolean truncated) {
            this.truncated = truncated;
        }

    } // -> end of column class.

    //--------------------------------------------------------------------------
    // Translation Process.
    //--------------------------------------------------------------------------
    private String tableName;
    private Column[] tableColumns;
    private Column idColumn;

    public String getTableName() {
        return tableName;
    }

    public Column[] getTableColumns() {
        return Arrays.copyOf(tableColumns, tableColumns.length);
    }

    public Column getIdColumn() {
        return idColumn;
    }

    private void setIdColumn(Column idColumn) {
        if (this.idColumn != null) {
            throw new RuntimeException(this.ownerClass + "->" + idColumn.getFieldName() + " CANNOT BE SET AS ID, AN ID HAS ALREADY BEEN SET.");
        }
        this.idColumn = idColumn;
    }

    private void translate() {
        this.translateAnnotations();
        this.translateFields();
    }

    private void translateAnnotations() {
        for (Annotation annotation : this.classAnnotations) {
            if (annotation instanceof Table) {
                Table table = (Table) annotation;
                this.tableName = table.value();
                break;
            }
        }

        //----------------------------------------------------------------------
        this.classAnnotations = null;
        //----------------------------------------------------------------------
    }

    private void translateFields() {
        /*
         # Translate Reflected Fields.
         */
        final List<Column> row = new ArrayList<>(); // <- collection of Columns.
        for (Field annotatedField : annotatedFields) {
            Column col = new Column();
            col.setFieldName(annotatedField.getName());
            col.setFieldType(annotatedField.getType());
            //
            for (Annotation annotation : annotatedField.getAnnotations()) {
                if (annotation instanceof org.afterschoolcreatives.polaris.java.sql.osql.annotations.Column) {
                    org.afterschoolcreatives.polaris.java.sql.osql.annotations.Column column = (org.afterschoolcreatives.polaris.java.sql.osql.annotations.Column) annotation;
                    col.setColumnName(column.name());
                    /**
                     * Negate the allow null boolean.
                     */
                    col.setNullRestricted(!column.nullable());
                    /**
                     * Truncate Length.
                     */
                    col.setLength(column.length());
                    col.setTruncated(column.truncate());
                } else if (annotation instanceof Id) {
                    col.setId(true);
                } else {
                    // IGNORE ANNOTATION
                }
            }
            //------------------------------------------------------------------
            row.add(col);
            // set as id column.
            if (col.isId()) {
                this.setIdColumn(col);
            }
            //------------------------------------------------------------------
        } // end field loop.
        //----------------------------------------------------------------------
        // construct table columns
        this.tableColumns = row.toArray(new Column[row.size()]);
        //----------------------------------------------------------------------
        // DESTRUCT ANNOTATED FIELDS after translation.
        this.annotatedFields = null;
        //----------------------------------------------------------------------
    }
}
