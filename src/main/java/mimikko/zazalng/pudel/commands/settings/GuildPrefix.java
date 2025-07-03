package mimikko.zazalng.pudel.commands.settings;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.interaction.TextEntity;

public class GuildPrefix extends AbstractCommand {
    @Override
    public void execute(TextEntity session, String args) {
        initialState(session, args);
    }

    @Override
    public String getDescription(TextEntity session) {
        return localize(session, "guild.prefix.help");
    }

    @Override
    public String getDetailedHelp(TextEntity session) {
        return localize(session, "guild.prefix.details");
    }

    public GuildPrefix initialState(TextEntity session, String args) {
        localizationArgs.put("username", session.getManager().getUserManager().getUserName(session));

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
}