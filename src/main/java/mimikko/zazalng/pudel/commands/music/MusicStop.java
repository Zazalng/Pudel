package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.SessionEntity;

public class MusicStop extends AbstractCommand {
    @Override
    public MusicStop execute(SessionEntity session, String args) {
        initialState(session, args);
        return this;
    }

    public MusicStop initialState(SessionEntity session, String args){
        localizationArgs.put("username", session.getPudelWorld().getUserManager().getUserName(session));

        args = localize(session,"music.stop.init",localizationArgs);

        session.getGuild().stopPlayer();
        session.getPudelWorld().getPudelManager().closeVoiceConnection(session);
        session.getChannel().sendMessage(args).queue();
        super.terminate(session);
        return this;
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
