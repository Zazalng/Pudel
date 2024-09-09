package mimikko.zazalng.pudel.entities;

import net.dv8tion.jda.api.entities.User;

public class UserEntity {
    private final UserEntity userEntity;
    private final User JDAuser;

    private long royalty;
    private long diamond;
    private int level;

    public UserEntity(User JDAuser){
        this.userEntity = this;
        this.JDAuser = JDAuser;
    }

    public User getJDA() {
        return JDAuser;
    }

    public UserEntity getUserEntity() {
        return userEntity;
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
