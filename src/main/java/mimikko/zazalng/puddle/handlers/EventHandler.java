package mimikko.zazalng.puddle.handlers;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventHandler extends ListenerAdapter{

    public void onMessageReceived(MessageReceivedEvent e){
        if(e.getAuthor().isBot()){
            return;
        } else {
            //From {guildName} in {channelName} by {userName} said: {contentRaw}
            String fullRespond = "From "+e.getGuild().getName()+" in "+e.getGuildChannel().getName()+" by "+e.getAuthor().getName()+" said: "+e.getMessage().getContentRaw();
            System.out.println(fullRespond);
        }
    }
}
