package mimikko.zazalng.pudel.commands;

import mimikko.zazalng.pudel.contracts.Command.BaseCommandState;
import mimikko.zazalng.pudel.entities.SessionEntity;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCommand<S extends Enum<S> & BaseCommandState> implements Command<S> {
    protected Map<String, String> localizationArgs = new HashMap<>();

    @Override
    public <T extends Command<S>> T execute(SessionEntity session, String args) {
        if (session.getState().equals(BaseCommandState.INIT)) {
            initialState(session, args);
        }
        return (T) this;
    }

    protected String localize(SessionEntity session, String key, Map<String, String> args) {
        return session.getPudelWorld().getLocalizationManager().getLocalizedText(session, key, args);
    }

    protected String localize(SessionEntity session, String key) {
        return localize(session, key, null);
    }
}