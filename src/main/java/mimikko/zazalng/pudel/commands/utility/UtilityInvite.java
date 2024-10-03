package mimikko.zazalng.pudel.commands.utility;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.SessionEntity;

import java.util.HashMap;
import java.util.Map;

public class UtilityInvite extends AbstractCommand {

    @Override
    public void execute(SessionEntity session, String args) {
        super.execute(session, args);
    }

    @Override
    protected void initialState(SessionEntity session, String args) {
        Map<String, String> localizationArgs = new HashMap<>();
        localizationArgs.put("inviteurl", "(<"+session.getPudelWorld().getEnvironment().getInviteURL()+">)");

        args = localize(session,"utility.invite.init",localizationArgs);

        session.getChannel().sendMessage(args).queue();
        session.setState("END");
    }

    @Override
    public void reload() {
        // Optional: reload logic if necessary
    }

    @Override
    public String getDescription(SessionEntity session) {
        Map<String, String> localizationArgs = new HashMap<>();
        localizationArgs.put("bot.name", session.getPudelWorld().getPudelManager().getName(session.getGuild()));
        return localize(session, "utility.invite.help",localizationArgs);
    }

    @Override
    public String getDetailedHelp(SessionEntity session) {
        return localize(session, "utility.invite.details");
    }
}