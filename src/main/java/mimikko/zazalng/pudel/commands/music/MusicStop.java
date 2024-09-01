package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.Command;
import mimikko.zazalng.pudel.entities.GuildEntity;
import mimikko.zazalng.pudel.entities.UserEntity;

public class MusicStop implements Command {

    @Override
    public void execute(GuildEntity guild, UserEntity user,String replyChannel, String args) {
        guild.getMusicManager().stop();
        guild.getGuild().getTextChannelById(replyChannel).sendMessage("Stopping music...").queue();
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
