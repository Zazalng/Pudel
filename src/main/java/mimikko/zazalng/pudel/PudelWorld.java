package mimikko.zazalng.pudel;

import mimikko.zazalng.pudel.contracts.ManagersType;
import mimikko.zazalng.pudel.entities.UserEntity;
import mimikko.zazalng.pudel.handlers.CommandLineHandler;
import mimikko.zazalng.pudel.handlers.EnvironmentHandler;
import mimikko.zazalng.pudel.manager.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.sharding.ShardManager;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import static mimikko.zazalng.pudel.utility.JDAshardBuilder.buildJDAshardManager;

public class PudelWorld {
    private final PudelWorld pudelWorld;
    //Get From other Class
    protected final CommandLineHandler worldCommand;
    protected final EnvironmentHandler Environment;
    protected final ManagerFactory managerFactory;
    //Get API Class
    protected ShardManager JDAshardManager;
    //Get In Pudel's World Class
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
        return this.managerFactory;
    }
    
    public boolean getWorldStatus(){
        return this.worldStatus;
    }

    public PudelWorld setWorldStatus(boolean status){
        this.worldStatus = status;
        return this;
    }

    public ShardManager getJDAshardManager(){
        return this.JDAshardManager;
    }

    public PudelWorld setJDAshardManager(ShardManager shardManager){
        this.JDAshardManager = shardManager;
        return this;
    }
    ///////////////////////////////////////////////////
    /*Action Method: Method that will only work when getting 'new' and with correct constructor*/
    ///////////////////////////////////////////////////
    public PudelWorld buildShard(String api){
        getManagerFactory().startAllManagers();
        setJDAshardManager(buildJDAshardManager(getManagerFactory().getManager(ManagersType.PUDEL),api));
        return this;
    }

    public PudelWorld reloadManager(ManagersType managerName) {
        getManagerFactory().reloadManager(managerName);
        return this;
    }

    public PudelWorld shutdownWorld() {
        CompletableFuture<Void> shutdownFuture = CompletableFuture.runAsync(() -> getManagerFactory().shutdownAllManagers());
        shutdownFuture.join();

        // Shut down JDA shard manager gracefully
        shutdownFuture = CompletableFuture.runAsync(() -> {
            try {
                // Wait for all shutdown processes to complete
                for (JDA jda : getJDAshardManager().getShards()) {
                    jda.shutdown();
                    if (!jda.awaitShutdown(Duration.ofSeconds(10))) {
                        jda.shutdownNow(); // Cancel all remaining requests
                        jda.awaitShutdown(); // Wait until shutdown is complete (indefinitely)
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();  // Restore interrupt status
            }
        });
        shutdownFuture.join();

        return this;
    }
}