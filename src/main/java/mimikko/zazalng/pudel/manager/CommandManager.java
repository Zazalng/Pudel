package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.commands.Command;
import mimikko.zazalng.pudel.commands.music.*;
import mimikko.zazalng.pudel.commands.settings.*;
import mimikko.zazalng.pudel.entities.GuildEntity;
import mimikko.zazalng.pudel.entities.SessionEntity;
import mimikko.zazalng.pudel.entities.UserEntity;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

public class CommandManager implements Manager {
    protected final PudelWorld pudelWorld;
    private final Map<String, Command> commands;
    private final Map<String, SessionEntity> sessions;

    public CommandManager(PudelWorld pudelWorld) {
        this.pudelWorld = pudelWorld;
        this.commands = new HashMap<>();
        this.sessions = new HashMap<>();

        //music category
        loadCommand("loop", new MusicLoop());
        loadCommand("play", new MusicPlay());
        loadCommand("np", new MusicPlaying());
        loadCommand("shuffle", new MusicShuffle());
        loadCommand("skip", new MusicSkip());
        loadCommand("stop", new MusicStop());
        //settings category
        loadCommand("prefix", new GuildPrefix());
    }

    public void loadCommand(String name, Command command) {
        commands.put(name, command);
    }

    public void reloadCommand(String name) {
        commands.remove(name);
    }

    public void handleCommand(MessageReceivedEvent e) {
        String prefix = this.pudelWorld.getGuildManager().getGuildEntity(e.getGuild()).getPrefix();
        if (e.getMessage().getContentRaw().startsWith(prefix)) {
            String[] parts = e.getMessage().getContentRaw().substring(prefix.length()).split(" ", 2);
            String commandName = parts[0];
            String args = parts.length > 1 ? parts[1] : "";

            if (commandName.equalsIgnoreCase("help")) {
                if (args.isEmpty()) {
                    // Show the list of commands
                    StringBuilder helpMessage = new StringBuilder("Available commands:\n");
                    commands.forEach((name, command) -> helpMessage.append("`").append(prefix).append(name).append("` - ").append(command.getDescription()).append("\n"));
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
                if (command != null) {
                    SessionEntity session = getSession(e, command);
                    session.getCommand().execute(session, args);
                } else {
                    //e.getChannel().sendMessage("Unknown command!").queue();
                }
            }
        } else {
            //not being called
            return;
        }
    }

    public SessionEntity getSession(MessageReceivedEvent e, Command command) {
        UserEntity user = this.pudelWorld.getUserManager().getUserEntity(e.getAuthor());
        GuildEntity guild = this.pudelWorld.getGuildManager().getGuildEntity(e.getGuild());
        MessageChannelUnion channelIssue = e.getChannel();

        // Create a unique session key using userId, guildId, and channelId
        String sessionKey = createSessionKey(e.getAuthor().getId(), e.getGuild().getId(), e.getChannel().getId());

        return this.sessions.computeIfAbsent(sessionKey, k -> new SessionEntity(this, user, guild, channelIssue, command));
    }

    // Remove session after it ends
    public void endSession(String userId, String guildId, String channelId) {
        String sessionKey = createSessionKey(userId, guildId, channelId);
        this.sessions.remove(sessionKey);
    }

    private String createSessionKey(String userId, String guildId, String channelId) {
        return userId + ":" + guildId + ":" + channelId;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void reload() {

    }

    @Override
    public void shutdown() {
        this.sessions.clear();
    }
}