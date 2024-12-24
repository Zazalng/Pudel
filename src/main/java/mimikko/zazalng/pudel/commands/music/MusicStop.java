package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.InteractionEntity;
import mimikko.zazalng.pudel.entities.SessionEntity;

/**
 *
 */
@Deprecated
public class MusicStop extends AbstractCommand {
    /**
     * @param session
     * @param args
     */
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
        interaction.getManager().getMusicManager().stopPlayer(interaction);
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
        localizationArgs.put("username", session.getManager().getUserManager().getUserName(session));

        args = localize(session,"music.stop.init",localizationArgs);
        //https://puu.sh/KgAxn.gif
        session.getManager().getMusicManager().stopPlayer(session);
        terminate(session);
        return this;
    }
}
