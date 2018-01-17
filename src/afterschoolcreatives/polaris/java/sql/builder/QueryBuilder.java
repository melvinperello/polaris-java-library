package afterschoolcreatives.polaris.java.sql.builder;

import java.util.ArrayList;

/**
 * A base class for all the query builders.
 *
 * @author Jhon Melvin
 */
public abstract class QueryBuilder {

    protected StringBuilder queryString;
    protected ArrayList<Object> parameterList;

    /**
     * Constructor.
     */
    public QueryBuilder() {
        this.queryString = new StringBuilder();
        this.parameterList = new ArrayList<>();
    }

    /**
     * Returns the query string.
     *
     * @return
     */
    public String getQueryString() {
        return this.queryString.toString().replaceAll("\\s+", " ").trim();// trim left and right space.
    }

    /**
     * returns the parameter in order.
     *
     * @return
     */
    public Object[] getParameters() {
        return this.parameterList.toArray();
    }
}
