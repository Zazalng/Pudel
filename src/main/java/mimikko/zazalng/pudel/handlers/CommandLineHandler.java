package mimikko.zazalng.pudel.handlers;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.handlers.CommandLineInputHandler.CommandProcessing;
import net.dv8tion.jda.api.entities.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class CommandLineHandler implements Runnable{
    protected final PudelWorld pudelWorld;
    private static final Logger logger = LoggerFactory.getLogger(CommandLineHandler.class);

    public CommandLineHandler(PudelWorld pudelWorld) {
        this.pudelWorld = pudelWorld;
    }

    @Override
    public void run() {
        new CommandProcessing(this).run();
    }

    public void unknownCommand(String command){
        logger.warn("Encounter Unexpected command: `{}`", command);
    }

    public void stopWorld() {
        if(pudelWorld.getWorldStatus()) {
            logger.info("Disconnecting World called `{}`.", pudelWorld.getEnvironment().getWorldName());
            CompletableFuture<Void> shutdownFuture = CompletableFuture.runAsync(() -> pudelWorld.getJDAshardManager().shutdown());
            shutdownFuture.join();
            pudelWorld.setJDAshardManager(null);
            pudelWorld.setWorldStatus(false);
            logger.info("`{}` world has stopped.", pudelWorld.getEnvironment().getWorldName());
        } else{
            logger.warn("`{}` world is not running currently.",pudelWorld.getEnvironment().getWorldName());
        }
    }

    public void startWorld(){
        if(pudelWorld.getEnvironment().isLoaded()){
            if(!pudelWorld.getWorldStatus()){
                logger.info("Starting World called `{}`.", pudelWorld.getEnvironment().getWorldName());
                pudelWorld.buildShard(pudelWorld.getEnvironment().getDiscordAPI());
                pudelWorld.getJDAshardManager().setActivity(Activity.streaming("Pudel V2 (Dev. Build)","https://github.com/Zazalng/Pudel"));
                pudelWorld.setWorldStatus(true);
            } else{
                logger.warn("`{}` world is still running.",pudelWorld.getEnvironment().getWorldName());
            }
        } else{
            logger.error("World Environment is missing.");
        }
    }

    public void setStatus(String text){
        pudelWorld.getJDAshardManager().setActivity(Activity.customStatus(text));
    }

    public void unloadEnv() {
        if(pudelWorld.getWorldStatus()) {
            logger.warn("I'm still seeing `{}` world is running.",pudelWorld.getEnvironment().getWorldName());
        } else{
            pudelWorld.getEnvironment().unloadEnv();
            logger.info("Environment is empty now.");
        }
    }

    public void loadEnv() {
        if(!pudelWorld.getEnvironment().isLoaded()) {
            logger.info("Getting an Environment setting, where should I be?");
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Environment files", "env");
            fileChooser.setFileFilter(filter);
            fileChooser.setAcceptAllFileFilterUsed(false);  // Only show the filtered files
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);  // Ensure only files are selectable
            fileChooser.setFileHidingEnabled(false); // Show hidden files

            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if (selectedFile.isFile() && selectedFile.getName().equals(".env")) {
                    loadEnv(selectedFile.getAbsolutePath());
                } else {
                    logger.error("Please select a valid .env file. Exiting command.");
                }
            } else {
                logger.error("No file selected. Exiting command.");
            }
        } else{
            logger.warn("Environment of `{}` is currently loaded",pudelWorld.getEnvironment().getWorldName());
        }
    }

    public void loadEnv(String filePath) {
        pudelWorld.getEnvironment().loadEnv(filePath);
    }
}