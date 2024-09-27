package mimikko.zazalng.pudel.entities;

import mimikko.zazalng.pudel.manager.GuildManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

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
    private String languageCode;
    private String prefix;
    private String staffLogChannel;

    public GuildEntity(GuildManager guildManager, Guild guild){
        this.guildManager = guildManager;
        this.guild = guild;
        this.musicPlayer = null;

        this.ignoreChannel = new ArrayList<>();
        this.disableCommand = new ArrayList<>();
        this.languageCode = "ENG";
        this.prefix = "p!";
        this.staffLogChannel = "";
    }

    public Guild getJDA() {
        return this.guild;
    }

    public Member getAsMember(User user){
        return this.guild.getMemberById(user.getId());
    }

    public MusicPlayerEntity getMusicPlayer() {
        if (this.musicPlayer != null) {
            return this.musicPlayer;
        } else {
            this.musicPlayer = new MusicPlayerEntity(guildManager.getPudelWorld().getMusicManager().musicManagerBuilder());
            return this.musicPlayer;
        }
    }

    public void setMusicPlayer(MusicPlayerEntity player){
        this.musicPlayer = player;
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

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }
}