package mimikko.zazalng.puddle.entities;

import mimikko.zazalng.puddle.manager.MusicManager;
import net.dv8tion.jda.api.entities.Guild;

public class GuildEntity {
    //Import variable
    private final GuildEntity GuildEntity;
    private final Guild guild;
    private final MusicManager musicManager;

    //Class variable
    private String prefix;

    public GuildEntity(Guild guild){
        this.GuildEntity = this;
        this.guild = guild;
        this.musicManager = new MusicManager();
        this.prefix = "."; // Implement more after MySQL
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

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        //MySQL handler for self variable
    }
}
