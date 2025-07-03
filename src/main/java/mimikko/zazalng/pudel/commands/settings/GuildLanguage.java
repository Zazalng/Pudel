package mimikko.zazalng.pudel.commands.settings;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.interaction.TextEntity;

public class GuildLanguage extends AbstractCommand {
    @Override
    public void execute(TextEntity session, String args) {
        initialState(session, args);
    }

    public GuildLanguage initialState(TextEntity session, String args) {
        localizationArgs.put("username", session.getManager().getUserManager().getUserName(session));

        if(args.isEmpty()){
            localizationArgs.put("lang.name", session.getManager().getLocalizationManager().getLanguageName(session));
            args = localize(session,"guild.language.init.display",localizationArgs);
        } else{
            session.getGuild().setLanguageCode(args);
            localizationArgs.put("lang.name", session.getManager().getLocalizationManager().getLanguageName(session));
            args = localize(session,"guild.language.init.accept",localizationArgs);
        }
        session.getChannel().sendMessage(args).queue();
        super.terminate(session);
        return this;
    }
}
