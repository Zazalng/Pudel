package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.contracts.ManagersType;
import mimikko.zazalng.pudel.entities.UserEntity;
import mimikko.zazalng.pudel.manager.session.ReactionManager;
import mimikko.zazalng.pudel.manager.session.TextManager;
import net.dv8tion.jda.api.entities.User;

public abstract class AbstractManager implements Manager{
    private final PudelWorld pudelWorld;

    protected AbstractManager(PudelWorld pudelWorld){
        this.pudelWorld = pudelWorld;
    }

    protected PudelWorld getPudelWorld() {
        return this.pudelWorld;
    }

    private boolean isPudel(User user){
        return getPudelManager().getPudelEntity().equals(user);
    }

    protected boolean isAuthorized(User user){
        return user.getId().equals(getPudelWorld().getEnvironment().getDevUserID()) || isPudel(user);
    }

    public CommandManager getCommandManager(){
        return getPudelWorld().getManagerFactory().getManager(ManagersType.COMMAND);
    }

    public EmbedManager getEmbedManager(){
        return getPudelWorld().getManagerFactory().getManager(ManagersType.EMBED);
    }

    public GuildManager getGuildManager(){
        return getPudelWorld().getManagerFactory().getManager(ManagersType.GUILD);
    }

    public LocalizationManager getLocalizationManager(){
        return getPudelWorld().getManagerFactory().getManager(ManagersType.LOCALIZATION);
    }

    public MusicManager getMusicManager(){
        return getPudelWorld().getManagerFactory().getManager(ManagersType.MUSIC);
    }

    public PudelManager getPudelManager(){
        return getPudelWorld().getManagerFactory().getManager(ManagersType.PUDEL);
    }

    public SessionManager getSessionManager(){
        return getPudelWorld().getManagerFactory().getManager(ManagersType.SESSION);
    }

    public UserManager getUserManager(){
        return getPudelWorld().getManagerFactory().getManager(ManagersType.USER);
    }
}
