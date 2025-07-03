package mimikko.zazalng.pudel.entities;

import mimikko.zazalng.pudel.manager.UserManager;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.Nullable;

public class UserEntity{
    protected final UserManager userManager;
    private final transient User JDAuser;

    private long royalty;
    private long diamond;
    private int level;

    public UserEntity(UserManager userManager, User JDAuser){
        this.userManager = userManager;
        this.JDAuser = JDAuser;
    }

    public UserManager getUserManager(){
        return this.userManager;
    }

    protected User getJDA() {
        return this.JDAuser;
    }

    public String getName(@Nullable GuildEntity guild) {
        if (guild == null) return getName();
        return getName(guild.getJDA().getMember(getJDA()));
    }

    private String getName(Member user){
        return user.getNickname() != null ? user.getNickname() : getName();
    }

    private String getName(){
        return getJDA().getName();
    }

    public long getRoyalty() {
        return royalty;
    }

    public UserEntity setRoyalty(long royalty) {
        this.royalty = royalty;
        return this;
    }

    public long getDiamond() {
        return diamond;
    }

    public UserEntity setDiamond(long diamond) {
        this.diamond = diamond;
        return this;
    }

    public int getLevel() {
        return level;
    }

    public UserEntity setLevel(int level) {
        this.level = level;
        return this;
    }
}