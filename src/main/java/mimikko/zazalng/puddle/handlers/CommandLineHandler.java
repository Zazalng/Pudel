package mimikko.zazalng.puddle.handlers;

import mimikko.zazalng.puddle.PuddleWorld;
import mimikko.zazalng.puddle.handlers.CommandLineInputHandler.CommandProcessing;
import mimikko.zazalng.puddle.manager.JDAshardManager;

import net.dv8tion.jda.api.entities.Activity;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;


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

    public void unknownCommand(String command){
        puddleWorld.puddleReply("What is '"+command+"' that should do, Master?");
    }

    public void stopWorld() {
        puddleWorld.puddleReply("Disconnecting World called '"+puddleWorld.getEnvironment().getWorldName()+"'");
        if (puddleWorld.getJDAshardManager().getShardManager() != null) {
            puddleWorld.getJDAshardManager().getShardManager().shutdown();
            puddleWorld.getJDAshardManager().setShardManagerNull();
            puddleWorld.setWorldStatus(false);
            puddleWorld.puddleReply("'"+puddleWorld.getEnvironment().getWorldName()+"' world has stopped.");
        }
    }

    public void startWorld(){
        if(puddleWorld.getEnvironment().isLoaded()){
            if(!puddleWorld.getWorldStatus()){
                puddleWorld.puddleReply("Starting World called \""+puddleWorld.getEnvironment().getWorldName()+"\"");
                puddleWorld.setJDAshardManager(puddleShard.buildJDAshardManager(puddleWorld.getEnvironment().getDiscordAPI()));
                puddleWorld.getJDAshardManager().getShardManager().setActivity(Activity.listening("My Master"));
                puddleWorld.setWorldStatus(true);
            } else{
                puddleWorld.puddleReply("'"+puddleWorld.getEnvironment().getWorldName()+"' world is still running");
            }
        } else{
            puddleWorld.puddleReply("World Environment is missing, Master");
        }
    }

    public void setStatus(String text){
        puddleWorld.getJDAshardManager().getShardManager().setActivity(Activity.customStatus(text));
    }

    public void unloadEnv() {
        if(puddleWorld.getWorldStatus()) {
            puddleWorld.puddleReply("I'm still seeing '" + puddleWorld.getEnvironment().getWorldName() + "' world is running.");
        } else{
            puddleWorld.getEnvironment().unloadEnv();
            puddleWorld.puddleReply("Environment is empty now.");
        }
    }

    public void loadEnv() {
        puddleWorld.puddleReply("Getting an Environment setting, where should I be?");
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
                puddleWorld.getEnvironment().loadEnv(selectedFile.getAbsolutePath());
            } else {
                System.out.println("Please select a valid .env file. Exiting command.");
            }
        } else {
            System.out.println("No file selected. Exiting command.");
        }
    }

    public void loadEnv(String filePath) {
        puddleWorld.puddleReply("Getting an Environment setting, where should I be?");
        puddleWorld.getEnvironment().loadEnv(filePath);
    }
}