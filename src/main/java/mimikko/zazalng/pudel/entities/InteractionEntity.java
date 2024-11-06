package mimikko.zazalng.pudel.entities;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.commands.Command;
import mimikko.zazalng.pudel.manager.InteractionManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.EmojiUnion;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class InteractionEntity {
    protected final InteractionManager interactionManager;
    private final Message message;
    private final UserEntity interactor;
    private final Command issueCommand;
    private final int defaultTimeout;
    private EmojiUnion reactAction;
    private ScheduledFuture<?> timeoutTask;

    private InteractionEntity(InteractionManager manager, Message message, UserEntity interactor, Command issueCommand, int defaultTimeout) {
        this.interactionManager = manager;
        this.message = message;
        this.interactor = interactor;
        this.issueCommand = issueCommand;
        this.defaultTimeout = defaultTimeout;
    }

    public InteractionEntity(InteractionManager manager, Message message, SessionEntity session) {
        this(manager, message, session.getUser(), session.getCommand(), 60);
    }

    public InteractionEntity(InteractionManager manager, Message message, SessionEntity session, int timeout) {
        this(manager, message, session.getUser(), session.getCommand(), timeout);
    }

    public PudelWorld getPudelWorld() {
        return this.interactionManager.getPudelWorld();
    }

    public Message getMessage() {
        return this.message;
    }

    private UserEntity getInteractor() {
        return this.interactor;
    }

    private Command getCommand() {
        return this.issueCommand;
    }

    private EmojiUnion getReactAction(){
        return this.reactAction;
    }

    private InteractionEntity setReactAction(EmojiUnion e){
        this.reactAction = e;
        return this;
    }

    private InteractionEntity setReactAction(MessageReactionAddEvent e){
        setReactAction(e.getEmoji());
        return this;
    }

    private boolean isAuthorizedUser(User user){
        return getInteractor().getJDA()==user;
    }

    private boolean isAuthorizedUser(MessageReactionAddEvent e){
        return isAuthorizedUser(e.getUser());
    }

    public InteractionEntity startTimeout(Duration duration) {
        this.timeoutTask = Executors.newScheduledThreadPool(1)
                .schedule(this::terminate, duration.toMillis(), TimeUnit.MILLISECONDS);
        return this;
    }

    public InteractionEntity resetTimeout() {
        if (timeoutTask != null) {
            timeoutTask.cancel(false);
        }
        startTimeout(Duration.ofSeconds(this.defaultTimeout)); // Refresh timeout (example)
        return this;
    }

    public void terminate() {
        this.interactionManager.unregisterInteraction(this);
    }

    public InteractionEntity execute(MessageReactionAddEvent e){
        if(isAuthorizedUser(e)){
            resetTimeout().setReactAction(e).getCommand().execute(this);
        }
        return this;
    }

    public String getReact(){
        return getReactAction().getName();
    }
}
