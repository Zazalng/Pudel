package mimikko.zazalng.pudel.handlers;

import mimikko.zazalng.pudel.entities.SessionEntity;
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

        if(e.getAuthor().isBot()){
            return;
        }

        SessionEntity session = this.pudel.getSessionManager().getSession(e);
            /*
                Still Possible to implement command for direct message interacting
                - A possible thing that can happen (from what I have discovery)
                1. Message from Guild asTextChannel
                2. Message from Guild asVoiceChannel
                3. Message from Direct Message
             */

            //e.getChannel().sendTyping().queue();


        this.pudel.getCommandManager().handleCommand(session, e);
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent e){
        if(!e.getUser().isBot()){
            this.pudel.getInteractionManager().getInteraction(e);
        }
    }
}