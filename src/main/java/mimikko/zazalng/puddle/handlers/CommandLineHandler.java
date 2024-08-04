package mimikko.zazalng.puddle.handlers;

import mimikko.zazalng.puddle.PuddleWorld;
import mimikko.zazalng.puddle.handlers.CommandLineInputHandler.CommandProcessing;
import mimikko.zazalng.puddle.manager.JDAshardManager;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CommandLineHandler implements Runnable{
    protected final PuddleWorld puddleWorld;
    protected JDAshardManager puddleShard;

    public CommandLineHandler(PuddleWorld puddleWorld) {
        this.puddleWorld = puddleWorld;
        this.puddleShard = puddleWorld.getJDAshardManager();
    }

    @Override
    public void run() {
        new CommandProcessing(this).run();
    }

    public boolean getWolrdStatus(){
        return puddleWorld.getWorldStatus();
    }

    public void stopWorld() {
        if (puddleWorld.getJDAshardManager().getShardManager() != null) {
            puddleWorld.getJDAshardManager().getShardManager().shutdown();
            puddleWorld.getJDAshardManager().setShardManagerNull();
            puddleWorld.setWorldStatus(false);
            puddleWorld.PuddleLog("Puddle World stopped.");
        }
    }

    public void startWorld(){
        puddleWorld.PuddleLog("Starting Puddle's World called \"Eden\"");
        try {
            puddleWorld.setJDAshardManager(puddleShard.buildJDAshardManager());
            puddleWorld.getJDAshardManager().getShardManager().setActivity(Activity.listening("My Master"));
            puddleWorld.setWorldStatus(true);
        } catch (LoginException ex) {
            Logger.getLogger(PuddleWorld.class.getName()).log(Level.ALL, null, ex);
        }
    }
}