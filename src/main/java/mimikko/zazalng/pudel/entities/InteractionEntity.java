package mimikko.zazalng.pudel.entities;

import mimikko.zazalng.pudel.commands.Command;
import mimikko.zazalng.pudel.manager.AbstractManager;
import mimikko.zazalng.pudel.manager.InteractionManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.EmojiUnion;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class InteractionEntity {
    protected final InteractionManager interactionManager;
    private final Message message;
    private final boolean selfDeleted;
    private final UserEntity interactor;
    private final Command issueCommand;
    private final int defaultTimeout;
    private EmojiUnion reactAction;
    private ScheduledFuture<?> timeoutTask;

    private InteractionEntity(InteractionManager manager, Message message, UserEntity interactor, Command issueCommand, int defaultTimeout , boolean selfDeleted) {
        this.interactionManager = manager;
        this.message = message;
        this.selfDeleted = selfDeleted;
        this.interactor = interactor;
        this.issueCommand = issueCommand;
        this.defaultTimeout = defaultTimeout;
        startTimeout(Duration.ofSeconds(this.defaultTimeout));
    }

    public InteractionEntity(InteractionManager manager, Message message, SessionEntity session) {
        this(manager, message, session.getUser(), session.getCommand(), 60, true);
    }

    // Overloaded constructor with specified defaultTimeout and inferred mayDeletedMessage
    public InteractionEntity(InteractionManager manager, Message message, SessionEntity session, int defaultTimeout) {
        this(manager, message, session.getUser(), session.getCommand(), defaultTimeout, true); // Defaulting mayDeletedMessage to true
    }

    // Overloaded constructor with specified mayDeletedMessage and inferred defaultTimeout
    public InteractionEntity(InteractionManager manager, Message message, SessionEntity session, boolean selfDeleted) {
        this(manager, message, session.getUser(), session.getCommand(), 60, selfDeleted); // Defaulting defaultTimeout to 60
    }

    // Overloaded constructor with specified defaultTimeout and mayDeletedMessage
    public InteractionEntity(InteractionManager manager, Message message, SessionEntity session, int defaultTimeout, boolean selfDeleted) {
        this(manager, message, session.getUser(), session.getCommand(), defaultTimeout, selfDeleted);
    }

    public AbstractManager getManager() {
        return this.interactionManager;
    }

    public Message getMessage() {
        return this.message;
    }

    public GuildEntity getGuild(){
        return getManager().getGuildManager().getGuildEntity(getMessage().getGuild());
    }

    public UserEntity getUser() {
        return this.interactor;
    }

    public MessageChannelUnion getChannel(){
        return getMessage().getChannel();
    }

    private Command getCommand() {
        return this.issueCommand;
    }

    public EmojiUnion getReactAction(){
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
        return getUser().getJDA()==user;
    }

    private boolean isAuthorizedUser(MessageReactionAddEvent e){
        return isAuthorizedUser(e.getUser());
    }

    private InteractionEntity startTimeout(Duration duration) {
        this.timeoutTask = Executors.newScheduledThreadPool(1)
                .schedule(this::terminate, duration.toMillis(), TimeUnit.MILLISECONDS);
        return this;
    }

    private InteractionEntity resetTimeout() {
        if (timeoutTask != null) {
            timeoutTask.cancel(false);
        }
        startTimeout(Duration.ofSeconds(this.defaultTimeout)); // Refresh timeout (example)
        return this;
    }

    private InteractionEntity cancelTimeout(){
        if(timeoutTask != null){
            timeoutTask.cancel(false);
        }
        return this;
    }

    public void terminate() {
        this.cancelTimeout().interactionManager.unregisterInteraction(this, this.selfDeleted);
    }

    public InteractionEntity execute(MessageReactionAddEvent e){
        if(isAuthorizedUser(e)){
            resetTimeout().setReactAction(e).getCommand().execute(this);
        } else{
            e.retrieveMessage().complete().removeReaction(e.getEmoji(),e.getUser()).queue();
        }
        return this;
    }

    public String getReact(){
        if(getReactAction().getType() == Emoji.Type.UNICODE){
            return getReactAction().asUnicode().getAsCodepoints();
        } else{
            return getReactAction().asCustom().getFormatted();
        }
    }
}
