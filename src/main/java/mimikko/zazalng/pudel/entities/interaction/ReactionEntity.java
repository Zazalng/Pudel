package mimikko.zazalng.pudel.entities.interaction;

import mimikko.zazalng.pudel.commands.Command;
import mimikko.zazalng.pudel.contracts.InteractionType;
import mimikko.zazalng.pudel.entities.AbstractInteractionEntity;
import mimikko.zazalng.pudel.entities.GuildEntity;
import mimikko.zazalng.pudel.entities.InteractionEntity;
import mimikko.zazalng.pudel.entities.UserEntity;
import mimikko.zazalng.pudel.manager.SessionManager;
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

public class ReactionEntity extends AbstractInteractionEntity {
    private final Message message;
    private final boolean selfDeleted;
    private final Command issueCommand;
    private final int defaultTimeout;
    private EmojiUnion reactAction;
    private ScheduledFuture<?> timeoutTask;

    public ReactionEntity(SessionManager manager, Message message, TextEntity session, int defaultTimeout , boolean selfDeleted) {
        super(manager,session.getUser(),session.getGuild());
        this.message = message;
        this.selfDeleted = selfDeleted;
        this.issueCommand = session.getCommand();
        this.defaultTimeout = defaultTimeout;
        startTimeout(Duration.ofSeconds(this.defaultTimeout));
    }

    public ReactionEntity(SessionManager manager, Message message, TextEntity session) {
        this(manager, message, session, 60, true);
    }

    // Overloaded constructor with specified defaultTimeout and inferred mayDeletedMessage
    public ReactionEntity(SessionManager manager, Message message, TextEntity session, int defaultTimeout) {
        this(manager, message, session, defaultTimeout, true); // Defaulting mayDeletedMessage to true
    }

    // Overloaded constructor with specified mayDeletedMessage and inferred defaultTimeout
    public ReactionEntity(SessionManager manager, Message message, TextEntity session, boolean selfDeleted) {
        this(manager, message, session, 60, selfDeleted); // Defaulting defaultTimeout to 60
    }

    @Override
    public InteractionEntity asInteractionEntity() {
        return this;
    }

    /**
     * @return
     */
    @Override
    public InteractionType getType() {
        return InteractionType.REACTION;
    }

    protected Message getMessage() {
        return this.message;
    }

    public UserEntity getUser() {
        return super.getUser();
    }

    protected MessageChannelUnion getChannel(){
        return getMessage().getChannel();
    }

    public Command getCommand() {
        return this.issueCommand;
    }

    public EmojiUnion getReactAction(){
        return this.reactAction;
    }

    private ReactionEntity setReactAction(EmojiUnion e){
        this.reactAction = e;
        return this;
    }

    private ReactionEntity setReactAction(MessageReactionAddEvent e){
        setReactAction(e.getEmoji());
        return this;
    }

    private boolean isAuthorizedUser(User user){
        return super.getUserJDA(getUser())==user;
    }

    private boolean isAuthorizedUser(MessageReactionAddEvent e){
        return isAuthorizedUser(e.getUser());
    }

    private ReactionEntity startTimeout(Duration duration) {
        this.timeoutTask = Executors.newScheduledThreadPool(1)
                .schedule(this::terminate, duration.toMillis(), TimeUnit.MILLISECONDS);
        return this;
    }

    private ReactionEntity resetTimeout() {
        if (timeoutTask != null) {
            timeoutTask.cancel(false);
        }
        startTimeout(Duration.ofSeconds(this.defaultTimeout)); // Refresh timeout (example)
        return this;
    }

    private ReactionEntity cancelTimeout(){
        if(timeoutTask != null){
            timeoutTask.cancel(false);
        }
        return this;
    }

    public void terminate() {
        this.cancelTimeout().getManager().unregisterInteraction(this, this.selfDeleted);
    }

    @Override
    public void execute(Object args){
        if(!(args instanceof MessageReactionAddEvent)) return;

        MessageReactionAddEvent e = (MessageReactionAddEvent) args;
        if(isAuthorizedUser(e)){
            resetTimeout().setReactAction(e).getCommand().execute(this,null);
        } else{
            e.retrieveMessage().complete().removeReaction(e.getEmoji(),e.getUser()).queue();
        }
    }

    public String getReact(){
        if(getReactAction().getType() == Emoji.Type.UNICODE){
            return getReactAction().asUnicode().getAsCodepoints();
        } else{
            return getReactAction().asCustom().getFormatted();
        }
    }
}
