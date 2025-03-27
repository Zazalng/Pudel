package mimikko.zazalng.pudel.contracts;

import mimikko.zazalng.pudel.manager.*;

public enum ManagersEnum {
    COMMAND(CommandManager.class),
    DATABASE(DatabaseManager.class),
    EMBED(EmbedManager.class),
    GUILD(GuildManager.class),
    INTERACTION(InteractionManager.class),
    LOCALIZATION(LocalizationManager.class),
    MUSIC(MusicManager.class),
    PUDEL(PudelManager.class),
    SESSION(SessionManager.class),
    USER(UserManager.class);

    private final Class<? extends Manager> managerClass;

    ManagersEnum(Class<? extends Manager> managerClass) {
        this.managerClass = managerClass;
    }

    public Class<? extends Manager> getManagerClass() {
        return this.managerClass;
    }
}
