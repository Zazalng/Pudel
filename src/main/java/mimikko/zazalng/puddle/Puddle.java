/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package mimikko.zazalng.puddle;

/**
 *
 * @author User
 */

import java.io.FileInputStream;
import java.io.IOException;
import mimikko.zazalng.puddle.listenerEvent.CommandParser;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.requests.GatewayIntent;
import java.util.Properties;
import java.util.Scanner;

public class Puddle{
    public static void PuddleSaid(String preText){
        System.out.println("Puddle: "+ preText);
    }

    public static void main(String[] args) throws Exception {
        Properties env = new Properties();
        Boolean apiLoader = false;
        FileInputStream envFile;
        
        while(!apiLoader){
            try{
                envFile = new FileInputStream(".env");
                env.load(envFile);
                String botToken = env.getProperty("Discord_API");
                JDA jda = JDABuilder.createLight(botToken, 
                    GatewayIntent.DIRECT_MESSAGES, 
                    GatewayIntent.GUILD_MESSAGES, 
                    GatewayIntent.MESSAGE_CONTENT, 
                    GatewayIntent.GUILD_MEMBERS)
                    .addEventListeners(new CommandParser())
                    .build();
            } catch(IOException e){
                e.printStackTrace();
                PuddleSaid("Unable to locate file .env");
                PuddleSaid("Recheck locate of file .env then press [Enter]");
                new Scanner(System.in).nextLine();
            }
        }
    }
}