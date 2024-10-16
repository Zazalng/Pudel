package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.commands.Command;
import mimikko.zazalng.pudel.commands.gacha.GachaDrawing;
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

        loadCommand("drawing", new GachaDrawing())
        //music category
                .loadCommand("loop", new MusicLoop())
                .loadCommand("play", new MusicPlay())
                .loadCommand("shuffle", new MusicShuffle())
                .loadCommand("skip", new MusicSkip())
                .loadCommand("stop", new MusicStop())
        //settings category
                .loadCommand("language", new GuildLanguage())
                .loadCommand("prefix", new GuildPrefix())
        //utility category
                .loadCommand("invite", new UtilityInvite())
        ;
    }

    public CommandManager loadCommand(String name, Command command) {
        commands.put(name, command);
        return this;
    }

    public CommandManager reloadCommand(String name) {
        commands.remove(name);
        return this;
    }

    public CommandManager handleCommand(SessionEntity session, MessageReceivedEvent e) {
        String input = e.getMessage().getContentRaw();
        String prefix = session.getGuild().getPrefix();

        if (session.getState().equals("INIT") && input.startsWith(prefix)) {
            processInitialCommand(session, e, input, prefix);
        } else if (!session.getState().isEmpty() && session.getCommand() != null) {
            session.execute(input);
        } else {
            session.setState("END");
        }
        return this;
    }

    private CommandManager processInitialCommand(SessionEntity session, MessageReceivedEvent e, String input, String prefix) {
        String[] parts = input.substring(prefix.length()).split(" ", 2);
        String commandName = parts[0].toLowerCase();
        input = parts.length > 1 ? parts[1] : "";

        if (commandName.equalsIgnoreCase("help")) {
            handleHelpCommand(session, e, input, prefix);
        } else {
            executeCommand(session, e, commandName, input);
        }
        return this;
    }

    private CommandManager handleHelpCommand(SessionEntity session, MessageReceivedEvent e, String input, String prefix) {
        if (input.isEmpty()) {
            showCommandList(session, e, prefix);
        } else {
            showCommandDetails(session, e, input);
        }
        return this;
    }

    private CommandManager showCommandList(SessionEntity session, MessageReceivedEvent e, String prefix) {
        StringBuilder helpMessage = new StringBuilder("Available commands:\n");
        commands.forEach((name, command) -> helpMessage.append("`").append(prefix).append(name).append("` - ").append(command.getDescription(session)).append("\n"));
        e.getChannel().sendMessage(helpMessage.toString()).queue();
        return this;
    }

    private CommandManager showCommandDetails(SessionEntity session, MessageReceivedEvent e, String input) {
        Command command = commands.get(input.toLowerCase());
        if (command != null) {
            e.getChannel().sendMessage(command.getDetailedHelp(session)).queue();
        } else {
            e.getChannel().sendMessage("Unknown command!").queue();
        }
        return this;
    }

    private CommandManager executeCommand(SessionEntity session, MessageReceivedEvent e, String commandName, String input) {
        Command command = commands.get(commandName.toLowerCase());
        if (command != null) {
            session.setCommand(command);
            session.execute(input);
        } else {
            e.getChannel().sendMessage("Unknown command!").queue();
            session.setState("END");
        }
        return this;
    }


    @Override
    public PudelWorld getPudelWorld(){
        return this.pudelWorld;
    }

    @Override
    public CommandManager initialize() {
        return this;
    }

    @Override
    public CommandManager reload() {
        return this;
    }

    @Override
    public CommandManager shutdown() {
        return this;
    }
}