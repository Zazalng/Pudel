package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.InteractionEntity;
import mimikko.zazalng.pudel.entities.SessionEntity;

import static mimikko.zazalng.pudel.utility.BooleanUtility.*;

@Deprecated
public class MusicLoop extends AbstractCommand{
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
        interaction.getManager().getMusicManager().getMusicPlayer(interaction).toggleLoop();
    }

    @Override
    public String getDescription(SessionEntity session) {
        return localize(session,"music.loop.help");
    }

    @Override
    public String getDetailedHelp(SessionEntity session) {
        return localize(session,"music.loop.details");
    }

    private MusicLoop initialState(SessionEntity session, String args) {
        if (toggleLogic(args,true)) { //Handle when input was logical able (TRUE result)
            //toggleValue(session,true);
        } else if (toggleLogic(args,false)) { //Handle when input was logical able (FALSE result)
            //toggleValue(session,false);
        } else {
            showCurrent(session);
        }
        super.terminate(session);
        return this;
    }

    /*private MusicLoop toggleValue(SessionEntity session, boolean flag){
        session.getChannel().sendMessageEmbeds(session.getPudelWorld().getEmbedManager().embedCommand(session)
                .setTitle(localize(session,"music.loop.title"))
                .addField(localize(session,"old.value"), localize(session,session.getPudelWorld().getMusicManager().getMusicPlayer(session).getFlagLoop()),false)
                .addField(localize(session,"new.value"), localize(session,session.getPudelWorld().getMusicManager().getMusicPlayer(session).setLoop(flag).isLoop()),false)
                .build()).queue();
        return this;
    }*/

    private MusicLoop showCurrent(SessionEntity session){
        session.getChannel().sendMessageEmbeds(session.getManager().getEmbedManager().embedCommand(session)
                .setTitle(localize(session,"music.loop.title"))
                .addField(localize(session,"current.value"), localize(session, localize(session,session.getManager().getMusicManager().getLoopKey(session))), true)
                .build()).queue();
        return this;
    }
}