package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;

import java.util.HashMap;
import java.util.Map;

public class ManagerFactory {
    protected final PudelWorld pudelWorld;
    private final Map<String, Manager> managers = new HashMap<>();

    public ManagerFactory(PudelWorld pudelWorld) {
        this.pudelWorld = pudelWorld;
        managers.put("guildManager", new GuildManager(pudelWorld));
        managers.put("localizationManager", new LocalizationManager(pudelWorld));
        managers.put("musicManager", new MusicManager(pudelWorld));
        managers.put("pudelManager", new PudelManager(pudelWorld));
        managers.put("sessionManager", new SessionManager(pudelWorld));
        managers.put("userManager", new UserManager(pudelWorld));
    }

    public Manager getManager(String managerName) {
        return managers.get(managerName);
    }

    @SuppressWarnings("unchecked")
    public <T extends Manager> T getManager(String key, Class<T> managerClass) {
        Manager manager = managers.get(key);
        if (managerClass.isInstance(manager)) {
            return (T) manager;
        } else {
            throw new IllegalArgumentException("Manager with key " + key + " is not of type " + managerClass.getName());
        }
    }

    public void reloadManager(String managerName) {
        Manager manager = managers.get(managerName);
        if (manager != null) {
            manager.reload();
        }
    }

    public void shutdownAllManagers() {
        managers.values().forEach(Manager::shutdown);
    }
}
