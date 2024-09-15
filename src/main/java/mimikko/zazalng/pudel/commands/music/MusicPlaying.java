package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.SessionEntity;

public class MusicPlaying extends AbstractCommand {
    @Override
    public void execute(SessionEntity session, String args) {
        super.execute(session, args);
    }

    @Override
    protected void initialState(SessionEntity session, String args){
        if(!session.getGuild().getMusicPlayer().getTrackInfo().isEmpty()) {
            args = "Now Playing:\n" + session.getGuild().getMusicPlayer().getTrackInfo();
        } else{
            args = "There is no current track that playing";
        }
        session.getChannel().sendMessage(args).queue();
        session.setState("END");
    }

    @Override
    public void reload() {

    }

    @Override
    public String getDescription(SessionEntity session) {
        return "Show current track information.";
    }

    @Override
    public String getDetailedHelp(SessionEntity session) {
        return "Usage: np" +
                "\nExample: `p!np`" +
                "\n\nShow information of playing track title / url";
    }
}
