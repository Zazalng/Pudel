package mimikko.zazalng.pudel.manager;

import net.dv8tion.jda.api.entities.User;

public interface Manager {
    boolean initialize(User user);  // Setup logic
    boolean reload(User user);      // Reload logic (for hotfix updates)
    boolean shutdown(User user);    // Gracefully shutdown
}