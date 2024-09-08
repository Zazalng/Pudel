package mimikko.zazalng.pudel.commands.settings;

import mimikko.zazalng.pudel.commands.Command;
import mimikko.zazalng.pudel.entities.GuildEntity;
import mimikko.zazalng.pudel.entities.UserEntity;

public class GuildPrefix implements Command {
    @Override
    public void execute(GuildEntity guild, UserEntity user, String replyChannel, String args) {
        if(args.isEmpty()){
            guild.getGuild().getTextChannelById(replyChannel).sendMessage("Prefix for this server is `"+guild.getPrefix()+"`").queue();
        } else{
            guild.setPrefix(args); // still bug
            guild.getGuild().getTextChannelById(replyChannel).sendMessage(guild.getGuild().getMemberById(user.getJDAuser().getId()).getNickname()+" has setting for Prefix to `"+guild.getPrefix()+"`").queue();
        }
    }

    @Override
    public void reload() {

    }

    @Override
    public String getDescription() {
        return "Set a custom prefix for the server. I will respond to this new prefix.";
    }

    @Override
    public String getDetailedHelp() {
        return "";
    }
}
