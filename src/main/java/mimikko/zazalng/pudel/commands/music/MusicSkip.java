package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.SessionEntity;

import java.util.HashMap;
import java.util.Map;

public class MusicSkip extends AbstractCommand {
    @Override
    public void execute(SessionEntity session, String args) {
        super.execute(session, args);
    }

    @Override
    protected void initialState(SessionEntity session, String args){
        session.getGuild().getMusicPlayer().nextTrack(true);

        Map<String, String> localizationArgs = new HashMap<>();
        localizationArgs.put("username", session.getPudelWorld().getUserManager().getUserName(session));
        localizationArgs.put("args", args);

        if(args.isEmpty()){
            args = localize(session, "music.skip.init", localizationArgs);
        } else{
            args = localize(session, "music.skip.init.reason", localizationArgs);
        }
        session.getChannel().sendMessage(args).queue();

        session.setState("END");
    }

    @Override
    public void reload() {

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
