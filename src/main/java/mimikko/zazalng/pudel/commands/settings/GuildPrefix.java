package mimikko.zazalng.pudel.commands.settings;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.SessionEntity;

public class GuildPrefix extends AbstractCommand {
    @Override
    public GuildPrefix execute(SessionEntity session, String args) {
        initialState(session, args);
        return this;
    }

    public GuildPrefix initialState(SessionEntity session, String args) {
        localizationArgs.put("username", session.getPudelWorld().getUserManager().getUserName(session));

        if (args.isEmpty()) {
            localizationArgs.put("guild.prefix", session.getGuild().getPrefix());
            args = localize(session, "guild.prefix.init.display", localizationArgs);
        } else {
            session.getGuild().setPrefix(args);
            localizationArgs.put("guild.prefix", session.getGuild().getPrefix());
            args = localize(session, "guild.prefix.init.accept", localizationArgs);
        }

        session.getChannel().sendMessage(args).queue();
        super.terminate(session);

        return this;
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