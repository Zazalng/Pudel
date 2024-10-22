package mimikko.zazalng.pudel.commands;

import mimikko.zazalng.pudel.contracts.BaseCommandState;
import mimikko.zazalng.pudel.entities.SessionEntity;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCommand implements Command {
    protected Map<String, String> localizationArgs = new HashMap<>();

    @Override
    public <T extends Command> T stateEnd(SessionEntity session){
        session.setState(BaseCommandState.END);
        return (T) this;
    }

    protected String localize(SessionEntity session, String key, Map<String, String> args) {
        return session.getPudelWorld().getLocalizationManager().getLocalizedText(session, key, args);
    }

    protected String localize(SessionEntity session, String key) {
        return localize(session, key, null);
    }
}