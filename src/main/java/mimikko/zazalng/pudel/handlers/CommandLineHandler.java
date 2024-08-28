package mimikko.zazalng.pudel.handlers;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.handlers.CommandLineInputHandler.CommandProcessing;
import net.dv8tion.jda.api.entities.Activity;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

import static mimikko.zazalng.pudel.utility.JDAshardBuilder.buildJDAshardManager;


public class CommandLineHandler implements Runnable{
    protected final PudelWorld pudelWorld;

    public CommandLineHandler(PudelWorld pudelWorld) {
        this.pudelWorld = pudelWorld;
    }

    @Override
    public void run() {
        new CommandProcessing(this).run();
    }

    public void unknownCommand(String command){
        pudelWorld.puddleReply("What is '"+command+"' that should do, Master?");
    }

    public void stopWorld() {
        pudelWorld.puddleReply("Disconnecting World called '"+ pudelWorld.getEnvironment().getWorldName()+"'");
        pudelWorld.getJDAshardManager().shutdown();
        pudelWorld.setWorldStatus(false);
        pudelWorld.puddleReply("'"+ pudelWorld.getEnvironment().getWorldName()+"' world has stopped.");
    }

    public void startWorld(){
        if(pudelWorld.getEnvironment().isLoaded()){
            if(!pudelWorld.getWorldStatus()){
                pudelWorld.puddleReply("Starting World called \""+ pudelWorld.getEnvironment().getWorldName()+"\"");
                pudelWorld.setJDAshardManager(buildJDAshardManager(pudelWorld.getEnvironment().getDiscordAPI()));
                pudelWorld.getJDAshardManager().setActivity(Activity.listening("My Master"));
                pudelWorld.setWorldStatus(true);
            } else{
                pudelWorld.puddleReply("'"+ pudelWorld.getEnvironment().getWorldName()+"' world is still running");
            }
        } else{
            pudelWorld.puddleReply("World Environment is missing, Master");
        }
    }

    public void setStatus(String text){
        pudelWorld.getJDAshardManager().setActivity(Activity.customStatus(text));
    }

    public void unloadEnv() {
        if(pudelWorld.getWorldStatus()) {
            pudelWorld.puddleReply("I'm still seeing '" + pudelWorld.getEnvironment().getWorldName() + "' world is running.");
        } else{
            pudelWorld.getEnvironment().unloadEnv();
            pudelWorld.puddleReply("Environment is empty now.");
        }
    }

    public void loadEnv() {
        pudelWorld.puddleReply("Getting an Environment setting, where should I be?");
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
                pudelWorld.getEnvironment().loadEnv(selectedFile.getAbsolutePath());
            } else {
                System.out.println("Please select a valid .env file. Exiting command.");
            }
        } else {
            System.out.println("No file selected. Exiting command.");
        }
    }

    public void loadEnv(String filePath) {
        pudelWorld.puddleReply("Getting an Environment setting, where should I be?");
        pudelWorld.getEnvironment().loadEnv(filePath);
    }
}