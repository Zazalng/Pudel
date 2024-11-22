package mimikko.zazalng.pudel.commands;

import mimikko.zazalng.pudel.entities.InteractionEntity;
import mimikko.zazalng.pudel.entities.SessionEntity;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCommand implements Command{
    protected Map<String, String> localizationArgs = new HashMap<>();

    @Override
    public void reload(SessionEntity session){
        terminate(session);
    }

    @Override
    public void terminate(SessionEntity session){
        session.getPudelWorld().getSessionManager().sessionEnd(session);
    }

    public void terminate(InteractionEntity interaction){
        interaction.getPudelWorld().getInteractionManager().unregisterInteraction(interaction);
    }

    protected String localize(SessionEntity session, String key, Map<String, String> args) {
        return session.getPudelWorld().getLocalizationManager().getLocalizedText(session, key, args);
    }

    protected String localize(SessionEntity session, String key) {
        return localize(session, key, null);
    }

    protected String localize(SessionEntity session, Boolean flag) {
        return session.getPudelWorld().getLocalizationManager().getBooleanText(session, flag);
    }
}