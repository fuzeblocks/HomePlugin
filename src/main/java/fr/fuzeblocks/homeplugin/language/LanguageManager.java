package fr.fuzeblocks.homeplugin.language;

import fr.fuzeblocks.homeplugin.HomePlugin;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * The type Language manager.
 */
public class LanguageManager {

    private final Language language;
    private final File file;
    private final HomePlugin plugin;
    private YamlConfiguration yamlConfiguration;

    /**
     * Instantiates a new Language manager.
     *
     * @param type   the type
     * @param plugin the plugin
     */
    public LanguageManager(Language type, HomePlugin plugin) {
        this.language = type;
        this.plugin = plugin;

        String fileName = type.toString().toLowerCase() + ".yml";
        this.file = new File(plugin.getDataFolder(), fileName);

        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }

        this.yamlConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Gets string.
     *
     * @param key the key
     * @return the string
     */
    @NotNull
    public String getString(String key) {
        String value = yamlConfiguration.getString(key);
        return value != null ? value : "";
    }

    /**
     * Gets string.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the string
     */
    @NotNull
    public String getString(String key, String defaultValue) {
        return yamlConfiguration.getString(key, defaultValue);
    }

    /**
     * Gets string with color.
     *
     * @param key the key
     * @return the string with color
     */
    @NotNull
    public String getStringWithColor(String key) {
        return HomePlugin.translateAlternateColorCodes(yamlConfiguration.getString(key));
    }

    /**
     * Gets string with color.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the string with color
     */
    @NotNull
    public String getStringWithColor(String key, String defaultValue) {
        return HomePlugin.translateAlternateColorCodes(yamlConfiguration.getString(key, defaultValue));
    }


    /**
     * Regenerates the language file from the internal resource and reloads it.
     * Warning: this will overwrite the current file.
     *
     * @return true if the operation succeeded, false otherwise.
     */
    public boolean regenerate() {
        try {
            if (file.exists()) {
                if (!file.delete()) {
                    return false;
                }
            }
            plugin.saveResource(file.getName(), true);
            this.yamlConfiguration = YamlConfiguration.loadConfiguration(file);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gets language.
     *
     * @return the language
     */
    public Language getLanguage() {
        return language;
    }
}
