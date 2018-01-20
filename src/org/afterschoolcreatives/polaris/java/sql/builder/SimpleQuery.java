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

    public SimpleQuery addStatement(String query) {
        this.queryString.append(" ");
        this.queryString.append(query);
        this.queryString.append(" ");
        return this;
    }

    public SimpleQuery addParameter(Object... value) {
        if (value.length != 0) {
            this.parameterList.addAll(Arrays.asList(value));
        }
        return this;
    }

}
