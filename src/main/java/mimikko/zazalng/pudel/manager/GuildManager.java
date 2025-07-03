package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.entities.GuildEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class GuildManager extends AbstractManager {
    private final Logger logger = LoggerFactory.getLogger(GuildManager.class);
    protected final Map<Guild, GuildEntity> guildEntity = new HashMap<>();

    protected GuildManager(PudelWorld pudelWorld){
        super(pudelWorld);
    }

    public GuildEntity getEntity(Guild JDAguild){
        return this.guildEntity.computeIfAbsent(JDAguild, Entity -> new GuildEntity(this,JDAguild));
    }

    public GuildEntity getEntity(String id){
        return getEntity(getPudelManager().getPudelEntity().getJDA().getGuildById(id));
    }

    public GuildManager fetchGuildEntity(){
        StringBuilder helpMessage = new StringBuilder("Loaded Guild Entity: "+guildEntity.size()+"\n");
        guildEntity.forEach((jda, ignore) -> helpMessage.append(jda.getId()).append(" - ").append(jda.getName()).append("\n"));
        System.out.println(helpMessage);
        return this;
    }

    @Override
    public boolean initialize(User user){
        if(!super.isAuthorized(user)){
            logger.error("400 Bad Request");
            return false;
        }

        return true;
    }

    @Override
    public boolean reload(User user){
        if(!super.isAuthorized(user)){
            logger.error("400 Bad Request");
            return false;
        }

        return true;
    }

    @Override
    public boolean shutdown(User user){
        if(!super.isAuthorized(user)){
            logger.error("400 Bad Request");
            return false;
        }

        return true;
    }
}
