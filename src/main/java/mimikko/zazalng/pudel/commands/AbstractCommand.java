package mimikko.zazalng.pudel.commands;

import mimikko.zazalng.pudel.entities.InteractionEntity;
import mimikko.zazalng.pudel.entities.interaction.ReactionEntity;
import mimikko.zazalng.pudel.entities.interaction.TextEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class AbstractCommand implements Command{
    protected Map<String, String> localizationArgs = new HashMap<>();

    @Override
    public void execute(TextEntity session, String args){
        session.getChannel().sendMessage(localize(session,"command.abstract.implement")).complete().delete().queueAfter(10, TimeUnit.SECONDS);
        terminate(session);
    }

    @Override
    public void execute(ReactionEntity interaction){
        interaction.getMessage().getChannel().sendMessage(localize(interaction,"command.abstract.implement")).complete().delete().queueAfter(10, TimeUnit.SECONDS);
        terminate(interaction);
    }

    @Override
    public void terminate(InteractionEntity session){
        session.terminate();
    }

    @Override
    public String getDescription(InteractionEntity session){
        return localize(session, "command.abstract.nohelp");
    }

    @Override
    public String getDetailedHelp(InteractionEntity session){
        return localize(session, "command.abstract.nodetail");
    }

    protected String localize(InteractionEntity session, String key, Map<String, String> args) {
        return session.getManager().getLocalizationManager().getLocalizedText(session, key, args);
    }

    protected String localize(InteractionEntity session, String key) {
        return localize(session, key, null);
    }

    protected String localize(InteractionEntity session, Boolean flag) {
        return session.getManager().getLocalizationManager().getBooleanText(session, flag);
    }
}