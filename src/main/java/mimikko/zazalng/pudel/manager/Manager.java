package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;

public interface Manager {
    PudelWorld getPudelWorld();
    void initialize();  // Setup logic
    void reload();      // Reload logic (for hotfix updates)
    void shutdown();    // Gracefully shutdown
}