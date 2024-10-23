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
import java.util.Map;

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
                return new SessionEntity(this, user, guild, channelIssue);
            }
            return v;
        });

    }

    public SessionManager sessionEnd(SessionEntity session){
        this.sessions.remove(session);
        return this;
    }

    private String createSessionKey(MessageReceivedEvent e) {
        return e.getAuthor().getId() + ":" + e.getGuild().getId() + ":" + e.getChannel().getId();
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
    public SessionManager shutdown() {
        this.sessions.clear();
        return this;
    }
}
