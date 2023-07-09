package mimikko.zazalng.puddle.handlers.connection.MySQL;

import mimikko.zazalng.puddle.handlers.DatabaseHandler;
import mimikko.zazalng.puddle.handlers.connection.ConnectionMysql;

public abstract class HostnameMySQL extends ConnectionMysql{
    private String hostname;
    private String port;
    private String database;
    private String username;
    private String password;
    
    public HostnameMySQL(DatabaseHandler databaseHandler, String hostname, String port, String database, String username, String password){
        super(databaseHandler);
        
        this.hostname = hostname;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }
}
