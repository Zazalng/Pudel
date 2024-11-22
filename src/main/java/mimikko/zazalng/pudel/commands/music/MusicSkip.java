package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.InteractionEntity;
import mimikko.zazalng.pudel.entities.SessionEntity;

public class MusicSkip extends AbstractCommand {
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
        return localize(session, "music.skip.help");
    }

    @Override
    public String getDetailedHelp(SessionEntity session) {
        return localize(session, "music.skip.details");
    }

    private MusicSkip initialState(SessionEntity session, String args){
        session.getPudelWorld().getMusicManager().nextTrack(session,true);
        if(args.isEmpty()){
            replyBack(session);
        } else{
            replyBack(session,args);
        }

        super.terminate(session);
        return this;
    }

    private MusicSkip replyBack(SessionEntity session){
        session.getChannel().sendMessageEmbeds(
                session.getPudelWorld().getEmbedManager().embedCommand(session)
                        .setTitle("music.skip.title").build()
        ).queue();

        return this;
    }

    private MusicSkip replyBack(SessionEntity session, String args){
        session.getChannel().sendMessageEmbeds(
                session.getPudelWorld().getEmbedManager().embedCommand(session)
                        .setTitle("music.skip.title.reason")
                        .setDescription(String.format("`%s`",args)).build()
        ).queue();

        return this;
    }
}
