package mimikko.zazalng.pudel.entities;

import mimikko.zazalng.pudel.manager.MusicManager;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.List;

public class GuildEntity {
    //Import variable
    private final GuildEntity GuildEntity;
    private final Guild guild;
    private final MusicManager musicManager;

    //Class variable
    private final List<String> ignoreChannel;
    private final List<String> disableCommand;

    private String ownerID;
    private String prefix;
    private String staffLogChannel;

    public GuildEntity(Guild guild){
        this.ignoreChannel = new ArrayList<>();
        this.disableCommand = new ArrayList<>();
        this.GuildEntity = this;
        this.guild = guild;
        this.musicManager = new MusicManager(this);
        this.ownerID = getOwnerID();
        this.prefix = "p!"; // Implement more after MySQL
        this.staffLogChannel = "";
    }

    public GuildEntity getGuildEntity() {
        return this.GuildEntity;
    }

    public Guild getGuild() {
        return this.guild;
    }

    public MusicManager getMusicManager() {
        return this.musicManager;
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

    public void setOwnerID() {
        this.ownerID = getGuild().getOwnerId();
    }
}
