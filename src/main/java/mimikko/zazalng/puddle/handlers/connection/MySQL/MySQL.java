package mimikko.zazalng.puddle.handlers.connection.MySQL;

import java.sql.SQLException;
import mimikko.zazalng.puddle.handlers.DatabaseHandler;
import mimikko.zazalng.puddle.handlers.connection.StatementInterface;

public class MySQL extends HostnameMySQL{
    public MySQL(DatabaseHandler databaseHandler){
        this(
                databaseHandler,
                databaseHandler.getPuddleWorld().getPuddleWorldEnvironment().getProperty("mysql.host"),
                databaseHandler.getPuddleWorld().getPuddleWorldEnvironment().getProperty("mysql.port"),
                databaseHandler.getPuddleWorld().getPuddleWorldEnvironment().getProperty("mysql.database"),
                databaseHandler.getPuddleWorld().getPuddleWorldEnvironment().getProperty("mysql.username"),
                databaseHandler.getPuddleWorld().getPuddleWorldEnvironment().getProperty("mysql.password"));
    }
    
    public MySQL(DatabaseHandler databaseHandler, String hostname, String port, String database, String username, String password){
        super(databaseHandler, hostname, port, database, username, password);
    }

    @Override
    protected boolean initialize() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean open() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public StatementInterface getStatement(String query) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean hasTable(String table) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean truncate(String table) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
