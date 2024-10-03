package mimikko.zazalng.pudel.manager;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.entities.GuildEntity;
import mimikko.zazalng.pudel.entities.SessionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class LocalizationManager implements Manager{
    protected final PudelWorld pudelWorld;
    private static final Logger logger = LoggerFactory.getLogger(LocalizationManager.class);
    private final Map<String, Properties> languageFiles;

    public LocalizationManager(PudelWorld pudelWorld) {
        this.pudelWorld = pudelWorld;
        this.languageFiles = new HashMap<>();
        initialize();
    }

    public void loadLanguages() {
        loadCSVLanguageFile(this.pudelWorld.getEnvironment().getWorldLocalization()); // Assuming you download the CSV manually to this location
    }

    public String getLocalizedText(String key, String languageCode, Map<String, String> args) {
        // Check if the language code exists
        Properties properties = languageFiles.get(languageCode);
        if (properties == null) {
            logger.error("No properties found for language: {}", languageCode);
            return key; // Fallback to the key if the language is missing
        }

        // Check if the key exists in the language file
        String text = properties.getProperty(key);
        if (text == null) {
            logger.error("Key '{}' not found for language: {}", key, languageCode);
            return key; // Fallback to the key if the translation is missing
        } else if(text.isEmpty()){
            properties = languageFiles.get("ENG");
            text = "@Override Language\n"+properties.getProperty(key);
        }

        // Replace placeholders if arguments are provided
        if (args != null) {
            for (Map.Entry<String, String> entry : args.entrySet()) {
                text = text.replace("{" + entry.getKey() + "}", entry.getValue()); // Replace {key} with value
            }
        }

        return text;
    }

    public void loadCSVLanguageFile(String filePath) {
        try (CSVReader reader = new CSVReader(new FileReader(Paths.get(filePath).toFile()))) {
            List<String[]> rows = reader.readAll();
            String[] headers = rows.get(0); // First line with language codes
            Map<String, Map<String, String>> tempLanguageMap = new HashMap<>();

            // Initialize a map for each language column
            for (int i = 1; i < headers.length; i++) {
                tempLanguageMap.put(headers[i].trim(), new HashMap<>());
            }

            // Process the rest of the CSV rows (key-value pairs for each language)
            for (int rowIndex = 1; rowIndex < rows.size(); rowIndex++) {
                String[] row = rows.get(rowIndex);
                String key = row[0].trim();

                for (int i = 1; i < row.length; i++) {
                    String lang = headers[i].trim();
                    String value = row[i].trim();
                    tempLanguageMap.get(lang).put(key, value);
                }
            }

            // Load each map into Properties for consistency with the original structure
            for (String lang : tempLanguageMap.keySet()) {
                Properties properties = new Properties();
                properties.putAll(tempLanguageMap.get(lang));
                languageFiles.put(lang, properties);
                logger.info("Loaded {} properties for language: {}", properties.size(), lang);
            }
        } catch (IOException | CsvException e) {
            logger.error("Failed to load CSV file", e);
        }
    }

    public String getLanguageName(SessionEntity session){
        return getLocalizedText("lang.name",session.getGuild().getLanguageCode(),null);
    }

    public String getBooleanText(GuildEntity guild, boolean flag){
        if(flag){
            return getLocalizedText("boolean.enable",guild.getLanguageCode(),null);
        } else{
            return getLocalizedText("boolean.disable",guild.getLanguageCode(),null);
        }
    }

    @Override
    public PudelWorld getPudelWorld() {
        return this.pudelWorld;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void reload() {
        // Reload all language files
    }

    @Override
    public void shutdown() {
        // Optional: shutdown logic
    }
}