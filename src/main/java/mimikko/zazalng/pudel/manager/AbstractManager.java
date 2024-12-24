package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;

public abstract class AbstractManager implements Manager{
    private final PudelWorld pudelWorld;

    protected AbstractManager(PudelWorld pudelWorld){
        this.pudelWorld = pudelWorld;
    }

    PudelWorld getPudelWorld() {
        return this.pudelWorld;
    }

    public CommandManager getCommandManager(){
        return getPudelWorld().getManagerFactory().getManager("commandManager", CommandManager.class);
    }

    public EmbedManager getEmbedManager(){
        return getPudelWorld().getManagerFactory().getManager("embedManager", EmbedManager.class);
    }

    public GuildManager getGuildManager(){
        return getPudelWorld().getManagerFactory().getManager("guildManager", GuildManager.class);
    }

    public InteractionManager getInteractionManager(){
        return getPudelWorld().getManagerFactory().getManager("interactionManager", InteractionManager.class);
    }

    public LocalizationManager getLocalizationManager(){
        return getPudelWorld().getManagerFactory().getManager("localizationManager", LocalizationManager.class);
    }

    public MusicManager getMusicManager(){
        return getPudelWorld().getManagerFactory().getManager("musicManager", MusicManager.class);
    }

    public PudelManager getPudelManager(){
        return getPudelWorld().getManagerFactory().getManager("pudelManager", PudelManager.class);
    }

    public SessionManager getSessionManager(){
        return getPudelWorld().getManagerFactory().getManager("sessionManager", SessionManager.class);
    }

    public UserManager getUserManager(){
        return getPudelWorld().getManagerFactory().getManager("userManager", UserManager.class);
    }
}
