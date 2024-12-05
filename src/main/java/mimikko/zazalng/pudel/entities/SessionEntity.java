package mimikko.zazalng.pudel.entities;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.commands.Command;
import mimikko.zazalng.pudel.manager.SessionManager;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessionEntity{
    protected final SessionManager sessionManager;
    private final UserEntity user;
    private final GuildEntity guild;
    private final MessageChannelUnion channel;
    private final List<MessageReceivedEvent> sessionCollector = new ArrayList<>();
    private final Map<String, Object> objectCollector = new HashMap<>();
    private Command command;
    private MessageReceivedEvent event;

    public SessionEntity(SessionManager sessionManager, UserEntity user, GuildEntity guild, MessageChannelUnion channelIssue) {
        this.sessionManager = sessionManager;
        this.user = user;
        this.guild = guild;
        this.channel = channelIssue;
    }

    public List<MessageReceivedEvent> getSessionCollector() {
        return sessionCollector;
    }

    public SessionEntity addData(String key, Object value) {
        objectCollector.put(key, value);
        return this;
    }

    public Object getData(String key, boolean delObject) {
        return delObject ? objectCollector.remove(key) : objectCollector.get(key);
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

    private MessageReceivedEvent latestEvent() {
        return this.event;
    }

    public SessionEntity setEvent(MessageReceivedEvent e) {
        if (this.event != null) {
            this.sessionCollector.add(latestEvent());
        }
        this.event = e;
        return this;
    }
}