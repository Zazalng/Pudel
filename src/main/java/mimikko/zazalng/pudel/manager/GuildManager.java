package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.entities.GuildEntity;
import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;
import java.util.Map;

public class GuildManager {
    protected final PudelWorld pudelWorld;
    protected final Map<String, GuildEntity> guildEntity;

    public GuildManager(PudelWorld pudelWorld){
        this.pudelWorld = pudelWorld;
        this.guildEntity = new HashMap<>();
    }

    public PudelWorld getPudelWorld(){
        return this.pudelWorld;
    }

    public GuildEntity getGuildEntity(Guild JDAguild){
        return this.guildEntity.computeIfAbsent(JDAguild.getId(), Entity -> new GuildEntity(JDAguild));
    }
}
