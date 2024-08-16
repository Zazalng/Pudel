package mimikko.zazalng.puddle.handlers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class EnvironmentHandler {
    protected final String classCodename;
    private String responded;
    private final Properties env;
    private String discordAPI;
    private String devServerID;
    private String worldName;
    private String botName;
    private boolean isLoaded;

    public EnvironmentHandler(){
        this.env = new Properties();
        this.isLoaded = false;
        this.classCodename = "EnvironmentHandler";
        this.responded = "";
    }

    public String respondBuilder(String responded,char type){
        this.responded += " " + responded;
        switch(type){
            case 'r':
                logResponded();
                break;
            default:

        }
        return responded;
    }

    public void logResponded(){
        System.out.println(this.responded);
        this.responded = "";
    }

    public void loadEnv(String filepath){
        unloadEnv();
        respondBuilder("Getting filepath: \""+filepath+"\"",'n');
        try(FileInputStream fileInputStream = new FileInputStream(filepath)){
            this.env.load(fileInputStream);
            assignEnvironment();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void assignEnvironment(){
        //discord.api.key
        setDiscordAPI(this.env.getProperty("discord.api.key"));
        //discord.dev.guildID
        setDevServerID(this.env.getProperty("discord.dev.guildID"));
        //world.name
        setWorldName(this.env.getProperty("world.name"));
        //bot.name
        setBotName(this.env.getProperty("bot.name"));
        setLoaded(true);
    }

    public void unloadEnv(){
        this.env.clear();
        setDiscordAPI(null);
        setDevServerID(null);
        setWorldName(null);
        setBotName(null);
        setLoaded(false);
    }

    public String getDiscordAPI() {
        return discordAPI;
    }

    public void setDiscordAPI(String discordAPI) {
        this.discordAPI = discordAPI;
    }

    public String getDevServerID() {
        return devServerID;
    }

    public void setDevServerID(String devServerID) {
        this.devServerID = devServerID;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public String getBotName() {
        return botName;
    }

    public void setBotName(String botName) {
        this.botName = botName;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setLoaded(boolean flag) {
        isLoaded = flag;
    }
}
