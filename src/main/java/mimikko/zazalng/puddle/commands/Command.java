package mimikko.zazalng.puddle.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface Command {
    void execute(MessageReceivedEvent e, String [] args);
}