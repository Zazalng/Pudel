package mimikko.zazalng.pudel;

public class Pudel {
    public static void main(String[] args){
        String envFilePath;
        PudelWorld world;
        world = new PudelWorld();
        if (args.length > 0) {
            // Use the first command-line argument as the path to the .env file
            envFilePath = args[0];
            world = new PudelWorld(envFilePath);
        }
        world.getWorldCommand().run();
    }
}