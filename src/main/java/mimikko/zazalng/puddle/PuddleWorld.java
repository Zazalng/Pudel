package mimikko.zazalng.puddle;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;
import mimikko.zazalng.puddle.listenerEvent.PromptDeclare;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class PuddleWorld {
    protected static PuddleWorld puddleWorld;
    private boolean isWorldOnline;
    private Properties env;
    private final Scanner prompt;
    private JDA jda;
    
    public PuddleWorld(){
        PuddleWorld.puddleWorld = this;
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
        PuddleLog("Getting World's Environment with \""+fileName+"\"");
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
        PuddleLog("Initializ Discord API Connector");
        try{
            this.jda = JDABuilder.createDefault(botToken)
                    .enableIntents(GatewayIntent.DIRECT_MESSAGES,
                    GatewayIntent.GUILD_MESSAGES,
                    GatewayIntent.MESSAGE_CONTENT,
                    GatewayIntent.GUILD_MEMBERS)
                    .addEventListeners(new PromptDeclare())
                    .build();
            this.jda.awaitReady();
            setPuddleWorldOnline(true);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
