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
        localizationArgs.put("inviteurl", "(<https://discord.com/oauth2/authorize?client_id=908724400738672710>)");

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
        return localize(session, "utility.invite.help");
    }

    @Override
    public String getDetailedHelp(SessionEntity session) {
        return localize(session, "utility.invite.details");
    }
}