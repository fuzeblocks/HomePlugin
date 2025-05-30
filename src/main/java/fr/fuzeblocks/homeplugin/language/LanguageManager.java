package fr.fuzeblocks.homeplugin.language;

import fr.fuzeblocks.homeplugin.HomePlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class LanguageManager {

    private final Language language;
    private final YamlConfiguration yamlConfiguration;

    public LanguageManager(Language type) {
        this.language = type;
        HomePlugin instance = HomePlugin.getPlugin(HomePlugin.class);
        String fileName = type.toString().toLowerCase() + ".yml";
        File file = new File(instance.getDataFolder(), fileName);
        instance.saveResource(fileName, false);
        yamlConfiguration = YamlConfiguration.loadConfiguration(file);
    }
    public String getString(String key) {
        return yamlConfiguration.getString(key);
    }
    public String getStringWithColor(String key) {
        return HomePlugin.translateAlternateColorCodes(getString(key));
    }
    public int getInt(String key) {
        return yamlConfiguration.getInt(key);
    }

    public Language getLanguage() {
        return language;
    }
}
