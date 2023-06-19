/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mimikko.zazalng.puddle;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

/**
 *
 * @author kengh
 */
public class Message {
    public static void sendMesage(MessageChannel channel, String text){
        channel.sendMessage(text).queue();
    }
}
