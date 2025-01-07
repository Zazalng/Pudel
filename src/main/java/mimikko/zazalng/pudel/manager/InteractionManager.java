package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.entities.InteractionEntity;
import mimikko.zazalng.pudel.entities.SessionEntity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

import java.util.HashMap;
import java.util.Map;

public class InteractionManager extends AbstractManager{
    private final Map<Message,InteractionEntity> interactionList;

    protected InteractionManager(PudelWorld pudelWorld) {
        super(pudelWorld);
        this.interactionList = new HashMap<>();
    }

    public void getInteraction(MessageReactionAddEvent e){
        e.retrieveMessage().queue(message -> {
            InteractionEntity interaction = this.interactionList.get(message);
            if (interaction != null) {
                interaction.execute(e);
            }
        }, throwable -> {

        });
    }

    public InteractionEntity newInteraction(Message message, SessionEntity session){
        return this.interactionList.computeIfAbsent(message, Entity -> new InteractionEntity(this, message, session));
    }

    public InteractionEntity newInteraction(Message message, SessionEntity session, int setTimeout){
        return this.interactionList.computeIfAbsent(message, Entity -> new InteractionEntity(this, message, session, setTimeout));
    }

    public InteractionManager unregisterInteraction(InteractionEntity interaction) {
        interaction.getMessage().clearReactions().queue();
        interactionList.remove(interaction.getMessage());
        return this;
    }

    /**
     * @return
     */
    @Override
    public InteractionManager initialize() {
        return this;
    }

    /**
     * @return
     */
    @Override
    public void reload() {

    }

    /**
     *
     */
    @Override
    public void shutdown() {

    }
}
