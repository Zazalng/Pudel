package mimikko.zazalng.puddle.handlers;

import mimikko.zazalng.puddle.PuddleWorld;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class EnvironmentHandler {
    protected final PuddleWorld puddleWorld;
    protected final String classCodename;
    private String responded;
    private Properties env;
    private String discordAPI;
    private String devServerID;
    private String worldName;
    private String botName;

    public EnvironmentHandler(PuddleWorld puddleWorld){
        this.puddleWorld = puddleWorld;
        this.env = new Properties();
        this.classCodename = "EnvorimentHandler";
        this.responded = "";
    }

    public PuddleWorld getPuddleWorld(){
        return this.puddleWorld;
    }

    public String respondBuilder(String responded,char type){
        this.responded += " " + responded;
        switch(type){
            case 'r':
                logResponed();
                break;
            default:

        }
        return responded;
    }

    public void logResponed(){
        getPuddleWorld().getWorldLogging().logoutput(this.responded);
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
    }

    public void unloadEnv(){
        this.env.clear();
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
}
