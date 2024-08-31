package mimikko.zazalng.pudel.handlers;

import mimikko.zazalng.pudel.PudelWorld;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class EnvironmentHandler {
    protected final PudelWorld pudelWorld;
    private final Properties env;
    private String discordAPI;
    private String devServerID;
    private String worldName;
    private String botName;
    private boolean isLoaded;

    public EnvironmentHandler(PudelWorld pudelWorld){
        this.pudelWorld = pudelWorld;
        this.env = new Properties();
        this.isLoaded = false;
    }

    private void Logging(int type, String methodName, String message){
        switch(type){
            case 1:
                this.pudelWorld.getWorldLogging().debug(this.getClass().getName(),methodName,message);
                break;
            default:
                this.pudelWorld.getWorldLogging().info(this.getClass().getName(),methodName,message);
        }
    }

    private void Logging(String methodName, String message, Throwable e){
        this.pudelWorld.getWorldLogging().error(this.getClass().getName(),methodName,message,e);
    }

    public void loadEnv(String filepath){
        String methodName = "loadEnv";

        if(isLoaded()){
            Logging(1,methodName,"Being Reject via Environment flag isLoaded set as "+ isLoaded());
            return;
        }

        System.out.println("Getting filepath: \""+filepath+"\"");
        try(FileInputStream fileInputStream = new FileInputStream(filepath)){
            this.env.load(fileInputStream);
            assignEnvironment();
        }catch(IOException e){
            Logging(methodName,"catch IOException",e);
        }
    }

    public void assignEnvironment(){
        String methodName = "assignEnvironment";
        //discord.api.key
        setDiscordAPI(this.env.getProperty("discord.api.key"));
        //discord.dev.guildID
        setDevServerID(this.env.getProperty("discord.dev.guildID"));
        //world.name
        setWorldName(this.env.getProperty("world.name"));
        //bot.name
        setBotName(this.env.getProperty("bot.name"));
        setLoaded(true);
        Logging(0,methodName,"Successfully Assign Environment variable");
    }

    public void unloadEnv(){
        String methodName = "unloadEnv";
        this.env.clear();
        setDiscordAPI(null);
        setDevServerID(null);
        setWorldName(null);
        setBotName(null);
        setLoaded(false);
        Logging(0,methodName,"Successfully unload Environment and reassign variable as null");
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
