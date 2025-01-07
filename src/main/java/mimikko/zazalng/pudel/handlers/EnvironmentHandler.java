package mimikko.zazalng.pudel.handlers;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.contracts.ManagersEnum;
import mimikko.zazalng.pudel.manager.LocalizationManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class EnvironmentHandler {
    protected final PudelWorld pudelWorld;
    private final Properties env;
    private boolean isLoaded;

    public EnvironmentHandler(PudelWorld pudelWorld){
        this.pudelWorld = pudelWorld;
        this.env = new Properties();
        this.isLoaded = false;
    }

    public EnvironmentHandler loadEnv(String filepath){
        if(!isLoaded()){
            System.out.println("Getting filepath: \"" + filepath + "\"");
            try (FileInputStream fileInputStream = new FileInputStream(filepath)) {
                this.env.load(fileInputStream);
                setLoaded(true);
            } catch (IOException e) {

            }
        }
        return this;
    }

    public EnvironmentHandler unloadEnv(){
        this.env.clear();
        setLoaded(false);
        return this;
    }

    public String getDiscordAPI() {
        return this.env.getProperty("discord.api.key");
    }

    public String getDevServerID() {
        return this.env.getProperty("discord.dev.guildID");
    }

    public String getDevUserID(){
        return this.env.getProperty("discord.dev.userID");
    }

    public String getInviteURL(){
        return this.env.getProperty("discord.bot.invite");
    }

    public String getWorldName() {
        return this.env.getProperty("world.name");
    }

    public String getWorldLocalization(){
        return this.env.getProperty("pudel.language");
    }

    public String getWorldSecret(){
        return this.env.getProperty("pudel.secret.reply");
    }

    public boolean isLoaded() {
        return this.isLoaded;
    }

    public EnvironmentHandler setLoaded(boolean flag) {
        this.isLoaded = flag;
        return this;
    }
}
