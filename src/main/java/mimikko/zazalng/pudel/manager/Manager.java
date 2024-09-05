package mimikko.zazalng.pudel.manager;

public interface Manager {
    void initialize();  // Setup logic
    void reload();      // Reload logic (for hotfix updates)
    void shutdown();    // Gracefully shutdown
}