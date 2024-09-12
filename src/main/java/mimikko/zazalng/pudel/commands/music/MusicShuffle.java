package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.SessionEntity;

import static mimikko.zazalng.pudel.utility.BooleanUtility.*;

public class MusicShuffle extends AbstractCommand {
    @Override
    public void execute(SessionEntity session, String args) {
        super.execute(session, args);
    }

    @Override
    protected void initialState(SessionEntity session, String args) {
        if(triggerTrue(args)){
            session.getGuild().getMusicPlayer().setShuffle(true);
            args = session.getUser().getNickname(session.getGuild())+" has setting for Shuffle player to`"+session.getGuild().getMusicPlayer().isShuffle()+"`";
        } else if(triggerFalse(args)){
            session.getGuild().getMusicPlayer().setShuffle(false);
            args = session.getUser().getNickname(session.getGuild())+" has setting for Shuffle player to`"+session.getGuild().getMusicPlayer().isShuffle()+"`";
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
    public String getDescription() {
        return "Toggles the shuffle of the playlist or scrambled playlist 1 time.";
    }

    @Override
    public String getDetailedHelp() {
        return "Usage: shuffle [args]" +
                "\nExamples: `p!shuffle`, `p!shuffle 0`, `p!shuffle true` `p!shuffle 85d6w4d65asdcdw`" +
                "\n\n`false` `0` `off` `disable` will result set loop as `off`." +
                "\n`true` `1` `on` `enable` will result set loop as `on`." +
                "\nother string will scrambled playlist by String seed." +
                "\nNo argument will result as showing current setting loop.";
    }
}
