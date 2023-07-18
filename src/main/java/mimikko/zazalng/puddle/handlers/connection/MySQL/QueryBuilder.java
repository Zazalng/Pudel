/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mimikko.zazalng.puddle.handlers.connection.MySQL;

import java.sql.SQLException;
import mimikko.zazalng.puddle.handlers.DatabaseHandler;

import java.util.*;

/**
 *
 * @author User
 */
public final class QueryBuilder {
    private final DatabaseHandler databaseHandler;
    private final List<QueryOrder> order = new ArrayList<>();
    private final List<QueryClause> wheres = new ArrayList<>();
    private final List<String> columns = new ArrayList<>();
    private final List<JoinClause> joins = new ArrayList<>();
    private final List<Map<String, Object>> items = new ArrayList<>();
    
    private QueryType type;
    private String table = null;
    private int take = -1;
    private int skip = -1;
    private boolean async = false;
    
    public QueryBuilder(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;

        table(table);
    }
    
    public QueryBuilder(DatabaseHandler databaseHandler, String table) {
        this.databaseHandler = databaseHandler;

        table(table);
    }
    
    public final QueryBuilder forcefullySetItems(Map<String, Object>... items) {
        this.items.addAll(Arrays.asList(items));

        return this;
    }
    
    public QueryBuilder table(String table) {
        return selectAll().from(table);
    }
    
    public QueryBuilder from(String table) {
        this.table = table;

        return this;
    }
    
    public String getTable() {
        return table;
    }
    
    public QueryBuilder selectAll() {
        return select("*");
    }
    
    public QueryBuilder select(String... columns) {
        type = QueryType.SELECT;

        for (String column : columns) {
            addColumn(column);
        }

        return this;
    }
    
    public QueryBuilder selectRaw(String select) {
        type = QueryType.SELECT;

        columns.clear();
        columns.add("RAW:" + select.trim());

        return this;
    }
    
    protected void addColumn(String column) {
        if (!column.equals("*")) {
            columns.remove("*");

            if (!columns.contains(column)) {
                columns.add(column);
            }

            return;
        }

        columns.clear();
        columns.add("*");
    }
    
    public List<String> getColumns() {
        return columns;
    }
    
    public QueryBuilder skip(int skip) {
        this.skip = Math.max(skip, 0);

        return this;
    }
    
    public QueryBuilder removeSkip() {
        this.skip = -1;

        return this;
    }
    
    public int getSkip() {
        return skip;
    }
    
    public QueryBuilder take(int take) {
        this.take = Math.max(take, 0);

        return this;
    }
    
    public QueryBuilder removeTake() {
        this.take = -1;

        return this;
    }
    
    public int getTake() {
        return take;
    }
    
    public QueryBuilder where(String column, Object value) {
        return where(column, "=", value);
    }
    
    public QueryBuilder where(String column, String operator, Object value) {
        wheres.add(new Clause(column, operator, value));

        return this;
    }
    
    public QueryBuilder where(ClauseConsumer consumer) {
        NestedClause clause = new NestedClause(this.databaseHandler);

        consumer.build(clause);

        wheres.add(clause);

        return this;
    }
    
    public QueryBuilder andWhere(String column, Object value) {
        return andWhere(column, "=", value);
    }
    
    public QueryBuilder andWhere(String column, String operator, Object value) {
        wheres.add(new Clause(column, operator, value, OperatorType.AND));

        return this;
    }
    
    public QueryBuilder andWhere(ClauseConsumer consumer) {
        NestedClause clause = new NestedClause(this.databaseHandler, OperatorType.AND);

        consumer.build(clause);

        wheres.add(clause);

        return this;
    }
    
    public QueryBuilder orWhere(String column, Object value) {
        return orWhere(column, "=", value);
    }
    
    public QueryBuilder orWhere(String column, String operator, Object value) {
        wheres.add(new Clause(column, operator, value, OperatorType.OR));

        return this;
    }
    
    public QueryBuilder orWhere(ClauseConsumer consumer) {
        NestedClause clause = new NestedClause(this.databaseHandler, OperatorType.OR);

        consumer.build(clause);

        wheres.add(clause);

        return this;
    }
    
