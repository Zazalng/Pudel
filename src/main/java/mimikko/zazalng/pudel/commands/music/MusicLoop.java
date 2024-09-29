package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.SessionEntity;

import java.util.HashMap;
import java.util.Map;

import static mimikko.zazalng.pudel.utility.BooleanUtility.*;

public class MusicLoop extends AbstractCommand{
    @Override
    public void execute(SessionEntity session, String args) {
        super.execute(session, args);
    }

    @Override
    protected void initialState(SessionEntity session, String args) {
        Map<String, String> localizationArgs = new HashMap<>();
        localizationArgs.put("username", session.getUser().getNickname(session.getGuild()));

        if (toggleLogic(args,true)) {
            session.getGuild().getMusicPlayer().setLoop(true);
            localizationArgs.put("player.loop", session.getPudelWorld().getLocalizationManager().getBooleanText(session.getGuild(),session.getGuild().getMusicPlayer().isLoop()));
            args = localize(session,"music.loop.init.set",localizationArgs);
        } else if (toggleLogic(args,false)) {
            session.getGuild().getMusicPlayer().setLoop(false);
            localizationArgs.put("player.loop", session.getPudelWorld().getLocalizationManager().getBooleanText(session.getGuild(),session.getGuild().getMusicPlayer().isLoop()));
            args = localize(session,"music.loop.init.set",localizationArgs);
        } else {
            localizationArgs.put("player.loop", session.getPudelWorld().getLocalizationManager().getBooleanText(session.getGuild(),session.getGuild().getMusicPlayer().isLoop()));
            args = localize(session,"music.loop.init.display",localizationArgs);
        }
        session.getChannel().sendMessage(args).queue();
        session.setState("END");
    }

    @Override
    public void reload() {

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