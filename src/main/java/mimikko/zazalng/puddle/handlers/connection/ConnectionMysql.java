package mimikko.zazalng.puddle.handlers.connection;

import mimikko.zazalng.puddle.handlers.DatabaseHandler;
import java.sql.*;

public abstract class ConnectionMysql implements ConnectionInterface{
    protected DatabaseHandler databaseHandler = null;
    protected Connection connection;

    private boolean lastState;    
    private long lastChecked;

    public ConnectionMysql(DatabaseHandler databaseHandler){
        databaseHandler.getPuddleWorld().PuddleLog("Initialize Database Handler...");
        this.databaseHandler = databaseHandler;
    }
    
    protected abstract boolean initialize();
    
    public synchronized Connection getConnection() throws SQLException {
        if (!isOpen()) {
            open();
            lastState = true;
        }

        return connection;
    }
    
    public final boolean isOpen() {
        return isOpen(2);
    }
    
    public final synchronized boolean isOpen(int seconds) {
        if (connection != null) {
            // Returns the last state if the connection was checked less than three seconds ago.
            if (System.currentTimeMillis() - 5000 < lastChecked) {
                return lastState;
            }

            try {
                if (connection.isClosed()) {
                    return false;
                }

                lastState = connection.isValid(seconds);
                lastChecked = System.currentTimeMillis() - (lastState ? 0L : 250L);

                return lastState;
            } catch (SQLException e) {
                
            }
        }

        return false;
    }
    
    public final boolean close() throws SQLException {
        if (connection == null) {
            this.databaseHandler.getPuddleWorld().PuddleLog("Could not close connection, it is null.");
            return false;
        }

        try {
            connection.close();
            lastState = false;

            return true;
        } catch (SQLException e) {
            this.databaseHandler.getPuddleWorld().PuddleLog("Could not close connection, SQLException: " + e.getMessage());
        }
        return false;
    }
    
    public enum QueryType {
        SELECT,
        INSERT,
        UPDATE,
        DELETE,
        CREATE
    }
}
