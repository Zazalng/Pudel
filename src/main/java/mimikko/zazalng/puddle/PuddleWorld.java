package mimikko.zazalng.puddle;

import mimikko.zazalng.puddle.handlers.CommandLineHandler;
import mimikko.zazalng.puddle.handlers.EnvironmentHandler;
import mimikko.zazalng.puddle.manager.JDAshardManager;
import mimikko.zazalng.puddle.utility.WorldLogging;
import net.dv8tion.jda.api.sharding.ShardManager;

public class PuddleWorld {
    //Get From other Class
    protected final PuddleWorld puddleWorld;
    protected final EnvironmentHandler env;
    protected final CommandLineHandler worldCommand;
    protected final JDAshardManager JDAshardManager;
    protected final WorldLogging worldLogging;
    
    //Get In Puddle's World Class
    private boolean isWorldOnline;
    
    public PuddleWorld(){
        //Get From other Class
        this.puddleWorld = this;
        this.env = new EnvironmentHandler(this);
        this.JDAshardManager = new JDAshardManager(this);
        this.worldCommand = new CommandLineHandler(this);
        this.worldLogging = new WorldLogging(this);
        
        //Get In Puddle's World Class
        this.isWorldOnline = false;
    }

    public PuddleWorld(String envPath){
        this();
        this.puddleWorld.getEnvironment().loadEnv(envPath);
    }
    ///////////////////////////////////////////////////
    /*       Getter/Setter Method: Self-Explain      */
    ///////////////////////////////////////////////////
    public CommandLineHandler getWorldCommand(){
        return this.worldCommand;
    }
    
    public boolean getWorldStatus(){
        return this.isWorldOnline;
    }

    public void setWorldStatus(boolean status){
        this.isWorldOnline = status;
    }

    public JDAshardManager getJDAshardManager(){
        return this.JDAshardManager;
    }

    public void setJDAshardManager(ShardManager shardManager){
        this.JDAshardManager.setShardManager(shardManager);
    }

    public EnvironmentHandler getEnvironment(){
        return this.env;
    }

    public WorldLogging getWorldLogging(){
        return this.worldLogging;
    }
    ///////////////////////////////////////////////////
    /*Action Method: Method that will only work when getting 'new' and with correct constructor*/
    ///////////////////////////////////////////////////
    public void puddleReply(String reply){
        System.out.printf("%s: %s\n",getEnvironment().getBotName(),reply);
    }
}
