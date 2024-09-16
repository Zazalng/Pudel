package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.commands.Command;
import mimikko.zazalng.pudel.commands.music.*;
import mimikko.zazalng.pudel.commands.settings.*;
import mimikko.zazalng.pudel.commands.utility.UtilityInvite;
import mimikko.zazalng.pudel.entities.SessionEntity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

public class CommandManager implements Manager {
    protected final PudelWorld pudelWorld;
    private final Map<String, Command> commands;

    public CommandManager(PudelWorld pudelWorld) {
        this.pudelWorld = pudelWorld;
        this.commands = new HashMap<>();

        //music category
        loadCommand("loop", new MusicLoop());
        loadCommand("play", new MusicPlay());
        loadCommand("np", new MusicPlaying());
        loadCommand("shuffle", new MusicShuffle());
        loadCommand("skip", new MusicSkip());
        loadCommand("stop", new MusicStop());
        //settings category
        loadCommand("language", new GuildLanguage());
        loadCommand("prefix", new GuildPrefix());
        //utility category
        loadCommand("invite", new UtilityInvite());
    }

    public void loadCommand(String name, Command command) {
        commands.put(name, command);
    }

    public void reloadCommand(String name) {
        commands.remove(name);
    }

    public void handleCommand(SessionEntity session, MessageReceivedEvent e) {
        String input = e.getMessage().getContentRaw();
        String prefix = session.getGuild().getPrefix();
        if(session.getState().equals("INIT") && input.startsWith(prefix)){
            String[] parts = input.substring(prefix.length()).split(" ",2);
            String commandName = parts[0].toLowerCase();
            input = parts.length > 1 ? parts[1] : "";

            if (commandName.equalsIgnoreCase("help")) {
                if (input.isEmpty()) {
                    // Show the list of commands
                    StringBuilder helpMessage = new StringBuilder("Available commands:\n");
                    commands.forEach((name, command) -> helpMessage.append("`").append(prefix).append(name).append("` - ").append(command.getDescription(session)).append("\n"));
                    e.getChannel().sendMessage(helpMessage.toString()).queue();
                } else {
                    // Show detailed help for a specific command
                    Command command = commands.get(input.toLowerCase());
                    if (command != null) {
                        e.getChannel().sendMessage(command.getDetailedHelp(session)).queue();
                    } else {
                        e.getChannel().sendMessage("Unknown command!").queue();
                    }
                }
            }else {
                Command command = commands.get(commandName.toLowerCase());
                if (command != null) {
                    session.setCommand(command);
                    session.execute(input);
                } else {
                    e.getChannel().sendMessage("Unknown command!").queue();
                    session.setState("END");
                }
            }
        } else if(!session.getState().isEmpty() && session.getCommand() != null){
            session.execute(input);
        } else{
            session.setState("END");
        }
    }

    @Override
    public PudelWorld getPudelWorld(){
        return this.pudelWorld;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void reload() {

    }

    @Override
    public void shutdown() {

    }
}