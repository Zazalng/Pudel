package mimikko.zazalng.pudel.entities;

import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.List;

public class GuildEntity {
    //Import variable
    private final Guild guild;
    private MusicPlayerEntity musicPlayer;

    //Class variable
    private final List<String> ignoreChannel;
    private final List<String> disableCommand;
    private String prefix;
    private String staffLogChannel;

    public GuildEntity(Guild guild){
        this.ignoreChannel = new ArrayList<>();
        this.disableCommand = new ArrayList<>();
        this.guild = guild;
        this.musicPlayer = null;
        this.prefix = "p!";
        this.staffLogChannel = "";
    }

    public Guild getGuild() {
        return this.guild;
    }

    public MusicPlayerEntity getMusicPlayer() {
        if(isMusicPlayerExist()){
            return
        }
    }

    public void createMusicPlayer(){

    }

    public boolean isMusicPlayerExist(){
        return musicPlayer==null;
    }

    public List<String> getIgnoreChannel() {
        return ignoreChannel;
    }

    public List<String> getDisableCommand() {
        return disableCommand;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getStaffLogChannel() {
        return staffLogChannel;
    }

    public void setStaffLogChannel(String staffLogChannel) {
        this.staffLogChannel = staffLogChannel;
    }

    public String getOwnerID() {
        return getGuild().getOwnerId();
    }
}