package mimikko.zazalng.pudel.handlers;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.entities.GuildEntity;
import mimikko.zazalng.pudel.entities.UserEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;

public class EventHandler extends ListenerAdapter{
    protected final PudelWorld pudelWorld;

    public EventHandler(PudelWorld pudelWorld) {
        this.pudelWorld = pudelWorld;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        if(!e.getAuthor().isBot()){
            //From {guildName} in {channelName} by {userName} said: {contentRaw}
            String fullRespond = "From "+e.getGuild().getName()+" in "+e.getGuildChannel().getName()+" by "+e.getAuthor().getName()+" said: \n"+e.getMessage().getContentRaw();
            System.out.println(fullRespond);

            //e.getChannel().sendTyping().queue();
            new CommandHandler(pudelWorld.getGuildManager().getGuildEntity(e.getGuild()), pudelWorld.getUserManager().getUserEntity(e.getAuthor()), e);
        }
    }
}