    public List<QueryClause> getWhereClauses() {
        return wheres;
    }
    
    public QueryBuilder orderBy(String field) {
        return orderBy(field, "ASC");
    }
    
    public QueryBuilder orderBy(String field, String type) {
        order.add(new QueryOrder(field, type));

        return this;
    }
    
    public QueryBuilder inRandomOrder() {
        order.add(new QueryOrder("RAND()", null, true));

        return this;
    }
    
    public List<QueryOrder> getOrder() {
        return order;
    }
    
    public JoinClause join(String table, String type) {
        JoinClause join = new JoinClause(type, table);

        joins.add(join);

        return join;
    }
    
    public JoinClause leftJoin(String table) {
        return join(table, "left");
    }
    
    public QueryBuilder leftJoin(String table, String one, String two) {
        JoinClause join = leftJoin(table);

        join.on(one, two);

        return this;
    }
    
    public QueryBuilder leftJoin(String table, String one, String operator, String two) {
        JoinClause join = leftJoin(table);

        join.on(one, operator, two);

        return this;
    }
    
    public JoinClause rightJoin(String table) {
        return join(table, "right");
    }
    
    public QueryBuilder rightJoin(String table, String one, String two) {
        JoinClause join = rightJoin(table);

        join.on(one, two);

        return this;
    }
    
    public QueryBuilder rightJoin(String table, String one, String operator, String two) {
        JoinClause join = rightJoin(table);

        join.on(one, operator, two);

        return this;
    }
    
    public JoinClause innerJoin(String table) {
        return join(table, "inner");
    }
    
    public QueryBuilder innerJoin(String table, String one, String two) {
        JoinClause join = innerJoin(table);

        join.on(one, two);

        return this;
    }
    
    public QueryBuilder innerJoin(String table, String one, String operator, String two) {
        JoinClause join = innerJoin(table);

        join.on(one, operator, two);

        return this;
    }
    
    public JoinClause outerJoin(String table) {
        return join(table, "outer");
    }
    
    public QueryBuilder outerJoin(String table, String one, String two) {
        JoinClause join = outerJoin(table);

        join.on(one, two);

        return this;
    }
    
    public QueryBuilder outerJoin(String table, String one, String operator, String two) {
        JoinClause join = outerJoin(table);

        join.on(one, operator, two);

        return this;
    }
    
    public JoinClause fullJoin(String table) {
        return join(table, "full");
    }
    
    public QueryBuilder fullJoin(String table, String one, String two) {
        JoinClause join = fullJoin(table);

        join.on(one, two);

        return this;
    }
    
    public QueryBuilder fullJoin(String table, String one, String operator, String two) {
        JoinClause join = fullJoin(table);

        join.on(one, operator, two);

        return this;
    }
    
    public List<JoinClause> getJoins() {
        return joins;
    }
    
    public QueryBuilder useAsync(boolean async) {
        this.async = async;

        return this;
    }
    
    public String toSQL() {
        return toSQL(type);
    }
    
    public String toSQL(QueryType type) {
        try {
            switch (type) {
                case SELECT:
                    return this.databaseHandler.getConnection().select(this.databaseHandler, this, null);
                case INSERT:
                    return this.databaseHandler.getConnection().insert(this.databaseHandler, this, null);
                case UPDATE:
                    return this.databaseHandler.getConnection().update(this.databaseHandler, this, null);
                case DELETE:
                    return this.databaseHandler.getConnection().delete(this.databaseHandler, this, null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Collection get() throws SQLException {
        String query = toSQL();

        this.databaseHandler.getPuddleWorld().PuddleLog("QueryBuilder get() was called with the following SQL query.\nSQL: " + query);

        // Note: When parsing the result to a collection, we can't use the DBM query method since it auto closes the result,
        // and the collection still needs to communicated with the result set to get meta data so it can build the keysets
        // for the column names, this isn't possible if we close the result before parsing it to the collection, instead
        // we use the direct connection and have the collection close the connection instead.
        return new Collection(this.databaseHandler.getConnection().query(query));
    }
}
