package mimikko.zazalng.pudel.handlers;

import mimikko.zazalng.pudel.contracts.InteractionType;
import mimikko.zazalng.pudel.manager.PudelManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class EventHandler extends ListenerAdapter{
    protected final PudelManager pudel;

    public EventHandler(PudelManager pudel) {
        this.pudel = pudel;
    }

    @Override
    public void onReady(@NotNull ReadyEvent e) {
        this.pudel.setPudelEntity(e.getJDA().getSelfUser());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        if (e.getChannelType().isGuild() && !e.getChannel().canTalk()) {
            return;
        }

        if(e.getAuthor().isBot()) {
            return;
        }

        this.pudel.getSessionManager().dispatch(InteractionType.TEXT,e).getCommandManager().handleCommand(this.pudel.getSessionManager().getSession(InteractionType.TEXT,e), e);
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent e){
        if(e.getUser().isBot()){
            return;
        }

        this.pudel.getSessionManager().getSession(InteractionType.REACTION, e);
    }
}