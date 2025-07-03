package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.contracts.InteractionType;
import mimikko.zazalng.pudel.entities.InteractionEntity;
import mimikko.zazalng.pudel.manager.session.SessionInterface;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class SessionManager extends AbstractManager{
    private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);
    private final Map<InteractionType, SessionInterface<?, ?>> handlers = new EnumMap<>(InteractionType.class);
    private final EnumMap<InteractionType, Map<String, InteractionEntity>> sessionMap = new EnumMap<>(InteractionType.class);

    protected SessionManager(PudelWorld pudelWorld) {
        super(pudelWorld);
        for (InteractionType type : InteractionType.values()) {
            sessionMap.put(type, new HashMap<>());
        }
    }

    protected <T extends InteractionEntity> void registerHandler(SessionInterface<T, ?> handler) {
        handlers.put(handler.getType(), handler);
    }

    public <E extends Event> SessionManager dispatch(InteractionType type, E event) {
        SessionInterface<?, E> handler = (SessionInterface<?, E>) handlers.get(type);
        if (handler != null) {
            handler.createSession(event);
        } else {
            logger.warn("No handler registered for interaction type: {}", type);
        }
        return this;
    }

    public <T extends InteractionEntity> T getSession(InteractionType type, Object event) {
        return (T) sessionMap.get(type).get(generateSessionKey(type, event));
    }

    public String generateSessionKey(InteractionType type, Object event) {
        return switch (type) {
            case TEXT -> {
                MessageReceivedEvent e = (MessageReceivedEvent) event;
                yield e.getAuthor().getId() + ":" + e.getChannel().getId();
            }
            case REACTION -> {
                MessageReactionAddEvent e = (MessageReactionAddEvent) event;
                yield e.getMessageId();
            }
            case SLASH, BUTTON, MODAL, MENU_CONTEXT, MENU_SELECTED, AUTOCOMPLETE -> {
                GenericInteractionCreateEvent e = (GenericInteractionCreateEvent) event;
                yield e.getInteraction().getId();  // Or use getMessageId() if context is message-bound
            }
        };
    }

    public void registerSession(InteractionType type, String key, InteractionEntity entity) {
        sessionMap.get(type).put(key, entity);
    }

    public void terminateSession(InteractionType type, String key) {
        InteractionEntity entity = sessionMap.get(type).remove(key);
        if (entity != null) {
            entity.terminate();
        }
    }

    @Override
    public boolean initialize(User user){
        if(!isAuthorized(user)){
            logger.error("400 Bad Request");
            return false;
        }

        return true;
    }

    @Override
    public boolean reload(User user){
        if(!isAuthorized(user)){
            logger.error("400 Bad Request");
            return false;
        }

        return true;
    }

    @Override
    public boolean shutdown(User user){
        if(!isAuthorized(user)){
            logger.error("400 Bad Request");
            return false;
        }

        return true;
    }
}
