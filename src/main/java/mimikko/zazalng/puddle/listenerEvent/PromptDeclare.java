package mimikko.zazalng.puddle.listenerEvent;

import mimikko.zazalng.puddle.PuddleWorld;
import mimikko.zazalng.puddle.handlers.EventHandler;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PromptDeclare extends EventHandler{
    public PromptDeclare(PuddleWorld puddleWorld){
        super(puddleWorld);
    }
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return; // Ignore messages from other bots
        }
        String preText = event.getAuthor().getEffectiveName()+": "+event.getMessage().getContentRaw();
        if (event.isFromType(ChannelType.PRIVATE))
        {
            preText = "[PM] "+preText;
            System.out.println(preText);
        }
        else
        {
            preText = event.getGuild().getName()+"@"+event.getChannel().getName()+" By "+ preText;
            System.out.println(preText);
            //Homework: Command Varidator Process String
            
            if(event.getMessage().getContentRaw().equalsIgnoreCase("greet")){
                event.getChannel().sendMessage("Hello!"+" I'm from "+event.getGuild().getName()).queue();
            }
            
            if(!event.getAuthor().getId().equals("152140348980723712")){
                return;
            }
            event.getChannel().sendMessage("Puddle's Wolrd: "+String.valueOf(super.puddleWorld.getWorldStatus())
            +"\nMessage to: "+event.getChannel().getId()
            +"\nAt Guild: "+event.getGuild().getId()
            +"\n\nMessage Raw Prefix: "
            +preText).queue();
        }
    }
    
    public boolean isCommand(String preText){
        return false;
    }
}
