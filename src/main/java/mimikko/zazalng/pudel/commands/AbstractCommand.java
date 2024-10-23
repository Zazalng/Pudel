package mimikko.zazalng.pudel.commands;

import mimikko.zazalng.pudel.entities.SessionEntity;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCommand implements Command{
    protected enum state{};
    protected Map<String, String> localizationArgs = new HashMap<>();

    protected Command end(SessionEntity session){
        session.getPudelWorld().getSessionManager().sessionEnd(session);
        return this;
    }

    protected String localize(SessionEntity session, String key, Map<String, String> args) {
        return session.getPudelWorld().getLocalizationManager().getLocalizedText(session, key, args);
    }

    protected String localize(SessionEntity session, String key) {
        return localize(session, key, null);
    }
}