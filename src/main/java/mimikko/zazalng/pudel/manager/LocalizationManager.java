package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class LocalizationManager implements Manager{
    protected final PudelWorld pudelWorld;
    private static final Logger logger = LoggerFactory.getLogger(LocalizationManager.class);
    private final Map<String, Properties> languageFiles;

    public LocalizationManager(PudelWorld pudelWorld) {
        this.pudelWorld = pudelWorld;
        this.languageFiles = new HashMap<>();
        loadAllLanguages();
    }

    public void loadAllLanguages() {
        // Implement loading logic for each language file into the languageFiles map
        // Example: languageFiles.put("en", loadLanguageFile("en.txt"));
    }

    public void reloadLanguage(String languageCode) {
        // Reload a specific language file
        // Example: languageFiles.put(languageCode, loadLanguageFile(languageCode + ".txt"));
    }

    public String getLocalizedText(String key, String languageCode, Map<String, String> args) {
        Properties properties = languageFiles.get(languageCode);
        String text = properties.getProperty(key);

        if (text != null && args != null) {
            for (Map.Entry<String, String> entry : args.entrySet()) {
                text = text.replace("`" + entry.getKey() + "`", entry.getValue());
            }
        }
        return text != null ? text : key;
    }

    // Helper method to load a language file
    private Properties loadLanguageFile(String fileName) {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(fileName)) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void reload() {

    }

    @Override
    public void shutdown() {

    }
}
