package mimikko.zazalng.pudel.commands.utility;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.interaction.ReactionEntity;
import mimikko.zazalng.pudel.entities.interaction.TextEntity;

public class UtilityInvite extends AbstractCommand {
    @Override
    public void execute(TextEntity session, String args) {
        initialState(session, args);
    }

    /**
     * @param interaction
     * @return
     */
    @Override
    public void execute(ReactionEntity interaction) {

    }

    @Override
    public String getDescription(TextEntity session) {
        localizationArgs.put("bot.name", session.getManager().getPudelManager().getName(session));
        return localize(session, "utility.invite.help",localizationArgs);
    }

    @Override
    public String getDetailedHelp(TextEntity session) {
        return localize(session, "utility.invite.details");
    }

    private UtilityInvite initialState(TextEntity session, String args) {
        localizationArgs.put("inviteurl", session.getManager().getPudelManager().getInviteURL());

        args = localize(session,"utility.invite.init",localizationArgs);

        session.getChannel().sendMessage(args).queue();
        super.terminate(session);
        return this;
    }
}