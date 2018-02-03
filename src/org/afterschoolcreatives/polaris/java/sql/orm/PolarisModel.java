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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.afterschoolcreatives.polaris.java.PolarisException;
import org.afterschoolcreatives.polaris.java.sql.ConnectionManager;

/**
 * Model Dialect that supports MySQL Variants including:
 *
 * <ul>
 * <li>MySQL</li>
 * <li>MariaDB</li>
 * <li>SQLite</li>
 * </ul>
 *
 * @author Jhon Melvin
 */
public class PolarisModel implements Model {
    
    protected String INSERT_INTO = "INSERT INTO"; // insert keyword
    protected String BACKTICK = "`"; // used when using reserved words
    protected String VALUES = "VALUES";

    /**
     * Create a model.
     */
    public PolarisModel() {
        
    }
    
    @Override
    public void insert(ConnectionManager con) throws SQLException {
        ArrayList<PolarisModelData> fields = PolarisModelData.reflect(this);
        if (fields.isEmpty()) {
            throw new PolarisException("No Declared Fields");
        }
        // get table name
        String tableName = fields.get(0).getTable();
        // create starting query
        final String prologue = this.INSERT_INTO
                + " " + this.BACKTICK + tableName + this.BACKTICK
                + " ";
        // field building
        StringBuilder fieldBuilder = new StringBuilder("(");
        // parameter building
        StringBuilder valueBuilder = new StringBuilder("(");
        // parameter values
        int cursor = 0;
        final Object[] parameters = new Object[fields.size()];
        Iterator<PolarisModelData> iterator = fields.iterator();
        PolarisModelData primaryKeyData = null;
        while (iterator.hasNext()) {
            PolarisModelData field = iterator.next();
            if (field.isPrimaryKey()) {
                primaryKeyData = field;
            }
            String fieldName = this.BACKTICK + field.getColumnName() + this.BACKTICK;
            valueBuilder.append("?");
            if (iterator.hasNext()) {
                fieldName += ",";
                valueBuilder.append(",");
            }
            fieldBuilder.append(fieldName);
            // add paramter
            parameters[cursor] = field.getFieldValue();
            cursor++;
        }
        fieldBuilder.append(")"); // close fields
        valueBuilder.append(")"); // close values

        final String generatedQuery = prologue + fieldBuilder.toString() + " " + this.VALUES + " " + valueBuilder.toString() + ";";
        Object primaryKey = con.insert(generatedQuery, parameters);
        
        try {
            Object value = new PropertyDescriptor(primaryKeyData.getFieldName(), this.getClass()).getWriteMethod().invoke(this, Integer.valueOf(primaryKey.toString()));
            System.out.println(value);
        } catch (Exception e) {
            /**
             * Throw runtime error.
             */
            throw new PolarisException("Unable to write field " + primaryKeyData.getFieldName(), e);
        }
    }
    
    @Override
    public boolean update() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public boolean delete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public boolean find(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public boolean findQuery(String sql) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public List findMany(String sql) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
