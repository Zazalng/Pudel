package mimikko.zazalng.puddle;

public class Puddle {
    public static void main(String[] args){
        String envFilePath;
        PuddleWorld world;
        world = new PuddleWorld();
        if (args.length > 0) {
            // Use the first command-line argument as the path to the .env file
            envFilePath = args[0];
            world = new PuddleWorld(envFilePath);
        }
        world.getWorldCommand().run();
    }
}