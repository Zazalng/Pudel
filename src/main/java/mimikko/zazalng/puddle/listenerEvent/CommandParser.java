/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mimikko.zazalng.puddle.listenerEvent;

import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import mimikko.zazalng.puddle.Message;

/**
 *
 * @author User
 */
public class CommandParser extends ListenerAdapter{
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return; // Ignore messages from other bots
        }
        
        if (event.isFromType(ChannelType.PRIVATE))
        {
            System.out.printf("[PM] %s: %s\n", event.getAuthor().getName(),
                                    event.getMessage().getContentDisplay());
        }
        else
        {
            System.out.printf("[%s][%s] %s: %s\n", event.getGuild().getName()
                    ,event.getChannel().getName()
                    , event.getMember().getEffectiveName()
                    ,event.getMessage().getContentDisplay()
            );
            
            System.out.println(
                    //event.getMessage().
            );
            
            System.out.println(
                    event.getMessage().getContentDisplay().equalsIgnoreCase("greet")
            );
            
            //Homework: Command Varidator Process String
            
            if(event.getMessage().getContentRaw().equalsIgnoreCase("greet")){
                //event.getChannel().sendMessage("Hello!").queue();
                //Message.sendMesage(event.getChannel(), "Hello!");
            }
        }
    }
}
