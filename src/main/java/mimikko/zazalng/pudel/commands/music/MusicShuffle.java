package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.interaction.ReactionEntity;
import mimikko.zazalng.pudel.entities.interaction.TextEntity;

import static mimikko.zazalng.pudel.utility.BooleanUtility.*;

@Deprecated
public class MusicShuffle extends AbstractCommand {
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
        interaction.getManager().getMusicManager().getMusicPlayer(interaction).toggleShuffle();
    }

    @Override
    public String getDescription(TextEntity session) {
        return localize(session, "music.shuffle.help");
    }

    @Override
    public String getDetailedHelp(TextEntity session) {
        return localize(session, "music.shuffle.details");
    }

    private MusicShuffle toggleValue(TextEntity session, boolean flag){
        session.getChannel().sendMessageEmbeds(session.getManager().getEmbedManager().embedCommand(session)
                .setTitle(localize(session,"music.shuffle.title"))
                .addField(localize(session,"old.value"), localize(session,session.getManager().getMusicManager().getMusicPlayer(session).isShuffle()),false)
                .addField(localize(session,"new.value"), localize(session,session.getManager().getMusicManager().getMusicPlayer(session).setShuffle(flag).isShuffle()),false)
                .build()).queue();
        return this;
    }

    private MusicShuffle showCurrent(TextEntity session){
        session.getChannel().sendMessageEmbeds(session.getManager().getEmbedManager().embedCommand(session)
                .setTitle(localize(session,"music.shuffle.title"))
                .addField(localize(session,"current.value"), localize(session,session.getManager().getMusicManager().getMusicPlayer(session).isShuffle()),false)
                .build()).queue();
        return this;
    }

    private MusicShuffle seedShuffle(TextEntity session, String args){
        session.getManager().getMusicManager().getMusicPlayer(session).shufflePlaylist();
        session.getChannel().sendMessageEmbeds(session.getManager().getEmbedManager().embedCommand(session)
                .setTitle(localize(session,"music.shuffle.seed"))
                .addField(null, String.format("`%s`",args),false)
                .build()).queue();
        return this;
    }

    private MusicShuffle initialState(TextEntity session, String args) {
        localizationArgs.put("username", session.getManager().getUserManager().getUserName(session));
        localizationArgs.put("args", args);

        if(toggleLogic(args,true)){
            toggleValue(session,true);
        } else if(toggleLogic(args,false)){
            toggleValue(session,false);
        } else if(args.isEmpty()){
            showCurrent(session);
        } else{
            seedShuffle(session,args);
        }
        super.terminate(session);
        return this;
    }
}
