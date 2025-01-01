package fr.fuzeblocks.homeplugin.language;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class LanguageManager {

    private final Language language;
    public YamlConfiguration yamlConfiguration;

    public LanguageManager(Language type) {
        this.language = type;
        yamlConfiguration = YamlConfiguration.loadConfiguration(new File(type.toString().toLowerCase() + ".yml"));
    }
    public String getString(String key) {
        return yamlConfiguration.getString("Language." +  key);
    }

    public Language getLanguage() {
        return language;
    }
}
