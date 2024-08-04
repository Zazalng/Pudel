package mimikko.zazalng.puddle;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import mimikko.zazalng.puddle.handlers.CommandLineHandler;
import mimikko.zazalng.puddle.manager.JDAshardManager;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;

public class PuddleWorld {
    //Get From other Class
    protected static PuddleWorld puddleWorld;

    private final Properties env;

    protected CommandLineHandler worldCommand;
    protected JDAshardManager JDAshardManager;
    
    //Get In Puddle's World Class
    private boolean isWorldOnline;
    
    public PuddleWorld(){
        //Get From other Class
        PuddleWorld.puddleWorld = this;
        this.env = new Properties();
        this.JDAshardManager = new JDAshardManager(puddleWorld);
        
        //Get In Puddle's World Class
        this.isWorldOnline = false;
        this.worldCommand = new CommandLineHandler(this);
    }
    ///////////////////////////////////////////////////
    /*Static Area: Whenever this class was called this method should be work without 'new' keyword need*/
    ///////////////////////////////////////////////////
    public static PuddleWorld getInstance(){
        return puddleWorld;
    }

    public void initalRun() throws LoginException {
        if(this.getJDAshardManager().getShardManager()==null){
            setJDAshardManager(this.getJDAshardManager().buildJDAshardManager());
            this.worldCommand.run();
        }
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

    public Properties getEnvironment(){
        return this.env;
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
    ///////////////////////////////////////////////////
    /*Action Method: Method that will only work when getting 'new' and with correct constructor*/
    ///////////////////////////////////////////////////
    public void PuddleLog(String preText){
        System.out.printf("Puddle: %s\n", preText);
    }
}
