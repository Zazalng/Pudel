package mimikko.zazalng.puddle.handlers;

import java.sql.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import mimikko.zazalng.puddle.PuddleWorld;
import mimikko.zazalng.puddle.contracts.database.BatchQueryFunction;
import mimikko.zazalng.puddle.exceptions.database.DatabaseException;
import mimikko.zazalng.puddle.handlers.connection.ConnectionMysql;
import mimikko.zazalng.puddle.handlers.connection.MySQL.MySQL;
import mimikko.zazalng.puddle.handlers.connection.MySQL.QueryBuilder;

import com.mysql.cj.jdbc.exceptions.MySQLTransactionRollbackException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class DatabaseHandler {
    private static final Logger log = LoggerFactory.getLogger(DatabaseHandler.class);
    
    private final PuddleWorld puddleWorld;
    private final AtomicInteger batchIncrementer;
    
    private final Set<Integer> runningBatchRequests;
    private int queryRetries = 5;
    private ConnectionMysql connection = null;
    
    public DatabaseHandler(PuddleWorld puddleWorld) {
        this.puddleWorld = puddleWorld;
        this.batchIncrementer = new AtomicInteger(0);
        this.runningBatchRequests = new HashSet<>();
    }
    
    public PuddleWorld getPuddleWorld(){
        return this.puddleWorld;
    }
    
    public DatabaseHandler getDatabaseHandler(){
        return this;
    }
    
    public ConnectionMysql getConnection() throws SQLException, DatabaseException {
        if (connection == null) {
            connection = new MySQL(this);
        }

        if (connection.isOpen()) {
            return connection;
        }

        if (!connection.open()) {
            throw new DatabaseException("Failed to connect to the database.");
        }

        return connection;
    }
    
    public void setRetries(int retries) {
        this.queryRetries = retries;
    }
    
    public QueryBuilder newQueryBuilder() {
        return new QueryBuilder(this);
    }

    public QueryBuilder newQueryBuilder(String table) {
        return new QueryBuilder(this, table);
    }
    
    public Collection query(String query) throws SQLException {
        log.debug("query(String query) was called with the following SQL query.\nSQL: " + query);
        MDC.put("query", query);

        return runQuery(query, queryRetries);
    }
    
    public Collection query(QueryBuilder query) throws SQLException {
        return query(query.toSQL());
    }
    
    public int queryUpdate(String query) throws SQLException {
        log.debug("queryUpdate(String query) was called with the following SQL query.\nSQL: " + query);
        MDC.put("query", query);

        return runQueryUpdate(query, queryRetries);
    }
    
    public int queryUpdate(QueryBuilder query) throws SQLException {
        return queryUpdate(query.toSQL());
    }
    
    public Set<Integer> queryInsert(String query) throws SQLException {
        log.debug("queryInsert(String query) was called with the following SQL query.\nSQL: " + query);
        Metrics.databaseQueries.labels("INSERT").inc();
        MDC.put("query", query);

        if (!query.toUpperCase().startsWith("INSERT INTO")) {
            throw new DatabaseException("queryInsert was called with a query without an INSERT statement!");
        }

        return runQueryInsert(query, queryRetries);
    }
    
    public Set<Integer> queryInsert(QueryBuilder queryBuilder) throws SQLException {
        String query = queryBuilder.toSQL();
        log.debug("queryInsert(QueryBuilder queryBuilder) was called with the following SQL query.\nSQL: " + query);
        Metrics.databaseQueries.labels("INSERT").inc();
        MDC.put("query", query);

        if (query == null) {
            throw new SQLException("null query was generated, null can not be used as a valid query");
        }

        if (!query.toUpperCase().startsWith("INSERT INTO")) {
            throw new DatabaseException("queryInsert was called with a query without an INSERT statement!");
        }

        return runQueryInsert(queryBuilder, queryRetries);
    }
    
    public void queryBatch(String query, BatchQueryFunction<PreparedStatement> queryFunction) throws SQLException {
        runQueryBatch(query, queryFunction, batchIncrementer.getAndIncrement(), queryRetries);
    }
    
    public boolean hasRunningBatchQueries() {
        return !runningBatchRequests.isEmpty();
    }
    
    private Collection runQuery(String query, int retriesLeft) throws SQLException {
        try (ResultSet resultSet = getConnection().query(query)) {
            return new Collection(resultSet);
        } catch (MySQLTransactionRollbackException e) {
            if (--retriesLeft > 0) {
                return runQuery(query, --retriesLeft);
            }
            throw new MySQLTransactionRollbackException(
                    e.getMessage(), e.getSQLState(), e.getErrorCode()
            );
        }
    }
    
    private int runQueryUpdate(String query, int retriesLeft) throws SQLException {
        try (Statement stmt = getConnection().prepare(query)) {
            if (stmt instanceof PreparedStatement) {
                return ((PreparedStatement) stmt).executeUpdate();
            }

            return stmt.executeUpdate(query);
        } catch (MySQLTransactionRollbackException e) {
            if (--retriesLeft > 0) {
                return runQueryUpdate(query, retriesLeft);
            }
            throw new MySQLTransactionRollbackException(
                    e.getMessage(), e.getSQLState(), e.getErrorCode()
            );
        }
    }
    
    private Set<Integer> runQueryInsert(String query, int retriesLeft) throws SQLException {
        try (PreparedStatement stmt = getConnection().getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.executeUpdate();

            Set<Integer> ids = new HashSet<>();

            ResultSet keys = stmt.getGeneratedKeys();
            while (keys.next()) {
                ids.add(keys.getInt(1));
            }

            return ids;
        } catch (MySQLTransactionRollbackException e) {
            if (--retriesLeft > 0) {
                return runQueryInsert(query, retriesLeft);
            }
            throw new MySQLTransactionRollbackException(
                    e.getMessage(), e.getSQLState(), e.getErrorCode()
            );
        }
    }
    
    private Set<Integer> runQueryInsert(QueryBuilder queryBuilder, int retriesLeft) throws SQLException {
        String query = queryBuilder.toSQL();

        try (PreparedStatement stmt = getConnection().getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            int preparedIndex = 1;
            for (Map<String, Object> row : queryBuilder.getItems()) {
                for (Map.Entry<String, Object> item : row.entrySet()) {
                    if (item.getValue() == null) {
                        continue;
                    }

                    String value = item.getValue().toString();

                    if (value.startsWith("RAW:")
                            || value.equalsIgnoreCase("true")
                            || value.equalsIgnoreCase("false")
                            || value.matches("[-+]?\\d*\\.?\\d+")) {
                        continue;
                    }

                    stmt.setString(preparedIndex++, value);
                }
            }

            stmt.executeUpdate();

            Set<Integer> ids = new HashSet<>();

            ResultSet keys = stmt.getGeneratedKeys();
            while (keys.next()) {
                ids.add(keys.getInt(1));
            }

            return ids;
        } catch (MySQLTransactionRollbackException e) {
            if (--retriesLeft > 0) {
                return runQueryInsert(query, retriesLeft);
            }
            throw new MySQLTransactionRollbackException(
                    e.getMessage(), e.getSQLState(), e.getErrorCode()
            );
        }
    }
    
    private void runQueryBatch(String query, BatchQueryFunction<PreparedStatement> queryFunction, int batchId, int retriesLeft) throws SQLException {
        log.debug("Running batch query with the following values:\n - Query: {}\n - Batch ID: {}\n - Retries Left: {}",
                query, batchId, retriesLeft
        );

        Connection connection = getConnection().getConnection();

        if (!runningBatchRequests.contains(batchId)) {
            runningBatchRequests.add(batchId);
        }

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                queryFunction.run(preparedStatement);

                preparedStatement.executeBatch();
            }
        } catch (SQLException e) {
            log.error("An SQL exception was thrown while running a batch query: {}", query, e);

            try {
                connection.rollback();
            } catch (SQLException e1) {
                log.error("An SQL exception was thrown while attempting to rollback a batch query: {}", query, e);
            }
        } finally {
            try {
                connection.commit();

                runningBatchRequests.remove(batchId);
                if (runningBatchRequests.isEmpty()) {
                    connection.setAutoCommit(true);
                }
            } catch (MySQLTransactionRollbackException e) {
                if (--retriesLeft > 0) {
                    runQueryBatch(query, queryFunction, batchId, retriesLeft);
                }
            }
        }
    }
}