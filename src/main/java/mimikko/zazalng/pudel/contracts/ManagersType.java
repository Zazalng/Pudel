package mimikko.zazalng.pudel.contracts;

import mimikko.zazalng.pudel.manager.*;
import mimikko.zazalng.pudel.manager.session.ReactionManager;
import mimikko.zazalng.pudel.manager.session.TextManager;

public enum ManagersType {
    COMMAND(CommandManager.class),
    DATABASE(DatabaseManager.class),
    EMBED(EmbedManager.class),
    GUILD(GuildManager.class),
    LOCALIZATION(LocalizationManager.class),
    MUSIC(MusicManager.class),
    PUDEL(PudelManager.class),
    SESSION(SessionManager.class),
    USER(UserManager.class);

    private final Class<? extends Manager> managerClass;

    ManagersType(Class<? extends Manager> managerClass) {
        this.managerClass = managerClass;
    }

    public Class<? extends Manager> getManagerClass() {
        return this.managerClass;
    }
}
