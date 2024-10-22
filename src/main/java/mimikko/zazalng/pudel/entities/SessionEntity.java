package mimikko.zazalng.pudel.entities;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.commands.Command;
import mimikko.zazalng.pudel.commands.CommandState;
import mimikko.zazalng.pudel.contracts.BaseCommandState;
import mimikko.zazalng.pudel.manager.SessionManager;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessionEntity {
    protected final SessionManager sessionManager;
    private final UserEntity user;
    private final GuildEntity guild;
    private final MessageChannelUnion channel;
    private final List<MessageReceivedEvent> sessionCollector = new ArrayList<>();
    private Command command;
    private final Map<String, Object> promptCollection = new HashMap<>();
    private CommandState sessionState;

    public SessionEntity(SessionManager sessionManager, UserEntity user, GuildEntity guild, MessageChannelUnion channelIssue) {
        this.sessionManager = sessionManager;
        this.user = user;
        this.guild = guild;
        this.channel = channelIssue;
        this.sessionState = BaseCommandState.INIT;
    }

    public SessionEntity setState(CommandState state) {
        this.sessionState = state;
        return this;
    }

    public CommandState getState() {
        return this.sessionState;
    }

    public SessionEntity clearPromptSession() {
        for (int i = 1; i < sessionCollector.size(); i++) {
            sessionCollector.get(i).getMessage().delete().queue();
        }
        return this;
    }

    public SessionEntity addData(String key, Object value) {
        promptCollection.put(key, value);
        return this;
    }

    public Object getData(String key, boolean delObject) {
        return delObject ? promptCollection.remove(key) : promptCollection.get(key);
    }

    public SessionEntity setCommand(Command command){
        this.command = command;
        return this;
    }

    public Command getCommand(){
        return this.command;
    }
    // Getters for entities
    public PudelWorld getPudelWorld(){
        return sessionManager.getPudelWorld();
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

    public SessionEntity execute(String args){
        getCommand().execute(this,args);
        return this;
    }
}