package mimikko.zazalng.pudel.entities;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.commands.Command;
import mimikko.zazalng.pudel.manager.CommandManager;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

import java.util.HashMap;
import java.util.Map;

public class SessionEntity {
    protected final CommandManager commandManager;
    private final UserEntity user;
    private final GuildEntity guild;
    private final MessageChannelUnion channel;
    private final Command command;
    private final Map<String, String> promptCollection = new HashMap<>();
    private String sessionState;

    public SessionEntity(CommandManager commandManager, UserEntity user, GuildEntity guild, MessageChannelUnion channelIssue, Command command) {
        this.commandManager = commandManager;
        this.user = user;
        this.guild = guild;
        this.channel = channelIssue;
        this.command = command;
        this.sessionState = "INIT";
    }

    public void setState(String sessionState) {
        this.sessionState = sessionState;
    }

    public String getState() {
        return this.sessionState;
    }

    public void addData(String sessionState, String value) {
        promptCollection.put(sessionState, value);
    }

    public Object getData(String sessionState) {
        return promptCollection.get(sessionState);
    }
    // Getters for entities
    public PudelWorld getPudelWorld(){
        return commandManager.getPudelWorld();
    }

    public UserEntity getUser() {
        return user;
    }

    public GuildEntity getGuild() {
        return guild;
    }

    public MessageChannelUnion getChannel() {
        return channel;
    }

    public Command getCommand() {
        return command;
    }

    public void execute(String args){
        command.execute(this,args);
    }
}