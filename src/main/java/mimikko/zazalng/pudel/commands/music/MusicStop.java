package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.SessionEntity;

public class MusicStop extends AbstractCommand {
    @Override
    public void execute(SessionEntity session, String args) {
        if(session.getState()=="INIT"){
            initialState(session);
        }
    }

    private void initialState(SessionEntity session){
        String reply = session.getGuild().getJDA().getMemberById(session.getUser().getJDA().getId())+" has forced stop music";

        session.getGuild().getMusicPlayer().stop();
        session.getChannel().asTextChannel().sendMessage(reply).queue();
        session.setState("END");
    }

    @Override
    public void reload() {

    }

    @Override
    public String getDescription() {
        return "Stopping Music and clear all remaining playlist then be gone.";
    }

    @Override
    public String getDetailedHelp() {
        return "Usage: stop" +
                "\nExample: `p!stop`" +
                "\n\nStopping Music and clear all remaining playlist then be gone.";
    }
}
