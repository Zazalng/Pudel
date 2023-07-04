/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mimikko.zazalng.puddle;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;
import mimikko.zazalng.puddle.listenerEvent.PromptDeclare;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

/**
 *
 * @author kengh
 */
public class PuddleWorld {
    private boolean isWorldOnline;
    private Properties env;
    private final Scanner prompt;
    private static JDA jda;
    
    PuddleWorld(){
        this.isWorldOnline = false;
        this.env = new Properties();
        this.prompt = new Scanner(System.in);
    }
    public void PuddleLog(String preText){
        System.out.println("Puddle: "+ preText);
    }
    
    public void setPuddleWorldOnline(boolean status){
        this.isWorldOnline = status;
    }
    
    public boolean getPuddleWorldOnline(){
        return this.isWorldOnline;
    }
    
    public void setPuddleWorldEnvironment(String fileName){
        while(!getPuddleWorldOnline()){
            try(FileInputStream fileInputStream = new FileInputStream(fileName)){
                this.env.load(fileInputStream);
                fileInputStream.close();
                return;
            }catch(IOException e){
                e.printStackTrace();
                PuddleLog("Unable to locate file .env");
                PuddleLog("Recheck locate of file .env then press [Enter]");
                this.prompt.nextLine();
            }
        }
    }
    
    public Properties getPuddleWorldEnvironment(){
        return this.env;
    }
    
    public void startPuddleWorld(){
        String botToken = this.env.getProperty("discord.api.key");
        PuddleLog(botToken);
        PuddleWorld.jda = JDABuilder.createLight(botToken,
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.MESSAGE_CONTENT,
                GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(new PromptDeclare())
                .build();
        setPuddleWorldOnline(true);
    }
}
