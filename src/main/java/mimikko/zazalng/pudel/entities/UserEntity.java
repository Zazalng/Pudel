package mimikko.zazalng.pudel.entities;

import mimikko.zazalng.pudel.manager.UserManager;
import net.dv8tion.jda.api.entities.User;

public class UserEntity {
    protected final UserManager userManager;
    private final User JDAuser;

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

    public User getJDA() {
        return JDAuser;
    }

    public String getNickname(GuildEntity guild) {
        if(guild.getJDA().getMember(JDAuser).getNickname()!=null){
            return guild.getJDA().getMember(JDAuser).getNickname();
        } else{
            return JDAuser.getName();
        }
    }

    public long getRoyalty() {
        return royalty;
    }

    public void setRoyalty(long royalty) {
        this.royalty = royalty;
    }

    public long getDiamond() {
        return diamond;
    }

    public void setDiamond(long diamond) {
        this.diamond = diamond;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
