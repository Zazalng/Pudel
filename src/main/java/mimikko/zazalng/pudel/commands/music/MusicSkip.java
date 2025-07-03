package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.interaction.ReactionEntity;
import mimikko.zazalng.pudel.entities.interaction.TextEntity;

@Deprecated
public class MusicSkip extends AbstractCommand {
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
        replyBack(interaction);
    }



    @Override
    public String getDescription(TextEntity session) {
        return localize(session, "music.skip.help");
    }

    @Override
    public String getDetailedHelp(TextEntity session) {
        return localize(session, "music.skip.details");
    }

    private MusicSkip initialState(TextEntity session, String args){
        session.getManager().getMusicManager().nextTrack(session,true);
        if(args.isEmpty()){
            replyBack(session);
        } else{
            replyBack(session,args);
        }

        super.terminate(session);
        return this;
    }

    private MusicSkip replyBack(TextEntity session){
        session.getChannel().sendMessageEmbeds(
                session.getManager().getEmbedManager().embedCommand(session)
                        .setTitle("music.skip.title").build()
        ).queue();

        return this;
    }

    private MusicSkip replyBack(ReactionEntity interaction) {
        interaction.getManager().getMusicManager().nextTrack(interaction,true);
        return this;
    }

    private MusicSkip replyBack(TextEntity session, String args){
        session.getChannel().sendMessageEmbeds(
                session.getManager().getEmbedManager().embedCommand(session)
                        .setTitle("music.skip.title.reason")
                        .setDescription(String.format("`%s`",args)).build()
        ).queue();

        return this;
    }
}
