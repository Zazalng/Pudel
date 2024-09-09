package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.entities.GuildEntity;
import net.dv8tion.jda.api.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class GuildManager implements Manager {
    private final static Logger logger = LoggerFactory.getLogger(GuildManager.class);
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

    public void fetchGuildEntity(){
        StringBuilder helpMessage = new StringBuilder("Loaded Guild Entity: "+guildEntity.size()+"\n");
        guildEntity.forEach((id, guild) -> helpMessage.append(id).append(" - ").append(guild.getJDA().getName()).append("\n"));
        System.out.println(helpMessage);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void reload() {

    }

    @Override
    public void shutdown() {

    }
}
