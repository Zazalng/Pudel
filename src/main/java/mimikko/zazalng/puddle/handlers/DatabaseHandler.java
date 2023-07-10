package mimikko.zazalng.puddle.handlers;

import mimikko.zazalng.puddle.PuddleWorld;
import mimikko.zazalng.puddle.exceptions.database.DatabaseException;
import mimikko.zazalng.puddle.handlers.connection.ConnectionMysql;
import java.sql.*;
import mimikko.zazalng.puddle.handlers.connection.MySQL.MySQL;

public class DatabaseHandler {
    private final PuddleWorld puddleWorld;
    private ConnectionMysql connection = null;
    
    public DatabaseHandler(PuddleWorld puddleWorld) {
        this.puddleWorld = puddleWorld;
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
}