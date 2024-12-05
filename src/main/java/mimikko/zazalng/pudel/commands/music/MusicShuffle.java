package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.InteractionEntity;
import mimikko.zazalng.pudel.entities.SessionEntity;

import static mimikko.zazalng.pudel.utility.BooleanUtility.*;

public class MusicShuffle extends AbstractCommand {
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
        interaction.getPudelWorld().getMusicManager().getMusicPlayer(interaction).toggleShuffle();
    }

    @Override
    public String getDescription(SessionEntity session) {
        return localize(session, "music.shuffle.help");
    }

    @Override
    public String getDetailedHelp(SessionEntity session) {
        return localize(session, "music.shuffle.details");
    }

    private MusicShuffle toggleValue(SessionEntity session, boolean flag){
        session.getChannel().sendMessageEmbeds(session.getPudelWorld().getEmbedManager().embedCommand(session)
                .setTitle(localize(session,"music.shuffle.title"))
                .addField(localize(session,"old.value"), localize(session,session.getPudelWorld().getMusicManager().getMusicPlayer(session).isShuffle()),false)
                .addField(localize(session,"new.value"), localize(session,session.getPudelWorld().getMusicManager().getMusicPlayer(session).setShuffle(flag).isShuffle()),false)
                .build()).queue();
        return this;
    }

    private MusicShuffle showCurrent(SessionEntity session){
        session.getChannel().sendMessageEmbeds(session.getPudelWorld().getEmbedManager().embedCommand(session)
                .setTitle(localize(session,"music.shuffle.title"))
                .addField(localize(session,"current.value"), localize(session,session.getPudelWorld().getMusicManager().getMusicPlayer(session).isShuffle()),false)
                .build()).queue();
        return this;
    }

    private MusicShuffle seedShuffle(SessionEntity session, String args){
        session.getPudelWorld().getMusicManager().getMusicPlayer(session).shufflePlaylist();
        session.getChannel().sendMessageEmbeds(session.getPudelWorld().getEmbedManager().embedCommand(session)
                .setTitle(localize(session,"music.shuffle.seed"))
                .addField(null, String.format("`%s`",args),false)
                .build()).queue();
        return this;
    }

    private MusicShuffle initialState(SessionEntity session, String args) {
        localizationArgs.put("username", session.getPudelWorld().getUserManager().getUserName(session));
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
