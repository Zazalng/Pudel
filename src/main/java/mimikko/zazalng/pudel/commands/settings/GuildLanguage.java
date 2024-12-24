package mimikko.zazalng.pudel.commands.settings;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.InteractionEntity;
import mimikko.zazalng.pudel.entities.SessionEntity;

public class GuildLanguage extends AbstractCommand {
    @Override
    public void execute(SessionEntity session, String args) {
        initialState(session, args);
    }

    /**
     * @param interaction
     * @return
     */
    @Override
    public void execute(InteractionEntity interaction) {
    }

    @Override
    public String getDescription(SessionEntity session) {
        return localize(session,"guild.language.help");
    }

    @Override
    public String getDetailedHelp(SessionEntity session) {
        return localize(session,"guild.language.details");
    }

    public GuildLanguage initialState(SessionEntity session, String args) {
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
