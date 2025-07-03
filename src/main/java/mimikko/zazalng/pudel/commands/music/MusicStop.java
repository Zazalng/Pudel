package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.interaction.ReactionEntity;
import mimikko.zazalng.pudel.entities.interaction.TextEntity;

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
    public void execute(TextEntity session, String args) {
        initialState(session, args);
    }

    /**
     * @param interaction
     * @return
     */
    @Override
    public void execute(ReactionEntity interaction) {
        interaction.getManager().getMusicManager().stopPlayer(interaction);
    }

    @Override
    public String getDescription(TextEntity session) {
        return localize(session,"music.stop.help");
    }

    @Override
    public String getDetailedHelp(TextEntity session) {
        return localize(session,"music.stop.details");
    }

    private MusicStop initialState(TextEntity session, String args){
        localizationArgs.put("username", session.getManager().getUserManager().getUserName(session));

        args = localize(session,"music.stop.init",localizationArgs);
        //https://puu.sh/KgAxn.gif
        session.getManager().getMusicManager().stopPlayer(session);
        terminate(session);
        return this;
    }
}
