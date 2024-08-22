package mimikko.zazalng.puddle.commands.music;

import mimikko.zazalng.puddle.commands.Command;
import mimikko.zazalng.puddle.entities.GuildEntity;
import mimikko.zazalng.puddle.entities.UserEntity;

import static mimikko.zazalng.puddle.utility.StringUtility.isEqualStr;

public class MusicShuffle implements Command {
    @Override
    public void execute(GuildEntity guild, UserEntity user, String replyChannel, String args) {
        if(isEqualStr("true",args) || isEqualStr("1",args) || isEqualStr("on",args)){
            guild.getMusicManager().setShuffle(true);
            guild.getGuild().getTextChannelById(replyChannel).sendMessage(guild.getGuild().getMemberById(user.getJDAuser().getId()).getNickname()+" has setting for Shuffle player is `"+guild.getMusicManager().isShuffle()+"`").queue();
        } else if(isEqualStr("false", args) || isEqualStr("0",args) || isEqualStr("off",args)){
            guild.getMusicManager().setShuffle(false);
            guild.getGuild().getTextChannelById(replyChannel).sendMessage(guild.getGuild().getMemberById(user.getJDAuser().getId()).getNickname()+" has setting for Shuffle player is `"+guild.getMusicManager().isShuffle()+"`").queue();
        } else if(args.isEmpty()){
            guild.getGuild().getTextChannelById(replyChannel).sendMessage("The current setting for Shuffle player is `"+guild.getMusicManager().isShuffle()+"`").queue();
        } else{
            guild.getMusicManager().shufflePlaylist();
            guild.getGuild().getTextChannelById(replyChannel).sendMessage("The current playlist has been shuffle").queue();
        }
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
