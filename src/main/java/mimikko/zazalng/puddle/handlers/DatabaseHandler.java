package mimikko.zazalng.puddle.handlers;

import mimikko.zazalng.puddle.PuddleWorld;
import mimikko.zazalng.puddle.handlers.connection.ConnectionMysql;

public class DatabaseHandler {
    private final PuddleWorld puddleWorld;
    private final ConnectionMysql connection;
    
    
    public DatabaseHandler(PuddleWorld puddleWorld) {
        this.puddleWorld = puddleWorld;
        this.connection = new ConnectionMysql(this.puddleWorld.getPuddleWorldEnvironment());
    }
}