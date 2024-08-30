package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.Command;
import mimikko.zazalng.pudel.entities.GuildEntity;
import mimikko.zazalng.pudel.entities.UserEntity;

public class MusicPlaying implements Command {

    @Override
    public void execute(GuildEntity guild, UserEntity user, String replyChannel, String args) {
        guild.getGuild().getTextChannelById(replyChannel).sendMessage("Now Playing: " + guild.getMusicManager().getTrackInfo()).queue();
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
