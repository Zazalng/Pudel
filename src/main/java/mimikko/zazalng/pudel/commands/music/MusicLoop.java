package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.SessionEntity;

import static mimikko.zazalng.pudel.utility.BooleanUtility.*;

public class MusicLoop extends AbstractCommand{
    @Override
    public void execute(SessionEntity session, String args) {
        super.execute(session, args);
    }

    @Override
    protected void initialState(SessionEntity session, String args) {
        if (triggerTrue(args)) {
            session.getGuild().getMusicPlayer().setLoop(true);
            args = session.getUser().getNickname(session.getGuild()) + " has setting for Looping to`" + session.getGuild().getMusicPlayer().isLoop() + "`";
        } else if (triggerFalse(args)) {
            session.getGuild().getMusicPlayer().setLoop(false);
            args = session.getUser().getNickname(session.getGuild()) + " has setting for Looping to`" + session.getGuild().getMusicPlayer().isLoop() + "`";
        } else {
            args = "The current setting for Looping is `" + session.getGuild().getMusicPlayer().isLoop() + "`";
        }
        session.getChannel().sendMessage(args).queue();
        session.setState("END");
    }

    @Override
    public void reload() {

    }

    @Override
    public String getDescription() {
        return "Toggles the looping of the current track.";
    }

    @Override
    public String getDetailedHelp() {
        return "Usage: loop [args]" +
                "\nExamples: `p!loop`, `p!loop 0`, `p!loop true`" +
                "\n\n`false` `0` `off` `disable` will result set loop as `off`." +
                "\n`true` `1` `on` `enable` will result set loop as `on`." +
                "\nNo argument will result as showing current setting loop.";
    }
}