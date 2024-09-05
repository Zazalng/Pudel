package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.contracts.SessionState;

import java.util.HashMap;
import java.util.Map;

public class SessionManager implements Manager {
    protected final PudelWorld pudelWorld;
    private final Map<String, Object> data;
    private SessionState sessionState;

    public SessionManager(PudelWorld pudelWorld){
        this.pudelWorld = pudelWorld;
        this.data = new HashMap<>();
    }

    @Override
    public void initialize() {

    }

    @Override
    public void reload() {

    }

    @Override
    public void shutdown() {

    }
}
