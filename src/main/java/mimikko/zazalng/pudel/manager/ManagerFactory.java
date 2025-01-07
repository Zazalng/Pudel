package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.contracts.ManagersEnum;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class ManagerFactory {
    protected final PudelWorld pudelWorld;
    private final EnumMap<ManagersEnum, Manager> managers = new EnumMap<>(ManagersEnum.class);

    public ManagerFactory(PudelWorld pudelWorld) {
        this.pudelWorld = pudelWorld;
    }

    @SuppressWarnings("unchecked")
    public <T extends Manager> T getManager(ManagersEnum managersEnum) {
        Manager manager = this.managers.get(managersEnum);
        if (manager != null && managersEnum.getManagerClass().isInstance(manager)) {
            return (T) manager;
        } else {
            throw new IllegalArgumentException("Manager with name " + managersEnum + " is not of type " + managersEnum.getManagerClass().getName());
        }
    }

    private ManagerFactory loadManager(ManagersEnum managersEnum, Manager manager) {
        this.managers.put(managersEnum, manager);
        return this;
    }

    public ManagerFactory reloadManager(ManagersEnum managersEnum) {
        Manager manager = this.managers.get(managersEnum);
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

    public ManagerFactory startAllManagers() {
        loadManager(ManagersEnum.COMMAND, new CommandManager(pudelWorld))
                .loadManager(ManagersEnum.EMBED, new EmbedManager(pudelWorld))
                .loadManager(ManagersEnum.GUILD, new GuildManager(pudelWorld))
                .loadManager(ManagersEnum.INTERACTION, new InteractionManager(pudelWorld))
                .loadManager(ManagersEnum.LOCALIZATION, new LocalizationManager(pudelWorld))
                .loadManager(ManagersEnum.MUSIC, new MusicManager(pudelWorld))
                .loadManager(ManagersEnum.PUDEL, new PudelManager(pudelWorld))
                .loadManager(ManagersEnum.SESSION, new SessionManager(pudelWorld))
                .loadManager(ManagersEnum.USER, new UserManager(pudelWorld));
        return this;
    }
}
