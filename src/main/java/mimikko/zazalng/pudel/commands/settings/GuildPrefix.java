package mimikko.zazalng.pudel.commands.settings;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.SessionEntity;

public class GuildPrefix extends AbstractCommand {
    @Override
    public void execute(SessionEntity session, String args) {
        if(session.getState()=="INIT"){
            initialState(session,args);
        }
    }

    private void initialState(SessionEntity session, String args){
        if(args.isEmpty()){
            session.getChannel().asTextChannel().sendMessage("Prefix for this server is `"+session.getGuild().getPrefix()+"`").queue();
        } else{
            session.getGuild().setPrefix(args); // still bug
            session.getChannel().asTextChannel().sendMessage(session.getGuild().getJDA().getMemberById(session.getUser().getJDA().getId()).getNickname()+" has setting for Prefix to `"+session.getGuild().getPrefix()+"`").queue();
        }
        session.setState("END");
    }

    @Override
    public void reload() {

    }

    @Override
    public String getDescription() {
        return "Set a custom prefix for the server. I will respond to this new prefix.";
    }

    @Override
    public String getDetailedHelp() {
        return "Usage: prefix [args]" +
                "\nExamples: `p!prefix`, `p!prefix I love Janu`" +
                "\n\nany [args] will result set guild prefix as [args] input." +
                "\nNo argument will result as showing current guild prefix." +
                "\n\n Caution: blankspace ` ` at the end of input also include";
    }
}
