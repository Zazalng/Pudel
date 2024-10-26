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
            session.getGuild().getMusicPlayer().setShuffle(true);
            localizationArgs.put("player.shuffle", session.getPudelWorld().getLocalizationManager().getBooleanText(session, session.getGuild().getMusicPlayer().isShuffle()));
            args = localize(session,"music.shuffle.init.set",localizationArgs);
        } else if(toggleLogic(args,false)){
            session.getGuild().getMusicPlayer().setShuffle(false);
            localizationArgs.put("player.shuffle", session.getPudelWorld().getLocalizationManager().getBooleanText(session, session.getGuild().getMusicPlayer().isShuffle()));
            args = localize(session,"music.shuffle.init.set",localizationArgs);
        } else if(args.isEmpty()){
            localizationArgs.put("player.shuffle", session.getPudelWorld().getLocalizationManager().getBooleanText(session, session.getGuild().getMusicPlayer().isShuffle()));
            args = localize(session,"music.shuffle.init.display",localizationArgs);
        } else{
            session.getGuild().getMusicPlayer().shufflePlaylist();
            args = localize(session,"music.shuffle.init.seed",localizationArgs);
        }
        session.getChannel().sendMessage(args).queue();

        super.terminate(session);
        return this;
    }

    @Override
    public MusicShuffle reload() {
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
}
