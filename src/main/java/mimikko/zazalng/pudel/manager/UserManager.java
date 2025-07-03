package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.entities.UserEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class UserManager extends AbstractManager {
    private static final Logger logger = LoggerFactory.getLogger(UserManager.class);
    protected final Map<User, UserEntity> userEntity = new HashMap<>();

    protected UserManager(PudelWorld pudelWorld) {
        super(pudelWorld);
    }

    public boolean isVoiceActive(Guild guild, User user){
        return guild.getMember(user).getVoiceState().inAudioChannel();
    }

    public UserEntity getEntity(User JDAuser){
        return this.userEntity.computeIfAbsent(JDAuser, Entity -> new UserEntity(this, JDAuser));
    }

    public UserEntity requestEntity(String id){
        return getEntity(getPudelManager().getPudelEntity().getJDA().getUserById(id));
    }

    public UserEntity castUserEntity(Object user){
        return (UserEntity) user;
    }

    public void fetchUserEntity(){
        StringBuilder helpMessage = new StringBuilder("Loaded User Entity: "+userEntity.size()+"\n");
        userEntity.forEach((jda, ignore) -> helpMessage.append(jda.getId()).append(" - ").append(jda.getName()).append("\n"));
        System.out.println(helpMessage);
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
