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
package org.afterschoolcreatives.polaris.java.sql.builder;

import java.util.Arrays;

/**
 * A simple query builder that allows SQL statements to be readable and
 * manageable.
 *
 * @author Jhon Melvin
 */
public class SimpleQuery extends QueryBuilder {

    /**
     * Constructor.
     */
    public SimpleQuery() {
        super();
    }

    /**
     * Append a query statement with parameters.
     *
     * @param query
     * @param value
     * @return
     */
    public SimpleQuery addStatementWithParameter(String query, Object... value) {
        this.addStatement(query);
        this.addParameter(value);
        return this;
    }

    /**
     * Add a statement.
     *
     * @param query
     * @return
     */
    public SimpleQuery addStatement(String query) {
        this.queryString.append(" ");
        this.queryString.append(query);
        this.queryString.append(" ");
        return this;
    }

    /**
     * Add a Parameter.
     *
     * @param value
     * @return
     */
    public SimpleQuery addParameter(Object... value) {
        if (value.length != 0) {
            this.parameterList.addAll(Arrays.asList(value));
        }
        return this;
    }

}
