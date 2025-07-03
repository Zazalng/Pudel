package mimikko.zazalng.pudel.handlers;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.handlers.CommandLineInputHandler.CommandProcessing;
import net.dv8tion.jda.api.entities.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class CommandLineHandler implements Runnable{
    protected final PudelWorld pudelWorld;
    private static final Logger logger = LoggerFactory.getLogger(CommandLineHandler.class);

    public CommandLineHandler(PudelWorld pudelWorld){
        this.pudelWorld = pudelWorld;
    }

    @Override
    public void run(){
        new CommandProcessing(this).run();
    }

    public void unknownCommand(String command){
        logger.warn("Encounter Unexpected command: `{}`", command);
    }

    public void stopWorld(){
        if (!pudelWorld.getWorldStatus()){
            logger.warn("`{}` world is not running currently.", pudelWorld.getEnvironment().getWorldName());
            return;
        }

        logger.info("Disconnecting World called `{}`.", pudelWorld.getEnvironment().getWorldName());
        pudelWorld.shutdownWorld()
                .setJDAshardManager(null)
                .setWorldStatus(false)
                .getManagerFactory().shutdownAllManagers();
        logger.info("`{}` world has stopped.", pudelWorld.getEnvironment().getWorldName());
    }

    public void startWorld(){
        if (!pudelWorld.getEnvironment().isLoaded()) {
            logger.error("World Environment is missing.");
            return;
        }

        if (pudelWorld.getWorldStatus()){
            logger.warn("`{}` world is still running.", pudelWorld.getEnvironment().getWorldName());
            return;
        }

        logger.info("Starting World called `{}`.", pudelWorld.getEnvironment().getWorldName());
        pudelWorld.buildShard(pudelWorld.getEnvironment().getDiscordAPI())
                .setWorldStatus(true)
                .getJDAshardManager().setActivity(
                        Activity.streaming("Pudel V2.2 (Stable Build)", "https://github.com/Zazalng/Pudel")
                );
    }

    public void setStatus(String text){
        pudelWorld.getJDAshardManager().setActivity(Activity.customStatus(text));
    }

    public void unloadEnv(){
        if (pudelWorld.getWorldStatus()){
            logger.warn("I'm still seeing `{}` world is running.", pudelWorld.getEnvironment().getWorldName());
            return;
        }

        pudelWorld.getEnvironment().unloadEnv();
        logger.info("Environment is empty now.");
    }

    public void loadEnv() {
        if (pudelWorld.getEnvironment().isLoaded()) {
            logger.warn("Environment of `{}` is currently loaded.", pudelWorld.getEnvironment().getWorldName());
            return;
        }

        logger.info("Getting an Environment setting, where should I be?");
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Environment files", "env");
        fileChooser.setFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileHidingEnabled(false);

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue != JFileChooser.APPROVE_OPTION) {
            logger.error("No file selected. Exiting command.");
            return;
        }

        File selectedFile = fileChooser.getSelectedFile();
        if (selectedFile.isFile() && selectedFile.getName().equals(".env")) {
            loadEnv(selectedFile.getAbsolutePath());
        } else {
            logger.error("Please select a valid .env file. Exiting command.");
        }
    }

    public void loadEnv(String filePath) {
        pudelWorld.getEnvironment().loadEnv(filePath);
    }
}