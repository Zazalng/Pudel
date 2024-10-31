package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.entities.GuildEntity;
import mimikko.zazalng.pudel.entities.SessionEntity;
import mimikko.zazalng.pudel.entities.UserEntity;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static mimikko.zazalng.pudel.utility.StringUtility.stringFormat;

public class SessionManager implements Manager{
    private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);
    protected final PudelWorld pudelWorld;
    private final Map<String, SessionEntity> sessions;

    public SessionManager(PudelWorld pudelWorld){
        this.pudelWorld = pudelWorld;
        this.sessions = new HashMap<>();
    }

    public SessionEntity getSession(MessageReceivedEvent e) {
        UserEntity user = getPudelWorld().getUserManager().getUserEntity(e.getAuthor());
        GuildEntity guild = getPudelWorld().getGuildManager().getGuildEntity(e.getGuild());
        MessageChannelUnion channelIssue = e.getChannel();
        String sessionKey = createSessionKey(e);

        return this.sessions.compute(sessionKey, (k, v) -> {
            if (v == null || v.getCommand() == null) {
                logger.info(stringFormat("[Create Session] %s",sessionKey));
                return new SessionEntity(this, user, guild, channelIssue).setEvent(e);
            }
            logger.debug(stringFormat("[Active Session] %s",sessionKey));
            return v.setEvent(e);
        });
    }

    public SessionManager sessionTerminate(SessionEntity session) {
        Iterator<Map.Entry<String, SessionEntity>> iterator = this.sessions.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, SessionEntity> entry = iterator.next();
            if (entry.getValue().equals(session)) {
                logger.info(String.format("[Terminate Session] %s", entry.getKey()));
                iterator.remove();
            }
        }

        return this;
    }

    public SessionManager sessionEnd(SessionEntity session){
        session.getSessionCollector().stream().peek(messageReceivedEvent -> messageReceivedEvent.getMessage().delete().queue());
        sessionTerminate(session);
        return this;
    }

    private String createSessionKey(MessageReceivedEvent e) {
        return e.getGuild().getId() + ":" + e.getChannel().getId() + ":" + e.getAuthor().getId();
    }

    @Override
    public PudelWorld getPudelWorld() {
        return this.pudelWorld;
    }

    @Override
    public SessionManager initialize() {
        return this;
    }

    @Override
    public SessionManager reload() {
        return this;
    }

    @Override
    public void shutdown() {

    }
}
