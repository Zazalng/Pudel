package mimikko.zazalng.pudel.entities;

import mimikko.zazalng.pudel.manager.GuildManager;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.List;

public class GuildEntity{
    //Import variable
    protected final GuildManager guildManager;
    private final transient Guild guild;

    //Class variable
    private final List<String> ignoreChannel;
    private final List<String> disableCommand;
    private String languageCode;
    private String prefix;
    private String staffLogChannel;
    private int replyLevel;

    public GuildEntity(GuildManager guildManager, Guild guild){
        this.guildManager = guildManager;
        this.guild = guild;
        this.ignoreChannel = new ArrayList<>();
        this.disableCommand = new ArrayList<>();
        this.languageCode = "ENG";
        this.prefix = "p!";
        this.staffLogChannel = "";
        this.replyLevel = 0;
    }

    public GuildManager getGuildManager(){
        return this.guildManager;
    }

    protected Guild getJDA() {
        return this.guild;
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

    public GuildEntity setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String getStaffLogChannel() {
        return staffLogChannel;
    }

    public GuildEntity setStaffLogChannel(String staffLogChannel) {
        this.staffLogChannel = staffLogChannel;
        return this;
    }

    public String getOwnerID() {
        return getJDA().getOwnerId();
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public GuildEntity setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
        return this;
    }
}