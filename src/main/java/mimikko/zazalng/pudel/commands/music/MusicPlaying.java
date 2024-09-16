package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.SessionEntity;

import java.util.HashMap;
import java.util.Map;

public class MusicPlaying extends AbstractCommand {
    @Override
    public void execute(SessionEntity session, String args) {
        super.execute(session, args);
    }

    @Override
    protected void initialState(SessionEntity session, String args){
        if(!session.getGuild().getMusicPlayer().getTrackInfo().isEmpty()) {
            Map<String, String> localizationArgs = new HashMap<>();
            localizationArgs.put("track.info",session.getGuild().getMusicPlayer().getTrackInfo());
            args = localize(session, "music.playing.init",localizationArgs);
        } else{
            args = localize(session, "music.playing.init.noresult");
        }
        session.getChannel().sendMessage(args).queue();
        session.setState("END");
    }

    @Override
    public void reload() {

    }

    @Override
    public String getDescription(SessionEntity session) {
        return localize(session, "music.playing.help");
    }

    @Override
    public String getDetailedHelp(SessionEntity session) {
        return localize(session, "music.playing.details");
    }
}
