package mimikko.zazalng.puddle.commands.utility;

import mimikko.zazalng.puddle.commands.Command;
import mimikko.zazalng.puddle.entities.GuildEntity;
import mimikko.zazalng.puddle.entities.UserEntity;

public class GuildPrefix implements Command {
    @Override
    public void execute(GuildEntity guild, UserEntity user, String replyChannel, String args) {
        if(args.isEmpty()){
            guild.getGuild().getTextChannelById(replyChannel).sendMessage("Prefix for this server / guild is `"+guild.getPrefix()+"`").queue();
        } else{
            guild.setPrefix(args); // still bug
        }
    }
}
