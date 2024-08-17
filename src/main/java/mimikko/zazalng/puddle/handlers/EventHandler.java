package mimikko.zazalng.puddle.handlers;

import mimikko.zazalng.puddle.entities.GuildEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class EventHandler extends ListenerAdapter{
    protected final Map<Guild, GuildEntity> guildsEntity;

    public EventHandler() {
        this.guildsEntity = new HashMap<>();
    }

    public GuildEntity getGuildEntity(Guild guildEvent){
        return this.guildsEntity.computeIfAbsent(guildEvent, guild -> new GuildEntity(guildEvent));
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        if(!e.getAuthor().isBot()){
            //From {guildName} in {channelName} by {userName} said: {contentRaw}
            String fullRespond = "From "+e.getGuild().getName()+" in "+e.getGuildChannel().getName()+" by "+e.getAuthor().getName()+" said: \n"+e.getMessage().getContentRaw();
            System.out.println(fullRespond);

            new CommandHandler(getGuildEntity(e.getGuild()), e);
        }
    }
}