package mimikko.zazalng.pudel.commands.settings;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.SessionEntity;

public class GuildLanguage extends AbstractCommand {
    @Override
    public void execute(SessionEntity session, String args) {
        super.execute(session, args);
    }

    @Override
    protected void initialState(SessionEntity session, String args) {
        if(args.isEmpty()){
            args = "Language for this server is `"+session.getGuild().getLanguageCode()+"`";
        } else{
            session.getGuild().setLanguageCode(args);
            args = session.getUser().getNickname(session.getGuild())+" has setting Language for this server to `"+session.getGuild().getLanguageCode()+"`";
        }
        session.getChannel().sendMessage(args).queue();
        session.setState("END");
    }

    @Override
    public void reload() {

    }

    @Override
    public String getDescription() {
        return "Set / Display a Language for this server";
    }

    @Override
    public String getDetailedHelp() {
        return "";
    }
}
