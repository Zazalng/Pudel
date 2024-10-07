package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.SessionEntity;

import java.util.HashMap;
import java.util.Map;

public class MusicStop extends AbstractCommand {
    @Override
    public void execute(SessionEntity session, String args) {
        super.execute(session, args);
    }

    @Override
    protected void initialState(SessionEntity session, String args){
        Map<String, String> localizationArgs = new HashMap<>();
        localizationArgs.put("username", session.getPudelWorld().getUserManager().getUserName(session));

        args = localize(session,"music.stop.init",localizationArgs);

        session.getGuild().getMusicPlayer().stop();
        session.getPudelWorld().getPudelManager().CloseVoiceConnection(session);
        session.getChannel().sendMessage(args).queue();
        session.setState("END");
    }

    @Override
    public void reload() {

    }

    @Override
    public String getDescription(SessionEntity session) {
        return localize(session,"music.stop.help");
    }

    @Override
    public String getDetailedHelp(SessionEntity session) {
        return localize(session,"music.stop.details");
    }
}
