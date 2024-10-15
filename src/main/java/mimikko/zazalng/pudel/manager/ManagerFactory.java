package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;

import java.util.HashMap;
import java.util.Map;

public class ManagerFactory {
    protected final PudelWorld pudelWorld;
    private final Map<String, Manager> managers = new HashMap<>();

    public ManagerFactory(PudelWorld pudelWorld) {
        this.pudelWorld = pudelWorld;
        loadManager("commandManager", new CommandManager(pudelWorld))
            .loadManager("embedManager", new EmbedManager(pudelWorld))
            .loadManager("guildManager", new GuildManager(pudelWorld))
            .loadManager("localizationManager", new LocalizationManager(pudelWorld))
            .loadManager("musicManager", new MusicManager(pudelWorld))
            .loadManager("pudelManager", new PudelManager(pudelWorld))
            .loadManager("sessionManager", new SessionManager(pudelWorld))
            .loadManager("userManager", new UserManager(pudelWorld))
        ;
    }

    @SuppressWarnings("unchecked")
    public <T extends Manager> T getManager(String key, Class<T> managerClass) {
        Manager manager = this.managers.get(key);
        if (managerClass.isInstance(manager)) {
            return (T) manager;
        } else {
            throw new IllegalArgumentException("Manager with key " + key + " is not of type " + managerClass.getName());
        }
    }

    public ManagerFactory loadManager(String name, Manager manager){
        this.managers.put(name, manager);
        return this;
    }

    public ManagerFactory reloadManager(String managerName) {
        Manager manager = this.managers.get(managerName);
        if (manager != null) {
            manager.reload();
        }
        return this;
    }

    public ManagerFactory shutdownAllManagers() {
        this.managers.values().forEach(Manager::shutdown);
        this.managers.clear();
        return this;
    }
}
