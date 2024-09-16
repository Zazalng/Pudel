package mimikko.zazalng.pudel.commands.settings;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.SessionEntity;

import java.util.HashMap;
import java.util.Map;

public class GuildLanguage extends AbstractCommand {
    @Override
    public void execute(SessionEntity session, String args) {
        super.execute(session, args);
    }

    @Override
    protected void initialState(SessionEntity session, String args) {
        Map<String, String> localizationArgs = new HashMap<>();
        localizationArgs.put("username", session.getUser().getNickname(session.getGuild()));

        if(args.isEmpty()){
            localizationArgs.put("lang.name", session.getPudelWorld().getLocalizationManager().getLanguageName(session.getGuild()));
            args = localize(session,"guild.language.init.display",localizationArgs);
        } else{
            session.getGuild().setLanguageCode(args);
            localizationArgs.put("lang.name", session.getPudelWorld().getLocalizationManager().getLanguageName(session.getGuild()));
            args = localize(session,"guild.language.init.accept",localizationArgs);
        }
        session.getChannel().sendMessage(args).queue();
        session.setState("END");
    }

    @Override
    public void reload() {

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
