package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.entities.SessionEntity;
import mimikko.zazalng.pudel.entities.UserEntity;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.Map;

public class UserManager extends AbstractManager {
    protected final Map<String, UserEntity> userEntity;

    protected UserManager(PudelWorld pudelWorld) {
        super(pudelWorld);
        this.userEntity = new HashMap<>();
    }

    public String getUserName(SessionEntity session){
        if(session.getGuild().getJDA().getMember(session.getUser().getJDA()).getNickname()!=null){
            return session.getGuild().getJDA().getMember(session.getUser().getJDA()).getNickname();
        } else{
            return session.getUser().getJDA().getName();
        }
    }

    public boolean isVoiceActive(SessionEntity session){
        return session.getGuild().getJDA().getMember(session.getUser().getJDA()).getVoiceState().inAudioChannel();
    }

    public UserEntity getUserEntity(User JDAuser){
        return this.userEntity.computeIfAbsent(JDAuser.getId(), Entity -> new UserEntity(this, JDAuser));
    }

    public UserEntity castUserEntity(Object user){
        return (UserEntity) user;
    }

    public void fetchUserEntity(){
        StringBuilder helpMessage = new StringBuilder("Loaded User Entity: "+userEntity.size()+"\n");
        userEntity.forEach((id, user) -> helpMessage.append(id).append(" - ").append(user.getJDA().getName()).append("\n"));
        System.out.println(helpMessage);
    }

    @Override
    public UserManager initialize() {
        return this;
    }

    @Override
    public void reload() {

    }

    @Override
    public void shutdown() {

    }
}
