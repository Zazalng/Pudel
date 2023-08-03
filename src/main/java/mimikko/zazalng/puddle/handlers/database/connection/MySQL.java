package mimikko.zazalng.puddle.handlers.database.connection;

import java.sql.*;
import java.util.concurrent.Executors;
import mimikko.zazalng.puddle.contracts.database.connection.HostnameMySQL;
import mimikko.zazalng.puddle.handlers.database.DatabaseHandler;
import mimikko.zazalng.puddle.contracts.database.StatementInterface;

public class MySQL extends HostnameMySQL{
    public MySQL(DatabaseHandler databaseHandler){
        this(
                databaseHandler,
                databaseHandler.getPuddleWorld().getPuddleWorldEnvironment().getProperty("mysql.host"),
                databaseHandler.getPuddleWorld().getPuddleWorldEnvironment().getProperty("mysql.port"),
                databaseHandler.getPuddleWorld().getPuddleWorldEnvironment().getProperty("mysql.database"),
                databaseHandler.getPuddleWorld().getPuddleWorldEnvironment().getProperty("mysql.username"),
                databaseHandler.getPuddleWorld().getPuddleWorldEnvironment().getProperty("mysql.password"),
                databaseHandler.getPuddleWorld().getPuddleWorldEnvironment().getProperty("mysql.verifyServerCertificate"));
    }
    
    public MySQL(DatabaseHandler databaseHandler, String hostname, String port, String database, String username, String password, String verifyServerCertificate){
        super(databaseHandler, hostname, port, database, username, password, verifyServerCertificate);
    }

    @Override
    protected boolean initialize() {
        try {
            Class.forName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");

            return true;
        } catch (ClassNotFoundException ex) {
            databaseHandler.getPuddleWorld().PuddleLog("MySQL DataSource class missing.\n"+ ex.getMessage());
        }

        return false;
    }

    @Override
    public boolean open() throws SQLException {
        try {
            String url = String.format("jdbc:mysql://%s:%s/%s?autoReconnect=true&verifyServerCertificate=%s&useSSL=true",
                getHostname(), getPort(), getDatabase(),getVerifyServerCertificate()
            );

            if (initialize()) {
                connection = DriverManager.getConnection(url, getUsername(), getPassword());

                // Sets a timeout of 20 seconds(This is an extremely long time, however the default
                // is around 10 minutes so this should give some improvements with the threads
                // not being blocked for ages due to hanging database queries.
                connection.setNetworkTimeout(Executors.newCachedThreadPool(), 1000 * 20);

                return true;
            }
        } catch (SQLException ex) {
            String reason = "Could not establish a MySQL connection, SQLException: " + ex.getMessage();

            databaseHandler.getPuddleWorld().PuddleLog(reason);
            throw new SQLException(reason);
        }

        return false;
    }

    @Override
    public StatementInterface getStatement(String query) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean hasTable(String table) {
        try {
            DatabaseMetaData md = getConnection().getMetaData();

            try (ResultSet tables = md.getTables(null, null, table, new String[]{"TABLE"})) {
                if (tables.next()) {
                    tables.close();

                    return true;
                }
            }
        } catch (SQLException ex) {
            databaseHandler.getPuddleWorld().PuddleLog(String.format("Failed to check if table exists \"%s\": %s", table, ex.getMessage()));
        }

        return false;
    }

    @Override
    public boolean truncate(String table) {
        try {
            if (!hasTable(table)) {
                return false;
            }

            try (Statement statement = getConnection().createStatement()) {
                statement.executeUpdate(String.format("DELETE FROM `%s`;", table));
            }

            return true;
        } catch (SQLException ex) {
            databaseHandler.getPuddleWorld().PuddleLog(String.format("Failed to truncate \"%s\": %s", table, ex.getMessage()));
        }

        return false;
    }
}
