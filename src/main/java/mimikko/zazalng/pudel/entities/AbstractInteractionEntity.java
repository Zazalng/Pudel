package mimikko.zazalng.pudel.entities;

import mimikko.zazalng.pudel.contracts.InteractionType;
import mimikko.zazalng.pudel.manager.SessionManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

public class AbstractInteractionEntity implements InteractionEntity{
    protected final SessionManager sessionManager;
    private final UserEntity user;
    private final GuildEntity guild;

    public AbstractInteractionEntity(SessionManager manager, UserEntity user, GuildEntity guild){
        this.sessionManager = manager;
        this.user = user;
        this.guild = guild;
    }

    public AbstractInteractionEntity(SessionManager manager, UserEntity user){
        this(manager, user, null);
    }
    /**
     * @return
     */
    @Override
    public InteractionEntity asInteractionEntity() {
        return this;
    }

    /**
     * @return
     */
    @Override
    public InteractionType getType() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public SessionManager getManager() {
        return this.sessionManager;
    }

    /**
     * @return
     */
    @Override
    public GuildEntity getGuild() {
        return this.guild;
    }

    /**
     * @return
     */
    @Override
    public UserEntity getUser() {
        return this.user;
    }

    protected User getUserJDA(UserEntity user){
        return user.getJDA();
    }

    protected Guild getGuildJDA(GuildEntity guild){
        return guild.getJDA();
    }

    /**
     * @param obj
     */
    @Override
    public void execute(Object obj) {
    }

    /**
     *
     */
    @Override
    public void terminate() {
    }
}
