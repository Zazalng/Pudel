package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;

public class DatabaseManager extends AbstractManager{
    //private final String [] connection;

    protected DatabaseManager(PudelWorld pudelWorld) {
        super(pudelWorld);
    }

    /**
     * @return
     */
    @Override
    public DatabaseManager initialize() {
        return this;
    }

    /**
     *
     */
    @Override
    public void reload() {

    }

    /**
     *
     */
    @Override
    public void shutdown() {

    }
}
