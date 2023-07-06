package mimikko.zazalng.puddle;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;
import mimikko.zazalng.puddle.handlers.DatabaseHandler;
import mimikko.zazalng.puddle.listenerEvent.PromptDeclare;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.SessionControllerAdapter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class PuddleWorld {
    protected static PuddleWorld puddleWorld;
    private DatabaseHandler dbHandler;
    private boolean isWorldOnline;
    private final Properties env;
    private final Scanner prompt;
    private ShardManager shardManger = null;
    
    public PuddleWorld(){
        PuddleWorld.puddleWorld = this;
        this.isWorldOnline = false;
        this.env = new Properties();
        this.prompt = new Scanner(System.in);
    }
    
    private ShardManager buildShardManager() throws LoginException{
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.create(EnumSet.of(
            GatewayIntent.GUILD_MEMBERS,
            GatewayIntent.GUILD_MODERATION,
            GatewayIntent.GUILD_VOICE_STATES,
            GatewayIntent.GUILD_PRESENCES,
            GatewayIntent.MESSAGE_CONTENT,
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.GUILD_MESSAGE_REACTIONS,
            GatewayIntent.DIRECT_MESSAGES,
            GatewayIntent.DIRECT_MESSAGE_REACTIONS
        ))
                .setToken(getPuddleWorldEnvironment().getProperty("discord.api.key"))
                .setSessionController(new SessionControllerAdapter())
                .setActivity(Activity.watching("Generating Puddle's World Instance..."))
                .setBulkDeleteSplittingEnabled(false)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setChunkingFilter(ChunkingFilter.NONE)
                .disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS)
                .setEnableShutdownHook(true)
                .setAutoReconnect(true)
                .setContextEnabled(true)
                .setShardsTotal(1);
        
        builder.addEventListeners(new PromptDeclare(this));
        
        return builder.build();
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
        this.PuddleLog("Starting Puddle World called \"Eden\"");
        try {
            this.shardManger = this.buildShardManager();
            this.shardManger.setActivity(Activity.listening("My Master"));
        } catch (LoginException ex) {
            Logger.getLogger(PuddleWorld.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.setPuddleWorldOnline(true);
    }
}
