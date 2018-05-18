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

import java.lang.reflect.Field;
import java.util.List;
import org.afterschoolcreatives.polaris.java.reflection.PolarisAnnotatedClass;
import org.afterschoolcreatives.polaris.java.sql.orm.annotations.Limit;
import org.afterschoolcreatives.polaris.java.sql.orm.annotations.Nullable;

/**
 *
 * @author Jhon Melvin
 */
public class PolarisEntityInformation {

    private PolarisAnnotatedClass annotatedStructure;
    private String entityName;
    private List<EntityField> entityFields;

    public PolarisEntityInformation() {
        this.annotatedStructure = null;
        this.entityName = null;
        this.entityFields = null;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public PolarisAnnotatedClass getAnnotatedStructure() {
        return annotatedStructure;
    }

    public void setAnnotatedStructure(PolarisAnnotatedClass annotatedStructure) {
        this.annotatedStructure = annotatedStructure;
    }

    public List<EntityField> getEntityFields() {
        return entityFields;
    }

    public void setEntityFields(List<EntityField> entityFields) {
        this.entityFields = entityFields;
    }

    /**
     * Polaris Representation of the Field Data.
     */
    public static class EntityField {

        public EntityField() {
            //
            this.fieldName = null;
            this.columnName = null;
            this.primaryKey = false;
            this.fetchOnly = false;
            this.length = 0;
            this.limitApprehensionMode = null;
            this.nullMode = null;
            this.unsigned = false;
        }

        private String fieldName;
        private String columnName;
        private boolean primaryKey;
        private boolean fetchOnly;
        private long length;
        private Limit.Apprehension limitApprehensionMode;
        private Nullable.Mode nullMode;
        private boolean unsigned;

        //----------------------------------------------------------------------
        // Entity Field Setters.
        //----------------------------------------------------------------------
        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public void setPrimaryKey(boolean primaryKey) {
            this.primaryKey = primaryKey;
        }

        public void setFetchOnly(boolean fetchOnly) {
            this.fetchOnly = fetchOnly;
        }

        public void setLength(long length) {
            this.length = length;
        }

        public void setLimitApprehensionMode(Limit.Apprehension apprehensionMode) {
            this.limitApprehensionMode = apprehensionMode;
        }

        public void setNullMode(Nullable.Mode nullMode) {
            this.nullMode = nullMode;
        }

        public void setUnsigned(boolean unsigned) {
            this.unsigned = unsigned;
        }

        //----------------------------------------------------------------------
        // Entity Field Getters.
        //----------------------------------------------------------------------
        public String getFieldName() {
            return fieldName;
        }

        public String getColumnName() {
            return columnName;
        }

        public boolean isPrimaryKey() {
            return primaryKey;
        }

        public boolean isFetchOnly() {
            return fetchOnly;
        }

        public long getLength() {
            return length;
        }

        public Limit.Apprehension getApprehensionMode() {
            return limitApprehensionMode;
        }

        public Nullable.Mode getNullMode() {
            return nullMode;
        }

        public boolean isUnsigned() {
            return unsigned;
        }

    }

}
