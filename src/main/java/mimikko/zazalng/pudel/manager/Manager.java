package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;

public interface Manager {
    PudelWorld getPudelWorld();
    <T extends Manager> T initialize();  // Setup logic
    <T extends Manager> T reload();      // Reload logic (for hotfix updates)
    <T extends Manager> T shutdown();    // Gracefully shutdown
}