package mimikko.zazalng.pudel.handlers.CommandLineInputHandler;

import mimikko.zazalng.pudel.handlers.CommandLineHandler;

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
            System.out.print(">_");
            String command = scanner.nextLine();
            processCommand(command);
        }
    }

    private void processCommand(String input) {
        String[] parts = input.split(" ", 2);
        String command = parts[0].toLowerCase();
        String args = parts.length > 1 ? parts[1] : "";

        switch (command) {
            case "exit":
                isOperate = false;
                break;
            case "stop":
                worldCommand.stopWorld();
                break;
            case "start":
                worldCommand.startWorld();
                break;
            case "unloadenv":
                worldCommand.unloadEnv();
                break;
            case "loadenv":
                if(args.isEmpty()){
                    worldCommand.loadEnv();
                } else{
                    worldCommand.loadEnv(args);
                }
                break;
                //discord related command
            case "setstatus":
                worldCommand.setStatus(args);
                break;
            default:
                worldCommand.unknownCommand(input);
                break;
        }
    }
}