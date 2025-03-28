package mimikko.zazalng.pudel.commands;

import mimikko.zazalng.pudel.entities.InteractionEntity;
import mimikko.zazalng.pudel.entities.SessionEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class AbstractCommand implements Command{
    protected Map<String, String> localizationArgs = new HashMap<>();

    @Override
    public void execute(SessionEntity session, String args){
        session.getChannel().sendMessage(localize(session,"not.implemented")).queueAfter(10, TimeUnit.SECONDS, e -> e.delete().queue());
        terminate(session);
    }

    @Override
    public void execute(InteractionEntity interaction){
        interaction.getMessage().getChannel().sendMessage(localize(interaction,"not.implemented")).queueAfter(10, TimeUnit.SECONDS, e -> e.delete().queue());
        terminate(interaction);
    }

    @Override
    public void reload(){

    }

    @Override
    public void terminate(SessionEntity session){
        session.getManager().getSessionManager().sessionEnd(session);
    }

    public void terminate(InteractionEntity interaction){
        interaction.getManager().getInteractionManager().unregisterInteraction(interaction);
    }

    protected String localize(SessionEntity session, String key, Map<String, String> args) {
        return session.getManager().getLocalizationManager().getLocalizedText(session, key, args);
    }

    protected String localize(SessionEntity session, String key) {
        return localize(session, key, null);
    }

    protected String localize(SessionEntity session, Boolean flag) {
        return session.getManager().getLocalizationManager().getBooleanText(session, flag);
    }

    protected String localize(InteractionEntity interaction, String key) {
        return interaction.getManager().getLocalizationManager().getLocalizedText(interaction, key, null);
    }
}