package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;

public class DatabaseManager extends AbstractManager{
    private final String [] connection;

    protected DatabaseManager(PudelWorld pudelWorld) {
        super(pudelWorld);
    }

    /**
     * @param <T>
     * @return
     */
    @Override
    public <T extends Manager> T initialize() {
        return null;
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
