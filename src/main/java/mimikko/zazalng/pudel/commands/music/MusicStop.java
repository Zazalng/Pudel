package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.commands.Command;
import mimikko.zazalng.pudel.entities.InteractionEntity;
import mimikko.zazalng.pudel.entities.SessionEntity;

public class MusicStop extends AbstractCommand {
    @Override
    public MusicStop execute(SessionEntity session, String args) {
        initialState(session, args);
        return this;
    }

    /**
     * @param interaction
     * @return
     */
    @Override
    public MusicStop execute(InteractionEntity interaction) {
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

    private MusicStop initialState(SessionEntity session, String args){
        localizationArgs.put("username", session.getPudelWorld().getUserManager().getUserName(session));

        args = localize(session,"music.stop.init",localizationArgs);
        //https://puu.sh/KgAxn.gif
        session.getPudelWorld().getMusicManager().stopPlayer(session);
        super.terminate(session);
        return this;
    }
}
