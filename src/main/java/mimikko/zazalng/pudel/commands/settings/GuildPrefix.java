package mimikko.zazalng.pudel.commands.settings;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.SessionEntity;

import java.util.HashMap;
import java.util.Map;

public class GuildPrefix extends AbstractCommand {

    @Override
    protected void initialState(SessionEntity session, String args) {
        Map<String, String> localizationArgs = new HashMap<>();
        localizationArgs.put("guild.prefix", session.getGuild().getPrefix());
        localizationArgs.put("username", session.getUser().getNickname(session.getGuild()));

        if (args.isEmpty()) {
            args = localize(session, "guild.prefix.init.display", localizationArgs);
        } else {
            session.getGuild().setPrefix(args);
            args = localize(session, "guild.prefix.init.accept", localizationArgs);
        }

        session.getChannel().sendMessage(args).queue();
        session.setState("END");
    }

    @Override
    public void reload() {

    }

    @Override
    public String getDescription(SessionEntity session) {
        return localize(session, "guild.prefix.help");
    }

    @Override
    public String getDetailedHelp(SessionEntity session) {
        return localize(session, "guild.prefix.details");
    }
}