package mimikko.zazalng.puddle.exceptions.database;

public class DatabaseException extends RuntimeException {

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Exception e) {
        super(message, e);
    }
}