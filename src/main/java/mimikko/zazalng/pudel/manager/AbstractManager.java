package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.contracts.ManagersEnum;
import mimikko.zazalng.pudel.entities.UserEntity;

public abstract class AbstractManager implements Manager{
    private final PudelWorld pudelWorld;

    protected AbstractManager(PudelWorld pudelWorld){
        this.pudelWorld = pudelWorld;
    }

    PudelWorld getPudelWorld() {
        return this.pudelWorld;
    }

    boolean isAuthorized(UserEntity user){
        return user.getJDA().getId().equals(getPudelWorld().getEnvironment().getDevUserID());
    }

    public CommandManager getCommandManager(){
        return getPudelWorld().getManagerFactory().getManager(ManagersEnum.COMMAND);
    }

    public EmbedManager getEmbedManager(){
        return getPudelWorld().getManagerFactory().getManager(ManagersEnum.EMBED);
    }

    public GuildManager getGuildManager(){
        return getPudelWorld().getManagerFactory().getManager(ManagersEnum.GUILD);
    }

    public InteractionManager getInteractionManager(){
        return getPudelWorld().getManagerFactory().getManager(ManagersEnum.INTERACTION);
    }

    public LocalizationManager getLocalizationManager(){
        return getPudelWorld().getManagerFactory().getManager(ManagersEnum.LOCALIZATION);
    }

    public MusicManager getMusicManager(){
        return getPudelWorld().getManagerFactory().getManager(ManagersEnum.MUSIC);
    }

    public PudelManager getPudelManager(){
        return getPudelWorld().getManagerFactory().getManager(ManagersEnum.PUDEL);
    }

    public SessionManager getSessionManager(){
        return getPudelWorld().getManagerFactory().getManager(ManagersEnum.SESSION);
    }

    public UserManager getUserManager(){
        return getPudelWorld().getManagerFactory().getManager(ManagersEnum.USER);
    }
}
