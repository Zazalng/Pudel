package mimikko.zazalng.puddle.handlers;

import mimikko.zazalng.puddle.commands.Command;
import mimikko.zazalng.puddle.commands.utility.GuildPrefix;
import mimikko.zazalng.puddle.commands.utility.MusicPlay;
import mimikko.zazalng.puddle.commands.utility.MusicStop;
import mimikko.zazalng.puddle.entities.GuildEntity;
import mimikko.zazalng.puddle.entities.UserEntity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler {
    private MessageReceivedEvent messageReceivedEvent;
    private GuildEntity guild;
    private UserEntity user;
    private final Map<String, Command> commands = new HashMap<>();

    private CommandHandler(){
        registerCommand("play", new MusicPlay());
        registerCommand("stop", new MusicStop());
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
        String[] parts = e.getMessage().getContentRaw().split(" ", 2);
        String commandName = parts[0].substring(guild.getPrefix().length());
        String args = parts.length > 1 ? parts[1] : "";
        String replyChannel;

        if(guild.getLogChannel().isEmpty()){
            replyChannel = e.getChannel().getId();
        } else{
            replyChannel = guild.getLogChannel();
        }

        Command command = commands.get(commandName.toLowerCase());
        System.out.println("Parameters: String commandName, MessageReceivedEvent e, String[] args" +
                "\ncommandName: "+ commandName+
                "\nargs: "+ args);
        if (command != null) {
            command.execute(this.guild, this.user, replyChannel, args);
        } else {
            //e.getChannel().sendMessage("Unknown command!").queue();
        }
    }
}
