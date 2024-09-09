package mimikko.zazalng.pudel.entities;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import mimikko.zazalng.pudel.manager.GuildManager;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.List;

public class GuildEntity {
    //Import variable
    protected final GuildManager guildManager;
    private final Guild guild;
    private MusicPlayerEntity musicPlayer;

    //Class variable
    private final List<String> ignoreChannel;
    private final List<String> disableCommand;
    private String guildLang;
    private String prefix;
    private String staffLogChannel;

    public GuildEntity(GuildManager guildManager, Guild guild){
        this.guildManager = guildManager;
        this.guild = guild;
        this.musicPlayer = null;

        this.ignoreChannel = new ArrayList<>();
        this.disableCommand = new ArrayList<>();
        this.guildLang = "en";
        this.prefix = "p!";
        this.staffLogChannel = "";
    }

    public Guild getJDA() {
        return this.guild;
    }

    public MusicPlayerEntity getMusicPlayer() {
        return this.musicPlayer;
    }

    public void createMusicPlayer(AudioPlayer playerManager){
        this.musicPlayer = new MusicPlayerEntity(playerManager);
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
        return getJDA().getOwnerId();
    }

    public String getGuildLang() {
        return guildLang;
    }

    public void setGuildLang(String guildLang) {
        this.guildLang = guildLang;
    }
}