package mimikko.zazalng.pudel.handlers;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.entities.SessionEntity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class EventHandler extends ListenerAdapter{
    protected final PudelWorld pudelWorld;

    public EventHandler(PudelWorld pudelWorld) {
        this.pudelWorld = pudelWorld;
    }

    @Override
    public void onReady(@NotNull ReadyEvent e) {
        this.pudelWorld.getPudelManager().setPudelEntity(pudelWorld.getUserManager().getUserEntity(e.getJDA().getSelfUser()));
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        if (e.getChannelType().isGuild() && !e.getChannel().canTalk()) {
            return;
        }

        if(!e.getAuthor().isBot()){
            /*
                Still Possible to implement command for direct message interacting
                - A possible thing that can happen (from what I have discovery)
                1. Message from Guild asTextChannel
                2. Message from Guild asVoiceChannel
                3. Message from Direct Message
             */

            //e.getChannel().sendTyping().queue();

            SessionEntity session = this.pudelWorld.getSessionManager().getSession(e);
            this.pudelWorld.getCommandManager().handleCommand(session, e);
        }
    }
}