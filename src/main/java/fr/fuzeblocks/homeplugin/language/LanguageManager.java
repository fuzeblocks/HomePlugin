package fr.fuzeblocks.homeplugin.language;

import fr.fuzeblocks.homeplugin.HomePlugin;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class LanguageManager {

    private final Language language;
    private final YamlConfiguration yamlConfiguration;

    public LanguageManager(Language type,HomePlugin plugin) {
        this.language = type;
        String fileName = type.toString().toLowerCase() + ".yml";
        File file = new File(plugin.getDataFolder(), fileName);
        plugin.saveResource(fileName, false);
        yamlConfiguration = YamlConfiguration.loadConfiguration(file);
    }
    @Nullable
    public String getString(String key) {
        return yamlConfiguration.getString(key);
    }
    @Nullable
    public String getString(String key, String defaultValue) {
        return yamlConfiguration.getString(key, defaultValue);
    }
    @Nullable
    public String getStringWithColor(String key) {
        return HomePlugin.translateAlternateColorCodes(yamlConfiguration.getString(key));
    }

    public Language getLanguage() {
        return language;
    }

}
