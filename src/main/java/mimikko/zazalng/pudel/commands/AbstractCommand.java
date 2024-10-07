package mimikko.zazalng.pudel.commands;

import mimikko.zazalng.pudel.entities.SessionEntity;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCommand implements Command {
    protected Map<String, String> localizationArgs = new HashMap<>();

    @Override
    public void execute(SessionEntity session, String args) {
        if(session.getState().equals("INIT")){
            initialState(session,args);
        }
    }

    protected void initialState(SessionEntity session, String args){}

    protected String localize(SessionEntity session, String key, Map<String, String> args) {
        return session.getPudelWorld().getLocalizationManager().getLocalizedText(key, session.getGuild().getLanguageCode(), args);
    }

    protected String localize(SessionEntity session, String key) {
        return localize(session, key, null);
    }
}