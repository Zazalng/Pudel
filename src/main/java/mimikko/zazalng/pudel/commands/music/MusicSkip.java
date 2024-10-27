package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.SessionEntity;

public class MusicSkip extends AbstractCommand {
    @Override
    public MusicSkip execute(SessionEntity session, String args) {
        initialState(session, args);
        return this;
    }

    public MusicSkip initialState(SessionEntity session, String args){
        session.getGuild().getMusicPlayer().nextTrack(true);
        localizationArgs.put("username", session.getPudelWorld().getUserManager().getUserName(session));
        localizationArgs.put("args", args);

        if(args.isEmpty()){
            args = localize(session, "music.skip.init", localizationArgs);
        } else{
            args = localize(session, "music.skip.init.reason", localizationArgs);
        }
        session.getChannel().sendMessage(args).queue();

        super.terminate(session);
        return this;
    }

    @Override
    public String getDescription(SessionEntity session) {
        return localize(session, "music.skip.help");
    }

    @Override
    public String getDetailedHelp(SessionEntity session) {
        return localize(session, "music.skip.details");
    }
}
