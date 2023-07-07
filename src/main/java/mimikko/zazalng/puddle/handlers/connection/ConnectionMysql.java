package mimikko.zazalng.puddle.handlers.connection;

import java.util.Properties;

public class ConnectionMysql {
    private final Properties puddleWorldEnviroment;
    private final String connectionHostname;
    private final String connectionPort;
    private final String connectionSchema;
    private final String connectionDatabase;
    private final String connectionUsername;
    private final String connectionPassword;
    private boolean isConnected;
    
    public ConnectionMysql(Properties puddleWorldEnviroment){
        this.puddleWorldEnviroment = puddleWorldEnviroment;
        this.connectionHostname = puddleWorldEnviroment.getProperty("mysql.host");
        this.connectionPort = puddleWorldEnviroment.getProperty("mysql.port");
        this.connectionSchema = null;
        this.connectionDatabase = puddleWorldEnviroment.getProperty("mysql.database");
        this.connectionUsername = puddleWorldEnviroment.getProperty("mysql.username");
        this.connectionPassword = puddleWorldEnviroment.getProperty("mysql.password");
        this.isConnected = false;
    }
}
