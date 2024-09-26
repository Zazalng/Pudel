package mimikko.zazalng.pudel;

import mimikko.zazalng.pudel.handlers.CommandLineHandler;
import mimikko.zazalng.pudel.handlers.EnvironmentHandler;
import mimikko.zazalng.pudel.manager.*;
import net.dv8tion.jda.api.sharding.ShardManager;

import static mimikko.zazalng.pudel.utility.JDAshardBuilder.buildJDAshardManager;

public class PudelWorld {
    private final PudelWorld pudelWorld;
    //Get From other Class
    protected final CommandLineHandler worldCommand;
    protected final EnvironmentHandler Environment;
    protected final ManagerFactory managerFactory;
    //Get API Class
    protected ShardManager JDAshardManager;
    //Get In Puddle's World Class
    private boolean worldStatus;
    
    public PudelWorld(){
        this.pudelWorld = this;
        //Get From other Class
        this.worldCommand = new CommandLineHandler(this);
        this.Environment = new EnvironmentHandler(this);
        this.managerFactory = new ManagerFactory(this);
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

    public ManagerFactory getManagerFactory() {
        return managerFactory;
    }

    public CommandManager getCommandManager(){
        return this.managerFactory.getManager("commandManager", CommandManager.class);
    }

    public EmbedManager getEmbedManager(){
        return this.managerFactory.getManager("embedManager", EmbedManager.class);
    }

    public GuildManager getGuildManager(){
        return this.managerFactory.getManager("guildManager", GuildManager.class);
    }

    public LocalizationManager getLocalizationManager(){
        return this.managerFactory.getManager("localizationManager", LocalizationManager.class);
    }

    public MusicManager getMusicManager(){
        return this.managerFactory.getManager("musicManager", MusicManager.class);
    }

    public PudelManager getPudelManager(){
        return this.managerFactory.getManager("pudelManager", PudelManager.class);
    }

    public SessionManager getSessionManager(){
        return this.managerFactory.getManager("sessionManager", SessionManager.class);
    }

    public UserManager getUserManager(){
        return this.managerFactory.getManager("userManager", UserManager.class);
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
    public void buildShard(String api){
        setJDAshardManager(buildJDAshardManager(this,api));
    }

    public void reloadManager(String managerName) {
        managerFactory.reloadManager(managerName);
    }

    public void shutdownWorld() {
        managerFactory.shutdownAllManagers();
        // Additional shutdown logic for the world
    }
}