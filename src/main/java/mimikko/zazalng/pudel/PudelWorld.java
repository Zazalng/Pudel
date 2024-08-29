package mimikko.zazalng.pudel;

import mimikko.zazalng.pudel.handlers.CommandLineHandler;
import mimikko.zazalng.pudel.handlers.EnvironmentHandler;
import mimikko.zazalng.pudel.logging.WorldLogging;
import net.dv8tion.jda.api.sharding.ShardManager;

public class PudelWorld {
    //Get From other Class
    protected final PudelWorld pudelWorld;
    protected final EnvironmentHandler env;
    protected final CommandLineHandler worldCommand;
    protected final WorldLogging worldLogging;

    //Get API Class
    protected ShardManager JDAshardManager;
    
    //Get In Puddle's World Class
    private boolean worldStatus;
    
    public PudelWorld(){
        //Get From other Class
        this.pudelWorld = this;
        this.env = new EnvironmentHandler(this);
        this.worldLogging = new WorldLogging(this);

        this.worldCommand = new CommandLineHandler(this);
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
        return this.env;
    }

    public WorldLogging getWorldLogging(){
        return this.worldLogging;
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
    public void puddleReply(String reply){
        System.out.printf("%s: %s\n",getEnvironment().getBotName(),reply);
    }
}
