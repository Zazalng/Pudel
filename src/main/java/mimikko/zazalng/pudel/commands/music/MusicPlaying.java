package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.Command;
import mimikko.zazalng.pudel.entities.GuildEntity;
import mimikko.zazalng.pudel.entities.UserEntity;

public class MusicPlaying implements Command {

    @Override
    public void execute(GuildEntity guild, UserEntity user, String replyChannel, String args) {
        if(!guild.getMusicPlayer().getTrackInfo().isEmpty()) {
            guild.getGuild().getTextChannelById(replyChannel).sendMessage("Now Playing: " + guild.getMusicPlayer().getTrackInfo()).queue();
        } else{
            guild.getGuild().getTextChannelById(replyChannel).sendMessage("There is no current track that playing");
        }
    }

    @Override
    public String getDescription() {
        return "Show current track information.";
    }

    @Override
    public String getDetailedHelp() {
        return "Usage: np" +
                "\nExample: `p!np`" +
                "\n\nShow information of playing track title / url";
    }
}
