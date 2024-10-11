package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.SessionEntity;

public class MusicPlaying extends AbstractCommand {
    @Override
    public MusicPlaying execute(SessionEntity session, String args) {
        super.execute(session, args);
        return this;
    }

    @Override
    public MusicPlaying initialState(SessionEntity session, String args){
        if(!session.getGuild().getMusicPlayer().getTrackInfo().isEmpty()) {
            localizationArgs.put("track.info",session.getGuild().getMusicPlayer().getTrackInfo());
            args = localize(session, "music.playing.init",localizationArgs);
        } else{
            args = localize(session, "music.playing.init.noresult");
        }
        session.getChannel().sendMessage(args).queue();
        session.setState("END");
        return this;
    }

    @Override
    public MusicPlaying reload() {
        return this;
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
