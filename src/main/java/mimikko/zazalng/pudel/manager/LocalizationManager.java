package mimikko.zazalng.pudel.manager;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.entities.GuildEntity;
import mimikko.zazalng.pudel.entities.InteractionEntity;
import mimikko.zazalng.pudel.entities.UserEntity;
import mimikko.zazalng.pudel.entities.interaction.ReactionEntity;
import mimikko.zazalng.pudel.entities.interaction.TextEntity;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class LocalizationManager extends AbstractManager{
    private static final Logger logger = LoggerFactory.getLogger(LocalizationManager.class);
    private final Map<String, Properties> languageFiles;

    protected LocalizationManager(PudelWorld pudelWorld) {
        super(pudelWorld);
        this.languageFiles = new HashMap<>();
        loadLanguages();
    }

    public LocalizationManager loadLanguages() {
        loadCSVLanguageFile(getPudelWorld().getEnvironment().getWorldLocalization()); // Assuming you download the CSV manually to this location
        return this;
    }

    public String getLocalizedText(InteractionEntity session, String key, Map<String, String> args){
        return getLocalizedText(session.getGuild(),key,args);
    }

    public String getLocalizedText(GuildEntity guild, String key, Map<String, String> args) {
        // Check if the language code exists
        Properties properties = languageFiles.get(guild.getLanguageCode());
        if (properties == null) {
            logger.error("No properties found for language: {}", guild.getLanguageCode());
            return key; // Fallback to the key if the language is missing
        }

        // Check if the key exists in the language file
        String text = properties.getProperty(key);
        if (text == null) {
            logger.error("Key '{}' not found for language: {}", key, guild.getLanguageCode());
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

    public boolean loadCSVLanguageFile(String filePath) {
        try (CSVReader reader = new CSVReader(new FileReader(Paths.get(filePath).toFile()))) {
            List<String[]> rows = reader.readAll();
            String[] headers = rows.getFirst(); // First line with language codes
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
            return false;
        }
        return true;
    }

    public String getLanguageName(InteractionEntity session){
        return getLocalizedText(session,"lang.name",null);
    }

    public String getBooleanText(InteractionEntity session, boolean flag){
        if(flag){
            return getLocalizedText(session, "boolean.enable", null);
        } else{
            return getLocalizedText(session, "boolean.disable", null);
        }
    }

    @Override
    public boolean initialize(User user){
        if(!super.isAuthorized(user)){
            logger.error("400 Bad Request");
            return false;
        }

        return true;
    }

    @Override
    public boolean reload(User user){
        if(!super.isAuthorized(user)){
            logger.error("400 Bad Request");
            return false;
        }

        return true;
    }

    @Override
    public boolean shutdown(User user){
        if(!super.isAuthorized(user)){
            logger.error("400 Bad Request");
            return false;
        }

        return true;
    }
}