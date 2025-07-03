package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.commands.Command;
import mimikko.zazalng.pudel.commands.dev.*;
import mimikko.zazalng.pudel.commands.music.*;
import mimikko.zazalng.pudel.commands.settings.*;
import mimikko.zazalng.pudel.commands.utility.*;
import mimikko.zazalng.pudel.entities.UserEntity;
import mimikko.zazalng.pudel.entities.interaction.TextEntity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CommandManager extends AbstractManager {
    private final Map<String, Supplier<Command>> commandFactories;
    private final Logger logger = LoggerFactory.getLogger(CommandManager.class);

    protected CommandManager(PudelWorld pudelWorld) {
        super(pudelWorld);
        this.commandFactories = new HashMap<>();
        loadDefault();
    }

    private CommandManager loadDefault(){
            // Music category commands
            loadCommand("play", MusicPlay::new).
            loadCommand("shuffle", MusicShuffle::new).
            loadCommand("skip", MusicSkip::new).
            loadCommand("stop", MusicStop::new).
            loadCommand("loop", MusicLoop::new).
            // Settings category commands
            loadCommand("language", GuildLanguage::new).
            loadCommand("prefix", GuildPrefix::new).
            // Utility category commands
            loadCommand("emoji", DevEmojiUnicode::new).
            loadCommand("invite", UtilityInvite::new).
            loadCommand("time", UtilityTimeFormatting::new)
        ;
        return this;
    }

    public CommandManager loadCommand(String name, Supplier<Command> commandFactory) {
        commandFactories.put(name, commandFactory);
        return this;
    }

    public CommandManager reloadCommand(String name) {
        commandFactories.remove(name);
        return this;
    }

    public Command getCommand(String command){
        try{
            return commandFactories.get(command.toLowerCase()).get();
        } catch (NullPointerException ex){
            return null;
        }
    }

    public CommandManager handleCommand(TextEntity session, MessageReceivedEvent e) {
        String input = e.getMessage().getContentRaw();
        String prefix = session.getGuild().getPrefix();

        // Check if the message starts with the command prefix
        if (input.startsWith(prefix)) {
            processInitialCommand(session, e, input, prefix);
        } else {
            // If there is an ongoing session, execute the continuation
            if (session.getCommand() != null) {
                session.execute(input);
            } else {
                // Log or ignore non-command messages
                System.out.printf("%s in %s by %s say:\n%s%n",
                        session.getGuild().getJDA().getName(),
                        session.getChannel().getName(),
                        getUserManager().getUserName(session),
                        input);
                getSessionManager().sessionEnd(session);
            }
        }
        return this;
    }

    private CommandManager processInitialCommand(TextEntity session, MessageReceivedEvent e, String input, String prefix) {
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

    private CommandManager handleHelpCommand(TextEntity session, MessageReceivedEvent e, String input, String prefix) {
        if (input.isEmpty()) {
            showCommandList(session, e, prefix);
        } else {
            showCommandDetails(session, e, input);
        }
        getSessionManager().sessionEnd(session);
        return this;
    }

    private CommandManager showCommandList(TextEntity session, MessageReceivedEvent e, String prefix) {
        StringBuilder helpMessage = new StringBuilder("Available commands:\n");
        commandFactories.forEach((name, commandFactory) ->
                helpMessage.append("`").append(prefix).append(name).append("` - ")
                        .append(commandFactory.get().getDescription(session)).append("\n")
        );
        e.getChannel().sendMessage(helpMessage.toString()).queue();
        return this;
    }

    private CommandManager showCommandDetails(TextEntity session, MessageReceivedEvent e, String input) {
        Supplier<Command> commandFactory = commandFactories.get(input.toLowerCase());
        if (commandFactory != null) {
            e.getChannel().sendMessage(commandFactory.get().getDetailedHelp(session)).queue();
        } else {
            e.getChannel().sendMessage("Unknown command!").queue();
        }
        return this;
    }

    private CommandManager executeCommand(TextEntity session, MessageReceivedEvent e, String commandName, String input) {
        Command command = getCommand(commandName);
        if (command!=null) {
            // Create a new command instance for this session
            session.setCommand(command).execute(input);
        } else {
            e.getChannel().sendMessage("Unknown command!").queue();
            getSessionManager().sessionEnd(session);
        }
        return this;
    }

    @Override
    public boolean initialize(User user){
        if(!super.isAuthorized(user)){
            logger.error("400 Bad Request");
            return false;
        }

        return true;
    }

    @Override
    public boolean reload(User user){
        if(!super.isAuthorized(user)){
            logger.error("400 Bad Request");
            return false;
        }

        return true;
    }

    @Override
    public boolean shutdown(User user){
        if(!super.isAuthorized(user)){
            logger.error("400 Bad Request");
            return false;
        }

        return true;
    }
}