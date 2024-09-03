package mimikko.zazalng.pudel;

import mimikko.zazalng.pudel.handlers.CommandLineHandler;
import mimikko.zazalng.pudel.handlers.EnvironmentHandler;
import mimikko.zazalng.pudel.manager.GuildManager;
import mimikko.zazalng.pudel.manager.LocalizationManager;
import net.dv8tion.jda.api.sharding.ShardManager;

import static mimikko.zazalng.pudel.utility.JDAshardBuilder.buildJDAshardManager;

public class PudelWorld {
    private final PudelWorld pudelWorld;
    //Get From other Class
    protected final CommandLineHandler worldCommand;
    protected final EnvironmentHandler Environment;
    protected final GuildManager guildManager;
    protected final LocalizationManager localizationManager;
    //Get API Class
    protected ShardManager JDAshardManager;
    //Get In Puddle's World Class
    private boolean worldStatus;
    
    public PudelWorld(){
        this.pudelWorld = this;
        //Get From other Class
        this.worldCommand = new CommandLineHandler(this);
        this.Environment = new EnvironmentHandler(this);
        this.guildManager = new GuildManager(this);
        this.localizationManager = new LocalizationManager(this);
        //Get In Puddle's World Class
        this.worldStatus = false;
    }

    public PudelWorld(String envPath){
        this();
        this.pudelWorld.getEnvironment().loadEnv(envPath);
    }
    ///////////////////////////////////////////////////
    /*       Getter/Setter Method: Self-Explain      */
    ///////////////////////////////////////////////////
    public CommandLineHandler getWorldCommand(){
        return this.worldCommand;
    }

    public EnvironmentHandler getEnvironment(){
        return this.Environment;
    }

    public GuildManager getGuildManager(){
        return this.guildManager;
    }

    public LocalizationManager getLocalizationManager(){
        return this.localizationManager;
    }
    
    public boolean getWorldStatus(){
        return this.worldStatus;
    }

    public void setWorldStatus(boolean status){
        this.worldStatus = status;
    }

    public ShardManager getJDAshardManager(){
        return this.JDAshardManager;
    }

    public void setJDAshardManager(ShardManager shardManager){
        this.JDAshardManager = shardManager;
    }
    ///////////////////////////////////////////////////
    /*Action Method: Method that will only work when getting 'new' and with correct constructor*/
    ///////////////////////////////////////////////////
    public void JDAShutdown(){
        this.getJDAshardManager().shutdown();
        this.setJDAshardManager(null);
    }

    public void buildShard(String api){
        setJDAshardManager(buildJDAshardManager(api));
    }
}
