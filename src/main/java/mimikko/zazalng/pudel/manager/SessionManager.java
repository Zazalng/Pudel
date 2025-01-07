package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.entities.GuildEntity;
import mimikko.zazalng.pudel.entities.SessionEntity;
import mimikko.zazalng.pudel.entities.UserEntity;
import mimikko.zazalng.pudel.utility.StringUtility;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static mimikko.zazalng.pudel.utility.StringUtility.stringFormat;

public class SessionManager extends AbstractManager{
    private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);
    private final Map<String, SessionEntity> sessions;

    protected SessionManager(PudelWorld pudelWorld){
        super(pudelWorld);
        this.sessions = new HashMap<>();
    }

    public SessionEntity getSession(MessageReceivedEvent e) {
        UserEntity user = getUserManager().getUserEntity(e.getAuthor());
        GuildEntity guild = getGuildManager().getGuildEntity(e.getGuild());
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
        logger.info(StringUtility.stringFormat("[Terminate Session] %s", createSessionKey(this.sessions.remove(createSessionKey(session)))));
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

    private String createSessionKey(SessionEntity session){
        return session.getGuild().getJDA().getId() + ":" + session.getChannel().getId() + ":" + session.getUser().getJDA().getId();
    }

    @Override
    public SessionManager initialize() {
        return this;
    }

    @Override
    public void reload() {

    }

    @Override
    public void shutdown() {

    }
}
