package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.InteractionEntity;
import mimikko.zazalng.pudel.entities.SessionEntity;

public class MusicStop extends AbstractCommand {
    @Override
    public void execute(SessionEntity session, String args) {
        initialState(session, args);
    }

    /**
     * @param interaction
     * @return
     */
    @Override
    public void execute(InteractionEntity interaction) {
    }

    @Override
    public String getDescription(SessionEntity session) {
        return localize(session,"music.stop.help");
    }

    @Override
    public String getDetailedHelp(SessionEntity session) {
        return localize(session,"music.stop.details");
    }

    private MusicStop initialState(SessionEntity session, String args){
        localizationArgs.put("username", session.getPudelWorld().getUserManager().getUserName(session));

        args = localize(session,"music.stop.init",localizationArgs);
        //https://puu.sh/KgAxn.gif
        session.getPudelWorld().getMusicManager().stopPlayer(session);
        terminate(session);
        return this;
    }
}
