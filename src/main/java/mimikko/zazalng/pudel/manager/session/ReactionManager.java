package mimikko.zazalng.pudel.manager.session;

import mimikko.zazalng.pudel.contracts.InteractionType;
import mimikko.zazalng.pudel.entities.interaction.ReactionEntity;
import mimikko.zazalng.pudel.entities.interaction.TextEntity;
import mimikko.zazalng.pudel.manager.SessionManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReactionManager implements SessionInterface<ReactionEntity, MessageReactionAddEvent> {
    private final SessionManager sessionManager;
    private static final Logger logger = LoggerFactory.getLogger(ReactionManager.class);

    protected ReactionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public SessionManager getManager(){
        return this.sessionManager;
    }

    public void getReaction(MessageReactionAddEvent e){
        e.retrieveMessage().queue(message -> {
            ReactionEntity interaction = this.reactionList.get(message);
            if (interaction != null) {
                interaction.execute(e);
            }
        }, throwable -> {});
    }

    public ReactionEntity newReaction(Message message, TextEntity session){
        return this.reactionList.computeIfAbsent(message, Entity -> new ReactionEntity(getManager(), message, session));
    }

    public ReactionEntity newReaction(Message message, TextEntity session, int setTimeout){
        return this.reactionList.computeIfAbsent(message, Entity -> new ReactionEntity(getManager(), message, session, setTimeout));
    }

    public ReactionEntity newReaction(Message message, TextEntity session, boolean selfDeleted){
        return this.reactionList.computeIfAbsent(message, Entity -> new ReactionEntity(getManager(), message, session, selfDeleted));
    }

    public ReactionEntity newReaction(Message message, TextEntity session, int setTimeout, boolean selfDeleted){
        return this.reactionList.computeIfAbsent(message, Entity -> new ReactionEntity(getManager(), message, session, setTimeout, selfDeleted));
    }

    public ReactionManager unregisterInteraction(ReactionEntity interaction, boolean selfDeleted) {
        interaction.getMessage().clearReactions().queue(unused -> {
            reactionList.remove(interaction.getMessage());
            if(selfDeleted) interaction.getMessage().delete().queue();
        });
        return this;
    }

    /**
     * @return
     */
    @Override
    public InteractionType getType() {
        return InteractionType.REACTION;
    }

    /**
     * @param event
     * @return
     */
    @Override
    public ReactionEntity createSession(MessageReactionAddEvent event) {
        return null;
    }

    /**
     * @param session
     */
    @Override
    public void terminateSession(ReactionEntity session) {

    }
}
