package mimikko.zazalng.pudel.handlers.CommandLineInputHandler;

import mimikko.zazalng.pudel.handlers.CommandLineHandler;

import java.util.Scanner;

public class CommandProcessing implements Runnable {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Object onLock = new Object();

    public static Scanner getScanner(){
        return scanner;
    }

    public static Object getOnLock(){
        return onLock;
    }

    private final CommandLineHandler worldCommand;
    private boolean isOperate;

    public CommandProcessing(CommandLineHandler worldCommand) {
        this.worldCommand = worldCommand;
        this.isOperate = true;
    }

    @Override
    public void run() {
        while (isOperate) {
            synchronized (getOnLock()){
                System.out.print(">_");
                executing(scanner.nextLine());
            }
        }
    }

    private void executing(String input) {
        String[] parts = input.split(" ", 2);
        String command = parts[0].toLowerCase();
        String args = parts.length > 1 ? parts[1] : null;

        switch (command) {
            case "exit":
                worldCommand.stopWorld();
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