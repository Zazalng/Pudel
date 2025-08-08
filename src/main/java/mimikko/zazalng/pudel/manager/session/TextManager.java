package mimikko.zazalng.pudel.manager.session;

import mimikko.zazalng.pudel.contracts.InteractionType;
import mimikko.zazalng.pudel.entities.interaction.TextEntity;
import mimikko.zazalng.pudel.manager.SessionManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextManager implements SessionInterface<TextEntity, MessageReceivedEvent> {
    private final SessionManager sessionManager;
    private static final Logger logger = LoggerFactory.getLogger(TextManager.class);

    protected TextManager(SessionManager sessionManager){
        this.sessionManager = sessionManager;
    }

    private SessionManager getManager(){
        return this.sessionManager;
    }

    @Override
    public InteractionType getType() {
        return InteractionType.TEXT;
    }

    /**
     * @param e
     * @return
     */
    @Override
    public TextEntity createSession(MessageReceivedEvent e) {
        TextEntity session;
        if(e.isFromGuild()){
            session = new TextEntity(getManager(), e.getChannel(), getManager().getUserManager().getEntity(e.getAuthor()), getManager().getGuildManager().getEntity(e.getGuild())).setEvent(e);
        } else{
            session = new TextEntity(getManager(), e.getChannel(), getManager().getUserManager().getEntity(e.getAuthor())).setEvent(e);
        }
        getManager().registerSession(getType(), getManager().generateSessionKey(getType(), e), session);
        return session;
    }

    /**
     * @param session
     */
    @Override
    public void terminateSession(TextEntity session) {
        session.terminate();
    }
}
