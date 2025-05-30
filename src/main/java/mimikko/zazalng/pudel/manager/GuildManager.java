package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.entities.GuildEntity;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuildManager extends AbstractManager {
    private final Map<String, GuildEntity> guildEntity;

    protected GuildManager(PudelWorld pudelWorld){
        super(pudelWorld);
        this.guildEntity = new HashMap<>();
    }

    public GuildEntity getGuildEntity(Guild JDAguild){
        return this.guildEntity.computeIfAbsent(JDAguild.getId(), Entity -> new GuildEntity(this,JDAguild));
    }

    public List<GuildEntity> getGuildEntity(){
        return new ArrayList<>(this.guildEntity.values());
    }

    public GuildManager fetchGuildEntity(){
        StringBuilder helpMessage = new StringBuilder("Loaded Guild Entity: "+guildEntity.size()+"\n");
        guildEntity.forEach((id, guild) -> helpMessage.append(id).append(" - ").append(guild.getJDA().getName()).append("\n"));
        System.out.println(helpMessage);
        return this;
    }

    @Override
    public GuildManager initialize() {
        return this;
    }

    @Override
    public void reload() {

    }

    @Override
    public void shutdown() {

    }
}
