package mimikko.zazalng.puddle.handlers.CommandLineInputHandler;

import mimikko.zazalng.puddle.handlers.CommandLineHandler;

import java.util.Scanner;

public class CommandProcessing implements Runnable {
    private final Scanner scanner;
    private final CommandLineHandler worldCommand;
    private boolean isOperate;

    public CommandProcessing(CommandLineHandler worldCommand) {
        this.worldCommand = worldCommand;
        this.scanner = new Scanner(System.in);
        this.isOperate = true;
    }

    @Override
    public void run() {
        while (isOperate) {
            System.out.print(">");
            String command = scanner.nextLine();
            processCommand(command);
        }
    }

    private void processCommand(String command) {
        switch (command.toLowerCase()) {
            case "stop":
                worldCommand.stopWorld();
                isOperate = false;
                break;
            case "start":
                worldCommand.startWorld();
                break;
            // Add more commands as needed
            default:
                PuddleLog("Unknown command: " + command);
                break;
        }
    }

    public void PuddleLog(String preText){
        System.out.printf("Puddle: %s\n", preText);
    }
}