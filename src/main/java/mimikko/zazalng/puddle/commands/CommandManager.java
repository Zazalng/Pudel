package mimikko.zazalng.puddle.commands;

import mimikko.zazalng.puddle.commands.utility.*;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

public class CommandManager{
    private final Map<String, Command> commands = new HashMap<>();

    public CommandManager() {
        // Register commands here
        registerCommand("play", new MusicPlay());
        registerCommand("stop", new MusicStop());
    }

    private void registerCommand(String name, Command command) {
        commands.put(name, command);
    }

    public void handleCommand(String commandName, MessageReceivedEvent e, String[] args) {
        Command command = commands.get(commandName.toLowerCase());
        if (command != null) {
            command.execute(e, args);
        } else {
            e.getChannel().sendMessage("Unknown command!").queue();
        }
    }
}
