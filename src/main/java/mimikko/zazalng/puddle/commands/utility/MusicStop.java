package mimikko.zazalng.puddle.commands.utility;

import mimikko.zazalng.puddle.commands.Command;
import mimikko.zazalng.puddle.entities.GuildEntity;
import mimikko.zazalng.puddle.entities.UserEntity;
import mimikko.zazalng.puddle.manager.MusicManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MusicStop implements Command {

    @Override
    public void execute(GuildEntity guild, UserEntity user,String replyChannel, String args) {
        guild.getMusicManager().stop();
        guild.getGuild().getTextChannelById(replyChannel).sendMessage("Stopping music...").queue();
    }
}
