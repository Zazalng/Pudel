package mimikko.zazalng.puddle;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class Puddle {
    public static void main(String[] args) throws Exception {
        String envFilePath = "";
        PuddleWorld world;

        if (args.length > 0) {
            // Use the first command-line argument as the path to the .env file
            envFilePath = args[0];
        } else {
            // Show file chooser dialog if no arguments are provided
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
                    envFilePath = selectedFile.getAbsolutePath();
                } else {
                    System.out.println("Please select a valid .env file. Skipping.");
                }
            } else {
                System.out.println("No file selected. Skipping.");
            }
        }

        if(envFilePath.isEmpty()){
            world = new PuddleWorld();
        } else{
            world = new PuddleWorld(envFilePath);
        }
    }
}