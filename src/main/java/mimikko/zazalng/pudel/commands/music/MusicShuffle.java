package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.SessionEntity;

import static mimikko.zazalng.pudel.utility.BooleanUtility.*;

public class MusicShuffle extends AbstractCommand {
    @Override
    public MusicShuffle execute(SessionEntity session, String args) {
        initialState(session, args);
        return this;
    }

    public MusicShuffle initialState(SessionEntity session, String args) {
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
                .addField(localize(session,"old.value"), String.valueOf(session.getGuild().getMusicPlayer().isShuffle()),false)
                .addField(localize(session,"new.value"), String.valueOf(session.getGuild().getMusicPlayer().setShuffle(flag).isShuffle()),false)
                .build()).queue();
        return this;
    }

    private MusicShuffle showCurrent(SessionEntity session){
        session.getChannel().sendMessageEmbeds(session.getPudelWorld().getEmbedManager().embedCommand(session)
                .setTitle(localize(session,"music.shuffle.title"))
                .addField(localize(session,"current.value"), String.valueOf(session.getGuild().getMusicPlayer().isShuffle()),false)
                .build()).queue();
        return this;
    }

    private MusicShuffle seedShuffle(SessionEntity session, String args){
        session.getGuild().getMusicPlayer().shufflePlaylist();
        session.getChannel().sendMessageEmbeds(session.getPudelWorld().getEmbedManager().embedCommand(session)
                .setTitle(localize(session,"music.shuffle.seed"))
                .addField(localize(session,"music.shuffle.seedmsg"), String.format("`%s`",args),false)
                .build()).queue();
        return this;
    }
}
