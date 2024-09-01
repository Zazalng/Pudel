package mimikko.zazalng.pudel.manager;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class LocalizationManager {
    private Map<String, Map<String, String>> localizations;
    private String currentLanguage;

    public LocalizationManager() {
        this.localizations = new HashMap<>();
        this.currentLanguage = "en"; // Default language
    }

    // Load localization from a file and store it in the map
    public void loadLocalization(String languageCode, String filePath) throws IOException {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(filePath)) {
            properties.load(inputStream);
        }

        Map<String, String> localizationMap = new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            localizationMap.put(key, properties.getProperty(key));
        }

        localizations.put(languageCode, localizationMap);
    }

    // Set the current language
    public void setLanguage(String languageCode) {
        if (localizations.containsKey(languageCode)) {
            this.currentLanguage = languageCode;
        }
    }

    // Get a localized string and replace placeholders with actual variables
    public String getLocalizedString(String key, Map<String, String> variables) {
        String template = localizations.get(currentLanguage).get(key);
        if (template == null) {
            return "Missing localization for key: " + key;
        }
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            template = template.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return template;
    }

    // Method to reload the localization file (hot-swapping)
    public void reloadLocalization(String languageCode, String filePath) throws IOException {
        loadLocalization(languageCode, filePath);
    }
}
