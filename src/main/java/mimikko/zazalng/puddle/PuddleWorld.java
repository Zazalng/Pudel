package mimikko.zazalng.puddle;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;
import mimikko.zazalng.puddle.handlers.EventHandler;
import mimikko.zazalng.puddle.handlers.database.DatabaseHandler;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.SessionControllerAdapter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class PuddleWorld {
    //Get From other Class
    protected static PuddleWorld puddleWorld;
    private final DatabaseHandler dbHandler;
    private final Properties env;
    private final CommandPrompt prompt;
    private ShardManager shardManager;
    
    //Get In Puddle's World Class
    private boolean isWorldOnline;
    
    public PuddleWorld(){
        //Get From other Class
        PuddleWorld.puddleWorld = this;
        this.dbHandler = new DatabaseHandler(this);
        this.env = new Properties();
        this.prompt = new CommandPrompt(this);
        this.shardManager = null;
        
        //Get In Puddle's World Class
        this.isWorldOnline = false;
    }
    
    private ShardManager buildShardManager() throws LoginException{
        DefaultShardManagerBuilder builder = 
                DefaultShardManagerBuilder.create(
                    EnumSet.of(
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_MODERATION,
                        GatewayIntent.GUILD_VOICE_STATES,
                        GatewayIntent.GUILD_PRESENCES,
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_MESSAGE_REACTIONS,
                        GatewayIntent.DIRECT_MESSAGES,
                        GatewayIntent.DIRECT_MESSAGE_REACTIONS
                    )
                )
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
        
        builder.addEventListeners(new EventHandler(this) {});
        
        return builder.build();
    }
    ///////////////////////////////////////////////////
    /*Static Area: Whenever this class was called this method should be work without 'new' keyword need*/
    ///////////////////////////////////////////////////
    public static PuddleWorld getInstance(){
        return puddleWorld;
    }
    ///////////////////////////////////////////////////
    /*       Getter/Setter Method: Self-Explain      */
    ///////////////////////////////////////////////////
    public void setWorldStatus(boolean status){
        this.isWorldOnline = status;
    }
    
    public boolean getWorldStatus(){
        return this.isWorldOnline;
    }
    
    public DatabaseHandler getDatabase(){
        return this.dbHandler;
    }
    
    public void setEnvironment(String fileName){
        PuddleLog("Getting World's Environment with \""+fileName+"\"");
        while(!getWorldStatus()){
            try(FileInputStream fileInputStream = new FileInputStream(fileName)){
                this.env.load(fileInputStream);
                fileInputStream.close();
                return;
            }catch(IOException e){
                e.printStackTrace();
                PuddleLog("Unable to locate file .env");
                PuddleLog("Recheck locate of file .env then press [Enter]");
            }
        }
    }
    
    public Properties getPuddleWorldEnvironment(){
        return this.env;
    }
    ///////////////////////////////////////////////////
    /*Action Method: Method that will only work when getting 'new' and with correct constructor*/
    ///////////////////////////////////////////////////
    public void PuddleLog(String preText){
        System.out.printf("Puddle: %s", preText);
    }
    
    public void startWorld(){
        PuddleLog("Starting Puddle's World called \"Eden\"");
        try {
            this.shardManager = this.buildShardManager();
            this.shardManager.setActivity(Activity.listening("My Master"));
        } catch (LoginException ex) {
            Logger.getLogger(PuddleWorld.class.getName()).log(Level.ALL, null, ex);
        }
        this.setWorldStatus(true);
    }
    
    public void stopPuddleWorld() {
        if (this.shardManager != null) {
            shardManager.shutdown();
            shardManager = null;
            setWorldStatus(false);
            PuddleLog("Puddle World stopped.");
        }
    }
}
