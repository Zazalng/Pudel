package mimikko.zazalng.puddle.commands.music;

import mimikko.zazalng.puddle.commands.Command;
import mimikko.zazalng.puddle.entities.GuildEntity;
import mimikko.zazalng.puddle.entities.UserEntity;

public class MusicSkip implements Command {
    @Override
    public void execute(GuildEntity guild, UserEntity user, String replyChannel, String args) {
        guild.getMusicManager().nextTrack(true);
        guild.getGuild().getTextChannelById(replyChannel).sendMessage(guild.getGuild().getMemberById(user.getJDAuser().getId()).getNickname()+" has Skip current song because...\n`*TBA Reason*`").queue();
    }

    @Override
    public String getDescription() {
        return "Skip the current track and start up the next track";
    }

    @Override
    public String getDetailedHelp() {
        return "Usage: skip [reason]" +
                "\nExamples: `p!skip`, `p!skip noob song`, `p!skip wtf?`" +
                "\n\nAny argument will result as skip with input reason." +
                "\nNo argument will result as skip for no reason.";
    }
}
