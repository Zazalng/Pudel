package mimikko.zazalng.puddle.commands.music;

import mimikko.zazalng.puddle.commands.Command;
import mimikko.zazalng.puddle.entities.GuildEntity;
import mimikko.zazalng.puddle.entities.UserEntity;

public class MusicStop implements Command {

    @Override
    public void execute(GuildEntity guild, UserEntity user,String replyChannel, String args) {
        guild.getMusicManager().stop();
        guild.getGuild().getTextChannelById(replyChannel).sendMessage("Stopping music...").queue();
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getDetailedHelp() {
        return "";
    }
}
