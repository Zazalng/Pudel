package mimikko.zazalng.puddle.contracts.database;

import mimikko.zazalng.puddle.handlers.DatabaseHandler;

import java.sql.SQLException;

@FunctionalInterface
public interface BatchQueryFunction<PreparedStatement>{
    void run(PreparedStatement statement) throws SQLException;
}
