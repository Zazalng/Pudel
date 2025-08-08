package mimikko.zazalng.pudel.handlers;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.contracts.InteractionType;
import mimikko.zazalng.pudel.contracts.ManagersType;
import mimikko.zazalng.pudel.entities.interaction.TextEntity;
import mimikko.zazalng.pudel.manager.PudelManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;

public class EventHandler extends ListenerAdapter{
    protected final PudelWorld pudelWorld;
    private final PudelManager pudel;

    public EventHandler(PudelWorld pudelWorld) {
        this.pudelWorld = pudelWorld;
        pudel = this.pudelWorld.getManagerFactory().getManager(ManagersType.PUDEL);
    }

    @Override
    public void onReady(@NotNull ReadyEvent e) {
        pudel.setPudelEntity(e.getJDA().getSelfUser());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        if (!e.getChannel().canTalk()) {
            e.getJDA().openPrivateChannelById(e.getAuthor().getId()).queue(t -> t.sendMessage("Pudel can't access to that Channel").queue());
            return;
        }

        if(e.getAuthor().isBot()) {
            return;
        }

        this.pudel.getSessionManager().dispatch(InteractionType.TEXT,e); // Create Token Session
        TextEntity t = pudel.getSessionManager().getSession(InteractionType.TEXT,e);
        pudel.getCommandManager().handleCommand()
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