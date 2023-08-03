package mimikko.zazalng.puddle.contracts.database;

import java.sql.SQLException;

public interface ConnectionInterface {
    boolean open() throws SQLException;
    StatementInterface getStatement(String query) throws SQLException;
    boolean hasTable(String table);
    boolean truncate(String table);
}