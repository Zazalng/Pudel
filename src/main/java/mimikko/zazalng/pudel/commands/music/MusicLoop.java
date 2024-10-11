package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.SessionEntity;

import static mimikko.zazalng.pudel.utility.BooleanUtility.*;

public class MusicLoop extends AbstractCommand{
    @Override
    public MusicLoop execute(SessionEntity session, String args) {
        super.execute(session, args);
        return this;
    }

    @Override
    public MusicLoop initialState(SessionEntity session, String args) {
        localizationArgs.put("username", session.getPudelWorld().getUserManager().getUserName(session));

        if (toggleLogic(args,true)) {
            session.getGuild().getMusicPlayer().setLoop(true);
            localizationArgs.put("player.loop", session.getPudelWorld().getLocalizationManager().getBooleanText(session, session.getGuild().getMusicPlayer().isLoop()));
            args = localize(session,"music.loop.init.set",localizationArgs);
        } else if (toggleLogic(args,false)) {
            session.getGuild().getMusicPlayer().setLoop(false);
            localizationArgs.put("player.loop", session.getPudelWorld().getLocalizationManager().getBooleanText(session, session.getGuild().getMusicPlayer().isLoop()));
            args = localize(session,"music.loop.init.set",localizationArgs);
        } else {
            localizationArgs.put("player.loop", session.getPudelWorld().getLocalizationManager().getBooleanText(session, session.getGuild().getMusicPlayer().isLoop()));
            args = localize(session,"music.loop.init.display",localizationArgs);
        }
        session.getChannel().sendMessage(args).queue();
        session.setState("END");
        return this;
    }

    @Override
    public MusicLoop reload() {
        return this;
    }

    @Override
    public String getDescription(SessionEntity session) {
        return localize(session,"music.loop.help");
    }

    @Override
    public String getDetailedHelp(SessionEntity session) {
        return localize(session,"music.loop.details");
    }
}