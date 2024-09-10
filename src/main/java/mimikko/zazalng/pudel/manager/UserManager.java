package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.entities.GuildEntity;
import mimikko.zazalng.pudel.entities.UserEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.Map;

public class UserManager implements Manager {
    protected final PudelWorld pudelWorld;
    protected final Map<String, UserEntity> userEntity;

    public UserManager(PudelWorld pudelWorld) {
        this.pudelWorld = pudelWorld;
        this.userEntity = new HashMap<>();
    }

    public Member userConvert(Guild guild, User user){
        return guild.getMember(user);
    }

    public boolean isVoiceActive(Guild guild, User user){
        return guild.getMember(user).getVoiceState().inAudioChannel();
    }

    public UserEntity getUserEntity(User JDAuser){
        return this.userEntity.computeIfAbsent(JDAuser.getId(), Entity -> new UserEntity(this, JDAuser));
    }

    @Override
    public PudelWorld getPudelWorld(){
        return this.pudelWorld;
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
