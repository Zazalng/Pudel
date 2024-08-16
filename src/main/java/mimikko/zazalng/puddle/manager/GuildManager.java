package mimikko.zazalng.puddle.manager;

import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;
import java.util.Map;

public class GuildManager {
    private final Map<String, MusicManager> musicManagers;
    private final Map<String, String> guildPrefix;

    public GuildManager() {
        this.musicManagers = new HashMap<>();
        this.guildPrefix = new HashMap<>();
    }

    public MusicManager getMusicManager(Guild guild) {
        return musicManagers.computeIfAbsent(guild.getId(), id -> new MusicManager());
    }

    public void removeMusicManager(Guild guild) {
        MusicManager manager = musicManagers.remove(guild.getId());
        if (manager != null) {
            manager.stop(guild);
        }
    }

    public void saveGuildData(Guild guild) {
        // Save any necessary guild-specific data to your SQL database
    }

    public void loadGuildData(Guild guild) {
        // Load any necessary guild-specific data from your SQL database
    }
}
