package mimikko.zazalng.pudel.handlers;

import mimikko.zazalng.pudel.commands.*;
import mimikko.zazalng.pudel.commands.music.*;
import mimikko.zazalng.pudel.commands.settings.*;
import mimikko.zazalng.pudel.entities.GuildEntity;
import mimikko.zazalng.pudel.entities.UserEntity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler {
    private GuildEntity guild;
    private UserEntity user;
    private final Map<String, Command> commands = new HashMap<>();

    private CommandHandler(){
        //music category
        registerCommand("loop", new MusicLoop());
        registerCommand("play", new MusicPlay());
        registerCommand("np", new MusicPlaying());
        registerCommand("shuffle", new MusicShuffle());
        registerCommand("skip", new MusicSkip());
        registerCommand("stop", new MusicStop());
        //settings category
        registerCommand("prefix", new GuildPrefix());
    }
    
    public CommandHandler(GuildEntity guild, UserEntity user, MessageReceivedEvent e){
        this();
        this.guild = guild;
        this.user = user;
        if(isPrefix(e.getMessage().getContentRaw())){
            handleCommand(e);
        }
    }

    public CommandHandler(GuildEntity guild, UserEntity user, MessageReceivedEvent e, boolean isMention){
        this(guild, user, e);
        handleCommand(e);
    }

    private void registerCommand(String name, Command command) {
        commands.put(name, command);
    }

    private boolean isPrefix(String contentCheck){
        return contentCheck.startsWith(this.guild.getPrefix());
    }

    private void handleCommand(MessageReceivedEvent e) {
        String[] parts = e.getMessage().getContentRaw().substring(guild.getPrefix().length()).split(" ", 2);
        String commandName = parts[0];
        String args = parts.length > 1 ? parts[1] : "";
        String replyChannel = e.getChannel().getId();

        if (commandName.equalsIgnoreCase("help")) {
            if (args.isEmpty()) {
                // Show the list of commands
                StringBuilder helpMessage = new StringBuilder("Available commands:\n");
                commands.forEach((name, command) -> helpMessage.append("`").append(guild.getPrefix()).append(name).append("` - ").append(command.getDescription()).append("\n"));
                e.getChannel().sendMessage(helpMessage.toString()).queue();
            } else {
                // Show detailed help for a specific command
                Command command = commands.get(args.toLowerCase());
                if (command != null) {
                    e.getChannel().sendMessage(command.getDetailedHelp()).queue();
                } else {
                    e.getChannel().sendMessage("Unknown command!").queue();
                }
            }
        } else {
            Command command = commands.get(commandName.toLowerCase());
            System.out.println(
                    "commandName: " + commandName +
                    "\nargs: " + args);
            if (command != null) {
                command.execute(this.guild, this.user, replyChannel, args);
            } else {
                //e.getChannel().sendMessage("Unknown command!").queue();
            }
        }
    }
}
