package mimikko.zazalng.puddle.commands.music;

import mimikko.zazalng.puddle.commands.Command;
import mimikko.zazalng.puddle.entities.GuildEntity;
import mimikko.zazalng.puddle.entities.UserEntity;

import static mimikko.zazalng.puddle.utility.StringUtility.isEqualStr;

public class MusicLoop implements Command {
    @Override
    public void execute(GuildEntity guild, UserEntity user, String replyChannel, String args) {
        if(isEqualStr("true",args) || isEqualStr("1",args) || isEqualStr("on",args)){
            guild.getMusicManager().setLoop(true);
            guild.getGuild().getTextChannelById(replyChannel).sendMessage(guild.getGuild().getMemberById(user.getJDAuser().getId()).getNickname()+" has setting for Looping to `"+guild.getMusicManager().isLoop()+"`").queue();
        } else if(isEqualStr("false", args) || isEqualStr("0",args) || isEqualStr("off",args)){
            guild.getMusicManager().setLoop(false);
            guild.getGuild().getTextChannelById(replyChannel).sendMessage(guild.getGuild().getMemberById(user.getJDAuser().getId()).getNickname()+" has setting for Looping to `"+guild.getMusicManager().isLoop()+"`").queue();
        } else{
            guild.getGuild().getTextChannelById(replyChannel).sendMessage("The current setting for Looping is `"+guild.getMusicManager().isLoop()+"`").queue();
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