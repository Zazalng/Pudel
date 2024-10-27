package mimikko.zazalng.pudel.commands.settings;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.SessionEntity;

public class GuildLanguage extends AbstractCommand {
    @Override
    public GuildLanguage execute(SessionEntity session, String args) {
        initialState(session, args);
        return this;
    }

    public GuildLanguage initialState(SessionEntity session, String args) {
        localizationArgs.put("username", session.getPudelWorld().getUserManager().getUserName(session));

        if(args.isEmpty()){
            localizationArgs.put("lang.name", session.getPudelWorld().getLocalizationManager().getLanguageName(session));
            args = localize(session,"guild.language.init.display",localizationArgs);
        } else{
            session.getGuild().setLanguageCode(args);
            localizationArgs.put("lang.name", session.getPudelWorld().getLocalizationManager().getLanguageName(session));
            args = localize(session,"guild.language.init.accept",localizationArgs);
        }
        session.getChannel().sendMessage(args).queue();
        super.terminate(session);
        return this;
    }

    @Override
    public String getDescription(SessionEntity session) {
        return localize(session,"guild.language.help");
    }

    @Override
    public String getDetailedHelp(SessionEntity session) {
        return localize(session,"guild.language.details");
    }
}
