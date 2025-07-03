package mimikko.zazalng.pudel.entities.interaction;

import mimikko.zazalng.pudel.commands.Command;
import mimikko.zazalng.pudel.contracts.InteractionType;
import mimikko.zazalng.pudel.entities.AbstractInteractionEntity;
import mimikko.zazalng.pudel.entities.GuildEntity;
import mimikko.zazalng.pudel.entities.InteractionEntity;
import mimikko.zazalng.pudel.entities.UserEntity;
import mimikko.zazalng.pudel.manager.SessionManager;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextEntity extends AbstractInteractionEntity {
    private final MessageChannelUnion channel;
    private final List<MessageReceivedEvent> sessionCollector = new ArrayList<>();
    private final Map<String, Object> objectCollector = new HashMap<>();
    private Command command;
    private MessageReceivedEvent event;

    public TextEntity(SessionManager manager,MessageChannelUnion channel, UserEntity user, GuildEntity guild){
        super(manager, user, guild);
        this.channel = channel;
    }

    public TextEntity(SessionManager manager, MessageChannelUnion channel, UserEntity user) {
        this(manager, channel, user, null);
    }

    @Override
    public InteractionEntity asInteractionEntity(){
        return this;
    }

    @Override
    public void terminate(){
        getManager().sessionEnd(this);
    }

    public List<MessageReceivedEvent> getSessionCollector() {
        return sessionCollector;
    }

    public TextEntity addData(String key, Object value) {
        objectCollector.put(key, value);
        return this;
    }

    public Object getData(String key, boolean delObject) {
        return delObject ? objectCollector.remove(key) : objectCollector.get(key);
    }

    public TextEntity setCommand(Command command){
        this.command = command;
        return this;
    }

    public Command getCommand(){
        return this.command;
    }

    /**
     * @return
     */
    @Override
    public InteractionType getType() {
        return InteractionType.TEXT;
    }

    public UserEntity getUser() {
        return super.getUser();
    }

    public GuildEntity getGuild() {
        return super.getGuild();
    }

    private MessageChannelUnion getChannel() {
        return channel;
    }

    /**
     *
     */
    @Override
    public void execute(Object args) {
        if(!(args instanceof String)) return;

        getCommand().execute(this, (String) args);
    }

    private MessageReceivedEvent latestEvent() {
        return this.event;
    }

    public TextEntity setEvent(MessageReceivedEvent e) {
        if (this.event != null) {
            this.sessionCollector.add(latestEvent());
        }
        this.event = e;
        return this;
    }
}