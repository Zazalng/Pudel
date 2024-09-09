package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.SessionEntity;

public class MusicSkip extends AbstractCommand {
    @Override
    public void execute(SessionEntity session, String args) {
        super.execute(session, args);
    }

    @Override
    protected void initialState(SessionEntity session, String args){
        session.getGuild().getMusicPlayer().nextTrack(true);
        if(args.isEmpty()){
            args = "without reason.";
        } else{
            args = "because `"+args+"`";
        }
        session.getChannel().sendMessage(session.getUser().getNickname(session.getGuild())+" has Skip current song "+args).queue();

        session.setState("END");
    }

    @Override
    public void reload() {

    }

    @Override
    public String getDescription() {
        return "Skip the current track and start up the next track";
    }

    @Override
    public String getDetailedHelp() {
        return "Usage: skip [reason]" +
                "\nExamples: `p!skip`, `p!skip noob song`, `p!skip wtf?`" +
                "\n\nAny argument will result as skip with input reason for skip." +
                "\nNo argument will result as skip for no reason.";
    }
}
