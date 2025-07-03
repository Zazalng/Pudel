package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.contracts.ManagersType;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;

public class ManagerFactory {
    protected final PudelWorld pudelWorld;
    private static final Logger logger = LoggerFactory.getLogger(ManagerFactory.class);
    private final EnumMap<ManagersType, Manager> managers = new EnumMap<>(ManagersType.class);

    public ManagerFactory(PudelWorld pudelWorld) {
        this.pudelWorld = pudelWorld;
    }

    @SuppressWarnings("unchecked")
    public <T extends Manager> T getManager(ManagersType managersType) {
        Manager manager = this.managers.get(managersType);
        if (manager != null && managersType.getManagerClass().isInstance(manager)) {
            return (T) manager;
        } else {
            throw new IllegalArgumentException("Manager with name " + managersType + " is not of type " + managersType.getManagerClass().getName());
        }
    }

    private User getPudelEntity(){
        return ((PudelManager) getManager(ManagersType.PUDEL)).getPudelEntity();
    }

    private ManagerFactory loadManager(ManagersType managersType, Manager manager) {
        this.managers.put(managersType, manager);
        return this;
    }

    public boolean startManager(ManagersType managersType){

    }

    public boolean reloadManager(ManagersType managersType) {
        Manager manager = this.managers.get(managersType);
        if (manager != null) {
            manager.reload(getPudelEntity());
        }
        return true;
    }

    public boolean shutdownAllManagers() {
        this.managers.values().forEach(Manager -> Manager.shutdown(getPudelEntity()));
        this.managers.clear();
        return true;
    }

    public ManagerFactory startAllManagers() {
        loadManager(ManagersType.COMMAND, new CommandManager(pudelWorld))
                .loadManager(ManagersType.DATABASE, new DatabaseManager(pudelWorld))
                .loadManager(ManagersType.EMBED, new EmbedManager(pudelWorld))
                .loadManager(ManagersType.GUILD, new GuildManager(pudelWorld))
                .loadManager(ManagersType.LOCALIZATION, new LocalizationManager(pudelWorld))
                .loadManager(ManagersType.MUSIC, new MusicManager(pudelWorld))
                .loadManager(ManagersType.PUDEL, new PudelManager(pudelWorld))
                .loadManager(ManagersType.SESSION, new SessionManager(pudelWorld))
                .loadManager(ManagersType.USER, new UserManager(pudelWorld));
        return this;
    }
}
