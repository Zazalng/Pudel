package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.SessionEntity;

import java.util.HashMap;
import java.util.Map;

import static mimikko.zazalng.pudel.utility.BooleanUtility.*;

public class MusicShuffle extends AbstractCommand {
    @Override
    public void execute(SessionEntity session, String args) {
        super.execute(session, args);
    }

    @Override
    protected void initialState(SessionEntity session, String args) {
        Map<String, String> localizationArgs = new HashMap<>();
        localizationArgs.put("username", session.getUser().getNickname(session.getGuild()));
        localizationArgs.put("args", args);

        if(triggerTrue(args)){
            session.getGuild().getMusicPlayer().setShuffle(true);
            args = localize(session,"music.shuffle.init.set");
        } else if(triggerFalse(args)){
            session.getGuild().getMusicPlayer().setShuffle(false);
            args = session.getUser().getNickname(session.getGuild())+" has setting for Shuffle player to `"+session.getGuild().getMusicPlayer().isShuffle()+"`";
        } else if(args.isEmpty()){
            args = "The current setting for Shuffle player is `"+session.getGuild().getMusicPlayer().isShuffle()+"`";
        } else{
            session.getGuild().getMusicPlayer().shufflePlaylist();
            args = session.getUser().getNickname(session.getGuild())+" has shuffle playlist by seed `"+args+"`";
        }
        session.getChannel().sendMessage(args).queue();

        session.setState("END");
    }

    @Override
    public void reload() {

    }

    @Override
    public String getDescription(SessionEntity session) {
        return localize(session, "music.shuffle.help");
    }

    @Override
    public String getDetailedHelp(SessionEntity session) {
        return localize(session, "music.shuffle.details");
    }
}
