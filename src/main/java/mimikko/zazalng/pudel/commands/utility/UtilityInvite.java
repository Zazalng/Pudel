package mimikko.zazalng.pudel.commands.utility;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.SessionEntity;

public class UtilityInvite extends AbstractCommand {
    @Override
    public UtilityInvite execute(SessionEntity session, String args) {
        initialState(session, args);
        return this;
    }

    public UtilityInvite initialState(SessionEntity session, String args) {
        localizationArgs.put("inviteurl", session.getPudelWorld().getEnvironment().getInviteURL());

        args = localize(session,"utility.invite.init",localizationArgs);

        session.getChannel().sendMessage(args).queue();
        super.terminate(session);
        return this;
    }

    @Override
    public String getDescription(SessionEntity session) {
        localizationArgs.put("bot.name", session.getPudelWorld().getPudelManager().getName(session));
        return localize(session, "utility.invite.help",localizationArgs);
    }

    @Override
    public String getDetailedHelp(SessionEntity session) {
        return localize(session, "utility.invite.details");
    }
}