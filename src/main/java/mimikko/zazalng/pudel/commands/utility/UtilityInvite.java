package mimikko.zazalng.pudel.commands.utility;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.SessionEntity;

public class UtilityInvite extends AbstractCommand {
    @Override
    public void execute(SessionEntity session, String args){
        super.execute(session,args);
    }

    @Override
    protected void initialState(SessionEntity session, String args){
        session.getChannel().sendMessage("Thank you for giving me an interested to invite me\n[Invite Link](<https://discord.com/oauth2/authorize?client_id=908724400738672710>)").queue();
        session.setState("END");
    }
    @Override
    public void reload() {

    }

    @Override
    public String getDescription() {
        return "Show an invitation link for invite Pudel.";
    }

    @Override
    public String getDetailedHelp() {
        return "Usage: invite" +
                "\nExample: `p!invite`" +
                "\n\nShow an invitation link for invite Pudel.";
    }
}
