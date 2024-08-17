package mimikko.zazalng.puddle.handlers;

import mimikko.zazalng.puddle.commands.Command;
import mimikko.zazalng.puddle.commands.utility.MusicPlay;
import mimikko.zazalng.puddle.commands.utility.MusicStop;
import mimikko.zazalng.puddle.entities.GuildEntity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler {
    private MessageReceivedEvent messageReceivedEvent;
    private GuildEntity guild;
    private final Map<String, Command> commands = new HashMap<>();
    private String commandExec;

    private CommandHandler(){
        registerCommand("play", new MusicPlay());
        registerCommand("stop", new MusicStop());
    }
    
    public CommandHandler(GuildEntity guild, MessageReceivedEvent e){
        this();
        this.guild = guild;
    }

    private void registerCommand(String name, Command command) {
        commands.put(name, command);
    }
}
