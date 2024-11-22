package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;

public interface Manager {
    PudelWorld getPudelWorld();
    <T extends Manager> T initialize();  // Setup logic
    void reload();      // Reload logic (for hotfix updates)
    void shutdown();    // Gracefully shutdown
}