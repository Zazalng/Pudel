package mimikko.zazalng.puddle.commands;

import mimikko.zazalng.puddle.entities.GuildEntity;
import mimikko.zazalng.puddle.entities.UserEntity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface Command {
    void execute(GuildEntity guild, UserEntity user, String replyChannel, String args);
}