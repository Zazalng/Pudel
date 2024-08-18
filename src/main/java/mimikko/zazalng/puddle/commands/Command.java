package mimikko.zazalng.puddle.commands;

import mimikko.zazalng.puddle.entities.GuildEntity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface Command {
    void execute(GuildEntity guild, MessageReceivedEvent e, String [] args);
}